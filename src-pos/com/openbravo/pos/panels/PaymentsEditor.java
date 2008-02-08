//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007 Openbravo, S.L.
//    http://sourceforge.net/projects/
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

package com.openbravo.pos.panels;
import java.awt.Component;
import java.util.UUID;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.loader.IKeyed;
import com.openbravo.format.Formats;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;

/**
 *
 * @author adrianromero
 */
public class PaymentsEditor extends javax.swing.JPanel implements EditorRecord {
    
    private ComboBoxValModel m_ReasonModel;
    
    private String m_sId;
    private String m_sReceipt;
   
    private AppView m_App;
    
    /** Creates new form JPanelPayments */
    public PaymentsEditor(AppView oApp, DirtyManager dirty) {
        
        m_App = oApp;
        
        initComponents();
       
        m_ReasonModel = new ComboBoxValModel();
        m_ReasonModel.add(new PaymentReasonPositive("cashin", AppLocal.getIntString("transpayment.cashin")));
        m_ReasonModel.add(new PaymentReasonNegative("cashout", AppLocal.getIntString("transpayment.cashout")));              
        m_jreason.setModel(m_ReasonModel);

        m_jreason.addActionListener(dirty);
        m_jtotal.getDocument().addDocumentListener(dirty);

        writeValueEOF();
    }
    
    public void writeValueEOF() {
        m_sId = null;
        m_sReceipt = null;
        setReasonTotal(null, null);
        m_jreason.setEnabled(false);
        m_jtotal.setEnabled(false);
    }  
    
    public void writeValueInsert() {
        m_sId = null;
        m_sReceipt = null;
        setReasonTotal(null, null);
        m_jreason.setEnabled(true);
        m_jtotal.setEnabled(true);
    }
    
    public void writeValueDelete(Object value) {
        Object[] payment = (Object[]) value;
        m_sId = (String) payment[0];
        m_sReceipt = (String) payment[2];
        setReasonTotal(payment[3], payment[4]);
        m_jreason.setEnabled(false);
        m_jtotal.setEnabled(false);
    }
    
    public void writeValueEdit(Object value) {
        Object[] payment = (Object[]) value;
        m_sId = (String) payment[0];
        m_sReceipt = (String) payment[2];
        setReasonTotal(payment[3], payment[4]);
        m_jreason.setEnabled(false);
        m_jtotal.setEnabled(false);
    }
    
    public Object createValue() throws BasicException {
        Object[] payment = new Object[5];
        payment[0] = m_sId == null ? UUID.randomUUID().toString() : m_sId;
        payment[1] = m_App.getActiveCashIndex();
        payment[2] = m_sReceipt == null ? UUID.randomUUID().toString() : m_sReceipt;
        payment[3] = m_ReasonModel.getSelectedKey();
        PaymentReason reason = (PaymentReason) m_ReasonModel.getSelectedItem();
        Double dtotal = (Double) Formats.DOUBLE.parseValue(m_jtotal.getText());
        payment[4] = reason == null ? dtotal : reason.addSignum(dtotal);
        return payment;
    }
    
    public Component getComponent() {
        return this;
    }
    
    private void setReasonTotal(Object reasonfield, Object totalfield) {
        
        m_ReasonModel.setSelectedKey(reasonfield);
        
        PaymentReason reason = (PaymentReason) m_ReasonModel.getSelectedItem();     
        
        Double dtotal;
        if (reason == null) {
            dtotal = (Double) totalfield;
        } else {
            dtotal = reason.positivize((Double) totalfield);
        }

        m_jtotal.setText(Formats.CURRENCY.formatValue(dtotal));  
    }
    
    private static abstract class PaymentReason implements IKeyed {
        private String m_sKey;
        private String m_sText;
        
        public PaymentReason(String key, String text) {
            m_sKey = key;
            m_sText = text;
        }
        public Object getKey() {
            return m_sKey;
        }
        public abstract Double positivize(Double d);
        public abstract Double addSignum(Double d);
        
        public String toString() {
            return m_sText;
        }
    }
    private static class PaymentReasonPositive extends PaymentReason {
        public PaymentReasonPositive(String key, String text) {
            super(key, text);
        }
        public Double positivize(Double d) {
            return d;
        }
        public Double addSignum(Double d) {
            if (d == null) {
                return null;
            } else if (d.doubleValue() < 0.0) {
                return new Double(-d.doubleValue());
            } else {
                return d;
            }
        }
    }
    private static class PaymentReasonNegative extends PaymentReason {
        public PaymentReasonNegative(String key, String text) {
            super(key, text);
        }
        public Double positivize(Double d) {
            return d == null ? null : new Double(-d.doubleValue());
        }
        public Double addSignum(Double d) {
            if (d == null) {
                return null;
            } else if (d.doubleValue() > 0.0) {
                return new Double(-d.doubleValue());
            } else {
                return d;
            }
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        m_jreason = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        m_jtotal = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();

        setLayout(null);

        add(m_jreason);
        m_jreason.setBounds(160, 30, 200, 20);

        jLabel2.setText(AppLocal.getIntString("label.paymentreason"));
        add(jLabel2);
        jLabel2.setBounds(10, 30, 150, 15);

        m_jtotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        add(m_jtotal);
        m_jtotal.setBounds(160, 60, 70, 19);

        jLabel3.setText(AppLocal.getIntString("label.paymenttotal"));
        add(jLabel3);
        jLabel3.setBounds(10, 60, 150, 15);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JComboBox m_jreason;
    private javax.swing.JTextField m_jtotal;
    // End of variables declaration//GEN-END:variables
    
}
