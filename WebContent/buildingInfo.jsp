<%@include file='buildingInfo_code.inc'%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/xhtml1-loose.dtd">
<html>
<head>
<!-- START META TAGS -->
<meta name="author" content="Zetek Corporation" />
<meta name="copyright" content="2008 Zetek Corporation (All Rights Reserved)" />
<meta name="robots" content="noindex" />
<meta name="robots" content="nofollow" />
<meta name="robots" content="noodp" />
<!-- END META TAGS -->
<!-- START STYLESHEETS -->
<link rel="stylesheet" type="text/css" href="../css/importer_admin.css" media="screen" />
<link rel="stylesheet" type="text/css" href="../css/print.css" media="print" />
<!-- END STYLESHEETS -->
<!-- START JAVASCRIPT -->
<script type="text/javascript" src="../javascript/jquery-1.2.6.js"></script>
<script type="text/javascript" src="../javascript/jquery.popupwindow.js"></script>
<script type="text/javascript" src="../javascript/jquery.boxy.js"></script>
<script type="text/javascript" src="../javascript/niftycube.js"></script>
<script type="text/javascript" src="../javascript/utilityscripts.js"></script>
<script type="text/javascript" src="../javascript/general_admin.js"></script>
<script type="text/javascript" src="../javascript/htmlFrags.js"></script>
<script type="text/javascript" src="../javascript/edifice_object.js"></script>
<script type="text/javascript">

/* The current edifice goes into the global name space. */

var currentEdifice;
if (!currentEdifice) {
  currentEdifice = new Edifice();
}

$(document).ready(function () {
    $("button#admin_srchFindBtn").click(currentEdifice.getDefaultEdifice);
// Install the current building if there is one
var jsonData = <%= uo.getBuildingJSON() %>;
if (jsonData) {
  // debugger;
  Edifice.processOptionalEdifice(jsonData);
}
});

</script>
<!-- END JAVASCRIPT -->
<title>Wayfinding System Administration - Building Information</title>
</head>
<body id="way_adminBldgInfo" class="adminMode">
<!-- TOOLTIP JAVASCRIPT MUST STAY HERE - DO NOT MOVE! -->
<script type="text/javascript" src="../javascript/wz_tooltip.js"></script>
<!-- TOOLTIP JAVASCRIPT MUST STAY HERE - DO NOT MOVE! -->
<!-- START OUTER CONTAINER -->
<div id="way_outerContainer">
<!-- START MASTHEAD CONTAINER -->
<%@include file='globalnav.inc'%>
<!-- END MASTHEAD CONTAINER -->

<!-- START CONTENT CONTAINER -->
<div id="way_adminContentContainer" class="bgWhite">
<!-- START ADMIN NAV CONTAINER -->
<div id="way_adminNavContainer" class="pageColor">
<ul id="way_adminMainMenu">
<li><a href="javascript:void(0)" class="current">Building Information</a></li>
<li><a href="<%=URLEncoder.encode("locationInfo.jsp")%>">
Location Information</a></li>
<li><a href="<%=URLEncoder.encode("personInfo.jsp")%>">
Personnel Information</a></li>
<li><a href="<%=URLEncoder.encode("companyInfo.jsp")%>">
Company Information</a></li>
<li><a href="<%=URLEncoder.encode("reporting.jsp")%>">
Reports</a></li>
<li><a href="<%=URLEncoder.encode("updates.jsp")%>">
System Updates</a></li>
<li><a href="<%=URLEncoder.encode("bulkUploads.jsp")%>">
Bulk Upload</a></li>
</ul>
</div>
<!-- END ADMIN NAV CONTAINER -->
<!-- START ADMIN SUB NAV CONTAINER -->
<div id="way_adminSubNavContainer" class="bgWhite">
<!-- START ADMIN SEARCH CONTAINER -->
<div id="way_adminSearchContainer" class="bgWhite alignCenter width75"
<%= (uo.buildingCount <= 1) ? "style=\"display:none;\"" : ""%>
>
<!-- START BLDG SEARCH FORM  -->
<form name="admin_bldgSrchForm" id="admin_bldgSrchForm">
<div class="alignLeft">
<h4 class="floatLeft">Building Search</h4><div class="floatLeft"><img src="../images/icon_help.png" width="16" height="16" class="marginL4px cursorHand" onmouseover="Tip(tip_bldgSearch)" onmouseout="UnTip()" /></div>
<div class="clearFloat"></div>
</div>

