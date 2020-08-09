/** object which stores building information and the current route and
 * destination list for map manipulation purposes.  Admin defines an
 * equivalent edifice class.  Note that the building class has some
 * class variables which define building behavior. */

 /** This file also defines some methods which are called from the
  * general_client file.  This file must be loaded before
  * general_client.*/

   <!-- HIDES & RESETS LOCATION SEARCH. SHOWS CURRENT LOCATION INFO -->
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
 }

/** List of building attributes which are returned as part of the JSON
 * object.  these names are also the IDs of the .html elements which
 * are to receive the street names for display to the user.*/
Building.prototype.streetList = ["topStreet", "leftStreet", "rightStreet",
				 "bottomStreet"];

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
	      if (data['msg'])  { alert(data['msg']); }
	      if (data['name']) {
		$('#way_currentAddr').text(data['name']);
	      }
	      for (i=0; i<currentBuilding.streetList.length; i++) {
		$street = currentBuilding.streetList[i];
		if (data[$street]) {
		  $('#'+$street).text(data[$street]);
		}
	      }
	      hideResetSearch();
	    });
  return false;		// Suppress the normal submit
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

/* This is a static method of the Building class.  The purpose of
 * defining static methods is to minimize damage to the name space.
 * Adding a route adds triggers, etc., to the DOM.*/
Building.addRoute = function () {
  // console.log("Add Route " + this.id);
  var rteno = currentBuilding.routeCount;
  currentBuilding.routeCount++;
  $.get('../html.jsp?verb=1', {'rteno': rteno },
	function(data) {
	  if (UIU.goodData(data)) {
	    $("div#way_routeList").append(data);
	    $("div#route" + rteno + "_ToggleTrigger").click(Route.showHideRoute);
	    $(  "a#route" + rteno + "_AddDestTrigger").click(Route.addDestination);
	    $(   "#route" + rteno + "_Delete").click(Route.delete);
	    currentBuilding.routes[currentBuilding.routes.length] = new Route(rteno);
	    // Add an open start and a destination
	    Route.addSubRouteByID("route" + rteno);
	  }
	} );
};

/* Un-select all destinations in all routes and sub-routes currently
 * defined for the entire building.  */
Building.unselectDests = function() {
  var i;
  var j;
  var rt;
  var subR;
  var elem;
  for (i=0; i<currentBuilding.routes.length; i++) {
    if (rt = currentBuilding.routes[i]) {
      for (j=0; j<rt.subRoutes.length; j++) {
	if (subR = rt.subRoutes[j]) {
	  subR.unselect();
	}
      }
    }
  }
};

/* Action routine to select the map associated with a destination or
 * an inserted vertical destination for viewing.  This is defined at
 * the building level because it operates on all of the routes. TODO
 * be able to select a vertical in a sub route.*/
Building.selectADest = function() {
  Building.unselectDests();

  var subIDString = SubRoute.getSubRouteIDString(this.id);

  console.log("selected " + this.id + " " + subIDString);
  var route = Route.findRoute(this.id);
  var subR  = route.getSubRoute(subIDString, this.id);
  var which = 0;
  if (this.id.indexOf("Dst1") > 0) {
    which = 1;
  }
  var dest = subR.select(which);
  if (this.id.indexOf("Pt1") > 0) {
    dest.whichEnd = 0;
  } else {
    dest.whichEnd = 1;
  }
  wayMap.setMapTiles(dest);
};

/* The Route class defines a number of static methods which help
 * manipulate routes.  The purpose of defining static methods is to
 * minimize damage to the name space.  */
function Route (number) {
  /* This count is incremented every time a sub route is added to a
   * route.  It is not decremented when a sub route is deleted so that
   * sub route numbers will stay unique.*/
  this.subRouteCount = 0;
  /* Variable which holds the array of sub routes along this route.*/
  this.subRoutes = new Array();
  /* The ID of the div that holds the array of sub routes in the DOM */
  this.divID = "route" + number + "_";
};

/* The id identifies the outermost sub route.  The inner ID MAY
 * identify a sub-route which is nested inside this one because of
 * inserted verticals.  */
