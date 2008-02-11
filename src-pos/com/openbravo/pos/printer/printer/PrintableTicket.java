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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import com.openbravo.pos.printer.ticket.BasicTicket;

public class PrintableTicket implements Printable {
    
    private static final int H_GAP = 8;
    private static final int V_GAP = 8;
    private static final int COLUMNS = 42;
    private static final int LINEWIDTH = COLUMNS * 7;  
    
    private BasicTicket basict;
    
    /** Creates a new instance of PrintableTicket */
    public PrintableTicket(BasicTicket t) {
        basict = t;        
    }
    
    public int print(Graphics g, PageFormat pf, int pi) throws PrinterException {
        
        if (pi >= 1) {
            return Printable.NO_SUCH_PAGE;
        }        
        
        basict.draw((Graphics2D) g, (int) pf.getImageableX() + H_GAP, (int) pf.getImageableY() + V_GAP, LINEWIDTH);
        return Printable.PAGE_EXISTS;
    }    
}
