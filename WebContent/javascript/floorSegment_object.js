/* FloorSegment */
function FloorSegment(data) {
  /* The json route segment data, set by setData */
  this.data = data;
};

/* Used to load a hard-coded test route */
//FloorSegment.prototype.getTestData = function() {
//  $.getJSON('../route.json', function(data) {
//      rtSeg.setData(data);
//    });
//}

/* Sets the data to the json data returned when selecting a route */
FloorSegment.prototype.setData = function(data) {
  this.data = data;
};

/* Draw the route if data are defined */
FloorSegment.prototype.drawRoute = function(dYBR) {
  if (!this.data) {
    alert("Internal error - route data undefined");
    return;
  }
  var start     = this.data['st'];
  var end       = this.data['en'];
  var wayPoints = this.data['wp'];
  // console.log("Drawing route..." + start + " " + end);

  var onFloor = false;
  for (i=0; i < wayPoints.length; i++) {
    var wpsFloor = wayPoints[i]['o'];
    var mapFloor = wayMap.fio.floorDWG;
    if (wpsFloor == mapFloor) {
      var x = (wayPoints[i]['x'] * wayMap.scale);
      var y = (wayPoints[i]['y'] * wayMap.scale);
      //console.log("scale="+wayMap.scale
      //+" wayPoints["+i+"]['x']="+wayPoints[i]['x']
      //+" wayPoints["+i+"]['y']="+wayPoints[i]['y']+" x="+x+" y="+y);
      //console.log("x="+x+" y="+y);

      if (onFloor) {
	dYBR.pathArrow.lineTo(x, y);
	//dYBR.pathShadow.lineTo(x-1, y+1);
	dYBR.path.lineTo(x, y);
      } else {
	dYBR.pathArrow.moveTo(x, y);
	//dYBR.pathShadow.moveTo(x-1, y+1);
	dYBR.path.moveTo(x, y);
	onFloor = true;
      }
    } else {
      onFloor = false;
    }
  }
};

/* Return hardcoded values for the width of "red arrows" based on the zoom
 * level passed in */
FloorSegment.getArrowWidth = function(zoomLevel) {
  switch(zoomLevel) {
  case 1:
    return(1);
  case 2:
    return(2);
  case 3:
    return(4);
  case 4:
    return(5);
  case 5:
    return(6);
  default:
    return(1);
  }
};

/* Return hardcoded values for the width of yellow path based on the zoom
 * level passed in */
FloorSegment.getPathWidth = function(zoomLevel) {

  switch(zoomLevel) {
  case 1:
    return(6);
  case 2:
    return(8);
  case 3:
    return(12);
  case 4:
    return(14);
  case 5:
    return(24);
  default:
    return(6);
  }
};

