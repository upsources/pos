//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2008 Openbravo, S.L.
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

package com.openbravo.pos.ticket;

import com.openbravo.data.loader.DataRead;
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.SerializableRead;

/**
 *
 * @author  Mikel irurita
 */
public class FindTicketsInfo implements SerializableRead {

    private int ticketid;
    private int tickettype;
    private String date;
    private String name;
    private String customer;
    private String payment;
    private double total;
    
    /** Creates new ProductInfo */
    public FindTicketsInfo() {
        
    }
    
    @Override
    public void readValues(DataRead dr) throws BasicException {
        ticketid = dr.getInt(1);
        tickettype = dr.getInt(2);
        date = dr.getString(3);
        name = dr.getString(4);
        customer = dr.getString(5);
        payment = dr.getString(6);
        total = (dr.getObject(7) == null) ? 0.0 : dr.getDouble(7).doubleValue();
    }
    
    @Override
    public String toString(){
        
        String sTicketType = (tickettype==0) ? "SALE" : "REFUND" ;
        String sCustomer = (customer==null) ? "" : customer;
        String sTotal = (total==0.0) ? "" : Double.toString(total);
        String sPayment = (payment==null) ? "" : payment;
        
        String sHtml = "<tr><td width=\"90\">"+ "["+ ticketid +", "+ sTicketType +"]" +"</td>"+
                "<td width=\"100\">"+ date +"</td>"+
                "<td width=\"100\">"+ name +"</td>"+
                "<td align=\"center\" width=\"80\">"+ sTotal +"</td>"+
                "<td width=\"100\">"+ sPayment +"</td>"+
                "<td width=\"100\">"+ sCustomer +"</td></tr>";
        
        return sHtml;
    }
    
    public int getTicketId(){
        return this.ticketid;
    }
    
    public int getTicketType(){
        return this.tickettype;
    }
    
    
}
