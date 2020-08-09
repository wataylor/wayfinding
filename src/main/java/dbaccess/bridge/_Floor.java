package dbaccess.bridge;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;import java.util.ArrayList;
import zetek.dbcommon.BaseSQLClass;
import dbaccess.Floor;

public class _Floor extends BaseSQLClass implements Serializable {
  private static final long serialVersionUID=1;

  public _Floor() {}

public static List<Floor> fetchMany(String colName, Object colValue) {
return (List<Floor>)BaseSQLClass.getMany(Floor.class, null, colName, colValue);
}

public static Floor fetchOne(String colName, Object colValue) {
return (Floor)BaseSQLClass.getOne(Floor.class, null, colName, colValue);
}

public boolean DisplayNameChanged;
/** This name is the display name for the entity. The rules vary with entity type.  A room display name must be unique within a building.  The display name defaults to the name when the entity is first created. */ String DisplayName;
public String getDisplayName() { return DisplayName; }
public void setDisplayName(String DisplayName) {
  DisplayNameChanged=BaseSQLClass.twoParms(this.DisplayName, DisplayName);
  this.DisplayName = DisplayName;
}

public boolean BuildingIDChanged;
/** Links the floor with the building, many floors may have the same building ID */ Long BuildingID;
public Long getBuildingID() { return BuildingID; }
public void setBuildingID(Long BuildingID) {
  BuildingIDChanged=BaseSQLClass.twoParms(this.BuildingID, BuildingID);
  this.BuildingID = BuildingID;
}

public boolean TagsChanged;
/** Semicolon-separated search categories. */ String Tags;
public String getTags() { return Tags; }
public void setTags(String Tags) {
  TagsChanged=BaseSQLClass.twoParms(this.Tags, Tags);
  this.Tags = Tags;
}

public boolean SortOrderChanged;
/** Sorts the floors for menus.  Lower floors have smaller numbers.  Numbers may be negative and do not have to be contiguous.  The order is the only thing that matters.  Floors are displayed in this order in pull down menus.  If this field is omitted, floors */ Long SortOrder;
public Long getSortOrder() { return SortOrder; }
public void setSortOrder(Long SortOrder) {
  SortOrderChanged=BaseSQLClass.twoParms(this.SortOrder, SortOrder);
  this.SortOrder = SortOrder;
}

public boolean ConstructionOrderChanged;
/** Sorts the floors in ascending order from the lowest floor of the building to the highest.  Lower floors have smaller numbers.  Numbers may be negative and do not have to be contiguous.  The order is the only thing that matters. */ Long ConstructionOrder;
public Long getConstructionOrder() { return ConstructionOrder; }
public void setConstructionOrder(Long ConstructionOrder) {
  ConstructionOrderChanged=BaseSQLClass.twoParms(this.ConstructionOrder, ConstructionOrder);
  this.ConstructionOrder = ConstructionOrder;
}

public boolean NameChanged;
/** This name is the official system name of the entity.  The rules for naming vary with entity type.  For example a room name must be unique within a building floor and a device name must be unique within a building.  This name does not ever change.  A locat */ String Name;
public String getName() { return Name; }
public void setName(String Name) {
  NameChanged=BaseSQLClass.twoParms(this.Name, Name);
  this.Name = Name;
}

public boolean DetailsChanged;
/** Description of the floor and / or notes. */ String Details;
public String getDetails() { return Details; }
public void setDetails(String Details) {
  DetailsChanged=BaseSQLClass.twoParms(this.Details, Details);
  this.Details = Details;
}

public boolean FloorIDChanged;
/** Floor ID database key.  A floor is an entity. */ Long FloorID;
public Long getFloorID() { return FloorID; }
public void setFloorID(Long FloorID) {
  FloorIDChanged=BaseSQLClass.twoParms(this.FloorID, FloorID);
  this.FloorID = FloorID;
}

public boolean FloorDWGChanged;
/** The drawing name is unique within a building.  It links various constructs in the floor to additional information such as the street address of an exterior door. */ String FloorDWG;
public String getFloorDWG() { return FloorDWG; }
public void setFloorDWG(String FloorDWG) {
  FloorDWGChanged=BaseSQLClass.twoParms(this.FloorDWG, FloorDWG);
  this.FloorDWG = FloorDWG;
}

// 1 to 1 references from Floor

protected dbaccess.Building BuildingBuildingIDBuildingIDObj;
public dbaccess.Building fetchOneBuildingBuildingIDBuildingID() { try {
  if (BuildingBuildingIDBuildingIDObj != null) { return BuildingBuildingIDBuildingIDObj;}
  return(BuildingBuildingIDBuildingIDObj =
    dbaccess.bridge._Building.fetchOne("BuildingID", (long)BuildingID));
  } catch (NullPointerException e) { return null; }}

protected dbaccess.Entity EntityEntityIDFloorIDObj;
public dbaccess.Entity fetchOneEntityEntityIDFloorID() { try {
  if (EntityEntityIDFloorIDObj != null) { return EntityEntityIDFloorIDObj;}
  return(EntityEntityIDFloorIDObj =
    dbaccess.bridge._Entity.fetchOne("EntityID", (long)FloorID));
  } catch (NullPointerException e) { return null; }}

// 1 to many references from Floor

protected List<dbaccess.Space> SpaceFloorIDFloorIDLst;
public void addSpaceFloorIDFloorID(dbaccess.Space obj) {
  if (SpaceFloorIDFloorIDLst == null) { SpaceFloorIDFloorIDLst = new ArrayList<dbaccess.Space>();}
SpaceFloorIDFloorIDLst.add(obj); }
public List<dbaccess.Space> fetchAllSpaceFloorIDFloorID() { try {
  if (SpaceFloorIDFloorIDLst != null) { return SpaceFloorIDFloorIDLst;}
  return(SpaceFloorIDFloorIDLst =
    dbaccess.bridge._Space.fetchMany("FloorID", (long)FloorID));
  } catch (NullPointerException e) { return null; }}

public void cleanDirtyFlags() {
  DisplayNameChanged=false;
  BuildingIDChanged=false;
  TagsChanged=false;
  SortOrderChanged=false;
  ConstructionOrderChanged=false;
  NameChanged=false;
  DetailsChanged=false;
  FloorIDChanged=false;
  FloorDWGChanged=false;
}

public boolean testDirtyFlags() { return (
  DisplayNameChanged ||
  BuildingIDChanged ||
  TagsChanged ||
  SortOrderChanged ||
  ConstructionOrderChanged ||
  NameChanged ||
  DetailsChanged ||
  FloorIDChanged ||
  FloorDWGChanged);
}

public static final String primaryKey = "FloorID";
public static String fetchPrimaryKey() { return primaryKey; }
public static Floor fetchByPrimaryKey(Long colValue) {
  return (Floor)BaseSQLClass.getOne(Floor.class, null, primaryKey, colValue);
}
public static boolean hasPrimaryKey() { return true; }
}
