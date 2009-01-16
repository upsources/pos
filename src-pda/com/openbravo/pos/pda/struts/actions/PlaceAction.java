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

import com.openbravo.pos.pda.bo.RestaurantManager;
import com.openbravo.pos.pda.struts.forms.FloorForm;
import com.openbravo.pos.ticket.ProductInfo;
import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.ticket.TicketLineInfo;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;

/**
 *
 * @author jaroslawwozniak
 */
public class PlaceAction extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private final static String SUCCESS = "success";
    private final static String EDITING = "editing";

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
         * */
        FloorForm floorForm = (FloorForm) form;
        RestaurantManager manager = new RestaurantManager();
        String floorId = (String) floorForm.getFloorId();
        String place = (String) floorForm.getId();
        String str = (String) floorForm.getMode();
        String[] array = null;
        int mode = 0;
        if (!str.equals("")) {
            mode = Integer.valueOf(str);
        }
        List<TicketLineInfo> linesList = new ArrayList<TicketLineInfo>();
        List products = new ArrayList<ProductInfo>();
        TicketInfo ticket;
        switch (mode) {

            //removes products
            case 1:
                ticket = manager.findTicket(place);
                linesList = ticket.getM_aLines();
                array = floorForm.getParameters();
                if (array != null) {
                    for (int i = 0; i < array.length; i++) {
                        linesList.remove(Integer.valueOf(array[i]) - 0);     //strange
                    }
                }
                manager.updateLineFromTicket(place, ticket);
                for (Object line : linesList) {
                    TicketLineInfo li = (TicketLineInfo) line;
                    products.add(manager.findProductById(li.getProductid()));
                }
                break;
            //edits lines
            case 2:
                ticket = manager.findTicket(place);
                linesList = ticket.getM_aLines();
                String[] index = floorForm.getParameters();
                //if null go to default and refresh products. that's why no break
                products.add(manager.findProductById(linesList.get(Integer.valueOf(index[0])).getProductid()));



                request.setAttribute("product", products.get(0));
                request.setAttribute("place", place);
                request.setAttribute("floorId", floorId);
                request.setAttribute("line", linesList.get(Integer.valueOf(index[0])));
                request.setAttribute("lineNo", index[0]);

                return mapping.findForward(EDITING);

            //increment product
            case 3:
                ticket = manager.findTicket(place);
                linesList = ticket.getM_aLines();
                array = floorForm.getParameters();
                if (array != null) {
                    for (int i = 0; i < array.length; i++) {
                        linesList.get(Integer.valueOf(array[i]) - 0).setMultiply(linesList.get(Integer.valueOf(array[i]) - 0).getMultiply() + 1);    //strange
                    }
                }
                manager.updateLineFromTicket(place, ticket);
                for (Object line : linesList) {
                    TicketLineInfo li = (TicketLineInfo) line;
                    products.add(manager.findProductById(li.getProductid()));
                }
                break;

            //adds new products or just refresh
            default:
                if (manager.findTicket(place) == null) {
                    manager.initTicket(place);
                } else {
                    linesList = manager.findTicketLines(place);
                }
                for (Object line : linesList) {
                    TicketLineInfo li = (TicketLineInfo) line;
                    products.add(manager.findProductById(li.getProductid()));
                }
                break;

        }

        request.setAttribute("place", place);
        request.setAttribute("floorId", floorId);
        request.setAttribute("lines", linesList);
        request.setAttribute("products", products);

        return mapping.findForward(SUCCESS);
    }
}