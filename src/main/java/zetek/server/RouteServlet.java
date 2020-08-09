/* @name RouteServlet.java

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

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONWriter;

import zetek.common.CommandArgs;
import zetek.dbcommon.DBParams;
import zetek.graphserve.BuildingGraphData;

/**
 * Handle routing requests from the client

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class RouteServlet extends HttpServlet {

  public static final long serialVersionUID = 1;

  public static Logger logger;

  /** Stores information about the building graph.  TODO create a
   * hash set to map any number of these to their building ID*/
  public static BuildingGraphData bgd = new BuildingGraphData();
  
  /** Obligatory constructor.*/
  public RouteServlet() { /* */ }

  /** Called once before any requests are honored.
   * @param configuration Servlet Configuration which, among other
   * things, gives access to the Servlet Context which contains
   * parameters specified in the web.xml file. */

  public void init(ServletConfig configuration) {
    System.out.println("\n" + getClass().getSimpleName() + " Init called");

    logger = Logger.getLogger(DBParams.class);
    String fileName = JsonServlet.fileRoot + 
    "WEB-INF" + File.separator + "NYST.xml";
    bgd.g = GraphVerbs.loadBuildingGraph(fileName);
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

    JsonServlet.debugTransaction =
      (request.getParameter(ParamNames.DEBUG) != null);

    /* The presence of this parameter causes all of the HTTP request
     * parameters to be dumped to the Tomcat log.  */
    if (request.getParameter(ParamNames.DUMP) != null) {
      Enumeration<String> en;
      String nam;
      String vals[];
      StringBuilder sb = new StringBuilder();

      en = request.getParameterNames();
      System.out.println();
      while (en.hasMoreElements()) {
        nam = en.nextElement();
        vals = request.getParameterValues(nam);
        if (vals != null) {
          for (String v : vals) {
            if (sb.length() > 0) { sb.append(", "); }
            sb.append(v);
          }
        }
        System.out.println(nam + " " + sb.toString());
        sb.setLength(0);
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
      case 1:
        GraphVerbs.makeRoute(pw, request, response);
	break;
      case 2:
        GraphVerbs.alterAccess(pw, request, response);
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
          logger.error(getClass().getSimpleName() + " " + e.toString());
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
    JsonServlet.debugTransaction = false;
  }

  /** Called when servlet is shut down*/
  public void destroy() {
    System.out.println(getClass().getSimpleName() + " destroy method called");
  }
}
