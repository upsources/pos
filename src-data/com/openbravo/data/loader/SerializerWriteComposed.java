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

package com.openbravo.data.loader;

import com.openbravo.basic.BasicException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SerializerWriteComposed implements SerializerWrite {
    
    private List<SerializerWrite> serwrites = new ArrayList<SerializerWrite>();    
    
    /** Creates a new instance of SerializerWriteComposed */
    public SerializerWriteComposed() {
    }
    
    public void add(SerializerWrite sw) {
        serwrites.add(sw);
    }
    
    public void writeValues(DataWrite dp, Object obj) throws BasicException {
        
        Object[] a = (Object[]) obj;
        DataWriteComposed dpc = new DataWriteComposed(dp);
        
        int i = 0;
        for (SerializerWrite sw : serwrites) {
            dpc.next();
            sw.writeValues(dpc, a[i++]);
        }
    }  
    
    private static class DataWriteComposed implements DataWrite {
        
        private DataWrite dp;
        private int offset = 0;
        private int max = 0;       
        
        public DataWriteComposed(DataWrite dp) {
            this.dp = dp;
        }
        
        public void next() {
            offset = max;
        }
        
        public void setInt(int paramIndex, Integer iValue) throws BasicException {
            dp.setInt(offset + paramIndex, iValue);
            max = Math.max(max, offset + paramIndex);
        }

        public void setString(int paramIndex, String sValue) throws BasicException {
            dp.setString(offset + paramIndex, sValue);
            max = Math.max(max, offset + paramIndex);
        }

        public void setDouble(int paramIndex, Double dValue) throws BasicException {
            dp.setDouble(offset + paramIndex, dValue);
            max = Math.max(max, offset + paramIndex);
        }

        public void setBoolean(int paramIndex, Boolean bValue) throws BasicException {
            dp.setBoolean(offset + paramIndex, bValue);
            max = Math.max(max, offset + paramIndex);
        }

        public void setTimestamp(int paramIndex, Date dValue) throws BasicException {
            dp.setTimestamp(offset + paramIndex, dValue);
            max = Math.max(max, offset + paramIndex);
        }

        public void setBytes(int paramIndex, byte[] value) throws BasicException {
            dp.setBytes(offset + paramIndex, value);
            max = Math.max(max, offset + paramIndex);
        }

        public void setObject(int paramIndex, Object value) throws BasicException {
            dp.setObject(offset + paramIndex, value);
            max = Math.max(max, offset + paramIndex);
        }
    }
    
}