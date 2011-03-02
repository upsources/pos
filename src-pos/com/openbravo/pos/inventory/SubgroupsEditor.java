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

package com.openbravo.pos.inventory;

import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;

import com.openbravo.basic.BasicException;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.data.gui.JMessageDialog;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.pos.catalog.*;
import com.openbravo.pos.ticket.CategoryInfo;
import com.openbravo.pos.ticket.SubgroupInfo;
import com.openbravo.pos.ticket.ProductInfoExt;
import com.openbravo.pos.util.ThumbNailBuilder;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.DataLogicSales;

/**
 * Esta clase crea un JPanel para editar los subgrupos que contiene una composición 
 * y los productos contenidos en dichos subgrupos. 
 * 
 * @author  Luis Ig. Bacas Riveiro	lbacas@opensistemas.com
 * @author  Pablo J. Urbano Santos	purbano@opensistemas.com
 */
public class SubgroupsEditor extends JPanel implements ListSelectionListener, CatalogSelector {
    
    protected EventListenerList listeners = new EventListenerList();
    protected ComboBoxValModel m_CategoryModel;
    protected DirtyManager m_dirty;
    
    private java.util.List m_catlist;
    private DataLogicSales m_dlSales;
    private String m_composition;
    
    private JCatalogTab m_jCurrTab; 
    private Map<String, JCatalogTab> m_tabset = new HashMap<String, JCatalogTab>();
    private Map<String, Set<ProductInfoExt>> m_productsset = new HashMap<String, Set<ProductInfoExt>>();
    private Set<SubgroupInfo> m_subgroupsset = new HashSet<SubgroupInfo>();
        
    private ThumbNailBuilder tnbbutton;
    private ThumbNailBuilder tnbsubgr;
    
    private SubgroupInfo showingsubgr = null;
    private SubgroupInfo m_selSubgr = null;
    private ProductInfoExt m_selProd = null;
    
    /** Creates new form JCatalog */
    public SubgroupsEditor(DataLogicSales dlSales, DirtyManager dirty) {
        this(dlSales, dirty, 64, 54);
    }
    
    public SubgroupsEditor(DataLogicSales dlSales, DirtyManager dirty, int width, int height) {      
        m_dlSales = dlSales;
        m_dirty = dirty;
        
        initComponents();
        
        m_jListSubgroups.addListSelectionListener(this);                
        m_jscrollsubgr.getVerticalScrollBar().setPreferredSize(new Dimension(35, 35));

//TODO Cambiar imagenes por defecto si hay otras. Cambiar tb en JCatalogSubgroups
        tnbsubgr = new ThumbNailBuilder(32, 32, "com/openbravo/images/folder_yellow.png");           
        tnbbutton = new ThumbNailBuilder(width, height, "com/openbravo/images/package.png");
        
        // Rellenamos el JComboBox con las categorias disponibles
        m_CategoryModel = new ComboBoxValModel();
        m_jCategory.addActionListener(new SelectedCategory());
        m_jProduct.addActionListener(new SelectedProduct());
    }
    
    /**
     * Establece la composición que será objeto de edición
     * @param id Identificador de la composición
     */
    public void setComposition(Object id) {
        m_composition = id != null? id.toString(): null;
        try {        
            loadCatalog();
        } catch (BasicException e) {
            m_composition = null;
        }
    }
    
    public Component getComponent() {
        return this;
    }
    
    
    private Set<SubgroupInfo> getSubgroups() {
        return m_subgroupsset;
    }
    
    private Set<ProductInfoExt> getProducts(SubgroupInfo s) {
        return m_productsset.get(s.getID());
    }
    
