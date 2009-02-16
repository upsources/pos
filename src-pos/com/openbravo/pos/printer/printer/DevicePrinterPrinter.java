//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007 Openbravo, S.L.
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
 * @since 2.30
 */
public class DevicePrinterPrinter implements DevicePrinter {

    /*name of a printer*/
    private String m_sName;
    /*a ticket to print*/
    private BasicTicketForPrinter m_ticketcurrent;
    /*system printer*/
    private PrintService printservice;
    //book
    private PrinterBook printerBook;
    private boolean receiptPrinter;

    /** 
     * Creates a new instance of DevicePrinterPrinter
     * 
     * @param printername - name of printer that will be called in the system
     * @param isReceiptPrinter - string with boolean values if the printer is a receipt
     */
    public DevicePrinterPrinter(String printername, boolean receiptPrinter) {
        this.receiptPrinter = receiptPrinter;
        m_sName = "Printer"; // "AppLocal.getIntString("Printer.Screen");
        m_ticketcurrent = null;
        printservice = ReportUtils.getPrintService(printername);
        printerBook = new PrinterBook(this);
    }

    /**
     * Method that returns a boolean value if a printer is a recipt printert or not
     *
     * @return receiptPrinter a boolean value if a printer is a receipt printer
     */
    public boolean isReceiptPrinter(){
        return receiptPrinter;
    }

    /**
     * Getter that returns the name of a printer
     *
     * @return m_sName a name of a printer
     */
    @Override
    public String getPrinterName() {
        return m_sName;
    }

    /**
     * Getter that returns the description of a printer
     *
     * @return decription of a printer
     */
    @Override
    public String getPrinterDescription() {
        return null;
    }

    /**
     * Getter that returns the printer's component
     *
     * @return printer's component
     */
    @Override
    public JComponent getPrinterComponent() {
        return null;
    }

    /**
     * Method that sets the current ticket as a null
     */
    @Override
    public void reset() {
        m_ticketcurrent = null;
    }

    /**
     * Method that is responsible for start a new ticket
     */
    @Override
    public void beginReceipt() {
        m_ticketcurrent = new BasicTicketForPrinter();
    }

    /**
     * Method that is responsible for printing an image
     *
     * @param image a buffered image object
     */
    @Override
    public void printImage(BufferedImage image) {
        m_ticketcurrent.printImage(image, isReceiptPrinter());
    }

    /**
     * Method that is responsible for printing a barcode
     *
     * @param type a type of a barcode
     * @param position coordinates of a barcode on a receipt
     * @param code the code of a productmiale
     */
    @Override
    public void printBarCode(String type, String position, String code) {
        m_ticketcurrent.printBarCode(type, position, code, isReceiptPrinter());
    }

    /**
     * Method that is responsible for starting a new line on a receipt
     *
     * @param iTextSize a size of text in the line
     */
    @Override
    public void beginLine(int iTextSize) {
        m_ticketcurrent.beginLine(iTextSize);
    }

    /**
     * Method that is responsible for printing text
     *
     * @param iStyle style of text
     * @param sText text to print
     */
    @Override
    public void printText(int iStyle, String sText) {
        m_ticketcurrent.printText(iStyle, sText);
    }

    /**
     * Method that is responsible for ending a line
     */
    @Override
    public void endLine() {
        m_ticketcurrent.endLine();
    }

    /**
     * Method that is responsible for ending and printing a ticket<br>
     * It manages to get a printerJob, set the name of the job, get a Book object<br>
     * and print the receipt
     */
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

    /**
     * Method that is responsible for opening a drawer
     */
    @Override
    public void openDrawer() {
        // Una simulacion
        Toolkit.getDefaultToolkit().beep();
    }
}