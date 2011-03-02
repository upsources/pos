//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2008 Open Sistemas de Información Internet, S.L.
//    http://www.opensistemas.com
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

package com.openbravo.pos.catalog;

import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.JMessageDialog;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.pos.catalog.*;
import com.openbravo.pos.ticket.SubgroupInfo;
import com.openbravo.pos.ticket.ProductInfoExt;
import com.openbravo.pos.util.ThumbNailBuilder;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.DataLogicSales;

/**
 *
 * @author  Luis Ig. Bacas Riveiro	lbacas@opensistemas.com
 * @author  Pablo J. Urbano Santos	purbano@opensistemas.com
 */
public class JCatalogSubgroups extends JPanel implements ListSelectionListener, CatalogSelector {
    protected EventListenerList listeners = new EventListenerList();
    private DataLogicSales m_dlSales;   
    private boolean pricevisible;
    private boolean taxesincluded;
    
    // Set of Products panels
    private Map<String, ProductInfoExt> m_productsset = new HashMap<String, ProductInfoExt>();
    
    // Set of Categoriespanels
     private Set<String> m_subgroupsset = new HashSet<String>();
        
    private ThumbNailBuilder tnbbutton;
    private ThumbNailBuilder tnbcat;
    
    private SubgroupInfo showingsubgr = null;
    private int m_index;
    private boolean m_guided;
    
    /** Creates new form JCatalog */
    public JCatalogSubgroups(DataLogicSales dlSales) {
        this(dlSales, false, false, 64, 54);
    }
    
    public JCatalogSubgroups(DataLogicSales dlSales, boolean pricevisible, boolean taxesincluded, int width, int height) {
        
        m_dlSales = dlSales;
        this.pricevisible = pricevisible;
        this.taxesincluded = taxesincluded;
        
        initComponents();
        
        m_jListSubgroups.addListSelectionListener(this);                
        m_jscrollsubgr.getVerticalScrollBar().setPreferredSize(new Dimension(35, 35));

//TODO Cambiar imagenes por defecto si hay otras. Cambiar tb en SubgroupEditor
        tnbcat = new ThumbNailBuilder(32, 32, "com/openbravo/images/folder_yellow.png");           
        tnbbutton = new ThumbNailBuilder(width, height, "com/openbravo/images/package.png");
    }
    
    public Component getComponent() {
        return this;
    }
    
    public void showCatalogPanel(String id) {
           
        if (id == null) {
            showRootSubgroupsPanel();
        } else {
            try {
                java.util.List<SubgroupInfo> subgroups = m_dlSales.getSubgroups(id);
                m_jListSubgroups.setModel(new SubgroupsListModel(subgroups));
            } catch (BasicException ex) {}
            
            if (m_jListSubgroups.getModel().getSize() > 0) {
                m_jListSubgroups.setSelectedIndex(0);
            }
            //showProductPanel(id);
            showRootSubgroupsPanel();
        }
    }

    public void loadCatalog() throws BasicException {
        // delete all categories panel
        m_jProducts.removeAll();
        
        m_productsset.clear();        
        m_subgroupsset.clear();
        
        showingsubgr = null;
        m_index = 0;

        // Select the first category
        m_jListSubgroups.setCellRenderer(new SmallSubgroupRenderer());
         
        // Display catalog panel
        showRootSubgroupsPanel();
    }

    public void setComponentEnabled(boolean value) {
        
        m_jListSubgroups.setEnabled(value);
        m_jscrollsubgr.setEnabled(value);
        m_lblIndicator.setEnabled(value);
        m_btnBack.setEnabled(value);
        m_jProducts.setEnabled(value); 
        synchronized (m_jProducts.getTreeLock()) {
            int compCount = m_jProducts.getComponentCount();
            for (int i = 0 ; i < compCount ; i++) {
                m_jProducts.getComponent(i).setEnabled(value);
            }
        }
     
        this.setEnabled(value);
    }

    public void setGuideMode (boolean value) {
        m_guided = value;
        m_jListSubgroups.setEnabled(!value);
        m_jscrollsubgr.setEnabled(!value);
    }   
    
