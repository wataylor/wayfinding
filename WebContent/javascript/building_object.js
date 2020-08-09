/** object which stores building information and the current route and
 * destination list for map manipulation purposes.  Admin defines an
 * equivalent edifice class.  Note that the building class has some
 * class variables which define building behavior. */

 /** This file also defines some methods which are called from the
  * general_client file.  This file must be loaded before
  * general_client.*/

 /** This method is called from the cancel button trigger as defined
  * in general_client.*/
function hideResetSearch() {
  var $bldgInfoTrigger = $('#way_bldgInfoTrigger');
  if ($bldgInfoTrigger.is('.icon_collapse')){
    $bldgInfoTrigger.removeClass('icon_collapse');
    $bldgInfoTrigger.addClass('icon_expand');
  };
  $("#way_bldgInfoTrigger").show();
  $("#way_currentAddr").show();
  $("#way_chngAddrContainer").show();
  $("#way_chgAddrClear").show();
  $("#way_chngAddrPanel").hide();
 }

 /** The standard building properties are listed here and set to
  * undefined.  That means that the properties exist so that their
  * names can be found by examining this file, but if
  * (building.property) returns false until they are assigned explicit
  * values.  There is only one of these objects named
  * "currentBuilding"; it is instantiated when general_client is
  * read.*/
function Building () {
  /* This count is incremented every time a route is added to a
   * building.  It is not decremented when a route is deleted so that
   * route numbers will stay unique.  The first route in a bulding is
   * route 1.*/
  this.routeCount = 1;
  /* JSON object returned from the server when the building is found
   * in the database.  Note that most of the properties have the same
   * names as the columns in the SQL table.  Properties are subject to
   * change as the database table changes.  Note: A JSON object is a
   * text representation of a Java Script object.  By the time the
   * variable value is set, the text has been converted into a Java
   * Script object.*/
  this.buildingJson = undefined;
  /* Ordered array of routes which the user has selected or is
   * constructing.  These are displayed in an array of route divs
   * which are written into the DOM.*/
  this.routes = new Array();
  /* Map floor drawing names to correct floor names*/
  this.floorMap = undefined;
  /* Identifies the most recently selected destination in a route*/
  this.selectedDest = undefined;
  /* Stores the timer which polls for new alarms*/
  this.alarmTimer = undefined;
  /* Stores the most recent right-click menu event*/
  this.rightMenuEvent = undefined;
 }

/* Determines whether or not to close the default search box.*/
Building.closeDefault = true;
/* Determines whether or not to ask for a destination when a route is
 * creaated.*/
Building.addDestination = true;
/* Determines whether or not to open a new destination box as it is
 * added.*/
Building.openDestination = true;

/** List of building attributes which are returned as part of the JSON
 * object.  these names are also the IDs of the .html elements which
 * are to receive the street names for display to the user.*/
Building.prototype.streetList = ["topStreet", "leftStreet", "rightStreet", "bottomStreet"];

/* This method passes the search parameters back to the server but is
 * guaranteed to return one and only one building regardless of how
 * many buildings match the search criteria.  This is intended for
 * single-building systems, but it returns a default building for
 * multi-building systems.*/
Building.prototype.getDefaultBuilding = function() {
  var i;
  $.getJSON('../json.jsp?verb=1',
	    $('#way_ChngAddrForm').serialize(),
	    function(data) {
	      currentBuilding.buildingJson = data;
	      if (data['msg'])  {
		alert(data['msg']);
	      } else {
		var bldg = data[0];
		currentBuilding.floorMap = data[1];
		if (bldg['name']) {
		  $('#way_currentAddr').text(bldg['name']);
		}
		for (i=0; i<currentBuilding.streetList.length; i++) {
		  $street = currentBuilding.streetList[i];
		  if (bldg[$street]) {
		    $('#'+$street).text(bldg[$street]);
		  }
		}
	      }
	      hideResetSearch();
	    });
  return false;		// Suppress the normal submit
};

Building.prototype.dwgToFloor = function(dwg) {
  if (!this.floorMap) { return dwg; }
  var fl = this.floorMap[dwg];
  if (!fl) { return dwg; }
  return fl;
};

/* Find the route object in this building which matches the name of
 * the div which displays the route in the DOM.*/
Building.prototype.getRouteObject = function(rIDString) {
  var i;
  var rt;
  for (i=0; i<this.routes.length; i++) {
    if (rt = this.routes[i]) {
      if (rt.divID == rIDString) {
	return rt;
      }
    }
  }
  return undefined;
};

/* This is a method of the Building class*/
Building.prototype.getBuildingID = function() {
  if (this.buildingJson.buildingID) { return this.buildingJson.buildingID; }
  return undefined;
};

/* This function is called whenever a user selects anything from the
 * menu which appears after right-clicking on the map.  The element
 * parameter is not the dom element; it is the object rerieved by
 * jQuery.  For reasons obscure, tiles.style.top is not the same as
 * el.offset().top.  x relative to the element seems to be computed
 * using offset() before this function is called.  It is not clear why
 * rx and ry work out as they do, but they are correct.  */
