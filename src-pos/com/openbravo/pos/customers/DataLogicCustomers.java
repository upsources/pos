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
//    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  US

package com.openbravo.pos.customers;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.QBFBuilder;
import com.openbravo.data.loader.SentenceExec;
import com.openbravo.data.loader.SentenceExecTransaction;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.loader.SerializerRead;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.SerializerWrite;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.SerializerWriteBasicExt;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.data.loader.Session;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.loader.TableDefinition;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.BeanFactoryDataSingle;

/**
 *
 * @author adrianromero
 */
public class DataLogicCustomers extends BeanFactoryDataSingle {
    
    protected Session s;
    private TableDefinition tcustomers;
    private static Datas[] customerdatas = new Datas[] {Datas.STRING, Datas.TIMESTAMP, Datas.TIMESTAMP, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.INT, Datas.BOOLEAN, Datas.STRING};
    
    public void init(Session s){
        
        this.s = s;
        tcustomers = new TableDefinition(s,
            "CUSTOMERS"
            , new String[] {"ID", "TAXID", "SEARCHKEY", "NAME", "ADDRESS", "NOTES", "VISIBLE", "CARD", "MAXDEBT", "CURDATE", "CURDEBT"}
            , new String[] {"ID", AppLocal.getIntString("label.taxid"), AppLocal.getIntString("label.searchkey"), AppLocal.getIntString("label.name"), AppLocal.getIntString("label.address"), AppLocal.getIntString("label.notes"), "VISIBLE", "CARD", AppLocal.getIntString("label.maxdebt"), AppLocal.getIntString("label.curdate"), AppLocal.getIntString("label.curdebt")}
            , new Datas[] {Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.BOOLEAN, Datas.STRING, Datas.DOUBLE, Datas.TIMESTAMP, Datas.DOUBLE}
            , new Formats[] {Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.BOOLEAN, Formats.STRING, Formats.CURRENCY, Formats.TIMESTAMP, Formats.CURRENCY}
            , new int[] {0}
        );   
        
    }
    
    // CustomerList list
    public SentenceList getCustomerList() {
        return new StaticSentence(s
            , new QBFBuilder("SELECT ID, TAXID, SEARCHKEY, NAME FROM CUSTOMERS WHERE VISIBLE = TRUE AND ?(QBF_FILTER) ORDER BY NAME", new String[] {"NAME"})
            , new SerializerWriteBasic(new Datas[] {Datas.OBJECT, Datas.STRING})
            , new SerializerRead() {
                    public Object readValues(DataRead dr) throws BasicException {
                        return new CustomerInfo(
                                dr.getString(1),
                                dr.getString(2),
                                dr.getString(3),
                                dr.getString(4));
                    }                
                });
    }
    
    public CustomerInfo findCustomer(String card) throws BasicException {
        return (CustomerInfo) new PreparedSentence(s
                , "SELECT ID, TAXID, NAME FROM CUSTOMERS WHERE VISIBLE = TRUE AND CARD = ?"
                , SerializerWriteString.INSTANCE
                , new SerializerRead() {
                    public Object readValues(DataRead dr) throws BasicException {
                        return new CustomerInfo(
                                dr.getString(1),
                                dr.getString(2),
                                dr.getString(3),
                                dr.getString(4));
                    }
                }).find(card);
    }
    
    public int updateCustomerExt(CustomerInfoExt customer) throws BasicException {
     
        return new PreparedSentence(s
                , "UPDATE CUSTOMERS SET ADDRESS = ?, NOTES = ? WHERE ID = ?"
                , new SerializerWrite() {
                    public void writeValues(DataWrite dp, Object obj) throws BasicException {
                        CustomerInfoExt c = (CustomerInfoExt) obj;
                        dp.setString(1, c.getAddress());
                        dp.setString(2, c.getNotes());
                        dp.setString(3, c.getId());
                    }
                }).exec(customer);        
    }
    
    public CustomerInfoExt findCustomerExt(String card) throws BasicException {
        return (CustomerInfoExt) new PreparedSentence(s
                , "SELECT ID, TAXID, SEARCHKEY, NAME, CARD, ADDRESS, NOTES, MAXDEBT, VISIBLE, CURDATE, CURDEBT FROM CUSTOMERS WHERE CARD = ?"
                , SerializerWriteString.INSTANCE
                , new CustomerExtRead()).find(card);        
    }    
    
