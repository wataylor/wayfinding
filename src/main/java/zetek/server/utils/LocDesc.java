/* @name LocDesc.java

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

import org.json.JSONObject;

import zetek.graphserve.CGB;

/**
 * Holds location information which is to be transmitted to the client
 * as a Json object.  JSON objects are manufactured by looking for get
 * methods.

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class LocDesc implements Comparable<LocDesc> {

  public static final long serialVersionUID = 1;

  /** Obligatory constructor.*/
  public LocDesc() { /* */ }

  public String desc;
  public String fDWG;
  public String divID;

  public long id;
  public int ilk;
  public int fOrd;
  public double x = 0d;
  public double y = 0d;

  public String innerDesc;

  @Override
    public int compareTo(LocDesc arg0) {
    int c;
    if ( (c = desc.compareTo(arg0.desc)) != 0) { return c; }
    return new Integer(ilk).compareTo(arg0.ilk);
  }

  public String getDesc() {
    return desc;
  }

  public String getDivID() {
    return divID;
  }

  public long getId() {
    return id;
  }

  public int getIlk() {
    return ilk;
  }

  public String getFDWG() {
    return fDWG;
  }

  public int getFOrd() {
    return fOrd;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public String getInDsc() {
    return innerDesc;
  }

  public void setInDsc(String innerDesc) {
    this.innerDesc = innerDesc;
  }

  public LocDesc(String desc, long id, int ilk, String fDWG, int fOrd) {
    this.desc = desc;
    this.id   = id;
    this.ilk  = ilk;
    this.fDWG = fDWG;
    this.fOrd = fOrd;
  }

  public LocDesc(String desc, long id, int ilk, String fDWG, int fOrd, double x, double y) {
    this(desc, id, ilk, fDWG, fOrd);
    recordXY(x, y);
  }

  public void recordXY(double x, double y) {
    recordXY(new Point2D.Double(x, y));
  }

  public void recordXY(Point2D pt) {
    if (CGB.ipu != null) {
      pt = CGB.ipu.frobnicatePoint(pt);
    }
      this.x = pt.getX();
      this.y = pt.getY();
  }

  public String toString () {
    JSONObject js = new JSONObject(this, false);
    // System.out.println(js.toString());
    return js.toString();
  }
}
