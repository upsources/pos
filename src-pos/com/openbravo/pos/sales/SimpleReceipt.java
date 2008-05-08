//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2008 Openbravo, S.L.
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

import com.openbravo.format.Formats;
import com.openbravo.pos.customers.DataLogicCustomers;
import com.openbravo.pos.customers.JCustomerFinder;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.ticket.TicketLineInfo;
import java.awt.BorderLayout;

/**
 *
 * @author  adrian
 */
public class SimpleReceipt extends javax.swing.JPanel {
    
    protected DataLogicCustomers dlCustomers;
        
    private JTicketLines ticketlines;
    private TicketInfo ticket;
    private Object ticketext;
    
    /** Creates new form SimpleReceipt */
    public SimpleReceipt(String ticketline, DataLogicCustomers dlCustomers) {        
        
        initComponents();
        
        // dlSystem.getResourceAsXML("Ticket.Line")
        ticketlines = new JTicketLines(ticketline);
        this.dlCustomers = dlCustomers;
        
        jPanel2.add(ticketlines, BorderLayout.CENTER);
    }
    
    public void setCustomerEnabled(boolean value) {
        btnCustomer.setEnabled(value);
    }
    
    public void setTicket(TicketInfo ticket, Object ticketext) {
        
        this.ticket = ticket;
        this.ticketext = ticketext;
        
        // The ticket name
        m_jTicketId.setText(ticket.getName(ticketext));        
        
        ticketlines.clearTicketLines();
        for (int i = 0; i < ticket.getLinesCount(); i++) {
            ticketlines.addTicketLine(ticket.getLine(i));
        }
        
        if (ticket.getLinesCount() > 0) {
            ticketlines.setSelectedIndex(0);
        }
        
        printTotals();
               
    }
    
    private void printTotals() {
        
        if (ticket.getLinesCount() > 0) {
            m_jSubtotalEuros.setText(Formats.CURRENCY.formatValue(new Double(ticket.getSubTotal())));
            m_jTaxesEuros.setText(Formats.CURRENCY.formatValue(new Double(ticket.getTax())));
            m_jTotalEuros.setText(Formats.CURRENCY.formatValue(new Double(ticket.getTotal()))); 
        } else {
            m_jSubtotalEuros.setText(null);
            m_jTaxesEuros.setText(null);
            m_jTotalEuros.setText(null); 
        }
    }
    
    public TicketInfo getTicket()  {
        return ticket;
    }
    
    public TicketLineInfo getSelectedLine() {
        
        int i = ticketlines.getSelectedIndex();
        if (i >= 0) {
            TicketLineInfo line = ticket.getLine(i);
            ticket.removeLine(i);
            ticketlines.removeTicketLine(i);
            printTotals();
            return line;
        } else {
            return null;
        }
    }
    
    public TicketLineInfo getSelectedLineUnit() {
        
        int i = ticketlines.getSelectedIndex();
        if (i >= 0) {
            TicketLineInfo line = ticket.getLine(i);
            if (line.getMultiply() > 1.0) {
                line.setMultiply(line.getMultiply() -1.0);
                ticketlines.setTicketLine(i, line);
                line = line.copyTicketLine();
                line.setMultiply(1.0);
                printTotals();
                return line;
            } else if (line.getMultiply() == 1.0) {                
                ticket.removeLine(i);
                ticketlines.removeTicketLine(i);
                printTotals();
                return line;
            } else {
                return null;
            }            
        } else {
            return null;
        }
    }
    
