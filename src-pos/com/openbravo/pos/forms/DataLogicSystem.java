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

package com.openbravo.pos.forms;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import javax.imageio.ImageIO;
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.*;
import com.openbravo.format.Formats;
import com.openbravo.pos.util.ThumbNailBuilder;
import javax.swing.ImageIcon;

/**
 *
 * @author adrianromero
 */
public class DataLogicSystem extends BeanFactoryDataSingle {
    
    protected String m_sInitScript;
    private SentenceFind m_version;    
    private SentenceFind m_libreposversion;    
    private SentenceExec m_dummy;
    
    protected SentenceList m_peoplevisible;  
    protected SentenceFind m_peoplebycard;  
    protected SerializerRead peopleread;
    
    private SentenceFind m_rolepermissions; 
    private SentenceExec m_changepassword;    
    private SentenceFind m_locationfind;
    
    private SentenceFind m_resourcebytes;
    private SentenceExec m_resourcebytesinsert;
    private SentenceExec m_resourcebytesupdate;

    protected SentenceFind m_activecash;
    protected SentenceExec m_insertcash;
    
    /** Creates a new instance of DataLogicSystem */
    public DataLogicSystem() {            
    }
    
    public void init(Session s){
        
        m_version = new PreparedSentence(s, "SELECT VERSION FROM APPLICATIONS WHERE ID = ?", SerializerWriteString.INSTANCE, SerializerReadString.INSTANCE);
        m_libreposversion = new PreparedSentence(s, "SELECT VERSION FROM LIBREPOS", null, SerializerReadString.INSTANCE);
        m_dummy = new StaticSentence(s, "SELECT * FROM PEOPLE WHERE 1 = 0");
         
        final ThumbNailBuilder tnb = new ThumbNailBuilder(32, 32, "com/openbravo/images/yast_sysadmin.png");        
        peopleread = new SerializerRead() {
            public Object readValues(DataRead dr) throws BasicException {
                return new AppUser(
                        dr.getString(1),
                        dr.getString(2),
                        dr.getString(3),
                        dr.getString(4),
                        dr.getString(5),
                        new ImageIcon(tnb.getThumbNail(ImageUtils.readImage(dr.getBytes(6)))));                
            }
        };
         
        m_resourcebytes = new PreparedSentence(s
            , "SELECT CONTENT FROM RESOURCES WHERE NAME = ?"
            , SerializerWriteString.INSTANCE
            , SerializerReadBytes.INSTANCE);
        
        Datas[] resourcedata = new Datas[] {Datas.STRING, Datas.STRING, Datas.INT, Datas.BYTES};
        m_resourcebytesinsert = new PreparedSentence(s
                , "INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES (?, ?, ?, ?)"
                , new SerializerWriteBasic(resourcedata));
        m_resourcebytesupdate = new PreparedSentence(s
                , "UPDATE RESOURCES SET NAME = ?, RESTYPE = ?, CONTENT = ? WHERE NAME = ?"
                , new SerializerWriteBasicExt(resourcedata, new int[] {1, 2, 3, 1}));
        
        m_rolepermissions = new PreparedSentence(s
                , "SELECT PERMISSIONS FROM ROLES WHERE ID = ?"
            , SerializerWriteString.INSTANCE
            , SerializerReadBytes.INSTANCE);     
        
        m_changepassword = new StaticSentence(s
                , "UPDATE PEOPLE SET APPPASSWORD = ? WHERE ID = ?"
                ,new SerializerWriteBasic(new Datas[] {Datas.STRING, Datas.STRING}));
        
        m_activecash = new StaticSentence(s
            , "SELECT HOST, DATESTART, DATEEND FROM CLOSEDCASH WHERE MONEY = ?"
            , SerializerWriteString.INSTANCE
            , new SerializerReadBasic(new Datas[] {Datas.STRING, Datas.TIMESTAMP, Datas.TIMESTAMP}));            
        m_insertcash = new StaticSentence(s
                , "INSERT INTO CLOSEDCASH(MONEY, HOST, DATESTART, DATEEND) VALUES (?, ?, ?, ?)"
                , new SerializerWriteBasic(new Datas[] {Datas.STRING, Datas.STRING, Datas.TIMESTAMP, Datas.TIMESTAMP}));
            
        m_locationfind = new StaticSentence(s
                , "SELECT NAME FROM LOCATIONS WHERE ID = ?"
                , SerializerWriteString.INSTANCE
                , SerializerReadString.INSTANCE);        
        
    }


