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
import java.util.UUID;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.openbravo.pos.forms.AppLocal;
import com.openbravo.format.Formats;
import com.openbravo.basic.BasicException;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.data.user.DirtyManager;

/**
 *
 * @author  Luis Ig. Bacas Riveiro	lbacas@opensistemas.com
 * @author  Pablo J. Urbano Santos	purbano@opensistemas.com
 */
public class DiscountEditor extends JPanel implements EditorRecord {
    
    private Object m_oId;
    private Double m_dQuantity;
    private DirtyManager m_dirty;
    
    private boolean m_bQuantityLock = false;
    
    /** Creates new form DiscountEditor */
    public DiscountEditor(DirtyManager dirty) {
        initComponents();

        m_dirty = dirty;
        m_jName.getDocument().addDocumentListener(dirty);
        m_jQuantity.getDocument().addDocumentListener(dirty);
        m_jPercentage.addActionListener(dirty);
        
        m_jQuantity.getDocument().addDocumentListener(new QuantityManager());
        m_jPercentage.addActionListener(new PercentageManager());
        
        writeValueEOF();
    }
    
    public void writeValueEOF() {
        m_oId = null;
        m_jName.setText(null);
        m_dQuantity = 0.0;
        m_jQuantity.setText(null);
        m_jPercentage.setSelected(false);
        m_bQuantityLock = true;
        
        m_jName.setEnabled(false);
        m_jQuantity.setEnabled(false);
        m_jPercentage.setEnabled(false);
    }
    
    public void writeValueInsert() {
        m_oId = null;
        m_jName.setText(null);
        m_dQuantity = 0.0;
        m_jQuantity.setText(null);
        m_jPercentage.setSelected(false);
        m_bQuantityLock = false;
        
        m_jName.setEnabled(true);
        m_jQuantity.setEnabled(true);
        m_jPercentage.setEnabled(true);
    }
    
    public void writeValueDelete(Object value) {

        Object[] discount = (Object[]) value;
        m_oId = discount[0];
        m_jName.setText(Formats.STRING.formatValue(discount[1]));
        
        m_bQuantityLock = true;
        m_dQuantity = (Double)discount[2];
        m_jPercentage.setSelected( ((Boolean)discount[3]).booleanValue());
        m_jQuantity.setText(m_jPercentage.isSelected()
                ? Formats.PERCENT.formatValue(m_dQuantity)
                : Formats.CURRENCY.formatValue(m_dQuantity));
        
        m_jName.setEnabled(false);
        m_jQuantity.setEnabled(false);
        m_jPercentage.setEnabled(false);
    }    
    
    public void writeValueEdit(Object value) {

        Object[] discount = (Object[]) value;
        m_oId = discount[0];
        m_jName.setText(Formats.STRING.formatValue(discount[1]));
        
        m_bQuantityLock = true;
        m_dQuantity = (Double)discount[2];
        m_jPercentage.setSelected( ((Boolean)discount[3]).booleanValue());
        m_jQuantity.setText(m_jPercentage.isSelected()
                ? Formats.PERCENT.formatValue(m_dQuantity)
                : Formats.CURRENCY.formatValue(m_dQuantity));
        m_bQuantityLock = false;
        
        m_jName.setEnabled(true);
        m_jQuantity.setEnabled(true);
        m_jPercentage.setEnabled(true);
        
        m_dirty.setDirty(false);
    }

    public Object createValue() throws BasicException {
        
        Object[] discount = new Object[5];

        discount[0] = m_oId == null ? UUID.randomUUID().toString() : m_oId;
        discount[1] = m_jName.getText();
        discount[2] = m_dQuantity;
        discount[3] = Boolean.valueOf(m_jPercentage.isSelected());
        
        return discount;
    }    
     
    public Component getComponent() {
        return this;
    }

    
    
    private void updateQuantity() {
        if (!m_bQuantityLock) {
            m_bQuantityLock = true;
            
            boolean selected = m_jPercentage.isSelected();
            m_jQuantity.setText( selected 
                    ? Formats.PERCENT.formatValue( readPercent(m_jQuantity.getText()) )
                    : Formats.CURRENCY.formatValue( readCurrency(m_jQuantity.getText()) )
            );
            m_bQuantityLock = false;
        }
    }

    private void updatePercentage() {
        if (!m_bQuantityLock) {
            m_bQuantityLock = true;
            
            boolean hasSymbol = (m_jQuantity.getText()).endsWith("%");
            

            try {
                m_dQuantity = hasSymbol 
                        ? readPercent(m_jQuantity.getText() )
                        : readCurrency(m_jQuantity.getText() );
                m_jPercentage.setSelected(hasSymbol);
            } catch (Exception e) {
                m_dQuantity = 0.0;
                m_jPercentage.setSelected(false);
            }
            
            m_bQuantityLock = false;
        }       
    }
    
    private class QuantityManager implements DocumentListener {
        public void changedUpdate(DocumentEvent e) {
            updatePercentage();
        }
        public void insertUpdate(DocumentEvent e) {
            updatePercentage();
        }    
        public void removeUpdate(DocumentEvent e) {
            updatePercentage();
        }         
    }
    
    private class PercentageManager implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            updateQuantity();
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

        m_jName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        m_jQuantity = new javax.swing.JTextField();
        m_jPercentage = new javax.swing.JCheckBox();

        setLayout(null);
        add(m_jName);
        m_jName.setBounds(100, 20, 200, 20);

        jLabel2.setText(AppLocal.getIntString("Label.Name")); // NOI18N
        add(jLabel2);
        jLabel2.setBounds(20, 20, 80, 17);

        jLabel3.setText(AppLocal.getIntString("label.quantity")); // NOI18N
        add(jLabel3);
        jLabel3.setBounds(20, 50, 80, 17);
        add(m_jQuantity);
        m_jQuantity.setBounds(100, 50, 60, 20);

        m_jPercentage.setText(AppLocal.getIntString("label.percentage")); // NOI18N
        add(m_jPercentage);
        m_jPercentage.setBounds(170, 50, 97, 22);
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField m_jName;
    private javax.swing.JCheckBox m_jPercentage;
    private javax.swing.JTextField m_jQuantity;
    // End of variables declaration//GEN-END:variables
    
}
