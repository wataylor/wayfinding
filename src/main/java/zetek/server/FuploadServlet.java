/* @name FuploadServlet.java

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

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import zetek.dbcommon.BaseSQLClass;
import zetek.server.utils.AnyUser;

import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.ParamPart;
import com.oreilly.servlet.multipart.Part;

import dbaccess.DBClean;
import dbaccess.Location;
import dbaccess.Space;

/**
 * Servlet to handle file upload requests via a dynamically-created iFrame

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class FuploadServlet extends HttpServlet {

  public static final long serialVersionUID = 1;

  /** Obligatory constructor.*/
  public FuploadServlet() { /* */ }

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
  public void service(HttpServletRequest request, HttpServletResponse response)
    throws IOException {
    PrintWriter pw = null;

    String fakeHTML = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/xhtml1-loose.dtd\">" +
      "<html>" +
      "<head/>" +
      "<body>";

    AnyUser uo;
    File file;                      // Java file object
    FilePart  filePart = null;      // multipart upload object
    MultipartParser parser;
    ParamPart paramPart = null;
    Part part;
    String fileName = null;
    String nik;
    String pathAndFileName;
    StringBuilder sb = new StringBuilder();
    boolean bad = false;

    //System.out.println(req.getRemoteHost() + " " + req.getRequestURI());

    uo = AnyUser.getUserObject(request);

    response.setContentType("text/html; charset=UTF-8");
    response.setHeader("Cache-Control", "no-cache");

    pw = response.getWriter();
    JsonServlet.writeString(pw, fakeHTML); // Start the return page

    try {

      parser = new MultipartParser(request, 5000000);     // 5MB upload limit
      while ( (part = parser.readNextPart()) != null) {
	if (part.isParam()) {
          paramPart = (ParamPart)part;
	  System.out.println(part.getName() + " " + paramPart.getStringValue());
	} else if (part.isFile()) {
	  filePart = (FilePart)part;
	  fileName = filePart.getFileName();
	  // System.out.println("Uploading " + fileName);
	  if ((fileName == null) || (fileName.length() <= 0)) {
	    sb.append( "ERRYou did not select a file to upload or used an invalid filename");
	    bad = true;
	  } else {

	    if (uo.currentBuilding == null) { // Should be null if not logged in
	      sb.append("ERRNo building has been selected; file not written.");
	      bad = true;
	    } else {
	      if (( (nik = uo.currentBuilding.getNickName()) == null) ||
		  (nik.length() <= 0)) {
		sb.append("ERRBuilding " + uo.currentBuilding.getName() +
			  " Has no nick name");
		bad = true;
	      } else {
		pathAndFileName = JsonServlet.uploadRoot + nik + File.separator + "photos";
		file = new File(pathAndFileName);
		if (!file.exists()) {
		  file.mkdirs();      // Create any needed directory paths
		}

		pathAndFileName += File.separator + fileName;
		sb.append("Uploaded " + fileName);
		file = new File(pathAndFileName);
		filePart.writeTo(file);
	      }
	    }
	  }
	} else {
	  sb.append("ERRUnknown part " + part.getName());
	}
      }

      // All the parts have been processed, see if need a photo relationship.
      if (!bad && (fileName != null) && (paramPart != null)) {
	if ((uo.editObject != null) && ((uo.editObject instanceof Location) ||
					(uo.editObject instanceof Space))) {
	  if (paramPart.getStringValue().length() <= 0) {
	    sb.append("ERRMust supply a photo caption");
	  } else {
	    Long eID = null;
	    if (uo.editObject instanceof Location) {
	      eID = ((Location)uo.editObject).getLocationID();
	    } else {
	      eID = ((Space)uo.editObject).getSpaceID();
	    }
	    DBClean.establishRelationship(eID, null, paramPart.getStringValue(),
					  fileName);
	  }
	}
      }
    } catch (Exception e) {
      JsonServlet.logger.error(getClass().getSimpleName() + " " + e.toString());
      e.printStackTrace();
      JsonServlet.writeString(pw, "ERR Exception " + e.toString());
    }
    JsonServlet.writeString(pw, "<script type=\"text/javascript\">parent.fuploadCB('" + sb.toString() + "');</script></body></html>");
    if (pw != null) { pw.close(); }
  }

  /** Called when servlet is shut down*/
  public void destroy() {
    System.out.println(getClass().getSimpleName() + " destroy method called");
  }
}