Route.prototype.getSubRoute = function(id, innerID) {
  var i;
  var subR;
  for (i=0; i<this.subRoutes.length; i++) {
    subR = this.subRoutes[i];
    if (subR.divID == id) { return subR; }
  }
  return undefined;
};

Route.addSubRouteByID = function (routeID) {
  var rID    = Route.getRouteIDString(routeID);
  var rteNo  = Route.getRouteID(routeID);
  // UIU.dumpRayElAttr(currentBuilding.routes, "divID");
  var route  = currentBuilding.getRouteObject(rID);
  var subNo  = route.subRouteCount;
  var rteSub = "route" + rteNo + "_Sub" + subNo + "_";
  $.get('../html.jsp?verb=4', {'rteno': rteNo, 'subno': subNo },
	function(data) {
	  if (UIU.goodData(data)) {
	    var subRte;
	    $("div#route" + rteNo + "_Content").append(data);
	    route.subRoutes[route.subRoutes.length] = subRte =
	      new SubRoute(rteNo, subNo);
	    subRte.route = route;
	    subRte.wireUp(false);
	    route.subRouteCount++;
	    Destination.showDestChangeFormID(subRte.dest0.divID);
	    Destination.showDestChangeFormID(subRte.dest1.divID);
	  }
	});
};

/* Element IDs for routes begin with routexx.  Return the string which
 * starts the element ID.*/
Route.getRouteIDString = function (id) {
  if (id.indexOf("route") != 0) {
    return undefined;
  }
  var rno = parseInt(id.substring(5));
  return "route" + rno + "_";
};

/* Element IDs for routes begin with routexx.  Return the route number.*/
Route.getRouteID = function (id) {
  if (id.indexOf("route") != 0) {
    return undefined;
  }
  return parseInt(id.substring(5));
};

/* Delete the route identified by the element which triggered the
 * delete.  This requires deleting all the destinations in the route
 * and all associated div elements in the DOM. */
Route.delete = function () {
  var rID = Route.getRouteIDString(this.id);
  // console.log("delete route " + this.id + " " + rID);
  // UIU.dumpRayElAttr(currentBuilding.routes, "divID");
  var rt = document.getElementById(rID);
  if (rt) {
    $(rt).remove();		// remove the division from the document
  } else {
    alert("No route division " + rID);
  }
  var i;
  /* Iterating on an array returns all valid indexes as properties of
   * the object, not the array members themselves. */
  for (i=0; i<currentBuilding.routes.length; i++) {
    if (rt = currentBuilding.routes[i]) {
      if (rt.divID == rID) {
	while (rt.destinations.length > 0) {
	  Destination.deleteByID(rt.destinations[rt.destinations.length-1].divID);
	}
	currentBuilding.routes.splice(i,1); // Delete ith element of the array
      }
    }
  }
  // UIU.dumpRayElAttr(currentBuilding.routes, "divID");
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
  };
  $("#" + rID + "Content").slideToggle("fast");
};

function SubRoute(rteNo, subNo) {
  /* Object representing the route which contains this destination.  */
  this.route = undefined;
  this.rteNo = rteNo;
  this.subNo = subNo;
  /* The sub route segment information needed to draw the route from
   * the previous destination in the route to this destination as sent
   * from the server.  */
  this.subRouteJson = undefined;
  /* The division which displays the destination within the route*/
  this.divID = "route" + rteNo + "_Sub" + subNo + "_";

  /* Array of verticals if any*/
  this.verticals = new Array();

  this.dest0 = new Destination(rteNo, subNo, 0);
  this.dest0.subRoute = this;
  this.dest1 = new Destination(rteNo, subNo, 1);
  this.dest1.subRoute = this;
};

/* These divs within the sub route respond to clicks to select them
 * and their associated map display.*/
SubRoute.prototype.selectables =
  ["Dst0_Pt1_icon", "Dst0_Pt1_name", "Dst0_Pt2_icon", "Dst0_Pt2_name",
   "Dst1_Pt1_icon", "Dst1_Pt1_name", "Dst1_Pt2_icon", "Dst1_Pt2_name", ];

