/* Device.java

  Copyright (c) 2008 by Zetek Corporation (All Rights Reserved)

*/

package dbaccess;
import java.io.Serializable;
import java.util.List;

import zetek.dbcommon.BaseSQLClass;
import zetek.dbcommon.SQLU;
import dbaccess.bridge._Device;

/** Class to extend the class which was generated to hold Device data
  * by incorporating business logic for objects stored in the database.
  * <B>NOTE</b>: Adding methods whose names begin with either
  * "get" or "set" will cause errors when writing to the database.  */

public class Device extends _Device implements Serializable {

  private static final long serialVersionUID=1;

  /** Default constructor needed for automatic instantiation. */
  public Device () {}

/* Methods which implement class-specific business logic.  */

  /**
   * Return the space or location to which this device is assigned.
   */
  public BaseSQLClass fetchDestination() {
    Location loc;
    Space spc;

    if ((getSpaceID()    != null) &&
        ( (spc = fetchOneSpaceSpaceIDSpaceID()) != null)) { return spc; }
    if ((getLocationID() != null) &&
        ( (loc = fetchOneLocationLocationIDLocationID()) != null)) {
      return loc;
    }
    return null;
  }

  public boolean haveLocation() { return ((getSpaceID() != null) || (getLocationID() != null)); }

  /**
   * Return a device based on its name
   * @param name name of the device
   * @param buildingID identifies the building  TODO use the building
   * ID to condition the device.  Needed for multiple buildings.
   * @return device or null 
   */
  public static Device fetchByName(String name, Long buildingID) {
    if ((name == null) || (name.length() <= 0) || (buildingID == null)) { return null; }
    name = SQLU.toSQLConst(name);

    @SuppressWarnings("unchecked")
    List builds = BaseSQLClass.getMany(Device.class, null, "Name=" +
                                       name + " or DisplayName=" + name
                                       );
  if (builds.size() == 1) { return (Device)builds.get(0); }
  if (builds.size() <= 0) { return null; }
  // Should never happen, data base should not allow this condition
  String message = "Found " + builds.size() + " devices named " + name;
  System.out.println(message);
  throw new RuntimeException(message);
  }
}
