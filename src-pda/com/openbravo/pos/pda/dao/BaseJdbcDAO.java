//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2009 Openbravo, S.L.
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
//    Foundation, Inc., 51 Franklin Street, Fifth floor, Boston, MA  02110-1301  USA

package com.openbravo.pos.pda.dao;

import com.openbravo.pos.pda.util.PropertyUtils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jaroslawwozniak
 * @version 1.0
 */
public abstract class BaseJdbcDAO {

    private PropertyUtils properties;
    
    public BaseJdbcDAO() {
        properties = new PropertyUtils();
    }

    protected Connection getConnection() throws Exception {
        try {
            Class.forName(properties.getDriverName());
            return DriverManager.getConnection(properties.getUrl(), properties.getDBUser(), properties.getDBPassword());
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();

        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return null;
    }

    protected List transformSet(ResultSet rs) throws SQLException {
        List voList = new ArrayList();
        Object vo;
        while (rs.next()) {
            vo = map2VO(rs);
            voList.add(vo);
        }
        return voList;
    }

    protected abstract Object map2VO(ResultSet rs) throws SQLException;
}