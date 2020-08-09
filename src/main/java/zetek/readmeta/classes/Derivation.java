/* @name Derivation.java

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

package zetek.readmeta.classes;

import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import zetek.graphserve.SGB;
import zetek.graphserve.ShapeSerializer;
import edu.uci.ics.jung.graph.impl.DirectedSparseVertex;

/**
 * Java-specific information derived about a meta data block reference
 * based on data extracted from the .dwg file.  This structure holds
 * all geometric and text attribute data from the .dwg file except
 * that the geometry has been converted to Java screen
 * coordinates.</p>

 * <P>This class extends vertex so that meta data objects can be
 * processed by the graph utilities.

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class Derivation extends DirectedSparseVertex
  implements Comparable<Derivation> {

  public static final long serialVersionUID = 1;

  /** Parameters which specify how to map the drawing data to Java.
   * In general, the same object is used for all block references in a
   * .dwg.*/
  //public DwgToJava dwg;

  /** This shape can be drawn by one call to the graphics 2d which is
   * passed to the component paint method.  It is generated as a
   * polygon when processing a polyline from the .dwg database; it is
   * set as other types when other .dwl classes re passed in.*/
  public Shape theShape;

  /** This is an area representation of the shape.  This is used when
   * computing intersections between Derivations for detecting halls
   * which meet and doors which meet other shapes.*/
  public Area theArea;

  /** Store the bounding rectangle of the shape. */
  public Rectangle2D shapeBounds;

  /** Store the center point of the shape. */
  public Point2D.Double shapeCenter;

  /** The layer in which this block is found.  Being able to find the
   * layer helps in deriving connectivity.*/
  public String layerName;

  /** Remember the node type.  This is redundant with respect to the
   * layer name, but having the information where it can be passed
   * directly to a <code>switch</code> statement is convenient.*/
  public short Ilk = SGB.UNKNOWN_ILK;

  /** Real doors have swing arcs.  This list defines the shapes needed
   * to draw the swing arcs if there are any in the door.  This list
   * should be null for non-doors.*/
  public List<Shape>  doorArcs = new ArrayList<Shape>();
  /** Some objects have lines; they are stored here for display during
   * meta data debugging.  */
  public List<Shape> lineList = null;
  public boolean onlyLines = false;
  public int sideCount = 0;
  public double rotation;
  public boolean debug = false;
  public boolean disconnected = false;
  public String floorName = "";
  public String floorDWG = "";
  public List<Derivation> nestedNodes;

  /** Order in which the floor on which this derivation is located was
   * constructed.  It is important that these numbers be contiguous
   * and without gaps.  They are used in giving instructions for
   * vertical traversals as in instructions for going from floor 3 to
   * floor 5 would say that the user must go up two floors. */
  public short floorOrder = 0;

  /** String representation to identify the derivation.*/
  public String StringRep = "";

  public String toString() { return StringRep; }

  @Override
    public int compareTo(Derivation arg0) {
    return StringRep.compareTo(arg0.StringRep);
  }

  public boolean isOnlyLines() {
    return onlyLines;
  }

  public void setOnlyLines(boolean onlyLines) {
    this.onlyLines = onlyLines;
  }

  public int getSideCount() {
    return sideCount;
  }

  public void setSideCount(int sideCount) {
    this.sideCount = sideCount;
  }

  public String getFloorName() {
    return floorName;
  }

  public void setFloorName(String floorName) {
    this.floorName = floorName;
  }

  public String getFloorDWG() {
    return floorDWG;
  }

  public void setFloorDWG(String floorDWG) {
    this.floorDWG = floorDWG;
  }

  public short getFloorOrder() {
    return floorOrder;
  }

  public void setFloorOrder(short floorOrder) {
    this.floorOrder = floorOrder;
  }

  public short getIlk() {
    return Ilk;
  }

  public void setIlk(short ilk) {
    this.Ilk = ilk;
  }

  public String getStringRep() {
    return StringRep;
  }

  public String getShapeString() {
    if (theShape == null) { return ""; }
    return ShapeSerializer.serializeShape(theShape);
  }

  public void setShapeString(String sh) {
    if ((sh == null) || (sh.length() <= 0)) { return; }
    theShape = ShapeSerializer.syntehsizeShape(sh);
    theArea = new Area(theShape);
    shapeBounds = theShape.getBounds2D();
    shapeCenter = new Point2D.Double(shapeBounds.getCenterX(),
				     shapeBounds.getCenterY());
  }

  /** The method returns the final name derived for the block.  This
   * is a combination of the name assigned it in the.dwg database and
   * one letter from its layer name.  In the case of duplicates, a
   * number is put at the end of the name to make it unique.*/
  public void setStringRep(String stringRep) {
    this.StringRep = stringRep;
  }

  /** Obligatory constructor.*/
  public Derivation() { super(); }

  /**
   * Construct a derivation and tell it how to map points from .dwg
   * space to Java space and which will also contain the graphical
   * representation of the shale along with all of its text
   * attributes.
   * @param floorOrder the order in which the floor to which this derivation
   * belongs was constructed.
   */
  public Derivation(short floorOrder) {
    super();
    // this.dwg = dwg;
    this.floorOrder = floorOrder;
  }
}
