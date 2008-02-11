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

package com.openbravo.pos.ticket;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.reports.ReportException;
import com.openbravo.pos.reports.ReportObject;

public class ProductInfoReport extends ProductInfo implements ReportObject {
    
    protected String m_sCategoryName;
   
    /** Creates a new instance of ProductInfoReport */
    public ProductInfoReport() {
        super();
        m_sCategoryName = null;
    }
    
    public void readValues(DataRead dr) throws BasicException {
        m_ID = dr.getString(1);
        m_sRef = dr.getString(2);
        m_sCode = dr.getString(3);
        m_sName = dr.getString(4);
        m_bCom = dr.getBoolean(5).booleanValue();
        m_bScale = dr.getBoolean(6).booleanValue();
        m_dPriceBuy = dr.getDouble(7).doubleValue();
        m_dPriceSell = dr.getDouble(8).doubleValue();
        m_TaxInfo = new TaxInfo(dr.getString(9), dr.getString(10), dr.getDouble(11).doubleValue());      
        m_sCategoryID = dr.getString(12);
        m_sCategoryName = dr.getString(13);
    }
    
    public Object getField(String field) throws ReportException {

        if ("ID".equals(field)) {
            return m_ID;
        } else if ("REFERENCE".equals(field)) {
            return m_sRef;
        } else if ("CODE".equals(field)) {
            return m_sCode;
        } else if ("NAME".equals(field)) {
            return m_sName;
        } else if ("PRICEBUY".equals(field)) {
            return new Double(m_dPriceBuy);
        } else if ("PRICESELL".equals(field)) {
            return new Double(m_dPriceSell);
        } else if ("PRICESELLTAX".equals(field)) {
            return new Double(getPriceSellTax());
        } else if ("TAXNAME".equals(field)) {
            return getTaxName();
        } else if ("TAXRATE".equals(field)) {
            return new Double(getTaxRate());
        } else if ("CATEGORY".equals(field)) {
            return m_sCategoryID;
        } else if ("CATEGORYNAME".equals(field)) {
            return m_sCategoryName;
        } else {
            throw new ReportException(AppLocal.getIntString("exception.unavailablefields"));
        }
    }
    
    public final String getCategoryName() {            
        return m_sCategoryName;
    }
    public final void setCategoryName(String sCategoryName){            
        m_sCategoryName = sCategoryName;
    }    
}
