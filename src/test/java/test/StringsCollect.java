/* @name StringsCollect.java

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

package test;

import java.util.List;

import zetek.server.utils.CollectStrings;
import zetek.server.utils.CollectStrings.KeyedStringer;
import zetek.server.utils.CollectStrings.Stringer;

/**
 * Test the string manager and make sure it can produce proper and
 * appropriate .json objects

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class StringsCollect {

  public static final long serialVersionUID = 1;

  /** Obligatory constructor.*/
  public StringsCollect() { /* */ }

  /**
   * @param args ignored
   */
  public static void main(String[] args) {
    CollectStrings sm = new CollectStrings();
    List<KeyedStringer> keyList;
    Stringer[] stringers;

    boolean did = false;

    Integer igr = new Integer(1);
    int flo = 1;

    sm.addString(igr, "Aardwolf", 1, flo);
    sm.addString(igr, "Aardvark", 1, flo);
    sm.addString(igr, "alpine", 1, flo);
    sm.addString(igr, "beatiful", 2, flo);
    sm.addString(igr, "nearer",   2, flo);
    sm.addString(igr, "nearest",  2, flo);
    sm.addString(igr, "n0 one cares",  2, flo);
    sm.addString(igr, "gone are the days", 3, flo);

    igr = new Integer(2);
    sm.addString(igr, "N001Noo3", 4, flo);
    sm.addString(igr, "N001No7",  4, flo);
    sm.addString(igr, "N001No3",  4, flo);
    sm.addString(igr, "N001Noo4", 4, flo);
    sm.addString(igr, "N002Noo3", 4, flo);
    sm.addString(igr, "N002No7",  4, flo);
    sm.addString(igr, "N002No3",  4, flo);
    sm.addString(igr, "N002Noo4", 4, flo);

    try {
      keyList = sm.makeKeyedStrings();
      System.out.println("[");
      for (KeyedStringer ks : keyList) {
	if (did) { System.out.println(","); }
	System.out.print(ks.toString());
	did = true;
      }
      System.out.println("\n]");

      System.out.println(sm.toString());

      /* For reasons obscure, trying to create a JSON array of these
       * objects and turning that to a JSON string results in all of
       * the quotes being quoted which violates the Java Script
       * syntax.  Having the objects generate individual JSON objects
       * works, see the toString method of CollectStrings */

      //JSONArray ja = new JSONArray(keyList);
      //System.out.println(ja.toString(4));
      //ja = JSONML.toJSONArray(sm.makeKeyedStrings());
      //JSONObject jo = new JSONObject(ja);
      //System.out.println(jo.toString(3));

      if ( (stringers = sm.matchInput("A")) != null) {
        for (Stringer s : stringers) {
          System.out.println(s.toString());
        }
	System.out.println();
      }

      if ( (stringers = sm.matchInput("n")) != null) {
        for (Stringer s : stringers) {
          System.out.println(s.toString());
        }
	System.out.println();
      }

      if ( (stringers = sm.matchInput("n0")) != null) {
        for (Stringer s : stringers) {
          System.out.println(s.toString());
        }
	System.out.println();
      }

      if ( (stringers = sm.matchInput("n002")) != null) {
        for (Stringer s : stringers) {
          System.out.println(s.toString());
        }
	System.out.println();
      }

    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    //System.out.println(sm.toString());
  }
}