Building.rightClickMenu = function(action, el, pos) {
  var tiles = UIU.getElementOrWhinge("way_mapTiles");

  var offset = $(tiles).offset();
  var rOx = (pos.x - offset.left)/wayMap.scale;
  var rOy = (pos.y - offset.top) /wayMap.scale;

/*
  var rx  = (pos.x - parseInt(tiles.style.left))/wayMap.scale;
  var ry  = (pos.y - parseInt(tiles.style.top) )/wayMap.scale;

  var rDx = (pos.x - tiles.offsetLeft)/wayMap.scale;
  var rDy = (pos.y - tiles.offsetTop) /wayMap.scale;

  alert(
    'Action: ' + action + '\n' +
    'X: ' + pos.x       + ' Y: ' + pos.y    + ' x, y from page\n' +
    "X: " + tiles.style.left + " Y: " + tiles.style.top + ' tiles style\n' +
    "X: " + offset.left + " Y: " + offset.top + ' tiles offset object\n' +
    "X: " + tiles.offsetLeft + " Y: " + tiles.offsetTop + ' tiles.offset\n' +
    // 'X: ' + pos.docX    + ' Y: ' + pos.docY + ' (relative to document)\n'+
    "X: " + rDx         + " Y: " + rDy + " x, y - tiles.offset\n" +
    "X: " + rx          + " Y: " + ry  + " x, y - tiles.style\n" +
    "X: " + rOx         + " Y: " + rOy + " x, y - tiles offset object\n" +
    "Scale: " + wayMap.scale
    );
*/

  currentBuilding.rightMenuEvent = undefined;
  var why   = action;

  var param = "&x=" + rOx + "&y=" + rOy + "&FDWG=" + wayMap.fio.floorDWG +
  "&why=" + action;
  $.getJSON('../route.jsp?verb=2' + param, // set the state of the space
	    function(data) {
	      if (data['msg'])  {
		alert(data['msg']);
	      } else if (data['scs']) {
		alert(data['scs']);
		wayMap.clearFloorImpedimenta();
		wayMap.getFloorImpedimenta(true);
	      } else if (data['dst']) {
		var dest, route, rID;
		//Route.cancelAddDest();
		if (dest = currentBuilding.selectedDest) {
		  /* It appears thata there is a timing situation
		   * where right click add does not always work.  This
		   * changes how the code works which might work
		   * better - less of a race condition.*/
		  // dest   = Destination.getDestInRoute(dest)[0];
		  // route  = dest.routeObj;
		  rID   = Route.getRouteIDString(dest);
		  route = currentBuilding.getRouteObject(rID);
		  UIU.hideShow(route.divID, "AddDestFormBox", "AddDestination");
		  if (why == "go") {
		    dest   = route.destinations[route.destinations.length-1];
		    Destination.destAfterDest(dest.divID, data.dst, false);
		  } else {
		    dest   = route.destinations[0];
		    dest.serverSubRoute = undefined;
		    dest.destJson       = {divID:dest.divID};
		    var choice      = {ilk:0, sug:data.dst};
		    Destination.takeSuggestion(dest, choice, true);
		  }
		}
	      } else if (data['over']) { // Overview is non-null
		InfoIcon.showBubble(null, data);
	      } else {
		console.log(data); // TODO take out
	      }
	    });
};

Building.translateRight = function(id) {
  Building.rightClickMenu(id, null, {x: currentBuilding.rightMenuEvent.pageX,
	                             y: currentBuilding.rightMenuEvent.pageY});
};

Building.acForInacc = function(elem) {
  Building.translateRight("inaccess");
};

Building.acForAcc = function(elem) {
  Building.translateRight("access");
};

Building.acForGoHere = function(elem) {
  Building.translateRight("go");
};

Building.acForStartHere = function(elem) {
  Building.translateRight("st");
};

Building.acForInfoHere = function(elem) {
  Building.translateRight("info");
};

/* Called before the right-click menu is popped up.  TODO make it
 * check whether the space is accessible or not and condition the menu
 * accordingly.  It would be better to see if it is possible to find if
 * a point is inside a shape known on the client, but may have to go
 * back to the server. */
Building.onShowMenu = function(event, menu) {
  currentBuilding.rightMenuEvent = event;
  return menu;
};

/* This callback function is called whenever the server sends a new
 * route to the client.  Defining the callback function outside the
 * context of the server request means that NO parameter data are
 * available to the callback function. */
Building.addRouteCallback = function(data) {
  if (UIU.goodData(data)) {
    var rteno = currentBuilding.routeCount;
    var elem = UIU.getElementOrWhinge("way_routeList");
    //debugger;
    //$(elem).append(data); // Works in Firefox but not in IE
    //$("div#way_routeList").append(data); // Does not work in ID
    elem.innerHTML += data;
    $("div#route" + rteno + "_ToggleTrigger").click(Route.showHideRoute);
    $(  "a#route" + rteno + "_AddDestTrigger").click(Route.addDestination);
    $(   "#route" + rteno + "_Delete").click(Route.deleteIE);
    $(   "#route" + rteno + "_chngHelp").click(Destination.addHelp);
    $(   "#route" + rteno + "_AddDestCancelBtn").click(Route.cancelAddDest);
    $(   "#route" + rteno + "_AddDestFindBtn").click(Route.findAddDestination);
    currentBuilding.routes[currentBuilding.routes.length] =
    new Route(rteno);
    currentBuilding.routeCount++;
    // Add start destination, can add another
    Route.addDestinationByID("route" + rteno, true,
			     Building.addDestination);
  }
};

/* This is a static method of the Building class.  The purpose of
 * defining static methods is to minimize damage to the name space.
 * Adding a route adds triggers, etc., to the DOM.*/
Building.addRoute = function () {
  // console.log("Add Route " + this.id);
  $.get('../html.jsp?verb=1', {'rteno': currentBuilding.routeCount },
	Building.addRouteCallback);
};

/** Ger rid of all routes in preparation for recomputing them.  This
 * occurs on any change in accessibilty.*/
Building.unRouteRoutes = function() {
  var i;
  var j;
  var rt;
  var dest;
  for (i=0; i<currentBuilding.routes.length; i++) {
    if (rt = currentBuilding.routes[i]) {
      for (j=0; j<rt.destinations.length; j++) {
	if (dest = rt.destinations[j]) {
	  dest.serverSubRoute = undefined;
	}
      }
    }
  }
};

/* This function goes to the server to see if there are any alarms
 * which have not yet been displayed on this particular client.*/
