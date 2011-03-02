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

package com.openbravo.pos.inventory;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.ComparatorCreator;
import com.openbravo.data.loader.Vectorer;
import com.openbravo.data.user.EditorListener;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.data.user.ListProvider;
import com.openbravo.data.user.ListProviderCreator;
import com.openbravo.data.user.SaveProvider;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.panels.JPanelTable;

/**
 *
 * @author  Luis Ig. Bacas Riveiro	lbacas@opensistemas.com
 * @author  Pablo J. Urbano Santos	purbano@opensistemas.com
 * Created on April 16, 2008
 */
public class TariffAreasPanel extends JPanelTable implements EditorListener {
    private DataLogicSales m_dlSales = null;
    private TariffAreasEditor jeditor;
    
    /** Creates a new instance of CompositionsPanel */
    public TariffAreasPanel() {
    }
    
    @Override
    protected void init() {
        m_dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");
        
        // el panel del editor
        jeditor = new TariffAreasEditor(m_dlSales, dirty);       
    }

    public ListProvider getListProvider() {
        return new ListProviderCreator(m_dlSales.getTableTariffAreas());
    }

    //TODO cambiar
    public SaveProvider getSaveProvider() {
        return new SaveProvider(
            m_dlSales.getTariffAreaUpdate(), 
            m_dlSales.getTariffAreaInsert(), 
            m_dlSales.getTariffAreaDelete());        
    }
    
    @Override
    public Vectorer getVectorer() {
        return jeditor.getTableDefinition().getVectorerBasic(new int[]{1, 2});
    }
    
    @Override
    public ComparatorCreator getComparatorCreator() {
        return jeditor.getTableDefinition().getComparatorCreator(new int[] {1, 2});
    }

    public EditorRecord getEditor() {
        return jeditor;
    }
    
    @Override
    public void activate() throws BasicException {
        super.activate();
        jeditor.activate(bd); 
    } 
    
    public void updateValue(Object value) {
    }    
    
    public String getTitle() {
        return AppLocal.getIntString("Menu.TariffArea");
    } 
} 
