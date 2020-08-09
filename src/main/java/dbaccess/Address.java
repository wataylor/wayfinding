/* Address.java

  Copyright (c) 2008 by Zetek Corporation (All Rights Reserved)

*/

package dbaccess;
import java.io.Serializable;
import java.util.List;

import zetek.common.ProcessManager;
import zetek.dbcommon.BaseSQLClass;
import zetek.dbcommon.SQLU;
import dbaccess.bridge._Address;
import dbaccess.bridge._TC;

/** Class to extend the class which was generated to hold Address data
  * by incorporating business logic for objects stored in the database.
  * <B>NOTE</b>: Adding methods whose names begin with either
  * "get" or "set" will cause errors when writing to the database.  */

public class Address extends _Address implements Serializable {

  private static final long serialVersionUID=1;

  /** Default constructor needed for automatic instantiation. */
  public Address () {}

  public String nullVC(String value) {
    return (value == null) ? " is null" : "='" + value.replace("'","\\'") + "'";
  }

  /** Returns the entity ID of any address in the Address table which
   * has the same values as this address
   * @return entity ID of an identical address or null if there is none.*/
  public Long sameIDInDB() {
    String query = "select AddressID from Address where " +
      "StateID="   + getStateID()            + " and " +
      "CountryID=" + getCountryID()          + " and " +
      "Address1"   + nullVC(getAddress1())   + " and " +
      "Address2"   + nullVC(getAddress2())   + " and " +
      "Address3"   + nullVC(getAddress3())   + " and " +
      "City"       + nullVC(getCity())       + " and " +
      "PostalCode" + nullVC(getPostalCode());
    List<String> l = SQLU.listQuery(query);
    if (l.size() <= 0) { return null; }
    return Long.valueOf(l.get(0));
  }

  public void linkAddresses(Long ownerID, Long addressID, short typeID) {
    String query = "insert into EntityAddress (AddressID,EntityID,AddressTypeID) values (" +
      addressID + "," + ownerID + "," + typeID + ")";
    SQLU.anyStatement(query);
  }

/* Methods which implement class-specific business logic.  */
  public Address(Long parentKey, String[] s) {
    if ((!s[0].equals(this.getClass().getSimpleName()) || (s.length != 7))) {
      String message = "Bad " + getClass().getSimpleName() + " " + s.length +
	" " + ProcessManager.stringArrayToString(s);
      System.out.println(message);
      throw new RuntimeException(message);
    }

    Long childKey = null;

    if (s[1].length() > 0) { setAddress1(s[1]); }
    if (s[2].length() > 0) { setAddress2(s[2]); }
    if (s[3].length() > 0) { setCity(s[3]);     }
    if (BaseSQLClass.dbParams != null) {  // AVoid using the database if none
        setStateID(State.fetchStateIDByAbbrev(s[4])); 
        setCountryID(Country.fetchCountryIDByName(s[6]));
      } else {
        setStateID(1l);
        setCountryID(1l);
      }
    if (s[5].length() > 0) { setPostalCode(s[5]); }

    // Make sure that all required parameters are set
    if (Address1Changed && CityChanged && StateIDChanged && CountryIDChanged) {
      if ( (childKey = sameIDInDB()) == null) {
	writeNewEntity(_TC.ET_Address);
	childKey = getAddressID();
      }
    }

    if (childKey == null) { return; } // Had a bad address

    // Link the address to the parent object
    linkAddresses(parentKey, childKey, _TC.AdT_Entrance);
  }
}
