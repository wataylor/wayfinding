/* @name AlarmServices.java

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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.comm.CommPortIdentifier;
import javax.comm.NoSuchPortException;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONException;

import zetek.common.CommandArgs;
import zetek.common.IntervalPrinter;
import zetek.common.ProcessManager;
import zetek.dbcommon.BaseSQLClass;
import zetek.dbcommon.SQLU;
import zetek.server.utils.AnyUser;
import dbaccess.Device;
import dbaccess.Happening;
import dbaccess.Location;
import dbaccess.bridge._TC;

/**
 * Listen for alarm messages from the panel and handle them.  Panel errata
 * such as the serial port not opening are added to the happening log without
 * a building ID which is why the happening selector allows any message
 * about a panel error regardless of building ID.  TODO it may be necessary to
 * include building ID in the panel messages, but that requires being able to
 * associate a panel input device with a building.

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class AlarmServices extends HttpServlet {

  public static final long serialVersionUID = 1;

  /** This date parser must match the date formst using in inbound
   * panel messages.  Dates are extracted from the input line using
   * the regexp pattern <code>alarmDatePattern</code>.  Unfortunately,
   * the Jave date formatters do not permit A for AM or P for PM.
   * This is awkward.  */
  public static final SimpleDateFormat PANEL_DATE_TIME =
    new SimpleDateFormat("hh:mm:ssa EEE MMM dd,yyyy");
  //                      10:44:55A FRI OCT 19,2007

  AlarmMonitor mon;
  String port = "/media/data/dev/vty1"; // for real, use /dev/ttyS0
  String fileBase = "/tmp";
  int baud = 9600;

  /** Obligatory constructor.*/
  public AlarmServices() { /* */ }

  /**
   * Get a property with a default
   * @param context Tomcat servlet context
   * @param param parameter name
   * @param defa default value - default is a reserved word in Java
   * @return
   */
  public static String getDefParameter(ServletContext context, String param,
				       String defa) {
    String anyParam;
    anyParam = BaseSQLClass.dbParams.getParameter(param);
    if ((anyParam != null) && (anyParam.length() > 0)) { return anyParam; }
    anyParam = context.getInitParameter(param);
    if ((anyParam != null) && (anyParam.length() > 0)) { return anyParam; }
    return defa;
  }

  public static void startPanelDaemon(ServletContext context) {
    String fileRoot = context.getRealPath("/");

    String[] s;
    List<String> cargs = new ArrayList<String>();
    /* First argument is the name of the Java executable*/
    cargs.add(System.getProperty("java.home") + "/bin/java");
    /* Specify the class path based on what Tomcat has for this context*/
    cargs.add("-cp");
    cargs.add((String)context.getAttribute("org.apache.catalina.jsp_classpath"));
    /* Name of the java class to be run*/
    cargs.add("zetek.server.PanelDaemon");
    /* Command line args for the Java program, in particular, its
     * logging parameter file. */
    cargs.add("logProps=" + fileRoot + "WEB-INF/" +
	      context.getInitParameter("log4j"));
    cargs.add("+vb");

    s = cargs.toArray(new String[cargs.size()]);

    ProcessManager.runPluggedCommand(s, null);
  }

  public static void logKillMsgs(List<String> msgs, Logger log) {
    for (String s : msgs) {
      if (s.startsWith("ERR"))      { log.error(s.substring(3, s.length())); }
      else if (s.startsWith("INF")) { log.info (s.substring(3, s.length())); }
      else log.error(s);
    }
  }

  /** Called once before any requests are honored.  This servlet
   * should start after the database servlet; it has a spin wait on
   * the database connection to ensure that the database is OK before
   * it proceeds.
   * @param configuration Servlet Configuration which, among other
   * things, gives access to the Servlet Context which contains
   * parameters specified in the web.xml file. */
  public void init(ServletConfig configuration) {
    List<String> shutDownRes;
    ServletContext context = configuration.getServletContext();

    System.out.println("" + getClass().getSimpleName() + " Init starting");

    /* Wait for the Json servlet to initialize the database
     * connection.  In theory, the Json servlet fires up first.*/
    while (BaseSQLClass.dbParams == null) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        String message = getClass().getSimpleName() +
	  " interrupted " + e.toString();
        if (JsonServlet.logger != null) {
          JsonServlet.logger.error(message);
          Happening.thingsHappen(message, _TC.HT_Panel_Error);
        } else {
          System.out.println(message);
        }
      }
    }

    shutDownRes = ProcessManager.shutDownMatchingTasks("PanelDaemon");
    logKillMsgs(shutDownRes, JsonServlet.logger);

    startPanelDaemon(context);

    System.out.println("" + getClass().getSimpleName() + " Init complete");
  }

  public static void routeUnroutedAlarms(PrintWriter pw,
					 HttpServletRequest request,
					 HttpServletResponse response)
    throws IOException, JSONException {
    long bID;
    if ( (bID= VerbsJson.getBuildingID(request)) <= 0l) {
      FragmentVerbs.whinge(pw, ParamNames.noBuild);
      return;
    }

    AnyUser uo     = AnyUser.getUserObject(request);

    if (uo.recentAccessNotifiation.before(GraphVerbs.accessibilityDelta)) {
      uo.recentAccessNotifiation = new java.util.Date();
      JsonServlet.writeString(pw, "RER");  // Tell the client to re-route
      return;
    }

    int routeID;

    {
      String anyParam;

      if (( (anyParam = request.getParameter(ParamNames.ROUTE_NO)) == null)  ||
	  (anyParam.length() <= 0)) {
	FragmentVerbs.whinge(pw, "No route number in request");
	return;
      }

      routeID = CommandArgs.integerFromString(anyParam);
    }

    if (uo.defaultDestination == null) {
      FragmentVerbs.whinge(pw, "Building has no default destination");
      return;
    }

    IntervalPrinter ip = new IntervalPrinter();
    Date scratch;
    ResultSet rs     = null;    // Result of the retrieval
    Statement stmt   = null;    // Retrieve the row
    String anyParam;
    StringBuilder sb = new StringBuilder("select h.HappeningID,DATE_FORMAT(h.ExternalTime, '%m/%d/%y %T'),h.Description,h.DeviceID,d.DisplayName,d.Name from Happening h left join Device d on (d.DeviceID=h.DeviceID) where h.HappeningTypeID=" + _TC.HT_Alarm + " and (h.Cleared!='Y' or h.Cleared is null) and h.BuildingID=" + bID + ((uo.recentAlarmRoute == null) ? "" : " and h.ExternalTime >" + SQLU.toSQLConst(uo.recentAlarmRoute)) + " order by h.ExternalTime ASC limit 1");

    try {
      stmt  = BaseSQLClass.dbParams.forceConn().createStatement();
      // System.out.println(sb.toString());
      rs = stmt.executeQuery(sb.toString());
      if (!rs.next()) {
        JsonServlet.writeString(pw, "NON");  // Tell the client no alarms
        return;
      }

      sb = FragmentVerbs.makeRouteDIVString(routeID, sb);
      anyParam = "[" + uo.defaultDestination + ",{\"ilk\":1,\"sug\":\"" + rs.getString(6) + "\", \"inDsc\":\"A|" + rs.getString(6) +"|" + rs.getString(4) + "|1|" + rs.getString(3) + "|" + rs.getString(2)  + "|" + rs.getString(1) + "\"}]";
      sb = FragmentVerbs.replaceAll(sb, "Default Destination", anyParam);

      JsonServlet.writeString(pw, sb.toString());

      /* Record the official time stamp of this alarm so that the next alarm
       * route request from this client will not repeat this alarm.*/
      try {
        scratch = FragmentVerbs.REPORT_DATE_TIME.parse(rs.getString(2));
      } catch (ParseException e) {
        scratch = new Date();
      }
      uo.recentAlarmRoute = FragmentVerbs.SQL_DATE_TIME.format(scratch);
    } catch (SQLException e) {
      FragmentVerbs.whinge(pw, sb.toString() + " " + e.toString());
      return;
    } finally {
      try {
        if (rs   != null) { rs.close(); }
        if (stmt != null) { stmt.close(); }
      } catch (SQLException e) { }
    }

    System.out.println(ip.howLongSince() + "to send alarm notification");
  }

  /**
   * Processes a service request.  Reads the parameters sent from the
   * client and dispatches the appropriate action.  Each outstanding
   * alarm creates a route object expressed in html.  After
   * dispatching, closes the input and output streams.</p>

   * <P> Unlike other servlets, this servlet does not have facilities
   * for dumping request parameters.  This is because Firebug has
   * proven to be a reliable mechanism for debugging interaction
   * between clients and servlets

   * @param request  the request.
   * @param response  the response.

   * @throws ServletException if there is a servlet related problem.
   * @throws IOException if there is an I/O problem.
   */
