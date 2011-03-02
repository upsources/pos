//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007 Openbravo, S.L.
//    http://sourceforge.net/projects/openbravopos
//
//    This file is modified as part of fastfood branch of Openbravo POS. 2008
//    These modifications are copyright Open Sistemas de Información Internet, S.L.
//    http://www.opensistemas.com
//    e-mail: imasd@opensistemas.com
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

import java.util.Date;
import java.util.List;
import java.util.UUID;
import com.openbravo.data.loader.*;
import com.openbravo.format.Formats;
import com.openbravo.basic.BasicException;
import com.openbravo.pos.inventory.LocationInfo;
import com.openbravo.pos.inventory.MaterialProdInfo;
import com.openbravo.pos.inventory.MovementReason;
import com.openbravo.pos.mant.FloorsInfo;
import com.openbravo.pos.payment.PaymentInfo;
import com.openbravo.pos.payment.PaymentInfoTicket;
import com.openbravo.pos.ticket.TariffInfo;
import com.openbravo.pos.ticket.CategoryInfo;
import com.openbravo.pos.ticket.SubgroupInfo;
import com.openbravo.pos.ticket.ProductInfoExt;
import com.openbravo.pos.ticket.TaxInfo;
import com.openbravo.pos.ticket.DiscountInfo;
import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.ticket.TicketLineInfo;

/**
 *
 * @author adrianromero
 * Modified by:
 * @author  Luis Ig. Bacas Riveiro	lbacas@opensistemas.com
 * @author  Pablo J. Urbano Santos	purbano@opensistemas.com
 */
public abstract class DataLogicSales extends BeanFactoryDataSingle {
    
    protected Session s;

    protected Datas[] stockdiaryDatas;
    protected Datas[] productcatDatas;
    protected Datas[] compositionDatas;
    protected Datas[] subgroupDatas;
    protected Datas[] subgroup_prodDatas;
    protected Datas[] paymenttabledatas;
    protected Datas[] stockdatas;
    protected Datas[] tariffareaDatas;
    protected Datas[] tariffprodDatas;
    protected Datas[] materialDatas;
    protected Datas[] categoryDatas;
    
    /** Creates a new instance of SentenceContainerGeneric */
    public DataLogicSales() {
        productcatDatas = new Datas[] {Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.BOOLEAN, Datas.BOOLEAN, Datas.DOUBLE, Datas.DOUBLE, Datas.STRING, Datas.STRING, Datas.IMAGE, Datas.DOUBLE, Datas.DOUBLE, Datas.BOOLEAN, Datas.INT, Datas.BYTES};
        compositionDatas = new Datas[] {Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.BOOLEAN, Datas.BOOLEAN, Datas.DOUBLE, Datas.DOUBLE, Datas.STRING, Datas.STRING, Datas.IMAGE, Datas.BOOLEAN, Datas.INT, Datas.BYTES};
        subgroupDatas = new Datas[] {Datas.STRING, Datas.STRING, Datas.STRING, Datas.IMAGE};
        subgroup_prodDatas = new Datas[] {Datas.STRING, Datas.STRING};
        stockdiaryDatas = new Datas[] {Datas.STRING, Datas.TIMESTAMP, Datas.INT, Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.DOUBLE};
        paymenttabledatas = new Datas[] {Datas.STRING, Datas.STRING, Datas.TIMESTAMP, Datas.STRING, Datas.STRING, Datas.DOUBLE};
        stockdatas = new Datas[] {Datas.STRING, Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.DOUBLE, Datas.DOUBLE};
        tariffareaDatas = new Datas[] {Datas.STRING, Datas.STRING, Datas.INT};
        tariffprodDatas = new Datas[] {Datas.STRING, Datas.STRING, Datas.DOUBLE};
        materialDatas = new Datas[] {Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.BOOLEAN, Datas.BOOLEAN, Datas.DOUBLE, Datas.DOUBLE, Datas.STRING, Datas.STRING, Datas.IMAGE, Datas.DOUBLE, Datas.DOUBLE, Datas.STRING, Datas.DOUBLE, Datas.DOUBLE, Datas.BYTES};
        categoryDatas = new Datas[] {Datas.STRING, Datas.STRING, Datas.STRING, Datas.IMAGE};
    }
    public void init(Session s){        
        this.s = s;  
    }      
    
    // Utilidades de productos
    public final ProductInfoExt getProductInfo(String id) throws BasicException {
        return (ProductInfoExt) new PreparedSentence(s
            , "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAX, T.NAME, T.RATE, P.CATEGORY, P.IMAGE, P.ATTRIBUTES " +
              "FROM PRODUCTS P LEFT OUTER JOIN TAXES T ON P.TAX = T.ID WHERE P.ID = ?"
            , SerializerWriteString.INSTANCE
            , new SerializerReadClass(ProductInfoExt.class)).find(id);
    }
    public final ProductInfoExt getProductInfoByCode(String sCode) throws BasicException {
        return (ProductInfoExt) new PreparedSentence(s
            , "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAX, T.NAME, T.RATE, P.CATEGORY, P.IMAGE, P.ATTRIBUTES " +
              "FROM PRODUCTS P LEFT OUTER JOIN TAXES T ON P.TAX = T.ID WHERE P.CODE = ?"
            , SerializerWriteString.INSTANCE
            , new SerializerReadClass(ProductInfoExt.class)).find(sCode);
    }
    public final ProductInfoExt getProductInfoByReference(String sReference) throws BasicException {
        return (ProductInfoExt) new PreparedSentence(s
            , "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAX, T.NAME, T.RATE, P.CATEGORY, P.IMAGE, P.ATTRIBUTES " +
              "FROM PRODUCTS P LEFT OUTER JOIN TAXES T ON P.TAX = T.ID WHERE P.REFERENCE = ?"
            , SerializerWriteString.INSTANCE
            , new SerializerReadClass(ProductInfoExt.class)).find(sReference);
    }
    
