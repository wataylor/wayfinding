/* Object to load, manage, and move about map tiles */

/* Class level variable for pan incerement */
WayMap.PAN_SIZE = 10;

/* How much padding to add around the map's bounding rectangle, 20 means a pad
 * of 10 on each side */
WayMap.RCT_PADDING = 20;

/* The default zoom level.  1 is the smallest zoom level possible */
WayMap.DEFAULT_ZOOM_LEVEL = 1;

/* If false call setMapTiles when zooming - ugly hack */
WayMap.doNotSet = false;

/* WayMap Constructor */
function WayMap(buildingName, mapType) {

  /* The name of the building.  Must be set whenever the building changes */
  this.buildingName = buildingName;

  /* Type of map: Axonementric - Axoimg, Room Diagram - Roomimg, etc...
   * (Bill please fill in the rest) */
  this.mapType = mapType;

  /* Html element that contains the map */
  this.mapDiv = UIU.getElementOrWhinge("way_mapTiles");

  /* Html element that contains the map */
  this.viewport = UIU.getElementOrWhinge("way_mapContainer");

  /* The current floor info object.  Each fio has its own zoom and
   * pan parameters. */
  this.fio = undefined;

  /* Length of a tile edge.  All sides are currently the same length.
   * Obtained from the jsom file */
  this.edge = undefined;

  /* Dimensions of the map's bounding rectangle.
   * Obtained from the jsom file */
  this.rctX = undefined;
  this.rctY = undefined;
  this.rctW = undefined;
  this.rctH = undefined;

  /* The scale of the map compared to the largest size possible.  The largest
   * size is not necessary the largest zoom level, the largest zoom level may
   * be smaller.  Obtained from the jsom file */
  this.scale = undefined;

  /* How many rows of tiles there are.  Obtained from the jsom file */
  this.rows = undefined;

  /* How many columns of tiles there are.  Obtained from the jsom file */
  this.cols = undefined;

  /* Simple the rows multiplied by the columns */
  this.tileCount = undefined;

  /* Keeps track of all of the cached JSON objects.  It is initialized
   * as an object with no attributes. */
  this.cachedJSONs = {};
  /* Populated with the ids of the tile images when setMapTiles is called,
   * this is used to see when all of the images are "complete."  This is
   * because we can't draw the route until all the images are loaded */
  this.imgIDs = new Array();

  /* Holds the list of info icons that can be turned on and off */
  this.deviceIcons = new Array();

  /* Booleans used for toggling the display of info icons */
  this.showFireAlarms   = false;
  this.showFirePhones   = false;
  this.showWaterFlows   = false;
  this.showPullStations = false;
  this.showAllIcons = false;

  /* Dojo surface to draw on, we need only one surface */
  this.surface = undefined;

  /* Dojo group containing shapes representing all inaccessible
   * spaces.  This group is released when any shape accessibility
   * changes.*/
  this.inaccessGroup = undefined;
}

/* Load and place tiles and calculate widths and heights.  This is
 * called from the bujilding object in two places.  1) and 2) It must
 * create a state when the floor info box is first selected.  Zoom level
 * and pan X nd Y must be recorded for each floor info box. The floor info
 * box's state is stored in a floor info object or fio. */
WayMap.prototype.setMapTiles = function(fio) {
  /* If there is an existing fio, record the current pan
   * parameters from the map div in that fio so that they can
   * be restored when the user looks at that fio later.  This
   * accounts for the case where a user drags the map but does not hit
   * a pan button before switching fios.*/

  if (wayMap.fio && (wayMap.fio != fio)) {
    var mRect = wayMap.getViewportRect();
    wayMap.fio.xPan = mRect[UIU.LEFT];
    wayMap.fio.yPan = mRect[UIU.TOP];
  }

  /* Get rid of shapes for inccessible regions just in case there
   * happened to be some from the previous floor or view.  */
  this.clearFloorImpedimenta();

  /* Remove whatever was in the map container before we add to it */
  $(wayMap.mapDiv).empty();
  wayMap.fio = fio;
  document.getElementById("way_mapFloorName").innerHTML
    = currentBuilding.dwgToFloor(fio.floorDWG);
  /* Set the current pan parameters from the fio pan parameters */
  wayMap.mapDiv.style.left = fio.xPan+'px';
  wayMap.mapDiv.style.top  = fio.yPan+'px';
  wayMap.getJsonData();

  /* Stop the endless recursion, this is set to false by the sliders callback */
  WayMap.doNotSet = true;
  wayMap.setZoomLevel(wayMap.fio.zoomLevel);
};

/* This should cache the data.  This gets the little JSON file in the
 * image tile directory.  It tells how many rows, columns, etc.  These
 * become variables of the current map.  There is one of these JSON
 * files per zoom level per floor.  */
