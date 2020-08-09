/* @name DevDesc.java

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

package zetek.server.utils;

import java.awt.geom.Point2D;

import org.json.JSONException;
import org.json.JSONStringer;

/**
 * Information about a device which is to be transmitted to the client.
 * Instances are sorted on the basis of X, Y, and then on the device type.

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class DevDesc implements Comparable<DevDesc> {

  public static final long serialVersionUID = 1;

  public Point2D point;
  public short ilk;
  public StringBuilder id = new StringBuilder();
  public String name;
  public String type;

  /** Obligatory constructor.*/
  public DevDesc() { /* */ }

  @Override
  public int compareTo(DevDesc o) {
    if (point.equals(o.point)) { return type.compareTo(o.type); }
    Double x = point.getX();
    if (x == o.point.getX()) {
      Double y = point.getY();
      return y.compareTo(o.point.getY());
    }
    return x.compareTo(o.point.getX());
  }

  /**
   * Returns true if the devices are co-located and of the same type.
   * @param o other device
   * @return true if they are at the same x,y and have the same type
   */
  public boolean colo(DevDesc o) {
    return point.equals(o.point) && (isSameDeviceType(type, o.type));
    //return point.equals(o.point) && (type.equals(o.type));
  }

  /**
   * Check to see if the device is of the same type, but group certain dissimilar
   * names to the same type
   * @param typeA
   * @param typeB
   * @return
   */
  public static boolean isSameDeviceType(String typeA, String typeB) {
    if (typeA == null || typeB == null) { return(false); }
    if (typeA.equals(typeB)) { return(true); }
    if ( (typeA.startsWith("Smoke") || (typeA.startsWith("Heat"))) &&
        (typeB.startsWith("Smoke") || typeB.startsWith("Heat"))) {
      return(true);
    }
    if ( (typeA.startsWith("Monitor W") || (typeA.startsWith("Monitor T"))) &&
        (typeB.startsWith("Monitor W") || typeB.startsWith("Monitor T"))) {
      return(true);
    }
    return(false);
  }

  /**
   * Get the name to be used in the menu bar of the info bubble
   * @param type
   * @return
   */
  public static String getTypeDisplayName(String type) {
    if (type == null) { return(""); }
    if ( (type.startsWith("Smoke") || (type.startsWith("Heat"))) ) {
      return("Smoke Alarms");
    }
    if ( (type.startsWith("Monitor W") || (type.startsWith("Monitor T"))) ) {
      return("Waterflow/Track Superv");
    }
    return(type);
  }

  /** Generates a Json string to represent a Java Script object */
  public String toString() {
    JSONStringer jw = new JSONStringer();
    try {
      jw.object();
      jw.key("X").value(point.getX());
      jw.key("Y").value(point.getY());
      jw.key("ilk").value("dev");
      jw.key("subilk").value(getTypeDisplayName(type));
      jw.key("id").value(id.toString());
      /* This ilk is the server-side ilk which tells the server to get
       * information about the device out of the Device table as
       * opposed to the space or location table, see
       * getDestinationForBubble */
      jw.key("serverilk").value(StringsManager.DEVICE_ILK);
      jw.key("name").value(name);
      jw.endObject();
    } catch (JSONException e) {
      return e.toString();
    }
    return jw.toString();
  }
}