    public void addActionListener(ActionListener l) {
        listeners.add(ActionListener.class, l);
    }
    public void removeActionListener(ActionListener l) {
        listeners.remove(ActionListener.class, l);
    }

    public void valueChanged(ListSelectionEvent evt) {
        
        if (!evt.getValueIsAdjusting()) {
            int i = m_jListSubgroups.getSelectedIndex();
            if (i >= 0) {
                // Lo hago visible...
                Rectangle oRect = m_jListSubgroups.getCellBounds(i, i);
                m_jListSubgroups.scrollRectToVisible(oRect);       
            }
        }
    }  
    
    protected void fireSelectedProduct(ProductInfoExt prod) {
        EventListener[] l = listeners.getListeners(ActionListener.class);
        ActionEvent e = null;
        
        for (int i = 0; i < l.length; i++) {
            if (e == null) {
                e = new ActionEvent(prod, ActionEvent.ACTION_PERFORMED, prod.getID());
            }
            ((ActionListener) l[i]).actionPerformed(e);	       
        }
        
        if (m_guided) {
            m_index++;
            
            //Si hay subgrupos de los que elegir...
            if (m_jListSubgroups.getModel().getSize() > m_index) {
                m_jListSubgroups.setSelectedIndex(m_index);
            
            //Si ya hemos elegido un producto de cada subgrupo hacemos changeCatalog
            } else {
                //Enviamos un codigo para que se haga un changeCatalog
                e = null;
                for (int i = 0; i < l.length; i++) {
                    if (e == null) {
                        e = new ActionEvent(prod, ActionEvent.ACTION_PERFORMED, "-1");
                    }
                    ((ActionListener) l[i]).actionPerformed(e);	       
                }
            }
        }
    }   

