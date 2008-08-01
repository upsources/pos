//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2008 Openbravo, S.L.
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

package com.openbravo.pos.customers;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.util.StringUtils;
import java.awt.Component;
import java.util.List;
import java.util.UUID;
import javax.swing.JOptionPane;

/**
 *
 * @author  adrianromero
 */
public class CustomersView extends javax.swing.JPanel implements EditorRecord {

    private Object m_oId;
    
    private SentenceList m_sentcat;
    private ComboBoxValModel m_CategoryModel;
    
    private DirtyManager m_Dirty;
        
    /** Creates new form CustomersView */
    public CustomersView(AppView app, DirtyManager dirty) {
        
        DataLogicSales dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");
        
        initComponents();
        
        m_sentcat = dlSales.getTaxCustCategoriesList();
        m_CategoryModel = new ComboBoxValModel();
        
        m_Dirty = dirty;
        m_jTaxID.getDocument().addDocumentListener(dirty);
        m_jSearchkey.getDocument().addDocumentListener(dirty);
        m_jName.getDocument().addDocumentListener(dirty);
        m_jCategory.addActionListener(dirty);
        m_jNotes.getDocument().addDocumentListener(dirty);
        txtMaxdebt.getDocument().addDocumentListener(dirty);
        m_jVisible.addActionListener(dirty);
        
        txtFirstName.getDocument().addDocumentListener(dirty);
        txtLastName.getDocument().addDocumentListener(dirty);
        txtEmail.getDocument().addDocumentListener(dirty);
        txtPhone.getDocument().addDocumentListener(dirty);
        txtPhone2.getDocument().addDocumentListener(dirty);
        txtFax.getDocument().addDocumentListener(dirty);
        
        txtAddress.getDocument().addDocumentListener(dirty);
        txtAddress2.getDocument().addDocumentListener(dirty);
        txtPostal.getDocument().addDocumentListener(dirty);
        txtCity.getDocument().addDocumentListener(dirty);
        txtRegion.getDocument().addDocumentListener(dirty);
        txtCountry.getDocument().addDocumentListener(dirty);
        
        writeValueEOF(); 
    }
    
    public void activate() throws BasicException {
        
        List a = m_sentcat.list();
        a.add(0, null); // The null item
        m_CategoryModel = new ComboBoxValModel(a);
        m_jCategory.setModel(m_CategoryModel);
    }
    
    public void writeValueEOF() {
        m_oId = null;
        m_jTaxID.setText(null);
        m_jSearchkey.setText(null);
        m_jName.setText(null);
        m_CategoryModel.setSelectedKey(null);
        m_jNotes.setText(null);
        txtMaxdebt.setText(null);
        txtCurdebt.setText(null);
        txtCurdate.setText(null);
        m_jVisible.setSelected(false);
        jcard.setText(null);
        
        txtFirstName.setText(null);
        txtLastName.setText(null);
        txtEmail.setText(null);
        txtPhone.setText(null);
        txtPhone2.setText(null);
        txtFax.setText(null);
       
        txtAddress.setText(null);
        txtAddress2.setText(null);
        txtPostal.setText(null);
        txtCity.setText(null);
        txtRegion.setText(null);
        txtCountry.setText(null);
        
        m_jTaxID.setEnabled(false);
        m_jSearchkey.setEnabled(false);
        m_jName.setEnabled(false);
        m_jCategory.setEnabled(false);
        m_jNotes.setEnabled(false);
        txtMaxdebt.setEnabled(false);
        txtCurdebt.setEnabled(false);
        txtCurdate.setEnabled(false);
        m_jVisible.setEnabled(false);
        jcard.setEnabled(false);
        
        txtFirstName.setEnabled(false);
        txtLastName.setEnabled(false);
        txtEmail.setEnabled(false);
        txtPhone.setEnabled(false);
        txtPhone2.setEnabled(false);
        txtFax.setEnabled(false);
       
        txtAddress.setEnabled(false);
        txtAddress2.setEnabled(false);
        txtPostal.setEnabled(false);
        txtCity.setEnabled(false);
        txtRegion.setEnabled(false);
        txtCountry.setEnabled(false);
        
        jButton2.setEnabled(false);
        jButton3.setEnabled(false);
    } 
    
