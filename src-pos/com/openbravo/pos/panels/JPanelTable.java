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

package com.openbravo.pos.panels;

import java.awt.*;
import javax.swing.*;
import com.openbravo.pos.forms.JPanelView;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.data.loader.Vectorer;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.JSaver;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.gui.JCounter;
import com.openbravo.data.gui.JLabelDirty;
import com.openbravo.data.gui.JListNavigator;
import com.openbravo.data.gui.JNavigator;
import com.openbravo.data.loader.ComparatorCreator;
import com.openbravo.data.user.BrowsableEditableData;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.data.user.ListProvider;
import com.openbravo.data.user.SaveProvider;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;

/**
 *
 * @author adrianromero
 */
public abstract class JPanelTable extends JPanel implements JPanelView, BeanFactoryApp {
    
    protected BrowsableEditableData bd;    
    protected DirtyManager dirty;    
    protected AppView app;
    
    /** Creates new form JPanelTableEditor */
    public JPanelTable() {

        initComponents();
    }
    
    public void init(AppView app) throws BeanFactoryException {
        
        this.app = app;
        dirty = new DirtyManager();
        bd = null;
        
        init();
    }

    public Object getBean() {
        return this;
    }
    
    private void startNavigation() {
        
        if (bd == null) {
            
            // init browsable editable data
            bd = new BrowsableEditableData(getListProvider(), getSaveProvider(), getEditor(), dirty);

            // Add the filter panel
            Component c = getFilter();
            if (c != null) {
                add(c, BorderLayout.NORTH);
            }

            // Add the editor
            c = getEditor().getComponent();
            if (c != null) {
                m_jContEditor.add(c, BorderLayout.CENTER);            
            }

            // el panel este
            ListCellRenderer cr = getListCellRenderer();
            if (cr != null) {
                JListNavigator nl = new JListNavigator(bd);
                if (cr != null) nl.setCellRenderer(cr);
                m_jContEditor.add(nl, java.awt.BorderLayout.WEST);
            }

            // add toolbar extras
            c = getToolbarExtras();
            if (c != null) {
                m_jToolbar.add(c);
            }

            // La Toolbar
            m_jToolbar.add(new JLabelDirty(dirty));
            m_jToolbar.add(new JCounter(bd));
            m_jToolbar.add(new JNavigator(bd, getVectorer(), getComparatorCreator()));
            m_jToolbar.add(new JSaver(bd));
        }
    }
    
    public Component getToolbarExtras() {
        return null;
    }

    public Component getFilter() {    
        return null;
    }
    
    protected abstract void init();
    
    public abstract EditorRecord getEditor();
    
    public abstract ListProvider getListProvider();
    
    public abstract SaveProvider getSaveProvider();
    
    public Vectorer getVectorer() {
        return null;
    }
    
    public ComparatorCreator getComparatorCreator() {
        return null;
    }
    
    public ListCellRenderer getListCellRenderer() {
        return null;
    }

    public JComponent getComponent() {
        return this;
    }

    public void activate() throws BasicException {
        startNavigation();
        bd.actionLoad();
    }    
    
    public boolean deactivate() {

        try {
            return bd.actionClosingForm(this);
        } catch (BasicException eD) {
            MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.CannotMove"), eD);
            msg.show(this);
            return false;
        }
    }  
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel2 = new javax.swing.JPanel();
        m_jContEditor = new javax.swing.JPanel();
        m_jToolbar = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(5, 5, 5, 5)));
        jPanel2.setLayout(new java.awt.BorderLayout());

        m_jContEditor.setLayout(new java.awt.BorderLayout());

        m_jContEditor.add(m_jToolbar, java.awt.BorderLayout.NORTH);

        jPanel2.add(m_jContEditor, java.awt.BorderLayout.CENTER);

        add(jPanel2, java.awt.BorderLayout.CENTER);

    }
    // </editor-fold>//GEN-END:initComponents
  
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel m_jContEditor;
    private javax.swing.JPanel m_jToolbar;
    // End of variables declaration//GEN-END:variables
    
}
