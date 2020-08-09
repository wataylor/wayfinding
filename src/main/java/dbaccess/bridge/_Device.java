package dbaccess.bridge;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;import java.util.ArrayList;
import zetek.dbcommon.BaseSQLClass;
import dbaccess.Device;

public class _Device extends BaseSQLClass implements Serializable {
  private static final long serialVersionUID=1;

  public _Device() {}

public static List<Device> fetchMany(String colName, Object colValue) {
return (List<Device>)BaseSQLClass.getMany(Device.class, null, colName, colValue);
}

public static Device fetchOne(String colName, Object colValue) {
return (Device)BaseSQLClass.getOne(Device.class, null, colName, colValue);
}

public boolean TypeChanged;
/** This may become a link to a device type table in the future */ String Type;
public String getType() { return Type; }
public void setType(String Type) {
  TypeChanged=BaseSQLClass.twoParms(this.Type, Type);
  this.Type = Type;
}

public boolean LocationIDChanged;
/**  */ Long LocationID;
public Long getLocationID() { return LocationID; }
public void setLocationID(Long LocationID) {
  LocationIDChanged=BaseSQLClass.twoParms(this.LocationID, LocationID);
  this.LocationID = LocationID;
}

public boolean DisplayNameChanged;
/** This name is the display name for the entity. The rules vary with entity type.  A room display name must be unique within a building.  The display name defaults to the name when the entity is first created. */ String DisplayName;
public String getDisplayName() { return DisplayName; }
public void setDisplayName(String DisplayName) {
  DisplayNameChanged=BaseSQLClass.twoParms(this.DisplayName, DisplayName);
  this.DisplayName = DisplayName;
}

public boolean DeviceTypeIDChanged;
/** Identifies the device type such as smoke, heat, etc.  Most type information is derived from the drawing, but type data which is finer-grained than the metadata types can be entered via the spread sheet. */ Long DeviceTypeID;
public Long getDeviceTypeID() { return DeviceTypeID; }
public void setDeviceTypeID(Long DeviceTypeID) {
  DeviceTypeIDChanged=BaseSQLClass.twoParms(this.DeviceTypeID, DeviceTypeID);
  this.DeviceTypeID = DeviceTypeID;
}

public boolean TagsChanged;
/** Semicolon-separated search categories. */ String Tags;
public String getTags() { return Tags; }
public void setTags(String Tags) {
  TagsChanged=BaseSQLClass.twoParms(this.Tags, Tags);
  this.Tags = Tags;
}

public boolean DeviceIDChanged;
/**  */ Long DeviceID;
public Long getDeviceID() { return DeviceID; }
public void setDeviceID(Long DeviceID) {
  DeviceIDChanged=BaseSQLClass.twoParms(this.DeviceID, DeviceID);
  this.DeviceID = DeviceID;
}

public boolean NameChanged;
/** This name is the official system name of the entity.  The rules for naming vary with entity type.  For example a room name must be unique within a building floor and a device name must be unique within a building.  This name does not ever change.  A locat */ String Name;
public String getName() { return Name; }
public void setName(String Name) {
  NameChanged=BaseSQLClass.twoParms(this.Name, Name);
  this.Name = Name;
}

public boolean SpaceIDChanged;
/**  */ Long SpaceID;
public Long getSpaceID() { return SpaceID; }
public void setSpaceID(Long SpaceID) {
  SpaceIDChanged=BaseSQLClass.twoParms(this.SpaceID, SpaceID);
  this.SpaceID = SpaceID;
}

public boolean DetailsChanged;
/** Description of the device and / or notes. */ String Details;
public String getDetails() { return Details; }
public void setDetails(String Details) {
  DetailsChanged=BaseSQLClass.twoParms(this.Details, Details);
  this.Details = Details;
}

// 1 to 1 references from Device

protected dbaccess.Location LocationLocationIDLocationIDObj;
public dbaccess.Location fetchOneLocationLocationIDLocationID() { try {
  if (LocationLocationIDLocationIDObj != null) { return LocationLocationIDLocationIDObj;}
  return(LocationLocationIDLocationIDObj =
    dbaccess.bridge._Location.fetchOne("LocationID", (long)LocationID));
  } catch (NullPointerException e) { return null; }}

protected dbaccess.Space SpaceSpaceIDSpaceIDObj;
public dbaccess.Space fetchOneSpaceSpaceIDSpaceID() { try {
  if (SpaceSpaceIDSpaceIDObj != null) { return SpaceSpaceIDSpaceIDObj;}
  return(SpaceSpaceIDSpaceIDObj =
    dbaccess.bridge._Space.fetchOne("SpaceID", (long)SpaceID));
  } catch (NullPointerException e) { return null; }}

protected dbaccess.DeviceType DeviceTypeDeviceTypeIDDeviceTypeIDObj;
public dbaccess.DeviceType fetchOneDeviceTypeDeviceTypeIDDeviceTypeID() { try {
  if (DeviceTypeDeviceTypeIDDeviceTypeIDObj != null) { return DeviceTypeDeviceTypeIDDeviceTypeIDObj;}
  return(DeviceTypeDeviceTypeIDDeviceTypeIDObj =
    dbaccess.bridge._DeviceType.fetchOne("DeviceTypeID", (long)DeviceTypeID));
  } catch (NullPointerException e) { return null; }}

protected dbaccess.Entity EntityEntityIDDeviceIDObj;
public dbaccess.Entity fetchOneEntityEntityIDDeviceID() { try {
  if (EntityEntityIDDeviceIDObj != null) { return EntityEntityIDDeviceIDObj;}
  return(EntityEntityIDDeviceIDObj =
    dbaccess.bridge._Entity.fetchOne("EntityID", (long)DeviceID));
  } catch (NullPointerException e) { return null; }}

// 1 to many references from Device

protected List<dbaccess.Happening> HappeningDeviceIDDeviceIDLst;
public void addHappeningDeviceIDDeviceID(dbaccess.Happening obj) {
  if (HappeningDeviceIDDeviceIDLst == null) { HappeningDeviceIDDeviceIDLst = new ArrayList<dbaccess.Happening>();}
HappeningDeviceIDDeviceIDLst.add(obj); }
public List<dbaccess.Happening> fetchAllHappeningDeviceIDDeviceID() { try {
  if (HappeningDeviceIDDeviceIDLst != null) { return HappeningDeviceIDDeviceIDLst;}
  return(HappeningDeviceIDDeviceIDLst =
    dbaccess.bridge._Happening.fetchMany("DeviceID", (long)DeviceID));
  } catch (NullPointerException e) { return null; }}

public void cleanDirtyFlags() {
  TypeChanged=false;
  LocationIDChanged=false;
  DisplayNameChanged=false;
  DeviceTypeIDChanged=false;
  TagsChanged=false;
  DeviceIDChanged=false;
  NameChanged=false;
  SpaceIDChanged=false;
  DetailsChanged=false;
}

public boolean testDirtyFlags() { return (
  TypeChanged ||
  LocationIDChanged ||
  DisplayNameChanged ||
  DeviceTypeIDChanged ||
  TagsChanged ||
  DeviceIDChanged ||
  NameChanged ||
  SpaceIDChanged ||
  DetailsChanged);
}

public static final String primaryKey = "DeviceID";
public static String fetchPrimaryKey() { return primaryKey; }
public static Device fetchByPrimaryKey(Long colValue) {
  return (Device)BaseSQLClass.getOne(Device.class, null, primaryKey, colValue);
}
public static boolean hasPrimaryKey() { return true; }
}
