<%@include file='personInfo_code.inc'%>
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
<script type="text/javascript" src="../javascript/general_admin.js"></script>
<script type="text/javascript" src="../javascript/edifice_object.js"></script>
<script type="text/javascript" src="../javascript/company_object.js"></script>
<script type="text/javascript" src="../javascript/person_object.js"></script>
<script type="text/javascript">
//LOOSE JS HERE

/** This code makes sure that a building has been defined before this
 * page can be loaded.  It switches to the building page if there has
 * not been a building loaded.*/

var currentEdifice = new Edifice();
var currentPerson  = new Person();

function fuploadCB(data) {
  Person.logoCallback(data);
};

$(document).ready(function() {
  Person.clearPersonFields();
  $("button#findBtn").click(Person.findChosenPerson);
  $("button#clearBtn").click(Person.clearPersonSearch);
  $("button#updateBtn").click(Person.modifyPerson);
  $("button#clearPersonBtn").click(Person.clearPersonFields);
  $("button#addBtn").click(Person.addPerson);
  $("button#deleteBtn").click(Person.deletePerson);
  $("button#addImageCancelBtn").click(Person.cancelChangeLogo);
  $("button#addImageSubmitBtn").click(Person.submitChangeLogo);
  $("button#newCoCancelBtn").click(Person.cancelNewCo);
  $("button#newCoSubmitBtn").click(Person.submitNewCo);
  $("button#custFieldCancelBtn").click(Person.cancelCustField);
  $("button#custFieldSubmitBtn").click(Person.submitCustField);

/* Calls modal pop-up windows This strategy permits only one pop
 * up per .jsp page.*/
 //$("a.modalPopup").boxy({modal:true,title:true,closeable:true});
 $("a.modalPopupImage").click(Person.changeLogo);
 $("a.modalPopupNewco").click(Person.newCo);
 $("a.modalPopup").click(Person.customFields);

  UIU.cantSmite("findBtn");
  UIU.cantSmite("updateBtn");
  UIU.canSmite("addBtn");
  UIU.cantSmite("deleteBtn");
  UIU.setClearOnFocus();

  Person.fixSelector(); // Get the company selector filled in

// Get the current building for the personnel editor if there is one

  if (!currentEdifice.edificeJson) {
    Edifice.getCurrentEdifice(Edifice.getCBLocationsNoShow);
  }

    /* The current location goes into the global name space */
    currentPerson.suggestObj =
      new AutoSuggest(document.getElementById("SrchInputField"),
		      document.getElementById("Suggestions"),
		      "srchForm","findBtn");

});

</script>
<!-- END JAVASCRIPT -->
<title>Wayfinding System Administration - Personnel Information</title>
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
<li><a href="javascript:void(0)" class="current">
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

<!-- START PERSONELL SEARCH FORM  -->
<form name="srchForm" id="srchForm">
<div class="alignLeft">
<h4 class="floatLeft">Personnel Search</h4>
<div class="floatLeft">
<img src="../images/icon_help.png" width="16" height="16" class="marginL4px cursorHand" onmouseover="Tip(tip_personnelSearch)" onmouseout="UnTip()" /></div>
<div class="clearFloat"></div>
</div>

<!-- START PERSONELL SEARCH FIELD WITH OPTIONS  -->
<div class="admin_srchWithOptsBox" style="display:visible;">
<input type="hidden" name="SrchOptsMenu" value="person" />
<input type="text" name="SrchInputField" id="SrchInputField" class="width78" />
<select id="SrchFilter" name="SrchFilter" class="width20">
<option value = "0" selected>Filter Locations By...</option>
<option value="1">1</option>
<option value="2">2</option>
<option value="3">3</option>
</select>
<div id="Suggestions"><ul></ul></div>
</div>
<!-- END PERSONELL SEARCH FIELD WITH OPTIONS  -->

<!-- START PERSONELL SEARCH FIELD BUTTONS  -->
<button type="reset" id="clearBtn" name="clearBtn" class="btnRed cursorHand">Clear</button>
<button type="reset" id="findBtn" name="findBtn" class="btnBlue cursorHand">Find</button>
<!-- END PERSONELL SEARCH FIELD BUTTONS  -->

</form>
<!-- END PERSONELL SEARCH FORM -->

</div>
<!-- END ADMIN SEARCH CONTAINER -->

<!-- START CURRENT BLDG/LOCATION NAME  -->
<fieldset id="currentBLDGInfo">
<legend class="green">Current Building <img src="../images/icon_information.png" width="16" height="16" class="cursorHand" onmouseover="Tip(tip_currentBldg)" onmouseout="UnTip()" /></legend>
<h1 class="floatLeft"><span id="admin_bldg_name"></span> :
<span id="admin_person_name"></span></h1>
</fieldset>
<!-- END CURRENT BLDG/LOCATION NAME  -->

