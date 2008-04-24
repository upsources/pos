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

package com.openbravo.pos.ticket;

import com.openbravo.pos.util.StringUtils;
import java.io.Serializable;
import java.util.Properties;

/**
 *
 * @author adrianromero
 */
public class TicketProductInfo implements Serializable {
    
    private String id;
    private String name;
    private boolean com;
    
    private TaxInfo tax;
    private Properties attributes;
    
    public TicketProductInfo(String id, String name, boolean com, TaxInfo tax, Properties attributes) {
        this.id = id;
        this.name = name;
        this.com = com;
        this.tax = tax;
        this.attributes = attributes;
    }
    
    public TicketProductInfo(String name, TaxInfo tax) {
        this(null, name, false, tax, new Properties());
    }
    
    public TicketProductInfo() {
        this(null, null, false, null, new Properties());
    }
    
    public TicketProductInfo(ProductInfoExt product) {
        this(product.getID(), product.getName(), product.isCom(), product.getTaxInfo(), product.getProperties());
    }
    
    public TicketProductInfo cloneTicketProduct() {
        TicketProductInfo p = new TicketProductInfo();
        p.id = id;
        p.name = name;
        p.com = com;
        p.tax = tax;   
        p.attributes = attributes;
        return p;        
    }
    
    public String getId() {
        return id;
    }    
    
    public String getName() {
        return name;
    }     
    
    public void setName(String value) {
        if (id == null) {
            name = value;
        }
    }
 
    public boolean isCom() {
        return com;
    }
    
    public void setCom(boolean value) {
        if (id == null) {
            com = value;
        }
    }    
    
    public TaxInfo getTax() {
        return tax;
    }    
    
    public void setTax(TaxInfo value) {
        tax = value;
    }
    
    public String getProperty(String key) {
        return attributes.getProperty(key);
    }
    
    public String getProperty(String key, String defaultvalue) {
        return attributes.getProperty(key, defaultvalue);
    }
    
    public void setProperty(String key, String value) {
        attributes.setProperty(key, value);
    }
    
    public Properties getProperties() {
        return attributes;
    }
    
    public String printName() {
         return name == null ? "" : StringUtils.encodeXML(name);
    }    
}
