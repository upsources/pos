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
import com.openbravo.pos.forms.AppLocal;
/**
 *
 * @author adrianromero
 */
public abstract class DeviceDisplaySerial implements DeviceDisplay {
    
    private String m_sName;    
    protected PrinterWritter display;     
    
    /** Creates a new instance of DeviceDisplayESCPOS */
    public DeviceDisplaySerial(PrinterWritter display) {        
        
        m_sName = AppLocal.getIntString("Printer.Serial");
        this.display = display;
        initVisor();        
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

    public abstract void initVisor();
    public abstract void writeVisor(String sLine1, String sLine2);    
    public abstract void clearVisor();
}
