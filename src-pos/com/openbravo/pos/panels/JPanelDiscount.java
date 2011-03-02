//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2008 Open Sistemas de Información Internet, S.L.
//    http://www.opensistemas.com
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

package com.openbravo.pos.panels;
import com.openbravo.pos.inventory.DiscountEditor;
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
 * Created on April 2, 2008
 * Modified by:
 * @author  Luis Ig. Bacas Riveiro	lbacas@opensistemas.com
 * @author  Pablo J. Urbano Santos	purbano@opensistemas.com
 */
public class JPanelDiscount extends JPanelTable {

    private TableDefinition tdiscounts;
    private DiscountEditor jeditor;
    
    /** Creates a new instance of JPanelDuty */
    public JPanelDiscount() {
    }
    
    @Override
    protected void init() {
        DataLogicSales dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");
        
        tdiscounts = dlSales.getTableDiscounts();
        jeditor = new DiscountEditor(dirty);    
    }
    
    public ListProvider getListProvider() {
        return new ListProviderCreator(tdiscounts);
    }
    
    public SaveProvider getSaveProvider() {
        return new SaveProvider(tdiscounts);      
    }
    
    @Override
    public Vectorer getVectorer() {
        return tdiscounts.getVectorerBasic(new int[]{1, 2});
    }
    
    @Override
    public ComparatorCreator getComparatorCreator() {
        return tdiscounts.getComparatorCreator(new int[] {1, 2});
    }
    
    @Override
    public ListCellRenderer getListCellRenderer() {
        return new ListCellRendererBasic(tdiscounts.getRenderStringBasic(new int[]{1}));
    }
    
    public EditorRecord getEditor() {
        return jeditor;
    }
        
    public String getTitle() {
        return AppLocal.getIntString("Menu.Discounts");
    }     
}
