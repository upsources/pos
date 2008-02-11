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

import com.openbravo.format.Formats;
import com.openbravo.basic.BasicException;

public class VectorerBasic implements Vectorer {
    
    private int[] m_aiIndex;
    private String[] m_asHeaders;
    private Formats[] m_aFormats;
    
//    /** Creates a new instance of VectorerBasic */
//    public VectorerBasic(String[] asHeaders, Formats[] aFormats) {
//        
//        m_aFormats = aFormats;  // los nulos se saltan el elemento del array
//        
//        String[] asTempHeaders = new String[aFormats.length];
//        int[] aiTempIndex = new int[aFormats.length];
//        int ivalues = 0;
//        for (int i = 0; i < m_aFormats.length; i++) {
//            if (m_aFormats[i] != null) {
//                aiTempIndex[ivalues] = i;
//                asTempHeaders[ivalues] = asHeaders[i];
//                ivalues++;
//            }
//        }
//        
//        m_asHeaders = new String[ivalues];
//        System.arraycopy(asTempHeaders, 0, m_asHeaders, 0, ivalues);
//        m_aiIndex = new int[ivalues];
//        System.arraycopy(aiTempIndex, 0, m_aiIndex, 0, ivalues);
//    }
    
    public VectorerBasic(String[] asHeaders, Formats[] aFormats, int[] aiIndex) {
        m_asHeaders = asHeaders;
        m_aFormats = aFormats;
        m_aiIndex = aiIndex;
    }
      
    public String[] getHeaders() throws BasicException {
        
        String[] asvalues = new String[m_aiIndex.length];
        for (int i = 0; i < m_aiIndex.length; i++) {
            asvalues[i] = m_asHeaders[m_aiIndex[i]];
        }
        
        return asvalues;
    }
    
    public String[] getValues(Object obj) throws BasicException {
        Object[] avalues = (Object[]) obj;
        String[] asvalues = new String[m_aiIndex.length];
        for (int i = 0; i < m_aiIndex.length; i++) {
            asvalues[i] = m_aFormats[m_aiIndex[i]].formatValue(avalues[m_aiIndex[i]]);
        }
        
        return asvalues;
    }    
}
