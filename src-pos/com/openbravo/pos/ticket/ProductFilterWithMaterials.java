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

import java.util.List;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;

/**
 *
 * @author  Luis Ig. Bacas Riveiro	lbacas@opensistemas.com
 * @author  Pablo J. Urbano Santos	purbano@opensistemas.com
 */
public class ProductFilterWithMaterials extends ProductFilter {

    /** Creates new form JQBFProduct */
    public ProductFilterWithMaterials() {
        super();
    }
    
    @Override
    public void activate() throws BasicException {        
        CategoryInfo catinfo;

        // El modelo de categorias
        m_CategoryModel = new ComboBoxValModel();    
        List catlist = m_dlSales.getCategoriesList().list();
        catlist.addAll(m_dlSales.getCategoryMaterial());
        //Eliminamos de la lista de categorias la categoria Composiciones
        for (int i=0; i < catlist.size(); i++) {
            catinfo = (CategoryInfo)catlist.get(i);
            if (catinfo.getID().equals("0")) {
                catlist.remove(i);
                break;
            } 
        }
        
        catlist.add(0, null);
        m_CategoryModel.refresh(catlist);
        m_jCategory.setModel(m_CategoryModel);
    }
}