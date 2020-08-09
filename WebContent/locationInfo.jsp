<%@include file='locationInfo_code.inc'%>
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
<script type="text/javascript" src="../javascript/autosuggest.js"></script>
<script type="text/javascript" src="../javascript/niftycube.js"></script>
<script type="text/javascript" src="../javascript/utilityscripts.js"></script>
<script type="text/javascript" src="../javascript/htmlFrags.js"></script>
<script type="text/javascript" src="../javascript/general_admin.js"></script>
<script type="text/javascript" src="../javascript/edifice_object.js"></script>
<script type="text/javascript" src="../javascript/location_object.js"></script>
<script type="text/javascript">
//LOOSE JS HERE

/** This code makes sure that a building has been defined before this
 * page can be loaded.  It switches to the building page if there has
 * not been a building loaded.*/

var currentEdifice  = new Edifice();
var currentLocation = new Location();

function fuploadCB(data) {
  console.log("fupload " + data);
  Location.addImageCB(data);
};

$(document).ready(function() {
  Location.clearLocationFields();
  $("button#findBtn").click(Location.findChosenLocation);
  $("button#clearBtn").click(Location.clearLocationSearch);
  $("button#updateBtn").click(Location.modifyLocation);
  $("button#addImageCancelBtn").click(Location.clearAddImage);
  $("button#addImageSubmitBtn").click(Location.submitAddImage);
  $("button#unassignPersonnelBtn").click(Location.unassignPersonnel);
  $("button#assignPersonnelBtn").click(Location.assignPersonnel);
  $("button#unassignCompanyBtn").click(Location.unassignCompany);
  $("button#assignCompanyBtn").click(Location.assignCompany);
  $("button#custFieldCancelBtn").click(Location.cancelCustField);
  $("button#custFieldSubmitBtn").click(Location.submitCustField);

  $("#filterCompanyName").change(Location.populatePotentialCompanies);
  $("#filterLastName").change(Location.populatePotentialPersonnel);
  $("#filterCompany").change(Location.populatePotentialPersonnel);

  $("#images_content").empty();

  $("a.modalPopup").click(Location.customFields);

  UIU.cantSmite("findBtn");
  UIU.setClearOnFocus();

// Get the current building for the location editor if there is one

  if (!currentEdifice.edificeJson) {
    Edifice.getCurrentEdifice(Edifice.getCBLocationsNoShow);
  }

    /* The current location goes into the global name space */
    currentLocation.suggestObj =
      new AutoSuggest(document.getElementById("SrchInputField"),
		      document.getElementById("Suggestions"),
		      "srchForm","findBtn");

});

</script>
<!-- END JAVASCRIPT -->
<title>Wayfinding System Administration - Location Information</title>
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
<li><a href="javascript:void(0)" class="current">
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
<div id="way_adminSearchContainer" class="bgWhite alignCenter width75">

<!-- START LOCATION SEARCH FORM  -->
<form name="srchForm" id="srchForm">
<div class="alignLeft">
<h4 class="floatLeft">Location Search</h4>
<div class="floatLeft">
<img src="../images/icon_help.png" width="16" height="16" class="marginL4px cursorHand" onmouseover="Tip(tip_locationSearch)" onmouseout="UnTip()" /></div>
<div class="clearFloat"></div>
</div>

<!-- START LOCATION SEARCH FIELD WITH OPTIONS  -->
<div class="admin_srchWithOptsBox" style="display:visible;">
<input type="hidden" name="SrchOptsMenu" value="location" />
<input type="text" name="SrchInputField" id="SrchInputField" class="width78" />
<select id="SrchFilter" name="SrchFilter" class="width20">
<option value = "0" selected>Filter Locations By...</option>
<option value="1">1</option>
<option value="2">2</option>
<option value="3">3</option>
</select>
<div id="Suggestions"><ul></ul></div>
</div>
<!-- END LOCATION SEARCH FIELD WITH OPTIONS  -->

<!-- START LOCATION SEARCH FIELD BUTTONS  -->
<button type="reset" id="clearBtn" name="clearBtn" class="btnRed cursorHand">
Clear</button>
<button type="reset" id="findBtn" name="findBtn" class="btnBlue cursorHand">
Find</button>
<!-- END LOCATION SEARCH FIELD BUTTONS  -->

