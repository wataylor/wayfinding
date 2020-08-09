/* @name Route.java

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

package zetek.graphserve;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * holds all the sub-routes in a route and the building nick name to help
 * find the appropriate tile and map directories.

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class Route implements Serializable {

  public static final long serialVersionUID = 1;

  public String buildingNickname;
  public List<SubRoute> subRoutes;

  /** Obligatory constructor.*/
  public Route() { /* */ }

  public Route(String nickName) {
    this.buildingNickname = nickName;
  }

  public void addSubRoute(SubRoute sr) {
    if (subRoutes == null) {
      subRoutes = new ArrayList<SubRoute>();
    }
    subRoutes.add(sr);
  }
}
