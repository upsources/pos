//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007 Openbravo, S.L.
//    http://sourceforge.net/projects/
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

public class TicketInfo implements SerializableRead, Externalizable {
 
    private static DateFormat m_dateformat = new SimpleDateFormat("hh:mm");
 
    private String m_sId;
    private int m_iTicketId;
    private java.util.Date m_dDate;
    private UserInfo m_User;
    private String m_sActiveCash;
    private List<PaymentInfo> m_aPayment;    
    private List<TicketLineInfo> m_aLines;
    
    /** Creates new TicketModel */
    public TicketInfo() {
        m_sId = UUID.randomUUID().toString();
        m_iTicketId = 0; // incrementamos
        m_dDate = null;
        m_User = null;
        m_sActiveCash = null;
        m_aPayment = new ArrayList<PaymentInfo>();        
        m_aLines = new ArrayList<TicketLineInfo>(); // vacio de lineas
    }
    public void writeExternal(ObjectOutput out) throws IOException  {
        // esto es solo para serializar tickets que no estan en la bolsa de tickets pendientes
        out.writeObject(m_sId);
        out.writeInt(m_iTicketId);      
        out.writeObject(m_dDate);
        out.writeObject(m_aLines);
    }   
    
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        // esto es solo para serializar tickets que no estan en la bolsa de tickets pendientes
        m_sId = (String) in.readObject();
        m_iTicketId = in.readInt();
        m_dDate = (Date) in.readObject();
        m_User = null;
        m_sActiveCash = null;
        m_aPayment = new ArrayList<PaymentInfo>();        
        m_aLines = (List<TicketLineInfo>) in.readObject();
    }
    
    public void readValues(DataRead dr) throws BasicException {
        m_sId = dr.getString(1);
        m_iTicketId = dr.getInt(2).intValue();
        m_dDate = dr.getTimestamp(3);
        m_sActiveCash = dr.getString(4);
        m_User = new UserInfo(dr.getString(5), dr.getString(6)); 
        m_aPayment = new ArrayList<PaymentInfo>(); 
        m_aLines = new ArrayList<TicketLineInfo>();
    }
    
    public TicketInfo cloneTicket() {
        TicketInfo t = new TicketInfo();
        t.m_sId = m_sId;
        t.m_iTicketId = m_iTicketId; // incrementamos
        t.m_dDate = m_dDate;
        t.m_User = m_User;
        t.m_sActiveCash = m_sActiveCash;
        
        t.m_aPayment = new LinkedList<PaymentInfo>(); 
        for (PaymentInfo p : m_aPayment) {
            t.m_aPayment.add(p.clonePayment());
        }
        
        t.m_aLines = new ArrayList<TicketLineInfo>(); 
        for (TicketLineInfo l : m_aLines) {
            t.m_aLines.add(l.cloneTicketLine());
        }
        
        return t;
    }
    
    public String getId() {
        return m_sId;
    }
    
    public int getTicketId(){
        return m_iTicketId;
    }
    public void setTicketId(int iTicketId) {
        m_iTicketId = iTicketId;
        refreshLines();
    }   
    
    public String getName() {
        return "(" + m_dateformat.format(m_dDate) + " " + Long.toString(m_dDate.getTime() % 1000) + ")";
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
    
    public void removeLine(int index) {
        m_aLines.remove(index);
        refreshLines();        
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
            dArticles += oLine.getMultiply();
        }
        
        return dArticles;
    }
    
    public double getSubTotal() {
        double dSuma = 0.0;
        TicketLineInfo oLine;            
        for (Iterator<TicketLineInfo> i = m_aLines.iterator(); i.hasNext();) {
            oLine = i.next();
            dSuma += oLine.getSubValue();
        }        
        return dSuma;
    }
    
    public double getTax() {
        double dSuma = 0.0;
        TicketLineInfo oLine;            
        for (Iterator<TicketLineInfo> i = m_aLines.iterator(); i.hasNext();) {
            oLine = i.next();
            dSuma += oLine.getTax();
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
        return dSuma;
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
            
            if (tax.getID().equals(oLine.getTaxInfo().getID())) {
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
    
    public String printId() {
        return Formats.INT.formatValue(new Integer(m_iTicketId));
    }
    public String printDate() {
        return Formats.TIMESTAMP.formatValue(m_dDate);
    }
    public String printUser() {
        return m_User == null ? "" : m_User.getName();
    }
    public String printArticlesCount() {
        return Formats.DOUBLE.formatValue(new Double(getArticlesCount()));
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
    public String printTotalPts() {
        return Formats.INT.formatValue(new Double(CurrencyChange.changeEurosToPts(getTotal())));
    } 
}
