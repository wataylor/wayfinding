/* Object which represents location icons and info icons.  */

/* Class definition/Contructor
 * dest - Destination object, used to get the id and ilk of the icon
 * id - the html id associated with the icon.  This is used elsewhere, so
 *      we append _id to this to make it unique for use here.
 * x - unscaled x of the icon
 * y - unscaled y of the icon
 * w - width of the icon
 * h - height of the icon
 * image - the image to display for this icon
 * direction - the direction the icon is moved away from its actual x, y
 *             to accommodate its tail
 *
 * sx and sy are the original x and y scaled
 * cx and cy are the calculated x and y; that is, the x and y shifted to
 * accommodate the tail.  This is needed because elements are always
 * positioned by their top left coordinates.
 */
function InfoIcon(dest, name, id, x, y, w, h, image, direction, type) {
  this.dest    = dest;
  this.destID  = undefined;
  this.destILK = undefined;
  if (type) {
    this.type = type;
  } else {
    this.type = InfoIcon.NO_TYPE;
  }
  if (this.dest !== null) {
    /* Important, if dest is null you had better set destID and destILK
       manually after creating the icon */
    this.destID  = this.dest.destJson.id;
    this.destILK = this.dest.destJson.ilk;
  }
  this.name = name;
  this.id = id;
  this.x  = x;
  this.y  = y;
  this.cx = x*wayMap.scale;
  this.cy = y*wayMap.scale;
  this.sx = x*wayMap.scale;
  this.sy = y*wayMap.scale;
  this.w  = w;
  this.h  = h;
  this.image = image;
  this.direction = direction;
};

/* Location icons are info icons with arrows and therefore have a
   compass direction. Bare info icons do not have arrows and
   thererfore their direction is set to No_ARROW */
InfoIcon.NORTH_ARROW = 0;
InfoIcon.SOUTH_ARROW = 1;
InfoIcon.EAST_ARROW  = 2;
InfoIcon.WEST_ARROW  = 3;
InfoIcon.NO_ARROW    = 4;
InfoIcon.directions  = [];
InfoIcon.directions[0] = InfoIcon.NORTH_ARROW;
InfoIcon.directions[1] = InfoIcon.SOUTH_ARROW;
InfoIcon.directions[2] = InfoIcon.EAST_ARROW;
InfoIcon.directions[3] = InfoIcon.WEST_ARROW;
InfoIcon.directions[4] = InfoIcon.NO_ARROW;

InfoIcon.BUBBLE_ID = "infoBubbleID";
InfoIcon.BUBBLE_HEADER_ID = "bubbleHeaderID";
InfoIcon.BUBBLE_CLOSE = "infoBubbleClose";

InfoIcon.IMG_W = 26;
InfoIcon.IMG_H = 29;

/* Defines types of icons */
InfoIcon.NO_TYPE    = 0;
InfoIcon.VERTICAL   = 1;
InfoIcon.FIRE_ALARM = 2;
InfoIcon.FIRE_PHONE = 3;
InfoIcon.WATER_FLOW = 4;
InfoIcon.PULL_STATION = 5;

/* Adds an icon by creating the necessary html, positioning it by calculating
 * the offset from the actual locations coordinates */
