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
//    Foundation, Inc., 51 Franklin Street, Fifth floor, Boston, MA  02110-1301  USA

package com.openbravo.pos.inventory;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ListCellRendererBasic;
import com.openbravo.data.loader.ComparatorCreator;
import com.openbravo.data.loader.ComparatorCreatorBasic;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.RenderStringBasic;
import com.openbravo.data.loader.Vectorer;
import com.openbravo.data.loader.VectorerBasic;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.data.user.ListProvider;
import com.openbravo.data.user.ListProviderCreator;
import com.openbravo.data.user.SaveProvider;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.panels.JPanelTable;
import com.openbravo.pos.panels.AuxiliarFilter;
import com.openbravo.pos.ticket.ProductInfoExt;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ListCellRenderer;

/**
 *
 * @author jaroslawwozniak
 */
public class AuxiliarPanel extends JPanelTable {

    private AuxiliarEditor jeditor;
    private DataLogicSales dlSales;
    private AuxiliarFilter filter;
    private ListProviderCreator lpr;
    private SaveProvider spr;

    protected void init() {
        
        dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");    
        
        filter = new AuxiliarFilter();
        filter.init(app);
        filter.addActionListener(new ReloadActionListener());
      
        lpr = new ListProviderCreator(dlSales.getAuxiliarList(), filter);     
        spr = new SaveProvider(dlSales.getAuxiliarUpdate()
                              , dlSales.getAuxiliarInsert()
                              , dlSales.getAuxiliarDelete());
        
        jeditor = new AuxiliarEditor(app, dirty);
    }

    @Override
    public void activate() throws BasicException {
        filter.activate();
        
        //super.activate();
        startNavigation();
        reload(filter);
    }

    @Override
    public EditorRecord getEditor() {
        return jeditor;
    }

    @Override
    public ListProvider getListProvider() {
        return lpr;
    }

    @Override
    public SaveProvider getSaveProvider() {
        return spr;
    }

    @Override
    public String getTitle() {
        return AppLocal.getIntString("Menu.Auxiliar");
    }

    @Override
    public Vectorer getVectorer() {
        return new VectorerBasic(
                new String[] {"ID", "PRODUCT1", "PRODUCT2", AppLocal.getIntString("label.prodref"), AppLocal.getIntString("label.prodbarcode"), AppLocal.getIntString("label.prodname")},
                new Formats[] {Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING}, 
                new int[]{3, 4, 5}); 
    }

    @Override
    public ComparatorCreator getComparatorCreator() {
        return new ComparatorCreatorBasic(
                new String[] {"ID", "PRODUCT1", "PRODUCT2", AppLocal.getIntString("label.prodref"), AppLocal.getIntString("label.prodbarcode"), AppLocal.getIntString("label.prodname")},
                new Datas[] {Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING}, 
                new int[]{3, 4, 5});
    }

    @Override
    public ListCellRenderer getListCellRenderer() {
        return new ListCellRendererBasic(new RenderStringBasic(
                new Formats[] {Formats.STRING,Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING},
                new int[]{3, 5}));

    }

    @Override
    public Component getFilter(){
        return filter.getComponent();
    }

    private void reload(AuxiliarFilter filter) throws BasicException {
        ProductInfoExt prod = filter.getProductInfoExt();
        bd.setEditable(prod != null);
        bd.actionLoad();
        jeditor.setInsertProduct(prod);        
    }
            
    private class ReloadActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                reload((AuxiliarFilter) e.getSource());
            } catch (BasicException w) {
            }
        }
    }
}
