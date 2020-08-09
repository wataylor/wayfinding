<%@ page import="java.io.ByteArrayOutputStream,
java.io.PrintStream" isErrorPage="true" %>
<html>
<head><title>Run Error</title>

<%-- runError.jsp

/* @name runError.jsp

  Copyright (c) 2002-2008 Zetek Corporation (All Rights Reserved)

-------- Licensed Software Proprietary Information Notice -------------

This software is a working embodiment of certain trade secrets of
Zetek Corporation.  The software is licensed only for the
day-to-day business use of the licensee.  Use of this software for
reverse engineering, decompilation, use as a guide for the design of a
competitive product, or any other use not for day-to-day business use
is strictly prohibited.

All screens and their formats, color combinations, layouts, and
organization are proprietary to and copyrighted by Zetek Corporation.

All rights are reserved.

Authorized Zetek customer use of this software is subject to the
terms and conditions of the software license executed between Customer
and Zetek Corporation.

------------------------------------------------------------------------

*/

--%>
</head>
<body>
<h1 align="center">Execution Error</h1>
<p><font color="red"><%= exception.toString() %></font>
<hr />

<pre>
<%
    ByteArrayOutputStream ostr = new ByteArrayOutputStream();
    exception.printStackTrace(new PrintStream(ostr));
    out.print(ostr);
%>
</pre>

<hr />
<p>Please copy this display and email it to the owner of this page.</p>
<p><a href="mailto:jhubbell@zetek.com">
jhubbell@zetek.com</a></p>
</body></html>
