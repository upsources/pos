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

import com.openbravo.pos.customers.CustomerInfo;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.forms.JPanelView;
import com.openbravo.pos.payment.JPaymentSelect;
import com.openbravo.pos.payment.JPaymentSelectCustomer;
import com.openbravo.pos.payment.PaymentInfo;
import com.openbravo.pos.payment.PaymentInfoTicket;
import com.openbravo.pos.printer.TicketParser;
import com.openbravo.pos.printer.TicketPrinterException;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.ticket.TicketInfo;
import java.util.Date;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

/**
 *
 * @author  adrianromero
 */
public class CustomersPayment extends javax.swing.JPanel implements JPanelView, BeanFactoryApp {

    private AppView app;
    private DataLogicCustomers dlcustomers;
    private DataLogicSales dlsales;
    private DataLogicSystem dlsystem;
    private TicketParser ttp;    
    private JPaymentSelect paymentdialog;
    
    private CustomerInfoExt customerext;
    private DirtyManager dirty;

    /** Creates new form CustomersPayment */
    public CustomersPayment() {

        initComponents();
        
        editorcard.addEditorKeys(m_jKeys);
        txtAddress.addEditorKeys(m_jKeys);
        txtNotes.addEditorKeys(m_jKeys);

        dirty = new DirtyManager();
        txtAddress.addPropertyChangeListener("Text", dirty);
        txtNotes.addPropertyChangeListener("Text", dirty);
    }

    public void init(AppView app) throws BeanFactoryException {

        this.app = app;
        dlcustomers = (DataLogicCustomers) app.getBean("com.openbravo.pos.customers.DataLogicCustomersCreate");
        dlsales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");
        dlsystem = (DataLogicSystem) app.getBean("com.openbravo.pos.forms.DataLogicSystemCreate");
        ttp = new TicketParser(app.getDeviceTicket(), dlsystem);
    }

    public Object getBean() {
        return this;
    }

    public String getTitle() {
        return AppLocal.getIntString("Menu.CustomersPayment");
    }

    public void activate() throws BasicException {

        paymentdialog = JPaymentSelectCustomer.getDialog(this);        
        paymentdialog.init(app);

        resetCustomer();

        editorcard.reset();
        editorcard.activate();
    }

    public boolean deactivate() {
        if (dirty.isDirty()) {
            int res = JOptionPane.showConfirmDialog(this, AppLocal.getIntString("message.wannasave"), AppLocal.getIntString("title.editor"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (res == JOptionPane.YES_OPTION) {
                save();
                return true;
            } else {
                return res == JOptionPane.NO_OPTION;
            }
        } else {
            return true;
        }
    }

    public JComponent getComponent() {
        return this;
    }

    private void editCustomer(CustomerInfoExt customer) {

        customerext = customer;

        txtTaxId.setText(customer.getTaxid());
        txtName.setText(customer.getName());
        txtCard.setText(customer.getCard());
        txtAddress.reset();
        txtAddress.setText(customer.getAddress());
        txtNotes.reset();
        txtNotes.setText(customer.getNotes());
        txtMaxdebt.setText(Formats.CURRENCY.formatValue(customer.getMaxdebt()));
        txtCurdebt.setText(Formats.CURRENCY.formatValue(customer.getCurdebt()));
        txtCurdate.setText(Formats.DATE.formatValue(customer.getCurdate()));
        chkVisible.setSelected(customer.isVisible());

        txtAddress.setEnabled(true);
        txtNotes.setEnabled(true);

        dirty.setDirty(false);

        btnSave.setEnabled(true);    
        btnPay.setEnabled(customer.getCurdebt() != null && customer.getCurdebt().doubleValue() > 0.0);
    }

    private void resetCustomer() {

        customerext = null;

        txtTaxId.setText(null);
        txtName.setText(null);
        txtCard.setText(null);
        txtAddress.reset();
        txtNotes.reset();
        txtMaxdebt.setText(null);
        txtCurdebt.setText(null);
        txtCurdate.setText(null);
        chkVisible.setSelected(false);

        txtAddress.setEnabled(false);
        txtNotes.setEnabled(false);

        dirty.setDirty(false);

        btnSave.setEnabled(false);
        btnPay.setEnabled(false);

    }

    private void readCustomer() {

        try {
            CustomerInfoExt customer = dlcustomers.findCustomerExt(editorcard.getText());
            if (customer == null) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotfindcustomer"));
                msg.show(this);
            } else {
                editCustomer(customer);
            }

        } catch (BasicException ex) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotfindcustomer"), ex);
            msg.show(this);
        }

        editorcard.reset();
        editorcard.activate();
    }

    private void save() {

        customerext.setAddress(txtAddress.getText());
        customerext.setNotes(txtNotes.getText());

        try {
            dlcustomers.updateCustomerExt(customerext);
            editCustomer(customerext);
        } catch (BasicException e) {
            MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.nosave"), e);
            msg.show(this);
        }

    }

