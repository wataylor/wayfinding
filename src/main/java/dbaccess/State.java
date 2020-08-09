/* State.java

  Copyright (c) 2008 by Zetek Corporation (All Rights Reserved)

*/

package dbaccess;
import java.io.Serializable;

import zetek.dbcommon.BaseSQLClass;
import dbaccess.bridge._State;

/** Class to extend the class which was generated to hold State data
  * by incorporating business logic for objects stored in the database.
  * <B>NOTE</b>: Adding methods whose names begin with either
  * "get" or "set" will cause errors when writing to the database.  */

public class State extends _State implements Serializable {

  private static final long serialVersionUID=1;

  /** Default constructor needed for automatic instantiation. */
  public State () {}

/* Methods which implement class-specific business logic.  */
  public static Long fetchStateIDByAbbrev(String abbrev) {
    State st = (State)BaseSQLClass.getOne(State.class, null, "StateAbbrev", abbrev);
    if (st == null) { return null; }
    return st.getStateID();
  }
}
