package dbaccess.bridge;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;import java.util.ArrayList;
import zetek.dbcommon.BaseSQLClass;
import dbaccess.BuildingGroup;

public class _BuildingGroup extends BaseSQLClass implements Serializable {
  private static final long serialVersionUID=1;

  public _BuildingGroup() {}

public static List<BuildingGroup> fetchMany(String colName, Object colValue) {
return (List<BuildingGroup>)BaseSQLClass.getMany(BuildingGroup.class, null, colName, colValue);
}

public static BuildingGroup fetchOne(String colName, Object colValue) {
return (BuildingGroup)BaseSQLClass.getOne(BuildingGroup.class, null, colName, colValue);
}

public boolean NameChanged;
/** This name is the official system name of the entity.  The rules for naming vary with entity type.  For example a room name must be unique within a building floor and a device name must be unique within a building.  This name does not ever change.  A locat */ String Name;
public String getName() { return Name; }
public void setName(String Name) {
  NameChanged=BaseSQLClass.twoParms(this.Name, Name);
  this.Name = Name;
}

public boolean GroupTypeIDChanged;
/** Links to a group type, it is up to the software to handle group types appropriately.  It is an error to define a group whose type is not known. */ Long GroupTypeID;
public Long getGroupTypeID() { return GroupTypeID; }
public void setGroupTypeID(Long GroupTypeID) {
  GroupTypeIDChanged=BaseSQLClass.twoParms(this.GroupTypeID, GroupTypeID);
  this.GroupTypeID = GroupTypeID;
}

public boolean DisplayNameChanged;
/** This name is the display name for the entity. The rules vary with entity type.  A room display name must be unique within a building.  The display name defaults to the name when the entity is first created. */ String DisplayName;
public String getDisplayName() { return DisplayName; }
public void setDisplayName(String DisplayName) {
  DisplayNameChanged=BaseSQLClass.twoParms(this.DisplayName, DisplayName);
  this.DisplayName = DisplayName;
}

public boolean GroupIDChanged;
/** Uniquely identify the group within the entity table. */ Long GroupID;
public Long getGroupID() { return GroupID; }
public void setGroupID(Long GroupID) {
  GroupIDChanged=BaseSQLClass.twoParms(this.GroupID, GroupID);
  this.GroupID = GroupID;
}

// 1 to 1 references from BuildingGroup

protected dbaccess.GroupType GroupTypeGroupTypeIDGroupTypeIDObj;
public dbaccess.GroupType fetchOneGroupTypeGroupTypeIDGroupTypeID() { try {
  if (GroupTypeGroupTypeIDGroupTypeIDObj != null) { return GroupTypeGroupTypeIDGroupTypeIDObj;}
  return(GroupTypeGroupTypeIDGroupTypeIDObj =
    dbaccess.bridge._GroupType.fetchOne("GroupTypeID", (long)GroupTypeID));
  } catch (NullPointerException e) { return null; }}

protected dbaccess.Entity EntityEntityIDGroupIDObj;
public dbaccess.Entity fetchOneEntityEntityIDGroupID() { try {
  if (EntityEntityIDGroupIDObj != null) { return EntityEntityIDGroupIDObj;}
  return(EntityEntityIDGroupIDObj =
    dbaccess.bridge._Entity.fetchOne("EntityID", (long)GroupID));
  } catch (NullPointerException e) { return null; }}

// 1 to many references from BuildingGroup

protected List<dbaccess.Building> BuildingBuildingGroupIDGroupIDLst;
public void addBuildingBuildingGroupIDGroupID(dbaccess.Building obj) {
  if (BuildingBuildingGroupIDGroupIDLst == null) { BuildingBuildingGroupIDGroupIDLst = new ArrayList<dbaccess.Building>();}
BuildingBuildingGroupIDGroupIDLst.add(obj); }
public List<dbaccess.Building> fetchAllBuildingBuildingGroupIDGroupID() { try {
  if (BuildingBuildingGroupIDGroupIDLst != null) { return BuildingBuildingGroupIDGroupIDLst;}
  return(BuildingBuildingGroupIDGroupIDLst =
    dbaccess.bridge._Building.fetchMany("BuildingGroupID", (long)GroupID));
  } catch (NullPointerException e) { return null; }}

public void cleanDirtyFlags() {
  NameChanged=false;
  GroupTypeIDChanged=false;
  DisplayNameChanged=false;
  GroupIDChanged=false;
}

public boolean testDirtyFlags() { return (
  NameChanged ||
  GroupTypeIDChanged ||
  DisplayNameChanged ||
  GroupIDChanged);
}

public static final String primaryKey = "GroupID";
public static String fetchPrimaryKey() { return primaryKey; }
public static BuildingGroup fetchByPrimaryKey(Long colValue) {
  return (BuildingGroup)BaseSQLClass.getOne(BuildingGroup.class, null, primaryKey, colValue);
}
public static boolean hasPrimaryKey() { return true; }
}
