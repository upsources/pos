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
import javax.swing.*;
import java.awt.image.*;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.format.Formats;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.pos.forms.DataLogicSales;
import java.util.UUID;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author  Luis Ig. Bacas Riveiro	lbacas@opensistemas.com
 * @author  Pablo J. Urbano Santos	purbano@opensistemas.com
 */
public class MaterialsEditor extends JPanel implements EditorRecord {
    private ComboBoxValModel m_CodetypeModel;
    
    private SentenceList m_sentunit;
    private ComboBoxValModel m_UnitModel;
    
    private Object m_id;
    private Object m_code;
    
    private double dUnitPrice;
    
    /** Creates new form JEditProduct */
    public MaterialsEditor(DataLogicSales dlSales, DirtyManager dirty) {
        initComponents();
        
        m_CodetypeModel = new ComboBoxValModel();
        m_CodetypeModel.add(null);
        m_CodetypeModel.add(CodeType.EAN13);
        m_CodetypeModel.add(CodeType.CODE128);
        m_jCodetype.setModel(m_CodetypeModel);
        m_jCodetype.setVisible(false);
        
        // El modelo de unidades
        m_sentunit = dlSales.getUnitsList();
        m_UnitModel = new ComboBoxValModel();

        m_jName.getDocument().addDocumentListener(dirty);
        m_jScale.addActionListener(dirty);
        m_jPriceBuy.getDocument().addDocumentListener(dirty);
        m_jImage.addPropertyChangeListener("image", dirty);
        m_jstockcost.getDocument().addDocumentListener(dirty);
        m_jstockvolume.getDocument().addDocumentListener(dirty);
        m_jAmount.getDocument().addDocumentListener(dirty);
        m_jUnitPrice.getDocument().addDocumentListener(dirty);
        m_jCboUnit.addActionListener(dirty);
        txtAttributes.getDocument().addDocumentListener(dirty);
        
        m_jPriceBuy.getDocument().addDocumentListener(new PriceManager());
        m_jAmount.getDocument().addDocumentListener(new PriceManager());
        
        m_jUnitPrice.setEditable(false);
        writeValueEOF();
    }
    
    public void activate() throws BasicException {
        m_UnitModel = new ComboBoxValModel(m_sentunit.list());
        m_jCboUnit.setModel(m_UnitModel);
    }
    
    public void writeValueEOF() {
        // Los valores
        m_jTitle.setText(AppLocal.getIntString("label.recordeof"));
        m_id = null;
        m_code = null;
        m_jName.setText(null);
        m_jScale.setSelected(false);
        m_jPriceBuy.setText(null);
        m_jImage.setImage(null);
        m_jstockcost.setText(null);
        m_jstockvolume.setText(null);
        m_UnitModel.setSelectedKey(null);
        m_jAmount.setText(null);
        m_jUnitPrice.setText(null);
        txtAttributes.setText(null);
        dUnitPrice = 0.0;
        
        // Los habilitados
        m_jName.setEnabled(false);
        m_jScale.setEnabled(false);
        m_jPriceBuy.setEnabled(false);
        m_jImage.setEnabled(false);
        m_jstockcost.setEnabled(false);
        m_jstockvolume.setEnabled(false);
        m_jCboUnit.setEnabled(false);
        m_jAmount.setEnabled(false);
        txtAttributes.setEnabled(false);
    }
    
    public void writeValueInsert() {
        // Los valores
        m_jTitle.setText(AppLocal.getIntString("label.recordnew"));
        m_id = null;
        m_code = null;
        m_jName.setText(null);
        m_jScale.setSelected(false);
        m_jPriceBuy.setText(null);
        m_jImage.setImage(null);
        m_jstockcost.setText(null);
        m_jstockvolume.setText(null);
        m_UnitModel.setSelectedKey(null);
        m_jAmount.setText(null);
        m_jUnitPrice.setText(null);
        txtAttributes.setText(null);
        dUnitPrice = 0.0;
        
        // Los habilitados
        m_jName.setEnabled(true);
        m_jScale.setEnabled(true);
        m_jPriceBuy.setEnabled(true);
        m_jImage.setEnabled(true);
        m_jstockcost.setEnabled(true);
        m_jstockvolume.setEnabled(true);
        m_jCboUnit.setEnabled(true);
        m_jAmount.setEnabled(true);
        txtAttributes.setEnabled(true);
   }
    