    public void writeValueInsert() {
        m_oId = null;
        m_jTaxID.setText(null);
        m_jSearchkey.setText(null);
        m_jName.setText(null);
        m_CategoryModel.setSelectedKey(null);
        m_jNotes.setText(null);
        txtMaxdebt.setText(null);
        txtCurdebt.setText(null);
        txtCurdate.setText(null);        
        m_jVisible.setSelected(true);
        jcard.setText(null);
        
        txtFirstName.setText(null);
        txtLastName.setText(null);
        txtEmail.setText(null);
        txtPhone.setText(null);
        txtPhone2.setText(null);
        txtFax.setText(null);
       
        txtAddress.setText(null);
        txtAddress2.setText(null);
        txtPostal.setText(null);
        txtCity.setText(null);
        txtRegion.setText(null);
        txtCountry.setText(null);
        
        m_jTaxID.setEnabled(true);
        m_jSearchkey.setEnabled(true);
        m_jName.setEnabled(true);
        m_jCategory.setEnabled(true);
        m_jNotes.setEnabled(true);
        txtMaxdebt.setEnabled(true);
        txtCurdebt.setEnabled(true);
        txtCurdate.setEnabled(true);
        m_jVisible.setEnabled(true);
        jcard.setEnabled(true);
               
        txtFirstName.setEnabled(true);
        txtLastName.setEnabled(true);
        txtEmail.setEnabled(true);
        txtPhone.setEnabled(true);
        txtPhone2.setEnabled(true);
        txtFax.setEnabled(true);
       
        txtAddress.setEnabled(true);
        txtAddress2.setEnabled(true);
        txtPostal.setEnabled(true);
        txtCity.setEnabled(true);
        txtRegion.setEnabled(true);
        txtCountry.setEnabled(true);
        
        jButton2.setEnabled(true);
        jButton3.setEnabled(true);
    }

    public void writeValueDelete(Object value) {
        Object[] customer = (Object[]) value;
        m_oId = customer[0];
        m_jTaxID.setText((String) customer[1]);
        m_jSearchkey.setText((String) customer[2]);
        m_jName.setText((String) customer[3]);
        m_jNotes.setText((String) customer[4]);
        m_jVisible.setSelected(((Boolean) customer[5]).booleanValue());
        jcard.setText((String) customer[6]);
        txtMaxdebt.setText(Formats.CURRENCY.formatValue(customer[7]));
        txtCurdate.setText(Formats.DATE.formatValue(customer[8]));        
        txtCurdebt.setText(Formats.CURRENCY.formatValue(customer[9]));    
        
        txtFirstName.setText(Formats.STRING.formatValue(customer[10]));
        txtLastName.setText(Formats.STRING.formatValue(customer[11]));
        txtEmail.setText(Formats.STRING.formatValue(customer[12]));
        txtPhone.setText(Formats.STRING.formatValue(customer[13]));
        txtPhone2.setText(Formats.STRING.formatValue(customer[14]));
        txtFax.setText(Formats.STRING.formatValue(customer[15]));
       
        txtAddress.setText(Formats.STRING.formatValue(customer[16]));
        txtAddress2.setText(Formats.STRING.formatValue(customer[17]));
        txtPostal.setText(Formats.STRING.formatValue(customer[18]));
        txtCity.setText(Formats.STRING.formatValue(customer[19]));
        txtRegion.setText(Formats.STRING.formatValue(customer[20]));
        txtCountry.setText(Formats.STRING.formatValue(customer[21]));      
        
        m_CategoryModel.setSelectedKey(customer[22]);
        
        m_jTaxID.setEnabled(false);
        m_jSearchkey.setEnabled(false);
        m_jName.setEnabled(false);
        m_jNotes.setEnabled(false);
        txtMaxdebt.setEnabled(false);
        txtCurdebt.setEnabled(false);
        txtCurdate.setEnabled(false);
        m_jVisible.setEnabled(false);
        jcard.setEnabled(false);       
        
        txtFirstName.setEnabled(false);
        txtLastName.setEnabled(false);
        txtEmail.setEnabled(false);
        txtPhone.setEnabled(false);
        txtPhone2.setEnabled(false);
        txtFax.setEnabled(false);
       
        txtAddress.setEnabled(false);
        txtAddress2.setEnabled(false);
        txtPostal.setEnabled(false);
        txtCity.setEnabled(false);
        txtRegion.setEnabled(false);
        txtCountry.setEnabled(false);
        
        m_jCategory.setEnabled(false);
        
        jButton2.setEnabled(false);
        jButton3.setEnabled(false);
    }

