/* @name akeDoor.java

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
package zetek.test.path;

import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class FakeDoor extends Object {
  private Rectangle2D rect;
  double w = 10.0;
  double h = 10.0;
  Point2D center;

  public FakeDoor(Point2D center) {
    this.center = center;
    rect = new Rectangle2D.Double(center.getX()-(w/2), center.getY()-(h/2),w,h);
    //System.out.println(rect.getBounds());
  }

  public Point2D getCenter() {
    return(center);
  }

  public Shape getShape() {
    return(rect);
  }

  public Rectangle2D getRect() {
    return rect;
  }

  public void setRect(Rectangle2D rect) {
    this.rect = rect;
  }
}
