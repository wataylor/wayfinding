/* Person.java

  Copyright (c) 2008 by Zetek Corporation (All Rights Reserved)

*/

package dbaccess;
import java.io.Serializable;

import zetek.dbcommon.BaseSQLClass;
import dbaccess.bridge._Person;

/** Class to extend the class which was generated to hold Person data
  * by incorporating business logic for objects stored in the database.
  * <B>NOTE</b>: Adding methods whose names begin with either
  * "get" or "set" will cause errors when writing to the database.  */

public class Person extends _Person implements Serializable {

  private static final long serialVersionUID=1;

  /** Default constructor needed for automatic instantiation. */
  public Person () { super(); }

  public Person(Long buildingID) {
    this();
    setBuildingID(buildingID);
  }

/* Methods which implement class-specific business logic.  */

  /**
   * Return the space or location to which this person is assigned.
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
}
