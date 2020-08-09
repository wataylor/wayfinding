/* @name SessionExpiry.java

    Copyright (c) 2008 by Advanced Systems and Software Technologies.
    All Rights Reserved<br>

*/

package zetek.server.utils;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

/**
 * SessionExpiry objects are intended to be attached to sessions as
 * session attributes.  Because this class implements the
 * HttpSessionBindingListener interface, this object is notified when
 * it is first attached to the session and when it is detatched.</p>

 * <P>The object constructor is passed an instance of the
 * SessionDestroyNotifyListener class whose actions are
 * application-dependent.  This object is notified when the session is
 * invalidated so that it can carry out application-dependent cleanup
 * actions.  It is up to the application to retrieve the
 * <code>SessionDestroyNotifyListener</code> and call its
 * <code>cancelTimeout</code> method if the session is closed
 * normally.

 * @author wat
 * @version %I%, %G%
 * @since

 * @see zetek.server.utils.SessionDestroyNotifyListener
 */

public class SessionExpiry implements HttpSessionBindingListener {

  public static final long serialVersionUID = 1;

  /** Obligatory constructor.*/
  public SessionExpiry() {
  }

  /** Constructor.which specifies the object which is to be notified
   * when the session expires.
   @param listener is the object whose notification method is to be
   called when the session terminates or times out.*/
  public SessionExpiry(SessionDestroyNotifyListener listener) {
    this.listener = listener;
  }

  /** Return the listener.  The listener implements various methods
   * which may be of use to the task which set this object as a
   * session attribute. */
  public SessionDestroyNotifyListener getListener() {
    return this.listener;
  }

  /** Called when the object is bound to the session.  Nothing has to
   * be done except to verify that the object has a
   * SessionDestroyNotifyListener object defined. */
  public void valueBound(HttpSessionBindingEvent e) {
    if (listener == null) {
      System.out.println("SessionExpiry bound without a listener");
    }
  }

  /** Called when the session is invalidated.  Although
   * the application may remove this object at any time, the
   * convention is to leave this object on the session until the
   * session expires.  If the user logs out so as to terminate the
   * session, it is up to the application to suppress whatever cleanup
   * might occur by retrieving the listener object and calling its
   * cancelTimeout method.*/
  public void valueUnbound(HttpSessionBindingEvent e) {
    if (listener != null) {
      listener.theSessionHasExpired(e.getSession()); // Pass the word
      System.out.println(new java.util.Date().toString() + " called expiry cleanup");
      listener = null;		// Promote garbage collection
    }
  }

  /** Object to be notified when the session is invalidated or times
   * out.  This object is set non-null before the object is bound to
   * the session but it is not notified of the binding because the
   * binding occurs when the object is instantiated; any initialization
   * is assumed to have been carried out by the constructor. */
  private SessionDestroyNotifyListener listener = null;
}
