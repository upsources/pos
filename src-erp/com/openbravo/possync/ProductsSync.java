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

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.ImageUtils;
import com.openbravo.pos.customers.CustomerInfoExt;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.forms.ProcessAction;
import com.openbravo.pos.inventory.MovementReason;
import com.openbravo.pos.ticket.CategoryInfo;
import com.openbravo.pos.ticket.ProductInfoExt;
import com.openbravo.pos.ticket.TaxInfo;
import com.openbravo.ws.customers.Customer;
import com.openbravo.ws.externalsales.ProductPlus;
import java.util.Date;
import java.util.UUID;

public class ProductsSync implements ProcessAction {
        
    private DataLogicSystem dlsystem;
    private DataLogicIntegration dlintegration;
    private DataLogicSales dlsales;
    private String warehouse;
    
    /** Creates a new instance of ProductsSync */
    public ProductsSync(DataLogicSystem dlsystem, DataLogicIntegration dlintegration, DataLogicSales dlsales, String warehouse) {
        this.dlsystem = dlsystem;
        this.dlintegration = dlintegration;
        this.dlsales = dlsales;
        this.warehouse = warehouse;
    }
    
    public MessageInf execute() throws BasicException {
        
        try {
        
            ExternalSalesHelper externalsales = new ExternalSalesHelper(dlsystem);
            
            ProductPlus[] products = externalsales.getProductsPlusCatalog();
            Customer[] customers = externalsales.getCustomers();

            if (products == null || customers == null){
                throw new BasicException(AppLocal.getIntString("message.returnnull"));
            }     
            
            if (products.length > 0){
                
                dlintegration.syncProductsBefore();
                
                Date now = new Date();
                
                for (int i = 0; i < products.length; i++) {
                    // Synchonization of taxes
                    TaxInfo t = new TaxInfo();
                    t.setID(Integer.toString(products[i].getTax().getId()));
                    t.setName(products[i].getTax().getName());
                    t.setRate(products[i].getTax().getPercentage() / 100);                        
                    dlintegration.syncTax(t);

                    // Synchonization of categories
                    CategoryInfo c = new CategoryInfo();
                    c.setID(Integer.toString(products[i].getCategory().getId()));
                    c.setName(products[i].getCategory().getName());
                    c.setImage(null);                        
                    dlintegration.syncCategory(c);

                    // Synchonization of products
                    ProductInfoExt p = new ProductInfoExt();
                    p.setID(Integer.toString(products[i].getId()));
                    p.setReference(Integer.toString(products[i].getId()));
                    p.setCode(products[i].getEan() == null || products[i].getEan().equals("") ? Integer.toString(products[i].getId()) : products[i].getEan());
                    p.setName(products[i].getName());
                    p.setCom(false);
                    p.setScale(false);
                    p.setPriceBuy(products[i].getPurchasePrice());
                    p.setPriceSell(products[i].getListPrice());
                    p.setCategoryID(c.getID());
                    p.setTaxInfo(t);
                    p.setImage(ImageUtils.readImage(products[i].getImageUrl()));
                    dlintegration.syncProduct(p);  
                    
                    // Synchronization of stock                    
                    double diff = products[i].getQtyonhand() - dlsales.findProductStock(p.getID(), warehouse);
                    
                    Object[] diary = new Object[7];
                    diary[0] = UUID.randomUUID().toString();
                    diary[1] = now;
                    diary[2] = diff > 0.0 
                            ? MovementReason.IN_MOVEMENT.getKey()
                            : MovementReason.OUT_MOVEMENT.getKey();
                    diary[3] = warehouse;
                    diary[4] = p.getID();
                    diary[5] = new Double(diff);
                    diary[6] = new Double(p.getPriceBuy());                                
                    dlsales.getStockDiaryInsert().exec(diary);                    
                }
                
                // datalogic.syncProductsAfter();
            }
            
            if (customers.length > 0 ) {
                
                dlintegration. syncCustomersBefore();
                
                for (int i = 0; i < customers.length; i++) {                    
                    CustomerInfoExt cinfo = new CustomerInfoExt(Integer.toString(customers[i].getId()), customers[i].getName());
                    cinfo.setAddress(customers[i].getDescription());
                    dlintegration.syncCustomer(cinfo);
                }
            }
            
            if (products.length == 0 && customers.length == 0) {
                return new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.zeroproducts"));               
            } else {
                return new MessageInf(MessageInf.SGN_SUCCESS, AppLocal.getIntString("message.syncproductsok"), AppLocal.getIntString("message.syncproductsinfo", products.length, customers.length));
            }
                
        } catch (ServiceException e) {            
            throw new BasicException(AppLocal.getIntString("message.serviceexception"), e);
        } catch (RemoteException e){
            throw new BasicException(AppLocal.getIntString("message.remoteexception"), e);
        } catch (MalformedURLException e){
            throw new BasicException(AppLocal.getIntString("message.malformedurlexception"), e);
        }
    }   
}