    public String getInitScript() {
        return m_sInitScript;
    }
    
//    public abstract BaseSentence getShutdown();
    
    public final String findVersion() throws BasicException {
        return (String) m_version.find(AppLocal.APP_ID);
    }
    public final String findLibreposVersion() throws BasicException {
        return (String) m_libreposversion.find();
    }
    public final void execDummy() throws BasicException {
        m_dummy.exec();
    }
    public final List listPeopleVisible() throws BasicException {
        return m_peoplevisible.list();
    }      
    public final AppUser findPeopleByCard(String card) throws BasicException {
        return (AppUser) m_peoplebycard.find(card);
    }   
    
    public final String findRolePermissions(String sRole) {
        
        try {
            return Formats.BYTEA.formatValue(m_rolepermissions.find(sRole));        
        } catch (BasicException e) {
            return null;                    
        }             
    }
    
    public final void execChangePassword(Object[] userdata) throws BasicException {
        m_changepassword.exec(userdata);
    }
    
    private final byte[] getResource(String sName) {

        byte[] resource;
        
        // Primero trato de obtenerlo de la tabla de recursos
        try {
            resource = (byte[]) m_resourcebytes.find(sName);
        } catch (BasicException e) {
            resource = null;
        }
        
        return resource;
    }
    
    public final void setResource(String sName, int iType, byte[] data) {
        
        Object[] value = new Object[] {UUID.randomUUID().toString(), sName, new Integer(iType), data};
        try {
            if (m_resourcebytesupdate.exec(value) == 0) {
                m_resourcebytesinsert.exec(value);
            }
        } catch (BasicException e) {
        }
    }
    
    public final void setResourceAsBinary(String sName, byte[] data) {
        setResource(sName, 2, data);
    }
    
    public final byte[] getResourceAsBinary(String sName) {
        return getResource(sName);
    }
    
    public final String getResourceAsText(String sName) {
        return Formats.BYTEA.formatValue(getResource(sName));
    }
    
    public final String getResourceAsXML(String sName) {
        return Formats.BYTEA.formatValue(getResource(sName));
    }    
    
    public final BufferedImage getResourceAsImage(String sName) {
        try {
            byte[] img = getResource(sName); // , ".png"
            return img == null ? null : ImageIO.read(new ByteArrayInputStream(img));
        } catch (IOException e) {
            return null;
        }
    }
    
    public final void setResourceAsProperties(String sName, Properties p) {
        if (p == null) {
            setResource(sName, 0, null); // texto
        } else {
            try {
                ByteArrayOutputStream o = new ByteArrayOutputStream();
                p.storeToXML(o, AppLocal.APP_NAME, "UTF8");
                setResource(sName, 0, o.toByteArray()); // El texto de las propiedades   
            } catch (IOException e) { // no deberia pasar nunca
            }            
        }
    }
    
    public final Properties getResourceAsProperties(String sName) {
        
        Properties p = new Properties();
        try {
            byte[] img = getResourceAsBinary(sName);
            if (img != null) {
                p.loadFromXML(new ByteArrayInputStream(img));
            }
        } catch (IOException e) {
        }
        return p;
    }    

    public final Object[] findActiveCash(String sActiveCashIndex) throws BasicException {
        return (Object[]) m_activecash.find(sActiveCashIndex);
    }
    
    public final void execInsertCash(Object[] cash) throws BasicException {
        m_insertcash.exec(cash);
    } 
    
    public final String findLocationName(String iLocation) throws BasicException {
        return (String) m_locationfind.find(iLocation);
    }    
}
