/* @name ParamNames.java

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

/**
 * Defines constants which are used to refer to request parameters and
 * other variables which must be mutually comprehensible between client
 * and server.  All servlets and verbs which take the same parameters
 * use the same names for them.

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class ParamNames {

  public static final long serialVersionUID = 1;

  /** Obligatory constructor.*/
  public ParamNames() { /* */ }

  /** String which identifies the parameter which specifies the action
   * to be taken by the servlet.  The meaning of the numerical verb
   * and the parameters it requires is determined by the servlet.</p>

   * json servlet verbs return JSON objects.  If the returned object
   * has a parameter "msg", it means that there was an error.  The
   * value of the parameter should be displayed in an alert box and
   * the rest of the data should be ignored.  The verbs are:

   <ul>

   <li><b>1</b> search for building information.  It accepts the
   input parameters which are defined in the building search form.</li>

   <li><b>2</b> create a list of all the building strings in the
   database.  This is for testing only; it returns a great deal of
   data.</li>

   <li><b>3</b> create a JSON array of suggestions which match the
   current input string.  Takes building ID, floor ID, type, and
   input parameters.</li>

   <li><b>4</b> accept a JSON suggestion and returns location
   information.  Takes buildingID if present, otherwise uses the
   building ID in the session object.</li>

   </ul>

   * html servlet verbs return .html code fragments as strings.  If a
   * string starts with ERR, it means that there was an error.  The
   * string should be displayed in an alert box and the rest of the
   * data should be ignored.  The verbs are:

   <ul>

   <li><b>1</b> return the .html code for a route which is
   identified by the route parameter.  The .html is updated to
   reflect the proper route number in all element IDs.</li>

   <li><b>2</b> return the .html code for a destination within a
   sub-route which is identified by route number and destination
   number.  The .html is updated to reflect the proper route and
   destination numbers in all element IDs.</li>

   <li><b>3</b>return the .html code for a vertical which is needed to
   carry out a sub-route which is identified by route number and
   destination number.  The .html is updated to reflect the proper
   route, destination, and vertical numbers in all element IDs.</li>

   </ul>
  */

  public static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

  public static final String VERB = "verb";

  /** String which identifies the parameter which causes the servlet
   * to print all its request parameters to the log.*/
  public static final String DUMP = "dump";

  /** String which identifies the parameter which causes the servlet
   * to enter debug mode for the current transaction.*/
  public static final String DEBUG       = "debug";
  /** Identifies a numerical building ID.  Each session is associated
   * with a session object which remembers the current building ID.
   * Any verb which requires a buildingID checks to see if the
   * building ID is specified in the URL; uses the one in
   * the session object if not.  */
  public static final String BUILDING_ID = "bID";
  /** Identifies a numerical floor ID.  Each session is associated
   * with a session object which remembers the current building ID.
   * Any verb which requires a floorID checks to see if the
   * floor ID is specified in the URL and if not, uses the one in
   * the session object. */
  public static final String FLOOR_ID    = "fID";
  /** Identifies a sub-type within a verb or specifies the type of
   * data to be returned from a request.*/
  public static final String TYPE        = "type";
  /** Identifies the route on whose behalf data are requested.  Route
   * numbers run from 1.*/
  public static final String ROUTE_NO    = "rteno";
  /** Identifies the sub-route within a route on whose behalf data
   * are requested.  Sub-route numbers run from 0; destination 0 is
   * the start of the route; destination 1 is the first destination to
   * be reached from the starting point.*/
  public static final String SUB_NO     = "subno";
  /** Identifies the destination within a route on whose behalf data
   * are requested.  Destination numbers run from 0; destination 0 is
   * the start of the route; destination 1 is the first destination to
   * be reached from the starting point.*/
  public static final String DEST_NO     = "dstno";
  /** Identifies the vertical traverse number within a destination on
   * whose behalf data are requested.  Vertical numbers run from 1 but
   * are not particularly meaningful as they are inserted and deleted
   * automatically.*/
  public static final String VERT_NO     = "vrtno";

  /** Identifies the list of space names passed back during a route
   * request.  These names are derived from the chosen suggestion and
   * match the suggestion attribute which is defined in the Strings
   * Manager and in LocDesc.*/
  public static final String SPACE_NAM   = "sug";

  /** Identifies the list of floor drawings in a series of
   * LocDesc destinations passed back from the client.  */
  public static final String FLOOR_DWG   = "FDWG";
  /** Identifies the list of floor construction orders passed back
   * during a route request.  These types are derived from the chosen
   * destination as an instance of LocDesc.*/
  public static final String FLOOR_ORDER = "FOrd";
  /** Floor order is returned as part of a suggestion in this parameter.*/
  public static final String SUGG_FLOOR  = "flo";
  /** Identifies the list of space descriptions in a series of
   * LocDesc destinations passed back from the client*/
  public static final String SPACE_DESC  = "desc";
  /** Identifies the list of route and destination divisions in the
   * DOM in a series of LocDesc destinations passed back from the client. */
  public static final String DIV_ID      = "divID";
  /** Identifies the list of location IDs in a series of LocDesc destinations
   * passed back from the client*/
  public static final String SPACE_ID    = "id";
  /** Identifies the space name types passed back during a
   * route request.  These types are derived from the chosen
   * suggestion and match the suggestion type attribute which sent
   * back to the client as a chosen LocDesc destination.*/
  public static final String SPACE_TYPE  = "ilk";
  /** A destination may have an additional description about where in the
   * space or location the route ends.  This internal description gives
   * different data depending on the initial letter.*/
   public static final String IN_DESCR   = "inDsc";
  /** A suggestion may be chosen for many reasons; this tells why the
   * suggestion was returned to the server.  1 -> destination, 0 ->
   * editing an object. */
  public static final String WHY_CHOICE  = "why";
  /** Forms have the same string prepended to all the field names to make it
   * easier to separate out parameter names for updating objects.*/
  public static final String FORM_KEY    = "fKey";
  /** Objects which are created may or may not want to become the current edit
   * object.  This parameter is ignored unless it is present and has a value of
   * "y" in which case the new object becomes the edit object*/
  public static final String BE_EDIT     = "beEO";
  /** The class name must be specified when a new object is being created.
   * This parameter specifies the name of the class.*/
  public static final String OBJ_CLASS   = "ojCl";
  /** Identify a relationship whose iID is passed in a request*/
  public static final String REL_ID      = "rID";
  /** The client needs various different selection lists.  This parameter
   * tells the server which list is desired.
   * 0 -> all organization names, used for people editor
   * 1 -> organizations assigned to current location or not assigned to
   * current location, filtered by "assigned" parameter and other fields.*/
  public static final String WHICH_LIST  = "wLs";
  public static final String ASSIGNED    = "ast";
  /** This parameter is a letter or string with which the organization name
   * must start for the organization to pass the filter. */
  public static final String ORG_FILTER  = "filterCompanyName";
  public static final String PERS_FILTER = "filterLastName";
  /** The parameter supplies an organization ID to which people must be
   * assigned in order for a person to pass the filter.*/
  public static final String ORG_ID_FILT = "filterCompany";
  /** Parameter which lists organizations which are to be removed from
   * a location.*/
  public static final String REM_ORGS    = "assignedCompanies";
  public static final String ADD_ORGS    = "companyNameList";
  public static final String REM_PEEP    = "assignedPersonnelList";
  public static final String ADD_PEEP    = "unassignedPersonnelList";

  /** Element IDs have to be unique in a DOM or odd results ensue.
   * Thus, input and search options in a destination form are preceded
   * with the route number and the destination number as in
   * routexx_Dstyy.  This string defines the end of the request
   * parameter which contains the destination search type.*/
  public static final String searchType = "SrchOptsMenu";
  /** Element IDs have to be unique in a DOM or odd results ensue.
   * Thus, input and search options in a destination form are preceded
   * with the route number and the destination number as in
   * routexx_Dstyy.  This string defines the end of the request
   * parameter which contains the destination search type.*/
  public static final String searchValue = "SrchInputField";
  /** Some requests contain a destination division ID of the form
   * routexxDstyy*/
  public static final String divID = "divID";
  /** Error message returned when a building is needed and none has yet
   * been specified*/
  public static final String noBuild = "No building has been specified.";
  /** Error message returned when a location is to be updated but none has yet
   * been specified*/
  public static final String noObj   = "No object has been specified for update.";
}
