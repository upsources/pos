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

import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.StringReader;
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
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppUser;
import com.openbravo.pos.util.ThumbNailBuilder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class JPanelButtons extends javax.swing.JPanel {
    
    private static SAXParser m_sp = null;
    
    private Properties props;
    private Map<String, String> events;
    
    private ThumbNailBuilder tnbmacro;
    
    private JPanelTicket.ScriptObject scriptobject;
    
    /** Creates new form JPanelButtons */
    public JPanelButtons(String sConfigKey, JPanelTicket.ScriptObject scriptobject) {
        initComponents();
        
        // Load categories default thumbnail
        tnbmacro = new ThumbNailBuilder(16, 16, "com/openbravo/images/greenled.png");
        
        this.scriptobject = scriptobject;
        
        props = new Properties();
        events = new HashMap<String, String>();
        
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
    
    public String getProperty(String key) {
        return props.getProperty(key);
    }
    
     public String getProperty(String key, String defaultvalue) {
        return props.getProperty(key, defaultvalue);
    }
     
    public String getEvent(String key) {
        return events.get(key);
    }
    
    private class ConfigurationHandler extends DefaultHandler {       
        @Override
        public void startDocument() throws SAXException {}
        @Override
        public void endDocument() throws SAXException {}    
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
            if ("button".equals(qName)){
                String stemplate = attributes.getValue("template");
                add(new JButtonFunc(
                        attributes.getValue("key"), 
                        attributes.getValue("image"), 
                        attributes.getValue("name"),  
                        stemplate == null
                            ? scriptobject.getResourceAsXML(attributes.getValue("code"))
                            : "sales.printTicket(\"" + stemplate + "\");"));
            } else if ("event".equals(qName)) {
                events.put(attributes.getValue("key"), scriptobject.getResourceAsXML(attributes.getValue("code")));
            } else {
                String value = attributes.getValue("value");
                if (value != null) {                  
                    props.setProperty(qName, attributes.getValue("value"));
                }
            }
        }      
        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {}
        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {}
    }  
        
    private class JButtonFunc extends JButton {
        private String m_sCode;
        
        public JButtonFunc(String sKey, String sImage, String sKeyText, String sCode) {
            
            m_sCode = sCode;
            setName(sKey);
            setText(AppLocal.getIntString(sKeyText));
            setIcon(new ImageIcon(tnbmacro.getThumbNail(scriptobject.getResourceAsImage(sImage))));
            setFocusPainted(false);
            setFocusable(false);
            setRequestFocusEnabled(false);
            setMargin(new Insets(8, 14, 8, 14));
  
            addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {

                    if (m_sCode == null) {
                        MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotexecute"));
                        msg.show(JPanelButtons.this);
                    } else {
                        try {
                            scriptobject.evalScript(m_sCode);
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
