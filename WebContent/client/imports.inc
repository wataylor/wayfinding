<!--

    Copyright (c) 2008 by Zetek, Inc.
    All Rights Reserved

--><%-- imports.inc

Java classes to be imported by all .jsp pages.
--%><%@ page import="java.util.Enumeration,
java.net.URLEncoder,
java.util.HashSet,
java.util.ArrayList,
java.util.ResourceBundle,
java.lang.RuntimeException,
zetek.server.JsonServlet,
zetek.server.utils.AnyUser,
zetek.server.utils.DebugUtils"
errorPage="runError.jsp"%>
<%!AnyUser uo;
String anyParam;
StringBuilder sb;
int i;
%><%
uo = AnyUser.getUserObject(request); // Makes a new uo if there isn't one
uo.sessionIDsuff = ";jsessionid=" + session.getId();
uo.msg = request.getParameter("msg");
uo.sb.setLength(0);
%>