    // Catalogo de productos
    public final List<CategoryInfo> getRootCategories() throws BasicException {
        return new PreparedSentence(s
            , "SELECT ID, NAME, IMAGE FROM CATEGORIES WHERE ID >= 0 AND PARENTID IS NULL ORDER BY NAME"
            , null
            , new SerializerReadClass(CategoryInfo.class)).list();
    }
    public final List<CategoryInfo> getSubcategories(String category) throws BasicException  {
        return new PreparedSentence(s
            , "SELECT ID, NAME, IMAGE FROM CATEGORIES WHERE PARENTID = ? ORDER BY NAME"
            , SerializerWriteString.INSTANCE
            , new SerializerReadClass(CategoryInfo.class)).list(category);
    }
    
    //Subgrupos de una composición
    public final List<SubgroupInfo> getSubgroups(String composition) throws BasicException  {
        return new PreparedSentence(s
            , "SELECT ID, NAME, IMAGE FROM SUBGROUPS WHERE COMPOSITION = ? ORDER BY NAME"
            , SerializerWriteString.INSTANCE
            , new SerializerReadClass(SubgroupInfo.class)).list(composition);
    }
    
    //Productos de un grupo de tarifas
    public final List<ProductInfoExt> getTariffProds(String area) throws BasicException  {
        return new PreparedSentence(s
            , "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, TAP.PRICESELL, P.TAX, T.NAME, T.RATE, P.CATEGORY, P.IMAGE, P.ATTRIBUTES " +
              "FROM PRODUCTS P LEFT OUTER JOIN TAXES T ON P.TAX = T.ID " +
              "		LEFT OUTER JOIN TARIFFAREAS_PROD TAP ON P.ID = TAP.PRODUCTID " +
              "		LEFT OUTER JOIN TARIFFAREAS TA ON TA.ID = TAP.TARIFFID " +
              "WHERE TA.ID = ? ORDER BY P.NAME"
            , SerializerWriteString.INSTANCE
            , new SerializerReadClass(ProductInfoExt.class)).list(area);
    }
    
    //Producto de un subgrupo de una composicion
    public final List<ProductInfoExt> getSubgroupCatalog(String subgroup) throws BasicException  {
            return new PreparedSentence(s
            , "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAX, T.NAME, T.RATE, P.CATEGORY, P.IMAGE, P.ATTRIBUTES " +
              "FROM SUBGROUPS S INNER JOIN SUBGROUPS_PROD SP ON S.ID = SP.SUBGROUP LEFT OUTER JOIN PRODUCTS P ON SP.PRODUCT = P.ID RIGHT OUTER JOIN TAXES T ON P.TAX = T.ID " +
              "WHERE S.ID = ? " +
              "ORDER BY P.NAME"
            , SerializerWriteString.INSTANCE
            , new SerializerReadClass(ProductInfoExt.class)).list(subgroup);
    }

    public final List<CategoryInfo> getCategoryComposition() throws BasicException {
        return new PreparedSentence(s
            , "SELECT ID, NAME, IMAGE FROM CATEGORIES WHERE ID = '0'"
            , null
            , new SerializerReadClass(CategoryInfo.class)).list();
    }
    
    public final List<CategoryInfo> getCategoryMaterial() throws BasicException {
        return new PreparedSentence(s
            , "SELECT ID, NAME, IMAGE FROM CATEGORIES WHERE ID = '-1'"
            , null
            , new SerializerReadClass(CategoryInfo.class)).list();
    }

    public final List<ProductInfoExt> getProductCatalog(String category) throws BasicException  {
        return new PreparedSentence(s
            , "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAX, T.NAME, T.RATE, P.CATEGORY, P.IMAGE, P.ATTRIBUTES " +
              "FROM PRODUCTS P LEFT OUTER JOIN TAXES T ON P.TAX = T.ID LEFT OUTER JOIN CATEGORIES C ON P.CATEGORY = C.ID, PRODUCTS_CAT O WHERE P.ID = O.PRODUCT AND P.CATEGORY = ?" +
              "ORDER BY C.NAME, O.CATORDER, P.REFERENCE"
            , SerializerWriteString.INSTANCE
            , new SerializerReadClass(ProductInfoExt.class)).list(category);
    }
    
    public final List<ProductInfoExt> getProductStockable(String category) throws BasicException  {
        return new PreparedSentence(s
            , "SELECT DISTINCT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAX, T.NAME, T.RATE, P.CATEGORY, P.IMAGE, P.ATTRIBUTES " +
              "FROM PRODUCTS P LEFT OUTER JOIN TAXES T ON P.TAX = T.ID LEFT OUTER JOIN CATEGORIES C ON P.CATEGORY = C.ID, PRODUCTS_CAT O " + 
              "WHERE P.ID NOT IN (SELECT DISTINCT PRODUCT FROM PRODUCTS_MAT) AND P.CATEGORY = ? " +
              "ORDER BY P.CATEGORY, P.NAME"
            , SerializerWriteString.INSTANCE
            , new SerializerReadClass(ProductInfoExt.class)).list(category);
    }
    
