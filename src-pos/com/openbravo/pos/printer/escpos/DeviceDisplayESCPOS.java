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
    
import com.openbravo.pos.printer.*;

public class DeviceDisplayESCPOS extends DeviceDisplaySerial {
       
    private UnicodeTranslator trans;

    /** Creates a new instance of DeviceDisplayESCPOS */
    public DeviceDisplayESCPOS(PrinterWritter display, UnicodeTranslator trans) {
        this.trans = trans;
        init(display);       
    }

    @Override
    public void initVisor() {
        display.init(ESCPOS.INIT);
        display.write(ESCPOS.SELECT_DISPLAY); // Al visor
        display.write(trans.getCodeTable());
        display.write(ESCPOS.VISOR_HIDE_CURSOR);         
        display.write(ESCPOS.VISOR_CLEAR);
        display.write(ESCPOS.VISOR_HOME);
        display.flush();
    }
    
        
     @Override
     public void writeVisor(String sLine1, String sLine2) {
               
        display.write(ESCPOS.SELECT_DISPLAY);
        display.write(ESCPOS.VISOR_CLEAR);
        display.write(ESCPOS.VISOR_HOME);
        display.write(trans.transString(DeviceTicket.alignLeft(sLine1, 20)));
        display.write(trans.transString(DeviceTicket.alignLeft(sLine2, 20)));        
        display.flush();
    }
    
    @Override
    public void clearVisor() {

        display.write(ESCPOS.SELECT_DISPLAY);
        display.write(ESCPOS.VISOR_CLEAR);
        display.write(ESCPOS.VISOR_HOME);
        display.flush();
    }
}
