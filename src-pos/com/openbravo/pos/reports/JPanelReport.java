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

package com.openbravo.pos.reports;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.io.*;

import com.openbravo.pos.forms.JPanelView;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.AppLocal;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.engine.design.*;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.BaseSentence;
import com.openbravo.data.user.EditorCreator;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;

public abstract class JPanelReport extends JPanel implements JPanelView, BeanFactoryApp   {
    
    private JRViewerMod reportviewer = null;   
    private JasperReport jr = null;
    private EditorCreator editor = null;
            
    protected AppView m_App;


    /** Creates new form JPanelReport */
    public JPanelReport() {
        
        initComponents();      
    }
    
    public void init(AppView app) throws BeanFactoryException {   
        
        m_App = app;
        
        editor = createEditorCreator();
        if (editor instanceof JPanel) {
            jPanelHeader.add((JPanel) editor, BorderLayout.CENTER);
        }
        
        try {            
            reportviewer = new JRViewerMod();            
            // reportviewer = new JRViewer(null);          
            
            add(reportviewer, BorderLayout.CENTER);
        } catch (JRException e) {
        }        
        
        try {     
            
            InputStream in = getClass().getResourceAsStream(getReport() + ".ser");
            if (in == null) {      
                // read and compile the report
                JasperDesign jd = JRXmlLoader.load(getClass().getResourceAsStream(getReport() + ".jrxml"));            
                jr = JasperCompileManager.compileReport(jd);    
            } else {
                // read the compiled report
                ObjectInputStream oin = new ObjectInputStream(in);
                jr = (JasperReport) oin.readObject();
                oin.close();
            }
        } catch (Exception e) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotloadreport"), e);
            msg.show(this);
            jr = null;
        }  
    }
    
    public Object getBean() {
        return this;
    }
    
    protected abstract String getReport();
    protected abstract String getResourceBundle();
    protected abstract BaseSentence getSentence();
    protected abstract ReportFields getReportFields();
    protected EditorCreator createEditorCreator() {
        return null;
    }

    public JComponent getComponent() {
        return this;
    }
    
    public void activate() throws BasicException {
    }    
    
    public boolean deactivate() {
        try {     
            reportviewer.loadJasperPrint(null);
        } catch (JRException e) {
        }
        return true;
    }
    
    private void launchreport() {     
        
        m_App.waitCursorBegin();
        
        if (jr != null) {
            try {     
                
                // Archivo de recursos
                String res = getResourceBundle();  
                
                // Parametros y los datos
                Object params = (editor == null) ? null : editor.createValue();                
                JRDataSource data = new JRDataSourceBasic(getSentence(), getReportFields(), params);
                
                // Construyo el mapa de los parametros.
                Map reportparams = new HashMap();
                reportparams.put("ARG", params);
                if (res != null) {
//                    LocaleResources lr = new LocaleResources();
//                    reportparams.put("REPORT_RESOURCE_BUNDLE", lr.getBundle(res));
                      reportparams.put("REPORT_RESOURCE_BUNDLE", ResourceBundle.getBundle(res));
                }
                
                JasperPrint jp = JasperFillManager.fillReport(jr, reportparams, data);    
            
                reportviewer.loadJasperPrint(jp);               
                
            } catch (MissingResourceException e) {    
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotloadresourcedata"), e);
                msg.show(this);
            } catch (JRException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotfillreport"), e);
                msg.show(this);
            } catch (BasicException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotloadreportdata"), e);
                msg.show(this);
            }
        }
        
        m_App.waitCursorEnd();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanelHeader = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(5, 5, 5, 5)));
        jPanelHeader.setLayout(new java.awt.BorderLayout());

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/launch.png")));
        jButton1.setText(AppLocal.getIntString("Button.ExecuteReport"));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel1.add(jButton1);

        jPanelHeader.add(jPanel1, java.awt.BorderLayout.SOUTH);

        add(jPanelHeader, java.awt.BorderLayout.NORTH);

    }
    // </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        launchreport();
    }//GEN-LAST:event_jButton1ActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelHeader;
    // End of variables declaration//GEN-END:variables
    
}
