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

import com.openbravo.pos.ticket.CategoryInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author jaroslawwozniak
 */
public class CategoryDAO extends BaseJdbcDAO {

    public List<CategoryInfo> findAllCategories() {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<CategoryInfo> vos = null;
        String sqlStr = "select * from CATEGORIES where PARENTID IS NULL";

        try {
            //get connection
            con = getConnection();
            //prepare statement
            ps = con.prepareStatement(sqlStr);
            //execute
            rs = ps.executeQuery();
            //transform to VO
            vos = transformSet(rs);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                // close the resources 
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException sqlee) {
                sqlee.printStackTrace();
            }
        }

        return vos;
    }

    public String findFirstCategory() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<CategoryInfo> vos = null;
        String sqlStr = "select * from CATEGORIES where PARENTID IS NULL";

        try {
            //get connection
            con = getConnection();
            //prepare statement
            ps = con.prepareStatement(sqlStr);
            //execute
            rs = ps.executeQuery();
            //transform to VO
            vos = transformSet(rs);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                // close the resources
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException sqlee) {
                sqlee.printStackTrace();
            }
        }

        return vos.get(0).getId();
    }

    @Override
    protected CategoryInfo map2VO(ResultSet rs) throws SQLException {
        CategoryInfo category = new CategoryInfo();
        category.setId(rs.getString("id"));
        category.setParentid(rs.getString("parentid"));
        category.setName(rs.getString("name"));

        return category;
    }
}
