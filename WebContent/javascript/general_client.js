<!-- START PRINT FUNCTION --> 
function printMap() {
	 window.focus(); 
	 window.print();
 }
<!-- END PRINT FUNCTION --> 

<!-- START IFRAME CREATION FUNCTION --> 
//This function generates and iframe and then populates with the url passed in the the function call
function createPrintFrame(url){
	var el = document.createElement("iframe");
	el.setAttribute('id', 'printFrame');
	el.setAttribute('name', 'printFrame');
	el.setAttribute('frameborder', '0');
	el.setAttribute('height', '0');
	el.setAttribute('width', '0');
	document.body.appendChild(el);
	el.setAttribute('src', url);
}
<!-- END IFRAME CREATION FUNCTION --> 

<!-- ADDS ROUNDED CORNERS TO BLDGS NAME TAB -->
window.onload=function() {
   Nifty("div#way_bldgNameTab","big tl tr");
   Nifty("div#way_globalNavMenu","bottom, big");
};

<!-- DOCUMENT READY -->
$(document).ready(function () {
// IE BROWSER DETECT FIX                                                                                                                                                             
jQuery.browser.version = jQuery.browser.msie && parseInt(jQuery.browser.version) == 6 && window["XMLHttpRequest"] ? "7.0" : jQuery.browser.version;

//      $().mousedown(function(e) {
//  	window.status = "x:"+e.pageX +" y:"+ e.pageY;
//        }); 

	<!-- START MAP MENU CODE-->
	  var timeout    = 500;
	  var closetimer = 0;
	  var ddmenuitem = 0;

	  function dropDown_open()
	  {  dropDown_canceltimer();
	     dropDown_close();
	     ddmenuitem = $(this).find('ul').css('visibility', 'visible');}

	  function dropDown_close()
	  {  if(ddmenuitem) ddmenuitem.css('visibility', 'hidden');}

	  function dropDown_timer()
	  {  closetimer = window.setTimeout(dropDown_close, timeout);}

	  function dropDown_canceltimer()
	  {  if(closetimer)
	     {  window.clearTimeout(closetimer);
	        closetimer = null;}}

	  $(document).ready(function()
	  {  $('#mapDropDown > li').bind('mouseover', dropDown_open)
	     $('#mapDropDown > li').bind('mouseout',  dropDown_timer)});

	  //document.onclick = dropDown_close;

    <!-- END MAP MENU CODE -->

	<!-- START RIGHT CLICK MENU CODE  -->
	$('#way_mapTiles').contextMenu('mapRightClkMenu', {
		menuStyle: {listStyle: 'none',padding:'0',margin:'0',backgroundColor:'#f0f0f0',border:'1px solid #bbb',borderTop:'none',width:'180px'},
		itemStyle: {margin:'0',color:'#000',display:'block',cursor:'pointer',padding:'3px 3px 3px 5px',borderTop:'solid 1px #bbb',backgroundColor:'transparent'},
	      itemHoverStyle: {color:'#fff',backgroundColor:'#0088cc'},
	      eventPosX: 'pageX',
	      eventPosY: 'pageY',
	      bindings: {
		'goHere':       Building.acForGoHere,
		'infoHere':     Building.acForInfoHere,
		'startHere':    Building.acForStartHere,
		'inaccessible': Building.acForInacc,
		'accessible':   Building.acForAcc,
		'printMap':     Building.printRoute
		},
	    onShowMenu: Building.onShowMenu
	  });

    <!-- END RIGHT CLICK MENU CODE  -->

    // Conflicts with popup link.  Have to connect later.
    $("a#javaPrintRoute").click(Building.javaPrintRoute);
    $("a#printRoute").click(Building.printRoute);
    $("a#fetchAlarm").click(Building.getAlarmRoute);
    //var pt = UIU.getElementOrWhinge("fetchAlarm");
    //pt.onclick = Building.getAlarmRoute;

    /* The current building and map go into the global name space */
    currentBuilding = new Building();

    /* Set the div containing the map to be mouse draggable */
    $(".dragmap").draggable();

    wayMap = new WayMap("NYST", "Axoimg");

    wayMap.initSlider("#way_slider");

    /* Set up the events for the map controls */
    wayMap.connectZoomInEvent("div#way_zoomIn");
    wayMap.connectZoomOutEvent("div#way_zoomOut");
    wayMap.connectPanLeftEvent("div#way_panLeft");
    $("div#way_panLeft").mousehold(200, WayMap.panhold);
    wayMap.connectPanRightEvent("div#way_panRight");
    $("div#way_panRight").mousehold(200, WayMap.panhold);
    wayMap.connectPanUpEvent("div#way_panUp");
    $("div#way_panUp").mousehold(200, WayMap.panhold);
    wayMap.connectPanDownEvent("div#way_panDown");
    $("div#way_panDown").mousehold(200, WayMap.panhold);
    wayMap.connectCenterMapEvent("div#way_centerMap");

    $("#aFireAlarms").click(wayMap.toggleFireAlarms);
    $("#aFirePhones").click(wayMap.toggleFirePhones);
    $("#aWaterFlows").click(wayMap.toggleWaterFlows);
    $("#aPullStations").click(wayMap.togglePullStations);
    $("#aAllInfoIcons").click(wayMap.toggleShowAllIcons);

    wayMap.wayResize();
    $(wayMap.mapDiv).empty();

    // For debugging - get default building
    currentBuilding.getDefaultBuilding();

    Building.addRoute();
    if (!manualAlarmPoll) {	// Suppress polling flag set in .jsp file
      currentBuilding.alarmTimer = setTimeout("Building.getAlarmRoute()",15000);
    }

    //wayMap.createTileTable();
    //wayMap.setMapTiles("route1_Dst1", "EP-03");

/*
    rtSeg = new FloorSegment(undefined);
    rtSeg.getTestData();
    var route;
    currentBuilding.routes[currentBuilding.routes.length] =route= new Route(1);
    var dest;
    route.destinations[route.destinations.length] =
      dest = new Destination(1, 1);
    dest.floorSegment = rtSeg;
*/

    <!-- SHOWS/HIDES BLDG INFO PANE & SWAPS EXPAND/COLLAPSE ICON -->
    $("#way_bldgInfoTrigger").click(function() {
	var $this = $(this);
	if ($this.is('.icon_collapse')){
	  $this.removeClass('icon_collapse');$this.addClass('icon_expand');
	} else if ($this.is('.icon_expand')){
	  $this.removeClass('icon_expand');$this.addClass('icon_collapse');
	};
	$("#way_bldgInfoPanel").slideToggle("fast");
      });

    <!-- SHOWS LOCATION SEARCH. HIDES CURRENT LOCATION INFO -->
    $("a#way_chngAddrTrigger").click(function() {
	$("#way_bldgInfoTrigger").hide();$("#way_currentAddr").hide();$("#way_chngAddrContainer").hide();$("#way_chgAddrClear").hide();$("#way_bldgInfoPanel").hide();$("#way_chngAddrPanel").show();
      });

    $("button#way_chngAddrCancelBtn").click(hideResetSearch);

    <!-- CLEARS CHANGE LOCATION FORM WHEN THE CANCEL BUTTON IS CLICKED -->
    $ ("button#way_chngAddrCancelBtn").click(function() {
	$('#way_ChngAddrForm:input').val("");
      });

   <!-- CLEARS CHANGE LOCATION FORM WHEN THE FIND BUTTON IS CLICKED -->
   $ ("button#way_chngAddrFindBtn").click(currentBuilding.getDefaultBuilding);

   <!-- SHOWS CHANGE ADDRESS SEARCH OPTIONS -->
   $ ("a#way_chngAddrShowSrchOpts").click(function() {
       $('#way_chngAddrFldNoOpts').hide();$('#way_chngAddrFldWithOpts').show();
   });
   <!-- HIDES CHANGE ADDRESS SEARCH OPTIONS -->
   $ ("a#way_chngAddrHideSrchOpts").click(function() {
       $('#way_chngAddrFldNoOpts').show();$('#way_chngAddrFldWithOpts').hide();
   });
   <!-- ADD ROUTE trigger -->
  $("a#way_addRouteTrigger").click(Building.addRoute);

  });
