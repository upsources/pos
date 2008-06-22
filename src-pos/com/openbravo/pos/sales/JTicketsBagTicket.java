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

package com.openbravo.pos.sales;

import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.ticket.TicketLineInfo;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.pos.forms.AppView; 
import com.openbravo.pos.forms.AppLocal; 
import com.openbravo.pos.printer.*;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.JMessageDialog;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.forms.DataLogicSystem;

public class JTicketsBagTicket extends JTicketsBag {
    
    private DataLogicSystem m_dlSystem = null;

    private DeviceTicket m_TP;    
    private TicketParser m_TTP;    
    private TicketParser m_TTP2; 
    
    private TicketInfo m_ticket;
    private TicketInfo m_ticketCopy;
    
    private JTicketsBagTicketBag m_TicketsBagTicketBag;
    
    private JPanelTicketEdits m_panelticketedit;

    /** Creates new form JTicketsBagTicket */
    public JTicketsBagTicket(AppView app, JPanelTicketEdits panelticket) {
        
        super(app, panelticket);
        m_panelticketedit = panelticket; 
        m_dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystemCreate");
        
        // Inicializo la impresora...
        m_TP = new DeviceTicket();
   
        // Inicializo el parser de documentos de ticket
        m_TTP = new TicketParser(m_TP, m_dlSystem); // para visualizar el ticket
        m_TTP2 = new TicketParser(m_App.getDeviceTicket(), m_dlSystem); // para imprimir el ticket
        
        initComponents();
        
        m_TicketsBagTicketBag = new JTicketsBagTicketBag(this);
        
        m_jTicketEditor.addEditorKeys(m_jKeys);
        
        // Este deviceticket solo tiene una impresora, la de pantalla
        m_jPanelTicket.add(m_TP.getDevicePrinter("1").getPrinterComponent(), BorderLayout.CENTER);
    }
    
    public void activate() {
        
        // precondicion es que no tenemos ticket activado ni ticket en el panel
        
        m_ticket = null;
        m_ticketCopy = null;
        
        printTicket();        
        
        m_jTicketEditor.reset();
        m_jTicketEditor.activate();
        
        m_panelticketedit.setActiveTicket(null, null); 
             
        // postcondicion es que tenemos ticket activado aqui y ticket en el panel
    }
    
    public boolean deactivate() {
        
        // precondicion es que tenemos ticket activado aqui y ticket en el panel        
        m_ticket = null;   
        m_ticketCopy = null;
        return true;       
        // postcondicion es que no tenemos ticket activado ni ticket en el panel
    }
    
    public void deleteTicket() {
        
        if (m_ticketCopy != null) {           
            // Para editar borramos el ticket anterior
            try {               
                m_dlSales.deleteTicket(m_ticketCopy, m_App.getInventoryLocation());
            } catch (BasicException eData) {
                MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.nosaveticket"), eData);
                msg.show(this);                
            }            
        }
        
        m_ticket = null;
        m_ticketCopy = null;
        resetToTicket(); 
    }    
        
    public void canceleditionTicket() {
        
        m_ticketCopy = null;
        resetToTicket();
    }    
    
    private void resetToTicket() {       
        printTicket();
        m_jTicketEditor.reset();        
        m_panelticketedit.setActiveTicket(null, null); 
    }
    
    protected JComponent getBagComponent() {
        return m_TicketsBagTicketBag;
    }
    
    protected JComponent getNullComponent() {
        return this;
    }
      
