/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openbravo.pos.printer.escpos;

import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.printer.DevicePrinter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;

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

    public DevicePrinterFPrint() {
        m_sName = AppLocal.getIntString("Printer.Serial");
        ticket = new HashMap<String,String>();
        lines = new ArrayList<String>();
        items = new ArrayList();
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
        System.out.print(ticket.toString());
        //System.out.print(items.toString());
        formattedPrint();
        ticket.clear();
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
        String endMsg = "P,1,______,_,__;VA MULTUMIM!;VA DORIM O ZI BUNA;;;;";

        for( int index = 0; index < items.size(); index++ ) {
            output( formatItem(ticket.get("Receipt:"), (String[]) items.get(index)) );
        }

        output(endMsg);
    }

    private void output( String text ) {
        System.out.println( text );
    }

    private String formatItem(String id, String[] item) {
        String msg = String.format( "S,%s,______,_,__;%s;%s;%s;0;0;0;0;0;", id, item[0], item[2], item[1] );
        return msg;
    }

}
