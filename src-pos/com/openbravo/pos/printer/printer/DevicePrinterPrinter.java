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

/**
 *
 * @author jaroslawwozniak
 */
public class DevicePrinterPrinter implements DevicePrinter {
    
    private String m_sName;
    private BasicTicketForPrinter m_ticketcurrent;
    private PrintService printservice;

    /** Creates a new instance of DevicePrinterPrinter */
    public DevicePrinterPrinter(String printername) {
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

       // }
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
            // Print
           PrinterJob printJob = PrinterJob.getPrinterJob();
            printJob.setPrintable(new PrintableTicket(m_ticketcurrent));
            //PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
         //   aset.add(OrientationRequested.PORTRAIT);
         //   aset.add(new Copies(1));
         //   aset.add(new JobName("My job", null));
//           aset.add(MediaSize.ISO.A4);
       // PageFormat format = printJob.pageDialog(printJob.defaultPage());
            printJob.setJobName(AppLocal.APP_NAME + " - Document");
            printJob.setPrintService(printservice);
        //   printJob.s
            //printJob.printDialog();
         //    if (printJob.printDialog())
                printJob.print();
        } catch (Exception ex) {
            ex.printStackTrace();
        }   
        
        m_ticketcurrent = null;
    }
    
    public void openDrawer() {
        // Una simulacion
        Toolkit.getDefaultToolkit().beep();
    }  
}
