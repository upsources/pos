//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2009 Openbravo, S.L.
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

import com.openbravo.pos.pda.util.RoundUtils;


public class TicketTaxInfo {
    
    private TaxInfo tax;    
    private double subtotal;
    private double taxtotal;
            
    /** Creates a new instance of TicketTaxInfo */
    public TicketTaxInfo(TaxInfo tax) {
        this.tax = tax;
        
        subtotal = 0.0;
        taxtotal = 0.0;
    }
    
    public TaxInfo getTaxInfo() {
        return tax;
    }
    
    public void add(double dValue) {
        subtotal += dValue;
        taxtotal = subtotal * tax.getRate();
    }
    
    public double getSubTotal() {    
        return subtotal;
    }
    
    public double getTax() {       
        return taxtotal;
    }
    
    public double getTotal() {         
        return subtotal + taxtotal;
    }
    
  
}
