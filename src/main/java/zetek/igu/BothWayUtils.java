/* @name BothWayUtils.java

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

package zetek.igu;

import zetek.graphserve.IGraphUtils;
import zetek.readmeta.classes.Derivation;
import zetek.readmeta.classes.MethodExtraction;
import zetek.readmeta.classes.MethodExtraction.AcDbClassMap;
import acdb.AcDbPoint;

/**
 * This class implements graph manipulation routines which must be
 * implemented differently depending on whether the system is running
 * in the server environment or in the read meta data environment.  An
 * instance of a class of this name is instantiated during start up in
 * either environment.  Thus, both the server and the read meta data
 * system have classes of the same package and class name.</p>

 * <P>This version of this class runs in the server environment and
 * has access to all classes implemented therein.

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class BothWayUtils implements IGraphUtils {

  public static final long serialVersionUID = 1;

  /** Obligatory constructor.*/
  public BothWayUtils() { /* */ }

  public AcDbClassMap derivMap = null;

  @Override
  public AcDbClassMap getDerivationMap() {
    if (derivMap == null) {
      derivMap = MethodExtraction.getMapForClass(Derivation.class);
    }
    return derivMap;
  }

  /**
   * Instantiate a point object based on a string which specifies x,
   * y, and z This version returns null because no points should be
   * made when the server reads the graph.  If it ever becomes n
   * ecessary for the server to rad a point and give it a value, this
   * method must be changed and the lcass itself must be estended so
   * as to be able to store x y and z.
   * @param value string of the form [x y z]
   * @return a point object with values initialized
   */
  @Override
  public AcDbPoint makeStringPoint(String value) {
    System.out.println("ERR Unexpected call to point maker.");
    return null;
  }
}
