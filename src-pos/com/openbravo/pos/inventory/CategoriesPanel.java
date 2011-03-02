//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2008 Openbravo, S.L.
//    http://sourceforge.net/projects/openbravopos
//
//    This file is modified as part of fastfood branch of Openbravo POS. 2008
//    These modifications are copyright Open Sistemas de Información Internet, S.L.
//    http://www.opensistemas.com
//    e-mail: imasd@opensistemas.com
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
 * Modified by:
 * @author  Luis Ig. Bacas Riveiro	lbacas@opensistemas.com
 * @author  Pablo J. Urbano Santos	purbano@opensistemas.com
 */
public class CategoriesPanel extends JPanelTable {
    
    private TableDefinition tcategories;
    private CategoriesEditor jeditor;
    
    private DataLogicSales m_dlSales;
    
    /** Creates a new instance of JPanelCategories */
    public CategoriesPanel() {
    }   
    
    @Override
    protected void init() {
        m_dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");           
        tcategories = m_dlSales.getTableCategories();
        jeditor = new CategoriesEditor(app, dirty);    
    }
    
    @Override
    public void activate() throws BasicException { 
        jeditor.activate();         
        super.activate();
    }
    
    public ListProvider getListProvider() {
        return new ListProviderCreator(m_dlSales.getCategories(), null);
    }
    
    public SaveProvider getSaveProvider() {
        return new SaveProvider(tcategories);
    }
    
    @Override
    public Vectorer getVectorer() {
        return tcategories.getVectorerBasic(new int[]{1});
    }
    
    @Override
    public ComparatorCreator getComparatorCreator() {
        return tcategories.getComparatorCreator(new int[]{1});
    }
    
    @Override
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