InfoIcon.addIcon = function(icon) {
  if (!icon) {
    //console.log("InfoIcon.addIcon:icon="+icon);
    return;
  }

  icon.sx = icon.x*wayMap.scale;
  icon.sy = icon.y*wayMap.scale;
  icon.id = icon.id+"_id";
  var xOffset, yOffset, arrowStr, iconStr;

  switch(icon.direction) {
  case InfoIcon.NORTH_ARROW:
    /* this offset needs to match the offset in client.css */
    yOffset = -4; // infoIconPointerDown top
    icon.cx = Math.round(icon.sx-(InfoIcon.IMG_W/2));
    icon.cy = Math.round(icon.sy-InfoIcon.IMG_H-13+yOffset);
    arrowStr = "arrow_black_down.png";

    if (jQuery.browser.msie) {
      iconStr = '<div id="'+icon.id+ '" ' + 'class="posAbsolute" style="left:' +icon.cx+ 'px;top:'+icon.cy+ 'px; z-index:29; width:29px;" ' + 'origx="'+icon.sx+ '" origy="'+icon.sy+ '" >' + '<div class="alignCenter infoIconBodyNorth cursorHand">' + '<div class=""><img src="' +icon.image+ '" width="16" height="16"></div>' + '</div>' + '<div class="posRelative alignCenter infoIconPointerDown">' + '<img src="../images/'+arrowStr+ '" ' + 'width="'+InfoIcon.IMG_W+ '" height="'+InfoIcon.IMG_H+ '"></div>' + '</div>';
    } else {
      iconStr = '<div id="'+icon.id+ '" ' + 'class="posAbsolute" style="left:' +icon.cx+ 'px;top:'+icon.cy+ 'px; z-index:29;" ' + 'origx="'+icon.sx+ '" origy="'+icon.sy+ '" >' + '<div class="alignCenter infoIconBodyNorth cursorHand">' + '<div class="floatLeft"><img src="' +icon.image+ '" width="16" height="16"></div>' + '<p class="floatLeft paddingL5px"><strong>' +icon.name+ '</strong></p>' + '<br class="clearFloat">' + '</div>' + '<div class="posRelative alignCenter infoIconPointerDown">' + '<img src="../images/'+arrowStr+ '" ' + 'width="'+InfoIcon.IMG_W+ '" height="'+InfoIcon.IMG_H+ '"></div>' + '</div>';
    }
    break;
  case InfoIcon.SOUTH_ARROW:
    /* this offset needs to match the offset in client.css */
    yOffset = 10; // infoIconPointerUp top
    icon.cx = Math.round(icon.sx-(InfoIcon.IMG_W/2));
    icon.cy = Math.round(icon.sy+yOffset);
    arrowStr = "arrow_black_up.png";

    if (jQuery.browser.msie) {
      iconStr = '<div id="'+icon.id+ '" ' + 'class="posAbsolute" style="left:' +icon.cx+ 'px;top:'+icon.cy+ 'px; z-index:29; width:29px;" ' + 'origx="'+icon.sy+ '" origy="'+icon.sy+ '" >' + '<div class="alignCenter infoIconPointerUp">' + '<img src="../images/'+arrowStr+ '" ' + 'width="'+InfoIcon.IMG_W+ '" height="'+InfoIcon.IMG_H+ '"></div>' + '<div class="alignCenter infoIconBodySouth cursorHand">' + '<div class=""><img src="' +icon.image+ '" width="16" height="16"></div>' + '</div>' + '</div>';
    } else {
      iconStr = '<div id="'+icon.id+ '" ' + 'class="posAbsolute" style="left:' +icon.cx+ 'px;top:'+icon.cy+ 'px; z-index:29;" ' + 'origx="'+icon.sy+ '" origy="'+icon.sy+ '" >' + '<div class="posRelative alignCenter infoIconPointerUp">' + '<img src="../images/'+arrowStr+ '" ' + 'width="'+InfoIcon.IMG_W+ '" height="'+InfoIcon.IMG_H+ '"></div>' + '<div class="alignCenter infoIconBodySouth cursorHand">' + '<div class="floatLeft"><img src="' +icon.image+ '" width="16" height="16"></div>' + '<p class="floatLeft paddingL5px"><strong>' +icon.name+ '</strong></p>' + '<br class="clearFloat">' + '</div>' + '</div>';
    }
    break;
  case InfoIcon.EAST_ARROW:
    xOffset = -40;
    yOffset = 12;
    icon.cx = Math.round((icon.sx-(InfoIcon.IMG_W/2))+xOffset);
    icon.cy = Math.round(icon.sy+yOffset);
    arrowStr = "arrow_black_left.png";
    iconStr = '<div id="'+icon.id+ '" ' + 'class="posAbsolute" style="left:' +icon.cx+ 'px;top:'+icon.cy+ 'px; z-index:29;" ' + 'origx="'+icon.sx+ '" origy="'+icon.sy+ '" >' + '<div class="posRelative alignCenter infoIconPointerLeft">' + '<img src="../images/'+arrowStr+ '" ' + 'width="'+InfoIcon.IMG_W+ '" height="'+InfoIcon.IMG_H+ '"></div>' + '<div class="alignCenter infoIconBodyEast cursorHand">' + '<div class="floatLeft"><img src="' +icon.image+ '" width="16" height="16"></div>' + '<p class="floatLeft paddingL5px"><strong>' +icon.name+ '</strong></p>' + '<br class="clearFloat">' + '</div>' + '</div>';
    break;
  case InfoIcon.WEST_ARROW:
    xOffset = 41;
    yOffset = 12;
    icon.cx = Math.round((icon.sx-(InfoIcon.IMG_W/2))+xOffset);
    icon.cy = Math.round(icon.sy+yOffset);
    arrowStr = "arrow_black_right.png";
    iconStr = '<div id="'+icon.id+ '" ' + 'class="posAbsolute" style="left:' +icon.cx+ 'px;top:'+icon.cy+ 'px; z-index:29;" ' + 'origx="'+icon.sx+ '" origy="'+icon.sy+ '" >' + '<div class="posRelative infoIconPointerRight">' + '<img src="../images/'+arrowStr+ '" ' + 'width="'+InfoIcon.IMG_W+ '" height="'+InfoIcon.IMG_H+ '"></div>' + '<div class="alignCenter infoIconBodyWest cursorHand">' + '<div class="floatLeft"><img src="' +icon.image+ '" width="16" height="16"></div>' + '<p class="floatLeft paddingL5px"><strong>' +icon.name+ '</strong></p>' + '<br class="clearFloat">' + '</div>' + '</div>';
    break;
  case InfoIcon.NO_ARROW:
    /* html for the info icons that do not have tails */
    icon.cx = Math.round(icon.sx-(icon.w/2));
    icon.cy = Math.round(icon.sy-(icon.h/2));
    iconStr = '<div id="'+icon.id+ '" ' + 'class="posAbsolute" style="left: ' +icon.cx+ 'px; top: '+icon.cy+ 'px; z-index:29;" ' + 'origx="'+icon.sx+ '" origy="'+icon.sy+ '" >' + '<div class="alignCenter cursorHand">' + '<div class="floatLeft"><img src="' +icon.image+ '" width="24" height="24"></div>' + '<br class="clearFloat">' + '</div>' + '</div>';
    break;
  }

  $(wayMap.mapDiv).append(iconStr); // this works in firefox, not IE
  //wayMap.mapDiv.innerHTML += iconStr; //not work in either firefox or IE
  /* Toggle is commented out because we want to always show the name for now */
  //InfoIcon.connectIconTextToggle(icon.id);
  var elem = UIU.getElementOrWhinge(icon.id);
  if (elem) { elem.icon = icon; }
  InfoIcon.showIconLabel(elem);
  InfoIcon.connectShowBubble(icon.id);
  icon.w = $(elem).width();
  icon.h = $(elem).height();
};

