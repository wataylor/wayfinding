/* @name FloorSegment.java

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
 * Container for an ordered list of waypoints which make up whatever
 * portion of a route segment is confined to one floor.  This class is
 * structured for easy conversion to JSON so that it can be sent to
 * the client.

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class FloorSegment implements Serializable {

  public static final long serialVersionUID = 1;

  public List<Waypoint> waypointList;
  public FloorChange floorChange;
  public String startNode;
  public String endNode = "";
  public String stD;
  public String enD;
  public String floorDWG;
  public Waypoint recentWaypoint;
  /** Used for supplying the client with extra information about the
   * sub route.  This object is not included as part of the JSON object
   * if this variable is null.  */
  public Object supplement;

  /** Obligatory constructor.
   * @param floorDWG Tell the client the map on which to draw this segment. */
  public FloorSegment(String start, String floorDWG) {
    this.startNode = start;
    this.floorDWG  = floorDWG;
    waypointList = new ArrayList<Waypoint>();
  }

  public FloorSegment(String start, String floorDWG, int length) {
    this.startNode = start;
    this.floorDWG = floorDWG;
    waypointList = new ArrayList<Waypoint>(length);
  }

  public Waypoint getLastPoint() {
    return recentWaypoint;
  }

  public void addWaypoint(Waypoint wp) {
    if (waypointList == null) { waypointList = new ArrayList<Waypoint>(); }
    if ((recentWaypoint != null) && recentWaypoint.equals(wp)) { return; }
    recentWaypoint = wp;
    waypointList.add(wp);
  }

  public void addWaypoints(List<Waypoint> wpList) {
    if (waypointList == null) { waypointList = wpList; }
    for(Waypoint wp : wpList) {
      addWaypoint(wp);
    }
  }

  public void setFloorChange(FloorChange fc) {
    floorChange = fc;
  }

  /**
   * Express the object in JSON notation
   */
  public String toString() {
    int len;
    boolean did;

    if (waypointList == null) {
      len = 5;
    } else {
      len = (10 * waypointList.size()) + 5;
    }
    if (len < 16) { len = 16; }
    StringBuilder sb = new StringBuilder(len);

    sb.append("{\"st\":\"" + startNode + "\"," +
	      ((stD == null) ? "" : "\"stD\":\"" +
	       stD + "\",") + "\"en\":\"" + endNode + "\"," +
	      ((enD == null) ? "" : "\"enD\":\"" + enD + "\",") +
	      "\"o\":\"" + floorDWG + "\",\"x\":0,\"y\":0,\"zm\":1,\"wp\":[");
    if (waypointList != null) {
      did = false;
      for (Waypoint wp : waypointList) {
        if (did) { sb.append(",\n"); }
        sb.append(wp.toString());
        did = true;
      }
    }
    sb.append("],\"fc\":");
    if (floorChange == null) {
      sb.append("null");
    } else {
      sb.append(floorChange.toString());
    }
    if (supplement != null) {
      sb.append(",\"sup\":" + supplement.toString());
    }
    sb.append("}");
    return sb.toString();
  }
}
