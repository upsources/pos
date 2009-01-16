/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.pda.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author openbravo
 */
public abstract class BaseJdbcDAO {

    private String driverName = "com.mysql.jdbc.Driver";
    private String dbURL = "jdbc:mysql://localhost:3306/obpos";
    private String dbUser = "user";
    private String dbPassword = "user";

    public BaseJdbcDAO(String driverName, String dbURL, String dbUser,
            String dbPassword) {
       /* this.driverName = driverName;
        this.dbURL = dbURL;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
        * */
    }

    public BaseJdbcDAO() {
       // this("driverName", "dbURL", "dbUser", "dbPassword");
    }

    protected Connection getConnection() throws Exception {
        try {
            Class.forName(driverName);
            return DriverManager.getConnection(dbURL, dbUser, dbPassword);
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