SubRoute.prototype.changeBtns =
  ["Dst0_changeTrigger", "Dst1_changeTrigger"];
SubRoute.prototype.findBtns =
  ["Dst0_ChngFindBtn",   "Dst1_ChngFindBtn"];
SubRoute.prototype.cancelBtns =
  ["Dst0_ChngCancelBtn", "Dst1_ChngCancelBtn"];
SubRoute.prototype.deleteBtns =
  ["Dst1_Pt2_delete" ];
SubRoute.prototype.helpBtns =
  ["Dst0_chngHelp", "Dst1_chngHelp" ];

/* Connect the div to the various action routines.*/
SubRoute.prototype.wireUp = function(isVert) {
  UIU.assignClick(this.divID, this.selectables, Building.selectADest);
  if (isVert) { return; }

  UIU.assignClick(this.divID, this.changeBtns,
		       Destination.showDestChangeForm);
  UIU.assignClick(this.divID, this.cancelBtns,
		       Destination.cancelDestChange);
  UIU.assignClick(this.divID, this.findBtns,
		       Destination.findInDestChangeForm);
  UIU.assignClick(this.divID, this.deleteBtns, Destination.delete);
  UIU.assignClick(this.divID, this.helpBtns,   Destination.help);
};

/* These divs within the sub route are highlighted brightly */
SubRoute.prototype.brightHigh = [ "Dst0_header", "Dst1_header", ];

/* These divs within the sub route are highlighted dimly */
SubRoute.prototype.dimHigh = [ "Dst0_directions", "Dst1_directions", ];

/* Unselect a sub-route.  TODO unselect sub-routes which are inserted
 * for verticals.*/
SubRoute.prototype.unselect = function() {
  var i;
  var s;
  var elem;
  for (i in this.brightHigh) {
    s = this.brightHigh[i];
    elem = document.getElementById(this.divID + s);
    if (elem) {
      UIU.swapClasses(elem, "bgNotSelected", "bgSelectedHeader");
    } else {
      console.log("No element " + this.divID + s);
    }
  }
  for (i in this.dimHigh) {
    s = this.dimHigh[i];
    elem = document.getElementById(this.divID + s);
    if (elem) {
      UIU.swapClasses(elem, "bgNotSelected", "bgSelectedBody");
    } else {
      console.log("No element " + this.divID + s);
    }
  }
};

/* Decide which destination to highlight and return the destination
 * object. TODO handle case of an inserted sub-route and select it as
 * needed.  */
SubRoute.prototype.select = function(which) {
  var elem;
  elem = document.getElementById(this.divID + this.brightHigh[which]);
  if (elem) {
    UIU.swapClasses(elem, "bgSelectedHeader", "bgNotSelected");
  } else {
    console.log("No element " + this.divID + s);
  }
  elem = document.getElementById(this.divID + this.dimHigh[which]);
  if (elem) {
    UIU.swapClasses(elem, "bgSelectedBody", "bgNotSelected");
  } else {
    console.log("No element " + this.divID + s);
  }
  if (which == 0) { return this.dest0; }
  return this.dest1;
}

SubRoute.getSubRouteIDString = function(id) {
  if (id.indexOf("route") != 0) {
    return undefined;
  }
  var ix;
  if ( (ix = id.indexOf("_",5)) < 0) {
    return undefined;
  }
  var sID = parseInt(id.substring(ix + 4)); // Skip _Sub and get the number
  return id.substring(0, ix) + "_Sub" + sID + "_";
};

SubRoute.getSubRouteObject = function(id) {
  var subIDString = SubRoute.getSubRouteIDString(id);
  var route = Route.findRoute(id);
  var subR  = route.getSubRoute(subIDString, id);
  return subR;
};

SubRoute.getDestInSubRoute = function(id) {
  var subR = SubRoute.getSubRouteObject(id);
  if (id.indexOf("Dst1") > 0) { return subR.dest1; }
  return subR.dest0;
};

/* Control the appearance of a sub route based on its contents.  If
 * both destinations have a destinaton Json, compute a route between
 * them.  TODO If there is an adjacent route, fix it as needed. */