InfoIcon.prototype.setLocation = function(x, y) {
  var elem = UIU.getElementOrWhinge(this.id);
  this.x = x;
  this.y = y;
  this.sx = this.x*wayMap.scale;
  this.sy = this.y*wayMap.scale;
  var xOffset, yOffset;

  switch(this.direction) {
  case InfoIcon.NORTH_ARROW:
    /* this offset needs to match the offset in client.css */
    yOffset = -4; // infoThisPointerDown top
    this.cx = this.sx-(InfoIcon.IMG_W/2);
    this.cy = this.sy-InfoIcon.IMG_H-13+yOffset;
    break;
  case InfoIcon.SOUTH_ARROW:
    /* this offset needs to match the offset in client.css */
    yOffset = 10; // infoThisPointerUp top
    this.cx = this.sx-(InfoIcon.IMG_W/2);
    this.cy = this.sy+yOffset;
    break;
  case InfoIcon.EAST_ARROW:
    xOffset = -40;
    yOffset = 12;
    this.cx = (this.sx-(InfoIcon.IMG_W/2))+xOffset;
    this.cy = this.sy+yOffset;
    break;
  case InfoIcon.WEST_ARROW:
    xOffset = 41;
    yOffset = 12;
    this.cx = (this.sx-(InfoIcon.IMG_W/2))+xOffset;
    this.cy = this.sy+yOffset;
    break;
  case InfoIcon.NO_ARROW:
    /* html for the info thiss that do not have tails */
    this.cx = this.sx-(this.w/2);
    this.cy = this.sy-(this.h/2);
    break;
  }
  $(elem).attr("origx", this.sx);
  $(elem).attr("origy", this.sy);

  //console.log("sx="+this.sx+" sy="+this.sy);
  //console.log("cx="+this.cx+" cy="+this.cy);
  elem.style.left = this.cx + 'px';
  elem.style.top  = this.cy + 'px';
};

