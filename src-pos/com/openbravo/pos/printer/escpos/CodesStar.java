//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2008 Openbravo, S.L.
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

package com.openbravo.pos.printer.escpos;

import java.awt.image.BufferedImage;

public class CodesStar extends Codes {
    
    private static final byte[] CHAR_SIZE_0 = {0x1B, 0x69, 0x00, 0x00};
    private static final byte[] CHAR_SIZE_1 = {0x1B, 0x69, 0x01, 0x00};
    private static final byte[] CHAR_SIZE_2 = {0x1B, 0x69, 0x00, 0x01};
    private static final byte[] CHAR_SIZE_3 = {0x1B, 0x69, 0x01, 0x01};
    
    private static final byte[] OPEN_DRAWER = {0x1C};    
    private static final byte[] PARTIAL_CUT = {0x1B, 0x64, 0x30};
    private static final byte[] IMAGE_HEADER = {0x1B, 0x4B};
    private static final byte[] NEW_LINE = {0x0D, 0x0A}; // Print and carriage return
    
    /** Creates a new instance of CodesStar */
    public CodesStar() {
    }
     
    public byte[] getSize0() { return CHAR_SIZE_0; }
    public byte[] getSize1() { return CHAR_SIZE_1; }
    public byte[] getSize2() { return CHAR_SIZE_2; }
    public byte[] getSize3() { return CHAR_SIZE_3; }
    
    public byte[] getOpenDrawer() { return OPEN_DRAWER; }    
    public byte[] getCutReceipt() { return PARTIAL_CUT; }   
    public byte[] getNewLine() { return NEW_LINE; } 
    public byte[] getImageHeader() { return IMAGE_HEADER; }     
    
    @Override
    public byte[] transImage(BufferedImage oImage) {
                        
        // Imprimo los par\u00e1metros en cu\u00e1druple
        int iWidth = oImage.getWidth();
        int iHeight = (oImage.getHeight() + 7) / 8; // n\u00famero de bytes
        
        // Array de datos
        byte[] bData = new byte[(getImageHeader().length + 2 + iWidth + getNewLine().length) * iHeight];
        
        // Comando de impresion de imagen
        
        int index = 0;

        // Raw data
        int iRGB;
        int p;
        for (int i = 0; i < oImage.getHeight(); i += 8) {
            System.arraycopy(getImageHeader(), 0, bData, index, getImageHeader().length);
            index += getImageHeader().length;
            
             // Line Dimension
            bData[index ++] = (byte) (iWidth % 256);
            bData[index ++] = (byte) (iWidth / 256);           
            
            for (int j = 0; j < oImage.getWidth(); j++) {                
                p = 0x00;
                for (int d = 0; d < 8; d ++) {
                    p = p << 1;
                    if (i + d < oImage.getHeight()){
                        iRGB = oImage.getRGB(j, i + d);
                        
                        int gray = (int)(0.30 * ((iRGB >> 16) & 0xff) + 
                                         0.59 * ((iRGB >> 8) & 0xff) + 
                                         0.11 * (iRGB & 0xff));
                        
                        if (gray < 128) {
                            p = p | 0x01;
                        }
                    }
                }
                
                bData[index ++] = (byte) p;
            }
            System.arraycopy(getNewLine(), 0, bData, index, getImageHeader().length);
            index += getImageHeader().length;
        
        }        
        return bData;
    }     
}
