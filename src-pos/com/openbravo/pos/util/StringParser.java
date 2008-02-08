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

package com.openbravo.pos.util;

public class StringParser {
    
    private int currentPosition;
    private int maxPosition;
    private String str;
    
    /** Creates a new instance of StringParser */
    public StringParser(String str) {
        this.str = str;
        currentPosition = 0;
        maxPosition = str == null ? 0 : str.length();
    }
    
    public String nextToken(char c) {
       
        if (currentPosition < maxPosition) {

            int start = currentPosition;
            while (currentPosition < maxPosition && c != str.charAt(currentPosition)) {
                currentPosition ++;
            }

            if (currentPosition < maxPosition) {
                return str.substring(start, currentPosition++);
            } else {
                return str.substring(start);
            }
        } else {
            return "";
        }
    }
}
