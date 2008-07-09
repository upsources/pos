//    Openbravo POS is a point of sales application designed for touch screens.
//    http://sourceforge.net/projects/openbravopos
//
//    Copyright (c) 2007 openTrends Solucions i Sistemes, S.L
//    Modified by Openbravo SL on March 22, 2007
//    These modifications are copyright Openbravo SL
//    Author/s: A. Romero
//    You may contact Openbravo SL at: http://www.openbravo.com
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

package com.openbravo.possync;

import java.util.List;
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.ImageUtils;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SerializerRead;
import com.openbravo.data.loader.SerializerReadClass;
import com.openbravo.data.loader.SerializerWrite;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.data.loader.Session;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.loader.Transaction;
import com.openbravo.pos.customers.CustomerInfoExt;
import com.openbravo.pos.forms.BeanFactoryDataSingle;
import com.openbravo.pos.payment.PaymentInfoTicket;
import com.openbravo.pos.ticket.CategoryInfo;
import com.openbravo.pos.ticket.ProductInfoExt;
import com.openbravo.pos.ticket.TaxInfo;
import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.ticket.TicketLineInfo;

/**
 *
 * @author adrianromero
 * Created on 5 de marzo de 2007, 19:56
 *
 */
public abstract class DataLogicIntegration extends BeanFactoryDataSingle {
    
    protected Session s;

    /** Creates a new instance of DataLogicIntegration */
    public DataLogicIntegration() {
    }
    
    public void init(Session s) {
        this.s = s;
    }
     
    public abstract void syncCustomersBefore() throws BasicException;
    
    public abstract void syncCustomer(final CustomerInfoExt customer) throws BasicException;
        
    
    public void syncProductsBefore() throws BasicException {
        new StaticSentence(s, "DELETE FROM PRODUCTS_CAT").exec();
    }     
    
    public void syncTax(final TaxInfo tax) throws BasicException {
        
        Transaction t = new Transaction(s) {
            public Object transact() throws BasicException {
                // Sync the Tax in a transaction
                
                // Try to update                
                if (new PreparedSentence(s, 
                            "UPDATE TAXES SET NAME = ?, RATE = ? WHERE ID = ?", 
                            new SerializerWrite() {
                                public void writeValues(DataWrite dp, Object obj) throws BasicException {
                                    TaxInfo t = (TaxInfo) obj;
                                    dp.setString(1, t.getName());
                                    dp.setDouble(2, t.getRate());
                                    dp.setString(3, t.getId());
                                }
                            }).exec(tax) == 0) {
                       
                    // If not updated, try to insert
                    new PreparedSentence(s, 
                            "INSERT INTO TAXES(ID, NAME, RATE) VALUES (?, ?, ?)", 
                            new SerializerWrite() {
                                public void writeValues(DataWrite dp, Object obj) throws BasicException {
                                    TaxInfo t = (TaxInfo) obj;
                                    dp.setString(1, t.getId());
                                    dp.setString(2, t.getName());
                                    dp.setDouble(3, t.getRate());
                                }
                            }).exec(tax);
                }
                
                return null;
            }
        };
        t.execute();                   
    }
    
    public void syncCategory(final CategoryInfo cat) throws BasicException {
        
        Transaction t = new Transaction(s) {
            public Object transact() throws BasicException {
                // Sync the Category in a transaction
                
                // Try to update
                if (new PreparedSentence(s, 
                            "UPDATE CATEGORIES SET NAME = ?, IMAGE = ? WHERE ID = ?", 
                            new SerializerWrite() {
                                public void writeValues(DataWrite dp, Object obj) throws BasicException {
                                    CategoryInfo c = (CategoryInfo) obj;
                                    dp.setString(1, c.getName());
                                    dp.setBytes(2, ImageUtils.writeImage(c.getImage()));
                                    dp.setString(3, c.getID());
                                }
                            }).exec(cat) == 0) {
                       
                    // If not updated, try to insert
                    new PreparedSentence(s, 
                        "INSERT INTO CATEGORIES(ID, NAME, IMAGE) VALUES (?, ?, ?)", 
                        new SerializerWrite() {
                            public void writeValues(DataWrite dp, Object obj) throws BasicException {
                                CategoryInfo c = (CategoryInfo) obj;
                                dp.setString(1, c.getID());
                                dp.setString(2, c.getName());
                                dp.setBytes(3, ImageUtils.writeImage(c.getImage()));
                            }
                        }).exec(cat);
                }
                return null;        
            }
        };
        t.execute();        
    }    
    
