package zetek.dbcommon;

import java.math.BigInteger;

import java.util.Hashtable;
import java.util.HashSet;

/**
 * Map various data types back and forth.  See also ReflectionUtils in
 * the zetek.common package.

 * @author W Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class SQLTypesToJava {

  /**
   * Map SQL data types to the corresponding Java data types.
   */
  public static Hashtable<String, String> typeMap =
    new Hashtable<String, String>();
  static {			// SQL type -> java type
    typeMap.put("BIT",               "Boolean");
    typeMap.put("bit",               "Boolean");
    typeMap.put("bigint",            "Long");
    typeMap.put("bigint unsigned",   "Long");
    typeMap.put("int",               "Long");
    typeMap.put("int unsigned",      "Long");
    typeMap.put("smallint",          "Long");
    typeMap.put("smallint unsigned", "Long");
    typeMap.put("tinyint",           "Long");
    typeMap.put("float",             "Float");
    typeMap.put("double",            "Double");
    typeMap.put("timestamp",         "Timestamp");
    typeMap.put("varchar",           "String");
    typeMap.put("char",              "String");
    typeMap.put("text",              "String");
    typeMap.put("blob",              "String");  // This may or may not work
  }

  /**
   * List of columns to be ignored when generating objects which align
   * with SQL tables.
   */
  public static HashSet<String> ignoreCols = new HashSet<String>();

  static {
    ignoreCols.add("Modified");
  }

  public SQLTypesToJava() {
  }

  /**
   * Convert SQL objects to Java objects.  A SQL result set returns
   * a Java object corresponding to the column data type.  These objects
   * are generally appropriate for use in setting methods.  When the
   * object type is not appropriate, however, the object must be
   * converted to another object of the appropriate type.
   * @param o SQL object retrieved from a result set.
   * @return either the original object or an object of a different
   * class with the appropriate value.
   */
  public static Object SQLClassToJava(Object o) {
    if (o instanceof BigInteger) {
      BigInteger bi = (BigInteger)o;
      return new Long(bi.longValue());
    }

    if (o instanceof Integer) {
      Integer i = (Integer)o;
      return new Long(i.longValue());
    }

    return o;
  }
}
