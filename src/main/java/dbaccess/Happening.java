/* Happening.java

  Copyright (c) 2008 by Zetek Corporation (All Rights Reserved)

*/

package dbaccess;
import java.io.Serializable;
import dbaccess.bridge._Happening;

/** Class to extend the class which was generated to hold Happening data
  * by incorporating business logic for objects stored in the database.
  * <B>NOTE</b>: Adding methods whose names begin with either
  * "get" or "set" will cause errors when writing to the database.  */

public class Happening extends _Happening implements Serializable {

  private static final long serialVersionUID=1;

  /** Default constructor needed for automatic instantiation. */
  public Happening () {
    setExternalTime(new java.sql.Timestamp(System.currentTimeMillis()));
  }

/* Methods which implement class-specific business logic.  */

  public static void thingsHappen(String m, short type) {
    Happening hap = new Happening();
    hap.setDescription(m);
    hap.setHappeningTypeID((long)type);
    hap.writeFromKey(null);
  }
}
