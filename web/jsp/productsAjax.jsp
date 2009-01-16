<%--
    Document   : productsAjax
    Created on : Nov 20, 2008, 10:06:21 AM
    Author     : openbravo
--%>

<%@page contentType="text/javascript" pageEncoding="UTF-8"
        import="java.util.ArrayList"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>

<span id="productSpan" class="table">


       <table class="pickme">
           <thead>
                <tr>
                    <th class="name">Item</th>
                    <th>Price</th>
                </tr>
            </thead>
           <tbody>
               <% ArrayList products = (ArrayList) request.getSession().getAttribute("products");%>
                <c:forEach var="product" items="${products}">
                    <tr>
                        <td class="name">${product.name}</td>
                        <td><fmt:formatNumber type="currency" value="${product.priceSell}" maxFractionDigits="2" minFractionDigits="2"/></td>
                    </tr>
                </c:forEach>
            </tbody>
       </table>
   </span>


          
             
