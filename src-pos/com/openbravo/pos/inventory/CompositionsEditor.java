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
import java.awt.image.*;
import java.util.UUID;
import java.util.List;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.format.Formats;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.ticket.TaxInfo;
import com.openbravo.pos.ticket.CategoryInfo;
import com.openbravo.pos.inventory.SubgroupsEditor;

/**
 *
 * @author  Luis Ig. Bacas Riveiro	lbacas@opensistemas.com
 * @author  Pablo J. Urbano Santos	purbano@opensistemas.com
 */
public class CompositionsEditor extends JPanel implements EditorRecord {
    private SubgroupsEditor m_subeditor;
    
    private List<CategoryInfo> m_lcat;
    private ComboBoxValModel m_CategoryModel;

    private SentenceList m_senttax;
    private ComboBoxValModel m_TaxModel;  
    
    private ComboBoxValModel m_CodetypeModel;
    
    private boolean m_bPriceSellLock = false;
    private boolean m_bPriceSellTaxLock = false;
    private boolean m_bPriceSellTaxWriteLock = false;
    
    private Object m_id;
    private Double m_dPriceSell;
    
    /** Creates new form JEditComposition */
    public CompositionsEditor(DataLogicSales dlSales, DirtyManager dirty) {
        initComponents();
        
        //Insetamos un SubgroupsEditor para los subgrupos
        m_subeditor = new SubgroupsEditor(dlSales, dirty);
        m_subeditor.addActionListener(new CatalogListener());
        m_jsubgroups.add(m_subeditor.getComponent());
        (m_subeditor.getComponent()).setBounds(10, 8, 540, 270);

        // El modelo de categorias
        try {
            m_lcat = dlSales.getCategoryComposition();
        } catch (BasicException e) {    
            m_lcat = null;
        }
        m_CategoryModel = new ComboBoxValModel();
        
        // El modelo de impuestos
        m_senttax = dlSales.getTaxList();
        m_TaxModel = new ComboBoxValModel();
        
        m_CodetypeModel = new ComboBoxValModel();
        m_CodetypeModel.add(null);
        m_CodetypeModel.add(CodeType.EAN13);
        m_CodetypeModel.add(CodeType.CODE128);
        m_jCodetype.setModel(m_CodetypeModel);
        m_jCodetype.setVisible(false);
               
        m_jRef.getDocument().addDocumentListener(dirty);
        m_jCode.getDocument().addDocumentListener(dirty);
        m_jName.getDocument().addDocumentListener(dirty);
        m_jComment.addActionListener(dirty);
        m_jScale.addActionListener(dirty);
        m_jCategory.addActionListener(dirty);
        m_jTax.addActionListener(dirty);
        m_jPriceSell.getDocument().addDocumentListener(dirty);
        m_jImage.addPropertyChangeListener("image", dirty);
        m_jInCatalog.addActionListener(dirty);
        m_jCatalogOrder.getDocument().addDocumentListener(dirty);
        txtAttributes.getDocument().addDocumentListener(dirty);

        // el informe de stock
        ReportManager rm = new ReportManager();
        m_jTax.addActionListener(rm);
        
        m_jPriceSell.getDocument().addDocumentListener(new PriceManager());
        m_jPriceSellTax.getDocument().addDocumentListener(new PriceTaxManager());
        
        writeValueEOF();
    }
    
    public void activate() throws BasicException {
        m_subeditor.loadCatalog();
        m_subeditor.setComponentEnabled(true);
        
        m_CategoryModel = new ComboBoxValModel(m_lcat);
        m_CategoryModel.setSelectedFirst();
        m_jCategory.setModel(m_CategoryModel);

        m_TaxModel = new ComboBoxValModel(m_senttax.list());
        m_jTax.setModel(m_TaxModel);
    }
    
    public void writeValueEOF() {
        // Los valores
        m_jTitle.setText(AppLocal.getIntString("label.recordeof"));
        writeValueAux(null);
        
        // Los habilitados
        enableControls(false);
        m_jCatalogOrder.setEnabled(false);
        m_subeditor.setComponentEnabled(false);
        jTabbedPane1.getComponent(2).setEnabled(false);
        jTabbedPane1.setSelectedIndex(0);
        
        writeReport(null, 0.0);
    }
    
