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
import java.util.List;
import com.openbravo.basic.BasicException;
import java.awt.Dimension;

/**
 * Modified by:
 * @author  Luis Ig. Bacas Riveiro	lbacas@opensistemas.com
 * @author  Pablo J. Urbano Santos	purbano@opensistemas.com
 */
public class JPanelTicketEdits extends JPanelTicket {
    
    private JTicketCatalogLines m_catandlines;
    
    /** Creates a new instance of JPanelTicketRefunds */
    public JPanelTicketEdits() {
    }
    
    public String getTitle() {
        return null;
    }
    
    @Override
    public void activate() throws BasicException {      
        super.activate();
        m_catandlines.loadCatalog();
    }
    
    public void showCatalog() {
        m_jbtnconfig.setVisible(true);
        m_catandlines.showCatalog();
        m_bIsRefundPanel = false;
    }
    
    public void showRefundLines(List aRefundLines) {
        // añado las lineas de refund
        m_jbtnconfig.setVisible(false);
        m_catandlines.showRefundLines(aRefundLines);
        m_bIsRefundPanel = true;
    }
    
    protected JTicketsBag getJTicketsBag() {
        return new JTicketsBagTicket(m_App, this);
    }    
    protected Component getSouthComponent() {
        if (m_catandlines == null) {
//            m_catandlines = new JTicketCatalogLines(m_App, this);
            m_catandlines = new JTicketCatalogLines(m_App, this,                
                "true".equals(m_jbtnconfig.getProperty("pricevisible")),
                "true".equals(m_jbtnconfig.getProperty("taxesincluded")),
                Integer.parseInt(m_jbtnconfig.getProperty("img-width", "64")),
                Integer.parseInt(m_jbtnconfig.getProperty("img-height", "54")));
            m_catandlines.setPreferredSize(new Dimension(
                    0,
                    Integer.parseInt(m_jbtnconfig.getProperty("cat-height", "245"))));
//
            m_catandlines.addActionListener(new CatalogListener());
            m_catandlines.showDiscounts(true);
        } else {
            m_catandlines.showCatalog();
        }
        return m_catandlines;
    } 
       
}