Building.getAlarmRoute = function() {
  //console.log("Get alarm route " + this.id);
  if (currentBuilding.alarmTimer) {
    clearTimeout(currentBuilding.alarmTimer);
  }
  $.get('../alarm.jsp?verb=1', {'rteno': currentBuilding.routeCount },
	function(data) {
	  if (UIU.goodData(data)) {
	    if (data.indexOf("NON") === 0) {
	    } else if (data.indexOf("RER") === 0) {
	      Building.unRouteRoutes();
	      if (currentBuilding.selectedDest) {
		Building.selectADestID(currentBuilding.selectedDest, true);
	      }
	    } else {
	      Building.addRouteCallback(data);
	    }
	  }
	});
  if (!manualAlarmPoll) {
    currentBuilding.alarmTimer = setTimeout("Building.getAlarmRoute()",15000);
  }
};

/* lastIndexOf may simplify this one.*/
Building.topElementID = function(id) {
  var ix = id.lastIndexOf("_");
  if (ix > 0) { return id.substring(0, ix+1); }
  return Destination.getDestIDString(id);
};

/* Un-select all floors in all routes currently defined for the
 * entire building.*/
Building.unselectFloors = function() {
  var i;
  var j;
  var rt;
  var fio;
  var elem;
  for (i=0; i<currentBuilding.routes.length; i++) {
    if (rt = currentBuilding.routes[i]) {
      for (j=0; j<rt.floorInfoObjects.length; j++) {
	if (fio = rt.floorInfoObjects[j]) {
	  fio.dim();
	}
      }
    }
  }
};

/* Action routine to select the map associated with a destination or
 * an inserted vertical destination for viewing.  This is defined at
 * the building level because it operates on all of the routes. */
Building.selectADest = function() {
  Building.selectADestID(this.id, false);
};

Building.selectADestID = function(id, beautify) {
  currentBuilding.selectedDest = elID = id;
  //console.log("Select " + this.id + " " + elID);
  var elem = UIU.getElementOrWhinge(Building.topElementID(id));

  if (elem) {
    // console.log(elem.route.divID + " " + elem.whichFloor + " " + elem.inFloor);
  } else {
    return;
  }

  Building.unselectFloors();

  var route = Route.findRoute(elID);
  var objA  = route.floorInfoObjects;
  var fio   = objA[elem.whichFloor-1];
  fio.select();
  /* See comments in FloorInfoObject dim and select*/
  $("#" + this.id + " [id$='name']").css("font-weight", "bold");
  var belem = this.boldDiv;
  if (belem) {
    belem = UIU.getElementOrWhinge(belem + "name");
    // console.log(this.boldDiv + " " + belem.id);
    // $(belem.id).css("font-weight", "bold");
    belem.style.fontWeight = "bold";
  }
  if (elem.inFloor >= fio.pathIcons.length) {
    elem.inFloor = fio.pathIcons.length-1;
  }
  fio.selectedIcon = elem.inFloor;

  if (beautify) {
    route.beautify();		// Eventually calls set map tiles
  } else {
    wayMap.setMapTiles(fio);
  }
};

Building.printRoute = function() {
  Building.printRouteDo(false);
};
Building.javaPrintRoute = function() {
  Building.printRouteDo(true);
};

Building.printRouteDo = function(javaPrint) {

  if (!currentBuilding.selectedDest) {
    alert("Please select a destination in the route to be printed");
    return;
  }
  var route = Route.findRoute(currentBuilding.selectedDest);
  if (route.destinations.length < 2) {
    alert("Please select a route with at least one destination");
    return;
  }
  var dest, i, j=0, params = javaPrint ? "&jP=y" : "";
  for (i in route.destinations) {
    dest = route.destinations[i];
    if (dest.destJson) {
      params += "&" + jQuery.param(dest.destJson);
      j++;
    }
  }
  if (j < 2) {
    alert("Please select a route with at least one destination");
    return;
  }
  $.get('../html.jsp?why=0&verb=6' + params,
	function(data) {
	  if (UIU.goodData(data)) {
	    //window.open(data);
	    // The .html has onLoad code to force the print to occur
	    createPrintFrame(data);
	    // *******
	  }
	});
};

/* The Route class defines a number of static methods which help
 * manipulate routes.  The purpose of defining static methods is to
 * minimize damage to the name space.  */
function Route (number) {
  /* This count is incremented every time a destination is added to a
   * route.  It is not decremented when a destination is deleted so
   * that destination numbers will stay unique.*/
  this.destCount = 0;
  /* Variable which holds the array of destinations along this route.*/
  this.destinations = new Array();
  /* Destinations are separated into floors for display purposes.
   * Floor objects keep track of what is to be displayed on each
   * floor.*/
  this.floorInfoObjects = new Array();
  /* Remember the route number for later*/
  this.rteNo = number;
  /* The ID of the div that holds the array of destinations in the DOM */
  this.divID = "route" + number + "_";
};

/* This method is called whenever anything changes in the destination
 * array.*/
