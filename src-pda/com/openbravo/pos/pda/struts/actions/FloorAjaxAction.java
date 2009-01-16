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

package com.openbravo.pos.pda.struts.actions;

import com.openbravo.pos.pda.struts.forms.FloorForm;
import com.openbravo.pos.pda.bo.RestaurantManager;
import com.openbravo.pos.ticket.Place;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;

/**
 *
 * @author Expected hash. user evaluated instead to freemarker.template.SimpleScalar on line 20, column 14 in Templates/Struts/StrutsAction.java.
 */
public class FloorAjaxAction extends org.apache.struts.action.Action {

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


        /*     HttpSession session = request.getSession();
        if(session.getAttribute("user") == null){
        ActionMessages errors = new ActionErrors();
        ActionMessage msg = new ActionMessage("errors.nologon", "nouser");
        errors.add(ActionMessages.GLOBAL_MESSAGE, msg);
        saveErrors(request, errors);
        return mapping.findForward(FAILURE);
        } 
         */
        FloorForm formFloor = (FloorForm) form;
        RestaurantManager manager = new RestaurantManager();
        List places = new ArrayList<Place>();

        places = formFloor.getFloorId() != null ? manager.findAllPlaces((String) formFloor.getFloorId()) : manager.findAllPlaces(manager.findTheFirstFloor());
        //System.out.print(places.get(0));
        request.getSession().setAttribute("floorId", (String) formFloor.getFloorId());
        request.getSession().setAttribute("places", places);
        response.setContentType("text/html");
        return mapping.findForward(SUCCESS);

    }
}