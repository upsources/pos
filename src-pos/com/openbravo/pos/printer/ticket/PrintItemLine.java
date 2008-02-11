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

import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import com.openbravo.pos.printer.screen.*;
import javax.swing.JLabel;

public class PrintItemLine implements PrintItem {
    
    private static final Font BASEFONT = new Font("Monospaced", Font.PLAIN, 12);
    private static final int FONTASCENT = 13; //
    private static final int FONTHEIGHT = 17; //
    private static final int FONTWIDTH = 7; //
    
    private int m_itextsize;
    private List<StyledText> m_atext;
    
    private JLabel label;
    
    /** Creates a new instance of PrinterItemLine */
    public PrintItemLine(int itextsize) {
        m_itextsize = itextsize;
        m_atext = new ArrayList<StyledText>();
        
        label = new JLabel();
        label.setLocation(0, 0);
    }
    
    public void addText(int style, String text) {
        m_atext.add(new StyledText(style, text));
    }
    
    public void draw(Graphics2D g, int x, int y, int width) {
        
        MyPrinterState ps = new MyPrinterState(m_itextsize);
        int left = x;
        for (int i = 0; i < m_atext.size(); i++) {
            StyledText t = m_atext.get(i);
            
            label.setFont(ps.getFont(BASEFONT, t.style));
            label.setText(t.text);
            label.setSize(label.getPreferredSize());
//            label.setBounds(0,0, FONTWIDTH * t.text.length(), FONTASCENT * ps.getLineMult());
            
            g.translate(left, y);
            label.paint(g);
            g.translate(-left, -y);
            
            // left += label.getWidth();
            left += FONTWIDTH * t.text.length();
        }
    }
    
    public int getHeight() {
        return FONTHEIGHT * MyPrinterState.getLineMult(m_itextsize);
    }    
    
    private static class StyledText {
        
        public StyledText(int style, String text) {
            this.style = style;
            this.text = text;
        }
        public int style;
        public String text;
    }
}
