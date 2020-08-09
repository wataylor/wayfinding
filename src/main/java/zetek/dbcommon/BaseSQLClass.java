/* @name BaseSQLClass.java

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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Class which is the parent of all generated classes.  This class has
 * methods for getting new keys with which to write new rows and other
 * common operations.  It also serves as a collection of static
 * methods which transfer data between classes and result sets.</p>

 * <P>Note: The code contains various SuppressWarnings tags so that
 * warnings about classes, Hash tables, and other generic classes will be
 * suppressed.  By it nature, this class deals with many different
 * generated classes, so it is rarely possible to specify a data type
 * for a generic.

 * @author W Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class BaseSQLClass {
  /**
   * Database parameters as read from the site table, command line, or
   * set from other sources. */
  public static DBParams dbParams;

  /** Application-specific methods for manipulating the database.  */
  public static IDBCounter appApi;

  /** Define the global logger.  It is intended that this logger will
   * be left on at all times.  Other loggers may be turned off for
   * speed.*/
  public static Logger logger = Logger.getLogger(DBParams.class);

  /**
   * Table which maps classes to the ability to convert them to and
   * from SQL result sets. */
  @SuppressWarnings("unchecked")
  public static Hashtable<Class, SQLTableMap> classToTable =
    new Hashtable<Class, SQLTableMap>(30);

  public BaseSQLClass() {
  }

  /**
   * The SQL row classes maintain dirty bits for each parameter.  This
   * method checks for differences when one or the other might be
   * null.  The two objects are always of the same underlying class.
   * @param o1 first object
   * @param o2 second object
   * @return true if the objects are not equal in the Java sense.
   */
  public static boolean twoParms(Object o1, Object o2) {
    if (o1 == null) {
      return (o2 != null);
    }
    return (!o2.equals(o1));
  }

  /**
   * Close a result and a statement after use.  Some JDBC drivers
   * close statements and result sets automatically, some do not.  Any
   * errors are logged but not otherwise noted.
   * @param stmt A statement or null
   * @param res A result set or null
   */
  public static void closeStatementAndResult(Statement stmt, ResultSet res) {
    try {
      if (res  != null) { res.close();  }
      if (stmt != null) { stmt.close(); }
    } catch (SQLException e) {
      BaseSQLClass.logger.error("SQL Close " + e.toString());
    }
  }

  /**
   * Make a SQL request when there is anticipated to be only one row
   * which matches the where clause.  The difference is that this
   * method generates a message if multiple rows are returned.  This
   * method is used only for columns which have numeric values.  It is
   * anticipated that most usages will be for retrieving against
   * foreign keys which are primary keys in the other table.</p>

   * <P>Note: If there is any possibility that such a request might
   * return multiple rows, call <code>getMany</code> instead.

   * @param aClass class object whose bean attributes match the table
   * of the same name.
   * @param join list of join statements to come after the table name
   * or null.  The where clause comes after any join statements.
   * @param col name of the column to be examined
   * @param val value of the column to be matched.
   * @return the first, or hopefully only, object returned.
   */
  @SuppressWarnings("unchecked")
  public static BaseSQLClass getOne(Class aClass, String join, String col,
				    Object val) {

    if ((col == null) || (val == null)) { return null; }

    List<BaseSQLClass> many = getMany(aClass, join, col, val);
    int len;

    if (many == null) { return null; }
    len = many.size();
    if (len <= 0) {
      return null;
    }
    if (len > 1) {
      BaseSQLClass.logger.error("Multiple rows " + aClass.getName() + " " +
				col + " " + val);
    }
    return many.get(0);
  }

  /**
   * Make a SQL request when there is anticipated to be more than one
   * row which matches the column name and value.  This method is used
   * only for columns which have numeric values.  It is anticipated
   * that most usages will be for retrieving against foreign keys.

   * @param aClass class object whose bean attributes match the table
   * of the same name.
   * @param join list of join statements to come after the table name
   * or null.  The where clause comes after any join statements.
   * @param col name of the column to be examined
   * @param val an object whose toString method gives the value of the
   * column to be matched.
   * @return List of objects whose rows matched the specified column
   * and value.
   */
  @SuppressWarnings("unchecked")
  public static List getMany(Class aClass, String join, String col, Object val) {
    if ((col == null) || (val == null)) { return null; }

    try {
      return getMany(aClass, join, col + "='" +
		     SQLU.quoteForSQL(val.toString()) + "'");
    } catch (NullPointerException e) {
      BaseSQLClass.logger.error("getMany exception " + e.toString());
    }
    return null;
  }

  /**
   * Make a SQL select request when there is anticipated to be more
   * than one row which matches the where clauses.  The entire table
   * is returned if the where string is null.  It is proper to include
   * <code>order by</code> after the <code>where</code> conditions.

   * @param aClass class object whose bean attributes match the table
   * of the same name.
   * @param join list of join statements to come after the table name
   * or null.  The where clause comes after any join statements.
   * @param where string to condition the select statement or null
   * @return List of objects whose rows matched the specified column
   * and value.
   */
  @SuppressWarnings("unchecked")
  public static List getMany(Class aClass, String join, String where) {
    String query;
    query = "select * from " + aClass.getSimpleName() +
      ((join  != null) ? " " + join + " " : "") +
      ((where != null) ? (" where " + where) : "");
    return getAQuery(aClass, query);
  }

  /**
   * Make a limited SQL select request when there is anticipated to be
   * more than one row which matches the where clauses.  The entire
   * table is returned if the where string is null and contains fewer
   * rows than the specified limit.

   * @param aClass class object whose bean attributes match the table
   * of the same name.
   * @param join list of join statements to come after the table name
   * or null.  The where clause comes after any join statements.
   * @param where string to condition the select statement or null
   * @param limit the maximum number of objects to return
   * @return List of objects whose rows matched the specified column
   * and value.
   */
  @SuppressWarnings("unchecked")
  public static List getLimitedMany(Class aClass, String join, String where,
				    int limit) {
    String query;
    query = "select * from " + aClass.getSimpleName() +
      ((join  != null) ? " " + join + " " : "") +
      ((where != null) ? (" where " + where) : "") + " limit " + limit;
    return getAQuery(aClass, query);
  }

  /**
   * Return a linked list of objects which are retrieved from a query.

   * @param aClass defines the class whose attributes match the column
   * names in the query either directly or by use of the "AS" keyword
   * when defining the query
   * @param query which retrieves data from a table
   * @return linked list of objects which contain the rows which
   * matched the query
   */
  @SuppressWarnings("unchecked")
  public static List<BaseSQLClass> getAQuery(Class aClass, String query) {
    List<BaseSQLClass> many = new LinkedList<BaseSQLClass>();
    Object inst;
    Object rso;
    Object[] params;
    ResultSet rs = null;
    ResultSetMetaData meta;
    SQLColumnMap aColMap;
    SQLTableMap tblMap;
    Statement stmt = null;
    String colName;
    String tn;
    String tblName = aClass.getSimpleName();
    int i;
    int numCol;
    /**/

    try {
      dbParams.forceConn();
      stmt = dbParams.conn.createStatement();
      logger.debug(query);
      rs = stmt.executeQuery(query);
      if (rs.next()) {
	tblMap = getClassTableMap(aClass);
	meta = rs.getMetaData();
	numCol = meta.getColumnCount();
	do {
	  inst = aClass.newInstance(); // make the object for the row
	  many.add((BaseSQLClass)inst);
	  for (i=1; i<= numCol; i++) {
	    colName = meta.getColumnName(i);
	    /* A select with joins in it gets all the columns in all the tables
	     * which are joined.  This skips columns which are not part of the
	     * original table.
	     */
	    if (!meta.getTableName(i).equalsIgnoreCase(tblName)) { continue; }

	    // System.out.println(colName);
	    if (SQLTypesToJava.ignoreCols.contains(colName)) { continue; }

	    // Used for debugging, need to know what SQL thinks of
	    // the data type so that the data type can be mapped to
	    // something sensible by SQLTypesToJava.BaseSQLClassToJava
	    tn = meta.getColumnTypeName(i);
	    if ( (aColMap = tblMap.colMaps.get(colName)) == null) {
	      BaseSQLClass.logger.error("getAQuery " + aClass.getSimpleName() +
					" no map for column " + colName +
					" of SQL type " + tn + " " + query);
	      continue;
	    }
	    params = new Object[1];
	    rso = SQLTypesToJava.SQLClassToJava(rs.getObject(i));
	    params[0] = rso;
	    try {
	      aColMap.writeMethod.invoke(inst, params);
	    } catch (IllegalArgumentException e) {
	      BaseSQLClass.logger.error("Cannot set column " + colName +
					" of SQL type " + tn + " " +
					e.toString());
	    }
	  }
	  if (tblMap.clean != null) {
	    // Clear all of the dirty flags if the method exists
	    tblMap.clean.invoke(inst, (Object[])null);
	  }
	} while (rs.next());
      }
    } catch (Exception e) {
      BaseSQLClass.logger.error(e.toString() + " " + query);
      return null;
    } finally {
      closeStatementAndResult(stmt, rs);
    }
    return many;
  }

  /**
   * The get name method SHOULD be overridden in any generated class.  It is
   * put here so that an object can be treated as a BaseSQLClass and be able
   * to deliver its name.  The method is defined as other than abstract
   * because not all generated classes have names.
   * @return a default name
   */
  public String getName() { return "Default Name"; }

  /**
   * The set name method SHOULD be overridden in any generated class.  It is
   * put here so that an object can be treated as a BaseSQLClass and be able
   * to specify its name  The method is defined as other than abstract
   * because not all generated classes have names.
   * @param name is ignored in the default implementation
   * @return
   */
  public void setName(String name) {};

  /** This method should be overridden by derived classes which have actual
   * dirty flags to test*/
  public boolean testDirtyFlags() { return false; }

  /** Return a where string containing the values of all non-null
   * attributes.*/
  @SuppressWarnings("unchecked")
  public String whereString() {
    Class aClass;
    Enumeration enu;
    Object colValue;		// Attributes taken from the object per column
    SQLColumnMap aColMap;
    SQLTableMap tblMap;
    String colName;
    String primaryCol;
    StringBuilder colWheres = new StringBuilder(100);
    /**/

    dbParams.forceConn();
    aClass   = this.getClass();

    try {
      tblMap   = getClassTableMap(aClass); // Table map for the class
      // Need the primary column to skip using it for a where string
      primaryCol = (String)tblMap.getPrimary.invoke(this, (Object[])null);
      // Walk all the columns and generate the where string.

      for (enu = tblMap.colMaps.keys(); enu.hasMoreElements();) {
	colName = (String) enu.nextElement();
	if ( (aColMap = tblMap.colMaps.get(colName)) == null) {
	  BaseSQLClass.logger.error("whereString " + aClass.getSimpleName() +
				    " no map for column " + colName);
	  continue;
	}

	// All objects support a getClass method which is not useful for SQL.
	if ("Class".equals(colName))    { continue; }
	if (aColMap.readMethod == null) { continue; }
	if (primaryCol.equals(colName)) { continue; }

	try {
	  aClass.getField(colName + "Changed");
	} catch (NoSuchFieldException e) {
	  continue;             // Skip if no changed flag, not a column
	}

	colValue = aColMap.readMethod.invoke(this, (Object[])null);
	if (colValue == null) { continue; }

	if (colWheres.length() > 0) {
	  colWheres.append(" and ");
	}
	colWheres.append(colName + "=" + SQLU.toSQLConst(colValue.toString()));
      }

    } catch (SQLException e) {
      BaseSQLClass.logger.error("WNE where " + e.toString() + " " +
				colWheres.toString());
    } catch (Exception e) {
      BaseSQLClass.logger.error("WNE where ", e);
    }
    return colWheres.toString();
  }

  /**
   * Write an object to a table.  The table name is the same as the
   * class name.  Column names are the same as the attribute names
   * which are available via get and set methods.
   * @param entityID the primary key for the new row
   * @return the primary key which was passed in
   */
  @SuppressWarnings("unchecked")
  public Long writeFromKey(Long entityID) {
    Class aClass;
    Enumeration enu;
    Object colValue;		// Attributes taken from the object per column
    SQLColumnMap aColMap;
    SQLTableMap tblMap;
    Statement stmt;
    String primaryCol;
    String colName;
    String query = null;
    StringBuilder colNames = new StringBuilder(100);
    StringBuilder colVals  = new StringBuilder(100);
    /**/

    dbParams.forceConn();
    aClass   = this.getClass();

    try {
      tblMap   = getClassTableMap(aClass); // Table map for the class
      primaryCol = (String)tblMap.getPrimary.invoke(this, (Object[])null);
      // Get the primary column to be able to set the new primary key value
      if ( (aColMap = tblMap.colMaps.get(primaryCol)) == null) {
	BaseSQLClass.logger.error("writeFromKey " + aClass.getSimpleName() +
				  " no map for primary column " + primaryCol);
	return null;
      }
      aColMap.writeMethod.invoke(this, new Object[] { entityID } );

      // Walk all the columns and generate the insert statement.

      for (enu = tblMap.colMaps.keys(); enu.hasMoreElements();) {
	colName = (String) enu.nextElement();
	if ( (aColMap = tblMap.colMaps.get(colName)) == null) {
	  BaseSQLClass.logger.error("writeFromKey " + aClass.getSimpleName() +
				    " no map for column " + colName);
	  continue;
	}

	// All objects support a getClass method which is not useful for SQL.
	if ("Class".equals(colName))    { continue; }
	if (aColMap.readMethod == null) { continue; }

	if (!primaryCol.equals(colName)) {
	  try {
	    aClass.getField(colName + "Changed");
	  } catch (NoSuchFieldException e) {
	    continue;		// Skip if no changed flag, not a column
	  }
	}

	if (colNames.length() > 0) {
	  colNames.append(",");
	  colVals.append(",");
	}
	colNames.append(colName);
	colValue = aColMap.readMethod.invoke(this, (Object[])null);
	if (colValue == null) {
	  colVals.append("null");
	} else {
	  colVals.append("'" + colValue.toString().replace("'","\\'") + "'");
	}
      }

      //System.out.println(colNames);
      //System.out.println(colVals);
      stmt = dbParams.conn.createStatement();
      query = "insert into " + aClass.getSimpleName() + " (" + colNames +
	") values (" + colVals + ")";
      logger.debug(query);
      stmt.execute(query);
      closeStatementAndResult(stmt, null);

      // Reset all the dirty bits in the object
      tblMap.clean.invoke(this, (Object[])null);
    } catch (SQLException e) {
      BaseSQLClass.logger.error("WNE write " + e.toString() + " " + query);
    } catch (Exception e) {
      BaseSQLClass.logger.error("WNE write ", e);
    }
    return entityID;
  }

  /** Write a new entity object into an SQL table.  The base object
      class must match a table name.  This method is used for objects
      whose primary key values must also appear in the entity table.
      It is up to the caller to know whether an object should be
      written as an entity or not.  Compare with <code>writeNewRow()</code>.
      @return primary key of the new row.  */
  public Long writeNewEntity(short entityType) {
    Long  entityID;
    /**/

    /* Get a new entity key for the new object.  */
    entityID = new Long(appApi.makeNewEntityID(entityType));
    return writeFromKey(entityID);
  }

  /** Write an ordinary row object into an SQL table.  The base object
      class must match a table name.  This method is used for objects
      whose primary keys do not appear in the entity table.  It is up
      to the caller to know whether an object should be written as an
      entity or not.  Compare with <code>writeNewEntity()</code>.
      @return primary key of the new row. */
  public Long writeNewRow() {
    Long  entityID;
    /**/

    /* Get a new non-entity key for the new object.  */
    entityID = new Long(appApi.makeNewID());
    return writeFromKey(entityID);
  }

  /** Return the value of the primary key for the object.  The key is
   * stored in a database column, of course, so it is available via a
   * get method if the caller knows the name of the key.  This method
   * is provided to make it possible to get the key value of any
   * object without knowing the name of the class or of the column.
   * <B>Note</b>: This method is not named "get" because that would
   * require that each class have a column named "KeyOfEntity."
   * @return primary key of the object.*/

  @SuppressWarnings("unchecked")
  public Long fetchKeyOfEntity() {
    Class aClass;
    SQLColumnMap aColMap;
    SQLTableMap tblMap;
    String primaryCol;

    aClass     = this.getClass();
    try {
      tblMap   = getClassTableMap(aClass); // Table map for the class
      primaryCol = (String)tblMap.getPrimary.invoke(this, (Object[])null);
      // Get the primary column to be able to set the new primary key value
      if ( (aColMap = tblMap.colMaps.get(primaryCol)) == null) {
	BaseSQLClass.logger.error("fetchKeyOfEntity " + aClass.getSimpleName()+
				  " no map for primary column " + primaryCol);
	return null;
      }
      return (Long)aColMap.readMethod.invoke(this, (Object[])null);
    } catch (Exception e) {
      BaseSQLClass.logger.error("WNE fetch ", e);
    }
    return null;
  }

  /** Update an object which already exists in an SQL table.  The base
      object class must match a table name.  This method can be used
      for either entities or non-entity rows because it does not change
      the existing primary key.
      @return primary key of the row or null if there is an error or
      zero if the object did not have any dirty flags set.  */
  @SuppressWarnings("unchecked")
  public Long updateEntity() {
    Class aClass;
    Enumeration enu;
    Long  entityID = null;
    Object colValue;		// Attributes taken from the object per column
    SQLColumnMap aColMap;
    SQLTableMap tblMap;
    Statement stmt;
    String primaryCol;
    String colName;
    String query = null;
    StringBuilder setVals  = new StringBuilder(100);
    /**/

    if (!testDirtyFlags()) { return 0l; }
    aClass   = this.getClass();

    try {
      tblMap   = getClassTableMap(aClass); // Table map for the class
      primaryCol = (String)tblMap.getPrimary.invoke(this, (Object[])null);
      // Get the primary column to be able to set the new primary key value
      if ( (aColMap = tblMap.colMaps.get(primaryCol)) == null) {
	BaseSQLClass.logger.error("updateEntity " + aClass.getSimpleName() +
				  " no map for primary column " + primaryCol);
	return null;
      }
      entityID = (Long)aColMap.readMethod.invoke(this, (Object[])null);

      // Walk all the columns and generate the update statement.

      for (enu = tblMap.colMaps.keys(); enu.hasMoreElements();) {
	colName = (String) enu.nextElement();
	if ( (aColMap = tblMap.colMaps.get(colName)) == null) {
	  BaseSQLClass.logger.error("updateEntity " + aClass.getSimpleName() +
				    " no map for column " + colName);
	  continue;
	}

	// All objects support a getClass method which is not useful for SQL.
	if ("Class".equals(colName)) { continue; }

	// Do not set the value of the primary key
	if (primaryCol.equals(colName)) { continue; }

	/* A variable without a change flag is not part of the SQL table.
	 * This occurs if a table row does not have a column named "Name"
	 * for example, because this class, from which all generated classes
	 * inherit, has a Name method which is not overridden.  In that case,
	 * "Name" has no change flag, so "Name" ought not to be treated as
	 * a column name.  */
	if (aColMap.changeFlag == null) {
	  // BaseSQLClass.logger.error("No change flag for " + colName);
	  continue;
	}

	// See if the column value has changed
	if (aColMap.changeFlag.getBoolean(this)) {

	  if (setVals.length() > 0) {
	    setVals.append(",");
	  }
	  setVals.append(colName + "=");
	  colValue = aColMap.readMethod.invoke(this, (Object[])null);
	  if (colValue == null) {
	    setVals.append("null");
	  } else {
	    setVals.append("'" + colValue.toString().replace("'","\\'") + "'");
	  }
	}
      }

      if (setVals.length() > 0) {
	stmt = dbParams.forceConn().createStatement();
	query = "update " + aClass.getSimpleName() + " set " + setVals +
	  " where " + primaryCol + "=" + entityID;
	// System.out.println(query);
	logger.debug(query);
	stmt.execute(query);
	closeStatementAndResult(stmt, null);
      }

      // Reset all the dirty bits in the object
      tblMap.clean.invoke(this, (Object[])null);
    } catch (Exception e) {
      BaseSQLClass.logger.error("WNE update " + e.toString() + " " + query);
      e.printStackTrace();
      return null;
    }
    return entityID;
  }

  /**
   * Update an existing table row which is NOT in the entity table.
   * @return primary key of the row.
   */
  public Long updateRow() {
    return updateEntity();
  }

  /**
   * Get property descriptors for all the properties in a class
   * @param aClass class whose descriptors are to be returned
   * @return array of descriptors
   */
  public static PropertyDescriptor[]
  getPropertyDescriptors(Class<?> aClass) {
    PropertyDescriptor descriptors[] = null;
    if (aClass == null) {
      throw new IllegalArgumentException("No class specified");
    }
    // Introspect the bean and cache the generated descriptors
    BeanInfo beanInfo = null;
    try {
      beanInfo = Introspector.getBeanInfo(aClass);
    } catch (IntrospectionException e) {
      return (new PropertyDescriptor[0]);
    }
    descriptors = beanInfo.getPropertyDescriptors();
    if (descriptors == null) {
      descriptors = new PropertyDescriptor[0];
    }
    Introspector.flushCaches(); // The data are stored in cache maps
    return (descriptors);
  }

  /**
   * Return a map giving methods which set and get various attributes
   * of a class which is intended to match a SQL table.  It is up to
   * the caller NOT to use this map for classes which do not match
   * tables.  Stores the map in a cache for future use.
   * @param aClass specifies the class for which a method map is to be
   * created
   * @return class-specific method map
   * @throws SQLException
   */
  @SuppressWarnings("unchecked")
  public static SQLTableMap getClassTableMap(Class aClass)
    throws SQLException {
    SQLTableMap tblMap;
    if ( (tblMap = classToTable.get(aClass)) == null) {
      tblMap = createSqlTblMap(aClass);
      classToTable.put(aClass, tblMap); // cache for later use
    }
    return tblMap;
  }

  /**
   * Create an object which contains information about classes which are used
   * to manipulate SQL table rows.  This method works ONLY for beans which
   * have the following properties:
   <UL>
   <LI>The SQL table name matches the class name exactly. </li>
   <LI>Column names begin with an upper case letter </li>
   <LI>There may be get and put methods ONLY for attributes which match
   column names.  Other attributes use fetch and store.</li>
   </ul>

   * @param aClass the class which is to be converted to and from SQL
   * result set table rows, insert statements, or update statements.
   * @return
   * @throws SQLException
   */
  @SuppressWarnings("unchecked")
  public static SQLTableMap createSqlTblMap(Class aClass)
    throws SQLException {
    BaseSQLClass cp = new BaseSQLClass();
    Field field;
    PropertyDescriptor[] descriptors = null;
    SQLColumnMap aColMap;
    SQLTableMap  tblMap;
    String attrName;
    String colName;
    /**/

    descriptors = getPropertyDescriptors(aClass);
    if (BaseSQLClass.logger.isInfoEnabled()) {
      BaseSQLClass.logger.info("Property names from class " + aClass.getName());
      for (PropertyDescriptor desc : descriptors) {
	BaseSQLClass.logger.info(desc.getName());
      }
    }

    tblMap = cp.new SQLTableMap();
    tblMap.colMaps = new Hashtable<String, SQLColumnMap>(descriptors.length);

    try {
      tblMap.clean      = aClass.getMethod("cleanDirtyFlags");
      tblMap.isDirtyP   = aClass.getMethod("testDirtyFlags");
      tblMap.getPrimary = aClass.getMethod("fetchPrimaryKey");
    } catch (SecurityException e1) {
      BaseSQLClass.logger.debug("Security Exception " + e1.toString());
    } catch (NoSuchMethodException e1) {
      BaseSQLClass.logger.debug("No Method Exception " + e1.toString());
    }

    for (PropertyDescriptor desc : descriptors) {
      attrName = desc.getName();
      colName  = (Character.toUpperCase(attrName.charAt(0)) +
		  attrName.substring(1)).intern();
      field = null;
      try {
	field = aClass.getField(colName + "Changed");
      }  catch (NoSuchFieldException e) {
	if (!"Class".equals(colName)) {	// All classes support getClass
	  BaseSQLClass.logger.info("No changed field " + aClass.getName() +
				   " " + attrName + " " + e.toString());
	}
      } catch (SecurityException e) {
	BaseSQLClass.logger.error("Security Exception " + e.toString());
      }

      aColMap  = cp.new SQLColumnMap();
      aColMap.writeMethod = desc.getWriteMethod();
      aColMap.readMethod  = desc.getReadMethod();

      aColMap.changeFlag  = field;

      tblMap.colMaps.put(colName, aColMap);
    }
    return tblMap;
  }

  /** Class to store infSQLConvertation about the class itself as well as
   * about mappings between SQL column names and object attributes. */
  public class SQLTableMap {
    public Method clean;
    public Method isDirtyP;
    public Method getPrimary;
    public Hashtable<String, SQLColumnMap> colMaps;
  }

  /** Class to store infSQLConvertation about the methods which support one
   * column of the class / table. */
  public class SQLColumnMap {
    public Method writeMethod;
    public Method readMethod;
    public Field  changeFlag;
  }
}
