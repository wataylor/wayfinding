// // Helper function overlap called with 2 rectangles - determines how badly they
// // overlap.  The number it returns is the area of the overlap and will include
// // path overlap as well.

// // Assume both arrows start out pointing south(better yet make the one with the
// // greater y point South and the lesser y point north to begin).  
// // If they do not overlap, we are done.

// // If not, we need NSEW rectanges for start and end and seek to minimize overlap.

// // First, make rectStart for a south-pointing rectangle for start and
// // rectEnd for the end.

// NORTH_ARROW = 0
// SOUTH_ARROW = 1
// EAST_ARROW  = 2
// WEST_ARROW  = 3
// var directions = new Array();
// directions[0] = NORTH_ARROW;
// directions[1] = SOUTH_ARROW;
// directions[2] = EAST_ARROW;
// directions[3] = WEST_ARROW;
// var bestStartDirection,
// var bestEndDirection;
// // Need to rework info icon
// var startIcon = new InfoIcon();
// var endIcon = new InfoIcon();

// var rects = getDefaultIconRects(startIcon, endIcon);
// var rectStart = rects[0];
// var rectEnd   = rects[1];

// if (overlap(rectStart, rectEnd) <= 0) {
//   bestStartDirection = rectStart.direction;
//   bestEndDirection = rectEnd.direction;
// } else {
//   var startRects = getRectsForAllIconDirections(startIcon)
//   var endRects = getRectsForAllIconDirections(endIcon);
//   var best = 100000;//whatever a very very big number is;
//   var now;

//   var startDirection;
//   var endDirection;

//   for (startDirection=0; startDirection<directions.length; startDirection++) {
//     for (endDirection=0; endDirection<directions.length, endDirection++) {
//       now = overlap(startRects(startDirection), endRects(endDirection));
//       if (best > now) {
// 	bestStartDirection = startDirection;
// 	bestEndDirection = endDirection;
// 	best = now;
//       }
//     }
//   }
// }

// overLap(rect1, rect2) {
//   //TODO return % of overlap
// }

// getRectsForAllIconDirections(icon) {
//   var rects = new Array();
//   for (int i = 0; i < directions.length; i++) {
//     rects[i] = new IconRect(icon, directions[i]);
//   }
// }

// getIconRect(icon, direction) {
//   return(new IconRect(icon, direction))
// }

// // Get the rects of the two icons, the highest y's rect is south and the lowest
// // north
// getDefaultIconRects(startIcon, endIcon) {
//   var rects = new Array();
//   if (startIcon.y >= endIcon.y) {
//     rects[0] = new IconRect(startIcon, SOUTH_ARROW);
//     rects[1] = new IconRect(endIcon, NORTH_ARROW);
//   } else {
//     rects[0] = new IconRect(startIcon, NORTH_ARROW);
//     rects[1] = new IconRect(endIcon, SOUTH_ARROW);
//   }
//   return rects;
// }

// function IconRect(icon, direction) {
//   this.icon = icon;
//   this.direction = direction;
//   this.x = icon.x;
//   this.y = icon.y;
//   this.w = icon.w;
//   this.h = icon.h;
// }

// // bestI and bestJ now give the directions the destinations should
// // point for minimum overlap.