    /*
     * Generamos un array para el createValue de la clase CompositorEditor
     * @return Object[] Lista de valores para el createValue
     */
    public Object getValues() throws BasicException {
        int i = 0;
        int ssize = getSubgroups().size() * 4;
        
        if (ssize == 0)
            throw new BasicException(AppLocal.getIntString("message.emptycomposition"));
        
        //Calculamos el numero de productos totales
        int psize = 0;
        for (SubgroupInfo s: m_subgroupsset) {
            if (getProducts(s) == null || getProducts(s).size() == 0)
                throw new BasicException(AppLocal.getIntString("message.emptysubgroup"));
            else
                psize += getProducts(s).size();
        }
        
        Object[] ret = new Object[1 + psize + ssize];
        
        //Empezamos a rellenar
        ret[i] = m_subgroupsset.size();      
        for (SubgroupInfo s: m_subgroupsset) {
            ret[++i] = s.getID();
            ret[++i] = s.getName();
            ret[++i] = s.getImage();
            ret[++i] = getProducts(s).size();
            for (ProductInfoExt p: getProducts(s)) {
                ret[++i] = p.getID();
            }
        }
        
        return ret;
    }

    public void showCatalogPanel(String id) {           
//        showProductPanel(id);
    }
    
    /*
     * Limpia los controles y carga de nuevo la información de subgrupos y productos
     * desde la Base de Datos */
    public void loadCatalog() throws BasicException {
        java.util.List<SubgroupInfo> subgroups = new java.util.ArrayList<SubgroupInfo>();
        
        // delete all panels
        m_jProducts.removeAll();
        
        m_productsset.clear();        
        m_subgroupsset.clear();
        
        showingsubgr = null;
        
        m_selSubgr = null;
        m_selProd = null;
        
        //Limpiamos los controles de edicion de subgrupos
        m_jName.setText("");
        m_jLittleImageEditor.setImage(null);
        
        // Load all subgroups.
        if (m_composition != null) {
            subgroups = m_dlSales.getSubgroups(m_composition);
        }
        
        // Load subgroup panels
        for (SubgroupInfo s: subgroups) {
            m_jCurrTab = new JCatalogTab();      
            m_jProducts.add(m_jCurrTab, s.getID());
            selectSubgroupPanel(s);
            //m_subgroupsset.add(s);

            // Add products
            java.util.List<ProductInfoExt> products = m_dlSales.getSubgroupCatalog(s.getID());
            m_productsset.put(s.getID(), new HashSet<ProductInfoExt>(products));
            for (ProductInfoExt prod : products) {
                m_jCurrTab.addButton(new ImageIcon(tnbbutton.getThumbNailText(prod.getImage(), getProductLabel(prod))), prod.getID(), new SelectedAction(prod));
            }
        }

        // Select the first subgroup
        m_jListSubgroups.setCellRenderer(new SmallSubgroupRenderer());
        m_jListSubgroups.setModel(new SubgroupsListModel(subgroups));
        if (m_jListSubgroups.getModel().getSize() > 0) {
            m_jListSubgroups.setSelectedIndex(0);
        }
        
        fillCategoriesBox();
    }
    
