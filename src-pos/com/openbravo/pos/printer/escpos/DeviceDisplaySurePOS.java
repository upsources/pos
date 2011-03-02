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

import com.openbravo.pos.printer.DeviceTicket;

/**
 *
 * @author adrianromero
 */
public class DeviceDisplaySurePOS extends DeviceDisplaySerial {
    
    private UnicodeTranslator trans;
    
    public DeviceDisplaySurePOS(PrinterWritter display) { 
        trans = new UnicodeTranslatorSurePOS();
        init(display);                
    }
   
    @Override
    public void initVisor() {
        display.write(new byte[]{0x00, 0x01}); // IBM Mode
        display.write(new byte[]{0x02}); // Set the code page
        display.write(trans.getCodeTable());
        display.write(new byte[]{0x11}); // HIDE CURSOR
        display.write(new byte[]{0x14}); // HIDE CURSOR
        display.write(new byte[]{0x10, 0x00}); // VISOR HOME
        display.flush();
    }

    @Override
    public void writeVisor(String sLine1, String sLine2) {
        display.write(new byte[]{0x10, 0x00}); // VISOR HOME
        display.write(trans.transString(DeviceTicket.alignLeft(sLine1, 20)));
        display.write(new byte[]{0x10, 0x14});
        display.write(trans.transString(DeviceTicket.alignLeft(sLine2, 20)));        
        display.flush();
    }

    @Override
    public void clearVisor() {
        display.write(new byte[]{0x10, 0x00}); // VISOR HOME   
        display.write(trans.transString(DeviceTicket.getWhiteString(20)));
        display.write(new byte[]{0x10, 014});
        display.write(trans.transString(DeviceTicket.getWhiteString(20)));          
        display.flush();
    }
}
