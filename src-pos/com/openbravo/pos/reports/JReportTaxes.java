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
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.QBFBuilder;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.user.EditorCreator;
import com.openbravo.pos.forms.AppLocal;

public class JReportTaxes extends JPanelReport {
    
    /** Creates a new instance of JReportTaxes */
    public JReportTaxes() {
    }
    
    public String getTitle() {
        return AppLocal.getIntString("Menu.ReportTaxes");
    } 
    protected String getReport() {
        return "/com/openbravo/pos/reports/taxes";
    }
    protected String getResourceBundle() {
        return "report_taxes_messages";
    }
    protected BaseSentence getSentence() {
        return new StaticSentence(m_App.getSession()
            , new QBFBuilder(
                "SELECT " +
                "TAXES.ID AS TAXID, " +
                "TAXES.NAME AS TAXNAME, " +
                "SUM(TICKETLINES.UNITS * TICKETLINES.PRICE) AS TOTAL, " +
                "SUM(TICKETLINES.UNITS * TICKETLINES.PRICE * (TAXES.RATE)) AS TOTALTAXES " +
                "FROM RECEIPTS, TICKETS, TICKETLINES, TAXES " +
                "WHERE RECEIPTS.ID = TICKETS.ID AND TICKETS.ID = TICKETLINES.TICKET AND TICKETLINES.TAXID = TAXES.ID " +
                "AND ?(QBF_FILTER) " +
                "GROUP BY TAXES.ID, TAXES.NAME",  new String[] {"RECEIPTS.DATENEW", "RECEIPTS.DATENEW"})
            , new SerializerWriteBasic(new Datas[] {Datas.OBJECT, Datas.TIMESTAMP, Datas.OBJECT, Datas.TIMESTAMP})
            , new SerializerReadBasic(new Datas[] {Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.DOUBLE}));
    }
    protected ReportFields getReportFields() {
        return new ReportFieldsArray(new String[]{"TAXID", "TAXNAME", "TOTAL", "TOTALTAXES"});
    }
    protected EditorCreator createEditorCreator() {
        return new JParamsDatesInterval();
    }     
}
