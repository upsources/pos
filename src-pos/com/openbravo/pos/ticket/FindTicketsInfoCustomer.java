/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openbravo.pos.ticket;

import com.openbravo.format.Formats;

/**
 *
 * @author Administrator
 */
public class FindTicketsInfoCustomer extends FindTicketsInfo {



    @Override
    public String toString(){
        String sHtml = "["+ super.getTicketId() +"]   " + Formats.DATE.formatValue(super.date) +" " +
                Formats.CURRENCY.formatValue(total) ;

        return sHtml;
    }

}