    public final List<ProductInfoExt> getProductComments(String id) throws BasicException {
        return new PreparedSentence(s
            , "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAX, T.NAME, T.RATE, P.CATEGORY, P.IMAGE, P.ATTRIBUTES " +
              "FROM PRODUCTS P LEFT OUTER JOIN TAXES T ON P.TAX = T.ID, PRODUCTS_CAT O, PRODUCTS_COM M WHERE P.ID = O.PRODUCT AND P.ID = M.PRODUCT2 AND M.PRODUCT = ? " +
              "ORDER BY O.CATORDER, P.NAME"
            , SerializerWriteString.INSTANCE 
            , new SerializerReadClass(ProductInfoExt.class)).list(id);
    }
    
    //Productos de un grupo de tarifas
    public final List<MaterialProdInfo> getMaterialsProd(String prodID) throws BasicException  {
        String sAmount = "1 AS AMOUNT";
        String sWhere = "WHERE P.CATEGORY = ? ";
        String param = "-1";
        
        if (prodID != null && !prodID.equals("") ) {
            sAmount = "PM.AMOUNT";
            sWhere = "WHERE PM.PRODUCT = ? ";
            param = prodID;
        }   
        return new PreparedSentence(s
            , "SELECT DISTINCT P.ID, P.NAME, P.PRICEBUY, "+ sAmount +", U.NAME " +
              "FROM PRODUCTS P LEFT OUTER JOIN PRODUCTS_MAT PM ON P.ID = PM.MATERIAL INNER JOIN MATERIALS_UNITS MU ON MU.MATERIAL = P.ID INNER JOIN UNITS U ON MU.UNIT = U.ID " +
              sWhere +"ORDER BY P.NAME"
            , SerializerWriteString.INSTANCE
            , new SerializerReadClass(MaterialProdInfo.class)).list(param);
    }
    
    
    // Products list
    public final SentenceList getProductList() {
        return new StaticSentence(s
            , new QBFBuilder("SELECT DISTINCT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAX, T.NAME, T.RATE, P.CATEGORY, P.IMAGE, P.ATTRIBUTES FROM PRODUCTS P LEFT OUTER JOIN TAXES T ON P.TAX = T.ID, PRODUCTS PR LEFT OUTER JOIN CATEGORIES C ON PR.CATEGORY = C.ID WHERE P.CATEGORY NOT LIKE '0' AND P.CATEGORY NOT LIKE '-1' AND ?(QBF_FILTER) ORDER BY P.REFERENCE", new String[] {"P.NAME", "P.PRICEBUY", "P.PRICESELL", "P.CATEGORY", "P.CODE"})
            , new SerializerWriteBasic(new Datas[] {Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.STRING})
            , new SerializerReadClass(ProductInfoExt.class));
    }
    
        // Products list

    public final SentenceList getCompositionList() {
        return new StaticSentence(s
            , new QBFBuilder("SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAX, T.NAME, T.RATE, P.CATEGORY, P.IMAGE, P.ATTRIBUTES FROM PRODUCTS P LEFT OUTER JOIN TAXES T ON P.TAX = T.ID, CATEGORIES C WHERE P.CATEGORY = C.ID AND C.ID LIKE '0' AND ?(QBF_FILTER) ORDER BY P.NAME", new String[] {"P.NAME", "P.PRICESELL", "P.CODE"})
            , new SerializerWriteBasic(new Datas[] {Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.STRING})
            , new SerializerReadClass(ProductInfoExt.class));
    }

    // Listados para combo de materiales
    public final SentenceList getMaterialList() {
        return new StaticSentence(s
            , "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAX, T.NAME, T.RATE, P.CATEGORY, P.IMAGE, P.ATTRIBUTES FROM PRODUCTS P LEFT OUTER JOIN TAXES T ON P.TAX = T.ID WHERE P.CATEGORY = '-1'ORDER BY P.NAME"
            , null
            , new SerializerReadClass(ProductInfoExt.class));
    }
    
    // Listados para combo        
    public final SentenceList getTaxList() {
        return new StaticSentence(s
            , "SELECT ID, NAME, RATE FROM TAXES WHERE ID >= 0 ORDER BY NAME"
            , null
            , new SerializerReadClass(TaxInfo.class));
    }
    
    public final SentenceList getDiscountList() {
        return new StaticSentence(s
            , "SELECT ID, NAME, QUANTITY, PERCENTAGE FROM DISCOUNTS ORDER BY NAME"
            , null
            , new SerializerReadClass(DiscountInfo.class));
    }
    
