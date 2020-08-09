/* @name GraphSplore.java

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
import java.util.Set;

import zetek.readmeta.classes.Derivation;
import edu.uci.ics.jung.graph.DirectedEdge;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.StringLabeller;

/**
 * Explore and print various aspects of the specified node or route

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class GraphSplore {

  public static final long serialVersionUID = 1;

  /** Obligatory constructor.*/
  public GraphSplore() { /* */ }

  @SuppressWarnings("unchecked")
  public static Set<Vertex> sploreNode(Graph g, Vertex vx) {
    Set<Derivation> neighs = vx.getNeighbors();
    String name;

    StringLabeller sl = StringLabeller.getLabeller(g);

    System.out.println("\n" + ((Derivation)vx).numNeighbors() + " neighbors of " + ((Derivation)vx).StringRep);
    for (Derivation d : neighs) {
      name = sl.getLabel(d);
      if (name != null) { System.out.println(name + " " + d.floorDWG); }
    }

    return vx.getNeighbors();
  }

  /**
   * Print the nodes along a route along with the distances between nodes
   * @param g the graph
   * @param route the route - list of vertices in order of traversal
   */
  public static void printRouteNodeDistances(Graph g, List<Vertex> route) {
    if ((route == null) || (route.size() <= 0)) {
      System.out.println("\nNull route");
      return;
    }

    EdgeWeight ew = EdgeWeight.getEdgeWeightInstance();
    Vertex recentV = null;
    Edge e;
    int distSum = 0;
    int dist;

    for (Vertex v : route) {
      if (recentV != null) {
        e = recentV.findEdge(v);
	if (e != null) {
	  dist = ew.getNumber(e).intValue();
	  System.out.println(dist);
	  distSum += dist;
	}
      }
      System.out.print(((Derivation)v).StringRep + " ");
      recentV = v;
    }
    System.out.println("\nTotal node distance: " + distSum);
  }

  /**
   * Print the edges along a route along with the distance weights of
   * the edges
   * @param g the graph
   * @param route the route - list of vertices in order of traversal
   */
  public static void printRouteEdgeDistances(Graph g, List<Edge> route) {
    if ((route == null) || (route.size() <= 0)) {
      System.out.println("\nNull route");
      return;
    }

    EdgeWeight ew = EdgeWeight.getEdgeWeightInstance();
    Edge eg;
    Vertex recentV;
    int distSum = 0;
    int dist;

    eg = route.get(0);
    recentV = ((DirectedEdge)eg).getSource();

    for (Edge e : route) {
      if (recentV != null) {
	System.out.print(((Derivation)recentV).StringRep + " ");
      }
      dist = ew.getNumber(e).intValue();
      System.out.println(dist);
      recentV = ((DirectedEdge)e).getDest();
      distSum += dist;
    }
    System.out.println("\nTotal edge distance: " + distSum);
  }
}
