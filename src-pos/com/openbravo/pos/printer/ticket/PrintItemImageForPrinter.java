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

import com.openbravo.pos.printer.printer.PrinterBook;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author jaroslawwozniak
 */
public class PrintItemImageForPrinter extends PrintItemImage {

    private int imageWidthForPrinter;
    private int imageHeightForPrinter;
    /*Top Margin */
    private final int V_GAP = 10;
    /*Guter*/
    private final int H_GAP = 10;

    public PrintItemImageForPrinter(BufferedImage image) {
        super(image);
        imageWidthForPrinter = (int) (image.getWidth() / (3.0 / 2));
        imageHeightForPrinter = (int) (image.getHeight() / (3.0 / 2));
    }

    public void draw(Graphics2D g, int x, int y, int width) {
        if (PrinterBook.isReceiptPrinter) {
            g.drawImage(image, (width - imageWidthForPrinter) / 2 - H_GAP, y, imageWidthForPrinter, imageHeightForPrinter, null);
        } else {
            g.drawImage(image, 72 + (width - imageWidthForPrinter) / 2 - H_GAP, y, imageWidthForPrinter, imageHeightForPrinter, null);
        }
    }

    public int getHeight() {
        return imageHeightForPrinter + V_GAP;
    }
}
