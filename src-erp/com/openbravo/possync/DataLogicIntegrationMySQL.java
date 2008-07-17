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

package com.openbravo.possync;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataParams;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.SerializerWrite;
import com.openbravo.data.loader.SerializerWriteParams;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.loader.Transaction;
import com.openbravo.pos.customers.CustomerInfoExt;

/**
 *
 * @author adrianromero
 */
public class DataLogicIntegrationMySQL extends DataLogicIntegration {
    
    /** Creates a new instance of DataLogicIntegrationHSQLDB */
    public DataLogicIntegrationMySQL() {
    }
    
    @Override
    public void syncCustomersBefore() throws BasicException {
        new StaticSentence(s, "UPDATE CUSTOMERS SET VISIBLE = FALSE").exec();
    }
    
    @Override
    public void syncCustomer(final CustomerInfoExt customer) throws BasicException {
        
        Transaction t = new Transaction(s) {
            public Object transact() throws BasicException {
                // Sync the Customer in a transaction
                
                // Try to update                
                if (new PreparedSentence(s, 
                            "UPDATE CUSTOMERS SET SEARCHKEY = ?, NAME = ?, NOTES = ?, VISIBLE = TRUE WHERE ID = ?", 
                            SerializerWriteParams.INSTANCE
                            ).exec(new DataParams() { public void writeValues() throws BasicException {
                                setString(1, customer.getSearchkey());
                                setString(2, customer.getName());
                                setString(3, customer.getAddress());
                                setString(4, customer.getId());                                   
                            }}) == 0) {
                       
                    // If not updated, try to insert
                    new PreparedSentence(s, 
                            "INSERT INTO CUSTOMERS(ID, SEARCHKEY, NAME, NOTES, VISIBLE) VALUES (?, ?, ?, ?, TRUE)", 
                            SerializerWriteParams.INSTANCE
                            ).exec(new DataParams() { public void writeValues() throws BasicException {
                                setString(1, customer.getId());
                                setString(2, customer.getSearchkey());
                                setString(3, customer.getName());
                                setString(4, customer.getAddress());                                    
                            }});
                }
                
                return null;
            }
        };
        t.execute(); 
    }
}