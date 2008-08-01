//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2008 Openbravo, S.L.
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

package com.openbravo.pos.inventory;

import com.openbravo.pos.panels.*;
import javax.swing.ListCellRenderer;
import com.openbravo.data.gui.ListCellRendererBasic;
import com.openbravo.data.loader.ComparatorCreator;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.data.loader.TableDefinition;
import com.openbravo.data.loader.Vectorer;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.data.user.SaveProvider;
import com.openbravo.data.user.ListProvider;
import com.openbravo.data.user.ListProviderCreator;
import com.openbravo.pos.forms.DataLogicSales;

/**
 *
 * @author adrianromero
 */
public class TaxCustCategoriesPanel extends JPanelTable {

    private TableDefinition ttaxcategories;
    private TaxCustCategoriesEditor jeditor;
    
    /** Creates a new instance of JPanelDuty */
    public TaxCustCategoriesPanel() {
    }
    
    protected void init() {
        DataLogicSales dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");        
        ttaxcategories = dlSales.getTableTaxCustCategories();
        jeditor = new TaxCustCategoriesEditor(dirty);
    }
    
    public ListProvider getListProvider() {
        return new ListProviderCreator(ttaxcategories);
    }
    
    public SaveProvider getSaveProvider() {
        return new SaveProvider(ttaxcategories);      
    }
    
    @Override
    public Vectorer getVectorer() {
        return ttaxcategories.getVectorerBasic(new int[]{1});
    }
    
    @Override
    public ComparatorCreator getComparatorCreator() {
        return ttaxcategories.getComparatorCreator(new int[] {1});
    }
    
    @Override
    public ListCellRenderer getListCellRenderer() {
        return new ListCellRendererBasic(ttaxcategories.getRenderStringBasic(new int[]{1}));
    }
    
    public EditorRecord getEditor() {
        return jeditor;
    }
        
    public String getTitle() {
        return AppLocal.getIntString("Menu.TaxCustCategories");
    }     
}
