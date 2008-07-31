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

package com.openbravo.pos.forms;

import java.awt.Color;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.border.MatteBorder;

/**
 *
 * @author adrianromero
 */
public class MenuTitleDefinition implements MenuElement {
    
    public String KeyText;
    
    public void addComponent(JPanelMenu menu) {
       
//        if (p.x > 20) {
//            p.x = 20;
//            p.y += 55;
//        }
//        p.y += 20; // 20 puntitos extra de separacion.
        
        JLabel lbl = new JLabel(AppLocal.getIntString(KeyText));
        lbl.applyComponentOrientation(menu.getComponentOrientation());
        lbl.setBorder(new MatteBorder(new Insets(0, 0, 1, 0), new Color(0, 0, 0)));
//        lbl.setSize(670, 20);
//        lbl.setLocation(p);
//        p.y += 35;
//        comp.add(lbl);  
        
        menu.addTitle(lbl);
    }  
}
