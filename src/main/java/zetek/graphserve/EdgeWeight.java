/* @name EdgeWeight.java

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

import edu.uci.ics.jung.graph.ArchetypeEdge;
import edu.uci.ics.jung.graph.decorators.UserDatumNumberEdgeValue;

/**
 * Store edge weights for building graphs.  Recall that all edge
 * properties are stored as .xml strings.  The major distinctions
 * between this class and the class it extends are:

 * <ul>

 * <li>Incoming edge weights are converted to integers.  Dropping the
 * fractional part of an edge weight doesn't affect the shortest path
 * computation significantly.</li>

 * <li>When returning the value, if there is a class cast exception
 * because the edge weight value is stored as a string instead of
 * being stored as a number, the method catches the exception,
 * converts the string to an integer, stores it so that the error will
 * not recur, and returns the numerical value.  This handles the case
 * of weights being stored as strings when the .xml file is read.</li>

 * </ul>

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class EdgeWeight extends UserDatumNumberEdgeValue {

  public static final long serialVersionUID = 1;

  /** The key used to identify edge weight in the user data field. */
  public static final String WEIGHT_KEY = "weight";

  /** The one instance of the edge weight object needed in the system. */
  public static EdgeWeight edgeWeightInstance;

  public static EdgeWeight getEdgeWeightInstance() {
    if (edgeWeightInstance == null) {
      edgeWeightInstance = new EdgeWeight();
    }
    return edgeWeightInstance;
  }

  /** Obligatory constructor.  The copy action is irrelevant because
   * it is always set to shared when the .xml file is converted to a
   * graph.  */
  public EdgeWeight() {
    super(WEIGHT_KEY);
  }

  @Override
  public Number getNumber(ArchetypeEdge e) {
    try {
      return super.getNumber(e);
    } catch (ClassCastException ex) {
      setNumber(e, Integer.valueOf((String)e.getUserDatum(key)));
    }
    return super.getNumber(e);
  }

  @Override
  public void setNumber(ArchetypeEdge e, Number n) {
    super.setNumber(e, n.intValue());
  }
}
