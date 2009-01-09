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

import java.util.*;
import com.openbravo.basic.BasicException;

public abstract class BaseSentence implements SentenceList, SentenceFind, SentenceExec {
 
    // Funciones de bajo nivel    
    public abstract DataResultSet openExec(Object params) throws BasicException;
    public abstract DataResultSet moreResults() throws BasicException;
    public abstract void closeExec() throws BasicException;

    // Funciones
    public final int exec() throws BasicException {
        return exec(null);
    }

    public final int exec(Object params) throws BasicException {        
        DataResultSet SRS = openExec(params);
        if (SRS == null) {
            throw new BasicException(LocalRes.getIntString("exception.noupdatecount"));
        }
        int iResult = SRS.updateCount();
        SRS.close();
        closeExec();
        return iResult;
    }
    
    public final List list() throws BasicException {
        return list(null);
    }
    public final List list(Object params) throws BasicException {
    // En caso de error o lanza un pepinazo en forma de DataException          
        DataResultSet SRS = openExec(params);
        List aSO = fetchAll(SRS);    
        SRS.close();
        closeExec();       
        return aSO;
    }
    
    public final List listPage(int offset, int length) throws BasicException {
        return listPage(null, offset, length);
    }
    public final List listPage(Object params, int offset, int length) throws BasicException {
    // En caso de error o lanza un pepinazo en forma de DataException         
        DataResultSet SRS = openExec(params);
        List aSO = fetchPage(SRS, offset, length);    
        SRS.close();
        closeExec();       
        return aSO;
    }
    
    public final Object find() throws BasicException {
        return find((SerializableWrite) null);
    }

    public final Object find(Object... params) throws BasicException {
        return find((Object) params);
    }

    public final Object find(Object params) throws BasicException {
    // En caso de error o lanza un pepinazo en forma de SQLException          
        DataResultSet SRS = openExec(params);
        Object obj = fetchOne(SRS);  
        SRS.close();
        closeExec();       
        return obj;
    }

    // Utilidades
    public final List fetchAll(DataResultSet SRS) throws BasicException {
        if (SRS == null) {
            throw new BasicException(LocalRes.getIntString("exception.nodataset"));
        }
        
        List aSO = new ArrayList();
        while (SRS.next()) {
            aSO.add(SRS.getCurrent());
        }     
        return aSO;
    }
    
    // Utilidades
    public final List fetchPage(DataResultSet SRS, int offset, int length) throws BasicException {
        
        if (SRS == null) {
            throw new BasicException(LocalRes.getIntString("exception.nodataset"));
        }  
        
        if (offset < 0 || length < 0) {
            throw new BasicException(LocalRes.getIntString("exception.nonegativelimits"));
        }
         
        // Skip los primeros que no me importan
        while (offset > 0 && SRS.next()) {
            offset--;
        }
        
        // me traigo tantos como me han dicho
        List aSO = new ArrayList();
        if (offset == 0) {
            while (length > 0 && SRS.next()) {
                length--;
                aSO.add(SRS.getCurrent());
            }     
        }
        return aSO;            
    }
    
    public final Object fetchOne(DataResultSet SRS) throws BasicException {
                       
        if (SRS == null) {
            throw new BasicException(LocalRes.getIntString("exception.nodataset"));
        } 
        
        if (SRS.next()) {
            return SRS.getCurrent();  
        } else {
            return null;
        }
    }
    
 }
