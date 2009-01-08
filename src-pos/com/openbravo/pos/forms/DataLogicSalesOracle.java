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
//    Foundation, Inc., 51 Franklin Street, Fifth floor, Boston, MA  02110-1301  USA

package com.openbravo.pos.forms;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.QBFBuilder;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.loader.SerializerReadInteger;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.pos.customers.CustomerInfoExt;
import com.openbravo.pos.ticket.ProductInfoExt;
import java.util.List;

/**
 *
 * @author adrianromero
 */
public class DataLogicSalesOracle extends DataLogicSales {
    
    /** Creates a new instance of SentenceContainerOracle */
    public DataLogicSalesOracle() {
    }
    
    public final SentenceList getProductCatQBF() {
        return new StaticSentence(s
            , new QBFBuilder(
                "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.CATEGORY, P.TAXCAT, P.ATTRIBUTESET_ID, P.IMAGE, P.STOCKCOST, P.STOCKVOLUME, CASE WHEN C.PRODUCT IS NULL THEN 0 ELSE 1 END, C.CATORDER, P.ATTRIBUTES " +
                "FROM PRODUCTS P LEFT OUTER JOIN PRODUCTS_CAT C ON P.ID = C.PRODUCT " +
                "WHERE ?(QBF_FILTER) " +
                "ORDER BY P.REFERENCE", new String[] {"P.NAME", "P.PRICEBUY", "P.PRICESELL", "P.CATEGORY", "P.CODE"})
            , new SerializerWriteBasic(new Datas[] {Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.STRING})
            , productsRow.getSerializerRead());
    }        
  
    public final Integer getNextTicketIndex() throws BasicException {
        return (Integer) new StaticSentence(s, "SELECT TICKETSNUM.NEXTVAL FROM DUAL", null, SerializerReadInteger.INSTANCE).find();
    }
    public final Integer getNextTicketRefundIndex() throws BasicException {
        return (Integer) new StaticSentence(s, "SELECT TICKETSNUM_REFUND.NEXTVAL FROM DUAL", null, SerializerReadInteger.INSTANCE).find();
    }
    public final Integer getNextTicketPaymentIndex() throws BasicException {
        return (Integer) new StaticSentence(s, "SELECT TICKETSNUM_PAYMENT.NEXTVAL FROM DUAL", null, SerializerReadInteger.INSTANCE).find();
    }
    
    @Override
    public CustomerInfoExt findCustomerExt(String card) throws BasicException {
        return (CustomerInfoExt) new PreparedSentence(s
                , "SELECT ID, TAXID, SEARCHKEY, NAME, TAXCATEGORY, CARD, NOTES, MAXDEBT, VISIBLE, CURDATE, CURDEBT" +
                  ", FIRSTNAME, LASTNAME, EMAIL, PHONE, PHONE2, FAX" +
                  ", ADDRESS, ADDRESS2, POSTAL, CITY, REGION, COUNTRY" +
                  " FROM CUSTOMERS WHERE CARD = ? AND VISIBLE = 1"
                , SerializerWriteString.INSTANCE
                , new CustomerExtRead()).find(card);        
    }

    // Products list
    @Override
    public final SentenceList getProductListNormal() {
        return new StaticSentence(s
            , new QBFBuilder(
               "SELECT ID, REFERENCE, CODE, NAME, ISCOM, ISSCALE, PRICEBUY, PRICESELL, TAXCAT, CATEGORY, ATTRIBUTESET_ID, IMAGE, ATTRIBUTES " +
               "FROM PRODUCTS WHERE ISCOM = 0 AND ?(QBF_FILTER) ORDER BY REFERENCE", new String[] {"NAME", "PRICEBUY", "PRICESELL", "CATEGORY", "CODE"})
               , new SerializerWriteBasic(new Datas[] {Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.STRING})
            , ProductInfoExt.getSerializerRead());
    }

    //Auxiliar list for a filter
    @Override
    public final SentenceList getProductListAuxiliar() {
         return new StaticSentence(s
            , new QBFBuilder(
               "SELECT ID, REFERENCE, CODE, NAME, ISCOM, ISSCALE, PRICEBUY, PRICESELL, TAXCAT, CATEGORY, ATTRIBUTESET_ID, IMAGE, ATTRIBUTES " +
               "FROM PRODUCTS WHERE ISCOM = 1 AND ?(QBF_FILTER) ORDER BY REFERENCE", new String[] {"NAME", "PRICEBUY", "PRICESELL", "CATEGORY", "CODE"})
            , new SerializerWriteBasic(new Datas[] {Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.DOUBLE, Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.STRING})
            , ProductInfoExt.getSerializerRead());
    }

    @Override
    public List<ProductInfoExt> getProductCatalog(String category) throws BasicException  {
        return new PreparedSentence(s
            , "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAXCAT, P.CATEGORY, P.ATTRIBUTESET_ID, P.IMAGE, P.ATTRIBUTES " +
              "FROM PRODUCTS P, PRODUCTS_CAT O WHERE P.ID = O.PRODUCT AND P.CATEGORY = ? " +
              "AND P.ISCOM = 0 " +
              "ORDER BY O.CATORDER, P.NAME"
            , SerializerWriteString.INSTANCE
            , ProductInfoExt.getSerializerRead()).list(category);
    }

    @Override
    public List<ProductInfoExt> getProductComments(String id) throws BasicException {
        return new PreparedSentence(s
            , "SELECT P.ID, P.REFERENCE, P.CODE, P.NAME, P.ISCOM, P.ISSCALE, P.PRICEBUY, P.PRICESELL, P.TAXCAT, P.CATEGORY, P.ATTRIBUTESET_ID, P.IMAGE, P.ATTRIBUTES " +
              "FROM PRODUCTS P, PRODUCTS_CAT O, PRODUCTS_COM M WHERE P.ID = O.PRODUCT AND P.ID = M.PRODUCT2 AND M.PRODUCT = ? " +
              "AND P.ISCOM = 1 " +
              "ORDER BY O.CATORDER, P.NAME"
            , SerializerWriteString.INSTANCE
            , ProductInfoExt.getSerializerRead()).list(id);
    }
}