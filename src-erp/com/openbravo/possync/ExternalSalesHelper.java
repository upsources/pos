//    Openbravo POS is a point of sales application designed for touch screens.
//    http://sourceforge.net/projects/
//
//    Copyright (c) 2007 openTrends Solucions i Sistemes, S.L
//    Modified by Openbravo SL on March 22, 2007
//    These modifications are copyright Openbravo SL
//    Author/s: A. Romero
//    You may contact Openbravo SL at: http://www.openbravo.com
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

package com.openbravo.possync;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import com.openbravo.basic.BasicException;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.util.AltEncrypter;
import com.openbravo.pos.util.Base64Encoder;
import net.opentrends.openbravo.ws.types.ExternalSalesImpl;
import net.opentrends.openbravo.ws.types.ExternalSalesImplServiceLocator;
import net.opentrends.openbravo.ws.types.Order;
import net.opentrends.openbravo.ws.types.Product;

public class ExternalSalesHelper {
    
    private ExternalSalesImpl externalSales;
    
    private String m_sERPUser;
    private String m_sERPPassword;
    private int m_iERPId;
    private int m_iERPOrg;
    private int m_iERPPos;
    
    /** Creates a new instance of WebServiceHelper */
    public ExternalSalesHelper(DataLogicSystem dlsystem) throws BasicException, ServiceException, MalformedURLException {
        
        
        Properties prop = dlsystem.getResourceAsProperties("openbravo.properties");
        if (prop == null) {
            throw new BasicException(AppLocal.getIntString("message.propsnotdefined"));            
        } else {
            String url = prop.getProperty("url");
            if (url == null || url.equals("")) {
                throw new BasicException(AppLocal.getIntString("message.urlnotdefined"));
            } else {
                externalSales = new ExternalSalesImplServiceLocator().getExternalSales(new URL(url));

                m_sERPUser = prop.getProperty("user");
                m_sERPPassword = prop.getProperty("password");        
                if (m_sERPUser != null && m_sERPPassword != null && m_sERPPassword.startsWith("crypt:")) {
                    // La clave esta encriptada.
                    AltEncrypter cypher = new AltEncrypter("key" + m_sERPUser);
                    m_sERPPassword = cypher.decrypt(m_sERPPassword.substring(6));
                } 
                m_sERPPassword = getPasswordHash(m_sERPPassword);
                m_iERPId = Integer.parseInt(prop.getProperty("id"));
                m_iERPOrg = Integer.parseInt(prop.getProperty("org"));
                m_iERPPos = Integer.parseInt(prop.getProperty("pos"));
            }
        }
    }
    
    public Product[] getProductsCatalog() throws RemoteException {
        return externalSales.getProductsCatalog(m_iERPId, m_iERPOrg, m_iERPPos, m_sERPUser, m_sERPPassword);
    }
    
    public void uploadOrders(Order[] orderstoupload) throws RemoteException {
        externalSales.uploadOrders(m_iERPId, m_iERPOrg, m_iERPPos, orderstoupload, m_sERPUser, m_sERPPassword);
    }
       
    private static String getPasswordHash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte raw[] = md.digest(password.getBytes("UTF-8"));
            return Base64Encoder.encode(raw);
        } catch (NoSuchAlgorithmException e) {
            return null; // never happens :-)
        } catch (UnsupportedEncodingException e) {
            return null; // never happens :-)
        }
    }     
}
