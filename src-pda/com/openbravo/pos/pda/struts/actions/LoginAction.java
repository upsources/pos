/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.pda.struts.actions;

import com.openbravo.pos.pda.bo.RestaurantManager;
import com.openbravo.pos.ticket.UserInfo;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

/**
 *
 * @author jaroslawwozniak
 */
public class LoginAction extends org.apache.struts.action.Action {

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

        DynaActionForm inputForm = (DynaActionForm) form;
        RestaurantManager manager = new RestaurantManager();
        String login = inputForm.getString("login");
        String password = inputForm.getString("password");
        UserInfo user = manager.findUser(login, password);
        if ( user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            return mapping.findForward(SUCCESS);
        }
        ActionMessages errors = new ActionErrors();
        ActionMessage msg = new ActionMessage("errors.loginfailed", "login/password");
        errors.add(ActionMessages.GLOBAL_MESSAGE, msg);
        saveErrors(request, errors);

        return mapping.findForward(FAILURE);

    }
}