<!-- START BLDG SEARCH FIELD WITH OPTIONS  -->
<div class="admin_srchWithOptsBox" style="display:visible;">
<input type="hidden" name="SrchOptsMenu" value="building" />
<input type="text" name="SrchInputField" id="SrchInputField" class="width78" />
<select id="SrchFilter" name="SrchFilter" class="width20">
<option value = "0" selected>Filter Locations By...</option>
<option value="1">1</option>
<option value="2">2</option>
<option value="3">3</option>
</select>
<div id="Suggestions"><ul></ul></div>
</div>
<!-- END BLDG SEARCH FIELD WITH OPTIONS  -->

<!-- START BLDG SEARCH FIELD BUTTONS  -->
<button type="reset" id="admin_srchClearBtn" name="admin_srchClearBtn" class="btnRed cursorHand">Clear</button>
<button type="submit" id="admin_srchFindBtn" name="admin_srchFindBtn" class="btnBlue cursorHand">Find</button>
<!-- END BLDG SEARCH FIELD BUTTONS  -->

</form>
<!-- END BLDG SEARCH FORM -->

</div>
<!-- END ADMIN SEARCH CONTAINER -->
<!-- START BLDG INFO SUB NAV MENU -->
<ul id="way_adminSubMenu">
<li><a href="javascript:void(0)" class="current">Overview</a></li>
</ul>
<!-- END BLDG INFO SUB NAV MENU -->
</div>
<!-- END ADMIN SUB NAV CONTAINER -->
<!-- START ADMIN FORMS CONTAINER -->
<div id="way_adminFormsContainer" class="bgWhite">
<!-- START GENERAL BLDG INFO CONTENT -->
<fieldset id="bldgInfomationFields">
<legend>Building Information
<img src="../images/icon_help.png" width="16" height="16" class="cursorHand"
onmouseover="Tip(tip_bldgDefaultInfo)" onmouseout="UnTip()" /></legend>
<label class="floatLeft">Name:</label>
<p class="floatLeft marginT3px" id="admin_bldg_name" />
</p>
<br class="clearFloat" />
<label class="floatLeft">Address:</label><p class="floatLeft marginT3px"
id="admin_bldg_address1Line1" class="width78" /></p>
<br class="clearFloat" />
<label class="floatLeft">Phone:</label><p class="floatLeft marginT3px"
id="admin_bldg_mainPhone" class="width78" /></p>
<br class="clearFloat" />
<label class="floatLeft">Fax:</label><p class="floatLeft marginT3px"
id="admin_bldg_mainFax" class="width78" /></p>
<br class="clearFloat" />
<label class="floatLeft">Email:</label><p class="floatLeft marginT3px"
id="admin_bldg_mainEmail" class="width78" /></p>
<br class="clearFloat" />
</fieldset>
<fieldset id="bldgInfomationFields">
<legend>Building Contacts
<img src="../images/icon_help.png" width="16" height="16" class="cursorHand"
onmouseover="Tip(tip_bldgContacts)" onmouseout="UnTip()" />
</legend>
<div id="contacts_content">
<label class="floatLeft">Facility/Building Manager:</label>
<p class="floatLeft marginT3px">
<span id="facMgrID_firstName"></span>
<span id="facMgrID_lastName"></span> |
<span id="facMgrID_workPhone"></span> |
<a>
<span id="facMgrID_emailAddress"></span></a>
</p>
<hr class="clearFloat" />
<br class="clearFloat" />
</div>
</fieldset>
<!-- END GENERAL BLDG INFO CONTENT -->
</div>
<!-- END ADMIN FORMS CONTAINER -->
<!-- START ADMIN FOOTER CONTAINER -->
<%@include file='footer.inc'%>
<!-- START ADMIN FOOTER CONTAINER -->
</div>
<!-- END CONTENT CONTAINER -->
</div>
<!-- END OUTER CONTAINER -->
<%@include file='alert.inc'%>
</body>
</html>
<!-- EOF -->