    private void printTicket(String resname, TicketInfo ticket, CustomerInfoExt customer) {

        String resource = dlsystem.getResourceAsXML(resname);
        if (resource == null) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"));
            msg.show(this);
        } else {
            try {
                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                script.put("ticket", ticket);
                script.put("customer", customer);
                ttp.printTicket(script.eval(resource).toString());
            } catch (ScriptException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(this);
            } catch (TicketPrinterException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(this);
            }
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel2 = new javax.swing.JPanel();
        btnCustomer = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        btnPay = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        m_jKeys = new com.openbravo.editor.JEditorKeys();
        jPanel5 = new javax.swing.JPanel();
        editorcard = new com.openbravo.editor.JEditorString();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtCard = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtCurdebt = new javax.swing.JTextField();
        txtCurdate = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        txtMaxdebt = new javax.swing.JTextField();
        chkVisible = new javax.swing.JCheckBox();
        txtAddress = new com.openbravo.editor.JEditorString();
        txtNotes = new com.openbravo.editor.JEditorString();
        txtTaxId = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        btnCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/kuser.png"))); // NOI18N
        btnCustomer.setFocusPainted(false);
        btnCustomer.setFocusable(false);
        btnCustomer.setMargin(new java.awt.Insets(8, 14, 8, 14));
        btnCustomer.setRequestFocusEnabled(false);
        btnCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustomerActionPerformed(evt);
            }
        });
        jPanel2.add(btnCustomer);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/filesave.png"))); // NOI18N
        btnSave.setFocusPainted(false);
        btnSave.setFocusable(false);
        btnSave.setMargin(new java.awt.Insets(8, 14, 8, 14));
        btnSave.setRequestFocusEnabled(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        jPanel2.add(btnSave);
        jPanel2.add(jSeparator1);

        btnPay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/greenled.png"))); // NOI18N
        btnPay.setText(AppLocal.getIntString("button.pay")); // NOI18N
        btnPay.setFocusPainted(false);
        btnPay.setFocusable(false);
        btnPay.setMargin(new java.awt.Insets(8, 14, 8, 14));
        btnPay.setRequestFocusEnabled(false);
        btnPay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPayActionPerformed(evt);
            }
        });
        jPanel2.add(btnPay);

        add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.Y_AXIS));

        m_jKeys.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jKeysActionPerformed(evt);
            }
        });
        jPanel4.add(m_jKeys);

        jPanel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel5.setLayout(new java.awt.GridBagLayout());
        jPanel5.add(editorcard, new java.awt.GridBagConstraints());

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/button_ok.png"))); // NOI18N
        jButton1.setFocusPainted(false);
        jButton1.setFocusable(false);
        jButton1.setMargin(new java.awt.Insets(8, 14, 8, 14));
        jButton1.setRequestFocusEnabled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel5.add(jButton1, gridBagConstraints);

        jPanel4.add(jPanel5);

        jPanel3.add(jPanel4, java.awt.BorderLayout.NORTH);

        add(jPanel3, java.awt.BorderLayout.EAST);

        jPanel1.setLayout(null);

        jLabel3.setText(AppLocal.getIntString("label.name")); // NOI18N
        jPanel1.add(jLabel3);
        jLabel3.setBounds(20, 50, 140, 15);

        jLabel12.setText(AppLocal.getIntString("label.notes")); // NOI18N
        jPanel1.add(jLabel12);
        jLabel12.setBounds(20, 200, 140, 15);

        jLabel13.setText(AppLocal.getIntString("label.address")); // NOI18N
        jPanel1.add(jLabel13);
        jLabel13.setBounds(20, 110, 140, 15);

        jLabel4.setText(AppLocal.getIntString("label.visible")); // NOI18N
        jPanel1.add(jLabel4);
        jLabel4.setBounds(20, 380, 140, 15);

        jLabel5.setText(AppLocal.getIntString("label.card")); // NOI18N
        jPanel1.add(jLabel5);
        jLabel5.setBounds(20, 80, 140, 15);

        txtCard.setEditable(false);
        txtCard.setFocusable(false);
        txtCard.setRequestFocusEnabled(false);
        jPanel1.add(txtCard);
        txtCard.setBounds(160, 80, 240, 20);

        jLabel1.setText(AppLocal.getIntString("label.maxdebt")); // NOI18N
        jPanel1.add(jLabel1);
        jLabel1.setBounds(20, 290, 140, 15);

        jLabel2.setText(AppLocal.getIntString("label.curdebt")); // NOI18N
        jPanel1.add(jLabel2);
        jLabel2.setBounds(20, 320, 140, 15);

        txtCurdebt.setEditable(false);
        txtCurdebt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtCurdebt.setFocusable(false);
        txtCurdebt.setRequestFocusEnabled(false);
        jPanel1.add(txtCurdebt);
        txtCurdebt.setBounds(160, 320, 130, 19);

        txtCurdate.setEditable(false);
        txtCurdate.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCurdate.setFocusable(false);
        txtCurdate.setRequestFocusEnabled(false);
        jPanel1.add(txtCurdate);
        txtCurdate.setBounds(160, 350, 130, 19);

        jLabel6.setText(AppLocal.getIntString("label.curdate")); // NOI18N
        jPanel1.add(jLabel6);
        jLabel6.setBounds(20, 350, 140, 15);

        txtName.setEditable(false);
        txtName.setFocusable(false);
        txtName.setRequestFocusEnabled(false);
        jPanel1.add(txtName);
        txtName.setBounds(160, 50, 240, 20);

        txtMaxdebt.setEditable(false);
        txtMaxdebt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtMaxdebt.setFocusable(false);
        txtMaxdebt.setRequestFocusEnabled(false);
        jPanel1.add(txtMaxdebt);
        txtMaxdebt.setBounds(160, 290, 130, 19);

        chkVisible.setEnabled(false);
        chkVisible.setFocusPainted(false);
        chkVisible.setFocusable(false);
        chkVisible.setRequestFocusEnabled(false);
        jPanel1.add(chkVisible);
        chkVisible.setBounds(160, 380, 140, 20);
        jPanel1.add(txtAddress);
        txtAddress.setBounds(160, 110, 270, 80);
        jPanel1.add(txtNotes);
        txtNotes.setBounds(160, 200, 270, 80);

        txtTaxId.setEditable(false);
        txtTaxId.setFocusable(false);
        txtTaxId.setRequestFocusEnabled(false);
        jPanel1.add(txtTaxId);
        txtTaxId.setBounds(160, 20, 240, 20);

        jLabel7.setText(AppLocal.getIntString("label.taxid")); // NOI18N
        jPanel1.add(jLabel7);
        jLabel7.setBounds(20, 20, 140, 15);

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        readCustomer();
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void m_jKeysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jKeysActionPerformed

        readCustomer();
        
    }//GEN-LAST:event_m_jKeysActionPerformed

    private void btnCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomerActionPerformed

        JCustomerFinder finder = JCustomerFinder.getCustomerFinder(this, dlcustomers);
        finder.search(null);
        finder.setVisible(true);
        CustomerInfo customer = finder.getSelectedCustomer();
        if (customer != null) {
            try {
                CustomerInfoExt c = dlcustomers.loadCustomerExt(customer.getId());
                if (c == null) {
                    MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotfindcustomer"));
                    msg.show(this);
                } else {
                    editCustomer(c);
                }
            } catch (BasicException ex) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotfindcustomer"), ex);
                msg.show(this);
            }
            editorcard.reset();
            editorcard.activate();
        }        
                
}//GEN-LAST:event_btnCustomerActionPerformed

    private void btnPayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPayActionPerformed

        paymentdialog.setPrintSelected(true);
        
        if (paymentdialog.showDialog(customerext.getCurdebt(), null)) {

            // Save the ticket
            TicketInfo ticket = new TicketInfo();

            List<PaymentInfo> payments = paymentdialog.getSelectedPayments();

            double total = 0.0;
            for (PaymentInfo p : payments) {
                total += p.getTotal();
            }

            payments.add(new PaymentInfoTicket(-total, "debtpaid"));

            ticket.setPayments(payments);

            ticket.setUser(app.getAppUserView().getUser().getUserInfo());
            ticket.setActiveCash(app.getActiveCashIndex());
            ticket.setDate(new Date());
            ticket.setCustomer(customerext.getCustomerInfo());

            try {
                dlsales.saveTicket(ticket, app.getInventoryLocation());
            } catch (BasicException eData) {
                MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.nosaveticket"), eData);
                msg.show(this);
            }


            // reload customer
            CustomerInfoExt c;
            try {
                c = dlcustomers.loadCustomerExt(customerext.getId());
                if (c == null) {
                    MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotfindcustomer"));
                    msg.show(this);
                } else {
                    editCustomer(c);
                }
            } catch (BasicException ex) {
                c = null;
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotfindcustomer"), ex);
                msg.show(this);
            }

            printTicket(paymentdialog.isPrintSelected()
                    ? "Printer.CustomerPaid"
                    : "Printer.CustomerPaid2",
                    ticket, c);

            editorcard.reset();
            editorcard.activate();
        }        
        
}//GEN-LAST:event_btnPayActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed

        if (dirty.isDirty()) {
            save();

            editorcard.reset();
            editorcard.activate();
        }
        
}//GEN-LAST:event_btnSaveActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCustomer;
    private javax.swing.JButton btnPay;
    private javax.swing.JButton btnSave;
    private javax.swing.JCheckBox chkVisible;
    private com.openbravo.editor.JEditorString editorcard;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JSeparator jSeparator1;
    private com.openbravo.editor.JEditorKeys m_jKeys;
    private com.openbravo.editor.JEditorString txtAddress;
    private javax.swing.JTextField txtCard;
    private javax.swing.JTextField txtCurdate;
    private javax.swing.JTextField txtCurdebt;
    private javax.swing.JTextField txtMaxdebt;
    private javax.swing.JTextField txtName;
    private com.openbravo.editor.JEditorString txtNotes;
    private javax.swing.JTextField txtTaxId;
    // End of variables declaration//GEN-END:variables
}
