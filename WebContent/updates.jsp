<%@include file='updates_code.inc'%>
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
<script type="text/javascript">
//LOOSE JS HERE

</script>
<!-- END JAVASCRIPT -->
<title>Wayfinding System Administration - Updates</title>
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
<li><a href="<%=URLEncoder.encode("buildingInfo.jsp")%>">
Building Information</a></li>
<li><a href="<%=URLEncoder.encode("locationInfo.jsp")%>">
Location Information</a></li>
<li><a href="<%=URLEncoder.encode("personInfo.jsp")%>">
Personnel Information</a></li>
<li><a href="<%=URLEncoder.encode("companyInfo.jsp")%>">
Company Information</a></li>
<li><a href="<%=URLEncoder.encode("reporting.jsp")%>">
Reports</a></li>
<li><a href="javascript:void(0)" class="current">System Updates</a></li>
<li><a href="<%=URLEncoder.encode("bulkUploads.jsp")%>">
Bulk Upload</a></li>
</ul>
</div>
<!-- END ADMIN NAV CONTAINER -->
<!-- START ADMIN SUB NAV CONTAINER -->
<div id="way_adminSubNavContainer" class="bgWhite">
<!-- START ADMIN SEARCH CONTAINER -->
<!-- START CURRENT BLDG/LOCATION NAME  -->
<fieldset id="currentBLDGInfo">
<legend class="green">Current Building <img src="../images/icon_information.png" width="16" height="16" class="cursorHand" onmouseover="Tip(tip_currentBldg)" onmouseout="UnTip()" /></legend>
<h1>Building Name</h1>
</fieldset>
<!-- END CURRENT BLDG/LOCATION NAME  -->
<!-- START BLDG INFO SUB NAV MENU -->
<ul id="way_adminSubMenu">
<li><a href="javascript:void(0)" id="tabAutoUpdates" class="current">Automatic</a></li>
<li><a href="javascript:void(0)" id="tabManualUpdates" class="">Manual</a></li>
</ul>
<!-- END BLDG INFO SUB NAV MENU -->
</div>
<!-- END ADMIN SUB NAV CONTAINER -->
<!-- START ADMIN FORMS CONTAINER -->
<div id="way_adminFormsContainer" class="bgWhite">
<!-- START FORM SUBMISSION MESSAGE BOX -->
<div id="formMsgBox" class="formSuccess" style="display:none"><p><img src="../images/icon_finished.png" width="16" height="16" alt="" /> <img src="../images/icon_alert.png" width="16" height="16" alt="" /> Message...</p></div>
<!-- END FORM SUBMISSION MESSAGE BOX -->

<!-- START AUTO UPDATE TAB CONTENT -->
<div id="autoUpdateContent" style="display:visible;">
<!-- START AUTO UPDATE FORM -->
<form id="autoUpdateForm" name="autoUpdateForm">
<!-- START AUTO UPDATE INFO FIELDSET -->
<fieldset id="avaliableUpdatesFields">
<legend>Available Updates <img src="../images/icon_help.png" width="16" height="16" class="cursorHand" onmouseover="Tip(tip_availableUpdates)" onmouseout="UnTip()" /></legend>
<!-- START SOFTWARE UPDATE INFO FIELDSET -->
<fieldset id="sofwareUpdatesFields" class="width47 floatLeft">
<legend>Software <img src="../images/icon_help.png" width="16" height="16" class="cursorHand" onmouseover="Tip(tip_softwareUpdates)" onmouseout="UnTip()" /></legend>
<ul>
<li><strong>Update Title:</strong> Description...</li>
</ul>
</fieldset>
<!-- END SOFTWARE UPDATE INFO FIELDSET -->
<!-- START CAD UPDATE INFO FIELDSET -->
<fieldset id="cadUpdatesFields" class="width47 floatRight">
<legend>CAD <img src="../images/icon_help.png" width="16" height="16" class="cursorHand" onmouseover="Tip(tip_cadUpdates)" onmouseout="UnTip()" /></legend>
<ul>
<li><strong>Update Title:</strong> Description...</li>
</ul>
</fieldset>
<!-- END CAD UPDATE INFO FIELDSET -->
<div class="clearFloat"></div>
</fieldset>
<!-- END AUTO UPDATE INFO FIELDSET -->
<!-- START UPDATE BUTTON -->
<div class="alignCenter marginTB5px">
<button type="submit" id="updateSubmitBtn" name="upadateSubmitBtn" class="btnBlue cursorHand">Update</button>
</div>
<!-- END UPDATE BUTTON -->
</form>
<!-- END AUTO UPDATE FORM -->
</div>
<!-- END AUTO UPDATE TAB CONTENT -->

<!-- START AUTO UPDATE TAB CONTENT -->
<div id="manualUpdateContent" style="display:none;">
<!-- START AUTO UPDATE FORM -->
<form id="manualUpdateForm" name="manualUpdateForm">
<!-- START AUTO UPDATE INFO FIELDSET -->
<fieldset id="avaliableUpdatesFields">
<legend>Manual Updates <img src="../images/icon_help.png" width="16" height="16" class="cursorHand" onmouseover="Tip(tip_maualUpdates)" onmouseout="UnTip()" /></legend>
<label class="floatLeft width15">Load Files:</label><input type="file" id="manualUpdateSrc" name="manualUpdateSrc" size="60" class="floatLeft marginB5px" />
<br class="clearFloat" />
</fieldset>
<!-- START UPDATE BUTTON -->
<div class="alignCenter marginTB5px">
<button type="reset" id="updateClearBtn" name="upadateClearBtn" class="btnRed cursorHand">Clear</button>
<button type="submit" id="updateSubmitBtn" name="upadateSubmitBtn" class="btnBlue cursorHand">Update</button>
</div>
<!-- END UPDATE BUTTON -->
</form>
<!-- END AUTO UPDATE FORM -->
</div>
<!-- END AUTO UPDATE TAB CONTENT -->

</div>
<!-- END ADMIN FORMS CONTAINER -->
<!-- START ADMIN FOOTER CONTAINER -->
<%@include file='footer.inc'%>
<!-- START ADMIN FOOTER CONTAINER -->
</div>
<!-- END CONTENT CONTAINER -->

</div>
<!-- END OUTER CONTAINER -->
</body>
</html>
<!-- EOF -->
