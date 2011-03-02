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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;
import java.util.List;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.gui.JListNavigator;
import com.openbravo.data.gui.JMessageDialog;
import com.openbravo.data.gui.ListCellRendererBasic;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.loader.TableDefinition;
import com.openbravo.data.user.BrowsableEditableData;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.ticket.CategoryInfo;
import com.openbravo.pos.ticket.ProductInfoExt;
import com.openbravo.pos.ticket.TaxInfo;

/**
 *
 * @author  Luis Ig. Bacas Riveiro	lbacas@opensistemas.com
 * @author  Pablo J. Urbano Santos	purbano@opensistemas.com
 */
public class TariffAreasEditor extends JPanel implements EditorRecord {
    private DirtyManager m_dirty;
    private DataLogicSales m_dlSales;
    private TableDefinition ttariffareas;
    
    private ComboBoxValModel m_CategoryModel;
    private ComboBoxValModel m_ProductModel;
    private List m_catlist;
    private List<ProductInfoExt> m_prodlist;
    
    private boolean m_bPriceSellLock = false;
    private boolean m_bMarginLock = false;
    private boolean m_bPriceSellTaxLock = false;
    private boolean m_bMarginWriteLock = false;
    private boolean m_bPriceSellTaxWriteLock = false;
    
    private ProductInfoExt m_selProd = null;
    private Object m_oId;
    private Double m_dPriceSell;
    
    /** Creates new form DiscountEditor */
    public TariffAreasEditor(DataLogicSales dlSales, DirtyManager dirty) {
        m_dlSales = dlSales;
        m_dirty = dirty;
        ttariffareas = dlSales.getTableTariffAreas();
        
        initComponents();
        
        // Rellenamos el JComboBox con las categorias disponibles
        m_CategoryModel = new ComboBoxValModel();
        m_ProductModel = new ComboBoxValModel();
        m_jCategory.addActionListener(new SelectedCategory());
        m_jProduct.addActionListener(new SelectedProduct());
        fillCategoriesBox();
        
        m_jPriceSell.getDocument().addDocumentListener(new PriceManager());
        m_jPriceSellTax.getDocument().addDocumentListener(new PriceTaxManager());
        m_jmargin.getDocument().addDocumentListener(new MarginManager());
        
        m_jName.getDocument().addDocumentListener(m_dirty);
        m_jOrder.getDocument().addDocumentListener(m_dirty);
        m_jPriceSell.getDocument().addDocumentListener(m_dirty);
        m_jPriceSellTax.getDocument().addDocumentListener(m_dirty);
        
        writeValueEOF();
    }
    
    public TableDefinition getTableDefinition() {
        return ttariffareas;
    }
    
    public void activate(BrowsableEditableData bd) throws BasicException {
        // la lista superior
        ListCellRenderer cr = new ListCellRendererBasic(ttariffareas.getRenderStringBasic(new int[]{1}));
        if (cr != null) {
            JListNavigator nl = new JListNavigator(bd);
            nl.setCellRenderer(cr);
            m_jContEditor.add(nl, java.awt.BorderLayout.WEST);
        }
    }
    
    public void writeValueEOF() {
        m_oId = null;
        m_jOrder.setText("0");
        m_jName.setText(null);
        m_jOrder.setText(null);
        m_prodlist = null;
        fillProductsList();
        
        enableComponents(false);
    }
    
    public void writeValueInsert() {
        m_oId = null;
        m_jOrder.setText("0");
        m_jName.setText(null);
        m_jOrder.setText(null);
        m_prodlist = null;
        fillProductsList();
        // Ponemos el precio real del producto y no el del que hubiese seleccionado
        //en el anterior grupo de tarifas
        selectProductBox((ProductInfoExt)m_jProduct.getSelectedItem());
        
        enableComponents(true);
    }
    
    public void writeValueDelete(Object value) {
        Object[] tariff = (Object[]) value;
        
        m_oId = tariff[0];
        m_jName.setText(Formats.STRING.formatValue(tariff[1]));
        m_jOrder.setText(Formats.INT.formatValue((Integer)tariff[2]) );
        
        enableComponents(false);
    }    
    
