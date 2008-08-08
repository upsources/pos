/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openbravo.pos.sales;

import com.openbravo.pos.customers.CustomerInfo;
import com.openbravo.pos.inventory.TaxCategoryInfo;
import com.openbravo.pos.ticket.TaxInfo;

/**
 *
 * @author adrianromero
 */
public class TaxesLogic {
    
//    DataLogicSales dlsales;
    
    java.util.List<TaxInfo> taxlist;
//    java.util.List<TaxCategoryInfo> taxcategorieslist;
    
//    private Map<Object, TaxInfo> taxmap;
//    
//    public TaxesLogic(DataLogicSales dlsales) {
//        
//        this.dlsales = dlsales;
//        
//
//        
//    }
//    
//    public void activate() throws BasicException {
//        
//        taxlist =  dlsales.getTaxList().list();        
//        taxcategorieslist = dlsales.getTaxCategoriesList().list();
//        
//    }
//    
//    
//    public getTaxList() {
//        return taxlist;
//    }
    
    public TaxesLogic(java.util.List<TaxInfo> taxlist) {
        this.taxlist = taxlist;
    }

    public TaxInfo getTaxInfo(TaxCategoryInfo tc) {
        return getTaxInfo(tc, null);
    }
    
    
    public TaxInfo getTaxInfo(TaxCategoryInfo tc, CustomerInfo customer) {
        
        for (TaxInfo tax : taxlist) {
            if (tax.getParentID() == null && tax.getTaxCategoryID().equals(tc.getID())) {
                if (customer == null && tax.getTaxCustCategoryID() == null) {
                    return tax;
                } else if (customer != null && customer.getTaxid().endsWith(tax.getTaxCustCategoryID())) {
                    return tax;
                }
            }
        }
        
        // No tax
        return null;
    }
}
