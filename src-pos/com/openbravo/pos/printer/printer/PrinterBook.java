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

package com.openbravo.pos.printer.printer;

import com.openbravo.pos.printer.ticket.BasicTicketForPrinter;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jaroslawwozniak
 */
public class PrinterBook {

    private PageFormat pageFormat;
    DevicePrinterPrinter printer;
    /*current line ang page */
    private int line,  page;
    /*Array lists for start and end line */
    private List startLine,  endLine;

    public PrinterBook(DevicePrinterPrinter printer) {
        this.printer = printer;
        pageFormat = new PageFormat();
        setPageFormatForPage();
        startLine = new ArrayList();
        //the first page will start from the first line
        startLine.add(0, 0);
        endLine = new ArrayList();
    }

    public Book getBook(BasicTicketForPrinter ticket) {
        countLinesOnPage(ticket);
        Book book = new Book();
        int i = 0;
        while (i <= page) {
            book.append(new PrintableTicket(ticket, (Integer) startLine.get(i), (Integer) endLine.get(i)), pageFormat);
            i++;
        }
        return book;
    }

    private void countLinesOnPage(BasicTicketForPrinter ticket) {
        boolean flag = false;
        //a variable for the index of next line
        int temp = 0;
        //loop goes if until the last line in a ticket
        while (ticket.getTheLastIndex() > line) {
            //total hight of lines on a page
            int height = 0;
            //loop goes until an imageable height of a page is reached
            while ((int) pageFormat.getImageableHeight() > height + ticket.getHeightOfCommands(temp)) {
                //checks if the line is the last in a ticket
                if (ticket.getTheLastIndex() == line) {
                    flag = true;
                    break;
                }
                //add height of a line to total height
                height += ticket.getHeightOfCommands(line);
                //temp is the index of the next line until the next line is the last
                temp = ticket.getTheLastIndex() != ++line ? line : line - 1;
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
    }

    private Paper getPaperForPage() {

        Paper paper = new Paper();
        if (printer.isReceiptPrinter()) {
            paper.setSize(226, 226);
            paper.setImageableArea(10, 10, 194, 210);
        } else {
            paper.setSize(595, 841);
            paper.setImageableArea(72, 72, 451, 597);
        }

        return paper;
    }

    private void setPageFormatForPage() {

        pageFormat.setPaper(getPaperForPage());
        pageFormat.setOrientation(PageFormat.PORTRAIT);
    }
}
