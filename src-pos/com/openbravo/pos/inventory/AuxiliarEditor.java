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
//    Foundation, Inc., 51 Franklin Street, Fifth floor, Boston, MA  02110-1301  USA

package com.openbravo.pos.inventory;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.panels.AuxiliarFilter;
import com.openbravo.pos.ticket.ProductInfoExt;
import java.awt.Component;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.panels.JProductFinder;
import java.awt.Toolkit;


/**
 *
 * @author jaroslawwozniak
 */
public class AuxiliarEditor extends javax.swing.JPanel implements EditorRecord {

    private ProductInfoExt m_auxiliar;
    private AuxiliarFilter m_filter;
    private SentenceList auxSent;
    private ComboBoxValModel auxModel;
    DataLogicSales m_dlSales;
    private AuxiliarPanel m_panel;

    /** Creates new form AuxiliarEditor */
    public AuxiliarEditor(AppView app, DirtyManager dirty, AuxiliarFilter filter) {
        m_filter = filter;
        m_dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");

        initComponents();
     
        m_jProduct.getDocument().addDocumentListener(dirty);
    }

    public void activate() throws BasicException{
    }

    @Override
    public void refresh() {
        m_jProductLabel.setText(m_filter.getM_product().getName());
    }

    @Override
    public void writeValueEOF() {
        m_jBarcode.setText(null);
        getM_jReference().setText(null);
        m_jProduct.setText(null);

        m_jBarcode.setEnabled(false);
        m_jProduct.setEnabled(false);
    }

    @Override
    public void writeValueInsert() {
        m_jBarcode.setText(null);
        getM_jReference().setText(null);
        m_jProduct.setText(null);

        m_jBarcode.setEnabled(true);
        m_jProduct.setEnabled(true);
    }

    @Override
    public void writeValueEdit(Object value) {
        Object[] object = (Object[]) value;
        getM_jReference().setText(Formats.STRING.formatValue(object[0]));
        this.refresh();
        m_jBarcode.setText(getBarcodeByReference(Formats.STRING.formatValue(object[0])));
        m_jProduct.setText(getNameById(Formats.STRING.formatValue(object[1])));

        m_jBarcode.setEnabled(true);
        m_jProduct.setEnabled(true);
    }

    @Override
    public void writeValueDelete(Object value) {
        Object[] object = (Object[]) value;

        m_jBarcode.setText(null);
        getM_jReference().setText(null);
        m_jProduct.setText(null);

        m_jBarcode.setEnabled(false);
        m_jProduct.setEnabled(false);
    }

    @Override
    public Object createValue() throws BasicException {
        return  new Object[] {
            m_filter.getM_product().getID(),
            m_auxiliar.getID()
        };
    }

    @Override
    public Component getComponent() {
        return this;
    }

    private void assignProduct(ProductInfoExt prod) {

        if (m_jSearch.isEnabled()) {
            if (prod == null) {
                m_jProduct.setText(null);
                m_jBarcode.setText(null);
                getM_jReference().setText(null);
            } else {
                m_auxiliar = prod;
                m_jProduct.setText(m_auxiliar.getName());
                m_jBarcode.setText(m_auxiliar.getCode());
                getM_jReference().setText(m_auxiliar.getReference());

            }
        }

    }

    private void assignProductByCode() {
        try {
            ProductInfoExt oProduct = m_dlSales.getProductInfoByCode(m_jBarcode.getText());
            if (oProduct == null) {
                assignProduct(null);
                Toolkit.getDefaultToolkit().beep();
            } else {
               // Se anade directamente una unidad con el precio y todo
                    assignProduct(oProduct);


            }
        } catch (BasicException eData) {
            assignProduct(null);
            MessageInf msg = new MessageInf(eData);
            msg.show(this);
        }
    }


    private void assignProductByReference() {
        try {
            ProductInfoExt oProduct = m_dlSales.getProductInfoByReference(getM_jReference().getText());
            if (oProduct == null) {
                assignProduct(null);
                Toolkit.getDefaultToolkit().beep();
            } else {
                // Se anade directamente una unidad con el precio y todo
                assignProduct(oProduct);
            }
        } catch (BasicException eData) {
            assignProduct(null);
            MessageInf msg = new MessageInf(eData);
            msg.show(this);
        }
    }

    private String getBarcodeByReference(String ref){
        try {
            ProductInfoExt oProduct = m_dlSales.getProductInfoByReference(getM_jReference().getText());
            if (oProduct == null) {
                assignProduct(null);
                Toolkit.getDefaultToolkit().beep();
            } else {
                // Se anade directamente una unidad con el precio y todo
                return oProduct.getCode();
            }
        } catch (BasicException eData) {
            assignProduct(null);
            MessageInf msg = new MessageInf(eData);
            msg.show(this);
        }
        return null;
    }

