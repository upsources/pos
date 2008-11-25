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
import java.util.ArrayList;

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
    private static final int LINEWIDTH = COLUMNS * 7;
    private boolean flag;
    /*a ticket */
    private BasicTicketForPrinter basict;
    /*current line ang page */
    private int line,  page;
    /*Array lists for start and end line */
    private ArrayList startLine,  endLine;

    /** Creates a new instance of PrintableTicket
     * 
     * @param t - BasicTicketForPrinter
     */
    public PrintableTicket(BasicTicketForPrinter t) {
        basict = t;
        startLine = new ArrayList();
        //the first page will start from the first line
        startLine.add(0, 0);
        endLine = new ArrayList();
    }

    /**
     * Method print prints the ticket and is being called for each site twice.
     * For futher info look into: 
     * @see java.awt.print.Printable;
     */
    @Override
    public int print(Graphics g, PageFormat pf, int pi) throws PrinterException {

        //a variable for the index of next line
        int temp = 0;
        //loop goes if until the last line in a ticket
        while (basict.getTheLastIndex() > line) {
            //total hight of lines on a page
            int height = 0;
            //loop goes until a imageable height of a page is reached
            while ((int) pf.getImageableHeight() > height + basict.getHeightOfCommands(temp)) {
                //checks if the line is the last in a ticket
                if (basict.getTheLastIndex() == line) {
                    flag = true;
                    break;
                }
                //add height of a line to total height
                height += basict.getHeightOfCommands(line);
                //temp is the index of the next line until the next line is the last
                temp = basict.getTheLastIndex() != ++line ? line : line - 1;
            }
            //the line was the last
            if (flag) {
                endLine.add(page, line - 1);
            } //there are still some lines...
            else {
                endLine.add(page, line);
                startLine.add(++page, endLine.get(--page));
                page++;
            }
        }
        //checks if the present page exists
        if (pi > page) {
            return Printable.NO_SUCH_PAGE;
        }
        //exists so draw the ticket
        basict.draw((Graphics2D) g, (int) pf.getImageableX(), (int) pf.getImageableY(), LINEWIDTH, (Integer) startLine.get(pi), (Integer) endLine.get(pi));

        return Printable.PAGE_EXISTS;
    }
}
