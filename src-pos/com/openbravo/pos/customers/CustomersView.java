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
import com.openbravo.data.user.DirtyManager;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import java.awt.Component;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;
import java.util.UUID;
import javax.swing.JOptionPane;

/**
 *
 * @author  adrianromero
 */
public class CustomersView extends javax.swing.JPanel implements EditorRecord {

    private Object m_oId;
    
    private DirtyManager m_Dirty;
    
    private NumberFormat cardformat = new DecimalFormat("000000");
    private Random cardrandom = new Random();
        
    /** Creates new form CustomersView */
    public CustomersView(AppView app, DirtyManager dirty) {
        
        initComponents();
        
        m_Dirty = dirty;
        m_jName.getDocument().addDocumentListener(dirty);
        m_jAddress.getDocument().addDocumentListener(dirty);
        m_jNotes.getDocument().addDocumentListener(dirty);
        m_jVisible.addActionListener(dirty);
        
        writeValueEOF(); 
    }
    
    public void writeValueEOF() {
        m_oId = null;
        m_jName.setText(null);
        m_jAddress.setText(null);
        m_jNotes.setText(null);
        m_jVisible.setSelected(false);
        jcard.setText(null);
        m_jName.setEnabled(false);
        m_jAddress.setEnabled(false);
        m_jNotes.setEnabled(false);
        m_jVisible.setEnabled(false);
        jcard.setEnabled(false);
        jButton2.setEnabled(false);
        jButton3.setEnabled(false);
    } 
    
    public void writeValueInsert() {
        m_oId = null;
        m_jName.setText(null);
        m_jAddress.setText(null);
        m_jNotes.setText(null);
        m_jVisible.setSelected(true);
        jcard.setText(null);
        m_jName.setEnabled(true);
        m_jAddress.setEnabled(true);
        m_jNotes.setEnabled(true);
        m_jVisible.setEnabled(true);
        jcard.setEnabled(true);
        jButton2.setEnabled(true);
        jButton3.setEnabled(true);
    }

    public void writeValueDelete(Object value) {
        Object[] customer = (Object[]) value;
        m_oId = customer[0];
        m_jName.setText((String) customer[1]);
        m_jAddress.setText((String) customer[2]);
        m_jNotes.setText((String) customer[3]);
        m_jVisible.setSelected(((Boolean) customer[4]).booleanValue());
        jcard.setText((String) customer[5]);
        m_jName.setEnabled(false);
        m_jAddress.setEnabled(false);
        m_jNotes.setEnabled(false);
        m_jVisible.setEnabled(false);
        jcard.setEnabled(false);
        jButton2.setEnabled(false);
        jButton3.setEnabled(false);
    }

    public void writeValueEdit(Object value) {
        Object[] customer = (Object[]) value;
        m_oId = customer[0];
        m_jName.setText((String) customer[1]);
        m_jAddress.setText((String) customer[2]);
        m_jNotes.setText((String) customer[3]);
        m_jVisible.setSelected(((Boolean) customer[4]).booleanValue());
        jcard.setText((String) customer[5]);
        m_jName.setEnabled(true);
        m_jAddress.setEnabled(true);
        m_jNotes.setEnabled(true);
        m_jVisible.setEnabled(true);
        jcard.setEnabled(true);
        jButton2.setEnabled(true);
        jButton3.setEnabled(true);
    }
    
    public Object createValue() throws BasicException {
        Object[] customer = new Object[6];
        customer[0] = m_oId == null ? UUID.randomUUID().toString() : m_oId;
        customer[1] = m_jName.getText();
        customer[2] = m_jAddress.getText();
        customer[3] = m_jNotes.getText();
        customer[4] = Boolean.valueOf(m_jVisible.isSelected());
        customer[5] = jcard.getText();
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
        jLabel12 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        m_jNotes = new javax.swing.JTextArea();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        m_jAddress = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        m_jVisible = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        jcard = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setLayout(null);

        jLabel3.setText(AppLocal.getIntString("label.name")); // NOI18N
        add(jLabel3);
        jLabel3.setBounds(20, 20, 90, 14);
        add(m_jName);
        m_jName.setBounds(110, 20, 270, 18);

        jLabel12.setText(AppLocal.getIntString("label.notes")); // NOI18N
        add(jLabel12);
        jLabel12.setBounds(20, 160, 90, 14);

        jScrollPane1.setViewportView(m_jNotes);

        add(jScrollPane1);
        jScrollPane1.setBounds(110, 160, 270, 70);

        jLabel13.setText(AppLocal.getIntString("label.address")); // NOI18N
        add(jLabel13);
        jLabel13.setBounds(20, 80, 90, 14);

        jScrollPane2.setViewportView(m_jAddress);

        add(jScrollPane2);
        jScrollPane2.setBounds(110, 80, 270, 70);

        jLabel4.setText(AppLocal.getIntString("label.visible")); // NOI18N
        add(jLabel4);
        jLabel4.setBounds(20, 240, 90, 14);
        add(m_jVisible);
        m_jVisible.setBounds(110, 240, 140, 20);

        jLabel5.setText(AppLocal.getIntString("label.card")); // NOI18N
        add(jLabel5);
        jLabel5.setBounds(20, 50, 90, 14);

        jcard.setEditable(false);
        add(jcard);
        jcard.setBounds(110, 50, 270, 18);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/color_line16.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        add(jButton2);
        jButton2.setBounds(390, 50, 50, 26);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/fileclose.png"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        add(jButton3);
        jButton3.setBounds(450, 50, 50, 26);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (JOptionPane.showConfirmDialog(this, AppLocal.getIntString("message.cardnew"), AppLocal.getIntString("title.editor"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            StringBuffer b = new StringBuffer();
            b.append('c');
            b.append(cardformat.format(Math.abs(System.currentTimeMillis()) % 1000000L));
            b.append(cardformat.format(Math.abs(cardrandom.nextLong()) % 1000000L));
            
            jcard.setText(b.toString());
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
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jcard;
    private javax.swing.JTextArea m_jAddress;
    private javax.swing.JTextField m_jName;
    private javax.swing.JTextArea m_jNotes;
    private javax.swing.JCheckBox m_jVisible;
    // End of variables declaration//GEN-END:variables
    
}