    public void setComponentEnabled(boolean value) {
        m_jListSubgroups.setEnabled(value);
        m_jscrollsubgr.setEnabled(value);
        m_jUp.setEnabled(value);
        m_jDown.setEnabled(value);
        m_jProducts.setEnabled(value); 
        synchronized (m_jProducts.getTreeLock()) {
            int compCount = m_jProducts.getComponentCount();
            for (int i = 0 ; i < compCount ; i++) {
                m_jProducts.getComponent(i).setEnabled(value);
            }
        }
     
        this.setEnabled(value);
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
        for (int i = 0; i < m_jCategory.getItemCount(); i++) {
            if ( ((CategoryInfo)m_jCategory.getItemAt(i)).getID().equals(prod.getCategoryID()) ) {
                m_jCategory.setSelectedIndex(i);
            }
        }
        
        for (int i = 0; i < m_jProduct.getItemCount(); i++) {
            if ( ((ProductInfoExt)m_jProduct.getItemAt(i)).getID().equals(prod.getID()) ) {
                m_jProduct.setSelectedIndex(i);
            }
        }
        
        //Establecemos el producto seleccionado
        m_selProd = prod;
    }
    
    
    private void selectSubgroupPanel(SubgroupInfo s) {
        // Load subgroup panel if not exists
        if (!m_subgroupsset.contains(s)) {
            m_jCurrTab = new JCatalogTab();      
            m_jProducts.add(m_jCurrTab, s.getID());
            m_subgroupsset.add(s);
            m_tabset.put(s.getID(), m_jCurrTab);
        } else
            m_jCurrTab = m_tabset.get(s.getID());
        
        m_selSubgr = s;

        // Show subgroups panel
        CardLayout cl = (CardLayout)(m_jProducts.getLayout());
        cl.show(m_jProducts, s.getID());
    }
    
    
    private String getProductLabel(ProductInfoExt product) {
        return product.getName();
    }
    
/*
    private void selectIndicatorPanel(Icon icon, String label) {
        // Show subcategories panel
        CardLayout cl = (CardLayout)(m_jSubgroups.getLayout());
        cl.show(m_jSubgroups, "subcategories");
    }

    private void showSubgroupPanel(SubgroupInfo subgr) {
        selectIndicatorPanel(new ImageIcon(tnbbutton.getThumbNail(subgr.getImage())), subgr.getName());
        selectSubgroupPanel(subgr);
        showingsubgr= subgr;
    }
*/
    
/*
    private void showProductPanel(String id) { 
        ProductInfoExt product = m_productsset.get(id);

        if (product == null) {
            if (m_productsset.containsKey(id)) {
                // It is an empty panel
                showSubgroupPanel(showingsubgr);
            } else {
                try {
                    // Create  products panel
                    java.util.List<ProductInfoExt> products = m_dlSales.getProductComments(id);

                    if (products.size() == 0) {
                        // no hay productos por tanto lo anado a la de vacios y muestro el panel principal.
                        m_productsset.put(id, null);
                        showSubgroupPanel(showingsubgr);
                    } else {
                        // Load product panel
                        product = m_dlSales.getProductInfo(id);
                        m_productsset.put(id, product);

                        JCatalogTab jcurrTab = new JCatalogTab();      
                        m_jProducts.add(jcurrTab, "PRODUCT." + id);                        

                        // Add products
                        for (ProductInfoExt prod : products) {
                            jcurrTab.addButton(new ImageIcon(tnbbutton.getThumbNailText(prod.getImage(), getProductLabel(prod))), prod.getID(), new SelectedAction(prod));
                        }                       

                        selectIndicatorPanel(new ImageIcon(tnbbutton.getThumbNail(product.getImage())), product.getName());

                        CardLayout cl = (CardLayout)(m_jProducts.getLayout());
                        cl.show(m_jProducts, "PRODUCT." + id); 
                    }
                } catch (BasicException eb) {
                    eb.printStackTrace();
                    m_productsset.put(id, null);
                    showSubgroupPanel(showingsubgr);
                }
            }
        } else {
            // already exists
            selectIndicatorPanel(new ImageIcon(tnbbutton.getThumbNail(product.getImage())), product.getName());

            CardLayout cl = (CardLayout)(m_jProducts.getLayout());
            cl.show(m_jProducts, "PRODUCT." + id); 
        }
    }
*
    /*
     * Metodo privado que rellena el jComboBox con las categorias para poder 
     * elegir posteriormente un producto y añadirlo a un sugrupo.
     * NOTA: La categoria Composiciones no será incluida
     */
    private void fillCategoriesBox () {   
        //Leemos y añadimos al JComboBox las categorias
        SentenceList sentcat = m_dlSales.getCategoriesList();
        CategoryInfo catinfo;
        
        try {
            m_catlist = sentcat.list();

            //Eliminamos de la lista de categorias la categoria Composiciones
            for (int i=0; i < m_catlist.size(); i++) {
                catinfo = (CategoryInfo)m_catlist.get(i);
                if (catinfo.getID().equals("0")) {
                    m_catlist.remove(i);
                    break;
                } 
            }
        } catch (BasicException e) {
            m_catlist = null;
        }
        
        m_CategoryModel.refresh(m_catlist);
        m_jCategory.setModel(m_CategoryModel);
        if (m_jCategory.getItemCount() > 0) {
            m_jCategory.setSelectedIndex(0);
            fillProductsBox ();
        }
    }
    
