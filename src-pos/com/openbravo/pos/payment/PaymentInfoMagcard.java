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

package com.openbravo.pos.payment;

public class PaymentInfoMagcard extends PaymentInfo {
     
    protected double m_dTotal;
    
    protected String m_sHolderName;
    protected String m_sCardNumber;
    protected String m_sExpirationDate;
    
    protected String m_sTransactionID;
    
    protected String m_sAuthorization;    
    protected String m_sMessage;
    
    /** Creates a new instance of PaymentInfoMagcard */
    public PaymentInfoMagcard(String sHolderName, String sCardNumber, String sExpirationDate, String sTransactionID, double dTotal) {
        m_sHolderName = sHolderName;
        m_sCardNumber = sCardNumber;
        m_sExpirationDate = sExpirationDate;
        m_sTransactionID = sTransactionID;
        m_dTotal = dTotal;
        
        m_sAuthorization = null;
        m_sMessage = null;
    }
    
    public PaymentInfo clonePayment(){
        PaymentInfoMagcard p = new PaymentInfoMagcard(m_sHolderName, m_sCardNumber, m_sExpirationDate, m_sTransactionID, m_dTotal);
        p.m_sAuthorization = m_sAuthorization;
        p.m_sMessage = m_sMessage;
        return p;
    }    
    public String getName() {
        return "magcard";
    }
    public double getTotal() {
        return m_dTotal;
    }         
    
    public boolean isPaymentOK() {
        return m_sAuthorization != null;
    }    
    public String getHolderName() {
        return m_sHolderName;
    }
    public String getCardNumber() {
        return m_sCardNumber;
    }
    public String getExpirationDate() {
        return m_sExpirationDate;
    }    
    public String getTransactionID() {
        return m_sTransactionID;
    }
    
    public String getAuthorization() {
        return m_sAuthorization;
    }

    public String getMessage() {
        return m_sMessage;
    }
    
    public void paymentError(String sMessage) {
        m_sAuthorization = null;
        m_sMessage = sMessage;
    }    
    
    public void paymentOK(String sAuthorization) {
        m_sAuthorization = sAuthorization;
        m_sMessage = null;
    }  

    public String printCardNumber() {
        // hide start numbers
        if (m_sCardNumber.length() > 4) {
            return "************" + m_sCardNumber.substring(m_sCardNumber.length() - 4);
        } else {
            return "****************";
        }
    }
    public String printExpirationDate() {
        return m_sExpirationDate;
    }
    public String printAuthorization() {
        return m_sAuthorization;
    }
    public String printTransactionID() {
        return m_sTransactionID;
    }
}