    public void writeValueInsert() {
        // Los valores
        m_jTitle.setText(AppLocal.getIntString("label.recordnew"));
        writeValueAux(null);
        
        // Los habilitados
        enableControls(true);
        m_jCatalogOrder.setEnabled(false);
        m_subeditor.setComponentEnabled(false);
        jTabbedPane1.getComponent(2).setEnabled(false);
        jTabbedPane1.setSelectedIndex(0);

        writeReport(null, 0.0);
   }
    public void writeValueDelete(Object value) {
        m_jTitle.setText(Formats.STRING.formatValue(((Object[])value)[1]) + " - " + Formats.STRING.formatValue(((Object[])value)[3]) + " " + AppLocal.getIntString("label.recorddeleted"));
        writeValueAux(value);
        
        // Los habilitados
        enableControls(false);
        m_jCatalogOrder.setEnabled(false);
        m_subeditor.setComponentEnabled(false);
        m_subeditor.setComponentEnabled(false);
        jTabbedPane1.getComponent(2).setEnabled(false);
    }    
    
    public void writeValueEdit(Object value) {
        m_jTitle.setText(Formats.STRING.formatValue(((Object[])value)[1]) + " - " + Formats.STRING.formatValue(((Object[])value)[3]));
        writeValueAux(value);
        
        // Los habilitados
        enableControls(true);
        m_jCatalogOrder.setEnabled(m_jInCatalog.isSelected());   
        m_subeditor.setComponentEnabled(true);
        m_subeditor.setComponentEnabled(true);
        jTabbedPane1.getComponent(2).setEnabled(true);
    }

    public Object createValue() throws BasicException {
        Object[] subgr = (Object[]) m_subeditor.getValues();
        
        Object[] myprod = new Object[14 + subgr.length];
        myprod[0] =  m_id == null ? UUID.randomUUID().toString() : m_id;
        myprod[1] = m_jRef.getText();
        myprod[2] = m_jCode.getText();
        myprod[3] = m_jName.getText();
        myprod[4] = Boolean.valueOf(m_jComment.isSelected());
        myprod[5] = Boolean.valueOf(m_jScale.isSelected());
        myprod[6] = Formats.CURRENCY.parseValue("0");
        myprod[7] = m_dPriceSell; // Formats.CURRENCY.parseValue(m_jPriceSell.getText());
        myprod[8] = "0";
        myprod[9] = m_TaxModel.getSelectedKey();
        myprod[10] = m_jImage.getImage();
        myprod[11] = Boolean.valueOf(m_jInCatalog.isSelected());       
        myprod[12] = Formats.INT.parseValue(m_jCatalogOrder.getText()); 
        myprod[13] = Formats.BYTEA.parseValue(txtAttributes.getText());
        
        //Añadimos los datos de los subgrupos
        for (int i = 0; i < subgr.length; i++) {
            myprod[i+14] = subgr[i];
        }
        
        return myprod;
    }    
    
    public Component getComponent() {
        return this;
    }

    private void enableControls (boolean e) {
        m_jRef.setEnabled(e);
        m_jCode.setEnabled(e);
        m_jName.setEnabled(e);
        m_jComment.setEnabled(e);
        m_jScale.setEnabled(e);
        m_jTax.setEnabled(e);
        m_jPriceSell.setEnabled(e); 
        m_jPriceSellTax.setEnabled(e);
        m_jImage.setEnabled(e);
        m_jInCatalog.setEnabled(e);
        txtAttributes.setEnabled(e);
    }
    
    private void writeValueAux(Object value) {
        if (value != null) {
            Object[] myprod = (Object[]) value;
            m_id = myprod[0];
            m_jRef.setText(Formats.STRING.formatValue(myprod[1]));
            m_jCode.setText(Formats.STRING.formatValue(myprod[2]));
            m_jName.setText(Formats.STRING.formatValue(myprod[3]));
            m_jComment.setSelected(((Boolean)myprod[4]).booleanValue());
            m_jScale.setSelected(((Boolean)myprod[5]).booleanValue());
            m_dPriceSell = (Double) myprod[7];
            m_bPriceSellLock = true;
            m_jPriceSell.setText(Formats.CURRENCY.formatValue(m_dPriceSell));            
            m_bPriceSellLock = false;
            m_CategoryModel.setSelectedKey(myprod[9]);
            m_TaxModel.setSelectedKey(myprod[9]);
            m_jImage.setImage((BufferedImage) myprod[10]);
            m_jInCatalog.setSelected(((Boolean)myprod[11]).booleanValue());
            m_jCatalogOrder.setText(Formats.INT.formatValue(myprod[12]));
            txtAttributes.setText(Formats.BYTEA.formatValue(myprod[13]));
            txtAttributes.setCaretPosition(0);
            m_subeditor.setComposition(m_id);
            
            writeReport();
        } else {
            m_id = null;
            m_jRef.setText(null);
            m_jCode.setText(null);
            m_jName.setText(null);
            m_jComment.setSelected(false);
            m_jScale.setSelected(false);
            m_TaxModel.setSelectedKey(null);
            m_dPriceSell = null;
            m_bPriceSellLock = true;
            m_jPriceSell.setText(null);            
            m_bPriceSellLock = false;
            m_jImage.setImage(null);
            m_jInCatalog.setSelected(true);
            m_jCatalogOrder.setText(null);
            m_subeditor.setComposition(null);
            txtAttributes.setText(null);
        }
    }
    
