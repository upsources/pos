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
import com.openbravo.data.loader.TableDefinition;
import com.openbravo.data.loader.Vectorer;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.data.user.ListProvider;
import com.openbravo.data.user.ListProviderCreator;
import com.openbravo.data.user.SaveProvider;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.panels.JPanelTable;
import com.openbravo.pos.panels.AuxiliarFilter;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ListCellRenderer;

/**
 *
 * @author jaroslawwozniak
 */
public class AuxiliarPanel extends JPanelTable{

    private AuxiliarEditor jeditor;
    private TableDefinition tAuxiliar;
    private DataLogicSales dlSales;
    private AuxiliarFilter filter;
    private ListProviderCreator lpr;

    protected void init() {
        dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");             
        filter = new AuxiliarFilter();
        filter.addActionListener(new ReloadActionListener());
        filter.init(app);
        tAuxiliar = dlSales.getTableAuxiliar();
        jeditor = new AuxiliarEditor(app, dirty, filter);
        filter.forwardEditor(jeditor);     
    }

    @Override
    public void activate() throws BasicException {
        jeditor.activate();
        filter.activate();
        super.activate();
    }

    @Override
    public EditorRecord getEditor() {
        return jeditor;
    }

    @Override
    public ListProvider getListProvider() {
        return  new ListProviderCreator(dlSales.getAuxiliarList(), filter);
    }

    @Override
    public SaveProvider getSaveProvider() {
        return new SaveProvider(dlSales.getAuxiliarInsert()
                            , dlSales.getAuxiliarInsert()
                            , dlSales.getAuxiliarDelete());
    }

    @Override
    public String getTitle() {
        return AppLocal.getIntString("Menu.Auxiliar");
    }

    @Override
    public Vectorer getVectorer() {
        return tAuxiliar.getVectorerBasic(new int[]{1});
    }

    @Override
    public ComparatorCreator getComparatorCreator() {
        return tAuxiliar.getComparatorCreator(new int[] {1});
    }

    @Override
    public ListCellRenderer getListCellRenderer() {
        return new ListCellRendererBasic(tAuxiliar.getRenderStringBasic(new int[]{0}));
    }

    @Override
    public Component getFilter(){
        return filter.getComponent();
    }

    private class ReloadActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                AuxiliarPanel.this.bd.actionLoad();
            } catch (BasicException w) {
            }
        }
    }

    public void refresh(){
    }
}