</form>
<!-- END LOCATION SEARCH FORM -->

</div>
<!-- END ADMIN SEARCH CONTAINER -->

<!-- START CURRENT BLDG/LOCATION NAME  -->
<fieldset id="currentBLDGInfo">
<legend class="green">Current Building &amp; Location
<img src="../images/icon_information.png" width="16" height="16"
class="cursorHand" onmouseover="Tip(tip_currentBldgLoco)"
onmouseout="UnTip()" /></legend>
<h1 class="floatLeft"><span id="admin_bldg_name"></span> :
<span id="admin_loc_name"></span></h1>
<div class="floatRight" class="display:none;"><div class="marginT2px floatLeft">
<img src="../images/icon_view.png" width="16" height="16" alt="" /></div>
<a href="mapwindow.html" class="popupwindow fontSize08Em" rel="mapWindow"
title="Click to View Location" />View Location</a></div>
<br class="clearFloat" />
</fieldset>
<!-- START CURRENT BLDG/LOCATION NAME  -->

<!-- START BLDG INFO SUB NAV MENU -->
<ul id="way_adminSubMenu">
<li><a href="javascript:void(0)" id="tabGeneral" class="current">General</a></li>
<li><a href="javascript:void(0)" id="tabAssignPersonnel" class="">Assign Personnel</a></li>
<li><a href="javascript:void(0)" id="tabAssignCompany" class="">Assign Company</a></li>
<li><a href="javascript:void(0)" id="tabImages" class="">Upload Images</a></li>
<!-- <li><a href="javascript:void(0)" id="tabCameras" class="">Security Cameras</a></li> -->
</ul>
<!-- END BLDG INFO SUB NAV MENU -->
</div>
<!-- END ADMIN SUB NAV CONTAINER -->

<!-- START ADMIN FORMS CONTAINER -->
<div id="way_adminFormsContainer" class="bgWhite">
<!-- START FORM SUBMISSION MESSAGE BOXES -->
<!-- START ERROR MSG BOX -->
<div id="formMsgBoxError" class="formError padding8px" style="display:visible">
<div class="floatLeft marginR5px">
<img src="../images/icon_alert.png" width="16" height="16" alt="" />
</div>
<div id="formMsgBoxErrorContent" class="floatLeft boldText">
The following fields have errors or have not
been filled in.&nbsp; Please correct the problems and re-submit the form.
<ul>
<li>field name 1</li>
<li>field name 2</li>
</ul>
</div>
<br class="clearFloat"/>
</div>
<!-- END ERROR MSG BOX -->
<!-- START SUCCESS MSG BOX -->
<div id="formMsgBoxSuccess" class="formSuccess padding8px"
style="display:visible">
<div class="floatLeft marginR5px">
<img src="../images/icon_finished.png" width="16" height="16" alt="" />
</div>
<div  id="formMsgBoxSuccessContent" class="floatLeft boldText">
The operation has been successfully completed.
</div>
<br class="clearFloat"/>
</div>
<!-- ENDSUCCESS MSG BOX -->
<!-- START END SUBMISSION MESSAGE BOX -->
<!-- START GENERAL INFO TAB CONTENT -->
<div id="locationGeneralContent" style="display:visible;">
<!-- START GENERAL INFO FORM -->
<form id="locationDefaultInfo" name="locationInfo">
<input type="hidden" name="fKey" value="admin_loc_">
<!-- START DEFAULT INFO FEILD SET -->
<fieldset id="locationDefaultInfo">
<legend>Default Location Information
<img src="../images/icon_help.png" width="16" height="16" class="cursorHand"
onmouseover="Tip(tip_locationDefaultInfo)" onmouseout="UnTip()" /></legend>
<label class="floatLeft width18">Location Name:</label>
<input type="text" id="admin_loc_displayName" name="admin_loc_displayName"
class="floatLeft width25 marginB5px" />
<br class="clearFloat" />
<label  id="locationAccessibilityLbl" class="floatLeft width18">
Location Accessibility:</label>
<select id="locationAccessibility" name="locationAccessibility"
class="floatLeft marginB5px width26">
<option selected />Set Location Accessibility
<option value="n">Accessible</option>
<option value="Y">Not Accessible</option>
</select>
<br class="clearFloat" />
<label class="floatLeft width18">Location Details:</label>
<textarea name="admin_loc_details" id="admin_loc_details"
class="width25 marginB5px"> </textarea>
<br class="clearFloat" />
<label class="floatLeft width18">Location Tags:</label>
<input type="text" id="admin_loc_tags" name="admin_loc_tags"
class="floatLeft width25 marginB5px" />
<br class="clearFloat" />
</fieldset>
<!-- END DEFAULT INFO FEILD -->
<!-- START ADDITIONAL INFO FEILD SET -->
<fieldset id="locationAdditionalInfo">
<legend>Additional Location Information (Optional) <img src="../images/icon_help.png" width="16" height="16" class="cursorHand" onmouseover="Tip(tip_locationAdditionalInfo)" onmouseout="UnTip()" /></legend>
<div class="floatRight">
<img src="../images/icon_add.png" width="16" height="16" class="floatLeft marginL5px" /></a>
<p class="floatLeft marginL2px">
<a href="#addCustomFieldsWizard" class="modalPopup" title="Add Custom Field">Add Custom Field</a></p>
<br class="clearFloat" />
</div>
<br class="clearFloat" />
<p class="txtGrey"><em>No custom fields have been created.</em></p>
</fieldset>
<!-- END ADDITIONAL INFO FEILD SET -->
<!-- START BUTTON CONTAINER -->
<div class="alignCenter marginTB5px">
<!-- <button type="reset" id="genInfoClearBtn" name="genInfoClearBtn" class="btnRed cursorHand">Clear</button> -->
<button type="reset" id="updateBtn" name="updateBtn"
class="btnBlue cursorHand">Update</button>
</div>
<!-- END BUTTON CONTAINER -->
</form>
<!-- END GENERAL INFO FORM -->
</div>
<!-- END GENERAL INFO TAB CONTENT -->

