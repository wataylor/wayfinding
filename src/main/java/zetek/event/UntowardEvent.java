/* @name UntowardEvent.java

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

package zetek.event;

import java.util.EventObject;

/**
 * An event that is fired to indicate that the user should be notified that
 * something which is both unexpected and undesirable has occurred.   It is
 * up to the caller to set the variable which indicates the part of the
 * system from which the event originates.  These constants and an array of
 * system component names are maintained in this class.</p>

 * <p>User notifications involve interaction with the Swing UI, so a
 * method which creates a new untoward event must <b>not</b> call
 * <code>fireUntowardEvent</code>.  In order to ensure that the
 * listener is invoked in the event thread, the method should call
 * <code>deferUntowardEvent</code> which later calls
 * <code>fireUntowardEvent</code> in the event thread.  There are examples
 * of calls to <code>deferUntowardEvent</code> in the Alarm Listener.</p>

 * <P>All uses of untoward events require instantiating a member of
 * this class.  Since the time of occurrence is of possible relevance,
 * the constructor records the millisecond of instantiation in the
 * event itself.

 * @author W Taylor
 * @version %I%, %G%
 * @since
 *
 * @see com.zetek.alarm.AlarmListener
 */

public class UntowardEvent extends EventObject {

  public static final long serialVersionUID = 1;

  /** Constant to indicate that the event occurred from a node name.*/
  public static final short NAME = 1;

  /** Constant to indicate that the event occurred from a graph
   * structure issue.  These are generally related to issues in entering meta
   * data.  */
  public static final short GRAPH = 2;

  /** Constant to indicate that the event occurred from the input file.*/
  public static final short FILE = 3;

  /** Constant to indicate that the event occurred in the xml processing
   * system*/
  public static final short XML = 4;

  /** Constant to indicate that an internal error occurred.*/
  public static final short INT_ERROR = 5;

  /** Constant to indicate that the event occurred from errors in
   * using the program.*/
  public static final short USAGE = 6;

  /** Constant to define an informational message*/
  public static final short INFO = 7;

  /** COnstant to define a user input error */
  public static final short ERROR = 8;

  /** Indicate that there is no message, the goal is to force the
   * display to come up.*/
  public static final short NOMSG = 9;

  /** Indicate that there is no message, the goal is to force the
   * messages into a properties object.*/
  public static final short PROPMSG = 10;

  /** Describe the sources of untoward events. */
  public static final String[] ILK_NAMES =
  {"Unknown", "Node Name", "Graph", "Input File", "XML System",
   "Internal Error", "Usage", "Input Error", "Force display", "Properties" };

  /** Indicate the type of event so that the proper icon can be
   * displayed in the dialog box.*/
  public short ilk;
  /** Details for the edification of some user. */
  public String message;
  /** Exception that caused the message or null.  It may be desirable
   * to trace the exception back to find a cause if the user hits the
   * "Details" button in the informative dialog. */
  Exception e;
  /** The specific millisecond when the event was instantiated*/
  long untowardMillis;

  /** The default constructor is used only for events which force display
   * of any pending events.  These events do not have any message content.*/
  public UntowardEvent() {
    super("");
    ilk = NOMSG;
  }

  /**
   * This no-message constructor is used to specify the title for the message
   * display panel
   * @param msg text to augment the message frame title.
   */
  public UntowardEvent(String msg) {
    super("msg");
    this.message = msg;
    ilk = NOMSG;
  }
  /** Identify the source of the event as well as the type and the
   * message for the user. */
  public UntowardEvent(Object source, short ilk, String message) {
    super(source);
    this.ilk     = ilk;
    this.message = message;
    untowardMillis = System.currentTimeMillis();
  }

  public UntowardEvent(Object source, short ilk, String message, Exception e) {
    super(source);
    this.ilk     = ilk;
    this.message = message;
    this.e       = e;
    untowardMillis = System.currentTimeMillis();
  }
}
