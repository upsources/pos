<%-- 
    Document   : productsAjaxNextLevel
    Created on : Apr 23, 2009, 12:57:09 PM
    Author     : openbravo
--%>

<%@page contentType="text/javascript" pageEncoding="UTF-8"
        import="java.util.ArrayList"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>d">

<span>
    <c:forEach var="category" items="${subcategories}" varStatus="nr">
        <tr>
            <td class="category" colspan="4" onclick="retrieveURLforCategories('productAjaxAction.do?categoryId=${category.id}&mode=1', '${category.id}');update();">${category.name}</td>
        </tr>
        <tr>
            <td colspan="4"><div id="${category.id}"></div></td>
        </tr>
    </c:forEach>
    <% ArrayList products = (ArrayList) request.getSession().getAttribute("products");%>
    <c:forEach var="product" items="${products}" varStatus="nr">
        <tr id="${nr.count - 1}">
            <td class="name">${product.name}</td>
            <td class="normal"><fmt:formatNumber type="currency" value="${product.priceSell}" maxFractionDigits="2" minFractionDigits="2"/></td>
            <td class="normal"></td>
            <td><input value="Add" type="submit" class="floor" onclick="ajaxAddProduct('<%=request.getSession().getAttribute("place")%>', ${nr.count - 1}, '${product.name}', '${product.id}', 0);"/></td>
        </tr>
    </c:forEach>
</span>
