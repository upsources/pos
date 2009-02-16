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

import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;

public class PrintItemLine implements PrintItem {

    protected static Font BASEFONT = new Font("Monospaced", Font.PLAIN, 12);
    protected static int FONTHEIGHT = 17; //
    protected static int FONTWIDTH = 7; //
    protected int m_itextsize;
    protected List<StyledText> m_atext;
    protected JLabel label;

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

    protected static class StyledText {

        public StyledText(int style, String text) {
            this.style = style;
            this.text = text;
        }
        public int style;
        public String text;
    }
}
