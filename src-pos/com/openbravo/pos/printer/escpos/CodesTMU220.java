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

import java.awt.image.BufferedImage;

public class CodesTMU220 extends Codes {
    
    public static final byte[] CHAR_SIZE_0 = {0x1B, 0x21, 0x01}; // This sets 7x9 font 
    public static final byte[] CHAR_SIZE_1 = {0x1B, 0x21, 0x11}; // This sets double hight 7x9 font 
    public static final byte[] CHAR_SIZE_2 = {0x1B, 0x21, 0x21}; // This sets 7x9 double width font 
    public static final byte[] CHAR_SIZE_3 = {0x1B, 0x21, 0x31}; // This sets 7x9 double width/hight font 
    
    private static final byte[] OPEN_DRAWER = {0x1B, 0x70, 0x00, 0x32, -0x06};
    
    private static final byte[] PARTIAL_CUT_1 = {0x1B, 0x69};
    private static final byte[] PARTIAL_CUT_3 = {0x1B, 0x6D};

    /** Creates a new instance of CodesTMU220 */
    public CodesTMU220() {
    }
     
    public byte[] getSize0() { return CHAR_SIZE_0; }
    public byte[] getSize1() { return CHAR_SIZE_1; }
    public byte[] getSize2() { return CHAR_SIZE_2; }
    public byte[] getSize3() { return CHAR_SIZE_3; }
    
    public byte[] getOpenDrawer() { return OPEN_DRAWER; }
    
    public byte[] getCutReceipt() { return PARTIAL_CUT_1; }   
    
    public byte[] transImage(BufferedImage oImage) {
        // No imprimo nada. Al menos asi no imprimire basura.
        return new byte[0];
    }
}
