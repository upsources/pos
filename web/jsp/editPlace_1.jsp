package jsp;

<%-- 
    Document   : editPlace
    Created on : Jan 12, 2009, 10:33:44 AM
    Author     : openbravo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"
        import="java.util.ArrayList, com.openbravo.pos.ticket.ProductInfo, java.util.HashMap,
        com.openbravo.pos.ticket.TicketLineInfo"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"  %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <LINK REL=StyleSheet HREF="layout.css" TYPE="text/css" MEDIA=screen>
        <script type="text/javascript" src="tableScript.js"></script>
    </head>
    <body>
        <img src="images/logo.gif" alt="Openbravo" class="logo" /><br>
      
        <jsp:useBean id="place" type="java.lang.String" scope="request" />
       <a href="showPlace.do?id=${place}"><img alt="back" src="images/back.png" class="back"></a><br>
        <table border="0" id="table" class="pickme">
            <thead>
                <tr>
                    <th>Item</th>
                    <th>Price</th>
                    <th>Units</th>
                    <th>Value</th>
                </tr>
            </thead>
            <tbody>
            <form action="modifyProduct.do" method="post">
                <html:hidden property="id" value="${place}" />
                
                <% HashMap map = (HashMap)request.getAttribute("lines");
                    ArrayList<ProductInfo> products = (ArrayList<ProductInfo>) request.getAttribute("products");
                    TicketLineInfo line;
                    for(int i = 0, j = 0; i < (Integer)request.getAttribute("size"); i++){
                        if(map.get(i) != null){
                            line = (TicketLineInfo)map.get(i); %>

                            <tr onclick="">
                                <td class="name"><%=products.get(j).getName()%></td>
                                <td><%=line.printPrice()%></td>
                                <td><input type="text" name="parameters" size="3" value="<%=line.printMultiply()%>"></td>
                                <td><%=line.getValue()%></td>
                    
                            </tr>
           <%               j++;
                        }
                    }
                    %> 

        </tbody>
    </table>
    <br>
        <html:submit style="margin-left:80px;">Modify</html:submit>
        </form>
   </body>
</html>
