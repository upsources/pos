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

package com.openbravo.pos.printer.escpos;

public class UnicodeTranslatorEur extends UnicodeTranslator {
    
    /** Creates a new instance of UnicodeTranslatorEur */
    public UnicodeTranslatorEur() {
    }
    
    public byte[] getCodeTable() {
        return ESCPOS.CODE_TABLE_13;            
    }
    
    public byte transChar(char sChar) {
        if ((sChar >= 0x0000) && (sChar < 0x0080)) {
            return (byte) sChar;
        } else {
            switch (sChar) {
                case '\u00e1': return -0x60; // a acute
                case '\u00e9': return -0x7E; // e acute
                case '\u00ed': return -0x5F; // i acute
                case '\u00f3': return -0x5E; // o acute
                case '\u00fa': return -0x5D; // u acute
                case '\u00fc': return -0x7F; // u dieresis
                case '\u00f1': return -0x5C; // n tilde
                case '\u00d1': return -0x5B; // N tilde 
                
                case '\u00c1': return 0x41; // A acute
                case '\u00c9': return 0x45; // E acute
                case '\u00cd': return 0x49; // I acute
                case '\u00d3': return 0x4F; // O acute
                case '\u00da': return 0x55; // U acute
                case '\u00dc': return -0x66; // U dieresis
                case '\u00bf': return -0x58; // abrir interrogacion
                case '\u00a1': return -0x53; // abrir admiracion
                case '\u20ac': return -0x12; // Euro Sign

        
                default: return 0x3F; // ? Not valid character.
            }          
        }
    } 
    
    public byte transNumberChar(char sChar) {
        switch (sChar) {
        case '0' : return 0x30;
        case '1' : return 0x31;
        case '2' : return 0x32;
        case '3' : return 0x33;
        case '4' : return 0x34;
        case '5' : return 0x35;
        case '6' : return 0x36;
        case '7' : return 0x37;
        case '8' : return 0x38;
        case '9' : return 0x39;
        default: return 0x30;
        }          
    }       
}
