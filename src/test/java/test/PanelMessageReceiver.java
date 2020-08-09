/* @name PanelMessageReceiver.java

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

package test;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import zetek.common.CommandArgs;
import zetek.dbcommon.BaseSQLClass;
import zetek.dbcommon.DBParams;
import zetek.server.AlarmServices;

/**
 * Test the server thead that parses and reports panel messages.

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class PanelMessageReceiver {

  public static final long serialVersionUID = 1;

  /** Obligatory constructor.*/
  public PanelMessageReceiver() { /* */ }

  public static Logger logger;
  /**
   * @param args
   */
  public static void main(String[] args) {
    String port = "/media/data/dev/vty1"; // for real, use /dev/ttyS0
    String fileBase = "/tmp";
    List<String> list;
    int baud = 9600;

    CommandArgs ca =
      new CommandArgs(new String[] {
	  "logProps=log4jDebugging.properties",
	  "jdbcDriver=com.mysql.jdbc.Driver",
	  "dbHost=localhost",
	  "dbType=mysql",
	  "dbName=dynWay",
	  "user=root",
	  "userPW=mysql",
	  "machineID=1",
	});
    ca.parseArgs(args);		// Pick up user-specified args

    // PropertyConfigurator.configure((String)ca.get("logProps"));

    /* Connect to the database */
    BaseSQLClass.dbParams =  new DBParams(ca);

    logger = Logger.getLogger(DBParams.class);
    logger.debug("Testing the panel message receiver");

    AlarmServices.AlarmMonitor mon =
      new AlarmServices().new AlarmMonitor(port, baud, fileBase, logger);

    list = new ArrayList<String>(3);
    list.add("WELCOME TO NEW YORK STATE");
    list.add("SYSTEM POWER UP");
    list.add("TROUBLE");
    mon.setNotifies(list);

    list = new ArrayList<String>(3);
    list.add("CLEARED");
    list.add("NETWORK RESET INITIATED");
    list.add("ACKNOWLEDGED");
    mon.setClears(list);

    list = new ArrayList<String>(2);
    list.add("PREALARM VERIFYING DETECTOR");
    list.add("FIRE ALARM");
    mon.setMakeMaps(list);

    if (!mon.streamOKP()) {
      logger.error("Panel port did not open for input");
      System.exit(-1);
    }

    mon.start();
    logger.debug("Started endless receive loop");
  }
}
