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

package com.openbravo.format;

import java.text.ParseException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class FormatsRESOURCE extends Formats {
    
    private ResourceBundle m_rb;
    private String m_sPrefix;
    
    /** Creates a new instance of FormatsRESOURCE */
    public FormatsRESOURCE(ResourceBundle rb, String sPrefix) {
        m_rb = rb;
        m_sPrefix = sPrefix;
    }
    protected String formatValueInt(Object value) {
        try {
            return m_rb.getString(m_sPrefix + (String) value);
        } catch (MissingResourceException e) {
            return (String) value;
        }
    }   
    protected Object parseValueInt(String value) throws ParseException {
        return value;
    }
    public int getAlignment() {
        return javax.swing.SwingConstants.LEFT;
    }    
}
