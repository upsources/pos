//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007 Openbravo, S.L.
//    http://sourceforge.net/projects/
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

import java.awt.*;
import java.awt.geom.*;

import com.openbravo.pos.printer.DevicePrinter;
import com.openbravo.pos.printer.screen.*;

public class MyPrinterState {

    private int m_iSize;
    
    /** Creates a new instance of PrinterState */
    public MyPrinterState(int iSize) {
        m_iSize = iSize;
    }
    
    public int getLineMult() {
        return getLineMult(m_iSize);
    }   
    public static int getLineMult(int iSize) {
        switch (iSize) {
        case 0: case 2:
            return 1;
        case 1: case 3:
            return 2;
        default:
            return 0;
        }
    }
    
    public Font getFont(Font baseFont, int iStyle) {
        Font f;
        switch (m_iSize) {
            case 0: 
                f = baseFont;
                break;
            case 2:
                f = baseFont.deriveFont(AffineTransform.getScaleInstance(2.0, 1.0));
                break;
           case 1:
                f = baseFont.deriveFont(AffineTransform.getScaleInstance(1.0, 2.0));
                break;
            case 3:
                f = baseFont.deriveFont(AffineTransform.getScaleInstance(2.0, 2.0));
                break;
            default:
                f = baseFont;
                break;
        }
        f = f.deriveFont((iStyle & DevicePrinter.STYLE_BOLD) != 0 ? Font.BOLD : Font.PLAIN);
        // Falta aplicar el subrayado
        return f;        
    }
    
}
