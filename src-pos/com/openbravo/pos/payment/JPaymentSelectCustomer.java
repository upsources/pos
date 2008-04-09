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

package com.openbravo.pos.payment;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;

/**
 *
 * @author adrianromero
 */
public class JPaymentSelectCustomer extends JPaymentSelect {
    
    /** Creates new form JPaymentSelect */
    protected JPaymentSelectCustomer(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
    }
    /** Creates new form JPaymentSelect */
    protected JPaymentSelectCustomer(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);
    } 
    
    public static JPaymentSelect getDialog(Component parent) {
         
        Window window = getWindow(parent);
        
        if (window instanceof Frame) { 
            return new JPaymentSelectCustomer((Frame) window, true);
        } else {
            return new JPaymentSelectCustomer((Dialog) window, true);
        }
    } 
    
    protected void addTabs() {
        
        addTabPaymentCash();
        addTabPaymentCheque();
        addTabPaymentPaper();
        addTabPaymentTicket();
        addTabPaymentMagcard();
        setHeaderVisible(true);
    }
    
    protected void setStatusPanel(boolean isPositive, boolean isComplete) {
        
        setAddEnabled(isPositive && !isComplete);
        setOKEnabled(isPositive);
    }
    
    protected PaymentInfo getDefaultPayment(double total) {
        return new PaymentInfoCash(total, total);
    }    
}