    /*
     * Metodo privado que rellena el jComboBox con las categorias para poder 
     * elegir posteriormente un producto y añadirlo a un sugrupo.
     * NOTA: La categoria Composiciones no será incluida
     */
    protected void fillProductsBox () {
        java.util.List<ProductInfoExt> products;
//        ProductInfoExt prodAll = new ProductInfoExt();  
        String catid = (String)m_CategoryModel.getSelectedKey();

        try {
            // Obtenemos la lista de productos de la BBDD
            products = m_dlSales.getProductCatalog(catid);
            if (products.size() > 0)
                m_selProd = products.get(0);
            
            // Si hay algun elemento añadimos al combo la opcion "All"
/*            if (products.size() > 0) {
                prodAll.setID("all");
                prodAll.setReference("");
                prodAll.setName(AppLocal.getIntString("label.allprod"));
                products.add(0, prodAll);
            }
 */
            // Añadimos la lista de productos al ComboBox
            m_jProduct.setModel(new ComboBoxValModel(products));
        } catch (BasicException e) {
            JMessageDialog.showMessage(this, new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.notactive"), e));            
        }
        
        //Si hay elementos en el ComboBox, seleccionamos el primero
        if (m_jProduct.getItemCount() > 0)
            m_jProduct.setSelectedIndex(0);
    }
    
    protected void selectProductBox() {
        m_selProd = ((ProductInfoExt)m_jProduct.getSelectedItem());
    }

    
    
    private class SelectedAction implements ActionListener {
        private ProductInfoExt prod;
        
        public SelectedAction(ProductInfoExt prod) {
            this.prod = prod;
        }
        public void actionPerformed(ActionEvent e) {
            fireSelectedProduct(prod);
        }
    }

    private class SelectedCategory implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            fillProductsBox();
        }
    }
    
    private class SelectedProduct implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            selectProductBox();
        }
    }
