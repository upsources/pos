<%-- 
    Document   : productsPlaceAjax
    Created on : Apr 21, 2009, 7:02:07 PM
    Author     : openbravo
--%>

<%@page contentType="java" pageEncoding="UTF-8"
        import="java.util.List, com.openbravo.pos.ticket.ProductInfoExt, com.openbravo.pos.ticket.TicketLineInfo"%>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"   %>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean"  %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
      <script type="text/javascript" src='tableScript.js'></script>
        <script type="text/javascript" src='a.js'></script>
        <jsp:useBean id="products" type="List<ProductInfoExt>" scope="request" />
            <jsp:useBean id="place" type="java.lang.String" scope="request" />
            <jsp:useBean id="placeName" type="java.lang.String" scope="request" />
            <jsp:useBean id="floorName" type="java.lang.String" scope="request" />
            <jsp:useBean id="floorId" type="java.lang.String" scope="request" />
            <jsp:useBean id="lines" type="List<TicketLineInfo>" scope="request" />

                    <span id="products" >
                   <!--     <c:forEach var="line" items="${lines}" varStatus="nr">
                            <tr id="${nr.count - 1}">
                                <td class="name">${products[nr.count - 1].name}gdfgdfgdff</td>
                                <td class="normal"><fmt:formatNumber type="currency" value="${line.price}" maxFractionDigits="2" minFractionDigits="2"/></td>
                                <td class="units" id="mul${nr.count - 1}"><fmt:formatNumber type="number" value="${line.multiply}" maxFractionDigits="2" minFractionDigits="0"/></td>
                                <td class="normal" id="value${nr.count - 1}"><fmt:formatNumber type="currency" value="${line.value}" maxFractionDigits="2" minFractionDigits="2"/></td>
                                <td><a href="#" onclick="ajaxCall(3, '${place}', '${nr.count - 1}');"><img src="images/plus.png" alt="add" class="button" /></a></td>
                                <!--<td><a href="#" onclick="refreshList('${place}', ${nr.count}, ${line.multiply});"><img src="images/minus.png" alt="remove" class="button" /></a></td>
                                <!--<td><a href="#" onclick="getIndexBackByEditing('${nr.count -1}', '${place}');"><img src="images/star.png" alt="multiply" class="button" /></a></td>
                                <td><a href="#" onclick="ajaxCall(1, '${place}', '${nr.count - 1}');"><img src="images/minus.png" alt="remove" class="button" /></a></td>
                            </tr>
                        </c:forEach> -->
                    </span>
