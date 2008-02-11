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
public class MenuStockManagement extends BeanFactoryCache {
    
    /** Creates a new instance of MenuStockManagement */
    public MenuStockManagement() {
    }
    
    public Object constructBean(AppView app) throws BeanFactoryException {
        
        MenuDefinition menudef = new MenuDefinition("Menu.StockManagement"); 
        menudef.addMenuTitle("Menu.StockManagement.Edit");
        menudef.addMenuItem(new MenuPanelAction(app, "/com/openbravo/images/bookmark.png", "Menu.Products", "com.openbravo.pos.inventory.ProductsPanel"));
        menudef.addMenuItem(new MenuPanelAction(app, "/com/openbravo/images/bookmark.png", "Menu.ProductsWarehouse", "com.openbravo.pos.inventory.ProductsWarehousePanel"));
        menudef.addMenuItem(new MenuPanelAction(app, "/com/openbravo/images/bookmark.png", "Menu.Categories", "com.openbravo.pos.inventory.CategoriesPanel"));
        menudef.addMenuItem(new MenuPanelAction(app, "/com/openbravo/images/bookmark.png", "Menu.Taxes", "com.openbravo.pos.panels.JPanelTax"));
        menudef.addMenuItem(new MenuPanelAction(app, "/com/openbravo/images/bookmark.png", "Menu.StockDiary", "com.openbravo.pos.inventory.StockDiaryPanel"));
        menudef.addMenuItem(new MenuPanelAction(app, "/com/openbravo/images/bookmark.png", "Menu.StockMovement", "com.openbravo.pos.inventory.StockManagement"));
        menudef.addMenuTitle("Menu.StockManagement.Reports");
        menudef.addMenuItem(new MenuPanelAction(app, "/com/openbravo/images/appointment.png", "Menu.Products", "com.openbravo.pos.reports.JReportProducts"));
        menudef.addMenuItem(new MenuPanelAction(app, "/com/openbravo/images/appointment.png", "Menu.Catalog", "com.openbravo.pos.reports.JReportCatalog"));
        menudef.addMenuItem(new MenuPanelAction(app, "/com/openbravo/images/appointment.png", "Menu.Inventory", "com.openbravo.pos.reports.JReportInventory"));
        menudef.addMenuItem(new MenuPanelAction(app, "/com/openbravo/images/appointment.png", "Menu.Inventory2", "com.openbravo.pos.reports.JReportInventory2"));
        menudef.addMenuItem(new MenuPanelAction(app, "/com/openbravo/images/appointment.png", "Menu.InventoryBroken", "com.openbravo.pos.reports.JReportInventoryBroken"));
        menudef.addMenuItem(new MenuPanelAction(app, "/com/openbravo/images/appointment.png", "Menu.InventoryDiff", "com.openbravo.pos.reports.JReportInventoryDiff"));
        return new JPanelMenu(menudef);        
    }    
}