Route.prototype.beautify = function() {
  var i, j, dest, destN, divID, canID, floorDWG, floorElem, floorElemID, selDID,
  floorNo, inFloor, firstDID, fInFl, fio, pathIcon, len, vno, thno, subRoute;
  // console.log("Beautify " + this.divID);

  if (this.destinations.length <= 0) { return; }

  len = this.destinations.length;
  for (i = 0; i < len; i++) {
    dest = this.destinations[i];
    dest.setNo(i);
  }

  /* Walk the destinations and put paths in sub-routes between
   * destinations where they are needed.  Each destination has a
   * destJson which describes it in terms that the routing method can
   * handle and a serverSubRoute which tells how to get to the next
   * destination.  If a destination has a destJson but not a
   * serverSubRoute and the following destination has a destJson, it
   * is necessary to compute the path from the destination to the next
   * one.  */
  len = this.destinations.length -1;
  for (i = 0; i < len; i++) {
    dest = this.destinations[i];
    if (dest.serverSubRoute) { continue; } // Had a route
    if (dest.destJson) {		   // Has a destination but no sub route
      if ( (destN = this.destinations[i+1]) && destN.destJson) {
	Destination.getRouteBetween(this, dest, destN);
	return;			// Bail out, getRouteBetween calls beautify
      }
    }
  }

  // Clean out all floor divs
  var elem = UIU.getElementOrWhinge("route" + this.rteNo + "_Content");
  $("div#route" + this.rteNo + "_Content").empty();
  // Get rid of all floor info objects
  this.floorInfoObjects = new Array();

  /* Find the first valid floor, if there isn't one, that, too, is OK.*/
  floorDWG = null;
  floorNo  = 1;
  inFloor  = 0;
  for (i in this.destinations) {
    dest = this.destinations[i];
    if (dest.destJson) {
      floorDWG = dest.destJson.FDWG;
      break;
    }
  }

  firstDID = undefined;
  selDID   = undefined;

  /* These lines of code must be repeated whenever a subRoute goes to
   * a different floor.  */
  floorElemID  = Frag.floorBox(this, floorNo, elem);
  floorElem    = UIU.getElementOrWhinge(floorElemID+ "content");
  fio          = new FloorInfo(floorDWG, floorElemID);
  this.floorInfoObjects[this.floorInfoObjects.length] = fio;
  vno          = 1;
  thno         = 1;
/*
  if (subRoute = this.destinations[0].serverSubRoute) {
    j = "Leaving " + subRoute[0].st + " for " + subRoute[0].en;
  } else {
    j = "";
  }

  UIU.assignHTML(floorElemID + "description", j);
*/
  UIU.assignHTML(floorElemID + "description",
		 currentBuilding.dwgToFloor(floorDWG));
  UIU.setElementAttr(floorElemID, "route",      this);
  UIU.setElementAttr(floorElemID, "whichFloor", floorNo);
  UIU.setElementAttr(floorElemID, "inFloor",    0);
  UIU.assignClick(floorElemID, ["description"], Building.selectADest);

  for (i in this.destinations) {
    dest = this.destinations[i];
    if (!dest.destJson) { continue; } // Skip empties

    if (dest.dstNo === 0) {
      divID = Frag.startPoint(this, dest.dstNo, floorElem,
			      Destination.showDestChangeForm,
			      Destination.deleteIE, Building.selectADest);
    } else {
      divID  = Frag.destPoint(this, dest.dstNo, floorElem,
			      Destination.showDestChangeForm,
			      Destination.deleteIE, Building.selectADest,
	                      Destination.exitFromHere);
    }
    if (!selDID) {
      selDID = divID;
    }
    if (!firstDID) {
      firstDID = divID;
      UIU.setElementAttr(floorElemID + "description", "boldDiv", firstDID);
    }
/*
    canID = Frag.changeForm(this, dest.dstNo, floorElem, Destination.help,
			    Destination.hideDestChangeForm,
			    Destination.findInDestChangeForm);
*/

    /* Assign a floor order for later use in selecting it.*/
    UIU.setElementAttr(divID, "route",      this);
    UIU.setElementAttr(divID, "whichFloor", floorNo);
    UIU.setElementAttr(divID, "inFloor",    inFloor);
    inFloor++;			// Lint does not like this in the method call

    if (dest.destJson) {
//      Destination.hideDestChangeForm.call(UIU.getElementOrWhinge(canID + "ChngCancelBtn"));
      UIU.assignHTML(divID + "name", dest.destJson.desc);
      UIU.assignHTML(floorElemID + "description",
		     "Path to " + dest.destJson.desc);

      if (dest.destJson.about) {
	UIU.visibility(divID + "info", true);
	UIU.assignHTML(divID + "infoAbout", dest.destJson.about);
      } else {
	UIU.visibility(divID + "info", false);
	UIU.assignHTML(divID + "infoAbout", "");
      }
    } else {
      UIU.assignHTML(divID + "name", "New destination");
      UIU.visibility(divID + "info", false);
      UIU.assignHTML(divID + "infoAbout", "");
    }
    pathIcon = dest.makePathIcon(null);
    fio.pathIcons[fio.pathIcons.length] = pathIcon;
    if (dest.serverSubRoute) {
      fio.floorSegments[fio.floorSegments.length] =
	new FloorSegment(dest.serverSubRoute[0]);
    }

/* else {
      Destination.showDestChangeForm.call(UIU.getElementOrWhinge(divID + "changeTrigger"));
      } */

    /* Pop in any required verticals.  If the sub route array is
     * longer than 1, a vertical is needed for each floor segment
     * beyond segment 0.  The sequence is:
     * Insert a vertical to get off of the current floor.
     * Start a new floor.
     * Insert a vertical to get onto the next floor*/
    if (dest.serverSubRoute) {
      for (j=1; j<dest.serverSubRoute.length; j++) {
	divID = Frag.vertPoint(this, floorNo, vno, floorElem,
			       Building.selectADest);
	/* Assign a floor order for later use in selecting it.*/
	UIU.setElementAttr(divID, "route",      this);
	UIU.setElementAttr(divID, "whichFloor", floorNo);
	UIU.setElementAttr(divID, "inFloor",    inFloor);
	UIU.assignHTML(divID + "name", dest.serverSubRoute[j-1].en);
	UIU.assignHTML(floorElemID + "description",
		       "Path to " + dest.serverSubRoute[j-1].en);
	UIU.assignHTML(divID + "list", dest.serverSubRoute[j-1].fc.ins);
	pathIcon = dest.makePathIcon(vno);
	pathIcon.name = dest.serverSubRoute[j-1].en;
	var wp = dest.serverSubRoute[j-1].wp;
	wp = wp[wp.length-1];
	pathIcon.x = wp.x;
	pathIcon.y = wp.y;
	fio.pathIcons[fio.pathIcons.length] = pathIcon;

	divID = Frag.mapThumb(this, floorNo, vno, floorElem,
			      Building.selectADest);
	/* Assign a floor order for later use in selecting it.*/
	UIU.setElementAttr(divID, "route",      this);
	UIU.setElementAttr(divID, "whichFloor", floorNo);
	UIU.setElementAttr(divID, "inFloor",    inFloor);
	UIU.setElementAttr(divID, "boldDiv",    firstDID);
	UIU.assignSrc(divID + "thumbImg", "../tiles/" +
		      wayMap.buildingName + '/' + fio.floorDWG + '/' +
		      wayMap.mapType.substring(0,3) + fio.floorDWG +
		      'thumb.png');

	inFloor++;
	vno++;

	/* That completes the current floor */

	/* These lines of code must be repeated whenever a subRoute goes to
	 * a different floor.  */
	floorNo++;
	inFloor = 0;
	floorElemID = Frag.floorBox(this, floorNo, elem);
	floorElem   = UIU.getElementOrWhinge(floorElemID+ "content");
	fio         = new FloorInfo(dest.serverSubRoute[j].o, floorElemID);
	fio.dim();		// It is not selected
	this.floorInfoObjects[this.floorInfoObjects.length] = fio;
	//vno         = 1;	// Verticals accumulate over all dests

	/*
	UIU.assignHTML(floorElemID + "description",
		       "Leaving " + dest.serverSubRoute[j].st +
		       " for " + dest.serverSubRoute[j].en);
	*/
	UIU.assignHTML(floorElemID + "description",
		       currentBuilding.dwgToFloor(dest.serverSubRoute[j].o));
	UIU.setElementAttr(floorElemID, "route",      this);
	UIU.setElementAttr(floorElemID, "whichFloor", floorNo);
	UIU.setElementAttr(floorElemID, "inFloor",    0);
	UIU.assignClick(floorElemID, ["description"], Building.selectADest);

	fio.floorSegments[fio.floorSegments.length] =
	  new FloorSegment(dest.serverSubRoute[j]);

	divID = Frag.vertPoint(this, floorNo, vno, floorElem,
			       Building.selectADest);
	if (!selDID) {
	  selDID = divID;
	}
	firstDID = divID;

	UIU.setElementAttr(floorElemID + "description", "boldDiv", divID);

	UIU.setElementAttr(divID, "route",      this);
	UIU.setElementAttr(divID, "whichFloor", floorNo);
	UIU.setElementAttr(divID, "inFloor",    inFloor);
	inFloor++;	 // Lint does not like this in the method call
	UIU.assignHTML(divID + "eox", "Exit: ");
	UIU.assignHTML(divID + "name", dest.serverSubRoute[j].st);
	UIU.assignHTML(floorElemID + "description",
		     "Path to " + dest.serverSubRoute[j].st);
	$("#" + divID + "info").hide();
	pathIcon      = dest.makePathIcon(vno);
	pathIcon.name = dest.serverSubRoute[j].st;
	wp = dest.serverSubRoute[j].wp;
	wp = wp[0];
	pathIcon.x = wp.x;
	pathIcon.y = wp.y;
	fio.pathIcons[fio.pathIcons.length] = pathIcon;
	vno++;
      }
    }
  }

  if (fio.floorDWG) {
    divID = Frag.mapThumb(this, floorNo, thno, floorElem,
			  Building.selectADest);
    thno++;		 // Lint does not like this in the method call
    /* Assign a floor order for later use in selecting it.*/
    UIU.setElementAttr(divID, "route",      this);
    UIU.setElementAttr(divID, "whichFloor", floorNo);
    UIU.setElementAttr(divID, "inFloor",    0); // selects first dest inFloor);
    UIU.setElementAttr(divID, "boldDiv", firstDID);
    UIU.assignSrc(divID + "thumbImg", "../tiles/" +
		  wayMap.buildingName + '/' + fio.floorDWG + '/' +
		  wayMap.mapType.substring(0,3) + fio.floorDWG +
		  'thumb.png');
  }
  if (selDID) {
    // selDID for the very first one, firstDID means we stay where we were
    elem = UIU.getElementOrWhinge(selDID);
    Building.selectADest.call(elem, false);
  } else {
    alert("selDID undefined");
  }
  floorNo++;			// Handy place for a breakpoint
};

