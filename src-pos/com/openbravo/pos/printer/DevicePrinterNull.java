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

package com.openbravo.pos.printer;

import com.openbravo.pos.forms.AppLocal;

public class DevicePrinterNull implements DevicePrinter {
    
    private String m_sName;
    private String m_sDescription;
    
    /** Creates a new instance of DevicePrinterNull */
    public DevicePrinterNull() {
        this(null);
    }
    
    /** Creates a new instance of DevicePrinterNull */
    public DevicePrinterNull(String desc) {
        m_sName = AppLocal.getIntString("Printer.Null");
        m_sDescription = desc;
    }

    public String getPrinterName() {
        return m_sName;
    }    
    public String getPrinterDescription() {
        return m_sDescription;
    }        
    public javax.swing.JComponent getPrinterComponent() {
        return null;
    }
    public void reset() {
    }
    
    public void printBarCode(String sType, String sCode) {
    }    
    public void printCutPartial() {
    }
    public void printImage(java.awt.image.BufferedImage image) {
    }
    public void beginLine(int iTextSize) {
    }   
    public void printText(int iStyle, String sText) {
    }   
    public void endLine() {
    }
    public void openDrawer() {
    }    
}