SubRoute.beautify = function(dest) {
  var subR = dest.subRoute;
  var d0 = subR.dest0;
  var d1 = subR.dest1;
  var elem, elem2;
  var length = 0;

  /* Get a route if both destinations have had a destination chosen
   * and there is no route yet.*/
  if (d0.destJson && d1.destJson && !subR.subRouteJson) {

    console.log("Eliminate verticals in " + subR.divID);
    subR.verticals = new Array(); // old ones get garbage collected
    elem = UIU.getElementOrWhinge(subR.divID + "verts");
    if (elem) { $(elem).empty(); }

    /* Gets a route, sets subRouteJson, then calls beautify again.
     * The next time through, there should be a route.*/
    Destination.getRouteBetween(subR);
  } else {
    d0.hideFields();
    d1.hideFields();

    if (subR.subRouteJson) {
      length = subR.subRouteJson.length;
    }

    if (d0.destJson) {
      d0.showField("Pt1_name", d0.destJson['desc']);
      if (length <= 1) {
	d0.showField("description", "From " + d0.destJson.desc);
      } else {
	d0.showField("description", "Path to: " +
		     subR.subRouteJson[d0.whichSegment].en);
	d0.showField("Pt2_name", subR.subRouteJson[d0.whichSegment].en);
	d0.showField("list", subR.subRouteJson[d0.whichSegment].fc.ins);
	d0.showField("info", undefined);
	d0.showField("Pt2_", undefined);
	d0.thumbulate();
      }
    } else {
      d0.showField("description", "");
      d0.showField("Pt1_name", "");
    }
    d0.showField("Pt1_", undefined);

    if (d1.destJson) {
      d1.showField("Pt2_name", d1.destJson['desc']);
      if (length <= 1) {
	d1.showField("description", "To " + d1.destJson['desc']);
      } else {
	d1.showField("description", "Path To: " + d1.destJson['desc']);
	d1.showField("Pt1_name",
		     subR.subRouteJson[d1.whichSegment].st);
	d1.showField("Pt1_", undefined);
      }
    } else {
      d1.showField("description", "");
      d1.showField("Pt2_name", "");
    }
    d1.showField("Pt2_", undefined);

    d1.thumbulate();		// Last destination in a pair always shows thumb

    if (d0.destJson) {
      wayMap.setMapTiles(d0);
    }
  }
};

function Destination (rteNo, subNo, dstNo) {
  /* JSON object returned from the server when the destination is
   * found in the database.  Note that most of the properties have the
   * same names as the columns in the SQL table.  Properties are
   * subject to change as the database table changes.*/
  this.destJson = undefined;
  /* Current search object for a destination.  If null or does not
   * have a current selection, the submit cannot happen.*/
  this.suggestObj = undefined;
  this.rteNo = rteNo;
  this.subNo = subNo;
  this.dstNo = dstNo;
  this.subRoute = undefined;
  /* The division which displays the destination within the route*/
  this.divID = "route" + rteNo + "_Sub" + subNo + "_Dst" + dstNo + "_";
  /* The floor drawing name determines which set of map ties to use to
   * display this destination.*/
  this.floorDWG = undefined;
  /* The map has a zoom level and pan variables which change as the
   * user pans and scrolls the display while this destination is
   * selected.*/
  this.xPan = 0;
  this.yPan = 0;
  this.zoomLevel = WayMap.DEFAULT_ZOOM_LEVEL;
  /* The route segment information needed to draw the route from this
   * destination to the previous destination in the route.*/
  this.routeSegment = undefined;
  /* Indicate the index into the overall route segment arrau which
   * selects the route segment associated with this destination.*/
  this.whichSegment = undefined;
  /* Remember the image icon associated with the destination so that
   * it can be repeated on the map display.*/
  this.strtImageSrc = undefined;
  /* Remember the other image icon associated with the destination so
   * that it can be repeated on the map display.*/
  this.endImageSrc = undefined;
  /* Determine whether to display the start point of the floor segment
   * or the end point.  This is used to control pan in case only one
   * end of the floor segment is visible.*/
  this.whichEnd;
};

