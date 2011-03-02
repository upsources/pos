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
import java.io.Serializable;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.format.Formats;
import com.openbravo.data.loader.SerializableWrite;
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.IKeyed;

/**
 *
 * @author  Luis Ig. Bacas Riveiro	lbacas@opensistemas.com
 * @author  Pablo J. Urbano Santos	purbano@opensistemas.com
 */
public class DiscountInfo implements SerializableRead, SerializableWrite, Serializable, IKeyed {

    private String m_sID;
    private String m_sName;
    private double m_dQuantity;
    private boolean m_bPercentage;
    
    /** Creates new DiscountInfo */
    public DiscountInfo() {
        m_sID = null;
        m_sName = null;
        m_dQuantity = 0.0;
        m_bPercentage = false;
    }
    
    /** Creates new DiscountInfo */
    public DiscountInfo(String sID, String sName, double dQuantity, boolean bPercentage) {
        m_sID = sID;
        m_sName = sName;
        m_dQuantity = dQuantity;
        m_bPercentage = bPercentage;
    }
    
    public DiscountInfo(String sID, String sName, double dQuantity) {
        this(sID, sName, dQuantity, false);
    }
    
    public DiscountInfo(String sID, String sName, String value) {
        this(sID, sName, 0.0, false);
        
        if (value != null) {
            boolean hasSymbol = value.endsWith("%");
            try {               
                double quantity = (hasSymbol) 
                        ? (Double)Formats.PERCENT.parseValue(value)
                        : (Double)Formats.CURRENCY.parseValue(value);
                setQuantity(quantity);
            } catch (BasicException ex) {
                setQuantity(0.0);
            }
            setPercentage(hasSymbol);
        }
    }
    
    public Object getKey() {
        return m_sID;
    }
    public void readValues(DataRead dr) throws BasicException {
        m_sID = dr.getString(1);
        m_sName = dr.getString(2);
        m_dQuantity = dr.getDouble(3).doubleValue();
        m_bPercentage = dr.getBoolean(4).booleanValue();
    }   
    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, m_sID);
        dp.setString(2, m_sName);
        dp.setDouble(3, new Double(m_dQuantity));
        dp.setBoolean(4, m_bPercentage);
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
    
    public void setName(String sName) {
        m_sName = sName;
    }
    
    public double getQuantity() {
        return m_dQuantity;
    }
    
    public void setQuantity(double dValue) {
        m_dQuantity = dValue;
    }

    public boolean isPercentage() {
        return m_bPercentage;
    }
    
    public String getValue() {
        return (m_bPercentage) 
                ? Formats.PERCENT.formatValue(m_dQuantity)
                : Formats.CURRENCY.formatValue(m_dQuantity);
    }
    
    public void setPercentage(boolean bValue) {
        m_bPercentage = bValue;
    }
    
    @Override
    public String toString(){
        return m_sName;
    }
    
    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	} else if (obj instanceof DiscountInfo) {
            DiscountInfo t = (DiscountInfo) obj;
            
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
            
            // la cantidad
            if (m_dQuantity != t.m_dQuantity) return false;
            
            // el porcentage
            if (m_bPercentage != t.m_bPercentage) return false;
            
            return true;
        } else {
            return false;
        } 
    }
    
    @Override
    public int hashCode() {
        return (m_sID == null ? 0 : m_sID.hashCode()) + 
                (m_sName == null ? 0 : m_sName.hashCode()) + 
                new Double(m_dQuantity).hashCode() +
                new Boolean(m_bPercentage).hashCode();
    }    
}
