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

package com.openbravo.pos.reports;
import com.openbravo.basic.BasicException;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.loader.BaseSentence;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.QBFBuilder;
import com.openbravo.data.loader.SerializerWriteBasicComposed;
import com.openbravo.data.user.EditorCreator;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.ticket.ProductFilter;

public class JReportInventory extends JPanelReport {
    
    private ProductFilter m_productfilter;
    private JParamsLocation m_paramslocation;
    private JParamsComposed m_params;

    private DataLogicSales m_dlSales = null;

    /** Creates a new instance of JReportInventory */
    public JReportInventory() {
    }
    
    public void init(AppView app) throws BeanFactoryException {   

        m_dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");
        super.init(app);
    }
    
    public void activate() throws BasicException {
        m_productfilter.activate();
        m_paramslocation.activate();
        super.activate();
    }           
    public String getTitle() {
        return AppLocal.getIntString("Menu.Inventory");
    }   
    protected String getReport() {
        return "/com/openbravo/pos/reports/inventory";
    }
    protected String getResourceBundle() {
        return "report_inventory_messages";
    }
    protected BaseSentence getSentence() {
        return new StaticSentence(m_App.getSession(),
            new QBFBuilder(
                "SELECT " +
                "STOCKCURRENT.LOCATION AS LOCATIONID, " +
                "LOCATIONS.NAME AS LOCATIONNAME, " +
                "PRODUCTS.REFERENCE, " +
                "PRODUCTS.NAME AS NAME, " +
                "PRODUCTS.CATEGORY, " +
                "CATEGORIES.NAME AS CATEGORYNAME, " +
                "STOCKCURRENT.UNITS, " +
                "PRODUCTS.PRICEBUY, " +
                "PRODUCTS.PRICESELL, " +
                "COALESCE(PRODUCTS.STOCKVOLUME, 0) AS STOCKVOLUME, " +
                "COALESCE(PRODUCTS.STOCKCOST, 0) AS STOCKCOST, " +
                "COALESCE(STOCKCURRENT.STOCKSECURITY, 0) AS STOCKSECURITY, " +
                "COALESCE(STOCKCURRENT.STOCKMAXIMUM, 0) AS STOCKMAXIMUM " +
                "FROM STOCKCURRENT " +
                "JOIN LOCATIONS ON STOCKCURRENT.LOCATION = LOCATIONS.ID " +
                "JOIN PRODUCTS ON STOCKCURRENT.PRODUCT = PRODUCTS.ID " +
                "JOIN CATEGORIES ON PRODUCTS.CATEGORY = CATEGORIES.ID " +
                "WHERE ?(QBF_FILTER) " +
                "ORDER BY STOCKCURRENT.LOCATION, CATEGORIES.NAME, PRODUCTS.NAME"
                , new String[] {
                        "PRODUCTS.NAME", "PRODUCTS.PRICEBUY", "PRODUCTS.PRICESELL", "PRODUCTS.CATEGORY", "PRODUCTS.CODE", 
                        "STOCKCURRENT.LOCATION"})
            , new SerializerWriteBasicComposed(
                new Datas[] {Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.STRING}, 
                new Datas[] {Datas.OBJECT, Datas.STRING})
            , new SerializerReadBasic(new Datas[] {Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.DOUBLE, Datas.DOUBLE, Datas.DOUBLE, Datas.DOUBLE, Datas.DOUBLE, Datas.DOUBLE}));
    }
    protected ReportFields getReportFields() {
        return new ReportFieldsArray(new String[]{"LOCATIONID", "LOCATIONNAME", "REFERENCE", "NAME", "CATEGORY", "CATEGORYNAME", "UNITS", "PRICEBUY", "PRICESELL", "STOCKVOLUME", "STOCKCOST", "STOCKSECURITY", "STOCKMAXIMUM"});
    }
    protected EditorCreator createEditorCreator() {

        m_paramslocation =  new JParamsLocationWithFirst(m_dlSales);
        m_productfilter =  new ProductFilter(m_dlSales);
        
        m_params = new JParamsComposed(m_productfilter, m_paramslocation);
        m_params.add(m_productfilter);
        m_params.add(m_paramslocation);
        
        return m_params;
    }    
}
