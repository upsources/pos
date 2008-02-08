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

package com.openbravo.data.gui;

import javax.swing.*;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.data.user.BrowsableEditableData;
import com.openbravo.data.user.DirtyListener;

public class JLabelDirty extends JLabel {
    
    private static Icon m_IconModif = null;
    private static Icon m_IconNull = null;

    /** Creates a new instance of JDirtyPicture */
    public JLabelDirty(DirtyManager dm) {
        
        if (m_IconModif == null) {
            m_IconModif = new ImageIcon(getClass().getResource("/com/openbravo/images/edit.png"));
        }
        if (m_IconNull == null) {
            m_IconNull = new NullIcon(16, 16);
        }
        
        dm.addDirtyListener(new DirtyListener() {
            public void changedDirty(boolean bDirty) {
                setIcon(bDirty ? m_IconModif : m_IconNull);
            }
        });
    }  
}
