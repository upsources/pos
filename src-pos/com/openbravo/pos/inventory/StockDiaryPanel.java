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

package com.openbravo.pos.inventory;

import com.openbravo.basic.BasicException;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.data.user.ListProvider;
import com.openbravo.data.user.SaveProvider;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.panels.JPanelTable;

/**
 *
 * @author adrianromero
 */
public class StockDiaryPanel extends JPanelTable {
    
    private StockDiaryEditor jeditor;
    
    private DataLogicSales m_dlSales = null;
    
    /** Creates a new instance of JPanelDiaryEditor */
    public StockDiaryPanel(AppView app) {
        super(app);
        
        try {
            m_dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");
        } catch (BeanFactoryException e) {
        }   

        jeditor = new StockDiaryEditor(app, m_Dirty);    
    }
    
    public ListProvider getListProvider() {
        return null;
    }
    
    public SaveProvider getSaveProvider() {
        return  new SaveProvider(null
                , m_dlSales.getStockDiaryInsert()
                , m_dlSales.getStockDiaryDelete());      
    }
    
    public EditorRecord getEditor() {
        return jeditor;
    }
    
    public String getTitle() {
        return AppLocal.getIntString("Menu.StockDiary");
    }     
    
        
    public void activate() throws BasicException {
        jeditor.activate(); // primero activo el editor 
        super.activate();   // segundo activo el padre        
    } 
}
