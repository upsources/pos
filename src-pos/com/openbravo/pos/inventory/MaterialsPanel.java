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

import java.awt.Component;
import javax.swing.ListCellRenderer;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ListCellRendererBasic;
import com.openbravo.data.loader.ComparatorCreator;
import com.openbravo.data.loader.ComparatorCreatorBasic;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.RenderStringBasic;
import com.openbravo.data.loader.Vectorer;
import com.openbravo.data.loader.VectorerBasic;
import com.openbravo.data.user.EditorListener;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.data.user.ListProvider;
import com.openbravo.data.user.ListProviderCreator;
import com.openbravo.data.user.SaveProvider;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.panels.JPanelTable;

/**
 * Created on April, 2008
 * @author  Luis Ig. Bacas Riveiro	lbacas@opensistemas.com
 * @author  Pablo J. Urbano Santos	purbano@opensistemas.com
 */
public class MaterialsPanel extends JPanelTable implements EditorListener {
    private MaterialsEditor jeditor;
    private MaterialFilter jfilter;    
    
    private DataLogicSales m_dlSales = null;
    
    /** Creates a new instance of MaterialPanel */
    public MaterialsPanel() {
    }
        
    @Override
    protected void init() {   
        m_dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");

        // el panel del filtro
        jfilter = new MaterialFilter(m_dlSales);
        // el panel del editor
        jeditor = new MaterialsEditor(m_dlSales, dirty);       
    }
    
    public ListProvider getListProvider() {
        return new ListProviderCreator(m_dlSales.getMaterialQBF(), jfilter);

    }

    public SaveProvider getSaveProvider() {
        return new SaveProvider(
            m_dlSales.getMaterialUpdate(),
            m_dlSales.getMaterialInsert(),
            m_dlSales.getMaterialDelete());        
    }

    @Override
    public Vectorer getVectorer() {
        return  new VectorerBasic(
                new String[]{"ID", AppLocal.getIntString("label.prodref"), AppLocal.getIntString("label.prodbarcode"), AppLocal.getIntString("label.prodname"), "ISCOM", "ISSCALE", AppLocal.getIntString("label.prodpricebuy"), AppLocal.getIntString("label.prodpricesell"), AppLocal.getIntString("label.prodcategory"), AppLocal.getIntString("label.prodtax"), "IMAGE", "STOCKCOST", "STOCKVOLUME"},
                new Formats[] {Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.BOOLEAN, Formats.BOOLEAN, Formats.CURRENCY, Formats.CURRENCY, Formats.STRING, Formats.STRING, Formats.NULL, Formats.CURRENCY, Formats.DOUBLE},
                new int[] {3, 6});
    }
    
    @Override
    public ComparatorCreator getComparatorCreator() {
        return new ComparatorCreatorBasic(
                new String[]{"ID", AppLocal.getIntString("label.prodref"), AppLocal.getIntString("label.prodbarcode"), AppLocal.getIntString("label.prodname"), "ISCOM", "ISSCALE", AppLocal.getIntString("label.prodpricebuy"), AppLocal.getIntString("label.prodpricesell"), AppLocal.getIntString("label.prodcategory"), AppLocal.getIntString("label.prodtax"), "IMAGE", "STOCKCOST", "STOCKVOLUME"},
                new Datas[]{Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.BOOLEAN, Datas.BOOLEAN, Datas.DOUBLE, Datas.DOUBLE, Datas.STRING, Datas.STRING, Datas.IMAGE, Datas.DOUBLE, Datas.DOUBLE, Datas.BOOLEAN, Datas.INT}, 
                new int[]{3});
    }

    @Override
    public ListCellRenderer getListCellRenderer() {
        return new ListCellRendererBasic(new RenderStringBasic(new Formats[] {Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING}, new int[]{3}));
    }
    
    public EditorRecord getEditor() {
        return jeditor;
    }
    
    @Override
    public Component getFilter() {
        return jfilter;
    }  

    public String getTitle() {
        return AppLocal.getIntString("Menu.Materials");
    } 
    
    @Override
    public void activate() throws BasicException {
        jeditor.activate(); 
        jfilter.activate();
        
        super.activate();
    } 
    
    public void updateValue(Object value) {
    }    
}
