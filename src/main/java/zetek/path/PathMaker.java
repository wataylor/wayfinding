/* @name PathMaker.java

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
package zetek.path;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;

import zetek.common.CommandArgs;
import zetek.geom.CollisionInfo;
import zetek.geom.GeomReturn;
import zetek.geom.LineUtils;
import zetek.geom.ShapeUtils;
import zetek.graphserve.DoorProject;
import zetek.graphserve.FloorSegment;
import zetek.graphserve.SGB;
import zetek.graphserve.Waypoint;
import zetek.readmeta.classes.Derivation;

public class PathMaker {

  public PathMaker() {}

  public static int HIT_SIZE = 14;
  public static final Color REMOVED_START_COLOR = Color.yellow;
  public static final Color REMOVED_END_COLOR = Color.cyan;

  public static void traverseShape(Graphics2D gg, FloorSegment route,
      Derivation d,
      Point2D pathStart, Point2D pathEnd) {
    traverseShape(gg, route, d, pathStart, pathEnd, true);
  }

  /**
   * Generate enough way points to traverse a shape from the start of the
   * path(pathStart) to the end of the path(pathEnd), without colliding into
   * the "sides" of the shape.  The start of the path has already been added
   * to the route by either a prior call to traverseShape, or by arriving
   * at the first shape on a floor.  The goal is to add way points as needed to
   * get to the end of the path(endPath).
   * @param gg graphics object on which to draw debugging information,
   * can be null
   * @param route current route from the start point to the edge of this shape;
   * already contains pathStart
   * @param d current shape to cross from pathStart to pathEnd
   * @param pathStart Entry point into the shape.  Can be outside, near
   * the border, or in the interior of the shape depending on the
   * circumstances.
   * @param pathEnd exit point from the shape.  Can be outside, near
   * the border, or in the interior of the shape depending on the
   * circumstances.  Must be the last point added to the route.
   * @param smoothIt if true smooth the inner path
   * @return updates the route.
   */
  public static void traverseShape(Graphics2D gg, FloorSegment route,
      Derivation d,
      Point2D pathStart, Point2D pathEnd,
      boolean smoothIt) {

    //System.out.println("traverseShape: " + d.StringRep + " " + d.floorDWG);

    Point2D[] pts = new Point2D[2];
    pts[0] = pathStart;
    pts[1] = pathEnd;

    FloorSegment tmpRoute = new FloorSegment("start", "Floor DWG", 8);

    /* Add the start point to the route; it is often already there,
     * but the add routine won't add it if it is already in the
     * route. */
    tmpRoute.addWaypoint(new Waypoint(pathStart, d.floorOrder, d.floorDWG));

    // Make sure the start and end points are inside the shape
    pts    = DoorProject.projectPointsIntoShape(null, d, pts);
    Point2D pickStart = pts[0];
    Point2D pickEnd   = pts[1];

    //System.out.println(" " + CommandArgs.stringFromPoint(pathStart) +
    //" " + CommandArgs.stringFromPoint(pickStart) +
    //" " + CommandArgs.stringFromPoint(pathEnd)   +
    //" " + CommandArgs.stringFromPoint(pickEnd));

    if (!d.theShape.contains(pickStart) || !d.theShape.contains(pickEnd)) {
      if (!d.theShape.contains(pickStart)) {
        //System.out.println("Start outside " + CommandArgs.stringFromPoint(pickStart));
      }
      if (!d.theShape.contains(pickEnd)) {
        //System.out.println("End outside " + CommandArgs.stringFromPoint(pickEnd));
      }
      //System.out.println("Shape " + CommandArgs.stringFromRect(d.shapeBounds.getBounds()));

      route.addWaypoint(new Waypoint(pathStart, d.floorOrder, d.floorDWG));
      route.addWaypoint(new Waypoint(d.shapeCenter, d.floorOrder, d.floorDWG));
      route.addWaypoint(new Waypoint(pathEnd, d.floorOrder, d.floorDWG));
      return;
    }

    /* Add the modified start point which is now known to be inside
     * the shape to the route if different. */
    tmpRoute.addWaypoint(new Waypoint(pts[0], d.floorOrder, d.floorDWG));

    /* Get the Waypoints within the room that allow us to navigate without
     * colliding into anything */
    int sanity = 0;
    int counter = 0;
    CollisionInfo hit = new CollisionInfo();
    Waypoint wp;
    while (sanity < 10 &&
        !pts[1].equals((hit =
          PathMaker.getNextPoint(gg, d, pts[0], pts[1],
              pickStart, pickEnd,
              hit,
              sanity, counter)).getMovedPoint())) {
      if ((hit == null) || (hit.getMovedPoint() == null)) { break; }
      tmpRoute.addWaypoint(wp = hit.getMovedPoint());
      wp.setFloorDWG(d.floorDWG);
      wp.setFloorOrder(d.floorOrder);
      pickStart = hit.getMovedPoint();
      sanity++;
    }

    /* Add the end points */
    tmpRoute.addWaypoint(new Waypoint(pickEnd, d.floorOrder, d.floorDWG));
    // Add modified path end
    tmpRoute.addWaypoint(new Waypoint(pts[1], d.floorOrder, d.floorDWG));
    // Add original path end
    tmpRoute.addWaypoint(new Waypoint(pathEnd, d.floorOrder, d.floorDWG));

    if (smoothIt) { smoothRoute(tmpRoute, d.theShape); }
    route.addWaypoints(tmpRoute.waypointList);
  }

  public static void smoothRoute(FloorSegment route, Shape shape) {
    ArrayList<Line2D> sides = ShapeUtils.getSides(shape);
    Line2D line;
    GeomReturn geomReturn;
    int i = 0;
    while(i < route.waypointList.size()-2) {
      line = new Line2D.Double(route.waypointList.get(i),
          route.waypointList.get(i+2));
      boolean removeMiddle = true;
      for (Line2D side : sides) {
        geomReturn = LineUtils.linesIntersect(line, side);
        if (geomReturn.result == GeomReturn.INTERSECT) {
          removeMiddle = false;
          break;
        }
      }
      if (removeMiddle) {
        route.waypointList.remove(i+1);
      } else {
        i++;
      }
    }
  }

  /**
   * Determine the first valid collision(hit) and return a new way point, an
   * "avoidance point," which tries to avoid that collision.  Backwards
   * collisions may be created when we try to add an avoidance point.  We
   * remove "back collisions" by calling this method recursively on
   * each new segment we create.
   *
   * @param gg graphics context to draw for debugging, may be null for
   * production.
   * @param d shape to be traversed.  Could just pass the shape; doesn't use
   * any other attributes
   * @param pathStart the very start point of the path; when iterating through
   * the shape, this is the place where the path enters the shape
   * @param pathEnd the very last point of the path with respect to the shape
   * @param pickStart point returned by the previous call to getNextPoint.
   * It is the start of the line used to test for intersections against the
   * shape.
   * @param pickEnd always the same as pathEnd; it may change, but probably
   * not.
   * @return next point which is on the path across the shape.
   */
  public static CollisionInfo getNextPoint(Graphics2D gg, Derivation d,
      Point2D pathStart, Point2D pathEnd,
      Point2D pickStart, Point2D pickEnd,
      CollisionInfo previousHit,
      int counter1, int counter2) {

    double parallelDistance = SGB.parallelDistance;
    double perpDistance = SGB.perpDistance;
    CollisionInfo hit = null;

    /* Get the collisions, sort them, and filter out any garbage.  A
     * collision with the start wall or the end wall is invalid.  A
     * collision with a wall is valid unless the line goes outside the
     * shape and comes back in.  */
    ArrayList<CollisionInfo> hits =
      getCollisions(gg, d.theShape, pathStart, pathEnd,
          pickStart, pickEnd);
    Collections.sort(hits);

    /* I think this is incorrect when we are recursing it seems likely that
     * we will have not hits and want to return what we were not the end. */
    if (!(hits.size() > 0) || ((hit = hits.get(0)) == null)) {
      previousHit.setMovedPoint(new Waypoint(pickEnd));
      return(previousHit);
    }

    // Move move different if infinite loop
    if (previousHit != null) {
      if (previousHit.getPointHit() != null) {
        if (previousHit.getPointHit().equals(hit.getPointHit())) {
          //System.out.println("previousHit equals (hit.getPointHit())");
          parallelDistance = SGB.parallelDistanceChange;
          if (counter1 > 5) { // Bill, what should this value be?
            //System.out.println("getNextPoint : counter > 5");
            previousHit.setMovedPoint(new Waypoint(pickEnd));
            return(previousHit);
          }
        }
      }
    }

    if (gg != null) {
      gg.setColor(Color.blue);
      gg.draw(hit.getPick());
      Point2D lc = LineUtils.getLineCenter(hit.getPick());
      ShapeUtils.drawTextPoint(gg, lc, 2, Color.red, ""+ counter1);
      ShapeUtils.drawPoint(gg, hit.getPointHit(), HIT_SIZE, Color.red);
    }

    /* If we collide, move away from the line collided with
     * perpendicularly but stay inside the shape. */
    Point2D newPoint;
    Point2D perpPoint = getClosestPerpPoint(hit, hit.getPointHit(),
        d.theShape, perpDistance);
    if (perpPoint == null) {
      /* We were unable to get a perpPoint within the shape so set nextPoint to
       * the point collided with so that our next calculation starts from
       * there. */
      newPoint = hit.getPointHit();
    } else { newPoint = perpPoint; }

    /* Extend the point collided at beyond the end of the line it is on to
     * whichever side is within the shape and closest to the end point. */
    Point2D extendedPoint = getPointExtendedBeyondLineHit(gg, d.theShape, hit,
        newPoint, hits, parallelDistance);

    /* We can't find a point to move to inside the shape so just bail and at
     * some point do something better */
    if (perpPoint == null && extendedPoint == null) {
      //System.out.println("getNextPoint : perpPoint == null && extendedPoint == null");
      previousHit.setMovedPoint(new Waypoint(pickEnd));
      return(previousHit);
    }

    if (extendedPoint != null) {
      hit.setMovedPoint(new Waypoint(extendedPoint));
    } else {
      hit.setMovedPoint(new Waypoint(newPoint));
    }

    /* Added the check against counter1 to avoid StackOverflowError */
    if (hit.getPointHit() != null && counter1 < 10) {
      // Check for back collisions
      Point2D checkingEnd = hit.getMovedPoint();
      counter1++;
      counter2++;
      hit = getNextPoint(gg, d, ((previousHit == null) ? pathStart : previousHit.getPointHit()), pathEnd,
          pickStart, checkingEnd, hit, counter1, -1);
      if (hit == null) {
        hit = new CollisionInfo();
        hit.setMovedPoint(new Waypoint(checkingEnd));
      }
    } else {
      hit.setMovedPoint(new Waypoint(pickEnd));
    }

    //    /* If y has moved down move it down 10 more */
    //    if (hit.getMovedPoint().getY() > hit.getPointHit().getY()) {
    //      hit.setMovedPoint(new Waypoint(hit.getMovedPoint().getX(),hit.getMovedPoint().getY()));
    //    }
    return(hit);
  }

  /**
   * Get a list of intersections between the collisions of the supplied shape
   * and line.  This obtains a list of collisions between the supplied pick and
   * shape and sorts them by the natural sort order of a CollisionInfo object.
   *
   * @param gg Graphics2D object which allows us to paint to so that we can
   * visual debug the results
   * @param shape the shape we are trying to traverse, or more generically the
   * shape we wish to test intersections against the supplied pick with
   * @param pathStart start point of the path we are testing for collision
   * against.
   * @param pathEnd end point of the path we are testing for collision against
   * @param pickStart start point of the pick we use to test for collision with
   * @return ArrayList of CollisionInfo objects
   */
  public static ArrayList<CollisionInfo>
  getCollisions(Graphics2D gg, Shape shape, Point2D pathStart, Point2D pathEnd,
      Point2D pickStart, Point2D pickEnd) {
    ArrayList<CollisionInfo> hits = new ArrayList<CollisionInfo>();

    /* Test each side, segment, of the shape for collision with the pick */
    ArrayList<Line2D> sides = ShapeUtils.getSides(shape);
    Line2D pick = new Line2D.Double(pickStart, pickEnd);
    for (Line2D side : sides) {
      if ((side != null)) {
        GeomReturn geomReturn = LineUtils.linesIntersect(pick, side);
        if (geomReturn.result == GeomReturn.INTERSECT) {
          CollisionInfo cInfo =
            new CollisionInfo(geomReturn.point, side, pathStart, pathEnd,
                pickStart, pickEnd, null);
          hits.add(cInfo);
        }
      }
    }
    return(hits);
  }

  public static ArrayList<CollisionInfo>
  getWideCollisions(Graphics2D gg, Shape shape, Point2D pathStart, Point2D pathEnd,
      Point2D pickStart, Point2D pickEnd, int counter, double width) {
    ArrayList<CollisionInfo> hits = new ArrayList<CollisionInfo>();

    Line2D pick = new Line2D.Double(pickStart, pickEnd);
    Point2D perpPoint = LineUtils.getPerpendicularPointOffLine(pick, pickStart, width/2);
    Line2D pick1 = LineUtils.getParallelLineAtPoint(gg, shape, pick, perpPoint);
    perpPoint = LineUtils.getPerpendicularPointOffLine(pick, pickStart, -width/2);
    Line2D pick2 = LineUtils.getParallelLineAtPoint(gg, shape, pick, perpPoint);

    /* Test each side, segment, of the shape for collision with the pick */
    ArrayList<Line2D> sides = ShapeUtils.getSides(shape);
    for (Line2D side : sides) {
      if ((side != null)) {
        GeomReturn info1 = LineUtils.linesIntersect(pick1, side);
        if (info1.result == GeomReturn.INTERSECT) {
          CollisionInfo cInfo1;
          cInfo1 = new CollisionInfo(info1.point, side, pathStart, pathEnd,
              pick1.getP1(), pick1.getP2(), null);
          hits.add(cInfo1);
        }
        GeomReturn info2 = LineUtils.linesIntersect(pick2, side);
        if (info2.result == GeomReturn.INTERSECT) {
          CollisionInfo cInfo2;
          cInfo2 = new CollisionInfo(info1.point, side, pathStart, pathEnd,
              pick2.getP1(), pick2.getP2(), null);
          hits.add(cInfo2);
        }
      }
    }
    return(hits);
  }

  /**
   * Get the perpPoint from the point on the line we collided with that when
   * connected with the point collided with makes a line perpendicular to the
   * line collided with.  The point must be inside the shape or we return null.
   * This also ensures we get the closest point to the end point.
   *
   * @param hit CollisionInfo object that describes the line we collided with
   * @param shape the shape we are traversing
   * @return null if it was not able to get a perpendicular point within the
   * shape
   */
  public static Point2D getClosestPerpPoint(CollisionInfo hit,
      Point2D initialPoint, Shape shape,
      double distance) {
    Point2D newPoint = null;
    Point2D waypoint = null;
    if (hit != null) {
      Point2D[] points = new Point2D[2];
      int trys = 0;
      while ( (points[0] == null && points[1] == null) && trys < 4) {
        points = getPerpPoints(hit, initialPoint, shape, distance);
        distance = distance/2;
        trys++;
      }

      if (points[0] != null && shape.contains(points[0]) &&
          points[1] != null && shape.contains(points[1])) {
        double distance1 = points[0].distance(hit.getPathEnd());
        double distance2 = points[1].distance(hit.getPathEnd());
        if (distance1 < distance2) {
          newPoint = points[0];
        } else {
          newPoint = points[1];
        }
      } else if (points[0] != null && shape.contains(points[0])){
        newPoint = points[0];
      } else if (points[1] != null && shape.contains(points[1])) {
        newPoint = points[1];
      }
      if (newPoint != null) {
        waypoint = new Waypoint(newPoint);
      } else {

      }
    }
    return(waypoint);
  }

  public static Point2D[] getPerpPoints(CollisionInfo hit,
      Point2D initialPoint, Shape shape, double distance) {
    Point2D[] points = new Point2D[2];
    points[0] =
      LineUtils.getPerpendicularPointOffLine(hit.getLineHit(), initialPoint,
          distance);
    points[1] =
      LineUtils.getPerpendicularPointOffLine(hit.getLineHit(), initialPoint,
          -distance);
    if (!shape.contains(points[0])) { points[0] = null; }
    if (!shape.contains(points[1])) { points[1] = null; }
    return(points);
  }

  /**
   * Extend the point collided at beyond the end of the line hit to
   * whichever side is within the shape and closest to the end point.</p>
   *
   * If we pass in some point other than the point of collision as the initial
   * point, we create a "mirror" of the line hit that is parallel to the line
   * hit, but which passes through the initial point.  We then extend from
   * this "mirror" line instead of the line collided with.
   *
   * @param shape
   * @param cInfo
   * @param initialPoint this should either be the point collided with or some
   * pre-calculated move of the point collided with; For instance, we might pass
   * the result of getClosestPerpPoint to this function.
   * call
   * @param hits
   * @return
   */
  public static Point2D
  getPointExtendedBeyondLineHit(Graphics2D gg, Shape shape, CollisionInfo cInfo,
      Point2D initialPoint,
      ArrayList<CollisionInfo> hits, double distance) {
    Point2D newPoint = null;

    Point2D[] points = new Point2D[2];
    int trys = 0;
    while ( (points[0] == null && points[1] == null) && trys < 4) {
      points =
        getExtendedPoints(gg, shape, cInfo, initialPoint, hits, distance);
      distance = distance/3;
      trys++;
    }

    if (points[0] != null && points[1] != null) {
      /* Both points of the extended line are valid.  Choose the point closest
       * to the path's end point */
      double distance1 = points[0].distance(cInfo.getPickEnd());
      double distance2 = points[1].distance(cInfo.getPickEnd());
      if (distance1 < distance2) {
        newPoint = points[0];
      } else {
        newPoint = points[1];
      }
    } else if (points[0] != null) {
      /* point1 is the only valid point so return it */
      newPoint = points[0];
    } else if (points[1] != null) {
      /* point2 is the only valid point so return it */
      newPoint = points[1];
    }
    if (newPoint == null) {
      System.out.println("newPoint null getPointExtendedBeyondLineHit");
    }
    return(newPoint);
  }

  public static Point2D[] getExtendedPoints(Graphics2D gg, Shape shape,
      CollisionInfo hit, Point2D initialPoint, ArrayList<CollisionInfo> hits,
      double distance) {
    Line2D lineToExtend = null;
    Point2D[] points = new Point2D[2];
    if (initialPoint != hit.getPointHit()) {
      /* A point other than the point of collision was passed in, so create a
       * parallel mirror of the line hit, which passes through the point passed
       * in and extend that */
      lineToExtend =
        LineUtils.getParallelLineAtPoint(gg, shape, hit.getLineHit(),
            initialPoint);
    } else {
      /* The point of collision was passed in so just extend the line hit */
      lineToExtend = hit.getLineHit();
    }
    Line2D extendedLine = new Line2D.Double();
    LineUtils.extendLine(lineToExtend, extendedLine, distance);

    if (extendedLine != null) {
      points[0] = extendedLine.getP1();
      points[1] = extendedLine.getP2();
      //double d1 = hit.getPathEnd().distance(points[0]);
      //double d2 = hit.getPathEnd().distance(points[1]);
      //point = ((d1 > d2) ? points[0] : points[1]);

      if (!shape.contains(points[0])) { points[0] = null; }
      if (!shape.contains(points[1])) { points[1] = null; }
    }
    return(points);
  }

  /**
   * Test to see if a corner points in towards to room and not outside
   * NOT IMPLEMENTED - ALWAYS returns true
   *
   * @param shape the room
   * @param cInfo information about a collision
   * @return true if this is an inside corner
   */
  public boolean isInsideCorner(Shape shape, CollisionInfo cInfo) {
    boolean isInside = true;

    return(isInside);
  }
}
