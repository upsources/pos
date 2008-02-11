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
import com.openbravo.pos.printer.screen.*;

public class BasicTicket implements PrintItem {
    
    private java.util.List<PrintItem> m_aCommands;  
    private PrintItemLine pil; 
    private int m_iBodyHeight;
    
    /** Creates a new instance of AbstractTicket */
    public BasicTicket() {
        m_aCommands = new ArrayList<PrintItem>();
        pil = null;
        m_iBodyHeight = 0;
    }
    
    public int getHeight() {
        return m_iBodyHeight;
    }
    
    public void draw(Graphics2D g2d, int x, int y, int width) {
        
        int currenty = y;
        for (PrintItem pi : m_aCommands) {
            pi.draw(g2d, x, currenty, width);
            currenty += pi.getHeight(); 
        }      
    }
    
    // INTERFAZ PRINTER 2
    public void printImage(BufferedImage image) {
        
        PrintItem pi = new PrintItemImage(image);
        m_aCommands.add(pi);
        m_iBodyHeight += pi.getHeight();
    }
    public void printBarCode(String sType, String sCode) {

        PrintItem pi = new PrintItemBarcode(sType, sCode);
        m_aCommands.add(pi);
        m_iBodyHeight += pi.getHeight();
    }
    public void beginLine(int iTextSize) {
        pil = new PrintItemLine(iTextSize);
    }
    public void printText(int iStyle, String sText) {
        if (pil != null) {
            pil.addText(iStyle, sText);
        }
    }    
    public void endLine() {
        if (pil != null) {
            m_aCommands.add(pil);
            m_iBodyHeight += pil.getHeight(); 
            pil = null;
        }
    }    
}
