/* @name ShapeUtils.java

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

package zetek.test.geom;

import java.awt.Shape;
import java.awt.geom.PathIterator;
import java.text.NumberFormat;

/**
 * Utilities for print shapes and doing other things with them
 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class ShapeUtils {

  public static final long serialVersionUID = 1;

  /** Obligatory constructor.*/
  public ShapeUtils() { /* */ }

  public static NumberFormat doubleFormat = NumberFormat.getNumberInstance();
  static {
    doubleFormat.setMaximumFractionDigits(2);
  }

  public static void clearCoords(double[] coords) {
    for (int i = 0; i < coords.length; i++) {
      coords[i] = 0;
    }
  }

  public static void dumpSegCoords(String type, double startx, double starty,
      double[] coords) {
    System.out.print(type + "\t" + doubleFormat.format(startx) + " "
        + doubleFormat.format(starty));
    for (int i = 0; i < coords.length; i++) {
      System.out.print("\t" + i + ":" + doubleFormat.format(coords[i]));
    }
    System.out.println();
  }

  public static void printAShape(Shape shape) {
    if (shape != null) {
      int pathType;
      // Shape intersected;
      double[] segCoords = new double[6];
      PathIterator pi = shape.getPathIterator(null);
      double startX = segCoords[0];
      double startY = segCoords[1];
      while (!pi.isDone()) {
        // intersected = null;
        pathType = pi.currentSegment(segCoords);

        switch (pathType) {
        case PathIterator.SEG_MOVETO:
          dumpSegCoords("MOVETO", startX, startY, segCoords);
          startX = segCoords[0];
          startY = segCoords[1];
          clearCoords(segCoords);
          break;
        case PathIterator.SEG_CUBICTO:
          dumpSegCoords("CUBICTO", startX, startY, segCoords);
          /*
           * intersected = new CubicCurve2D.Double(startX, startY, segCoords[0],
           * segCoords[1], segCoords[2], segCoords[3], segCoords[4],
           * segCoords[5]);
           */
          startX = segCoords[4];
          startY = segCoords[5];
          clearCoords(segCoords);
          break;
        case PathIterator.SEG_CLOSE:
          /* has no coordinates */
          dumpSegCoords("CLOSE", startX, startY, segCoords);
          clearCoords(segCoords);
          break;
        case PathIterator.SEG_LINETO:
          dumpSegCoords("LINETO", startX, startY, segCoords);
          /*
           * intersected = new Line2D.Double(startX, startY, segCoords[0],
           * segCoords[1]);
           */
          startX = segCoords[0];
          startY = segCoords[1];
          clearCoords(segCoords);
          break;
        case PathIterator.SEG_QUADTO:
          dumpSegCoords("QUADTO", startX, startY, segCoords);
          /*
           * intersected = new QuadCurve2D.Double(startX, startY, segCoords[0],
           * segCoords[1], segCoords[2], segCoords[3]);
           */
          startX = segCoords[2];
          startY = segCoords[3];
          clearCoords(segCoords);
          break;
        }

        pi.next();
      }
    }
  }
}
