/* @name BuildingGraphData.java

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

import java.util.List;
import java.util.Map;

import zetek.readmeta.classes.Derivation;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.Graph;

/**
 * This class holds all of the graph-related information for a given
 * building.  The server stores an instance of this class indexed by
 * building ID for each building in the system.

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class BuildingGraphData {

  public static final long serialVersionUID = 1;

  /** Holds the graph for one building in the system.  */
  public Graph g;
  /** Path computation system for the building graph.  */
  public DijkstraShortestPath dsp;
  /** Holds the floor map for the building graph.  */
  public Map<String, List<Derivation>> floorMap;

  /** Obligatory constructor.*/
  public BuildingGraphData() { /* */ }

  public BuildingGraphData(Graph g) {
    this.g = g;
    dsp = new DijkstraShortestPath(g, EdgeWeight.getEdgeWeightInstance());
  }
}