    public void writeValueEdit(Object value) {
        Object[] customer = (Object[]) value;
        m_oId = customer[0];
        m_jTaxID.setText((String) customer[1]);
        m_jSearchkey.setText((String) customer[2]);
        m_jName.setText((String) customer[3]);
        m_jNotes.setText((String) customer[4]);
        m_jVisible.setSelected(((Boolean) customer[5]).booleanValue());
        jcard.setText((String) customer[6]);
        txtMaxdebt.setText(Formats.CURRENCY.formatValue(customer[7]));
        txtCurdate.setText(Formats.DATE.formatValue(customer[8]));        
        txtCurdebt.setText(Formats.CURRENCY.formatValue(customer[9]));    
        
        txtFirstName.setText(Formats.STRING.formatValue(customer[10]));
        txtLastName.setText(Formats.STRING.formatValue(customer[11]));
        txtEmail.setText(Formats.STRING.formatValue(customer[12]));
        txtPhone.setText(Formats.STRING.formatValue(customer[13]));
        txtPhone2.setText(Formats.STRING.formatValue(customer[14]));
        txtFax.setText(Formats.STRING.formatValue(customer[15]));
       
        txtAddress.setText(Formats.STRING.formatValue(customer[16]));
        txtAddress2.setText(Formats.STRING.formatValue(customer[17]));
        txtPostal.setText(Formats.STRING.formatValue(customer[18]));
        txtCity.setText(Formats.STRING.formatValue(customer[19]));
        txtRegion.setText(Formats.STRING.formatValue(customer[20]));
        txtCountry.setText(Formats.STRING.formatValue(customer[21]));   
        
        m_CategoryModel.setSelectedKey(customer[22]);
        
        m_jTaxID.setEnabled(true);
        m_jSearchkey.setEnabled(true);
        m_jName.setEnabled(true);
        m_jNotes.setEnabled(true);
        txtMaxdebt.setEnabled(true);
        txtCurdebt.setEnabled(true);
        txtCurdate.setEnabled(true);
        m_jVisible.setEnabled(true);
        jcard.setEnabled(true);
               
        txtFirstName.setEnabled(true);
        txtLastName.setEnabled(true);
        txtEmail.setEnabled(true);
        txtPhone.setEnabled(true);
        txtPhone2.setEnabled(true);
        txtFax.setEnabled(true);
       
        txtAddress.setEnabled(true);
        txtAddress2.setEnabled(true);
        txtPostal.setEnabled(true);
        txtCity.setEnabled(true);
        txtRegion.setEnabled(true);
        txtCountry.setEnabled(true);
        
        m_jCategory.setEnabled(true);
        
        jButton2.setEnabled(true);
        jButton3.setEnabled(true);
    }
    
    public Object createValue() throws BasicException {
        Object[] customer = new Object[23];
        customer[0] = m_oId == null ? UUID.randomUUID().toString() : m_oId;
        customer[1] = m_jTaxID.getText();
        customer[2] = m_jSearchkey.getText();
        customer[3] = m_jName.getText();
        customer[4] = m_jNotes.getText();
        customer[5] = Boolean.valueOf(m_jVisible.isSelected());
        customer[6] = Formats.STRING.parseValue(jcard.getText()); // Format to manage NULL values
        customer[7] = Formats.CURRENCY.parseValue(txtMaxdebt.getText(), new Double(0.0));
        customer[8] = Formats.TIMESTAMP.parseValue(txtCurdate.getText()); // not saved
        customer[9] = Formats.CURRENCY.parseValue(txtCurdebt.getText()); // not saved
        
        customer[10] = Formats.STRING.parseValue(txtFirstName.getText());
        customer[11] = Formats.STRING.parseValue(txtLastName.getText());
        customer[12] = Formats.STRING.parseValue(txtEmail.getText());
        customer[13] = Formats.STRING.parseValue(txtPhone.getText());
        customer[14] = Formats.STRING.parseValue(txtPhone2.getText());
        customer[15] = Formats.STRING.parseValue(txtFax.getText());
       
        customer[16] = Formats.STRING.parseValue(txtAddress.getText());
        customer[17] = Formats.STRING.parseValue(txtAddress2.getText());
        customer[18] = Formats.STRING.parseValue(txtPostal.getText());
        customer[19] = Formats.STRING.parseValue(txtCity.getText());
        customer[20] = Formats.STRING.parseValue(txtRegion.getText());
        customer[21] = Formats.STRING.parseValue(txtCountry.getText()); 
        
        customer[22] = m_CategoryModel.getSelectedKey();
        
        return customer;
    }   
    
