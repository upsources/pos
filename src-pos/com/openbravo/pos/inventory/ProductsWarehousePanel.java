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

package com.openbravo.pos.inventory;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ListCellRenderer;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ListCellRendererBasic;
import com.openbravo.data.loader.ComparatorCreator;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.RenderStringBasic;
import com.openbravo.data.loader.SentenceExec;
import com.openbravo.data.loader.SentenceExecTransaction;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.SerializerWriteBasicExt;
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
import com.openbravo.pos.reports.JParamsLocation;

/**
 *
 * @author adrianromero
 */
public class ProductsWarehousePanel extends JPanelTable {

    private JParamsLocation m_paramslocation;
    
    private ProductsWarehouseEditor jeditor;
    private ListProvider lpr;
    private SaveProvider spr;
    
    /** Creates a new instance of ProductsWarehousePanel */
    public ProductsWarehousePanel() {
    }
    
    protected void init() {   
        DataLogicSales dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");
               
        m_paramslocation =  new JParamsLocation(dlSales);
        m_paramslocation.addActionListener(new ReloadActionListener());
        
        final Datas[] prodstock = new Datas[] {Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.DOUBLE, Datas.DOUBLE};

        lpr = new ListProviderCreator(new PreparedSentence(app.getSession(),
                "SELECT PRODUCTS.ID, PRODUCTS.REFERENCE, PRODUCTS.NAME, ?," +
                "S.STOCKSECURITY, S.STOCKMAXIMUM, COALESCE(S.UNITS, 0) " +
                "FROM PRODUCTS LEFT OUTER JOIN " +
                "(SELECT PRODUCT, LOCATION, STOCKSECURITY, STOCKMAXIMUM, UNITS FROM STOCKCURRENT WHERE LOCATION = ?) S " +
                "ON PRODUCTS.ID = S.PRODUCT ORDER BY PRODUCTS.NAME",
                new SerializerWriteBasicExt(new Datas[] {Datas.OBJECT, Datas.STRING}, new int[]{1, 1}),
                new SerializerReadBasic(prodstock)),
                m_paramslocation);
        
        
        SentenceExec updatesent =  new SentenceExecTransaction(app.getSession()) {
            public int execInTransaction(Object params) throws BasicException {
                if (new PreparedSentence(app.getSession()
                        , "UPDATE STOCKCURRENT SET STOCKSECURITY = ?, STOCKMAXIMUM = ? WHERE LOCATION = ? AND PRODUCT = ?"
                        , new SerializerWriteBasicExt(prodstock, new int[] {4, 5, 3, 0})).exec(params) == 0) {
                    return new PreparedSentence(app.getSession()
                        , "INSERT INTO STOCKCURRENT (LOCATION, PRODUCT, STOCKSECURITY, STOCKMAXIMUM, UNITS) VALUES (?, ?, ?, ?, 0)"
                        , new SerializerWriteBasicExt(prodstock, new int[] {3, 0, 4, 5})).exec(params);
                } else {
                    return 1;
                }
            }
        };     
        
        spr = new SaveProvider(updatesent, null, null);
         
        jeditor = new ProductsWarehouseEditor(dirty);   
    }    
    
    public ListProvider getListProvider() {
        return lpr;
    }
    
    public SaveProvider getSaveProvider() {
        return spr;        
    }    
    public Vectorer getVectorer() {
        return  new VectorerBasic(
                new String[] {AppLocal.getIntString("label.prodref"), AppLocal.getIntString("label.prodbarcode")},
                new Formats[] {Formats.STRING, Formats.STRING},
                new int[] {1, 2});
    }
    
    public ComparatorCreator getComparatorCreator() {
        return null;
    }
    
    public ListCellRenderer getListCellRenderer() {
        return new ListCellRendererBasic(new RenderStringBasic(new Formats[] {Formats.STRING, Formats.STRING, Formats.STRING}, new int[]{1, 2}));
    }
    
    public Component getFilter() {
        return m_paramslocation;
    }  
    
    public EditorRecord getEditor() {
        return jeditor;
    }  
    
    public void activate() throws BasicException {
        
        m_paramslocation.activate(); 
        super.activate();
    }     
    
    public String getTitle() {
        return AppLocal.getIntString("Menu.ProductsWarehouse");
    }      
    
    private class ReloadActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                ProductsWarehousePanel.this.bd.actionLoad();
            } catch (BasicException w) {
            }
        }
    }
}
