/* @name TestFrobnicator.java

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
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;

import zetek.geom.GeomReturn;
import zetek.geom.LineUtils;
import zetek.geom.ProjectableLine;

/**
 * Class to implement axonometric point and shape transforms.  There is
 * a test program: zetek.geom.TestFrobnicator

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see zetek.geom.TestFrobnicator
 */

public class Frobnicator implements IPathUtils {

  public static final long serialVersionUID = 1;

  /** Lines defining the edges of the bounding shape. */
  ProjectableLine bottomLine;
  ProjectableLine topLine;
  ProjectableLine leftLine;
  ProjectableLine rightLine;

  /** Obligatory constructor.*/
  public Frobnicator() {
    complete();
  }

  private void complete() {
    /* It is important that these lines have the right orientation and
     * direction as determined by which point is the first one.  It is
     * believed that most buildings will be able to map their floors
     * into the same axonometric area so long as the scale factors for
     * different tile sizes re settable on a per-building basis.  */
    topLine    = new ProjectableLine(SGB.ulX, SGB.ulY, SGB.urX, SGB.urY);
    bottomLine = new ProjectableLine(SGB.llX, SGB.llY, SGB.lrX, SGB.lrY);
    leftLine   = new ProjectableLine(SGB.ulX, SGB.ulY, SGB.llX, SGB.llY);
    rightLine  = new ProjectableLine(SGB.urX, SGB.urY, SGB.lrX, SGB.lrY);
  }

  public Point2D frobnicatePoint(Point2D pt) {
    Point2D p1;
    Point2D p2;
    Point2D p3;
    Point2D p4;
    Line2D l1;
    Line2D l2;
    GeomReturn gr;
    Point2D pcnt = LineUtils.xyPercents(SGB.envelope, pt);
    p1 = bottomLine.projectPercent(pcnt.getX());
    p2 = topLine.projectPercent(pcnt.getX());
    l1 = new Line2D.Double(p1, p2);
    p3 = leftLine.projectPercent(pcnt.getY());
    p4 = rightLine.projectPercent(pcnt.getY());
    l2 = new Line2D.Double(p3, p4);
    gr = LineUtils.linesIntersect(l1, l2);
    if (gr.result != GeomReturn.INTERSECT) {
      LineUtils.extendLine(l1);
      LineUtils.extendLine(l2);
      gr = LineUtils.linesIntersect(l1, l2);
      if (gr.result != GeomReturn.INTERSECT) {
	String message = pt + " not transform % "
          + pcnt + "\t" + l1 + "\t"
          + l2;
	System.out.println(message);
	return pt;
      }
    }
    // System.out.println(UfG.printPoint(pt) + " " +
    // UfG.printPoint(gr.point));
    return gr.point;
  }

  public Point2D unFrobnicatePoint(Point2D pt) {
    /** The math to really invert the transform above is a bit tense,
     * so this routine does not attempt it.  It simply uses a log
     * search to get close enough to the actual inversion.*/

    Point2D p1;
    Point2D p2;
    Point2D p3;
    Point2D p4;
    Line2D l1;
    Line2D l2;
    GeomReturn gr;
    Point2D pcnt = new Point2D.Double(50d, 50d);
    double stepX  = 25d;
    double stepY  = 25d;
    boolean upX   = false;
    boolean downX = false;
    boolean upY   = false;
    boolean downY = false;
    double x, y;

    do {
      p1 = bottomLine.projectPercent(pcnt.getX());
      p2 = topLine.projectPercent(pcnt.getX());
      l1 = new Line2D.Double(p1, p2);
      p3 = leftLine.projectPercent(pcnt.getY());
      p4 = rightLine.projectPercent(pcnt.getY());
      l2 = new Line2D.Double(p3, p4);
      gr = LineUtils.linesIntersect(l1, l2);
      if (gr.result != GeomReturn.INTERSECT) {
	System.out.println("Sicko " + pcnt.getX() + " " + pcnt.getY());
	return null;
      }

      x = pcnt.getX();
      if (gr.point.getX() > pt.getX()) {
	if (upX) {
	  stepX /= 2d; upX = false;
	}
	x-=stepX;
	if ( x < 0) {
	  stepX /= 2d; x = 0;
	  upX = true;
	} else {
	  downX = true;
	}
      } else {
	if (downX) {
	  stepX /= 2d; downX = false;
	}
	x+=stepX;
	if ( x > 100) {
	  stepX /= 2d; x = 100d;
	  downX = true;
	} else {
	  upX = true;
	}
      }

      y = pcnt.getY();
      if (gr.point.getY() > pt.getY()) {
	if (upY) {
	  stepY /= 2d; upY = false;
	}
	y-=stepY;
	if (y < 0) {
	  stepY /= 2d; y = 0;
	  upY = true;
	} else {
	  downY = true;
	}
      } else {
	if (downY) {
	  stepY /= 2d; downY = false;
	}
	y+=stepY;
	if ( y > 100) {
	  stepY /= 2d; y = 100d;
	  downY = true;
	} else {
	  upY = true;
	}
      }
      if ((x < 0) || (x > 100) || (y < 0) || (y > 100)) {
        System.out.println("Percentages off " + x + " " + y + " " + stepX + " " + stepY);
      }
      pcnt.setLocation(x, y);

    } while ((stepX > .05d) || (stepY > .05d));

    pcnt.setLocation(SGB.envelope.getX() + (SGB.envelope.getWidth() * pcnt.getX())/100, SGB.envelope.getY() + (SGB.envelope.getHeight() * pcnt.getY())/100);
    return pcnt;
  }

  public Shape frobnicateShape(Shape shape) {
    Point2D pt;
    float segCoords[] = new float[6];
    double startX = 0;
    double startY = 0;
    int flag = -1;
    PathIterator pIt = shape.getPathIterator(new AffineTransform(), 0.01);
    Path2D path = new Path2D.Double();

    while (!pIt.isDone()) {
      flag = pIt.currentSegment(segCoords);
      startX = segCoords[0];
      startY = segCoords[1];
      pt = new Point2D.Double(startX, startY);
      // System.out.print(flag + " "); // println in frobnicatePoint
      pt = frobnicatePoint(pt);
      switch (flag) {
      case PathIterator.SEG_CLOSE:
	path.closePath();
	break;
      case PathIterator.SEG_MOVETO:
	path.moveTo(pt.getX(), pt.getY());
	break;
      case PathIterator.SEG_LINETO:
	path.lineTo(pt.getX(), pt.getY());
	break;
      default:
	System.out.println("Unexpected segment type " + flag);
        break;
      }

      pIt.next();
    }
    return path;
  }

}
