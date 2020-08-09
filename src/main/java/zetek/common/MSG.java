/* @name MSG.java

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

package zetek.common;

/**
 * Distribute messages to a variety of locations

 * @author WEB Development
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class MSG {

  public static final long serialVersionUID = 1;

  /** Obligatory constructor.*/
  public MSG() { /* */ }

  /**
   * Send a message to various destinations.
   * @param untoward if true, include the message in the list of
   * recent messages which is displayed to users via the Admin
   * interface.
   * @param inLog if true, include the message in the system log.
   * The system log routine operates benignly on systems which do not
   * support a system log.
   * @param message text of the message
   */
  public static void disMess(boolean untoward, boolean inLog,
			     String message) {
    // message = Thread.currentThread().getName() + " " + message;
    if (!inLog)   { System.out.println(message); }
    if (inLog)    { SystemLogging.writeToLog(message); }
    if (untoward) { }
  }

  /** Dump the entire stack of the current thread to System.out.  */
  public static void disStack() {
    disStack(Thread.currentThread(), 9999);
  }

  /** Dump part of the stack of the current thread to System.out.
   * @param many how many stack frames to dump  */
  public static void disStack(int many) {
    disStack(Thread.currentThread(), many);
  }

  /** Dump the stack of the specified thread to System.out
   * @param thread the thread whose stack to dump.
   * @param many how many levels of the stack to dump*/
  public static void disStack(Thread thread, int many) {
    disStack(false, false, thread, many);
  }

  /** Dump some portion of the stack of the specified thread as specified
   * @param inMonitor if true, include the message in the vector of
   * recent messages which is displayed to users via the Admin
   * interface.
   * @param inSyslog if true, include the message in the system log.
   * The system log routine operates benignly on systems which do not
   * support a system log.
   * @param thread the thread whose stack to dump.
   * @param many how many levels of the stack to dump*/
  public static void disStack(boolean inMonitor, boolean inSyslog,
			      Thread thread, int many) {
    StackTraceElement[] traces = thread.getStackTrace();
    MSG.disMess(inMonitor, inSyslog, "");
    for (StackTraceElement ste : traces) {
      MSG.disMess(inMonitor, inSyslog, ste.getClassName() + " " +
		      ste.getLineNumber());
      if (many-- <= 0) { break; }
    }
  }

  /**
   * Print a chain of root causes.
   * @param cause top cause in the chain, must not be null
   * @param printStack if true, prints the stack of the last cause in
   * the chain
   * @return String listing the causes and optionally the stack of the
   * last cause.
   */
  public static String stringCauses(Throwable cause, boolean printStack) {
    StringBuffer sb = new StringBuffer(cause.toString());
    Throwable cause2;
    int many = 10;

    cause2 = cause.getCause();

    while (cause2 != null) {
      cause = cause2;
      sb.append("\n" + cause.toString());
      cause2 = cause.getCause();
    }
    if (printStack) {
      StackTraceElement[] traces = cause.getStackTrace();
      for (StackTraceElement ste : traces) {
	sb.append("\n" + ste.getClassName() + " " + ste.getLineNumber());
	if (many-- <= 0) { break; }
      }
    }
    return sb.toString();
  }
}
