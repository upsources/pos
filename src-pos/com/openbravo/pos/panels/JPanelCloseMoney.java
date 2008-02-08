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

import com.openbravo.pos.forms.JPanelView;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.AppLocal;
import java.awt.*;
import java.text.ParseException;
import javax.swing.*;
import java.util.Date;
import java.util.UUID;
import javax.swing.table.*;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.format.Formats;
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.gui.TableRendererBasic;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.printer.TicketParser;
import com.openbravo.pos.printer.TicketPrinterException;

/**
 *
 * @author adrianromero
 */
public class JPanelCloseMoney extends JPanel implements JPanelView {
    
    private AppView m_App;
    private DataLogicSystem m_dlSystem;
    
    private PaymentsModel m_PaymentsToClose = null;   
    
    private TicketParser m_TTP;
    
    /** Creates new form JPanelCloseMoney */
    public JPanelCloseMoney(AppView oApp) {
        
        m_App = oApp;        
        try {
            m_dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystemCreate");
        } catch (BeanFactoryException e) {
        }
        m_TTP = new TicketParser(m_App.getDeviceTicket(), m_dlSystem);
        
        initComponents();    
        
        m_jTicketTable.setDefaultRenderer(Object.class, new TableRendererBasic(
                new Formats[] {new FormatsPayment(), Formats.CURRENCY}));
        m_jTicketTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        m_jScrollTableTicket.getVerticalScrollBar().setPreferredSize(new Dimension(25,25));       
        m_jTicketTable.getTableHeader().setReorderingAllowed(false);         
        m_jTicketTable.setRowHeight(25);
        m_jTicketTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);         
        
