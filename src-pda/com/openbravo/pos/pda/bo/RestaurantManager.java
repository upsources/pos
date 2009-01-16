/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.pda.bo;

import com.openbravo.pos.pda.dao.CategoryDAO;
import com.openbravo.pos.pda.dao.FloorDAO;
import com.openbravo.pos.pda.dao.LoginDAO;
import com.openbravo.pos.pda.dao.PlaceDAO;
import com.openbravo.pos.pda.dao.ProductDAO;
import com.openbravo.pos.pda.dao.TaxDAO;
import com.openbravo.pos.pda.dao.TicketDAO;
import com.openbravo.pos.pda.dao.TicketLineDAO;
import com.openbravo.pos.ticket.CategoryInfo;
import com.openbravo.pos.ticket.Floor;
import com.openbravo.pos.ticket.Place;
import com.openbravo.pos.ticket.ProductInfo;
import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.ticket.TicketLineInfo;
import com.openbravo.pos.ticket.UserInfo;
import java.util.List;

/**
 *
 * @author openbravo
 */
public class RestaurantManager {

    private FloorDAO floor;
    private PlaceDAO place;
    private TicketDAO ticket;
    private TicketLineDAO lines;
    private ProductDAO product;
    private LoginDAO login;
    private CategoryDAO category;
    private TaxDAO tax;

    public List<Floor> findAllFloors() {
        floor = new FloorDAO();

        return floor.findAllFloors();
    }

    public List<Place> findAllPlaces(String floor) {
        place = new PlaceDAO();

        return place.findAllPlaceByFloor(floor);
    }

    public String findTheFirstFloor() {
        floor = new FloorDAO();

        return floor.findAllFloors().get(0).getId();
    }

    public TicketInfo findTicket(String id) {
        ticket = new TicketDAO();

        return ticket.getTicket(id);
    }

    public void initTicket(String id){
        ticket = new TicketDAO();
        ticket.initTicket(id);
    }

    public List<TicketLineInfo> findTicketLines(String ticketId) {
        lines = new TicketLineDAO();

        return lines.findLinesByTicket(ticketId);
    }

    public ProductInfo findProductById(String productId) {
        product = new ProductDAO();

        return product.findProductById(productId);
    }

    public UserInfo findUser(String aLogin, String password) {
        login = new LoginDAO();

        return login.findUser(aLogin, password);
    }

    public List<ProductInfo> findProductsByCategory(String categoryId) {
        product = new ProductDAO();

        return product.findProductsByCategory(categoryId);
    }
    
    public List<CategoryInfo> findAllCategories(){
        category = new CategoryDAO();
        
        return category.findAllCategories();
    }

    public void setTableBusy(String placeId, TicketInfo ticket){
        place = new PlaceDAO();
        place.setTableBusy(placeId, ticket.getM_dDate().toString());
    }

    public void addLineToTicket(String ticketId, String aCategory, String productIndex){
        lines = new TicketLineDAO();
        ticket = new TicketDAO();
        product = new ProductDAO();
        category = new CategoryDAO();
        tax = new TaxDAO();
        TicketInfo obj = ticket.getTicket(ticketId);
        ProductInfo productObj = null;
        if(aCategory.equals("undefined")) {
            aCategory = category.findFirstCategory();
        }
         
        productObj = product.findProductsByCategory(aCategory).get(Integer.valueOf(productIndex));
        obj.addLine(new TicketLineInfo(productObj, productObj.getPriceSell(), tax.getTax(productObj.getTaxcat())));
        
        ticket.updateTicket(ticketId, obj);
    }

    public void updateLineFromTicket(String ticketId, TicketInfo aticket){
        ticket = new TicketDAO();
        ticket.updateTicket(ticketId, aticket);
    }
}