<!-- START ASSIGN PRESONNEL TAB CONTENT -->
<div id="locationPersonnelContent" style="display:none;">
<form id="unassignPersonnelForm" name="unassignPersonnelForm">
<fieldset id="currentPersonnel" class="width45 floatLeft">
<legend>Currently Assigned Personnel
<img src="../images/icon_help.png" width="16" height="16" class="cursorHand"
onmouseover="Tip(tip_locationPeopleAssiged)" onmouseout="UnTip()" /></legend>
<select id="assignedPersonnelList" name="assignedPersonnelList"
class="floatLeft
width100 height245px" multiple>
</select>
<br class="clearFloat" />
<!-- START BUTTON CONTAINER -->
<div class="alignCenter marginTB5px">
<button type="reset" id="unassignPersonnelBtn"
name="unassignPersonnelBtn" class="btnBlue cursorHand">
Un-assign From Location</button>
<p class="exampleTxt">(hold Ctrl &amp; click to select multiple people)</p>
</div>
<!-- END BUTTON CONTAINER -->
</fieldset>
</form>
<form id="assignPersonnelForm" name="assignPersonnelForm">
<fieldset id="assignPersonnel" class="width45 floatRight">
<legend>Assign Personnel To Location
<img src="../images/icon_help.png" width="16" height="16" class="cursorHand"
onmouseover="Tip(tip_locationPeopleUnAssiged)" onmouseout="UnTip()" /></legend>
<p class="marginB5px"><strong>Filter By:</strong></p>
<label class="floatLeft alignLeft">Letter:</label>
<select id="filterLastName" name="filterLastName"
class="floatLeft width30 marginB5px">
<option selected>All</option>
<option>A</option>
<option>B</option>
<option>C</option>
<option>D</option>
<option>E</option>
<option>F</option>
<option>G</option>
<option>H</option>
<option>I</option>
<option>J</option>
<option>K</option>
<option>L</option>
<option>M</option>
<option>N</option>
<option>O</option>
<option>P</option>
<option>Q</option>
<option>R</option>
<option>S</option>
<option>T</option>
<option>U</option>
<option>V</option>
<option>W</option>
<option>X</option>
<option>Y</option>
<option>Z</option>
</select>
<p class="floatLeft marginT4px marginL10px">or</p>
<label class="floatLeft width15">Company:</label>
<select id="filterCompany" name="filterCompany"
class="floatLeft width30 marginB5px">
<option selected>All</option>
</select>
<br class="clearFloat" />
<select id="unassignedPersonnelList"
name="unassignedPersonnelList" class="floatLeft width100 height200px" multiple>
</select>
<br class="clearFloat" />
<!-- START BUTTON CONTAINER -->
<div class="alignCenter marginTB5px">
<button type="reset" id="assignPersonnelBtn"
name="assignPersonnelSubmitBtn" class="btnBlue cursorHand">
Assign To Location</button>
<p class="exampleTxt">(hold Ctrl &amp; click to select multiple people)</p>
</div>
<!-- END BUTTON CONTAINER -->
</fieldset>
<br class="clearFloat" />
</form>
</div>
<!-- END ASSIGN PRESONNEL TAB CONTENT -->

