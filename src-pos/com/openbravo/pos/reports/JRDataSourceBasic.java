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

package com.openbravo.pos.reports;

import com.openbravo.pos.forms.AppLocal;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRException;

import com.openbravo.data.loader.BaseSentence;
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataResultSet;

public class JRDataSourceBasic implements JRDataSource {
    
    private BaseSentence sent;
    private DataResultSet SRS = null;
    private Object current = null;
    
    private ReportFields m_fields = null;
    
    /** Creates a new instance of JRDataSourceBasic */
    public JRDataSourceBasic(BaseSentence sent, ReportFields fields, Object params) throws BasicException  {   
        
        this.sent = sent;
        SRS = sent.openExec(params);
        m_fields = fields;
    }
    
    public Object getFieldValue(JRField jrField) throws JRException {
        
        try {
            return m_fields.getField(current,  jrField.getName());
        } catch (ReportException er) {
            throw new JRException(er);
        }
    }
    
    public boolean next() throws JRException {
        
        if (SRS == null) {
            throw new JRException(AppLocal.getIntString("exception.unavailabledataset"));
        }
        
        try {
            if (SRS.next()) {
                current = SRS.getCurrent();
                return true;
            } else {                
                current = null;
                SRS = null;
                sent.closeExec();
                sent = null;
                return false;
            }                
        } catch (BasicException e) {
            throw new JRException(e);
        }      
    }
}
