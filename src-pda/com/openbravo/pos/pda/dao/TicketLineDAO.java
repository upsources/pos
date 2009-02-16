//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2008 Openbravo, S.L.
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

package com.openbravo.pos.pda.dao;


import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.ticket.TicketLineInfo;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jaroslawwozniak
 */
public class TicketLineDAO extends BaseJdbcDAO {

    public List<TicketLineInfo> findLinesByTicket(String ticketId) {
        TicketDAO ticketDao = new TicketDAO();
        TicketInfo ticket = ticketDao.getTicket(ticketId);

        return ticket.getM_aLines();

    }

    public void insertLine(TicketLineInfo line) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sqlStr = "INSERT INTO TICKETLINES (TICKET, LINE, PRODUCT, UNITS, PRICE, TAXID, ATTRIBUTES) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            //get connection
            con = getConnection();
            //prepare statement
            ps = con.prepareStatement(sqlStr);
            ps.setString(1, line.getM_sTicket());
            ps.setInt(2, line.getM_iLine());
            ps.setString(3, line.getProductid());
            ps.setDouble(4, line.getMultiply());
            ps.setDouble(5, line.getPrice());
            ps.setString(6, line.getTax().getId());
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bytes);
            out.writeObject(line.getAttributes());
            ps.setBytes(7, bytes.toByteArray());
            //execute
            ps.executeUpdate();
        } catch (Exception ex) {
            Logger.getLogger(TicketDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected Object map2VO(ResultSet rs) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
