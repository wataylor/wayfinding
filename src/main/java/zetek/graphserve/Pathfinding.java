/* @name Pathfinding.java

   Copyright (c) 2002-2008 Zetek Corporation (All Rights Reserved)

-------- Licensed Software Proprietary Information Notice -------------

This software is a working embodiment of certain trade secrets of
Zetek Corporation.  The software is licensed only for the
day-to-day business use of the licensee.  Use of this software for
reverse engineering, decompilation, use as a guide for the design of a
competitive product, or any other use not for day-to-day business use
is strictly prohibited.

All screens and their formats, color combinations, layouts, and
organization are proprietary to and copyrighted by Zetek Corporation.

All rights are reserved.

Authorized Zetek customer use of this software is subject to the
terms and conditions of the software license executed between Customer
and Zetek Corporation.

------------------------------------------------------------------------

*/

package zetek.graphserve;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import zetek.common.CommandArgs;
import zetek.path.PathMaker;
import zetek.readmeta.classes.Derivation;
import edu.uci.ics.jung.graph.Edge;

/**
 * Path manipulation routines which are common to both the server and
 * read meta data environments. The only one that must be available is
 * convertRoute.

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class Pathfinding {

  public static final long serialVersionUID = 1;

  /** Obligatory constructor.*/
  public Pathfinding() { /* */ }

  /**
   * Called only when two nodes are on the floor to determine how to
   * get from one node to the next.  If the next node is linked to
   * this node via a link which used to represent a door, process the
   * center point of the door and return true.
   * @param floorO current floor ID
   * @param routeNodes array of nodes in the path
   * @param currentRoute The route which is under construction
   * @param frobber if not null, maps points.*/
  private static Point2D howNext(Derivation now, Derivation next) {
    try {
      if (next.Ilk == SGB.DOOR_ILK) { // next is door, just go to it.
	return next.shapeCenter;
      }
      Edge e = now.findEdge(next);
      String ptS = (String)e.getUserDatum(SGB.NAME_OF_DOOR_CTR);
      if (ptS == null) { return next.shapeCenter; }

      return CommandArgs.pointFromString(ptS);
    } catch (Exception e) { }
    return next.shapeCenter;
  }

  /**
   * Convert an array of nodes to a path.  Each node may generate many
   * way points along the route depending on the shapes.
   * @param routeNodes ordered array of nodes containing shapes
   * @param frobber if not null, contains methods for manipulating points
   * @return route object with points either mapped or not mapped
   * depending on whether frobber is null or not null.
   */
  public static SubRoute convertRoute(Derivation[] routeNodes,
				      IPathUtils frobber) {
    List<Derivation> der = new ArrayList<Derivation>(routeNodes.length);
    for (Derivation d: routeNodes) { der.add(d); }
    return convertRoute(der, null, null, frobber);
  }

  /**
   * Vertical derivations are named by putting the name of the floor at the
   * end of the name.  This makes names unique for the purposes of the graph,
   * but makes the names too long for use.  This routine looks for the floor
   * name and strips it out.
   * @param rep original space name
   * @return space name with floor name chopped off the end
   */
  public static String deFloor(String rep) {
    int ix = rep.indexOf("_");
    if (ix < 0) { return rep; }
    return rep.substring(0, ix);
  }

  /**
   * Vertical derivations are named by putting the name of the floor at the
   * end of the name.  This makes names unique for the purposes of the graph,
   * but makes the names too long for use.  This routine looks for the floor
   * name and returns it.
   * @param rep original space name
   * @return floor name
   */
  public static String floorOf(String rep) {
    int ix = rep.indexOf("_");
    if (ix < 0) { return rep; }
    return rep.substring(ix+1, rep.length());
  }

  /**
   * Convert a list of nodes to a path.	 Each node may generate many
   * way points along the route depending on its shape.	 If a route
   * starts with a location, that specifies the starting point within
   * the shape at the beginning of the route.  If the route ends with
   * a location, the point specifies the ending point within the last
   * shape.  Otherwise shape centers are used
   * @param routeNodes ordered array of nodes containing shapes
   * @param startPt starting point within the first shape
   * @param endPoint ending point within the last shape
   * @param frobber if not null, contains methods for mapping points
   * @return route object with points either mapped or not mapped
   * depending on whether frobber is null or not null.
   */
  public static SubRoute convertRoute(List<Derivation> nodes, DestInfo fromD,
				      DestInfo toD, IPathUtils frobber) {
    SubRoute	 currentSubRoute;
    FloorSegment currentFloorSegment = null;
    Derivation d;		// Current node
    Derivation dn;		// Next node
    Derivation dnn;		// Node after the next node
    Point2D startPt  = (fromD != null) ? fromD.point : null;
    Point2D endPoint = (toD   != null) ? toD.point   : null;
    Point2D ptE;		// Entry point to current shape
    Point2D ptL;		// Exit point for leaving current shape
    String how;
    short floorO = -1;		// Floor order of current node
    int delt = 0;
    int i;
    int len;

    for (i=0; i<nodes.size(); i++) {
      while ((i < nodes.size()) && ( nodes.get(i).theShape == null)) {
	nodes.remove(i);
      }
    }

    if ( (len = nodes.size()) < 2) { return null; }

    currentSubRoute = new SubRoute();
    d = nodes.get(0);
    floorO = -1;		// Will not match first time through

    for (i = 0; i<len; i++) {
      d = nodes.get(i);
      if (currentFloorSegment == null ) {
	currentSubRoute.addFloorSegment(currentFloorSegment =
					new FloorSegment(deFloor(d.StringRep), d.floorDWG));
      }

      currentFloorSegment.endNode = deFloor(d.StringRep);

      // Find out if this node is on this floor; always fails on first node

      if (d.floorOrder != floorO) { // The node is not on the current floor
	// First shape on this floor.  Start a route segment on the
	// floor.
	floorO = d.floorOrder;
	// If this is the first shape in the path and there is a
	// starting point, use that as the way point.
	if ((i != 0) || ( (ptE = startPt) == null)) {
	  // First on this floor, enter the shape in the center
	  ptE = d.shapeCenter;
	}
	currentFloorSegment.addWaypoint(new Waypoint(ptE, floorO, d.floorDWG));
      } else {
	// Node is on the floor, but not the first on the floor
	ptE = currentFloorSegment.getLastPoint(); // Entering this shape
      }

      if (i >= len-1) {
	/* Last node in the route, leave the last shape by going to
	 * the end point specified for the route or to the shape
	 * center. */
	if ( (ptL = endPoint) == null) { ptL = d.shapeCenter;}
      } else if ( (dn = nodes.get(i+1)).floorOrder == floorO) {
	/* Next node is on this floor, find out how to leave it.  If
	 * the next node is a door and there is another node on this
	 * floor beyond the door, skip the door and use its center
	 * point as the point by which to leave this shape.
	 * Otherwise, do the standard move of leaving the current node
	 * via a virtual door between non-doors. */
	try {
	  // Causes exception if there is none
	  if (( (dnn = nodes.get(i+2)).floorOrder == floorO) &&
	      (dn.Ilk  == SGB.DOOR_ILK) && 
	      (dnn.Ilk != SGB.DOOR_ILK)) {
	    ptL = dn.shapeCenter;
	    i++;		// Skip the door, treat as virtual point
	  } else {
	  ptL = howNext(d, dn);
	  }
	} catch (Exception e) {
	  ptL = howNext(d, dn);	// Happens if there is no next of next
	}
      } else {
	// Next node is not on this floor, go to center of this node
	ptL  = d.shapeCenter;
	dn   = lookAhead(d, i, nodes);
	delt = dn.floorOrder - d.floorOrder;
	how  = /* "<li>Follow the map to " + deFloor(d.StringRep) + "</li>" +*/ "<li>Go " +
	  ((delt > 0) ? "up " : "down ") + Math.abs(delt) + " floor" +
	  ((Math.abs(delt) > 1) ? "s" : "") +
	  "</li><li>Exit at " + floorOf(dn.StringRep) + "</li>";
	currentFloorSegment.setFloorChange(new FloorChange(how, (short)delt, dn.floorDWG));
	if (delt != 0) {  // it should always be nonzero
	  i += (Math.abs(delt)-1); // skip the intervening nodes
	}
      }
      switch(SGB.shapeTraverse) {
      default:
      case 0:
	Pathfinding.traverseShape(null, currentFloorSegment, d, ptE, ptL);
	break;
      case 1:
	PathMaker.  traverseShape(null, currentFloorSegment, d, ptE, ptL,
				  SGB.smoosePaths);
	break;
      }
      if (delt != 0) { // had a vertical
	currentFloorSegment = null;
	delt = 0;
      }
    }

    // Note that Waypoint extends Point2D.Double
    if (frobber != null) {
      for (FloorSegment fs : currentSubRoute.segmentList) {
	for (Waypoint w : fs.waypointList) {
	  w.setLocation(frobber.frobnicatePoint(w));
	}
      }
    }

    currentSubRoute.specifyInDescs((fromD != null) ? fromD.inDesc :
				   "", (toD != null) ? toD.inDesc : "");
    return currentSubRoute;
  }

  /** Called to see how many levels to traverse when the node which
   * follows node d in the route is not on the same floor as node d.
   * Returns the first derivation from which the program does not
   * depart immediately or the last in the route if the route ends on the
   * a node which has no other nodes on its level.
   * @param d derivation which is not on the same floor as its
   * successor.
   * @param i index into the node list where the last node on the current floor
   * is found.	There is known to be a successor node to d.
   * @param nodes list of nodes in the path.
   */
  public static Derivation lookAhead(Derivation d, int i,
				     List<Derivation>nodes) {
    Derivation dNext = nodes.get(++i);
    Derivation dOther;
    int len = nodes.size()-1;

    while (i < len) {
      dOther = nodes.get(++i);
      if (dOther.floorOrder == dNext.floorOrder) { break; }
      dNext = dOther;
    }
    return dNext;
  }

  /**
   * Generate enough way points to traverse a shape from sPoint to
   * ePoint.  The route already knows about sPoint; it has been added
   * by either a prior call to traverseShape or by arriving at the
   * first shape on a floor.  Goal is to add way points as needed to
   * get to ePoint.
   * @param gg in the test version which runs inside a paint method, this
   * represents a graphics object which is used to display information
   * which is to be used for debugging.
   * @param route current route; already contains sPoint
   * @param d current shape to cross
   * @param sPoint Entry point into the shape.	Can be outside, near
   * the border, or in the interior of the shape depending on the
   * circumstances.
   * @param ePoint exit point from the shape.  Can be outside, near
   * the border, or in the interior of the shape depending on the
   * circumstances.
   * @return updates the route.
   */
  public static void traverseShape(Object gg, FloorSegment route, Derivation d,
				   Point2D sPoint, Point2D ePoint) {
    route.addWaypoint(new Waypoint(d.shapeCenter, d.floorOrder, d.floorDWG));
    route.addWaypoint(new Waypoint(ePoint, d.floorOrder, d.floorDWG));
  }
}
