//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2009 Openbravo, S.L.
//    http://www.openbravo.com/product/pos
//
//    This file is part of Openbravo POS.
//
//    Openbravo POS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    Openbravo POS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with Openbravo POS.  If not, see <http://www.gnu.org/licenses/>.

package com.openbravo.pos.epm;

import com.openbravo.data.loader.IKeyed;

/**
 *
 * @author  Ali Safdar & Aneeqa Baber
 */
public class LeavesInfo implements IKeyed {

    private static final long serialVersionUID = 8936482715929L;
    private String m_sID;
    private String m_sName;
    private String m_sEmployeeID;
    private String m_sStartDate;
    private String m_sEndDate;
    private String m_sNotes;


    /** Creates new LeavesInfo */
    public LeavesInfo(String id, String name, String employeeid, String startdate, String enddate, String notes) {
        m_sID = id;
        m_sName = name;
        m_sEmployeeID = employeeid;
        m_sStartDate = startdate;
        m_sEndDate = enddate;
        m_sNotes = notes;
    }

    public Object getKey() {
        return m_sID;
    }

    public void setID(String sID) {
        m_sID = sID;
    }

    public String getID() {
        return m_sID;
    }

    public String getName() {
        return m_sName;
    }

    public void setName(String sName) {
        m_sName = sName;
    }

    @Override
    public String toString(){
        return m_sName;
    }

    public String getEmployeeID() {
        return m_sEmployeeID;
    }

    public void setEmployeeID(String EmployeeID) {
        this.m_sEmployeeID = EmployeeID;
    }

    public String getStartDate() {
        return m_sStartDate;
    }

    public void setStartDate(String StartDate) {
        this.m_sStartDate = StartDate;
    }

    public String getEndDate() {
        return m_sEndDate;
    }

    public void setEndDate(String EndDate) {
        this.m_sEndDate = EndDate;
    }

    public String getNotes() {
        return m_sNotes;
    }

    public void setNotes(String Notes) {
        this.m_sNotes = Notes;
    }
}
