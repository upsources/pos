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
import com.openbravo.pos.inventory.LocationInfo;
import com.openbravo.pos.inventory.MovementReason;
import com.openbravo.pos.mant.FloorsInfo;
import com.openbravo.pos.payment.PaymentInfo;

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
        productcatDatas = new Datas[] {Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.BOOLEAN, Datas.BOOLEAN, Datas.DOUBLE, Datas.DOUBLE, Datas.STRING, Datas.STRING, Datas.IMAGE, Datas.DOUBLE, Datas.DOUBLE, Datas.BOOLEAN, Datas.INT};
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
            , "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAX, T.NAME, T.RATE, P.CATEGORY, P.IMAGE " +
              "FROM PRODUCTS P LEFT OUTER JOIN TAXES T ON P.TAX = T.ID WHERE P.ID = ?"
            , SerializerWriteString.INSTANCE
            , new SerializerReadClass(ProductInfoExt.class)).find(id);
    }
    public final ProductInfoExt getProductInfoByCode(String sCode) throws BasicException {
        return (ProductInfoExt) new PreparedSentence(s
            , "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAX, T.NAME, T.RATE, P.CATEGORY, P.IMAGE " +
              "FROM PRODUCTS P LEFT OUTER JOIN TAXES T ON P.TAX = T.ID WHERE P.CODE = ?"
            , SerializerWriteString.INSTANCE
            , new SerializerReadClass(ProductInfoExt.class)).find(sCode);
    }
    public final ProductInfoExt getProductInfoByReference(String sReference) throws BasicException {
        return (ProductInfoExt) new PreparedSentence(s
            , "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAX, T.NAME, T.RATE, P.CATEGORY, P.IMAGE " +
              "FROM PRODUCTS P LEFT OUTER JOIN TAXES T ON P.TAX = T.ID WHERE P.REFERENCE = ?"
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
            , "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAX, T.NAME, T.RATE, P.CATEGORY, P.IMAGE " +
              "FROM PRODUCTS P LEFT OUTER JOIN TAXES T ON P.TAX = T.ID LEFT OUTER JOIN CATEGORIES C ON P.CATEGORY = C.ID, PRODUCTS_CAT O WHERE P.ID = O.PRODUCT AND P.CATEGORY = ?" +
              "ORDER BY C.NAME, O.CATORDER, P.NAME"
            , SerializerWriteString.INSTANCE
            , new SerializerReadClass(ProductInfoExt.class)).list(category);
    }
    public final List<ProductInfoExt> getProductComments(String id) throws BasicException {
        return new PreparedSentence(s
            , "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAX, T.NAME, T.RATE, P.CATEGORY, P.IMAGE " +
              "FROM PRODUCTS P LEFT OUTER JOIN TAXES T ON P.TAX = T.ID, PRODUCTS_CAT O, PRODUCTS_COM M WHERE P.ID = O.PRODUCT AND P.ID = M.PRODUCT2 AND M.PRODUCT = ? " +
              "ORDER BY O.CATORDER, P.NAME"
            , SerializerWriteString.INSTANCE 
            , new SerializerReadClass(ProductInfoExt.class)).list(id);
    }     
    
    // Products list
    public final SentenceList getProductList() {
        return new StaticSentence(s
            , new QBFBuilder("SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAX, T.NAME, T.RATE, P.CATEGORY, P.IMAGE FROM PRODUCTS P LEFT OUTER JOIN TAXES T ON P.TAX = T.ID WHERE ?(QBF_FILTER) ORDER BY P.NAME", new String[] {"P.NAME", "P.PRICEBUY", "P.PRICESELL", "P.CATEGORY", "P.CODE"})
            , new SerializerWriteBasic(new Datas[] {Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.STRING})
            , new SerializerReadClass(ProductInfoExt.class));
    }
    
    // Listados para combo        
    public final SentenceList getTaxList() {
        return new StaticSentence(s
            , "SELECT ID, NAME, RATE FROM TAXES ORDER BY NAME"
            , null
            , new SerializerReadClass(TaxInfo.class));
    }        
    public final SentenceList getCategoriesList() {
        return new StaticSentence(s
            , "SELECT ID, NAME, IMAGE FROM CATEGORIES ORDER BY NAME"
            , null
            , new SerializerReadClass(CategoryInfo.class));
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
    
    public final TicketInfo loadTicket(Integer ticketid) throws BasicException {
        TicketInfo ticket = (TicketInfo) new PreparedSentence(s
                , "SELECT T.ID, T.TICKETID, R.DATENEW, R.MONEY, P.ID, P.NAME, C.ID, C.NAME FROM RECEIPTS R JOIN TICKETS T ON R.ID = T.ID LEFT OUTER JOIN PEOPLE P ON T.PERSON = P.ID LEFT OUTER JOIN CUSTOMERS C ON T.CUSTOMER = C.ID WHERE T.TICKETID = ?"
                , SerializerWriteInteger.INSTANCE
                , new SerializerReadClass(TicketInfo.class)).find(ticketid);
        if (ticket != null) {
            ticket.setLines(new PreparedSentence(s
                , "SELECT L.TICKET, L.LINE, L.PRODUCT, L.NAME, L.ISCOM, L.UNITS, L.PRICE, T.ID, T.RATE FROM TICKETLINES L, TAXES T WHERE L.TAXID = T.ID AND L.TICKET = ?"
                , SerializerWriteString.INSTANCE
                , new SerializerReadClass(TicketLineInfo.class)).list(ticket.getId()));  
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
                    , "INSERT INTO RECEIPTS (ID, MONEY, DATENEW) VALUES (?, ?, ?)"
                    , new SerializerWrite<TicketInfo>() {
                    public void writeValues(DataWrite dp, TicketInfo value) throws BasicException {
                        dp.setString(1, value.getId());
                        dp.setString(2, value.getActiveCash());
                        dp.setTimestamp(3, value.getDate());             
                    }})
                    .exec(ticket);
                
                // new ticket
                new PreparedSentence(s
                    , "INSERT INTO TICKETS (ID, TICKETID, PERSON, CUSTOMER) VALUES (?, ?, ?, ?)"
                    , new SerializerWrite<TicketInfo>() {
                    public void writeValues(DataWrite dp, TicketInfo value) throws BasicException {
                        dp.setString(1, value.getId());
                        dp.setInt(2, value.getTicketId());
                        dp.setString(3, value.getUser().getId());
                        dp.setString(4, value.getCustomerId());            
                    }})
                    .exec(ticket);                
                
                SentenceExec ticketlineinsert = new PreparedSentence(s
                    , "INSERT INTO TICKETLINES (TICKET, LINE, PRODUCT, NAME, ISCOM, UNITS, PRICE, TAXID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
                    , SerializerWriteBuilder.INSTANCE); 
                
                for (TicketLineInfo l : ticket.getLines()) {
                    ticketlineinsert.exec(l);
                    if (l.getProductID() != null)  {
                        // Hay que actualizar el stock si el hay producto
                        Object[] diary = new Object[7];
                        diary[0] = UUID.randomUUID().toString();
                        diary[1] = ticket.getDate();
                        diary[2] = l.getMultiply() < 0.0 
                                ? MovementReason.IN_REFUND.getKey()
                                : MovementReason.OUT_SALE.getKey();
                        diary[3] = location;
                        diary[4] = l.getProductID() ;
                        diary[5] = new Double(-l.getMultiply());
                        diary[6] = new Double(l.getPrice());                                
                        getStockDiaryInsert().exec(diary);
                    }
                }
                
                SentenceExec paymentinsert = new PreparedSentence(s
                    , "INSERT INTO PAYMENTS (ID, RECEIPT, PAYMENT, TOTAL) VALUES (?, ?, ?, ?)"
                    , new SerializerWriteBasic(new Datas[] {Datas.STRING, Datas.STRING, Datas.STRING, Datas.DOUBLE}));                
                for (PaymentInfo p : ticket.getPayments()) {
                    Object[] payment = new Object[4];
                    payment[0] = UUID.randomUUID().toString();
                    payment[1] = ticket.getId();
                    payment[2] = p.getName();
                    payment[3] = new Double(p.getTotal());
                    paymentinsert.exec(payment);
                } 
                return null;
            }
        };
        t.execute();
    }

    public final void deleteTicket(final TicketInfo ticket, final String location) throws BasicException {
        
        Transaction t = new Transaction(s) {
            public Object transact() throws BasicException {
                
                // actualizamos el inventario.
                Date d = new Date();
                for (int i = 0; i < ticket.getLinesCount(); i++) {
                    if (ticket.getLine(i).getProductID() != null)  {
                        // Hay que actualizar el stock si el hay producto
                        Object[] diary = new Object[7];
                        diary[0] = UUID.randomUUID().toString();
                        diary[1] = d;
                        diary[2] = ticket.getLine(i).getMultiply() >= 0.0 
                                ? MovementReason.IN_REFUND.getKey()
                                : MovementReason.OUT_SALE.getKey();                                
                        diary[3] = location;
                        diary[4] = ticket.getLine(i).getProductID() ;
                        diary[5] = new Double(ticket.getLine(i).getMultiply());
                        diary[6] = new Double(ticket.getLine(i).getPrice());                                
                        getStockDiaryInsert().exec(diary);
                    }
                }   
                // Y borramos el ticket definitivamente
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
                    , "INSERT INTO PRODUCTS (ID, REFERENCE, CODE, NAME, ISCOM, ISSCALE, PRICEBUY, PRICESELL, CATEGORY, TAX, IMAGE, STOCKCOST, STOCKVOLUME) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                    , new SerializerWriteBasicExt(productcatDatas, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12})).exec(params);
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
                    , "UPDATE PRODUCTS SET ID = ?, REFERENCE = ?, CODE = ?, NAME = ?, ISCOM = ?, ISSCALE = ?, PRICEBUY = ?, PRICESELL = ?, CATEGORY = ?, TAX = ?, IMAGE = ?, STOCKCOST = ?, STOCKVOLUME = ? WHERE ID = ?"
                    , new SerializerWriteBasicExt(productcatDatas, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 0})).exec(params);
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
            , new String[] {"ID", "NAME", "RATE"}
            , new String[] {"ID", AppLocal.getIntString("Label.Name"), AppLocal.getIntString("label.dutyrate")}
            , new Datas[] {Datas.STRING, Datas.STRING, Datas.DOUBLE}
            , new Formats[] {Formats.STRING, Formats.STRING, Formats.PERCENT}
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
}