/* Disable the right click in the info bubble pop-up */
InfoIcon.disableRightClick = function() {
  var elem = UIU.getElementOrWhinge(InfoIcon.BUBBLE_ID);
$(elem).contextMenu('mapRightClkMenu', {
    onContextMenu: function(event) { return false; }
  });
};

InfoIcon.showIcon = function(icon) {
  var elem = UIU.getElementOrWhinge(icon.id);
  if (elem) { $(elem).show(); }
};

InfoIcon.hideIcon = function(icon) {
  var elem = UIU.getElementOrWhinge(icon.id);
  if (elem) { $(elem).hide(); }
};

/* Adds the structure which houses the info bubble.  There is only ever
 * one info bubble object that is used by whichever icon is clicked last */
InfoIcon.addInfoBubble = function() {
  /* START INFO BUBBLE POPUP */
  var bubbleStr = '\n<div id="'+InfoIcon.BUBBLE_ID + '" class="posAbsolute infoBubbleWrapper" ' + 'style="left:0px;top:0px;z-index:30;display:none;">';
  $(wayMap.mapDiv).append(bubbleStr);
  InfoIcon.disableRightClick();
};

/* Populates the contents of the info bubble.  This is called from
 * showBubble.  If there is no icon passed, the image and space name
 * are omitted. */
