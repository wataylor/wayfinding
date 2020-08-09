<!--

    Copyright (c) 2008 by Zetek, Inc.
    All Rights Reserved

--><%
// Check to see if there is an alert message in the User Object and display it
if (uo.msg != null) {
%>
<script type="text/javascript">alert("<%=uo.msg%>");</script>
<%
uo.msg = null;
}
%>
