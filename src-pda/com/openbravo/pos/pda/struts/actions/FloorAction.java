/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openbravo.pos.pda.struts.actions;

import com.openbravo.pos.pda.bo.RestaurantManager;
import com.openbravo.pos.ticket.Floor;
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
 * @author openbravo
 */
public class FloorAction extends org.apache.struts.action.Action {

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
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        /*HttpSession session = request.getSession();
        if(session.getAttribute("user") == null){
        ActionMessages errors = new ActionErrors();
        ActionMessage msg = new ActionMessage("errors.nologon", "nouser");
        errors.add(ActionMessages.GLOBAL_MESSAGE, msg);
        saveErrors(request, errors);
        return mapping.findForward(FAILURE);
        }
         */
        RestaurantManager manager = new RestaurantManager();
        ArrayList<Floor> floors = new ArrayList<Floor>();
        floors = (ArrayList<Floor>) manager.findAllFloors();
        request.setAttribute("floors", floors);
        List places = new ArrayList<Place>();

        places = manager.findAllPlaces(floors.get(0).getId());
        //System.out.print(places.get(0));
        request.getSession().setAttribute("places", places);

        return mapping.findForward(SUCCESS);


    }
}