WayMap.prototype.getJsonData = function() {
  /* Determine the Json file key.  A client has to work with only one
   * building.  TODO clear the cache when the building changes.  This
   * should be called from the building_object file when a new
   * building is selected.*/
  var keyJson = wayMap.fio.floorDWG + '/' + wayMap.fio.zoomLevel;
  var imPath = wayMap.buildingName + '/'+ keyJson;
  var dataPath = '../tiles/'+imPath+'/0'+wayMap.mapType+'.json';

  var jsd;
  if ( (jsd = wayMap.cachedJSONs[keyJson])) {
    wayMap.applyJsonData(jsd, imPath);
    return;
  }
  /* Set the path to where the tiles are */
  $.getJSON(dataPath, function(data) {
      wayMap.cachedJSONs[keyJson] = data;
      wayMap.applyJsonData(data, imPath);
    });
}

WayMap.prototype.applyJsonData = function(data, imPath) {
  /* Load the variables from the json */
  wayMap.rows = data['rows'];
  wayMap.cols = data['cols'];
  wayMap.edge = data['edge'];
  wayMap.rctX = data['rctX'];
  wayMap.rctY = data['rctY'];
  wayMap.rctW = data['rctW'];
  wayMap.rctH = data['rctH'];
  wayMap.scale = data['scale'];
  wayMap.tileCount = (wayMap.rows * wayMap.cols);
  wayMap.createTileTable(imPath);

  // checks for a size change to adjust pan and scroll
  wayMap.wayResize();

  // wayMap.storeState(fio.divID); // state is in the fio
  wayMap.drawWhenReady();
};

/* Construct the table of map tiles */
WayMap.prototype.createTileTable = function(imPath) {
  /* Empty the array of image ids */
  wayMap.imgIDs.length = 0;

  /* The format of the image name consists of xxyzz, where xx and zz are
   * numbers representing the tiles location in the tile grid.  tileY is
   * used to keep track of xx */
  var tileY;

  /* Keep track of the xxyzz string, used for the image tile's id */
  var tileID;

  var str = '\n<div style="top: 0px; left: 0px; z-index: 5;">'
  +'<table border="0" cellpadding="0" cellspacing="0">';
  var i;
  var j;
  for (i=0; i < wayMap.rows; i++) {
    if (i < 10) {
      tileY = '0'+i;
    } else {
      tileY = i;
    }
    str += '<tr>\n';
    for (j=0; j < wayMap.cols; j++) {
      if (j < 10) {
	tileID = '0'+j +'y' + tileY;
      } else {
	tileID = j +'y'+ tileY;
      }
      wayMap.imgIDs[wayMap.imgIDs.length] = "i"+tileID;
      str += '<td><img '
	+'id="i'+tileID+'" '
	+'style="display:block;" '
	+'src="../tiles/' + imPath + '/'
	+ wayMap.mapType + tileID + '.png"></td>\n';
    }
    str += '</tr>\n';
  }
  str += '</table></div>';
  str += '\n<div id=way_drawingSurface ';
  str += 'style="position:absolute; top: 0px; left: 0px; z-index:10;'
  //str += 'right: 0px; bottom: 0px;'
  str += '"></div>\n';
  $(wayMap.mapDiv).append(str);
}

/* This is called continually, after a timeout, until all of the images are
 * loaded.  Once all of the images are loaded it draws the array of
 * floorSegments and icons.
 */
WayMap.prototype.drawWhenReady = function() {
  var allImagesLoaded = true;
  var i = 0;
  var timeout = undefined;
  while(allImagesLoaded && i < wayMap.imgIDs.length) {
    img = document.getElementById(wayMap.imgIDs[i]);
    if (!img.complete) {
      allImagesLoaded = false;
      timeout = setTimeout("wayMap.drawWhenReady()",500);
    }
    i++;
  }
  if (allImagesLoaded) {
    if (timeout) { clearTimeout(timeout); }

    /* Drawing surface width and height */
    var sw = $("div#way_mapTiles").width();
    var sh = $("div#way_mapTiles").height();
    /* Construct the dojo drawing surface; this div is inserted into
     * the document by the function createTileTable */
    var drawingSurface = dojo.byId("way_drawingSurface");
    /* The drawing surface is always available as part of the map.  */
    wayMap.surface = dojox.gfx.createSurface(drawingSurface, sw, sh);

    var currentFio = wayMap.fio;
    /* The fio has two images in it, one for the start icon and one for
     * the end icon.  There is also a variable in fio named whichEnd which is
     * 0 or 1 depending on which end of the floor segment ought to be visible.
     * Panning is done as required to ensure that this is the case. */
    if (currentFio) {
      InfoIcon.addInfoBubble();
      //debugger;
      for (i=0; i<currentFio.floorSegments.length; i++) {
	var dYBR = new DojoYBR(wayMap.surface, wayMap.fio.zoomLevel);
	currentFio.floorSegments[i].drawRoute(dYBR);
      }
      // Need to rewrite setIconDirections
      InfoIcon.setIconDirections(currentFio.pathIcons);
      for (i=0; i<currentFio.pathIcons.length; i++) {
	InfoIcon.addIcon(currentFio.pathIcons[i]);
      }

      wayMap.drawMarkers(currentFio.pathIcons);
      wayMap.showIconInViewport(currentFio.pathIcons, currentFio.selectedIcon);
      wayMap.getFloorImpedimenta();
      wayMap.getFloorInfoIcons();
    }
  }
};