    private void readTicket() {
        
        try {
            Integer ticketid = m_jTicketEditor.getValueInteger();
            TicketInfo ticket = m_dlSales.loadTicket(ticketid);
            if (ticket == null) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.notexiststicket"));
                msg.show(this);
            } else {
                m_ticket = ticket;
                m_ticketCopy = null; // se asigna al pulsar el boton de editar o devolver
                printTicket();
            }
            
        } catch (BasicException e) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotloadticket"), e);
            msg.show(this);
        }
        
        m_jTicketEditor.reset();
    }
    
    private void printTicket() {
        
        // imprimo m_ticket
        
        m_jEdit.setEnabled(m_ticket != null);
        m_jRefund.setEnabled(m_ticket != null && m_ticket.getTotal() > 0.0);
        m_jPrint.setEnabled(m_ticket != null);
        
        // Este deviceticket solo tiene una impresora, la de pantalla
        m_TP.getDevicePrinter("1").reset();
        
        if (m_ticket == null) {
            m_jTicketId.setText(null);            
        } else {
            m_jTicketId.setText(m_ticket.getName());
            
            try {
                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                script.put("ticket", m_ticket);
                m_TTP.printTicket(script.eval(m_dlSystem.getResourceAsXML("Printer.TicketPreview")).toString());
            } catch (ScriptException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(this);
            } catch (TicketPrinterException eTP) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), eTP);
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

        m_jOptions = new javax.swing.JPanel();
        m_jButtons = new javax.swing.JPanel();
        m_jTicketId = new javax.swing.JLabel();
        m_jEdit = new javax.swing.JButton();
        m_jRefund = new javax.swing.JButton();
        m_jPrint = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        m_jPanelTicket = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        m_jKeys = new com.openbravo.editor.JEditorKeys();
        jPanel5 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        m_jTicketEditor = new com.openbravo.editor.JEditorIntegerPositive();

        setLayout(new java.awt.BorderLayout());

        m_jOptions.setLayout(new java.awt.BorderLayout());

        m_jButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        m_jTicketId.setBackground(java.awt.Color.white);
        m_jTicketId.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        m_jTicketId.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jTicketId.setOpaque(true);
        m_jTicketId.setPreferredSize(new java.awt.Dimension(160, 25));
        m_jTicketId.setRequestFocusEnabled(false);
        m_jButtons.add(m_jTicketId);

        m_jEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/edit.png"))); // NOI18N
        m_jEdit.setText(AppLocal.getIntString("button.edit")); // NOI18N
        m_jEdit.setFocusPainted(false);
        m_jEdit.setFocusable(false);
        m_jEdit.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jEdit.setRequestFocusEnabled(false);
        m_jEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jEditActionPerformed(evt);
            }
        });
        m_jButtons.add(m_jEdit);

        m_jRefund.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/inbox.png"))); // NOI18N
        m_jRefund.setText(AppLocal.getIntString("button.refund")); // NOI18N
        m_jRefund.setFocusPainted(false);
        m_jRefund.setFocusable(false);
        m_jRefund.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jRefund.setRequestFocusEnabled(false);
        m_jRefund.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jRefundActionPerformed(evt);
            }
        });
        m_jButtons.add(m_jRefund);

        m_jPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/yast_printer.png"))); // NOI18N
        m_jPrint.setText(AppLocal.getIntString("button.print")); // NOI18N
        m_jPrint.setFocusPainted(false);
        m_jPrint.setFocusable(false);
        m_jPrint.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jPrint.setRequestFocusEnabled(false);
        m_jPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jPrintActionPerformed(evt);
            }
        });
        m_jButtons.add(m_jPrint);

        m_jOptions.add(m_jButtons, java.awt.BorderLayout.WEST);

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        m_jOptions.add(jPanel2, java.awt.BorderLayout.CENTER);

        add(m_jOptions, java.awt.BorderLayout.NORTH);

        m_jPanelTicket.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        m_jPanelTicket.setLayout(new java.awt.BorderLayout());
        add(m_jPanelTicket, java.awt.BorderLayout.CENTER);

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
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel5.add(m_jTicketEditor, gridBagConstraints);

        jPanel4.add(jPanel5);

        jPanel3.add(jPanel4, java.awt.BorderLayout.NORTH);

        add(jPanel3, java.awt.BorderLayout.EAST);
    }// </editor-fold>//GEN-END:initComponents

    private void m_jEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jEditActionPerformed
                 
        m_ticketCopy = m_ticket;        
        m_TicketsBagTicketBag.showEdit();
        m_panelticketedit.showCatalog();
        m_panelticketedit.setActiveTicket(m_ticket.copyTicket(), null);  
        
    }//GEN-LAST:event_m_jEditActionPerformed

    private void m_jPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jPrintActionPerformed
       
        if (m_ticket != null) {
            try {
                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                script.put("ticket", m_ticket);
                m_TTP2.printTicket(script.eval(m_dlSystem.getResourceAsXML("Printer.TicketPreview")).toString());
            } catch (ScriptException e) {
                JMessageDialog.showMessage(this, new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.cannotprint"), e));
            } catch (TicketPrinterException e) {
                JMessageDialog.showMessage(this, new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.cannotprint"), e));
            }
        }  
        
    }//GEN-LAST:event_m_jPrintActionPerformed

    private void m_jRefundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jRefundActionPerformed
        
        java.util.List aRefundLines = new ArrayList();
        
        for(int i = 0; i < m_ticket.getLinesCount(); i++) {
            TicketLineInfo newline = new TicketLineInfo(m_ticket.getLine(i));
            newline.setMultiply(newline.getMultiply());
            aRefundLines.add(newline);
        } 
        
        m_ticketCopy = null;
        m_TicketsBagTicketBag.showRefund();
        m_panelticketedit.showRefundLines(aRefundLines);
        
        TicketInfo refundticket = new TicketInfo();
        refundticket.setCustomer(m_ticket.getCustomer());
        m_panelticketedit.setActiveTicket(refundticket, null);      
        
    }//GEN-LAST:event_m_jRefundActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        readTicket();
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void m_jKeysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jKeysActionPerformed
        
        readTicket();
        
    }//GEN-LAST:event_m_jKeysActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel m_jButtons;
    private javax.swing.JButton m_jEdit;
    private com.openbravo.editor.JEditorKeys m_jKeys;
    private javax.swing.JPanel m_jOptions;
    private javax.swing.JPanel m_jPanelTicket;
    private javax.swing.JButton m_jPrint;
    private javax.swing.JButton m_jRefund;
    private com.openbravo.editor.JEditorIntegerPositive m_jTicketEditor;
    private javax.swing.JLabel m_jTicketId;
    // End of variables declaration//GEN-END:variables
    
}
