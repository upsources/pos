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

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

/**
 *
 * @author adrianromero
 */
public class MenuPanelAction extends AbstractAction {

    private AppView m_App;
    private String m_sMyView;

    /** Creates a new instance of MenuPanelAction */
    public MenuPanelAction(AppView app, String icon, String keytext, String sMyView) {
        putValue(Action.SMALL_ICON, new ImageIcon(JPrincipalApp.class.getResource(icon)));
        putValue(Action.NAME, AppLocal.getIntString(keytext));
        putValue(AppUserView.ACTION_TASKNAME, sMyView);
        m_App = app;
        m_sMyView = sMyView;
    }
    public void actionPerformed(ActionEvent evt) {

        m_App.getAppUserView().showTask(m_sMyView);            
    }    
}
