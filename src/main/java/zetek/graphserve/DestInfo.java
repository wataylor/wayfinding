/* @name DestInfo.java

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

import java.awt.geom.Point2D;

/**
 * Store information about a destination in terms of a location and a
 * space name.  Locations have X, Y values which specify points but
 * they are not named locations in the graph.  The location is found
 * by going to its X, Y within the space.</p>

 * <P>This class holds the name of a node in the graph and a point
 * value where the route segment should start or end within the shape
 * which is associated with that node.</p>

 * <P>The meta data environment does not permit a route segment to be
 * specified in terms of locations, so this structure is used only by
 * the server.

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class DestInfo {

  public static final long serialVersionUID = 1;

  public String  name;
  public Point2D point;
  public String inDesc;
  /** If nonzero, tells how to modify the destination, either by going further
   * along the path or back one destination.  The modifications depend on
   * whether the modifiers are applied to the first node in a route
   * or the last node.*/
  public short modifier;

  /**
   * Html string used to display the overview in the info bubble popup.
   */
  private String over;

  /**
   * Html string used to display the device info in the info bubble popup.
   */
  private String  dev;

  /**
   * Html string used to display the company info in the info bubble popup.
   */
  private String org;

  /**
   * Html string used to display the people in the info bubble popup.
   */
  private String peep;

  /**
   * Html string used to display the pictures in the info bubble popup.
   */
  private String pic;

  /** Obligatory constructor.*/
  public DestInfo() { /* */ }

  public DestInfo(String name, Point2D point) {
    this.name  = name;
    this.point = point;
  }

  public String getName() {
    return name;
  }

  public String getOver() {
    return over;
  }

  /**
   * Set to null to have the Overview tab not display when string is empty
   * @param over html string to display associated overview
   */
  public void setOver(String over) {
    this.over = (over.isEmpty()) ? null : over;
  }

  public String getOrg() {
    return org;
  }

  /**
   * Set to null to have the Company tab not display when string is empty
   * @param org html string to display associated companies
   */
  public void setOrg(String org) {
    this.org = (org.isEmpty()) ? null : org;
  }

  public String getPeep() {
    return peep;
  }

  /**
   * Set to null to have the personnel tab not display when string is empty
   * @param peep html string to display associated pictures
   */
  public void setPeep(String peep) {
    this.peep = (peep.isEmpty()) ? null : peep;
  }

  public String getPic() {
    return pic;
  }

  /**
   * Set to null to have the photo tab not display when string is empty
   * @param pic html string to display associated pictures
   */
  public void setPic(String pic) {
    this.pic = (pic.isEmpty()) ? null : pic;
  }

  public String getDev() {
    return dev;
  }

  /**
   * Set to null to have the device tab not display when string is empty
   * @param pic html string to display associated pictures
   */
  public void setDev(String dev) {
    this.dev = dev;
  }
}
