package jsp;

<%-- 
    Document   : showPlace
    Created on : Nov 11, 2008, 5:38:14 PM
    Author     : openbravo
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
       <a href="showFloors.do?id=${place}"><img alt="back" src="images/back.png" class="back"></a><br>
        </div>
        <div class="table">
        <table border="0" id="table" class="pickme">
            <thead>
                <tr>
                    <th class="name">Item</th>
                    <th>Price</th>
                    <th>Units</th>
                    <th>Value</th>
                </tr>
            </thead>
            <tbody>
                <div id="products">
            <c:forEach var="line" items="${lines}" varStatus="nr">                
                <tr onclick="">
                    <td class="name">${products[nr.count - 1].name}</td>

                    <td><fmt:formatNumber type="number" value="${line.price}" maxFractionDigits="2" minFractionDigits="2"/></td>
                    <td>${line.multiply}</td>
                    <td>${line.value}</td>
                </tr>
            </c:forEach>
            </div>
           
            </tbody>
        </table>
        </div>
         <br>
             <div class="logo">
            <c:url value="showProducts.do" var="add">
                <c:param name="place" value="${place}"></c:param>
            </c:url>
            <a href="${add}"><img src="images/plus.png" alt="add" class="button" /></a>
            <a href="#" onclick="getIndexBackByRemoving('${place}');"><img src="images/minus.png" alt="remove" class="button" /></a>
           
            <a href="#" onclick="getIndexBackByEditing('${place}');"><img src="images/star.png" alt="multiply" class="button" /></a>
             </div>
        
    </body>
</html>
