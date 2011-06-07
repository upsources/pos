/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openbravo.pos.panels;

import com.openbravo.basic.BasicException;
import com.openbravo.pos.forms.AppView;
import java.sql.SQLException;
import java.util.ArrayList;

public class Company {
    
    static boolean insertHeader(AppView app, ArrayList headerAL)
            throws BasicException, SQLException {

        String sqlQuery = "INSERT INTO HEADER (ID, companyID, taxRegistrationNumber, " +
                "companyName, businessName, addressDetail, buildingNumber, city, postalCode, region, country, telephone, fax, email )VALUES(1";
        
        for (int i = 0; i < headerAL.size(); i++) {
            sqlQuery += " ,'"+headerAL.get(i).toString()+"'" ;
        }
        sqlQuery += ")";

        return DataBaseAccess.Insert(app, sqlQuery);
    }
    static void updateHeader(AppView app, ArrayList dataAL)
            throws SQLException, BasicException {
        String sqlQuery = "UPDATE HEADER SET companyID ='" + dataAL.get(1).toString() + "', taxRegistrationNumber ='" 
                + dataAL.get(2).toString() +
                "', companyName ='" + dataAL.get(3).toString() + "', businessName ='" + dataAL.get(4).toString() + "', addressDetail ='"
                + dataAL.get(5).toString() + "', buildingNumber ='" + dataAL.get(6).toString() +
                "', city ='" + dataAL.get(7).toString() + "', postalCode ='"
                + dataAL.get(8).toString() + "', region ='" + dataAL.get(9).toString() +
                "', country ='" + dataAL.get(12).toString() + "', telephone ='"
                + dataAL.get(10).toString() + "', fax ='" + dataAL.get(11).toString() +
                "', email ='" + dataAL.get(12).toString() +
                "' WHERE ID = 1";

        int updateResult = DataBaseAccess.Update(app, sqlQuery);
    }

}
