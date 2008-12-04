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

import com.openbravo.pos.printer.ticket.BasicTicketForPrinter;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

/**
 *Class PrintableTicket implements interface Printable and it is responsible <br>
 * for printing a ticket.
 * 
 * @author jaroslawwozniak
 */
public class PrintableTicket implements Printable {

    /*how many columns in the ticket? */
    private static final int COLUMNS = 42;
    /*the width of a line dependent on No. of columns */
    private static final int LINEWIDTH = COLUMNS * 5;
    /*a ticket */
    private BasicTicketForPrinter basict;
    private int start,  end;

    /** Creates a new instance of PrintableTicket
     * 
     * @param t - BasicTicketForPrinter
     */
    public PrintableTicket(BasicTicketForPrinter t, int start, int end) {
        basict = t;
        this.start = start;
        this.end = end;
    }

    /**
     * Method print prints the ticket and is being called for each site twice.
     * For futher info look into: 
     * @param g
     * @param pf
     * @param pi
     * @see java.awt.print.Printable
     */
    @Override
    public int print(Graphics g, PageFormat pf, int pi) throws PrinterException {

        Graphics2D g2 = (Graphics2D) g;
        //exists so draw the ticket
        basict.draw(g2, (int) pf.getImageableX(), (int) pf.getImageableY(), LINEWIDTH, start, end);

        return Printable.PAGE_EXISTS;
    }
}
