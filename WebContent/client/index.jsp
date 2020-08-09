<%@include file='index_code.inc'%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/xhtml1-loose.dtd">
<html>
<head>
<!-- START META TAGS -->
<meta name="author" content="Zetek Corporation"/>
<meta name="copyright" content="2009 Zetek Corporation (All Rights Reserved)" />
<meta name="robots" content="noindex" />
<meta name="robots" content="nofollow" />
<meta name="robots" content="noodp" />
<!-- END META TAGS -->
<!-- START STYLESHEETS -->
<link rel="stylesheet" type="text/css" href="../css/importer_client.css" media="screen" />
<!--[if IE]>
 <link rel="stylesheet" type="text/css" href="../css/client_IE.css" />
<![endif]-->
<!-- END STYLESHEETS -->
<!-- START JAVASCRIPT -->
<!-- JavaScript Libraries -->
<script type="text/javascript" src="../javascript/jquery-1.2.6.js"></script>
<script type="text/javascript" src="../javascript/jquery.contextmenu.simple.js"></script>
<script type="text/javascript" src="../javascript/autosuggest.js"></script>
<script type="text/javascript" src="../javascript/dojo/dojo/dojo.js" jConfig="parseOnLoad:true, isDebug:true"></script>
<script type="text/javascript" src="../javascript/floorSegment_object.js"></script>
<script type="text/javascript" src="../javascript/mousehold.js"></script>
<script type="text/javascript" src="../javascript/htmlFrags.js"></script>
<script type="text/javascript" src="../javascript/building_object.js"></script>
<script type="text/javascript" src="../javascript/dYBR_object.js"></script>
<script type="text/javascript" src="../javascript/map_object.js"></script>
<script type="text/javascript" src="../javascript/infoIcon_object.js"></script>
<script type="text/javascript" src="../javascript/floorInfo_object.js"></script>
<script type="text/javascript" src="../javascript/niftycube.js"></script>
<script type="text/javascript" src="../javascript/general_client.js"></script>
<script type="text/javascript" src="../javascript/jquery.popupwindow.js"></script>
<script type="text/javascript" src="../javascript/ui/ui.core.js"></script>
<script type="text/javascript" src="../javascript/ui/ui.slider.js"></script>
<script type="text/javascript" src="../javascript/ui/buildslider.js"></script>
<script type="text/javascript" src="../javascript/utilityscripts.js"></script>
<script type="text/javascript" src="../javascript/ui/ui.draggable.js"></script>
<!-- End Javascript Libraries for Slider -->
<script type="text/javascript">
<!-- LOOSE SCRIPTS HERE -->
dojo.require("dojox.gfx");
var manualAlarmPoll = <%=JsonServlet.noPoll%>;
<!-- END LOOSE JAVASCRIPT-->
</script>
<!-- END JAVASCRIPT -->
<title>Map Mode - Zetek's Wayfinding System</title>
</head>
<body onResize="wayMap.wayResize()" class="mapMode">
<!-- START OUTER CONTAINER -->
<div id="way_outerContainer">
<!-- START MASTHEAD CONTAINER -->
<%@include file='globalnav.inc'%>
<!-- END MASTHEAD CONTAINER -->
<!-- START CONTENT CONTAINER -->
<div id="way_contentContainer">
<!-- START BLDG INFO CONTAINER -->
<div id="way_bldgInfoContainer" class="pageColor">
<!-- START BLDG INFO TAB -->
<div id="way_bldgNameTab">
<!-- START TRIGGER TO SHOW/HIDE INFO PANEL -->
<div id="way_bldgInfoTrigger"
class="widthExpandCollapseIcon floatLeft marginT5px icon_expand cursorHand"
title="Show/Hide Building Info">
</div>
<!-- END TRIGGER TO SHOW/HIDE INFO PANEL -->
<h3 id="way_currentAddr" class="floatLeft marginL5px">BLDG Name & Address</h3>
<!-- START TRIGGER TO SHOW/HIDE CHANGE ADDRESS PANEL -->
<p id="way_chngAddrContainer" class="floatRight marginL5px">
<a href="javascript:void(0);" id="way_chngAddrTrigger" class="noLine"
title="Click To Change Bulding Location">Change</a></p>
<!-- END TRIGGER TO SHOW/HIDE CHANGE ADDRESS PANEL -->
<br id="way_chgAddrClear" class="clearFloat" />
<!-- START CHANGE ADDRESS PANEL -->
<div id="way_chngAddrPanel" style="display:none;">
<!-- START CHANGE ADDRESS FORM HEADER COPY -->
<h4 class="floatLeft">Change Building:</h4>
<div class="floatLeft marginL4px">
<img src="../images/icon_help.png" width="16" height="16" title="Help..."/>
</div>
<br class="clearFloat" />
<!-- END CHANGE ADDRESS FORM HEADER COPY -->
<!-- START CHANGE ADDRESS FORM CONTAINER -->
<div id="way_chngAddrFieldBox">
<!-- START CHANGE ADDRESS FORM -->
<form name="way_ChngAddrForm" id="way_ChngAddrForm">

