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

package zetek.test.geom;

import java.awt.geom.Point2D;

import zetek.common.CommandArgs;
import zetek.common.IntervalPrinter;
import zetek.graphserve.Frobnicator;

/**
 * Test the frobnicator by transforming points and then reverse
 * transforming them.

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class TestFrobnicator {

  public static final long serialVersionUID = 1;

  /** Obligatory constructor.*/
  public TestFrobnicator() { /* */ }

  /**
   * @param args ignored
   */
  public static void main(String[] args) {
    Frobnicator frob = new Frobnicator();

    Point2D inPt;
    Point2D frobPt;
    Point2D outPt;
    int i;

    IntervalPrinter ip = new IntervalPrinter();

    for (i=1; i<10; i++) {
      inPt   = new Point2D.Double(200d * (double)i, 100d * (double)i);
      frobPt = frob.frobnicatePoint(inPt);
      outPt  = frob.unFrobnicatePoint(frobPt);
      System.out.println(CommandArgs.stringFromPoint(inPt)   + "\t" +
			 CommandArgs.stringFromPoint(frobPt) + "\t" +
			 CommandArgs.stringFromPoint(outPt));
      System.out.println();
    }
    System.out.println(ip.howLongSince() + "for 10 inversions");
  }
}
