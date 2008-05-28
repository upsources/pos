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

package com.openbravo.pos.customers;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.QBFBuilder;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.loader.SerializerRead;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.data.loader.StaticSentence;

/**
 *
 * @author adrianromero
 */
public class DataLogicCustomersOracle extends DataLogicCustomers {
    
    // CustomerList list
    @Override
    public SentenceList getCustomerList() {
        return new StaticSentence(s
            , new QBFBuilder("SELECT ID, TAXID, NAME FROM CUSTOMERS WHERE VISIBLE = 1 AND ?(QBF_FILTER) ORDER BY NAME", new String[] {"NAME"})
            , new SerializerWriteBasic(new Datas[] {Datas.OBJECT, Datas.STRING})
            , new SerializerRead() {
                    public Object readValues(DataRead dr) throws BasicException {
                        return new CustomerInfo(
                                dr.getString(1),
                                dr.getString(2),
                                dr.getString(3));
                    }                
                });
    }
    
    @Override
    public CustomerInfo findCustomer(String card) throws BasicException {
        return (CustomerInfo) new PreparedSentence(s
                , "SELECT ID, TAXID, NAME FROM CUSTOMERS WHERE VISIBLE = 1 AND CARD = ?"
                , SerializerWriteString.INSTANCE
                , new SerializerRead() {
                    public Object readValues(DataRead dr) throws BasicException {
                        return new CustomerInfo(
                                dr.getString(1),
                                dr.getString(2),
                                dr.getString(3));
                    }
                }).find(card);
    }
}