/* Returns as many as three destinations - the selected one, the prior
 * destination in the array, and the next destination in the array,
 * skipping inserted verticals.
 * When a destination is found, there may be a previous destination
 * from which to create a route. */
Route.prototype.getDestObject = function(dIDString) {
  var i;
  var dest;
  var priorDest = undefined;
  var thisDest  = undefined;
  for (i=0; i<this.destinations.length; i++) {
    if (dest = this.destinations[i]) {
      if (dest.divID == dIDString) {
	thisDest = dest;
	if (dest = this.destinations[i+1]) {
	  return [thisDest, priorDest, dest];
	}
	return [thisDest, priorDest, undefined];
      } else {
	priorDest = dest;
      }
    }
  }
  return undefined;
};

/* Called when an add destination operation is canceled  */
Route.cancelAddDest = function() {
  // console.log("Cancel add dest " + this.id);
  rID = Route.getRouteIDString(this.id);
  var route = currentBuilding.getRouteObject(rID);
  route.destinations.pop();	// remove last element of the array
  UIU.hideShow(rID, "AddDestFormBox", "AddDestination");
  route.beautify();

  return false;			// Suppress default behavior
};

/* Called when the add new destination button is pressed. This cannot
 * be done until there is a valid suggestion, because the find button
 * is disabled until then. */
Route.findAddDestination = function() {
  //console.log("Find add dest " + this.id);
  rID = Route.getRouteIDString(this.id);
  var route = currentBuilding.getRouteObject(rID);
  var dest = route.destinations[route.destinations.length-1];
  if (Destination.findInDestChangeFormID(dest.divID)) {
    if (dest.suggestObj) {
      dest.suggestObj.finalize();
    }
    dest.suggestObj = undefined;

    UIU.hideShow(rID, "AddDestFormBox", "AddDestination");
  }

  return false;			// Suppress default behavior
};

/* Add a blank destination to the route; opens the destination find box.*/
Route.addDestination = function () {
  Route.addDestinationByID(this.id, true, false);
};

