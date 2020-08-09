/* Country.java

  Copyright (c) 2008 by Zetek Corporation (All Rights Reserved)

*/

package dbaccess;
import java.io.Serializable;
import java.util.List;

import zetek.dbcommon.BaseSQLClass;
import dbaccess.bridge._Country;

/** Class to extend the class which was generated to hold Country data
  * by incorporating business logic for objects stored in the database.
  * <B>NOTE</b>: Adding methods whose names begin with either
  * "get" or "set" will cause errors when writing to the database.  */

public class Country extends _Country implements Serializable {

  private static final long serialVersionUID=1;

  /** Default constructor needed for automatic instantiation. */
  public Country () {}

/* Methods which implement class-specific business logic.  */
  public static Long fetchCountryIDByName(String name) {
    @SuppressWarnings("unchecked")
    List l = BaseSQLClass.getAQuery(Country.class, "select c.CountryID,c.TermID,c.ListOrder from Country c left join EnglishTerm et on (et.TermID = c.TermID) where et.Name = '" + name + "'");
    if ((l == null) || (l.size() <= 0)) { return null; }
    Country c = (Country)l.get(0);
    return c.getCountryID();
  }

}
