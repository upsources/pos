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
import java.awt.print.PrinterJob;
import javax.swing.JComponent;
import com.openbravo.pos.printer.DevicePrinter;
import com.openbravo.pos.printer.ticket.BasicTicketForPrinter;
import com.openbravo.pos.util.ReportUtils;
import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.Size2DSyntax;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;

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
    /*if the printer is a receìpt printer the value is "true" otherwise "false"*/
    private String isReceiptPrinter;
    /*1 point = 1/72 inch = 0.000352777m */
    private static double point = 0.000352777;

    /** Creates a new instance of DevicePrinterPrinter
     * @param printername - name of printer that will be called in the system
     * @param isReceiptPrinter - string with boolean values if the printer is a receipt
     */
    public DevicePrinterPrinter(String printername, String isReceiptPrinter) {
        this.isReceiptPrinter = isReceiptPrinter;
        m_sName = "Printer"; // "AppLocal.getIntString("Printer.Screen");
        m_ticketcurrent = null;
        printservice = ReportUtils.getPrintService(printername);
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
        m_ticketcurrent.printImage(image);
    }

    @Override
    public void printBarCode(String type, String position, String code) {
        m_ticketcurrent.printBarCode(type, position, code);
    }

    @Override
    public void beginLine(int iTextSize) {
        m_ticketcurrent.beginLine(0);
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
            printJob.setPrintable(new PrintableTicket(m_ticketcurrent));
            //add print requests
            PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
            aset.add(OrientationRequested.PORTRAIT);
            aset.add(new Copies(1));
            aset.add(new JobName(AppLocal.APP_NAME + " - Document", null));
            //is a receipt printer
            if (isReceiptPrinter.equals("true")) {
                MediaSize myISO = new MediaSize(78, getHeightForReceiptPrinters() + 10, Size2DSyntax.MM, MediaSizeName.NA_LEGAL);
                aset.add(MediaSizeName.NA_LEGAL);
            } else {
                aset.add(MediaSizeName.ISO_A4);
            }
            //set the printer
            printJob.setPrintService(printservice);
            //print with the attributes
            printJob.print(aset);
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

    /**
     * Getter that gives back lenght of a ticket
     * 
     * @return lenght of a ticket in mm
     */
    private int getHeightForReceiptPrinters() {

        int height = 0;
        int line = 0;
        //checks if the line is the last in a ticket
        while (m_ticketcurrent.getTheLastIndex() > line) {
            //add height of a line to total height
            height += m_ticketcurrent.getHeightOfCommands(line);
            line++;
        }
        return changePointsforMM(height);
    }

    /**
     * Method that changes points for milimeters. 
     * 
     * @param height - height in points
     */
    private int changePointsforMM(int height) {

        return (int) (height * point * 1000);
    }
}
