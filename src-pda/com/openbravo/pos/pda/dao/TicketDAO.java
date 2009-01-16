/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openbravo.pos.pda.dao;

import com.openbravo.pos.ticket.TicketInfo;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jaroslawwozniak
 */
public class TicketDAO extends BaseJdbcDAO implements Serializable {

    public TicketInfo getTicket(String id) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sqlStr = "Select CONTENT from SHAREDTICKETS where ID = ?";
        TicketInfo ticket = new TicketInfo();
        try {
            //get connection
            con = getConnection();
            //prepare statement
            ps = con.prepareStatement(sqlStr);

            ps.setString(1, id);
            //execute
            rs = ps.executeQuery();
            //transform to VO
            rs.next();
            //System.out.println(new ObjectInputStream(new BufferedInputStream(rs.getBinaryStream(1))));
            ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(rs.getBinaryStream(1)));
            ticket = (TicketInfo) in.readObject();

        } catch (Exception ex) {
            Logger.getLogger(TicketDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return ticket;
    }

    public void initTicket(String id) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sqlStr = "INSERT INTO SHAREDTICKETS (ID, NAME,CONTENT) VALUES (?, ?, ?)";
        TicketInfo ticket = new TicketInfo();
        try {
            //get connection
            con = getConnection();
            //prepare statement
            ps = con.prepareStatement(sqlStr);
            ps.setString(1, id);
            ps.setString(2, ticket.getName());
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bytes);
            out.writeObject(ticket);
            ps.setBytes(3, bytes.toByteArray());
         //   System.out.println(bytes.toString());
            //execute
            ps.executeUpdate();
            //transform to VO
           // System.out.println("poszlo  " + id);
            //System.out.println(new ObjectInputStream(new BufferedInputStream(rs.getBinaryStream(1))));
        } catch (Exception ex) {
            Logger.getLogger(TicketDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateTicket(String ticketId, TicketInfo ticket){
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sqlStr = "UPDATE SHAREDTICKETS SET SHAREDTICKETS.CONTENT = ? WHERE SHAREDTICKETS.ID = ?";
        try {
            //get connection
            con = getConnection();
            //prepare statement
            ps = con.prepareStatement(sqlStr);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bytes);
            out.writeObject(ticket);
            ps.setBytes(1, bytes.toByteArray());
            ps.setString(2, ticketId);
            //execute
            ps.executeUpdate();
        } catch (Exception ex) {
            Logger.getLogger(TicketDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected TicketInfo map2VO(ResultSet rs) throws SQLException {
        ObjectInputStream in = null;
        TicketInfo ticket = new TicketInfo();

        return ticket;

    }
}
