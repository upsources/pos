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
import javax.swing.JComponent;

/**
 * FPrint driver for Datecs devices
 * @author Stas
 */
public class DevicePrinterFPrint implements DevicePrinter {

    // private boolean m_bInline;
    private String m_sName;
    private PrinterWritter m_CommOutputPrinter;
    private UnicodeTranslator m_trans;
    private String ticket;

    public DevicePrinterFPrint(PrinterWritter CommOutputPrinter, UnicodeTranslator trans) throws TicketPrinterException {
        m_sName = AppLocal.getIntString("Printer.Serial");
        m_CommOutputPrinter = CommOutputPrinter;
        m_trans = trans;
        ticket = "";
    }

    @Override
    public String getPrinterName() {
        return m_sName;
    }

    @Override
    public String getPrinterDescription() {
        return "fprint";
    }

    @Override
    public JComponent getPrinterComponent() {
        return null;
    }

    @Override
    public void reset() {
    }

    @Override
    public void beginReceipt() {
    }

    @Override
    public void printImage(BufferedImage image) {
    }

    @Override
    public void printBarCode(String type, String position, String code) {
    }

    @Override
    public void beginLine(int iTextSize) {
    }

    @Override
    public void printText(int iStyle, String sText) {
        ticket = ticket.concat(sText.trim() + "\n");
    }

    @Override
    public void endLine() {
    }

    @Override
    public void endReceipt() {
        m_CommOutputPrinter.write(m_trans.transString(ticket));
        m_CommOutputPrinter.flush();
        ticket = "";
    }

    @Override
    public void openDrawer() {
    }
}