/* Set the checked/unchecked icon based on the passed in boolean */
WayMap.prototype.setCheckboxImage = function(id, isChecked) {
  var elem = UIU.getElementOrWhinge(id);
  if (isChecked) {
    $(elem).attr({"src" : "../images/icon_on.png"});
  } else {
    $(elem).attr({"src" : "../images/icon_off.png"});
  }
}

/* Toggle the show fire alarm icons boolean and call set icon visibility */
WayMap.prototype.toggleFireAlarms = function() {
  wayMap.showFireAlarms = !wayMap.showFireAlarms;
  wayMap.setCheckboxImage("iFireAlarms", wayMap.showFireAlarms);
  wayMap.setIconVisibility();
}

/* Toggle the show fire phone icons boolean and call set icon visibility */
WayMap.prototype.toggleFirePhones = function() {
  wayMap.showFirePhones = !wayMap.showFirePhones;
  wayMap.setCheckboxImage("iFirePhones", wayMap.showFirePhones);
  wayMap.setIconVisibility();
}

/* Toggle the show waterflow icons boolean and call set icon visibility */
WayMap.prototype.toggleWaterFlows = function() {
  wayMap.showWaterFlows = !wayMap.showWaterFlows;
  wayMap.setCheckboxImage("iWaterFlows", wayMap.showWaterFlows);
  wayMap.setIconVisibility();
}

/* Toggle the show fire alarm icons boolean and call set icon visibility */
WayMap.prototype.togglePullStations = function() {
  wayMap.showPullStations = !wayMap.showPullStations;
  wayMap.setCheckboxImage("iPullStations", wayMap.showPullStations);
  wayMap.setIconVisibility();
}

/* Toggle the show all icons boolean and call set icon visibility */
WayMap.prototype.toggleShowAllIcons = function() {
  wayMap.showAllIcons = !wayMap.showAllIcons;
  wayMap.setCheckboxImage("iAllInfoIcons", wayMap.showAllIcons);
  wayMap.setIconVisibility();
}

/* Run through all the device icons hiding and showing them based on their
 * type flag and the all flag */
WayMap.prototype.setIconVisibility = function() {
  for (i=0; i<wayMap.deviceIcons.length; i++) {
    if (wayMap.showDevIcon(wayMap.deviceIcons[i])) {
      InfoIcon.showIcon(wayMap.deviceIcons[i]);
    } else {
      InfoIcon.hideIcon(wayMap.deviceIcons[i]);
    }
  }
}

/* Return true if we should display the device icon.  If it is not a known
 * device icon type we return false */
WayMap.prototype.showDevIcon = function(icon) {
  switch(icon.type) {
  case InfoIcon.FIRE_ALARM:
    if (wayMap.showFireAlarms) { return(true); }
    break;
  case InfoIcon.FIRE_PHONE:
    if (wayMap.showFirePhones) { return(true); }
    break;
  case InfoIcon.WATER_FLOW:
    if (wayMap.showWaterFlows) { return(true); }
    break;
  case InfoIcon.PULL_STATION:
    if (wayMap.showPullStations) { return(true); }
    break;
  default:
    return(false);
    break;
  }
  return(wayMap.showAllIcons);
}

/* Add circle and triangle markers at icon positions.  A triangle is always
 * placed at the first icon, sub-zero, and circles are always placed at
 * every other icon. */
WayMap.prototype.drawMarkers = function(icons) {

  var g = wayMap.surface.createGroup();
  for (i=0; i<icons.length; i++) {
    var radius = 25*wayMap.scale;
    var ix = icons[i].sx;
    var iy = icons[i].sy;
    if (i != 0) {
      g.createCircle({cx: ix, cy: iy, r: radius})
	.setFill("red").setStroke({color: "red", width: 1});
    } else {
      tri = g.createPath().setFill("red").setStroke({color: "red", width: 1});
      ix = icons[i].sx;
      iy = icons[i].sy-radius;
      tri.moveTo(ix, iy);
      ix = icons[i].sx-(radius);
      iy = icons[i].sy+(radius);
      tri.lineTo(ix, iy);
      ix = icons[i].sx+(radius);
      tri.lineTo(ix, iy);
      tri.closePath();
    }
  }
};

/* Clear impedimenta such as inaccessible shapes and information icons
 * from the durrent map.  This call is needed when called to clean up
 * after accessibility has been changed. */