    public void writeValueDelete(Object value) {
        Object[] myprod = (Object[]) value;
        m_jTitle.setText(Formats.STRING.formatValue(myprod[3]) + " " + AppLocal.getIntString("label.recorddeleted"));
        m_id = myprod[0];
        m_code = myprod[2];
        m_jName.setText(Formats.STRING.formatValue(myprod[3]));
        m_jScale.setSelected(((Boolean)myprod[5]).booleanValue());
        m_jUnitPrice.setText(Formats.CURRENCY.formatValue(myprod[6]));
        m_jImage.setImage((BufferedImage) myprod[10]);
        m_jstockcost.setText(Formats.CURRENCY.formatValue(myprod[11]));
        m_jstockvolume.setText(Formats.DOUBLE.formatValue(myprod[12]));
        m_UnitModel.setSelectedKey(myprod[13]);
        m_jAmount.setText(Formats.DOUBLE.formatValue(myprod[14]));
        m_jPriceBuy.setText(Formats.CURRENCY.formatValue(myprod[15]));
        txtAttributes.setText(Formats.BYTEA.formatValue(myprod[16]));
        txtAttributes.setCaretPosition(0);
        
        // Los habilitados
        m_jName.setEnabled(false);
        m_jScale.setEnabled(false);
        m_jPriceBuy.setEnabled(false);
        m_jImage.setEnabled(false);
        m_jstockcost.setEnabled(false);
        m_jstockvolume.setEnabled(false);
        m_jCboUnit.setEnabled(false);
        m_jAmount.setEnabled(false);
        txtAttributes.setEnabled(false);
    }    
    
    public void writeValueEdit(Object value) {
        Object[] myprod = (Object[]) value;
        m_jTitle.setText(Formats.STRING.formatValue(myprod[3]));
        m_id = myprod[0];
        m_code = myprod[2];
        m_jName.setText(Formats.STRING.formatValue(myprod[3]));
        m_jScale.setSelected(((Boolean)myprod[5]).booleanValue());
        dUnitPrice = (Double)myprod[6];
        m_jUnitPrice.setText(Formats.CURRENCY.formatValue(dUnitPrice));
        m_jImage.setImage((BufferedImage) myprod[10]);
        m_jstockcost.setText(Formats.CURRENCY.formatValue(myprod[11]));
        m_jstockvolume.setText(Formats.DOUBLE.formatValue(myprod[12]));
        m_UnitModel.setSelectedKey(myprod[13]);
        m_jAmount.setText(Formats.DOUBLE.formatValue(myprod[14]));
        m_jPriceBuy.setText(Formats.CURRENCY.formatValue(myprod[15]));
        txtAttributes.setText(Formats.BYTEA.formatValue(myprod[16]));
        txtAttributes.setCaretPosition(0);
        
        // Los habilitados
        m_jName.setEnabled(true);
        m_jScale.setEnabled(true);
        m_jPriceBuy.setEnabled(true);
        m_jImage.setEnabled(true);
        m_jstockcost.setEnabled(true);
        m_jstockvolume.setEnabled(true);
        m_jCboUnit.setEnabled(true);
        m_jAmount.setEnabled(true);
        txtAttributes.setEnabled(true);
    }

    public Object createValue() throws BasicException {
        if (m_UnitModel.getSelectedKey() == null) {
            throw new BasicException(AppLocal.getIntString("message.unitnotselect"));
        }
        
        Object[] myprod = new Object[17];
        myprod[0] = m_id == null ? UUID.randomUUID().toString() : m_id;
        myprod[1] = m_jName.getText(); //Referencia (igual que el nombre)
        myprod[2] = m_code == null ? UUID.randomUUID().toString() : m_code;
        myprod[3] = m_jName.getText();
        myprod[4] = true; //Es auxiliar
        myprod[5] = Boolean.valueOf(m_jScale.isSelected());
        myprod[6] = dUnitPrice;
        myprod[7] = 0.0; //Price sell
        myprod[8] = "-1"; //Categoria BOM 
        myprod[9] = "-1"; //Tax NOTAX
        myprod[10] = m_jImage.getImage();
        myprod[11] = readCurrency(m_jstockcost.getText());
        myprod[12] = Formats.DOUBLE.parseValue(m_jstockvolume.getText());
        myprod[13] = m_UnitModel.getSelectedKey();
        myprod[14] = (Formats.DOUBLE.parseValue(m_jAmount.getText()) != null)
                ? Formats.DOUBLE.parseValue(m_jAmount.getText())
                : 0.0;
        myprod[15] = (readCurrency(m_jPriceBuy.getText()) != null)
                ? readCurrency(m_jPriceBuy.getText())
                : 0.0;
        myprod[16] = Formats.BYTEA.parseValue(txtAttributes.getText());
        return myprod;
    }    
    
    public Component getComponent() {
        return this;
    }
    
    private final static Double readCurrency(String sValue) {
        try {
            return (Double) Formats.CURRENCY.parseValue(sValue);
        } catch (BasicException e) {
            return null;
        }
    }

