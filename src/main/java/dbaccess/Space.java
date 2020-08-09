/* Space.java

  Copyright (c) 2008 by Zetek Corporation (All Rights Reserved)

*/

package dbaccess;
import java.io.Serializable;
import java.util.List;

import zetek.common.ProcessManager;
import zetek.dbcommon.SQLU;
import dbaccess.bridge._Space;
import dbaccess.bridge._TC;

/** Class to extend the class which was generated to hold Space data
  * by incorporating business logic for objects stored in the database.
  * <B>NOTE</b>: Adding methods whose names begin with either
  * "get" or "set" will cause errors when writing to the database.  */

public class Space extends _Space implements Serializable {

  private static final long serialVersionUID=1;

  /** Default constructor needed for automatic instantiation. */
  public Space () {}

/* Methods which implement class-specific business logic.  The Java split
 * method does not return an empty string if there is an empty string
 * after the last occurrence of the split target.  Thus, if a space has an
 * empty string for the shape, the string array will have 3 elements.*/
  public Space(Long parentKey, String[] s) {
    if ((!s[0].equals(getClass().getSimpleName()) || ((s.length != 4) && (s.length != 3)))) {
      String message = "Bad " + getClass().getSimpleName() + " " + s.length +
	" " + ProcessManager.stringArrayToString(s);
      System.out.println(message);
      throw new RuntimeException(message);
    }
    setFloorID(parentKey);
    setName(s[1]);
    setSpaceTypeID(Long.valueOf(Integer.valueOf(s[2])+1));
    if (s.length == 4) {
      setShape(s[3]);
    } else {
      setShape("");
    }
    if (parentKey != 0) { writeNewEntity(_TC.ET_Space); }
  }

  /**
   * Return a list of strings which define the shapes for all blocked spaces
   * on a particular floor of a building
   * @param bID building ID
   * @param floorDWG name of the CAD drawing for the floor
   * @return list of blocked shapes which must be frobnicated.  A shape may
   * be empty in which case the select entry returns the empty string.
   * Empty strings are removed before the list is returned.
   */
  public static List<String> fetchBlockedShapesOnFloor(Long bID, String floorDWG) {
    String query = "select Shape from Space s where Blocked='Y' and s.FloorID=(select FloorID from Floor f where f.BuildingID=" + bID + " and f.FloorDWG=" + SQLU.toSQLConst(floorDWG)+ ")";
    List<String> blockages = SQLU.listQuery(query);
    blockages.remove("");
    return blockages;
  }
}
