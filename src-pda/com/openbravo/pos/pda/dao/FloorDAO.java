/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.pda.dao;

import com.openbravo.pos.ticket.Floor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author openbravo
 */
public class FloorDAO extends BaseJdbcDAO {

    public List<Floor> findAllFloors() {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Floor> vos = null;
        String sqlStr = "select * from FLOORS";

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

    @Override
    protected Floor map2VO(ResultSet rs) throws SQLException {

        Floor floor = new Floor();
        floor.setId(rs.getString("id"));
        floor.setImage(rs.getString("image"));
        floor.setName(rs.getString("name"));

        return floor;
    }
}
