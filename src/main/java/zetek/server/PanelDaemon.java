/* @name PanelDaemon.java

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

package zetek.server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import zetek.common.CommandArgs;
import zetek.dbcommon.BaseSQLClass;
import zetek.dbcommon.DBParams;

/**
 * Main program which opens a database connection, listens for panel
 * messages, and writes messages into the database.  It assumes that
 * there will be a file named Panel.propertis somewhere on its class
 * path so that it can find out how to connect to the database and how
 * to manage the alarm panel.

 * TODO expand the program to read a list of a series of serial port
 * names matched to building IDs.  The Happening table has a slot for
 * building ID for all messages.

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class PanelDaemon {

  public static final long serialVersionUID = 1;

  /** Obligatory constructor.*/
  public PanelDaemon() { /* */ }

  public static Logger logger;

  /**
   * Return a list of all context parameters which match a header
   * followed by 0, 1, etc.
   * @param init string to define the beginning of the parameter names
   * @return list of matching parameter values
   */
  public static List<String> getMatchingParams(String init) {
    List<String> params = new ArrayList<String>();
    String anyParam;
    int i = -1;

    do {
      i++;
      if (( (anyParam = BaseSQLClass.dbParams.getParameter(init + i)) == null) ||
	  (anyParam.length() <= 0)) {
	break;
      }
      params.add(anyParam);
    } while (true);
    return params;
  }

  /**
   * @param args ignored except for + vb flag and log 4 j properties file
   */
  public static void main(String[] args) {
    String port = "/media/data/dev/vty1"; // for real, use /dev/ttyS0
    String fileBase = "/tmp";

    boolean verbose;
    int baud = 9600;

    CommandArgs ca = new CommandArgs(args);
    verbose = ca.getBoolean("vb");

    /* Connect to the database */
    BaseSQLClass.dbParams =  new DBParams(null, "Panel", ca);

    logger = Logger.getLogger(DBParams.class);
    logger.debug("Testing the panel message receiver");

    /* The port may be defined either in web.xml or in a resource
     * bundle so that ports can differ between development and
     * deployment.  this program has no access to web.xml, however. */
    port     = BaseSQLClass.dbParams.getParameter("port");
    fileBase = BaseSQLClass.dbParams.getParameter("fileBase");
    baud     = CommandArgs.integerFromString(BaseSQLClass.dbParams.getParameter("baud"));

    AlarmServices.AlarmMonitor mon =
      new AlarmServices().new AlarmMonitor(port, baud, fileBase, logger);

    mon.setNotifies(getMatchingParams("NOTIFY"));
    mon.setClears  (getMatchingParams("CLEAR"));
    mon.setMakeMaps(getMatchingParams("ALARM"));

    if (verbose) {
      File file = new File("./");
      System.out.println("Current Directory " + file.getAbsolutePath());

      System.out.println("Port " + port + " " + baud);

      System.out.print("Notifies " );
      for (String s : mon.notifies) {
	System.out.print(" " + s);
      }
      System.out.println();

      System.out.print("Clears " );
      for (String s : mon.clears) {
	System.out.print(" " + s);
      }
      System.out.println();

      System.out.print("Alarms " );
      for (String s : mon.makeMaps) {
	System.out.print(" " + s);
      }
      System.out.println();
    }

    if (!mon.streamOKP()) {
      if (verbose) { System.out.println("Panel port did not open for input"); }
      logger.error("Panel port did not open for input");
      System.exit(-1);
    }

    mon.start();
    if (verbose) { System.out.println("Started endless receive loop"); }
    logger.debug("Started endless receive loop");
  }
}
