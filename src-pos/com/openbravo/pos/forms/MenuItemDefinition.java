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

import java.awt.Dimension;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.SwingConstants;

/**
 *
 * @author adrianromero
 */
public class MenuItemDefinition implements MenuElement {
    
    private Action act;
    
    public MenuItemDefinition(Action act) {
        this.act = act;
    }
    
    public void addComponent(JPanelMenu menu) {
        
        JButton btn = new JButton(act); 
        
        btn.setFocusPainted(false);
        btn.setFocusable(false);
        btn.setRequestFocusEnabled(false);
        btn.setHorizontalAlignment(SwingConstants.LEADING);
        btn.setPreferredSize(new Dimension(220, 50));
        
//        btn.setSize(220, 50);
//        btn.setLocation(p);
//        if (p.x >= 470) {
//            p.x = 20;
//            p.y += 55;
//        } else {
//            p.x += 225;
//        }
//        comp.add(btn);        
        
        menu.addEntry(btn);
    }
}
