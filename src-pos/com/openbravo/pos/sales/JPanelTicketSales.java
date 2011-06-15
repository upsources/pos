//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2009 Openbravo, S.L.
//    http://www.openbravo.com/product/pos
//
//    This file is part of Openbravo POS.
//
//    Openbravo POS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    Openbravo POS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with Openbravo POS.  If not, see <http://www.gnu.org/licenses/>.

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
import java.awt.Dimension;
//Subgrupos
import com.openbravo.pos.catalog.JCatalogSubgroups;
import com.openbravo.pos.ticket.TicketLineInfo;

public class JPanelTicketSales extends JPanelTicket {

    private CatalogSelector m_cat;
   
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
        return m_cat.getComponent();
    }

    //Subgrupos

    protected Component getSouthAuxComponent() {
        m_cat = new JCatalogSubgroups(dlSales,
                "true".equals(m_jbtnconfig.getProperty("pricevisible")),
                "true".equals(m_jbtnconfig.getProperty("taxesincluded")),
                Integer.parseInt(m_jbtnconfig.getProperty("img-width", "64")),
                Integer.parseInt(m_jbtnconfig.getProperty("img-height", "54")));
        m_cat.getComponent().setPreferredSize(new Dimension(
                0,
                Integer.parseInt(m_jbtnconfig.getProperty("cat-height", "245"))));
        m_cat.addActionListener(new CatalogListener());
        ((JCatalogSubgroups)m_cat).setGuideMode(true);
        return m_cat.getComponent();
    }

    protected void resetSouthComponent() {
        m_cat.showCatalogPanel(null);
    }
    
    protected JTicketsBag getJTicketsBag() {
        return JTicketsBag.createTicketsBag(m_App.getProperties().getProperty("machine.ticketsbag"), m_App, this);
    }
    
    @Override
    public void activate() throws BasicException {      
        super.activate();
        m_cat.loadCatalog();
    }      
    
    private class CatalogListener implements ActionListener {

        //Subgrupos

        private void reloadCatalog () {
            changeCatalog();
            try {
                m_cat.loadCatalog();
            } catch (BasicException ex) {}
        }

        public void actionPerformed(ActionEvent e) {
            //Subgrupos
            //buttonTransition((ProductInfoExt) e.getSource());
            //Si se ha seleccionado un producto...
            if ( (e.getSource()).getClass().equals(ProductInfoExt.class) ) {
                ProductInfoExt prod = ((ProductInfoExt) e.getSource());

                // Terminamos de procesar una composición.
                if (e.getActionCommand().equals("-1")) {
                    m_iProduct = PRODUCT_SINGLE;
                    reloadCatalog();

                } else {
                    if (prod.getCategoryID().equals("0")) { //Empezamos a procesar una composicion
                        m_iProduct = PRODUCT_COMPOSITION;
                        buttonTransition(prod);
                        reloadCatalog();
                        m_cat.showCatalogPanel(prod.getID());
                    } else
                        buttonTransition(prod);
                }
            } else {
                // Si es una orden de cancelar la venta de una composición
                if ( e.getActionCommand().equals("cancelSubgroupSale")){
                    int i=m_oTicket.getLinesCount();
                    TicketLineInfo line = m_oTicket.getLine(--i);
                    //Quito todas las líneas que son subproductos (puesto que está recién añadido, pertenecen al menú que estamos cancelando
                    while( (i>0) && (line.isSubproduct()) ){
                        m_oTicket.removeLine(i);
                        m_ticketlines.removeTicketLine(i);
                        line= m_oTicket.getLine(--i);
                    }
                    // Quito la línea siguiente, perteneciente al menú en sí
                    if(i >= 0){
                        m_oTicket.removeLine(i);
                        m_ticketlines.removeTicketLine(i);
                    }
                    // Actualizo el interfaz
                    m_iProduct = PRODUCT_SINGLE;
                    reloadCatalog();
                }
            }
        }  
    }
    
    private class CatalogSelectionListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {      
            
            if (!e.getValueIsAdjusting()) {
                int i = m_ticketlines.getSelectedIndex();
                
                if (i >= 0) {
                    // Look for the first non auxiliar product.
                    while (i >= 0 && m_oTicket.getLine(i).isProductCom()) {
                        i--;
                    }

                    // Show the accurate catalog panel...
                    if (i >= 0) {
                        m_cat.showCatalogPanel(m_oTicket.getLine(i).getProductID());
                    } else {
                        m_cat.showCatalogPanel(null);
                    }
                }
            }
        }  
    }
}
