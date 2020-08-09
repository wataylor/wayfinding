package dbaccess.bridge;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;import java.util.ArrayList;
import zetek.dbcommon.BaseSQLClass;
import dbaccess.Entity;

public class _Entity extends BaseSQLClass implements Serializable {
  private static final long serialVersionUID=1;

  public _Entity() {}

public static List<Entity> fetchMany(String colName, Object colValue) {
return (List<Entity>)BaseSQLClass.getMany(Entity.class, null, colName, colValue);
}

public static Entity fetchOne(String colName, Object colValue) {
return (Entity)BaseSQLClass.getOne(Entity.class, null, colName, colValue);
}

public boolean EntityIDChanged;
/** Uniquely identify an entity with respect to all buildings and all mirrored databases.  This key is published abroad and can be used in any database. */ Long EntityID;
public Long getEntityID() { return EntityID; }
public void setEntityID(Long EntityID) {
  EntityIDChanged=BaseSQLClass.twoParms(this.EntityID, EntityID);
  this.EntityID = EntityID;
}

public boolean EntityTypeIDChanged;
/** Tells the entity type such as door, room, person, etc.  It is an error to define an entity without stating its type */ Long EntityTypeID;
public Long getEntityTypeID() { return EntityTypeID; }
public void setEntityTypeID(Long EntityTypeID) {
  EntityTypeIDChanged=BaseSQLClass.twoParms(this.EntityTypeID, EntityTypeID);
  this.EntityTypeID = EntityTypeID;
}

public boolean SourceIDChanged;
/** The ID of the data source which created this entry */ Long SourceID;
public Long getSourceID() { return SourceID; }
public void setSourceID(Long SourceID) {
  SourceIDChanged=BaseSQLClass.twoParms(this.SourceID, SourceID);
  this.SourceID = SourceID;
}

// 1 to 1 references from Entity

protected dbaccess.EntityType EntityTypeEntityTypeIDEntityTypeIDObj;
public dbaccess.EntityType fetchOneEntityTypeEntityTypeIDEntityTypeID() { try {
  if (EntityTypeEntityTypeIDEntityTypeIDObj != null) { return EntityTypeEntityTypeIDEntityTypeIDObj;}
  return(EntityTypeEntityTypeIDEntityTypeIDObj =
    dbaccess.bridge._EntityType.fetchOne("EntityTypeID", (long)EntityTypeID));
  } catch (NullPointerException e) { return null; }}

// 1 to many references from Entity

protected List<dbaccess.Address> AddressAddressIDEntityIDLst;
public void addAddressAddressIDEntityID(dbaccess.Address obj) {
  if (AddressAddressIDEntityIDLst == null) { AddressAddressIDEntityIDLst = new ArrayList<dbaccess.Address>();}
AddressAddressIDEntityIDLst.add(obj); }
public List<dbaccess.Address> fetchAllAddressAddressIDEntityID() { try {
  if (AddressAddressIDEntityIDLst != null) { return AddressAddressIDEntityIDLst;}
  return(AddressAddressIDEntityIDLst =
    dbaccess.bridge._Address.fetchMany("AddressID", (long)EntityID));
  } catch (NullPointerException e) { return null; }}

protected List<dbaccess.Attribute> AttributeEntityIDEntityIDLst;
public void addAttributeEntityIDEntityID(dbaccess.Attribute obj) {
  if (AttributeEntityIDEntityIDLst == null) { AttributeEntityIDEntityIDLst = new ArrayList<dbaccess.Attribute>();}
AttributeEntityIDEntityIDLst.add(obj); }
public List<dbaccess.Attribute> fetchAllAttributeEntityIDEntityID() { try {
  if (AttributeEntityIDEntityIDLst != null) { return AttributeEntityIDEntityIDLst;}
  return(AttributeEntityIDEntityIDLst =
    dbaccess.bridge._Attribute.fetchMany("EntityID", (long)EntityID));
  } catch (NullPointerException e) { return null; }}

protected List<dbaccess.Building> BuildingBuildingIDEntityIDLst;
public void addBuildingBuildingIDEntityID(dbaccess.Building obj) {
  if (BuildingBuildingIDEntityIDLst == null) { BuildingBuildingIDEntityIDLst = new ArrayList<dbaccess.Building>();}
BuildingBuildingIDEntityIDLst.add(obj); }
public List<dbaccess.Building> fetchAllBuildingBuildingIDEntityID() { try {
  if (BuildingBuildingIDEntityIDLst != null) { return BuildingBuildingIDEntityIDLst;}
  return(BuildingBuildingIDEntityIDLst =
    dbaccess.bridge._Building.fetchMany("BuildingID", (long)EntityID));
  } catch (NullPointerException e) { return null; }}

protected List<dbaccess.BuildingGroup> BuildingGroupGroupIDEntityIDLst;
public void addBuildingGroupGroupIDEntityID(dbaccess.BuildingGroup obj) {
  if (BuildingGroupGroupIDEntityIDLst == null) { BuildingGroupGroupIDEntityIDLst = new ArrayList<dbaccess.BuildingGroup>();}
BuildingGroupGroupIDEntityIDLst.add(obj); }
public List<dbaccess.BuildingGroup> fetchAllBuildingGroupGroupIDEntityID() { try {
  if (BuildingGroupGroupIDEntityIDLst != null) { return BuildingGroupGroupIDEntityIDLst;}
  return(BuildingGroupGroupIDEntityIDLst =
    dbaccess.bridge._BuildingGroup.fetchMany("GroupID", (long)EntityID));
  } catch (NullPointerException e) { return null; }}

protected List<dbaccess.Device> DeviceDeviceIDEntityIDLst;
public void addDeviceDeviceIDEntityID(dbaccess.Device obj) {
  if (DeviceDeviceIDEntityIDLst == null) { DeviceDeviceIDEntityIDLst = new ArrayList<dbaccess.Device>();}
DeviceDeviceIDEntityIDLst.add(obj); }
public List<dbaccess.Device> fetchAllDeviceDeviceIDEntityID() { try {
  if (DeviceDeviceIDEntityIDLst != null) { return DeviceDeviceIDEntityIDLst;}
  return(DeviceDeviceIDEntityIDLst =
    dbaccess.bridge._Device.fetchMany("DeviceID", (long)EntityID));
  } catch (NullPointerException e) { return null; }}

protected List<dbaccess.EntityAddress> EntityAddressEntityIDEntityIDLst;
public void addEntityAddressEntityIDEntityID(dbaccess.EntityAddress obj) {
  if (EntityAddressEntityIDEntityIDLst == null) { EntityAddressEntityIDEntityIDLst = new ArrayList<dbaccess.EntityAddress>();}
EntityAddressEntityIDEntityIDLst.add(obj); }
public List<dbaccess.EntityAddress> fetchAllEntityAddressEntityIDEntityID() { try {
  if (EntityAddressEntityIDEntityIDLst != null) { return EntityAddressEntityIDEntityIDLst;}
  return(EntityAddressEntityIDEntityIDLst =
    dbaccess.bridge._EntityAddress.fetchMany("EntityID", (long)EntityID));
  } catch (NullPointerException e) { return null; }}

protected List<dbaccess.EntityPhoneNo> EntityPhoneNoEntityIDEntityIDLst;
public void addEntityPhoneNoEntityIDEntityID(dbaccess.EntityPhoneNo obj) {
  if (EntityPhoneNoEntityIDEntityIDLst == null) { EntityPhoneNoEntityIDEntityIDLst = new ArrayList<dbaccess.EntityPhoneNo>();}
EntityPhoneNoEntityIDEntityIDLst.add(obj); }
public List<dbaccess.EntityPhoneNo> fetchAllEntityPhoneNoEntityIDEntityID() { try {
  if (EntityPhoneNoEntityIDEntityIDLst != null) { return EntityPhoneNoEntityIDEntityIDLst;}
  return(EntityPhoneNoEntityIDEntityIDLst =
    dbaccess.bridge._EntityPhoneNo.fetchMany("EntityID", (long)EntityID));
  } catch (NullPointerException e) { return null; }}

protected List<dbaccess.EntityRelation> EntityRelationOfEntityIDEntityIDLst;
public void addEntityRelationOfEntityIDEntityID(dbaccess.EntityRelation obj) {
  if (EntityRelationOfEntityIDEntityIDLst == null) { EntityRelationOfEntityIDEntityIDLst = new ArrayList<dbaccess.EntityRelation>();}
EntityRelationOfEntityIDEntityIDLst.add(obj); }
public List<dbaccess.EntityRelation> fetchAllEntityRelationOfEntityIDEntityID() { try {
  if (EntityRelationOfEntityIDEntityIDLst != null) { return EntityRelationOfEntityIDEntityIDLst;}
  return(EntityRelationOfEntityIDEntityIDLst =
    dbaccess.bridge._EntityRelation.fetchMany("OfEntityID", (long)EntityID));
  } catch (NullPointerException e) { return null; }}

protected List<dbaccess.EntityRelation> EntityRelationEntityIDEntityIDLst;
public void addEntityRelationEntityIDEntityID(dbaccess.EntityRelation obj) {
  if (EntityRelationEntityIDEntityIDLst == null) { EntityRelationEntityIDEntityIDLst = new ArrayList<dbaccess.EntityRelation>();}
EntityRelationEntityIDEntityIDLst.add(obj); }
public List<dbaccess.EntityRelation> fetchAllEntityRelationEntityIDEntityID() { try {
  if (EntityRelationEntityIDEntityIDLst != null) { return EntityRelationEntityIDEntityIDLst;}
  return(EntityRelationEntityIDEntityIDLst =
    dbaccess.bridge._EntityRelation.fetchMany("EntityID", (long)EntityID));
  } catch (NullPointerException e) { return null; }}

protected List<dbaccess.Floor> FloorFloorIDEntityIDLst;
public void addFloorFloorIDEntityID(dbaccess.Floor obj) {
  if (FloorFloorIDEntityIDLst == null) { FloorFloorIDEntityIDLst = new ArrayList<dbaccess.Floor>();}
FloorFloorIDEntityIDLst.add(obj); }
public List<dbaccess.Floor> fetchAllFloorFloorIDEntityID() { try {
  if (FloorFloorIDEntityIDLst != null) { return FloorFloorIDEntityIDLst;}
  return(FloorFloorIDEntityIDLst =
    dbaccess.bridge._Floor.fetchMany("FloorID", (long)EntityID));
  } catch (NullPointerException e) { return null; }}

protected List<dbaccess.Location> LocationLocationIDEntityIDLst;
public void addLocationLocationIDEntityID(dbaccess.Location obj) {
  if (LocationLocationIDEntityIDLst == null) { LocationLocationIDEntityIDLst = new ArrayList<dbaccess.Location>();}
LocationLocationIDEntityIDLst.add(obj); }
public List<dbaccess.Location> fetchAllLocationLocationIDEntityID() { try {
  if (LocationLocationIDEntityIDLst != null) { return LocationLocationIDEntityIDLst;}
  return(LocationLocationIDEntityIDLst =
    dbaccess.bridge._Location.fetchMany("LocationID", (long)EntityID));
  } catch (NullPointerException e) { return null; }}

protected List<dbaccess.Permission> PermissionEntityIDEntityIDLst;
public void addPermissionEntityIDEntityID(dbaccess.Permission obj) {
  if (PermissionEntityIDEntityIDLst == null) { PermissionEntityIDEntityIDLst = new ArrayList<dbaccess.Permission>();}
PermissionEntityIDEntityIDLst.add(obj); }
public List<dbaccess.Permission> fetchAllPermissionEntityIDEntityID() { try {
  if (PermissionEntityIDEntityIDLst != null) { return PermissionEntityIDEntityIDLst;}
  return(PermissionEntityIDEntityIDLst =
    dbaccess.bridge._Permission.fetchMany("EntityID", (long)EntityID));
  } catch (NullPointerException e) { return null; }}

protected List<dbaccess.Person> PersonPersonIDEntityIDLst;
public void addPersonPersonIDEntityID(dbaccess.Person obj) {
  if (PersonPersonIDEntityIDLst == null) { PersonPersonIDEntityIDLst = new ArrayList<dbaccess.Person>();}
PersonPersonIDEntityIDLst.add(obj); }
public List<dbaccess.Person> fetchAllPersonPersonIDEntityID() { try {
  if (PersonPersonIDEntityIDLst != null) { return PersonPersonIDEntityIDLst;}
  return(PersonPersonIDEntityIDLst =
    dbaccess.bridge._Person.fetchMany("PersonID", (long)EntityID));
  } catch (NullPointerException e) { return null; }}

protected List<dbaccess.Space> SpaceSpaceIDEntityIDLst;
public void addSpaceSpaceIDEntityID(dbaccess.Space obj) {
  if (SpaceSpaceIDEntityIDLst == null) { SpaceSpaceIDEntityIDLst = new ArrayList<dbaccess.Space>();}
SpaceSpaceIDEntityIDLst.add(obj); }
public List<dbaccess.Space> fetchAllSpaceSpaceIDEntityID() { try {
  if (SpaceSpaceIDEntityIDLst != null) { return SpaceSpaceIDEntityIDLst;}
  return(SpaceSpaceIDEntityIDLst =
    dbaccess.bridge._Space.fetchMany("SpaceID", (long)EntityID));
  } catch (NullPointerException e) { return null; }}

public void cleanDirtyFlags() {
  EntityIDChanged=false;
  EntityTypeIDChanged=false;
  SourceIDChanged=false;
}

public boolean testDirtyFlags() { return (
  EntityIDChanged ||
  EntityTypeIDChanged ||
  SourceIDChanged);
}

public static final String primaryKey = "EntityID";
public static String fetchPrimaryKey() { return primaryKey; }
public static Entity fetchByPrimaryKey(Long colValue) {
  return (Entity)BaseSQLClass.getOne(Entity.class, null, primaryKey, colValue);
}
public static boolean hasPrimaryKey() { return true; }
}