/* trigger set true means to beautify

 * another set true means to add another destination after adding this
 * one.  It will be open or not depending on Building.openDestination */
Route.addDestinationByID = function (routeID, trigger, another) {
  var rID     = Route.getRouteIDString(routeID);
  var rteNo   = Route.getRouteID(routeID);
  // Building.dumpRayElAttr(currentBuilding.routes, "divID");
  var route   = currentBuilding.getRouteObject(rID);
  var dstNo   = route.destCount;
  var rteDest = "route" + rteNo + "_Dst" + dstNo;
  // console.log("Add dest " + rID + " " + rteNo + " " + trigger + " " + another + " " + route.destCount);
  var dest;
  var did = false;

  // If this is the start of a route, assign a default destination
  if (route.destCount <= 0) {
    //var elem = UIU.getElementOrWhinge(rID + "Default");
    var elem = document.getElementById(rID + "Default");
    if (elem === null || elem == undefined) {
      elem = $(rID + "Default").get(0);
    }
    //debugger;
    if (elem && elem.firstChild) {
      // This turns elemC into a string.
      var elemC = elem.firstChild.textContent;		 // for non-IE
      if (!elemC) { elemC = elem.firstChild.nodeValue; } // IE hack
      // console.log("Default dest " + elemC + " " + elemC.length);
      if (elemC.length > 2) {	// zero-length string for other destinations
	// This turns the string into a Java Script object.
	elemC = eval(elemC);
	// UIU.dumpObj(elemC);
	for (var i = 0; i<elemC.length; i++) {
	  route.destinations[route.destinations.length] =
	    dest = new Destination(rteNo, dstNo);
	  dest.routeObj = route;
	  Destination.takeSuggestion(dest, elemC[i],
	  (i >= (elemC.length-1))); // beautifies on the last destination
      }
      did = true;
    }
      // $(elem).show();
  }
} else {			// No default, show the change box
  route.destinations[route.destinations.length] =
  dest = new Destination(rteNo, dstNo);
  dest.routeObj = route;
    UIU.hideShow(rID, "AddDestination", "AddDestFormBox");
    dest.suggestObj =
    new AutoSuggest(UIU.getElementOrWhinge(rID + "SrchInputField"),
		    UIU.getElementOrWhinge(rID + "Suggestions"),
		    rID + "AddDestForm", rID + "AddDestFindBtn");
    elem = UIU.getElementOrWhinge(rID + "AddDestFindBtn");
    if (elem) {
      elem.disabled = true;
      UIU.swapClasses(elem, "btnGrey", "btnBlue");
    }
  }

  route.destCount = route.destinations.length;

  if (another) {
    Route.addDestinationByID("route" + rteNo, (trigger & !did), false);
  } else {
  // Taking the suggestion will beautify
    if (trigger && !did) { route.beautify(); }
  }
};

/* Element IDs for routes begin with routexx.  Return the string which
 * starts the element ID.*/
Route.getRouteIDString = function (id) {
  if (id.indexOf("route") !== 0) {
    return undefined;
  }
  var rno = parseInt(id.substring(5));
  return "route" + rno + "_";
};

/* Element IDs for routes begin with routexx.  Return the route number.*/
Route.getRouteID = function (id) {
  if (id.indexOf("route") !== 0) {
    return undefined;
  }
  return parseInt(id.substring(5));
};

/* Delete the route identified by the element which triggered the
 * delete.  This requires deleting all the destinations in the route
 * and all associated div elements in the DOM. */
Route.deleteIE = function () {
  var rID = Route.getRouteIDString(this.id);
  // console.log("delete route " + this.id + " " + rID);
  // Building.dumpRayElAttr(currentBuilding.routes, "divID");
  var rt = UIU.getElementOrWhinge(rID);
  if (rt) {
    $(rt).remove();		// remove the division from the document
  } else {
    alert("No route division " + rID);
  }
  var i, dest, j, params = "";
  /* Iterating on an array returns all valid indexes as properties of
   * the object. */
  for (i=0; i<currentBuilding.routes.length; i++) {
    if (rt = currentBuilding.routes[i]) {
      if (rt.divID == rID) {
	currentBuilding.routes.splice(i,1); // Delete ith element of the array
	for (j in rt.destinations) {
	  dest = rt.destinations[j];
	  if (dest.destJson) {
	    params += "&" + jQuery.param(dest.destJson);
	  }
	}
	$.get('../html.jsp?why=1&verb=6' + params,
	      function(data) {
		if (UIU.goodData(data)) {
		  if (data.indexOf("Ok") !== 0) {
		    alert(data);
		  }
		}
	      });
	break;
      }
    }
  }
  if (rt = currentBuilding.routes[0]) {
    rt.beautify();
  }
  // Building.dumpRayElAttr(currentBuilding.routes, "divID");
}

/* Find a particular route from an element ID*/
Route.findRoute = function(elementID) {
  var rID = Route.getRouteIDString(elementID);
  var i;
  /* Iterating on an array returns all valid indexes as properties of
   * the object. */
  for (i=0; i<currentBuilding.routes.length; i++) {
    if (rt = currentBuilding.routes[i]) {
      if (rt.divID == rID) {
	return rt;
      }
    }
  }
  return undefined;
};

/* On click function to show and hide any route.*/
Route.showHideRoute = function() {
  var rID = Route.getRouteIDString(this.id);
  // console.log(this.id + " " + rID);
  var $this = $(this);
  if ($this.is('.icon_expand')){
    $this.removeClass('icon_expand');$this.addClass('icon_collapse');
  } else if ($this.is('.icon_collapse')) {
    $this.removeClass('icon_collapse');$this.addClass('icon_expand');
  }
  $("#" + rID + "Content").slideToggle("fast");
};

/* Choosing a destination causes the software to generate a route
 * between the selected destination and the previous destination in
 * the route.  It also generates a new route between itself and the
 * next destination in the route.
 */