<!-- START SEARCH OPTIONS MENU ON CONTAINER  -->
<div id="way_chngAddrFldWithOpts" class="width98 alignLeft"
style="display:visible;">
<!-- START CHANGE ADDRESS INPUT FIELD & SEARCH OPTS MENU -->
<input type="text" name="way_chngOptsOnSrchFld"
id="way_chngOptsOnSrchFld" class="width78" />
<select id="way_chngAddrSrchOptsMenu" name="way_chngAddrSrchOptsMenu"
class="width20">
<option value="ignore" selected>Find Building By...</option>
<option value="bldg_address">Street Address</option>
<option value="bldg_name">Building Name</option>
<option value="bldg_bin">Identification Number (BIN) </option>
</select>
<!-- END CHANGE ADDRESS INPUT FIELD & SEARCH OPTS MENU -->
</div>
<!-- END SEARCH OPTIONS MENU ON CONTAINER  -->

<!-- START CHANGE ADDRESS FORM BUTTON CONTAINER -->
<div id="way_chngAddrBtnBox">
<button type="reset" id="way_chngAddrCancelBtn" name="way_chngAddrCancelBtn"
class="btnRed cursorHand">Cancel</button>
<button type="submit" id="way_chngAddrFindBtn" name="way_chngAddrFindBtn"
class="btnBlue cursorHand">Find</button>
</div>
<!-- END CHANGE ADDRESS FORM BUTTON CONTAINER -->
</form>
<!-- END CHANGE ADDRESS FORM -->
</div>
<!-- END CHANGE ADDRESS FORM CONTAINER -->
</div>
<!-- END CHANGE ADDRESS PANEL -->
</div>
<!-- END BLDG INFO TAB -->
<!-- START ADDITIONAL BLDG INFO PANEL -->
<div id="way_bldgInfoPanel" style="display:none;">
<h4>Additional Building Information:</h4>
<div id="additionalBldgInfoContent">Content Here...</div>
</div>
<!-- END ADDITIONAL BLDG INFO PANEL -->
</div>
<!-- END BLDG INFO CONTAINER -->
<!-- START MAP INFO CONTAINER -->
<div id="way_mapInfoContainer" class="floatLeft bgWhite"> <!-- 34% -->

<!-- START ROUTES LIST CONTAINER  -->
<div id="way_routeList">
<div style="display:none;"></div>
</div>
<!-- END ROUTES LIST CONTAINER  -->

<!-- START ADD ROUTE -->
<div id="way_addRoute">
<div class="floatLeft">
<img src="../images/icon_add.png" width="16" height="16" alt="Add Route" title="Add Route" class="cursorHand" onclick="" />
</div>
<p class="floatLeft">
<a href="javascript:void(0)" id="way_addRouteTrigger" title="Add Route">
Add Route</a></p>
<br class="clearFloat" />
</div>
<!-- END ADD ROUTE -->
<!-- START FOOTER -->
<%@include file='footer.inc'%>
<!-- END FOOTER -->
</div>
<!-- END MAP INFO CONTENT CONTAINER -->

<!-- START MAP CONTAINER -->

<div id="way_mapContainer" class="floatLeft" style="background-color:#FFF">

