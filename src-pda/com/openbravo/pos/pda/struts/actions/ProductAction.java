//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2009 Openbravo, S.L.
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

package com.openbravo.pos.pda.struts.actions;

import com.openbravo.pos.pda.bo.RestaurantManager;
import com.openbravo.pos.ticket.CategoryInfo;
import com.openbravo.pos.ticket.ProductInfoExt;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.DynaActionForm;

/**
 *
 * @author jaroslawwozniak
 */
public class ProductAction extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private final static String SUCCESS = "success";
    private final static String FAILURE = "failure";

    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        /*  HttpSession session = request.getSession();
        if(session.getAttribute("user") == null){
        ActionMessages errors = new ActionErrors();
        ActionMessage msg = new ActionMessage("errors.nologon", "nouser");
        errors.add(ActionMessages.GLOBAL_MESSAGE, msg);
        saveErrors(request, errors);
        return mapping.findForward(FAILURE);
        }
         * */
        DynaActionForm inputFormPlace = (DynaActionForm) form;
        RestaurantManager manager = new RestaurantManager();
        List categories = new ArrayList<CategoryInfo>();
        categories = manager.findAllCategories();
        List products = new ArrayList<ProductInfoExt>();
        products = manager.findProductsByCategory(manager.findAllCategories().get(0).getId());
        request.setAttribute("products", products);
        request.getSession().setAttribute("place", (String) inputFormPlace.get("place"));
        request.setAttribute("placeName", manager.findPlaceById((String) inputFormPlace.get("place")));
        request.setAttribute("categories", categories);
        request.setAttribute("floorId", request.getAttribute("floorId"));


        return mapping.findForward(SUCCESS);

    }
}