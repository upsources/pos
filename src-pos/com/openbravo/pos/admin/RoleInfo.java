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

package com.openbravo.pos.admin;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.IKeyed;
import com.openbravo.data.loader.SerializableRead;

/**
 *
 * @author adrianromero
 * Created on 27 de febrero de 2007, 23:27
 *
 */
public class RoleInfo implements SerializableRead, IKeyed {
    
    private String m_sID;
    protected String m_sName;
    
    /** Creates a new instance of RoleInfo */
    public RoleInfo() {
        m_sID = null;
        m_sName = null;
    }
   
    public Object getKey() {
        return m_sID;
    }
    
    public void readValues(DataRead dr) throws BasicException {
        m_sID = dr.getString(1);
        m_sName = dr.getString(2);
    }     
    
    public void setID(String sID) {
        m_sID = sID;
    }
    
    public String getID() {
        return m_sID;
    }
    
    public String getName() {
        return m_sName;
    }
    
    public void setName(String sValue) {
        m_sName = sValue;
    }    
    
    public String toString() {
        return m_sName;
    }
}