Destination.prototype.showField = function(fID, fCont) {
  // console.log(this.divID + fID + " " + fCont);
  var elem = UIU.getElementOrWhinge(this.divID + fID);
  if (elem) {
    if (fCont) {
      elem.innerHTML = fCont;
    }
    elem.style.display = ""; // setting it to visible seems not to work.
    // $(elem).show();
  }
};

/* Divisions with a destination division whch may or may not be visible */
Destination.prototype.onOffFields =
  [ "Pt1_", "Pt1_info", "Pt1_infoAbout", "Pt1_name",
    /* Pt1_ and Pt2_ must be shown for any of their inner DIVs to
     * show.  */
    "Pt2_", "Pt2_info", "Pt2_infoAbout", "Pt2_name", "thumb" ];

Destination.prototype.hideFields = function() {
  var i; var s; var elem;
  for (i in this.onOffFields) {
    s = this.onOffFields[i];
    elem = UIU.getElementOrWhinge(this.divID + s);
    if (elem) { elem.style.display= "none"; }
  }
};

Destination.prototype.setJson = function(data) {
  this.destJson = data;
  this.floorDWG = data['FDWG'];
  this.rememberImg();
};

Destination.prototype.rememberImg = function(dest) {
  var elem = UIU.getElementOrWhinge(this.divID + "Pt1_icon");
  if (elem) {
    this.strtImageSrc = elem.src;
  }
  elem = UIU.getElementOrWhinge(this.divID + "Pt2_icon");
  if (elem) {
    this.endImageSrc = elem.src;
  }
};

Destination.prototype.thumbulate = function() {
  var elem = UIU.getElementOrWhinge(this.divID + "thumbImg");
  if (elem && this.floorDWG) {
    elem.src = "../tiles/" + wayMap.buildingName + '/' +
      this.floorDWG + '/' + wayMap.mapType.substring(0,3) + this.floorDWG +
      'thumb.png';
    elem.style.display = "";
    this.showField("thumb", undefined);
  }
};

Destination.getDestIDString = function(id) {
  var rid = SubRoute.getSubRouteIDString(id);
  if (!rid) {
    return undefined;
  }
  var ix;
  if ( (ix = id.indexOf("_Dst",8)) < 0) {
    return undefined;
  }
  var dID = parseInt(id.substring(ix + 4)); // Skip _Dst and get the number
  return rid + "Dst" + dID + "_";
};

/* Returns the destination ID string if this is not a vertical*/
Destination.getVertIDString = function(id) {
  var destID = Destination.getDestIDString(id);
  var ix;
  if ( (ix = id.indexOf("_Vrt")) < 0) { return destID; }
  var vID = parseInt(id.substring(ix+4));
  return destID + "_Vrt" + vID;
};

/*
Destination.getDestID = function(id) {
  if (id.indexOf("route") != 0) {
    return undefined;
  }
  var ix;
  if ( (ix = id.indexOf("_",5)) < 0) {
    return undefined;
  }
  var dID = parseInt(id.substring(ix + 4)); // Skip _Dst and get the number
  return dID;
};
*/

Destination.showDestChangeForm = function() {
  Destination.showDestChangeFormID(this.id);
};

Destination.showDestChangeFormID = function(dIDString) {
  console.log("show " + dIDString);
  var dIDString = Destination.getDestIDString(dIDString);
  $("#" + dIDString + "display").hide();
  $("#" + dIDString + "chngFormBox").show();

  var dest = SubRoute.getDestInSubRoute(dIDString);

  dest.suggestObj =
  new AutoSuggest(document.getElementById(dIDString + "SrchInputField"),
		  document.getElementById(dIDString + "suggestions"),
		  dIDString + "chngForm", dIDString + "ChngFindBtn");

  var elem = document.getElementById(dIDString + "Default");
  if (elem) {
    var elemC = elem.innerHTML;
    // console.log("Def " + elem + " " + elemC.length);
    if (elemC.length > 2) {	// zero-length for other destinations
      elemC = eval(elemC);
      dest.suggestObj.suggestions = elemC;
      dest.suggestObj.highlighted = 0;
      dest.suggestObj.useSuggestion();
      // Updating the input field must come last
      dest.suggestObj.elem.value = elemC[0]['sug'];
      // if (Building.closeDefault) {
      Destination.findInDestChangeForm.call(document.getElementById(dest.suggestObj.btnID));
	// }
    }
    $(elem).remove();
  }
};

