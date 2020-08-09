<!--

    Copyright (c) 2008 by Zetek, Inc.
    All Rights Reserved

--><%
// Check the user object and forward to the building select page
// if the current user has not selected a building.
if (uo.currentBuilding == null) {
%>
<jsp:forward page="buildingInfo.jsp" >
<jsp:param name="msg" value="You must select a building before doing anything else." />
</jsp:forward>
<%
}
%>
