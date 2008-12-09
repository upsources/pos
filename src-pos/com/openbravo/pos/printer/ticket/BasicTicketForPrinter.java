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

package com.openbravo.pos.printer.ticket;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author jaroslawwozniak
 */
public class BasicTicketForPrinter extends BasicTicket {

    /** Creates a new instance of AbstractTicket */
    public BasicTicketForPrinter() {
        super();
    }

    @Override
    public void beginLine(int iTextSize) {
        pil = new PrintItemLineForPrinter(iTextSize);
    }

    public void printBarCode(String type, String position, String code, boolean isReceiptPrinter) {
        PrintItem pi = new PrintItemBarcodeForPrinter(type, position, code, isReceiptPrinter);
        m_aCommands.add(pi);
        m_iBodyHeight += pi.getHeight();
    }

    public void draw(Graphics2D g2d, int x, int y, int width, int start, int lines) {
        int currenty = y;
        int i = 0;
        //loop for lines on 1 page
        for (i = start; i < lines; i++) {

            m_aCommands.get(i).draw(g2d, x, currenty, width);
            currenty += m_aCommands.get(i).getHeight();
        }
        //print the last line
        if (i == lines) {
            m_aCommands.get(i).draw(g2d, x, currenty, width);
        }
    }

    public void printImage(BufferedImage image, boolean isReceiptPrinter) {

        PrintItem pi = new PrintItemImageForPrinter(image, isReceiptPrinter);
        m_aCommands.add(pi);
        m_iBodyHeight += pi.getHeight();
    }

    public int getHeightOfCommands(int line) {
        return m_aCommands.get(line).getHeight();

    }

    public String getCommand(int line) {
        return m_aCommands.get(line).toString();
    }

    public ArrayList getCommands() {
        return (ArrayList) m_aCommands;
    }

    public int getTheLastIndex() {
        return m_aCommands.size();
    }
}
