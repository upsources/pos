/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.pda.bo;


import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.ticket.TicketLineInfo;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author openbravo
 */
public class NewClass {
    private static TicketInfo TicketInfo;

    public static void main(String args[]) {
        RestaurantManager bo = new RestaurantManager();
        //Floor floor = new Floor();
     /*   List<Floor> list = bo.findAllFloors();
        for(Floor floor : list){
        System.out.println(floor.getName());
        }
         */
       /* List<TicketLineInfo> lines = bo.findTicketLines("3");
        for(TicketLineInfo li : lines){
            System.out.println(li.getProductid());
        }*/
        bo.initTicket("jvhjfhv");

    }
}
