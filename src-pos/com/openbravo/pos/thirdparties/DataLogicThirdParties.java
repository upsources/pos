//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007 Openbravo, S.L.
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

package com.openbravo.pos.thirdparties;

import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.Session;
import com.openbravo.data.loader.TableDefinition;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.BeanFactoryDataSingle;

public class DataLogicThirdParties extends BeanFactoryDataSingle {
    
    private TableDefinition m_tthirdparties;
 
    /** Creates a new instance of DataLogicAdmin */
    public DataLogicThirdParties() {
    }
    
    public void init(Session s){

        m_tthirdparties = new TableDefinition(s,
            "THIRDPARTIES"
            , new String[] {"ID", "CIF", "NAME", "ADDRESS", "CONTACTCOMM", "CONTACTFACT", "PAYRULE", "FAXNUMBER", "PHONENUMBER", "MOBILENUMBER", "EMAIL", "WEBPAGE", "NOTES"}
            , new Datas[] {Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING}
            , new Formats[] {Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING}
            , new int[] {0}
        );   
        
    }
       
    public final TableDefinition getTableThirdParties() {
        return m_tthirdparties;
    }    
}

