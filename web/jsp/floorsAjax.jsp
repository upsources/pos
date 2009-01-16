 <%-- 
    Document   : floorsAjax
    Created on : Nov 19, 2008, 8:46:14 AM
    Author     : openbravo
--%>
<%@page pageEncoding="UTF-8"
        import="java.util.ArrayList"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
   
<span id="ble">
    <logic:present name="places">
        <form action="showPlace.do" method="get">
            <% ArrayList places = (ArrayList) request.getSession().getAttribute("places");%>
            <c:forEach var="place" items="${places}">
                <br><button name="id" value="${place.id}" type="submit" class="floor">${place.name}</button>
            </c:forEach>
        </form>
    </logic:present>
</span>
    