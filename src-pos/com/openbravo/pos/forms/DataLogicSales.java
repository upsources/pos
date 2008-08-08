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

package com.openbravo.pos.forms;

import com.openbravo.pos.ticket.CategoryInfo;
import com.openbravo.pos.ticket.ProductInfoExt;
import com.openbravo.pos.ticket.TaxInfo;
import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.ticket.TicketLineInfo;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import com.openbravo.data.loader.*;
import com.openbravo.format.Formats;
import com.openbravo.basic.BasicException;
import com.openbravo.pos.customers.CustomerInfoExt;
import com.openbravo.pos.inventory.TaxCustCategoryInfo;
import com.openbravo.pos.inventory.LocationInfo;
import com.openbravo.pos.inventory.MovementReason;
import com.openbravo.pos.inventory.TaxCategoryInfo;
import com.openbravo.pos.mant.FloorsInfo;
import com.openbravo.pos.payment.PaymentInfo;
import com.openbravo.pos.payment.PaymentInfoTicket;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * @author adrianromero
 */
public abstract class DataLogicSales extends BeanFactoryDataSingle {
    
    protected Session s;

    protected Datas[] stockdiaryDatas;
    protected Datas[] productcatDatas;
    protected Datas[] paymenttabledatas;
    protected Datas[] stockdatas;
     
    /** Creates a new instance of SentenceContainerGeneric */
    public DataLogicSales() {
        productcatDatas = new Datas[] {Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.BOOLEAN, Datas.BOOLEAN, Datas.DOUBLE, Datas.DOUBLE, Datas.STRING, Datas.STRING, Datas.IMAGE, Datas.DOUBLE, Datas.DOUBLE, Datas.BOOLEAN, Datas.INT, Datas.BYTES};
        stockdiaryDatas = new Datas[] {Datas.STRING, Datas.TIMESTAMP, Datas.INT, Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.DOUBLE};
        paymenttabledatas = new Datas[] {Datas.STRING, Datas.STRING, Datas.TIMESTAMP, Datas.STRING, Datas.STRING, Datas.DOUBLE};
        stockdatas = new Datas[] {Datas.STRING, Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.DOUBLE, Datas.DOUBLE};
    }
    
    public void init(Session s){        
        this.s = s;  
    }      
    
    // Utilidades de productos
    public final ProductInfoExt getProductInfo(String id) throws BasicException {
        return (ProductInfoExt) new PreparedSentence(s
            , "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, TC.ID, TC.NAME, P.CATEGORY, P.IMAGE, P.ATTRIBUTES " +
              "FROM PRODUCTS P LEFT OUTER JOIN TAXCATEGORIES TC ON P.TAXCAT = TC.ID WHERE P.ID = ?"
            , SerializerWriteString.INSTANCE
            , new SerializerReadClass(ProductInfoExt.class)).find(id);
    }
    public final ProductInfoExt getProductInfoByCode(String sCode) throws BasicException {
        return (ProductInfoExt) new PreparedSentence(s
            , "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, TC.ID, TC.NAME, P.CATEGORY, P.IMAGE, P.ATTRIBUTES " +
              "FROM PRODUCTS P LEFT OUTER JOIN TAXCATEGORIES TC ON P.TAXCAT = TC.ID WHERE P.CODE = ?"
            , SerializerWriteString.INSTANCE
            , new SerializerReadClass(ProductInfoExt.class)).find(sCode);
    }
    public final ProductInfoExt getProductInfoByReference(String sReference) throws BasicException {
        return (ProductInfoExt) new PreparedSentence(s
            , "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, TC.ID, TC.NAME, P.CATEGORY, P.IMAGE, P.ATTRIBUTES " +
              "FROM PRODUCTS P LEFT OUTER JOIN TAXCATEGORIES TC ON P.TAXCAT = TC.ID WHERE P.REFERENCE = ?"
            , SerializerWriteString.INSTANCE
            , new SerializerReadClass(ProductInfoExt.class)).find(sReference);
    }
    
