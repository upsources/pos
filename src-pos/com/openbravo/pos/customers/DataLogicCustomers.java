//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2009 Openbravo, S.L.
//    http://www.openbravo.com/product/pos
//
//    This file is part of Openbravo POS.
//
//    Openbravo POS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    Openbravo POS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with Openbravo POS.  If not, see <http://www.gnu.org/licenses/>.

package com.openbravo.pos.customers;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataParams;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.QBFBuilder;
import com.openbravo.data.loader.SentenceExec;
import com.openbravo.data.loader.SentenceExecTransaction;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.loader.SerializerRead;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.SerializerWriteBasicExt;
import com.openbravo.data.loader.SerializerWriteParams;
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
        // MSL : add debt
        this.s = s;
        tcustomers = new TableDefinition(s
            , "CUSTOMERS"
            , new String[] { "ID", "TAXID", "SEARCHKEY", "NAME", "NOTES", "VISIBLE", "CARD", "MAXDEBT", "CURDATE", "CURDEBT"
                           , "FIRSTNAME", "LASTNAME", "EMAIL", "PHONE", "PHONE2", "FAX"
                           , "ADDRESS", "ADDRESS2", "POSTAL", "CITY", "REGION", "COUNTRY"
                           , "TAXCATEGORY" , "SHIPPINGADDRESS", "SHIPPINGADDRESS2", "SHIPPINGPOSTAL", "SHIPPINGCITY", "SHIPPINGREGION", "SHIPPINGCOUNTRY"
                           , "DEBTDATELIMIT", "DEBTMODE"
                           , "COMPANYNAME", "CUI", "NRREG", "COMPANYNOTE"
            }
            , new String[] { "ID", AppLocal.getIntString("label.taxid"), AppLocal.getIntString("label.searchkey"), AppLocal.getIntString("label.name"), AppLocal.getIntString("label.notes"), "VISIBLE", "CARD", AppLocal.getIntString("label.maxdebt"), AppLocal.getIntString("label.curdate"), AppLocal.getIntString("label.curdebt")
                           , AppLocal.getIntString("label.firstname"), AppLocal.getIntString("label.lastname"), AppLocal.getIntString("label.email"), AppLocal.getIntString("label.phone"), AppLocal.getIntString("label.phone2"), AppLocal.getIntString("label.fax")
                           , AppLocal.getIntString("label.address"), AppLocal.getIntString("label.address2"), AppLocal.getIntString("label.postal"), AppLocal.getIntString("label.city"), AppLocal.getIntString("label.region"), AppLocal.getIntString("label.country")
                           , "TAXCATEGORY"
                           , AppLocal.getIntString("label.shipaddress"), AppLocal.getIntString("label.shipaddress2"), AppLocal.getIntString("label.shippostal"), AppLocal.getIntString("label.shipcity"), AppLocal.getIntString("label.shipregion"), AppLocal.getIntString("label.shipcountry")
                           , "Debt Limit Date", "Debt Mode"
                           , AppLocal.getIntString("label.companyname"), AppLocal.getIntString("label.cui"), AppLocal.getIntString("label.nrreg"), AppLocal.getIntString("label.companynote")
            }
            , new Datas[] { Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.BOOLEAN, Datas.STRING, Datas.DOUBLE, Datas.TIMESTAMP, Datas.DOUBLE
                          , Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING
                          , Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING
                          , Datas.STRING
                          , Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING
                          , Datas.STRING, Datas.STRING
                          , Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING
            }
            , new Formats[] { Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.BOOLEAN, Formats.STRING, Formats.CURRENCY, Formats.TIMESTAMP, Formats.CURRENCY
                            , Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING
                            , Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING
                            , Formats.STRING
                            , Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING
                            , Formats.STRING, Formats.STRING
                            , Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING
            }
            , new int[] {0}
        );

    }

    // CustomerList list
    public SentenceList getCustomerList() {
        return new StaticSentence(s
            , new QBFBuilder("SELECT ID, TAXID, SEARCHKEY, NAME FROM CUSTOMERS WHERE VISIBLE = " + s.DB.TRUE() + " AND ID != '-1' AND ?(QBF_FILTER) ORDER BY NAME", new String[] {"TAXID", "SEARCHKEY", "NAME"})
            , new SerializerWriteBasic(new Datas[] {Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.STRING})
            , new SerializerRead() {
                    public Object readValues(DataRead dr) throws BasicException {
                        CustomerInfo c = new CustomerInfo(dr.getString(1));
                        c.setTaxid(dr.getString(2));
                        c.setSearchkey(dr.getString(3));
                        c.setName(dr.getString(4));
                        return c;
                    }
                });
    }

    public int updateCustomerExt(final CustomerInfoExt customer) throws BasicException {

        return new PreparedSentence(s
                , "UPDATE CUSTOMERS SET NOTES = ? WHERE ID = ?"
                , SerializerWriteParams.INSTANCE
                ).exec(new DataParams() { public void writeValues() throws BasicException {
                        setString(1, customer.getNotes());
                        setString(2, customer.getId());
                }});
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

    public Object[] getCustomerList(String name) throws BasicException {
        return (Object[]) new StaticSentence(s
            , "SELECT ID FROM CUSTOMERS WHERE SEARCHKEY = ? AND ID != '-1'"
            , SerializerWriteString.INSTANCE
            , new SerializerReadBasic(new Datas[] {Datas.STRING})
        ).find(name);
    }

    public void QuickAdd(String id, String name, String nif, String address, String address2, String postal, String city, String companyname, String cui, String nrreg) throws BasicException {
        new StaticSentence(s
            , "INSERT INTO CUSTOMERS (ID, SEARCHKEY, NAME, TAXID, ADDRESS, ADDRESS2, POSTAL, CITY, COMPANYNAME, CUI, NRREG) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
            , new SerializerWriteBasic(new Datas[] {Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING})
        ).exec(new Object[] {id.toString(), name, name, nif, address, address2, postal, city, companyname, cui, nrreg});
    }

    public void addCompany( Object[] ObjectData ) throws BasicException {
        System.out.println( ObjectData[0] );
        System.out.println( ObjectData[10] );
        new StaticSentence(s
            , "INSERT INTO CUSTOMERS (ID, SEARCHKEY, NAME, COMPANYNAME, CUI, NRREG, ADDRESS, POSTAL, CITY, REGION, COUNTRY, PHONE, FAX, EMAIL, NOTES) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
            , new SerializerWriteBasic(new Datas[] {
                Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING,
                Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING,
                Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING
            })
        ).exec(ObjectData);
    }

    public Object[] getCompany() throws BasicException {
        return (Object[]) new StaticSentence(s
            , "SELECT COMPANYNAME, CUI, NRREG, ADDRESS, POSTAL, CITY, REGION, COUNTRY, PHONE, FAX, EMAIL, NOTES FROM CUSTOMERS WHERE ID = ?"
            , SerializerWriteString.INSTANCE
            , new SerializerReadBasic(new Datas[] {
                Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING,
                Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING,
                Datas.STRING, Datas.STRING
            })
        ).find("-1");
    }

    public void updateCompany( Object[] ObjectData ) throws BasicException {
        new StaticSentence(s
            , "UPDATE CUSTOMERS SET ID = ?, SEARCHKEY = ?, NAME = ?, COMPANYNAME = ?, "
                + "CUI = ?, NRREG = ?, ADDRESS = ?, POSTAL = ?, CITY = ?, REGION = ?, "
                + "COUNTRY = ?, PHONE = ?, FAX = ?, EMAIL = ?, NOTES = ? WHERE ID = '-1'"
            , new SerializerWriteBasic(new Datas[] {
                Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING,
                Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING,
                Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING,
            })
        ).exec(ObjectData);
    }
}
