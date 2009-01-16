/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openbravo.pos.pda.struts.actions;

import com.openbravo.pos.pda.bo.RestaurantManager;
import com.openbravo.pos.pda.struts.forms.FloorForm;
import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.ticket.TicketLineInfo;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.DynaActionForm;

/**
 *
 * @author openbravo
 */
public class ModifyProductAction extends org.apache.struts.action.Action {
    
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
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        FloorForm aForm = (FloorForm) form;
        RestaurantManager manager = new RestaurantManager();
  
        TicketInfo ticket = manager.findTicket(aForm.getId());
        List<TicketLineInfo> list = ticket.getM_aLines();
        String[] array = aForm.getParameters();     
        list.get(Integer.valueOf(aForm.getLine())).setMultiply(Double.valueOf(array[0]));
        manager.updateLineFromTicket(aForm.getId(), ticket);
        

        return mapping.findForward(SUCCESS);
    }
}
