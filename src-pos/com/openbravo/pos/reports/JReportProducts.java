//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007 Openbravo, S.L.
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
import com.openbravo.data.loader.BaseSentence;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.QBFBuilder;
import com.openbravo.data.loader.SerializerReadClass;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.user.EditorCreator;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.ticket.ProductFilter;
import com.openbravo.pos.ticket.ProductInfoReport;

public class JReportProducts extends JPanelReport {
    
    private ProductFilter m_productfilter;

    private DataLogicSales m_dlSales = null;
    
    /** Creates a new instance of JReportProducts */
    public JReportProducts() {
    }
    
    public void init(AppView app) throws BeanFactoryException {   
        
        try {
            m_dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");
        } catch (BeanFactoryException e) {
        } 
        super.init(app);
    }    
    
    public void activate() throws BasicException {
        m_productfilter.activate();
        super.activate();
    }              
    public String getTitle() {
        return AppLocal.getIntString("Menu.Products");
    }      
    protected String getReport() {
        return "/com/openbravo/pos/reports/products";
    }
    protected String getResourceBundle() {
        return "report_products_messages";
    }
    protected BaseSentence getSentence() {
        return new StaticSentence(m_App.getSession()
            , new QBFBuilder(
              "SELECT PRODUCTS.ID, PRODUCTS.REFERENCE, PRODUCTS.CODE, PRODUCTS.NAME, PRODUCTS.ISCOM, PRODUCTS.ISSCALE, PRODUCTS.PRICEBUY, PRODUCTS.PRICESELL, PRODUCTS.TAX, TAXES.NAME AS TAXNAME, TAXES.RATE AS TAXRATE, PRODUCTS.CATEGORY, CATEGORIES.NAME AS CATEGORYNAME " +
              "FROM PRODUCTS LEFT OUTER JOIN CATEGORIES ON PRODUCTS.CATEGORY = CATEGORIES.ID LEFT OUTER JOIN TAXES ON PRODUCTS.TAX = TAXES.ID " +
              "WHERE ?(QBF_FILTER) " +
              "ORDER BY CATEGORIES.NAME, PRODUCTS.NAME",
               new String[] {"PRODUCTS.NAME", "PRODUCTS.PRICEBUY", "PRODUCTS.PRICESELL", "PRODUCTS.CATEGORY", "PRODUCTS.CODE"} )
            , new SerializerWriteBasic(new Datas[] {Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.STRING})
            , new SerializerReadClass(ProductInfoReport.class));
    }
    protected ReportFields getReportFields() {
        return ReportFieldsBuilder.INSTANCE;
    }    
    
    protected EditorCreator createEditorCreator() {
        m_productfilter =  new ProductFilter(m_dlSales) ;
        return m_productfilter;
    }         
}
