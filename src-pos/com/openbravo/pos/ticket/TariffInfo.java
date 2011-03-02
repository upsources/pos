//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2008 Open Sistemas de Información Internet, S.L.
//    http://www.opensistemas.com
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

package com.openbravo.pos.ticket;

import com.openbravo.data.loader.DataWrite;
import java.util.*;
import java.io.*;
import com.openbravo.pos.util.*;
import com.openbravo.data.loader.IKeyed;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;
import com.openbravo.basic.BasicException;

/**
 *
 * @author  Luis Ig. Bacas Riveiro	lbacas@opensistemas.com
 * @author  Pablo J. Urbano Santos	purbano@opensistemas.com
 */
public class TariffInfo implements Serializable, SerializableRead, SerializableWrite, IKeyed {
    
    private String m_sId;
    private String m_sName;
    private int m_iOrder;
    
    
    /** Creates new TariffInfo */
    public TariffInfo() {
        this(UUID.randomUUID().toString(), null);
    }
    
    public TariffInfo(String id, String name) {
        m_sId = id;
        m_sName = name;
        m_iOrder = 0;
    }
    
    public Object getKey() {
        return m_sId;
    }

    public void readValues(DataRead dr) throws BasicException {
        m_sId = dr.getString(1);
        m_sName = dr.getString(2);
        m_iOrder = dr.getInt(3).intValue();
    }

    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, m_sId);
        dp.setString(2, m_sName);
        dp.setInt(3, m_iOrder);
    }
        
    public String getID() {
        return m_sId;
    }
    
    
    public String getName() {
        return m_sName;
    }
    public void setName(String sName) {
        m_sName = sName;
    }
    
    
    public int getOrder() {
        return m_iOrder;
    }
    public void setOrder (int iOrder) {
        m_iOrder = iOrder;
    }
    
    public String toString(){
        return m_sName;
    }
}
    
