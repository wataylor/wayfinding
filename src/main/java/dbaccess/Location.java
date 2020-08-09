/* Location.java

  Copyright (c) 2008 by Zetek Corporation (All Rights Reserved)

*/

package dbaccess;
import java.io.Serializable;
import java.util.List;

import zetek.common.ProcessManager;
import zetek.dbcommon.BaseSQLClass;
import zetek.dbcommon.SQLU;
import dbaccess.bridge._Location;
import dbaccess.bridge._TC;

/** Class to extend the class which was generated to hold Location data
  * by incorporating business logic for objects stored in the database.
  * <B>NOTE</b>: Adding methods whose names begin with either
  * "get" or "set" will cause errors when writing to the database.  */

public class Location extends _Location implements Serializable {

  private static final long serialVersionUID=1;

  /** Default constructor needed for automatic instantiation. */
  public Location () {}

/* Methods which implement class-specific business logic.  */
  public Location(Long parentKey, String[] s) {
    if ((!s[0].equals(this.getClass().getSimpleName()) || (s.length != 5))) {
      String message = "Bad " + getClass().getSimpleName() + " " + s.length +
        " " + ProcessManager.stringArrayToString(s);
      System.out.println(message);
      throw new RuntimeException(message);
    }
    setSpaceID(parentKey);
    setName(s[1]);
    setLocationTypeID(new Long(s[2]));
    setX(new Double(s[3]));
    setY(new Double(s[4]));
    if (parentKey != 0) { writeNewEntity(_TC.ET_Location); }
  }

  /**
   * Return a location by name.  Locations and Spaces are equivalent and each
   * has two names, the display name and the .dwg name.  The display names are
   * used first.
   * @param buildingID Identifies the building wherein a location is sought.
   * @param name one of the two names of a space or location
   * @return location or space object or null.
   */
  @SuppressWarnings("unchecked")
   public static BaseSQLClass findLocationByName(Long buildingID,
						 String name) {
    if ((name == null) || (name.length() <= 0)) { return null; }

    String constSQL = SQLU.toSQLConst(name);
    String bid = "b.BuildingID=" + buildingID + " and ";
    String spcWh = "left join Floor f on (f.FloorID = Space.FloorID) left join Building b on (b.BuildingID = f.BuildingID)";
    List<BaseSQLClass> locs;

    locs = BaseSQLClass.getMany(Space.class, spcWh,
				bid + "Space.DisplayName=" + constSQL);
    if ((locs != null) && (locs.size() > 0)) { return locs.get(0); }

    String locWh = "left join Space s on (s.SpaceID = Location.SpaceID) left join Floor f on (f.FloorID = s.FloorID) left join Building b on (b.BuildingID = f.BuildingID)";
    locs = BaseSQLClass.getMany(Location.class, locWh,
				bid + "Location.DisplayName=" + constSQL);
    if ((locs != null) && (locs.size() > 0)) { return locs.get(0); }

    locs = BaseSQLClass.getMany(Space.class, spcWh,
				bid + "Space.Name=" + constSQL);
    if ((locs != null) && (locs.size() > 0)) { return locs.get(0); }

    locs = BaseSQLClass.getMany(Location.class, locWh,
				bid + "Location.Name=" + constSQL);
    if ((locs != null) && (locs.size() > 0)) { return locs.get(0); }
    return null;
  }
}
