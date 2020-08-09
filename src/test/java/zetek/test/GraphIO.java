/* @name GraphIO.java

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

import zetek.common.CommandArgs;
import zetek.io.GraphToXML;
import edu.uci.ics.jung.graph.Graph;

/**
 * Read a graph as an xml file and write it back out for comparison.
 * Unfortunately, node IDs are not preserved so it is difficult to
 * check the graph exhaustively.

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class GraphIO {

  public static final long serialVersionUID = 1;

  /** Obligatory constructor.*/
  public GraphIO() { /* */ }

  /**
   * @param args
   */
  public static void main(String[] args) {
    String fileName = "/media/data/projects/cadResearch/HardOne.xml";
    GraphToXML mlf = new GraphToXML();
    Graph g = mlf.load(fileName);
    mlf.save(g, CommandArgs.newFileType(fileName, "out"));
  }
}
