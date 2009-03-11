<%--
   Openbravo POS is a point of sales application designed for touch screens.
   Copyright (C) 2007-2009 Openbravo, S.L.
   http://sourceforge.net/projects/openbravopos

    This file is part of Openbravo POS.

    Openbravo POS is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Openbravo POS is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Openbravo POS.  If not, see <http://www.gnu.org/licenses/>.
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
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>

<span id="productSpan" class="middle">


    <table id="table" class="pickme">
        <thead>
            <tr>
                <th class="name"><bean:message key="item" /></th>
                <th class="normal"><bean:message key="price" /></th>
                <th></th>
            </tr>
        </thead>
        <tbody>
            <% ArrayList products = (ArrayList) request.getSession().getAttribute("products");%>
            <c:forEach var="product" items="${products}" varStatus="nr">
                        <tr>
                            <td class="name">${product.name}</td>
                            <td class="normal"><fmt:formatNumber type="currency" value="${product.priceSell}" maxFractionDigits="2" minFractionDigits="2"/></td>
                            <td><a href="#" onclick="ajaxAddProduct('<%=request.getSession().getAttribute("place")%>', '${nr.count - 1}');"><img src="images/plus.png" alt="add" class="button" /></a></td>
                        </tr>
            </c:forEach>
        </tbody>
    </table>
</span>