    // Catalogo de productos
    public final List<CategoryInfo> getRootCategories() throws BasicException {
        return new PreparedSentence(s
            , "SELECT ID, NAME, IMAGE FROM CATEGORIES WHERE PARENTID IS NULL ORDER BY NAME"
            , null
            , new SerializerReadClass(CategoryInfo.class)).list();
    }    
    public final List<CategoryInfo> getSubcategories(String category) throws BasicException  {
        return new PreparedSentence(s
            , "SELECT ID, NAME, IMAGE FROM CATEGORIES WHERE PARENTID = ? ORDER BY NAME"
            , SerializerWriteString.INSTANCE
            , new SerializerReadClass(CategoryInfo.class)).list(category);
    }    
    public final List<ProductInfoExt> getProductCatalog(String category) throws BasicException  {
        return new PreparedSentence(s
            , "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, TC.ID, TC.NAME, P.CATEGORY, P.IMAGE, P.ATTRIBUTES " +
              "FROM PRODUCTS P LEFT OUTER JOIN TAXCATEGORIES TC ON P.TAXCAT = TC.ID LEFT OUTER JOIN CATEGORIES C ON P.CATEGORY = C.ID, PRODUCTS_CAT O WHERE P.ID = O.PRODUCT AND P.CATEGORY = ?" +
              "ORDER BY C.NAME, O.CATORDER, P.REFERENCE"
            , SerializerWriteString.INSTANCE
            , new SerializerReadClass(ProductInfoExt.class)).list(category);
    }
    public final List<ProductInfoExt> getProductComments(String id) throws BasicException {
        return new PreparedSentence(s
            , "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, TC.ID, TC.NAME, P.CATEGORY, P.IMAGE, P.ATTRIBUTES " +
              "FROM PRODUCTS P LEFT OUTER JOIN TAXCATEGORIES TC ON P.TAXCAT = TC.ID, PRODUCTS_CAT O, PRODUCTS_COM M WHERE P.ID = O.PRODUCT AND P.ID = M.PRODUCT2 AND M.PRODUCT = ? " +
              "ORDER BY O.CATORDER, P.NAME"
            , SerializerWriteString.INSTANCE 
            , new SerializerReadClass(ProductInfoExt.class)).list(id);
    }     
    
    // Products list
    public final SentenceList getProductList() {
        return new StaticSentence(s
            , new QBFBuilder(
              "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, TC.ID, TC.NAME, P.CATEGORY, P.IMAGE, P.ATTRIBUTES " +
              "FROM PRODUCTS P LEFT OUTER JOIN TAXCATEGORIES TC ON P.TAXCAT = TC.ID WHERE ?(QBF_FILTER) ORDER BY P.REFERENCE", new String[] {"P.NAME", "P.PRICEBUY", "P.PRICESELL", "P.CATEGORY", "P.CODE"})
            , new SerializerWriteBasic(new Datas[] {Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.STRING})
            , new SerializerReadClass(ProductInfoExt.class));
    }
    
    // Listados para combo        
    public final SentenceList getTaxList() {
        return new StaticSentence(s
            , "SELECT ID, NAME, CATEGORY, CUSTCATEGORY, PARENTID, RATE, CASCADE FROM TAXES ORDER BY NAME"
            , null
            , new SerializerReadClass(TaxInfo.class));
    }        
    public final SentenceList getCategoriesList() {
        return new StaticSentence(s
            , "SELECT ID, NAME, IMAGE FROM CATEGORIES ORDER BY NAME"
            , null
            , new SerializerReadClass(CategoryInfo.class));
    }
    public final SentenceList getTaxCustCategoriesList() {
        return new StaticSentence(s
            , "SELECT ID, NAME FROM TAXCUSTCATEGORIES ORDER BY NAME"
            , null
            , new SerializerReadClass(TaxCustCategoryInfo.class));
    }
    public final SentenceList getTaxCategoriesList() {
        return new StaticSentence(s
            , "SELECT ID, NAME FROM TAXCATEGORIES ORDER BY NAME"
            , null
            , new SerializerReadClass(TaxCategoryInfo.class));
    }
    public final SentenceList getLocationsList() {
        return new StaticSentence(s
            , "SELECT ID, NAME, ADDRESS FROM LOCATIONS ORDER BY NAME"
            , null
            , new SerializerReadClass(LocationInfo.class));
    }
    public final SentenceList getFloorsList() {
        return new StaticSentence(s
            , "SELECT ID, NAME FROM FLOORS ORDER BY NAME"
            , null
            , new SerializerReadClass(FloorsInfo.class));
    }
    
