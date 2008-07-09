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

package com.openbravo.pos.customers;

import com.openbravo.format.Formats;
import java.util.Date;

/**
 *
 * @author adrianromero
 */
public class CustomerInfoExt extends CustomerInfo {
        
    private String card;
    private String address;
    private String notes;
    private Double maxdebt;
    private boolean visible;
    private Date curdate;
    private Double curdebt;
    
    /** Creates a new instance of UserInfoBasic */
    public CustomerInfoExt(String id, String taxid, String searchkey, String name) {
        super(id, taxid, searchkey, name);
    } 
    
    @Override
    public String toString() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Double getMaxdebt() {
        return maxdebt;
    }

    public void setMaxdebt(Double maxdebt) {
        this.maxdebt = maxdebt;
    }

    public Date getCurdate() {
        return curdate;
    }

    public void setCurdate(Date curdate) {
        this.curdate = curdate;
    }

    public Double getCurdebt() {
        return curdebt;
    }

    public void setCurdebt(Double curdebt) {
        this.curdebt = curdebt;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    public String printCurDebt() {
        
        return Formats.CURRENCY.formatValue(curdebt == null ? new Double(0.0) : curdebt);
    }
}