WayMap.prototype.clearFloorImpedimenta = function() {
  if (this.inaccessGroup) {
    this.inaccessGroup.removeShape(); // Removes from parent shape
    this.inaccessGroup = undefined;
  }
};

/* Return the group on which to draw inaccessible spaces; create it if
 * it does not already exist. */
WayMap.prototype.getInaccessibleGroup = function() {
  if (this.inaccessGroup) { return this.inaccessGroup; }
  this.inaccessGroup = this.surface.createGroup();
  this.inaccessGroup.setTransform({xx: wayMap.scale, yy: wayMap.scale});
  return this.inaccessGroup;
};

/* Return a shape whose fill, etc., are initialized so that it can show
 * an inaccessible space */

WayMap.prototype.makeInaccessShape = function(content) {
  var sh, gp = this.getInaccessibleGroup();
  sh = gp.createPath().setFill([255, 0, 0, .5]).setStroke("black");
  sh.setShape(content);
};

/* Get inaccessible shapes and information icons for the current
 * floor.  This method does not clear them because it is usually
 * called from drawWhenready.  It is up to other callers to get rid of
 * such objects before calling this function.  Recomputes all routes
 * after loading all floor imagery if reroute is true.
 */
WayMap.prototype.getFloorImpedimenta = function(reroute) {
  var rer = reroute;
  $.getJSON('../json.jsp?verb=15&FDWG=' + wayMap.fio.floorDWG,
	    function(data) {
	      if (data['msg'])  {
		alert(data['msg']);
	      } else if (data['scs']) {
		// alert(data['scs']); // Skip this once it works
	      } else {
		var i, one, gp, shp, imped = data; // array of things
		for (i=0; i<imped.length; i++) {
		  one = imped[i];
		  if (one.ilk && (one.ilk == "inac")) {
		    wayMap.makeInaccessShape(one.sh);
		  }
		}
	      }
	      if (rer) {
		Building.unRouteRoutes();
		Building.selectADestID(currentBuilding.selectedDest, true);
	      }
	    });
};

/* Get the info icons for the floor.  This should only be called once per
 * floor load.  We add the icons to the display and then hide them.  We
 * show them if the user selects show from the menu. This currently gets
 * called every time the map changes, including zooming, so we currently
 * clear the arrays of info icons.  We may decide we need to cache these,
 * seeing as that would be much more efficient. */
WayMap.prototype.getFloorInfoIcons = function() {
  this.deviceIcons.length = 0;

  $.getJSON('../json.jsp?verb=17&FDWG=' + wayMap.fio.floorDWG,
	    function(data) {
	      if (data['msg'])  {
		alert(data['msg']);
	      } else if (data['scs']) {
		// alert(data['scs']); // Skip this once it works
	      } else {
		var i, nfo, devices = data; // array of things
		for (i=0; i<devices.length; i++) {
		  nfo = devices[i];
		  if (nfo.ilk && (nfo.ilk == "dev")) {
		    if (nfo.subilk) {
		      /* Sort out the lists of devices */
		      switch(nfo.subilk) {
		      case "Smoke(Photo)" :
		      case "Smoke(Duct P) PHOTO" :
		      case "Smoke(Ion)" :
		      case "Heat" :
		      case "Smoke Alarms" :
			wayMap.addDevIcon(nfo, InfoIcon.FIRE_ALARM,
					     "icon-24_device_firealarm.png");
			break;
		      case "Control Telephdev" :
			wayMap.addDevIcon(nfo, InfoIcon.FIRE_PHONE,
					     "icon-24_device_firephdev.png");
			//console.log("fire_phone");
			break;
		      case "Monitor Waterflow" :
		      case "Monitor Track Superv" :
		      case "Waterflow/Track Superv" :
			wayMap.addDevIcon(nfo, InfoIcon.WATER_FLOW,
					     "icon-24_device_waterflow.png");
			break;
		      case "Monitor Pull Station" :
			wayMap.addDevIcon(nfo, InfoIcon.PULL_STATION,
					  "icon-24_device_pullstation.png");
			break;
		      case "Control Relay" :
			break;
		      default:
			break;
		      }
		    }
		  }
		}
		wayMap.adjustDevIcons();
	      }
	    });
};

/* Move any icons that have the same x and y so that they do not completely
 * cover each other.  The icons may still overlap each other, but they will
 * not be completely hidden. */