<!-- START BLDG INFO SUB NAV MENU -->
<ul id="way_adminSubMenu">
<li><a href="javascript:void(0)" class="current">General</a></li>
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
<!-- END FORM SUBMISSION MESSAGE BOXES -->
<!-- START GENERAL PERSONNAL INFO FORM CONTAINER -->
<div id="personnelGeneralInfoContent">
<!-- START GENERAL PERSONNAL INFO FORM -->
<form id="personnelGeneralInfoForm" name="personnelGeneralInfoForm">
<input type="hidden" name="fKey" value="admin_pers_">
<!-- START PERSONNAL DEFAULT INFO FEILD SET -->
<fieldset id="personnelGeneralInfo">
<legend>Default Personnel Information
<img src="../images/icon_help.png" width="16" height="16" class="cursorHand" onmouseover="Tip(tip_personnelDefaultInfo)" onmouseout="UnTip()" /></legend>
<label class="floatLeft width18">Current Photo:</label>
<div class="floatLeft">
<img id="admin_pers_thumb" src="../images/thumbnail.png"
class="borderGrey marginB5px" width="100" height="100" /></div>
<div class="floatLeft marginL2px marginT4px">
<img src="../images/icon_add.png" width="16" height="16" class="floatLeft" />
<p class="floatLeft marginL2px">
<a href="#addImageWizard" class="modalPopupImage" title="Upload/Change Photo">Upload/Change Photo</a></p>
<br class="clearFloat" />
</div>
<br class="clearFloat" />
<input type="hidden" id="admin_pers_image" name="admin_pers_image" value="">
<label class="floatLeft width18">First Name:</label>
<input type="text" id="admin_pers_firstName" name="admin_pers_firstName" class="floatLeft width25 marginB5px" />
<br class="clearFloat" />
<label class="floatLeft width18">Last Name:</label>
<input type="text" id="admin_pers_lastName" name="admin_pers_lastName" class="floatLeft width25 marginB5px" />
<br class="clearFloat" />
<label class="floatLeft width18">Title:</label>
<input type="text" id="admin_pers_title" name="admin_pers_title" class="floatLeft width25 marginB5px" />
<br class="clearFloat" />
<label class="floatLeft width18">Work Phone:</label>
<input type="text" id="admin_pers_workPhone" name="admin_pers_workPhone" class="floatLeft width25 marginB5px" />
<br class="clearFloat" />
<label class="floatLeft width18">Mobile Phone:</label>
<input type="text" id="admin_pers_mobilePhone" name="admin_pers_mobilePhone" class="floatLeft width25 marginB5px" />
<br class="clearFloat" />
<label class="floatLeft width18">Fax:</label>
<input type="text" id="admin_pers_fax" name="admin_pers_fax" class="floatLeft width25 marginB5px" />
<br class="clearFloat" />
<label class="floatLeft width18">Email:</label>
<input type="text" id="admin_pers_emailAddress" name="admin_pers_emailAddress" class="floatLeft width25 marginB5px" />
<br class="clearFloat" />
<label class="floatLeft width18">Company:</label>
<select id="admin_pers_organizationID" name="admin_pers_organizationID"
class="floatLeft marginB5px width26">
<option value="" selected >Select A Company</option>
</select>
<div class="floatLeft marginL2px marginT4px">
<img src="../images/icon_add.png" width="16" height="16" class="floatLeft" />
<p class="floatLeft marginL2px">
<a href="#addCompanyWizard" class="modalPopupNewco" title="Add A Company">Add A Company</a></p>
<br class="clearFloat" />
</div>
<br class="clearFloat" />
<label class="floatLeft width18">Details:</label>
<textarea name="admin_pers_details" id="admin_pers_details" class="width25 marginB5px"></textarea>
<br class="clearFloat" />
<label class="floatLeft width18">Tags:</label>
<input type="text" id="admin_pers_tags" name="admin_pers_tags" class="floatLeft width25 marginB5px" />
<br class="clearFloat" />
</fieldset>
<!-- END PERSONNAL DEFAULT INFO FEILD SET -->
<!-- START ADDITIONAL INFO FEILD SET -->
<fieldset id="personnelAdditionalInfo">
<legend>Additional Personnel Information (Optional) <img src="../images/icon_help.png" width="16" height="16" class="cursorHand" onmouseover="Tip(tip_personnelAdditionalInfo)" onmouseout="UnTip()" /></legend>
<div class="floatRight">
<img src="../images/icon_add.png" width="16" height="16" class=" floatLeft marginL5px" /></a>
<p class="floatLeft marginL2px"><a href="#addCustomFieldsWizard" class="modalPopup" title="Add Custom Field">Add Custom Field</a></p>
<br class="clearFloat" />
</div>
<br class="clearFloat" />
<p class="txtGrey"><em>No custom fields have been created.</em></p>
</fieldset>
<!-- END ADDITIONAL INFO FEILD SET -->
<!-- START BUTTON CONTAINER -->
<div id="formBtnContainer" class="alignCenter marginTB5px">
<button type="reset" id="clearPersonBtn" name="clearPersonBtn" class="btnRed cursorHand">Clear</button>
<button type="reset" id="addBtn" name="addBtn" class="btnBlue cursorHand">Add</button>
<button type="reset" id="deleteBtn" name="deleteBtn" class="btnRed cursorHand">Delete</button>
<button type="reset" id="updateBtn" name="updateBtn" class="btnBlue cursorHand">Update</button>
<div>
<!-- END BUTTON CONTAINER -->
</form>
<!-- END GENERAL PERSONNAL INFO FORM -->
<!-- START HIDDEN MODAL POPUP CONTENT -->
<!-- START ADD PHOTO WIZARD -->
<div id="addImageWizard" style="display:none;" class="width500px alignLeft">
<form id="addImageForm" name="addImageForm"
action="<%=response.encodeURL("/way_dynamic/fupload.jsp")%>"
method="post" target="subIF"
enctype="multipart/form-data" encoding="multipart/form-data">
<fieldset id="addImageField" >
<legend>Upload Photo
<img src="../images/icon_help.png" width="16" height="16" /></legend>
<label class="floatLeft width25">Photo Image File:</label>
<input type="file" id="imageSrc" name="imageSrc"
size="30" class="marginB5px" />
<br class="clearFloat" />
</fieldset>
<div class="alignCenter marginTB5px">
<button type="reset" id="addImageCancelBtn"
name="addImageCancelBtn" class="btnRed cursorHand">Cancel</button>
<button type="reset" id="addImageSubmitBtn"
name="addImageSubmitBtn" class="btnBlue cursorHand">Upload</button>
</div>
</form>
</div>
<!-- END ADD PHOTO WIZARD -->