    public void writeValueEdit(Object value) {
        Object[] tariff = (Object[]) value;
        
        m_oId = tariff[0];
        m_jName.setText(Formats.STRING.formatValue(tariff[1]));
        m_jOrder.setText(Formats.INT.formatValue((Integer)tariff[2]) );
        fillProductsList();
        // Ponemos el precio real del producto y no el del que hubiese seleccionado
        //en el anterior grupo de tarifas
        selectProductBox((ProductInfoExt)m_jProduct.getSelectedItem());
        
        enableComponents (true);
        m_dirty.setDirty(false);
    }

    public Object createValue() throws BasicException {
        Object[] tariff = new Object[m_prodlist.size()*2 + 4];
        int i = 4;

        tariff[0] = m_oId == null ? UUID.randomUUID().toString() : m_oId;
        tariff[1] = m_jName.getText();
        
        if (Formats.INT.parseValue(m_jOrder.getText()) != null)
            tariff[2] = Formats.INT.parseValue(m_jOrder.getText());
        else tariff[2] = Formats.INT.parseValue("0");
        tariff[3] = m_prodlist.size();
        
        for (ProductInfoExt p : m_prodlist) {
            tariff[i] = p.getID();
            tariff[i+1] = p.getPriceSell();
            i+= 2;
        }
        return tariff;
    }    
     
    public Component getComponent() {
        return this;
    }
    
    
    private void enableComponents (boolean bValue) {
        m_jName.setEnabled(bValue);
        m_jOrder.setEnabled(bValue);
        m_jCategory.setEnabled(bValue);
        m_jProduct.setEnabled(bValue);
        m_jProductsList.setEnabled(bValue);
        m_jPriceSell.setEnabled(bValue);
        m_jPriceSellTax.setEnabled(bValue);
    }

    private void fireSelectedProduct(ProductInfoExt prod) {
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
    
    
    private void fillProductsList () {
        String tid = (m_oId != null) ? m_oId.toString() : "";
        
        //Leemos y añadimos al JList los productos del grupo de tarificación
        m_jProductsList.removeAll();
        try {
            m_prodlist = m_dlSales.getTariffProds(tid);
        } catch (BasicException e) {
            m_prodlist.clear();
        }
        
        m_ProductModel.refresh(m_prodlist);
        m_jProductsList.setModel(m_ProductModel);
        if (m_prodlist.size() > 0) {
            m_jProductsList.setSelectedIndex(0);
            selectProductBox(m_prodlist.get(0));
        }
        m_jProductsList.updateUI();
    }
    
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
    
    protected void fillProductsBox () {
        java.util.List<ProductInfoExt> products;
        String catid = (String)m_CategoryModel.getSelectedKey();

        try {
            // Obtenemos la lista de productos de la BBDD
            products = m_dlSales.getProductCatalog(catid);
            if (products.size() > 0)
                m_selProd = products.get(0);
            
            // Añadimos la lista de productos al ComboBox
            m_jProduct.setModel(new ComboBoxValModel(products));
        } catch (BasicException e) {
            JMessageDialog.showMessage(this, new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.notactive"), e));            
        }
        
        //Si hay elementos en el ComboBox, seleccionamos el primero
        if (m_jProduct.getItemCount() > 0)
            m_jProduct.setSelectedIndex(0);
    }
    
    protected void selectProductBox(ProductInfoExt p) {
        m_selProd = p;
        try {
            m_jPriceSell.setText( Formats.CURRENCY.formatValue(m_selProd.getPriceSell()) );
        } catch (NullPointerException e) {
            m_jPriceSell.setText("");
        }
    }
    

    
    
    private class SelectedCategory implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            fillProductsBox();
        }
    }
    
