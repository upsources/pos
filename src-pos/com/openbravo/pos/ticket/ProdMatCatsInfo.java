package com.openbravo.pos.ticket;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.IKeyed;
import com.openbravo.data.loader.SerializableRead;

/**
 *
 * @author stas
 */
public class ProdMatCatsInfo implements SerializableRead, IKeyed {
    protected String c_ID;

    public ProdMatCatsInfo() {
        c_ID = null;
    }

    public void readValues(DataRead dr) throws BasicException {
        c_ID = dr.getString(1);
    }

    public Object getKey() {
        return c_ID;
    }

    public String getMatCatID() {
        return c_ID;
    }
    
}
