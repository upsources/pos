/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openbravo.pos.pda.struts.actions;

import com.openbravo.pos.pda.bo.RestaurantManager;
import com.openbravo.pos.ticket.CategoryInfo;
import com.openbravo.pos.ticket.ProductInfo;
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
 * @author Expected hash. user evaluated instead to freemarker.template.SimpleScalar on line 20, column 14 in Templates/Struts/StrutsAction.java.
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
    public ActionForward execute(ActionMapping mapping, ActionForm  form,
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
        List products = new ArrayList<ProductInfo>();
        products = manager.findProductsByCategory(manager.findAllCategories().get(0).getId());
        request.setAttribute("products", products);
        request.getSession().setAttribute("place", (String)inputFormPlace.get("place"));   
        request.setAttribute("categories", categories);
        request.setAttribute("floorId", request.getAttribute("floorId"));
        
        
        return mapping.findForward(SUCCESS);
        
    }
}