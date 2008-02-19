//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007 Openbravo, S.L.
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

package com.openbravo.pos.reports;

import java.util.HashMap;
import java.util.Map;
import com.openbravo.pos.forms.AppLocal;

public class ReportFieldsArray implements ReportFields {
    
    // private String[] m_afields;
    private Map m_keys = null;
    
    /** Creates a new instance of ReportFieldsArray */
    public ReportFieldsArray(String[] afields) {
               
        // Creo el mapa de claves
        m_keys = new HashMap();
        for (int i = 0; i < afields.length; i++) {
            m_keys.put(afields[i], new Integer(i));
        }
    }
    
    public Object getField(Object record, String field) throws ReportException {
        
        Integer i = (Integer) m_keys.get(field);
        if (i == null) {
            throw new ReportException(AppLocal.getIntString("exception.unavailablefield", new Object[] {field}));
        } else {
            Object[] arecord = (Object[]) record;
            if (arecord == null || i.intValue() < 0 || i.intValue() >= arecord.length) {
                throw new ReportException(AppLocal.getIntString("exception.unavailablefields"));
            } else {
                return arecord[i.intValue()];
            }
        }        
    }
}
