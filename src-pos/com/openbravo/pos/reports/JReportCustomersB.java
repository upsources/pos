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

import com.openbravo.data.loader.BaseSentence;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.pos.forms.AppLocal;

/**
 *
 * @author adrianromero
 */
public class JReportCustomersB extends JPanelReport {
    
    /** Creates a new instance of JReportCustomers2 */
    public JReportCustomersB() {
    }    
               
    public String getTitle() {
        return AppLocal.getIntString("Menu.CustomersBReport");
    }      
    
    protected String getReport() {
        return "/com/openbravo/pos/reports/customers";
    }
    protected String getResourceBundle() {
        return "report_customers_messages";
    }
    protected BaseSentence getSentence() {
        return new StaticSentence(m_App.getSession()
            ,   "SELECT ID, NAME, ADDRESS, NOTES, CARD, MAXDEBT, CURDATE, CURDEBT " +
                "FROM CUSTOMERS " +
                "WHERE VISIBLE = TRUE AND CURDEBT IS NOT NULL AND CURDEBT > 0"
            , null
            , new SerializerReadBasic(new Datas[] {Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.TIMESTAMP, Datas.DOUBLE}));
    }
    protected ReportFields getReportFields() {
        return new ReportFieldsArray(new String[]{"ID", "NAME", "ADDRESS", "NOTES", "CARD", "MAXDEBT", "CURDATE", "CURDEBT"});
    }          
}
