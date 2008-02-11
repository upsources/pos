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

import java.sql.Connection;
import com.openbravo.format.Formats;

public class TableDefinition {
    
    private Session m_s;
    private String m_sTableName;
   
    private String[] m_asFields;
    private String[] m_asFieldsLocal;
    private Datas[] m_aDatas;
    private Formats[] m_aFormats;
    
    private int[] m_aiIDs;
   
    
    /** Creates a new instance of TableDefinition */
    public TableDefinition(
            Session s,
            String sTableName, 
            String[] asFields, String[] asFieldsLocal, Datas[] aDatas, Formats[] aFormats,
            int[] aiIDs) {
        m_s = s;
        m_sTableName = sTableName;       
        
        m_asFields = asFields;
        m_asFieldsLocal = asFieldsLocal;
        m_aDatas = aDatas;
        m_aFormats = aFormats;
  
        m_aiIDs = aiIDs;
    }  
    public TableDefinition(
            Session s,
            String sTableName, 
            String[] asFields, Datas[] aDatas, Formats[] aFormats,
            int[] aiIDs) {
        this(s, sTableName, asFields, asFields, aDatas, aFormats, aiIDs);
    }    
    
    public String getTableName() {
        return m_sTableName;
    }
    
    public String[] getFields() {
        return m_asFields;
    }
    
    public Vectorer getVectorerBasic(int[] aiFields) {
        return new VectorerBasic(m_asFieldsLocal, m_aFormats, aiFields);
    }
    
    public IRenderString getRenderStringBasic(int[] aiFields) {
        return new RenderStringBasic(m_aFormats, aiFields);
    }
    
    public ComparatorCreator getComparatorCreator(int [] aiOrders) {
        return new ComparatorCreatorBasic(m_asFieldsLocal, m_aDatas, aiOrders);
    }
    
    public IKeyGetter getKeyGetterBasic() {
        if (m_aiIDs.length == 1) {
            return new KeyGetterFirst(m_aiIDs);
        } else {
            return new KeyGetterBasic(m_aiIDs);     
        }
    }    
    
    public SerializerRead getSerializerReadBasic() {
        return new SerializerReadBasic(m_aDatas);
    }
    public SerializerWrite getSerializerInsertBasic() {
        return new SerializerWriteBasic(m_aDatas);
    }
    public SerializerWrite getSerializerDeleteBasic() {     
        return new SerializerWriteBasicExt(m_aDatas, m_aiIDs);
    }
    public SerializerWrite getSerializerUpdateBasic() {
        
        int[] aindex = new int[m_asFields.length + m_aiIDs.length];

        for (int i = 0; i < m_asFields.length; i++) {
            aindex[i] = i;
        } 
        for (int i = 0; i < m_aiIDs.length; i++) {
            aindex[i + m_asFields.length] = m_aiIDs[i];
        }       
 
        return new SerializerWriteBasicExt(m_aDatas, aindex);
    }
    
    public SentenceList getListSentence() {
        return getListSentence(getSerializerReadBasic());
    }
    
    public SentenceList getListSentence(SerializerRead sr) {
        return new PreparedSentence(m_s, getListSQL(), null,  sr);
    }
    
    public String getListSQL() {
        
        StringBuffer sent = new StringBuffer();
        sent.append("select ");

        for (int i = 0; i < m_asFields.length; i ++) {
            if (i > 0) {
                sent.append(", ");
            }
            sent.append(m_asFields[i]);
        }        
        
        sent.append(" from ");        
        sent.append(m_sTableName);
        
        return sent.toString();    
    }
   
    public SentenceExec getDeleteSentence() {
        return getDeleteSentence(getSerializerDeleteBasic());
    }
    
    public SentenceExec getDeleteSentence(SerializerWrite sw) {
        return new PreparedSentence(m_s, getDeleteSQL(), sw, null);
    }
    
    public String getDeleteSQL() {
        
        StringBuffer sent = new StringBuffer();
        sent.append("delete from ");
        sent.append(m_sTableName);
        
        for (int i = 0; i < m_aiIDs.length; i ++) {
            sent.append((i == 0) ? " where " : " and ");
            sent.append(m_asFields[m_aiIDs[i]]);
            sent.append(" = ?");
        }
        
        return sent.toString();     
    }
   
    public SentenceExec getInsertSentence() {
        return getInsertSentence(getSerializerInsertBasic());
    }
    
    public SentenceExec getInsertSentence(SerializerWrite sw) {
        return new PreparedSentence(m_s, getInsertSQL(), sw, null);
    }
    
    public String getInsertSQL() {
        
        StringBuffer sent = new StringBuffer();
        StringBuffer values = new StringBuffer();
        
        sent.append("insert into ");
        sent.append(m_sTableName);
        sent.append(" (");        
        
        for (int i = 0; i < m_asFields.length; i ++) {
            if (i > 0) {
                sent.append(", ");
                values.append(", ");
            }
            sent.append(m_asFields[i]);
            values.append("?");
        }
        
        sent.append(") values (");
        sent.append(values.toString());
        sent.append(")");

        return sent.toString();       
    }
   
    public SentenceExec getUpdateSentence() {
        return getUpdateSentence(getSerializerUpdateBasic());
    }
    
    public SentenceExec getUpdateSentence(SerializerWrite sw) {
        return new PreparedSentence(m_s, getUpdateSQL(), sw, null);
    }
    
    public String getUpdateSQL() {
        
        StringBuffer sent = new StringBuffer();
        
        sent.append("update ");
        sent.append(m_sTableName);
        sent.append(" set ");
        
        for (int i = 0; i < m_asFields.length; i ++) {
            if (i > 0) {
                sent.append(", ");
            }
            sent.append(m_asFields[i]);
            sent.append(" = ?");
        }
        
        for (int i = 0; i < m_aiIDs.length; i ++) {
            sent.append((i == 0) ? " where " : " and ");
            sent.append(m_asFields[m_aiIDs[i]]);
            sent.append(" = ?");
        }
        
        return sent.toString();               
    }
}
