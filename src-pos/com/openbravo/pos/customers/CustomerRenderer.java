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

package com.openbravo.pos.customers;

import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;

public class CustomerRenderer extends DefaultListCellRenderer {
                
    private Icon icocustomer;

    /** Creates a new instance of ProductRenderer */
    public CustomerRenderer() {
        try {
            icocustomer = new ImageIcon(ImageIO.read(getClass().getClassLoader().getResourceAsStream("com/openbravo/images/kdmconfig.png")));
        } catch (Exception fnfe) {
            icocustomer = null;
        }   
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, null, index, isSelected, cellHasFocus);
        
        Object[] customer = (Object[]) value;
        if (customer != null) {
            setText((String) customer[1]);
            setIcon(icocustomer);
        }
        return this;
    }      
}
