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
public class MenuMaintenance extends BeanFactoryCache {
    
    /** Creates a new instance of MenuMaintenance */
    public MenuMaintenance() {
    }
    
    public Object constructBean(AppView app) throws BeanFactoryException {
        
        MenuDefinition menudef = new MenuDefinition("Menu.Maintenance");
        menudef.addMenuTitle("Menu.Maintenance.POS");        
        menudef.addMenuItem(new MenuPanelAction(app, "/com/openbravo/images/kdmconfig.png", "Menu.Users", "com.openbravo.pos.admin.PeoplePanel"));
        menudef.addMenuItem(new MenuPanelAction(app, "/com/openbravo/images/yast_group_add.png", "Menu.Roles", "com.openbravo.pos.admin.RolesPanel"));
        menudef.addMenuItem(new MenuPanelAction(app, "/com/openbravo/images/clipart.png", "Menu.Resources", "com.openbravo.pos.admin.ResourcesPanel"));
        menudef.addMenuItem(new MenuPanelAction(app, "/com/openbravo/images/gohome.png", "Menu.Locations", "com.openbravo.pos.inventory.LocationsPanel"));
        menudef.addMenuItem(new MenuPanelAction(app, "/com/openbravo/images/bookmark.png", "Menu.Floors", "com.openbravo.pos.mant.JPanelFloors"));
        menudef.addMenuItem(new MenuPanelAction(app, "/com/openbravo/images/bookmark.png", "Menu.Tables", "com.openbravo.pos.mant.JPanelPlaces"));
        menudef.addMenuTitle("Menu.Maintenance.ERP");        
        menudef.addMenuItem(new MenuExecAction(app, "/com/openbravo/images/openbravo.png", "Menu.ERPProducts", "com.openbravo.possync.ProductsSyncCreate"));
        menudef.addMenuItem(new MenuExecAction(app,"/com/openbravo/images/openbravo.png", "Menu.ERPOrders", "com.openbravo.possync.OrdersSyncCreate"));
        return new JPanelMenu(menudef);        
    }    
}
