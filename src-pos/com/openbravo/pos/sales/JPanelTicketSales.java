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

package com.openbravo.pos.sales;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.openbravo.basic.BasicException;
import com.openbravo.pos.catalog.CatalogSelector;
import com.openbravo.pos.catalog.JCatalog;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.ticket.ProductInfoExt;
import com.openbravo.pos.ticket.ThumbNailBuilderProduct;

public class JPanelTicketSales extends JPanelTicket {

    private CatalogSelector m_cat;
   
    /** Creates a new instance of JPanelTicketSales */
    public JPanelTicketSales(AppView oApp) {
        super(oApp);
        
        m_ticketlines.addListSelectionListener(new CatalogSelectionListener());
    }
    
    public String getTitle() {
        return null;
    }
    
    protected Component getSouthComponent() {
        m_cat = new JCatalog(m_dlSales,
                m_jbtnconfig.isPriceVisible() 
                ? (m_jbtnconfig.isTaxesIncluded() 
                    ? ThumbNailBuilderProduct.PRICE_SELLTAX 
                    : ThumbNailBuilderProduct.PRICE_SELL)
                : ThumbNailBuilderProduct.PRICE_NONE);
        m_cat.addActionListener(new CatalogListener());
        return m_cat.getComponent();
    }
    
    protected JTicketsBag getJTicketsBag() {
        return JTicketsBag.createTicketsBag(m_App.getProperties().getProperty("machine.ticketsbag"), m_App, this);
    }
    
    public void activate() throws BasicException {      
        super.activate();
        m_cat.loadCatalog();
    }      
    
    private class CatalogListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            buttonTransition((ProductInfoExt) e.getSource());
        }  
    }
    
    private class CatalogSelectionListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {      
            
            if (!e.getValueIsAdjusting()) {
                int i = m_ticketlines.getSelectedIndex();
                
                // Buscamos el primer producto no Auxiliar.
                while (i >= 0 && m_oTicket.getLine(i).isProductCom()) {
                    i--;
                }
                
                // Mostramos el panel de catalogo adecuado...
                if (i >= 0) {
                    m_cat.showCatalogPanel(m_oTicket.getLine(i).getProductID());
                } else {
                    m_cat.showCatalogPanel(null);
                }
            }
        }  
    }
}
