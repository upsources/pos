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

package com.openbravo.pos.printer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Date;

public class DeviceDisplayBase {
    
    private DeviceDisplayImpl impl;
    
    private String m_sLine1;
    private String m_sLine2;    
    private DateFormat df;
    private javax.swing.Timer m_tTimeTimer;    
    
    /** Creates a new instance of DeviceDisplayBase */
    public DeviceDisplayBase(DeviceDisplayImpl impl) {
        this.impl = impl;
        
        m_sLine1 = "                    "; // 20 espacios
        m_sLine2 = "                    "; // 20 espacios      
        df = DateFormat.getTimeInstance();
        m_tTimeTimer = new javax.swing.Timer(250, new PrintTimeAction());
    }
    
    public void writeVisor(String sLine1, String sLine2) {
        m_tTimeTimer.stop();
        m_sLine1 = DeviceTicket.alignLeft(sLine1, 20);
        m_sLine2 = DeviceTicket.alignLeft(sLine2, 20);
        impl.repaintLines();
    }

    public void writeTimeVisor(String sLine1) {    
        m_tTimeTimer.stop();        
        m_sLine1 = DeviceTicket.alignCenter(sLine1, 20);
        m_sLine2 = getLineTimer();
        impl.repaintLines();
        
        m_tTimeTimer.start();
    }
     
    public void clearVisor() {
        writeVisor("", "");
    }
    
    public String getLine1() {
        return m_sLine1;
    }
    
    public String getLine2() {
        return m_sLine2;
    }
    
    private String getLineTimer() {
        return DeviceTicket.alignRight(df.format(new Date()), 20);
    }
    
    private class PrintTimeAction implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            m_sLine2 = getLineTimer();
            impl.repaintLines();
        }        
    }    
}