    protected void calcUnitPrice() {
        double priceBuy;
        
        try {
            priceBuy = readCurrency(m_jPriceBuy.getText());
        } catch (Exception e) {
            priceBuy = 0.0;
        }

        try {
            double amount = (Double)Formats.DOUBLE.parseValue(m_jAmount.getText());
            
            if (amount != 0) dUnitPrice = priceBuy/amount;
            else             dUnitPrice = 0.0;
            
            if (dUnitPrice < 0.01) 
                m_jUnitPrice.setText(Formats.DOUBLE.formatValue(dUnitPrice));
            else m_jUnitPrice.setText(Formats.CURRENCY.formatValue(dUnitPrice));
        } catch (Exception e) {
            m_jUnitPrice.setText(Formats.CURRENCY.formatValue(0.0));
        } 
    }
        
    
    private class PriceManager implements DocumentListener  {
        public void changedUpdate(DocumentEvent e) {
            calcUnitPrice();
        }
        public void insertUpdate(DocumentEvent e) {
            calcUnitPrice();
        }    
        public void removeUpdate(DocumentEvent e) {
            calcUnitPrice();
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        m_jName = new javax.swing.JTextField();
        m_jTitle = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        m_jImage = new com.openbravo.data.gui.JImageEditor();
        jLabel3 = new javax.swing.JLabel();
        m_jPriceBuy = new javax.swing.JTextField();
        m_jCodetype = new javax.swing.JComboBox();
        m_jUnitPrice = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        m_jAmount = new javax.swing.JTextField();
        m_jCboUnit = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        m_jstockcost = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        m_jstockvolume = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        m_jScale = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAttributes = new javax.swing.JTextArea();

        setLayout(null);

        jLabel2.setText(AppLocal.getIntString("label.prodname")); // NOI18N
        add(jLabel2);
        jLabel2.setBounds(20, 50, 70, 17);
        add(m_jName);
        m_jName.setBounds(90, 50, 220, 20);

        m_jTitle.setFont(new java.awt.Font("SansSerif", 3, 18));
        add(m_jTitle);
        m_jTitle.setBounds(10, 10, 320, 30);

        jTabbedPane1.setPreferredSize(new java.awt.Dimension(84, 158));

        jPanel1.setLayout(null);

        m_jImage.setMaxDimensions(new java.awt.Dimension(64, 64));
        jPanel1.add(m_jImage);
        m_jImage.setBounds(340, 20, 200, 180);

        jLabel3.setText(AppLocal.getIntString("label.prodpricebuy")); // NOI18N
        jPanel1.add(jLabel3);
        jLabel3.setBounds(10, 30, 150, 17);

        m_jPriceBuy.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel1.add(m_jPriceBuy);
        m_jPriceBuy.setBounds(160, 30, 80, 20);
        jPanel1.add(m_jCodetype);
        m_jCodetype.setBounds(300, 0, 80, 19);

        m_jUnitPrice.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel1.add(m_jUnitPrice);
        m_jUnitPrice.setBounds(160, 90, 80, 20);

        jLabel4.setText(AppLocal.getIntString("label.unitprice")); // NOI18N
        jPanel1.add(jLabel4);
        jLabel4.setBounds(10, 90, 150, 17);

        m_jAmount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel1.add(m_jAmount);
        m_jAmount.setBounds(160, 60, 50, 20);
        jPanel1.add(m_jCboUnit);
        m_jCboUnit.setBounds(220, 60, 110, 19);

        jLabel12.setText(AppLocal.getIntString("label.prodamount")); // NOI18N
        jPanel1.add(jLabel12);
        jLabel12.setBounds(10, 80, 150, 0);

        jTabbedPane1.addTab(AppLocal.getIntString("label.prodgeneral"), jPanel1); // NOI18N

        jPanel2.setLayout(null);

        jLabel9.setText(AppLocal.getIntString("label.prodstockcost")); // NOI18N
        jPanel2.add(jLabel9);
        jLabel9.setBounds(10, 20, 150, 17);

        m_jstockcost.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel2.add(m_jstockcost);
        m_jstockcost.setBounds(160, 20, 80, 20);

        jLabel10.setText(AppLocal.getIntString("label.prodstockvol")); // NOI18N
        jPanel2.add(jLabel10);
        jLabel10.setBounds(10, 50, 150, 17);

        m_jstockvolume.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel2.add(m_jstockvolume);
        m_jstockvolume.setBounds(160, 50, 80, 20);

        jLabel13.setText(AppLocal.getIntString("label.prodscale")); // NOI18N
        jPanel2.add(jLabel13);
        jLabel13.setBounds(10, 80, 150, 17);
        jPanel2.add(m_jScale);
        m_jScale.setBounds(160, 80, 80, 22);

        jTabbedPane1.addTab(AppLocal.getIntString("label.prodstock"), jPanel2); // NOI18N

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel3.setLayout(new java.awt.BorderLayout());

        txtAttributes.setFont(new java.awt.Font("DialogInput", 0, 12));
        jScrollPane1.setViewportView(txtAttributes);

        jPanel3.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Attributes", jPanel3);

        add(jTabbedPane1);
        jTabbedPane1.setBounds(10, 90, 560, 290);
    }// </editor-fold>//GEN-END:initComponents

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField m_jAmount;
    private javax.swing.JComboBox m_jCboUnit;
    private javax.swing.JComboBox m_jCodetype;
    private com.openbravo.data.gui.JImageEditor m_jImage;
    private javax.swing.JTextField m_jName;
    private javax.swing.JTextField m_jPriceBuy;
    private javax.swing.JCheckBox m_jScale;
    private javax.swing.JLabel m_jTitle;
    private javax.swing.JTextField m_jUnitPrice;
    private javax.swing.JTextField m_jstockcost;
    private javax.swing.JTextField m_jstockvolume;
    private javax.swing.JTextArea txtAttributes;
    // End of variables declaration//GEN-END:variables
    
}
