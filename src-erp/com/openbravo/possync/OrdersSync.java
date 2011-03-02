//    Openbravo POS is a point of sales application designed for touch screens.
//    http://sourceforge.net/projects/openbravopos
//
//    Copyright (c) 2007 openTrends Solucions i Sistemes, S.L
//    Modified by Openbravo SL on March 22, 2007
//    These modifications are copyright Openbravo SL
//    Author/s: A. Romero
//    You may contact Openbravo SL at: http://www.openbravo.com
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

package com.openbravo.possync;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.List;
import javax.xml.rpc.ServiceException;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.forms.ProcessAction;
import com.openbravo.pos.payment.PaymentInfo;
import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.ticket.TicketLineInfo;
import com.openbravo.ws.externalsales.BPartner;
import com.openbravo.ws.externalsales.Order;
import com.openbravo.ws.externalsales.OrderIdentifier;
import com.openbravo.ws.externalsales.OrderLine;
import com.openbravo.ws.externalsales.Payment;


public class OrdersSync implements ProcessAction {
     
    private DataLogicSystem dlsystem;
    private DataLogicIntegration dlintegration;

    /** Creates a new instance of OrdersSync */
    public OrdersSync(DataLogicSystem dlsystem, DataLogicIntegration dlintegration) {
        this.dlsystem = dlsystem;
        this.dlintegration = dlintegration;
    }
    
    public MessageInf execute() throws BasicException {        
        
        try {
        
            ExternalSalesHelper externalsales = new ExternalSalesHelper(dlsystem);                    

            // Obtenemos los tickets
            List<TicketInfo> ticketlist = dlintegration.getTickets();
            for (TicketInfo ticket : ticketlist) {
                ticket.setLines(dlintegration.getTicketLines(ticket.getId()));
                ticket.setPayments(dlintegration.getTicketPayments(ticket.getId()));
            }
            
            if (ticketlist.size() == 0) {
                
                return new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.zeroorders"));
            } else {

                // transformo tickets en ordenes
                Order[] orders = transformTickets(ticketlist);

                // subo las ordenes
                externalsales.uploadOrders(orders);

                // actualizo los tickets como subidos
                dlintegration.execTicketUpdate();

                return new MessageInf(MessageInf.SGN_SUCCESS, AppLocal.getIntString("message.syncordersok"), AppLocal.getIntString("message.syncordersinfo", orders.length));
            }

        } catch (ServiceException e) {            
            throw new BasicException(AppLocal.getIntString("message.serviceexception"), e);
        } catch (RemoteException e){
            throw new BasicException(AppLocal.getIntString("message.remoteexception"), e);
        } catch (MalformedURLException e){
            throw new BasicException(AppLocal.getIntString("message.malformedurlexception"), e);
        }
    }  
    
    private Order[] transformTickets(List<TicketInfo> ticketlist) {

        // Transformamos de tickets a ordenes
        Order[] orders = new Order[ticketlist.size()];
        for (int i = 0; i < ticketlist.size(); i++) {
            TicketInfo ticket = ticketlist.get(i);

            orders[i] = new Order();

            OrderIdentifier orderid = new OrderIdentifier();
            Calendar datenew = Calendar.getInstance();
            datenew.setTime(ticket.getDate());
            orderid.setDateNew(datenew);
            orderid.setDocumentNo(Integer.toString(ticket.getTicketId()));

            orders[i].setOrderId(orderid);
            orders[i].setState(800175);
            
            // set the business partner
            BPartner bp;
            if (ticket.getCustomerId() == null) {
                bp = null;
            } else {
                bp = new BPartner();
                bp.setId(ticket.getCustomer().getId());
                bp.setName(ticket.getCustomer().getName());
            }
            orders[i].setBusinessPartner(bp);

            //Saco las lineas del pedido
            OrderLine[] orderLine = new OrderLine[ticket.getLines().size()];
            for (int j = 0; j < ticket.getLines().size(); j++){
                TicketLineInfo line = ticket.getLines().get(j);

                orderLine[j] = new OrderLine();
                orderLine[j].setOrderLineId(line.getTicketLine()); // o simplemente "j"
                if (line.getProduct().getId() == null) {
                    orderLine[j].setProductId(0);
                } else {
                    orderLine[j].setProductId(parseInt(line.getProduct().getId())); // capturar error
                }
                orderLine[j].setUnits(line.getMultiply());
                orderLine[j].setPrice(line.getPrice());
                orderLine[j].setTaxId(parseInt(line.getTaxInfo().getId()));     
            }
            orders[i].setLines(orderLine);

            //Saco las lineas de pago
            Payment[] paymentLine = new Payment[ticket.getPayments().size()];
            for (int j = 0; j < ticket.getPayments().size(); j++){       
                PaymentInfo payment = ticket.getPayments().get(j);

                paymentLine[j] = new Payment();
                paymentLine[j].setAmount(payment.getTotal());
                if ("magcard".equals(payment.getName())) {
                    paymentLine[j].setPaymentType("K");
                } else if ("cheque".equals(payment.getName())) {
                    paymentLine[j].setPaymentType("2");
                } else if ("cash".equals(payment.getName())) {
                    paymentLine[j].setPaymentType("B");
                } else {
                    paymentLine[j].setPaymentType(null); // unknown
                }        
            }     
            orders[i].setPayment(paymentLine);                    
        }
        
        return orders;
    }
    
    private static int parseInt(String sValue) {
        
        try {
            return Integer.parseInt(sValue); 
        } catch (NumberFormatException eNF) {
            return 0;
        }
    }
}
