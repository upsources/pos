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

package com.openbravo.pos.printer.escpos;

import java.awt.image.BufferedImage;

public abstract class Codes {
    
    public static final byte[] PRINT_IMAGE = {0x1D, 0x76, 0x30, 0x03};
    
    /** Creates a new instance of Codes */
    public Codes() {
    }
    
    public abstract byte[] getSize0();
    public abstract byte[] getSize1();
    public abstract byte[] getSize2();
    public abstract byte[] getSize3();
    
    public abstract byte[] getOpenDrawer();
    
    public abstract byte[] getCutReceipt();
   
    public byte[] transImage(BufferedImage oImage) {
                        
        // Imprimo los par\u00e1metros en cu\u00e1druple
        int iWidth = (oImage.getWidth() + 7) / 8; // n\u00famero de bytes
        int iHeight = oImage.getHeight();
        
        // Array de datos
        byte[] bData = new byte[PRINT_IMAGE.length + 4 + iWidth * iHeight];
        
        // Comando de impresion de imagen
        System.arraycopy(PRINT_IMAGE, 0, bData, 0, PRINT_IMAGE.length);
        
        int index = PRINT_IMAGE.length;
        
        // Dimension de la imagen
        bData[index ++] = (byte) (iWidth % 256);
        bData[index ++] = (byte) (iWidth / 256);
        bData[index ++] = (byte) (iHeight % 256);
        bData[index ++] = (byte) (iHeight / 256);       
        
        // Raw data
        int iRGB;
        int p;
        for (int i = 0; i < oImage.getHeight(); i++) {
            for (int j = 0; j < oImage.getWidth(); j = j + 8) {                
                p = 0x00;
                for (int d = 0; d < 8; d ++) {
                    p = p << 1;
                    if (j + d < oImage.getWidth()){
                        iRGB = oImage.getRGB(j + d, i);
                        // La condici\u00f3n es que no imprima lo claro 
                        // y que s\u00ed imprima lo oscuro.
                        
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
        }        
        return bData;
    }    
}
