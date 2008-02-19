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

public class ESCPOS {

    public static final byte[] INIT = {0x1B, 0x40};
    
    public static final byte[] TRANSMIT_STATUS = {0x1D, 0x72, 0x02};
        
    public static final byte[] SELECT_PRINTER = {0x1B, 0x3D, 0x01};
    public static final byte[] SELECT_DISPLAY = {0x1B, 0x3D, 0x02};    

    public static final byte[] HT = {0x09}; // Horizontal Tab
    public static final byte[] LF = {0x0A}; // Print and line feed
    public static final byte[] FF = {0x0C}; // 
    public static final byte[] CR = {0x0D}; // Print and carriage return
  
    public static final byte[] STANDARD_MODE = {0x1B, 0x53}; // Select Standard Mode
    public static final byte[] PAGE_MODE = {0x1B, 0x4C}; // Select page mode
        
    public static final byte[] CHAR_FONT_0 = {0x1B, 0x4D, 0x00};
    public static final byte[] CHAR_FONT_1 = {0x1B, 0x4D, 0x01};
    public static final byte[] CHAR_FONT_2 = {0x1B, 0x4D, 0x30};
    public static final byte[] CHAR_FONT_3 = {0x1B, 0x4D, 0x31};
    
    public static final byte[] BOLD_SET = {0x1B, 0x45, 0x01};
    public static final byte[] BOLD_RESET = {0x1B, 0x45, 0x00};
    
    public static final byte[] REVERSED_SET = {0x1D, 0x42, 0x01};
    public static final byte[] REVERSED_RESET = {0x1D, 0x42, 0x00};
    
    public static final byte[] SMOOTH_SET = {0x1D, 0x62, 0x01};
    public static final byte[] SMOOTH_RESET = {0x1D, 0x62, 0x00};

    public static final byte[] UNDERLINE_SET = {0x1B, 0x2D, 0x01};
    public static final byte[] UNDERLINE_SETDOUBLE = {0x1B, 0x2D, 0x02};
    public static final byte[] UNDERLINE_RESET = {0x1B, 0x2D, 0x00};
    
//    public static final byte[] LOAD_IMAGE = {0x1C, 0x71, 0x01};
//    public static final byte[] PRINT_LOADEDIMAGE = {0x1C, 0x70, 0x01, 0x03};
    
    public static final byte[] BAR_HEIGHT = {0x1D, 0x68, 0x40};
    public static final byte[] BAR_POSITIONDOWN = {0x1D, 0x48, 0x02}; // ABAJO
    public static final byte[] BAR_HRIFONT1 = {0x1D, 0x66, 0x01}; // FUENTE 2
    
    public static final byte[] BAR_CODE00 = {0x1D, 0x6B, 0x02};
    public static final byte[] BAR_CODE01 = {0x1D, 0x6B, 0x02};
    public static final byte[] BAR_CODE02 = {0x1D, 0x6B, 0x02}; // 12 n\u00fameros fijos
    public static final byte[] BAR_CODE03 = {0x1D, 0x6B, 0x02};
    public static final byte[] BAR_CODE04 = {0x1D, 0x6B, 0x02};
    public static final byte[] BAR_CODE05 = {0x1D, 0x6B, 0x02}; // un mont\u00f3n
    public static final byte[] BAR_CODE06 = {0x1D, 0x6B, 0x05};
    
    public static final byte[] BAR_CODE43 = {0x1D, 0x6B, 0x43}; // EQUIV AL 02
    public static final byte[] BAR_CODE46 = {0x1D, 0x6B, 0x46}; // EQUIV AL 05
    
    public static final byte[] VISOR_HIDE_CURSOR = {0x1F, 0x43,0x00};
    public static final byte[] VISOR_SHOW_CURSOR = {0x1F, 0x43,0x01};
    public static final byte[] VISOR_HOME = {0x0B};
    public static final byte[] VISOR_CLEAR = {0x0C};
    public static final byte[] VISOR_SETTIME = {0x1F, 0x54};
    public static final byte[] VISOR_PRINTTIME = {0x1F, 0x55};
    
    public static final byte[] VISOR_BLINK_SET = {0x1F, 0x45, 0x01};
    public static final byte[] VISOR_BLINK_RESET = {0x1F, 0x45, 0x00};
    public static final byte[] VISOR_REVERSED_SET = {0x1F, 0x72, 0x01};
    public static final byte[] VISOR_REVERSED_RESET = {0x1F, 0x72, 0x00};
        
    public static final byte[] CODE_TABLE_00 = {0x1B, 0x74, 0x00}; // La de defecto
    public static final byte[] CODE_TABLE_01 = {0x1B, 0x74, 0x01};
    public static final byte[] CODE_TABLE_02 = {0x1B, 0x74, 0x02}; // Multilingual
    public static final byte[] CODE_TABLE_03 = {0x1B, 0x74, 0x03}; // Portuguesse
    public static final byte[] CODE_TABLE_13 = {0x1B, 0x74, 0x13}; // European, 19 en decimal  
    
    private ESCPOS() {       
    }
}
