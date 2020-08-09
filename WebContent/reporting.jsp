<%@include file='reporting_code.inc'%>
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
<script type="text/javascript" src="../javascript/edifice_object.js"></script>
<script type="text/javascript" src="../javascript/report_object.js"></script>
<script type="text/javascript">
//LOOSE JS HERE

/** This code makes sure that a building has been defined before this
 * page can be loaded.  It switches to the building page if there has
 * not been a building loaded.*/

var currentEdifice  = new Edifice();

$(document).ready(function() {
  Report.clearReportFields();
  $("button#clearReportBtn").click(Report.clearReportFields);
  $("button#generateReportBtn").click(Report.generateReport);

// Get the current building for the location editor if there is one

  if (!currentEdifice.edificeJson) {
    Edifice.getCurrentEdifice(Edifice.getCBLocationsNoShow);
  }

});

</script>
<!-- END JAVASCRIPT -->
<title>Wayfinding System Administration - Reporting</title>
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
<li><a href="javascript:void(0)" class="current">Reports</a></li>
<li><a href="<%=URLEncoder.encode("updates.jsp")%>">
System Updates</a></li>
<li><a href="<%=URLEncoder.encode("bulkUploads.jsp")%>">
Bulk Upload</a></li>
</ul>
</div>
<!-- END ADMIN NAV CONTAINER -->
<!-- START ADMIN SUB NAV CONTAINER -->
<div id="way_adminSubNavContainer" class="bgWhite">

<!-- START CURRENT BLDG/LOCATION NAME  -->
<fieldset id="currentBLDGInfo">
<legend class="green">Current Building
<img src="../images/icon_information.png" width="16" height="16"
class="cursorHand" onmouseover="Tip(tip_currentBldg)" onmouseout="UnTip()" />
</legend>
<h1><span id="admin_bldg_name"></span> </h1>
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
<!-- START FORM SUBMISSION MESSAGE BOX -->
<div id="formMsgBox" class="formSuccess" style="display:none"><p><img src="../images/icon_finished.png" width="16" height="16" alt="" /> <img src="../images/icon_alert.png" width="16" height="16" alt="" /> Message...</p></div>
<!-- END FORM SUBMISSION MESSAGE BOX -->
<!-- START REPORTS TAB CONTENT -->
<div id="generateReportContent" style="display:visible;">
<!-- START GENERATE REPORTS FORM -->
<form id="generateReportForm" name="generateReportForm">
<!-- START START DATE SELECT FIELDSET -->
<fieldset id="sofwareUpdatesFields" class="width47 floatLeft">
<legend>Start Date
<img src="../images/icon_help.png" width="16" height="16"
class="cursorHand" onmouseover="Tip(tip_startDate)" onmouseout="UnTip()" />
</legend>
<input type="hidden" name="name" value="value">
<label class="floatLeft">Select A Start Date:</label>
<select id="startSelectMonth" name="startSelectMonth" class="floatLeft">
<option value="All">All Months</option>
<option value="0">Jan</option>
<option value="1">Feb</option>
<option value="2">Mar</option>
<option value="3">Apr</option>
<option value="4">May</option>
<option value="5">Jun</option>
<option value="6">Jul</option>
<option value="7">Aug</option>
<option value="8">Sept</option>
<option value="9">Oct</option>
<option value="10">Nov</option>
<option value="11">Dec</option>
</select>
<select id="startSelectDay" name="startSelectDay" class="floatLeft">
<option value="All">All Days</option>
<option>1</option>
<option>2</option>
<option>3</option>
<option>4</option>
<option>5</option>
<option>6</option>
<option>7</option>
<option>8</option>
<option>9</option>
<option>10</option>
<option>11</option>
<option>12</option>
<option>13</option>
<option>14</option>
<option>15</option>
<option>16</option>
<option>17</option>
<option>18</option>
<option>19</option>
<option>20</option>
<option>21</option>
<option>22</option>
<option>23</option>
<option>24</option>
<option>25</option>
<option>26</option>
<option>27</option>
<option>28</option>
<option>29</option>
<option>30</option>
<option>31</option>
</select>
<select id="startSelectYear" name="startSelectYear" class="floatLeft">
<option value="All">All Years</option>
<option>2008</option>
<option>2009</option>
<option>2010</option>
<option>2011</option>
<option>2012</option>
<option>2013</option>
<option>2014</option>
<option>2015</option>
</select>
<br class="clearFloat" />
</fieldset>
<!-- END START DATE SELECT FIELDSET -->
<!-- START END DATE SELECT  FIELDSET -->
<fieldset id="cadUpdatesFields" class="width47 floatRight">
<legend>End Date <img src="../images/icon_help.png" width="16" height="16" class="cursorHand" onmouseover="Tip(tip_endDate)" onmouseout="UnTip()" /></legend>
<label class="floatLeft">Select An End Date:</label>
<select id="endSelectMonth" name="endSelectMonth" class="floatLeft">
<option selected value="All">All Months</option>
<option value="0">Jan</option>
<option value="1">Feb</option>
<option value="2">Mar</option>
<option value="3">Apr</option>
<option value="4">May</option>
<option value="5">Jun</option>
<option value="6">Jul</option>
<option value="7">Aug</option>
<option value="8">Sept</option>
<option value="9">Oct</option>
<option value="10">Nov</option>
<option value="11">Dec</option>
</select>
<select id="endSelectDay" name="endSelectDay" class="floatLeft">
<option value="All">Day</option>
<option>1</option>
<option>2</option>
<option>3</option>
<option>4</option>
<option>5</option>
<option>6</option>
<option>7</option>
<option>8</option>
<option>9</option>
<option>10</option>
<option>11</option>
<option>12</option>
<option>13</option>
<option>14</option>
<option>15</option>
<option>16</option>
<option>17</option>
<option>18</option>
<option>19</option>
<option>20</option>
<option>21</option>
<option>22</option>
<option>23</option>
<option>24</option>
<option>25</option>
<option>26</option>
<option>27</option>
<option>28</option>
<option>29</option>
<option>30</option>
<option>31</option>
</select>
<select id="endSelectYear" name="endSelectYear" class="floatLeft">
<option value="All">Year</option>
<option>2008</option>
<option>2009</option>
<option>2010</option>
<option>2011</option>
<option>2012</option>
<option>2013</option>
<option>2014</option>
<option>2015</option>
</select>
<!--  <div class="floatLeft marginL5px paddingT2px"><img src="../images/icon_calendar.png" width="16" height="16" class="cursorHand" /></div>-->
<br class="clearFloat" />
</fieldset>
<!-- END END DATE SELECT FIELDSET -->
<div class="clearFloat"></div>