Destination.cancelDestChangeID = function(dIDString) {
  console.log("hide " + dIDString);
  $("#" + dIDString + "display").show();
  $("#" + dIDString + "chngFormBox").hide();

  var dest = SubRoute.getDestInSubRoute(dIDString);
  if (dest) {
    if (dest.suggestObj) {
      dest.suggestObj.finalize();
    }
    dest.suggestObj = undefined;
  }
};

Destination.cancelDestChange = function() {
  var dIDString = Destination.getDestIDString(this.id);
  Destination.cancelDestChangeID(dIDString);
  return true;			// This is a reset button; want to do it
};

/* Function which is called when the find button is pressed in the
 * form for changing a destination.*/
Destination.findInDestChangeForm = function() {
  Destination.findInDestChangeFormID(this.id);
};

/* Called when the find button is pressed in a destination of a sub
 * route or when the contents of one of its destinations changes.
 * Given that one end of the route has changed, any existing route
 * must be discarded.
 */
Destination.findInDestChangeFormID = function(id) {
  console.log("Find " + id);

  var dIDString = Destination.getDestIDString(id);
  // console.log("Find " + this.id + " " + dIDString);
  // delete adjacent verticals
  // Destination.deleteAdjacentVerticals(dIDString);

  var dest = SubRoute.getDestInSubRoute(id);
  var subR = dest.subRoute;
  dest.subRoute.subRouteJson = undefined;

  var choice;
  if (dest.suggestObj.inserted >= 0) {
    dest.destJson = undefined;
    choice = dest.suggestObj.suggestions[dest.suggestObj.inserted];
    $.getJSON('../json.jsp?divID=' + dest.divID + '&verb=4&why=1',
	      choice,
	      function(data) {
		if (UIU.goodData(data))  {
		  dest = SubRoute.getDestInSubRoute(data['divID']);
		  dest.setJson(data);
		  Destination.cancelDestChangeID(dest.divID);
		  SubRoute.beautify(dest);
		}
	      });
  } else {
    alert("Make a choice before hitting the find button, please");
  }
  return false;			// Prevent normal action
};

/* The trick is to be able to send two destination objects to the
 * server.  jQuery is notably poor about building complex URLs; the
 * param function does not prepend a leading & for example.  This
 * strategy means that if additional parameters are added to the
 * suggestions, no changes on the client end will be needed.  The
 * suggestions are sent back to the server as they came.  */
Destination.getRouteBetween = function(suR) {
  // Save these data for use in the callback.  Variables are passed along
  var subR = suR;
  var dest0 = subR.dest0;
  var dest1 = subR.dest1;

  $.getJSON('../route.jsp?verb=1&' +
	    jQuery.param(dest0.destJson), dest1.destJson,
	    function(data) {
	      var last;
	      if (data['msg'])  {
		alert(data['msg']);
	      } else {
		last = data.length-1;
		subR.subRouteJson  = data;
		dest0.floorSegment = new FloorSegment(data[0]);
		dest0.whichSegment = 0;
		dest1.floorSegment = new FloorSegment(data[last]);
		dest1.whichSegment = last;

		if (data.length > 2) {
		  Destination.getVertFor(subR);
		} else {
		  SubRoute.beautify(dest0);
		}
	      }
	    });
};

