/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.panels;

import com.openbravo.basic.BasicException;
import com.openbravo.pos.forms.AppView;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DataBaseAccess {

    static int Count(AppView app, String sqlQuery)
            throws BasicException, SQLException {

        Connection conn = app.getSession().getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sqlQuery);

        ResultSet rs = pstmt.executeQuery();
        rs.next();

        return rs.getInt(1);
    }

    static int Update(AppView app, String sqlQuery)
            throws SQLException, BasicException {

        Statement stmt = createConnection(app);
        return stmt.executeUpdate(sqlQuery);
    }
    static boolean Insert(AppView app, String sqlQuery)
            throws BasicException, SQLException {

        Statement stmt = createConnection(app);
        PreparedStatement pStmt = stmt.getConnection().prepareStatement(sqlQuery);
       
        return pStmt.execute();
    }

    static ResultSet Select(AppView app, String sqlQuery)
            throws BasicException, SQLException {

        Statement stmt = createConnection(app);
        ResultSet dataRS = stmt.executeQuery(sqlQuery);     
       // closeConnection();
        return dataRS;
    }

    static Statement createConnection(AppView app)
            throws BasicException, SQLException {
        
        Connection con = app.getSession().getConnection();
        Statement stmt = con.createStatement();
        return stmt;
    }

    static void closeConnection(Statement stmt)
            throws SQLException {

        stmt.close();
    }
}
