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

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><bean:message key="welcome.title"/></title>
        <link rel=StyleSheet href="layout.css" type="text/css" media=screen>
        <html:base/>
    </head>
    <body>
        <img src="../images/logo.gif" alt="Openbravo" class="logo"/><br>
        <logic:messagesPresent >
            <html:messages id="msg">
                <p>
                    <strong><font color="red"><bean:write name="msg" /></font></strong>
                </p>
            </html:messages>
        </logic:messagesPresent>
        <html:form action="login.do" method="post">
            <table class="pad">
                <tbody>
                    <tr>
                        <td ><bean:message key="message.login" /></td>
                    </tr>
                    <tr>
                        <td><html:text property="login" size="10"/></td>
                    </tr>
                    <tr>
                        <td><bean:message key="message.password" /></td>
                    </tr>
                    <tr>
                        <td><html:password property="password" size="10"/></td>
                    </tr>
                    <tr>
                        <td><html:submit><bean:message key="button.login" /></html:submit></td>
                    </tr>
                </tbody>
            </table>
        </html:form>
        <logic:notPresent name="org.apache.struts.action.MESSAGE" scope="application">
            <div style="color: red">
                ERROR:  Application resources not loaded -- check servlet container
                logs for error messages.
            </div>
        </logic:notPresent>
        <logic:messagesPresent>
            <h3><bean:message key="welcome.heading"/></h3>
            <p><bean:message key="welcome.message"/></p>
        </logic:messagesPresent>
    </body>
</html>
