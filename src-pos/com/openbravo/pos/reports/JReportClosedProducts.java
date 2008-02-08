//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007 Openbravo, S.L.
//    http://sourceforge.net/projects/
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
import com.openbravo.data.user.EditorCreator;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.BaseSentence;
import com.openbravo.data.loader.QBFBuilder;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.Datas;

public class JReportClosedProducts extends JPanelReport {
    
    /** Creates a new instance of JReportClosedProducts */
    public JReportClosedProducts() {
    }
    
    public String getTitle() {
        return AppLocal.getIntString("Menu.ClosedProducts");
    } 
    protected String getReport() {
        return "/com/openbravo/pos/reports/closedproducts";
    }
    protected String getResourceBundle() {
        return "report_closedproducts_messages";
    }
    protected BaseSentence getSentence() {
       
         return new StaticSentence(m_App.getSession()
            , new QBFBuilder("SELECT " +
                "CLOSEDCASH.HOST, " +
                "CLOSEDCASH.MONEY, " +
                "CLOSEDCASH.DATESTART, " +
                "PRODUCTS.REFERENCE, " +
                "PRODUCTS.NAME, " +
                "SUM(TICKETLINES.UNITS) AS UNITS, " +
                "SUM(TICKETLINES.UNITS * TICKETLINES.PRICE) AS TOTAL " +
                "FROM CLOSEDCASH, RECEIPTS, TICKETS, TICKETLINES LEFT OUTER JOIN PRODUCTS ON TICKETLINES.PRODUCT = PRODUCTS.ID " +
                "WHERE CLOSEDCASH.MONEY = RECEIPTS.MONEY AND RECEIPTS.ID = TICKETS.ID AND TICKETS.ID = TICKETLINES.TICKET AND ?(QBF_FILTER) " +
                "GROUP BY CLOSEDCASH.HOST, CLOSEDCASH.MONEY, CLOSEDCASH.DATESTART, PRODUCTS.REFERENCE, PRODUCTS.NAME " +
                "ORDER BY PRODUCTS.NAME, CLOSEDCASH.HOST, CLOSEDCASH.DATESTART",  new String[] {"CLOSEDCASH.DATESTART", "CLOSEDCASH.DATESTART"})
            , new SerializerWriteBasic(new Datas[] {Datas.OBJECT, Datas.TIMESTAMP, Datas.OBJECT, Datas.TIMESTAMP})
            , new SerializerReadBasic(new Datas[] {Datas.STRING, Datas.STRING, Datas.TIMESTAMP, Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.DOUBLE}));            
    }
    protected ReportFields getReportFields() {
        return new ReportFieldsArray(new String[]{"HOST", "MONEY", "DATESTART", "REFERENCE", "NAME", "UNITS", "TOTAL"});  
    }
    protected EditorCreator createEditorCreator() {
        return new JParamsDatesInterval();
    }   
    
}
