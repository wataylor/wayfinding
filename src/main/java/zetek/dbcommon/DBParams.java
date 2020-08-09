/* @name DBParams.java

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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import zetek.common.CommandArgs;

/**
 * Keep track of database connection parameters regardless of source.
 * Parameter acceptance order is based on the data structures which
 * are passed to the constructor.  When a constructor is passed more
 * than one data source, the sources are processed from left to right.
 * Parameter values from later sources override earlier values.  A
 * null source is ignored.</p>

 * <P>Any parameter values found in
 * /usr/local/tomcat/common/classes/Site.properties are processed
 * <i>after</i> the map but </i>before</i> the named .properties file
 * and the command line; they thus override properties in web.xml but
 * can be overridden by properties in the named parameter file.</p>

 * <P>Note that database property names in the named .properties file
 * must be preceded by the name of the file.  That is, database
 * properties in a file named <code>Panel.properties</code> should be
 * encoded thus:

<pre>
Panel.user=dynWay
Panel.userPW=dynWay
Panel.dbOwner=Zetek
Panel.dbType=mysql
Panel.dbName=dynWay
Panel.dbHost=localhost
Panel.machineID=1
Panel.jdbcDriver=com.mysql.jdbc.Driver
</pre>

 * Other properties in such files should NOT have the file name
 * prepended to the name of the property.  Database properties should NOT
 * have the file name prepended in Site.properties:

<pre>
user=dynWay
userPW=dynWay
dbOwner=Zetek
dbType=mysql
dbName=dynWay
dbHost=localhost
machineID=1
jdbcDriver=com.mysql.jdbc.Driver
</pre>

 * After reading all of the specified parameter sources in the
 * specified order, the program attempts to open a connection to the
 * database.

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class DBParams implements IDBParams {

  public static final long serialVersionUID = 1;

  /** Define the global logger.  It is intended that this logger will
   * be left on at all times.  Other loggers may be turned off for
   * speed.*/
  public static Logger logger = null;

  public String driverName;
  public String url;
  public String dbType;
  public String dbHost;
  public String user;
  public String passwd;
  public String dbName;
  public String log4jConf;
  public Map<String, String> paramMap;
  public String message = null;
  public boolean nolog = false;
  public boolean borrowed = false;

  /** The machine ID is actually a 16 bit unsigned integer, but Java
   * does not have such a data type.  Thus, the machine ID is stored
   * as an integer.  All conversions between these integers and the
   * machine IDs are done by OR ing and AND ing.*/
  public int machineID;

  public Connection conn;
  public ResourceBundle bundle;
  public Properties props;
  public CommandArgs ca;

  /** Obligatory constructor.*/
  public DBParams() { this(null,null,null); }

  /**
   * Construct a database connection based on up to 3 different
   * parameter sources scanned from left to right.  Later sources
   * override earlier sources for all variables which they specify.
   * @param paramMap parameter map such as are created by Tomcat
   * contexts
   * @param whichDB resource bundle
   * @param ca command argument object
   */
  public DBParams(Map<String, String> paramMap, String whichDB,
		  CommandArgs ca) {

    if (paramMap != null) {
      this.paramMap = paramMap;
      driverName = (String)paramMap.get("jdbcDriver");
      dbType     = (String)paramMap.get("dbType");
      dbHost     = (String)paramMap.get("dbHost");
      user       = (String)paramMap.get("user");
      passwd     = (String)paramMap.get("userPW");
      dbName     = (String)paramMap.get("dbName");
      machineID  = CommandArgs.integerFromString((String)paramMap.get("machineID"));
      log4jConf  = (String)paramMap.get("logProps");
    }

    props = new Properties();
    try {
      props.load(new InputStreamReader(new FileInputStream(new File("/usr/local/tomcat/common/classes/Site.properties"))));
    } catch (Exception e) {
      System.out.println(e);
      props = null;
    }
    if (props != null) {
      String p;
      if ( (p = (String)props.get("jdbcDriver")) != null)   { driverName = p; }
      if ( (p = (String)props.get("dbType")) != null)       { dbType = p; }
      if ( (p = (String)props.get("dbHost")) != null)       { dbHost = p; }
      if ( (p = (String)props.get("user"))   != null)       { user   = p; }
      if ( (p = (String)props.get("userPW")) != null)       { passwd = p; }
      if ( (p = (String)props.get("dbName")) != null)       { dbName = p; }
      if ( (p = (String)props.get("machineID")) != null)    {
	machineID = CommandArgs.integerFromString(p);
      }
      if ( (p = (String)props.get("logProps")) != null)     { log4jConf = p; }
    }

    /* Resource bundles take exception to being asked to produce a
     * value which is not in stock. */
    if ((whichDB != null) && (whichDB.length() > 0)){
      try {
	bundle = ResourceBundle.getBundle(whichDB);
	try {
	  driverName = bundle.getString(whichDB + ".jdbcDriver");
	} catch (Exception e){}
	try {
	  dbType     = bundle.getString(whichDB + ".dbType");
	} catch (Exception e){}
	try {
	  dbHost     = bundle.getString(whichDB + ".dbHost");
	} catch (Exception e){}
	try {
	  user       = bundle.getString(whichDB + ".user");
	} catch (Exception e){}
	try {
	  passwd     = bundle.getString(whichDB + ".userPW");
	} catch (Exception e){}
	try {
	  dbName     = bundle.getString(whichDB + ".dbName");
	} catch (Exception e){}
	try {
	  machineID  = CommandArgs.integerFromString((String)bundle.getString(whichDB + ".machineID"));
	} catch (Exception e){}
	try {
	  log4jConf  = bundle.getString("logProps");
	} catch (Exception e){}
      } catch (Exception e) {
	message = "Missing Bundle " + e.toString();
      }
    }

    if (ca != null) {
      String p;
      this.ca = ca;
      if ( (p = (String)ca.get("jdbcDriver")) != null)   { driverName = p; }
      if ( (p = (String)ca.get("dbType")) != null)       { dbType = p; }
      if ( (p = (String)ca.get("dbHost")) != null)       { dbHost = p; }
      if ( (p = (String)ca.get("user"))   != null)       { user   = p; }
      if ( (p = (String)ca.get("userPW")) != null)       { passwd = p; }
      if ( (p = (String)ca.get("dbName")) != null)       { dbName = p; }
      if ( (p = (String)ca.get("machineID")) != null)    {
	machineID = CommandArgs.integerFromString(p);
      }
      if ( (p = (String)ca.get("logProps")) != null)     { log4jConf = p; }
    }

    if (log4jConf == null) {
      log4jConf = "log4jDebugging.properties";
      System.out.println("Logger properties not specified, using " + log4jConf);
    }
    try {
      /* log 4 j catches errors and does not throw anything.  Arg.  This
       * happens in the case of Tomcat because the default file directory
       * is not set to the Tomcat file root.  Thus, the Tomcat servlet which
       * attaches the database opens the DBParams logger and sets
       * DBParams.logger.  */
      PropertyConfigurator.configure(log4jConf);
    } catch (Exception e) {
      System.out.println("ERR not find logger properties file " + log4jConf);
      nolog = true;
    }

    /* Creating a logger generates many error messages from log 4 j if it has
     * not been able to find its properties file.  In that case, the logger
     * will not actually produce any output; there seems to be no default
     * configuration that would at least print.*/
    logger = Logger.getLogger(DBParams.class);
    if (message != null) {
      if (nolog) { System.out.println(message); }
      logger.error(message);
      message = null;
    }

    url = "jdbc:" +  dbType + "://" + dbHost + "/" + dbName;
    if (machineID == 0) {
      message = "No machine ID specified";
      if (nolog) { System.out.println(message); }
      logger.error(message);
    }

    complete();
  }

  public DBParams(CommandArgs ca) {
    this((Map<String, String>)null, (String)null, ca);
  }

  @Override
  public void specifyDefaultDatabase(String dbname) {

  }
  /** Make sure that the connection is still open or open it.  */
  public Connection forceConn() {
    //System.out.println("Conn " + dbParms.conn);
    try {
      if ((conn == null) || conn.isClosed()) {
	Class.forName(driverName).newInstance();
	conn = DriverManager.getConnection(url, user, passwd);
	if (conn == null) {
	  String whinge = "Cannot connect to db";
	  if (nolog) { System.out.println(whinge); }
	  logger.error(whinge);
	}
      }
    } catch (Exception e) {	// Socket Exception is thrown and not caught!
      conn = null;
      message = "DB not connected " + e.toString();
      if (nolog) { System.out.println(message); }
      logger.error(message);
    }
    return conn;
  }

  @Override
  public Connection borrowConnection(String dbname) {
    return borrowConnection();
  }

  @Override
  public Connection borrowConnection() {
    if (borrowed) {
      logger.error("Connection was not returned.");
    } else {
      borrowed = true;
    }
    return forceConn();
  }

  @Override
  public void returnConnection(Connection conn, Statement st, ResultSet rs) {
    if (!borrowed) {
      logger.error("Connection was not borrowed");
    } else {
      borrowed = false;
    }

    try {
      if (rs != null) { rs.close(); }
      if (st != null) { st.close(); }
    } catch (Exception e) {logger.warn("DBParams close ERR " + e.toString()); }
  }

  /** Try to open the DB connection so that errors occur early.*/
  private void complete() {
    try {
      Class.forName(driverName);
      if ( (conn = DriverManager.getConnection(url, user, passwd)) == null) {
        /* If the connection fails to open, there should be an
	   exception, so this ought not to occur, nevertheless, safety
	   and all that. */
	message = "DB connection failed " + url;
	if (nolog) { System.out.println(message); }
        logger.error(message);
        return;
      }
    } catch (Exception e) {
      e.printStackTrace();
      message = "DB Conn " + url + " " + e.toString();
      if (nolog) { System.out.println(message); }
      logger.error(message);
      return;
    }
    // returnConnection(conn, null, null);
    message = "Connected to DB " + url;
    if (nolog) { System.out.println(message); }
    logger.debug(message);
  }

  /**
   * Search through the various possibilities in priority order to
   * return the value of a named property.  Searches the data sources
   * in the following order:

   * <UL>
   * <li>Command arg structure if present</li>
   * <li>Caller-specific resource bundle if present</li>
   * <li>/usr/local/tomcat/common/classes/Site.properties</li>
   * <li>value map if present</li>
   * </ul>

   * @param which name of the property
   * @return property value or null if the property is not found in any of the
   * property data structures.
   */
  @Override
  public String getParameter(String which) {
    String val;

    if ((ca != null) &&
	( (val = (String)ca.get(which)) != null)) { return val; }

    if (bundle != null) {
      try {
	return bundle.getString(which);
      } catch (Exception e) {}
    }

    if ((props != null)    &&
	( (val = (String)props.get(which))    != null)) { return val; }
    if ((paramMap != null) &&
	( (val = (String)paramMap.get(which)) != null)) { return val; }

    return null;
  }
}