    private class SelectedProduct implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            selectProductBox((ProductInfoExt)m_jProduct.getSelectedItem());
        }
    }
    
    private class PriceManager implements DocumentListener  {
        public void changedUpdate(DocumentEvent e) {
            if (!m_bPriceSellLock) { // veo si puedo tocar m_jPriceSell
                m_dPriceSell = readCurrency(m_jPriceSell.getText());
                writeReport();
            }
        }
        public void insertUpdate(DocumentEvent e) {
            if (!m_bPriceSellLock) { // veo si puedo tocar m_jPriceSell
                m_dPriceSell = readCurrency(m_jPriceSell.getText());
                writeReport();
            }
        }    
        public void removeUpdate(DocumentEvent e) {
            if (!m_bPriceSellLock) { // veo si puedo tocar m_jPriceSell
                m_dPriceSell = readCurrency(m_jPriceSell.getText());
                writeReport();
            }
        }         
    }
    
    private class PriceTaxManager implements DocumentListener {
        public void changedUpdate(DocumentEvent e) {
            if (!m_bPriceSellTaxLock) {
                writePriceTax();
            }
        }
        public void insertUpdate(DocumentEvent e) {
            if (!m_bPriceSellTaxLock) {
                writePriceTax();
            }
        }    
        public void removeUpdate(DocumentEvent e) {
            if (!m_bPriceSellTaxLock) {
                writePriceTax();
            }
        }         
    }
    
    
    private void writeReport() {
        Double dPriceBuy = m_selProd.getPriceBuy();
        TaxInfo tax = m_selProd.getTaxInfo();
        double dTaxRate = (tax == null) ? 0.0 : tax.getRate();  
        writeReport(dPriceBuy, dTaxRate);
    }

    private void writeReport(Double dPriceBuy, double dTaxRate) {
        if (!m_bPriceSellTaxWriteLock) { 
            m_bPriceSellTaxLock = true;
            if (m_dPriceSell == null) {
                m_jPriceSellTax.setText(null);
            } else {
                m_jPriceSellTax.setText(Formats.CURRENCY.formatValue(new Double(m_dPriceSell.doubleValue() * (1.0 + dTaxRate))));
            }      
            m_bPriceSellTaxLock = false;
        }
        if (!m_bMarginWriteLock) {
            m_bMarginLock = true;
            if (dPriceBuy == null || m_dPriceSell == null) {
                m_jmargin.setText(null);
            } else {
                m_jmargin.setText(Formats.PERCENT.formatValue(new Double(m_dPriceSell.doubleValue() / dPriceBuy.doubleValue() - 1.0)));
            }
            m_bMarginLock = false;
        }
    }
    
    private void writePriceTax() {
        
        Double dPriceSellTax = readCurrency(m_jPriceSellTax.getText());  
        TaxInfo tax = m_selProd.getTaxInfo();
        double dTaxRate = (tax == null) ? 0.0 : tax.getRate();  
        if (dPriceSellTax == null) {
            m_dPriceSell = null;
        } else {
            m_dPriceSell = new Double(dPriceSellTax.doubleValue() / (1.0 + dTaxRate));
        }

        m_bPriceSellLock = true;
        m_jPriceSell.setText(Formats.CURRENCY.formatValue(m_dPriceSell));            
        m_bPriceSellLock = false;

        m_bPriceSellTaxWriteLock = true;
        writeReport();
        m_bPriceSellTaxWriteLock = false;
    }
    
    private void writeMargin() {
        Double dPriceBuy = m_selProd.getPriceBuy();
        Double dMargin = readPercent(m_jmargin.getText());  
        if (dMargin == null || dPriceBuy == null) {
            m_dPriceSell = dPriceBuy;
        } else {
            m_dPriceSell = new Double(dPriceBuy.doubleValue() * (1.0 + dMargin.doubleValue()));
        }

        m_bPriceSellLock = true;
        m_jPriceSell.setText(Formats.CURRENCY.formatValue(m_dPriceSell));            
        m_bPriceSellLock = false;

        m_bMarginWriteLock = true;
        writeReport();
        m_bMarginWriteLock = false;
    }
    
    private class MarginManager implements DocumentListener  {
        public void changedUpdate(DocumentEvent e) {
            if (!m_bMarginLock) {
                writeMargin();
            }
        }
        public void insertUpdate(DocumentEvent e) {
            if (!m_bMarginLock) {
                writeMargin();
            }
        }    
        public void removeUpdate(DocumentEvent e) {
            if (!m_bMarginLock) {
                writeMargin();
            }
        }         
    }
  
    
    
    private final static Double readCurrency(String sValue) {
        try {
            return (Double) Formats.CURRENCY.parseValue(sValue);
        } catch (BasicException e) {
            return null;
        }
    }
        
    private final static Double readPercent(String sValue) {
        try {
            return (Double) Formats.PERCENT.parseValue(sValue);
        } catch (BasicException e) {
            return null;
        }
    }

    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        m_jPanelAreas = new javax.swing.JPanel();
        m_jContEditor = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        m_jName = new javax.swing.JTextField();
        m_jOrder = new javax.swing.JTextField();
        m_jPanelProducts = new javax.swing.JPanel();
        m_jAddProducts = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        m_jCategory = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        m_jProduct = new javax.swing.JComboBox();
        m_jbtnAddProduct = new javax.swing.JButton();
        m_jbtnDelProduct = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        m_jmargin = new javax.swing.JTextField();
        m_jPriceSell = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        m_jPriceSellTax = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        m_jProductsList = new javax.swing.JList();

        setLayout(new java.awt.BorderLayout(0, 5));

        m_jPanelAreas.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        m_jContEditor.setLayout(new java.awt.BorderLayout());

        jLabel2.setText(AppLocal.getIntString("Label.Name")); // NOI18N

        jLabel3.setText(AppLocal.getIntString("label.prodorder")); // NOI18N

        org.jdesktop.layout.GroupLayout m_jPanelAreasLayout = new org.jdesktop.layout.GroupLayout(m_jPanelAreas);
        m_jPanelAreas.setLayout(m_jPanelAreasLayout);
        m_jPanelAreasLayout.setHorizontalGroup(
            m_jPanelAreasLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(m_jPanelAreasLayout.createSequentialGroup()
                .add(8, 8, 8)
                .add(m_jContEditor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 210, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(10, 10, 10)
                .add(m_jPanelAreasLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(m_jPanelAreasLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_jName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 200, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jOrder, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );
        m_jPanelAreasLayout.setVerticalGroup(
            m_jPanelAreasLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(m_jPanelAreasLayout.createSequentialGroup()
                .add(m_jPanelAreasLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_jPanelAreasLayout.createSequentialGroup()
                        .add(8, 8, 8)
                        .add(m_jContEditor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 180, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(m_jPanelAreasLayout.createSequentialGroup()
                        .add(18, 18, 18)
                        .add(jLabel2)
                        .add(13, 13, 13)
                        .add(jLabel3))
                    .add(m_jPanelAreasLayout.createSequentialGroup()
                        .add(18, 18, 18)
                        .add(m_jName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(10, 10, 10)
                        .add(m_jOrder, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(m_jPanelAreas, java.awt.BorderLayout.PAGE_START);

        m_jPanelProducts.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        m_jPanelProducts.setLayout(null);

        m_jAddProducts.setMinimumSize(new java.awt.Dimension(100, 0));
        m_jAddProducts.setPreferredSize(new java.awt.Dimension(252, 0));

        jLabel5.setText(AppLocal.getIntString("label.prodcategory")); // NOI18N

        jLabel7.setText(AppLocal.getIntString("label.stockproduct")); // NOI18N

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

        jLabel8.setText(AppLocal.getIntString("label.prodpricesell")); // NOI18N

        m_jmargin.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        m_jmargin.setEnabled(false);

        m_jPriceSell.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel17.setText(AppLocal.getIntString("label.prodpriceselltax")); // NOI18N

        m_jPriceSellTax.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        org.jdesktop.layout.GroupLayout m_jAddProductsLayout = new org.jdesktop.layout.GroupLayout(m_jAddProducts);
        m_jAddProducts.setLayout(m_jAddProductsLayout);
        m_jAddProductsLayout.setHorizontalGroup(
            m_jAddProductsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(m_jAddProductsLayout.createSequentialGroup()
                .add(m_jAddProductsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 83, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 83, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jAddProductsLayout.createSequentialGroup()
                        .add(m_jAddProductsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 150, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 150, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(m_jAddProductsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(m_jAddProductsLayout.createSequentialGroup()
                                .add(75, 75, 75)
                                .add(m_jbtnAddProduct)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(m_jbtnDelProduct))
                            .add(m_jAddProductsLayout.createSequentialGroup()
                                .add(m_jPriceSell, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(10, 10, 10)
                                .add(m_jmargin, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(m_jPriceSellTax, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, m_jAddProductsLayout.createSequentialGroup()
                                .add(m_jAddProductsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                    .add(m_jCategory, 0, 213, Short.MAX_VALUE)
                                    .add(m_jProduct, 0, 213, Short.MAX_VALUE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)))))
                .add(17, 17, 17))
        );
        m_jAddProductsLayout.setVerticalGroup(
            m_jAddProductsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(m_jAddProductsLayout.createSequentialGroup()
                .add(m_jAddProductsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_jAddProductsLayout.createSequentialGroup()
                        .add(18, 18, 18)
                        .add(jLabel5)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel7))
                    .add(m_jAddProductsLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(m_jCategory, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(m_jProduct, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .add(24, 24, 24)
                .add(m_jAddProductsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel8)
                    .add(m_jPriceSell, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jmargin, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(m_jAddProductsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel17)
                    .add(m_jPriceSellTax, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(19, 19, 19)
                .add(m_jAddProductsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(m_jbtnDelProduct, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(m_jbtnAddProduct, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 27, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(16, 16, 16))
        );

        m_jPanelProducts.add(m_jAddProducts);
        m_jAddProducts.setBounds(230, 60, 380, 190);

        m_jProductsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        m_jProductsList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                m_jProductsListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(m_jProductsList);

        m_jPanelProducts.add(jScrollPane1);
        jScrollPane1.setBounds(10, 10, 200, 290);

        add(m_jPanelProducts, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void m_jProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jProductActionPerformed

    }//GEN-LAST:event_m_jProductActionPerformed

    private void m_jbtnAddProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jbtnAddProductActionPerformed
        try {
            if (m_selProd != null) {
                 Double price = (Double) Formats.CURRENCY.parseValue(m_jPriceSell.getText());

                if (m_prodlist.contains(m_selProd)) {
                    m_selProd.setPriceSell(price);
                    ProductInfoExt p = m_prodlist.get(m_prodlist.indexOf(m_selProd) );
                    p.setPriceSell(price);
                }else {
                     m_selProd.setPriceSell(price);
                     m_prodlist.add(m_selProd);
                }

                m_dirty.setDirty(true);
                m_jProductsList.updateUI();
            }
        } catch (BasicException e) {
            JMessageDialog.showMessage(this, new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotaddproduct"), e));
        }
    }//GEN-LAST:event_m_jbtnAddProductActionPerformed

    
    private void m_jbtnDelProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jbtnDelProductActionPerformed
        if (m_selProd != null && m_prodlist.contains(m_selProd) ) {
            m_prodlist.remove(m_selProd.getID());
            m_ProductModel.del(m_selProd);
            m_jProductsList.updateUI();
            m_dirty.setDirty(true);
            
            if (m_prodlist.size() > 0)
                m_jProductsListValueChanged( new javax.swing.event.ListSelectionEvent(this, 0, 0, false) );
        }
    }//GEN-LAST:event_m_jbtnDelProductActionPerformed

    private void m_jProductsListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_m_jProductsListValueChanged
        if (!evt.getValueIsAdjusting()) {
            ProductInfoExt p = (ProductInfoExt) m_jProductsList.getSelectedValue();
            if (p != null) {
                fireSelectedProduct(p);
                selectProductBox(p);
            }
        }
    }//GEN-LAST:event_m_jProductsListValueChanged

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel m_jAddProducts;
    private javax.swing.JComboBox m_jCategory;
    private javax.swing.JPanel m_jContEditor;
    private javax.swing.JTextField m_jName;
    private javax.swing.JTextField m_jOrder;
    private javax.swing.JPanel m_jPanelAreas;
    private javax.swing.JPanel m_jPanelProducts;
    private javax.swing.JTextField m_jPriceSell;
    private javax.swing.JTextField m_jPriceSellTax;
    private javax.swing.JComboBox m_jProduct;
    private javax.swing.JList m_jProductsList;
    private javax.swing.JButton m_jbtnAddProduct;
    private javax.swing.JButton m_jbtnDelProduct;
    private javax.swing.JTextField m_jmargin;
    // End of variables declaration//GEN-END:variables
    
}