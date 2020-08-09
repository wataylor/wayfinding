<%-- debugDisplay.inc

Code to display all parameters of the page, used for debugging, can be
turned on and off via a parameter which is passed to the first screen
in the series and /or by toggle buttons on certain screens. Requires
that asst.debug.DebugUtils be included in the calling page.

   Copyright (c) 2008 Zetek Inc.
   All Rights Reserved
--%><% if (((anyParam = (String)session.getAttribute("debug")) != null) && anyParam.equals("y")) { %>
<%= DebugUtils.makeFormParamTable(request) %>
<%= DebugUtils.makeSessionTable(request) %>
<% } %>
