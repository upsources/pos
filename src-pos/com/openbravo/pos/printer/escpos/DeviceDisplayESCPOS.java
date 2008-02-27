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
    
import java.io.*;
import java.util.*;

import com.openbravo.pos.printer.*;
import com.openbravo.pos.forms.AppLocal;

public class DeviceDisplayESCPOS implements DeviceDisplay {
    
    private String m_sName;
    
    private PrinterWritter m_CommOutputPrinter;     
    private UnicodeTranslator m_trans;

    /** Creates a new instance of DeviceDisplayESCPOS */
    public DeviceDisplayESCPOS(PrinterWritter CommOutputPrinter, UnicodeTranslator trans) {
        
        m_sName = AppLocal.getIntString("Printer.Serial");
        m_CommOutputPrinter = CommOutputPrinter;
        m_trans = trans;
        
        // Inicializamos el visor
        m_CommOutputPrinter.write(ESCPOS.SELECT_DISPLAY); // Al visor
        m_CommOutputPrinter.write(m_trans.getCodeTable());
        m_CommOutputPrinter.write(ESCPOS.VISOR_HIDE_CURSOR);

        GregorianCalendar oCalendar = new GregorianCalendar();
        oCalendar.setTime(new Date());
        byte[] bTime = {
            (byte) oCalendar.get(GregorianCalendar.HOUR_OF_DAY),
            (byte) oCalendar.get(GregorianCalendar.MINUTE)
        };
        m_CommOutputPrinter.write(ESCPOS.VISOR_SETTIME);
        m_CommOutputPrinter.write(bTime);            
        m_CommOutputPrinter.write(ESCPOS.VISOR_CLEAR);
        m_CommOutputPrinter.write(ESCPOS.VISOR_HOME);
        m_CommOutputPrinter.flush();
        
    }
    
    public String getDisplayName() {
        return m_sName;
    }    
    public String getDisplayDescription() {
        return null;
    }        
    public javax.swing.JComponent getDisplayComponent() {
        return null;
    }
    
    public void writeVisor(String sLine1, String sLine2) {
               
        m_CommOutputPrinter.write(ESCPOS.SELECT_DISPLAY);
        m_CommOutputPrinter.write(ESCPOS.VISOR_CLEAR);
        m_CommOutputPrinter.write(ESCPOS.VISOR_HOME);
        m_CommOutputPrinter.write(m_trans.transString(DeviceTicket.alignLeft(sLine1, 20)));
        m_CommOutputPrinter.write(m_trans.transString(DeviceTicket.alignLeft(sLine2, 20)));        
        m_CommOutputPrinter.flush();
    }
    
    public void clearVisor() {
        
         // writeVisor(null, null);
        m_CommOutputPrinter.write(ESCPOS.SELECT_DISPLAY);
        m_CommOutputPrinter.write(ESCPOS.VISOR_CLEAR);
        m_CommOutputPrinter.write(ESCPOS.VISOR_HOME);
        m_CommOutputPrinter.flush();
    }
    
//    public void writeTimeVisor(String sLine1) {
//        
//        m_CommOutputPrinter.write(ESCPOS.SELECT_DISPLAY);
//        m_CommOutputPrinter.write(ESCPOS.VISOR_PRINTTIME);
//        m_CommOutputPrinter.write(m_trans.transString(DeviceTicket.alignCenter(sLine1, 19)));   
//        m_CommOutputPrinter.flush();        
//    }    
}
