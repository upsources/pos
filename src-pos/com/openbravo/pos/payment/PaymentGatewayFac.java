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

import com.openbravo.pos.forms.*;

public class PaymentGatewayFac {
    
    /** Creates a new instance of PaymentGatewayFac */
    private PaymentGatewayFac() {
    }
    
    public static PaymentGateway getPaymentGateway(AppProperties props) {
        
        String sReader = props.getProperty("payment.gateway");

        if ("external".equals(sReader)) {
            return new PaymentGatewayExt();
        } else if ("PayPoint / SecPay".equals(sReader)) {
            return new PaymentGatewayPayPoint(props);
        } else if ("AuthorizeNet".equals(sReader)) {
            return new PaymentGatewayAuthorizeNet(props);
        } else if ("La Caixa (Spain)".equals(sReader)) {
            return new PaymentGatewayCaixa(props);
        } else if ("Cyberauthorize".equals(sReader)) {
            return new PaymentGatewayCyberauthorize(props);
        } else if ("Firs Data / LinkPoint / YourPay".equals(sReader)) {
            return new PaymentGatewayLinkPoint(props);
        } else if ("PaymentsGateway.net".equals(sReader)) {
            return new PaymentGatewayPGNET(props);
        } else {
            return null;
        }
    }      
}