<!-- START GENERATE REPORTS BUTTON -->
<div class="alignCenter marginTB5px">
<button type="reset" id="clearReportBtn" name="clearReportBtn"
class="btnRed cursorHand">Clear</button>
<button type="reset" id="generateReportBtn" name="generateReportBtn"
class="btnBlue cursorHand">Generate Report</button>
</div>
<!-- START GENERATE REPORTS BUTTON -->
</form>
<div id="reportFormsContainer" class="bgWhite">
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
</div>
<!-- END GENERATE REPORTS FORM -->
<!-- START REPORTS RESULTS FIELD -->
<fieldset id="reportResuts">
<legend>Report Results
<img src="../images/icon_help.png" width="16" height="16"
class="cursorHand" onmouseover="Tip(tip_reportResults)"
onmouseout="UnTip()" /></legend>
<!--  START REPORT INFO DIV -->
<div id="reportInfo">
<h3 id="reporTitle" class="floatLeft">Wayfinding System Report:
<span class="dateRange"><span id="report_start"></span>
<strong> to </strong><span id="report_end"></span></span></h3>
<!-- START REPORT DOWNLOAD LINK -->
<div class="floatRight" style="display:visible;">
<a href="javaScript:void(0)">
<img src="../images/icon_report.png" width="16" height="16"
class="cursorHand floatLeft" title="Download Report" /></a>
<p class="floatLeft marginL2px paddingT2px">
<a href="javaScript:void(0)" id="downloadReport" title="Download Report">Download Report</a></p>
<br class="clearFloat" />
</div>
<!-- END REPORT DOWNLOAD LINK -->
<br class="clearFloat" />
</div>
<!--  END REPORT INFO DIV -->
<!--  START REPORT DATA DIV -->
<div id="report_content" class="marginT5px">
<!-- START REPORT TABLE  -->
<!-- END REPORT TABLE  -->
</div>
<!--  END REPORT DATA DIV -->
</fieldset>
<!-- END REPORTS RESULTS FIELD -->
</div>
<!-- END REPORTS TAB CONTENT -->

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
