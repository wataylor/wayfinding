/* @name LogManager.java

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

package zetek.test;

import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Exercise the Java system logger to see how it works.  This program was
 * unable to make it work in a sensible manner.
 *
 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class LogProps {

  public static final long serialVersionUID = 1;

  public static void dumpLogger(Logger logger) {
    System.out.println("Name   " + logger.getName());
    System.out.println("Level  " + logger.getLevel());
    System.out.println("Bundle " + logger.getResourceBundleName());
  }

  public static void exerciseLogger(Logger logger) {
    System.out.println("Name " + logger.getName() +
		       " Level " + logger.getLevel());
    logger.finest("Finest test messsage");
    logger.finer("Finer test messsage");
    logger.fine("Fine test messsage");
    logger.config("Config test messsage");
    logger.info("Info test messsage");
    logger.warning("Warning test messsage");
    logger.severe("Severe test messsage");
  }

  public static Logger logger;

  /**
   * @param args
   */
  public static void main(String[] args) {
    LogManager lm = LogManager.getLogManager();
    Enumeration<String>eu;

    eu = lm.getLoggerNames();
    while (eu.hasMoreElements()) {
      System.out.println(eu.nextElement());
    }

    logger = Logger.getLogger("Main");

    logger.finest("Finest test messsage");

    eu = lm.getLoggerNames();
    while (eu.hasMoreElements()) {
      System.out.println(eu.nextElement());
    }

    logger.setLevel(Level.ALL);

    dumpLogger(logger);
    dumpLogger(logger.getParent());

    logger.setLevel(Level.ALL);
    exerciseLogger(logger);

    logger.setLevel(Level.FINEST);
    exerciseLogger(logger);

    logger.setLevel(Level.FINER);
    exerciseLogger(logger);

    logger.setLevel(Level.FINE);
    exerciseLogger(logger);

    logger.setLevel(Level.CONFIG);
    exerciseLogger(logger);

    logger.setLevel(Level.INFO);
    exerciseLogger(logger);

    logger.setLevel(Level.WARNING);
    exerciseLogger(logger);

  }
}