WayMap.prototype.adjustDevIcons = function() {
  var needsAdjusting = new Array();
  var group = new Array();
  group[0] = -1;
  for (i=0; i<wayMap.deviceIcons.length-1; i++) {
    if ((wayMap.deviceIcons[i].x == wayMap.deviceIcons[i+1].x) &&
	(wayMap.deviceIcons[i].y == wayMap.deviceIcons[i+1].y)) {
      if (group[0] == -1) { group[0] = i; }
      group[1] = i+1;
      //console.log(wayMap.deviceIcons[i].name +" is under "
      //	  +wayMap.deviceIcons[i+1].name);
      /* Handle the case where the last one equals another  */
      if (i == wayMap.deviceIcons.length-2) {
	needsAdjusting[needsAdjusting.length] = group;
      }
    } else if (group[0] != -1) {
      needsAdjusting[needsAdjusting.length] = group;
      //console.log("_start="+group[0]+" _end="+group[1]);
      group = new Array();
      group[0] = -1;
    }
  }
  wayMap.pinwheelDevIcons(needsAdjusting);
}

/* pinwheels the groups of icons around their center points
 * groups - an array of arrays of indexes into the deviceIcon array which 
 * group together icons that currently share the same x and y values. */
WayMap.prototype.pinwheelDevIcons = function(groups) {
  var radius = (200)*(wayMap.scale);
  if (radius > 60) { radius = 24; }
  for (i=0; i<groups.length; i++) {
    //console.log("start="+groups[i][0]+" end="+groups[i][1]);
    var angle = 0;
    var len = (groups[i][1])-(groups[i][0])+1;
    var radians = (360/len)*(Math.PI/180);
    var icon = wayMap.deviceIcons[groups[i][0]];
    var ox = icon.x;
    var oy = icon.y;
    var x = ox;
    var y = oy;
    //console.log("ox="+ox+" oy="+oy);
    for (j=groups[i][0]; j<=groups[i][1]; j++) {
      icon = wayMap.deviceIcons[j];
      var x = ox+(Math.cos(radians*angle)*radius);
      var y = oy+(Math.sin(radians*angle)*radius);
      icon.setLocation(x, y);
      //console.log("x="+x+" y="+y);
      angle++;
    }
  }
}

/* 360/icons = degrees then locate points on a circle */

/* Add a device icon to the arrays of device icons.  Hide the icons only
 * if they are not currently set to be displayed.  This means that the icons
 * will stay visible when switching zoom levels and maps.
 *
 * dev - the hashmap array about this device
 * devIdx - the index into the device array indicating which type of device
 * iconName - the file name of the image to display for the icon */
WayMap.prototype.addDevIcon = function(dev, devType, iconName) {
  var icon = new InfoIcon(null,
		  dev.name +" - " + dev.subilk,
		  dev.id,
		  dev.X, dev.Y,
		  24, 24,
		  "../images/"+iconName,
		  InfoIcon.NO_ARROW,
		  devType);
  icon.destID  = dev.id;
  icon.destILK = dev.serverilk;
  wayMap.deviceIcons[wayMap.deviceIcons.length] = icon;
  InfoIcon.addIcon(icon);
  if (!wayMap.showDevIcon(icon)) { InfoIcon.hideIcon(icon); }
}


/* Calculate the map container (viewport), and the left nav (map info) sizes */
WayMap.prototype.wayResize = function() {
  /* These two divs are what's above the blue line above the map.  This is
   * what we need to subtract from the viewport height */
  var mastheadH = $("div#way_mastheadContainer").height();
  var bldInfoH = $("div#way_bldgInfoContainer").height();

  var viewportW = Math.floor(UIU.getW()-300-25);
  var viewportH = Math.floor(UIU.getH()-mastheadH-bldInfoH);

  /* Current idea is that the viewport need not be much larger than the map
   * wayMap.rctH and wayMap.rctPadding were undefined which caused
   * mapInfoH and viewportH to be NaN in IE */
  if ((wayMap.rctH != undefined) && (wayMap.rctPadding != undefined)) {
    if (viewportH > (wayMap.rctH + wayMap.rctPadding)) {
      viewportH = wayMap.rctH + wayMap.rctPadding;
    }
  }

  var mapInfoW = 300;
  var mapInfoH = viewportH;

  //console.log(viewportW);
  //console.log(viewportH);
  /* Set the width and height of the map's viewport */
  $("div#way_mapContainer").width(viewportW);
  $("div#way_mapContainer").height(viewportH);

  //$("div#way_drawingSurface").width(viewportW);
  //$("div#way_drawingSurface").height(viewportH);

  //console.log(mapInfoH);
  /* Set the width and height of the left nav (map info) */
  $("div#way_mapInfoContainer").width(mapInfoW);
  $("div#way_mapInfoContainer").height(mapInfoH);

  /* Call center which centers the map unless there is a recorded
     state.  The first time a fio comes up, the map should pan
     to be sure that the start point is visible. */
  if (!wayMap.fio) {
    wayMap.wayCenter();
  }
}

/* Center the map the first time we see it, load its state the rest of
 * the time. */