<!-- START ASSIGN COMPANY TAB CONTENT -->
<div id="locationCompanyContent" style="display:none;">
<form id="unassignCompanyForm" name="unassignCompanyForm">
<fieldset id="currentCompanies" class="width45 floatLeft">
<legend>Currently Assigned Companies <img src="../images/icon_help.png" width="16" height="16" class="cursorHand" onmouseover="Tip(tip_locationCompanyAssiged)" onmouseout="UnTip()" /></legend>
<select id="assignedCompanies" name="assignedCompanies" class="floatLeft width100 height245px" multiple>
<option>ACME Company</option>
<option>IBM</option>
</select>
<br class="clearFloat" />
<!-- START BUTTON CONTAINER -->
<div class="alignCenter marginTB5px">
<button type="reset" id="unassignCompanyBtn" name="unassignCompanyBtn"
class="btnBlue cursorHand">Un-assign From Location</button>
<p class="exampleTxt">(hold Ctrl &amp; click to select multiple companies)</p>
</div>
<!-- END BUTTON CONTAINER -->
</fieldset>
</form>
<form id="assignCompanyForm" name="assignCompanyForm">
<fieldset id="assignCompany" class="width45 floatRight">
<legend>Assign Company To Location <img src="../images/icon_help.png" width="16" height="16" class="cursorHand" onmouseover="Tip(tip_locationCompanyUnAssiged)" onmouseout="UnTip()" /></legend>
<p class="marginB5px"><strong>Filter By:</strong></p>
<label class="floatLeft alignLeft">Letter:</label>
<select id="filterCompanyName" name="filterCompanyName"
class="floatLeft width30 marginB5px">
<option selected>All</option>
<option>A</option>
<option>B</option>
<option>C</option>
<option>D</option>
<option>E</option>
<option>F</option>
<option>G</option>
<option>H</option>
<option>I</option>
<option>J</option>
<option>K</option>
<option>L</option>
<option>M</option>
<option>N</option>
<option>O</option>
<option>P</option>
<option>Q</option>
<option>R</option>
<option>S</option>
<option>T</option>
<option>U</option>
<option>V</option>
<option>W</option>
<option>X</option>
<option>Y</option>
<option>Z</option>
</select>
<br class="clearFloat" />
<select id="companyNameList" name="companyNameList"
class="floatLeft width100 height200px" multiple>
</select>
<br class="clearFloat" />
<!-- START BUTTON CONTAINER -->
<div class="alignCenter marginTB5px">
<button type="reset" id="assignCompanyBtn"
name="assignCompanyBtn" class="btnBlue cursorHand">
Assign To Location</button>
<p class="exampleTxt">(hold Ctrl &amp; click to select multiple companies)</p>
</div>
<!-- END BUTTON CONTAINER -->
</fieldset>
<br class="clearFloat" />
</form>
</div>
<!-- END ASSIGN COMPANY TAB CONTENT -->
<!-- START IMAGE TAB CONTENT -->
<div id="locationImageContent" style="display:none;">
<!-- START UPLOAD IMAGE FORM -->
<form id="addImageForm" name="addImageForm"
action="<%=response.encodeURL("/way_dynamic/fupload.jsp")%>"
method="post" target="subIF"
enctype="multipart/form-data" encoding="multipart/form-data">
<fieldset id="addImageField">
<legend>Upload Photos
<img src="../images/icon_help.png" width="16" height="16"
class="cursorHand" onmouseover="Tip(tip_locationUploadPhotos)"
onmouseout="UnTip()" /></legend>
<label class="floatLeft width10">Image File:</label>
<input type="file" id="addImageSrc" name="addImageSrc" size="47"
class="floatLeft marginB5px" />
<br class="clearFloat" />
<label class="floatLeft width10">Caption:</label>
<input type="text" id="addImageCaption" name="addImageCaption"
class="floatLeft width40 marginB5px" />
<br class="clearFloat" />
</fieldset>
</form>
<!-- START BUTTON CONTAINER -->
<div class="alignCenter marginTB5px">
<button type="reset" id="addImageCancelBtn" name="addImageCancelBtn"
class="btnRed cursorHand">Clear</button>
<button type="reset" id="addImageSubmitBtn" name="addImageSubmitBtn"
class="btnBlue cursorHand">Upload</button>
</div>
<!-- END BUTTON CONTAINER -->
<!-- END UPLOAD IMAGE FORM -->
<!-- START CURRENT IMAGE LIST/THUMBS -->
<fieldset id="currentImages">
<legend>Current Location Photos
<img src="../images/icon_help.png" width="16" height="16" class="cursorHand"
onmouseover="Tip(tip_locationCurrentPhotos)" onmouseout="UnTip()" /></legend>
<div id="images_content">
<!-- START PHOTO THUMB 0 -->
<div id="imageThumb0" class="margin8px">
<div class="floatLeft marginR5px">
<img src="../images/thumbnail.png" width="100" height="100" class="borderGrey" /></div>
<div class="floatLeft"><p><strong>caption</strong></p><p><em>filename</em></p></div>
<div class="floatRight paddingT40px">
<img src="../images/icon_delete.png" width="16" height="16" class="cursorHand" onmouseover="Tip(tip_delete)" onmouseout="UnTip()" /></div>
<div class="clearFloat"></div>
</div>
<!-- END PHOTO THUMB 0 -->
<hr />
<!-- START PHOTO THUMB 1 -->
<div id="imageThumb1_" class="margin8px">
<div class="floatLeft marginR5px">
<img id="imageThumb1_thumb" src="../images/thumbnail.png"
width="100" height="100" class="borderGrey" />
</div>
<div class="floatLeft">
<p><strong id="imageThumb1_caption">caption</strong></p>
<p><em id="imageThumb1_filename">filename</em></p>
</div>
<div class="floatRight paddingT40px">
<img id="imageThumb1_delete" src="../images/icon_delete.png"
width="16" height="16"
class="cursorHand" onmouseover="Tip(tip_delete)" onmouseout="UnTip()" />
</div>
<div class="clearFloat"></div>
</div>
<!-- END PHOTO THUMB 1 -->
</div>
</fieldset>
<!-- END CURRENT IMAGE LIST/THUMBS -->
</div>
<!-- END IMAGE TAB CONTENT -->

