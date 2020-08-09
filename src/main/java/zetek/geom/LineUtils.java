/* @name LineUtils.java

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

package zetek.geom;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * A collection of line manipulation utilities

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class LineUtils {

  public static final long serialVersionUID = 1;

  /** Obligatory constructor.*/
  public LineUtils() { /* */ }

  /** Return the angle of a line based on drawing the line from P1 to P2.
   * @param l line
   * @return angle from P1 to P2 in radians - 0 to 2 PI
   */
  public static double lineAngle(Line2D l) {
    double hyp = l.getP1().distance(l.getP2());
    double adj = l.getP2().getX() - l.getP1().getX();
    return Math.acos(adj / hyp);
  }

  /**
   * Investigate whether two line <B>segments</b> intersect or not.  A
   * different algorithm is needed to detect where two lines would
   * intersect if either of them were to be extended as needed.
   * Algorithm documented at
   * http://local.wasp.uwa.edu.au/~pbourke/geometry/lineline2d/

   * @param l1 First line
   * @param other second line
   * @return
   */
  public static GeomReturn linesIntersect(Line2D l1, Line2D other) {

    double denom = (((other.getP2().getY() - other.getP1().getY()) *
        (l1.getP2().getX() - l1.getP1().getX())) -
        ((other.getP2().getX() - other.getP1().getX())*
            (l1.getP2().getY() - l1.getP1().getY())));

    double nume_a = (((other.getP2().getX() - other.getP1().getX())*
        (l1.getP1().getY() - other.getP1().getY())) -
        ((other.getP2().getY() - other.getP1().getY())*
            (l1.getP1().getX() - other.getP1().getX())));

    double nume_b = (((l1.getP2().getX() - l1.getP1().getX())*
        (l1.getP1().getY() - other.getP1().getY())) -
        ((l1.getP2().getY() - l1.getP1().getY())*
            (l1.getP1().getX() - other.getP1().getX())));

    if (denom == 0.0d) {
      if (nume_a == 0.0d && nume_b == 0.0d) {
        return new GeomReturn(GeomReturn.COINCIDENT);
      }
      return new GeomReturn(GeomReturn.PARALLEL);
    }

    double ua = nume_a / denom;
    double ub = nume_b / denom;

    if (ua >= 0.0d && ua <= 1.0d && ub >= 0.0d && ub <= 1.0d) {
      // Get the intersection point.
      return new GeomReturn(GeomReturn.INTERSECT,
          new Point2D.Double(l1.getP1().getX() +
              ua*(l1.getP2().getX() -
                  l1.getP1().getX()),
                  l1.getP1().getY() +
                  ua*(l1.getP2().getY() -
                      l1.getP1().getY())));
    }
    return new GeomReturn(GeomReturn.NOT_INTERSECT);
  }

  /** Destructively modify a line to make it 40% longer by extending
   * it at both ends.  This is used to mitigate lines which ought to
   * intersect but do not because one or the other is too short.
   * @param sl the line which is to be modified by being extended
   * proportionately at both ends.  The line is defined so that X1
   * &lt;= X2 and Y1 &lt;= Y2.
   * @return the modified line.
   */
  public static Line2D extendLine(Line2D sl) {
    double deltax = (sl.getX2() - sl.getX1())/5;
    double deltay = (sl.getY2() - sl.getY1())/5;
    //double pcnt5  = sl.getP1().distance(sl.getP2())/20;
    sl.setLine(sl.getX1() - (deltax), sl.getY1() - (deltay),
        sl.getX2() + (deltax), sl.getY2() + (deltay));
    return sl;
  }

  /**
   * Extend the length of a line by a specified distance.  The line is
   * extended on each end.  A zero-length line returns the same line
   * that was input.
   * @param il input line.
   * @param ol output line.  If this is null, the method creates a new
   * line and returns it.  If it is not null, this line is modified
   * and returned.  If the input line and this line are the same, the
   * input line is modified and returned which saves garbage
   * collection.
   * @param dist how much to add to the length of the line at each end.
   * @return modified line whose length is the length of the original
   * line plus dist * 2.
   */
  public static Line2D extendLine(Line2D il, Line2D ol, double dist) {
    double length = il.getP1().distance(il.getP2());

    double X1 = il.getX1();
    double Y1 = il.getY1();
    double X2 = il.getX2();
    double Y2 = il.getY2();

    if (length > 0) {
      double deltaX = ((X2 - X1) * dist)/length;
      double deltaY = ((Y2 - Y1) * dist)/length;

      X2 += deltaX;
      X1 -= deltaX;
      Y2 += deltaY;
      Y1 -= deltaY;
    }

    if (ol == null) {
      ol = new Line2D.Double(X1,Y1,X2,Y2);
    } else {
      ol.setLine(X1,Y1,X2,Y2);
    }

    return ol;
  }


  public static Line2D getParallelLineAtPoint(Graphics2D gg, Shape shape, Line2D line,
      Point2D point) {
    Point2D pointOnLine = getPerpendicularPointOnLine(line, point);
    Color originalColor = null;
    if (gg != null) { originalColor = gg.getColor(); }
    if (gg != null) { ShapeUtils.drawPoint(gg, point, 8, Color.pink); }
    double distanceFromLine = point.distance(pointOnLine);
    if (gg != null) { gg.setColor(Color.red); }

    extendLine(line, line, -2);
    Point2D parallelPoint1 = getPerpendicularPointOffLine(line, line.getP1(),
        distanceFromLine);
    Point2D parallelPoint2 = getPerpendicularPointOffLine(line, line.getP2(),
        distanceFromLine);

    if (shape != null && (!shape.contains(parallelPoint1) && !shape.contains(parallelPoint2))) {
      parallelPoint1 = getPerpendicularPointOffLine(line, line.getP1(),
          -distanceFromLine);
      parallelPoint2 = getPerpendicularPointOffLine(line, line.getP2(),
          -distanceFromLine);
    }

    Line2D parallelLine = new Line2D.Double(parallelPoint1, parallelPoint2);
    if (gg != null) {
      gg.draw(parallelLine);
      gg.setColor(originalColor);
    }
    return(parallelLine);
  }

  /**
   * Return the fraction of the lines of a rectangle which are
   * represented by the X and Y values of the point.  The method
   * reserves the right to return odd results if the point is not
   * inside the rectangle or on the boundary.
   * @param rect the rectangle
   * @param point the point
   * @return a "point" containing the X and Y fractions as its X and
   * Y.
   */
  public static Point2D xyPercents(Rectangle2D rect, Point2D point) {
    Point2D pcnt = null;

    // Normalize the X and Y coordinates to the location within the
    // rectangle.
    double xNorm = point.getX() - rect.getMinX();
    double yNorm = point.getY() - rect.getMinY();

    pcnt = new Point2D.Double((xNorm * 100)/rect.getWidth(),
        (yNorm * 100)/rect.getHeight());

    return pcnt;
  }

  /**
   * Calculate the point on line1 that when passed through
   * pointOffLine creates a line perpendicular to line1.  Line1 is
   * considered to be of infinite length for purposes of calculation;
   * therefore, the "real" line1 and the perpendicular line created
   * from pointOffLine and the calculated point on the line may very
   * well not intersect.
   *
   * @param line1 the line we want to find a perpendicular line to
   * @param pointOffLine the point that we wish our perpendicular line
   * to pass through
   * @return the point that when passed through pointOffLine creates a line
   * perpendicular to line1
   */
  public static Point2D getPerpendicularPointOnLine(Line2D line1,
      Point2D pointOffLine) {
    Point2D perpPoint = new Point2D.Double();
    /* m1 is the slope of line1 m2 is the slope of line 2 */
    Double m1 = (line1.getY1()-line1.getY2())/(line1.getX1()-line1.getX2());
    Double m2;

    double x = 0.0;
    double y = 0.0;
    if (m1 != 0) {
      m2 = -1/m1;
      /* diving by infinity results in zero in java */
      if (m2 != 0) {
        /* b1 is the y-intercept of line1 b2 is the y-intercept of line2 */
        Double b1 = line1.getY1() - (m1*line1.getX1());
        Double b2 = pointOffLine.getY()-(m2*pointOffLine.getX());
        x = ((b2-b1)/(m1-m2));
        y = ((m1*x)+b1);
      } else {
        /* line1 is vertical and the perpLine is horizontal */
        x = line1.getX1();
        y = pointOffLine.getY();
      }
    } else {
      /* line1 is horizontal and the perpLine is vertical */
      x = pointOffLine.getX();
      y = line1.getY1();
    }

    perpPoint.setLocation(x, y);
    return(perpPoint);
  }

  /**
   * Calculate the point off line1 that when passed through
   * pointOnLine creates a line perpendicular to line1.
   *
   * @param line1 the line we want to find a perpendicular line to
   * @param pointOnLine the point that we wish our perpendicular line
   * to pass through
   * @param length how long to make our perpendicular line
   * @return the point that when passed through pointOnLine creates a line
   * perpendicular to line1
   */
  public static Point2D getPerpendicularPointOffLine(Line2D line1, Point2D pointOnLine, double length) {
    Point2D perpPoint = new Point2D.Double();
    double deltaX = Math.abs(line1.getX1()-line1.getX2());
    double deltaY = Math.abs(line1.getY1()-line1.getY2());
    double x = 0.0;
    double y = 0.0;
    double ll = line1.getP1().distance(line1.getP2());

    int slopeSign = -1;

    /* Comment appears to be wrong
       If the right most points y is greater than the left most points y
       our perpendicular slope is positive, otherwise it is negative  */
    if (line1.getX2() > line1.getX1()) {
      if (line1.getY2() < line1.getY1()) {
        slopeSign = 1;
      }
    } else {
      if (line1.getY1() < line1.getY2()) {
        slopeSign = 1;
      }
    }

    if (deltaX == 0) {
      // line1 is vertical and the perpLine is horizontal
      x = pointOnLine.getX() + length;
      y = pointOnLine.getY();
    } else if (deltaY == 0) {
      // line1 is horizontal and the perpLine is vertical
      x = pointOnLine.getX();
      y = pointOnLine.getY() + length;
    } else {
      Double m1 = deltaY/deltaX;
      Double m2 =  slopeSign/m1;
      // deltaX and deltaY swap for perpendicular lines
      x = pointOnLine.getX() + ((deltaY*length)/ll);
      // two point equation of a line
      y = ((m2)*(x-pointOnLine.getX()))+pointOnLine.getY();
    }
    perpPoint.setLocation(x, y);
    return(perpPoint);
  }

  /**
   * Get the center point of the line
   *
   * @param ll the line to get the center point of
   * @return the center point of the line
   */
  public static Point2D getLineCenter(Line2D ll) {
    return(getLineCenter(ll.getX1(), ll.getY1(), ll.getX2(),ll.getY2()));
  }

  /**
   * Get the center point between the 2 supplied points.
   *
   * @param p1 point 1
   * @param p2 point 2
   * @return Point2D object representing the point in between the supplied
   * points.
   */
  public static Point2D getLineCenter(Point2D p1, Point2D p2) {
    return(getLineCenter(p1.getX(), p1.getY(), p2.getX(),p2.getY()));
  }

  /**
   * Get the center point between the 2 supplied points.
   *
   * @param x1 the x of point 1
   * @param y1 the y of point 1
   * @param x2 the x of point 2
   * @param y2 the y of point 2
   * @return Point2D object representing the point in between the supplied
   * points.
   */
  public static Point2D getLineCenter(double x1, double y1,
      double x2, double y2) {
    Point2D center = new Point2D.Double((x1+x2)/2, (y1+y2)/2);
    return(center);
  }

  /**
   * Get the point length in distance away from (x1, y1),
   * which is a point on the line (x1, y1), (x2, y2)
   *
   * @param x1 the x of point 1
   * @param y1 the y of point 1
   * @param x2 the x of point 2
   * @param y2 the y of point 2
   * @param length the distance away from (x1, y1) our new point will be
   * @return a Point2D object representing the point which we can use to make
   * out new sub-line
   */
  public static Point2D getSubLinePoint(double x1, double y1, double x2, double y2, int length) {
    Point2D point = new Point2D.Double();
    double x;
    double y;
    if (x1 < x2) {
      x = x1+length;
    } else {
      x = x1-length;
    }
    double deltaX = x1-x2;
    double deltaY = y1-y2;
    if (deltaX == 0) {
      if (y1 > y2) { length *= -1; }
      y = y1+length;
    } else if (deltaY == 0) {
      y = y1;
    } else {
      // two point equation of a line
      y = ((deltaY/deltaX)*(x-x1))+y1;
    }
    point.setLocation(x, y);
    return(point);
  }

  public static Point2D getSubLinePoint(Line2D line, int length) {
    return(getSubLinePoint(line.getX1(), line.getY1(),
        line.getX2(), line.getY2(),length));
  }

  public static Point2D getSubLinePoint(Point2D point1, Point2D point2, int length) {
    return(getSubLinePoint(point1.getX(), point1.getY(),
        point2.getX(), point2.getY(), length));
  }

  public static Point2D getSubLinePointBkwd(double x1, double y1, double x2, double y2, int length) {
    Point2D point = new Point2D.Double();
    double x;
    double y;
    if (x1 < x2) {
      x = x2-length;
    } else {
      x = x1-length;
    }

    double deltaX = x2-x1;
    double deltaY = y2-y1;

    if (deltaX == 0) {
      y = y2-length;
    } else if (deltaY == 0) {
      y = y2;
    } else {
      // two point equation of a line
      y = ((deltaY/deltaX)*(x-x1))+y1;
    }
    point.setLocation(x, y);
    return(point);
  }

  public static Point2D getSubLinePointBkwd(Point2D point1, Point2D point2, int length) {
    return(getSubLinePoint(point1.getX(), point1.getY(),
        point2.getX(), point2.getY(), length));
  }

  public static boolean sameLines(Line2D l1, Line2D l2) {
    Point2D pt = l1.getP1();
    if (pt.equals(l2.getP1())) {
      return (l1.getP2().equals(l2.getP2()));
    }
    if (pt.equals(l2.getP2())) {
      return (l1.getP2().equals(l2.getP1()));
    }
    return false;
  }
}
