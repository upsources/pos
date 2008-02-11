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

package com.openbravo.pos.inventory;

import com.openbravo.data.loader.IKeyed;

/**
 *
 * @author adrianromero
 */
public class CodeType implements IKeyed {
    
    public static final CodeType EAN13 = new CodeType("EAN13", "EAN13");
    public static final CodeType CODE128 = new CodeType("CODE128", "CODE128");

    protected String m_sKey;
    protected String m_sValue;
    
    private CodeType(String key, String value) {
        m_sKey = key;
        m_sValue = value;
    }
    public Object getKey() {
        return m_sKey;
    }
    public String getValue() {
        return m_sValue;
    }
    public String toString() {
        return m_sValue;
    }   
}