    private String getNameById(String id){
     try {
            ProductInfoExt oProduct = m_dlSales.getProductInfoByReference(getM_jReference().getText());
            if (oProduct == null) {
                assignProduct(null);
                Toolkit.getDefaultToolkit().beep();
            } else {
                // Se anade directamente una unidad con el precio y todo
                return oProduct.getName();
            }
        } catch (BasicException eData) {
            assignProduct(null);
            MessageInf msg = new MessageInf(eData);
            msg.show(this);
        }
        return null;
    }

    /**
     * @return the m_jReference
     */
    public javax.swing.JTextField getM_jReference() {
        return m_jReference;
    }

    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        m_jProductLabel = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        m_jReference = new javax.swing.JTextField();
        m_jBarcode = new javax.swing.JTextField();
        m_jEnter2 = new javax.swing.JButton();
        m_jSearch = new javax.swing.JButton();
        m_jEnter1 = new javax.swing.JButton();
        m_jProduct = new javax.swing.JTextField();

        m_jProductLabel.setFont(new java.awt.Font("SansSerif", 3, 18)); // NOI18N
        m_jProductLabel.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                m_jProductLabelPropertyChange(evt);
            }
        });

        jLabel2.setText(AppLocal.getIntString("label.stockproduct")); // NOI18N

        jLabel3.setText(AppLocal.getIntString("label.prodref")); // NOI18N

        jLabel4.setText(AppLocal.getIntString("label.prodbarcode")); // NOI18N

        m_jReference.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jReferenceActionPerformed(evt);
            }
        });

        m_jBarcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jBarcodeActionPerformed(evt);
            }
        });

        m_jEnter2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/apply.png"))); // NOI18N
        m_jEnter2.setMaximumSize(new java.awt.Dimension(50, 26));
        m_jEnter2.setMinimumSize(new java.awt.Dimension(50, 26));
        m_jEnter2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jEnter2ActionPerformed(evt);
            }
        });

        m_jSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/search.png"))); // NOI18N
        m_jSearch.setMaximumSize(new java.awt.Dimension(50, 26));
        m_jSearch.setMinimumSize(new java.awt.Dimension(50, 26));
        m_jSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jSearchActionPerformed(evt);
            }
        });

        m_jEnter1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/apply.png"))); // NOI18N
        m_jEnter1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jEnter1ActionPerformed(evt);
            }
        });

        m_jProduct.setEditable(false);
        m_jProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jProductActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(126, 126, 126)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(36, 36, 36)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(m_jReference)
                            .addComponent(m_jBarcode, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)))
                    .addComponent(m_jProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(m_jSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(m_jEnter2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(m_jEnter1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(jLabel3)
                        .addComponent(m_jReference, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(m_jEnter1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(m_jBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(m_jEnter2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(m_jSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(m_jProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(38, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addComponent(m_jProductLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 563, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(m_jProductLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(192, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(682, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void m_jSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jSearchActionPerformed
        assignProduct(JProductFinder.showMessage(this, m_dlSales, true));
}//GEN-LAST:event_m_jSearchActionPerformed

    private void m_jReferenceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jReferenceActionPerformed
        this.assignProductByReference();
    }//GEN-LAST:event_m_jReferenceActionPerformed

    private void m_jProductLabelPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_m_jProductLabelPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_m_jProductLabelPropertyChange

    private void m_jEnter2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jEnter2ActionPerformed
        this.assignProductByCode();
    }//GEN-LAST:event_m_jEnter2ActionPerformed

    private void m_jEnter1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jEnter1ActionPerformed
        this.assignProductByReference();
    }//GEN-LAST:event_m_jEnter1ActionPerformed

    private void m_jBarcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBarcodeActionPerformed
        this.assignProductByCode();
    }//GEN-LAST:event_m_jBarcodeActionPerformed

    private void m_jProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jProductActionPerformed

    }//GEN-LAST:event_m_jProductActionPerformed

  


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField m_jBarcode;
    private javax.swing.JButton m_jEnter1;
    private javax.swing.JButton m_jEnter2;
    private javax.swing.JTextField m_jProduct;
    private javax.swing.JLabel m_jProductLabel;
    private javax.swing.JTextField m_jReference;
    private javax.swing.JButton m_jSearch;
    // End of variables declaration//GEN-END:variables

}
