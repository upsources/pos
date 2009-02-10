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

package com.openbravo.pos.payment;

public class PaymentInfoMagcard extends PaymentInfo {
     
    protected double m_dTotal;
    
    protected String m_sHolderName;
    protected String m_sCardNumber;
    protected String m_sExpirationDate;
    protected String track1;
    protected String track2;
    protected String track3;
    
    protected String m_sTransactionID;
    
    protected String m_sAuthorization;    
    protected String m_sErrorMessage;
    protected String m_sReturnMessage;
    
    /** Creates a new instance of PaymentInfoMagcard */
    public PaymentInfoMagcard(String sHolderName, String sCardNumber, String sExpirationDate, String track1, String track2, String track3, String sTransactionID, double dTotal) {
        m_sHolderName = sHolderName;
        m_sCardNumber = sCardNumber;
        m_sExpirationDate = sExpirationDate;
        this.track1 = track1;
        this.track2 = track2;
        this.track3 = track3;
        
        m_sTransactionID = sTransactionID;
        m_dTotal = dTotal;
        
        m_sAuthorization = null;
        m_sErrorMessage = null;
        m_sReturnMessage = null;
    }
    
    /** Creates a new instance of PaymentInfoMagcard */
    public PaymentInfoMagcard(String sHolderName, String sCardNumber, String sExpirationDate, String sTransactionID, double dTotal) {
        this(sHolderName, sCardNumber, sExpirationDate, null, null, null, sTransactionID, dTotal);
    }
    
    public PaymentInfo copyPayment(){
        PaymentInfoMagcard p = new PaymentInfoMagcard(m_sHolderName, m_sCardNumber, m_sExpirationDate, track1, track2, track3, m_sTransactionID, m_dTotal);
        p.m_sAuthorization = m_sAuthorization;
        p.m_sErrorMessage = m_sErrorMessage;
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
    public String getTrack1() {
        return track1;
    }
    public String getTrack2() {
        return track1;
    }
    public String getTrack3() {
        return track1;
    }
    
    public String getAuthorization() {
        return m_sAuthorization;
    }

    public String getMessage() {
        return m_sErrorMessage;
    }
    
    public void paymentError(String sMessage, String moreInfo) {
        m_sAuthorization = null;
        m_sErrorMessage = sMessage + "\n" + moreInfo;
    }    
    
    public void setReturnMessage(String returnMessage){
        m_sReturnMessage = returnMessage;
    }
    
    public String getReturnMessage(){
        return m_sReturnMessage;
    }
    
    public void paymentOK(String sAuthorization, String sTransactionId, String sReturnMessage) {
        m_sAuthorization = sAuthorization;
        m_sTransactionID = sTransactionId;
        m_sReturnMessage = sReturnMessage;
        m_sErrorMessage = null;
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
