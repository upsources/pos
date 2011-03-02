//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2008 Openbravo, S.L.
//    http://sourceforge.net/projects/openbravopos
//
//    This file is modified as part of fastfood branch of Openbravo POS. 2008
//    These modifications are copyright Open Sistemas de Información Internet, S.L.
//    http://www.opensistemas.com
//    e-mail: imasd@opensistemas.com
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

import com.openbravo.pos.customers.CustomerInfo;
import java.util.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import com.openbravo.pos.util.*;
import com.openbravo.pos.payment.PaymentInfo;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.format.Formats;
import com.openbravo.basic.BasicException;

/**
 *
 * @author adrianromero
 * Modified by:
 * @author  Luis Ig. Bacas Riveiro	lbacas@opensistemas.com
 * @author  Pablo J. Urbano Santos	purbano@opensistemas.com
 */
public class TicketInfo implements SerializableRead, Externalizable {
 
    private static DateFormat m_dateformat = new SimpleDateFormat("hh:mm");
 
    private String m_sId;
    private int m_iTicketId;
    private java.util.Date m_dDate;
    private UserInfo m_User;
    private CustomerInfo m_Customer;
    private String m_sActiveCash;
    private List<PaymentInfo> m_aPayment;    
    private List<TicketLineInfo> m_aLines;
    
    //Gestion de exclusion entre descuentos x linea y globales.
    private boolean m_LocalDiscountLock;
    private int m_LocalDiscounts; 
    private DiscountInfo m_GlobalDiscountInfo;
    
