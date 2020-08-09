/* @name IGraphUtils.java

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

import zetek.readmeta.classes.MethodExtraction;
import acdb.AcDbPoint;

/**
 * Define the interface to the routines which must be imlemented
 * differently in the server and in the read meta data system.

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public interface IGraphUtils {

  public static final long serialVersionUID = 1;

  public abstract AcDbPoint makeStringPoint(String value);
  public abstract MethodExtraction.AcDbClassMap getDerivationMap();
}
