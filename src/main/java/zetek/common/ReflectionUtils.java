/* @name ReflectionUtils.java

   Copyright (c) 2002-2008 Zetek Corporation (All Rights Reserved)

-------- Licensed Software Proprietary Information Notice -------------

This software is a working embodiment of certain trade secrets of
Zetek Corporation.  The software is licensed only for the
day-to-day business use of the licensee.  Use of this software for
reverse engineering, decompilation, use as a guide for the design of a
competitive product, or any other use not for day-to-day business use
is strictly prohibited.

All screens and their formats, color combinations, layouts, and
organization are proprietary to and copyrighted by Zetek Corporation.

All rights are reserved.

Authorized Zetek customer use of this software is subject to the
terms and conditions of the software license executed between Customer
and Zetek Corporation.

------------------------------------------------------------------------

*/

package zetek.common;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;

/**
 * Utilities which use the java.lang.Reflect package

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class ReflectionUtils {

  public static final long serialVersionUID = 1;

  /** Obligatory constructor.*/
  public ReflectionUtils() { /* */ }

  /**
   * Utility to set object attributes whose attribute types are other
   * than String. All attribute setting methods are assumed to take
   * only one parameter.

   * @param value
   *            A string representation of the value
   * @param method
   *            the method which sets the attribute
   * @param theObject
   *            the object to which the method is to be applied
   * @throws runtime exception if the string cannot be converted to
   * the required parameter type.
   */
  public static Object putSomething(String value, Method method,
                                    Object theObject, Logger logger) {
    Class<?>[] params = method.getParameterTypes();
    Object[] o = new Object[1];
    if (params[0] == String.class) {
      o[0] = value;
    } else if (params[0] == double.class) {
      o[0] = Double.valueOf(value);
    } else if (params[0] == boolean.class) {
      o[0] = Boolean.valueOf(value);
    } else if (params[0] == int.class) {
      o[0] = Integer.valueOf(value);
    } else if (params[0] == short.class) {
      o[0] = Short.valueOf(value);
    } else if (params[0] == float.class) {
      o[0] = Float.valueOf(value);
    } else if (params[0] == long.class) {
      o[0] = Long.valueOf(value);
    } else {
      logger.error("Cannot call set methods with objects of class " +
                        params[0].getSimpleName());
    }
    try {
      method.invoke(theObject, o);
    } catch (Exception e) {
      logger.error(e.toString());
    }
    return o[0];
  }
}