    public void syncProduct(final ProductInfoExt prod) throws BasicException {
        
        Transaction t = new Transaction(s) {
            public Object transact() throws BasicException {
                // Sync the Product in a transaction
                
                // Try to update
                if (new PreparedSentence(s, 
                            "UPDATE PRODUCTS SET REFERENCE = ?, CODE = ?, NAME = ?, PRICEBUY = ?, PRICESELL = ?, CATEGORY = ?, TAX = ?, IMAGE = ? WHERE ID = ?", 
                            new SerializerWrite() {
                                public void writeValues(DataWrite dp, Object obj) throws BasicException {
                                    ProductInfoExt p = (ProductInfoExt) obj;
                                    dp.setString(1, p.getReference());
                                    dp.setString(2, p.getCode());
                                    dp.setString(3, p.getName());
                                    // dp.setBoolean(x, p.isCom());
                                    // dp.setBoolean(x, p.isScale());
                                    dp.setDouble(4, p.getPriceBuy());
                                    dp.setDouble(5, p.getPriceSell());
                                    dp.setString(6, p.getCategoryID());
                                    dp.setString(7, p.getTaxID());
                                    dp.setBytes(8, ImageUtils.writeImage(p.getImage()));
                                    // dp.setDouble(x, 0.0);
                                    // dp.setDouble(x, 0.0);
                                    dp.setString(9, p.getID());                    
                                }
                            }).exec(prod) == 0) {
                            
                    // If not updated, try to insert
                    new PreparedSentence(s, 
                        "INSERT INTO PRODUCTS (ID, REFERENCE, CODE, NAME, ISCOM, ISSCALE, PRICEBUY, PRICESELL, CATEGORY, TAX, IMAGE, STOCKCOST, STOCKVOLUME) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                         new SerializerWrite() {
                            public void writeValues(DataWrite dp, Object obj) throws BasicException {
                                ProductInfoExt p = (ProductInfoExt) obj;
                                dp.setString(1, p.getID());
                                dp.setString(2, p.getReference());
                                dp.setString(3, p.getCode());
                                dp.setString(4, p.getName());
                                dp.setBoolean(5, p.isCom());
                                dp.setBoolean(6, p.isScale());
                                dp.setDouble(7, p.getPriceBuy());
                                dp.setDouble(8, p.getPriceSell());
                                dp.setString(9, p.getCategoryID());
                                dp.setString(10, p.getTaxID());
                                dp.setBytes(11, ImageUtils.writeImage(p.getImage()));
                                dp.setDouble(12, 0.0);
                                dp.setDouble(13, 0.0);
                            }
                        }).exec(prod);
                }
                        
                // Insert in catalog
                new StaticSentence(s, 
                    "INSERT INTO PRODUCTS_CAT(PRODUCT, CATORDER) VALUES (?, NULL)",
                    new SerializerWrite() {
                        public void writeValues(DataWrite dp, Object obj) throws BasicException {
                            ProductInfoExt p = (ProductInfoExt) obj;
                            dp.setString(1, p.getID());                    
                        }
                    }).exec(prod);   
                
                return null;        
            }
        };
        t.execute();     
    }
    
    public List getTickets() throws BasicException {
        return new PreparedSentence(s
                , "SELECT T.ID, T.TICKETID, R.DATENEW, R.MONEY, P.ID, P.NAME, C.ID, C.TAXID, C.SEARCHKEY, C.NAME FROM RECEIPTS R JOIN TICKETS T ON R.ID = T.ID LEFT OUTER JOIN PEOPLE P ON T.PERSON = P.ID LEFT OUTER JOIN CUSTOMERS C ON T.CUSTOMER = C.ID WHERE T.STATUS = 0"
                , null
                , new SerializerReadClass(TicketInfo.class)).list();
    }
    public List getTicketLines(final String ticket) throws BasicException {
        return new PreparedSentence(s
                , "SELECT L.TICKET, L.LINE, L.PRODUCT, L.NAME, L.ISCOM, L.UNITS, L.PRICE, T.ID, T.RATE, L.ATTRIBUTES FROM TICKETLINES L, TAXES T WHERE L.TAXID = T.ID AND L.TICKET = ?"
                , SerializerWriteString.INSTANCE
                , new SerializerReadClass(TicketLineInfo.class)).list(ticket);
    }
    public List getTicketPayments(final String ticket) throws BasicException {
        return new PreparedSentence(s
                , "SELECT TOTAL, PAYMENT FROM PAYMENTS WHERE RECEIPT = ?"
                , SerializerWriteString.INSTANCE
                , new SerializerRead() {
                    public Object readValues(DataRead dr) throws BasicException {
                        return new PaymentInfoTicket(
                                dr.getDouble(1),
                                dr.getString(2));
                    }                
                }).list(ticket);
    }    

    public void execTicketUpdate() throws BasicException {
        new StaticSentence(s, "UPDATE TICKETS SET STATUS = 1 WHERE STATUS = 0").exec();
    }
}