/*    
    private class SelectedSubgroup implements ActionListener {
        private SubgroupInfo subgr;
        public SelectedSubgroup(SubgroupInfo subgr) {
            this.subgr = subgr;
        }
        public void actionPerformed(ActionEvent e) {
            showSubgroupPanel(subgr);
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
        public void add(Object c) {
            m_aSubgroups.add(c);
        }
        public void del(Object c) {
            m_aSubgroups.remove(c);
        }
    }

    
    private class SmallSubgroupRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, null, index, isSelected, cellHasFocus);
            SubgroupInfo s = (SubgroupInfo) value;
            setText(s.getName());
            setIcon(new ImageIcon(tnbsubgr.getThumbNail(s.getImage())));
            return this;
        }      
    }            
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        m_jSubgroups = new javax.swing.JPanel();
        m_jRootSubgroups = new javax.swing.JPanel();
        m_jscrollsubgr = new javax.swing.JScrollPane();
        m_jListSubgroups = new javax.swing.JList();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        m_jUp = new javax.swing.JButton();
        m_jDown = new javax.swing.JButton();
        m_jProducts = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        m_jName = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        m_jLittleImageEditor = new com.openbravo.data.gui.JLittleImageEditor();
        m_jbtnNewSubgroup = new javax.swing.JButton();
        m_jbtnDelSubgroup = new javax.swing.JButton();
        m_jbtnSaveSubgroup = new javax.swing.JButton();
        m_jAddProducts = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        m_jCategory = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        m_jProduct = new javax.swing.JComboBox();
        m_jbtnAddProduct = new javax.swing.JButton();
        m_jbtnDelProduct = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        m_jSubgroups.setLayout(new java.awt.CardLayout());

        m_jRootSubgroups.setLayout(new java.awt.BorderLayout());

        m_jscrollsubgr.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        m_jscrollsubgr.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        m_jscrollsubgr.setPreferredSize(new java.awt.Dimension(180, 0));

        m_jListSubgroups.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        m_jListSubgroups.setFocusable(false);
        m_jListSubgroups.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                m_jListSubgroupsValueChanged(evt);
            }
        });
        m_jscrollsubgr.setViewportView(m_jListSubgroups);

        m_jRootSubgroups.add(m_jscrollsubgr, java.awt.BorderLayout.WEST);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 5));
        jPanel3.setLayout(new java.awt.GridLayout(0, 1, 0, 5));

        m_jUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/1uparrow22.png"))); // NOI18N
        m_jUp.setFocusPainted(false);
        m_jUp.setFocusable(false);
        m_jUp.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jUp.setRequestFocusEnabled(false);
        m_jUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jUpActionPerformed(evt);
            }
        });
        jPanel3.add(m_jUp);

        m_jDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/1downarrow22.png"))); // NOI18N
        m_jDown.setFocusPainted(false);
        m_jDown.setFocusable(false);
        m_jDown.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jDown.setRequestFocusEnabled(false);
        m_jDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jDownActionPerformed(evt);
            }
        });
        jPanel3.add(m_jDown);

        jPanel2.add(jPanel3, java.awt.BorderLayout.NORTH);

        m_jRootSubgroups.add(jPanel2, java.awt.BorderLayout.CENTER);

        m_jSubgroups.add(m_jRootSubgroups, "rootcategories");

        jPanel1.add(m_jSubgroups, java.awt.BorderLayout.WEST);

        m_jProducts.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 0));
        m_jProducts.setMinimumSize(new java.awt.Dimension(100, 0));
        m_jProducts.setPreferredSize(new java.awt.Dimension(252, 0));
        m_jProducts.setLayout(new java.awt.CardLayout());
        jPanel1.add(m_jProducts, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel5.setBorder(null);
        jPanel5.setMinimumSize(new java.awt.Dimension(97, 105));
        jPanel5.setPreferredSize(new java.awt.Dimension(252, 105));

        jLabel3.setText(AppLocal.getIntString("Label.Name")); // NOI18N

        jLabel8.setText(AppLocal.getIntString("label.image")); // NOI18N

        m_jLittleImageEditor.setMaxDimensions(new java.awt.Dimension(32, 32));

        m_jbtnNewSubgroup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/editnew.png"))); // NOI18N
        m_jbtnNewSubgroup.setFocusPainted(false);
        m_jbtnNewSubgroup.setFocusable(false);
        m_jbtnNewSubgroup.setMargin(new java.awt.Insets(2, 8, 2, 8));
        m_jbtnNewSubgroup.setRequestFocusEnabled(false);
        m_jbtnNewSubgroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jbtnNewSubgroupActionPerformed(evt);
            }
        });

        m_jbtnDelSubgroup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/btnredminus.png"))); // NOI18N
        m_jbtnDelSubgroup.setFocusPainted(false);
        m_jbtnDelSubgroup.setFocusable(false);
        m_jbtnDelSubgroup.setMargin(new java.awt.Insets(2, 8, 2, 8));
        m_jbtnDelSubgroup.setRequestFocusEnabled(false);
        m_jbtnDelSubgroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jbtnDelSubgroupActionPerformed(evt);
            }
        });

        m_jbtnSaveSubgroup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/btngreenplus.png"))); // NOI18N
        m_jbtnSaveSubgroup.setFocusPainted(false);
        m_jbtnSaveSubgroup.setFocusable(false);
        m_jbtnSaveSubgroup.setMargin(new java.awt.Insets(2, 8, 2, 8));
        m_jbtnSaveSubgroup.setRequestFocusEnabled(false);
        m_jbtnSaveSubgroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jbtnSaveSubgroupActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jPanel5Layout.createSequentialGroup()
                        .add(m_jbtnSaveSubgroup)
                        .add(1, 1, 1)
                        .add(m_jbtnDelSubgroup))
                    .add(jPanel5Layout.createSequentialGroup()
                        .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(4, 4, 4)
                        .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(m_jLittleImageEditor, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(m_jName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 113, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(m_jbtnNewSubgroup, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 31, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel5Layout.createSequentialGroup()
                        .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel3)
                            .add(m_jName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel8)
                            .add(m_jLittleImageEditor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(m_jbtnNewSubgroup, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 26, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_jbtnSaveSubgroup, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, Short.MAX_VALUE)
                    .add(m_jbtnDelSubgroup, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, Short.MAX_VALUE))
                .add(8, 8, 8))
        );

        jPanel4.add(jPanel5, java.awt.BorderLayout.LINE_START);

        m_jAddProducts.setMinimumSize(new java.awt.Dimension(100, 0));
        m_jAddProducts.setPreferredSize(new java.awt.Dimension(252, 0));

        jLabel5.setText(AppLocal.getIntString("label.prodcategory")); // NOI18N

        m_jCategory.setMaximumSize(new java.awt.Dimension(25, 19));

        jLabel6.setText(AppLocal.getIntString("label.stockproduct")); // NOI18N

        m_jProduct.setMaximumSize(new java.awt.Dimension(25, 19));
        m_jProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jProductActionPerformed(evt);
            }
        });

        m_jbtnAddProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/btngreenplus.png"))); // NOI18N
        m_jbtnAddProduct.setFocusPainted(false);
        m_jbtnAddProduct.setFocusable(false);
        m_jbtnAddProduct.setMargin(new java.awt.Insets(2, 8, 2, 8));
        m_jbtnAddProduct.setRequestFocusEnabled(false);
        m_jbtnAddProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jbtnAddProductActionPerformed(evt);
            }
        });

        m_jbtnDelProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/btnredminus.png"))); // NOI18N
        m_jbtnDelProduct.setFocusPainted(false);
        m_jbtnDelProduct.setFocusable(false);
        m_jbtnDelProduct.setMargin(new java.awt.Insets(2, 8, 2, 8));
        m_jbtnDelProduct.setRequestFocusEnabled(false);
        m_jbtnDelProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jbtnDelProductActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout m_jAddProductsLayout = new org.jdesktop.layout.GroupLayout(m_jAddProducts);
        m_jAddProducts.setLayout(m_jAddProductsLayout);
        m_jAddProductsLayout.setHorizontalGroup(
            m_jAddProductsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(m_jAddProductsLayout.createSequentialGroup()
                .add(m_jAddProductsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, m_jAddProductsLayout.createSequentialGroup()
                        .add(m_jAddProductsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(jLabel6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
                            .add(jLabel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(m_jAddProductsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(m_jCategory, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(m_jProduct, 0, 206, Short.MAX_VALUE)))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, m_jAddProductsLayout.createSequentialGroup()
                        .addContainerGap(190, Short.MAX_VALUE)
                        .add(m_jbtnAddProduct)
                        .add(1, 1, 1)
                        .add(m_jbtnDelProduct, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 43, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        m_jAddProductsLayout.setVerticalGroup(
            m_jAddProductsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(m_jAddProductsLayout.createSequentialGroup()
                .addContainerGap()
                .add(m_jAddProductsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel5)
                    .add(m_jCategory, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(m_jAddProductsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel6)
                    .add(m_jProduct, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(m_jAddProductsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(m_jbtnAddProduct, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, Short.MAX_VALUE)
                    .add(m_jbtnDelProduct, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(7, 7, 7))
        );

        jPanel4.add(m_jAddProducts, java.awt.BorderLayout.CENTER);

        add(jPanel4, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    /*
     * Boton bajar en la lista de subgrupos
     */
    private void m_jDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jDownActionPerformed

        int i = m_jListSubgroups.getSelectionModel().getMaxSelectionIndex();
        if (i < 0){
            i =  0; // No hay ninguna seleccionada
        } else {
            i ++;
            if (i >= m_jListSubgroups.getModel().getSize() ) {
                i = m_jListSubgroups.getModel().getSize() - 1;
            }
        }

        if ((i >= 0) && (i < m_jListSubgroups.getModel().getSize())) {
            // Solo seleccionamos si podemos.
            m_jListSubgroups.getSelectionModel().setSelectionInterval(i, i);
        }        
        
    }//GEN-LAST:event_m_jDownActionPerformed

    /*
     * Boton subir en la lista de subgrupos
     */
    private void m_jUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jUpActionPerformed

        int i = m_jListSubgroups.getSelectionModel().getMinSelectionIndex();
        if (i < 0){
            i = m_jListSubgroups.getModel().getSize() - 1; // No hay ninguna seleccionada
        } else {
            i --;
            if (i < 0) {
                i = 0;
            }
        }

        if ((i >= 0) && (i < m_jListSubgroups.getModel().getSize())) {
            // Solo seleccionamos si podemos.
            m_jListSubgroups.getSelectionModel().setSelectionInterval(i, i);
        }        
        
        
    }//GEN-LAST:event_m_jUpActionPerformed

    /*
     * Se activa cuando se selecciona un subproducto distinto
     */
    private void m_jListSubgroupsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_m_jListSubgroupsValueChanged
        if (!evt.getValueIsAdjusting()) {
            SubgroupInfo s = (SubgroupInfo) m_jListSubgroups.getSelectedValue();
            if (s != null) {
                selectSubgroupPanel(s);
                m_selSubgr = s;
//                m_selProd = null;
                m_jName.setText(s.getName());
                m_jLittleImageEditor.setImage(s.getImage());
            }
        }
        
}//GEN-LAST:event_m_jListSubgroupsValueChanged

    private void m_jProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jProductActionPerformed