    // Grupo de tarifas aplicado
    private String m_TariffArea;
    
    
    /** Creates new TicketModel */
    public TicketInfo() {
        m_sId = UUID.randomUUID().toString();
        m_iTicketId = 0; // incrementamos
        m_dDate = new Date();
        m_User = null;
        m_Customer = null;
        m_sActiveCash = null;
        m_aPayment = new ArrayList<PaymentInfo>();        
        m_aLines = new ArrayList<TicketLineInfo>(); // vacio de lineas
        
        m_LocalDiscountLock = false;
        m_LocalDiscounts = 0;
        m_GlobalDiscountInfo = null;
        
        m_TariffArea = null;
    }
    public void writeExternal(ObjectOutput out) throws IOException  {
        // esto es solo para serializar tickets que no estan en la bolsa de tickets pendientes
        out.writeObject(m_sId);
        out.writeInt(m_iTicketId);    
        out.writeObject(m_Customer);
        out.writeObject(m_dDate);
        out.writeObject(m_aLines);
        out.writeObject(m_GlobalDiscountInfo);
        out.writeInt(m_LocalDiscounts);
        out.writeBoolean(m_LocalDiscountLock);
        out.writeObject(m_TariffArea);
    }   
    
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        // esto es solo para serializar tickets que no estan en la bolsa de tickets pendientes
        m_sId = (String) in.readObject();
        m_iTicketId = in.readInt();
        m_Customer = (CustomerInfo) in.readObject();
        m_dDate = (Date) in.readObject();
        m_User = null;
        m_sActiveCash = null;
        m_aPayment = new ArrayList<PaymentInfo>();        
        m_aLines = (List<TicketLineInfo>) in.readObject();
        m_GlobalDiscountInfo = (DiscountInfo) in.readObject();
        if (m_GlobalDiscountInfo != null) setLocalDiscountLock(true);
        m_LocalDiscounts = in.readInt();
        m_LocalDiscountLock = in.readBoolean();
        m_TariffArea = (String) in.readObject();
    }
    
    public void readValues(DataRead dr) throws BasicException {
        m_sId = dr.getString(1);
        m_iTicketId = dr.getInt(2).intValue();
        m_dDate = dr.getTimestamp(3);
        m_sActiveCash = dr.getString(4);
        m_User = new UserInfo(dr.getString(5), dr.getString(6)); 
        String customerid = dr.getString(7);
        m_Customer = customerid == null 
                ? null
                : new CustomerInfo(dr.getString(7), dr.getString(8), dr.getString(9));
        if ( dr.getString(10) != null && dr.getString(11) != null) {
            m_GlobalDiscountInfo = new DiscountInfo(null, dr.getString(10), dr.getString(11));
            if (m_GlobalDiscountInfo != null) setLocalDiscountLock(true);
        } 
        m_TariffArea = dr.getString(12);
        m_aPayment = new ArrayList<PaymentInfo>(); 
        m_aLines = new ArrayList<TicketLineInfo>();
    }
    
    public TicketInfo copyTicket() {
        TicketInfo t = new TicketInfo();

        t.m_iTicketId = m_iTicketId;
        t.m_dDate = m_dDate;
        t.m_User = m_User;
        t.m_Customer = m_Customer;
        t.m_sActiveCash = m_sActiveCash;
        t.m_TariffArea = m_TariffArea;
        
        t.m_GlobalDiscountInfo = m_GlobalDiscountInfo;
        t.m_LocalDiscountLock = m_LocalDiscountLock;
        t.m_LocalDiscounts = m_LocalDiscounts;
        
        t.m_aPayment = new LinkedList<PaymentInfo>(); 
        for (PaymentInfo p : m_aPayment) {
            t.m_aPayment.add(p.copyPayment());
        }
        
        t.m_aLines = new ArrayList<TicketLineInfo>(); 
        for (TicketLineInfo l : m_aLines) {
            t.m_aLines.add(l.copyTicketLine());
        }
        t.refreshLines();
        
        return t;
    }
    
    
    public void applySpecialLines() {
        TicketLineInfo mainLine = null;
        
        for (TicketLineInfo l : m_aLines) {
            // Es un producto simple o una composicion
            if (! l.isProductCom() && ! l.isDiscount()) {
                mainLine = l;
            } 
            // Es un descuento x linea
            else if (l.isProductCom() && l.isDiscount() && mainLine != null) {
                mainLine.setDiscountInfo( new DiscountInfo(null, l.getProductName(), l.getPrice()) );
                increaseLocalDiscounts();
            }
            // Es un producto de una composicion
            else if (l.isProductCom() && ! l.isDiscount() && ((Double)l.getPrice()).equals(0.0) ) {
                l.setSubproduct(true);
            }
        }
    }
    
    public String getId() {
        return m_sId;
    }
    
    public int getTicketId(){
        return m_iTicketId;
    }
    public void setTicketId(int iTicketId) {
        m_iTicketId = iTicketId;
        // refreshLines();
    }   
    
    public String getName(Object info) {
        
        StringBuffer name = new StringBuffer();
        
        if (getCustomerId() != null) {
            name.append(m_Customer.toString());
            name.append(" - ");
        }
        
        if (info == null) {
            if (m_iTicketId == 0) {
                name.append("(" + m_dateformat.format(m_dDate) + " " + Long.toString(m_dDate.getTime() % 1000) + ")");
            } else {
                name.append(Integer.toString(m_iTicketId));
            }
        } else {
            name.append(info.toString());
        }
        
        return name.toString();
    }
    
    public String getName() {
        return getName(null);
    }
    
    public java.util.Date getDate() {
        return m_dDate;
    }
    public void setDate(java.util.Date dDate) { 
        m_dDate = dDate;
    }
    public UserInfo getUser() {
        return m_User;
    }    
    public void setUser(UserInfo value) {        
        m_User = value;
    }   
    
    public CustomerInfo getCustomer() {
        return m_Customer;
    }
    public void setCustomer(CustomerInfo value) {
        m_Customer = value;
    }
    public String getCustomerId() {
        if (m_Customer == null) {
            return null;
        } else {
            return m_Customer.getId();
        }
    }
    
    public void setActiveCash(String value) {     
        m_sActiveCash = value;
    }    
    public String getActiveCash() {
        return m_sActiveCash;
    }
   
    public TicketLineInfo getLine(int index){
        return m_aLines.get(index);
    }
    
    public void addLine(TicketLineInfo oLine) {

       oLine.setTicket(m_sId, m_aLines.size());
       m_aLines.add(oLine);
    }
    
    public void insertLine(int index, TicketLineInfo oLine) {
        m_aLines.add(index, oLine);
        refreshLines();        
    }
    
    public void setLine(int index, TicketLineInfo oLine) {
        oLine.setTicket(m_sId, index);
        m_aLines.set(index, oLine);     
    }
    
    public void removeLine(int index) {
        m_aLines.remove(index);
        refreshLines();        
    }
    
    public void decreaseLocalDiscounts() {
        m_LocalDiscounts--;
    }
    
    public void increaseLocalDiscounts() {
        m_LocalDiscounts++;
    }
    
    public int getLocalDiscountsCount() {
        return m_LocalDiscounts;
    }
    
    public void setLocalDiscountLock (boolean bValue) {
        m_LocalDiscountLock = bValue;
    }
    
    public boolean isLocalDiscountLocked() {
        return m_LocalDiscountLock;
    }
    
    public DiscountInfo getGlobalDiscount() {
        return m_GlobalDiscountInfo;
    }
    
    public void setGlobalDiscount(DiscountInfo disc) {
        m_GlobalDiscountInfo = disc;
    }
    
    private void refreshLines() {         
        for (int i = 0; i < m_aLines.size(); i++) {
            getLine(i).setTicket(m_sId, i);
        } 
    }
    
    public int getLinesCount() {
        return m_aLines.size();
    }
    
    public double getArticlesCount() {
        double dArticles = 0.0;
        TicketLineInfo oLine;
            
        for (Iterator<TicketLineInfo> i = m_aLines.iterator(); i.hasNext();) {
            oLine = i.next();
            if ( !oLine.isDiscount() && !oLine.isSubproduct() )
                dArticles += oLine.getMultiply();
        }
        
        return dArticles;
    }
    
    public double getBasePrice() {
        double dSuma = 0.0;
        TicketLineInfo oLine;
        
        for (Iterator<TicketLineInfo> i = m_aLines.iterator(); i.hasNext();) {
            oLine = i.next();
            dSuma += oLine.getSubValue();
        }
        return dSuma;
    }
    
    public double getSubTotal() {
        double dSuma = 0.0;
        int count = 0;
        TicketLineInfo oLine;
        
        for (Iterator<TicketLineInfo> i = m_aLines.iterator(); i.hasNext();) {
            oLine = i.next();
            dSuma += oLine.getSubValue();
            count+= oLine.getMultiply();
        }
        
        //Si el descuento global es un porcentaje
        if (m_GlobalDiscountInfo != null && m_GlobalDiscountInfo.isPercentage()) {
            dSuma *= (1.0 - m_GlobalDiscountInfo.getQuantity());
            
        // Si el descuento global es una cantidad monetaria
        } else if (m_GlobalDiscountInfo != null && !m_GlobalDiscountInfo.isPercentage()) {
            dSuma = 0.0;
            for (Iterator<TicketLineInfo> i = m_aLines.iterator(); i.hasNext();) {
                oLine = i.next();
                dSuma += oLine.getSubValue() - (oLine.getMultiply()/count * m_GlobalDiscountInfo.getQuantity() / (1 + oLine.getTaxRate()) );
            }
        }
        return dSuma;
    }
    
    public double getTax() {
        double dSuma = 0.0;
        int count = 0;
        TicketLineInfo oLine;
        
        for (Iterator<TicketLineInfo> i = m_aLines.iterator(); i.hasNext();) {
            oLine = i.next();
            dSuma += oLine.getTax();
            count+= oLine.getMultiply();
        }
        
        //Si el descuento global es un porcentaje
        if (m_GlobalDiscountInfo != null && m_GlobalDiscountInfo.isPercentage() ) {
            dSuma *= (1.0 - m_GlobalDiscountInfo.getQuantity());
            
        // Si el descuento global es una cantidad monetaria
        } else if (m_GlobalDiscountInfo != null && !m_GlobalDiscountInfo.isPercentage() ) {
            dSuma = 0.0;
            for (Iterator<TicketLineInfo> i = m_aLines.iterator(); i.hasNext();) {
                oLine = i.next();
                dSuma += oLine.getTax() - (oLine.getMultiply()/count 
                        * m_GlobalDiscountInfo.getQuantity() * oLine.getTaxRate() / (1 + oLine.getTaxRate()) );
            }
        }
        return dSuma;
    }
    
    public double getTotal() {  
        
        double dSuma = 0.0;
        TicketLineInfo oLine;            
        for (Iterator<TicketLineInfo> i = m_aLines.iterator(); i.hasNext();) {
            oLine = i.next();
            dSuma += oLine.getValue();
        }
        
        if (m_GlobalDiscountInfo != null) {
            if (m_GlobalDiscountInfo.isPercentage() ) 
                dSuma *= (1.0 - m_GlobalDiscountInfo.getQuantity());
            else 
                dSuma -= m_GlobalDiscountInfo.getQuantity();
        }
        
        return dSuma;
    }
    
    public double getTotalPaid() {        
        double sum = 0.0;
        for (Iterator<PaymentInfo> i = m_aPayment.iterator(); i.hasNext();) {
            PaymentInfo p = i.next();
            if (!"debtpaid".equals(p.getName())) {
                sum += p.getTotal();
            }                    
        }
        return sum;
    }

    public List<PaymentInfo> getPayments() {
        return m_aPayment;
    }
    
    public void setPayments(List<PaymentInfo> l) {
        m_aPayment = l;
    }
    
    
    public void resetPayments() {
        m_aPayment = new ArrayList<PaymentInfo>();
    }
    
    public List<TicketLineInfo> getLines() {
        return m_aLines;
    }    
    
    public void setLines(List<TicketLineInfo> l) {
        m_aLines = l;
    }
    
    public TicketTaxInfo getTaxLine(TaxInfo tax) {
        
        TicketTaxInfo taxinfo = new TicketTaxInfo(tax);

        TicketLineInfo oLine;            
        for (Iterator<TicketLineInfo> i = m_aLines.iterator(); i.hasNext();) {
            oLine = i.next();
            
            if (tax.getId().equals(oLine.getTaxInfo().getId())) {
                taxinfo.add(oLine.getSubValue());
            }
        }
        
        return taxinfo;
    }
    
    public TicketTaxInfo[] getTaxLines() {
        
        Map<TaxInfo, TicketTaxInfo> m = new HashMap<TaxInfo, TicketTaxInfo>();
        
        TicketLineInfo oLine;            
        for (Iterator<TicketLineInfo> i = m_aLines.iterator(); i.hasNext();) {
            oLine = i.next();
            
            TicketTaxInfo t = m.get(oLine.getTaxInfo());
            if (t == null) {
                t = new TicketTaxInfo(oLine.getTaxInfo());
                m.put(t.getTaxInfo(), t);
            }            
            t.add(oLine.getSubValue());
        }        
        
        // return dSuma;       
        Collection<TicketTaxInfo> avalues = m.values();
        return avalues.toArray(new TicketTaxInfo[avalues.size()]);
    }
    
    public String getTariffArea(){
        return m_TariffArea;
    }
    
    public void setTariffArea(String value){
        m_TariffArea = value;
    }
    
    public String printId() {
        if (m_iTicketId > 0) {
            // valid ticket id
            return Formats.INT.formatValue(new Integer(m_iTicketId));
        } else {
            return "";
        }
    }
    public String printDate() {
        return Formats.TIMESTAMP.formatValue(m_dDate);
    }
    public String printUser() {
        return m_User == null ? "" : m_User.getName();
    }
    public String printCustomer() {
        return m_Customer == null ? "" : m_Customer.getName();
    }
    public String printArticlesCount() {
        return Formats.DOUBLE.formatValue(new Double(getArticlesCount()));
    }
    
    public String printBasePrice() {
        return Formats.CURRENCY.formatValue(new Double(getBasePrice()));
    }
    public String printSubTotal() {
        return Formats.CURRENCY.formatValue(new Double(getSubTotal()));
    }
    public String printTax() {
        return Formats.CURRENCY.formatValue(new Double(getTax()));
    }    
    public String printTotal() {
        return Formats.CURRENCY.formatValue(new Double(getTotal()));
    }
    public String printTotalPaid() {
        return Formats.CURRENCY.formatValue(new Double(getTotalPaid()));
    }
    public String printTotalPts() {
        return Formats.INT.formatValue(new Double(CurrencyChange.changeEurosToPts(getTotal())));
    }
}
