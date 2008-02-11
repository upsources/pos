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

package com.openbravo.data.gui;

import javax.swing.Icon;

/**
 *
 * @author  adrian
 */
public class CompoundIcon implements Icon {
    
    private Icon m_icon1;
    private Icon m_icon2;
    
    /** Creates a new instance of CompoundIcon */
    public CompoundIcon(Icon icon1, Icon icon2) {
        m_icon1 = icon1;
        m_icon2 = icon2;
    }
    
    public int getIconHeight() {
        return Math.max(m_icon1.getIconHeight(), m_icon2.getIconHeight());
    }
    
    public int getIconWidth() {
        return m_icon1.getIconWidth() + m_icon2.getIconWidth();
    }
    
    public void paintIcon(java.awt.Component c, java.awt.Graphics g, int x, int y) {
        m_icon1.paintIcon(c, g, x, y);
        m_icon2.paintIcon(c, g, x + m_icon1.getIconWidth(), y);
    }
    
}
