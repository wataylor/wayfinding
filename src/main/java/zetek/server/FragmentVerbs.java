/* @name FragmentVerbs.java

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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.json.JSONException;

import zetek.common.CommandArgs;
import zetek.dbcommon.BaseSQLClass;
import zetek.dbcommon.SQLU;
import zetek.server.utils.AnyUser;
import dbaccess.Location;
import dbaccess.Organization;
import dbaccess.Space;
import dbaccess.bridge._TC;

/**
 * Handle various requests for .html fragments up to and including
 * entire .html documents.

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class FragmentVerbs {

  public static final long serialVersionUID = 1;

  /** Obligatory constructor.*/
  public FragmentVerbs() { /* */ }

  /** SQL-specific date and time formatter.  This formatter produces
      a string which can be compared to SQL timestamps:
      <CODE>"yyyy-MM-dd HH:mm:ss"</code>.  */
  public static final SimpleDateFormat SQL_DATE_TIME =
    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  public static final SimpleDateFormat REPORT_DATE_TIME =
    new SimpleDateFormat("MM/dd/yy HH:mm:ss");
  public static final SimpleDateFormat SHOW_DATE_TIME =
    new SimpleDateFormat("MM/dd/yyyy");

  public static GregorianCalendar calFromRequest(HttpServletRequest request,
      String prefix, boolean start) {
    String year;
    String month;
    String day;
    GregorianCalendar cal;

    /* If the calendar selects all years, it selects nothing and
     * therefore any date should be included in the report.*/
    if (( (year = request.getParameter(prefix + "SelectYear")) == null) ||
        "all".equalsIgnoreCase(year)) { return null; }

    month = request.getParameter(prefix + "SelectMonth");
    day   = request.getParameter(prefix + "SelectDay");

    cal = new GregorianCalendar();
    cal.set(Calendar.YEAR, CommandArgs.integerFromString(year));

    if ((month == null) || "all".equalsIgnoreCase(month)) {
      cal.set(Calendar.MONTH, (start ? 0 : 11));
    } else {
      cal.set(Calendar.MONTH, CommandArgs.integerFromString(month));
    }

    if ((day == null) || "all".equalsIgnoreCase(day)) {
      cal.set(Calendar.DAY_OF_MONTH, (start ? 1 : cal.getMaximum(Calendar.DAY_OF_MONTH)));
    } else {
      cal.set(Calendar.DAY_OF_MONTH, CommandArgs.integerFromString(day));
    }

    if (start) {
      cal.set(Calendar.HOUR_OF_DAY, 0);
      cal.set(Calendar.MINUTE, 0);
      cal.set(Calendar.SECOND, 0);
      cal.set(Calendar.MILLISECOND, 0);
    } else {
      cal.set(Calendar.HOUR_OF_DAY, 23);
      cal.set(Calendar.MINUTE, 59);
      cal.set(Calendar.SECOND, 59);
      cal.set(Calendar.MILLISECOND, 999);
    }

    return cal;
  }

  /** Extract optional strings from a result set in priority order.*/
  public static String extractOptions(ResultSet rs, int start, int many) {
    String what;
    int i;

    for (i=0; i<many; i++) {
      if (( (what = SQLU.stringFromResult(rs, i+start)) != null) &&
          (what.length() > 0)) {
        return what.replaceAll("_", " ");
      }
    }
    return "";
  }

  /** Issue an error message to the print writer as a non-.html string
   * which the receiver will notice.
   @param pw Print Writer  to receive the JSON object
   @param message text to be put up in an alert box.*/
  public static void whinge(PrintWriter pw, String message)
  throws JSONException {
    if (HtmlServlet.debugTransaction) {
      System.out.println(message);
    }
    JsonServlet.writeString(pw, "ERR" + message);
  }

  /**
   * Replace all occurrences of s1 in the string with s2
   * @param sb string to be modified
   * @param s1 string to be replaced
   * @param s2 replacement string.  If null, changed to the empty string
   * @return modified string
   */
  public static StringBuilder replaceAll(StringBuilder sb,
      String s1, String s2) {
    int ix  = 0;
    int len = s1.length();
    if (s2 == null) { s2 = ""; }

    while ( (ix = sb.indexOf(s1, ix)) >-1) {
      sb = sb.replace(ix, ix+len, s2);
    }
    return sb;
  }

  public static StringBuilder makeRouteDIVString(int routeID,
      StringBuilder sb)
  throws IOException {
    sb = HtmlServlet.slurpFile(JsonServlet.fileRoot +
        "WEB-INF/fragments/anyRoute.html", sb);

    if (routeID > 1) {
      sb = replaceAll(sb, "route1", "route" + routeID);
      sb = replaceAll(sb, "Route # 1", "Route # " + routeID);
    }
    return sb;
  }

  /**
   * The user has asked for a new route within a building.  The route
   * number must be in the request.  It is expected that this request
   * will be followed by requests for destinations 0 and 1 to start
   * the new route, but that is not required by this method.  TBD -
   * fill in the default starting point for the building.  This may be
   * done in the client.
   * @param pw
   * @param request
   * @param response
   * @throws IOException
   * @throws FileNotFoundException
   * @throws JSONException
   */
  public static void makeRouteDIV(PrintWriter pw,
      HttpServletRequest request,
      HttpServletResponse response)
  throws IOException, FileNotFoundException, JSONException {
    String anyParam;
    int routeID;

    if (( (anyParam = request.getParameter(ParamNames.ROUTE_NO)) == null)  ||
        (anyParam.length() <= 0)) {
      whinge(pw, "No route number in request");
      return;
    }

    routeID = CommandArgs.integerFromString(anyParam);

    StringBuilder sb =  makeRouteDIVString(routeID, null);
    AnyUser uo       = AnyUser.getUserObject(request);
    anyParam         = uo.defaultDestination;
    /* Turn the start point into a one-element array in case there is a
     * destination in addition to the start point as in the case of an
     * alarm. */
    if (anyParam != null) { anyParam = "[" + anyParam + "]"; }
    // s2 is treated as the empty string if it is null
    sb = replaceAll(sb, "Default Destination", anyParam);

    JsonServlet.writeString(pw, sb.toString());
  }

  /**
   * The user has asked for a new destination within a route within a
   * building.  The route number and destination number must be in the request.
   * @param pw
   * @param request
   * @param response
   * @throws IOException
   * @throws FileNotFoundException
   * @throws JSONException
   */
  public static void makeRouteDestDIV(PrintWriter pw,
      HttpServletRequest request,
      HttpServletResponse response)
  throws IOException, FileNotFoundException, JSONException {
    String anyParam;
    String fileName;
    String s1;
    String s2;
    String s3 = null;
    String s4 = null;
    int routeID;
    int destID;

    if (( (anyParam = request.getParameter(ParamNames.ROUTE_NO)) == null)  ||
        (anyParam.length() <= 0)) {
      whinge(pw, "No route number in request");
      return;
    }

    routeID = CommandArgs.integerFromString(anyParam);

    if (( (anyParam = request.getParameter(ParamNames.DEST_NO)) == null)  ||
        (anyParam.length() <= 0)) {
      whinge(pw, "No destination number in request");
      return;
    }

    destID = CommandArgs.integerFromString(anyParam);

    /* Destination zero has a different appearance from the other
     * destinations.  All other destinations are derived from
     * destination 1.*/
    if (true) {
      fileName = JsonServlet.fileRoot + "WEB-INF/fragments/dest0.html";
    } else {
      fileName = JsonServlet.fileRoot + "WEB-INF/fragments/dest1.html";
    }

    StringBuilder sb = HtmlServlet.slurpFile(fileName, null);

    /* Destinations 0 and 1 are coded into the file fragments so no
     * modifications are needed for those destinations for route 0.*/
    if (!((routeID == 1) && (destID == 0))) {
      if (destID == 0) {	// Only have to change the route
        s1 = "route1";
        s2 = "route" + routeID;
      } else {
        s1 = "route1_Dst0";
        s2 = "route" + routeID + "_Dst" + destID;
        s3 = "Start:";
        s4 = "Destination " + destID + ":";
      }
      sb = replaceAll(sb, s1, s2);
      if (s3 != null) {
        sb = replaceAll(sb, s3, s4);
        sb = replaceAll(sb, "Starting Point", "Destination " + destID);
      }
    }

    if (destID == 0) {
      AnyUser uo = AnyUser.getUserObject(request);
      s2 = uo.defaultDestination;
    } else {
      s2 = "";
    }
    // s2 is treated as the empty string if it is null
    sb = replaceAll(sb, "Default Destination", s2);

    // Replace the image icon
    if (destID != 0) {
      int delt = destID-1;
      if (delt > 4) {delt = 4; }

      s2 = "_destination_" + ParamNames.ALPHABET.charAt(delt) + ".png";
      sb = replaceAll(sb, "_startpoint.png", s2);
    }

    JsonServlet.writeString(pw, sb.toString());
  }

  /**
   * The user has asked for a new sub-route within a route within a
   * building.  The route number and sub route number must be in the
   * request.  A sub route includes a pair of destinations.
   * @param pw
   * @param request
   * @param response
   * @throws IOException
   * @throws FileNotFoundException
   * @throws JSONException
   */
  public static void makeRouteSubDIV(PrintWriter pw,
      HttpServletRequest request,
      HttpServletResponse response)
  throws IOException, FileNotFoundException, JSONException {
    String anyParam;
    String fileName;
    String s1;
    String s2;
    int routeID;
    int subID;

    if (( (anyParam = request.getParameter(ParamNames.ROUTE_NO)) == null)  ||
        (anyParam.length() <= 0)) {
      whinge(pw, "No route number in request");
      return;
    }

    routeID = CommandArgs.integerFromString(anyParam);

    if (( (anyParam = request.getParameter(ParamNames.SUB_NO)) == null)  ||
        (anyParam.length() <= 0)) {
      whinge(pw, "No sub-route number in request");
      return;
    }

    subID = CommandArgs.integerFromString(anyParam);

    fileName = JsonServlet.fileRoot + "WEB-INF/fragments/subRoute0.html";

    StringBuilder sb = HtmlServlet.slurpFile(fileName, null);

    if (subID > 0) {
      s1 = "route1_Sub0";
      s2 = "route" + routeID + "_Sub" + subID;

      sb = replaceAll(sb, s1, s2);
    }

    if (subID == 0) {
      AnyUser uo = AnyUser.getUserObject(request);
      s2 = uo.defaultDestination;
    } else {
      s2 = "";
    }
    // s2 is treated as the empty string if it is null
    sb = replaceAll(sb, "Default Destination", s2);

    // Replace the image icon
    int delt = subID;
    if (delt > 4) {delt = 4; }

    s2 = "_destination_" + ParamNames.ALPHABET.charAt(delt) + ".png";
    sb = replaceAll(sb, "_destination.png", s2);

    if (subID > 0) {
      delt = subID-1;
      if (delt > 4) {delt = 4; }

      s2 = "_destination_" + ParamNames.ALPHABET.charAt(delt) + ".png";
      sb = replaceAll(sb, "_startpoint.png", s2);
    }

    JsonServlet.writeString(pw, sb.toString());
  }

  /**
   * The user has asked for a new vertical traverse within a route
   * segment within a building.  The route number, destination number,
   * and vertical number must be in the request.
   * @param pw
   * @param request
   * @param response
   * @throws IOException
   * @throws FileNotFoundException
   * @throws JSONException
   */
  public static void makeRouteDestVertDIV(PrintWriter pw,
      HttpServletRequest request,
      HttpServletResponse response)
  throws IOException, FileNotFoundException, JSONException {
    String anyParam;
    String fileName;
    String s2;
    int routeID;
    int destID;
    int vertID;

    if (( (anyParam = request.getParameter(ParamNames.ROUTE_NO)) == null) ||
        (anyParam.length() <= 0)) {
      whinge(pw, "No route number in request");
      return;
    }

    routeID = CommandArgs.integerFromString(anyParam);

    if (( (anyParam = request.getParameter(ParamNames.DEST_NO)) == null) ||
        (anyParam.length() <= 0)) {
      whinge(pw, "No destination number in request");
      return;
    }

    destID = CommandArgs.integerFromString(anyParam);

    if (( (anyParam = request.getParameter(ParamNames.VERT_NO)) == null) ||
        (anyParam.length() <= 0)) {
      whinge(pw, "No vertical number in request");
      return;
    }

    vertID = CommandArgs.integerFromString(anyParam);

    fileName = JsonServlet.fileRoot + "WEB-INF/fragments/vert1.html";

    StringBuilder sb = HtmlServlet.slurpFile(fileName, null);

    s2 = "route" + routeID + "_Dst" + destID + "_Vrt" + vertID;
    sb = replaceAll(sb, "route1_Dst0_Vrt1", s2);

    JsonServlet.writeString(pw, sb.toString());
  }
  // /way_dynamic/html.jsp?verb=3&rteno=5&dstno=7&vrtno=3

  /**
   * Create various organization selectors based on client request
   */
  @SuppressWarnings("unchecked")
  public static void makeOrgSelector(PrintWriter pw,
      HttpServletRequest request,
      HttpServletResponse response)
  throws IOException, JSONException {
    long bID;
    if ( (bID= VerbsJson.getBuildingID(request)) <= 0l) {
      VerbsJson.whinge(pw, ParamNames.noBuild);
      return;
    }

    AnyUser uo;
    StringBuilder sb  = new StringBuilder();
    String otherWhere = "";
    String filter;
    boolean did  = false;
    int listType = CommandArgs.integerFromString(request.getParameter(ParamNames.WHICH_LIST));

    sb.append("{\"scs\":[");	// Start the selections object array

    if (listType == 0) {
      sb.append("[\"0\",\"Select a company\"]");
      did = true;
    } else if (listType == 1) {
      uo = AnyUser.getUserObject(request);
      if ((uo.editObject == null) || (!(uo.editObject instanceof Location) &&
          !(uo.editObject instanceof Space))) {
        VerbsJson.whinge(pw,
        "Have not selected a location to which to assign.");
        return;
      }
      Long eID = null;
      if (uo.editObject instanceof Location) {
        eID = ((Location)uo.editObject).getLocationID();
      } else {
        eID = ((Space)uo.editObject).getSpaceID();
      }

      if (VerbsJson.isY(request, ParamNames.ASSIGNED)) {
        otherWhere = " and (LocationID=" + eID + " or SpaceID=" + eID + ")";
      } else {
        /* Organizations which are not assigned to the space; apply filters in
         * the request such as initial letter.*/
        otherWhere = " and (LocationID is null or LocationID!=" + eID +
        ") and (SpaceID is null or SpaceID!=" + eID + ")";
        if (( (filter = request.getParameter(ParamNames.ORG_FILTER)) != null) &&
            !"all".equalsIgnoreCase(filter)) {
          otherWhere += " and Name like '" + filter + "%'";
        }
      }
    } else {
      VerbsJson.whinge(pw, "Unknown list request " + listType);
      return;
    }
    List<Organization> orgs =
      BaseSQLClass.getMany(Organization.class, null,
          "BuildingID=" + bID + otherWhere +
      " order by Name, Division");
    for (Organization org : orgs) {
      if (did) { sb.append(","); }
      sb.append("\n[\"" + org.getOrganizationID() + "\",\"" +
          org.fetchSelector() + "\"]");
      did = true;
    }
    sb.append("]}");		// End the selections object array
    // System.out.println(sb.toString());
    JsonServlet.writeString(pw, sb.toString());
  }

  /**
   * Create various person selectors based on client request
   */
  public static void makePeepSelector(PrintWriter pw,
      HttpServletRequest request,
      HttpServletResponse response)
  throws IOException, JSONException {
    long bID;
    if ( (bID= VerbsJson.getBuildingID(request)) <= 0l) {
      VerbsJson.whinge(pw, ParamNames.noBuild);
      return;
    }

    AnyUser uo;
    StringBuilder sb = new StringBuilder();
    String otherWhere = "";
    String filter;
    boolean did = false;
    int listType = CommandArgs.integerFromString(request.getParameter(ParamNames.WHICH_LIST));

    sb.append("{\"scs\":[");	// Start the selections object array

    if (listType == 0) {
      sb.append("[\"0\",\"Select a person\"]");
      did = true;
    } else if (listType == 1) {
      uo = AnyUser.getUserObject(request);
      if ((uo.editObject == null) || (!(uo.editObject instanceof Location) &&
          !(uo.editObject instanceof Space))) {
        VerbsJson.whinge(pw, "Have not selected a location to which to assign.");
        return;
      }
      Long eID = null;
      if (uo.editObject instanceof Location) {
        eID = ((Location)uo.editObject).getLocationID();
      } else {
        eID = ((Space)uo.editObject).getSpaceID();
      }
      if (VerbsJson.isY(request, ParamNames.ASSIGNED)) {
        otherWhere = " and (p.LocationID=" + eID + " or p.SpaceID=" + eID + ")";
      } else {
        /* People which are not assigned to the space; apply filters in
         * the request such as initial letter.*/
        otherWhere = " and (p.LocationID is null or p.LocationID!=" + eID +
        ") and (p.SpaceID is null or p.SpaceID!=" + eID + ")";
        if (( (filter=request.getParameter(ParamNames.PERS_FILTER)) != null) &&
            !"all".equalsIgnoreCase(filter)) {
          otherWhere += " and p.LastName like '" + filter + "%'";
        }
        if (( (filter=request.getParameter(ParamNames.ORG_ID_FILT)) != null) &&
            !"all".equalsIgnoreCase(filter)) {
          otherWhere += " and p.OrganizationID=" + filter;
        }
      }
    } else {
      VerbsJson.whinge(pw, "Unknown list request " + listType);
      return;
    }

    ResultSet rs   = null;    // Result of the retrieval
    Statement stmt = null;    // Retrieve the row
    String query = "select p.PersonID,CONCAT(p.LastName, ', ', p.FirstName), o.Name,o.Division from Person p left join Organization o on (o.OrganizationID = p.OrganizationID) where p.BuildingID=" + bID + otherWhere + " order by p.LastName, p.FirstName";

    try {
      stmt  = BaseSQLClass.dbParams.forceConn().createStatement();
      // System.out.println(query);
      rs = stmt.executeQuery(query);
      while (rs.next()) {
        if (did) { sb.append(","); }
        if ( (filter = rs.getString(4)) != null) {
          if (filter.length() > 0) {
            filter = " - " + filter;
          } else {
            filter = "";
          }
          filter = " (" + rs.getString(3) + filter + ")";
        } else if ( (filter = rs.getString(3)) != null) {
          filter = " (" + filter + ")";
        } else {
          filter = "";
        }
        sb.append("\n[\"" + rs.getString(1) + "\",\"" +
            rs.getString(2) + filter + "\"]");
        did = true;
      }

    } catch (SQLException e) {
      VerbsJson.whinge(pw, query + " " + e.toString());
      return;
    } finally {
      try {
        if (rs   != null) { rs.close(); }
        if (stmt != null) { stmt.close(); }
      } catch (SQLException e) { }
    }

    sb.append("]}");		// End the selections object array
    // System.out.println(sb.toString());
    JsonServlet.writeString(pw, sb.toString());
  }

  public static String makeIn(String[] sels) {
    if ((sels == null) || (sels.length <= 0)) { return null; }
    StringBuilder sb = new StringBuilder();
    for (String s : sels) {
      if (s == null) { continue; }
      if (sb.length() > 0) { sb.append(","); }
      sb.append(s);
    }
    return "in (" + sb.toString() + ")";
  }

  /**
   * Add or subtract persons or organizations from the location being
   * edited.  The building ID is not needed for processing the request,
   * but it is necessary to ensure that the user has logged in such that
   * the current building is known.  TODO make sure user has permission
   */
  public static void addSubPeepsOrgs(PrintWriter pw,
      HttpServletRequest request,
      HttpServletResponse response)
  throws IOException, JSONException {

    //long bID;
    if ( ( /* bID= */ VerbsJson.getBuildingID(request)) <= 0l) {
      VerbsJson.whinge(pw, ParamNames.noBuild);
      return;
    }

    AnyUser uo;
    String [] selections;	// Users or companies being manipulated
    String  in;
    String query = null;
    boolean spacey;

    uo = AnyUser.getUserObject(request);
    if ((uo.editObject == null) || (!(uo.editObject instanceof Location) &&
        !(uo.editObject instanceof Space))) {
      VerbsJson.whinge(pw, "Have not selected a location to which to assign.");
      return;
    }

    int listType = CommandArgs.integerFromString(request.getParameter(ParamNames.WHICH_LIST));

    Long eID = null;
    if (uo.editObject instanceof Location) {
      eID = ((Location)uo.editObject).getLocationID();
      spacey = false;
    } else {
      eID = ((Space)uo.editObject).getSpaceID();
      spacey = true;
    }

    switch (listType) {
    case 0:			// Assign personnel
      selections = request.getParameterValues(ParamNames.ADD_PEEP);
      if ( (in = makeIn(selections)) == null) {
        VerbsJson.whinge(pw, "No people to add.");
      } else {
        query = "update Person set " + (spacey ? "LocationID=null,SpaceID=" + eID : "SpaceID=null,LocationID=" + eID) + " where PersonID " + in;
      }
      break;
    case 1:			// Unassign personnel
      selections = request.getParameterValues(ParamNames.REM_PEEP);
      if ( (in = makeIn(selections)) == null) {
        VerbsJson.whinge(pw, "No people to remove.");
      } else {
        query = "update Person set LocationID=null,SpaceID=null where PersonID " + in;
      }
      break;
    case 2:			// Assign organizations
      selections = request.getParameterValues(ParamNames.ADD_ORGS);
      if ( (in = makeIn(selections)) == null) {
        VerbsJson.whinge(pw, "No companies to add.");
      } else {
        query = "update Organization set " + (spacey ? "LocationID=null,SpaceID=" + eID : "SpaceID=null,LocationID=" + eID) + " where OrganizationID " + in;
      }
      break;
    case 3:			// Unassign organizations
      selections = request.getParameterValues(ParamNames.REM_ORGS);
      if ( (in = makeIn(selections)) == null) {
        VerbsJson.whinge(pw, "No companies to remove.");
      } else {
        query = "update Organization set LocationID=null,SpaceID=null where OrganizationID " + in;
      }
      break;
    default:
      VerbsJson.whinge(pw, "Unknown list type " + listType);
    return;
    }
    if (query == null) { return; }
    if ( (query = SQLU.anyStatement(query)) != null) {
      VerbsJson.whinge(pw, query);
    } else {
      VerbsJson.rejoice(pw, "Database successfully updated.");
    }
  }

  /**
   * Generate a report.
   */
  public static void makeReport(PrintWriter pw,
      HttpServletRequest request,
      HttpServletResponse response)
  throws IOException, JSONException {
    long bID;
    if ( (bID= VerbsJson.getBuildingID(request)) <= 0l) {
      VerbsJson.whinge(pw, ParamNames.noBuild);
      return;
    }

    ResultSet rs   = null;    // Result of the retrieval
    Statement stmt = null;    // Retrieve the row
    GregorianCalendar start = calFromRequest(request, "start", true);
    GregorianCalendar end   = calFromRequest(request, "end",   false);
    StringBuilder sb = new StringBuilder("select h.HappeningID,DATE_FORMAT(h.ExternalTime, '%m/%d/%y %H:%m:%s'),h.Description,l1.DisplayName,l1.Name,s1.DisplayName,s1.Name,l2.DisplayName,l2.Name,s2.DisplayName,s2.Name,d.DisplayName,d.Name,et.Name,et2.Name,h.HappeningTypeID from Happening h left join Location l1 on (l1.LocationID=h.FromLocationID) left join Space s1 on (s1.SpaceID=h.FromSpaceID) left join Location l2 on (l2.LocationID=h.ToLocationID) left join Space s2 on (s2.SpaceID=h.ToSpaceID) left join Device d on (d.DeviceID=h.DeviceID) left join HappeningType ht on (ht.HappeningTypeID=h.HappeningTypeID) left join EnglishTerm et on (et.TermID = ht.TermID) left join DeviceType dt on (dt.DeviceTypeID=d.DeviceTypeID) left join EnglishTerm et2 on (et2.TermID = dt.TermID) where (h.HappeningTypeID=" + _TC.HT_Panel_Error + " or h.HappeningTypeID=" + _TC.HT_System_Info + " or BuildingID=" + bID + ")");
    short hType;
    int row = 0;

    if (start != null) {
      sb.append(" and h.ExternalTime>='" + SQL_DATE_TIME.format(start.getTime()) +
      "'");
    }
    if (end != null) {
      sb.append(" and h.ExternalTime<='" + SQL_DATE_TIME.format(end.getTime()) +
      "'");
    }
    sb.append(" order by h.ExternalTime ASC");
    HSSFWorkbook wb = null;
    String xlsName = "report_"+System.currentTimeMillis()+".xls";

    try {
      stmt  = BaseSQLClass.dbParams.forceConn().createStatement();
      // System.out.println(sb.toString());
      rs = stmt.executeQuery(sb.toString());

      if (!rs.next()) {
        sb.setLength(0);
        if (start != null) {
          sb.append(" after " + SQL_DATE_TIME.format(start.getTime()));
        }
        if (end != null) {
          if (sb.length() > 0) { sb.append(" and ");
          sb.append(" before" + SQL_DATE_TIME.format(end.getTime()));
          }
        }
        VerbsJson.whinge(pw, "No events found" + sb.toString());
        return;
      } else {

        wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet();
        sheet.setColumnWidth(0, 256*18);
        sheet.setColumnWidth(1, 256*18);
        sheet.setColumnWidth(2, 256*18);
        sheet.setColumnWidth(3, 256*18);
        sheet.setColumnWidth(4, 256*18);
        sheet.setColumnWidth(5, 256*18);

        wb.setSheetName(0, "Report");
        HSSFRow     sRow  = null;
        HSSFCell    sCell = null;
        int rowNum = 1;

        //        HSSFCellStyle clearStyle = wb.createCellStyle();
        //        clearStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        //        clearStyle.setBottomBorderColor(HSSFColor.WHITE.index);
        //        clearStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        //        clearStyle.setLeftBorderColor(HSSFColor.WHITE.index);
        //        clearStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        //        clearStyle.setRightBorderColor(HSSFColor.WHITE.index);
        //        clearStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        //        clearStyle.setTopBorderColor(HSSFColor.WHITE.index);

        HSSFCellStyle titleStyle = wb.createCellStyle();
        titleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        titleStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        titleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        titleStyle.setLeftBorderColor(HSSFColor.BLACK.index);
        titleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        titleStyle.setRightBorderColor(HSSFColor.BLACK.index);
        titleStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        titleStyle.setTopBorderColor(HSSFColor.BLACK.index);
        HSSFFont titleFont = wb.createFont();
        titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        titleStyle.setFont(titleFont);

        HSSFCellStyle headerStyle = wb.createCellStyle();
        headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        headerStyle.setFillForegroundColor(HSSFColor.BLACK.index);
        headerStyle.setFillBackgroundColor (HSSFColor.WHITE.index);
        headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        headerStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        headerStyle.setLeftBorderColor(HSSFColor.BLACK.index);
        headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        headerStyle.setRightBorderColor(HSSFColor.BLACK.index);
        headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        headerStyle.setTopBorderColor(HSSFColor.BLACK.index);
        HSSFFont headerFont = wb.createFont();
        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerStyle.setFont(titleFont);

        HSSFCellStyle lightStyle = wb.createCellStyle();
        lightStyle.setFillPattern(HSSFCellStyle.NO_FILL);
        lightStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        lightStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        lightStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        lightStyle.setLeftBorderColor(HSSFColor.BLACK.index);
        lightStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        lightStyle.setRightBorderColor(HSSFColor.BLACK.index);
        lightStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        lightStyle.setTopBorderColor(HSSFColor.BLACK.index);

        HSSFCellStyle darkStyle = wb.createCellStyle();
        darkStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        darkStyle.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
        darkStyle.setFillBackgroundColor(HSSFColor.WHITE.index);
        darkStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        darkStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        darkStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        darkStyle.setLeftBorderColor(HSSFColor.BLACK.index);
        darkStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        darkStyle.setRightBorderColor(HSSFColor.BLACK.index);
        darkStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        darkStyle.setTopBorderColor(HSSFColor.BLACK.index);

        HSSFCellStyle rowStyle;

        sb.setLength(0);
        sb.append("<table class=\"reportsTable\"><tr><th>Date / Time</th><th>Route Type</th><th>From</th><th>To</th><th>Alarm Type</th><th>Alarm ID</th></tr>");

        sRow = sheet.createRow(rowNum++);
        //sRow.setRowStyle(clearStyle);
        sCell = sRow.createCell(0, HSSFCell.CELL_TYPE_STRING);
        sCell.setCellValue(new HSSFRichTextString("Date / Time"));
        sCell.setCellStyle(headerStyle);
        sCell = sRow.createCell(1, HSSFCell.CELL_TYPE_STRING);
        sCell.setCellValue(new HSSFRichTextString("Route Type"));
        sCell.setCellStyle(headerStyle);
        sCell = sRow.createCell(2, HSSFCell.CELL_TYPE_STRING);
        sCell.setCellValue(new HSSFRichTextString("From"));
        sCell.setCellStyle(headerStyle);
        sCell = sRow.createCell(3, HSSFCell.CELL_TYPE_STRING);
        sCell.setCellValue(new HSSFRichTextString("To"));
        sCell.setCellStyle(headerStyle);
        sCell = sRow.createCell(4, HSSFCell.CELL_TYPE_STRING);
        sCell.setCellValue(new HSSFRichTextString("Alarm Type"));
        sCell.setCellStyle(headerStyle);
        sCell = sRow.createCell(5, HSSFCell.CELL_TYPE_STRING);
        sCell.setCellValue(new HSSFRichTextString("Alarm ID"));
        sCell.setCellStyle(headerStyle);

        start = new GregorianCalendar();
        start.set(Calendar.YEAR, 1990);
        Date startD = start.getTime(); // Put some time in the past
        Date endD   = new Date(); // Defaults to now
        Date scratch;
        String dat;
        do {
          dat = rs.getString(2);
          try {
            scratch = REPORT_DATE_TIME.parse(dat);
          } catch (ParseException e) {
            scratch = new Date();
          }
          if (startD.before(scratch)) { startD = scratch; }
          if (endD.after(scratch))    { endD   = scratch; }

          sb.append("<tr");
          if ((row & 1) != 0) {
            rowStyle = darkStyle;
            sb.append(" class=\"altrow\"");
          } else {
            rowStyle = lightStyle;
          }
          hType = rs.getShort(16);

          sRow = sheet.createRow(rowNum++);
          //sRow.setRowStyle(clearStyle);
          sCell = sRow.createCell(0, HSSFCell.CELL_TYPE_STRING);
          sCell.setCellValue(new HSSFRichTextString(DateFormat.getInstance().format(scratch)));
          sCell.setCellStyle(rowStyle);
          sCell = sRow.createCell(1, HSSFCell.CELL_TYPE_STRING);
          sCell.setCellValue(new HSSFRichTextString(SQLU.stringFromResult(rs, 14)));
          sCell.setCellStyle(rowStyle);

          if (hType == _TC.HT_Panel_Error) {
            sCell = sRow.createCell(2, HSSFCell.CELL_TYPE_STRING);
            sCell.setCellValue(new HSSFRichTextString(SQLU.stringFromResult(rs, 3)));
            sCell.setCellStyle(rowStyle);
            sCell = sRow.createCell(5, HSSFCell.CELL_TYPE_STRING);
            sCell.setCellStyle(rowStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowNum-1, rowNum-1, 2, 5));

            sb.append("><td>" + DateFormat.getInstance().format(scratch) +
                "</td><td>" + SQLU.stringFromResult(rs, 14) +
                "</td><td colspan=\"4\">" + SQLU.stringFromResult(rs,3) +
            "</td></tr>");

          } else {
            sCell = sRow.createCell(2, HSSFCell.CELL_TYPE_STRING);
            sCell.setCellValue(new HSSFRichTextString(extractOptions(rs, 4, 4)));
            sCell.setCellStyle(rowStyle);
            sCell = sRow.createCell(3, HSSFCell.CELL_TYPE_STRING);
            sCell.setCellValue(new HSSFRichTextString(extractOptions(rs, 8, 4)));
            sCell.setCellStyle(rowStyle);
            sCell = sRow.createCell(4, HSSFCell.CELL_TYPE_STRING);
            sCell.setCellValue(new HSSFRichTextString(SQLU.stringFromResult(rs, 3)));
            sCell.setCellStyle(rowStyle);
            sCell = sRow.createCell(5, HSSFCell.CELL_TYPE_STRING);
            sCell.setCellValue(new HSSFRichTextString(extractOptions(rs, 12, 2)));
            sCell.setCellStyle(rowStyle);

            sb.append("><td>" + DateFormat.getInstance().format(scratch) +
                "</td><td>" + SQLU.stringFromResult(rs, 14) +
                "</td><td>" + extractOptions(rs, 4, 4) +
                "</td><td>" + extractOptions(rs, 8, 4) +
                "</td><td>" + SQLU.stringFromResult(rs, 3) +
                "</td><td>" + extractOptions(rs,12, 2) +
            "</td></tr>");
          }
          row++;
        } while (rs.next());

        //        for (; rowNum < 296; rowNum++) {
        //          sRow = sheet.createRow(rowNum);
        //          sRow.setRowStyle(clearStyle);
        //        }

        sRow = sheet.createRow(0);
        //sRow.setRowStyle(clearStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
        sCell = sRow.createCell(0, HSSFCell.CELL_TYPE_STRING);
        StringBuilder cv = new StringBuilder("Wayfinding System Report: ");
        cv.append(SHOW_DATE_TIME.format(endD)).append(" to ").append(SHOW_DATE_TIME.format(startD));
        sCell.setCellValue(new HSSFRichTextString(cv.toString()));
        sCell.setCellStyle(titleStyle);

        JsonServlet.writeString(pw, "{\"st\":\"" + SHOW_DATE_TIME.format(endD) + "\",");
        JsonServlet.writeString(pw, "\"et\":\"" + SHOW_DATE_TIME.format(startD) + "\",");
        JsonServlet.writeString(pw, "\"fn\":\"" + JsonServlet.mapBrowseRoot + xlsName + "\",");
        JsonServlet.writeString(pw, "\"tb\":\"" + sb.toString().replace("\"","\\\"") + "</table>" + "\"}");
        sb.setLength(0);
      }
    } catch (SQLException e) {
      VerbsJson.whinge(pw, sb.toString() + " " + e.toString());
      return;
    } finally {
      try {
        if (rs   != null) { rs.close(); }
        if (stmt != null) { stmt.close(); }
      } catch (SQLException e) { }
    }

    if (wb != null) {
      try {
        System.out.println(JsonServlet.mapFileRoot + xlsName);
        wb.write(new FileOutputStream(JsonServlet.mapFileRoot + xlsName));
      } catch (Exception e) {
        System.out.println("ERR Writing spread sheet " + e.toString());
      }
    }

    if (sb.length() > 0) {
      JsonServlet.writeString(pw, sb.toString());
    }
  }
}
