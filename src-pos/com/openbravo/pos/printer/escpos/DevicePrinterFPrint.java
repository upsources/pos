/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openbravo.pos.printer.escpos;

import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.printer.DevicePrinter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import org.apache.axis.configuration.FileProvider;

/**
 *
 * @author Administrator
 */
public class DevicePrinterFPrint implements DevicePrinter {

    // private boolean m_bInline;
    private String m_sName;
    private Map<String,String> ticket;
    private ArrayList<String> lines;
    private ArrayList items;
    private ArrayList<String> formattedTicket;
    private boolean toFile;
    private String fileName = "bon.inp";

    public DevicePrinterFPrint() {
        m_sName = AppLocal.getIntString("Printer.Serial");
        ticket = new HashMap<String,String>();
        lines = new ArrayList<String>();
        items = new ArrayList();
        formattedTicket = new ArrayList<String>();
        toFile = true;
    }

    public String getPrinterName() {
        return m_sName;
    }

    public String getPrinterDescription() {
        return null;
    }

    public JComponent getPrinterComponent() {
        return null;
    }

    public void reset() {
    }

    public void beginReceipt() {
    }

    public void printImage(BufferedImage image) {
    }

    public void printBarCode(String type, String position, String code) {
    }

    public void beginLine(int iTextSize) {
    }

    public void printText(int iStyle, String sText) {
        sText = sText.trim();
        lines.add(sText);
    }

    public void endLine() {
    }

    public void endReceipt() {
        //System.out.println(lines.toString());
        int index = 0;
        while( index < lines.size() ) {
            if(lines.get(index).equals("Receipt:"))
                ticket.put(lines.get(index), lines.get(index+1));
            if(lines.get(index).equals("Date:"))
                ticket.put(lines.get(index), lines.get(index+1));
            if(lines.get(index).equals("Table:"))
                ticket.put(lines.get(index), lines.get(index+1));
            if(lines.get(index).equals("Items count:"))
                ticket.put(lines.get(index), lines.get(index+1));
            if(lines.get(index).equals("Subtotal."))
                ticket.put(lines.get(index), lines.get(index+1).replace("¤ ", ""));
            if(lines.get(index).equals("Total."))
                ticket.put(lines.get(index), lines.get(index+1).replace("¤ ", ""));
            if(lines.get(index).equals("Cash"))
                ticket.put("PaymentType", "0");
            if(lines.get(index).equals("Mag card"))
                ticket.put("PaymentType", "2");
            if(lines.get(index).equals("Cheque"))
                ticket.put("PaymentType", "3");
            if(lines.get(index).equals("Tendered:"))
                ticket.put(lines.get(index), lines.get(index+1).replace("¤ ", ""));
            if(lines.get(index).equals("Change:"))
                ticket.put(lines.get(index), lines.get(index+1).replace("¤ ", ""));
            if(lines.get(index).equals("Cashier:"))
                ticket.put(lines.get(index), lines.get(index+1));
            if(lines.get(index).equals("Item") && lines.get(index+1).equals("Price"))
                parseItems(index+4);
            index++;
        }
        //System.out.print(ticket.toString());
        //System.out.print(items.toString());
        formattedPrint();
    }

    public void openDrawer() {
    }

    private void parseItems(int startIndex) {
        int index = startIndex;
        
        if(!lines.get(index).startsWith("---")) // Search for first line!
            return;
        else
            index++;
        
        while( index < lines.size() ) {
            if(lines.get(index).startsWith("---"))
                return; // We're done parsing items, last line

            String[] item = {
                lines.get(index), // name
                lines.get(index+2).replace("x", ""), // count
                lines.get(index+3).replace("¤ ", "") // price
            };
            items.add(item);
            index+=4;
        }
    }

    private void formattedPrint() {
        String endMsg = String.format("P,%s,______,_,__;VA MULTUMIM!;VA DORIM O ZI BUNA;;;;", ticket.get("Receipt:"));

        for( int index = 0; index < items.size(); index++ )
            saveForPrint( formatItem((String[]) items.get(index)) );

        items.clear();
        saveForPrint( formatTotal() );
        saveForPrint( endMsg );
        output();
    }

    private void saveForPrint( String text ) {
        //System.out.print(text);
        formattedTicket.add(text);
    }

    private void output() {
        ticket.clear();
        lines.clear();
        
        if( toFile )
            try {
            printToFile();
            } catch (FileNotFoundException ex) {
                System.out.println( ex.toString() );
            } catch (IOException ex) {
                System.out.println( ex.toString() );
            }
        
        // TODO: Add serial handler
    }

    private String formatItem(String[] item) {
        String msg = String.format( 
            "S,%s,______,_,__;%s;%s;%s;0;0;0;0;0;",
            ticket.get("Receipt:"),
            item[0],
            item[2],
            item[1]
        );
        return msg;
    }

    private String formatTotal() {
        String msg = String.format(
            "T,%s,______,_,__;%s;%s;;;;",
            ticket.get("Receipt:"),
            ticket.get("PaymentType"),
            ticket.get("Total.")
        );
        return msg;
    }

    private void printToFile() throws FileNotFoundException, IOException {
        File bon = new File(fileName);
        
        bon.delete();
        bon.createNewFile();

        PrintStream printer = new PrintStream(bon);
        for( String line: formattedTicket )
            printer.println( line );
        
        printer.flush();
        printer.close();
        formattedTicket.clear();
    }
}
