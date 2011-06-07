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

package com.openbravo.data.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import com.openbravo.data.loader.LocalRes;

public class JLittleImageEditor extends javax.swing.JPanel {
    private Dimension m_maxsize;
    private BufferedImage m_Img = null;
    
    private static File m_fCurrentDirectory = null;
    
    /** Creates new form JImageEditor */
    public JLittleImageEditor() {
        initComponents();
        
        m_Img = null;
        m_maxsize = null;
        privateSetEnabled(isEnabled());
    }
    
    public void setMaxDimensions(Dimension size) {
        m_maxsize = size;
    }
    public Dimension getMaxDimensions() {
        return m_maxsize;
    }
    
    @Override
    public void setEnabled(boolean value) {

        privateSetEnabled(value);
        super.setEnabled(value);
    }
    
    private void privateSetEnabled(boolean value) {
        m_jbtnopen.setEnabled(value);
        m_jbtnclose.setEnabled(value && (m_Img != null));
    }
    
    public void setImage(BufferedImage img) {
        BufferedImage oldimg = m_Img;
        m_Img = img;

        privateSetEnabled(isEnabled());
        
        firePropertyChange("image", oldimg, m_Img);
    }
    
    public BufferedImage getImage() {
        return m_Img;
    }    
    
    public void doLoad() {
        JFileChooser fc = new JFileChooser(m_fCurrentDirectory);
        
        fc.addChoosableFileFilter(new ExtensionsFilter(LocalRes.getIntString("label.imagefiles"), "png", "gif", "jpg", "jpeg", "bmp"));

        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {  
            try {
                BufferedImage img = ImageIO.read(fc.getSelectedFile());
                if (img != null) {
                    // compruebo que no exceda el tamano maximo.
                    if (m_maxsize != null && (img.getHeight() > m_maxsize.height || img.getWidth() > m_maxsize.width)) {
                        if (JOptionPane.showConfirmDialog(this, LocalRes.getIntString("message.resizeimage"), LocalRes.getIntString("title.editor"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {          
                            // Redimensionamos la imagen para que se ajuste
                            img = resizeImage(img);
                        }                        
                    }
                    setImage(img);
                    m_fCurrentDirectory = fc.getCurrentDirectory();
                }
            } catch (IOException eIO) {
            }
        }
    }
    
    private BufferedImage resizeImage(BufferedImage img) {
        
        int myheight = img.getHeight();
        int mywidth = img.getWidth();
        
        if (myheight > m_maxsize.height) {
            mywidth = (int) (mywidth * m_maxsize.height / myheight); 
            myheight = m_maxsize.height;
        }
        if (mywidth > m_maxsize.width) {
            myheight = (int) (myheight * m_maxsize.width / mywidth);
            mywidth = m_maxsize.width;
        }

        BufferedImage thumb = new BufferedImage(mywidth, myheight, BufferedImage.TYPE_4BYTE_ABGR);

        double scalex = (double) mywidth / (double) img.getWidth(null);
        double scaley = (double) myheight / (double) img.getHeight(null);

        Graphics2D g2d = thumb.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        g2d.setColor(new Color(0, 0, 0, 0)); // Transparent

        g2d.fillRect(0, 0, mywidth, myheight);
        if (scalex < scaley) {
            g2d.drawImage(img, 0,(int) ((myheight - img.getHeight(null) * scalex) / 2.0)
            , mywidth, (int) (img.getHeight(null) * scalex),  null);
        } else {
           g2d.drawImage(img, (int) ((mywidth - img.getWidth(null) * scaley) / 2.0), 0
           , (int) (img.getWidth(null) * scaley), myheight, null);
        }
        g2d.dispose(); 
        
        return thumb;
    }

    private static class ExtensionsFilter extends FileFilter {
        
        private String message;
        private String[] extensions;
        
        public ExtensionsFilter(String message, String... extensions) {
            this.message = message;
            this.extensions = extensions;            
        }
        
        public boolean accept(java.io.File f) {
            if (f.isDirectory()) {
                return true;
            } else {
                String sFileName = f.getName();
                int ipos = sFileName.lastIndexOf('.');
                if (ipos >= 0) {
                    String sExt = sFileName.substring(ipos + 1);
                    for(String s : extensions) {
                        if (s.equalsIgnoreCase(sExt)) {
                            return true;
                        }
                    }
                }                        
                return false;
            }   
        }
        
        public String getDescription() {
            return message;
        }      
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        m_jbtnopen = new javax.swing.JButton();
        m_jbtnclose = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(100, 33));
        setLayout(new java.awt.BorderLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jPanel2.setMinimumSize(new java.awt.Dimension(70, 30));
        jPanel2.setPreferredSize(new java.awt.Dimension(70, 30));
        jPanel2.setLayout(new java.awt.GridLayout(1, 0, 8, 0));

        m_jbtnopen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/fileopen.png"))); // NOI18N
        m_jbtnopen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jbtnopenActionPerformed(evt);
            }
        });
        jPanel2.add(m_jbtnopen);

        m_jbtnclose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/fileclose.png"))); // NOI18N
        m_jbtnclose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jbtncloseActionPerformed(evt);
            }
        });
        jPanel2.add(m_jbtnclose);

        add(jPanel2, java.awt.BorderLayout.CENTER);

        getAccessibleContext().setAccessibleParent(this);
    }// </editor-fold>//GEN-END:initComponents

    private void m_jbtncloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jbtncloseActionPerformed
        
        setImage(null);
        
    }//GEN-LAST:event_m_jbtncloseActionPerformed

    private void m_jbtnopenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jbtnopenActionPerformed
        
        doLoad();
        
    }//GEN-LAST:event_m_jbtnopenActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton m_jbtnclose;
    private javax.swing.JButton m_jbtnopen;
    // End of variables declaration//GEN-END:variables
    
}
