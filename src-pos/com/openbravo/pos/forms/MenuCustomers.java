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
public class MenuCustomers extends BeanFactoryCache {
    
    /** Creates a new instance of MenuMaintenance */
    public MenuCustomers() {
    }
    
    public Object constructBean(AppView app) throws BeanFactoryException {
        
        MenuDefinition menudef = new MenuDefinition("Menu.Customers");
        menudef.addMenuTitle("Menu.Customers");        
        menudef.addMenuItem(new MenuPanelAction(app, "/com/openbravo/images/kdmconfig.png", "Menu.CustomersManagement", "com.openbravo.pos.customers.CustomersPanel"));
        return new JPanelMenu(menudef);        
    }    
}