function Destination (rteNo, dstNo) {
  /* JSON object returned from the server when the destination is
   * found in the database.  Note that most of the properties have the
   * same names as the columns in the SQL table.  Properties are
   * subject to change as the database table changes.*/
  this.destJson = undefined;
  /* Current search object for a destination.  If null or does not
   * have a current selection, the submit cannot happen.*/
  this.suggestObj = undefined;
  /* Object representing the route which contains this destination.*/
  this.routeObj = undefined;
  this.rteNo = rteNo;
  this.dstNo = dstNo;
  /* The division which displays the destination within the route*/
  this.divID = "route" + rteNo + "_Dst" + dstNo + "_";
  /* The floor drawing name determines which set of map tiles to use to
   * display this destination.*/
  this.floorDWG = undefined;
  /* This information tells how to get from this destination to the
   * subsequent destination in the route.  The sub route may traverse
   * any number of floors.*/
  this.serverSubRoute = undefined;
};

Destination.prototype.setNo = function(dstNo) {
  this.dstNo = dstNo;
  this.divID = "route" + this.rteNo + "_Dst" + dstNo + "_";
};

Destination.imageIconID = 0;

Destination.prototype.makePathIcon = function(isV) {
  if (!this.destJson) { return undefined; }

  var x = this.destJson.x;
  var y = this.destJson.y;
  var descr = this.destJson.desc;
  var image;
  if (isV !== null) {
    var vv = ((isV - 1) >> 1)  + 1;
    image = Frag.vertIconImage + vv + ".png";
    return new InfoIcon(this, descr, ("PI" + Destination.imageIconID++),
			x, y, 29, 26, image, InfoIcon.NORTH_ARROW,
                        InfoIcon.VERTICAL);
  } else {
    if (this.dstNo === 0) {
      image = Frag.startIconImage;
    } else {
      var alarm = (this.destJson.inDsc &&
		   (this.destJson.inDsc.indexOf("A") === 0));
      image = (alarm ? Frag.alarmIconImage : Frag.otherIconImage) +
      Frag.alphabet.substring(this.dstNo, 1+this.dstNo) + ".png";
      if (alarm) {
	var s = this.destJson.inDsc.split("|");
	descr = s[1] + " " + s[4] + " " + s[5];
      }
    }
    return new InfoIcon(this, descr, ("PI" + Destination.imageIconID++),
			x, y, 29, 26, image, InfoIcon.NORTH_ARROW);
  }
};

Destination.getDestIDString = function(id) {
  if (id.indexOf("route") !== 0) {
    return undefined;
  }
  var ix;
  if ( (ix = id.indexOf("_",5)) < 0) {
    return undefined;
  }
  var dID = parseInt(id.substring(ix + 4)); // Skip _Dst and get the number
  return id.substring(0, ix) + "_Dst" + dID + "_";
};

Destination.getDestID = function(id) {
  if (id.indexOf("route") !== 0) {
    return undefined;
  }
  var ix;
  if ( (ix = id.indexOf("_",5)) < 0) {
    return undefined;
  }
  var dID = parseInt(id.substring(ix + 4)); // Skip _Dst and get the number
  return dID;
};

/* Actually returns an array of 3 destinations - the matching dest,
 * the one before it in the array if there is one, and the one after
 * it in the array if there is one. */
Destination.getDestInRoute = function(triggerID) {
  var rIDString = Route.getRouteIDString(triggerID);
  if (!rIDString) { return undefined; }
  var route = currentBuilding.getRouteObject(rIDString);
  if (!route)     { return undefined; }
  var dIDString = Destination.getDestIDString(triggerID);
  var dest = route.getDestObject(dIDString);
  return dest;
};

Destination.getOuterID = function(id) {
  var ix = id.lastIndexOf("_");
  return id.substring(0, ix+1);
};

Destination.showDestChangeForm = function() {
  Destination.showDestChangeFormID(this.id);
};

Destination.showDestChangeFormID = function(dIDStrin) {
  var dIDString = Destination.getDestIDString(dIDStrin);
  var dest  = Destination.getDestInRoute(dIDString)[0];
  var route = dest.routeObj;
  // console.log("Show Change " + dIDString);

  var elem = UIU.getElementOrWhinge("route" + route.rteNo + "_Content");
  $("div#route" + route.rteNo + "_Content").empty();

  var canID = Frag.changeForm(route, elem, Destination.help,
			      Destination.hideDestChangeForm,
			      Destination.findInDestChangeForm);
  elem = UIU.getElementOrWhinge(canID);
  elem.dest = dest;		// Put the destination on the form

  var lbl;
  if (dest.dstNo <= 0) {
    lbl = "Start Point";
  } else {
    lbl = 'Destination <img src="../images/icon-24_destination_' +
    Frag.alphabet.substring(this.dstNo, 1+this.dstNo) + '.png">';
  }
  UIU.assignHTML(canID + "what", lbl);

  dest.suggestObj =
  new AutoSuggest(UIU.getElementOrWhinge(canID + "SrchInputField"),
		  UIU.getElementOrWhinge(canID + "Suggestions"),
		  canID + "ChngDestForm", canID + "ChngFindBtn");
};

Destination.hideDestChangeForm = function() {
  // console.log("Hide dest change " + this.id);

  var cIDString = Building.topElementID(this.id);
  var elem = UIU.getElementOrWhinge(cIDString);
  var dest = elem.dest;
  Destination.hideDestChangeFormID(dest.divID);
};

Destination.hideDestChangeFormID = function(dIDString) {
  // console.log("Hide Change" + dIDString);
  var dest = Destination.getDestInRoute(dIDString)[0];
  if (dest) {
    if (dest.suggestObj) {
      dest.suggestObj.finalize();
    }
    dest.suggestObj = undefined;
    var route = dest.routeObj;
    route.beautify();
  }
};