WayMap.prototype.wayCenter = function() {
  var viewW = $("div#way_mapContainer").width();
  var viewH = $("div#way_mapContainer").height();
  //console.log("viewW="+viewW+" viewH="+viewH);
  //console.log("wayMap.rctW="+wayMap.rctW+" wayMap.rctX="+-wayMap.rctX);
  //console.log("wayMap.rctH="+wayMap.rctH+" wayMap.rctX="+-wayMap.rctY);
  var newX = (parseInt(viewW)/2)-(parseInt(wayMap.rctW)/2)-wayMap.rctX;
  var newY = (parseInt(viewH)/2)-(parseInt(wayMap.rctH)/2)-wayMap.rctY;
  //console.log("newX="+newX+" newY="+newY);
  if (wayMap.fio) {
    wayMap.fio.xPan = newX;
    wayMap.fio.yPan = newY;
    wayMap.mapDiv.style.left = newX+'px';
    wayMap.mapDiv.style.top  = newY+'px';
  }
}

/* Step 1: Get the bounding rect enclosing all icons and center it.
 * Step 2: If the selected icon is inside the rect we are done, else goto 3
 * Step 3: If needed move the map so the selected icon is just inside */
WayMap.prototype.showIconInViewport = function(icons, selectedIcon) {
  var icon = icons[selectedIcon];
  wayMap.wayCenter();
  if (!wayMap.isIconInsideViewport(icon)) {
    wayMap.centerIcons(icons);
  }
  if (!wayMap.isIconInsideViewport(icon)) {
    wayMap.moveIconInsideViewport(icon);
  }
}

WayMap.ICON_INSET = 40;

/* Move The icon just inside the viewport */
WayMap.prototype.moveIconInsideViewport = function(icon) {
  var viewport = wayMap.getViewportRect();
  var mRect = wayMap.getMapDivRect();
  var x = icon.cx+mRect[UIU.LEFT];
  var y = icon.cy+mRect[UIU.TOP];
  var w = icon.w;
  var h = icon.h;
  var newX = mRect[UIU.LEFT];
  var newY = mRect[UIU.TOP];
  if ((x-WayMap.ICON_INSET) < viewport[UIU.X]) {
    newX = mRect[UIU.LEFT] - x + WayMap.ICON_INSET;
  } else if ((x+w+WayMap.ICON_INSET) > (viewport[UIU.WIDTH])) {
    newX = mRect[UIU.LEFT] - (x+w-viewport[UIU.WIDTH]+WayMap.ICON_INSET);
  }

  if (((y-WayMap.ICON_INSET) < viewport[UIU.Y])) {
    newY =  mRect[UIU.TOP] - y + WayMap.ICON_INSET*2;
  } else if ((y+h+WayMap.ICON_INSET) > (viewport[UIU.HEIGHT])) {
    newY = mRect[UIU.TOP] - (y+h-viewport[UIU.HEIGHT]+WayMap.ICON_INSET);
  }
  //debugger;
  if (wayMap.fio) {
    wayMap.fio.xPan = newX;
    wayMap.fio.yPan = newY;
    wayMap.mapDiv.style.left = newX+'px';
    wayMap.mapDiv.style.top  = newY+'px';
  }
}

WayMap.prototype.isIconInsideViewport = function(icon) {
  var viewport = wayMap.getViewportRect();
  var mRect = wayMap.getMapDivRect();
  var x = icon.cx+mRect[UIU.LEFT];
  var y = icon.cy+mRect[UIU.TOP];
  var w = icon.w;
  var h = icon.h;
  if ((x-WayMap.ICON_INSET < viewport[UIU.X]) ||
      ((x+w+WayMap.ICON_INSET) > (viewport[UIU.WIDTH]))) {
    return(false);
  }
  if ((y-WayMap.ICON_INSET < viewport[UIU.Y]) ||
      ((y+h+WayMap.ICON_INSET) > (viewport[UIU.HEIGHT]))) {
    return(false);
  }
  return(true);
}

/* Centers the point icon to the viewport */
WayMap.prototype.centerIcon = function(icon) {
  var viewport = wayMap.getViewportRect();
  var newX = ((viewport[UIU.WIDTH]/2)-(icon.cx));
  var newY = ((viewport[UIU.HEIGHT]/2)-(icon.cy));
  if (wayMap.fio) {
    wayMap.fio.xPan = newX;
    wayMap.fio.yPan = newY;
    wayMap.mapDiv.style.left = newX+'px';
    wayMap.mapDiv.style.top  = newY+'px';
  }
}

