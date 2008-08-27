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

package com.openbravo.pos.util;

import com.openbravo.format.Formats;

public class RoundUtils {
    
    /** Creates a new instance of DoubleUtils */
    private RoundUtils() {
    }
    
    public static double round(double dValue) {
        double fractionMultiplier = Math.pow(10.0, Formats.getCurrencyDecimals());
        return Math.rint(dValue * fractionMultiplier) / fractionMultiplier;
    }
    
    public static int compare(double d1, double d2) {
        
        return Double.compare(round(d1), round(d2));
    }   
    
    public static double getValue(Double value) {
        return value == null ? 0.0 : value.doubleValue();
    }
}
