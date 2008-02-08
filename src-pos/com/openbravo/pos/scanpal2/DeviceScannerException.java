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


package com.openbravo.pos.scanpal2;

/**
 *
 * @author adrianromero
 */
public class DeviceScannerException extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>DeviceScannerException</code> without detail message.
     */
    public DeviceScannerException() {
    }
    
    
    /**
     * Constructs an instance of <code>DeviceScannerException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public DeviceScannerException(String msg) {
        super(msg);
    }
    public DeviceScannerException(String msg, Throwable cause) {
        super(msg, cause);
    }    
    public DeviceScannerException(Throwable cause) {
        super(cause);
    }       
}
