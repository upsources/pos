/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openbravo.pos.pda.struts.actions;

import com.openbravo.pos.pda.bo.RestaurantManager;
import com.openbravo.pos.pda.struts.forms.AddedProductForm;
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
public class addProductAction extends org.apache.struts.action.Action {
    
    /* forward name="success" path="" */
    private final static String SUCCESS = "success";
    
    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */

    //niepottzebne
    public ActionForward execute(ActionMapping mapping, ActionForm  form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        AddedProductForm aForm = (AddedProductForm) form;
        RestaurantManager bo = new RestaurantManager();
        String place =  aForm.getPlace();
        String category = aForm.getCat();
        String[] array = aForm.getParameters();
        int counter = 0;
        for(int i = 0; i < array.length; i++){
            bo.addLineToTicket(place, category, array[i]);
            counter++;
        }
        request.setAttribute("floorId", aForm.getFloorId());
        request.getSession().setAttribute("place", place);
        request.getSession().setAttribute("counter", counter);
        
        return mapping.findForward(SUCCESS);
        
    }
}