InfoIcon.getBubbleContents = function(icon, data) {
  var image, iconName, extra = "";
  if (icon) {
    image = icon.image.replace("16", "24");
    extra = '\n<div class="floatLeft paddingL5px">' + '<img src="'+image+ '" width="24" height="24"></div>';
    iconName = icon.name;
  } else {
    iconName = data.name;
  }
  var contents = '<div id="'+InfoIcon.BUBBLE_HEADER_ID + '" class="infoBubbleHeader">' + extra + '<h1 class="floatLeft paddingRL5px">'+iconName+ '</h1>' + '\n<div id="'+InfoIcon.BUBBLE_CLOSE + '" class="floatRight cursorHand paddingRL5px">' + '<img src="../images/icon_close.png" width="16" height="16" /></div>' + '\n<br class="clearFloat"></div>' + '\n<ul id="infoBubbleMenu">';
  // END INFO BUBBLE HEADER
  // START INFO BUBBLE NAV

  /* Set these variables to null if there is no info */
  var overviewInfo = data.over;
  var companyInfo = data.org;
  var personnelInfo = data.peep;
  var photoInfo = data.pic;
  var deviceInfo = data.dev;

  if ((overviewInfo  === null) && (companyInfo === null) &&
      (personnelInfo === null) && (photoInfo   === null) &&
      (deviceInfo    === null)) {
    contents +='\n<li><a href="javascript:void(0);" ' + 'id="infoBubble_tab1" class="current">Overview</a></li>';
  }

  if (overviewInfo !== null) {
    contents +='\n<li><a href="javascript:void(0);" ' + 'id="infoBubble_tab1" class="current">Overview</a></li>';
  }
  if (deviceInfo !== null) {
    contents +='\n<li><a href="javascript:void(0);" ' + 'id="infoBubble_tab5" class="';
    if (overviewInfo === null) { contents += 'current'; }
    contents +='">Device Information</a></li>';
  }
  if (companyInfo !== null) {
    contents +='\n<li><a href="javascript:void(0);" ' + 'id="infoBubble_tab2" class="';
    if (overviewInfo === null && deviceInfo === null) { contents += 'current'; }
    contents +='">Company</a></li>';
  }
  if (personnelInfo !== null) {
    contents +='\n<li><a href="javascript:void(0);" ' + 'id="infoBubble_tab3" class="';
    if (overviewInfo === null && deviceInfo === null && companyInfo === null) {
      contents += 'current';
    }
    contents +='">Personnel</a></li>';
  }
  if (photoInfo !== null) {
    contents +='\n<li><a href="javascript:void(0);" ' + 'id="infoBubble_tab4" class="';
    if ((overviewInfo === null) && (deviceInfo    === null) &&
	(companyInfo  === null) && (personnelInfo === null)) {
      contents += 'current';
    }
    contents +='">Photos</a></li>';
  }
  contents +='</ul>';
  // END INFO BUBBLE NAV

  // START INFO BUBBLE CONTENT
  if ((overviewInfo  === null) && (companyInfo === null) &&
      (personnelInfo === null) && (photoInfo === null) &&
      (deviceInfo    === null)) {
    contents +='<div id="infoBubble_content1" ' + 'class="infoBubbleBody" style="display:visible;">' + '<p class="noInfoTxt marginT5px">There is currently no information ' + 'available about this location</p>' + '</div>';
  }

  // START OVERVIEW CONTENT
  if (overviewInfo !== null) {
    contents +='\n<div id="infoBubble_content1" ' + 'class="infoBubbleBody" style="display:visible;">' +overviewInfo + '</div>';
  }
  // END OVERVIEW CONTENT
  var displayIt;

  // START DEVICE INFO CONTENT
  if (deviceInfo !== null) {
    displayIt = "none";
    if (overviewInfo === null) {
      displayIt = "visible";
    }
    contents +='\n<div id="infoBubble_content5" ' + 'class="infoBubbleBody" style="display:'+displayIt+ ';">' +deviceInfo + '</div>';
  }
  // END DEVICE INFO CONTENT

  // START COMPANY CONTENT
  if (companyInfo !== null) {
    displayIt = "none";
    if (overviewInfo === null && deviceInfo === null) { displayIt = "visible"; }
    contents +='\n<div id="infoBubble_content2" ' + 'class="infoBubbleBody" style="display:'+displayIt+ ';">' +companyInfo + '</div>';
  }
  // END COMPANY CONTENT
  // START PERSONNEL CONTENT
  if (personnelInfo !== null) {
    displayIt = "none";
    if (overviewInfo === null && deviceInfo === null && companyInfo === null) {
      displayIt = "visible";
    }
    contents +='\n<div id="infoBubble_content3" ' + 'class="infoBubbleBody" style="display:'+displayIt+ ';">' +personnelInfo + '</div>';
  }
  // END PERSONNEL CONTENT
  // START PHOTOS CONTENT
  if (photoInfo !== null) {
    displayIt = "none";
    if ((overviewInfo === null) && (deviceInfo    === null) &&
	(companyInfo  === null) && (personnelInfo === null)) {
      displayIt = "visible";
    }
    contents +='\n<div id="infoBubble_content4" ' + 'class="infoBubbleBody" style="display:'+displayIt+ ';">' +photoInfo + '</div>';
  }
  // END PHOTOS CONTENT

  contents +='\n</div>' + '\n</div>';
  // END INFO BUBBLE CONTENT
  /* END INFO BUBBLE POPUP */

  return(contents);
};

/* Returns the rectangle which encompasses the icon.  This takes into account
 * the offset from the orginal x and y due to location icons having tails and
 * even info only icons which need to be centered over the original x and y.
 * This is currently only used so that we can get the rectangle that
 * encompasses all of the icons--which is needed so that we can smart position
 * the map to show as much of the path as possible, yet making sure the
 * selected icon is visible */
