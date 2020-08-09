/* @name ManagerProperty.java

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

import java.util.Set;

import zetek.common.PropertyManager;

/**
 *
 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class ManagerProperty {

  public static final long serialVersionUID = 1;

  /** Obligatory constructor.*/
  public ManagerProperty() { /* */ }

  /**
   * @param args ignored
   */
  public static void main(String[] args) {
    PropertyManager pm = new PropertyManager();

    String[] keys = pm.sortedStringKeys();

    for (String k : keys) {
      System.out.println(k + " " + pm.getProperty(k));
    }

    pm.addListMembers("mems", new String[] {"S0", "S1", "Sssss2"});

    pm.writePropertyFile("Zetek Building Metadata Manager Properties File");

    pm = new PropertyManager();

    keys = pm.sortedStringKeys();

    for (String k : keys) {
      System.out.println(k + " " + pm.getProperty(k));
    }

    keys = pm.getCategoryArray("mems");
    for (String s : keys) {
      System.out.println(s);
    }
  }
}
