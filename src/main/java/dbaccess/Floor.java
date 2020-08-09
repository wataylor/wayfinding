/* Floor.java

  Copyright (c) 2008 by Zetek Corporation (All Rights Reserved)

*/

package dbaccess;
import java.io.Serializable;
import java.util.List;

import zetek.common.ProcessManager;
import zetek.dbcommon.BaseSQLClass;
import dbaccess.bridge._Floor;
import dbaccess.bridge._TC;

/** Class to extend the class which was generated to hold Floor data
  * by incorporating business logic for objects stored in the database.
  * <B>NOTE</b>: Adding methods whose names begin with either
  * "get" or "set" will cause errors when writing to the database.  */

public class Floor extends _Floor implements Serializable {

  private static final long serialVersionUID=1;

  /** Default constructor needed for automatic instantiation. */
  public Floor () {}

/* Methods which implement class-specific business logic.  */
  public Floor(Long parentKey, String[] s) {
    if ((!s[0].equals(this.getClass().getSimpleName()) || (s.length != 4))) {
      String message = "Bad " + getClass().getSimpleName() + " " + s.length +
	" " + ProcessManager.stringArrayToString(s);
      System.out.println(message);
      throw new RuntimeException(message);
    }
    setBuildingID(parentKey);
    setName(s[1]);
    setSortOrder(Long.valueOf(s[2]));
    setConstructionOrder(Long.valueOf(s[2]));
    setFloorDWG(s[3]);
    if (parentKey != 0) { writeNewEntity(_TC.ET_Floor); }
  }

  public static Floor fetchFloorByNameAndBuildingID(Long buildingID,
						    String name) {
    if ((name == null) || (name.length() <= 0) ||
        (buildingID  == null)) { return null; }

    @SuppressWarnings("unchecked")
      List floors = BaseSQLClass.getMany(Floor.class, null, "Name='" +
					 name.replace("'","\\'") +
					 "' and BuildingID=" + buildingID);
    if (floors.size() == 1) { return (Floor)floors.get(0); }
    if (floors.size() <= 0) { return null; }
    // Should never happen, data base should not allow this condition
    String message = "Found " + floors.size() + " floors named " + name +
      " in building " + buildingID;
    System.out.println(message);
    throw new RuntimeException(message);
  }

  public static Long fetchFloorIDByNameAndBuildingID(Long buildingID,
						     String name) {
    Floor fl = fetchFloorByNameAndBuildingID(buildingID, name);
    if (fl == null) { return null; }
    return fl.getFloorID();
  }
}
