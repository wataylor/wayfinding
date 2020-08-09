/* @name AnyUser.java

    Copyright (c) 2008 by Advanced Systems & Software Technologies.
    All Rights Reserved

*/

package zetek.server.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import dbaccess.Building;
import dbaccess.Space;

import zetek.common.CommandArgs;

import zetek.dbcommon.BaseSQLClass;
import zetek.dbcommon.SQLU;

/**
 * Session object which stores information about each user.  There
 * are many user attributes; storing each one as a separate session
 * attribute leads to confusion and is unwieldy.  It is far faster to
 * retrieve a public object attribute than a session attribute.  Thus,
 * all sessions are assigned an instance of this class regardless of
 * whether the user is logged in.  User state changes such as log in,
 * log out, and session timeout are tracked in this object.

 * @author wat
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class AnyUser implements SessionDestroyNotifyListener {

  public static final long serialVersionUID = 1;

  public static final int HAS_ALL_PERMS = 0;
  public static final int LOGIN_PERM    = 1;
  public static final int ADMIN_PERM    = 2;

  /** Count the number of people who are logged in at this time.  This
   * number is incremented whenever one of these objects is
   * instantiated and decremented whenever it is detatched from its
   * session.  */
  public static int numberLoggedIn = 0;

  /** Complex classes need empty strings */
  public static final String ES = "";

  /**
   * Extract the user object from the session and return it
   * @param request gives access to the session which gives the user
   * object
   * @return current user object; creates a new one if necessary
   */
  public static AnyUser getUserObject(HttpServletRequest request) {
    AnyUser uo;
    HttpSession session;
    SessionExpiry se;
    /**/

    session = request.getSession();
    if ( (uo = (AnyUser)session.getAttribute("Uo")) == null) {
      uo = new AnyUser();
      uo.setUserParams(request);
      uo.setNotLoggedIn(false); // indicate not to make a log entry
      session.setAttribute("Uo", uo);
      se = new SessionExpiry(uo);
      session.setAttribute("Se", se);
      session.setMaxInactiveInterval(999);
      System.out.println("Sid " + session.getId() + " given uo in " +
			 request.getRequestURI());
    } else {
      //System.out.println("Recycling sid " + session.getId() + " in " +
      //		 request.getRequestURI());
    }
    uo.sessionIDsuff = ";jsessionid=" + session.getId();

    return uo;
  }

  public void findDefaultBuilding() {
    BaseSQLClass.dbParams.forceConn();
    List<String> results = SQLU.listQuery("select count(*) from Building");
    if ((results != null) && (results.size() > 0)) {
      buildingCount = CommandArgs.integerFromString(results.get(0));
      if (buildingCount == 1) {
        @SuppressWarnings("unchecked")
          List<Building> buildings = BaseSQLClass.getLimitedMany(Building.class,
                                                                 null, null, 1);
        if ((buildings != null) && (buildings.size() > 0)) {
          /* Setting the building ID in the object forces the set method to
           * re-read the building and the space name.  This is in effect
           * a cache-clearing operation.  It also sets the default
           * destination.  */
          this.setBuilding(buildings.get(0));
        }
      }
    }
    // this.setBuilding(null); // test error messages for no building    
  }

  /** User object constructor selects the default building if there is one.
   * If there is only one building in the database, it becomes the default
   * building by default.*/
  public AnyUser() {
    numberLoggedIn++;		// Count the number of sessions
    setNotLoggedIn(false);
  }

  /** Method to generate the greeting.*/
  public void setGreeting() {
    if (this.isLoggedIn) {
      this.greeting = "Welcome, " + this.userName + ",";
    } else {
      this.greeting = "Welcome";
    }
    if (this.domainName.length() > 0) {
      this.greeting += " to " + this.domainName + ".";
    } else {
      this.greeting += " to MVAD.";
    }
  }

  /** Return a usable domain string */
  public String getUserDomain() {
    if (this.domainName.length() > 0) {
      return this.domainName;
    }
    return ES;
  }

  /** Method to parse whatever information out of the request is
   * relevant to the user object and then set the greeting based on
   * information obtained thereby.  This is called rather often, but
   * has effect only on the first entry to the site.  That is why the
   * data are left in the user object after the first call.
   @param request HTTP request which may include sponsor parameters.
   @return true if there is no sponsor or there is a valid sponsor.*/
  public boolean setUserParams(HttpServletRequest request) {
    String anyParam;
    boolean ok;
    /**/

    ok = true;
    if ( (anyParam = request.getParameter("dom")) != null) {
      if (domainName.length() <= 0) { // If not already know it
	domainName = anyParam;
      }
    }

    if ( (anyParam = request.getParameter("debug")) != null) {
      //AdminConst.sessionDump = true;
    }

    this.setGreeting();
    return ok;
  }

  /** This entry point is called when the session times out and
   * expires or the object is detatched from the session.  Its job is
   * to log the fact that the person did not log out manually.  It is
   * up to the logOut routine to turn off the timeout when the user
   * logs out normally.*/
  public void theSessionHasExpired(HttpSession session) {
    // String task;
    // String result;
    /**/

    System.out.println("Sid " + session.getId() + " expired, userID " + userID + " Login ID " +
		       loginID + ", No. sessions " + numberLoggedIn);
    if (userID > 0) {
      try {
	System.out.println(userID + " timed out");
	// AdminUtils.logHist(this.loginID, "S", null);
      } catch (Exception e) { /* */ }	// best effort
      setNotLoggedIn(false);
    }
    if (--numberLoggedIn <= 0) {
      try {
	System.out.println(new java.util.Date().toString() + " no active sessions, reset");
	/*
	  task = AdminConst.bundle.getString("LastLogoutHandler");
	  if (task.length() > 1) {
	  System.out.println(task);
	  result = Tasking.StartATask(task);
	  System.out.println("Ran task " + result);
	  } else {
	  System.out.println("No reset task defined");
	  }
	*/
      } catch (Exception e) {
	System.out.println("Reset task excp " + e.toString());
      }
    }
  }

  /** Set permissions based on the input string.  If the string is
   * null, all permissions are left unchanged.  */
  public void setPermissions(String set) {
    if (set == null) {
      this.hasAllPerms  = false;
      this.logInPerm    = false;
      this.adminPerm    = false;
    } else if (StringSetUtils.TestASetStringMember(
		 set,HAS_ALL_PERMS)) {
      this.hasAllPerms  = true;
      this.logInPerm    = true;
      this.adminPerm    = true;
    } else {
      this.hasAllPerms = false;
      this.logInPerm   = StringSetUtils.TestASetStringMember(
	set,LOGIN_PERM);
      this.adminPerm   = StringSetUtils.TestASetStringMember(
	set,ADMIN_PERM);
    }
  }

  /** Return the best guess at the default login ID for a login attempt*/
  public String getLoginID() {
    if (!ES.equals(loginID)) return loginID;
    return ES;
  }

  /** Clear all parameters to indicate that no one is logged in. */
  public void setNotLoggedIn(boolean log) {

    if (log) {
      // AdminUtils.logHist(this.loginID, "O", null);
    }

    this.loginID  = ES;
    this.editID   = 0;
    this.greeting = ES;
    this.userName = ES;
    this.sortableUserName = ES;
    this.lastLogin = ES;
    this.uAgent = ES;
    this.setPermissions((String)null);
    this.msg        = ES;
    this.sb.setLength(0);
    this.defaultDestination = null;
    this.currentBuilding    = null;
    this.recentAlarmRoute   = null;

    this.userID = 0;	// Nobody is logged in

    findDefaultBuilding(); // It is not necessary to be logged in to see maps1
  }

  /**
   * Verify that the password is correct for the current user
   * @param password what might be the current password
   * @return true if the password is correct
   */
  public boolean verifyPassword(String password) {
    if ((password == null) || (password.length() <= 0)) { return false; }
    List<String> vec = SQLU.listQuery("select ID from Users where Password='" + password + "' and ID=" + userID);
    return (vec.size() > 0);
  }

  /**
   * Change the current user's password
   * @param newPassword new password for the current user
   */
  public void updatePassword(String newPassword) {
    if ((newPassword == null) || (newPassword.length() <= 0)) { return; }
    SQLU.anyStatement("update Users set Password='" + newPassword + "' where ID=" + userID);
  }

  /** Authenticate a password and login ID, save history information
   * for later display on success or failure.  */
  public boolean authenticate(String loginID, String password,
			      HttpServletRequest request)
    throws RuntimeException, SQLException {
    ResultSet results = null;	// Results of the retrieval
    Statement stmt;		// Retrieve the user record
    String query;
    /**/

    BaseSQLClass.dbParams.forceConn();
    stmt  = BaseSQLClass.dbParams.conn.createStatement();

    query = "select ID,Permissions,NamePrefixID,FirstName,MiddleName,LastName,NameSuffixID,RemoteAddr,RemoteHost,DATE_FORMAT(LastLogin,'%Y%m%d%H%i%s'),TimesLoggedIn from " +
      "Person" + " where LoginID = '"
      + loginID.replace("'","\\'") + "' and Password='" +
      password.replace("'","\\'") + "' and Deleted='n'";
    try {			// Finally closes the statement
      results = stmt.executeQuery(query);

      if (results.next()) {
	this.loginID  = loginID;
	userID   = results.getInt(1);
	this.setPermissions(results.getString(2));
	userName = results.getString(4) + " " + results.getString(5);
	sortableUserName = results.getString(5) + " " + results.getString(4);
	if (results.getInt(11) > 0) {
	  this.lastLogin =
	    results.getString(10) +
	    " from " + results.getString(8) + "/" + results.getString(9);
	} else {
	  this.lastLogin = ES;
	  SQLU.anyStatement("update " + "Person" +
			    " set FirstLogin=null where ID=" +
			    userID);
	}
	results.close();
	if (request != null) {
	  uAgent = request.getHeader("user-agent");
	  uHost  = request.getRemoteHost();
	  uAddr  = request.getRemoteAddr();
	}

	stmt.execute("update " +
		     "Person" +
		     " set TimesLoggedIn=TimesLoggedIn+1,LastLogin = null, UserAgent='" + uAgent +
		     "', RemoteAddr='" + uAddr +
		     "', RemoteHost='" + uHost +
		     "' where ID=" + userID);
	//AdminUtils.logHist(LoginID, "S", request);
	this.setGreeting();
	if (!this.logInPerm) {
	  //AdminUtils.logHist(LoginID, "N", request);
	  this.setNotLoggedIn(false);
	  return false;
	}

	this.isLoggedIn = true;
	return true;
      }

      //AdminUtils.logHist(LoginID, "P", request);
      this.setNotLoggedIn(false);
      return false;

    } catch (SQLException e) {
      throw new RuntimeException("Authenticate excp " + query + " " +
				 e.toString());
    } finally {
      if (results != null) { results.close(); }
      if (stmt    != null) { stmt.close();    }
    }
  }

  /** This entry is called when the person logs out normally and there
   * is no need to show a timeout logout.  The object is processed
   * when the session is deactivated so the object must be told not to
   * log the logout failure.*/
  public void cancelTimeout() {
    userID = 0;
  }

  /** Data generation*/
  public String toString() {
    return "ID " + userID + " " + loginID +
      " ed:" + editID + " " + editState + " name" + userName;
  }

  /** Set the current building and read the default route start
   * location for the building.  This requires clearing out
   * variables which are associated with the building.*/
  public void setBuilding(Building building) {
    Space space;
    String name;

    recentAlarmRoute   = null;
    currentLocation    = null;
    defaultDestination = null;
    editObject         = null;
    floorID            = 0l;

    if ( (this.currentBuilding = building) == null) { return; }

    if ( (space = Space.fetchByPrimaryKey(building.getLocationID())) !=
	 null) {
      if ( (name = space.getDisplayName()) == null) {
	name = space.getName();
      }
      /* Express the start point as a JSON object.  TODO make it
       * possible to have other types of default start point.  The
       * entity table gives the entity type for each entity. */
      this.defaultDestination = "{\"ilk\":0,\"sug\":\"" + name +"\"}";
    }
  }

  /** Set the building ID whenever it changes and record the default
   * location for the building.  The default location is returned as
   * the start point fill-in whenever a new route is started.*/
  public void setBuildingID(long bID) {
    if (bID == 0) {   // NO building, wipe out all building data
      currentBuilding    = null;
      recentAlarmRoute   = null;
      currentLocation    = null;
      defaultDestination = null;
      editObject         = null;
      floorID            = 0l;
      return;
    }

    if ((currentBuilding == null) || (currentBuilding.getBuildingID() != bID)) {
      this.floorID    = 0l;	// No floor selected
      this.defaultDestination = ES;

      setBuilding(Building.fetchByPrimaryKey(bID));
    }
  }

  /** Return a Java Script representation of the current building.*/
  public String getBuildingJSON() {
    if (currentBuilding == null) { return "undefined"; }
    return new JSONObject(currentBuilding, true).toString();
  }

  /** Variable which stores the unique database table ID of the person
   * who was logged in on the session which timed out.  The session is
   * not available to the timeout method so the object must store
   * enough information to carry out its task. */
  public int userID = 0;

  /** Variable which stores the string login ID of the person who was
   * logged in on the session which timed out.  This is the text
   * username typed into the login screen.  The session is not
   * available to the timeout method so the object must store enough
   * information to carry out its task. */
  public String loginID = ES;

  /** Greeting string.*/
  public String greeting = ES;

  /** Name string, holds the full user name after log in.*/
  public String userName = ES;

  /** Sortable name string, sorts by last name*/
  public String sortableUserName = ES;

  /** Describes the most recent login */
  public String lastLogin = ES;

  /** Current user agent*/
  public String uAgent = ES;
  /** Current user host*/
  public String uHost  = ES;
  /** Current user IP address*/
  public String uAddr  = ES;

  /** Current user type */
  public String userType;

  /** Originating domain name*/
  public String domainName = ES;

  public boolean hasAllPerms;	// Checkbox
  public boolean logInPerm;	// Checkbox, if not, can't log in
  public boolean adminPerm;	// Admin perm

  /** Boolean to indicate whether anyone is logged in or not.*/
  public boolean isLoggedIn = false;

  public int editState = 0;

  /** Edit ID string  */
  public int editID = 0;

  /** Redirect URL, this is in effect a subroutine call*/
  public String redirect = ES;

  public String msg;
  public StringBuilder sb = new StringBuilder();

  public String sessionIDsuff;

  public int buildingCount = 0;
  /** The server remembers the most recent building request.*/
  public Building currentBuilding;
  /** Remember the most recent location selected for editing.  The object
   * may be in one of several different tables, but its primary key can
   * be extracted without knowing its class.  The object type is stored in
   * the entity table.  OR its class can be matched in the class array in
   * StringsManager. */
  public BaseSQLClass currentLocation = null;
  public BaseSQLClass editObject = null;
  /** The server remembers the ID of the most recent floor request.  This is
   * zeroed if there is a building request because requesting a new building
   * invalidates whatever floor requests were outstanding.*/
  public Long floorID;
  /** The default destination depends on the building.  The
   * destination is always a space for now.  It is expressed as a string
   * which can be converted into a Java Script suggestion object.*/
  public String defaultDestination;
  /** When users request alarm routes to be sent to the client, the date
   * of the most recent such alarm is recorded so that the next request
   * moves to the next one in the list.  This string is a date and time
   * expressed as a SQL string.  If it is null, the user has never been
   * sent an alarm.*/
  public String recentAlarmRoute = null;
  /** When clients poll for alarms, they also need to know if any changes
   * have been made to space accessibility.  If so, all client routes must be
   * recomputed.  It is OK to initialize this date to the present because
   * no routes have been generated at the moment this object is
   * instantiated.*/
  public java.util.Date recentAccessNotifiation = new java.util.Date();
}
