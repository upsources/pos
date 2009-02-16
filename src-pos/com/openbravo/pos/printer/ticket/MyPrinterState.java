//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007 Openbravo, S.L.
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

package com.openbravo.pos.printer.ticket;

import java.awt.*;
import java.awt.geom.*;

import com.openbravo.pos.printer.DevicePrinter;
import java.awt.font.TextAttribute;
import java.text.AttributedString;

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
            case 0:
            case 2:
                return 1;
            case 1:
            case 3:
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

    @Deprecated
    public AttributedString changeSize(int size, AttributedString text) {

        // text.addAttribute(TextAttribute.WIDTH, TextAttribute.WIDTH_SEMI_EXTENDED);
        switch (size) {
            case 0:
                text.addAttribute(TextAttribute.SIZE, 7);
                break;
            case 1:
                text.addAttribute(TextAttribute.SIZE, 9);
                break;
            case 2:
                text.addAttribute(TextAttribute.SIZE, 11);
                break;
            case 3:
                text.addAttribute(TextAttribute.SIZE, 13);
                break;
            default:
                text.addAttribute(TextAttribute.SIZE, 7);


        }

        return text;
    }

    @Deprecated
    public AttributedString changeStyle(int style, AttributedString text) {

        switch (style) {
            case DevicePrinter.STYLE_BOLD:
                text.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
                break;
            case DevicePrinter.STYLE_UNDERLINE:
                text.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                break;
            case DevicePrinter.STYLE_PLAIN:
                break;
            default:
        }

        return text;
    }
}
