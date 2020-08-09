/* @name GeomReturn.java

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

import java.awt.geom.Point2D;
import java.text.NumberFormat;

/**
 * Class used to return information from the various geometry
 * utilities.  Most such utilities have to be able to return various
 * bits of information in addition to points and other geometric
 * objects.

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class GeomReturn {

  public static final long serialVersionUID = 1;

  public static NumberFormat doubleFormat = NumberFormat.getNumberInstance();
  static {
    doubleFormat.setMaximumFractionDigits(2);
  }

  public static final short PARALLEL      = 2;
  public static final short COINCIDENT    = 1;
  public static final short NOT_INTERSECT = 0;
  public static final short INTERSECT     = 3;

  public static final String[] DESCRIPTIONS = {
    "Not Intersect", "Coincident", "Parallel", "Intersect",
  };

  public short  result;
  public Point2D point;

  /** Obligatory constructor.*/
  public GeomReturn() { /* */ }

  public GeomReturn(short result) {
    this.result = result;
  }

  public GeomReturn(short result, Point2D point) {
    this.result = result;
    this.point  = point;
  }

  public String toString() {
    return DESCRIPTIONS[result] + ((point != null) ? " [" + doubleFormat.format(point.getX()) + ", " + doubleFormat.format(point.getY()) + "]" : "");
  }
}
