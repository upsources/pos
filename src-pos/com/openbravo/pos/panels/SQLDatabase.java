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

package com.openbravo.pos.panels;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import javax.swing.tree.TreeNode;

/**
 *
 * @author adrianromero
 */
public class SQLDatabase implements TreeNode {
    
    private ArrayList m_aTables;
    private HashMap m_mTables;
    private String m_sName;
    
    /** Creates a new instance of SQLDatabase */
    public SQLDatabase(String name) {
        m_sName = name;
        m_aTables = new ArrayList();
        m_mTables = new HashMap();
    }
    public String toString() {
        return m_sName;
    }
    
    public void addTable(String sTable) {
        SQLTable t = new SQLTable(this, sTable);
        m_aTables.add(t);
        m_mTables.put(sTable, t);
    }
    public SQLTable getTable(String sTable) {
        return (SQLTable) m_mTables.get(sTable);
    }
    
    public Enumeration children(){
        return new EnumerationIter(m_aTables.iterator());
    }
    public boolean getAllowsChildren() {
        return true;
    }
    public TreeNode getChildAt(int childIndex) {
        return (TreeNode) m_aTables.get(childIndex);
    }
    public int getChildCount() {
        return m_aTables.size();
    }
    public int getIndex(TreeNode node){
        return m_aTables.indexOf(node);
    }
    public TreeNode getParent() {
        return null;
    }
    public boolean isLeaf() {
        return m_aTables.size() == 0;
    }    
}
