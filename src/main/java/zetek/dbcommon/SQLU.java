/* @name SQLU.java

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

package zetek.dbcommon;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Data base manipulation utilities

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class SQLU {

  public static final long serialVersionUID = 1;

  public static final SimpleDateFormat SQL_DATE_TIME =
    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  public static final SimpleDateFormat SQL_DATE =
    new SimpleDateFormat("yyyy-MM-dd");
  public static final SimpleDateFormat EXCEL_DATE_TIME =
    new SimpleDateFormat("MM/dd/yy hh:mm:ss a");
  public static final SimpleDateFormat EXCEL_DATE =
    new SimpleDateFormat("MM/dd/yy");

  public static final SimpleDateFormat[] DFs = {
    SQL_DATE_TIME, SQL_DATE, EXCEL_DATE_TIME, EXCEL_DATE
  };
  /** Obligatory constructor.*/
  public SQLU() { /* */ }

  /**
   * There are a number of ways in which a string can be empty of meaningful
   * data.  This method tells whether a string is empty or not.
   * @param in input string
   * @return true if the string is null, contains only spaces, is zero
   * length, or is equal to the string "null"
   */
  public static boolean isEmpty(String in) {
    return (in == null) || (in.trim().length() <= 0) || "null".equalsIgnoreCase(in);
  }

  /**
   * Return the first string if it s not empty, otherwise the second string
   * @param firstChoice first string
   * @param secondChoice second string
   * @return first string if it is non-empty, otherwise the second string.
   */
  public static String bestString(String firstChoice, String secondChoice) {
    if (!isEmpty(firstChoice)) { return firstChoice; }
    return secondChoice;
  }

  /**
   * Generate a SQL "in" clause based on splitting the input string on pipe
   * characters
   * @param in input string
   * @return SQL in clause
   */
  public static String innies(String in) {
    String[] s = in.split("\\|");
    StringBuilder sb = new StringBuilder();
    for (String st : s) {
      if (sb.length() > 0) {sb.append(","); }
      sb.append(toSQLConst(st));
    }
    return " IN(" + sb.toString() + ")";
  }

  /**
   * Try very hard to parse an incoming date string.  More date formats may be
   * added over time as needed.
   * @param ds date expressed as a string
   * @return date object if possible
   */
  public static Date getDate(String ds) {
    for (int i = 0; i<DFs.length; i++) {
      try {
        return DFs[i].parse(ds);
      } catch (ParseException e) { /* */ }
    }
    return null;
  }

  /**
   * Turn a string into an acceptable SQL string
   * @param in input string.
   * @return string with quotes as required or the empty string if the
   * input string is null.
   */
  public static String quoteForSQL(String in) {
    if (in == null) { return ""; }
    return in.replace("'", "\\'"); // replaceAll is for regexp replace
  }

  /**
   * Turn a string into an SQL constant
   * @param in input string to be quoted as needed
   * @return quoted string surrounded by single quotes
   */
  public static String toSQLConst(String in) {
    return "'" + quoteForSQL(in) + "'";
  }

  /** Execute any update or insert statement against the database.
      @param query insert or update query
      @return null on success, error message on SQL error. */
  public static String anyStatement(String query) throws RuntimeException {
    Statement stmt = null;
    /**/

    try {			// stmt.close may throw exception
      try {
	stmt = BaseSQLClass.dbParams.conn.createStatement();
	stmt.execute(query);
      } finally {
	if (stmt != null) { stmt.close(); }
      }
    } catch (Exception e) {
      return ("anyStatement excp " + query + " " +
				 e.toString());
    }
    return null;
  }

  /** Turn the string or strings in a result set of a query into a list
      which is intended to be used to generate a selection list.
      @param query which has been properly formatted for the current database
      @return list with one element per row of results.  If more than
      one column was selected, the column values from a row are
      concatenated and a | is put between the first column and the rest
      of the columns in each row; the code to generate the selection list
      splits the string so that the string before the | is the selection
      value and the rest of the string is displayed.  The list is empty
      if the search has no results.
      @exception RuntimeException on SQL error.*/
  public static List<String> listQuery(String query) {
    List<String> list;		// Results returned to caller
    Statement getRow = null;	// Retrieve all the rows
    StringBuffer value;		// Result of the query
    ResultSet results = null;	// List of all values in the query
    ResultSetMetaData meta = null;
    int columnCount;
    int i;
    /**/

    list = new ArrayList<String>();
    value = new StringBuffer();

    try {
      getRow  = BaseSQLClass.dbParams.conn.createStatement();
      results = getRow.executeQuery(query);
      meta = results.getMetaData();
      columnCount = meta.getColumnCount();
      while (results.next()) {
	value.setLength(0);
	for (i=1; i<=columnCount; i++) {
	  if (i == 2) { value.append("|"); }
	  if (i > 2)  { value.append(" "); }
	  value.append(results.getString(i));
	}
	if (value.length() > 0) {
	  list.add(value.toString());
	}
      }
    } catch (Exception e) {
      throw new RuntimeException("ListQuery excp " + query + " " +
				 e.toString());
    } finally {
      try {
	if (results != null) { results.close(); }
	if (getRow  != null) { getRow.close();  }
      } catch (SQLException e) {
      }
    }
    return list;
  }

  /**
   * Generate a list returning all rows from a query.  Columns
   * are converted to strings which are separated by pipe characters.
   * The resulting string can be split to result in an array of strings,
   * one for each column in the order in which they appear in the query.
   * @param query SQL query which may mention multiple columns
   * @return list of strings, one for each row returned by the query.
   */
  public static List<String> listQueryPiped(String query) {
    List<String> list;		// Results returned to caller
    Statement getRow = null;	// Retrieve all the rows
    StringBuffer value;		// Result of the query
    ResultSet results = null;	// List of all values in the query
    ResultSetMetaData meta = null;
    int columnCount;
    int i;
    /**/

    list = new ArrayList<String>();
    value = new StringBuffer();

    try {
      getRow  = BaseSQLClass.dbParams.conn.createStatement();
      results = getRow.executeQuery(query);
      meta = results.getMetaData();
      columnCount = meta.getColumnCount();
      while (results.next()) {
	value.setLength(0);
	for (i=1; i<=columnCount; i++) {
	  if (i > 1) { value.append("|"); }
	  value.append(results.getString(i));
	}
	if (value.length() > 0) {
	  list.add(value.toString());
	}
      }
    } catch (SQLException e) {
      throw new RuntimeException("listQueryPiped excp " + query + " " +
				 e.toString());
    } finally {
      try {
	if (results != null) { results.close(); }
	if (getRow  != null) { getRow.close(); }
      } catch (SQLException e) { /* */ }
    }
    return list;
  }

  /** Safely convert a result set string to a non-null string,
      returning a non-null empty string if the result set does not
      have enough strings or if the selected string is null.  The
      effect is to be able to scan a result set without generating a
      SQLException or having to process a null string.
      @param result result set from a prior SQL query.
      @param w selects a string from the result set
      @return the string or the empty string if the column is null or if w
      is out of range for the result set*/
  public static String stringFromResult(ResultSet result, int w) {
    String str;
    /**/

    try {
      str = result.getString(w);
    } catch (Exception e) { str = ""; }
    if (str == null) { return ""; }
    return str;
  }
}
