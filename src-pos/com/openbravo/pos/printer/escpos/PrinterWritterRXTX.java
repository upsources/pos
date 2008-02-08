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

package com.openbravo.pos.printer.escpos;

// import javax.comm.*; // Java comm library
import gnu.io.*; // RXTX comm library

import java.io.*;
import com.openbravo.pos.printer.*;

public class PrinterWritterRXTX extends PrinterWritter /* implements SerialPortEventListener */ {
    
    private CommPortIdentifier m_PortIdPrinter;
    private CommPort m_CommPortPrinter;  
    
    private String m_sPortPrinter;
    private OutputStream m_out;
    
    /** Creates a new instance of PrinterWritterComm */
    public PrinterWritterRXTX(String sPortPrinter) throws TicketPrinterException {
        m_sPortPrinter = sPortPrinter;
        m_out = null;
        
        write(ESCPOS.INIT); // Arrancamos la impresora
        flush();        
    }
    
    protected void daemonWrite(byte[] data) {
        try {  
            if (m_out == null) {
                m_PortIdPrinter = CommPortIdentifier.getPortIdentifier(m_sPortPrinter); // Tomamos el puerto                   
                m_CommPortPrinter = m_PortIdPrinter.open("PORTID", 2000); // Abrimos el puerto       

                m_out = m_CommPortPrinter.getOutputStream(); // Tomamos el chorro de escritura   

                if (m_PortIdPrinter.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                    ((SerialPort)m_CommPortPrinter).setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE); // Configuramos el puerto
                } else if (m_PortIdPrinter.getPortType() == CommPortIdentifier.PORT_PARALLEL) {
                    ((ParallelPort)m_CommPortPrinter).setMode(1);
                }
            }
            m_out.write(data);
        } catch (NoSuchPortException e) {
        } catch (PortInUseException e) {
        } catch (UnsupportedCommOperationException e) {
        } catch (IOException e) {
        }      
    }
    
    protected void daemonFlush() {
        try {  
            if (m_out != null) {
                m_out.flush();
            }
        } catch (IOException e) {
        }    
    }
    
    protected void daemonClose() {
        try {  
            if (m_out != null) {
                m_out.flush();
                m_out.close();
                m_out = null;
                m_CommPortPrinter = null;
                m_PortIdPrinter = null;
            }
        } catch (IOException e) {
        }    
    }
}
