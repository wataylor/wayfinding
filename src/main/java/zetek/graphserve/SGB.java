/* @name SGB.java

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

import java.awt.geom.Rectangle2D;

import org.apache.log4j.Logger;

/**
 * Constants and variables which are shared between the meta data manager and
 * the server

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class SGB {

  public static final long serialVersionUID = 1;

  /** Obligatory constructor.*/
  public SGB() { /* */ }

  /** Value of a servlet parameter of the same name which tells which
   * algorithm to use to traverse shapes.  This permits
   * experimentation without necessitating compilation.*/
  public static int shapeTraverse = 1;
  /** Value of a servlet parameter of the same name which tells
   * whether to smooth paths across shapes or not.  To smoose or not
   * to smoose, that is the question.*/
  public static boolean smoosePaths = true;

  /** Used by PathMaker, this tells how long to extend the ends of the
   * line which is parallel to the line hit to make a new avoidance
   * point.  An avoidance point is created to try and avoid a
   * collision. */
  public static double parallelDistance = 20;

  /** Used by PathMaker, tells how to change the local copy of
   * parallelDistance after colliding with the same point twice.  The
   * purpose is to try a different avoidance point and hope to get
   * around the obstacle. */
  public static double parallelDistanceChange = 40;

  /** Used by PathMaker, this tells how far away from the current line
   * hit to create a perpendicular point used to make a new avoidance
   * point. */
  public static double perpDistance = 20;

  /** The .dwg database includes many block attributes.  These
   * attributes are given names in the block definition and are filled
   * in as users enter the meta data blocks.  This constant specifies
   * the name of one of the attributes, namely, the name
   * attribute.  This string must match what is found in the text
   * extracted from the .dwg database as the tag for the name
   * attribute of each block.   Some of these attributes are passed
   * into the .xml representation of the graph.  */
  public static final String NAME_OF_NAME     = "NAME";
  public static final String NAME_OF_SCALE    = "SCALE";
  public static final String NAME_OF_POSITION = "POSITION";
  public static final String NAME_OF_MIN_EXTS = "MIN_EXTS";
  public static final String NAME_OF_ORIGIN   = "ORIGIN";
  public static final String NAME_OF_MAX_EXT  = "MAX_EXT";
  public static final String NAME_OF_GRAPH_SCL= "SCL";
  public static final String NAME_OF_X_WIDTH  = "XW";
  public static final String NAME_OF_Y_HEIGHT = "YH";
  public static final String NAME_OF_ROTATION = "ROTATION";
  /** Name of the user data edge attribute which stores the location
   * of the door center when a door is taken out of the graph.*/
  public static final String NAME_OF_DOOR_CTR = "DrCr";
  /** Name of the user data edge attribute which tells what kind of
   * edge it is. */
   public static final String NAME_OF_EDGE_ILK = "EjIlk";
  /** Value of the edge ilk for entering an elevator*/
  public static final String EDGE_INTO_ELEV    = "InEl";
  /** Value of the edge ilk for entering a stairway*/
  public static final String EDGE_INTO_STAIR   = "InSt";
  /** Value of edge ilk for other verticals*/
  public static final String EDGE_INTO_OTHER   = "Other";

  /** Name of a hall or vertical which is skipped when door shortcuts
   * are created*/
  public static final String NAME_OF_SKIP_SPACE= "SkSp";

  /** Name of the layer in which halls appear.*/
  public static final String HALL_LAYER_NAME = "M-HALL";
  /** Name of the layer in which doors appear.*/
  public static final String DOOR_LAYER_NAME = "M-CONN";
  /** Layer names for the meta data types for the graph.  Blocks in
   * these layers are incorporated into the connectivity graph; blocks
   * from other layers are not.  <B>NOTE:</b> This list of layer names
   * parallels the array of vertex sets in <code>GraphInfo</code>.
   * Any additions or deletions made here must be reflected in that
   * class AND ALSO in the ilk constants below.  */
  public static final String[] LAYER_NAMES = {
    HALL_LAYER_NAME, "M-VERT-STR", "M-ROOM", DOOR_LAYER_NAME, "M-LOC",
  "M-OBJT" };

  /** Define the ending name of a room which defines the outer
   * envelope of the floor.  This room is put into the graph but it is
   * not connected to anything and it is treated in a special manner
   * by the wall gasket window.*/
  public static final String[] ENV_NAMES = {
    "Envelope", "Envelop", "ENVELOPE", "ENVELOP", "envelope", "envelop"};

  /** The building footprint defines the maximum extent of any part of any
   * floor of the building.  The footprint shape is found in the obstacle
   * layer.*/
  public static final String[] FOOT_NAMES = {
    "footprint", "FOOTPRINT", "Footprint" };

  /** The building scale defines a rectangle which is one foot square.
   * This maps .dwg coordinates to feet.  The scale shape is found in the
   * obstacle layer.*/
  public static final String[] SCALE_NAMES = {
    "scale", "SCALE", "Scale" };

  /** Define the name of the node which refers to the closest emergency exit
   * door.  Both the server and the meta data handler need this name.*/
  public static final String ANY_EMERG = "Nearest Emergency Exit";

  /** The edge weight cost of going one floor on a stairway or elevator.
   * This has nothing to do with distance; this cost is intended to discourage
   * the route program from going up one floor and then back down to shorten
   * travel distance on the original floor.  The path finder uses verticals
   * when there is no other alternative, of course.*/
  public static final double VERT_FLOOR_COST = 1000d;
  /** The edge weight cost of entering a vertical.  This is intended to
   * persuade the path finder to minimize the use of distinct vertical
   * pathways.  If this is not set, the program tends to use several
   * different stairways if the distances between the entry points to the
   * stairs on the source or destination floor is large.  This weight
   * on entering any vertical reduces the use of verticals for a partial
   * ascent.*/
  public static final double VERT_ENTRY_COST = 1000d;

  /** Constant to define a graph node whose type is unknown.  The
   * value of this constant must NOT parallel the LAYER_NAMES array
   * above.*/
  public static final short UNKNOWN_ILK = -1;

  /** Constant to define a graph node type as a door.  The value of
   * this constant must parallel the LAYER_NAMES array above.*/
  public static final short DOOR_ILK = 3;

  /** Constant to define a graph node type as a hall.  The value of
   * this constant must parallel the LAYER_NAMES array above.*/
  public static final short HALL_ILK = 0;

  /** Constant to define a graph node type as a vertical.  The value of
   * this constant must parallel the LAYER_NAMES array above.*/
  public static final short VERTICAL_ILK = 1;

  /** Constant to define a graph node type as a room.  The value of
   * this constant must parallel the LAYER_NAMES array above.*/
  public static final short ROOM_ILK = 2;

  /** Constant to define a graph node type as a location.  The value of
   * this constant must parallel the LAYER_NAMES array above.*/
  public static final short LOCATION_ILK = 4;

  /** Constant to define a graph node type as an obstacle.  The value of
   * this constant must parallel the LAYER_NAMES array above.  One of the
   * obstacles should be named "footprint" which is a bounding rectangle
   * which defines the maximum extent of any floor of the building in any
   * direction.*/
  public static final short OBSTACLE_ILK = 5;

  /** Define the attributes of the rectangular projection taken from
   * the .dwg database.  TODO make this building-dependent.  All
   * floors in a building must have the same values for these
   * parameters so that the floors line up vertically, but it is not
   * necessary that different buildings have the same values.  */
  public static final Rectangle2D envelope =
    new Rectangle2D.Double(0,0,3150,2370);

  /** Define the corners of the axonometric projection.  It is
   * important that these lines have the right orientation and
   * direction as determined by which point is the first one.  It is
   * believed that most buildings will be able to map their floors
   * into the same axonometric area so long as the scale factors for
   * different tile sizes are settable on a per-building basis.  */
  public static final double ulX = 200d;
  public static final double llX = 1706d;
  public static final double urX = 2696d;
  public static final double lrX = 4457d;

  public static final double ulY = 1279d;
  public static final double llY = 3184d;
  public static final double urY = 200d;
  public static final double lrY = 1579d;

  /** Define the global logger.  It is intended that this logger will
   * be left on at all times.  Other loggers may be turned off for
   * speed.*/
  public static Logger logger = Logger.getLogger(SGB.class);
}
