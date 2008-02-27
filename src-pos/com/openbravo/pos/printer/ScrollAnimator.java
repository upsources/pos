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

package com.openbravo.pos.printer;

/**
 *
 * @author adrianromero
 */
public class ScrollAnimator implements DisplayAnimator {
    
    protected String baseLine1;
    protected String baseLine2;
    protected String currentLine1;
    protected String currentLine2; 
    
    public ScrollAnimator(String line1, String line2) {
        baseLine1 = line1;
        baseLine2 = line2;
    }

    public void setTiming(int i) {
        int j = i % 20;
//        if (j < 20) {
            currentLine1 = DeviceTicket.alignLeft(baseLine1.substring(j), 20);
            currentLine2 = DeviceTicket.alignLeft(baseLine2.substring(j), 20);
//        } else {
//        }
    }

    public String getLine1() {
        return currentLine1;
    }

    public String getLine2() {
        return currentLine2;
    }
}
