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
    Document   : showFloors
    Created on : Nov 10, 2008, 1:24:13 PM
    Author     : openbravo
--%>


<%@page pageEncoding="UTF-8"
        import="java.util.ArrayList"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="javascript; charset=UTF-8">
        <title>Floors</title>
        <link rel=StyleSheet href="layout.css" type="text/css" media=screen>
        <script type="text/javascript" src="a.js"></script>
    </head>
    <body>
        <img src="images/logo.gif" alt="Openbravo" class="logo" /><br>
        <div class="pad">

            <form name="FloorForm" method="post">
                <html:select property="floorId" value="name" onchange="retrieveURL( 'floorAjaxAction.do?floorId=' + this.value, 'ble');"  >
                    <html:options collection="floors" property="id" labelProperty="name"  />
                </html:select>
            </form>
            <div class="pad2">
                <span id="ble" >
                    <logic:present name="places">
                        <form action="showPlace.do" method="post">
                            <% ArrayList places = (ArrayList) request.getSession().getAttribute("places");%>
                            <c:forEach var="place" items="${places}">
                                <button name="id" value="${place.id}" type="submit" class="floor">${place.name}</button>
                            </c:forEach>
                        </form>
                    </logic:present>
                </span>
            </div>
        </div>
    </body>

</html>