Destination.help = function() {
  var dIDString = Destination.getDestIDString(this.id);
  console.log("Change dest help " + dIDString);
};

Destination.addHelp = function() {
  var dIDString = Route.getRouteIDString(this.id);
  console.log("Add dest help " + dIDString);
};

/* Function which is used to carry out the selection of a destination*/
Destination.takeSuggestion = function(des, choice, beau) {
  var beautify = beau;
  var dest = des;
  // console.log(" Take sugg " + choice.sug + " " + dest.routeObj.destinations.length + " " + beau);
  $.getJSON('../json.jsp?divID=' + dest.divID + '&verb=4&why=1',
	    choice,
	    function(data) {
	      if (data['msg'])  {
		alert(data['msg']);
	      } else {
		dest.destJson = data;
	      }
	      if (beautify) { dest.routeObj.beautify(); }
	    });
}

/* Function which is called when the find button is pressed in the
 * form for changing a destination.  Changing a destination wipes out
 * any sub route from it to the next destination and from the previous
 * destination to it.  */
Destination.findInDestChangeForm = function() {
  // console.log("Find dest change " + this.id);
  var cIDString = Building.topElementID(this.id);
  var elem = UIU.getElementOrWhinge(cIDString);
  var dest = elem.dest;

  Destination.findInDestChangeFormID(dest.divID);
};

Destination.findInDestChangeFormID = function(destID) {
  var dests = Destination.getDestInRoute(destID);
  dest      = dests[0];

  if (dest.suggestObj.inserted >= 0) {
    var dest2  = dests[1];
    if (dest2) { dest2.serverSubRoute = undefined; }
    dest.serverSubRoute = undefined;
    var choice;
    dest.destJson = undefined;
    choice = dest.suggestObj.suggestions[dest.suggestObj.inserted];
    Destination.takeSuggestion(dest, choice, true); // beautifies later
    return true;
  } else {
    alert("Make a choice before hitting the find button, please");
  }
  return false;			// Prevent normal action
};

/** Called when the exit from here button is pressed in a UI box.  */
Destination.exitFromHere = function() {
  Destination.destAfterDest(this.id,"Nearest Emergency Exit", true);
};

Destination.destAfterDest = function(destID, where, clean) {
  var dests   = Destination.getDestInRoute(destID);
  dest        = dests[0];
  var i,route = Route.findRoute(destID);
  for (i=0; i<route.destinations.length; i++) {
    if (dest == route.destinations[i]) {
      break;
    }
  }
  //console.log(destID + " " + route.rteNo + " " + i);
  dest.serverSubRoute = undefined;	   // Any existing routes go nowhere
  if (clean && dest.destJson) {
    route.destinations[0] = dest;
    route.destinations.length = 1;
    i = 0;
    dests[1] = undefined;
  } else {
    route.destinations.length = i+1; // Delete any other destinations
  }
  if (dest.destJson) {
    // Destination has a destination, add another destination.
    route.destinations[route.destinations.length] =
      dest          = new Destination(route.rteNo, route.destCount);
    dest.routeObj   = route;
    route.destCount = route.destinations.length;
    dest.destJson   = {divID:dest.divID};
  } else {
    dest.destJson   = {divID:dest.divID};
    if (dests[1]) {
      dests[1].serverSubRoute = undefined;
    }
  }
  var choice      = {ilk:0, sug:where};
  Destination.takeSuggestion(dest, choice, true); // beautifies later
};

/* The trick is to be able to send two destination objects to the
 * server.  jQuery is notably poor about building complex URLs; the
 * param function does not prepend a leading & for example.  This
 * strategy means that if additional parameters are added to the
 * suggestions, no changes on the client end will be needed.  The
 * suggestions are sent back to the server as they came.

 * NOTE: It is imperative that this function call beautify
 * eventually.  */
Destination.getRouteBetween = function(rt, des1, des2) {
  /* Save these for use in the callback.  Variables are passed along
   * as part of the function environment, parameters are not. */
  var route = rt;
  var dest1 = des1;
  var dest2 = des2;
  //console.log("RouteBetween " + jQuery.param(dest1.destJson) + " " +
  //	      jQuery.param(dest2.destJson));

  $.getJSON('../route.jsp?verb=1&' +
	    jQuery.param(dest1.destJson), dest2.destJson,
	    function(data) {
	      var last;
	      if (data['msg'])  {
		alert(data['msg']);
		dest2.destJson = undefined; // Prevent infinite loops
		Destination.deleteByID(dest2.divID); // Beautifies
	      } else {
		dest1.serverSubRoute = data;
		/* If the destination was an abstract destination such
		 * as an emergency exit or floor, the X, Y of the
		 * destination cannot be determiend until the route
		 * has been computed.*/
		if (last = data[data.length-1].sup) {
		  last.divID = dest2.destJson.divID;
		  dest2.destJson = last;
		}
		route.beautify();
	      }
	    });
};

Destination.deleteIE = function() {
  var dID = Destination.getDestIDString(this.id);
  Destination.deleteByID(dID);
};

Destination.deleteByID = function(dID) {
  var rID = Route.getRouteIDString(dID);

  var dest = UIU.getElementOrWhinge(dID);
  if (dest) {
    $(dest).remove();		// remove the division from the document
  } else {
    // alert("No destination " + dID); // Not there until after beauty
  }

  var dests = Destination.getDestInRoute(dID);
  dest      = dests[1];		// Previous destination
  if (dest) { dest.serverSubRoute = undefined; }

  var i;
  var route = currentBuilding.getRouteObject(rID);
  // Building.dumpRayElAttr(route.destinations, "divID");
  // Iterating on an array returns all the valid indexes as properties of the object.
  for (i=0; i<route.destinations.length; i++) {
    if (dest = route.destinations[i]) {
      if (dest.divID == dID) {
	route.destinations.splice(i,1); // Delete the ith element of the array
      }
    }
  }
  route.beautify();
  //UIU.dumpRayElAttr(route.destinations, "divID");
}