//
    }//GEN-LAST:event_m_jProductActionPerformed

    /*
     * Boton nuevo subgrupo. 
     * Establece a null el subgrupo seleccionado y limpia las cajas de texto.
     */
    private void m_jbtnNewSubgroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jbtnNewSubgroupActionPerformed
        m_selSubgr = null;
        m_jName.setText("");
        m_jLittleImageEditor.setImage(null);
}//GEN-LAST:event_m_jbtnNewSubgroupActionPerformed

    /*
     * Boton eliminar un subgrupo
     */
    private void m_jbtnDelSubgroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jbtnDelSubgroupActionPerformed
        if (m_selSubgr != null) {
            //Eliminamos el subgrupo seleccionado
            //m_subgroupsset.remove((SubgroupInfo) m_jListSubgroups.getSelectedValue());
            m_subgroupsset.remove(m_selSubgr);
            m_productsset.remove(m_selSubgr.getID());
            ((SubgroupsListModel)m_jListSubgroups.getModel()).del(m_selSubgr);
            
            m_dirty.setDirty(true);
            m_selSubgr = null;
            
            //Actualizamos el JList
            if (m_subgroupsset.size() > 0) {
                m_jListSubgroups.setSelectedIndex(0);
                
                SubgroupInfo s = (SubgroupInfo) m_jListSubgroups.getSelectedValue();
                if (s != null) {
                    selectSubgroupPanel(s);
                    m_selSubgr = s;
                    m_jName.setText(s.getName());
                    m_jLittleImageEditor.setImage(s.getImage());
                }
            } else 
                setComponentEnabled(false);
            
            m_jCurrTab = null;
            m_jListSubgroups.updateUI();
        } else {
            MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.cannotdeletesubgroup"));
            msg.show(this);
        }
}//GEN-LAST:event_m_jbtnDelSubgroupActionPerformed

    /*
     * Boton salvar un subgrupo (añadir o modificar)
     */
    private void m_jbtnSaveSubgroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jbtnSaveSubgroupActionPerformed
        SubgroupInfo s = new SubgroupInfo();
        String name = m_jName.getText().trim();
        String id = m_selSubgr == null ? UUID.randomUUID().toString() : m_selSubgr.getID();
        
        if (!(name.length()==0)) {
            s.setID(id);
            s.setName(name);
            s.setImage(m_jLittleImageEditor.getImage());
            
            if (m_selSubgr != null) { //Si es una modificacion
                //Eliminamos el subgrupo antiguo de la lista y añadimos el editado
                m_subgroupsset.remove((SubgroupInfo) m_jListSubgroups.getSelectedValue()); 
                m_subgroupsset.add(s);
                
                //Eliminamos el elemento del JList
                ((SubgroupsListModel)m_jListSubgroups.getModel()).del(m_selSubgr);
            }
            
            m_selSubgr = s;
            ((SubgroupsListModel)m_jListSubgroups.getModel()).add(s);
            
            setComponentEnabled(true);
            selectSubgroupPanel(s);
            m_jListSubgroups.setSelectedValue(s, true);
            m_jListSubgroups.updateUI();
            m_dirty.setDirty(true);
        } else {
            MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.cannotsavesubgroup"));
            msg.show(this);
        }
}//GEN-LAST:event_m_jbtnSaveSubgroupActionPerformed

    /*
     * Boton añadir producto a un subgrupo
     */
    private void m_jbtnAddProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jbtnAddProductActionPerformed
        if (m_selProd != null && m_selSubgr != null) {
            //Actualizamos el hash de subgrupos-productos
            //String sid = ((SubgroupInfo) m_jListSubgroups.getSelectedValue()).getID();
            String sid = m_selSubgr.getID();
            Set<ProductInfoExt> p = m_productsset.get(sid);
            if (p == null) {
                p = new HashSet();
                p.add (m_selProd);
                m_productsset.put(sid, p);
            } else
                p.add (m_selProd);
            
            m_jCurrTab = m_tabset.get(sid);
            m_jCurrTab.addButton(new ImageIcon(tnbbutton.getThumbNailText(m_selProd.getImage(), getProductLabel(m_selProd))), m_selProd.getID(), new SelectedAction(m_selProd));
            m_jCurrTab.updateUI();
            m_dirty.setDirty(true);
        } else {
            MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.cannotaddproduct"));
            msg.show(this);
        }
}//GEN-LAST:event_m_jbtnAddProductActionPerformed

    /*
     * Boton eliminar producto de un subgrupo
     */
    private void m_jbtnDelProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jbtnDelProductActionPerformed
        if (m_selProd != null) {
            //Actualizamos el hash de subgrupos-productos
            String sid = ((SubgroupInfo) m_jListSubgroups.getSelectedValue()).getID();
            Set<ProductInfoExt> p = m_productsset.get(sid);
            if (p != null) p.remove (m_selProd);
            
            m_jCurrTab = m_tabset.get(sid);
            m_jCurrTab.delButton(m_selProd.getID());
            m_jCurrTab.updateUI();
            m_dirty.setDirty(true);
        } else {
            MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.cannotdeleteproduct"));
            msg.show(this);
        }
}//GEN-LAST:event_m_jbtnDelProductActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel m_jAddProducts;
    private javax.swing.JComboBox m_jCategory;
    private javax.swing.JButton m_jDown;
    private javax.swing.JList m_jListSubgroups;
    private com.openbravo.data.gui.JLittleImageEditor m_jLittleImageEditor;
    private javax.swing.JTextField m_jName;
    private javax.swing.JComboBox m_jProduct;
    private javax.swing.JPanel m_jProducts;
    private javax.swing.JPanel m_jRootSubgroups;
    private javax.swing.JPanel m_jSubgroups;
    private javax.swing.JButton m_jUp;
    private javax.swing.JButton m_jbtnAddProduct;
    private javax.swing.JButton m_jbtnDelProduct;
    private javax.swing.JButton m_jbtnDelSubgroup;
    private javax.swing.JButton m_jbtnNewSubgroup;
    private javax.swing.JButton m_jbtnSaveSubgroup;
    private javax.swing.JScrollPane m_jscrollsubgr;
    // End of variables declaration//GEN-END:variables
    
}