    /**
     * Call for cancelling the subgroup sale
     *
     */
    private void cancelSubgroupSale() {
        EventListener[] l = listeners.getListeners(ActionListener.class);
        ActionEvent e = null;
        
        for (int i = 0; i < l.length; i++) {
            if (e == null) {                
                e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "cancelSubgroupSale");
            }
            ((ActionListener) l[i]).actionPerformed(e);	       
        }

    }
    
    private void selectSubgroupPanel(String sid) {

        try {
            // Load subgroups panel if not exists
            if (!m_subgroupsset.contains(sid)) {
                
                JCatalogTab jcurrTab = new JCatalogTab();      
                m_jProducts.add(jcurrTab, sid);
                m_subgroupsset.add(sid);
                
                // Add products
                java.util.List<ProductInfoExt> products = m_dlSales.getSubgroupCatalog(sid);
                for (ProductInfoExt prod : products) {
                    jcurrTab.addButton(new ImageIcon(tnbbutton.getThumbNailText(prod.getImage(), getProductLabel(prod))), new SelectedAction(prod));
                }
            }
            
            // Show categories panel
            CardLayout cl = (CardLayout)(m_jProducts.getLayout());
            cl.show(m_jProducts, sid);  
        } catch (BasicException e) {
            JMessageDialog.showMessage(this, new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.notactive"), e));            
        }
    }
    
    private String getProductLabel(ProductInfoExt product) {

        if (pricevisible) {
            if (taxesincluded) {
                return "<html><center>" + product.getName() + "<br>" + product.printPriceSellTax();
            } else {
                return "<html><center>" + product.getName() + "<br>" + product.printPriceSell();
            }
        } else {
            return product.getName();
        }
    }
    
    private void selectIndicatorPanel(Icon icon, String label) {
        
        m_lblIndicator.setText(label);
        m_lblIndicator.setIcon(icon);
        
        // Show subcategories panel
        CardLayout cl = (CardLayout)(m_jCategories.getLayout());
        cl.show(m_jCategories, "subcategories");
    }

    private void selectIndicatorSubgroups() {
        // Show root categories panel
        CardLayout cl = (CardLayout)(m_jCategories.getLayout());
        cl.show(m_jCategories, "rootcategories");
    }

    private void showRootSubgroupsPanel() {
        selectIndicatorSubgroups();
        // Show selected root category
        SubgroupInfo s = (SubgroupInfo) m_jListSubgroups.getSelectedValue();
        if (s != null) {
            selectSubgroupPanel(s.getID());
        }
        showingsubgr = null;
    }

    private void showSubgroupPanel(SubgroupInfo s) {
        
        selectIndicatorPanel(new ImageIcon(tnbbutton.getThumbNail(s.getImage())), s.getName());
        selectSubgroupPanel(s.getID());
        showingsubgr = s;
    }
/*
    private void showProductPanel(String id) {
            
        ProductInfoExt product = m_productsset.get(id);

        if (product == null) {
            if (m_productsset.containsKey(id)) {
                // It is an empty panel
                if (showingsubgr == null) {
                    showRootSubgroupsPanel();                         
                } else {
                    showSubgroupPanel(showingsubgr);
                }
            } else {
                try {
                    // Create  products panel
                    java.util.List<ProductInfoExt> products = m_dlSales.getProductComments(id);

                    if (products.size() == 0) {
                        // no hay productos por tanto lo anado a la de vacios y muestro el panel principal.
                        m_productsset.put(id, null);
                        if (showingsubgr == null) {
                            showRootSubgroupsPanel();                         
                        } else {
                            showSubgroupPanel(showingsubgr);
                        }
                    } else {

                        // Load product panel
                        product = m_dlSales.getProductInfo(id);
                        m_productsset.put(id, product);

                        JCatalogTab jcurrTab = new JCatalogTab();      
                        m_jProducts.add(jcurrTab, "PRODUCT." + id);                        

                        // Add products
                        for (ProductInfoExt prod : products) {
                            jcurrTab.addButton(new ImageIcon(tnbbutton.getThumbNailText(prod.getImage(), getProductLabel(prod))), new SelectedAction(prod));
                        }                       

                        selectIndicatorPanel(new ImageIcon(tnbbutton.getThumbNail(product.getImage())), product.getName());

                        CardLayout cl = (CardLayout)(m_jProducts.getLayout());
                        cl.show(m_jProducts, "PRODUCT." + id); 
                    }
                } catch (BasicException eb) {
                    eb.printStackTrace();
                    m_productsset.put(id, null);
                    if (showingsubgr == null) {
                        showRootSubgroupsPanel();                         
                    } else {
                        showSubgroupPanel(showingsubgr);
                    }
                }
            }
        } else {
            // already exists
            selectIndicatorPanel(new ImageIcon(tnbbutton.getThumbNail(product.getImage())), product.getName());

            CardLayout cl = (CardLayout)(m_jProducts.getLayout());
            cl.show(m_jProducts, "PRODUCT." + id); 
        }
    }
*/
    private class SelectedAction implements ActionListener {
        private ProductInfoExt prod;
        public SelectedAction(ProductInfoExt prod) {
            this.prod = prod;
        }
        public void actionPerformed(ActionEvent e) {
            fireSelectedProduct(prod);
        }
    }
/*    
    private class SelectedSubgroup implements ActionListener {
        private SubgroupInfo s;
        public SelectedSubgroup(SubgroupInfo s) {
            this.s = s;
        }
        public void actionPerformed(ActionEvent e) {
            showSubgroupPanel(s);
        }
    }
*/    
    private class SubgroupsListModel extends AbstractListModel {
        private java.util.List m_aSubgroups;
        public SubgroupsListModel(java.util.List aSubgroups) {
            m_aSubgroups = aSubgroups;
        }
        public int getSize() { 
            return m_aSubgroups.size(); 
        }
        public Object getElementAt(int i) {
            return m_aSubgroups.get(i);
        }    
    }
    
    private class SmallSubgroupRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, null, index, isSelected, cellHasFocus);
            SubgroupInfo s = (SubgroupInfo) value;
            setText(s.getName());
            setIcon(new ImageIcon(tnbcat.getThumbNail(s.getImage())));
            return this;
        }      
    }            
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        m_jCategories = new javax.swing.JPanel();
        m_jRootCategories = new javax.swing.JPanel();
        m_jscrollsubgr = new javax.swing.JScrollPane();
        m_jListSubgroups = new javax.swing.JList();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        m_jCancel = new javax.swing.JButton();
        m_jSubCategories = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        m_lblIndicator = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        m_btnBack = new javax.swing.JButton();
        m_jProducts = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        m_jCategories.setLayout(new java.awt.CardLayout());

        m_jRootCategories.setLayout(new java.awt.BorderLayout());

        m_jscrollsubgr.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        m_jscrollsubgr.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        m_jscrollsubgr.setPreferredSize(new java.awt.Dimension(210, 0));
        m_jListSubgroups.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        m_jListSubgroups.setFocusable(false);
        m_jListSubgroups.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                m_jListSubgroupsValueChanged(evt);
            }
        });

        m_jscrollsubgr.setViewportView(m_jListSubgroups);

        m_jRootCategories.add(m_jscrollsubgr, java.awt.BorderLayout.WEST);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new java.awt.GridLayout(0, 1, 0, 5));

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 5));
        m_jCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/button_cancel.png")));
        m_jCancel.setFocusPainted(false);
        m_jCancel.setFocusable(false);
        m_jCancel.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jCancel.setRequestFocusEnabled(false);
        m_jCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jCancelActionPerformed(evt);
            }
        });

        jPanel3.add(m_jCancel);

        jPanel2.add(jPanel3, java.awt.BorderLayout.NORTH);

        m_jRootCategories.add(jPanel2, java.awt.BorderLayout.CENTER);

        m_jCategories.add(m_jRootCategories, "rootcategories");

        m_jSubCategories.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new java.awt.BorderLayout());

        m_lblIndicator.setText("jLabel1");
        jPanel4.add(m_lblIndicator, java.awt.BorderLayout.NORTH);

        m_jSubCategories.add(jPanel4, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel5.setLayout(new java.awt.GridLayout(0, 1, 0, 5));

        jPanel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 5));
        m_btnBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/3uparrow.png")));
        m_btnBack.setFocusPainted(false);
        m_btnBack.setFocusable(false);
        m_btnBack.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_btnBack.setRequestFocusEnabled(false);
        m_btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_btnBackActionPerformed(evt);
            }
        });

        jPanel5.add(m_btnBack);

        jPanel1.add(jPanel5, java.awt.BorderLayout.NORTH);

        m_jSubCategories.add(jPanel1, java.awt.BorderLayout.EAST);

        m_jCategories.add(m_jSubCategories, "subcategories");

        add(m_jCategories, java.awt.BorderLayout.WEST);

        m_jProducts.setLayout(new java.awt.CardLayout());

        m_jProducts.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 0));
        add(m_jProducts, java.awt.BorderLayout.CENTER);

    }// </editor-fold>//GEN-END:initComponents

    private void m_jCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jCancelActionPerformed
        cancelSubgroupSale();        
    }//GEN-LAST:event_m_jCancelActionPerformed

    private void m_btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_btnBackActionPerformed

        showRootSubgroupsPanel();        
        
    }//GEN-LAST:event_m_btnBackActionPerformed

    private void m_jListSubgroupsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_m_jListSubgroupsValueChanged

        if (!evt.getValueIsAdjusting()) {
            SubgroupInfo s = (SubgroupInfo) m_jListSubgroups.getSelectedValue();
            if (s != null) {
                selectSubgroupPanel(s.getID());
            }
        }
        
}//GEN-LAST:event_m_jListSubgroupsValueChanged
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JButton m_btnBack;
    private javax.swing.JButton m_jCancel;
    private javax.swing.JPanel m_jCategories;
    private javax.swing.JList m_jListSubgroups;
    private javax.swing.JPanel m_jProducts;
    private javax.swing.JPanel m_jRootCategories;
    private javax.swing.JPanel m_jSubCategories;
    private javax.swing.JScrollPane m_jscrollsubgr;
    private javax.swing.JLabel m_lblIndicator;
    // End of variables declaration//GEN-END:variables
    
}