    public Component getComponent() {
        return this;
    }    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        m_jName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        m_jVisible = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        jcard = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtMaxdebt = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtCurdebt = new javax.swing.JTextField();
        txtCurdate = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        m_jTaxID = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        m_jSearchkey = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        txtFax = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtLastName = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtPhone = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtPhone2 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtFirstName = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        txtAddress = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        txtCountry = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        txtAddress2 = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        txtPostal = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        txtCity = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtRegion = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        m_jNotes = new javax.swing.JTextArea();
        jLabel9 = new javax.swing.JLabel();
        m_jCategory = new javax.swing.JComboBox();

        setLayout(null);

        jLabel3.setText(AppLocal.getIntString("label.name")); // NOI18N
        add(jLabel3);
        jLabel3.setBounds(20, 80, 140, 15);
        add(m_jName);
        m_jName.setBounds(200, 80, 270, 19);

        jLabel4.setText(AppLocal.getIntString("label.visible")); // NOI18N
        add(jLabel4);
        jLabel4.setBounds(20, 170, 140, 15);
        add(m_jVisible);
        m_jVisible.setBounds(200, 170, 140, 20);

        jLabel5.setText(AppLocal.getIntString("label.card")); // NOI18N
        add(jLabel5);
        jLabel5.setBounds(20, 110, 140, 15);

        jcard.setEditable(false);
        add(jcard);
        jcard.setBounds(200, 110, 270, 19);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/color_line16.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        add(jButton2);
        jButton2.setBounds(480, 110, 50, 26);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/fileclose.png"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        add(jButton3);
        jButton3.setBounds(540, 110, 50, 26);

        jLabel1.setText(AppLocal.getIntString("label.maxdebt")); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(20, 200, 140, 15);

        txtMaxdebt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        add(txtMaxdebt);
        txtMaxdebt.setBounds(200, 200, 130, 19);

        jLabel2.setText(AppLocal.getIntString("label.curdebt")); // NOI18N
        add(jLabel2);
        jLabel2.setBounds(20, 230, 140, 15);

        txtCurdebt.setEditable(false);
        txtCurdebt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        add(txtCurdebt);
        txtCurdebt.setBounds(200, 230, 130, 19);

        txtCurdate.setEditable(false);
        txtCurdate.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        add(txtCurdate);
        txtCurdate.setBounds(200, 260, 130, 19);

        jLabel6.setText(AppLocal.getIntString("label.curdate")); // NOI18N
        add(jLabel6);
        jLabel6.setBounds(20, 260, 140, 15);
        add(m_jTaxID);
        m_jTaxID.setBounds(200, 20, 270, 19);

        jLabel7.setText(AppLocal.getIntString("label.taxid")); // NOI18N
        add(jLabel7);
        jLabel7.setBounds(20, 20, 140, 15);

        jLabel8.setText(AppLocal.getIntString("label.searchkey")); // NOI18N
        add(jLabel8);
        jLabel8.setBounds(20, 50, 140, 15);
        add(m_jSearchkey);
        m_jSearchkey.setBounds(200, 50, 270, 19);

        jPanel1.setLayout(null);

        jLabel14.setText(AppLocal.getIntString("label.fax")); // NOI18N
        jPanel1.add(jLabel14);
        jLabel14.setBounds(10, 160, 140, 15);
        jPanel1.add(txtFax);
        txtFax.setBounds(150, 160, 270, 19);

        jLabel15.setText(AppLocal.getIntString("label.lastname")); // NOI18N
        jPanel1.add(jLabel15);
        jLabel15.setBounds(10, 40, 140, 15);
        jPanel1.add(txtLastName);
        txtLastName.setBounds(150, 40, 270, 19);

        jLabel16.setText(AppLocal.getIntString("label.email")); // NOI18N
        jPanel1.add(jLabel16);
        jLabel16.setBounds(10, 70, 140, 15);
        jPanel1.add(txtEmail);
        txtEmail.setBounds(150, 70, 270, 19);

        jLabel17.setText(AppLocal.getIntString("label.phone")); // NOI18N
        jPanel1.add(jLabel17);
        jLabel17.setBounds(10, 100, 140, 15);
        jPanel1.add(txtPhone);
        txtPhone.setBounds(150, 100, 270, 19);

        jLabel18.setText(AppLocal.getIntString("label.phone2")); // NOI18N
        jPanel1.add(jLabel18);
        jLabel18.setBounds(10, 130, 140, 15);
        jPanel1.add(txtPhone2);
        txtPhone2.setBounds(150, 130, 270, 19);

