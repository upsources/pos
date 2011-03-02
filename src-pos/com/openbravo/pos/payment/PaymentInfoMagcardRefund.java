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

public class PaymentInfoMagcardRefund extends PaymentInfoMagcard {
    
    /** Creates a new instance of PaymentInfoMagcardRefund */
    public PaymentInfoMagcardRefund(String sHolderName, String sCardNumber, String sExpirationDate, String sTransactionID, double dTotal) {
        super(sHolderName, sCardNumber, sExpirationDate, sTransactionID, dTotal);
    }
    
    public PaymentInfo copyPayment(){
        PaymentInfoMagcardRefund p = new PaymentInfoMagcardRefund(m_sHolderName, m_sCardNumber, m_sExpirationDate, m_sTransactionID, m_dTotal);
        p.m_sAuthorization = m_sAuthorization;
        p.m_sMessage = m_sMessage;
        return p;
    }  
    public String getName() {
        return "magcardrefund";
    }    
}
