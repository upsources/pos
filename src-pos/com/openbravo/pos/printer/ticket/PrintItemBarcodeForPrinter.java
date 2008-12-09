//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2008 Openbravo, S.L.
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
//    Foundation, Inc., 51 Franklin Street, Fifth floor, Boston, MA  02110-1301  USA

package com.openbravo.pos.printer.ticket;

import com.openbravo.pos.printer.printer.DevicePrinterPrinter;
import com.openbravo.pos.printer.printer.PrinterBook;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import org.krysalis.barcode4j.output.java2d.Java2DCanvasProvider;

/**
 *
 * @author jaroslawwozniak
 */
public class PrintItemBarcodeForPrinter extends PrintItemBarcode {

    private boolean receiptPrinter;

    public PrintItemBarcodeForPrinter(String type, String position, String code, boolean receiptPrinter) {
        super(type, position, code);
        this.receiptPrinter = receiptPrinter;
        m_barcode.setBarHeight(20.0);
    }

    public void draw(Graphics2D g, int x, int y, int width) {

        if (m_barcode != null) {
            Graphics2D g2d = (Graphics2D) g;

            AffineTransform oldt = g2d.getTransform();
            if (receiptPrinter) {
                g2d.translate((width - m_iWidth) / 2 - 10, y + 10);
            } else {
                g2d.translate(72 + (width - m_iWidth) / 2 - 10, y + 10);
            }
            try {
                m_barcode.generateBarcode(new Java2DCanvasProvider(g2d, 0), m_sCode);
            } catch (IllegalArgumentException e) {
                g2d.drawRect(0, 0, m_iWidth, m_iHeight);
                g2d.drawLine(0, 0, m_iWidth, m_iHeight);
                g2d.drawLine(m_iWidth, 0, 0, m_iHeight);
            }

            g2d.setTransform(oldt);
        }
    }

    public int getHeight() {
        return m_iHeight + 10;
    }
}
