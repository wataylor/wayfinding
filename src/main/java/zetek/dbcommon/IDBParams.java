/* @name IDBParams.java

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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Interface which is supported by the classes which implement database
 * access.  there are two such classes - one which uses the Tomcat
 * database pool and one which does not.  The routines which use the
 * database facility borrow connections and return them after use.  The
 * underlying implementing classes handle these functions in different
 * ways.

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see zetek.dbcommon.DBParams
 */

public interface IDBParams {

  /**
   * Return a usable database connection.  In the pool implementation,
   * returns a connection from the pool.  In the non-pool
   * implementation, returns the one connection to the database.  The
   * caller must NOT close the connection; it is returned to the pool
   * or to the DB utility by the returnConnection method.
   */
  public abstract Connection borrowConnection();

  /**
   * Return a usable database connection.  In the pool implementation,
   * returns a connection from the pool.  In the non-pool
   * implementation, returns the one connection to the database.  The
   * caller must NOT close the connection; it is returned to the pool
   * or to the DB utility by the returnConnection method.
   * @param dbname the name of the database desired.  The pool version
   * supports access to many databases at the same time.  Throws a run
   * time error if the database is not found.
    */
  public abstract Connection borrowConnection(String dbName);

  /**
   * The non-pool version supports only one database at a time.  The
   * pool version supports many databases.  Code written for the
   * non-pool version need not know the database name.  To support use
   * of such code in pool systems, this method must be called to
   * supply the default database which is used when a caller borrows a
   * connection from an unnamed database.
   * @param dbname the name of the default database for calls which do
   * not specify a database. */
  public abstract void specifyDefaultDatabase(String dbName);

  /**
   * Return a database connection to the pool after the caller which
   * obtained it through borrowConnection is finished using it.  The
   * class either recycles the connection by closing it or keeps it
   * open for the next user.
   * @param conn database connection to be recycled.
   * @param stmt database statement.  If not null, the statement is closed
   * @param rs database result set.  If not null, the result set is closed.  */
  public abstract void returnConnection(Connection conn,
					Statement stmt, ResultSet rs);

  /**
   * The data base class obtains DB parameters such as user ID and
   * password from some source.  It can also obtain other parameters
   * in an installation-dependent manner.  This method returns the
   * value of a named parameter.
   * @param pName name of the parameter
   * @return parameter value or null if it is not known
   */
  public abstract String getParameter(String pName);

}
