/* @name HtmlServlet.java

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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import zetek.common.CommandArgs;
import zetek.dbcommon.BaseSQLClass;

/**
 * Servlet to serve various .html fragments to JQuery.

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class HtmlServlet extends HttpServlet {

  public static final long serialVersionUID = 1;

  /** Servlet-wide transaction debugging flag.  If this flag is true,
   * generate extra debugging information in the log file.  This flag
   * is turned on by a HTTP request which contains the parameter
   * debug=y.  The flag is turned off at the end of each request.
   * Being static, this flag can produce inconsistent results under
   * heavy load.*/
  public static boolean debugTransaction;

  /**
   * Read a file and return its entire contents in a mutable string
   * @param fileName name of the file
   * @param sb StringBuilder to use if convenient, otherwise the
   * method creates a n ew string builder
   * @return contents of the file or an error message if the file
   * cannot be read
   */
  public static StringBuilder slurpFile(String fileName, StringBuilder sb)
  throws FileNotFoundException, IOException {
    File file = new File(fileName);
    String aLine;
    BufferedReader br;
    if (sb == null) {
      sb = new StringBuilder((int)file.length());
    } else {
      sb.setLength(0);
    }

    br = new BufferedReader(new FileReader(file));
    while ( (aLine = br.readLine() ) != null) {
      sb.append(aLine + "\n");
    }
    return sb;
  }

  /** Obligatory constructor.*/
  public HtmlServlet() { /* */ }

  /** Called once before any requests are honored.
   * @param configuration Servlet Configuration which, among other
   * things, gives access to the Servlet Context which contains
   * parameters specified in the web.xml file. */

  public void init(ServletConfig configuration) {

    System.out.println("\n" + getClass().getSimpleName() + " Init called.");

    /* Wait for the Json servlet to initialize the database
     * connection.  In theory, the Json servlet fires up first.*/
    while (BaseSQLClass.dbParams == null) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        String message = getClass().getSimpleName() + " interrupted " + e.toString();
        if (JsonServlet.logger != null) {
          JsonServlet.logger.error(message);
        } else {
	  System.out.println(message);
        }
      }
    }
    System.out.println(getClass().getSimpleName() + " Init complete.");
  }

  /**
   * Processes a service request.  Reads the parameters sent from the
   * client and dispatches the appropriate action.  Each action
   * routine is responsible for creating a .html.  After
   * dispatching, closes the input and output streams.

   * @param request the request.
   * @param response the response.

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
      while (en.hasMoreElements()) {
        nam = en.nextElement();
        System.out.println(nam + " " + request.getParameter(nam));
      }
    }

    response.setContentType("text/html; charset=UTF-8");
    response.setHeader("Cache-Control", "no-cache");

    pw   = response.getWriter();

    verb = CommandArgs.integerFromString(request.getParameter(ParamNames.VERB));
    try {
      switch(verb) {
      case 1:
        FragmentVerbs.makeRouteDIV(pw, request, response);
	break;
      case 2:
        FragmentVerbs.makeRouteDestDIV(pw, request, response);
	break;
      case 3:
        FragmentVerbs.makeRouteDestVertDIV(pw, request, response);
        break;
      case 4:
        FragmentVerbs.makeRouteSubDIV(pw, request, response);
        break;
      case 5:
        FragmentVerbs.makeReport(pw, request, response);
        break;
      case 6:
        PrintVerbs.makeRoute(pw, request, response);
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
    debugTransaction = false;
  }

  /** Called when servlet is shut down*/
  public void destroy() {
    System.out.println(getClass().getSimpleName() + " destroy method called");
  }
}
