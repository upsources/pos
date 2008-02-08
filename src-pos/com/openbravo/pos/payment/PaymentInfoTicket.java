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

package com.openbravo.pos.payment;

import com.openbravo.format.Formats;

public class PaymentInfoTicket extends PaymentInfo {
    
    private double m_dTicket;
    private String m_sName;
    
    /** Creates a new instance of PaymentInfoCash */
    public PaymentInfoTicket(double dTicket, String sName) {
        m_dTicket = dTicket;
        m_sName = sName;
    }
    public PaymentInfo clonePayment(){
        return new PaymentInfoTicket(m_dTicket, m_sName);
    }    
    public String getName() {
        return m_sName;
    }   
    public double getTotal() {
        return m_dTicket;
    }    
    public String printPaid() {
        return Formats.CURRENCY.formatValue(new Double(m_dTicket));
    }       
    
    // Especificas
    public String printPaperTotal() {
        // En una devolucion hay que cambiar el signo al total
        return Formats.CURRENCY.formatValue(new Double(-m_dTicket));
    }          
}