        jLabel19.setText(AppLocal.getIntString("label.firstname")); // NOI18N
        jPanel1.add(jLabel19);
        jLabel19.setBounds(10, 10, 140, 15);
        jPanel1.add(txtFirstName);
        txtFirstName.setBounds(150, 10, 270, 19);

        jTabbedPane1.addTab(AppLocal.getIntString("label.contact"), jPanel1); // NOI18N

        jPanel2.setLayout(null);

        jLabel13.setText(AppLocal.getIntString("label.address")); // NOI18N
        jPanel2.add(jLabel13);
        jLabel13.setBounds(10, 10, 140, 15);
        jPanel2.add(txtAddress);
        txtAddress.setBounds(150, 10, 270, 19);

        jLabel20.setText(AppLocal.getIntString("label.country")); // NOI18N
        jPanel2.add(jLabel20);
        jLabel20.setBounds(10, 160, 140, 15);
        jPanel2.add(txtCountry);
        txtCountry.setBounds(150, 160, 270, 19);

        jLabel21.setText(AppLocal.getIntString("label.address2")); // NOI18N
        jPanel2.add(jLabel21);
        jLabel21.setBounds(10, 40, 140, 15);
        jPanel2.add(txtAddress2);
        txtAddress2.setBounds(150, 40, 270, 19);

        jLabel22.setText(AppLocal.getIntString("label.postal")); // NOI18N
        jPanel2.add(jLabel22);
        jLabel22.setBounds(10, 70, 140, 15);
        jPanel2.add(txtPostal);
        txtPostal.setBounds(150, 70, 270, 19);

        jLabel23.setText(AppLocal.getIntString("label.city")); // NOI18N
        jPanel2.add(jLabel23);
        jLabel23.setBounds(10, 100, 140, 15);
        jPanel2.add(txtCity);
        txtCity.setBounds(150, 100, 270, 19);

        jLabel24.setText(AppLocal.getIntString("label.region")); // NOI18N
        jPanel2.add(jLabel24);
        jLabel24.setBounds(10, 130, 140, 15);
        jPanel2.add(txtRegion);
        txtRegion.setBounds(150, 130, 270, 19);

        jTabbedPane1.addTab(AppLocal.getIntString("label.location"), jPanel2); // NOI18N

        jPanel3.setLayout(null);

        jScrollPane1.setViewportView(m_jNotes);

        jPanel3.add(jScrollPane1);
        jScrollPane1.setBounds(10, 10, 550, 180);

        jTabbedPane1.addTab(AppLocal.getIntString("label.notes"), jPanel3); // NOI18N

        add(jTabbedPane1);
        jTabbedPane1.setBounds(10, 300, 580, 230);

        jLabel9.setText(AppLocal.getIntString("label.custtaxcategory")); // NOI18N
        add(jLabel9);
        jLabel9.setBounds(20, 140, 180, 15);
        add(m_jCategory);
        m_jCategory.setBounds(200, 140, 270, 20);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (JOptionPane.showConfirmDialog(this, AppLocal.getIntString("message.cardnew"), AppLocal.getIntString("title.editor"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {            
            jcard.setText("c" + StringUtils.getCardNumber());
            m_Dirty.setDirty(true);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (JOptionPane.showConfirmDialog(this, AppLocal.getIntString("message.cardremove"), AppLocal.getIntString("title.editor"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            jcard.setText(null);
            m_Dirty.setDirty(true);
        }
    }//GEN-LAST:event_jButton3ActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jcard;
    private javax.swing.JComboBox m_jCategory;
    private javax.swing.JTextField m_jName;
    private javax.swing.JTextArea m_jNotes;
    private javax.swing.JTextField m_jSearchkey;
    private javax.swing.JTextField m_jTaxID;
    private javax.swing.JCheckBox m_jVisible;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtAddress2;
    private javax.swing.JTextField txtCity;
    private javax.swing.JTextField txtCountry;
    private javax.swing.JTextField txtCurdate;
    private javax.swing.JTextField txtCurdebt;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtFax;
    private javax.swing.JTextField txtFirstName;
    private javax.swing.JTextField txtLastName;
    private javax.swing.JTextField txtMaxdebt;
    private javax.swing.JTextField txtPhone;
    private javax.swing.JTextField txtPhone2;
    private javax.swing.JTextField txtPostal;
    private javax.swing.JTextField txtRegion;
    // End of variables declaration//GEN-END:variables
    
}
