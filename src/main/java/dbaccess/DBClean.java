/* @name DBClean.java

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

package dbaccess;

import java.util.List;

import dbaccess.bridge._TC;

import zetek.dbcommon.BaseSQLClass;
import zetek.dbcommon.SQLU;

/**
 * Various methods for deleting objects from the database.  There are
 * non-trivial concerns with respect to respecting foreign key
 * constraints during deletion.</p>

 * <P>Unfortunately, this class must to be changed as foreign keys are
 * added or subtracted.  The solution is to add a delete method to
 * each of the generated classes but that is for the future.

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class DBClean {

  public static final long serialVersionUID = 1;

  /** Obligatory constructor.*/
  public DBClean() { /* */ }

  /**
   * Delete a building identified by name and zip code.  The database
   * does not permit two buildings with the same name in the same zip
   * code.
   * @param name building name
   * @param zip building zip code.
   * @return true if the deletion is successful
   */
  public static boolean deleteBuildingByNameZip(String name, String zip) {
    @SuppressWarnings("unchecked")
      List<Building> buildings =
      BaseSQLClass.getMany(Building.class, null, "Name=" +
			   SQLU.toSQLConst(name) +
			   " and Zip=" + SQLU.toSQLConst(zip));
    if (buildings.size() <= 0) { return true; } // The building is not there
    return deleteBuilding(buildings.get(0));
  }

  public static boolean deleteBuilding(Building building) {
    boolean floorsOK = true;
    List<Floor> floors = building.fetchAllFloorBuildingIDBuildingID();
    for (Floor f : floors) {
      if (!deleteFloor(f)) { floorsOK = false; }
    }
    List<Person> persons = building.fetchAllPersonBuildingIDBuildingID();
    for (Person p : persons) {
      if (!deletePerson(p)) { floorsOK = false; }
    }
    List<Organization> orgs=building.fetchAllOrganizationBuildingIDBuildingID();
    for (Organization o : orgs) {
      if (!deleteOrganization(o)) { floorsOK = false; }
    }
    SQLU.anyStatement("delete from EntityRelation where OfEntityID=" +
		      building.getBuildingID() +
		      " or EntityID=" + building.getBuildingID());
    SQLU.anyStatement("delete from Building where BuildingID=" +
		      building.getBuildingID());
    return floorsOK;
  }

  public static boolean deleteFloor(Floor floor) {
    boolean floorOK = true;
    List<Space> spaces = floor.fetchAllSpaceFloorIDFloorID();
    for (Space sp : spaces) {
      if (!deleteSpace(sp)) { floorOK = false; }
    }
    SQLU.anyStatement("delete from EntityRelation where OfEntityID=" +
		      floor.getFloorID() +
		      " or EntityID=" + floor.getFloorID());
    SQLU.anyStatement("delete from Floor where FloorID=" + floor.getFloorID());
    return floorOK;
  }

  public static boolean deleteSpace(Space sp) {
    boolean spaceOK = true;
    List<Location> locations = sp.fetchAllLocationSpaceIDSpaceID();
    for (Location l : locations) {
      if (!deleteLocation(l)) { spaceOK = false; }
    }
    List<Device> devices = sp.fetchAllDeviceSpaceIDSpaceID();
    for (Device d : devices) {
      if (!deleteDevice(d)) { spaceOK = false; }
    }
    SQLU.anyStatement("delete from EntityRelation where OfEntityID=" +
		      sp.getSpaceID() +
		      " or EntityID=" + sp.getSpaceID());
    SQLU.anyStatement("update Person set SpaceID=null where SpaceID=" + sp.getSpaceID());
    SQLU.anyStatement("update Organization set SpaceID=null where SpaceID=" + sp.getSpaceID());
    SQLU.anyStatement("delete from Space where SpaceID=" + sp.getSpaceID());
    return spaceOK;
  }

  public static boolean deleteLocation(Location loc) {
    boolean locOK = true;
    List<Device> devices = loc.fetchAllDeviceLocationIDLocationID();
    for (Device d : devices) {
      if (!deleteDevice(d)) { locOK = false; }
    }
    SQLU.anyStatement("delete from EntityRelation where OfEntityID=" +
		      loc.getLocationID() +
		      " or EntityID=" + loc.getLocationID());
    SQLU.anyStatement("update Person set LocationID=null where LocationID=" + loc.getLocationID());
    SQLU.anyStatement("update Organization set LocationID=null where LocationID=" + loc.getLocationID());
    SQLU.anyStatement("delete from Location where LocationID=" +
		      loc.getLocationID());
    return locOK;
  }

  public static boolean deleteDevice(Device dev) {
    SQLU.anyStatement("delete from EntityRelation where OfEntityID=" +
		      dev.getDeviceID() +
		      " or EntityID=" + dev.getDeviceID());
    SQLU.anyStatement("delete from Device where DeviceID=" + dev.getDeviceID());
    return true;
  }

  public static boolean deletePerson(Person per) {
    SQLU.anyStatement("delete from EntityRelation where OfEntityID=" +
		      per.getPersonID() +
		      " or EntityID=" + per.getPersonID());
    SQLU.anyStatement("delete from Person where PersonID=" + per.getPersonID());
    return true;
  }

  /* Persons can be assigned to organizations; unaffiliate them */
  public static boolean deleteOrganization(Organization org) {
    SQLU.anyStatement("update Person set OrganizationID=null where OrganizationID="+
		      org.getOrganizationID());
    SQLU.anyStatement("delete from EntityRelation where OfEntityID=" +
		      org.getOrganizationID() +
		      " or EntityID=" + org.getOrganizationID());
    SQLU.anyStatement("delete from Organization where OrganizationID=" +
		      org.getOrganizationID());
    return true;
  }

  /**
   * Establish a relationship between an entity and another entity or
   * some other object which is known to the server.
   * @param ofEntityID identifies the entity which owns the relationship.
   * @param entityID identifies the entity which is related to the
   * owning entity or is null for a non-entity relationship.
   * @param relationship describes the relationship if entityID is not
   * null, otherwise its use depends on the context.
   * @param name Identifies an external item which is accessible to
   * the server.
   * @return ID of the entity relationship row.
   */
  public static Long establishRelationship(Long ofEntityID, Long entityID,
					   String relationship, String name) {
    EntityRelation er = new EntityRelation();
    er.setOfEntityID(ofEntityID);
    er.setEntityID(entityID);
    er.setName(name);
    er.setRelationship(relationship);
    return er.writeNewEntity(_TC.ET_Entity_Relation);
  }
}