    public CustomerInfoExt findCustomerExt(String card) throws BasicException {
        return (CustomerInfoExt) new PreparedSentence(s
                , "SELECT ID, TAXID, SEARCHKEY, NAME, CARD, NOTES, MAXDEBT, VISIBLE, CURDATE, CURDEBT" +
                  ", FIRSTNAME, LASTNAME, EMAIL, PHONE, PHONE2, FAX" +
                  ", ADDRESS, ADDRESS2, POSTAL, CITY, REGION, COUNTRY" +
                  " FROM CUSTOMERS WHERE CARD = ? AND VISIBLE = TRUE"
                , SerializerWriteString.INSTANCE
                , new CustomerExtRead()).find(card);        
    }    
    
    public CustomerInfoExt loadCustomerExt(String id) throws BasicException {
        return (CustomerInfoExt) new PreparedSentence(s
                , "SELECT ID, TAXID, SEARCHKEY, NAME, CARD, NOTES, MAXDEBT, VISIBLE, CURDATE, CURDEBT" +
                  ", FIRSTNAME, LASTNAME, EMAIL, PHONE, PHONE2, FAX" +
                  ", ADDRESS, ADDRESS2, POSTAL, CITY, REGION, COUNTRY" +
                " FROM CUSTOMERS WHERE ID = ?"
                , SerializerWriteString.INSTANCE
                , new CustomerExtRead()).find(id);        
    }
    
    public final boolean isCashActive(String id) throws BasicException {
        
        return new PreparedSentence(s,
                "SELECT MONEY FROM CLOSEDCASH WHERE DATEEND IS NULL AND MONEY = ?",
                SerializerWriteString.INSTANCE,
                SerializerReadString.INSTANCE).find(id)
            != null;            
    }
    
    public final TicketInfo loadTicket(Integer ticketid) throws BasicException {
        TicketInfo ticket = (TicketInfo) new PreparedSentence(s
                , "SELECT T.ID, T.TICKETID, R.DATENEW, R.MONEY, R.ATTRIBUTES, P.ID, P.NAME, T.CUSTOMER FROM RECEIPTS R JOIN TICKETS T ON R.ID = T.ID LEFT OUTER JOIN PEOPLE P ON T.PERSON = P.ID WHERE T.TICKETID = ?"
                , SerializerWriteInteger.INSTANCE
                , new SerializerReadClass(TicketInfo.class)).find(ticketid);
        if (ticket != null) {
            
            ticket.setCustomer(loadCustomerExt(ticket.getCustomerId()));
            
            ticket.setLines(new PreparedSentence(s
                , "SELECT L.TICKET, L.LINE, L.PRODUCT, L.NAME, L.ISCOM, L.UNITS, L.PRICE, T.ID, T.RATE, L.ATTRIBUTES FROM TICKETLINES L, TAXES T WHERE L.TAXID = T.ID AND L.TICKET = ? ORDER BY L.LINE"
                , SerializerWriteString.INSTANCE
                , new SerializerReadClass(TicketLineInfo.class)).list(ticket.getId()));  
            ticket.setPayments(new PreparedSentence(s
                , "SELECT PAYMENT, TOTAL FROM PAYMENTS WHERE RECEIPT = ?"
                , SerializerWriteString.INSTANCE
                , new SerializerReadClass(PaymentInfoTicket.class)).list(ticket.getId()));  
        }
        return ticket;
    }
    
