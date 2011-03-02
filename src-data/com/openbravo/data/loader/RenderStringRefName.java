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

package com.openbravo.data.loader;

import com.openbravo.format.Formats;

/**
 *
 * @author  Luis Ig. Bacas Riveiro	lbacas@opensistemas.com
 * @author  Pablo J. Urbano Santos	purbano@opensistemas.com
 */
public class RenderStringRefName implements IRenderString {
    
    private Formats[] m_aFormats;
    private int[] m_aiIndex;
    
    /** Creates a new instance of StringnizerBasic */
    public RenderStringRefName(Formats[] fmts, int[] aiIndex) {
        m_aFormats = fmts; 
        m_aiIndex = aiIndex;
    }
    public String getRenderString(Object value) {
        if (value == null) {
            return null; 
        } else {
            Object [] avalue = (Object[]) value;
            StringBuffer sb = new StringBuffer();
            
            if (m_aiIndex.length == 2) {
                String s1 = m_aFormats[m_aiIndex[0]].formatValue(avalue[m_aiIndex[0]]);
                String s2 = m_aFormats[m_aiIndex[1]].formatValue(avalue[m_aiIndex[1]]);

                if (s1.equals(s2)) 
                    sb.append("* ").append(s1);
                else sb.append(s1).append(" - ").append(s2);
            } else {
                for (int i = 0; i < m_aiIndex.length; i++) {
                    if (i > 0) {
                        sb.append(" - ");
                    }
                    sb.append(m_aFormats[m_aiIndex[i]].formatValue(avalue[m_aiIndex[i]]));
                }
            }
            return sb.toString();
        }
    }  
   
}
