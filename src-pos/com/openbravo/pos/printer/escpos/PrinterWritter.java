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

public abstract class PrinterWritter {
    
    private static final Integer ACTION_FLUSH = new Integer(0);
    private static final Integer ACTION_CLOSE = new Integer(1);
    
    // private OutputStream m_out = null;
    private PrinterBuffer m_buff = null;    
    private MyDaemon m_daemon = null;
    
    public PrinterWritter() {
        m_buff = new PrinterBuffer();
        m_daemon = new MyDaemon();        
        m_daemon.start();
    }
    
    protected abstract void daemonWrite(byte[] data);
    protected abstract void daemonFlush();
    protected abstract void daemonClose();
    
    public void write(byte[] data) {
        m_buff.putData(data);
    }
    
    public void write(String sValue) {
        m_buff.putData(sValue.getBytes());
    }
    
    public void flush() {
        m_buff.putData(ACTION_FLUSH);
    }
    
    public void close() {
        m_buff.putData(ACTION_CLOSE);
    }

    private class MyDaemon extends Thread {

        public void run() {

            boolean bItsRunning = true;

            while (bItsRunning) {               
                Object data = m_buff.getData();                   
                // esperemos un poco que estoy vago
                //try {
                //    this.sleep(1000);
                //} catch (InterruptedException ei) {
                //}

                // Que hacemos con ese objeto tan raro?
                if (data instanceof byte[]) {
                    // m_out.write((byte[]) data); // Lo imprimimos
                    daemonWrite((byte[]) data);
                } else if (data == ACTION_FLUSH) { 
                    // m_out.flush(); // flush
                    daemonFlush();
                } else if (data == ACTION_CLOSE) {
                    // m_out.flush(); // flush y terminamos con el demonio
                    daemonFlush();
                    daemonClose();
                    bItsRunning = false;
                }
            }
        }
    }   
}
