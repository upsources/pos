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

package com.openbravo.pos.sales;

import java.awt.Component;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.StringReader;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppUser;
import com.openbravo.pos.util.ThumbNailBuilder;


public class JPanelButtons extends javax.swing.JPanel {
    
    private static SAXParser m_sp = null;

    private Integer m_itaxesid = null;
    private boolean m_btaxesincluded = false;
    private boolean m_bpricevisible = false;
    
    private ThumbNailBuilder tnbmacro;
    
    private JPanelTicket.ScriptObject m_TicketScript;
    
    /** Creates new form JPanelButtons */
    public JPanelButtons(String sConfigKey, JPanelTicket.ScriptObject scriptobject) {
        initComponents();
        
        // Load categories default thumbnail
        Image defimg;
        try {
            defimg = ImageIO.read(getClass().getClassLoader().getResourceAsStream("com/openbravo/images/greenled.png"));               
        } catch (Exception fnfe) {
            defimg = null;
        } 
        tnbmacro = new ThumbNailBuilder(16, 16, defimg);
        
        m_TicketScript = scriptobject;
        String sConfigRes = scriptobject.getResourceAsXML(sConfigKey);
        
        if (sConfigRes != null) {
            try {
                if (m_sp == null) {
                    SAXParserFactory spf = SAXParserFactory.newInstance();
                    m_sp = spf.newSAXParser();
                }
                m_sp.parse(new InputSource(new StringReader(sConfigRes)), new ConfigurationHandler());

            } catch (ParserConfigurationException ePC) {
                System.out.println("Error en el analizador XML. Consulte con su administrador");
            } catch (SAXException eSAX) {
                System.out.println("El archivo no es un documento XML valido. Error de analisis.");
            } catch (IOException eIO) {
                System.out.println("Error al leer el archivo. Consulte con su administrador.");
            }
        }     
    
    }
    
    public void setPermissions(AppUser user) {
        for (Component c : this.getComponents()) {
            String sKey = c.getName();
            if (sKey == null || sKey.equals("")) {
                c.setEnabled(true);
            } else {
                c.setEnabled(user.hasPermission(c.getName()));
            }
        }
    }
    
    private class ConfigurationHandler extends DefaultHandler {       
        public void startDocument() throws SAXException {}
        public void endDocument() throws SAXException {}    
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
            if ("button".equals(qName)){
                String stemplate = attributes.getValue("template");
                add(new JButtonFunc(
                        attributes.getValue("key"), 
                        attributes.getValue("image"), 
                        attributes.getValue("name"),  
                        stemplate == null
                            ? m_TicketScript.getResourceAsXML(attributes.getValue("code"))
                            : "sales.printTicket(\"" + stemplate + "\");"));
            } else if ("taxesincluded".equals(qName)) {
                m_btaxesincluded = "true".equals(attributes.getValue("value"));
            } else if ("pricevisible".equals(qName)) {
                m_bpricevisible = "true".equals(attributes.getValue("value"));
            } else if ("taxesid".equals(qName)) {
                try {
                    m_itaxesid = Integer.valueOf(attributes.getValue("value"));
                } catch (NumberFormatException e ) {
                    m_itaxesid = null;
                }
            }
        }      
        public void endElement(String uri, String localName, String qName) throws SAXException {}
        public void characters(char[] ch, int start, int length) throws SAXException {}
    }  
    
    public boolean isPriceVisible() {
        return m_bpricevisible;
    }
    
    public boolean isTaxesIncluded() {
        return m_btaxesincluded;
    }
    
    public Integer getTaxesID() {
        return m_itaxesid;
    }
        
    private class JButtonFunc extends JButton {
        private String m_sCode;
        
        public JButtonFunc(String sKey, String sImage, String sKeyText, String sCode) {
            
            m_sCode = sCode;
            setName(sKey);
            setText(AppLocal.getIntString(sKeyText));
            setIcon(new ImageIcon(tnbmacro.getThumbNail(m_TicketScript.getResourceAsImage(sImage))));
            setFocusPainted(false);
            setFocusable(false);
            setRequestFocusEnabled(false);
            setMargin(new Insets(8, 14, 8, 14));
  
            addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
//                    String sresource = m_TicketScript.getResourceAsXML(m_sCode);
                    if (m_sCode == null) {
                        MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotexecute"));
                        msg.show(JPanelButtons.this);
                    } else {
                        try {
                            ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.BEANSHELL);
                            script.put("sales", m_TicketScript);
                            script.eval(m_sCode);
                        } catch (ScriptException e) {
                            MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.cannotexecute"), e);
                            msg.show(JPanelButtons.this);
                        }
                    }
                }
            });
        }         
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
}
