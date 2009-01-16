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

import com.openbravo.pos.ticket.TaxInfo;
import com.openbravo.pos.ticket.TicketInfo;
import java.io.BufferedInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jaroslawwozniak
 */
public class TaxDAO extends BaseJdbcDAO implements Serializable {
    
    
    public TaxInfo getTax(String id){
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sqlStr = "Select * from TAXES where category = ?";
        List<TaxInfo> list = new ArrayList<TaxInfo>();
        try {
            //get connection
            con = getConnection();
            //prepare statement
            ps = con.prepareStatement(sqlStr);

            ps.setString(1, id);
            //execute
            rs = ps.executeQuery();
            //transform to VO
            list =  transformSet(rs);
            

        } catch (Exception ex) {
            Logger.getLogger(TicketDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return list.get(0);
    }

    @Override
    protected TaxInfo map2VO(ResultSet rs) throws SQLException {
        TaxInfo tax = new TaxInfo();
        tax.setId(rs.getString("id"));
        tax.setName(rs.getString("name"));
        tax.setTaxcategoryid(rs.getString("category"));
        tax.setTaxcustcategoryid(rs.getString("custcategory"));
        tax.setParentid(rs.getString("parentid"));
        tax.setRate(rs.getDouble("rate"));
        tax.setCascade(rs.getBoolean("ratecascade"));
        tax.setOrder(rs.getInt("rateorder"));

        return tax;
    }
    
  

}
