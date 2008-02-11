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

package com.openbravo.pos.sales;

import com.openbravo.pos.ticket.TicketInfo;
import java.util.Date;
import com.openbravo.basic.BasicException;
import com.openbravo.pos.sales.simple.JTicketsBagSimple;
import com.openbravo.pos.forms.*; 
import javax.swing.*;
import com.openbravo.data.gui.MessageInf;

// las implementaciones
import com.openbravo.pos.sales.restaurant.JTicketsBagRestaurantMap;
import com.openbravo.pos.sales.shared.JTicketsBagShared;

public abstract class JTicketsBag extends JPanel {
    
    protected AppView m_App;     
    protected DataLogicSales m_dlSales = null;
    protected TicketsEditor m_panelticket;    
    
    /** Creates new form JTicketsBag */
    public JTicketsBag(AppView oApp, TicketsEditor panelticket) {        
        m_App = oApp;     
        m_panelticket = panelticket;
        
        try {
            m_dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");
        } catch (BeanFactoryException e) {
        }
    }
    
    public abstract void activate();
    public abstract boolean deactivate();
    public abstract void cancelTicket();
    public abstract void saveTicket();
    
    protected abstract JComponent getBagComponent();
    protected abstract JComponent getNullComponent();
    
    public static JTicketsBag createTicketsBag(String sName, AppView app, TicketsEditor panelticket) {
        
        if ("standard".equals(sName)) {
            // return new JTicketsBagMulti(oApp, user, panelticket);
            return new JTicketsBagShared(app, panelticket);
        } else if ("restaurant".equals(sName)) {
            return new JTicketsBagRestaurantMap(app, panelticket);
        } else { // "simple"
            return new JTicketsBagSimple(app, panelticket);
        }
    }
    
    protected final TicketInfo createTicketModel(){

        // creo el nuevo ticket
        TicketInfo ticket = new TicketInfo();
        ticket.setTicketId(0);
        ticket.setDate(new Date());    

        // Pinto el numero del ticket
        return ticket;
    }    
    
    protected final void saveTicket(TicketInfo ticket) {

        try {
            m_dlSales.saveTicket(ticket, m_App.getInventoryLocation());                       
        } catch (BasicException eData) {
            MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.nosaveticket" +
                    ""), eData);
            msg.show(this);
        }
    }
    
    protected final void deleteTicket(TicketInfo ticket) {
        
        try {               
            m_dlSales.deleteTicket(ticket, m_App.getInventoryLocation());
        } catch (BasicException be) {
        }
    }    
}