<!-- START RIGHT CLICK MENU -->
<div class="contextMenu" id="mapRightClkMenu">
<ul class="alignLeft">
<li id="goHere">Add Location To Route</li>
<li id="infoHere">Get Location Information</li>
<li id="startHere">Start Route at this Location</li>
<li id="inaccessible">Make Location Inaccessible</li>
<li id="accessible">Make Location Accessible</li>
<li id="printMap" class="separator">Print Map</li>
</ul>
</div>
<!-- END RIGHT CLICK MENU -->

<div id="way_mapContent">
<div id="way_mapMenuTitle">
<h3  id="way_mapFloorName">Map Title....</h3>
</div>

<!-- START MAP MENU -->
<div id="way_mapMenu">
<ul id="mapDropDown">
<!-- START ICONS MENU -->
<li><a href="javascript:void(0)">Icons</a>
<ul>
<li><a href="javascript:void(0)" id="aFireAlarms">
<img id = "iFireAlarms" src="../images/icon_off.png" height="16" width="16" />
 Fire Alarms</a></li>
<li><a href="javascript:void(0)" id="aFirePhones">
<img id = "iFirePhones" src="../images/icon_off.png" height="16" width="16" />
 Fire Phones</label></a></li>
<li><a href="javascript:void(0)" id="aWaterFlows">
<img id = "iWaterFlows" src="../images/icon_off.png" height="16" width="16" />
 Water Flows</label></a></li>
<li><a href="javascript:void(0)" id="aPullStations">
<img id = "iPullStations" src="../images/icon_off.png" height="16" width="16" />
 Pull Stations</label></a></li>
<li><a href="javascript:void(0)" id="aAllInfoIcons">
<img id = "iAllInfoIcons" src="../images/icon_off.png" height="16" width="16" />
 Show All Icons</label></a></li>
</ul>
</li>
<!-- END ICONS MENU -->
<!-- START TOOLS MENU -->
<li><a href="javascript:void(0)">Tools</a>
<ul>
<li>
<a href="javascript:void(0)" id="fetchAlarm">Fetch Route</a>
</li>
<li>
<a href="javascript:void(0)" id="javaPrintRoute">Java Print Route</a>
</li>
</ul>
</li>
<!-- END TOOLS MENU -->
<!-- START PRINT MENU -->
<li><a href="javascript:void(0)" id="printRoute">Print Route</a></li>
<!-- START PRINT MENU -->
</ul>
</div>
<!-- END MAP MENU -->

<div id="way_mapPanCtrl">
<div id="way_panUp" class="cursorHand" title="Pan Up">
<img src="../images/icon_panup.png" width="24" height="24" />
</div>
<div id="way_panLeft" class="cursorHand floatLeft" title="Pan Left">
<img src="../images/icon_panleft.png" width="24" height="24" />
</div>
<div id="way_centerMap" class="cursorHand floatLeft" title="Center Map">
<img src="../images/icon_centermap.png" width="24" height="24" />
</div>
<div id="way_panRight" class="cursorHand floatLeft" title="Pan Right">
<img src="../images/icon_panright.png" width="24" height="24" />
</div>
<div class="clearFloat"></div>
<div id="way_panDown" class="cursorHand" title="Pan Down">
<img src="../images/icon_pandown.png" width="24" height="24" />
</div>
</div>
<div style="z-index:100" class="posRelative">
<div id="way_mapZoomCtrl">

<div id="way_zoomIn" class="cursorHand" title="Zoom In">
<img src="../images/icon_zoomin.png" width="20" height="21" />
</div>

<div id="way_slider" class="ui-slider-2" style="margin-top:-7px;">

<div class="ui-slider-handle"></div>
</div>

<div id="way_zoomOut" class="cursorHand" title="Zoom Out">
<img src="../images/icon_zoomout.png" width="20" height="21" />
</div>

</div>
</div>

<div id="way_mapTiles" class="dragmap" style="position:absolute; top:0; left:0; z-index:1"></div>

</div>
</div>
<div class="clearFloat"></div>
<!-- END MAP CONTAINER -->
</div>
<!-- END CONTENT CONTAINER -->
</div>

<!-- END OUTER CONTAINER -->
<%@include file='alert.inc'%>
</body>
</html>
<!-- EOF -->