        m_jsalestable.setDefaultRenderer(Object.class, new TableRendererBasic(
                new Formats[] {Formats.STRING, Formats.CURRENCY, Formats.CURRENCY}));
        m_jsalestable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        m_jScrollSales.getVerticalScrollBar().setPreferredSize(new Dimension(25,25));       
        m_jsalestable.getTableHeader().setReorderingAllowed(false);         
        m_jsalestable.setRowHeight(25);
        m_jsalestable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);         
    }

    public JComponent getComponent() {
        return this;
    }

    public String getTitle() {
        return AppLocal.getIntString("Menu.CloseTPV");
    }    
    
    public void activate() throws BasicException {
        loadData();
    }   
    
    public boolean deactivate() {
        // se me debe permitir cancelar el deactivate   
        return true;
    }  
    
    private void loadData() throws BasicException {
        
        // Reset
        m_jMinDate.setText(null);
        m_jMaxDate.setText(null);
        m_jCloseCash.setEnabled(false);
        m_jCount.setText(null); // AppLocal.getIntString("label.noticketstoclose");
        m_jCash.setText(null);

        m_jSales.setText(null);
        m_jSalesSubtotal.setText(null);
        m_jSalesTotal.setText(null);
        
        m_jTicketTable.setModel(new DefaultTableModel());
        m_jsalestable.setModel(new DefaultTableModel());
            
        // LoadData
        m_PaymentsToClose = PaymentsModel.loadInstance(m_App);
        
        // Populate Data
        m_jMinDate.setText(m_PaymentsToClose.printDateStart());
        m_jMaxDate.setText(m_PaymentsToClose.printDateEnd());
        
        if (m_PaymentsToClose.getPayments() != 0 || m_PaymentsToClose.getSales() != 0) {

            m_jCloseCash.setEnabled(true);

            m_jCount.setText(m_PaymentsToClose.printPayments());
            m_jCash.setText(m_PaymentsToClose.printPaymentsTotal());
            
            m_jSales.setText(m_PaymentsToClose.printSales());
            m_jSalesSubtotal.setText(m_PaymentsToClose.printSalesSubtotal());
            m_jSalesTotal.setText(m_PaymentsToClose.printSalesTotal());
        }          
        
        m_jTicketTable.setModel(m_PaymentsToClose.getPaymentsModel());
                
        TableColumnModel jColumns = m_jTicketTable.getColumnModel();
        jColumns.getColumn(0).setPreferredWidth(150);
        jColumns.getColumn(0).setResizable(false);
        jColumns.getColumn(1).setPreferredWidth(100);
        jColumns.getColumn(1).setResizable(false);
//        jColumns.getColumn(2).setPreferredWidth(100);
//        jColumns.getColumn(2).setResizable(false);
//        jColumns.getColumn(3).setPreferredWidth(100);
//        jColumns.getColumn(3).setResizable(false);    
        
        m_jsalestable.setModel(m_PaymentsToClose.getSalesModel());
        
        jColumns = m_jsalestable.getColumnModel();
        jColumns.getColumn(0).setPreferredWidth(150);
        jColumns.getColumn(0).setResizable(false);
        jColumns.getColumn(1).setPreferredWidth(100);
        jColumns.getColumn(1).setResizable(false);
        jColumns.getColumn(1).setPreferredWidth(100);
        jColumns.getColumn(1).setResizable(false);
    }   
    
    private void printPayments() {
        
        String sresource = m_dlSystem.getResourceAsXML("Printer.CloseCash");
        if (sresource == null) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"));
            msg.show(this);
        } else {
            try {
                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                script.put("payments", m_PaymentsToClose);
                m_TTP.printTicket(script.eval(sresource).toString());
            } catch (ScriptException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(this);
            } catch (TicketPrinterException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(this);
            }
        }
    }

    private class FormatsPayment extends Formats {
        protected String formatValueInt(Object value) {
            return AppLocal.getIntString("transpayment." + (String) value);
        }   
        protected Object parseValueInt(String value) throws ParseException {
            return value;
        }
        public int getAlignment() {
            return javax.swing.SwingConstants.LEFT;
        }         
    }    
   
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        m_jCount = new javax.swing.JTextField();
        m_jMinDate = new javax.swing.JTextField();
        m_jMaxDate = new javax.swing.JTextField();
        m_jCash = new javax.swing.JTextField();
        m_jCloseCash = new javax.swing.JButton();
        m_jScrollTableTicket = new javax.swing.JScrollPane();
        m_jTicketTable = new javax.swing.JTable();
        m_jScrollSales = new javax.swing.JScrollPane();
        m_jsalestable = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        m_jSales = new javax.swing.JTextField();
        m_jSalesTotal = new javax.swing.JTextField();
        m_jSalesSubtotal = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();

        setLayout(null);

        jLabel1.setText(AppLocal.getIntString("Label.Tickets"));
        add(jLabel1);
        jLabel1.setBounds(430, 150, 90, 15);

        jLabel2.setText(AppLocal.getIntString("Label.StartDate"));
        add(jLabel2);
        jLabel2.setBounds(10, 50, 140, 15);

        jLabel3.setText(AppLocal.getIntString("Label.EndDate"));
        add(jLabel3);
        jLabel3.setBounds(10, 80, 140, 15);

        jLabel4.setText(AppLocal.getIntString("Label.Cash"));
        add(jLabel4);
        jLabel4.setBounds(430, 180, 90, 15);

        m_jCount.setEditable(false);
        m_jCount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        add(m_jCount);
        m_jCount.setBounds(520, 150, 100, 19);

        m_jMinDate.setEditable(false);
        m_jMinDate.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        add(m_jMinDate);
        m_jMinDate.setBounds(150, 50, 160, 19);

        m_jMaxDate.setEditable(false);
        m_jMaxDate.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        add(m_jMaxDate);
        m_jMaxDate.setBounds(150, 80, 160, 19);

        m_jCash.setEditable(false);
        m_jCash.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        add(m_jCash);
        m_jCash.setBounds(520, 180, 100, 19);

        m_jCloseCash.setText(AppLocal.getIntString("Button.CloseCash"));
        m_jCloseCash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jCloseCashActionPerformed(evt);
            }
        });

        add(m_jCloseCash);
        m_jCloseCash.setBounds(10, 490, 110, 30);

        m_jTicketTable.setFocusable(false);
        m_jTicketTable.setIntercellSpacing(new java.awt.Dimension(0, 1));
        m_jTicketTable.setRequestFocusEnabled(false);
        m_jTicketTable.setShowVerticalLines(false);
        m_jScrollTableTicket.setViewportView(m_jTicketTable);

        add(m_jScrollTableTicket);
        m_jScrollTableTicket.setBounds(10, 150, 400, 140);

        m_jsalestable.setFocusable(false);
        m_jsalestable.setIntercellSpacing(new java.awt.Dimension(0, 1));
        m_jsalestable.setRequestFocusEnabled(false);
        m_jsalestable.setShowVerticalLines(false);
        m_jScrollSales.setViewportView(m_jsalestable);

        add(m_jScrollSales);
        m_jScrollSales.setBounds(10, 330, 400, 140);

        jLabel5.setText(AppLocal.getIntString("label.sales"));
        add(jLabel5);
        jLabel5.setBounds(430, 340, 90, 15);

        m_jSales.setEditable(false);
        m_jSales.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        add(m_jSales);
        m_jSales.setBounds(520, 340, 100, 19);

        m_jSalesTotal.setEditable(false);
        m_jSalesTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        add(m_jSalesTotal);
        m_jSalesTotal.setBounds(520, 400, 100, 19);

        m_jSalesSubtotal.setEditable(false);
        m_jSalesSubtotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        add(m_jSalesSubtotal);
        m_jSalesSubtotal.setBounds(520, 370, 100, 19);

        jLabel6.setText(AppLocal.getIntString("label.subtotalcash"));
        add(jLabel6);
        jLabel6.setBounds(430, 370, 90, 15);

        jLabel7.setText(AppLocal.getIntString("label.totalcash"));
        add(jLabel7);
        jLabel7.setBounds(430, 400, 90, 15);

        jLabel8.setText(AppLocal.getIntString("label.paymentstitle"));
        jLabel8.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        add(jLabel8);
        jLabel8.setBounds(10, 120, 660, 16);

        jLabel9.setText(AppLocal.getIntString("label.salestitle"));
        jLabel9.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        add(jLabel9);
        jLabel9.setBounds(10, 300, 660, 16);

        jLabel10.setText(AppLocal.getIntString("label.datestitle"));
        jLabel10.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        add(jLabel10);
        jLabel10.setBounds(10, 20, 660, 16);

    }// </editor-fold>//GEN-END:initComponents

    private void m_jCloseCashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jCloseCashActionPerformed
        // TODO add your handling code here:
        int res = JOptionPane.showConfirmDialog(this, AppLocal.getIntString("message.wannaclosecash"), AppLocal.getIntString("message.title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (res == JOptionPane.YES_OPTION) {
            
            Date dNow = new Date();
            
            try {               
                // Cerramos la caja si esta pendiente de cerrar.
                if (m_App.getActiveCashDateEnd() == null) {
                    new StaticSentence(m_App.getSession()
                        , "UPDATE CLOSEDCASH SET DATEEND = ? WHERE HOST = ? AND MONEY = ?"
                        , new SerializerWriteBasic(new Datas[] {Datas.TIMESTAMP, Datas.STRING, Datas.STRING}))
                        .exec(new Object[] {dNow, m_App.getProperties().getHost(), m_App.getActiveCashIndex()}); 
                }
            } catch (BasicException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.cannotclosecash"), e);
                msg.show(this);
            }
            
            try {
                // Creamos una nueva caja          
                m_App.setActiveCash(UUID.randomUUID().toString(), dNow, null);
                
                // creamos la caja activa      
                m_dlSystem.execInsertCash(
                        new Object[] {m_App.getActiveCashIndex(), m_App.getProperties().getHost(), m_App.getActiveCashDateStart(), m_App.getActiveCashDateEnd()});                  
               
                // ponemos la fecha de fin
                m_PaymentsToClose.setDateEnd(dNow);
                
                // Imprimimos el miniinforme
                printPayments();
                // Mostramos el mensaje
                JOptionPane.showMessageDialog(this, AppLocal.getIntString("message.closecashok"), AppLocal.getIntString("message.title"), JOptionPane.INFORMATION_MESSAGE);
            } catch (BasicException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.cannotclosecash"), e);
                msg.show(this);
            }
            
            try {
                loadData();
            } catch (BasicException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("label.noticketstoclose"), e);
                msg.show(this);
            }
        }         
    }//GEN-LAST:event_m_jCloseCashActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField m_jCash;
    private javax.swing.JButton m_jCloseCash;
    private javax.swing.JTextField m_jCount;
    private javax.swing.JTextField m_jMaxDate;
    private javax.swing.JTextField m_jMinDate;
    private javax.swing.JTextField m_jSales;
    private javax.swing.JTextField m_jSalesSubtotal;
    private javax.swing.JTextField m_jSalesTotal;
    private javax.swing.JScrollPane m_jScrollSales;
    private javax.swing.JScrollPane m_jScrollTableTicket;
    private javax.swing.JTable m_jTicketTable;
    private javax.swing.JTable m_jsalestable;
    // End of variables declaration//GEN-END:variables
    
}
