/* @name SystemLogging.java

    Copyright (c) 2007 by Advanced Systems and Software Technologies.
    All Rights Reserved
*/

package zetek.common;

/**
 * Write to the system log on Unix systems.  It does nothing
 * on Windows systems.

 * @author WEB Development
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class SystemLogging {

  public static final long serialVersionUID = 1;

  /** Windows and other deficient operating systems lack a system-wide
   * logging facility.  This flag is set on the first logging failure
   * so that the logger knows not to try again.  */

  static boolean dead = false;

  /** Obligatory constructor.*/
  public SystemLogging() { /* */ }

  /**
   * Write a message to the system log unless there has been a logging
   * failure in which case, return immediately.  The assumption is
   * that the only reason for this method to fail is that the
   * underlying OS lacks a logging facility.  */
  public static void writeToLog(String message) {
    if (dead) { return; }

    String[] whatToLog = {"logger", "-i", "-t", "dynWay", message};

    try {
      Runtime.getRuntime().exec(whatToLog);
    } catch (java.io.IOException e) {
      System.out.println("ERR Log failure " + " " + message + " " +
			 e.toString());
      dead = true;
    }
  }
}