    public final void saveTicket(final TicketInfo ticket, final String location) throws BasicException {
        
        Transaction t = new Transaction(s) {
            public Object transact() throws BasicException {
                
                // Set Receipt Id
                if (ticket.getTicketId() == 0) {
                    ticket.setTicketId(getNextTicketIndex().intValue());
                }       
                
                // new receipt
                new PreparedSentence(s
                    , "INSERT INTO RECEIPTS (ID, MONEY, DATENEW, ATTRIBUTES) VALUES (?, ?, ?, ?)"
                    , SerializerWriteParams.INSTANCE
                    ).exec(new DataParams() { public void writeValues() throws BasicException {
                        setString(1, ticket.getId());
                        setString(2, ticket.getActiveCash());
                        setTimestamp(3, ticket.getDate());   
                        try {
                            ByteArrayOutputStream o = new ByteArrayOutputStream();
                            ticket.getProperties().storeToXML(o, AppLocal.APP_NAME, "UTF-8");
                            setBytes(4, o.toByteArray()); 
                        } catch (IOException e) {
                            setBytes(4, null);
                        }                         
                    }});
                
                // new ticket
                new PreparedSentence(s
                    , "INSERT INTO TICKETS (ID, TICKETID, PERSON, CUSTOMER) VALUES (?, ?, ?, ?)"
                    , SerializerWriteParams.INSTANCE
                    ).exec(new DataParams() { public void writeValues() throws BasicException {
                        setString(1, ticket.getId());
                        setInt(2, ticket.getTicketId());
                        setString(3, ticket.getUser().getId());
                        setString(4, ticket.getCustomerId());    
                    }});                
                
                SentenceExec ticketlineinsert = new PreparedSentence(s
                    , "INSERT INTO TICKETLINES (TICKET, LINE, PRODUCT, NAME, ISCOM, UNITS, PRICE, TAXID, ATTRIBUTES) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
                    , SerializerWriteBuilder.INSTANCE); 
                
                for (TicketLineInfo l : ticket.getLines()) {
                    ticketlineinsert.exec(l);
                    if (l.getProduct().getId() != null)  {
                        // update the stock                             
                        getStockDiaryInsert().exec(new Object[] {
                            UUID.randomUUID().toString(),
                            ticket.getDate(),
                            l.getMultiply() < 0.0 
                                ? MovementReason.IN_REFUND.getKey()
                                : MovementReason.OUT_SALE.getKey(),
                            location,
                            l.getProduct().getId(),
                            new Double(-l.getMultiply()),
                            new Double(l.getPrice())
                        });
                    }
                }
                
                SentenceExec paymentinsert = new PreparedSentence(s
                    , "INSERT INTO PAYMENTS (ID, RECEIPT, PAYMENT, TOTAL) VALUES (?, ?, ?, ?)"
                    , new SerializerWriteBasic(new Datas[] {Datas.STRING, Datas.STRING, Datas.STRING, Datas.DOUBLE}));                
                for (PaymentInfo p : ticket.getPayments()) {
                    paymentinsert.exec(new Object[] {
                        UUID.randomUUID().toString(),
                        ticket.getId(),
                        p.getName(),
                        new Double(p.getTotal())
                    });
                    if ("debt".equals(p.getName()) || "debtpaid".equals(p.getName())) {
                        getDebtUpdate().exec(new Object[]{                           
                            ticket.getCustomer().getId(),
                            new Double(p.getTotal()),
                            ticket.getDate()
                        });
                    }
                } 
                return null;
            }
        };
        t.execute();
    }

