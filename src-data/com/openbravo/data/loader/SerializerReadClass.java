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

public class SerializerReadClass implements SerializerRead {

    private Class m_clazz;
    
    /** Creates a new instance of DefaultSerializerRead */
    public SerializerReadClass(Class clazz) {
        m_clazz = clazz;
    }
    
    public Object readValues(DataRead dr) throws BasicException {
        try {
            SerializableRead sr = (SerializableRead) m_clazz.newInstance();
            sr.readValues(dr);
            return sr;
        } catch (java.lang.InstantiationException eIns) {
            return null;
        } catch (IllegalAccessException eIA) {
            return null;
        } catch (ClassCastException eCC) {
            return null;
        }
    }
}
