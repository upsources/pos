/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.pda.dao;

import com.openbravo.pos.ticket.Place;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author jaroslawozniak
 */
public class PlaceDAO extends BaseJdbcDAO {

    public List<Place> findAllPlaceByFloor(String floorId) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Place> vos = null;
        String sqlStr = "Select * from PLACES where Floor = ?";

        try {
            //get connection
            con = getConnection();
            //prepare statement
            ps = con.prepareStatement(sqlStr);
            ps.setString(1, floorId);
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

    public void setTableBusy(String placeId, String placeName){

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sqlStr = "INSERT INTO SHAREDTICKETS (ID, NAME) VALUES (?, ?)";

        try {
            //get connection
            con = getConnection();
            //prepare statement
            ps = con.prepareStatement(sqlStr);
            ps.setString(1, placeId);
            ps.setString(2, placeName);
            //execute
            rs = ps.executeQuery();
            
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

    }

    @Override
    protected Place map2VO(ResultSet rs) throws SQLException {

        Place place = new Place();
        place.setId(rs.getString("id"));
        place.setName(rs.getString("name"));
        place.setX(rs.getInt("x"));
        place.setY(rs.getInt("y"));
        place.setFloor(rs.getString("floor"));

        return place;
    }
}
