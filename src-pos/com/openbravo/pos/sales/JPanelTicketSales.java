//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007 Openbravo, S.L.
//    http://sourceforge.net/projects/openbravopos
//
//    This file is modified as part of fastfood branch of Openbravo POS. 2008
//    These modifications are copyright Open Sistemas de Información Internet, S.L.
//    http://www.opensistemas.com
//    e-mail: imasd@opensistemas.com
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
import com.openbravo.basic.BasicException;
import com.openbravo.pos.catalog.JCatalog;
import com.openbravo.pos.forms.AppView;
import java.awt.Dimension;

/**
 * Modified by:
 * @author  Luis Ig. Bacas Riveiro	lbacas@opensistemas.com
 * @author  Pablo J. Urbano Santos	purbano@opensistemas.com
 */
public class JPanelTicketSales extends JPanelTicket {
   
    /** Creates a new instance of JPanelTicketSales */
    public JPanelTicketSales() {        
    }
    
    @Override
    public void init(AppView app) {
        super.init(app); 
        m_ticketlines.addListSelectionListener(new CatalogSelectionListener()); 
    }
    
    public String getTitle() {
        return null;
    }
    
    protected Component getSouthComponent() {
        m_cat = new JCatalog(dlSales,
                "true".equals(m_jbtnconfig.getProperty("pricevisible")),
                "true".equals(m_jbtnconfig.getProperty("taxesincluded")),
                Integer.parseInt(m_jbtnconfig.getProperty("img-width", "64")),
                Integer.parseInt(m_jbtnconfig.getProperty("img-height", "54")));
        m_cat.addActionListener(new CatalogListener());
        m_cat.getComponent().setPreferredSize(new Dimension(
                0,
                Integer.parseInt(m_jbtnconfig.getProperty("cat-height", "245"))));
        ( (JCatalog)m_cat).showDiscounts(true);
        return m_cat.getComponent();
    }
    

    protected JTicketsBag getJTicketsBag() {
        return JTicketsBag.createTicketsBag(m_App.getProperties().getProperty("machine.ticketsbag"), m_App, this);
    }
    
    @Override
    public void activate() throws BasicException {      
        super.activate();
        m_cat.loadCatalog();
        m_bIsRefundPanel = false;
    }      
    
}
