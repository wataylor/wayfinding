/* @name VerbsGraph.java

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

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Set;

import zetek.readmeta.classes.Derivation;
import zetek.server.GraphVerbs;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;

/**
 * Test the graph utilities in the server environment

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class VerbsGraph {

  public static final long serialVersionUID = 1;

  public static String milliToElapsedTime(long milli) {
    String result = "";;
    String units = "";

    double tmpDouble = milli;
    if (tmpDouble > 60*60*24*7*1000) {
      result = tmpDouble/60/60/24/7/1000+"";
      units = " (weeks)";
    }
    else if (tmpDouble > 60*60*24*1000) {
      result = tmpDouble/60/60/24/1000+"";
      units = " (days)";
    }
    else if (tmpDouble >= 60*60*1000) {
      result = tmpDouble/60/60/1000+"";
      units = " (hrs)";
    }
    else if (tmpDouble >= 60*1000) {
      result = tmpDouble/60/1000+"";
      units = " (mins)";
    } else if (tmpDouble >= 1000) {
      result = tmpDouble/1000 +"";
      units = " (secs)";
    } else {
      result = tmpDouble +"";
      units = " (ms)";
    }
    BigDecimal bigD = new BigDecimal(result);
    bigD = bigD.setScale(2,BigDecimal.ROUND_HALF_UP);

    return bigD+units;
   }

  /** Obligatory constructor.*/
  public VerbsGraph() { /* */ }

  /**
   * @param args if the first argument specifies a file name, it is used instead
   * of the compiled-in file name
   */
  public static void main(String[] args) {
    Derivation deriv;
    String filePath;
    Graph g;
    int i = 0;
    long wtart;
    long used;

    if (args.length > 0) {
      filePath = args[0];
    } else {
      filePath = "/media/data/projects/NYST/workFiles/NYST.xml";
    }

    used = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

    wtart = System.currentTimeMillis();
    g = GraphVerbs.loadBuildingGraph(filePath);

    System.out.println(milliToElapsedTime(System.currentTimeMillis() - wtart));

    System.out.println("Graph took " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() - used));

    System.out.println("Edges " + g.numEdges() +
		       " vertices " + g.numVertices());

    Set<Vertex> vertexes  = g.getVertices();
    Iterator<Vertex> vIT= vertexes.iterator();
    while (vIT.hasNext()) {
      deriv = (Derivation)vIT.next();
      System.out.println(deriv.StringRep);
      i++;
      if (i > 20) { break;}
    }
    System.out.println(milliToElapsedTime(System.currentTimeMillis() - wtart));
  }
}
