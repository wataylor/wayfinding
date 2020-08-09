/* Dojo Path */
function DojoYBR(surface, zoomLevel) {
  /* Dojo Group */
  var g = surface.createGroup();

  /* Dash(dot or arrow) and line widths */
  var dw = FloorSegment.getArrowWidth(zoomLevel);
  var lw = FloorSegment.getPathWidth(zoomLevel);

  ///* Path Drop Shadow */
  //this.pathShadow = g.createPath().setStroke({color: "#999900", width: lw});

  /* Yellow Path */
  this.path = g.createPath().setStroke({color: "yellow", width: lw});

  /* Red Dashes */
  this.pathArrow =
    g.createPath().setStroke({color: "red",width: dw, style:"Dash"});
}
