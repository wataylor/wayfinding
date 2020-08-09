/* @name GraphVerbs.java

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

import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import zetek.common.CommandArgs;
import zetek.common.IntervalPrinter;
import zetek.dbcommon.BaseSQLClass;
import zetek.dbcommon.DBParams;
import zetek.dbcommon.SQLU;
import zetek.graphserve.CGB;
import zetek.graphserve.DestInfo;
import zetek.graphserve.EdgeWeight;
import zetek.graphserve.GraphUtilsServer;
import zetek.graphserve.SGB;
import zetek.graphserve.SubRoute;
import zetek.io.GraphToXML;
import zetek.readmeta.classes.Derivation;
import zetek.server.utils.AnyUser;
import zetek.server.utils.LocDesc;
import zetek.server.utils.StringsManager;
import dbaccess.Happening;
import dbaccess.Location;
import dbaccess.Space;
import dbaccess.bridge._TC;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.StringLabeller;
import edu.uci.ics.jung.utils.GraphUtils;

/**
 * Routines to handle building graphs to support the Path Servlet.

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class GraphVerbs {

  public static final long serialVersionUID = 1;

  public static String graphFilePath;
  public static IntervalPrinter ip = new IntervalPrinter();
  public static java.util.Date accessibilityDelta = new java.util.Date();

  /** Obligatory constructor.*/
  public GraphVerbs() { /* */ }

  public static Logger logger = Logger.getLogger(DBParams.class);

  /**
   * Load the building graph from the specified xml file
   * @param filePath complete path to the .xml file in the file system
   * @return the completed graph after breaking connections related to
   * inaccessible locations
   */
  @SuppressWarnings("unchecked")
  public static Graph loadBuildingGraph(String filePath) {
    ip.reset(); // Set the start of the interval to the present
    graphFilePath = filePath;

    GraphToXML mlf = new GraphToXML();
    Graph localGraph = mlf.load(filePath);
    // GraphInfo gi = mlf.getGraphInfo();
    StringLabeller sl = StringLabeller.getLabeller(localGraph);
    sl.clear();

    Derivation deriv;

    for (Iterator<Derivation> iter = localGraph.getVertices().iterator();
	 iter.hasNext();) {
      deriv = iter.next();
      try {
	sl.setLabel(deriv, deriv.getStringRep());  // Tell name of node
      } catch (Exception e) {
	String message = "ERR bad node " + deriv.getIlk() + " " +
	  deriv.getStringRep() + " " + e.toString();
	logger.error(message);
      }
    }

    /* TODO make this specific to the building by knowing a) which
     * building relates to the graph and b) putting enough joins in
     * the query to make sure that all spaces belong to the current
     * building.*/
    List<String> vec=SQLU.listQuery("select Name from Space where Blocked='Y'");
    for (String s : vec) {
      if ( (deriv = (Derivation)sl.getVertex(s)) != null) {
        disconnect(localGraph, deriv);
      }
    }

    RouteServlet.bgd.floorMap = makeGraphFlorist(localGraph);
    RouteServlet.bgd.dsp = new DijkstraShortestPath(localGraph, EdgeWeight.getEdgeWeightInstance());
    System.out.println(ip.howLongSince() + "to load graph file " + filePath);

    return localGraph;
  }

  /**
   * Disconnect a graph vertex from the graph so that it is inaccessible and
   * no paths can cross it.  Shortcuts which depend on the existence of the
   * location must also be removed.
   * @param g the graph in which the vertex appears
   * @param d the node to be removed
   */
  @SuppressWarnings("unchecked")
  public static void disconnect(Graph g, Derivation d) {
    if (d == null) { return; }
    Derivation nb;
    Iterator<Edge> edges;
    Edge edge;
    String ud;
    Iterator<Derivation> neighs = d.getNeighbors().iterator();
    int i = 0;

    for (; neighs.hasNext(); ) {
      nb = neighs.next();
      for (edges = nb.getIncidentEdges().iterator(); edges.hasNext(); ) {
	edge = edges.next();
	if ( (ud = (String)edge.getUserDatum(SGB.NAME_OF_SKIP_SPACE)) != null) {
	  if (d.StringRep.equals(ud)) {
	    g.removeEdge(edge);
	    i++;
	  }
	}
      }
    }
    System.out.println("Disconnecting " + d.StringRep + " " +
		     d.getInEdges().size() + " ins " +
		     d.getOutEdges().size() + " outs " + i + " shortcuts");
    GraphUtils.removeEdges(g, d.getInEdges());
    GraphUtils.removeEdges(g, d.getOutEdges());
  }

  /**
   * Examine all of the vertices in a graph and sort them by floor
   * drawing name.
   * @param g input graph
   * @return map floor names to array lists of derivations
   */
  @SuppressWarnings("unchecked")
  public static Map<String, List<Derivation>> makeGraphFlorist(Graph g) {
    Derivation d;
    List<Derivation> fList;
    Map<String, List<Derivation>> fMap =
      new HashMap<String, List<Derivation>>();
    Set<Vertex> vers= g.getVertices();
    String fd;

    for (Vertex v : vers) {
      d = (Derivation)v;
      if ( (fList = fMap.get(fd = d.floorDWG)) == null) {
	fList = new ArrayList<Derivation>();
	fMap.put(fd, fList);
      }
      fList.add(d);
    }
    return fMap;
  }

  /**
   * Examine a graph floor map for a derivation whose shape contains a
   * specified point
   * @param map floor map
   * @param floorDWG name of the floor
   * @param pt point which ought to fall within a shape on the floor
   * but might not depending on where the user smote the mouse
   * @return derivation whose shape encloses the point or null if none
   * do
   */
  public static Derivation elSmacko(Map<String, List<Derivation>> map,
				    String floorDWG, Point2D pt) {
    List<Derivation> fList;

    if ((map == null) || ( (fList=map.get(floorDWG)) == null)) { return null; }
    if (pt == null) {
      JsonServlet.logger.error("Trying to find a null point");
      return null;
    }
    for (Derivation d : fList) {
      if ((d.theShape != null) && (d.theShape.contains(pt))) { return d; }
    }
    return null;
  }

  /**
   * Return information about a route within a building to the client.
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

    IntervalPrinter ip = new IntervalPrinter();

    SubRoute rte;
    String[] dscs;
    String[] dvid;
    String[] dwgs;
    String[] fods;
    String[] ids;
    String[] ilks;
    String[] inDscs;

    DestInfo from;
    DestInfo to;

    dscs   = request.getParameterValues(ParamNames.SPACE_DESC);
    dvid   = request.getParameterValues(ParamNames.DIV_ID);
    dwgs   = request.getParameterValues(ParamNames.FLOOR_DWG);
    fods   = request.getParameterValues(ParamNames.FLOOR_ORDER);
    ids    = request.getParameterValues(ParamNames.SPACE_ID);
    ilks   = request.getParameterValues(ParamNames.SPACE_TYPE);
    inDscs = request.getParameterValues(ParamNames.IN_DESCR);

    if ((dscs   == null) || (dscs.length   != 2) ||
	(dvid   == null) || (dvid.length   != 2) ||
	(dwgs   == null) || (dwgs.length   != 2) ||
	(fods   == null) || (fods.length   != 2) ||
	(ids    == null) || (ids.length    != 2) ||
	(ilks   == null) || (ilks.length   != 2) ||
        (inDscs == null) || (inDscs.length != 2)) {
      VerbsJson.whinge(pw, "Must have exactly 2 destinations in a route request.");
      return;
    }

    /* A null value on the client is converted into the string "null" for
     * transmission as part of the URL.  Thus, strings with null values
     * have to be converted back to null.  */
    for (int i=0; i<inDscs.length; i++) {
      if ("null".equals(inDscs[i])) { inDscs[i] = null; }
    }

    Happening hap = new Happening();
    hap.setBuildingID(bID);
    from = getSystemName(ids[0], ilks[0], hap, true);
    to   = getSystemName(ids[1], ilks[1], hap, false);

    if ((from == null) || (to == null)) {
      VerbsJson.whinge(pw, "Could not find a destination in the route request.");
      return;
    }

    if (from == to) {
      VerbsJson.whinge(pw, "Start and end destinations are the same, " + from);
      return;
    }

    from.inDesc = inDscs[0];
    to  .inDesc = inDscs[1];

    if ( (rte=GraphUtilsServer.routeBetweenDests(RouteServlet.bgd, false, from, to)) ==
	 null) {
      VerbsJson.whinge(pw, "Cannot get from " + from.name + " to " + to.name);
      return;
    }

    /* If the route has been modified, have to determine a new destination
     * description.*/
    if (to.modifier != 0) {
      /* For the moment, there are no locations in the graph, so the new
       * destination has to be a space.*/
      LocDesc ld = StringsManager.findLocDescription(rte.fetchLastNodeName(), StringsManager.SPACE_ILK);
      if (to.point != null) {
        ld.recordXY(to.point);
      }
      rte.specifyLastSupplement(ld);
      hap.setToSpaceID(ld.id);
      hap.setToLocationID(null);
    }

    hap.setHappeningTypeID((long)_TC.HT_Non_Alarm);
    hap.writeFromKey(null);

    JsonServlet.writeString(pw, rte);
    System.out.println(ip.howLongSince() + "to route from " +
		       from.name + " to " + to.name);
  }

  /**
   * A user has requested a change in the accessibility of a graph node
   * @param pw print writer to send output data to the client
   * @param request  http request
   * @param response http response
   * @throws IOException
   * @throws JSONException
   */
  @SuppressWarnings("unchecked")
  public static void alterAccess(PrintWriter pw,
				 HttpServletRequest request,
				 HttpServletResponse response)
    throws IOException, JSONException {
    long bID;
    if ( (bID= VerbsJson.getBuildingID(request)) <= 0l) {
      VerbsJson.whinge(pw, ParamNames.noBuild);
      return;
    } // TODO fetch building graph data on basis of building ID

    Derivation deriv = null;
    Point2D pt;
    String what = request.getParameter(ParamNames.WHY_CHOICE);
    Space space;
    String floorDWG;
    List<Space> sl;
    double x,y;

    IntervalPrinter ip = new IntervalPrinter();

    x = CommandArgs.doubleFromString(request.getParameter("x"));
    y = CommandArgs.doubleFromString(request.getParameter("y"));
    pt = CGB.ipu.unFrobnicatePoint(new Point2D.Double(x, y));
    if (pt == null) {
      VerbsJson.whinge(pw, "Point " + x + " " + y + " did not un-transform");
      return;
    }
    floorDWG = request.getParameter(ParamNames.FLOOR_DWG);
    deriv = elSmacko(RouteServlet.bgd.floorMap, floorDWG, pt);

    if (deriv == null) {
      VerbsJson.whinge(pw, "Cannot find space at " + x + " " + y);
      return;
    }

    sl = BaseSQLClass.getMany(Space.class, null, "Space.Name=" + SQLU.toSQLConst(deriv.StringRep) + " and Space.FloorID=(select FloorID from Floor f where f.BuildingID=" + bID + " and f.FloorDWG=" + SQLU.toSQLConst(floorDWG)+ ")");
    if ((sl == null) || (sl.size() <= 0)) {
      VerbsJson.whinge(pw, "Cannot find space named " + deriv.StringRep);
      return;
    }

    space = sl.get(0);
    if (what.equals("inaccess") || what.equals("access")) {
    space.setBlocked("inaccess".equals(what) ? "Y" : "n");
    if (!space.BlockedChanged) {
      VerbsJson.whinge(pw, "Space " + deriv.StringRep + " was already " + ("inaccess".equals(what) ? "inaccessible" : "accessible"));
      return;
    }
    accessChanged(space, bID);
    VerbsJson.rejoice(pw, "Space " + deriv.StringRep + " set to " + ("inaccess".equals(what) ? "inaccessible" : "accessible"));
    } else if (what.equals("go") || what.equals("st")) {
      VerbsJson.whinge(pw, "dst", space.getName());
    } else if (what.equals("info")) {
      AnyUser uo = AnyUser.getUserObject(request);
      DestInfo ds = VerbsJson.getDestInfoForBubble(String.valueOf(space.getSpaceID()), String.valueOf(StringsManager.SPACE_ILK), uo.currentBuilding.getNickName());
      JsonServlet.writeString(pw, new JSONObject(ds));
    }
    System.out.println(ip.howLongSince() + "to " + what + " of " +
		       deriv.StringRep);
  }

  /**
   * Spaces in the graph can be referred to by many different names.
   * A space has its system name and a display name.  Users can search
   * by company name or person name as well
   * @param idS destination name the user chose.
   * @param ilkS defines the destination type as defined in StringsManager.
   * @param hap event to be used to record the request
   * @param from whether the destination is the start or end of the
   * path.  If true, represents the start of the path.
   * @return destination description using the canonical system name
   */
  public static DestInfo getSystemName(String idS, String ilkS, Happening hap, boolean from) {
    List<String> nameS = null;
    DestInfo ds = new DestInfo();
    int ilk = Integer.valueOf(ilkS);
    String addenda = "";
    long id;

    switch (ilk) {
    case StringsManager.LOCATION_ILK:
      Location loc =
	Location.fetchByPrimaryKey(id = CommandArgs.longFromString(idS));
      if (loc == null) { return null; }
      if (from) {
        hap.setFromLocationID(id);
      } else {
        hap.setToLocationID(id);
      }
      ds.point = new Point2D.Double(loc.getX(), loc.getY());
      idS = String.valueOf(loc.getSpaceID());
    case StringsManager.SPACE_ILK:  // space
      nameS = SQLU.listQuery("select name from Space where SpaceID=" + idS);
      try {
        if (SGB.ANY_EMERG.equals(nameS.get(0))) { ds.modifier = -1; }
      } catch (Exception e) {}
      id = CommandArgs.longFromString(idS);
      if (from) {
        hap.setFromSpaceID(id);
      } else {
        hap.setToSpaceID(id);
      }
      break;
    case StringsManager.FLOOR_ILK:  // space
      nameS = SQLU.listQuery("select name from Floor where FloorID=" + idS);
      addenda = " as destination";
      ds.modifier = 1;
      ds.point = null;
      break;
    default:
      System.out.println("Unhandled ilk " + ilk);
      return null;
    }
    if (nameS == null) { return null; }
    ds.name = nameS.get(0) + addenda;

    return ds;
  }

  /**
   * Called whenever the user updates the accessibility of a space.
   * This can be done through the administration interface or map clicking.
   * It is up to the caller not to call this method unless accessibility
   * in fact has changed.
   * @param space the space object which has been changed.  The changes have
   * not been written to the database when this method is called.
   * @param bID building ID
   */
  public static void accessChanged(Space space, Long bID) {
    /* Remember whether the object is now accessible or blocked before
     * writing it. */
    // TODO fetch building graph data on basis of building ID
    boolean isNowBlocked = space.getBlocked().equals("Y");
    space.updateEntity();
    JsonServlet.logger.info("Space " + space.getName() + " blocked " + isNowBlocked);
    if (isNowBlocked) {
      StringLabeller sl = StringLabeller.getLabeller(RouteServlet.bgd.g);
      Derivation deriv;
      if ( (deriv = (Derivation)sl.getVertex(space.getName())) != null) {
        disconnect(RouteServlet.bgd.g, deriv);
      }
      /* The short path algorithm caches links; must clear out its cache in
       * case some of the routes it has previously computed use the
       * newly disconnected node. */
      if (RouteServlet.bgd.dsp != null) {
        RouteServlet.bgd.dsp = new DijkstraShortestPath(RouteServlet.bgd.g, EdgeWeight.getEdgeWeightInstance());
      }
    } else {
      /* TODO make sure no client asks for a route during graph load;
       * the best way to implement this is probably
       * java.util.concurrent.ReadWriteLock */
      StringsManager.discardStrings(bID);

      StringLabeller sl = StringLabeller.getLabeller(RouteServlet.bgd.g);
      sl.clear();
      RouteServlet.bgd.g.removeAllEdges();
      RouteServlet.bgd.g.removeAllVertices();
      RouteServlet.bgd.g = null;
      RouteServlet.bgd.g = loadBuildingGraph(graphFilePath);
    }
    /* Indicate the most recent accessibility change so that routes can be
     * recomputed.  It is a good idea to defer setting this flag until
     * the graph changes have settled down.*/
    accessibilityDelta = new java.util.Date();
  }
}
