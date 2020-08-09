/* @name FloorChange.java

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

import java.io.Serializable;

/**
 * Class to hold information the client needs with respect to the
 * vertical transitions needed to follow the map.

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class FloorChange implements Serializable {

  public static final long serialVersionUID = 1;

  /** Human-readable instructions for making the floor change.  */
  public String instructions;
  /** Number of floors which must be traversed.  A positive number means go up,
   * a negative number means go down.*/
  public short  delta;
  /** Drawing name for the destination floor.*/
  public String floorDWG;

  /** Obligatory constructor.*/
  public FloorChange() { /* */ }

  public FloorChange(String instructions, short delta, String floorDWG) {
    this.instructions = instructions;
    this.delta        = delta;
    this.floorDWG     = floorDWG;
  }

  /** Returns the object in JSON notation */
  public String toString() {
    return "{\"ins\":\"" + instructions + "\",\"dlt\":" + delta + ",\"o\":\"" + floorDWG + "\"}";
  }
}
