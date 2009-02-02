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
//    Foundation, Inc., 51 Franklin Street, Fifth floor, Boston, MA  02110-1301  USA
package com.openbravo.pos.panels;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.gui.ListQBFModelNumber;
import com.openbravo.data.loader.LocalRes;
import com.openbravo.data.loader.QBFCompareEnum;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.user.EditorCreator;
import com.openbravo.data.user.ListProvider;
import com.openbravo.data.user.ListProviderCreator;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.inventory.TaxCategoryInfo;
import com.openbravo.pos.ticket.FindTicketsInfo;
import com.openbravo.pos.ticket.FindTicketsRenderer;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.JFrame;

/**
 *
 * @author  Mikel irurita
 */
public class JTicketsFinder extends javax.swing.JDialog implements EditorCreator {

    private ListProvider lpr;
    private SentenceList m_sentcat;
    private ComboBoxValModel m_CategoryModel;
    private DataLogicSales dlSales;
    private FindTicketsInfo selectedTicket;
    private Calendar cal;
   
    /** Creates new form JCustomerFinder */
    private JTicketsFinder(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
    }

    /** Creates new form JCustomerFinder */
    private JTicketsFinder(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);
    }
    
    public static JTicketsFinder getReceiptFinder(Component parent, DataLogicSales dlSales) {
        Window window = getWindow(parent);
        
        JTicketsFinder myMsg;
        if (window instanceof Frame) { 
            myMsg = new JTicketsFinder((Frame) window, true);
        } else {
            myMsg = new JTicketsFinder((Dialog) window, true);
        }
        myMsg.init(dlSales);
        myMsg.applyComponentOrientation(parent.getComponentOrientation());
        return myMsg;
    }
    
    public FindTicketsInfo getSelectedCustomer() {
        return selectedTicket;
    }

    private void init(DataLogicSales dlSales) {
        cal = Calendar.getInstance();
        this.dlSales = dlSales;
        
        initComponents();

        jScrollPane1.getVerticalScrollBar().setPreferredSize(new Dimension(35, 35));

        jtxtTicketID.addEditorKeys(m_jKeys);
        jtxtMoney.addEditorKeys(m_jKeys);
        jtxtTicketID.activate();
        lpr = new ListProviderCreator(dlSales.getTicketsList(), this);

        jListTickets.setCellRenderer(new FindTicketsRenderer());

        getRootPane().setDefaultButton(jcmdOK);
        
        initCombos();
        
        defaultValues();

        selectedTicket = null;
    }
    
    public void executeSearch() {
        try {
            jListTickets.setModel(new MyListData(lpr.loadData()));
            if (jListTickets.getModel().getSize() > 0) {
                jListTickets.setSelectedIndex(0);
            }
        } catch (BasicException e) {
            e.printStackTrace();
        }        
    }
    
    private void initCombos() {
        int year = cal.get(Calendar.YEAR);
        
        jcboMoney.setModel(new ListQBFModelNumber());
        
        jcboDayFrom.addItem(null);
        jcboMonthFrom.addItem(null);
        jcboYearFrom.addItem(null);
        jcboDayTo.addItem(null);
        jcboMonthTo.addItem(null);
        jcboYearTo.addItem(null);            
        
        for (int i = 1; i < 32; i++) {
            jcboDayFrom.addItem(String.valueOf(i));
            jcboDayTo.addItem(String.valueOf(i));
        }
        for (int i = 1; i < 13; i++) {
            jcboMonthFrom.addItem(String.valueOf(i));
            jcboMonthTo.addItem(String.valueOf(i));
        }
        for (int i = year; i > year-10; i--) {
            jcboYearFrom.addItem(String.valueOf(i));
            jcboYearTo.addItem(String.valueOf(i));
        }
        
        m_sentcat = dlSales.getUserList();
        m_CategoryModel = new ComboBoxValModel(); 
        
        List catlist=null;
        try {
            catlist = m_sentcat.list();
        } catch (BasicException ex) {
            ex.getMessage();
        }
        catlist.add(0, null);
        m_CategoryModel = new ComboBoxValModel(catlist);
        jcboUser.setModel(m_CategoryModel);

        ComboBoxValModel cbv = new ComboBoxValModel();
        cbv.add(null);
        cbv.add(LocalRes.getIntString("combo.today"));
        cbv.add(LocalRes.getIntString("combo.month"));
        cbv.add(LocalRes.getIntString("combo.year"));
        jcboTimeFrame.setModel(cbv);
        
        
    }
    
    private void defaultValues() {
        
        jListTickets.setModel(new MyListData(new ArrayList()));
        
        jcboUser.setSelectedItem(null);
        
        jtxtTicketID.reset();
        jtxtTicketID.activate();
        
        jCheckBoxSales.setSelected(false);
        jCheckBoxRefunds.setSelected(false); 
        
        jcboTimeFrame.setSelectedItem(null);
        jcboDayFrom.setSelectedItem(null);
        jcboMonthFrom.setSelectedItem(null);
        jcboYearFrom.setSelectedItem(null);
        jcboDayTo.setSelectedItem(null);
        jcboMonthTo.setSelectedItem(null);
        jcboYearTo.setSelectedItem(null);
        
        jcboUser.setSelectedItem(null);
        
        jcboMoney.setSelectedItem( ((ListQBFModelNumber)jcboMoney.getModel()).getElementAt(0) );
        jcboMoney.revalidate();
        jcboMoney.repaint();
                
        jtxtMoney.reset();

    }
    
    @Override
    public Object createValue() throws BasicException {
        boolean dateRange = false;
        
        Object[] afilter = new Object[12];
        
        // Ticket ID
        if (jtxtTicketID.getText() == null || jtxtTicketID.getText().equals("")) {
            afilter[0] = QBFCompareEnum.COMP_NONE;
            afilter[1] = null;
        } else {
            afilter[0] = QBFCompareEnum.COMP_EQUALS;
            afilter[1] = jtxtTicketID.getValueInteger();
        }
        
        // Sale and refund checkbox        
        if (jCheckBoxSales.isSelected() && jCheckBoxRefunds.isSelected() || !jCheckBoxSales.isSelected() && !jCheckBoxRefunds.isSelected()) {
            afilter[2] = QBFCompareEnum.COMP_NONE;
            afilter[3] = null;
        } else if (jCheckBoxSales.isSelected()) {
            afilter[2] = QBFCompareEnum.COMP_EQUALS;
            afilter[3] = 0;
        } else if (jCheckBoxRefunds.isSelected()) {
            afilter[2] = QBFCompareEnum.COMP_EQUALS;
            afilter[3] = 1;
        }
        
        // Receipt money
        afilter[5] = jtxtMoney.getDoubleValue();
        afilter[4] = afilter[5] == null ? QBFCompareEnum.COMP_NONE : jcboMoney.getSelectedItem();

        //Timeframe
        if (jcboTimeFrame.getSelectedItem() == null) {
            dateRange = true;
            afilter[6] = QBFCompareEnum.COMP_NONE;
            afilter[7] = null;
            afilter[8] = QBFCompareEnum.COMP_NONE;
            afilter[9] = null;
        } else {
            int year = cal.get(Calendar.YEAR);
            
            String month = (cal.get(Calendar.MONTH) + 1 < 10) 
                ? "0"+(cal.get(Calendar.MONTH) + 1)
                : Integer.toString(cal.get(Calendar.MONTH) + 1);
            
            String day = (cal.get(Calendar.DAY_OF_MONTH) < 10) 
                ? "0"+cal.get(Calendar.DAY_OF_MONTH)
                : Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
            
            if (jcboTimeFrame.getSelectedItem() == LocalRes.getIntString("combo.today")) {
                afilter[6] = QBFCompareEnum.COMP_RE;
                afilter[7] = "%" + year +"-"+ month +"-"+ day + "%" ;
                afilter[8] = QBFCompareEnum.COMP_NONE;
                afilter[9] = null;
            } else if (jcboTimeFrame.getSelectedItem() == LocalRes.getIntString("combo.month")) {
                afilter[6] = QBFCompareEnum.COMP_RE;
                afilter[7] = "%" + year +"-"+ month + "%";
                afilter[8] = QBFCompareEnum.COMP_NONE;
                afilter[9] = null;
            } else if (jcboTimeFrame.getSelectedItem() == LocalRes.getIntString("combo.year")) {
                afilter[6] = QBFCompareEnum.COMP_RE;
                afilter[7] = "%" + Integer.toString(year) + "%";
                afilter[8] = QBFCompareEnum.COMP_NONE;
                afilter[9] = null;
            }
        }
        
        // Date range
        if (dateRange) {
            String dayF = (jcboDayFrom.getSelectedItem() == null) ? "0" : (String)jcboDayFrom.getSelectedItem();
            String monthF = (jcboMonthFrom.getSelectedItem() == null) ? "1" : (String)jcboMonthFrom.getSelectedItem();

            String dayT = (jcboDayTo.getSelectedItem() == null) ? "32" : String.valueOf( Integer.parseInt((String)jcboDayTo.getSelectedItem()) + 1 );
            String monthT = (jcboMonthTo.getSelectedItem() == null) ? "12" : (String)jcboMonthTo.getSelectedItem();

            if (jcboYearFrom.getSelectedItem() == null && jcboYearTo.getSelectedItem() == null) {
                afilter[6] = QBFCompareEnum.COMP_NONE;
                afilter[7] = null;
                afilter[8] = QBFCompareEnum.COMP_NONE;
                afilter[9] = null;

            } else if (jcboYearFrom.getSelectedItem() == null && jcboYearTo.getSelectedItem() != null) {
                afilter[6] = QBFCompareEnum.COMP_NONE;
                afilter[7] = null;
                afilter[8] = QBFCompareEnum.COMP_LESSOREQUALS;
                afilter[9] = jcboYearTo.getSelectedItem() +"-"+ monthT +"-"+ dayT;

            } else if (jcboYearFrom.getSelectedItem() != null && jcboYearTo.getSelectedItem() == null) {
                afilter[6] = QBFCompareEnum.COMP_GREATEROREQUALS;
                afilter[7] = jcboYearFrom.getSelectedItem() +"-"+ monthF +"-"+ dayF;
                afilter[8] = QBFCompareEnum.COMP_NONE;
                afilter[9] = null;

            } else {
                afilter[6] = QBFCompareEnum.COMP_GREATEROREQUALS;
                afilter[7] = jcboYearFrom.getSelectedItem() +"-"+ monthF +"-"+ dayF;
                afilter[8] = QBFCompareEnum.COMP_LESSOREQUALS;
                afilter[9] = jcboYearTo.getSelectedItem() +"-"+ monthT +"-"+ dayT;
            }
        }

        //User
        if (jcboUser.getSelectedItem() == null) {
            afilter[10] = QBFCompareEnum.COMP_NONE;
            afilter[11] = null; 
        } else {
            afilter[10] = QBFCompareEnum.COMP_EQUALS;
            afilter[11] = ((TaxCategoryInfo)jcboUser.getSelectedItem()).getName(); 
        }
        
        return afilter;

    } 

    private static Window getWindow(Component parent) {
        if (parent == null) {
            return new JFrame();
        } else if (parent instanceof Frame || parent instanceof Dialog) {
            return (Window) parent;
        } else {
            return getWindow(parent.getParent());
        }
    }
    
    private static class MyListData extends javax.swing.AbstractListModel {
        
        private java.util.List m_data;
        
        public MyListData(java.util.List data) {
            m_data = data;
        }
        
        @Override
        public Object getElementAt(int index) {
            return m_data.get(index);
        }
        
        @Override
        public int getSize() {
            return m_data.size();
        } 
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jCheckBoxSales = new javax.swing.JCheckBox();
        jCheckBoxRefunds = new javax.swing.JCheckBox();
        jPanel9 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jcboYearFrom = new javax.swing.JComboBox();
        jcboMonthFrom = new javax.swing.JComboBox();
        jcboDayFrom = new javax.swing.JComboBox();
        jcboYearTo = new javax.swing.JComboBox();
        jcboMonthTo = new javax.swing.JComboBox();
        jcboDayTo = new javax.swing.JComboBox();
        jcboTimeFrame = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jtxtMoney = new com.openbravo.editor.JEditorCurrency();
        jcboUser = new javax.swing.JComboBox();
        jcboMoney = new javax.swing.JComboBox();
        m_jKeys = new com.openbravo.editor.JEditorKeys();
        jtxtTicketID = new com.openbravo.editor.JEditorIntegerPositive();
        jPanel6 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListTickets = new javax.swing.JList();
        jPanel8 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jcmdOK = new javax.swing.JButton();
        jcmdCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(AppLocal.getIntString("form.tickettitle")); // NOI18N
        setBackground(java.awt.Color.white);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel7.setPreferredSize(new java.awt.Dimension(0, 270));

        jLabel1.setText(AppLocal.getIntString("label.ticketid")); // NOI18N

        jLabel2.setText(AppLocal.getIntString("label.tickettype")); // NOI18N

        jCheckBoxSales.setText(AppLocal.getIntString("label.sales")); // NOI18N

        jCheckBoxRefunds.setText(AppLocal.getIntString("label.refunds")); // NOI18N

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(AppLocal.getIntString("label.timeperiod"))); // NOI18N

        jLabel4.setText(AppLocal.getIntString("label.to")); // NOI18N

        jLabel3.setText(AppLocal.getIntString("label.timeframe")); // NOI18N

        jLabel5.setText(AppLocal.getIntString("label.from")); // NOI18N

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4))
                .addGap(39, 39, 39)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jcboYearTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jcboMonthTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jcboDayTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jcboYearFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jcboMonthFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jcboDayFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jcboTimeFrame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(169, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel3))
                    .addComponent(jcboTimeFrame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jcboYearFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jcboMonthFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jcboDayFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jcboYearTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jcboMonthTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jcboDayTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel6.setText(AppLocal.getIntString("label.user")); // NOI18N

        jLabel7.setText(AppLocal.getIntString("label.money")); // NOI18N

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addGap(50, 50, 50)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jCheckBoxSales)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCheckBoxRefunds))
                            .addComponent(jtxtTicketID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(42, 42, 42))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel6))
                        .addGap(80, 80, 80)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jcboMoney, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jtxtMoney, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jcboUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(m_jKeys, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(jtxtTicketID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jCheckBoxSales)
                            .addComponent(jCheckBoxRefunds))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jcboUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jtxtMoney, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jcboMoney, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel7))))
                    .addComponent(m_jKeys, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel5.add(jPanel7, java.awt.BorderLayout.CENTER);

        jButton1.setText(AppLocal.getIntString("button.clean")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton1);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/launch.png"))); // NOI18N
        jButton3.setText(AppLocal.getIntString("button.executefilter")); // NOI18N
        jButton3.setFocusPainted(false);
        jButton3.setFocusable(false);
        jButton3.setRequestFocusEnabled(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton3);

        jPanel5.add(jPanel6, java.awt.BorderLayout.SOUTH);

        jPanel3.add(jPanel5, java.awt.BorderLayout.PAGE_START);

        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jListTickets.setFocusable(false);
        jListTickets.setRequestFocusEnabled(false);
        jListTickets.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jListTicketsMouseClicked(evt);
            }
        });
        jListTickets.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListTicketsValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jListTickets);

        jPanel4.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel4, java.awt.BorderLayout.CENTER);

        jPanel8.setLayout(new java.awt.BorderLayout());

        jcmdOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/button_ok.png"))); // NOI18N
        jcmdOK.setText(AppLocal.getIntString("Button.OK")); // NOI18N
        jcmdOK.setEnabled(false);
        jcmdOK.setFocusPainted(false);
        jcmdOK.setFocusable(false);
        jcmdOK.setMargin(new java.awt.Insets(8, 16, 8, 16));
        jcmdOK.setRequestFocusEnabled(false);
        jcmdOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcmdOKActionPerformed(evt);
            }
        });
        jPanel1.add(jcmdOK);

        jcmdCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/button_cancel.png"))); // NOI18N
        jcmdCancel.setText(AppLocal.getIntString("Button.Cancel")); // NOI18N
        jcmdCancel.setFocusPainted(false);
        jcmdCancel.setFocusable(false);
        jcmdCancel.setMargin(new java.awt.Insets(8, 16, 8, 16));
        jcmdCancel.setRequestFocusEnabled(false);
        jcmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcmdCancelActionPerformed(evt);
            }
        });
        jPanel1.add(jcmdCancel);

        jPanel8.add(jPanel1, java.awt.BorderLayout.LINE_END);

        jPanel3.add(jPanel8, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jPanel3, java.awt.BorderLayout.CENTER);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-645)/2, (screenSize.height-684)/2, 645, 684);
    }// </editor-fold>//GEN-END:initComponents
    private void jcmdOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcmdOKActionPerformed
        selectedTicket = (FindTicketsInfo) jListTickets.getSelectedValue();
        dispose();
    }//GEN-LAST:event_jcmdOKActionPerformed

    private void jcmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcmdCancelActionPerformed
        dispose();
    }//GEN-LAST:event_jcmdCancelActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        executeSearch();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jListTicketsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListTicketsValueChanged
        jcmdOK.setEnabled(jListTickets.getSelectedValue() != null);

}//GEN-LAST:event_jListTicketsValueChanged

    private void jListTicketsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListTicketsMouseClicked
        
        if (evt.getClickCount() == 2) {
            selectedTicket = (FindTicketsInfo) jListTickets.getSelectedValue();
            dispose();
        }
        
}//GEN-LAST:event_jListTicketsMouseClicked

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        defaultValues();
}//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JCheckBox jCheckBoxRefunds;
    private javax.swing.JCheckBox jCheckBoxSales;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JList jListTickets;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox jcboDayFrom;
    private javax.swing.JComboBox jcboDayTo;
    private javax.swing.JComboBox jcboMoney;
    private javax.swing.JComboBox jcboMonthFrom;
    private javax.swing.JComboBox jcboMonthTo;
    private javax.swing.JComboBox jcboTimeFrame;
    private javax.swing.JComboBox jcboUser;
    private javax.swing.JComboBox jcboYearFrom;
    private javax.swing.JComboBox jcboYearTo;
    private javax.swing.JButton jcmdCancel;
    private javax.swing.JButton jcmdOK;
    private com.openbravo.editor.JEditorCurrency jtxtMoney;
    private com.openbravo.editor.JEditorIntegerPositive jtxtTicketID;
    private com.openbravo.editor.JEditorKeys m_jKeys;
    // End of variables declaration//GEN-END:variables
}
