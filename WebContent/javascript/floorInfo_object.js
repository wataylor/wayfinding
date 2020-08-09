/* This object contains information needed to display a contiguous set
 * of paths on a floor.  setMapTiles receives one of these whenever a)
 * a destination changes or is added; this affects the overall display
 * or b) a user clicks on a part of a floor info box which is
 * associated with this floor.

 * There are USUALLY two path icons and one floor segment in this
 * object.  The only time there would be more would be when two
 * contiguous destinations fall on the same floor.  In that case, all
 * destinations on the same floor and all related icons are contained
 * in this object.

 * The info icons are independent of the path and are sent from the
 * server based on the floor and the user's characteristics and
 * permissions. */

function FloorInfo (floorDWG, floorElemID) {
  /* The ID of the element which displays this floor*/
  this.divID = floorElemID;
  /* Icons which mark interesting points along the path; specifically,
   * the start and end points of floor segments.  */
  this.pathIcons = new Array();
  /* Icons representing information on the floor whci is not related
   * to the path such as alarms, fire extinguishers, standpipes, etc.
   * These icons are not related to the path.*/
  this.infoIcons = new Array();
  /* Pieces of yellow brick road to represent paths between path
   * icons.*/
  this.floorSegments = new Array();
  /* Gives the number of the icon within the pathIcons array which the
   * user clicked when highlighting the floor info box in the info
   * pane.  The tiles are scrolled so that the selected icon is
   * visible.*/
  this.selectedIcon = undefined;
  /* The floor drawing name determines which set of map tiles to use to
   * display the paths on this floor.*/
  this.floorDWG  = floorDWG;
  /* Parameters relating to current zoom, etc.  Each floor / map
   * combination has a zoom level and pan variables which change as
   * the user pans and scrolls the display while this block of floor
   * information is selected.  Thus, when the user selects this floor
   * again, the zoom level is retained.  Pan is adjusted as needed to
   * ensure that the selected path icon is visible.  */
  this.zoomLevel = WayMap.DEFAULT_ZOOM_LEVEL;
  this.xPan = 0;
  this.yPan = 0;
  /* Array to hold integers to remember the current directions of all
   * icons in case a specific set of directions turns out to minimize
   * the overlap.*/
  this.iconCompasses = undefined;
  /* Flag which, when set to true, means that the icon compasses have
   * been boxed to minimize overlap.  It also implies that all icon
   * compasses have been set to point in the appropriate direction.*/
  this.iconsBoxed = undefined;
};

/* http://docs.jquery.com/Selectors is extremely helpful documentation
 * on jQuery selectors. */
FloorInfo.prototype.select = function() {
  var elem;
  elem = UIU.getElementOrWhinge(this.divID + "header");
  UIU.swapClasses(elem, "bgSelectedHeader", "bgNotSelected");
  UIU.swapClasses(elem, "mapInfoheaderBorderOn", "mapInfoheaderBorderOff");
  elem = UIU.getElementOrWhinge(this.divID + "content");
  UIU.swapClasses(elem, "bgSelectedBody", "bgNotSelected");
  $("#" + this.divID + " hr").removeClass("dashedRuleOff").addClass("dashedRuleOn");
};

FloorInfo.prototype.dim = function() {
  var elem;
  elem = UIU.getElementOrWhinge(this.divID + "header");
  UIU.swapClasses(elem, "bgNotSelected", "bgSelectedHeader");
  UIU.swapClasses(elem, "mapInfoheaderBorderOff", "mapInfoheaderBorderOn");
  elem = UIU.getElementOrWhinge(this.divID + "content");
  UIU.swapClasses(elem, "bgNotSelected", "bgSelectedBody");
  $("#" + this.divID + " hr").removeClass("dashedRuleOn").addClass("dashedRuleOff");
  $("#" + this.divID + " [id$='name']").css("font-weight", "normal");
};

// /*
// Loop through the various directional possibilities to determine the
// minimum overlap for an array of icons.  We may add more arrow
// directions over time so we can't build anything fancy into the loop.

// What the icons do is called "boxing the compass" from the days of
// wooden ships and iron men.  The simplext compass box is North, East,
// South, West.  The next layer is North, North East, East, South East,
// South, South West, West, North West, North.  It is to be hoped that
// the directions do not get any fancier than that, but they might.  The
// next most complex compass box is North, North North East, North East,
// East North East, East, and so on.  Each layer doubles the number of
// points in the box.

// infoIcons need these methods:

// pointNorth = no return, sets the icon pointing North, not needed if
// the constructor initializes the icon to point north which I think it
// does.

// setCompass(direction) sets the icon pointing in the specified
// direction.

// nextBox, moves to the next slot in the compass box for this icon,
// returns true if the result of boxing is to point north again, false
// otherwise.

// getCurrentDirection - returns the direction the icon is pointing.

// getCurrentRect - returns the rectangle representing the current icon
// based on the current direction in which it is pointing.  the icon need
// not compute all of these until they are needed, but it should have an
// array to cache them just in case they are needed again.

// These gyrations are done only the FIRST time a floor is displayed.
// */

// FloorInfo.prototype.boxIcons = function() {
//   if (this.iconsBoxed) { return; }

//   var i, icon, best, overlap;
//   var len = this.infoIcons.length;
//   var continuing = true;

//   this.iconCompasses = new Array(len);

//   /* Start all the icons pointing North.  The icons know what value to
//    * use for north; the floor object doesn't need to know what means
//    * what.  This can be skipped if it is assumed that icons are
//    * constructed with a northerly bent.*/
//   for (i=0; i<len; i++) {
//     icon = this.infoIcons[i];
//     icon.pointNorth();
//     /* We don't need this statement because the
//      * first overlap will be < the big number
//      this.iconCompasses[i] = icon.getCurrentDirection();
//     */
//   }

//   best = Number.MAX_VALUE; // set best to a big number

//   do {
//     // compute overlap; ask each icon for its current rectangle.

//     /* If the total overlap is smaller than the current best, record
//      * the new best and the icon compass settings.  This should happen
//      * the very first time through because the initial best overlap is
//      * LARGE. */
//     if (overlap < best) {
//       overlap = best;
//       for (i=0; i<len; i++) {
// 	this.iconCompasses[i] = this.infoIcons[i].getCurrentDirection();
//       }
//     }

//     /* No overlap, grab it and run! */
//     if (overlap <= 0) { break; }

//     /* Finite overlap, bump the compass needles.  Treats the icon
//      * array as an odometer - if an icon rolls over back to north,
//      * bump the next icon.  Break if an icon does not roll over.  Keep
//      * bumping as long as an icon rolls back to north, eventually they
//      * all bump and the loop finishes.  The extra statement is to have
//      * a place to set a break point.  */

//     for (i=0; i<len; i++) {
//       icon = this.infoIcons[i];
//       if (!icon.nextBox()) { break; } // Move one further in the compass box
//     }

//     /* If all the icons were bumped and hit north, all combinations
//      * have been tested and the loop completed without a break.  All
//      * north was the first combination to be tested, so it need not be
//      * checked again.  */
//     if (i >= len) { continuing = false; }

//   } while (continuing);

//   /* Now set all the icons to the best configuration.*/
//   for (i=0; i<len; i++) {
//     this.infoIcons[i].setCompass(this.iconCompasses[i]);
//   }

//   this.iconsBoxed = true;	// let's not pass this way again
// };
