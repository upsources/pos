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

package com.openbravo.pos.forms;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author adrianromero
 */
public class DriverWrapper implements Driver {
    
    private Driver driver;
    
    public DriverWrapper(Driver d) {
        driver = d;
    }
    public boolean acceptsURL(String u) throws SQLException {
        return driver.acceptsURL(u);
    }
    public Connection connect(String u, Properties p) throws SQLException {
        return driver.connect(u, p);
    }
    public int getMajorVersion() {
        return driver.getMajorVersion();
    }
    public int getMinorVersion() {
        return driver.getMinorVersion();
    }
    public DriverPropertyInfo[] getPropertyInfo(String u, Properties p) throws SQLException {
        return driver.getPropertyInfo(u, p);
    }
    public boolean jdbcCompliant() {
        return driver.jdbcCompliant();
    }
}