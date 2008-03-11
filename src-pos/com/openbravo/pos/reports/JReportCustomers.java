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
import com.openbravo.data.loader.BaseSentence;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.QBFBuilder;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.user.EditorCreator;
import com.openbravo.pos.customers.DataLogicCustomers;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.BeanFactoryException;

/**
 *
 * @author adrianromero
 */
public class JReportCustomers extends JPanelReport {
    
    private DataLogicCustomers m_dlCustomers = null;
    private JParamsCustomer customerfilter;
    
    /** Creates a new instance of JReportCustomers */
    public JReportCustomers() {
    }    
    
    @Override
    public void init(AppView app) throws BeanFactoryException {   
        
        m_dlCustomers = (DataLogicCustomers) app.getBean("com.openbravo.pos.customers.DataLogicCustomers");
        super.init(app);
    }
    
    @Override
    public void activate() throws BasicException {
        
        customerfilter.activate();
        super.activate();
    }      
    
    public String getTitle() {
        return AppLocal.getIntString("Menu.CustomersReport");
    }      
    
    protected String getReport() {
        return "/com/openbravo/pos/reports/customers";
    }
    protected String getResourceBundle() {
        return "report_customers_messages";
    }
    protected BaseSentence getSentence() {
        return new StaticSentence(m_App.getSession()
            ,   new QBFBuilder("SELECT ID, NAME, CARD, MAXDEBT, CURDATE, CURDEBT " +
                "FROM CUSTOMERS " +
                "WHERE VISIBLE = TRUE AND ?(QBF_FILTER)",
                new String[] {"ID", "NAME"})
            , new SerializerWriteBasic(new Datas[] {Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.STRING})
            , new SerializerReadBasic(new Datas[] {Datas.STRING, Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.TIMESTAMP, Datas.DOUBLE}));
    }
    protected ReportFields getReportFields() {
        return new ReportFieldsArray(new String[]{"ID", "NAME", "CARD", "MAXDEBT", "CURDATE", "CURDEBT"});
    }  
    
    @Override
    protected EditorCreator createEditorCreator() {

        customerfilter =  new JParamsCustomer(m_dlCustomers);       
        return customerfilter;
    }       

}