<!-- START CAMERA TAB CONTENT -->
<div id="locationCameraContent" style="display:none;">
content here...
</div>
<!-- END CAMERA TAB CONTENT -->

<!-- START HIDDEN MODAL POPUP CONTENT -->
<!-- START ADD CUSTOM FIELD WIZARD -->
<div id="addCustomFieldsWizard" style="display:none;" class="width500px alignLeft">
<form id="addcustomFieldForm" name="addcustomFieldForm">
<!-- START FIELD TYPE SELECT -->
<fieldset id="customFieldType">
<legend>Custom Field Type <img src="../images/icon_help.png" width="16" height="16" class="cursorHand" onmouseover="Tip(tip_addCustomField)" onmouseout="UnTip()" /></legend>
<label class="floatLeft width25">Field Type:</label><select id="addFieldType" name="addFieldType" class="width50" onchange="custFieldSelect();">
<option selected />Select A Field Type</option>
<option value="customInputBox" >Input Box</option>
<option value="customCheckBox" />Check Box</option>
<option value="customRadioBtn" />Radio Buttons</option>
<option value="customTextArea" />Text Area</option>
<option value="customSelectBox" />Select Box</option>
</select>
<br class="clearFloat" />
</fieldset>
<!-- END FIELD TYPE SELECT -->
<!-- START ADD INPUT FIELDS -->
<fieldset id="addInputBoxField" style="display:none;">
<legend>Custom Input Field <img src="../images/icon_help.png" width="16" height="16" class="cursorHand" onmouseover="Tip(tip_addCustomField)" onmouseout="UnTip()" /></legend>
<label class="floatLeft width25">Label Name:</label><input type="text" id="addInputBoxLabelName" name="addInputBoxLabelName" class="floatLeft marginB5px width50" />
<label class="floatLeft width25">Field Name:</label><input type="text" id="addInputBoxFieldName" name="addInputBoxFieldName" class="floatLeft marginB5px width50" />
<br class="clearFloat" />
<label class="floatLeft width25">Required Field?:</label>
<div class="floatLeft width50">
<input type="radio" name="addInputBoxRequired" id="addInputBoxRequired" value="No" class="floatLeft marginB5px marginR5px checkBox" checked /><label class="floatLeft chkBoxRadio">No</label>
<input type="radio" name="addInputBoxRequired" id="addInputBoxRequired" value="Yes" class="floatLeft marginB5px marginL5px marginR5px checkBox" /><label class="floatLeft chkBoxRadio">Yes</label>
</div>
<br class="clearFloat" />
</fieldset>
<!-- END ADD INPUT FIELDS -->
<!-- START ADD CHECKBOX FIELDS -->
<div id="addCheckBoxField" style="display:none;" class="height250px scrollAuto">
<fieldset>
<legend>Custom Checkboxes <img src="../images/icon_help.png" width="16" height="16" class="cursorHand" onmouseover="Tip(tip_addCustomField)" onmouseout="UnTip()" /></legend>
<label class="floatLeft width25">Label Name:</label><input type="text" id="addInputBoxLabelName" name="addInputBoxLabelName" class="floatLeft marginB5px width50" />
<br class="clearFloat" />
<label class="floatLeft width25">Required Field?:</label>
<div class="floatLeft width50">
<input type="radio" name="addCheckBoxRequired" id="addCheckBoxRequired" value="No" class="floatLeft marginB5px marginR5px checkBox" checked  /><label class="floatLeft chkBoxRadio">No</label>
<input type="radio" name="addCheckBoxRequired" id="addCheckBoxRequired" value="Yes" class="floatLeft marginB5px marginL5px marginR5px checkBox" /><label class="floatLeft chkBoxRadio">Yes</label>
</div>
<br class="clearFloat" />
<div class="padding8px">
<img src="../images/icon_add.png" width="16" height="16" class="floatLeft marginL5px" /></a>
<p class="floatLeft marginL2px"><a href="javascript:void(0)" title="Click To Add A Checkbox">Add A Checkbox</a></p>
<br class="clearFloat" />
</div>
<!-- START CHECKBOX OPTIONS FIELDS -->
<fieldset>
<legend>Check Box #1 <img src="../images/icon_help.png" width="16" height="16" class="cursorHand" onmouseover="Tip(tip_addCustomField)" onmouseout="UnTip()" /></legend>
<label class="floatLeft width24">Name:</label><input type="text" id="addInputCheckBox1Name" name="addInputCheckBox1Name" class="floatLeft marginB5px width50" />
<br class="clearFloat" />
<label class="floatLeft width24">Value:</label><input type="text" id="addInputCheckBox1Value" name="addInputCheckBox1Value" class="floatLeft marginB5px width50" />
<br class="clearFloat" />
</fieldset>
<fieldset>
<legend>Check Box #2 <img src="../images/icon_help.png" width="16" height="16" class="cursorHand" onmouseover="Tip(tip_addCustomField)" onmouseout="UnTip()" /></legend>
<label class="floatLeft width24">Name:</label><input type="text" id="addInputCheckBox2Name" name="addInputCheckBox2Name" class="floatLeft marginB5px width50" />
<br class="clearFloat" />
<label class="floatLeft width24">Value:</label><input type="text" id="addInputCheckBox2Value" name="addInputCheckBox2Value" class="floatLeft marginB5px width50" />
<br class="clearFloat" />
</fieldset>
<!-- END CHECKBOX OPTIONS FIELDS-->
</fieldset>
</div>
<!-- END ADD CHECKBOXES FIELD-->
<!-- START ADD SELECT FIELD -->
<div id="addSelectMenuField" style="display:none;" class="height250px scrollAuto">
<fieldset>
<legend>Custom Select Menu <img src="../images/icon_help.png" width="16" height="16" class="cursorHand" onmouseover="Tip(tip_addCustomField)" onmouseout="UnTip()" /></legend>
<label class="floatLeft width25">Label Name:</label><input type="text" id="addSelectMenuLabelName" name="addSelectMenuLabelName" class="floatLeft marginB5px width50" />
<br class="clearFloat" />
<label class="floatLeft width25">Field Name:</label><input type="text" id="addSelectMenuFieldName" name="addSelectMenuFieldName" class="floatLeft marginB5px width50" />
<br class="clearFloat" />
<label class="floatLeft width25">Required Field?:</label>
<div class="floatLeft width50">
<input type="radio" name="addSelectMenuRequired" id="addSelectMenuRequired" value="No" class="floatLeft marginB5px marginR5px checkBox" checked  /><label class="floatLeft chkBoxRadio">No</label>
<input type="radio" name="addSelectMenuRequired" id="addSelectMenuRequired" value="Yes" class="floatLeft marginB5px marginL5px marginR5px checkBox" /><label class="floatLeft chkBoxRadio">Yes</label>
</div>
<br class="clearFloat" />
<div class="padding8px">
<img src="../images/icon_add.png" width="16" height="16" class="floatLeft marginL5px" /></a>
<p class="floatLeft marginL2px"><a href="javascript:void(0)" title="Click To Add A Menu Option">Add A Menu Option</a></p>
<br class="clearFloat" />
</div>
<!-- START MENU OPTION FIELDS -->
<fieldset>
<legend>Menu Option #1 <img src="../images/icon_help.png" width="16" height="16" class="cursorHand" onmouseover="Tip(tip_addCustomField)" onmouseout="UnTip()" /></legend>
<label class="floatLeft width24">Name:</label><input type="text" id="addSelectMenuOpt1Name" name="addSelectMenuOpt1Name" class="floatLeft marginB5px width50" />
<br class="clearFloat" />
<label class="floatLeft width24">Value:</label><input type="text" id="addSelectMenuOpt1Value" name="addSelectMenuOpt1Value" class="floatLeft marginB5px width50" />
<br class="clearFloat" />
</fieldset>
<fieldset>
<legend>Menu Option #2 <img src="../images/icon_help.png" width="16" height="16" class="cursorHand" onmouseover="Tip(tip_addCustomField)" onmouseout="UnTip()" /></legend>
<label class="floatLeft width24">Name:</label><input type="text" id="addSelectMenuOpt2Name" name="addSelectMenuOpt2Name" class="floatLeft marginB5px width50" />
<br class="clearFloat" />
<label class="floatLeft width24">Value:</label><input type="text" id="addSelectMenuOpt2Value" name="addSelectMenuOpt2Value" class="floatLeft marginB5px width50" />
<br class="clearFloat" />
</fieldset>
<!-- END MENU OPTION FIELDS -->
</fieldset>
</div>
<!-- END ADD SELECT FIELD -->
<!-- START ADD RADIO BTNS FIELDS -->
<div id="addRadioBtnField" style="display:none;" class="height250px scrollAuto">
<fieldset>
<legend>Custom Radio Buttons <img src="../images/icon_help.png" width="16" height="16" class="cursorHand" onmouseover="Tip(tip_addCustomField)" onmouseout="UnTip()" /></legend>
<label class="floatLeft width25">Label Name:</label><input type="text" id="addRadioBtnLabelName" name="addRadioBtnLabelName" class="floatLeft marginB5px width50" />
<br class="clearFloat" />
<label class="floatLeft width25">Field Name:</label><input type="text" id="addRadioBtnLabelName" name="addRadioBtnLabelName" class="floatLeft marginB5px width50" />
<br class="clearFloat" />
<label class="floatLeft width25">Required Field?:</label>
<div class="floatLeft width50">
<input type="radio" name="addRadioBtnRequired" id="addRadioBtnRequired" value="No" class="floatLeft marginB5px marginR5px checkBox" checked  /><label class="floatLeft chkBoxRadio">No</label>
<input type="radio" name="addRadioBtnRequired" id="addRadioBtnRequired" value="Yes" class="floatLeft marginB5px marginL5px marginR5px checkBox" /><label class="floatLeft chkBoxRadio">Yes</label>
</div>
<br class="clearFloat" />
<div class="padding8px">
<img src="../images/icon_add.png" width="16" height="16" class="floatLeft marginL5px cursorHand" /></a>
<p class="floatLeft marginL2px"><a href="javascript:void(0)" title="Click To Add A Radio Button">Add A Radio Button</a> <span class="fontSize07Em">(2 radio buttons minimum)<span></p>
<br class="clearFloat" />
</div>
<!-- START RADIO BTN OPTIONS FIELDS -->
<fieldset>
<legend>Radio Button #1 <img src="../images/icon_help.png" width="16" height="16" class="cursorHand" onmouseover="Tip(tip_addCustomField)" onmouseout="UnTip()" /></legend>
<label class="floatLeft width24">Button Label:</label><input type="text" id="addRadioBtn1Name" name="addRadioBtn1Name" class="floatLeft marginB5px width50" />
<br class="clearFloat" />
<label class="floatLeft width24">Value:</label><input type="text" id="addRadioBtn1Value" name="addRadioBtn1Value" class="floatLeft marginB5px width50" />
<br class="clearFloat" />
</fieldset>
<fieldset>
<legend>Radio Button #2 <img src="../images/icon_help.png" width="16" height="16" class="cursorHand" onmouseover="Tip(tip_addCustomField)" onmouseout="UnTip()" /></legend>
<label class="floatLeft width24">Button Label:</label><input type="text" id="addRadioBtn2Name" name="addRadioBtn2Name" class="floatLeft marginB5px width50" />
<br class="clearFloat" />
<label class="floatLeft width24">Value:</label><input type="text" id="addRadioBtn2Value" name="addRadioBtn2Value" class="floatLeft marginB5px width50" />
<br class="clearFloat" />
</fieldset>
<!-- END RADIO BTN OPTIONS FIELDS-->
</fieldset>
</div>
<!-- END ADD RADIO BTNS FIELD-->
<!-- START ADD TEXT AREA FIELDS -->
<fieldset id="addTextAreaField" style="display:none;">
<legend>Custom Text Area <img src="../images/icon_help.png" width="16" height="16" class="cursorHand" onmouseover="Tip(tip_addCustomField)" onmouseout="UnTip()" /></legend>
<label class="floatLeft width25">Label Name:</label><input type="text" id="addTextAreaLabelName" name="addTextAreaLabelName" class="floatLeft marginB5px width50" />
<label class="floatLeft width25">Field Name:</label><input type="text" id="addTextAreaFieldName" name="addTextAreaFieldName" class="floatLeft marginB5px width50" />
<br class="clearFloat" />
<label class="floatLeft width25">Required Field?:</label>
<div class="floatLeft width50">
<input type="radio" name="addTextAreaRequired" id="addTextAreaRequired" value="No" class="floatLeft marginB5px marginR5px checkBox" checked /><label class="floatLeft chkBoxRadio">No</label>
<input type="radio" name="addITextAreaRequired" id="addTextAreaxRequired" value="Yes" class="floatLeft marginB5px marginL5px marginR5px checkBox" /><label class="floatLeft chkBoxRadio">Yes</label>
</div>
<br class="clearFloat" />
</fieldset>
<!-- END ADD TEXT AREA FIELDS -->
<!-- START ADD FIELD WIZARD MODAL POUP BTNS -->
<div class="alignCenter marginTB5px">
<button type="reset" id="custFieldCancelBtn" name="custFieldCancelBtn" class="btnRed cursorHand">Cancel</button>
<button type="reset" id="custFieldSubmitBtn" name="custFieldSubmitBtn" class="btnBlue cursorHand">Add Field</button>
</div>
<!-- END ADD FIELD WIZARD MODAL POUP BTNS -->
</div>
<!-- END ADD CUSTOM FIELD WIZARD -->
<!-- END HIDDEN MODAL POPUP CONTENT -->
</div>
<!-- END ADMIN FORMS CONTAINER -->
<!-- START ADMIN FOOTER CONTAINER -->
<%@include file='footer.inc'%>
<!-- START ADMIN FOOTER CONTAINER -->
</div>
<!-- END CONTENT CONTAINER -->
</div>
<!-- END OUTER CONTAINER -->
<iframe src="about:blank" name="subIF" id="subIF" style="display: none"></iframe>
</body>
</html>
<!-- EOF -->
