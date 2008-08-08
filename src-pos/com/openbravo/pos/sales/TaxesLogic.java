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
//    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

package com.openbravo.pos.sales;

import com.openbravo.pos.customers.CustomerInfo;
import com.openbravo.pos.inventory.TaxCategoryInfo;
import com.openbravo.pos.ticket.TaxInfo;

/**
 *
 * @author adrianromero
 */
public class TaxesLogic {
    
    java.util.List<TaxInfo> taxlist;
    
    public TaxesLogic(java.util.List<TaxInfo> taxlist) {
        this.taxlist = taxlist;
    }
    
    
    public double getTaxRate(TaxCategoryInfo tc) {
        return getTaxRate(tc, null);
    }
    
    public double getTaxRate(TaxCategoryInfo tc, CustomerInfo customer) {
        
        if (tc == null) {
            return 0.0;
        } else {
            TaxInfo tax = getTaxInfo(tc, customer);
            if (tax == null) {
                return 0.0;
            } else {
                return tax.getRate();
            }            
        }
    }

    public TaxInfo getTaxInfo(TaxCategoryInfo tc) {
        return getTaxInfo(tc, null);
    }
    
    
    public TaxInfo getTaxInfo(TaxCategoryInfo tc, CustomerInfo customer) {
        
        
        TaxInfo defaulttax = null;
        
        for (TaxInfo tax : taxlist) {
            if (tax.getParentID() == null && tax.getTaxCategoryID().equals(tc.getID())) {
                if (customer == null && tax.getTaxCustCategoryID() == null) {
                    return tax;
                } else if (customer != null && customer.getTaxid().endsWith(tax.getTaxCustCategoryID())) {
                    return tax;
                }
                
                if (tax.getTaxCustCategoryID() == null) {
                    defaulttax = tax;
                }
            }
        }
        
        // No tax found
        return defaulttax;
    }
}
