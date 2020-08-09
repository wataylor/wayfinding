/* @name PrintVerbs.java

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

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import zetek.common.CommandArgs;
import zetek.dbcommon.SQLU;
import zetek.graphserve.DestInfo;
import zetek.graphserve.GraphUtilsServer;
import zetek.graphserve.Route;
import zetek.graphserve.SubRoute;
import zetek.print.Imagination;
import zetek.print.JavaPrint;
import zetek.print.LG;
import zetek.print.LayoutPages;
import zetek.print.RouteBook;
import zetek.print.ZPage;
import zetek.server.utils.AnyUser;
import dbaccess.Happening;
import dbaccess.bridge._TC;

/**
 *
 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class PrintVerbs {

  public static final long serialVersionUID = 1;

  /** The web page size is read from the properties system during the init
   * method of JsonServlet which is the first servlet to start.*/
  public static int webPageSize = LG.PW8x11;
  /** The java page size is read from the properties system during the init
   * method of JsonServlet which is the first servlet to start.*/
  public static int javaPageSize = LG.PW8x11;

  /** Obligatory constructor.*/
  public PrintVerbs() { /* */ }

  /**
   * Return information about a printable route
   * @param pw print writer to send output data to the client
   * @param request  http request
   * @param response http response
   * @throws IOException
   * @throws JSONException
   */
  public static void makeRoute(PrintWriter pw,
      HttpServletRequest request,
      HttpServletResponse response)
  throws IOException, JSONException {
    long bID;
    if ( (bID= VerbsJson.getBuildingID(request)) <= 0l) {
      VerbsJson.whinge(pw, ParamNames.noBuild);
      return;
    } // TODO fetch building graph data on basis of building ID
    int whyChoice = CommandArgs.integerFromString(request.getParameter(ParamNames.WHY_CHOICE));
    AnyUser uo = AnyUser.getUserObject(request);

    Route route = new Route(uo.currentBuilding.getNickName());
    SubRoute rte;
    String fileName;
    String[] dscs;
    String[] dvid;
    String[] dwgs;
    String[] fods;
    String[] ids;
    String[] ilks;
    String[] inDscs;
    StringBuilder sb = new StringBuilder();
    StringBuilder sbht = null;
    DestInfo from;
    DestInfo to;
    int len,i;

    dscs   = request.getParameterValues(ParamNames.SPACE_DESC);
    dvid   = request.getParameterValues(ParamNames.DIV_ID);
    dwgs   = request.getParameterValues(ParamNames.FLOOR_DWG);
    fods   = request.getParameterValues(ParamNames.FLOOR_ORDER);
    ids    = request.getParameterValues(ParamNames.SPACE_ID);
    ilks   = request.getParameterValues(ParamNames.SPACE_TYPE);
    inDscs = request.getParameterValues(ParamNames.IN_DESCR);

    if (whyChoice == 1) { // Deleted the route, not a print request
      String[] s;
      boolean did = false;
      if (inDscs != null) {
        for (String st : inDscs) {
          if ((st != null) && (st.startsWith("A"))) {
            s = st.split("\\|");
            if ((s != null) && (s.length >= 7)) {
              did = true;
              SQLU.anyStatement("update Happening set Cleared='Y' where HappeningID=" + s[6]);
            }
          }
        }
      }
      if (did) {
        JsonServlet.writeString(pw, "Cleared alarm");
      } else {
        JsonServlet.writeString(pw, "OK");
      }
      return;
    }

    if ((dscs   == null) || (dscs.length   < 2) ||
        (dvid   == null) || (dvid.length   < 2) ||
        (dwgs   == null) || (dwgs.length   < 2) ||
        (fods   == null) || (fods.length   < 2) ||
        (ids    == null) || (ids.length    < 2) ||
        (ilks   == null) || (ilks.length   < 2) ||
        (inDscs == null) || (inDscs.length < 2)) {
      VerbsJson.whinge(pw, "Must have at least 2 destinations in a route request.");
      return;
    }
    len = ids.length - 1;
    for (i=0; i<len; i++) {
      Happening hap = new Happening();
      hap.setBuildingID(bID);
      from = GraphVerbs.getSystemName(ids[i],   ilks[i],   hap, true);
      to   = GraphVerbs.getSystemName(ids[i+1], ilks[i+1], hap, false);

      if ((from == null) || (to == null)) {
        if (sb.length() > 0) { sb.append("<br>"); }
        sb.append("Could not find a destination in the sub route request " +
            dscs[i] + " " + dscs[i+1]);
        continue;
      }

      if (from == to) {
        if (sb.length() > 0) { sb.append("<br>"); }
        sb.append("Start and end destinations are the same, " + from);
        continue;
      }

      from.inDesc = inDscs[i];
      to  .inDesc = inDscs[i+1];

      if ( (rte = GraphUtilsServer.routeBetweenDests(RouteServlet.bgd, false, from, to)) == null) {
        if (sb.length() > 0) { sb.append("<br>"); }
        sb.append("Cannot get from " + from + " to " + to);
        continue;
      }

      route.addSubRoute(rte);

      hap.setHappeningTypeID((long)_TC.HT_Print);
      hap.writeFromKey(null);
    }

    /*
      ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(JsonServlet.fileRoot + File.separator + "Aroute.rte"));
      out.writeObject(route);
      out.close();
     */

    // TODO if client IP same as server or want to print on server use 11x17
    boolean doJavaPrint = VerbsJson.isY(request, "jP");
    int pageSize;

    if (doJavaPrint) {
      pageSize = javaPageSize;
      } else {
      pageSize = webPageSize;
      }

    RouteBook rb = new RouteBook(route, bID, pageSize);
    List<ZPage> panels = LayoutPages.getPanels(rb, pageSize);
    long tag = System.currentTimeMillis();
    fileName = JsonServlet.fileRoot + "WEB-INF/fragments/printWindow.html";
    sbht = HtmlServlet.slurpFile(fileName, null);

    // TODO if printing on server, use JavaPrint to print locally instead of
    // images.  Detect client IP not reliable if local.  No data on server IP
    // JavaPrint.print(pages, webPageSize);
    if (doJavaPrint) {
      JavaPrint.print(panels, rb, pageSize);
      sbht.append("<P>Route being printed</p>");
    } else {
      List<ZPage> pages  = LayoutPages.layoutPages(panels, rb, pageSize, null);
      if (pages.size() > 0) {
        ZPage page;
        len = pages.size()-1;
        System.out.println("Page count " + len);
        // Now write the images and append divs for each of the images
        for (i=0; i<=len; i++) {
          sbht.append((i == len) ? "<div>" : "<div class=\"pageBreak\">");
          page = pages.get(i);
          Imagination.writeImage(JsonServlet.mapFileRoot + "im" + tag + i + ".png", page,
              ((page instanceof ZPage) && ((ZPage)page).isLandscape()));
          sbht.append("<img src=\"im"+ tag + i + ".png\" /></div>\n");
        }
      }
    }
    sbht.append("</body></html>");

    if (sb.length() > 0) {
      JsonServlet.writeString(pw, "ERR" + sb.toString());
    } else {
      fileName = JsonServlet.mapFileRoot + "map" + tag + ".html";
      BufferedOutputStream fOut = new BufferedOutputStream(new FileOutputStream(fileName));
      fOut.write(sbht.toString().getBytes());
      fOut.close();

      fileName = JsonServlet.mapBrowseRoot + "map" + tag + ".html";
      JsonServlet.writeString(pw, fileName);
    }
  }
}