InfoIcon.prototype.getRect = function() {
  var x = this.x*wayMap.scale;
  var y = this.y*wayMap.scale;
  var origX = x;
  var origY = y;
  var arrowStr;
  var iconID = this.id;
  var iconStr;
  var rect = [];
  var xOffset, yOffset;

  switch(this.direction) {
  case InfoIcon.NORTH_ARROW:
    /* this offset needs to match the offset in client.css */
    yOffset = -4; // infoIconPointerDown top
    x = origX-(InfoIcon.IMG_W/2);
    y = origY-InfoIcon.IMG_H-13+yOffset;
    rect[UIU.LEFT] = x;
    rect[UIU.TOP] = y;
    break;
  case InfoIcon.SOUTH_ARROW:
    /* this offset needs to match the offset in client.css */
    yOffset = 10; // infoIconPointerUp top
    x = origX-(InfoIcon.IMG_W/2);
    y = origY-(yOffset/2)-1 + InfoIcon.IMG_H;
    break;
  case InfoIcon.EAST_ARROW:
    xOffset = -40;
    yOffset = 12;
    x = (origX-(InfoIcon.IMG_W/2))+xOffset;
    y = origY+yOffset;
    break;
  case InfoIcon.WEST_ARROW:
    xOffset = 41;
    yOffset = 12;
    x = (origX-(InfoIcon.IMG_W/2))+xOffset;
    y = origY+yOffset;
    break;
  }
  rect[UIU.LEFT]   = x + (InfoIcon.IMG_W/2) - (this.w/2)+1;
  rect[UIU.TOP]    = y;
  rect[UIU.WIDTH]  = this.w;
  rect[UIU.HEIGHT] = this.h-InfoIcon.IMG_W/2;
  var rectDiv = '\n<div id="centerDiv" class="posAbsolute" style="width: '+rect[UIU.WIDTH]+ 'px; height: '+rect[UIU.HEIGHT]+ 'px; left: '+rect[UIU.LEFT]+ 'px; top: '+rect[UIU.TOP]+ 'px; z-index: 32; background:#f100f1; opacity:.5;"></div>';
  //$(wayMap.mapDiv).append("\n"+rectDiv);
  return(rect);
};

/* Connect the event for showing the info bubble.  This needs to call a
 * method for getting the info o fthe bubble from the server.  That functions
 * callback function will then call InfoIcon.showBubble */
InfoIcon.connectShowBubble = function(iconID) {
  UIU.assignClick(iconID, [""], InfoIcon.getBubbleInfo);
};

/* Send a reguest to the server to get data for the info bubble */
InfoIcon.getBubbleInfo = function() {
  /* Creating a var allows variables to persist to the callback */
  var bubbleClicked = this;
  if (bubbleClicked.icon.type !== InfoIcon.VERTICAL) {
    $.getJSON('../json.jsp?verb=16' + '&id='+this.icon.destID + '&ilk='+this.icon.destILK,
	      function(data) {
		if (data['msg'])  {
		  alert(data['msg']);
		} else {
		  InfoIcon.showBubble(bubbleClicked.icon, data);
		}
	      });
  } else {
    var param = "&x=" + bubbleClicked.icon.x + "&y=" + bubbleClicked.icon.y + "&FDWG=" + wayMap.fio.floorDWG + "&why=info";
    $.getJSON('../route.jsp?verb=2' + param,
	      function(data) {
		if (data['msg'])  {
		  alert(data['msg']);
		} else {
		  InfoIcon.showBubble(bubbleClicked.icon, data);
		}
	      });
  }
};

/* Called from the callback function that asks the server
 * for the info about the icon which was clicked. */
InfoIcon.showBubble = function(icon, data) {
  var contents = InfoIcon.getBubbleContents(icon, data);
  var elem = UIU.getElementOrWhinge(InfoIcon.BUBBLE_ID);
  if (elem) {
    $(elem).empty();
    // $(elem).append(contents); // this works in firefox but not IE
    elem.innerHTML = contents;	// This works in IE and in firefox
    /* Make the bubble draggable, but only by the bubble's header */
    var elemHandle = UIU.getElementOrWhinge(InfoIcon.BUBBLE_HEADER_ID);
    if (elemHandle) {
      $(elem).draggable({ handle:elemHandle });
      //, containment:wayMap.viewport
    }

  }

  /* Center bubble to viewport */
  var viewport = wayMap.getViewportRect();
  var map = wayMap.getMapDivRect();
  var x = viewport[UIU.WIDTH]/2-map[UIU.LEFT]-225;
  var y = viewport[UIU.HEIGHT]/2-map[UIU.TOP]-200;

  if (elem) {
    elem.style.left = x+ 'px';
    elem.style.top  = y+ 'px';

    $(elem).show();
  }
  InfoIcon.connectCloseBubble(InfoIcon.BUBBLE_CLOSE);
  InfoIcon.connectTabs();
  InfoIcon.connectTabSwitcher();
};

