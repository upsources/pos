<%-- 
    Document   : showProducts
    Created on : Nov 17, 2008, 3:30:52 PM
    Author     : openbravo
--%>


<%@ page pageEncoding="UTF-8" import="java.util.ArrayList, com.openbravo.pos.ticket.ProductInfo" %>
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
        <meta http-equiv="" content="NO-CACHE">
        <title>Floors</title>
        <link rel=StyleSheet href="layout.css" type="text/css" media=screen>
        <script type="text/javascript" src="tableScript.js"></script>
        <script type="text/javascript" src="a.js"></script> 
    </head>
    <body onload="addLoadEvent(lockRow);">
        <div class="logo">
        <img src="images/logo.gif" alt="Openbravo" class="logo"/><br>
        <a href="showPlace.do?id=<%=request.getSession().getAttribute("place")%>" ><img alt="back" src="images/back.png" class="back"></a><br>
      </div>
    
    <form action="#" method="get" >
        <html:select property="categoryId" value="id" 
        onchange="retrieveURL( 'productAjaxAction.do?categoryId=' + this.value, 'productSpan');update();setCategoryId(this.value);" >
                <html:options collection="categories" property="id" labelProperty="name"  />
        </html:select>
        </form>
    
           
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
                        <td><fmt:formatNumber type="number" value="${product.priceSell}" maxFractionDigits="2" minFractionDigits="2"/></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        </span>
        
        <br>
       
        <div class="logo">
        <br><button onclick="getIndexBack('<%=request.getSession().getAttribute("place")%>');" class="pad3">Add</button>
        </div>
       
   
    

    </body>
</html>