    public final void deleteTicket(final TicketInfo ticket, final String location) throws BasicException {
        
        Transaction t = new Transaction(s) {
            public Object transact() throws BasicException {
                
                // update the inventory
                Date d = new Date();
                for (int i = 0; i < ticket.getLinesCount(); i++) {
                    if (ticket.getLine(i).getProduct().getId() != null)  {
                        // Hay que actualizar el stock si el hay producto                              
                        getStockDiaryInsert().exec( new Object[] {
                            UUID.randomUUID().toString(),
                            d,
                            ticket.getLine(i).getMultiply() >= 0.0 
                                ? MovementReason.IN_REFUND.getKey()
                                : MovementReason.OUT_SALE.getKey(),
                            location,
                            ticket.getLine(i).getProduct().getId(),
                            new Double(ticket.getLine(i).getMultiply()),
                            new Double(ticket.getLine(i).getPrice())                                
                        });
                    }
                }  
                
                // update customer debts
                for (PaymentInfo p : ticket.getPayments()) {
                    if ("debt".equals(p.getName()) || "debtpaid".equals(p.getName())) {
                        getDebtUpdate().exec(new Object[]{                           
                            ticket.getCustomer().getId(),
                            new Double(-p.getTotal()),
                            ticket.getDate()
                        });
                    }
                }
                
                // and delete the receipt
                new StaticSentence(s
                    , "DELETE FROM PAYMENTS WHERE RECEIPT = ?"
                    , SerializerWriteString.INSTANCE).exec(ticket.getId());
                new StaticSentence(s
                    , "DELETE FROM TICKETLINES WHERE TICKET = ?"
                    , SerializerWriteString.INSTANCE).exec(ticket.getId());
                new StaticSentence(s
                    , "DELETE FROM TICKETS WHERE ID = ?"
                    , SerializerWriteString.INSTANCE).exec(ticket.getId());  
                new StaticSentence(s
                    , "DELETE FROM RECEIPTS WHERE ID = ?"
                    , SerializerWriteString.INSTANCE).exec(ticket.getId());  
                return null;
            }
        };
        t.execute();
    }
    
    public abstract Integer getNextTicketIndex() throws BasicException;
    
    public abstract SentenceList getProductCatQBF();   
       
    public final SentenceExec getProductCatInsert() {
        return new SentenceExecTransaction(s) {
            public int execInTransaction(Object params) throws BasicException {
                Object[] values = (Object[]) params;            
                int i = new PreparedSentence(s
                    , "INSERT INTO PRODUCTS (ID, REFERENCE, CODE, NAME, ISCOM, ISSCALE, PRICEBUY, PRICESELL, CATEGORY, TAXCAT, IMAGE, STOCKCOST, STOCKVOLUME, ATTRIBUTES) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                    , new SerializerWriteBasicExt(productcatDatas, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 15})).exec(params);
                if (i > 0 && ((Boolean)values[13]).booleanValue()) {
                    return new PreparedSentence(s
                        , "INSERT INTO PRODUCTS_CAT (PRODUCT, CATORDER) VALUES (?, ?)"
                        , new SerializerWriteBasicExt(productcatDatas, new int[] {0, 14})).exec(params);
                } else {
                    return i;
                }
            }
        };
    }
    
    public final SentenceExec getProductCatUpdate() {
        return new SentenceExecTransaction(s) {
            public int execInTransaction(Object params) throws BasicException {
                Object[] values = (Object[]) params;            
                int i = new PreparedSentence(s
                    , "UPDATE PRODUCTS SET ID = ?, REFERENCE = ?, CODE = ?, NAME = ?, ISCOM = ?, ISSCALE = ?, PRICEBUY = ?, PRICESELL = ?, CATEGORY = ?, TAXCAT = ?, IMAGE = ?, STOCKCOST = ?, STOCKVOLUME = ?, ATTRIBUTES = ? WHERE ID = ?"
                    , new SerializerWriteBasicExt(productcatDatas, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 15, 0})).exec(params);
                if (i > 0) {
                    if (((Boolean)values[13]).booleanValue()) {
                        if (new PreparedSentence(s
                                , "UPDATE PRODUCTS_CAT SET CATORDER = ? WHERE PRODUCT = ?"
                                , new SerializerWriteBasicExt(productcatDatas, new int[] {14, 0})).exec(params) == 0) {
                            new PreparedSentence(s
                                , "INSERT INTO PRODUCTS_CAT (PRODUCT, CATORDER) VALUES (?, ?)"
                                , new SerializerWriteBasicExt(productcatDatas, new int[] {0, 14})).exec(params);
                        }
                    } else {
                        new PreparedSentence(s
                            , "DELETE FROM PRODUCTS_CAT WHERE PRODUCT = ?"
                            , new SerializerWriteBasicExt(productcatDatas, new int[] {0})).exec(params);
                    }
                }
                return i;
            }        
        };
    }
   
