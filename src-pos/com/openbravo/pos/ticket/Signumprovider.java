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

package com.openbravo.pos.ticket;

import java.util.*;

public class Signumprovider {
    
    private Set m_positives = new HashSet();
    private Set m_negatives = new HashSet();
    
    /** Creates a new instance of Signumprovider */
    public Signumprovider() {
    }
    
    public void addPositive(Object key) {
        m_positives.add(key);
    }
    
    public void addNegative(Object key) {
        m_negatives.add(key);
    }
    
    public Double correctSignum(Object key, Double value) {
        if (m_positives.contains(key)) {
            return value.doubleValue() < 0.0 ? new Double(-value.doubleValue()) : value;
        } else if (m_negatives.contains(key)) {
            return value.doubleValue() > 0.0 ? new Double(-value.doubleValue()) : value;
        } else {
            return value;
        }        
    }    
}