//  @SuppressWarnings("unchecked")
  public void service(HttpServletRequest request, HttpServletResponse response)
    throws IOException {
    PrintWriter pw;
    int verb;

    response.setContentType("text/html; charset=UTF-8");
    response.setHeader("Cache-Control", "no-cache");

    pw   = response.getWriter();

    verb = CommandArgs.integerFromString(request.getParameter(ParamNames.VERB));
    try {
      switch(verb) {
      case 1:
	AlarmServices.routeUnroutedAlarms(pw, request, response);
	break;

      case 99:			// Test the error notification capability
        throw new RuntimeException("Test error");

      default:			// Unrecognized verb
        JsonServlet.writeString(pw, "ERR Unknown verb " + verb);
        break;
      }
    } catch (Exception e) {
      JsonServlet.logger.error(getClass().getSimpleName() + " " + e.toString());
      e.printStackTrace();
      JsonServlet.writeString(pw, "ERR Exception " + e.toString());
    }
    if (pw != null) { pw.close(); }
  }

  /** Called when servlet is shut down*/
  public void destroy() {
    System.out.println(getClass().getSimpleName() + " destroy method called");

    logKillMsgs(ProcessManager.shutDownMatchingTasks("PanelDaemon"),
                JsonServlet.logger);
  }

  public class AlarmMonitor extends Thread {

    /* Instantiating AnyUser causes the user object to refer to the
     * default building for the database.  This is fine in a
     * one-building system but TODO for a multi-building system, it
     * will be necessary to associate alarm input streams with the
     * correct building ID.*/
    AnyUser uo = new AnyUser();
    FileOutputStream fos;
    InputStream inputStream;
    Logger  logger;
    String  defaultPort;
    String  fileBase;

    boolean keepMonitoring = false;
    int     baud;

    /** Messages consist of a line which contains a device ID PRECEEDED
     * by a message which tells what to do with it.  This list stores
     * strings which cause the user to be notified.  */
    List<String> notifies = new ArrayList<String>();
    boolean hadNotify;

    /** Messages consist of a line which contains a device ID PRECEEDED
     * by a message which tells what to do with it.  This list stores
     * strings which cause the alarm queue to be cleared.  */
    List<String> clears   = new ArrayList<String>();
    boolean hadClear;

    /** Messages consist of a line which contains a device ID PRECEEDED
     * by a message which tells what to do with it.  This list stores
     * strings which cause maps to be generated.  */
    List<String> makeMaps = new ArrayList<String>();
    boolean hadMap;

    public void setNotifies(List<String> notifies) {
      this.notifies = notifies;
    }

    public void setClears(List<String> clears) {
      this.clears = clears;
    }

    public void setMakeMaps(List<String> makeMaps) {
      this.makeMaps = makeMaps;
    }

    StringBuilder accumulation = new StringBuilder(160);
    Pattern alarmIDPattern;
    Pattern alarmDatePattern;

    public AlarmMonitor (String defaultPort, int baud, String fileBase,
			 Logger logger) {
      this.defaultPort = defaultPort;
      this.baud        = baud;
      this.fileBase    = fileBase;
      this.logger      = logger;

      complete();
    }

    @SuppressWarnings("unchecked")
    private void complete() {
      String scratch = "";
      CommPortIdentifier portId = null;
      Enumeration<CommPortIdentifier> portList;
      SerialPort serialPort;
      boolean portFound = false;

      // Create a raw panel output log
      try {
        fos = new FileOutputStream(scratch = fileBase + File.separator +
                                   "PanelLog" + new Date() + ".txt");
	String message = "Panel log file " + scratch;
        logger.info(message);
	Happening.thingsHappen(message, _TC.HT_System_Info);
      } catch (FileNotFoundException e1) {
        fos = null;
        String message = "No raw panel log " + scratch + " " + e1.toString();
        logger.error(message);
        Happening.thingsHappen(message, _TC.HT_Panel_Error);
      }

      portList = CommPortIdentifier.getPortIdentifiers();
      if ((portList == null) || !portList.hasMoreElements()){
        String message ="No communications ports found";
        logger.error(message);
        Happening.thingsHappen(message, _TC.HT_Panel_Error);
      }

      if (portList != null) {
	String pName;
	while (portList.hasMoreElements()) {
	  portId = (CommPortIdentifier) portList.nextElement();
	  if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
	    logger.info(pName = portId.getName());
	    if (pName.equals(defaultPort)) {
	      String message = "Found defaultPort: " + defaultPort;
	      logger.info(message);
	      Happening.thingsHappen(message, _TC.HT_System_Info);
	      portFound = true;
	      break;
	    }
	  }
	}
      }

      if (!portFound) {
        String message = ("Port " + defaultPort + " not found during scan of all visible ports.");
        logger.error(message);
        Happening.thingsHappen(message, _TC.HT_Panel_Error);
        try {
          portId = CommPortIdentifier.getPortIdentifier(defaultPort);
        } catch (NoSuchPortException e) {
          String me2 = "Can not access " + defaultPort +
	    " by name.  No panel input. ";
          logger.error(me2, e);
          Happening.thingsHappen(me2 + e.toString(), _TC.HT_Panel_Error);
          return;
        }
      }

      try {
        serialPort = (SerialPort) portId.open("Wayfinding", 4000);
      } catch (PortInUseException e) {
        String message = "Port in use by " + portId.getCurrentOwner();
        logger.error(message);
        Happening.thingsHappen(message, _TC.HT_Panel_Error);
        return;
      } catch (RuntimeException re) {
        String message = "ERR runtime ";
        logger.error(message, re);
        Happening.thingsHappen(message + re.getMessage(), _TC.HT_Panel_Error);
        return;
      } catch (Exception re) {
	String message = "ERR exception ";
	logger.error(message, re);
        Happening.thingsHappen(message + re.getMessage(), _TC.HT_Panel_Error);
	return;
      }

      try {
        inputStream = serialPort.getInputStream();
      } catch (IOException e) {
	inputStream = null;
        String message = "Problem getting input stream ";
        logger.error(message, e);
        Happening.thingsHappen(message + e.toString(), _TC.HT_Panel_Error);
        return;
      }

      alarmIDPattern   = Pattern.compile("N\\d\\d\\dL\\d\\d[DM]\\d\\d\\d");
      alarmDatePattern = Pattern.compile("\\d\\d:\\d\\d:\\d\\d[AP] [A-Z]{3} [A-Z]{3} \\d\\d,\\d\\d\\d\\d");
      keepMonitoring = true;
    }

    public boolean streamOKP() {
      return (inputStream != null);
    }

    /** This method is run when the start method is called on an
     * instance of this class.  It monitors the serial stream from the
     * alarm panel.  The thread terminates when this method exits.*/
    public void run() {
      char ch;
      StringBuilder inputBuffer = new StringBuilder();

      while (keepMonitoring) {
	try {
	  int newData = 0;
	  while (newData != -1) {
	    newData = inputStream.read();
	    if (newData == -1) {
	      String message = "Got -1 on alarm serial port, unexpected end of file";
	      Happening.thingsHappen(message, _TC.HT_Panel_Error);
	      logger.error(message);
	      break;
	    }
	    if (fos != null) {
	      fos.write((byte) newData);
	    }
	    ch = (char)newData;
	    if (('\n' == ch) || ('\r' == ch)) {
	      // The alarm manager is responsible for rejecting 0-length lines.
	      processLine(inputBuffer.toString());
	      inputBuffer.setLength(0);
	    } else {
	      inputBuffer.append((char) newData);
	    }
	  }
	} catch (Exception e) {
	  String message = "Problem reading serial port ";
	  logger.error(message, e);
          Happening.thingsHappen(message + e.toString(), _TC.HT_Panel_Error);
	}
      }
    }

    /**
     * See if a line starts with any one of a list of strings
     * @param aLine the line to be tested
     * @param starts linked list of strings
     * @return true if the line started with one of the strings
     */
    public boolean startInList(String aLine, List<String>starts) {
      for (String s : starts) {
        if (aLine.startsWith(s)) { return true; }
      }
      return false;
    }

    /**
     * See if an alarm ID occurs in a string
     * @param aLine line of text
     * @return alarm ID if found or null if no alarm ID is found in the string
     */
    public String findAlarmID(String aLine) {
      Matcher match = alarmIDPattern.matcher(aLine);
      if (!match.find()) { return null; }
      return match.group();
    }

    /**
     * See if a panel-formatted date occurs in a string
     * @param aLine line of text
     * @return date if found or null if no date is found in the string
     */
    public String findDate(String aLine) {
      Matcher match = alarmDatePattern.matcher(aLine);
      if (!match.find()) { return null; }
      return match.group();
    }

    /**
     * Reset the alarm line scanner
     */
    public void resetScan() {
      hadNotify = false;
      hadClear  = false;
      hadMap    = false;
      accumulation.setLength(0);
    }

    public void processLine(String aLine) {
      if ((aLine == null) || (aLine.length() <= 0)) { return; }
      String alarmID;
      String alarmDate;

      hadNotify = hadNotify || startInList(aLine, notifies);
      hadClear  = hadClear  || startInList(aLine, clears);
      hadMap    = hadMap    || startInList(aLine, makeMaps);
      if (hadNotify || hadClear || hadMap) {
        if (accumulation.length() > 0) { accumulation.append("|"); }
        accumulation.append(aLine.trim());
      }
      alarmID   = findAlarmID(aLine);
      alarmDate = findDate(aLine);
      if ((alarmID != null) || (alarmDate != null)) {
        BaseSQLClass bsq;
        Happening hap = new Happening();
	if ( (alarmDate) != null) {
	  alarmDate = alarmDate.substring(0, 9) + "M" + alarmDate.substring(9);
	  try {
	    hap.setExternalTime(new java.sql.Timestamp(PANEL_DATE_TIME.parse(alarmDate).getTime()));
	  } catch (ParseException e) {
	    logger.error("ERR " + alarmDate + " " + e.toString());
	  }
	}
        if (uo.currentBuilding != null) {
          hap.setBuildingID(uo.currentBuilding.fetchKeyOfEntity());
        }
        Device dev = (Device)BaseSQLClass.getOne(Device.class, null, "Name", alarmID);
        if (dev != null) {
          hap.setDeviceID(dev.fetchKeyOfEntity());
          hap.setDescription(dev.getType());
        }
        if (hadNotify) { // the panel desires that users see the accumulation
          logger.debug("Notify " + accumulation.toString());
          hap.setHappeningTypeID((long)_TC.HT_Notify);
          hap.setDescription(accumulation.toString());
        } else if (hadClear) {
          logger.debug("Clear " + accumulation.toString());
          hap.setHappeningTypeID((long)_TC.HT_Alarm_Clear);
        } else if (hadMap) {
          logger.debug("Alarm ID " + alarmID + " " + accumulation.toString());
          if (dev == null) {
            hap.setHappeningTypeID((long)_TC.HT_Panel_Error);
            hap.setDescription("Unknown device " + alarmID);
          } else {
            hap.setHappeningTypeID((long)_TC.HT_Alarm);
            if (uo.currentBuilding != null) {
              /* This is always a space for now; will probably change*/
              bsq = uo.currentBuilding.fetchDefaultLocation();
              if (bsq != null) {
                if (bsq instanceof Location) {
                  hap.setFromLocationID(bsq.fetchKeyOfEntity());
                } else {
                  hap.setFromSpaceID(bsq.fetchKeyOfEntity());
                }
              }
            }
	    bsq = dev.fetchDestination();
            if (bsq != null) {
              if (bsq instanceof Location) {
                hap.setToLocationID(bsq.fetchKeyOfEntity());
              } else {
                hap.setToSpaceID(bsq.fetchKeyOfEntity());
              }
            }
          }
        } else {		// Unknown message type, call it an error
	  hap.setDescription(accumulation.toString());
	  hap.setHappeningTypeID((long)_TC.HT_Panel_Error);
	}
        hap.writeFromKey(null);
        resetScan();		// Clears flags and accumulation
      }
    }
  }
}
