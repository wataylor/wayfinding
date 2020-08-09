/* @name CGB.java

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

import zetek.igu.BothWayUtils;

/**
 * Global server variables which manage the interaction between the
 * graph, which has to work both in the server and in the read meta
 * data system, and the rest of the system.

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class CGB {

  public static final long serialVersionUID = 1;

  /** Obligatory constructor.*/
  public CGB() { /* */ }

  /** Instance of the object which implements various graphics methods
   * differently in the server and in the read meta data system.*/
  public static IGraphUtils igu = new BothWayUtils();
  /** Frobnicator which transforms points and shapes between .dwg and
   * axonometric coordinates.  */
  public static IPathUtils ipu = new Frobnicator();

}