/* Connect the event for closing the info bubble */
InfoIcon.connectCloseBubble = function() {
  UIU.assignClick(InfoIcon.BUBBLE_CLOSE, [""], InfoIcon.closeBubble);
};

/* Hide the info bubble */
InfoIcon.closeBubble = function() {
  var elem = UIU.getElementOrWhinge(InfoIcon.BUBBLE_ID);
  if (elem) { $(elem).hide(); }
};

/* Show the text of the location icon */
InfoIcon.showIconLabel = function(elem) {
  $("p:first", elem).show();
  var offset = parseInt(elem.offsetWidth)/2;
  var left = parseInt(elem.style.left)+(29/2);
  offset =  left - offset;
  elem.style.left = offset+ 'px';
};

/* TODO Why is this put into document.ready and not other functions are not?
 * Sets up toggling on and off the location icon text, not currently enabled */
$(document).ready(InfoIcon.connectIconTextToggle = function(iconID) {
  var elem = document.getElementById(iconID);
  //debugger;
  $("div#"+iconID).hover(
    function() {
      $("p:first",this).show();
      var offset = parseInt(elem.offsetWidth)/2;
      var left = parseInt(elem.style.left)+(29/2);
      offset = left - offset;
      elem.style.left = offset + 'px';
    },
    function() {
      elem.style.left = $(elem).attr("origx") + 'px';
      $("p:first",this).hide();
    });
  });

/* Makes the current tab hilighted */
InfoIcon.connectTabSwitcher = function() {
    $("#infoBubbleMenu a").click(function() {
	if ($("#infoBubbleMenu a.current").length > 0) {
	  $("#infoBubbleMenu a.current").removeClass('current');
	}
	$(this).addClass('current');
      });
};

/* Connect the functionality for switching tabs in the info bubble */
InfoIcon.connectTabs = function() {
  $("a#infoBubble_tab1").click(function() {
      $("#infoBubble_content1").show();
      $("#infoBubble_content2").hide();
      $("#infoBubble_content3").hide();
      $("#infoBubble_content4").hide();
      $("#infoBubble_content5").hide();});

  $("a#infoBubble_tab2").click(function() {
      $("#infoBubble_content1").hide();
      $("#infoBubble_content2").show();
      $("#infoBubble_content3").hide();
      $("#infoBubble_content4").hide();
      $("#infoBubble_content5").hide();});

  $("a#infoBubble_tab3").click(function() {
      $("#infoBubble_content1").hide();
      $("#infoBubble_content2").hide();
      $("#infoBubble_content3").show();
      $("#infoBubble_content4").hide();
      $("#infoBubble_content5").hide();});

  $("a#infoBubble_tab4").click(function() {
      $("#infoBubble_content1").hide();
      $("#infoBubble_content2").hide();
      $("#infoBubble_content3").hide();
      $("#infoBubble_content4").show();
      $("#infoBubble_content5").hide();});

  $("a#infoBubble_tab5").click(function() {
      $("#infoBubble_content1").hide();
      $("#infoBubble_content2").hide();
      $("#infoBubble_content3").hide();
      $("#infoBubble_content4").hide();
      $("#infoBubble_content5").show();});
};

/* This method will be removed once the server properly sets the icon's
   directions. */
InfoIcon.setIconDirections = function(pathIcons) {
  if (pathIcons.length < 2) { return; }
  InfoIcon.setDefaultIconDirections(pathIcons[0], pathIcons[1]);
};

/* Set the default directions of the two icons, the icon with the largest y is
 * south and the icon with the lowest y is north */
InfoIcon.setDefaultIconDirections = function(startIcon, endIcon) {
  var directions = [];
  if (startIcon.y >= endIcon.y) {
    startIcon.direction = InfoIcon.SOUTH_ARROW;
    endIcon.direction   = InfoIcon.NORTH_ARROW;
  } else {
    startIcon.direction = InfoIcon.NORTH_ARROW;
    endIcon.direction   = InfoIcon.SOUTH_ARROW;
  }
  directions[0] = startIcon;
  directions[1] = endIcon;
  return directions;
};
