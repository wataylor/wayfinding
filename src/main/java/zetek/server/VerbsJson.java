/* @name VerbsJson.java

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

import java.awt.Shape;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONWriter;

import zetek.common.CommandArgs;
import zetek.common.IntervalPrinter;
import zetek.dbcommon.BaseSQLClass;
import zetek.dbcommon.SQLU;
import zetek.graphserve.CGB;
import zetek.graphserve.DestInfo;
import zetek.graphserve.ShapeSerializer;
import zetek.readmeta.classes.Derivation;
import zetek.server.utils.AnyUser;
import zetek.server.utils.CollectStrings;
import zetek.server.utils.DevDesc;
import zetek.server.utils.LocDesc;
import zetek.server.utils.StringsManager;
import zetek.server.utils.TableObjUpdate;
import zetek.server.utils.CollectStrings.Stringer;
import dbaccess.Building;
import dbaccess.Device;
import dbaccess.EntityRelation;
import dbaccess.Location;
import dbaccess.Organization;
import dbaccess.Person;
import dbaccess.Space;
import dbaccess.bridge._TC;
import edu.uci.ics.jung.graph.decorators.StringLabeller;

/**
 * Database actions to support the application.  Many action handlers
 * look something like this template:

<pre>
    ResultSet rs   = null;    // Result of the retrieval
    Statement stmt = null;    // Retrieve the row
    String query;

    try {
      stmt  = SQL.getStatement();
      //System.out.println(query);
      rs = stmt.executeQuery(query);
      while (rs.next()) {
      }

    } catch (SQLException e) {
      System.out.println(query + " " + e.toString());
    } finally {
      try {
        if (rs   != null) { rs.close(); }
        if (stmt != null) { stmt.close(); }
        } catch (SQLException e) { }
    }
</pre>

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class VerbsJson {

  public static final long serialVersionUID = 1;

  /** Obligatory constructor.*/
  public VerbsJson() { /* */ }

  /** Element IDs have to be unique in a DOM or odd results ensue.
   * Thus, input and search options in a destination form are preceded
   * with the route number and the destination number as in
   * routexx_Dstyy.  This method takes an array of strings which
   * should end a request and returns the string values of the
   * parameters.  It whinges and returns null if a parameter is not
   * found.  */

  @SuppressWarnings("unchecked")
  public static String[] findTrailingParams(PrintWriter pw,
      HttpServletRequest request,
      String[] trails)
  throws JSONException {
    String[] s = new String[trails.length];
    Enumeration<String> en;
    StringBuilder sb = null;
    String nam;
    int i;

    en = request.getParameterNames();
    while (en.hasMoreElements()) {
      nam = en.nextElement();
      for (i=0; i<trails.length; i++) {
        if (nam.endsWith(trails[i])) {
          s[i] = request.getParameter(nam);
          break;
        }
      }
    }

    for (i=0; i<trails.length; i++) {
      if (s[i] == null) {       // A parameter was not found
        if (sb == null) {
          sb = new StringBuilder();
        } else {
          sb.append(", ");
        }
        sb.append(trails[i]);
      }
    }

    if (sb != null) {
      whinge(pw, "Missing request parameter " + sb.toString());
      return null;
    }
    return s;
  }

  public static boolean isY(HttpServletRequest request, String param) {
    String p = request.getParameter(param);
    return ((p != null) && "y".equalsIgnoreCase(p));
  }

  /** Issue an error message to the print writer as a JSON object
   * which the receiver will notice.
   @param pw Print Writer to receive the JSON object
   @param message text to be put up in a client alert box.*/
  public static void whinge(PrintWriter pw, String key, String message)
  throws JSONException {
    if (JsonServlet.debugTransaction) {
      System.out.println(message);
    }
    new JSONWriter(pw)
    .object()
    .key(key).value(message)
    .endObject();
  }

  public static void whinge(PrintWriter pw, String message)
  throws JSONException {
    whinge(pw, "msg", message);
  }

  public static void rejoice(PrintWriter pw, String message)
  throws JSONException {
    whinge(pw, "scs", message);
  }

  /**
   * Return information about a building to the client along with a
   * map from floor DWGs to floor names.
   * @param pw print writer to send output data to the client
   * @param request http request
   * @param response http response
   * @throws IOException
   * @throws JSONException
   */
  public static void makeBuildingInfo(PrintWriter pw,
      HttpServletRequest request,
      HttpServletResponse response)
  throws IOException, JSONException {
    Building buil;
    @SuppressWarnings("unchecked")
    List<Building> buildings = BaseSQLClass.getLimitedMany(Building.class,
        null, null, 1);
    if (buildings == null) {
      whinge(pw, "Database not connected; please refresh the page.");
    } else if (buildings.size() <= 0) {
      whinge(pw, "No buildings in the database.");
    } else {
      JSONObject obj = new JSONObject(buil = buildings.get(0),true);
      AnyUser uo = AnyUser.getUserObject(request);
      /* Setting the building ID in the object forces the set method to
       * re-read the building and the space name.  This is in effect
       * a cache-clearing operation.  */
      uo.setBuilding(buil);
      JsonServlet.writeString(pw, "[");
      JsonServlet.writeString(pw, obj);
      JsonServlet.writeString(pw, ",");
      JsonServlet.writeString(pw, buil.fetchFloorMapJson());
      JsonServlet.writeString(pw, "]");
    }
  }

  /**
   * Get the building ID either from the request or from the session object.
   * The goal is to remember the building ID across multiple requests.  The
   * building ID should always be read before the floor ID.
   * @param request servlet request
   * @return most current building ID
   */
  public static long getBuildingID(HttpServletRequest request) {
    long bID;
    AnyUser uo = AnyUser.getUserObject(request);
    if ( (bID = CommandArgs.longFromString(request.getParameter(ParamNames.BUILDING_ID))) <= 0) {
      if (uo.currentBuilding != null) {
        bID = uo.currentBuilding.getBuildingID();  // Get it from the session if not
      }
    } else {
      uo.setBuildingID(bID);
    }
    return bID;
  }

  /**
   * Get the floor ID either from the request or from the session object.
   * The goal is to remember the building ID across multiple requests.
   * @param request servlet request
   * @return most current building ID
   */
  public static long getFloorID(HttpServletRequest request) {
    long fID;
    AnyUser uo = AnyUser.getUserObject(request);
    if ( (fID = CommandArgs.longFromString(request.getParameter(ParamNames.FLOOR_ID))) <= 0) {
      if (uo.floorID != null) {
        fID = uo.floorID.longValue();  // Get it from the session if not
      }
    } else {
      uo.floorID = fID;
    }
    return fID;
  }

  /**
   * Return building location and person selection strings to the
   * client.  Looks for parameters bID and fID.  Floor ID is optional,
   * defaults to all floors.
   * @param pw print writer to send output data to the client
   * @param request http request
   * @param response http response
   * @throws IOException
   * @throws JSONException
   */
  public static void makeBuildingStrings(PrintWriter pw,
      HttpServletRequest request,
      HttpServletResponse response)
  throws IOException, JSONException {
    CollectStrings sm = null;

    long bID    = getBuildingID(request);
    long fID    = getFloorID(request);          // Floor id if passed in
    String type = request.getParameter("type"); // Defaults to all

    if (bID <= 0) {
      whinge(pw, "No building");
      return;
    }

    try {
      sm = StringsManager.getStrings(bID, fID, type);
    } catch (SQLException e) {
      whinge(pw, e.toString());
      return;
    }
    JsonServlet.writeString(pw, sm);
  }

  /** Request parameters which are required for the suggestion search.  */
  public static final String[] SUGG_PARAMS = {
    ParamNames.searchType, ParamNames.searchValue,
  };

  /**
   * Match user input against the building strings of the appropriate type.
   * @param pw print writer to send output data to the client
   * @param request http request
   * @param response http response
   * @throws IOException
   * @throws JSONException
   */
  public static void makeSuggestions(PrintWriter pw,
      HttpServletRequest request,
      HttpServletResponse response)
  throws IOException, JSONException {
    CollectStrings sm = null;
    String[] s;

    long bID    = getBuildingID(request);
    long fID    = getFloorID(request);          // Floor id if passed in
    if ( (s=findTrailingParams(pw, request, SUGG_PARAMS)) == null) { return; }
    String type = s[0];
    String input= s[1];

    if (bID <= 0) {
      whinge(pw, ParamNames.noBuild);
      return;
    }

    if (input == null) {
      whinge(pw, "No input text supplied");
      return;
    }

    try {
      sm = StringsManager.getStrings(bID, fID, type);
    } catch (SQLException e) {
      whinge(pw, e.toString());
      return;
    }

    Stringer[] ss = sm.matchInput(input);
    if (ss == null) {
      whinge(pw, "No match found");
      //whinge(pw, "Nothing matched the input string \"" + input + "\"");
      return;
    }

    boolean did = false;
    StringBuilder sb = new StringBuilder(10 * ss.length);
    sb.append("[");
    for (Stringer ks : ss) {
      if (did) { sb.append(","); }
      sb.append(ks.toString());
      did = true;
    }
    sb.append("]");
    JsonServlet.writeString(pw, sb);
  }

  /**
   * The user has accepted a suggestion from the list given by
   * <code>makeSuggestions</code>; return information about the
   * selected object either as an object or as a destination.
   * @param pw print writer to send output data to the client
   * @param request http request
   * @param response http response
   * @throws IOException
   * @throws JSONException
   */
  public static void takeSuggestion(PrintWriter pw,
      HttpServletRequest request,
      HttpServletResponse response)
  throws IOException, JSONException {
    String   inDesc  = null;
    LocDesc  locDesc = null;
    BaseSQLClass bsq = null;
    AnyUser uo    = AnyUser.getUserObject(request);
    int whyChoice = CommandArgs.integerFromString(request.getParameter(ParamNames.WHY_CHOICE));

    long bID    = getBuildingID(request);
    if (bID <= 0) {
      whinge(pw, ParamNames.noBuild);
      return;
    }

    String name = request.getParameter(ParamNames.SPACE_NAM);
    String ilkS = request.getParameter(ParamNames.SPACE_TYPE);
    inDesc = request.getParameter(ParamNames.IN_DESCR);

    if ((name == null) || (name.length() <= 0) ||
        (ilkS == null) || (ilkS.length() <= 0)) {
      whinge(pw, "Need both sug and ilk as request parameters");
      return;
    }

    int ilk = CommandArgs.integerFromString(ilkS);

    if (ilk == StringsManager.DEVICE_ILK) {
      Device device = Device.fetchByName(name, bID);
      if (device == null) {
        whinge(pw, "Cannot find device " + name);
        return;
      }
      if (whyChoice == 0) { // want the object
        uo.editObject = device;
        JsonServlet.writeString(pw, new JSONObject(device, true));
        return;
      }
      if (inDesc == null) {
        inDesc = "D|" + device.getName() + "|" +
        device.fetchKeyOfEntity() + "|" + ilk + "|" + device.getType();
      }
      bsq = device.fetchDestination();
      if (bsq == null) {
        whinge(pw, "Device " + name + " is not assigned to a location.");
        return;
      }
    }

    if (ilk == StringsManager.ORGANIZATION_ILK) {
      long colValue = CommandArgs.longFromString(request.getParameter(ParamNames.SUGG_FLOOR));
      Organization organization = Organization.fetchByPrimaryKey(colValue);
      if (organization == null) {
        whinge(pw, "Cannot find organization " + name);
        return;
      }
      if (whyChoice == 0) { // want the object
        uo.editObject = organization;
        JsonServlet.writeString(pw, new JSONObject(organization, true));
        return;
      }
      bsq = organization.fetchDestination();
      if (bsq == null) {
        whinge(pw, "Organization " + name + " is not assigned to a location.");
        return;
      }
    }

    if (ilk == StringsManager.PERSON_ILK) {
      long colValue = CommandArgs.longFromString(request.getParameter(ParamNames.SUGG_FLOOR));
      Person person = Person.fetchByPrimaryKey(colValue);
      if (person == null) {
        whinge(pw, "Cannot find person " + name);
        return;
      }
      if (whyChoice == 0) { // want the object
        uo.editObject = person;
        JsonServlet.writeString(pw, new JSONObject(person, true));
        return;
      }
      bsq = person.fetchDestination();
      if (bsq == null) {
        whinge(pw, "Person " + name + " is not assigned to a location.");
        return;
      }
    }

    if (ilk == StringsManager.FLOOR_ILK) {
      // do nothing, all that is needed is name and ilk
    }

    if (bsq != null) {          // Destination was determined from an object
      name = bsq.getName();
      if ( (locDesc = StringsManager.findLocDescription(name, bsq)) == null) {
        whinge(pw, "Could not find a destination named " + name +
            " of class " + bsq.getClass().getSimpleName());
        return;
      }
    } else {
      if ( (locDesc = StringsManager.findLocDescription(name, ilk)) == null) {
        whinge(pw, "Could not find a " + StringsManager.TBLS_ILK[ilk] +
            " named " + name);
        return;
      }
    }

    if (locDesc == null) {
      whinge(pw, "Ill-defined error regarding destination selection");
      return;
    }

    locDesc.setInDsc(inDesc);
    locDesc.divID = request.getParameter(ParamNames.divID);
    JsonServlet.writeString(pw, locDesc);
  }

  /**
   * The user has been given building data for a building which is
   * associated with some people in the database.  Returns a JSON
   * object containing information about the people who are associated
   * with the selected building.  The client generally verifies that
   * the building has at least one person before making this request,
   * but it is not an error to ask for people associated with a
   * building which has no people.
   * @param pw print writer to send output data to the client
   * @param request http request
   * @param response http response
   * @throws IOException
   * @throws JSONException
   */
  @SuppressWarnings("unchecked")
  public static void buildingFolks(PrintWriter pw,
      HttpServletRequest request,
      HttpServletResponse response)
  throws IOException, JSONException {
    StringBuilder sb = new StringBuilder(50);
    EntityRelation er;
    List<Object> list;
    boolean did = false;

    long bID    = getBuildingID(request);
    if (bID <= 0) {
      whinge(pw, ParamNames.noBuild);
      return;
    }

    sb.append("[");
    list = BaseSQLClass.getMany(Person.class, "left join EntityRelation er on (er.EntityID = Person.PersonID) left join Entity e on (e.EntityID = Person.PersonID)", "e.EntityTypeID=11 and er.Relationship is not null and er.OfEntityID=" + bID + " order by er.Relationship");
    JsonServlet.appendObjectArray(list, sb);
    sb.append(",[");
    list = BaseSQLClass.getMany(EntityRelation.class, "left join Entity e on (e.EntityID = EntityRelation.EntityID)", "e.EntityTypeID=11 and EntityRelation.Relationship is not null and EntityRelation.OfEntityID=" + bID + " order by EntityRelation.Relationship");
    for (Object o : list) {
      er = (EntityRelation)o;
      if (did) { sb.append(","); }
      sb.append("\"" + er.getRelationship() + "\"");
      did = true;
    }
    sb.append("]]");
    if (sb.length() <= 10) {
      whinge(pw, "No persons are associated with the selected building.");
    } else {
      // System.out.println(sb.toString());
      JsonServlet.writeString(pw, sb.toString());
    }
  }

  /**
   * The user has switched to a different page from the page which
   * selected the current building and the new page needs to know the
   * current building.
   * @param pw print writer to send output data to the client
   * @param request http request
   * @param response http response
   * @throws IOException
   * @throws JSONException
   */
  public static void currentBuilding(PrintWriter pw,
      HttpServletRequest request,
      HttpServletResponse response)
  throws IOException, JSONException {
    AnyUser uo = AnyUser.getUserObject(request);
    if (uo.currentBuilding == null) {
      whinge(pw, "No building has been selected.");
    } else {
      JsonServlet.writeString(pw, new JSONObject(uo.currentBuilding, true));
    }
  }

  /**
   * The user has accepted a suggestion from the list given by
   * <code>makeSuggestions</code>; return the selected destination.
   * @param pw print writer to send output data to the client
   * @param request http request
   * @param response http response
   * @throws IOException
   * @throws JSONException
   */
  @SuppressWarnings("unchecked")
  public static void getLocation(PrintWriter pw,
      HttpServletRequest request,
      HttpServletResponse response)
  throws IOException, JSONException {

    long bID    = getBuildingID(request);
    if (bID <= 0) {
      whinge(pw, ParamNames.noBuild);
      return;
    }

    String name = request.getParameter("sug");
    String ilkS = request.getParameter("ilk");

    if ((name == null) || (name.length() <= 0) ||
        (ilkS == null) || (ilkS.length() <= 0)) {
      whinge(pw, "Need both sug and ilk as request parameters");
      return;
    }

    int ilk = CommandArgs.integerFromString(ilkS);

    if (ilk == StringsManager.PERSON_ILK) {
      whinge(pw, "Cannot yet search for persons as locations");
      return;
    }

    List os = BaseSQLClass.getMany(StringsManager.CLAS_ILK[ilk],null,"Name='" +
        SQLU.quoteForSQL(name)+"' or DisplayName='" +
        SQLU.quoteForSQL(name)+ "'");
    if ((os == null) || (os.size() <= 0)) {
      whinge(pw, "Cannot find location named " + name + " in table " +
          StringsManager.TBLS_ILK[ilk]);
    } else {
      AnyUser uo = AnyUser.getUserObject(request);
      uo.editObject = uo.currentLocation = (BaseSQLClass)os.get(0);
      JsonServlet.writeString(pw, new JSONObject(os.get(0), true));
    }
  }

  /**
   * The user has updated the selected destination.
   * @param pw print writer to send output data to the client
   * @param request http request
   * @param response http response
   * @throws IOException
   * @throws JSONException
   */
  public static void updateEditObj(PrintWriter pw,
      HttpServletRequest request,
      HttpServletResponse response)
  throws IOException, JSONException {
    String paramKey = request.getParameter(ParamNames.FORM_KEY);
    long bID    = getBuildingID(request);
    if (bID <= 0) {
      whinge(pw, ParamNames.noBuild);
      return;
    }

    AnyUser uo = AnyUser.getUserObject(request);
    if (uo.editObject == null) {
      whinge(pw, ParamNames.noObj);
      return;
    }
    if (TableObjUpdate.updateTableObject(uo.editObject, request, paramKey)) {
      if ((uo.editObject instanceof Space) &&
          ((Space)uo.editObject).BlockedChanged) {
        GraphVerbs.accessChanged((Space)uo.editObject, bID);
      } else {
        uo.editObject.updateEntity();
      }
      rejoice(pw, "Object successfully updated.");
    } else {
      whinge(pw, "Errors writing new field values.");
    }
  }

  /**
   * The user has supplied enough data to create a new object
   * @param pw print writer to send output data to the client
   * @param request http request
   * @param response http response
   * @throws IOException
   * @throws JSONException
   */
  public static void addNewObj(PrintWriter pw,
      HttpServletRequest request,
      HttpServletResponse response)
  throws IOException, JSONException {
    String paramKey = request.getParameter(ParamNames.FORM_KEY);
    long bID    = getBuildingID(request);
    if (bID <= 0) {
      whinge(pw, ParamNames.noBuild);
      return;
    }

    BaseSQLClass newObj;
    String beEOS    = request.getParameter(ParamNames.BE_EDIT);
    boolean beEO    = "y".equals(beEOS);
    String obClass  = request.getParameter(ParamNames.OBJ_CLASS);

    if ((obClass == null) || (obClass.length() <= 0)) {
      whinge(pw, "The request must specify the object class to be created.");
      return;
    }

    try {
      @SuppressWarnings("unchecked")
      Class aClass = Class.forName("dbaccess." + obClass);
      newObj = (BaseSQLClass)aClass.newInstance();
    } catch (ClassNotFoundException e) {
      whinge(pw, "The class dbaccess." + obClass + " could not be found");
      return;
    } catch (Exception e) {
      whinge(pw, "The class dbaccess." + obClass +
          " could not be instantiated " + e.toString());
      return;
    }

    if (TableObjUpdate.updateTableObject(newObj, request, paramKey)) {
      if (newObj instanceof Person) {
        ((Person)newObj).setBuildingID(bID);
        newObj.writeNewEntity(_TC.ET_Person);
      } else if (newObj instanceof Organization) {
        if (Organization.fetchByNameAndDiv(bID, newObj.getName(), ((Organization) newObj).getDivision()) != null) {
          whinge(pw, ((Organization)newObj).fetchSelector() +
          " already exists.");
          return;
        }
        ((Organization)newObj).setBuildingID(bID);
        newObj.writeNewEntity(_TC.ET_Organization);
      } else {
        whinge(pw, "Unexpected new object class " + obClass);
      }

      // Clean out the building strings; a new entity has joined the building.
      StringsManager.discardStrings(bID);
      rejoice(pw, newObj.fetchKeyOfEntity() + "_" + obClass +
      " successfully created.");
    } else {
      whinge(pw, "Error writing field values.");
    }

    if (beEO) {
      AnyUser uo    = AnyUser.getUserObject(request);
      uo.editObject = newObj;
    }
  }

  /**
   * Return photos which relate to the current object
   * @param pw print writer to send output data to the client
   * @param request http request
   * @param response http response
   * @throws IOException
   * @throws JSONException
   */
  public static void fetchLocPhotos(PrintWriter pw,
      HttpServletRequest request,
      HttpServletResponse response)
  throws IOException, JSONException {
    long bID    = getBuildingID(request);
    if (bID <= 0) {
      whinge(pw, ParamNames.noBuild);
      return;
    }

    AnyUser uo = AnyUser.getUserObject(request);
    if ((uo.editObject == null) || (!(uo.editObject instanceof Location) &&
        !(uo.editObject instanceof Space))) {
      whinge(pw, "Do not have a location for which to get photos.");
      return;
    }

    StringBuilder sb = new StringBuilder();
    String[] s;
    Long eID = null;
    if (uo.editObject instanceof Location) {
      eID = ((Location)uo.editObject).getLocationID();
    } else {
      eID = ((Space)uo.editObject).getSpaceID();
    }

    List<String> vec = SQLU.listQueryPiped("select EntityRelationID,Relationship,Name from EntityRelation where EntityID is null and OfEntityID=" + eID);
    sb.append("[");
    for (String sg : vec) {
      s = sg.split("\\|");
      if (sb.length() > 5) { sb.append (",\n"); }
      sb.append("{\"id\":\"" + s[0] + "\",\"cap\":\"" + s[1] +
          "\",\"fil\":\"" + s[2] + "\"}");
    }
    sb.append("]");
    JsonServlet.writeString(pw, "{\"scs\":" + sb.toString() + "}");
  }

  /**
   * remove a relationship from the database
   * @param pw print writer to send output data to the client
   * @param request http request
   * @param response http response
   * @throws IOException
   * @throws JSONException
   */
  public static void toastRelationship(PrintWriter pw,
      HttpServletRequest request,
      HttpServletResponse response)
  throws IOException, JSONException {
    long rID =
      CommandArgs.longFromString(request.getParameter(ParamNames.REL_ID));
    if (rID <= 0) {
      whinge(pw, "Must supply a relationship ID");
      return;
    }
    String ow;
    if ( (ow = SQLU.anyStatement("delete from EntityRelation where EntityRelationID=" + rID)) == null) {
      rejoice(pw, "Deletion successful.");
    } else {
      whinge(pw, ow);
    }
  }

  public static void getBubbleInfo(PrintWriter pw,
      HttpServletRequest request,
      HttpServletResponse response) throws IOException, JSONException {

    long bID = getBuildingID(request);
    if (bID <= 0) {
      whinge(pw, ParamNames.noBuild);
      return;
    }

    IntervalPrinter ip = new IntervalPrinter();

    String builNickname;
    String[] ids;
    String[] ilks;
    DestInfo info;

    AnyUser uo = AnyUser.getUserObject(request);
    builNickname = uo.currentBuilding.getNickName();

    ids  = request.getParameterValues(ParamNames.SPACE_ID);
    ilks = request.getParameterValues(ParamNames.SPACE_TYPE);

    if ((ids  == null) || (ids.length  != 1) ||
        (ilks == null) || (ilks.length != 1)) {
      VerbsJson.whinge(pw,
      "Must have exactly 1 location in an info bubble request.");
      return;
    }

    info = getDestInfoForBubble(ids[0], ilks[0], builNickname);

    if ((info == null)) {
      VerbsJson.whinge(pw, "Could not find bubble info");
      return;
    }

    JsonServlet.writeString(pw, new JSONObject(info));
    System.out.println(ip.howLongSince() + "got info for " + info.name);
  }

  public static String getDeviceTypeName(String type) {
    if (type == null) { return(""); }
    if ( (type.startsWith("Smoke") || (type.startsWith("Heat"))) ) {
      return("Smoke Alarm");
    }
    if ( (type.startsWith("Monitor W") || (type.startsWith("Monitor T"))) ) {
      return("Waterflow/Track Superv");
    }
    return(type);
  }

  /**
   *
   * @param start beginning of string label such as html and label
   * @param v values of label
   * @param end end of string label such as html
   * @param showEmpty if false return an empty string if v is empty
   * @return formatted string or empty string
   */
  public static String mkLabel(String start, String v,
      String end, boolean showEmpty) {
    if (!showEmpty && (v == null || v.isEmpty())) { return(""); }
    if (v == null) { v = ""; }
    StringBuilder sb = new StringBuilder();
    sb.append(start).append(v).append(end).append("\n");
    return(sb.toString());
  }

  /**
   * Get DestInfo filled with data to populate info bubble
   * @param idS id of the space or location
   * @param ilkS type, either space or location
   * @param builNickname Building nick name
   * @return DestInfo filled with data to populate info bubble
   */
  @SuppressWarnings("unchecked")
  public static DestInfo getDestInfoForBubble(String idS, String ilkS,
      String builNickname) {
    DestInfo ds = new DestInfo();
    int ilk = Integer.valueOf(ilkS);
    List<Organization> orgs;
    List<Person> peeps;
    List<Device> devs;
    String[] s;

    switch (ilk) {
    case StringsManager.DEVICE_ILK:
      devs = BaseSQLClass.getMany(Device.class, null, "DeviceID" +
          SQLU.innies(idS) + " order by Device.Type, Device.Name");
      if ((devs == null) || (devs.size() <= 0)) { return null; }
      // This returns a list of organizations which are based in this location
      ds.name = devs.get(0).getName();
      orgs = new ArrayList<Organization>();
      // This returns a list of persons which are based in this location
      peeps = new ArrayList<Person>();
      break;
    case StringsManager.LOCATION_ILK:
      Location loc =
        Location.fetchByPrimaryKey(CommandArgs.longFromString(idS));
      if (loc == null) { return null; }
      ds.point = new Point2D.Double(loc.getX(), loc.getY());
      ds.name  = SQLU.bestString(loc.getDisplayName(), loc.getName());
      ds.setOver(mkLabel("<p>", loc.getDetails(), "</p>", false));
      // This returns a list of organizations which are based in this location
      orgs = BaseSQLClass.getMany(Organization.class,null, "LocationID=" + idS);
      // This returns a list of persons which are based in this location
      peeps = BaseSQLClass.getMany(Person.class, null, "LocationID=" + idS);
      devs  = BaseSQLClass.getMany(Device.class, null, "LocationID=" + idS + " order by Device.Type, Device.Name");
      break;
    case StringsManager.SPACE_ILK:  // space
      Space space =
        Space.fetchByPrimaryKey(CommandArgs.longFromString(idS));
      if (space == null) { return null; }
      //populate ds from space info
      ds.name = SQLU.bestString(space.getDisplayName(), space.getName());
      // This returns a list of organizations which are based in this space
      orgs = BaseSQLClass.getMany(Organization.class, null, "SpaceID=" + idS);
      // This returns a list of persons which are based in this space
      peeps = BaseSQLClass.getMany(Person.class, null, "SpaceID=" + idS);
      devs  = BaseSQLClass.getMany(Device.class, null, "SpaceID=" + idS + " order by Device.Type, Device.Name");
      ds.setOver(mkLabel("<p>", (SQLU.isEmpty(space.getDetails()) ? "There is currently no information available about this location" : space.getDetails()), "</p>", false));
      break;
    default:
      System.out.println("Unhandled ilk " + ilk);
    return null;
    }

    /* Now format information out of each organization object into the
     * bubble object.  The entire organization object is available so
     * any field can be included. */
    StringBuilder sb = new StringBuilder();
    for (Organization org : orgs) {
      StringBuilder tmpSb = new StringBuilder();
      tmpSb.append(mkLabel("<li><img src=\"/wayfinding/photos/" + builNickname + "/photos/",
          org.getImage(),
          "\" width=\"125\" height=\"125\" class=\"borderBlack\" /></li>",
          false));
      tmpSb.append(mkLabel(
          "<li>Company Name: ", org.getDisplayName(), "</li>", true));
      tmpSb.append(mkLabel(
          "<li>Division/Dept: ", org.getDivision(), "</li>", true));
      tmpSb.append(mkLabel("<li> Phone: ", org.getMainPhone(), "</li>", true));
      tmpSb.append(mkLabel("<li>Fax :", org.getMainFax(), "</li>", true));
      tmpSb.append(mkLabel("<li>Email: ", org.getMainEmail(), "</li>", true));
      if (tmpSb.length() != 0) {
        sb.append("<ul>").append(tmpSb.toString()).append("</ul>");
      }
      sb.append(mkLabel("<p>Details: ", org.getDetails(), "</p>", true));

    }
    if (sb.length() > 0) {
      ds.setOrg(sb.toString());
    }

    /* Now format information out of each person object into the
     * bubble object.  The entire person object is available so any
     * field can be included. */
    sb.setLength(0); // Clear out the string for re-use
    for (Person per : peeps) {
      StringBuilder tmpSb = new StringBuilder();
      tmpSb.append(mkLabel("<li><img src=\"/wayfinding/photos/" +
          builNickname + "/photos/",
          per.getImage(),
          "\" width=\"125\" height=\"125\" class=\"borderBlack\" /></li>",
          false));
      tmpSb.append(mkLabel(
          "<li>First Name: ", per.getFirstName(), "</li>", true));
      tmpSb.append(mkLabel(
          "<li>Last Name: ", per.getLastName(), "</li>", true));
      tmpSb.append(mkLabel("<li>Title: ", per.getTitle(), "</li>", true));
      tmpSb.append(
          mkLabel("<li>Office Phone: ", per.getWorkPhone(), "</li>", true));
      tmpSb.append(
          mkLabel("<li>Mobile Phone: ", per.getMobilePhone(), "</li>", true));
      tmpSb.append(mkLabel("<li>Fax: ", per.getFax(), "</li>", true));
      tmpSb.append(mkLabel("<li>Email: ", per.getEmailAddress(),"</li>", true));
      String tmpS =
        per.fetchOneOrganizationOrganizationIDOrganizationID().getName();
      tmpSb.append(mkLabel("<li>Company: ", tmpS, "</li>", true));

      if (tmpSb.length() != 0) {
        sb.append("<ul>").append(tmpSb.toString()).append("</ul>");
      }
      sb.append(mkLabel("<p>Details: ", per.getDetails(), "</p>", true));
    }
    if (sb.length() > 0) {
      ds.setPeep(sb.toString());
    }

    {
      String type;
      String oldType = "";
      int no = 0;
      boolean did = false;
      sb.setLength(0);
      for (Device dev : devs) {
        type = dev.getType();
        if (!type.equals(oldType)) {
          oldType = type;
          no = 1;
          if (did) { sb.append("<P>&nbsp;</p>"); }
          did = true;
          sb.append("<P><B>" + type + "s</b> in this location</p>");
        }
        sb.append(mkLabel("<p>&nbsp;&nbsp;" + ((no < 10) ? "&nbsp; " : "") + no++ + ". <B>Device ID</b> ", dev.getName() + ((dev.getDetails() == null) ? "" : " " + dev.getDetails()), "</p>", false));
      }
      if (sb.length() > 0) {
        ds.setDev(sb.toString());
      }
    }

    /* Create a list of pipe-separated strings of information about
     * photographs which are associated with the space or location.
     * The first string is the type of the relationship, the second
     * describes the relationship, and the third is the name of
     * whatever is related to the OfEntityID.  In the case of photos,
     * the EntityID is null.  Thus, selecting for a null EntityID only
     * gets photos.  In the case of photos which are related to an
     * entity, the Relationship is the caption and the Name is the
     * name of the photo in the file system.  The software has to know
     * where photos are stored in the file system and in the Apache
     * URL system.*/
    List<String> vec = SQLU.listQueryPiped("select EntityRelationID,Relationship,Name from EntityRelation where EntityID is null and OfEntityID=" + idS);
    sb.setLength(0);
    for (String sg : vec) {
      s = sg.split("\\|"); // xx, caption, photo file name
      sb.append("<div class=\"infoBubblePhoto\">\n");
      sb.append("<img src=\"/wayfinding/photos/" + builNickname + "/photos/");
      sb.append(s[2]);
      sb.append("\" />\n");
      sb.append("<p>");
      sb.append(s[1]);
      sb.append("</p>\n</div>\n");
    }
    if (sb.length() > 0) {
      ds.setPic(sb.toString());
    }
    return ds;
  }

  /**
   * Supply an array of shapes of all inaccessible spaces on this floor
   * This method is called whenever the client changes floors.  It need not
   * be called when changing zoom levels.  The return array includes all the
   * inaccessible spaces on the floor.
   * @param pw print writer to send output data to the client
   * @param request http request
   * @param response http response
   * @throws IOException
   * @throws JSONException
   */
  public static void impedimenta(PrintWriter pw,
      HttpServletRequest request,
      HttpServletResponse response)
  throws IOException, JSONException {
    long bID = getBuildingID(request);
    if (bID <= 0) {
      whinge(pw, ParamNames.noBuild);
      return;
    }

    IntervalPrinter ip = new IntervalPrinter();

    Derivation deriv;
    Shape sh;
    String frobbedShape;
    List<String> vec;
    String floorDWG = request.getParameter(ParamNames.FLOOR_DWG);

    /* The floor ID could be returned as a sub-select iv enclosed in
     * parentheses, but this is only slightly slower and less confusing.  */
    vec = SQLU.listQueryPiped("select FloorID from Floor f where f.BuildingID=" + bID + " and f.FloorDWG=" + SQLU.toSQLConst(floorDWG));
    Long floorID = CommandArgs.longFromString(vec.get(0));

    String query = "select Name from Space s where Blocked='Y' and s.FloorID=" + floorID;
    vec = SQLU.listQuery(query);

    //    List<String> vec2 = Space.fetchBlockedShapesOnFloor(bID, floorDWG);
    //    System.out.println(vec.size() + " " + vec2.size());
    /* return if there are no blocked spaces on the floor. */
    if ((vec == null) || (vec.size() <= 0)) {
      rejoice(pw, "No blocked spaces on this floor");
      return;
    }

    StringLabeller sl = StringLabeller.getLabeller(RouteServlet.bgd.g);
    JSONStringer jw = new JSONStringer();
    jw.array();

    try {
      for (String n : vec) {
        if (( (deriv = (Derivation)sl.getVertex(n)) != null) &&
            (deriv.theShape != null)) {
          sh = CGB.ipu.frobnicateShape(deriv.theShape);
          frobbedShape = ShapeSerializer.serializeShape(sh);
          jw.object();
          jw.key("ilk").value("inac");
          jw.key("sh").value(frobbedShape);
          jw.endObject();
        }
      }
      jw.endArray();
    } catch (Exception e) {
      JsonServlet.logger.error("JsonWriter", e);
      whinge(pw, "JSON Error " + e.toString()); // produces msg
      return;
    }
    /* There are issues with creating .json objects and then writing
     * them using toString - quotes get escaped.  This method turns
     * the object to a string using its toString method but does not escape
     * anything.*/
    JsonServlet.writeString(pw, jw);
    System.out.println(ip.howLongSince() + vec.size() + " impedimenta on " +
        floorDWG);
  }

  /**
   * Supply information icons for all spaces on the floor.  This
   * method is called whenever the client changes floors.  It need not
   * be called when changing zoom levels.  The return array includes
   * all the interesting icons on the floor.
   * @param pw print writer to send output data to the client
   * @param request http request
   * @param response http response
   * @throws IOException
   * @throws JSONException
   */
  public static void pointsOfInterest(PrintWriter pw,
      HttpServletRequest request,
      HttpServletResponse response)
  throws IOException, JSONException {
    long bID = getBuildingID(request);
    if (bID <= 0) {
      whinge(pw, ParamNames.noBuild);
      return;
    } // TODO fetch building graph data on basis of building ID

    IntervalPrinter ip = new IntervalPrinter();

    Derivation deriv;
    String[] s;
    List<String> vec;
    Point2D pt = new Point2D.Double();
    String floorDWG = request.getParameter(ParamNames.FLOOR_DWG);
    //String query;
    List<DevDesc> descList = new ArrayList<DevDesc>();
    DevDesc desc;
    DevDesc[] descs;
    boolean did = false;
    int sentCount = 0;
    int i, j;

    /* The floor ID is used in two queries. */
    vec = SQLU.listQueryPiped("select FloorID from Floor f where f.BuildingID=" + bID + " and f.FloorDWG=" + SQLU.toSQLConst(floorDWG));
    Long floorID = CommandArgs.longFromString(vec.get(0));

    /* get a list of devices for the floor which are related to
     * spaces */
    List<String> spaceDevs = SQLU.listQueryPiped(/*query = */"select d.DeviceID,d.Name,d.Type,s.Name,s.DisplayName from Device d left join Space s on (s.SpaceID=d.SpaceID) where s.FloorID=" + floorID);
    //System.out.println(query);
    List<String> locDevs   = SQLU.listQueryPiped(/*query = */"select d.DeviceID,d.Name,d.Type,s.Name,s.DisplayName,l.X,l.Y from Device d left join Location l on (l.LocationID=d.LocationID) left join Space s on (s.SpaceID=l.SpaceID) where s.FloorID=" + floorID);
    //System.out.println(query);
    spaceDevs.addAll(locDevs);
    int devCount = spaceDevs.size();

    /* return if there are no icons on the floor. */
    if (spaceDevs.size() <= 0) {
      rejoice(pw, "No device icons on this floor");
      return;
    }

    StringLabeller sl = StringLabeller.getLabeller(RouteServlet.bgd.g);

    try {
      // Add all of the devices to the stringer object
      for (String stg : spaceDevs) {
        s = stg.split("\\|");
        descList.add(desc = new DevDesc());
        if (s.length > 5) {
          pt.setLocation(Double.valueOf(s[5]),Double.valueOf(s[6]));
        } else {
          if (( (deriv = (Derivation)sl.getVertex(s[3])) != null) &&
              (deriv.shapeCenter != null)) {
            pt.setLocation(deriv.shapeCenter);
          }
        }
        desc.point = CGB.ipu.frobnicatePoint(pt);
        desc.type  = s[2];
        desc.id.append(s[0]);
        desc.name  = SQLU.bestString(s[4], s[3]);
      }
      descs = descList.toArray(new DevDesc[descList.size()]);
      Arrays.sort(descs);
    } catch (Exception e) {
      JsonServlet.logger.error("JsonWriter", e);
      whinge(pw, "JSON Error " + e.toString()); // produces msg
      return;
    }

    /* Now find all the co-located devices and combine them.*/
    for (i=0; i<descs.length; i++) {
      if ( (desc = descs[i]) == null) { continue; }
      for (j=i+1; j<descs.length; j++) {
        if (!desc.colo(descs[j])) { break; }
        desc.id.append("|" + descs[j].id);
        descs[j] = null;
      }
    }

    /* There are issues with creating .json objects and then writing
     * them using toString - quotes get escaped.  This method turns
     * the object to a string using its toString method but does not escape
     * anything.*/
    JsonServlet.writeString(pw, "[");
    for (DevDesc dd : descs) {
      if (dd != null) {
        if (did) JsonServlet.writeString(pw, ",");
        JsonServlet.writeString(pw, dd.toString());
        sentCount++;
        did = true;
      }
    }
    JsonServlet.writeString(pw, "]");
    System.out.println(ip.howLongSince() + devCount +
        " points of interest on " + floorDWG + ", " +
        sentCount + " separate locations / types");
  }
}