/* Center all icons in the viewport */
WayMap.prototype.centerIcons = function(icons) {
  var viewport = wayMap.getViewportRect();
  var center = wayMap.getCenterOfBoundingRectOfIcons(icons);
  var newX = ((viewport[UIU.WIDTH]/2)-(center[UIU.X]));
  var newY = ((viewport[UIU.HEIGHT]/2)-(center[UIU.Y]));
  if (wayMap.fio) {
    wayMap.fio.xPan = newX;
    wayMap.fio.yPan = newY;
    wayMap.mapDiv.style.left = newX+'px';
    wayMap.mapDiv.style.top  = newY+'px';
  }
  /* This creates a slightly translucent rectangle to go on top of the
   * existing map.*/
  var centerPointDiv = '\n<div id="centerPointDiv" class="posAbsolute" style="width: '+10+'px; height: '+10+'px; left: '+(center[UIU.X]-5)+'px; top: '+(center[UIU.Y]-5)+'px; z-index: 32; background:#0000f1; opacity:.6;"></div>'
  //$(wayMap.mapDiv).append(centerPointDiv);

}

/* Return the center of the bounding rectangle of the icons */
WayMap.prototype.getCenterOfBoundingRectOfIcons = function(icons) {
  var rect = wayMap.getBoundingRectOfIcons(icons);
  var center = new Array();
  center[UIU.X] = rect[UIU.LEFT]+(rect[UIU.WIDTH]/2);
  center[UIU.Y] = rect[UIU.TOP]+(rect[UIU.HEIGHT]/2);
  return(center);
}

/* Get the rect which encloses all the icons of the passed in InfoIcon array */
WayMap.prototype.getBoundingRectOfIcons = function(icons) {
  var viewport = wayMap.getViewportRect();
  var rect = new Array();
  rect[UIU.TOP] = Number.MAX_VALUE;;
  rect[UIU.LEFT] = Number.MAX_VALUE;
  rect[UIU.WIDTH] = 0;
  rect[UIU.HEIGHT] = 0;
  for (i=0; i<icons.length; i++) {
    var iconRect = icons[i].getRect();
    var ix = icons[i].cx;
    if (rect[UIU.LEFT] > ix) {
      //rect[UIU.LEFT] = ix-(icons[i].w/2); //+(InfoIcon.IMG_W/2);
      rect[UIU.LEFT] = iconRect[UIU.LEFT];
    }
    var iy = icons[i].cy;
    if (rect[UIU.TOP] > iy) {
      rect[UIU.TOP] = iy;
    }
    ix = iconRect[UIU.LEFT]+iconRect[UIU.WIDTH];
    //icons[i].cx+(icons[i].w/2); //+(InfoIcon.IMG_W/2)
    if ((rect[UIU.WIDTH]) < ix) {
      rect[UIU.WIDTH] = ix;
    }
    iy = icons[i].cy+icons[i].h;
    if (rect[UIU.HEIGHT] < iy) {
      rect[UIU.HEIGHT] = iy;
    }
  }
  rect[UIU.LEFT] = rect[UIU.LEFT];
  rect[UIU.TOP] = rect[UIU.TOP];
  rect[UIU.WIDTH] = rect[UIU.WIDTH]-rect[UIU.LEFT];
  rect[UIU.HEIGHT] = rect[UIU.HEIGHT]-rect[UIU.TOP];

  /* This creates a translucent rectangle on top of the map area*/
  var centerDiv = '\n<div id="centerDiv" class="posAbsolute shade" style="width: '+rect[UIU.WIDTH]+'px; height: '+rect[UIU.HEIGHT]+'px; left: '+rect[UIU.LEFT]+'px; top: '+rect[UIU.TOP]+'px; z-index: 31; opacity:.5;"></div>'
  //$(wayMap.mapDiv).append(centerDiv);
  //debugger;

  return(rect);
}

/* Return the dimensions of the mapDiv */
WayMap.prototype.getMapDivRect = function() {
  var rect = new Array();
  rect[UIU.TOP] = parseInt($(this.mapDiv).position().top);
  rect[UIU.LEFT] = parseInt($(this.mapDiv).position().left);
  rect[UIU.WIDTH] = parseInt($(this.mapDiv).width());
  rect[UIU.HEIGHT] = parseInt($(this.mapDiv).height());
  return(rect);
}

/* Return the dimensions of the viewport */
WayMap.prototype.getViewportRect = function() {
  var rect = new Array();
  rect[UIU.TOP] = 0;
  rect[UIU.LEFT] = 0;
  rect[UIU.WIDTH] = parseInt($(this.viewport).width());
  rect[UIU.HEIGHT] = parseInt($(this.viewport).height());
  return(rect);
};

/* Set the zoom level of the map programatically.  It moves the slider
 * which triggers the slider callback event. */
WayMap.prototype.setZoomLevel = function(level) {
  wayMap.fio.zoomLevel = level;
  var sliderPosition;
  // Top starts at 1 but our top is the largest size and having
  // the largest size be 1 instead of 5 hurts my head.
  if (level == 1) {
    sliderPosition = 5;
  } else if (level == 2) {
    sliderPosition = 4;
  } else if (level == 3) {
    sliderPosition = 3;
  } else if (level == 4) {
    sliderPosition = 2;
  } else if (level == 5) {
    sliderPosition = 1;
  }
  $("#way_slider").slider("moveTo", sliderPosition);
};

