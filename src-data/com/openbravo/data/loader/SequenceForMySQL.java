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

package com.openbravo.data.loader;

import com.openbravo.basic.BasicException;


public class SequenceForMySQL extends BaseSentence {
    
    private BaseSentence sent1;
    private BaseSentence sent2;
    
    /** Creates a new instance of SequenceForMySQL */
    public SequenceForMySQL(Session s, String sSeqTable, SerializerRead serread) {
        
        sent1 = new StaticSentence(s, "UPDATE " + sSeqTable + " SET ID = LAST_INSERT_ID(ID + 1)");
        sent2 = new StaticSentence(s, "SELECT LAST_INSERT_ID()", null, serread);
    }
    
    // Funciones de bajo nivel    
    public DataResultSet openExec(Object params) throws BasicException {        
        sent1.exec();
        return sent2.openExec(null);
    }   
    public DataResultSet moreResults() throws BasicException {
        return sent2.moreResults();
    }
    public void closeExec() throws BasicException {
        sent2.closeExec();
    }
}
