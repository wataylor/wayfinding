/* @name StringsManager.java

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

package zetek.server.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zetek.dbcommon.BaseSQLClass;
import zetek.dbcommon.SQLU;
import zetek.readmeta.classes.Derivation;
import zetek.server.JsonServlet;
import zetek.server.RouteServlet;
import dbaccess.Device;
import dbaccess.Floor;
import dbaccess.Location;
import dbaccess.Organization;
import dbaccess.Person;
import dbaccess.Space;
import edu.uci.ics.jung.graph.decorators.StringLabeller;

/**
 * Manage sets of strings which are held in holders of various categories
 * for buildings.

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class StringsManager {

  public static final long serialVersionUID = 1;

  public static final String[] TBLS_ILK = {
    "Space", "Device", "Person", "Location", "Floor", "Organization",
  };

  @SuppressWarnings("unchecked")
  public static final Class[] CLAS_ILK = {
    Space.class, Device.class, Person.class, Location.class, Floor.class,
    Organization.class,
  };

  public static final int SPACE_ILK        = 0;
  public static final int DEVICE_ILK       = 1;
  public static final int PERSON_ILK       = 2;
  public static final int LOCATION_ILK     = 3;
  public static final int FLOOR_ILK        = 4;
  public static final int ORGANIZATION_ILK = 5;

  static Map<Long, Map<String, CollectStrings>> buildingStrings =
    new HashMap<Long, Map<String, CollectStrings>>();

  /** Obligatory constructor.*/
  public StringsManager() { /* */ }

  /**
   * Get information about a searchable string out of a result set and
   * create a new suggestion object.  Closes the result set.
   * @param len length of the string needed to match the key of this string
   * @param sm StringsManager which is accumulating strings for this batch
   * @param ilk identifies the table from which the string is taken
   * @param rs result set
   * @throws SQLException
   */
  public static void stringResultSet(Integer len, CollectStrings sm, int ilk,
                                     ResultSet rs) throws SQLException {
    String s;
    long flo;
    while (rs.next()) {
      flo = rs.getLong(3);
      // System.out.println(flo + " " + rs.getString(1) + " " + rs.getString(2));
      s = rs.getString(1).replace("_", " ");
      /* Organization names may end with a - and a division name.  If the
       * division is blank, the string will end with a - followed by a
       * space which is ugly.  This line eliminates the dash in the case
       * of an empty division name. */
      if (s.endsWith("- ")) { s = s.substring(0, s.length()-2).trim(); }
      sm.addString(len, s, ilk, flo);
      s = SQLU.stringFromResult(rs, 2);
      if ((s != null) && (s.length() > 0)) {
        sm.addString(len, s, ilk, flo);
      }
    }
    rs.close();
  }

  /**
   * Collect all strings which could be used to describe an object in a
   * building
   * @param buildingID identify the building
   * @param fID identify the floor of the building, 0 means all floors
   * @param type what is to be sought, either as an object to be
   * displayed and edited or as a destination
   * @return collection of string choices to be displayed to the user.
   * @throws SQLException
   */
  public static CollectStrings getStrings(long buildingID, long fID,
					  String type)
    throws SQLException {
    if ((type == null) || (type.length() <= 0)) { type = "all"; }

    Map<String, CollectStrings> buildingMap;
    CollectStrings cs;
    if ( (buildingMap = buildingStrings.get(buildingID)) == null ) {
      buildingMap = new HashMap<String, CollectStrings>();
      buildingStrings.put(buildingID, buildingMap);
    }

    if ( (cs = buildingMap.get(type)) == null) {
      cs = getStringHolder(buildingID, fID, type);
      buildingMap.put(type, cs);
    }
    return cs;
  }

  public static void discardStrings(long buildingID) {
    buildingStrings.remove(buildingID);
  }

  public static void discardAllStrings() {
    buildingStrings = new HashMap<Long, Map<String, CollectStrings>>();
  }
  /**
   * Collect strings of a certain type.  Note that persons and
   * organizations may or may not be assigned to a space or to a
   * location.  Thus, when looking for a destination, those objects
   * must be verified to be specified to be some where.  When looking
   * for them for editing, it does not matter whether they are
   * assigned anywhere.
   * @param bID building ID
   * @param fID floor ID; 0 means all floors in the building
   * @param type string type such as all, person, or personD.  Note
   * that "all" implies a destination; when an object is being edited,
   * it is searched for by its type.
   * @return
   */
  public static CollectStrings getStringHolder(long bID, long fID, String type)
    throws SQLException {
    Integer len    = new Integer(1);
    ResultSet rs   = null;    // Result of the retrieval
    Statement stmt = null;    // Retrieve the row
    String query   = null;
    CollectStrings collStrings = new CollectStrings();

    boolean all = "all".equals(type);

    try {
      BaseSQLClass.dbParams.forceConn();
      stmt  = BaseSQLClass.dbParams.conn.createStatement();

      if (all || "deviceD".equals(type) || "device".equals(type)) {
	len = 2;
	if (fID <= 0) {
	  query = "select s.Name,s.DisplayName,f.ConstructionOrder from Device s left join Location l on (l.LocationID = s.LocationID) left join Space sp on (sp.SpaceID = s.SpaceID) left join Floor f on (f.FloorID = sp.FloorID) where f.BuildingID=" + bID;
	} else {
	  query = "select s.Name,s.DisplayName,f.ConstructionOrder from Device s left join Location l on (l.LocationID = s.LocationID) left join Space sp on (sp.SpaceID = s.SpaceID) left join Floor f on (f.FloorID = sp.FloorID) where sp.FloorID=" + fID;
	}

	rs = stmt.executeQuery(query);
	stringResultSet(len, collStrings, DEVICE_ILK, rs);
      }

      len = 1;
      if (all || "floorD".equals(type) || "floor".equals(type)) {
	if (fID <= 0) {
	  query = "select f.Name,f.DisplayName,f.ConstructionOrder from Floor f where f.BuildingID=" + bID;
	} else {
	  query = "select f.Name,f.DisplayName,f.ConstructionOrder from Floor f where f.FloorID=" + fID;
	}

	rs = stmt.executeQuery(query);
	stringResultSet(len, collStrings, FLOOR_ILK, rs);
      }

      if (all || "locationD".equals(type) || "location".equals(type) ||
	  "spaceD".equals(type) || "space".equals(type)) {
	if (fID <= 0) {
	  query = "select s.Name,s.DisplayName,f.ConstructionOrder from Location s left join Space sp on (sp.SpaceID = s.SpaceID) left join Floor f on (f.FloorID = sp.FloorID) where f.BuildingID=" + bID;
	} else {
	  query = "select s.Name,s.DisplayName,f.ConstructionOrder from Location s left join Space sp on (sp.SpaceID = s.SpaceID) left join Floor f on (f.FloorID = sp.FloorID) where f.FloorID=" + fID;
	}

	rs = stmt.executeQuery(query);
	stringResultSet(len, collStrings, LOCATION_ILK, rs);
      }

      if (all || "organizationD".equals(type)) {
	if (fID <= 0) {
	  query = "select CONCAT(s.Name, ' - ',  s.Division) as Name,s.DisplayName,s.OrganizationID from Organization s left join Location l on (l.LocationID = s.LocationID) left join Space sp on (sp.SpaceID = l.SpaceID) left join Floor f on (f.FloorID = sp.FloorID) where f.BuildingID=" + bID;
	  rs = stmt.executeQuery(query);
	  stringResultSet(len, collStrings, ORGANIZATION_ILK, rs);
	  query = "select CONCAT(s.Name, ' - ',  s.Division) as Name,s.DisplayName,s.OrganizationID from Organization s left join Space sp on (sp.SpaceID = s.SpaceID) left join Floor f on (f.FloorID = sp.FloorID) where f.BuildingID=" + bID;
	} else {
	  query = "select CONCAT(s.Name, ' - ',  s.Division) as Name,s.DisplayName,s.OrganizationID from Organization s left join Location l on (l.LocationID = s.LocationID) left join Space sp on (sp.SpaceID = l.SpaceID) left join Floor f on (f.FloorID = sp.FloorID) where sp.FloorID=" + fID;
	  rs = stmt.executeQuery(query);
	  stringResultSet(len, collStrings, ORGANIZATION_ILK, rs);
	  query = "select CONCAT(s.Name, ' - ',  s.Division) as Name,s.DisplayName,p.OrganizationID from Organization s left join Space sp on (sp.SpaceID = s.SpaceID) left join Floor f on (f.FloorID = sp.FloorID) where sp.FloorID=" + fID;
	}

	rs = stmt.executeQuery(query);
	stringResultSet(len, collStrings, ORGANIZATION_ILK, rs);
      }

      /* If organizations are being selected for editing, it does not
       * matter whether they are assigned to a location or not.*/
      if ("organization".equals(type)) {
	query = "select CONCAT(s.Name, ' - ',  s.Division) as Name,s.DisplayName,s.OrganizationID from Organization s";
	rs = stmt.executeQuery(query);
	stringResultSet(len, collStrings, ORGANIZATION_ILK, rs);
      }

      if (all || "personD".equals(type)) {
	if (fID <= 0) {
	  query = "select concat(p.LastName, ' ', p.FirstName),'null',p.PersonID from Person p left join Location l on (l.LocationID = p.LocationID) left join Space sp on (sp.SpaceID = l.SpaceID) left join Floor f on (f.FloorID = sp.FloorID) where f.BuildingID=" + bID;
	  rs = stmt.executeQuery(query);
	  stringResultSet(len, collStrings, PERSON_ILK, rs);
	  query = "select concat(p.LastName, ' ', p.FirstName),'null',p.PersonID from Person p left join Space sp on (sp.SpaceID = p.SpaceID) left join Floor f on (f.FloorID = sp.FloorID) where f.BuildingID=" + bID;
	} else {
	  query = "select concat(p.LastName, ' ', p.FirstName),'null',p.PersonID from Person p left join Location l on (l.LocationID = p.LocationID) left join Space sp on (sp.SpaceID = l.SpaceID) left join Floor f on (f.FloorID = sp.FloorID) where sp.FloorID=" + fID;
	  rs = stmt.executeQuery(query);
	  stringResultSet(len, collStrings, PERSON_ILK, rs);
	  query = "select concat(p.LastName, ' ', p.FirstName),'null',p.PersonID from Person p left join Space sp on (sp.SpaceID = p.SpaceID) left join Floor f on (f.FloorID = sp.FloorID) where sp.FloorID=" + fID;
	}

	rs = stmt.executeQuery(query);
	stringResultSet(len, collStrings, PERSON_ILK, rs);
      }

      /* If persons are being selected for editing, it does not
       * matter whether they are assigned to a location or not.*/
      if ("person".equals(type)) {
	query = "select concat(p.LastName, ' ', p.FirstName),'null',p.PersonID from Person p";
	rs = stmt.executeQuery(query);
	stringResultSet(len, collStrings, PERSON_ILK, rs);
      }

      if (all || "locationD".equals(type) || "location".equals(type) ||
	  "spaceD".equals(type) || "space".equals(type)) {
	if (fID <= 0) {
	  query = "select s.Name,s.DisplayName,f.ConstructionOrder from Space s left join Floor f on (f.FloorID = s.FloorID) where f.BuildingID=" + bID;
	} else {
	  query = "select s.Name,s.DisplayName,f.ConstructionOrder from Space s where s.FloorID=" + fID;
	}

	rs = stmt.executeQuery(query);
	stringResultSet(len, collStrings, SPACE_ILK, rs);
      }

    } catch (SQLException e) {
      String message = query + " " + e.toString();
      if (JsonServlet.debugTransaction) {
        System.out.println(message);
      }
      throw e;
    } finally {
      try {
        if (rs   != null) { rs.close(); }
        if (stmt != null) { stmt.close(); }
      } catch (SQLException e) { }
    }
    return collStrings;
  }

  @SuppressWarnings("unchecked")
  public static LocDesc findLocDescription(String name, BaseSQLClass bsq) {
    Class cl = bsq.getClass();
    for (int i=0; i<CLAS_ILK.length; i++) {
      if (cl == CLAS_ILK[i]) { return (findLocDescription(name, i)); }
    }
    return null;
  }

  public static LocDesc findLocDescription(String name, int ilk) {
    List<String> al;
    LocDesc loc;
    String[] s;
    String sName = SQLU.quoteForSQL(name);
    String query = "select " + TBLS_ILK[ilk] +
      "ID, f.FloorDWG, f.ConstructionOrder, t.Name from " + TBLS_ILK[ilk] + " t left join Floor f on (f.FloorID = t.FloorID) where t.Name='" +
      sName + "' or t.DisplayName='" + sName+ "'";

    // Modify the query as needed
    switch(ilk) {
    case SPACE_ILK:
      break;

    case DEVICE_ILK:
    case PERSON_ILK:
    case ORGANIZATION_ILK:
      return null;		// Should not happen, already mapped

    case LOCATION_ILK:
      query = "select " + TBLS_ILK[ilk] +
	"ID, f.FloorDWG, f.ConstructionOrder, t.X, t.Y from " + TBLS_ILK[ilk] + " t left join Space s on (s.SpaceID = t.SpaceID) left join Floor f on (f.FloorID = s.FloorID) where t.Name='" +
	sName + "' or t.DisplayName='" + sName+ "'";
      break;
    case FLOOR_ILK:
      query = "select FloorID,FloorDWG,ConstructionOrder,Name from Floor t where t.Name='" +
        sName + "' or t.DisplayName='" + sName+ "'";
      break;
    }

    al = SQLU.listQueryPiped(query);
    if (al.size() <= 0) { return null; }

    s = al.get(0).split("\\|");
    if (s.length > 4) {
      return new LocDesc(name, Long.parseLong(s[0]), ilk, s[1].replace("_", " "),
			 Integer.parseInt(s[2]),
			 Double.parseDouble(s[3]), Double.parseDouble(s[4]));
    } else {
      loc = new LocDesc(name, Long.parseLong(s[0]), ilk, s[1].replace("_", " "),
			Integer.parseInt(s[2]));
      // Convert space name to system name
      // TODO fetch building graph data on basis of building ID
      StringLabeller sl = StringLabeller.getLabeller(RouteServlet.bgd.g);
      Derivation d = (Derivation)sl.getVertex(s[3]);
      if ((d != null) && (d.shapeCenter != null)) { loc.recordXY(d.shapeCenter); }
    }
    return loc;
  }
}
