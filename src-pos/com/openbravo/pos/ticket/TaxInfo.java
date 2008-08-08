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

package com.openbravo.pos.ticket;

import java.io.Serializable;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableWrite;
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.IKeyed;

/**
 *
 * @author adrianromero
 */
public class TaxInfo implements SerializableRead, SerializableWrite, Serializable, IKeyed {

    private String m_sID;
    private String m_sName;
    private String taxcategoryid;
    private String taxcustcategoryid;
    private String parentid;
    
    private double m_dRate;
    private boolean cascade;
    
    /** Creates new TaxInfo */
    public TaxInfo() {
        m_sID = null;
        m_sName = null;
        taxcategoryid = null;
        taxcustcategoryid = null;
        parentid = null;
        
        m_dRate = 0.0;         
        cascade = false;
    }
    
    /** Creates new TaxInfo */
    public TaxInfo(String sID, String sName, double dRate) {
        m_sID = sID;
        m_sName = sName;
        taxcategoryid = null;
        taxcustcategoryid = null;
        parentid = null;
        
        m_dRate = dRate;
        cascade = false;
    }
    
    public Object getKey() {
        return m_sID;
    }
    public void readValues(DataRead dr) throws BasicException {
        m_sID = dr.getString(1);
        m_sName = dr.getString(2);
        taxcategoryid = dr.getString(3);
        taxcustcategoryid = dr.getString(4);
        parentid = dr.getString(5);
        
        m_dRate = dr.getDouble(6).doubleValue();
        cascade = dr.getBoolean(7).booleanValue();
    }   
    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, m_sID);
        dp.setString(2, m_sName);
        dp.setString(3, taxcategoryid);
        dp.setString(4, taxcustcategoryid);
        dp.setString(5, parentid);
        
        dp.setDouble(6, new Double(m_dRate));
        dp.setBoolean(7, Boolean.valueOf(cascade));
    }
    
    public void setID(String sID) {
        m_sID = sID;
    }
    
    public String getId() {
        return m_sID;
    }

    public String getName() {
        return m_sName;
    }
    
    public void setName(String sName) {
        m_sName = sName;
    }

    public String getTaxCategoryID() {
        return taxcategoryid;
    }
    
    public void setTaxCategoryID(String value) {
        taxcategoryid = value;
    }

    public String getTaxCustCategoryID() {
        return taxcustcategoryid;
    }
    
    public void setTaxCustCategoryID(String value) {
        taxcustcategoryid = value;
    }    

    public String getParentID() {
        return parentid;
    }
    
    public void setParentID(String value) {
        parentid = value;
    }
    
    public double getRate() {
        return m_dRate;
    }
    
    public void setRate(double dValue) {
        m_dRate = dValue;
    }

    public boolean isCascade() {
        return cascade;
    }
    
    public void setCascade(boolean value) {
        cascade = value;
    }
    
    @Override
    public String toString(){
        return m_sName;
    }
    
    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	} else if (obj instanceof TaxInfo) {
            TaxInfo t = (TaxInfo) obj;
            
            // el id
            if (m_sID == null) {
                if (t.m_sID != null) return false;
            } else {
                if (!m_sID.equals(t.m_sID)) return false;
            }
            
            // el nombre
            if (m_sName == null) {
                if (t.m_sName != null) return false;
            } else {
                if (!m_sName.equals(t.m_sName)) return false;
            }          
            
            // tax category ID
            if (taxcategoryid == null) {
                if (t.taxcategoryid != null) return false;
            } else {
                if (!taxcategoryid.equals(t.taxcategoryid)) return false;
            }   
            
            // tax cust category ID
            if (taxcustcategoryid == null) {
                if (t.taxcustcategoryid != null) return false;
            } else {
                if (!taxcustcategoryid.equals(t.taxcustcategoryid)) return false;
            } 
            
            // Parent ID
            if (parentid == null) {
                if (t.parentid != null) return false;
            } else {
                if (!parentid.equals(parentid)) return false;
            } 
            
            // Cascade
            if (cascade != t.cascade) return false;
            
            // el porcentage
            if (m_dRate != t.m_dRate) return false;
            
            return true;
        } else {
            return false;
        }           
    }
    
    @Override
    public int hashCode() {      
        return (m_sID == null ? 0 : m_sID.hashCode()) 
                + (m_sName == null ? 0 : m_sName.hashCode()) 
                + (taxcategoryid == null ? 0 : taxcategoryid.hashCode()) 
                + (taxcustcategoryid == null ? 0 : taxcustcategoryid.hashCode()) 
                + (parentid == null ? 0 : parentid.hashCode()) 
                + new Double(m_dRate).hashCode()
                + Boolean.valueOf(cascade).hashCode();
    }    
}
