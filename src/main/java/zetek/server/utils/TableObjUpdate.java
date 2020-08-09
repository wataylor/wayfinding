/* @name TableObjUpdate.java

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

package zetek.server.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import zetek.dbcommon.BaseSQLClass;
import zetek.dbcommon.SQLU;
import zetek.dbcommon.BaseSQLClass.SQLColumnMap;
import zetek.dbcommon.BaseSQLClass.SQLTableMap;
import zetek.graphserve.GraphUtilsServer;
import acdb.AcDbPoint;

/**
 * Utilities to update objects which represent table rows.

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class TableObjUpdate {

  public static final long serialVersionUID = 1;

  /** Obligatory constructor.*/
  public TableObjUpdate() { /* */ }

  /**
   * Update a table object based on request parameters whose names match the
   * column names in the SQL table.
   * @param obj the object which represents the value of a SQL table row
   * @param request the request object
   * @param reqHeader all request parameters which carry values for this object
   * begin with this string.  Thus, a field which updates, for example, the
   * Tags column value for this object would be named <reqHeader>Tags in the
   * form which is submitted with the request.
   */
  @SuppressWarnings("unchecked")
  public static boolean updateTableObject(BaseSQLClass obj,
					  HttpServletRequest request,
					  String reqHeader) {
    Enumeration<String> colNames;
    Enumeration<String> paramNames;
    Object[] oRay;
    SQLColumnMap aColMap;
    SQLTableMap tblMap;
    String colName;
    String colValue;		// Attributes taken from the object per column
    String pName;
    String primaryCol;
    String rParam;
    boolean ok = true;

    try {
      tblMap = BaseSQLClass.getClassTableMap(obj.getClass());
      primaryCol = (String)tblMap.getPrimary.invoke(obj, (Object[])null);

      for (colNames = tblMap.colMaps.keys(); colNames.hasMoreElements();) {
	colName = colNames.nextElement();
	if ( (aColMap = tblMap.colMaps.get(colName)) == null) {
	  BaseSQLClass.logger.error("No map for column " + colName);
	  continue;
	}
	// All objects support a getClass method which is not useful for SQL.
	if ("Class".equals(colName)) { continue; }
	// Do not set the value of the primary key
	if (primaryCol.equals(colName)) { continue; }

	// Column name will have been converted to initial lower case
	// when it comes back as a parameter.
	rParam = reqHeader + colName;
	for (paramNames = request.getParameterNames();
	     paramNames.hasMoreElements(); ) {
	  pName = paramNames.nextElement();
	  if (pName.equalsIgnoreCase(rParam)) {
	    colValue = request.getParameter(pName);
	    // Have a string value for the column; figure out how to
	    // convert it and then use the write method.
	    if ( (oRay = makePuttable(colValue, aColMap.writeMethod)) != null) {
	      aColMap.writeMethod.invoke(obj, oRay);
	    } else {
	      ok = false;
	    }
	  }
	}
      }
    } catch (Exception e) {
      String message = "WNE uto " + e.toString();
      BaseSQLClass.logger.error(message, e);
      System.out.println(message);
      ok = false;
    }
    return ok;
  }

  /**
   * Convert a string value to an object which can be used as an input to 
   * the specified method.
   * @param value value to be passed to the method
   * @param method method which takes one parameter
   * @return object which can be passed to the method and which represents
   * the original value in the new format.  The object is passed in an array
   * for convenience in invoking the method.
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   */
  public static Object[] makePuttable(String value, Method method) {
    Class<?>[] params = method.getParameterTypes();
    Object[] o = new Object[1];
    if (params[0] == String.class) {
      o[0] = value;
    } else if (params[0] == AcDbPoint.class) {
      o[0] = GraphUtilsServer.makeStringPoint(value);
    } else if (params[0] == double.class) {
      o[0] = Double.valueOf(value);
    } else if (params[0] == boolean.class) {
      o[0] = Boolean.valueOf(value);
    } else if (params[0] == int.class) {
      o[0] = Integer.valueOf(value);
    } else if (params[0] == short.class) {
      o[0] = Short.valueOf(value);
    } else if ((params[0] == long.class) || (params[0] == Long.class)) {
      o[0] = Long.valueOf(value);
    } else if (params[0] == java.sql.Timestamp.class) {
      o[0] = new java.sql.Timestamp(SQLU.getDate(value).getTime());
    } else {
      String message = ("ERR cannot handle objects of class " +
			params[0].getSimpleName());
      BaseSQLClass.logger.error(message);
      System.out.println(message);
      return null;
    }
    return o;
  }
}
