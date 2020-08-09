/* @name AdjustSpaceName.java

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

package zetek.test;

import zetek.graphserve.SGB;
import zetek.readmeta.classes.SpaceNameAdjust;

/**
 * Test the program that adjusts space name abbreviations

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class AdjustSpaceName {

  public static final long serialVersionUID = 1;

  /** Obligatory constructor.*/
  public AdjustSpaceName() { /* */ }

  /**
   * @param args ignored
   */
  public static void main(String[] args) {
    String in;

    in = "dN Lo test  enc   encl   rm   rm.  t";
    System.out.println(in + "\t" + SpaceNameAdjust.makeInitCaps(in, SpaceNameAdjust.embeddedAbbrevs));
    System.out.println(in + "\t" +
		       SpaceNameAdjust.adjustSpaceName(in, SGB.VERTICAL_ILK));

    in = "Me LO TEST    T";
    System.out.println(in + "\t" +
		       SpaceNameAdjust.adjustSpaceName(in, SGB.DOOR_ILK));

    in = "DR Paint Sto 03053";
    System.out.println(in + "\t" +
		       SpaceNameAdjust.adjustSpaceName(in, SGB.DOOR_ILK));

    in = "hall. LO TEST    T";
    System.out.println(in + "\t" +
		       SpaceNameAdjust.adjustSpaceName(in, SGB.HALL_ILK));

    in = "hvac. LO TEST    T";
    System.out.println(in + "\t" +
		       SpaceNameAdjust.adjustSpaceName(in, SGB.ROOM_ILK));

    in = "COSTUME SHOP NO.1 03014";
    System.out.println(in + "\t" +
		       SpaceNameAdjust.adjustSpaceName(in, SGB.ROOM_ILK));

    in = "HOUSE TRAP 03017";
    System.out.println(in + "\t" +
		       SpaceNameAdjust.adjustSpaceName(in, SGB.ROOM_ILK));
  }
}
