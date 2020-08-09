/* @name IPathUtils.java

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

import java.awt.Shape;
import java.awt.geom.Point2D;

/**
 *
 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public interface IPathUtils {

  public static final long serialVersionUID = 1;

  /** Convert a point in axonometric coordinate space to a point in
   * .dwg space.  Referses frobnicatePoint.  */
  public abstract Point2D unFrobnicatePoint(Point2D pt);
  /** Convert a point in .dwg space to a point in axonometric coordinate
   * space.*/
  public abstract Point2D frobnicatePoint(Point2D pt);
  /** Convert a shape in .dwg space to a shape in axonometric coordinate
   * space.*/
  public abstract Shape   frobnicateShape(Shape shape);
}
