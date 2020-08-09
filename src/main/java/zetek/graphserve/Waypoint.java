/* @name Waypoint.java

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
import java.io.Serializable;
import java.text.NumberFormat;

/**
 * Store one point in a list of points which constitute a route.

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class Waypoint extends Point2D.Double implements Serializable {

  public static final long serialVersionUID = 1;

  public static NumberFormat doubleFormat = NumberFormat.getNumberInstance();
  static {
    doubleFormat.setMaximumFractionDigits(4);
    doubleFormat.setGroupingUsed(false);  // no commas in the numbers, please
  }

  public short floorOrder = -1;
  public String floorDWG;

  public void setFloorOrder(short floorOrder) {
    this.floorOrder = floorOrder;
  }

  public void setFloorDWG(String floorDWG) {
    this.floorDWG = floorDWG;
  }

  /** Obligatory constructor.*/
  public Waypoint() {
    super();
  }

  public Waypoint(double x, double y) {
    super(x, y);
  }

  public Waypoint(double x, double y, short floorOrder) {
    super(x, y);
    this.floorOrder = floorOrder;
  }

  public Waypoint(Point2D pt, short floorOrder, String floorDWG) {
    super((pt != null) ? pt.getX() : 0, (pt != null) ? pt.getY() : 0);
    this.floorOrder = floorOrder;
    this.floorDWG   = floorDWG;
  }

  public Waypoint(Point2D pt) {
    super(pt.getX(), pt.getY());
  }

  /**
   * @param obj object we are testing for equality
   * @returns true if and only if the xs and ys are equal
   */
  @Override
  public boolean equals(Object obj) {
    boolean isEqual = false;
    if (obj != null && obj instanceof Waypoint) {
      if (((Waypoint)obj).getX() == this.getX()
          && ((Waypoint)obj).getY() == this.getY()) {
        isEqual = true;
      }
    }
    return(isEqual);
  }

  /** Returns the object in JSON notation */
  public String toString() {
    return "{\"x\":" + doubleFormat.format(getX()) + ",\"y\":" + doubleFormat.format(getY()) +
    ",\"o\":\"" + floorDWG + "\"}";
  }
}