/* Get .html for a vertical traverse which is supposed to go into the
 * DOM before the destination.
Destination.getVertFor = function(seg, dst, whch) {
  var segment = seg;
  var dest  = dst;
  var which = whch;
  var floorSeg = segment[which];

  var did   = dest.divID;
  var rteNo = Route.getRouteID(did);
  var dstNo = Destination.getDestID(did);
  var route = dest.routeObj;

  var url   = "../html.jsp?verb=3&rteno=" + rteNo +
  "&dstno=" + dstNo + "&vrtno=" + route.vertCount;
  var addedRoute = route.vertCount;
  route.vertCount++;

  $.get(url,
	function(data) {
	  if (UIU.goodData(data)) {
	    var newDest;
	    var elem;

	    route.destinations[route.destinations.length] =
	      newDest = new Destination(rteNo, dstNo);
	    newDest.becomeVertical(addedRoute);
	    newDest.floorDWG = floorSeg.wp[0].o;
	    newDest.floorSegment = new FloorSegment(floorSeg);
	    var vid = "route" + rteNo + "_Dst" + dstNo + "_Vrt" + addedRoute;
	    // console.log(did + " " + which + " " + vid + " " + aVert.o);
	    $("div#" + did).before(data);
	    $('p#' + newDest.divID + "SpaceName").text(floorSeg.en);

	    Destination.thumbulate(newDest, floorSeg.fc);
	    Destination.rememberImg(dest);

	    $("div#" + vid + "Header").click(Building.selectADest);
	    $("div#" + vid + "Directions").click(Building.selectADest);
	    which++;
	    if (segment.length > (which+1)) {
	      Destination.getVertFor(segment, dest, which);
	    }
	  }});
};
*/

Destination.hideDestSearchOpts = function() {
  var dID = Destination.getDestIDString(this.id);
  // console.log("hide " + this.id + " " + dID);
  $("div#" + dID + "ChngFldOpts").hide();
  $("div#" + dID + "ChngFldNoOpts").show();
};

Destination.showDestSearchOpts = function() {
  var dID = Destination.getDestIDString(this.id);
  // console.log("show " + this.id + " " + dID);
  $("div#" + dID + "ChngFldOpts").show();
  $("div#" + dID + "ChngFldNoOpts").hide();
};

Destination.delete = function() {
  var dID = Destination.getDestIDString(this.id);
  // delete verticals before and after it
  console.log("Delete " + dID);
};

Destination.help = function() {
  var dID = Destination.getDestIDString(this.id);
  // delete verticals before and after it
  console.log("Help " + dID);
};

Destination.clearInstructions = function(dest) {
  if (dest && dest.divID) {
    var elem = document.getElementById(dest.divID + "Instructions");
    if (elem) {
      elem.innerHTML = "";
    }
  }
};

/* A destination may have had verticals inserted either before or
 * after it.  They must be removed when either the destination changes
 * or is deleted.
Destination.deleteAdjacentVerticals = function(dID) {
  // Returns array of destinations, this one, the one before, and the one after
  var dests = SubRoute.getDestInSubRoute(dID);
  var nuke = new Array();
  nuke[0] = dID;
  Destination.clearInstructions(dests[0]);
  Destination.clearInstructions(dests[1]);
  if (dests[2]) { nuke[1] = dests[2].destID; }
  var rID = Route.getRouteIDString(dID);
  route = currentBuilding.getRouteObject(rID);
  var i; var dest; var tgtID; var j;
  for (i=route.destinations.length-1; i>=0; i--) {
    if ((dest = route.destinations[i]) && (tgt = dest.divID)) {
      if (tgt.indexOf("Vrt") > 0) {
	for (j=0; j<nuke.length; j++) {
	  if (tgt.indexOf(nuke[j]) == 0) {
	    Destination.deleteByID(tgt);
	    break;
	  }
	}
      }
    }
  }
};
*/
/*
Destination.deleteByID = function(dID) {
  wayMap.removeDestination(dID);
  var rID = Route.getRouteIDString(dID);
  // console.log("delete dest " + this.id + " " + rID + " " + dID);
  var dest = document.getElementById(dID);
  if (dest) {
    $(dest).remove();		// remove the division from the document
  } else {
    alert("No destination " + dID);
  }
  var i;
  var route = currentBuilding.getRouteObject(rID);
  // UIU.dumpRayElAttr(route.destinations, "divID");
  // Iterating on an array returns all the valid indexes as properties of the object.
  for (i=0; i<route.destinations.length; i++) {
    if (dest = route.destinations[i]) {
      if (dest.divID == dID) {
	route.destinations.splice(i,1); // Delete the ith element of the array
      }
    }
  }
  //UIU.dumpRayElAttr(route.destinations, "divID");
}
*/
