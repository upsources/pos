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
//    Foundation, Inc., 51 Franklin Street, Fifth floor, Boston, MA  02110-1301  USA

package com.openbravo.pos.pda.dao;

import com.openbravo.pos.ticket.StringUtils;
import com.openbravo.pos.ticket.UserInfo;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author jaroslawwozniak
 */
public class LoginDAO extends BaseJdbcDAO {

    public UserInfo findUser(String login, String password) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        UserInfo user = null;
        String sqlStr = "select * from PEOPLE where name = ? and (apppassword = ? || apppassword is null)";

        try {
            //get connection
            con = getConnection();
            //prepare statement
            ps = con.prepareStatement(sqlStr);
            ps.setString(1, login);
            ps.setString(2, hashString(password));
            //execute
            rs = ps.executeQuery();
            //transform to VO
            user = map2VO(rs);

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

        return user;
    }

    @Override
    protected UserInfo map2VO(ResultSet rs) throws SQLException {
        UserInfo user = new UserInfo();
        rs.next();
        user.setLogin(rs.getString("name"));
        user.setPassword(rs.getString("apppassword"));
        return user;
    }

    private static String hashString(String sPassword) {

        if (sPassword == null || sPassword.equals("")) {
            return "empty:";
        } else {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-1");
                md.update(sPassword.getBytes("UTF-8"));
                byte[] res = md.digest();
                return "sha1:" + StringUtils.byte2hex(res);
            } catch (NoSuchAlgorithmException e) {
                return "plain:" + sPassword;
            } catch (UnsupportedEncodingException e) {
                return "plain:" + sPassword;
            }
        }
    }
}