    private void writeReport() {
        Double dPriceSell = readCurrency(m_jPriceSell.getText());
        TaxInfo tax = (TaxInfo) m_TaxModel.getSelectedItem();
        double dTaxRate = (tax == null) ? 0.0 : tax.getRate();  
        writeReport(dPriceSell, dTaxRate);
    }
    
    private void writePriceTax() {
        
        Double dPriceSellTax = readCurrency(m_jPriceSellTax.getText());  
        TaxInfo tax = (TaxInfo) m_TaxModel.getSelectedItem();
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
    
    // private void writeReport(Double dPriceBuy, double dTaxRate, Double dStockSecurity, Double dStockCost, Double dStockVolume) {
    private void writeReport(Double dPriceSell, double dTaxRate) {
               
        if (!m_bPriceSellTaxWriteLock) { 
            m_bPriceSellTaxLock = true;
            if (dPriceSell == null) {
                m_jPriceSellTax.setText(null);
            } else {
                m_jPriceSellTax.setText(Formats.CURRENCY.formatValue(new Double(dPriceSell.doubleValue() * (1.0 + dTaxRate))));
            }      
            m_bPriceSellTaxLock = false;
        }
    }
    
    private class ReportManager implements DocumentListener, ActionListener {
        public void changedUpdate(DocumentEvent e) {
            writeReport();
        }
        public void insertUpdate(DocumentEvent e) {
            writeReport();
        }    
        public void removeUpdate(DocumentEvent e) {
            writeReport();
        }         
        public void actionPerformed(ActionEvent e) {
            writeReport();
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
    
    private class CatalogListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
//            assignProduct((ProductInfoExt) e.getSource());
        }  
    }   
    
    private final static Double readCurrency(String sValue) {
        try {
            return (Double) Formats.CURRENCY.parseValue(sValue);
        } catch (BasicException e) {
            return null;
        }
    }

    /*
    private final static Double readPercent(String sValue) {
        try {
            return (Double) Formats.PERCENT.parseValue(sValue);
        } catch (BasicException e) {
            return null;
        }
    }

    private static class MyListData extends javax.swing.AbstractListModel {
        
        private java.util.List m_data;
        
        public MyListData(java.util.List data) {
            m_data = data;
        }
        
        public Object getElementAt(int index) {
            return m_data.get(index);
        }
        
        public int getSize() {
            return m_data.size();
        } 
    } 
*/    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        m_jRef = new javax.swing.JTextField();
        m_jName = new javax.swing.JTextField();
        m_jTitle = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        m_jCode = new javax.swing.JTextField();
        m_jImage = new com.openbravo.data.gui.JImageEditor();
        jLabel4 = new javax.swing.JLabel();
        m_jPriceSell = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        m_jCategory = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        m_jTax = new javax.swing.JComboBox();
        m_jPriceSellTax = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        m_jCodetype = new javax.swing.JComboBox();
        jPanel4 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        m_jInCatalog = new javax.swing.JCheckBox();
        m_jComment = new javax.swing.JCheckBox();
        m_jScale = new javax.swing.JCheckBox();
        m_jCatalogOrder = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        m_jsubgroups = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAttributes = new javax.swing.JTextArea();

        setLayout(null);

        jLabel1.setText(AppLocal.getIntString("label.prodref")); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(10, 50, 80, 17);

        jLabel2.setText(AppLocal.getIntString("label.prodname")); // NOI18N
        add(jLabel2);
        jLabel2.setBounds(180, 50, 70, 17);
        add(m_jRef);
        m_jRef.setBounds(90, 50, 70, 20);
        add(m_jName);
        m_jName.setBounds(250, 50, 220, 20);

        m_jTitle.setFont(new java.awt.Font("SansSerif", 3, 18));
        add(m_jTitle);
        m_jTitle.setBounds(10, 10, 320, 30);

        jTabbedPane1.setPreferredSize(new java.awt.Dimension(90, 95));

        jPanel1.setLayout(null);

        jLabel6.setText(AppLocal.getIntString("label.prodbarcode")); // NOI18N
        jPanel1.add(jLabel6);
        jLabel6.setBounds(10, 20, 150, 17);
        jPanel1.add(m_jCode);
        m_jCode.setBounds(160, 20, 170, 20);

        m_jImage.setMaxDimensions(new java.awt.Dimension(64, 64));
        jPanel1.add(m_jImage);
        m_jImage.setBounds(340, 20, 200, 180);

        jLabel4.setText(AppLocal.getIntString("label.prodpricesell")); // NOI18N
        jPanel1.add(jLabel4);
        jLabel4.setBounds(10, 50, 150, 17);

        m_jPriceSell.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel1.add(m_jPriceSell);
        m_jPriceSell.setBounds(160, 50, 80, 20);

        jLabel5.setText(AppLocal.getIntString("label.prodcategory")); // NOI18N
        jPanel1.add(jLabel5);
        jLabel5.setBounds(10, 140, 150, 17);

        m_jCategory.setEnabled(false);
        jPanel1.add(m_jCategory);
        m_jCategory.setBounds(160, 140, 170, 20);

        jLabel7.setText(AppLocal.getIntString("label.prodtax")); // NOI18N
        jPanel1.add(jLabel7);
        jLabel7.setBounds(10, 110, 150, 17);
        jPanel1.add(m_jTax);
        m_jTax.setBounds(160, 110, 170, 20);

        m_jPriceSellTax.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel1.add(m_jPriceSellTax);
        m_jPriceSellTax.setBounds(160, 80, 80, 20);

        jLabel16.setText(AppLocal.getIntString("label.prodpriceselltax")); // NOI18N
        jPanel1.add(jLabel16);
        jLabel16.setBounds(10, 80, 150, 17);
        jPanel1.add(m_jCodetype);
        m_jCodetype.setBounds(300, 0, 80, 19);

        jTabbedPane1.addTab(AppLocal.getIntString("label.prodgeneral"), jPanel1); // NOI18N

        jPanel4.setLayout(null);

        jLabel12.setText(AppLocal.getIntString("label.prodscale")); // NOI18N
        jPanel4.add(jLabel12);
        jLabel12.setBounds(10, 140, 150, 17);

        jLabel11.setText(AppLocal.getIntString("label.prodaux")); // NOI18N
        jPanel4.add(jLabel11);
        jLabel11.setBounds(10, 110, 150, 17);

        jLabel8.setText(AppLocal.getIntString("label.prodincatalog")); // NOI18N
        jPanel4.add(jLabel8);
        jLabel8.setBounds(10, 80, 150, 17);

        m_jInCatalog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jInCatalogActionPerformed(evt);
            }
        });
        jPanel4.add(m_jInCatalog);
        m_jInCatalog.setBounds(160, 80, 50, 22);
        jPanel4.add(m_jComment);
        m_jComment.setBounds(160, 110, 80, 22);
        jPanel4.add(m_jScale);
        m_jScale.setBounds(160, 140, 80, 22);

        m_jCatalogOrder.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel4.add(m_jCatalogOrder);
        m_jCatalogOrder.setBounds(310, 80, 80, 20);

        jLabel18.setText(AppLocal.getIntString("label.prodorder")); // NOI18N
        jPanel4.add(jLabel18);
        jLabel18.setBounds(250, 80, 60, 17);

        jTabbedPane1.addTab(AppLocal.getIntString("label.prodproperties"), jPanel4); // NOI18N

        m_jsubgroups.setLayout(null);
        jTabbedPane1.addTab(AppLocal.getIntString("label.Subgroups"), m_jsubgroups); // NOI18N

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel3.setLayout(new java.awt.BorderLayout());

        txtAttributes.setFont(new java.awt.Font("DialogInput", 0, 12));
        jScrollPane1.setViewportView(txtAttributes);

        jPanel3.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Attributes", jPanel3);

        add(jTabbedPane1);
        jTabbedPane1.setBounds(10, 90, 560, 300);
    }// </editor-fold>//GEN-END:initComponents

    private void m_jInCatalogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jInCatalogActionPerformed
        if (m_jInCatalog.isSelected()) {
            m_jCatalogOrder.setEnabled(true);
        } else {
            m_jCatalogOrder.setEnabled(false);
            m_jCatalogOrder.setText(null);
        }
    }//GEN-LAST:event_m_jInCatalogActionPerformed

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField m_jCatalogOrder;
    private javax.swing.JComboBox m_jCategory;
    private javax.swing.JTextField m_jCode;
    private javax.swing.JComboBox m_jCodetype;
    private javax.swing.JCheckBox m_jComment;
    private com.openbravo.data.gui.JImageEditor m_jImage;
    private javax.swing.JCheckBox m_jInCatalog;
    private javax.swing.JTextField m_jName;
    private javax.swing.JTextField m_jPriceSell;
    private javax.swing.JTextField m_jPriceSellTax;
    private javax.swing.JTextField m_jRef;
    private javax.swing.JCheckBox m_jScale;
    private javax.swing.JComboBox m_jTax;
    private javax.swing.JLabel m_jTitle;
    private javax.swing.JPanel m_jsubgroups;
    private javax.swing.JTextArea txtAttributes;
    // End of variables declaration//GEN-END:variables
    
}
