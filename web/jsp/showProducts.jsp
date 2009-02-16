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
    Document   : showProducts
    Created on : Nov 17, 2008, 3:30:52 PM
    Author     : jaroslawwozniak
--%>


<%@ page pageEncoding="UTF-8" import="java.util.ArrayList, com.openbravo.pos.ticket.ProductInfoExt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="javascript; charset=UTF-8">
        <meta name = "viewport" content = "width=device-width">
        <title>Floors</title>
        <link rel=StyleSheet href="layout.css" type="text/css" media=screen>
        <script type="text/javascript" src="tableScript.js"></script>
        <script type="text/javascript" src="a.js"></script>
    </head>
    <body onload="">
        <jsp:useBean id="placeName" scope="request" type="java.lang.String"/>
        <div class="logo">
            <img src="images/logo.gif" alt="Openbravo" class="logo"/><br>
                <a href="showPlace.do?id=<%=request.getSession().getAttribute("place")%>" ><img alt="back" src="images/back.png" class="back"></a><%=placeName%><br>
        </div>
        <div class="pad">
        <form action="#" method="get" >
            <html:select property="categoryId" value="id"
                         onchange="retrieveURL( 'productAjaxAction.do?categoryId=' + this.value, 'productSpan');update();rememberCategory(this.value);" >
                <html:options collection="categories" property="id" labelProperty="name"  />
            </html:select>
        </form>
        <span id="productSpan">
            <table class="pickme">
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
                            <td><a href="#" onclick="ajaxAddProduct(<%=request.getSession().getAttribute("place")%>, '${nr.count - 1}');"><img src="images/plus.png" alt="add" class="button" /></a></td>

                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </span>
        </div>
        <br>
        <br>
           <!--     <input onclick="getIndexBack('<%=request.getSession().getAttribute("place")%>');" class="pad3" type="submit">
           -->



    </body>
</html>
