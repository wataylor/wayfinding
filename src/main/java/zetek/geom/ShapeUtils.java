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

package zetek.geom;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * A collection of utilities to manipulate and transform shapes
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

  /**
   * Draw a stylized point of the supplied size and color
   *
   * @param gg Graphics2D object with which to draw
   * @param p point to center our drawn point around
   * @param size how big to draw our point
   * @param color the color of the inside of the point
   */
  public static void drawPoint(Graphics2D gg, Point2D p, int size,
      Color color) {
    drawPoint(gg, p.getX(), p.getY(), size, color);
  }

  /**
   * Draw a stylized point of the supplied size and color
   *
   * @param gg Graphics2D object with which to draw
   * @param x x value of the point to center our drawn point around
   * @param y y value of the point to center our drawn point around
   * @param size how big to draw our point
   * @param color the color of the inside of the point
   */
  public static void drawPoint(Graphics2D gg, double x, double y, int size,
      Color color) {
    Color pColor = gg.getColor();
    gg.setColor(color);
    Ellipse2D.Double circle = new Ellipse2D.Double(x-size/2, y-size/2,
        size, size);
    gg.draw(circle);
    gg.fill(circle);
    gg.setColor(pColor);
  }

  public static void drawTextPoint(
      Graphics2D gg, double x, double y, int size, Color color, String text) {
    drawTextPoint(gg, x, y, size, color, text, null, null, 0);
  }

  public static void drawTextPoint(
      Graphics2D gg, Point2D p, int size, Color color, String text) {
    drawTextPoint(gg, p.getX(), p.getY(), size, color, text, null, null, 0);
  }

  public static void drawTextPoint(
      Graphics2D gg, Point2D p, int size, Color color, String text,
      Color textColor, Color textShadow, int offset) {
    drawTextPoint(gg, p.getX(), p.getY(), size, color, text, textColor,
        textShadow, offset);
  }

  public static void drawTextPoint(
      Graphics2D gg, double x, double y, int size, Color color, String text,
      Color textColor, Color textShadow, int offset) {

    drawPoint(gg, x, y, size, color);

    /* Draw point */
    Color previousColor = gg.getColor();

    /* Draw text shadow */
    if (textShadow != null) {
      gg.setColor(textShadow);
      gg.drawString(text, (int)(x+size+offset)-1, (int)(y+size)+1 );
    }

    /* Draw text */
    if (textColor != null) {
      gg.setColor(textColor);
    } else {
      gg.setColor(color);
    }
    gg.drawString(text, (int)(x+size+offset), (int)(y+size) );
    gg.setColor(previousColor);
  }

  /**
   * Rotate and scale a point about an origin.  The AffineTransform
   * class has a method getRotateInstance which is able to rotate a
   * point around a specified origin, BUT this does not include
   * scaling.  Scaling has to be done before translating back to the
   * origin.  Thus, this method alters the transform 3 times.
   * @param pt The point to be rotated and scaled
   * @param origin origin about which the point is to rotate
   * @param scale Scale factor in X and Y
   * @param rot rotation degrees
   */
  public static Point2D rotatePoint(Point2D pt, Point2D origin, Point2D scale,
      double rot) {

    // Transformations are carried out in the reverse order from which
    // they are defined.  This matrix shifts to the origin, rotates
    // round the origin, scales, then shifts back to the original
    // location.
    AffineTransform aff = AffineTransform.getTranslateInstance(origin.getX(),
        origin.getY());
    aff.scale(scale.getX(), scale.getY());
    aff.rotate((rot * Math.PI)/180d);
    aff.translate(0-origin.getX(), 0-origin.getY()); // Happens first
    return aff.transform(pt, null);
  }

  /**
   * Move the center of the shape to the origin, scale it, and then move the
   * center back to where it was.
   * @param shape shape to scale
   * @param xScale x scale
   * @param yScale y scale
   * @return the scaled shape
   */
  public static Shape scaleShape(Shape shape, double xScale, double yScale) {
    double cx = shape.getBounds().getCenterX();
    double cy = shape.getBounds().getCenterY();
    shape = AffineTransform.getTranslateInstance(-cx, -cy).createTransformedShape(shape);
    shape = AffineTransform.getScaleInstance(xScale, yScale).createTransformedShape(shape);
    shape = AffineTransform.getTranslateInstance(cx*xScale, cy*yScale).createTransformedShape(shape);
    return(shape);
  }

  /**
   * Get an alpha composite used for setting the transparency of a g2 object
   * @param alpha between 1.0 and 0
   * @return alpha composite
   */
  public static AlphaComposite makeComposite(float alpha) {
    int type = AlphaComposite.SRC_OVER;
    return(AlphaComposite.getInstance(type, alpha));
  }

  /**
   * Works properly with closed shapes ONLY
   * @param shape the shape to get the sides of, should be a closed shape only
   * @return ArrayList of sides of the shape
   */
  public static ArrayList<Line2D> getSides(Shape shape) {
    PathIterator points = shape.getPathIterator(new AffineTransform(), 0.01);
    double[] coords = new double[6];
    int pointType;
    double firstX = 0.0;
    double firstY = 0.0;
    double nextX = 0.0;
    double nextY = 0.0;
    ArrayList<Line2D> sides = new ArrayList<Line2D>();
    Line2D side;

    while (!points.isDone()) {
      side = null;
      pointType = points.currentSegment(coords);

      switch (pointType) {
      case PathIterator.SEG_MOVETO:
        /* Should ONLY occur at the start--rooms are always closed shapes */
        firstX = nextX = coords[0];
        firstY = nextY = coords[1];
        break;
      case PathIterator.SEG_CLOSE:
        /* Last segment should be captured on this here */
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
        sides.add(side);
      }
      points.next();
    }
    return(sides);
  }
}
