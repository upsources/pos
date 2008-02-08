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

package com.openbravo.data.loader;

import com.openbravo.basic.BasicException;

/**
 *
 * @author  adrian
 */
public class SentenceUpdateResultSet implements DataResultSet {
    
    private int m_iUpdateCount;
    
    /** Creates a new instance of UpdateResultSet */
    public SentenceUpdateResultSet(int iUpdateCount) {
        m_iUpdateCount = iUpdateCount;
    }
    
    public Integer getInt(int columnIndex) throws BasicException {
        throw new BasicException(LocalRes.getIntString("exception.nodataset"));
    }
    public String getString(int columnIndex) throws BasicException {
        throw new BasicException(LocalRes.getIntString("exception.nodataset"));
    }
    public Double getDouble(int columnIndex) throws BasicException {
        throw new BasicException(LocalRes.getIntString("exception.nodataset"));
    }
    public Boolean getBoolean(int columnIndex) throws BasicException {
        throw new BasicException(LocalRes.getIntString("exception.nodataset"));
    }
    public java.util.Date getTimestamp(int columnIndex) throws BasicException {
        throw new BasicException(LocalRes.getIntString("exception.nodataset"));
    }

    //public java.io.InputStream getBinaryStream(int columnIndex) throws DataException;
    public byte[] getBytes(int columnIndex) throws BasicException {
        throw new BasicException(LocalRes.getIntString("exception.nodataset"));
    }
    public Object getObject(int columnIndex) throws BasicException  {
        throw new BasicException(LocalRes.getIntString("exception.nodataset"));
    }

//    public int getColumnCount() throws DataException;
    public DataField[] getDataField() throws BasicException {
        throw new BasicException(LocalRes.getIntString("exception.nodataset"));
    }        
    
    public Object getCurrent() throws BasicException {
        throw new BasicException(LocalRes.getIntString("exception.nodataset"));
    }
    
    public boolean next() throws BasicException {
        throw new BasicException(LocalRes.getIntString("exception.nodataset"));
    }
     
    public void close() throws BasicException {
    }
   
    public int updateCount() throws BasicException {
        return m_iUpdateCount;
    }
}
