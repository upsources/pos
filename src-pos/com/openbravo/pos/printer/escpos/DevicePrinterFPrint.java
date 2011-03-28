//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2009 Openbravo, S.L.
//    Copyright (C) 2011 UpSources.
//    http://www.openbravo.com/product/pos
//
//    This file is part of Openbravo POS.
//
//    Openbravo POS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    Openbravo POS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with Openbravo POS.  If not, see <http://www.gnu.org/licenses/>.
package com.openbravo.pos.printer.escpos;

import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.printer.DevicePrinter;
import com.openbravo.pos.printer.TicketPrinterException;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;

/**
 * FPrint driver for Datecs devices
 * @author Stas
 */
public class DevicePrinterFPrint implements DevicePrinter {

    // private boolean m_bInline;
    private String m_sName;
    private Map<String,String> ticket;
    private ArrayList<String> lines;
    private ArrayList items;
    private ArrayList<String> formattedTicket;
    private PrinterWritter m_CommOutputPrinter;
    private UnicodeTranslator m_trans;

    public DevicePrinterFPrint(PrinterWritter CommOutputPrinter, UnicodeTranslator trans) throws TicketPrinterException {
        m_sName = AppLocal.getIntString("Printer.Serial");
        ticket = new HashMap<String,String>();
        lines = new ArrayList<String>();
        items = new ArrayList();
        formattedTicket = new ArrayList<String>();
        m_CommOutputPrinter = CommOutputPrinter;
        m_trans = trans;
    }

    public String getPrinterName() {
        return m_sName;
    }

    public String getPrinterDescription() {
        return "fprint";
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
                ticket.put(lines.get(index), lines.get(index+1).replace("¤ ", "").replace(" LEI", ""));
            if(lines.get(index).equals("Total."))
                ticket.put(lines.get(index), lines.get(index+1).replace("¤ ", "").replace(" LEI", ""));
            if(lines.get(index).equals("Cash"))
                ticket.put("PaymentType", "0");
            if(lines.get(index).equals("Mag card"))
                ticket.put("PaymentType", "2");
            if(lines.get(index).equals("Cheque"))
                ticket.put("PaymentType", "3");
            if(lines.get(index).equals("Tendered:"))
                ticket.put(lines.get(index), lines.get(index+1).replace("¤ ", "").replace(" LEI", ""));
            if(lines.get(index).equals("Change:"))
                ticket.put(lines.get(index), lines.get(index+1).replace("¤ ", "").replace(" LEI", ""));
            if(lines.get(index).equals("Cashier:"))
                ticket.put(lines.get(index), lines.get(index+1));
            if(lines.get(index).equals("Item") && lines.get(index+1).equals("Price"))
                parseItems(index+5);
            index++;
        }
        lines.clear();
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
                lines.get(index+3).replace("¤ ", "").replace(" LEI", ""), // price
                lines.get(index+4) // CMID
            };
            
            items.add(item);
            index+=5;
        }
    }

    private void formattedPrint() {
        for( int index = 0; index < items.size(); index++ )
            saveForPrint( formatItem((String[]) items.get(index)) );

        items.clear();
        saveForPrint( formatTotal() );
        ticket.clear();
        write();
    }

    private void saveForPrint( String text ) {
            formattedTicket.add(text);
    }

    private String formatItem(String[] item) {
        String msg = String.format( 
            "S,1,______,_,__;%s;%s;%s;1;%s;1;0;0;",
            item[0].toUpperCase(),
            item[2].replace(",", "."),
            item[1].replace(",", "."),
            item[3]
        );
        return msg;
    }

    private String formatTotal() {
        String msg = String.format(
            "T,1,______,_,__;%s;%s;;;;",
            ticket.get("PaymentType"),
            ticket.get("Total.").replace(",", ".")
        );
        return msg;
    }

    private void write() {
        String data = "";
        for( String line: formattedTicket )
            data = data + line + "\n";

        m_CommOutputPrinter.write( m_trans.transString(data) );
        m_CommOutputPrinter.flush();

        formattedTicket.clear();
    }
}
