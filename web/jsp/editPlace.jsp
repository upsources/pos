<%--
   Openbravo POS is a point of sales application designed for touch screens.
   Copyright (C) 2007-2009 Openbravo, S.L.
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
   Foundation, Inc., 51 Franklin Street, Fifth floor, Boston, MA  02110-1301  USA
 --%>

<%-- 
    Document   : editPlace
    Created on : Jan 12, 2009, 10:33:44 AM
    Author     : jaroslawwozniak
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"
        import="java.util.ArrayList, com.openbravo.pos.ticket.ProductInfo, java.util.HashMap,
        com.openbravo.pos.ticket.TicketLineInfo"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"  %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
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
    <div class="logo">
    <img src="images/logo.gif" alt="Openbravo" class="logo" /><br>

    <jsp:useBean id="place" type="java.lang.String" scope="request" />
    <jsp:useBean id="line" type="com.openbravo.pos.ticket.TicketLineInfo" scope="request" />
    <jsp:useBean id="product" type="com.openbravo.pos.ticket.ProductInfo" scope="request" />
    <jsp:useBean id="lineNo" type="java.lang.String" scope="request" />
    <a href="showPlace.do?id=${place}"><img alt="back" src="images/back.png" class="back">${place}</a><br>
    </div>
    <div class="middle">
    <form action="modifyProduct.do" >
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
            
                <html:hidden property="id" value="${place}" />
                <html:hidden property="line" value="${lineNo}" />
                 <tr onclick="">
                    <td class="name">${product.name}</td>
                    <td><fmt:formatNumber type="currency" value="${line.price}" maxFractionDigits="2" minFractionDigits="2"/></td>
                    <td><input type="text" name="parameters" size="3" value="${line.multiply}"></td>
                    <td><fmt:formatNumber type="currency" value="${line.value}" maxFractionDigits="2" minFractionDigits="2"/></td>
                </tr>
        </tbody>
    </table>
    <br>
    <html:submit style="margin-left:80px;">Modify</html:submit>
    </form>
    </div>
</body>
</html>
