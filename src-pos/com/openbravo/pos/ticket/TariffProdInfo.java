//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2008 Open Sistemas de Información Internet, S.L.
//    http://www.opensistemas.com
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
//    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

package com.openbravo.pos.ticket;

import java.util.*;
import java.io.*;
import com.openbravo.pos.util.*;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;
import com.openbravo.basic.BasicException;

/**
 *
 * @author  Luis Ig. Bacas Riveiro	lbacas@opensistemas.com
 * @author  Pablo J. Urbano Santos	purbano@opensistemas.com
 */
public class TariffProdInfo implements SerializableRead, SerializableWrite {

    private String m_sProdId;
    private double m_dPriceSell;

    public TariffProdInfo(String sId, double dPrice) {
        m_sProdId = sId;
        m_dPriceSell = dPrice; 
    }

    public TariffProdInfo(String sId) {
        this(sId, 0.0);
    }

    public TariffProdInfo() {
        this(null, 0.0);
    }


    public void readValues(DataRead dr) throws BasicException {
        m_sProdId = dr.getString(1);
        m_dPriceSell = dr.getDouble(2);
    }

    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, m_sProdId);
        dp.setDouble(2, m_dPriceSell);
    }

}