    public final SentenceList getCategoriesList() {
        return new StaticSentence(s
            , "SELECT ID, NAME, IMAGE FROM CATEGORIES WHERE ID >= 0 ORDER BY NAME"
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

//TODO: ¿Se necesita crear una clase para ello?
    public final SentenceList getUnitsList() {
        return new StaticSentence(s
            , "SELECT ID, NAME, SYMBOL FROM UNITS ORDER BY NAME"
            , null
            , new SerializerReadClass(FloorsInfo.class));
    }

    // Una lista de grupos de tarifas para el combo
    
    public final SentenceList getTariffAreaList() {
        return new StaticSentence(s
            , "SELECT ID, NAME, TARIFFORDER FROM TARIFFAREAS ORDER BY TARIFFORDER"
            , null
            , new SerializerReadClass(TariffInfo.class));
    }
    
    
    
    public final List<TicketInfo> getTickets(String timedate) throws BasicException {
        List<TicketInfo> tickets = (List<TicketInfo>) new PreparedSentence(s
            , "SELECT T.ID, T.TICKETID, R.DATENEW, R.MONEY, P.ID, P.NAME, C.ID, C.TAXID, C.NAME, T.DISCOUNTNAME, T.DISCOUNTVALUE, T.TARIFFAREA " +
              "FROM RECEIPTS R JOIN TICKETS T ON R.ID = T.ID LEFT OUTER JOIN PEOPLE P ON T.PERSON = P.ID LEFT OUTER JOIN CUSTOMERS C ON T.CUSTOMER = C.ID " +
              "WHERE R.DATENEW > ?"
            , SerializerWriteString.INSTANCE
            , new SerializerReadClass(TicketInfo.class)).list(timedate);
        
        for (TicketInfo t : tickets) {
            t.setLines(new PreparedSentence(s
                , "SELECT L.TICKET, L.LINE, L.PRODUCT, L.NAME, L.ISCOM, L.ISDISCOUNT, L.UNITS, L.PRICE, T.ID, T.RATE, L.ATTRIBUTES FROM TICKETLINES L, TAXES T WHERE L.TAXID = T.ID AND L.TICKET = ? ORDER BY L.TICKET, L.LINE"
                , SerializerWriteString.INSTANCE
                , new SerializerReadClass(TicketLineInfo.class)).list(t.getId()));
            
            t.applySpecialLines();
        }
        return tickets;
    }
    
    
    public final TicketInfo loadTicket(Integer ticketid) throws BasicException {
        TicketInfo ticket = (TicketInfo) new PreparedSentence(s
                , "SELECT T.ID, T.TICKETID, R.DATENEW, R.MONEY, P.ID, P.NAME, C.ID, C.TAXID, C.NAME, T.DISCOUNTNAME, T.DISCOUNTVALUE, T.TARIFFAREA FROM RECEIPTS R JOIN TICKETS T ON R.ID = T.ID LEFT OUTER JOIN PEOPLE P ON T.PERSON = P.ID LEFT OUTER JOIN CUSTOMERS C ON T.CUSTOMER = C.ID WHERE T.TICKETID = ?"
                , SerializerWriteInteger.INSTANCE
                , new SerializerReadClass(TicketInfo.class)).find(ticketid);
        if (ticket != null) {
            ticket.setLines(new PreparedSentence(s
                , "SELECT L.TICKET, L.LINE, L.PRODUCT, L.NAME, L.ISCOM, L.ISDISCOUNT, L.UNITS, L.PRICE, T.ID, T.RATE, L.ATTRIBUTES FROM TICKETLINES L, TAXES T WHERE L.TAXID = T.ID AND L.TICKET = ? ORDER BY L.TICKET, L.LINE"
                , SerializerWriteString.INSTANCE
                , new SerializerReadClass(TicketLineInfo.class)).list(ticket.getId()));
            ticket.setPayments(new PreparedSentence(s
                , "SELECT PAYMENT, TOTAL FROM PAYMENTS WHERE RECEIPT = ?"
                , SerializerWriteString.INSTANCE
                , new SerializerReadClass(PaymentInfoTicket.class)).list(ticket.getId()));  
            
            ticket.applySpecialLines();
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
                    , "INSERT INTO TICKETS (ID, TICKETID, PERSON, CUSTOMER, DISCOUNTNAME, DISCOUNTVALUE, TARIFFAREA) VALUES (?, ?, ?, ?, ?, ?, ?)"
                    , new SerializerWrite<TicketInfo>() {
                    public void writeValues(DataWrite dp, TicketInfo value) throws BasicException {
                        dp.setString(1, value.getId());
                        dp.setInt(2, value.getTicketId());
                        dp.setString(3, value.getUser().getId());
                        dp.setString(4, value.getCustomerId());
                        dp.setString(5, (value.getGlobalDiscount() != null)
                                ? value.getGlobalDiscount().getName()
                                : null);
                        dp.setString(6, (value.getGlobalDiscount() != null)
                                ? value.getGlobalDiscount().getValue()
                                : null);
                        dp.setString(7, value.getTariffArea());
                    }})
                    .exec(ticket);                
                
                SentenceExec ticketlineinsert = new PreparedSentence(s
                    , "INSERT INTO TICKETLINES (TICKET, LINE, PRODUCT, NAME, ISCOM, ISDISCOUNT, UNITS, PRICE, TAXID, ATTRIBUTES) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                    , SerializerWriteBuilder.INSTANCE); 
                
                for (TicketLineInfo l : ticket.getLines()) {
                    ticketlineinsert.exec(l);
                    if (l.getProduct().getId() != null)  {
                        // update the stock
                        List<MaterialProdInfo> mats = getMaterialsProd(l.getProduct().getId());

                        Object[] diary = new Object[7];
                        diary[0] = UUID.randomUUID().toString();
                        diary[1] = ticket.getDate();
                        diary[2] = l.getMultiply() < 0.0 
                                ? MovementReason.IN_REFUND.getKey()
                                : MovementReason.OUT_SALE.getKey();
                        diary[3] = location;
                        diary[4] = l.getProduct().getId();
                        diary[6] = new Double(l.getPrice());
                        
                        if (mats.size() == 0) {
                            //Sino tiene escandallo, descontamos la cantidad del producto del stock
                            diary[5] = new Double(-l.getMultiply());
                            getStockDiaryInsert().exec(diary);
                        } else {
                            //Si tiene escandallo, no descontamos la cantidad del producto del stock
                            //y descontamos del stock los materiales usados
                            diary[5] = new Double(0.0);
                            getStockDiaryInsert().exec(diary);
                            
                            for (MaterialProdInfo m: mats) {
                                diary = new Object[7];
                                diary[0] = UUID.randomUUID().toString();
                                diary[1] = ticket.getDate();
                                diary[2] = l.getMultiply() < 0.0 
                                        ? MovementReason.IN_REFUND.getKey()
                                        : MovementReason.OUT_SALE.getKey();
                                diary[3] = location;
                                diary[4] = m.getID();
                                diary[5] = new Double( -(l.getMultiply() * m.getAmount()) );
                                diary[6] = new Double(0.0);
                                getStockDiaryInsert().exec(diary);
                            }
                        }
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
                    // Hay que actualizar el stock si el hay producto
                    if (ticket.getLine(i).getProduct().getId() != null)  {
                        List<MaterialProdInfo> mats = getMaterialsProd(ticket.getLine(i).getProduct().getId());
                        
                        Object[] diary = new Object[7];
                        diary[0] = UUID.randomUUID().toString();
                        diary[1] = d;
                        diary[2] = ticket.getLine(i).getMultiply() >= 0.0 
                                ? MovementReason.IN_REFUND.getKey()
                                : MovementReason.OUT_SALE.getKey();                                
                        diary[3] = location;
                        diary[4] = ticket.getLine(i).getProduct().getId();
                        diary[6] = new Double(ticket.getLine(i).getPrice());                                
                        
                        if (mats.size() == 0) {
                            //Sino tiene escandallo, aumentamos la cantidad del producto del stock
                            diary[5] = new Double(ticket.getLine(i).getMultiply());
                            getStockDiaryInsert().exec(diary);
                        } else {
                            //Si tiene escandallo, no aumentamos la cantidad del producto del stock
                            //y descontamos del stock los materiales usados
                            diary[5] = new Double(0.0);
                            getStockDiaryInsert().exec(diary);
                            
                            for (MaterialProdInfo m: mats) {
                                diary = new Object[7];
                                diary[0] = UUID.randomUUID().toString();
                                diary[1] = d;
                                diary[2] = ticket.getLine(i).getMultiply() >= 0.0 
                                        ? MovementReason.IN_REFUND.getKey()
                                        : MovementReason.OUT_SALE.getKey();                                
                                diary[3] = location;
                                diary[4] = ticket.getLine(i).getProduct().getId();
                                diary[5] = new Double( -(ticket.getLine(i).getMultiply() * m.getAmount()) );
                                diary[6] = new Double(0.0);
                                getStockDiaryInsert().exec(diary);
                            }
                        }
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

    public final SentenceList getCompositionQBF() {
        return new StaticSentence(s
            , new QBFBuilder("SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.CATEGORY, P.TAX, P.IMAGE, NOT (C.PRODUCT IS NULL), C.CATORDER, P.ATTRIBUTES FROM PRODUCTS P LEFT OUTER JOIN PRODUCTS_CAT C ON P.ID = C.PRODUCT WHERE P.CATEGORY LIKE '0' AND ?(QBF_FILTER) ORDER BY P.NAME", new String[] {"P.NAME", "P.PRICESELL", "P.CODE"})
            , new SerializerWriteBasic(new Datas[] {Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.STRING})
            , new SerializerReadBasic(compositionDatas));
    }
    
        public final SentenceList getSingleProductCatQBF() {
        return new StaticSentence(s
            , new QBFBuilder("SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.CATEGORY, P.TAX, P.IMAGE, P.STOCKCOST, P.STOCKVOLUME, NOT (C.PRODUCT IS NULL), C.CATORDER, P.ATTRIBUTES FROM PRODUCTS P LEFT OUTER JOIN PRODUCTS_CAT C ON P.ID = C.PRODUCT WHERE P.CATEGORY >= 0 AND P.CATEGORY NOT LIKE '0' AND ?(QBF_FILTER) ORDER BY P.NAME", new String[] {"P.NAME", "P.PRICEBUY", "P.PRICESELL", "P.CATEGORY", "P.CODE"})
            , new SerializerWriteBasic(new Datas[] {Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.STRING})
            , new SerializerReadBasic(productcatDatas));
    }   


    public final SentenceExec getProductCatInsert() {
        return new SentenceExecTransaction(s) {
            public int execInTransaction(Object params) throws BasicException {
                Object[] values = (Object[]) params;            
                int i = new PreparedSentence(s
                    , "INSERT INTO PRODUCTS (ID, REFERENCE, CODE, NAME, ISCOM, ISSCALE, PRICEBUY, PRICESELL, CATEGORY, TAX, IMAGE, STOCKCOST, STOCKVOLUME, ATTRIBUTES) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                    , new SerializerWriteBasicExt(productcatDatas, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 15})).exec(params);
                if (i > 0 && ((Boolean)values[13]).booleanValue()) {
                    i = new PreparedSentence(s
                        , "INSERT INTO PRODUCTS_CAT (PRODUCT, CATORDER) VALUES (?, ?)"
                        , new SerializerWriteBasicExt(productcatDatas, new int[] {0, 14})).exec(params);
                }
                
                if (i > 0)
                    i = auxMaterialProd(values, s);
                
                return i;
            }
        };
    }
    
    public final SentenceExec getProductCatUpdate() {
        return new SentenceExecTransaction(s) {
            public int execInTransaction(Object params) throws BasicException {
                Object[] values = (Object[]) params;            
                int i = new PreparedSentence(s
                    , "UPDATE PRODUCTS SET ID = ?, REFERENCE = ?, CODE = ?, NAME = ?, ISCOM = ?, ISSCALE = ?, PRICEBUY = ?, PRICESELL = ?, CATEGORY = ?, TAX = ?, IMAGE = ?, STOCKCOST = ?, STOCKVOLUME = ?, ATTRIBUTES = ? WHERE ID = ?"
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
                
                if (i > 0)
                    i = auxMaterialProd(values, s);
                
                return i;
            }        
        };
    }
    
    private int auxMaterialProd(Object params, Session s) throws BasicException {
        Object[] values = (Object[]) params;
        int i = 1;
        
        //Eliminamos los subgrupos que pudiese tener
        new PreparedSentence(s
            , "DELETE FROM PRODUCTS_MAT WHERE PRODUCT = ?"
            , new SerializerWriteBasicExt(subgroupDatas, new int[] {0})).exec(params);                 

        //Por cada subgrupo
        int size = ((Integer)values[16]).intValue();
        int cont = 17;
        for (int e = 0; e < size && i > 0; e++) {
            Object[] sparams = new Object[] {values[0], values[cont++], values[cont++]};
            
            i = new PreparedSentence(s
                , "INSERT INTO PRODUCTS_MAT (PRODUCT, MATERIAL, AMOUNT) VALUES (?, ?, ?)"
                , new SerializerWriteBasicExt(new Datas[] {Datas.STRING, Datas.STRING, Datas.DOUBLE}
                                            , new int[] {0, 1, 2})
                ).exec(sparams);
        }
        
        return i;
    }

    public final SentenceExec getProductCatDelete() {
        return new SentenceExecTransaction(s) {
            public int execInTransaction(Object params) throws BasicException {
                new PreparedSentence(s
                    , "DELETE FROM PRODUCTS_MAT WHERE PRODUCT = ?"
                    , new SerializerWriteBasicExt(productcatDatas, new int[] {0})).exec(params);
                new PreparedSentence(s
                    , "DELETE FROM PRODUCTS_CAT WHERE PRODUCT = ?"
                    , new SerializerWriteBasicExt(productcatDatas, new int[] {0})).exec(params);
                return new PreparedSentence(s
                    , "DELETE FROM PRODUCTS WHERE ID = ?"
                    , new SerializerWriteBasicExt(productcatDatas, new int[] {0})).exec(params);
            } 
        };
    }

    public final SentenceList getMaterialQBF() {
        return new StaticSentence(s
            , new QBFBuilder("SELECT DISTINCT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.CATEGORY, P.TAX, P.IMAGE, P.STOCKCOST, P.STOCKVOLUME, MU.UNIT, MU.AMOUNT, MU.PRICEBUY, P.ATTRIBUTES FROM PRODUCTS P LEFT OUTER JOIN MATERIALS_UNITS MU ON P.ID = MU.MATERIAL LEFT OUTER JOIN PRODUCTS_MAT PM ON P.ID = PM.MATERIAL WHERE P.CATEGORY = '-1' AND ?(QBF_FILTER) ORDER BY P.NAME", new String[] {"P.NAME", "P.PRICEBUY", "PM.PRODUCT"})
            , new SerializerWriteBasic(new Datas[] {Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.STRING})
            , new SerializerReadBasic(materialDatas));
    }
    
    public final SentenceExec getMaterialInsert() {
        return new SentenceExecTransaction(s) {
            public int execInTransaction(Object params) throws BasicException {
                int i =  new PreparedSentence(s
                    , "INSERT INTO PRODUCTS (ID, REFERENCE, CODE, NAME, ISCOM, ISSCALE, PRICEBUY, PRICESELL, CATEGORY, TAX, IMAGE, STOCKCOST, STOCKVOLUME, ATTRIBUTES) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                    , new SerializerWriteBasicExt(materialDatas, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 16})).exec(params);
                if (i > 0)
                    i = new PreparedSentence(s
                        , "INSERT INTO MATERIALS_UNITS (MATERIAL, UNIT, AMOUNT, PRICEBUY) VALUES (?, ?, ?, ?)"
                        , new SerializerWriteBasicExt(materialDatas, new int[]{0, 13, 14, 15})).exec(params);
                    
                return i;
            }
        };
    }
    
    public final SentenceExec getMaterialUpdate() {
        return new SentenceExecTransaction(s) {
            public int execInTransaction(Object params) throws BasicException {
                int i =  new PreparedSentence(s
                    , "UPDATE PRODUCTS SET ID = ?, REFERENCE = ?, CODE = ?, NAME = ?, ISSCALE = ?, PRICEBUY = ?, IMAGE = ?, STOCKCOST = ?, STOCKVOLUME = ?, ATTRIBUTES = ? WHERE ID = ?"
                    , new SerializerWriteBasicExt(materialDatas, new int[]{0, 1, 2, 3, 5, 6, 10, 11, 12, 16, 0})).exec(params);
                if (i > 0)
                    if ((i = new PreparedSentence(s
                            , "UPDATE MATERIALS_UNITS SET MATERIAL = ?, UNIT = ?, AMOUNT = ?, PRICEBUY = ? WHERE MATERIAL = ?"
                            , new SerializerWriteBasicExt(materialDatas, new int[]{0, 13, 14, 15, 0})).exec(params)) == 0) {
                        i = new PreparedSentence(s
                            , "INSERT INTO MATERIALS_UNITS (MATERIAL, UNIT, AMOUNT, PRICEBUY) VALUES (?, ?, ?, ?)"
                            , new SerializerWriteBasicExt(materialDatas, new int[]{0, 13, 14, 15})).exec(params);
                    }
                return i;
            }        
        };
    }
   
    public final SentenceExec getMaterialDelete() {
        return new SentenceExecTransaction(s) {
            public int execInTransaction(Object params) throws BasicException {
                new PreparedSentence(s
                    , "DELETE FROM MATERIALS_UNITS WHERE MATERIAL = ?"
                    , new SerializerWriteBasicExt(materialDatas, new int[] {0})).exec(params);
                return new PreparedSentence(s
                    , "DELETE FROM PRODUCTS WHERE ID = ?"
                    , new SerializerWriteBasicExt(materialDatas, new int[] {0})).exec(params);

            } 
        };
    }
    
    
    public final SentenceExec getCompositionInsert() {
        return new SentenceExecTransaction(s) {
            public int execInTransaction(Object params) throws BasicException {
                Object[] values = (Object[]) params;            
                int i = new PreparedSentence(s
                    , "INSERT INTO PRODUCTS (ID, REFERENCE, CODE, NAME, ISCOM, ISSCALE, PRICEBUY, PRICESELL, CATEGORY, TAX, IMAGE, ATTRIBUTES) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                    , new SerializerWriteBasicExt(compositionDatas, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 13})).exec(params);
                if (i > 0 && ((Boolean)values[11]).booleanValue()) {
                    i = new PreparedSentence(s
                        , "INSERT INTO PRODUCTS_CAT (PRODUCT, CATORDER) VALUES (?, ?)"
                        , new SerializerWriteBasicExt(compositionDatas, new int[] {0, 12})).exec(params);
                }
                
                if (i > 0)
                    i = auxComposition(values, s);
                
                return i;
            }
        };
    }
    
    public final SentenceExec getCompositionUpdate() {
        return new SentenceExecTransaction(s) {
            public int execInTransaction(Object params) throws BasicException {
                Object[] values = (Object[]) params;            
                int i = new PreparedSentence(s
                    , "UPDATE PRODUCTS SET ID = ?, REFERENCE = ?, CODE = ?, NAME = ?, ISCOM = ?, ISSCALE = ?, PRICESELL = ?, CATEGORY = ?, TAX = ?, IMAGE = ?, ATTRIBUTES = ? WHERE ID = ?"
                    , new SerializerWriteBasicExt(compositionDatas, new int[]{0, 1, 2, 3, 4, 5, 7, 8, 9, 10, 13, 0})).exec(params);
                if (i > 0) {
                    if (((Boolean)values[11]).booleanValue()) {
                        if (new PreparedSentence(s
                                , "UPDATE PRODUCTS_CAT SET CATORDER = ? WHERE PRODUCT = ?"
                                , new SerializerWriteBasicExt(compositionDatas, new int[] {12, 0})).exec(params) == 0) {
                            new PreparedSentence(s
                                , "INSERT INTO PRODUCTS_CAT (PRODUCT, CATORDER) VALUES (?, ?)"
                                , new SerializerWriteBasicExt(compositionDatas, new int[] {0, 12})).exec(params);
                        }
                    } else {
                        new PreparedSentence(s
                            , "DELETE FROM PRODUCTS_CAT WHERE PRODUCT = ?"
                            , new SerializerWriteBasicExt(compositionDatas, new int[] {0})).exec(params);
                    }
                }
                if (i > 0)
                    i = auxComposition(values, s);
                
                return i;
            }        
        };
    }
    
    private int auxComposition(Object params, Session s) throws BasicException {
        Object[] values = (Object[]) params;
        int i = 1;

        //Eliminamos los subgrupos que pudiese tener
        new PreparedSentence(s
            , "DELETE FROM SUBGROUPS WHERE COMPOSITION = ?"
            , new SerializerWriteBasicExt(subgroupDatas, new int[] {0})).exec(params);                 

        //Por cada subgrupo
        int ssize = ((Integer)values[14]).intValue();
        int cont = 15;
        for (int e = 0; e < ssize && i > 0; e++) {
            Object[] sparams = new Object[] {values[cont], values[0], values[cont +1], values[cont +2]};
            
            //Eliminamos los productos que el subgrupo pudiera tener
            new PreparedSentence(s
                , "DELETE FROM SUBGROUPS_PROD WHERE SUBGROUP = ?"
                , new SerializerWriteBasicExt(subgroup_prodDatas, new int[] {0})).exec(sparams);        
            
            i = new PreparedSentence(s
                , "INSERT INTO SUBGROUPS (ID, COMPOSITION, NAME, IMAGE) VALUES (?, ?, ?, ?)"
                , new SerializerWriteBasicExt(subgroupDatas, new int[] {0, 1, 2, 3})).exec(sparams);

            int psize = ((Integer)values[cont+3]).intValue();
            for (int o = 0; o < psize && i > 0; o++) {
                Object[] pparams = new Object[] {values[cont], values[cont+o+4]};
                
                i = new PreparedSentence(s
                    , "INSERT INTO SUBGROUPS_PROD (SUBGROUP, PRODUCT) VALUES (?, ?)"
                    , new SerializerWriteBasicExt(subgroup_prodDatas, new int[] {0, 1})).exec(pparams);
            }
            //Avanzamos el contador al siguiente subgrupo
            cont += psize + 4;
        }
        
        return i;
    }
   
    
    public final SentenceExec getCompositionDelete() {
        return getProductCatDelete();
    }
    
    public final SentenceExec getTariffAreaInsert() {
        return new SentenceExecTransaction(s) {
            public int execInTransaction(Object params) throws BasicException {
                Object[] values = (Object[]) params;            
                int i = new PreparedSentence(s
                    , "INSERT INTO TARIFFAREAS (ID, NAME, TARIFFORDER) VALUES (?, ?, ?)"
                    , new SerializerWriteBasicExt(tariffareaDatas, new int[]{0, 1, 2})).exec(params);

                if (i > 0) i = auxTariffArea(values, s);
                return i;
            }
        };
    }
    
    public final SentenceExec getTariffAreaUpdate() {
        return new SentenceExecTransaction(s) {
            public int execInTransaction(Object params) throws BasicException {
                Object[] values = (Object[]) params;            
                int i = new PreparedSentence(s
                    , "UPDATE TARIFFAREAS SET NAME = ?, TARIFFORDER = ? WHERE ID = ?"
                    , new SerializerWriteBasicExt(tariffareaDatas, new int[]{1, 2, 0})).exec(params);
                
                if (i > 0) i = auxTariffArea(values, s);
                return i;
            }        
        };
    }
    
    private int auxTariffArea(Object params, Session s) throws BasicException {
        Object[] values = (Object[]) params;
        int i = 1;
        
        //Eliminamos los productos que pudiese tener el grupo de tarifas
        new PreparedSentence(s
            , "DELETE FROM TARIFFAREAS_PROD WHERE TARIFFID = ?"
            , new SerializerWriteBasicExt(tariffprodDatas, new int[] {0})).exec(params);                 

        //Por cada producto del grupo de tarifas...
        int ssize = ((Integer)values[3]).intValue();
        int cont = 4;
        for (int e = 0; e < ssize && i > 0; e++) {
            Object[] sparams = new Object[] {values[0], values[cont], values[cont +1]};
            
            i = new PreparedSentence(s
                , "INSERT INTO TARIFFAREAS_PROD (TARIFFID, PRODUCTID, PRICESELL) VALUES (?, ?, ?)"
                , new SerializerWriteBasicExt(tariffprodDatas, new int[] {0, 1, 2})).exec(sparams);

            //Avanzamos el contador al siguiente producto
            cont += 2;
        }
        return i;
    }
    
    
    public final SentenceExec getTariffAreaDelete() {
        return new SentenceExecTransaction(s) {
            public int execInTransaction(Object params) throws BasicException {
                return new PreparedSentence(s
                    , "DELETE FROM TARIFFAREAS WHERE ID = ?"
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
    
    public final SentenceList getCategories() {
        return new PreparedSentence(s
            , "SELECT ID, NAME, PARENTID, IMAGE FROM CATEGORIES"
            , null
            , new SerializerReadBasic(categoryDatas));
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
    
    public final SentenceList getTaxes() {
        return new PreparedSentence(s
            , "SELECT ID, NAME, RATE FROM TAXES WHERE ID >= 0"
            , null
            , new SerializerReadBasic(new Datas[] {Datas.STRING, Datas.STRING, Datas.DOUBLE}));
    }
    
    public final TableDefinition getTableDiscounts() {
        return new TableDefinition(s,
            "DISCOUNTS"
            , new String[] {"ID", "NAME", "QUANTITY", "PERCENTAGE"}
            , new String[] {"ID", AppLocal.getIntString("Label.Name"), AppLocal.getIntString("label.quantity"), AppLocal.getIntString("label.percentage")}
            , new Datas[] {Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.BOOLEAN}
            , new Formats[] {Formats.STRING, Formats.STRING, Formats.DOUBLE, Formats.BOOLEAN}
            , new int[] {0}
        );
    }
        
    public final TableDefinition getTableTariffAreas() {
        return new TableDefinition(s,
            "TARIFFAREAS"
            , new String[] {"ID", "NAME", "TARIFFORDER"}
            , new String[] {"ID", AppLocal.getIntString("Label.Name"), AppLocal.getIntString("label.prodorder")}
            , new Datas[] {Datas.STRING, Datas.STRING, Datas.INT}
            , new Formats[] {Formats.STRING, Formats.STRING, Formats.INT}
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
    
    public final TableDefinition getTableUnits() {
        return new TableDefinition(s,
            "UNITS"
            , new String[] {"ID", "NAME", "SYMBOL"}
            , new String[] {"ID", AppLocal.getIntString("Label.Name"), AppLocal.getIntString("label.symbol")}
            , new Datas[] {Datas.STRING, Datas.STRING, Datas.STRING}
            , new Formats[] {Formats.STRING, Formats.STRING, Formats.STRING}
            , new int[] {0}
        );
    }
/*
   
    public final TableDefinition getTableMaterials() {
        return new TableDefinition(s,
            "MATERIALS"
            , new String[] {"ID", "NAME", "UNITS", "PRICEBUY"}
            , new String[] {"ID", AppLocal.getIntString("Label.Name"), AppLocal.getIntString("label.unitsmeasure"), AppLocal.getIntString("label.prodpricebuy")}
            , new Datas[] {Datas.STRING, Datas.STRING, Datas.STRING, Datas.DOUBLE}
            , new Formats[] {Formats.STRING, Formats.STRING, Formats.STRING, Formats.DOUBLE}
            , new int[] {0}
        );
    }
*/
}
