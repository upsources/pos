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
//    Foundation, Inc., 51 Franklin Street, Fifth floor, Boston, MA  02110-1301  USA

package com.openbravo.data.gui;

import javax.swing.*;
import java.util.*;
import com.openbravo.data.loader.IKeyGetter;
import com.openbravo.data.loader.KeyGetterBuilder;

/**
 *
 * @author  adrian
 */
public class ComboBoxValModel extends AbstractListModel implements ComboBoxModel {  
   
    private List m_aData;
    private IKeyGetter m_keygetter;
    private Object m_selected;
    
    /** Creates a new instance of ComboBoxValModel */
    public ComboBoxValModel(List aData, IKeyGetter keygetter) {
        m_aData = aData;
        m_keygetter = keygetter;
        m_selected = null;
    }
    public ComboBoxValModel(List aData) {
        this(aData, KeyGetterBuilder.INSTANCE);
    }
    public ComboBoxValModel(IKeyGetter keygetter) {
        this(new ArrayList(), keygetter);
    }
    public ComboBoxValModel() {
        this(new ArrayList(), KeyGetterBuilder.INSTANCE);
    }
    
    public void add(Object c) {
        m_aData.add(c);
    }

    public void add(int index, Object c) {
        m_aData.add(index, c);
    }
    
    public void refresh(List aData) {
        m_aData = aData;
        m_selected = null;
    }
    
    public Object getSelectedKey() {
        if (m_selected == null) {
            return null;
        } else {
            return m_keygetter.getKey(m_selected);  // Si casca, excepcion parriba
        }
    }

    public String getSelectedText() {
        if (m_selected == null) {
            return null;
        } else {
            return m_selected.toString();
        }
    }
    
    public void setSelectedKey(Object aKey) {
        setSelectedItem(getElementByKey(aKey));
    }
    
    public void setSelectedFirst() {
        m_selected = (m_aData.size() == 0) ? null : m_aData.get(0);
    }
    
    public Object getElementByKey(Object aKey) {
        if (aKey != null) {
            Iterator it = m_aData.iterator();
            while (it.hasNext()) {
                Object value = it.next();
                if (aKey.equals(m_keygetter.getKey(value))) {
                    return value;
                }
            }           
        }
        return null;
    }
    
    public Object getElementAt(int index) {
        return m_aData.get(index);
    }
    
    public Object getSelectedItem() {
        return m_selected;
    }
    
    public int getSize() {
        return m_aData.size();
    }
    
    public void setSelectedItem(Object anItem) {
        
        if ((m_selected != null && !m_selected.equals(anItem)) || m_selected == null && anItem != null) {
            m_selected = anItem;
            fireContentsChanged(this, -1, -1);
        }
    }
    
}
