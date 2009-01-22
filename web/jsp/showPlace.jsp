<%--
    Openbravo POS is a point of sales application designed for touch screens.
    Copyright (C) 2007 Openbravo, S.L.
    http://sourceforge.net/projects/openbravopos

    This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
   --%>

<%-- 
    Document   : showPlace
    Created on : Nov 11, 2008, 5:38:14 PM
    Author     : jaroslawwozniak
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"
        import="java.util.List, com.openbravo.pos.ticket.ProductInfo"%>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"   %>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean"  %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="javascript; charset=UTF-8">
        <LINK REL=StyleSheet HREF="layout.css" TYPE="text/css" MEDIA=screen>
        <script type="text/javascript" src="tableScript.js"></script>
        <title>JSP Page</title>
    </head>
    <body>
        <div class="logo">
            <img src="images/logo.gif" alt="Openbravo" class="logo"/><br>
            <jsp:useBean id="products" type="List<ProductInfo>" scope="request" />
            <jsp:useBean id="place" type="java.lang.String" scope="request" />
            <jsp:useBean id="floorName" type="java.lang.String" scope="request" />
            <jsp:useBean id="floorId" type="java.lang.String" scope="request" />
            <a href="showFloors.do?floorId=${floorId}"><img alt="back" src="images/back.png" class="back"></a><%=floorName%><br>
        </div>
        <div class="table">
            <table border="0" id="table" class="pickme">
                <thead>
                    <tr>
                        <th class="name">Item</th>
                        <th>Price</th>
                        <th>Units</th>
                        <th>Value</th>
                        <th></th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <div id="products">
                        <c:forEach var="line" items="${lines}" varStatus="nr">
                            <tr onclick="">
                                <td class="name">${products[nr.count - 1].name}</td>

                                <td><fmt:formatNumber type="number" value="${line.price}" maxFractionDigits="2" minFractionDigits="2"/></td>
                                <td>${line.multiply}</td>
                                <td><fmt:formatNumber type="number" value="${line.value}" maxFractionDigits="2" minFractionDigits="2"/></td>
                                <td><a href="#" onclick="getIndexBackByAdding(${nr.count - 1}, '${place}');"><img src="images/plus.png" alt="add" class="button" /></a></td>
                                <td><a href="#" onclick="getIndexBackByRemoving(${nr.count - 1}, '${place}');"><img src="images/minus.png" alt="remove" class="button" /></a></td>
                                <td><a href="#" onclick="getIndexBackByEditing(${nr.count -1}, '${place}');"><img src="images/star.png" alt="multiply" class="button" /></a></td>
                            </tr>
                        </c:forEach>
                    </div>

                </tbody>
            </table>
        </div>
        <br>
        <div class="logo">

            <form action="showProducts.do">
                <button name="place" value="${place}">Add</button>
            </form>
        </div>

    </body>
</html>
