//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2008 Openbravo, S.L.
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

package com.openbravo.pos.admin;

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

/**
 *
 * @author adrianromero
 */
public class PeoplePanel extends JPanelTable {
    
    private TableDefinition tpeople;
    private PeopleView jeditor;
    
    /** Creates a new instance of JPanelPeople */
    public PeoplePanel() {
    }
    
    protected void init() {      
        DataLogicAdmin dlAdmin = (DataLogicAdmin) app.getBean("com.openbravo.pos.admin.DataLogicAdmin");        
        tpeople = dlAdmin.getTablePeople();           
        jeditor = new PeopleView(dlAdmin, dirty);    
    }
    
    public ListProvider getListProvider() {
        return new ListProviderCreator(tpeople);
    }
    
    public SaveProvider getSaveProvider() {
        return new SaveProvider(tpeople);        
    }
    
    public Vectorer getVectorer() {
        return tpeople.getVectorerBasic(new int[]{1});
    }
    
    public ComparatorCreator getComparatorCreator() {
        return tpeople.getComparatorCreator(new int[] {1, 3});
    }
    
    public ListCellRenderer getListCellRenderer() {
        return new ListCellRendererBasic(tpeople.getRenderStringBasic(new int[]{1}));
    }
    
    public EditorRecord getEditor() {
        return jeditor;
    }
    
    public void activate() throws BasicException {
        
        jeditor.activate(); // primero el editor    
        super.activate(); // y luego cargamos los datos
    }      
    public String getTitle() {
        return AppLocal.getIntString("Menu.Users");
    }     
}
