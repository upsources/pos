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

import com.openbravo.data.loader.BaseSentence;
import com.openbravo.data.loader.SerializerReadClass;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.ticket.ProductInfoReport;

public class JReportCatalog extends JPanelReport {
    
    /** Creates a new instance of JReportCatalog */
    public JReportCatalog() {
    }   
    
    public String getTitle() {
        return AppLocal.getIntString("Menu.Catalog");
    }   
    protected String getReport() {
        return "/com/openbravo/pos/reports/products";
    }
    protected String getResourceBundle() {
        return "report_catalog_messages";
    }
    protected BaseSentence getSentence() {
        return new StaticSentence(m_App.getSession()
            , "SELECT PRODUCTS.ID, PRODUCTS.REFERENCE, PRODUCTS.CODE, PRODUCTS.NAME, PRODUCTS.ISCOM, PRODUCTS.ISSCALE, PRODUCTS.PRICEBUY, PRODUCTS.PRICESELL, PRODUCTS.TAX, TAXES.NAME AS TAXNAME, TAXES.RATE AS TAXRATE, PRODUCTS.CATEGORY, CATEGORIES.NAME AS CATEGORYNAME " +
              "FROM PRODUCTS_CAT, PRODUCTS LEFT OUTER JOIN CATEGORIES ON PRODUCTS.CATEGORY = CATEGORIES.ID LEFT OUTER JOIN TAXES ON PRODUCTS.TAX = TAXES.ID " +
              "WHERE PRODUCTS_CAT.PRODUCT = PRODUCTS.ID " +
              "ORDER BY CATEGORIES.NAME, PRODUCTS_CAT.CATORDER, PRODUCTS.NAME"
            , null
            , new SerializerReadClass(ProductInfoReport.class));
    }
    protected ReportFields getReportFields() {
        return ReportFieldsBuilder.INSTANCE;
    }    
}
