/* Attribute.java

  Copyright (c) 2008 by Zetek Corporation (All Rights Reserved)

*/

package dbaccess;
import java.io.Serializable;
import dbaccess.bridge._Attribute;

/** Class to extend the class which was generated to hold Attribute data
  * by incorporating business logic for objects stored in the database.
  * <B>NOTE</b>: Adding methods whose names begin with either
  * "get" or "set" will cause errors when writing to the database.  */

public class Attribute extends _Attribute implements Serializable {

  private static final long serialVersionUID=1;

  /** Default constructor needed for automatic instantiation. */
  public Attribute () {}

/* Methods which implement class-specific business logic.  */

}
