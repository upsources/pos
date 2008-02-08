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

package com.openbravo.pos.admin;

import javax.swing.ListCellRenderer;
import com.openbravo.data.gui.ListCellRendererBasic;
import com.openbravo.data.loader.ComparatorCreator;
import com.openbravo.pos.forms.*;
import com.openbravo.pos.panels.*;
import com.openbravo.data.loader.TableDefinition;
import com.openbravo.data.loader.Vectorer;
import com.openbravo.data.user.*;

/**
 *
 * @author adrianromero
 */
public class ResourcesPanel extends JPanelTable {

    private TableDefinition tresources;
    private ResourcesView jeditor;
    
    /** Creates a new instance of JPanelResources */
    public ResourcesPanel(AppView app) {
        super(app);
        
        DataLogicAdmin dlAdmin = null;
        try {
            dlAdmin = (DataLogicAdmin) app.getBean("com.openbravo.pos.admin.DataLogicAdmin");
        } catch (BeanFactoryException e) {
        }
                
        tresources = dlAdmin.getTableResources();         
        jeditor = new ResourcesView(m_Dirty);    
    }
    
    public ListProvider getListProvider() {
        return new ListProviderCreator(tresources);
    }
    
    public SaveProvider getSaveProvider() {
        return new SaveProvider(tresources);        
    }
    
    public Vectorer getVectorer() {
        return tresources.getVectorerBasic(new int[] {1});
    }
    
    public ComparatorCreator getComparatorCreator() {
        return tresources.getComparatorCreator(new int[] {1, 2});
    }
    
    public ListCellRenderer getListCellRenderer() {
        return new ListCellRendererBasic(tresources.getRenderStringBasic(new int[] {1}));
    }
    
    public EditorRecord getEditor() {
        return jeditor;
    }
    
    public String getTitle() {
        return AppLocal.getIntString("Menu.Resources");
    }        
}
