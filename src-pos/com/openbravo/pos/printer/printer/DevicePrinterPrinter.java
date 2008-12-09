//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007 Openbravo, S.L.
//    http://sourceforge.net/projects/openbravopos
//
//    This program is free software; you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation; either version 2 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program; if not, write to the Free Software
//    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

package com.openbravo.pos.printer.printer;

import com.openbravo.pos.forms.AppLocal;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import com.openbravo.pos.printer.DevicePrinter;
import com.openbravo.pos.printer.ticket.BasicTicketForPrinter;
import com.openbravo.pos.util.ReportUtils;
import java.awt.print.PrinterJob;
import javax.print.PrintService;

/**
 *Class DevicePrinterPrinter is responsible for printing tickets using system <br>
 * printers. It takes into consideration if a user set a printer as a receipt <br>
 * printer or not. 
 * <p>For receipt printers lenght of a receipt must be calculated in this class.</p>
 * <p>For normal printers number of pages must be calculated dynamically in the <br>
 * class PrintableTicket @see com.openbravo.pos.printer.printer.PrintableTicket
 *  
 * @author jaroslawwozniak
 */
public class DevicePrinterPrinter implements DevicePrinter {

    /*name of a printer*/
    private String m_sName;
    /*a ticket to print*/
    private BasicTicketForPrinter m_ticketcurrent;
    /*system printer*/
    private PrintService printservice;
    private PrinterBook printerBook;
    private boolean receiptPrinter;

    /** Creates a new instance of DevicePrinterPrinter
     * @param printername - name of printer that will be called in the system
     * @param isReceiptPrinter - string with boolean values if the printer is a receipt
     */
    public DevicePrinterPrinter(String printername, String isReceiptPrinter) {
        this.receiptPrinter = Boolean.valueOf(isReceiptPrinter);
        m_sName = "Printer"; // "AppLocal.getIntString("Printer.Screen");
        m_ticketcurrent = null;
        printservice = ReportUtils.getPrintService(printername);
        printerBook = new PrinterBook(this);
    }

    public boolean isReceiptPrinter(){
        return receiptPrinter;
    }

    @Override
    public String getPrinterName() {
        return m_sName;
    }

    @Override
    public String getPrinterDescription() {
        return null;
    }

    @Override
    public JComponent getPrinterComponent() {
        return null;
    }

    @Override
    public void reset() {
        m_ticketcurrent = null;
    }

    @Override
    public void beginReceipt() {
        m_ticketcurrent = new BasicTicketForPrinter();
    }

    @Override
    public void printImage(BufferedImage image) {
        m_ticketcurrent.printImage(image, isReceiptPrinter());
    }

    @Override
    public void printBarCode(String type, String position, String code) {
        m_ticketcurrent.printBarCode(type, position, code, isReceiptPrinter());
    }

    @Override
    public void beginLine(int iTextSize) {
        m_ticketcurrent.beginLine(iTextSize);
    }

    @Override
    public void printText(int iStyle, String sText) {
        m_ticketcurrent.printText(iStyle, sText);
    }

    @Override
    public void endLine() {
        m_ticketcurrent.endLine();
    }

    @Override
    public void endReceipt() {

        try {
            //get the printer
            PrinterJob printJob = PrinterJob.getPrinterJob();
            printJob.setJobName(AppLocal.APP_NAME + " - Document");
            printJob.setPageable(printerBook.getBook(m_ticketcurrent));

            //set the printer
            if (printservice == null) {
                if (printJob.printDialog()) {
                    printJob.print();
                }
            } else {
                printJob.setPrintService(printservice);
                printJob.print();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //ticket is not needed any more
        m_ticketcurrent = null;
    }

    @Override
    public void openDrawer() {
        // Una simulacion
        Toolkit.getDefaultToolkit().beep();
    }
}