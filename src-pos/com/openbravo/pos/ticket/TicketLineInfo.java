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

import java.io.*;
import com.openbravo.pos.util.StringUtils;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.format.Formats;
import com.openbravo.data.loader.SerializableWrite;
import com.openbravo.basic.BasicException;
import com.openbravo.pos.forms.AppLocal;
import java.util.Properties;

/**
 *
 * @author adrianromero
 */
public class TicketLineInfo implements SerializableWrite, SerializableRead, Serializable {
    
    private String m_sTicket;
    private int m_iLine;
    
    private double m_dMultiply;    
    private double m_dPrice;
    private TaxInfo tax;
    private Properties attributes;
    
    private TicketProductInfo product;

    /** Creates new TicketLineInfo */   
     public TicketLineInfo(TicketProductInfo product, double dMultiply, double dPrice, TaxInfo tax, Properties attributes) {
        this.product = product; 
        m_dMultiply = dMultiply;
        m_dPrice = dPrice;
        this.tax = tax;
        this.attributes = attributes;
        
        m_sTicket = null;
        m_iLine = -1;
    }
    
     public TicketLineInfo() {
        this(new TicketProductInfo(), 0.0, 0.0, null, new Properties());
    }
     
    public TicketLineInfo(ProductInfoExt oProduct, double dMultiply, double dPrice, TaxInfo tax, Properties attributes) {
        if (oProduct == null) {
            product = new TicketProductInfo();
        } else {
            product = new TicketProductInfo(oProduct);
        }    
        m_dMultiply = dMultiply;
        m_dPrice = dPrice;
        this.tax = tax;
        this.attributes = attributes;
        
        m_sTicket = null;
        m_iLine = -1;
    }    
    public TicketLineInfo(ProductInfoExt oProduct, double dPrice, TaxInfo tax, Properties attributes) {       
        this(oProduct, 1.0, dPrice, tax, attributes);
    }
     
    public TicketLineInfo(TicketLineInfo line) {  
        
        product = line.product.copyTicketProduct();
        m_dMultiply = line.m_dMultiply;
        m_dPrice = line.m_dPrice;
        m_sTicket = null;
        tax = line.tax; 
        attributes = line.attributes; 
        m_iLine = -1;
    }
     
    void setTicket(String ticket, int line) {
        m_sTicket = ticket;
        m_iLine = line;
    }
    
    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, m_sTicket);
        dp.setInt(2, new Integer(m_iLine));
        dp.setString(3, product.getId());
        dp.setString(4, product.getName());
        dp.setBoolean(5, new Boolean(product.isCom()));
        dp.setDouble(6, new Double(m_dMultiply));
        dp.setDouble(7, new Double(m_dPrice));
        dp.setString(8, tax.getId());
        try {
            ByteArrayOutputStream o = new ByteArrayOutputStream();
            attributes.storeToXML(o, AppLocal.APP_NAME, "UTF-8");
            dp.setBytes(9, o.toByteArray()); 
        } catch (IOException e) {
            dp.setBytes(9, null);
        } 
    }
    
    public void readValues(DataRead dr) throws BasicException {
        m_sTicket = dr.getString(1);
        m_iLine = dr.getInt(2).intValue();
        String prodid = dr.getString(3);
        String prodname = dr.getString(4);
        boolean prodcom = dr.getBoolean(5).booleanValue();
        m_dMultiply = dr.getDouble(6).doubleValue();
        m_dPrice = dr.getDouble(7).doubleValue();
        tax = new TaxInfo(dr.getString(8), "", dr.getDouble(9).doubleValue());
        attributes = new Properties();
        try {
            byte[] img = dr.getBytes(10);
            if (img != null) {
                attributes.loadFromXML(new ByteArrayInputStream(img));
            }
        } catch (IOException e) {
        }         
        product = new TicketProductInfo(prodid, prodname, prodcom);
    }
    
    public TicketLineInfo copyTicketLine() {
        TicketLineInfo l = new TicketLineInfo();
        m_sTicket = null;
        m_iLine = -1;
        l.m_dMultiply = m_dMultiply;    
        l.m_dPrice = m_dPrice;
        l.tax = tax;   
        l.attributes = attributes;        
        l.product = product.copyTicketProduct();
        return l;
    }
    
    public int getTicketLine() {
        return m_iLine;
    }
    
    public TicketProductInfo getProduct() {
        return product;
    }
    
    public void setProduct(TicketProductInfo value) {
        product = value;
    }
    
    public String getProductName() {
        return product.getName();
    }
    
    public boolean isProductCom() {
        return product.isCom();
    }
    
    public double getMultiply() {
        return m_dMultiply;
    }
    
    public void setMultiply(double dValue) {
        m_dMultiply = dValue;
    }
    
    public double getPrice() {
        return m_dPrice;
    }
    
    public void setPrice(double dValue) {
        m_dPrice = dValue;
    }    
    
    public double getPriceTax() {
        return m_dPrice * (1.0 + getTaxRate());
    }
    
    public void setPriceTax(double dValue) {
        m_dPrice = dValue / (1.0 + getTaxRate());
    }
    
    public TaxInfo getTaxInfo() {
        return tax;
    }    
    
    public void setTaxInfo(TaxInfo value) {
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
    
    public double getTaxRate() {
        return tax == null ? 0.0 : tax.getRate();
    }
    
    public double getSubValue() {
        return m_dMultiply * m_dPrice;
    }
    
    public double getTax() {
        return m_dMultiply * m_dPrice * getTaxRate();
    }
    
    public double getValue() {
        return m_dMultiply * m_dPrice * (1.0 + getTaxRate());
    }
    
    public void addMore(double dPor) {
        m_dMultiply +=dPor;
    }
    public void addOneMore() {
        m_dMultiply ++;
    }
    public void remOneMore() {
        m_dMultiply --;
    }
    
    public String printName() {
         return product.getName() == null ? "" : StringUtils.encodeXML(product.getName());
    }
    public String printMultiply() {
        return Formats.DOUBLE.formatValue(new Double(m_dMultiply));
    }
    public String printPrice() {
        if (m_dMultiply == 1.0) {
            return "";
        } else {
            return Formats.CURRENCY.formatValue(new Double(getPrice()));
        }
    }    
    public String printPriceTax() {
        if (m_dMultiply == 1.0) {
            return "";
        } else {
            return Formats.CURRENCY.formatValue(new Double(getPriceTax()));
        }
    }    
    public String printTax() {
        return Formats.CURRENCY.formatValue(new Double(getTax()));
    }
    public String printTaxRate() {
        if (getTaxRate() == 0.0) {
            return "";
        } else {
            return Formats.PERCENT.formatValue(new Double(getTaxRate()));        
        }
    }
    public String printSubValue() {
        return Formats.CURRENCY.formatValue(new Double(getSubValue()));
    }
    public String printValue() {
        return Formats.CURRENCY.formatValue(new Double(getValue()));
    }
}