    public final SentenceExec getProductCatDelete() {
        return new SentenceExecTransaction(s) {
            public int execInTransaction(Object params) throws BasicException {
                new PreparedSentence(s
                    , "DELETE FROM PRODUCTS_CAT WHERE PRODUCT = ?"
                    , new SerializerWriteBasicExt(productcatDatas, new int[] {0})).exec(params);
                return new PreparedSentence(s
                    , "DELETE FROM PRODUCTS WHERE ID = ?"
                    , new SerializerWriteBasicExt(productcatDatas, new int[] {0})).exec(params);
            } 
        };
    }
    
    public final SentenceExec getDebtUpdate() {
        
        return new PreparedSentence(s
                , "UPDATE CUSTOMERS SET " +
                " CURDEBT = CASE WHEN (COALESCE(CURDEBT, 0) + ?) <= 0 THEN NULL ELSE (COALESCE(CURDEBT, 0) + ?) END, " +
                " CURDATE = CASE WHEN (COALESCE(CURDEBT, 0) + ?) <= 0 THEN NULL WHEN CURDATE IS NULL THEN ? ELSE CURDATE END " +
                " WHERE ID = ?"
                , new SerializerWriteBasicExt(new Datas[] {Datas.STRING, Datas.DOUBLE, Datas.TIMESTAMP}, new int[]{1, 1, 1, 2, 0}));   
    }
    
    public final SentenceExec getStockDiaryInsert() {
        return new SentenceExecTransaction(s) {
            public int execInTransaction(Object params) throws BasicException {
                if (new PreparedSentence(s
                        , "UPDATE STOCKCURRENT SET UNITS = (UNITS + ?) WHERE LOCATION = ? AND PRODUCT = ?"
                        , new SerializerWriteBasicExt(stockdiaryDatas, new int[] {5, 3, 4})).exec(params) == 0) {
                    new PreparedSentence(s
                        , "INSERT INTO STOCKCURRENT (LOCATION, PRODUCT, UNITS) VALUES (?, ?, ?)"
                        , new SerializerWriteBasicExt(stockdiaryDatas, new int[] {3, 4, 5})).exec(params);
                }
                return new PreparedSentence(s
                    , "INSERT INTO STOCKDIARY (ID, DATENEW, REASON, LOCATION, PRODUCT, UNITS, PRICE) VALUES (?, ?, ?, ?, ?, ?, ?)"
                    , new SerializerWriteBasicExt(stockdiaryDatas, new int[] {0, 1, 2, 3, 4, 5, 6})).exec(params);
            }
        };
    }
    
    public final SentenceExec getStockDiaryDelete() {
        return new SentenceExecTransaction(s) {
            public int execInTransaction(Object params) throws BasicException {
                if (new PreparedSentence(s
                        , "UPDATE STOCKCURRENT SET UNITS = (UNITS - ?) WHERE LOCATION = ? AND PRODUCT = ?"
                        , new SerializerWriteBasicExt(stockdiaryDatas, new int[] {5, 3, 4})).exec(params) == 0) {
                    new PreparedSentence(s
                        , "INSERT INTO STOCKCURRENT (LOCATION, PRODUCT, UNITS) VALUES (?, ?, -(?))"
                        , new SerializerWriteBasicExt(stockdiaryDatas, new int[] {3, 4, 5})).exec(params);
                }
                return new PreparedSentence(s
                    , "DELETE FROM STOCKDIARY WHERE ID = ?"
                    , new SerializerWriteBasicExt(stockdiaryDatas, new int[] {0})).exec(params);
            }
        };
    }
    
