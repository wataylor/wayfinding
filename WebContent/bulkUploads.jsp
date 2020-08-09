<%@include file='bulkUploads_code.inc'%>
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
<title>Wayfinding System Administration - Bulk Uploads</title>
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
<li><a href="<%=URLEncoder.encode("updates.jsp")%>">
System Updates</a></li>
<li><a href="javascript:void(0)" class="current">Bulk Upload</a></li>
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
<li><a href="javascript:void(0)" id="tabUpload" class="current">Upload</a></li>
<li><a href="javascript:void(0)" id="tabTemplates" class="">Templates</a></li>
</ul>
<!-- END BLDG INFO SUB NAV MENU -->
</div>
<!-- END ADMIN SUB NAV CONTAINER -->
<!-- START ADMIN FORMS CONTAINER -->
<div id="way_adminFormsContainer" class="bgWhite">
<!-- START FORM SUBMISSION MESSAGE BOX -->
<div id="formMsgBox" class="formSuccess" style="display:none"><p><img src="../images/icon_finished.png" width="16" height="16" alt="" /> <img src="../images/icon_alert.png" width="16" height="16" alt="" /> Message...</p></div>
<!-- END FORM SUBMISSION MESSAGE BOX -->
<!-- START UPLOAD TAB CONTENT-->
<div id="bulkUploadContent" style="display:visible;">
<!-- START BULK UPLOAD TEMPLATES FORM -->
<form id="bulkUploadsForm" name="bulkUploadsForm">
<fieldset id="templateUpload">
<legend>Upload Templates <img src="../images/icon_help.png" width="16" height="16" class="cursorHand" onmouseover="Tip(tip_uploadTemplates)" onmouseout="UnTip()" /></legend>
<label class="floatLeft width10">Template File:</label><input type="file" id="bulkTemplateSrc" name="bulkTemplateSrc" size="60" class="floatLeft marginB5px" />
<br class="clearFloat" />
</fieldset>
<!-- END BULK UPLOAD TEMPLATES FORM -->
<!-- START BULK UPLOAD IMAGES FORM -->
<fieldset id="imageUplaod">
<legend>Upload Images <img src="../images/icon_help.png" width="16" height="16" class="cursorHand" onmouseover="Tip(tip_uploadImages)" onmouseout="UnTip()" /></legend>
<label class="floatLeft width10">Image File(s):</label><input type="file" id="bulkImagesSrc" name="bulkImagesSrc" size="60" class="floatLeft marginB5px" />
<br class="clearFloat" />
</fieldset>
<!-- END BULK UPLOAD IMAGES FORM -->
<!-- START BUTTON CONTAINER -->
<div class="alignCenter marginTB5px">
<button type="reset" id="bulkUploadClearBtn" name="bulkUploadClearBtn" class="btnRed cursorHand">Clear</button>
<button type="submit" id="bulkUploadSubmitBtn" name="bulkUploadSubmitBtn" class="btnBlue cursorHand">Upload</button>
</form>
</div>
<!-- END BUTTON CONTAINER -->
</div>
<!-- END BULK UPLOAD TAB CONTENT -->
<!-- START BULK UPLOAD TEMPLATES CONTENT-->
<div id="bulkTemplatesContent" style="display:none;">
<!-- START BULK UPLOAD TEMPLATES FORM -->
<fieldset id="bulkUploadTemplates">
<legend>Bulk Upload Templates <img src="../images/icon_help.png" width="16" height="16" class="cursorHand" onmouseover="Tip(tip_bulkTemplates)" onmouseout="UnTip()" /></legend>
<dl>
<dt>Default:</dt>
<dd>The is the default bulk upload spreadsheet template for the currently selected building. | <a href="javascript:void(0)" >Download Template</a></dd>
<hr />
<dt>Template Title:</dt>
<dd>Description... | <a href="javascript:void(0)" >Download Template</a></dd>
</dl>
</fieldset>
<!-- END BULK UPLOAD TEMPLATES FORM -->
</div>
<!-- END BULK UPLOAD TEMPLATES TAB CONTENT -->
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