    public CustomerInfoExt loadCustomerExt(String id) throws BasicException {
        return (CustomerInfoExt) new PreparedSentence(s
                , "SELECT ID, TAXID, SEARCHKEY, NAME, CARD, ADDRESS, NOTES, MAXDEBT, VISIBLE, CURDATE, CURDEBT FROM CUSTOMERS WHERE ID = ?"
                , SerializerWriteString.INSTANCE
                , new CustomerExtRead()).find(id);        
    }
    
    public final SentenceList getReservationsList() {
        return new PreparedSentence(s
            , "SELECT R.ID, R.CREATED, R.DATENEW, C.CUSTOMER, CUSTOMERS.TAXID, CUSTOMERS.SEARCHKEY, COALESCE(CUSTOMERS.NAME, R.TITLE),  R.CHAIRS, R.ISDONE, R.DESCRIPTION " +
              "FROM RESERVATIONS R LEFT OUTER JOIN RESERVATION_CUSTOMERS C ON R.ID = C.ID LEFT OUTER JOIN CUSTOMERS ON C.CUSTOMER = CUSTOMERS.ID " +
              "WHERE R.DATENEW >= ? AND R.DATENEW < ?"
            , new SerializerWriteBasic(new Datas[] {Datas.TIMESTAMP, Datas.TIMESTAMP})
            , new SerializerReadBasic(customerdatas));             
    }
    
    public final SentenceExec getReservationsUpdate() {
        return new SentenceExecTransaction(s) {
            public int execInTransaction(Object params) throws BasicException {  
    
                new PreparedSentence(s
                    , "DELETE FROM RESERVATION_CUSTOMERS WHERE ID = ?"
                    , new SerializerWriteBasicExt(customerdatas, new int[]{0})).exec(params);
                if (((Object[]) params)[3] != null) {
                    new PreparedSentence(s
                        , "INSERT INTO RESERVATION_CUSTOMERS (ID, CUSTOMER) VALUES (?, ?)"
                        , new SerializerWriteBasicExt(customerdatas, new int[]{0, 3})).exec(params);                
                }
                return new PreparedSentence(s
                    , "UPDATE RESERVATIONS SET ID = ?, CREATED = ?, DATENEW = ?, TITLE = ?, CHAIRS = ?, ISDONE = ?, DESCRIPTION = ? WHERE ID = ?"
                    , new SerializerWriteBasicExt(customerdatas, new int[]{0, 1, 2, 6, 7, 8, 9, 0})).exec(params);
            }
        };
    }
    
    public final SentenceExec getReservationsDelete() {
        return new SentenceExecTransaction(s) {
            public int execInTransaction(Object params) throws BasicException {  
    
                new PreparedSentence(s
                    , "DELETE FROM RESERVATION_CUSTOMERS WHERE ID = ?"
                    , new SerializerWriteBasicExt(customerdatas, new int[]{0})).exec(params);
                return new PreparedSentence(s
                    , "DELETE FROM RESERVATIONS WHERE ID = ?"
                    , new SerializerWriteBasicExt(customerdatas, new int[]{0})).exec(params);
            }
        };
    }
    
    public final SentenceExec getReservationsInsert() {
        return new SentenceExecTransaction(s) {
            public int execInTransaction(Object params) throws BasicException {  
    
                int i = new PreparedSentence(s
                    , "INSERT INTO RESERVATIONS (ID, CREATED, DATENEW, TITLE, CHAIRS, ISDONE, DESCRIPTION) VALUES (?, ?, ?, ?, ?, ?, ?)"
                    , new SerializerWriteBasicExt(customerdatas, new int[]{0, 1, 2, 6, 7, 8, 9})).exec(params);

                if (((Object[]) params)[3] != null) {
                    new PreparedSentence(s
                        , "INSERT INTO RESERVATION_CUSTOMERS (ID, CUSTOMER) VALUES (?, ?)"
                        , new SerializerWriteBasicExt(customerdatas, new int[]{0, 3})).exec(params);                
                }
                return i;
            }
        };
    }
    
    public final TableDefinition getTableCustomers() {
        return tcustomers;
    }  
    
    private static class CustomerExtRead implements SerializerRead {
        public Object readValues(DataRead dr) throws BasicException {
            CustomerInfoExt c = new CustomerInfoExt(
                    dr.getString(1),
                    dr.getString(2),
                    dr.getString(3),
                    dr.getString(4));
            c.setCard(dr.getString(5));
            c.setAddress(dr.getString(6));
            c.setNotes(dr.getString(7));
            c.setMaxdebt(dr.getDouble(8));
            c.setVisible(dr.getBoolean(9).booleanValue());
            c.setCurdate(dr.getTimestamp(10));
            c.setCurdebt(dr.getDouble(11));
            return c;
        }                  
    }
}