<!-- START ADD COMPANY WIZARD -->
<div id="addCompanyWizard" style="display:none;" class="width500px alignLeft">
<form id="addCompanyForm" name="addCompanyForm">
<fieldset id="addCompanyField" >
<legend>Add A Company
<img src="../images/icon_help.png" width="16" height="16" /></legend>
<label class="floatLeft width30">Company Name:</label>
<input type="text" id="admin_co_name" name="admin_co_name"
class="floatLeft marginB5px width60" />
<label class="floatLeft width30">Division/Dept:</label>
<input type="text" id="admin_co_division" name="admin_co_division"
class="floatLeft marginB5px width60" />
<label class="floatLeft width30">Phone:</label>
<input type="text" id="admin_co_mainPhone" name="admin_co_mainPhone"
class="floatLeft marginB5px width60" />
<br class="clearFloat" />
</fieldset>
<div class="alignCenter marginTB5px">
<button type="reset" id="newCoCancelBtn" name="newCoCancelBtn" class="btnRed cursorHand">Cancel</button>
<button type="reset" id="newCoSubmitBtn" name="newCoSubmitBtn" class="btnBlue cursorHand">Add</button>
</div>
</form>
</div>
<!-- END ADD COMPANY WIZARD -->
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
<legend>Check Box #1 <img src="../images/icon_help.png" width="16" height="16" class="cursorHand" onmouseover="Tip(tip_addCustomField)" onmouseout="UnTip()"/></legend>
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
<legend>Custom Select Menu <img src="../images/icon_help.png" width="16" height="16" class="cursorHand" onmouseover="Tip(tip_addCustomField)" onmouseout="UnTip()"/></legend>
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
<img src="../images/icon_add.png" width="16" height="16" class=" floatLeft marginL5px" /></a>
<p class="floatLeft marginL2px"><a href="javascript:void(0)" title="Click To Add A Radio Button">Add A Radio Button</a> <span class="fontSize07Em">(2 radio buttons minimum)<span></p>
<br class="clearFloat" />
</div>
<!-- START RADIO BTN OPTIONS FIELDS -->
<fieldset>
<legend>Radio Button #1 <img src="../images/icon_help.png" width="16" height="16" class="cursorHand" onmouseover="Tip(tip_addCustomField)" onmouseout="UnTip()"/></legend>
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
<!-- END GENERAL PERSONNAL INFO FORM CONTAINER -->
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
