/* @name DoorProject.java

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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;

import zetek.common.CommandArgs;
import zetek.geom.GeomReturn;
import zetek.geom.LineUtils;
import zetek.readmeta.classes.Derivation;

/**
 * Code to position the start and end points of a sub-route which
 * traverses a shape.  Shapes are linked by doors whose center points
 * define the transition from one shape to another.  The path finder
 * for traversing the interior of a shape must start and end on points
 * inside the shape.  This module takes points which may or may not be
 * in the shape and computes corresponding point which is guaranteed
 * to be inside the shape.

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class DoorProject {

  public static final long serialVersionUID = 1;

  /** Obligatory constructor.*/
  public DoorProject() { /* */ }

  public static Point2D[] projectPointsIntoShape(Graphics2D gg, Derivation d,
      Point2D[] points) {

    GeomReturn geomReturn;

    Shape    shape      = d.theShape;
    Point2D center      = d.shapeCenter;

    Line2D    side;
    // The closest shape edge to the point
    Line2D[]  nearLine  = new Line2D.Double[points.length];

    Line2D[][] sectLines= new Line2D[points.length][];

    Line2D[] sctchLines;

    // Walk the sides of the shape as straight lines only
    PathIterator sides  = shape.getPathIterator(new AffineTransform(), 0.01);

    // Output points which are inside the shape.
    Point2D[] outPoints = new Point2D.Double[points.length];
    Point2D   onEdge;
    Point2D   inShape;		// Point being placed in the shape
    Point2D   pi;		// Used to hold points[i]

    // Array for path iterator coordinates, only need 2 as this path
    // returns lines only.
    double[] coords     = new double[6];

    // Distances from shape edges to the point
    double[] distance   = new double[points.length];

    double dist;		// Scratch distance
    double firstX = 0;
    double firstY = 0;
    double nextX  = 0;
    double nextY  = 0;

    int i;

    /* Set all the distances to the maximum possible distance, a
     * rather big number.*/
    for (i=0; i<distance.length; i++ ) {
      distance[i] = Double.MAX_VALUE;
    }

    /* Points which are already in the shape do not need to be
     * processed at all.  If a point is not in the shape, it is
     * necessary to find the closest shape edge to the point.
     * HOWEVER, points on the edge of the shape are regarded as being
     * in the shape but they collide so all points must be processed.  */
    for (i=0; i<points.length; i++) {
      if (false && (points[i] != null) && (shape.contains(points[i]))) {
        outPoints[i]  = points[i];
        points[i]     = null;
      } else {
        double x = points[i].getX();
        double y = points[i].getY();
        sectLines[i]  = sctchLines = new Line2D[6];
        sctchLines[0] = new Line2D.Double(center, points[i]);
        side = new Line2D.Double(center, points[i]);
        // Double the length of the line.
        side = LineUtils.extendLine(side, side,
            dist = side.getP1().distance(side.getP2()));
        side.setLine(side.getP2(), center);
        sctchLines[1] = side;
        sctchLines[2] = new Line2D.Double(x, y, x+dist, y);
        sctchLines[3] = new Line2D.Double(x, y, x-dist, y);
        sctchLines[4] = new Line2D.Double(x, y, x, y+dist);
        sctchLines[5] = new Line2D.Double(x, y, x, y-dist);
        if (gg != null) {
          for (Line2D l : sctchLines) {
            gg.draw(l);
          }
        }
      }
    }

    /* Walk the shape iterator and find the closest approach to each
     * point.  Test each line segment of the shape for closest
     * proximity to each point.  This rather awkward method is
     * necessitated by the fact that Java regards lines as infinite.
     * The method which computes the distance betwen a line and a
     * point computes the distance to the point which is on the
     * projected line.  This does not always work for finding the
     * closest shape edge to a point. */
    while (!sides.isDone()) {
      side = null;

      switch (sides.currentSegment(coords)) {
      case PathIterator.SEG_MOVETO:
        /* Should ONLY occur at the start--rooms are always closed shapes */
        firstX = nextX = coords[0];
        firstY = nextY = coords[1];
        break;
      case PathIterator.SEG_CLOSE:
        /* Last segment should be captured here */
        side = new Line2D.Double(nextX, nextY, firstX, firstY);
        break;
      case PathIterator.SEG_LINETO:
        /* All other segments should be caught here, including curves */
        side = new Line2D.Double(nextX, nextY, coords[0], coords[1]);
        nextX = coords[0];
        nextY = coords[1];
        break;
      }

      if ((side != null)) {
        for (i=0; i<points.length; i++) {
          if ((points[i] != null) && ((sctchLines = sectLines[i]) != null)) {
            for (Line2D l2 : sctchLines) {
              geomReturn = LineUtils.linesIntersect(l2, side);
              if (geomReturn.result == GeomReturn.INTERSECT) {
                dist = geomReturn.point.distance(points[i]);
                if (dist < distance[i]) {
                  distance[i] = dist;
                  nearLine[i] = side;
                }
              }
            }
          }
        }
      }
      sides.next();
    }

    if (gg != null) {
      Color oldColor = gg.getColor();
      gg.setColor(Color.yellow);
      for (i=0; i<points.length; i++) {
        if (points[i] != null) {
          if (nearLine[i] != null) { gg.draw(nearLine[i]); }
        }
      }
      gg.setColor(oldColor);
    }

    /* The software now has an array of points which are not in the
     * shape and for each such point, it has a line which is closest
     * to the point.  Now compute where the points go in the shape.*/

    for (i=0; i<points.length; i++) {
      if ( (pi = points[i]) != null) {
        if (nearLine[i] == null) {
          outPoints[i]  = pi;	// Not a very good guess; better than death.
        } else {
          /* Starting value for the distance from the side. */
          dist = SGB.parallelDistance;
          /* Get a point on the line by dropping a perpendicular from
           * the point of interest to the line.*/
          onEdge =
            LineUtils.getPerpendicularPointOnLine(nearLine[i], pi);
          do {
            /* Happens if the point of interest is precisely ON the
             * border of the shape.  A point ON the border of a shape
             * is not IN the shape by definition.  Have to move so
             * that the point is in the shape. */
            if (onEdge.equals(pi)) {
              /* Go away from the line a ways and take the point at
               * either end of the line such that the end point is in
               * the shape. */
              inShape = LineUtils.getPerpendicularPointOffLine(nearLine[i],
                  onEdge, dist);
              if (!shape.contains(inShape)) {
                inShape = LineUtils.getPerpendicularPointOffLine(nearLine[i],
                    onEdge, 0-dist);
              }
            } else {
              /* Go into the shape a ways and take whichever end of
               * the line that falls in the shape. */
              side = new Line2D.Double(onEdge, pi);
              LineUtils.extendLine(side, side, dist);
              if (shape.contains(side.getP1())) {
                inShape = side.getP1();
              } else {
                inShape = side.getP2();
              }
            }
            dist /= 2;
            /* In narrow corners of the shape, neither point may fall
             * in the shape.  In that case, cut the distance in half
             * and start over. */
          } while(!shape.contains(inShape) && (dist > .01));

          outPoints[i] = inShape;
          outPoints[i] = shallowP(onEdge, inShape, d, nearLine[i]);
        }
      }
    }
    return outPoints;
  }

  /**
   * Adjust the point in the shape so that it is not too far in and so
   * that it does not cross any lines in case there are thin
   * extensions of the shape in the way.

   * onEdge is a point on the edge of the shape which intersects
   * nearLine[i].  inShape is the point inside the shape. There are
   * three possible situations:

   * 1) There is a collision between onEdge and inShape.  In this
   * case, the door enters into a very thin hall and the place is
   * halfway between the intersection and onEdge.

   * 2) There is a collision between onEdge and a point as far from
   * inShape as onEdge but on the other side of onEdge.  In that case,
   * inShape is too far from the wall and has to be moved to a point
   * halfway between onEdge and the new collision.

   * 3) Neither of these lines collides with any edge of the shape,
   * inShape is OK.

   * @param onEdge the point on the edge of the shape
   * @param inShape the point which may be too far into the shape
   * @param dist how far the point was moved
   * @param shape the shape
   * @param edge the line which was closest to the original point
   * @return
   */
  public static Point2D shallowP(Point2D onEdge, Point2D inShape,
      Derivation d, Line2D edge) {
    Shape shape = d.theShape;
    PathIterator sides  = shape.getPathIterator(new AffineTransform(), 0.01);
    Point2D pt = null;
    Line2D side;

    Point2D[] points    = new Point2D.Double[2];
    points[0] = onEdge;
    points[1] = inShape;

    Line2D[] mayCollide = new Line2D.Double[2];
    GeomReturn[] spot   = new GeomReturn[2];
    GeomReturn ret;
    double[] distance   = new double[2];

    int i;

    /* Set all the distances to the maximum possible distance, a
     * rather big number.*/
    for (i=0; i<distance.length; i++ ) {
      distance[i] = Double.MAX_VALUE;
    }

    // Array for path iterator coordinates, only need 2 as this path
    // returns lines only.
    double[] coords     = new double[6];

    double dist;
    double firstX = 0;
    double firstY = 0;
    double nextX  = 0;
    double nextY  = 0;

    mayCollide[0] = new Line2D.Double(onEdge,  inShape);
    mayCollide[1] = new Line2D.Double(inShape, new Point2D.Double((inShape.getX()*2) - onEdge.getX(), (inShape.getY()*2) - onEdge.getY()));

    //System.out.println(CommandArgs.stringFromLine(mayCollide[0]) + " " + CommandArgs.stringFromLine(mayCollide[1]));

    while (!sides.isDone()) {
      side = null;

      switch (sides.currentSegment(coords)) {
      case PathIterator.SEG_MOVETO:
        /* Should ONLY occur at the start--rooms are always closed shapes */
        firstX = nextX = coords[0];
        firstY = nextY = coords[1];
        break;
      case PathIterator.SEG_CLOSE:
        /* Last segment should be captured here */
        side = new Line2D.Double(nextX, nextY, firstX, firstY);
        break;
      case PathIterator.SEG_LINETO:
        /* All other segments should be caught here, including curves */
        side = new Line2D.Double(nextX, nextY, coords[0], coords[1]);
        nextX = coords[0];
        nextY = coords[1];
        break;
      }

      if ((side != null) && (!LineUtils.sameLines(side, edge))) {
        for (i=0; i<mayCollide.length; i++) {
          ret = LineUtils.linesIntersect(mayCollide[i], side);
          if (ret.result == GeomReturn.INTERSECT) {
            dist = ret.point.distance(points[i]);
            if (dist < distance[i]) {
              distance[i] = dist;
              spot[i] = ret;
            }
          }
        }
      }
      sides.next();
    }

    // If no collisions, original guess as to the point is OK
    if ((spot[0] == null) && (spot[1] == null)) {
      return inShape;
    }

    if (spot[0] != null) {	// Thin protrusion in shape, go halfway
      ret = spot[0];
    } else {			// Shape is thin, but not too thin
      ret = spot[1];
    }

    pt = new Point2D.Double(onEdge.getX()+(ret.point.getX() - onEdge.getX())/2,
        onEdge.getY()+(ret.point.getY() - onEdge.getY())/2);
    if (!shape.contains(pt)) {
      System.out.println("ERR weird shape " + d.StringRep + " oe " +
          CommandArgs.stringFromPoint(onEdge)  + " is " +
          CommandArgs.stringFromPoint(inShape) + " bad pt " +
          CommandArgs.stringFromPoint(pt) + " edge " +
          CommandArgs.stringFromLine(edge));

      return inShape;
    }
    return pt;
  }
}
