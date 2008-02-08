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

public class SerializerWriteBasicComposed implements SerializerWrite {
    
    private Datas[][] m_classes;    
    
    /** Creates a new instance of SerializerWriteComposed */
    public SerializerWriteBasicComposed(Datas[]... classes) {
         m_classes = classes;
    }
    
    public void writeValues(DataWrite dp, Object obj) throws BasicException {

        Object[] values = (Object []) obj;
        int index = 0;
        for (int i = 0; i < m_classes.length; i++) {
            Object[] val = (Object[]) values[i];
            for (int j = 0; j < m_classes[i].length; j++) {
                index++;
                m_classes[i][j].setValue(dp, index, val[j]);
            }
        }
    }  
}
