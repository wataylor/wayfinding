/* Building.java

  Copyright (c) 2008 by Zetek Corporation (All Rights Reserved)

*/

package dbaccess;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import zetek.common.ProcessManager;
import zetek.dbcommon.BaseSQLClass;
import dbaccess.bridge._Building;
import dbaccess.bridge._TC;

/** Class to extend the class which was generated to hold Building data
  * by incorporating business logic for objects stored in the database.
  * <B>NOTE</b>: Adding methods whose names begin with either
  * "get" or "set" will cause errors when writing to the database.  */

public class Building extends _Building implements Serializable {

  private static final long serialVersionUID=1;

  List<FloorMapData> floorMapList = null;

  /** Default constructor needed for automatic instantiation. */
  public Building () {}

/* Methods which implement class-specific business logic.  */
  public Building(Long parentKey, String[] s) {
    if ((!s[0].equals(this.getClass().getSimpleName()) || (s.length != 8))) {
      String message = "Bad " + getClass().getSimpleName() + " " + s.length +
	" " + ProcessManager.stringArrayToString(s);
      System.out.println(message);
      throw new RuntimeException(message);
    }
    setName(s[1]);
    setNickName(s[2]);
    setZip(s[3]);
    setTopStreet(s[4]);
    setLeftStreet(s[5]);
    setRightStreet(s[6]);
    setBottomStreet(s[7]);
    if (parentKey != 0) { writeNewEntity(_TC.ET_Building); }
  }

  /**
   * Return a building based on its name and its zip code.  It is an
   * error for there to be more than one building in a zip code with
   * the same name.  This is OK since 10-digit zip codes uniquely
   * identify a building.  The database does not permit such overlap.
   * @param name building name
   * @param zip building zip code
   * @return building object or null if it is not found
   * @throws runtime exception if there is more than one building with
   * the same name and zip code.  This should not happen because the
   * database should not allow it.
   */
  public static Building fetchBuildingByNameAndZip(String name, String zip) {
    if ((name == null) || (name.length() <= 0) ||
	(zip  == null) || (zip.length()  <= 0)) { return null; }

    @SuppressWarnings("unchecked")
      List builds = BaseSQLClass.getMany(Building.class, null, "Name='" +
					 name.replace("'","\\'") +
					 "' and Zip='" + zip +"'");
    if (builds == null)     { return null; }
    if (builds.size() == 1) { return (Building)builds.get(0); }
    if (builds.size() <= 0) { return null; }
    // Should never happen, data base should not allow this condition
    String message = "Found " + builds.size() + " buildings named " + name +
      " in zip code " + zip;
    System.out.println(message);
    throw new RuntimeException(message);
  }

  public static Long fetchBuildingIDByNameAndZip(String name, String zip) {
    Building bd = fetchBuildingByNameAndZip(name, zip);
    if (bd == null) { return null; }
    return bd.getBuildingID();
  }

  /** Return the building's default location; this is always a space for now.
   * TODO support default location being a location, needs another column.  */
  public Space fetchDefaultLocation() {
    Long locationID;
    if ( (locationID = getLocationID()) == null) { return null; }
    return Space.fetchByPrimaryKey(locationID);
  }

  public List<FloorMapData> fetchFloorList() {
    if (floorMapList != null) { return floorMapList; }

    List<dbaccess.Floor> floors = fetchAllFloorBuildingIDBuildingID();
    floorMapList = new ArrayList<FloorMapData>(floors.size());
    FloorMapData fmd;
    String name;

    for (Floor f : floors) {
      fmd = new FloorMapData();
      floorMapList.add(fmd);
      fmd.floorDWG = f.getFloorDWG();
      if (( (name = f.getDisplayName()) != null) && (name.length() > 0)) {
	fmd.displayName = name;
      } else {
	fmd.displayName = f.getName();
      }
      fmd.constOrder = f.getConstructionOrder().intValue();
    }
    FloorBuildingIDBuildingIDLst = null; // Promote GC
    return floorMapList;
  }

  public String fetchFloorMapJson() {
    StringBuilder sb = new StringBuilder();
    boolean did = false;

    List<FloorMapData> floorList = fetchFloorList();
    sb.append("{");
    for (FloorMapData fmd : floorList) {
      if (did) { sb.append(","); }
      sb.append("\"" + fmd.floorDWG + "\":\"" + fmd.displayName + "\"");
      did = true;
    }
    sb.append("}");
    return sb.toString();
  }

  public class FloorMapData {
    public String floorDWG;
    /** The database table has both a name and a display name.  The
     * display name rules if it is non-null and if it is non-empty.*/
    public String displayName;
    public int constOrder;
  }
}
