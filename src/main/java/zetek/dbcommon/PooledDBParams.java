/* @name PooledDBParams.java

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
import java.util.HashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * This implementation of the IDBParams interface supports any number
 * of named databases which are listed as resources in the Tomcat
 * context.xml configuration file.  This is a sample entry which
 * specifies one database.  In this case, the desired database would
 * be identified as "jdbc/javadb" in the context lookup call.

<pre>
    <Resource name="jdbc/JavaDB" auth="Container"
      type="javax.sql.DataSource" removeAbandoned="true" logAbandoned="true"
      removeAbandonedTimeout="30" maxActive="16"
      maxIdle="4" maxWait="10000" username="java" password="t3st1ng"
      driverClassName="com.mysql.jdbc.Driver"
      validationQuery="select * from javadb.ini limit 1"
      url="jdbc:mysql://localhost/javadb"
    />
</pre>

 * @author WAT
 * @version %I%, %G%
 * @since
 *
 * @see zetek.dbcommon.IDBParams */

public class PooledDBParams implements IDBParams {

  public static final long serialVersionUID = 1;

  String defaultDB;

  HashMap<String, DataSource> dataSources = new HashMap<String, DataSource>();

  /** Obligatory constructor.*/
  public PooledDBParams() { /* */ }

  @Override
  public Connection borrowConnection() {
    return borrowConnection(defaultDB);
  }

  @Override
  public Connection borrowConnection(String dbName) {
    if (dbName == null) {
      throw new RuntimeException("Default database not specified");
    }
    DataSource ds;

    try {
      if ( (ds = dataSources.get(dbName)) == null) {

	Context initCtx = new InitialContext();
	Context envCtx = (Context) initCtx.lookup("java:comp/env");
	ds = (DataSource) envCtx.lookup(dbName);
	dataSources.put(dbName, ds);
      }
      return ds.getConnection();
    } catch (Exception e) {
      throw new RuntimeException("ERR db " + dbName + " " + e.toString());
    }
  }

  @Override
  public String getParameter(String name) {
    /* The database pooler does not have access to the Tomcat
       configuration or context parameters. */
    return null;
  }

  @Override
  public void returnConnection(Connection conn, Statement st, ResultSet rs) {
    try {
      if (rs != null) { rs.close(); }
      if (st != null) { st.close(); }
    } catch (Exception e) {
      System.out.println ("DBParams close ERR " + e.toString());
    }
    try {
      conn.close();		// Return connection to the pool
    } catch (Exception e) {
      throw new RuntimeException("ERR conn close " + e.toString());
    }
  }

  @Override
  public void specifyDefaultDatabase(String defaultDB) {
    this.defaultDB = defaultDB;
  }
}
