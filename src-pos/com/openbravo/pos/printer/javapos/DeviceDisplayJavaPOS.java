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

package com.openbravo.pos.printer.javapos;

import jpos.JposException;
import jpos.LineDisplay;
import jpos.LineDisplayConst;
import com.openbravo.pos.printer.DeviceDisplay;
import com.openbravo.pos.printer.DeviceDisplayBase;
import com.openbravo.pos.printer.DeviceDisplayImpl;
import com.openbravo.pos.printer.TicketPrinterException;

public class DeviceDisplayJavaPOS implements DeviceDisplay, DeviceDisplayImpl {
    
    private String m_sName;
    
    private DeviceDisplayBase m_displaylines;
    
    private LineDisplay m_ld;
    
    /** Creates a new instance of DeviceDisplayJavaPOS */
    public DeviceDisplayJavaPOS(String sDeviceName) throws TicketPrinterException {
        m_sName = sDeviceName;
        
        m_ld = new LineDisplay();
        try {       
            m_ld.open(m_sName);
            m_ld.claim(10000);
            m_ld.setDeviceEnabled(true);
        } catch (JposException e) {
            throw new TicketPrinterException(e.getMessage(), e);
        }

        m_displaylines = new DeviceDisplayBase(this);
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
        
        m_displaylines.writeVisor(sLine1, sLine2);
    }
     
    public void clearVisor() {
        m_displaylines.clearVisor();
    }
    
    public void repaintLines() {
        try {
            m_ld.displayTextAt(0, 0, m_displaylines.getLine1(), LineDisplayConst.DISP_DT_NORMAL);
            m_ld.displayTextAt(1, 0, m_displaylines.getLine2(), LineDisplayConst.DISP_DT_NORMAL);
        } catch (JposException e) {
        }
    }
    
    public void finalize() throws Throwable {
   
        m_ld.setDeviceEnabled(false);
        m_ld.release();
        m_ld.close();
        
        super.finalize();
    }
}
