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

import com.openbravo.data.loader.BaseSentence;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.QBFBuilder;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.user.EditorCreator;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;

public class JChartSales extends JPanelReport {
    
    /** Creates a new instance of JChartSales */
    public JChartSales() {
    }
    
    public String getTitle() {
        return AppLocal.getIntString("Menu.SalesChart");
    } 
    protected String getReport() {
        return "/com/openbravo/pos/reports/chartsales";
    }
    protected String getResourceBundle() {
        return "report_chartsales_messages";
    }
    protected BaseSentence getSentence() {
        return new StaticSentence(m_App.getSession()      
            , new QBFBuilder(
                "SELECT CLOSEDCASH.HOST, PEOPLE.ID, PEOPLE.NAME, SUM(TICKETLINES.UNITS * TICKETLINES.PRICE) AS TOTAL " +
                "FROM CLOSEDCASH, RECEIPTS, TICKETS, PEOPLE, TICKETLINES " +
                "WHERE CLOSEDCASH.MONEY = RECEIPTS.MONEY AND RECEIPTS.ID = TICKETS.ID AND TICKETS.ID = TICKETLINES.TICKET AND TICKETS.PERSON = PEOPLE.ID AND ?(QBF_FILTER) " +
                "GROUP BY CLOSEDCASH.HOST, PEOPLE.ID, PEOPLE.NAME",  new String[] {"TICKETS.DATENEW", "TICKETS.DATENEW"})          
            , new SerializerWriteBasic(new Datas[] {Datas.OBJECT, Datas.TIMESTAMP, Datas.OBJECT, Datas.TIMESTAMP})
            , new SerializerReadBasic(new Datas[] {Datas.STRING, Datas.STRING, Datas.STRING, Datas.DOUBLE}));
    }
    protected ReportFields getReportFields() {
        return new ReportFieldsArray(new String[]{"HOST", "PERSON", "NAME", "TOTAL"});
    }
    protected EditorCreator createEditorCreator() {
        return new JParamsDatesInterval();
    }     
}