    public final SentenceExec getPaymentMovementInsert() {
        return new SentenceExecTransaction(s) {
            public int execInTransaction(Object params) throws BasicException {
                new PreparedSentence(s
                    , "INSERT INTO RECEIPTS (ID, MONEY, DATENEW) VALUES (?, ?, ?)"
                    , new SerializerWriteBasicExt(paymenttabledatas, new int[] {0, 1, 2})).exec(params);
                return new PreparedSentence(s
                    , "INSERT INTO PAYMENTS (ID, RECEIPT, PAYMENT, TOTAL) VALUES (?, ?, ?, ?)"
                    , new SerializerWriteBasicExt(paymenttabledatas, new int[] {3, 0, 4, 5})).exec(params);
            }
        };
    }
    
    public final SentenceExec getPaymentMovementDelete() {
        return new SentenceExecTransaction(s) {
            public int execInTransaction(Object params) throws BasicException {
                new PreparedSentence(s
                    , "DELETE FROM PAYMENTS WHERE ID = ?"
                    , new SerializerWriteBasicExt(paymenttabledatas, new int[] {3})).exec(params);
                return new PreparedSentence(s
                    , "DELETE FROM RECEIPTS WHERE ID = ?"
                    , new SerializerWriteBasicExt(paymenttabledatas, new int[] {0})).exec(params);
            }
        };
    }
    
    public final double findProductStock(String id, String warehouse) throws BasicException {
        PreparedSentence p = new PreparedSentence(s, "SELECT UNITS FROM STOCKCURRENT WHERE PRODUCT=? AND LOCATION=?"
                , new SerializerWriteBasic(new Datas[]{ Datas.STRING, Datas.STRING})
                , SerializerReadDouble.INSTANCE);
        Double d = (Double) p.find(new Object[]{id, warehouse});
        return d == null ? 0.0 : d.doubleValue();
    }
    
    public final SentenceList getProductStock() {
        return new PreparedSentence (s
                , "SELECT L.ID, L.NAME, ?, COALESCE(S.UNITS, 0.0), S.STOCKSECURITY, S.STOCKMAXIMUM " +
                "FROM LOCATIONS L LEFT OUTER JOIN (" +
                "SELECT PRODUCT, LOCATION, STOCKSECURITY, STOCKMAXIMUM, UNITS FROM STOCKCURRENT WHERE PRODUCT = ?) S " +
                "ON L.ID = S.LOCATION"
                , new SerializerWriteBasicExt(productcatDatas, new int[]{0, 0})
                , new SerializerReadBasic(stockdatas));
    }     
    
    public final SentenceExec getStockUpdate() {
        return new SentenceExecTransaction(s) {
            public int execInTransaction(Object params) throws BasicException {
                if (new PreparedSentence(s
                        , "UPDATE STOCKCURRENT SET STOCKSECURITY = ?, STOCKMAXIMUM = ? WHERE LOCATION = ? AND PRODUCT = ?"
                        , new SerializerWriteBasicExt(stockdatas, new int[] {4, 5, 0, 2})).exec(params) == 0) {
                    return new PreparedSentence(s
                        , "INSERT INTO STOCKCURRENT(LOCATION, PRODUCT, UNITS, STOCKSECURITY, STOCKMAXIMUM) VALUES (?, ?, 0.0, ?, ?)"
                        , new SerializerWriteBasicExt(stockdatas, new int[] {0, 2, 4, 5})).exec(params);
                } else {
                    return 1;
                }     
            }
        };
    }
    
    public final SentenceExec getCatalogCategoryAdd() {
        return new StaticSentence(s
                , "INSERT INTO PRODUCTS_CAT(PRODUCT, CATORDER) SELECT ID, NULL FROM PRODUCTS WHERE CATEGORY = ?"
                , SerializerWriteString.INSTANCE);
    }
    public final SentenceExec getCatalogCategoryDel() {
        return new StaticSentence(s
                , "DELETE FROM PRODUCTS_CAT WHERE PRODUCT = ANY (SELECT ID FROM PRODUCTS WHERE CATEGORY = ?)"
                , SerializerWriteString.INSTANCE);
    }    
   
