/* Organization.java

  Copyright (c) 2008 by Zetek Corporation (All Rights Reserved)

*/

package dbaccess;
import java.io.Serializable;
import java.util.List;

import zetek.dbcommon.BaseSQLClass;
import zetek.dbcommon.SQLU;
import dbaccess.bridge._Organization;

/** Class to extend the class which was generated to hold Organization data
  * by incorporating business logic for objects stored in the database.
  * <B>NOTE</b>: Adding methods whose names begin with either
  * "get" or "set" will cause errors when writing to the database.  */

public class Organization extends _Organization implements Serializable {

  private static final long serialVersionUID=1;

  /** Default constructor needed for automatic instantiation. */
  public Organization () {
    super();
    setOrganizationTypeID(1l);   // Default organization type, can be changed
  }

  public Organization(Long buildingID) {
    this();
    setBuildingID(buildingID);
  }
/* Methods which implement class-specific business logic.  */

  /**
   * Return the space or location to which this organization is assigned.
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

  /**
   * Find an organization based on its name and division.  All combinations of
   * name and division are unique for a building
   * @param buildingID identifies the building in which the organiztion resides
   * @param name name of the organization
   * @param div division or department within the organization
   * @return organization or null
   */
  @SuppressWarnings("unchecked")
  public static Organization fetchByNameAndDiv(Long buildingID, String name, String div) {
    if ((name == null) || (name.length() <= 0)) { return null; }
    boolean notD = ((div == null) || (div.length() <= 0));
    List<BaseSQLClass> orgList;

    String where = "BuildingID=" + buildingID + " and Name=" + SQLU.toSQLConst(name) + " and Division=" +
      (notD ? "''" : (SQLU.toSQLConst(div)));
    orgList = BaseSQLClass.getMany(Organization.class, null, where);
    if ((orgList == null) || (orgList.size() <= 0)) { return null; }
    return (Organization)orgList.get(0);
  }

  public boolean haveLocation() { return ((getSpaceID() != null) || (getLocationID() != null)); }

  public boolean putLocationFrom(BaseSQLClass loc) {
    if (loc == null) { return false; }
    if (loc instanceof Space) {
      setSpaceID(loc.fetchKeyOfEntity());
      setLocationID(null);
    } else if (loc instanceof Location) {
      setSpaceID(null);
      setLocationID(loc.fetchKeyOfEntity());
    } else {
      return false;
    }
    return true;
  }

  public String fetchSelector() {
    String div = getDivision();
    if ((div == null) || (div.length() <= 0)) {
      div = "";
    } else {
      div = " - " + div;
    }
    return getName()  + div;
  }
}
