/* @name JsonServlet.java

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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONWriter;

import zetek.common.CommandArgs;
import zetek.dbcommon.BaseSQLClass;
import zetek.dbcommon.DBParams;
import zetek.dbcommon.MaintainIDs;
import zetek.graphserve.SGB;
import zetek.print.JavaPrint;
import zetek.print.MapPanel;

/**
 * Handle Json requests from the client.

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class JsonServlet extends HttpServlet {

  public static final long serialVersionUID = 1;

  public static Logger logger;
  /** Servlet-wide transaction debugging flag.  If this flag is true,
   * generate extra debugging information in the log file.  This flag
   * is turned on by a HTTP request which contains the parameter
   * debug=y.  The flag is turned off at the end of each request.
   * Being static, this flag can produce inconsistent results under
   * heavy load.*/
  public static boolean debugTransaction;

  /** File root for the servlet.  This tends to end up just above the
   * WEB-INF directory. */
  public static String fileRoot;
  /** Root of the place in the file system where uploaded files are
   * written.  This usually has the building nick name appended.  It
   * should not end in a slash.*/
  public static String uploadRoot;
  /** File system root where generated map images and .html files are written.
   * It should not end with a slash.*/
  public static String mapFileRoot;
  /** Root URL where generated .html files are browsed.  It should end with
   * a slash.*/
  public static String mapBrowseRoot;
  /** If this is true, the client is instructed not to poll for alarm
   * messages or accessibility updates.*/
  public static boolean noPoll = false;

  public static void writeString(PrintWriter pw, String outS) {
    char[] chars = new char[outS.length()];
    outS.getChars(0, outS.length(), chars, 0);
    pw.write(chars);
    logger.debug(outS);
  }

  public static void writeString(PrintWriter pw, JSONObject obj)
  throws JSONException {
    if (debugTransaction) {
      // Pretty-printed rendition of the JSON object for debugging
      System.out.println(obj.toString(1));
    }
    writeString(pw, obj.toString());
  }

  public static void writeString(PrintWriter pw, JSONArray obj)
  throws JSONException {
    if (debugTransaction) {
      // Pretty-printed rendition of the JSON object for debugging
      System.out.println(obj.toString(1));
    }
    writeString(pw, obj.toString());
  }

  public static void writeString(PrintWriter pw, Object obj)
  throws JSONException {
    if (debugTransaction) {
      // Printed rendition of what should generate a JSON object for debugging
      System.out.println(obj.toString());
    }
    writeString(pw, obj.toString());
  }

  public static StringBuilder appendObjectArray(List<Object> list, StringBuilder sb) {
    boolean did = false;
    if (sb == null) { sb = new StringBuilder(); }
    sb.append("[");
    for (Object o : list) {
      if (did) { sb.append(","); }
      sb.append(new JSONObject(o, true).toString() + "\n");
      did = true;
    }
    sb.append("]");
    return sb;
  }
  /** Obligatory constructor.*/
  public JsonServlet() { /* */ }

  /** Called once before any requests are honored.
   * @param configuration Servlet Configuration which, among other
   * things, gives access to the Servlet Context which contains
   * parameters specified in the web.xml file. */
  @SuppressWarnings("unchecked")
  public void init(ServletConfig configuration) {
    String nam;
    Map<String, String> parMap = new HashMap<String, String>();
    ServletContext context;
    Enumeration<String> en;

    System.out.println("\n" + getClass().getSimpleName() + " Init called");

    en = configuration.getInitParameterNames();
    while (en.hasMoreElements()) {
      nam = en.nextElement();
      System.out.println(nam + " " + configuration.getInitParameter(nam));
    }

    context = configuration.getServletContext();
    en = context.getInitParameterNames();
    while (en.hasMoreElements()) {
      nam = en.nextElement();
      parMap.put(nam, context.getInitParameter(nam));
      // System.out.println(nam + " " + context.getInitParameter(nam));
    }
    uploadRoot    = context.getInitParameter("uploadRoot");
    mapFileRoot   = context.getInitParameter("mapFileRoot");
    mapBrowseRoot = context.getInitParameter("mapBrowseRoot");
    MapPanel.filePathHeader = context.getInitParameter("tileFileRoot");
    System.out.println("Default path is " + new File("./").getAbsolutePath());

    /* The log4j parameter files go in the WEB-INF directory so they
     * are part of the .war file.  Other configuration files may be
     * put there in the future and will be loaded by this method. */
    fileRoot = context.getRealPath("/");
    PropertyConfigurator.configure(fileRoot + "WEB-INF/" +
        context.getInitParameter("log4j"));
    DBParams.logger = logger = Logger.getLogger(DBParams.class);

    SGB.shapeTraverse = CommandArgs.integerFromString(context.getInitParameter("shapeTraverse"));
    nam = context.getInitParameter("smoosePaths");
    if (nam != null) {
      SGB.smoosePaths = "true".equalsIgnoreCase(nam);
    }

    if (( (nam = context.getInitParameter("parallelDistance")) != null) &&
        (nam.length() > 0)) {
      SGB.parallelDistance = CommandArgs.doubleFromString(nam);
    }
    if (( (nam = context.getInitParameter("parallelDistanceChange")) != null) &&
        (nam.length() > 0)) {
      SGB.parallelDistanceChange = CommandArgs.doubleFromString(nam);
    }
    if (( (nam = context.getInitParameter("perpDistance")) != null) &&
        (nam.length() > 0)) {
      SGB.perpDistance = CommandArgs.doubleFromString(nam);
    }

    logger.error("parallelDistance " + SGB.parallelDistance +
        " parallelDistanceChange " + SGB.parallelDistanceChange +
        " perpDistance " + SGB.perpDistance);

    /* Loading DB parameters must come after the logger configuration
     * has been initialized because DBParams initializes the main
     * logger for any system which uses the database.

     * The database parameter system looks first at the map of
     * parameters from the web.xml file and then at parameters in the
     * DynDB properties file.  It is to be hoped that all required
     * parameters are to be found in one of those two places; if not,
     * a startup exception occurs because it is not possible to
     * establish a connection to the database.

     * It is important that <code>BaseSQLClass.dbParams</code> stay
     * null until the database initialization is complete because
     * other servlets rely on this servlet to establish the database
     * connection.*/
    BaseSQLClass.dbParams = new DBParams(parMap, "DynDB", null);
    BaseSQLClass.appApi   = new MaintainIDs();
    noPoll = "true".equalsIgnoreCase(BaseSQLClass.dbParams.getParameter("noPoll"));
    PrintVerbs.webPageSize  = CommandArgs.integerFromString(BaseSQLClass.dbParams.getParameter("webPageSize"));
    PrintVerbs.javaPageSize = CommandArgs.integerFromString(BaseSQLClass.dbParams.getParameter("javaPageSize"));
    JavaPrint.actuallyPrint = !"true".equalsIgnoreCase(BaseSQLClass.dbParams.getParameter("suppressPrint"));
    System.out.println("noPoll " + noPoll + " page size " + PrintVerbs.webPageSize + " really print " + JavaPrint.actuallyPrint);
  }

  /**
   * Processes a service request.  Reads the parameters sent from the
   * client and dispatches the appropriate action.  Each action
   * routine is responsible for creating a JSON object.  After
   * dispatching, closes the input and output streams.

   * @param request  the request.
   * @param response  the response.

   * @throws ServletException if there is a servlet related problem.
   * @throws IOException if there is an I/O problem.
   */
  @SuppressWarnings("unchecked")
  public void service(HttpServletRequest request, HttpServletResponse response)
  throws IOException {
    PrintWriter pw = null;
    //System.out.println(req.getRemoteHost() + " " + req.getRequestURI());
    int verb;

    debugTransaction = (request.getParameter(ParamNames.DEBUG) != null);

    /* The presence of this parameter causes all of the HTTP request
     * parameters to be dumped to the Tomcat log.  */
    if (request.getParameter(ParamNames.DUMP) != null) {
      Enumeration<String> en;
      String nam;

      en = request.getParameterNames();
      System.out.println();
      while (en.hasMoreElements()) {
        nam = en.nextElement();
        System.out.println(nam + " >" + request.getParameter(nam) + "<");
      }
    }

    response.setContentType("text/json; charset=UTF-8");
    response.setHeader("Cache-Control", "no-cache");
    // unfortunately this header won't work with i18n
    // response.setHeader("X-JSON", json);
    //OutputStream out = response.getOutputStream();

    pw   = response.getWriter();

    verb = CommandArgs.integerFromString(request.getParameter(ParamNames.VERB));
    try {
      switch(verb) {
      case 1: // Used when the user wants to find a building
        VerbsJson.makeBuildingInfo(pw, request, response);
        break;
      case 2:
        VerbsJson.makeBuildingStrings(pw, request, response);
        break;
      case 3: // Called for every keystroke when looking for something
        VerbsJson.makeSuggestions(pw, request, response);
        break;
      case 4: // Called when user accepts a suggestion, returns a location
        VerbsJson.takeSuggestion(pw, request, response);
        break;
      case 5: // Returns information about people assigned to a building
        VerbsJson.buildingFolks(pw, request, response);
        break;
      case 6: // Returns the current building as a Json object
        VerbsJson.currentBuilding(pw, request, response);
        break;
      case 7: // Returns the selected location as a Json object & remembers it
        VerbsJson.getLocation(pw, request, response);
        break;
      case 8: // Updates an object as a Json object & updates database
        VerbsJson.updateEditObj(pw, request, response);
        break;
      case 9: // Generate an organization selector for adjusting relationships
        FragmentVerbs.makeOrgSelector(pw, request, response);
        break;
      case 10:
        VerbsJson.addNewObj(pw, request, response);
        break;
      case 11:
        VerbsJson.fetchLocPhotos(pw, request, response);
        break;
      case 12: // Erase a relationship
        VerbsJson.toastRelationship(pw, request, response);
        break;
      case 13: // Make a person selector for adjusting relationships
        FragmentVerbs.makePeepSelector(pw, request, response);
        break;
      case 14: // Adjust person membership in organizations
        FragmentVerbs.addSubPeepsOrgs(pw, request, response);
        break;
      case 15: // Return blocked spaces for the floor
        VerbsJson.impedimenta(pw, request, response);
        break;
      case 16: // Return info for a pop-up bubble
        VerbsJson.getBubbleInfo(pw, request, response);
        break;
      case 17: // Return info icons for the floor
        VerbsJson.pointsOfInterest(pw, request, response);
        break;

      case 99:			// Test the error notification capability
        throw new RuntimeException("Test error");

      default:			// Unrecognized verb
        try {
          new JSONWriter(pw)
          .object()
          .key("msg").value("unknown verb " + verb)
          .endObject();
        } catch (JSONException e) {
          System.out.println("ERR json" + e.toString());
        }
        break;
      }
    } catch (Exception e) {
      logger.error(getClass().getSimpleName() + " " + e.toString());
      e.printStackTrace();
      try {
        new JSONWriter(pw)
        .object()
        .key("msg").value(e.toString())
        .endObject();
      } catch (JSONException ej) {
        logger.error(getClass().getSimpleName() + " " + ej.toString());
      }
    }
    if (pw != null) { pw.close(); }
    debugTransaction = false;
  }

  /** Called when servlet is shut down*/
  public void destroy() {
    System.out.println(getClass().getSimpleName() + " destroy method called");
  }
}
