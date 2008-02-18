//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007 Openbravo, S.L.
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

package com.openbravo.pos.payment;

import com.openbravo.pos.ticket.TicketInfo;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import javax.swing.JFrame;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.format.Formats;

/**
 *
 * @author adrianromero
 */
public class JPaymentSelect extends javax.swing.JDialog 
                            implements JPaymentNotifier {
    
    private TicketInfo m_ticket;    
    private PaymentInfoList m_aPaymentInfo;
    private String m_sresourcename;
    
    private double m_dTotal; // es funcion de m_ticket
    private String m_sTransaction; // es funcion de m_ticket
    
    /** Creates new form JPayTicket */
    private JPaymentSelect(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
    }
    /** Creates new form JPayTicket */
    private JPaymentSelect(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);
    }    

    private String init(AppView app, TicketInfo ticket) {
        
        // Primero inicializo las variables
        m_ticket = ticket;
        m_sresourcename = null;
        m_dTotal = m_ticket.getTotal();
        m_sTransaction = "1234"; // Integer.toString(m_ticket.getId());
        
        m_ticket.resetPayments(); // inicializo el pago
        m_aPaymentInfo = new PaymentInfoList();
        // m_jButtonRemove.setEnabled(false);
        
        // Inicializo los componentes
        initComponents();
        
        getRootPane().setDefaultButton(m_jButtonOK);   

        m_jTotalEuros.setText(Formats.CURRENCY.formatValue(new Double(m_dTotal)));
        
        if (m_dTotal >= 0.0) { 
            // Entrada de dinero
            addTabPayment(new JPaymentCashPos(this), 
                    app.getAppUserView().getUser().hasPermission("payment.cash"),
                    "tab.cash", 
                    "/com/openbravo/images/cash.png");
            addTabPayment(new JPaymentCheque(this), 
                    app.getAppUserView().getUser().hasPermission("payment.cheque"),
                    "tab.cheque", 
                    "/com/openbravo/images/desktop.png");
            addTabPayment(new JPaymentPaper(this, "paperin"), 
                    app.getAppUserView().getUser().hasPermission("payment.paper"),
                    "tab.paper", 
                    "/com/openbravo/images/knotes.png");            
            addTabPayment(new JPaymentTicket(this), 
                    app.getAppUserView().getUser().hasPermission("payment.ticket"),
                    "tab.ticket", 
                    "/com/openbravo/images/kontact.png");
            addTabPayment(new JPaymentMagcard(app, this), 
                    app.getAppUserView().getUser().hasPermission("payment.magcard"),
                    "tab.magcard", 
                    "/com/openbravo/images/vcard.png");
            addTabPayment(new JPaymentFree(this), 
                    app.getAppUserView().getUser().hasPermission("payment.free"),
                    "tab.free", 
                    "/com/openbravo/images/package_toys.png");
        } else { 
            // devoluciones...
            addTabPayment(new JPaymentRefund(this, "cashrefund"), 
                    app.getAppUserView().getUser().hasPermission("refund.cash"),
                    "tab.cashrefund", 
                    "/com/openbravo/images/cash.png");
            addTabPayment(new JPaymentRefund(this, "chequerefund"), 
                    app.getAppUserView().getUser().hasPermission("refund.cheque"),
                    "tab.chequerefund", 
                    "/com/openbravo/images/desktop.png");
            addTabPayment(new JPaymentRefund(this, "paperout"), 
                    app.getAppUserView().getUser().hasPermission("refund.paper"),
                    "tab.paper", 
                    "/com/openbravo/images/knotes.png");            
            addTabPayment(new JPaymentMagcard(app, this), 
                    app.getAppUserView().getUser().hasPermission("refund.magcard"),
                    "tab.magcard", 
                    "/com/openbravo/images/vcard.png");
            
            jPanel6.setVisible(false);
        }
        
        printState();
        setVisible(true);
        
        return m_sresourcename;
    }   
    
    private void addTabPayment(JPaymentInterface tab, boolean haspermission, String sIntString, String sIconRes) {
        if (haspermission) {
            m_jTabPayment.addTab(
                    AppLocal.getIntString(sIntString),
                    new javax.swing.ImageIcon(getClass().getResource(sIconRes)),
                    tab.getComponent());
        }
    }
    
    private void printState() {
        
        m_jRemaininglEuros.setText(Formats.CURRENCY.formatValue(new Double(m_dTotal - m_aPaymentInfo.getTotal())));
        m_jButtonRemove.setEnabled(!m_aPaymentInfo.isEmpty());
        m_jTabPayment.setSelectedIndex(0); // selecciono el primero
        ((JPaymentInterface) m_jTabPayment.getSelectedComponent()).activate(m_sTransaction, m_dTotal - m_aPaymentInfo.getTotal());
    }
    
    private static Window getWindow(Component parent) {
        if (parent == null) {
            return new JFrame();
        } else if (parent instanceof Frame || parent instanceof Dialog) {
            return (Window)parent;
        } else {
            return getWindow(parent.getParent());
        }
    }       
    
    public static String showMessage(Component parent, AppView app, TicketInfo ticket) {
         
        Window window = getWindow(parent);
        
        JPaymentSelect myMsg;
        if (window instanceof Frame) { 
            myMsg = new JPaymentSelect((Frame) window, true);
        } else {
            myMsg = new JPaymentSelect((Dialog) window, true);
        }
        return myMsg.init(app, ticket);
    }  
    
    public void setOKEnabled(boolean bValue) {
        m_jButtonOK.setEnabled(bValue);
    }
    
    public void setAddEnabled(boolean bValue) {
        m_jButtonAdd.setEnabled(bValue);
    }
    
    private void disposeOK(String sresourcename) {
        
        PaymentInfo returnPayment = ((JPaymentInterface) m_jTabPayment.getSelectedComponent()).executePayment();
        if (returnPayment != null) {
            m_aPaymentInfo.add(returnPayment);
            m_ticket.setPayments(m_aPaymentInfo.getPayments());
            m_sresourcename = sresourcename;
            dispose();
        }        
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        jPanel4 = new javax.swing.JPanel();
        m_jLblTotalEuros1 = new javax.swing.JLabel();
        m_jTotalEuros = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        m_jLblRemainingEuros = new javax.swing.JLabel();
        m_jRemaininglEuros = new javax.swing.JLabel();
        m_jButtonAdd = new javax.swing.JButton();
        m_jButtonRemove = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        m_jTabPayment = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        m_jButtonPrint = new javax.swing.JToggleButton();
        jPanel1 = new javax.swing.JPanel();
        m_jButtonOK = new javax.swing.JButton();
        m_jButtonCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(AppLocal.getIntString("payment.title")); // NOI18N
        setResizable(false);

        m_jLblTotalEuros1.setText(AppLocal.getIntString("label.totalcash")); // NOI18N
        jPanel4.add(m_jLblTotalEuros1);

        m_jTotalEuros.setBackground(java.awt.Color.white);
        m_jTotalEuros.setFont(new java.awt.Font("Dialog", 1, 14));
        m_jTotalEuros.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jTotalEuros.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jTotalEuros.setOpaque(true);
        m_jTotalEuros.setPreferredSize(new java.awt.Dimension(125, 25));
        m_jTotalEuros.setRequestFocusEnabled(false);
        jPanel4.add(m_jTotalEuros);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 0));

        m_jLblRemainingEuros.setText(AppLocal.getIntString("label.remainingcash")); // NOI18N
        jPanel6.add(m_jLblRemainingEuros);

        m_jRemaininglEuros.setBackground(java.awt.Color.white);
        m_jRemaininglEuros.setFont(new java.awt.Font("Dialog", 1, 14));
        m_jRemaininglEuros.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jRemaininglEuros.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jRemaininglEuros.setOpaque(true);
        m_jRemaininglEuros.setPreferredSize(new java.awt.Dimension(125, 25));
        m_jRemaininglEuros.setRequestFocusEnabled(false);
        jPanel6.add(m_jRemaininglEuros);

        m_jButtonAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/btnplus.png"))); // NOI18N
        m_jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jButtonAddActionPerformed(evt);
            }
        });
        jPanel6.add(m_jButtonAdd);

        m_jButtonRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/btnminus.png"))); // NOI18N
        m_jButtonRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jButtonRemoveActionPerformed(evt);
            }
        });
        jPanel6.add(m_jButtonRemove);

        jPanel4.add(jPanel6);

        getContentPane().add(jPanel4, java.awt.BorderLayout.NORTH);

        jPanel3.setLayout(new java.awt.BorderLayout());

        m_jTabPayment.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        m_jTabPayment.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        m_jTabPayment.setFocusable(false);
        m_jTabPayment.setRequestFocusEnabled(false);
        m_jTabPayment.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                m_jTabPaymentStateChanged(evt);
            }
        });
        jPanel3.add(m_jTabPayment, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        m_jButtonPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/fileprint.png"))); // NOI18N
        m_jButtonPrint.setSelected(true);
        m_jButtonPrint.setFocusPainted(false);
        m_jButtonPrint.setFocusable(false);
        m_jButtonPrint.setMargin(new java.awt.Insets(8, 16, 8, 16));
        m_jButtonPrint.setRequestFocusEnabled(false);
        jPanel2.add(m_jButtonPrint);
        jPanel2.add(jPanel1);

        m_jButtonOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/button_ok.png"))); // NOI18N
        m_jButtonOK.setText(AppLocal.getIntString("Button.OK")); // NOI18N
        m_jButtonOK.setFocusPainted(false);
        m_jButtonOK.setFocusable(false);
        m_jButtonOK.setMargin(new java.awt.Insets(8, 16, 8, 16));
        m_jButtonOK.setRequestFocusEnabled(false);
        m_jButtonOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jButtonOKActionPerformed(evt);
            }
        });
        jPanel2.add(m_jButtonOK);

        m_jButtonCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/button_cancel.png"))); // NOI18N
        m_jButtonCancel.setText(AppLocal.getIntString("Button.Cancel")); // NOI18N
        m_jButtonCancel.setFocusPainted(false);
        m_jButtonCancel.setFocusable(false);
        m_jButtonCancel.setMargin(new java.awt.Insets(8, 16, 8, 16));
        m_jButtonCancel.setRequestFocusEnabled(false);
        m_jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jButtonCancelActionPerformed(evt);
            }
        });
        jPanel2.add(m_jButtonCancel);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-672)/2, (screenSize.height-497)/2, 672, 497);
    }// </editor-fold>//GEN-END:initComponents

    private void m_jButtonRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jButtonRemoveActionPerformed

        m_aPaymentInfo.removeLast();
        printState();        
        
    }//GEN-LAST:event_m_jButtonRemoveActionPerformed

    private void m_jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jButtonAddActionPerformed

        PaymentInfo returnPayment = ((JPaymentInterface) m_jTabPayment.getSelectedComponent()).executePayment();
        if (returnPayment != null) {
            m_aPaymentInfo.add(returnPayment);
            printState();
        }        
        
    }//GEN-LAST:event_m_jButtonAddActionPerformed

    private void m_jTabPaymentStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_m_jTabPaymentStateChanged
        
        ((JPaymentInterface) m_jTabPayment.getSelectedComponent()).activate(m_sTransaction, m_dTotal - m_aPaymentInfo.getTotal());
        
    }//GEN-LAST:event_m_jTabPaymentStateChanged

    private void m_jButtonOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jButtonOKActionPerformed

            disposeOK(m_jButtonPrint.isSelected() 
                    ? "Printer.Ticket"
                    : "Printer.Ticket2"
            );
            
        
    }//GEN-LAST:event_m_jButtonOKActionPerformed

    private void m_jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jButtonCancelActionPerformed

        dispose();
        
    }//GEN-LAST:event_m_jButtonCancelActionPerformed
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog jDialog1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JButton m_jButtonAdd;
    private javax.swing.JButton m_jButtonCancel;
    private javax.swing.JButton m_jButtonOK;
    private javax.swing.JToggleButton m_jButtonPrint;
    private javax.swing.JButton m_jButtonRemove;
    private javax.swing.JLabel m_jLblRemainingEuros;
    private javax.swing.JLabel m_jLblTotalEuros1;
    private javax.swing.JLabel m_jRemaininglEuros;
    private javax.swing.JTabbedPane m_jTabPayment;
    private javax.swing.JLabel m_jTotalEuros;
    // End of variables declaration//GEN-END:variables
    
}
