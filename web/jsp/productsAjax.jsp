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
    Document   : productsAjax
    Created on : Nov 20, 2008, 10:06:21 AM
    Author     : jaroslawwozniak
--%>

<%@page contentType="text/javascript" pageEncoding="UTF-8"
        import="java.util.ArrayList"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>

<span id="productSpan" class="middle">


    <table id="table" class="pickme">
        <thead>
            <tr>
                <th class="name">Item</th>
                <th class="normal">Price</th>
                <th></th>
            </tr>
        </thead>
        <tbody>
            <% ArrayList products = (ArrayList) request.getSession().getAttribute("products");%>
            <c:forEach var="product" items="${products}" varStatus="nr">
                        <tr>
                            <td class="name">${product.name}</td>
                            <td class="normal"><fmt:formatNumber type="number" value="${product.priceSell}" maxFractionDigits="2" minFractionDigits="2"/></td>
                        <!--    <td><a href="#" onclick="getIndexBackByAddingWM5(${nr.count - 1}, '${place}');"><img src="images/plus.png" alt="add" class="button" /></a></td>-->
                            <td><a href="#" onclick="getIndexBack('${place}');"><img src="images/plus.png" alt="add" class="button" /></a></td>
                        </tr>
            </c:forEach>
        </tbody>
    </table>
</span>




