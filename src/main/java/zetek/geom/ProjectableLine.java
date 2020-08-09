/* @name ProjectableLine.java

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

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * A class which defines a line and is able to compute projections of
 * an X, Y point along it.  Projection runs from the point X1, Y1, to
 * the point X2, Y2.  It is up to the caller to define X and Y in the
 * appropriate order.  The class reserves the right to return unexpected
 * results if the percents are negative or out of range.<p>

 * <P>The class avoids division in order to be able to handle the case
 * of vertical and horizontal lines.

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class ProjectableLine extends Line2D.Double {

  public static final long serialVersionUID = 1;

  public double deltax;
  public double deltay;

  /** Obligatory constructor.*/
  public ProjectableLine() { /* */ }

  /**
   * Construct the line
   * @param x1 starting x
   * @param y1 starting y
   * @param x2 ending x
   * @param y2 ending y
   */
  public ProjectableLine(double x1, double y1, double x2, double y2) {
    super(x1, y1, x2, y2);

    deltax = x2 - x1;
    deltay = y2 - y1;
  }

  /**
   * Return a point specifying where X and Y would fall if the point is
   * projected along the line for a specified percentage of the length of
   * the line from P1 to P2.
   * @param pcnt specifies the percent along the length of the line
   * @return the point where X and Y would fall
   */
  public Point2D projectPercent(double pcnt) {
    return new Point2D.Double(x1 + (deltax * pcnt)/100,
                              y1 + (deltay * pcnt)/100);
  }

  public String toString() {
    return "[" + x1 + "," + y1 + "][" + x2 + "," + y2 + "]";
  }
}
