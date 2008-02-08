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

import javax.swing.ListCellRenderer;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ListCellRendererBasic;
import com.openbravo.data.loader.ComparatorCreator;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.panels.*;
import com.openbravo.pos.forms.AppView;
import com.openbravo.data.loader.TableDefinition;
import com.openbravo.data.loader.Vectorer;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.data.user.SaveProvider;
import com.openbravo.data.user.ListProvider;
import com.openbravo.data.user.ListProviderCreator;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.forms.DataLogicSales;

/**
 *
 * @author adrianromero
 */
public class CategoriesPanel extends JPanelTable {
    
    private TableDefinition tcategories;
    private CategoriesEditor jeditor;
    
    /** Creates a new instance of JPanelCategories */
    public CategoriesPanel(AppView app) {
        super(app);
        
        DataLogicSales dlSales = null;
        try {
            dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");
        } catch (BeanFactoryException e) {
        }    
        
        tcategories = dlSales.getTableCategories();
        jeditor = new CategoriesEditor(app, m_Dirty);    
    }   
    
    public void activate() throws BasicException { 
        
        jeditor.activate();         
        super.activate();
    }
    
    public ListProvider getListProvider() {
        return new ListProviderCreator(tcategories);
    }
    
    public SaveProvider getSaveProvider() {
        return new SaveProvider(tcategories);      
    }
    
    public Vectorer getVectorer() {
        return tcategories.getVectorerBasic(new int[]{1});
    }
    
    public ComparatorCreator getComparatorCreator() {
        return tcategories.getComparatorCreator(new int[]{1});
    }
    
    public ListCellRenderer getListCellRenderer() {
        return new ListCellRendererBasic(tcategories.getRenderStringBasic(new int[]{1}));
    }
    
    public EditorRecord getEditor() {
        return jeditor;
    }
    
    public String getTitle() {
        return AppLocal.getIntString("Menu.Categories");
    }        
}