    public void addSelectedLine(TicketLineInfo line) {
        int i = ticketlines.getSelectedIndex();
        if (i >= 0 
                && ticket.getLine(i).getProduct().getId() != null && line.getProduct().getId() != null && ticket.getLine(i).getProduct().getId().equals(line.getProduct().getId())
                && ticket.getLine(i).getTaxInfo().getId().equals(line.getTaxInfo().getId())
                && ticket.getLine(i).getPrice() == line.getPrice()) {    
            // inc the line
            ticket.getLine(i).setMultiply(ticket.getLine(i).getMultiply() + line.getMultiply());
            ticketlines.setTicketLine(i, ticket.getLine(i));  
            printTotals();
        } else {
            ticket.addLine(line);
            ticketlines.addTicketLine(line);
            printTotals();
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

        jPanel1 = new javax.swing.JPanel();
        m_jPanTotals = new javax.swing.JPanel();
        m_jTotalEuros = new javax.swing.JLabel();
        m_jLblTotalEuros1 = new javax.swing.JLabel();
        m_jSubtotalEuros = new javax.swing.JLabel();
        m_jTaxesEuros = new javax.swing.JLabel();
        m_jLblTotalEuros2 = new javax.swing.JLabel();
        m_jLblTotalEuros3 = new javax.swing.JLabel();
        m_jButtons = new javax.swing.JPanel();
        m_jTicketId = new javax.swing.JLabel();
        btnCustomer = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel1.setLayout(new java.awt.BorderLayout());

        m_jPanTotals.setLayout(new java.awt.GridBagLayout());

        m_jTotalEuros.setBackground(java.awt.Color.white);
        m_jTotalEuros.setFont(new java.awt.Font("Dialog", 1, 14));
        m_jTotalEuros.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jTotalEuros.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jTotalEuros.setOpaque(true);
        m_jTotalEuros.setPreferredSize(new java.awt.Dimension(150, 25));
        m_jTotalEuros.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        m_jPanTotals.add(m_jTotalEuros, gridBagConstraints);

        m_jLblTotalEuros1.setText(AppLocal.getIntString("label.totalcash")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        m_jPanTotals.add(m_jLblTotalEuros1, gridBagConstraints);

        m_jSubtotalEuros.setBackground(java.awt.Color.white);
        m_jSubtotalEuros.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jSubtotalEuros.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jSubtotalEuros.setOpaque(true);
        m_jSubtotalEuros.setPreferredSize(new java.awt.Dimension(150, 25));
        m_jSubtotalEuros.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        m_jPanTotals.add(m_jSubtotalEuros, gridBagConstraints);

        m_jTaxesEuros.setBackground(java.awt.Color.white);
        m_jTaxesEuros.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jTaxesEuros.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jTaxesEuros.setOpaque(true);
        m_jTaxesEuros.setPreferredSize(new java.awt.Dimension(150, 25));
        m_jTaxesEuros.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        m_jPanTotals.add(m_jTaxesEuros, gridBagConstraints);

        m_jLblTotalEuros2.setText(AppLocal.getIntString("label.taxcash")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        m_jPanTotals.add(m_jLblTotalEuros2, gridBagConstraints);

        m_jLblTotalEuros3.setText(AppLocal.getIntString("label.subtotalcash")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        m_jPanTotals.add(m_jLblTotalEuros3, gridBagConstraints);

        jPanel1.add(m_jPanTotals, java.awt.BorderLayout.EAST);

        add(jPanel1, java.awt.BorderLayout.SOUTH);

        m_jButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        m_jTicketId.setBackground(java.awt.Color.white);
        m_jTicketId.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        m_jTicketId.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.darkShadow")), javax.swing.BorderFactory.createEmptyBorder(1, 4, 1, 4)));
        m_jTicketId.setOpaque(true);
        m_jTicketId.setPreferredSize(new java.awt.Dimension(160, 25));
        m_jTicketId.setRequestFocusEnabled(false);
        m_jButtons.add(m_jTicketId);

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
        m_jButtons.add(btnCustomer);

        add(m_jButtons, java.awt.BorderLayout.NORTH);

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel2.setLayout(new java.awt.BorderLayout());
        add(jPanel2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomerActionPerformed
        
        JCustomerFinder finder = JCustomerFinder.getCustomerFinder(this, dlCustomers);
        finder.search(ticket.getCustomer());
        finder.setVisible(true);
        ticket.setCustomer(finder.getSelectedCustomer());
        
        // The ticket name
        m_jTicketId.setText(ticket.getName(ticketext));
        
    }//GEN-LAST:event_btnCustomerActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCustomer;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel m_jButtons;
    private javax.swing.JLabel m_jLblTotalEuros1;
    private javax.swing.JLabel m_jLblTotalEuros2;
    private javax.swing.JLabel m_jLblTotalEuros3;
    private javax.swing.JPanel m_jPanTotals;
    private javax.swing.JLabel m_jSubtotalEuros;
    private javax.swing.JLabel m_jTaxesEuros;
    private javax.swing.JLabel m_jTicketId;
    private javax.swing.JLabel m_jTotalEuros;
    // End of variables declaration//GEN-END:variables
    
}
