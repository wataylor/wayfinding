/* @name SubRoute.java

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
import java.util.ArrayList;
import java.util.List;

/**
 * Defines the information needed to get from the start point in a
 * building to the end point.  The User Interface defines a "route" as
 * the information needed to go from a start point to a destination,
 * and then to another destination, and so on.  Thus, there may be any
 * number of sub routes in a route, but they are requested one at a
 * time by the client, so the serve has no knowledge of a route as a
 * unified construct.  This class is structured for easy conversion to
 * JSON so that it can be sent to the client.

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class SubRoute implements Serializable {

  public static final long serialVersionUID = 1;

  public List<FloorSegment> segmentList;


  /** Obligatory constructor.*/
  public SubRoute() {
    segmentList = new ArrayList<FloorSegment>();
  }

  public void addFloorSegment(FloorSegment seg) {
    if (segmentList == null) { segmentList = new ArrayList<FloorSegment>(); }
    segmentList.add(seg);
  }

  public void specifyInDescs(String stD, String enD) {
    if ((segmentList == null) || (segmentList.size() <= 0)) { return; }
    FloorSegment seg = segmentList.get(0);
    seg.stD = stD;
    seg = segmentList.get(segmentList.size()-1);
    seg.enD = enD;
  }

  /**
   * Name hte last node in the last segment of the sub-route
   * @return the name of the last node in the sub-route
   */
  public String fetchLastNodeName() {
    if ((segmentList == null) || (segmentList.size() <= 0)) { return ""; }
    FloorSegment seg = segmentList.get(segmentList.size()-1);
    return seg.endNode;
  }

  /**
   * The last floor segment in a route may need to have the destination
   * re-specified.  This happens when the route is specified in terms of
   * an abstract destination such as a floor or the nearest emergency exit.
   * In that case, the X, Y, ID, and other attributes of the destination
   * are not known until the route has been computed.
   * @param o description of the destination at the end of the last floor
   * segment in the sub-route.
   */
  public void specifyLastSupplement(Object o) {
    if ((segmentList == null) || (segmentList.size() <= 0)) { return; }
    FloorSegment seg = segmentList.get(segmentList.size()-1);
    seg.supplement = o;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder(30 * segmentList.size());
    boolean did = false;

    sb.append("[");
    for (FloorSegment fs : segmentList) {
      if (did) { sb.append(",\n"); }
      sb.append(fs.toString());
      did = true;
    }
    sb.append("]");
    return sb.toString();
  }
}