/* The e and ui parameters are passed to the callback from the slider.
 * different floor.  If the other floor has a different zoom level, the UI must
 * adjust the slider position to match the zoom level of the new
 * floor.  To do that, it calls this function after setting the global boolean
 * WayMap.doNotSet to true so as not to call set map tiles again.
 *
 * This does not change the destination or the floor; only
 * zoom changes.  Changing zoom requires the new JSON after
 * which resize and store state must be called. */
WayMap.sliderChangeCallback = function(e,ui) {
  /* Top starts at 1 but our top is the largest size and having
     the largest size be 1 instead of 5 hurts my head. */
  if (ui.value == 1) {
    wayMap.fio.zoomLevel = 5;
  } else if (ui.value == 2) {
    wayMap.fio.zoomLevel = 4;
  } else if (ui.value == 3) {
    wayMap.fio.zoomLevel = 3;
  } else if (ui.value == 4) {
    wayMap.fio.zoomLevel = 2;
  } else if (ui.value == 5) {
    wayMap.fio.zoomLevel = 1;
  }
  if (!WayMap.doNotSet) {
    wayMap.setMapTiles(wayMap.fio);
  }
  /* Set this to false here so that we only have to set it to true when we
   * don't want setMapTiles to be called */
  WayMap.doNotSet = false;
};

/* Setup the map's zoom slider.  This is called only once, but it
 * defines a function which is called whenever the slider changes.
 * Pan X and Y must be adjusted whenever zoom changes. */
WayMap.prototype.initSlider = function(htmlElement) {
  $(htmlElement).slider({
      min:1, max:5, steps:5, startValue:5, change:WayMap.sliderChangeCallback});
};

/* Zoom in map event used when clicking the plus button */
WayMap.prototype.connectZoomInEvent = function(htmlElement) {
  $(htmlElement).click(function() {
      var oldZoom = wayMap.fio.zoomLevel;
      wayMap.fio.zoomLevel++;
      if (wayMap.fio.zoomLevel > 5) { wayMap.fio.zoomLevel = 5;}
      if (oldZoom != wayMap.fio.zoomLevel) {
	wayMap.setZoomLevel(wayMap.fio.zoomLevel);
      }
    });
};

/* Zoom out map event used wehn clicking the minus button */
WayMap.prototype.connectZoomOutEvent = function(htmlElement) {
  $(htmlElement).click(function() {
      var oldZoom = wayMap.fio.zoomLevel;
      wayMap.fio.zoomLevel--;
      if (wayMap.fio.zoomLevel < 1) { wayMap.fio.zoomLevel = 1; }
      if (oldZoom != wayMap.fio.zoomLevel) {
	wayMap.setZoomLevel(wayMap.fio.zoomLevel);
      }
    });
};

WayMap.panhold = function(count) {
  $(this).trigger("click");
};

/* Pan map left event */
WayMap.prototype.connectPanLeftEvent = function(htmlElement) {
  $(htmlElement).click(function() {
      if (!wayMap.fio) { return; }
      wayMap.fio.xPan = parseInt(wayMap.mapDiv.style.left);
      wayMap.fio.xPan += WayMap.PAN_SIZE;
      wayMap.mapDiv.style.left = (wayMap.fio.xPan)+'px';
    });
};

/* Pan map right event */
WayMap.prototype.connectPanRightEvent = function(htmlElement) {
  $(htmlElement).click(function() {
      if (!wayMap.fio) { return; }
      wayMap.fio.xPan = parseInt(wayMap.mapDiv.style.left);
      wayMap.fio.xPan -= WayMap.PAN_SIZE;
      wayMap.mapDiv.style.left = (wayMap.fio.xPan)+'px';
    });
};

/* Pan map up event */
WayMap.prototype.connectPanUpEvent = function(htmlElement) {
  $(htmlElement).click(function() {
      if (!wayMap.fio) { return; }
      wayMap.fio.yPan = parseInt(wayMap.mapDiv.style.top);
      wayMap.fio.yPan += WayMap.PAN_SIZE;
      wayMap.mapDiv.style.top = (wayMap.fio.yPan)+'px';
    });
};

/* Pan map down event */
WayMap.prototype.connectPanDownEvent = function(htmlElement) {
  $(htmlElement).click(function() {
      if (!wayMap.fio) { return; }
      wayMap.fio.yPan = parseInt(wayMap.mapDiv.style.top);
      wayMap.fio.yPan -= WayMap.PAN_SIZE;
      wayMap.mapDiv.style.top = (wayMap.fio.yPan)+'px';
    });
};

/* Center map event */
WayMap.prototype.connectCenterMapEvent = function(htmlElement) {
  $(htmlElement).click(function() {
      if (!wayMap.fio) { return; }
      wayMap.wayCenter();
    });
}