    public final TableDefinition getTableCategories() {
        return new TableDefinition(s,
            "CATEGORIES"
            , new String[] {"ID", "NAME", "PARENTID", "IMAGE"}
            , new String[] {"ID", AppLocal.getIntString("Label.Name"), "", AppLocal.getIntString("label.image")}
            , new Datas[] {Datas.STRING, Datas.STRING, Datas.STRING, Datas.IMAGE}
            , new Formats[] {Formats.STRING, Formats.STRING, Formats.STRING, Formats.NULL}
            , new int[] {0}
        );
    }     
    public final TableDefinition getTableTaxes() {
        return new TableDefinition(s,
            "TAXES"
            , new String[] {"ID", "NAME", "CATEGORY", "CUSTCATEGORY", "PARENTID", "RATE", "CASCADE"}
            , new String[] {"ID", AppLocal.getIntString("Label.Name"), AppLocal.getIntString("label.taxcategory"), AppLocal.getIntString("label.custtaxcategory"), AppLocal.getIntString("label.taxparent"), AppLocal.getIntString("label.dutyrate"), AppLocal.getIntString("label.cascade")}
            , new Datas[] {Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.BOOLEAN}
            , new Formats[] {Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.PERCENT, Formats.BOOLEAN}
            , new int[] {0}
        );
    }    
    public final TableDefinition getTableTaxCustCategories() {
        return new TableDefinition(s,
            "TAXCUSTCATEGORIES"
            , new String[] {"ID", "NAME"}
            , new String[] {"ID", AppLocal.getIntString("Label.Name")}
            , new Datas[] {Datas.STRING, Datas.STRING}
            , new Formats[] {Formats.STRING, Formats.STRING}
            , new int[] {0}
        );
    } 
    public final TableDefinition getTableTaxCategories() {
        return new TableDefinition(s,
            "TAXCATEGORIES"
            , new String[] {"ID", "NAME"}
            , new String[] {"ID", AppLocal.getIntString("Label.Name")}
            , new Datas[] {Datas.STRING, Datas.STRING}
            , new Formats[] {Formats.STRING, Formats.STRING}
            , new int[] {0}
        );
    } 
    
    public final TableDefinition getTableLocations() {
        return new TableDefinition(s,
            "LOCATIONS"
            , new String[] {"ID", "NAME", "ADDRESS"}
            , new String[] {"ID", AppLocal.getIntString("label.locationname"), AppLocal.getIntString("label.locationaddress")}        
            , new Datas[] {Datas.STRING, Datas.STRING, Datas.STRING}
            , new Formats[] {Formats.STRING, Formats.STRING, Formats.STRING}
            , new int[] {0}
        );
    }
    
    protected static class CustomerExtRead implements SerializerRead {
        public Object readValues(DataRead dr) throws BasicException {
            CustomerInfoExt c = new CustomerInfoExt(dr.getString(1));
            c.setTaxid(dr.getString(2));
            c.setSearchkey(dr.getString(3));
            c.setName(dr.getString(4));
            c.setCard(dr.getString(5));
            c.setNotes(dr.getString(6));
            c.setMaxdebt(dr.getDouble(7));
            c.setVisible(dr.getBoolean(8).booleanValue());
            c.setCurdate(dr.getTimestamp(9));
            c.setCurdebt(dr.getDouble(10));
            c.setFirstname(dr.getString(11));
            c.setLastname(dr.getString(12));
            c.setEmail(dr.getString(13));
            c.setPhone(dr.getString(14));
            c.setPhone2(dr.getString(15));
            c.setFax(dr.getString(16));
            c.setAddress(dr.getString(17));
            c.setAddress2(dr.getString(18));
            c.setPostal(dr.getString(19));
            c.setCity(dr.getString(20));
            c.setRegion(dr.getString(21));
            c.setCountry(dr.getString(22));
            
            return c;
        }                  
    }    
}
