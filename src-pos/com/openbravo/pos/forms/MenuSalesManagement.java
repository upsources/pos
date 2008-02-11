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

/**
 *
 * @author adrianromero
 */
public class MenuSalesManagement extends BeanFactoryCache {
    
    /** Creates a new instance of MenuSalesManagement */
    public MenuSalesManagement() {
    }
    
    public Object constructBean(AppView app) throws BeanFactoryException {
        
        MenuDefinition menudef = new MenuDefinition("Menu.SalesManagement");
        menudef.addMenuTitle("Menu.SalesManagement.Reports");
        menudef.addMenuItem(new MenuPanelAction(app, "/com/openbravo/images/appointment.png", "Menu.UserSells", "com.openbravo.pos.reports.JReportUserSales"));
        menudef.addMenuItem(new MenuPanelAction(app, "/com/openbravo/images/appointment.png", "Menu.ClosedProducts", "com.openbravo.pos.reports.JReportClosedProducts"));
        menudef.addMenuItem(new MenuPanelAction(app, "/com/openbravo/images/appointment.png", "Menu.ReportTaxes", "com.openbravo.pos.reports.JReportTaxes"));
        menudef.addMenuItem(new MenuPanelAction(app, "/com/openbravo/images/cakes3d.png", "Menu.SalesChart", "com.openbravo.pos.reports.JChartSales"));
        return new JPanelMenu(menudef);        
    }    
}
