/* @name ShapeMaker.java

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
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

import zetek.graphserve.ShapeSerializer;

public class ShapeMaker {

  private ShapeMaker() {}

  public static Shape makeCorridor03042_EP02() {
    String shapeString = "m1121.1188 2037.0l1121.1188 2097.0l1026.2438 2097.0l1026.2438 1896.7467l956.6188 1870.0735l956.6188 1779.0l1020.6188 1779.0l1020.6188 1807.0l1026.2438 1807.0l1026.2438 875.0l1020.6188 875.0l1020.6188 903.0l956.6188 903.0l956.6188 811.9265l1026.2438 785.2533l1026.2438 573.0l1121.1188 573.0l1121.1188 645.0l1090.4938 645.0l1090.4938 663.0l1123.7438 663.0l1123.7438 724.5l1090.4938 724.5l1090.4938 764.625l1090.4938 1245.375l1096.1188 1245.375l1096.1188 1219.375l1156.1188 1219.375l1156.1188 1461.875l1096.1188 1461.875l1096.1188 1436.625l1090.4938 1436.625l1090.4938 1957.5l1123.7438 1957.5l1123.7438 2019.0l1090.4938 2019.0l1090.4938 2037.0l1121.1188 2037.0";
    Shape shape = ShapeSerializer.syntehsizeShape(shapeString);
    //AffineTransform at = AffineTransform.getScaleInstance(1.0, 1.0);
    //at.translate(40,-400);
    //shape = at.createTransformedShape(shape);
    return(shape);
  }

  public static Shape makeOffice03002_EP02() {
    String shapeString = "m266.1188 2243.0l357.1188 2243.0l357.1188 2181.0l406.7438 2181.0l406.7438 2002.0l365.1188 2002.0l365.1188 1996.375l417.6188 1996.375l417.6188 1895.875l337.1188 1895.875l337.1188 1890.25l417.6188 1890.25l417.6188 1834.25l218.4938 1834.25l218.4938 1811.0l286.9938 1811.0l286.9938 1709.0l124.1188 1709.0l124.1188 1805.25l126.1182 1805.25l126.1182 1829.25l124.1188 1829.25l124.1188 1869.25l301.1188 1869.25l301.1188 2002.0l266.1188 2002.0l266.1188 2056.0l260.1188 2056.0l259.7438 2056.0l259.7438 2141.0l239.7438 2141.0l239.7438 2181.0l259.7438 2181.0l259.7438 2194.625l266.1188 2194.625";
    Shape shape = ShapeSerializer.syntehsizeShape(shapeString);
    //AffineTransform at = AffineTransform.getScaleInstance(0.8, 0.8);
    //at.translate(100,-1600);
    //shape = at.createTransformedShape(shape);
    return(shape);
  }

  public static Shape makeWheelWell(int x, int y, int w, int h) {
    GeneralPath shape = new GeneralPath();
    shape.moveTo(x, y);
    shape.lineTo(x, y + h);
    shape.lineTo(x + w/3, y + h);
    shape.lineTo(x + w/3, y + h - h/2);
    shape.lineTo(x + (w/3)*2, y + h - h/2);
    shape.lineTo(x + (w/3)*2, y + h);
    shape.lineTo(x + w, y + h);
    shape.lineTo(x + w, y);
    shape.closePath();
    return(shape);
  }

  public static Shape makeThinShape(int x, int y, int scale) {
    GeneralPath shape = new GeneralPath();
    shape.moveTo(x, y);
    shape.lineTo(x, y+(scale*3*2));           //v
    shape.lineTo(x+(scale*3), y+(scale*3*2)); //h
    shape.lineTo(x+(scale*3), y+(scale*6*2)); //v
    shape.lineTo(x, y+(scale*6*2));           //h
    shape.lineTo(x, y+(scale*9*2));           //v
    shape.lineTo(x+(scale*4), y+(scale*9*2)); //h
    shape.lineTo(x+(scale*4), y+(scale*17)); //v
    shape.lineTo(x+(scale), y+(scale*17));   //h
    shape.lineTo(x+(scale), y+(scale*13));   //v
    shape.lineTo(x+(scale*4), y+(scale*13)); //h
    shape.lineTo(x+(scale*4), y+(scale*5)); //v
    shape.lineTo(x+(scale), y+(scale*5));   //h
    shape.lineTo(x+(scale), y);             //v
    shape.closePath();
    return(shape);
  }

  public static Shape makeFunnyShape(int x, int y, int w, int h) {
    GeneralPath shape = new GeneralPath();
    shape.moveTo(x, y);
    shape.lineTo(x, y + h/3);
    shape.lineTo(x+w/3, y + h/3);
    shape.lineTo(x+w/3, y + (h/3)*2);
    shape.lineTo(x, y + (h/3)*2);
    shape.lineTo(x, y + h);
    shape.lineTo(x + w, y + h);
    shape.lineTo(x + w, y + h - h/5);
    shape.lineTo(x + w/2, y + h - (h/5));
    shape.lineTo(x + w/2, y + h - (h/5)*2);
    shape.lineTo(x + w, y + h - (h/5)*3);
    shape.lineTo(x + w/2, y + h - (h/5)*3);
    shape.lineTo(x + w/2, y + h - (h/5)*4);
    shape.lineTo(x + w, y + h - (h/5)*4);
    shape.lineTo(x + w, y);
    shape.closePath();
    return(shape);
  }

  public static Shape makeInnerShape(Shape shape, double xScale, double yScale) {
    double cx = shape.getBounds().getCenterX();
    double cy = shape.getBounds().getCenterY();
    shape = AffineTransform.getTranslateInstance(-cx, -cy).createTransformedShape(shape);
    shape = AffineTransform.getScaleInstance(xScale, yScale).createTransformedShape(shape);
    shape = AffineTransform.getTranslateInstance(cx, cy).createTransformedShape(shape);
    return(shape);
  }

  public static Shape makeBowtie() {
    int x = 200;
    int y = 200;
    GeneralPath tie = new GeneralPath();
    tie.moveTo(x,     y);
    tie.lineTo(x,     y+120);
    tie.lineTo(x+50,  y+70);
    tie.lineTo(x+100, y+120);
    tie.lineTo(x+100, y);
    tie.lineTo(x+50,  y+50);
    tie.closePath();
    return(tie);
  }
}
