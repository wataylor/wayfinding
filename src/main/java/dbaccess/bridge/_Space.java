package dbaccess.bridge;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;import java.util.ArrayList;
import zetek.dbcommon.BaseSQLClass;
import dbaccess.Space;

public class _Space extends BaseSQLClass implements Serializable {
  private static final long serialVersionUID=1;

  public _Space() {}

public static List<Space> fetchMany(String colName, Object colValue) {
return (List<Space>)BaseSQLClass.getMany(Space.class, null, colName, colValue);
}

public static Space fetchOne(String colName, Object colValue) {
return (Space)BaseSQLClass.getOne(Space.class, null, colName, colValue);
}

public boolean ImageChanged;
/** File path to the preferred space image. */ String Image;
public String getImage() { return Image; }
public void setImage(String Image) {
  ImageChanged=BaseSQLClass.twoParms(this.Image, Image);
  this.Image = Image;
}

public boolean BlockedChanged;
/** Set to Y when access to the space is blocked */ String Blocked="n";
public String getBlocked() { return Blocked; }
public void setBlocked(String Blocked) {
  BlockedChanged=BaseSQLClass.twoParms(this.Blocked, Blocked);
  this.Blocked = Blocked;
}

public boolean DisplayNameChanged;
/** This name is the display name for the entity. The rules vary with entity type.  A room display name must be unique within a building.  The display name defaults to the name when the entity is first created. */ String DisplayName;
public String getDisplayName() { return DisplayName; }
public void setDisplayName(String DisplayName) {
  DisplayNameChanged=BaseSQLClass.twoParms(this.DisplayName, DisplayName);
  this.DisplayName = DisplayName;
}

public boolean TagsChanged;
/** Semicolon-separated search categories. */ String Tags;
public String getTags() { return Tags; }
public void setTags(String Tags) {
  TagsChanged=BaseSQLClass.twoParms(this.Tags, Tags);
  this.Tags = Tags;
}

public boolean ShapeChanged;
/** The string representation of the shape, see ShapeSerializer.serializeShape */ String Shape;
public String getShape() { return Shape; }
public void setShape(String Shape) {
  ShapeChanged=BaseSQLClass.twoParms(this.Shape, Shape);
  this.Shape = Shape;
}

public boolean NameChanged;
/** This name is the official system name of the entity.  The rules for naming vary with entity type.  For example a room name must be unique within a building floor and a device name must be unique within a building.  This name does not ever change.  A space */ String Name;
public String getName() { return Name; }
public void setName(String Name) {
  NameChanged=BaseSQLClass.twoParms(this.Name, Name);
  this.Name = Name;
}

public boolean SpaceIDChanged;
/** Space ID database key */ Long SpaceID;
public Long getSpaceID() { return SpaceID; }
public void setSpaceID(Long SpaceID) {
  SpaceIDChanged=BaseSQLClass.twoParms(this.SpaceID, SpaceID);
  this.SpaceID = SpaceID;
}

public boolean DetailsChanged;
/** Description of the space and / or notes. */ String Details;
public String getDetails() { return Details; }
public void setDetails(String Details) {
  DetailsChanged=BaseSQLClass.twoParms(this.Details, Details);
  this.Details = Details;
}

public boolean SpaceTypeIDChanged;
/** Identifies the space type such as emergency device, room, hall, toilet, elevator, escalator, etc.  Most type information is derived from the drawing, but type data which is finer-grained than the metadata types can be entered via the spread sheet. */ Long SpaceTypeID;
public Long getSpaceTypeID() { return SpaceTypeID; }
public void setSpaceTypeID(Long SpaceTypeID) {
  SpaceTypeIDChanged=BaseSQLClass.twoParms(this.SpaceTypeID, SpaceTypeID);
  this.SpaceTypeID = SpaceTypeID;
}

public boolean FloorIDChanged;
/** Identifies the floor of the building on which the space appears.  Since all FloorIDs are unique across all buildings, the FloorID subsumes the building ID.  Thus, selecting all Spaces which have a specific FloorID gets only spaces from a particular buildi */ Long FloorID;
public Long getFloorID() { return FloorID; }
public void setFloorID(Long FloorID) {
  FloorIDChanged=BaseSQLClass.twoParms(this.FloorID, FloorID);
  this.FloorID = FloorID;
}

// 1 to 1 references from Space

protected dbaccess.Floor FloorFloorIDFloorIDObj;
public dbaccess.Floor fetchOneFloorFloorIDFloorID() { try {
  if (FloorFloorIDFloorIDObj != null) { return FloorFloorIDFloorIDObj;}
  return(FloorFloorIDFloorIDObj =
    dbaccess.bridge._Floor.fetchOne("FloorID", (long)FloorID));
  } catch (NullPointerException e) { return null; }}

protected dbaccess.SpaceType SpaceTypeSpaceTypeIDSpaceTypeIDObj;
public dbaccess.SpaceType fetchOneSpaceTypeSpaceTypeIDSpaceTypeID() { try {
  if (SpaceTypeSpaceTypeIDSpaceTypeIDObj != null) { return SpaceTypeSpaceTypeIDSpaceTypeIDObj;}
  return(SpaceTypeSpaceTypeIDSpaceTypeIDObj =
    dbaccess.bridge._SpaceType.fetchOne("SpaceTypeID", (long)SpaceTypeID));
  } catch (NullPointerException e) { return null; }}

protected dbaccess.Entity EntityEntityIDSpaceIDObj;
public dbaccess.Entity fetchOneEntityEntityIDSpaceID() { try {
  if (EntityEntityIDSpaceIDObj != null) { return EntityEntityIDSpaceIDObj;}
  return(EntityEntityIDSpaceIDObj =
    dbaccess.bridge._Entity.fetchOne("EntityID", (long)SpaceID));
  } catch (NullPointerException e) { return null; }}

// 1 to many references from Space

protected List<dbaccess.Device> DeviceSpaceIDSpaceIDLst;
public void addDeviceSpaceIDSpaceID(dbaccess.Device obj) {
  if (DeviceSpaceIDSpaceIDLst == null) { DeviceSpaceIDSpaceIDLst = new ArrayList<dbaccess.Device>();}
DeviceSpaceIDSpaceIDLst.add(obj); }
public List<dbaccess.Device> fetchAllDeviceSpaceIDSpaceID() { try {
  if (DeviceSpaceIDSpaceIDLst != null) { return DeviceSpaceIDSpaceIDLst;}
  return(DeviceSpaceIDSpaceIDLst =
    dbaccess.bridge._Device.fetchMany("SpaceID", (long)SpaceID));
  } catch (NullPointerException e) { return null; }}

protected List<dbaccess.Location> LocationSpaceIDSpaceIDLst;
public void addLocationSpaceIDSpaceID(dbaccess.Location obj) {
  if (LocationSpaceIDSpaceIDLst == null) { LocationSpaceIDSpaceIDLst = new ArrayList<dbaccess.Location>();}
LocationSpaceIDSpaceIDLst.add(obj); }
public List<dbaccess.Location> fetchAllLocationSpaceIDSpaceID() { try {
  if (LocationSpaceIDSpaceIDLst != null) { return LocationSpaceIDSpaceIDLst;}
  return(LocationSpaceIDSpaceIDLst =
    dbaccess.bridge._Location.fetchMany("SpaceID", (long)SpaceID));
  } catch (NullPointerException e) { return null; }}

protected List<dbaccess.Organization> OrganizationSpaceIDSpaceIDLst;
public void addOrganizationSpaceIDSpaceID(dbaccess.Organization obj) {
  if (OrganizationSpaceIDSpaceIDLst == null) { OrganizationSpaceIDSpaceIDLst = new ArrayList<dbaccess.Organization>();}
OrganizationSpaceIDSpaceIDLst.add(obj); }
public List<dbaccess.Organization> fetchAllOrganizationSpaceIDSpaceID() { try {
  if (OrganizationSpaceIDSpaceIDLst != null) { return OrganizationSpaceIDSpaceIDLst;}
  return(OrganizationSpaceIDSpaceIDLst =
    dbaccess.bridge._Organization.fetchMany("SpaceID", (long)SpaceID));
  } catch (NullPointerException e) { return null; }}

protected List<dbaccess.Person> PersonSpaceIDSpaceIDLst;
public void addPersonSpaceIDSpaceID(dbaccess.Person obj) {
  if (PersonSpaceIDSpaceIDLst == null) { PersonSpaceIDSpaceIDLst = new ArrayList<dbaccess.Person>();}
PersonSpaceIDSpaceIDLst.add(obj); }
public List<dbaccess.Person> fetchAllPersonSpaceIDSpaceID() { try {
  if (PersonSpaceIDSpaceIDLst != null) { return PersonSpaceIDSpaceIDLst;}
  return(PersonSpaceIDSpaceIDLst =
    dbaccess.bridge._Person.fetchMany("SpaceID", (long)SpaceID));
  } catch (NullPointerException e) { return null; }}

public void cleanDirtyFlags() {
  ImageChanged=false;
  BlockedChanged=false;
  DisplayNameChanged=false;
  TagsChanged=false;
  ShapeChanged=false;
  NameChanged=false;
  SpaceIDChanged=false;
  DetailsChanged=false;
  SpaceTypeIDChanged=false;
  FloorIDChanged=false;
}

public boolean testDirtyFlags() { return (
  ImageChanged ||
  BlockedChanged ||
  DisplayNameChanged ||
  TagsChanged ||
  ShapeChanged ||
  NameChanged ||
  SpaceIDChanged ||
  DetailsChanged ||
  SpaceTypeIDChanged ||
  FloorIDChanged);
}

public static final String primaryKey = "SpaceID";
public static String fetchPrimaryKey() { return primaryKey; }
public static Space fetchByPrimaryKey(Long colValue) {
  return (Space)BaseSQLClass.getOne(Space.class, null, primaryKey, colValue);
}
public static boolean hasPrimaryKey() { return true; }
}
