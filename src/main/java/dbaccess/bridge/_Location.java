package dbaccess.bridge;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;import java.util.ArrayList;
import zetek.dbcommon.BaseSQLClass;
import dbaccess.Location;

public class _Location extends BaseSQLClass implements Serializable {
  private static final long serialVersionUID=1;

  public _Location() {}

public static List<Location> fetchMany(String colName, Object colValue) {
return (List<Location>)BaseSQLClass.getMany(Location.class, null, colName, colValue);
}

public static Location fetchOne(String colName, Object colValue) {
return (Location)BaseSQLClass.getOne(Location.class, null, colName, colValue);
}

public boolean ImageChanged;
/** File path to the preferred image. */ String Image;
public String getImage() { return Image; }
public void setImage(String Image) {
  ImageChanged=BaseSQLClass.twoParms(this.Image, Image);
  this.Image = Image;
}

public boolean YChanged;
/** Y coordinate of the location relative to the floor */ Double Y;
public Double getY() { return Y; }
public void setY(Double Y) {
  YChanged=BaseSQLClass.twoParms(this.Y, Y);
  this.Y = Y;
}

public boolean LocationIDChanged;
/** Location ID database key */ Long LocationID;
public Long getLocationID() { return LocationID; }
public void setLocationID(Long LocationID) {
  LocationIDChanged=BaseSQLClass.twoParms(this.LocationID, LocationID);
  this.LocationID = LocationID;
}

public boolean XChanged;
/** X coordinate of the location relative to the floor */ Double X;
public Double getX() { return X; }
public void setX(Double X) {
  XChanged=BaseSQLClass.twoParms(this.X, X);
  this.X = X;
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

public boolean NameChanged;
/** This name is the official system name of the entity.  The rules for naming vary with entity type.  For example a room name must be unique within a building floor and a device name must be unique within a building.  This name does not ever change.  A locat */ String Name;
public String getName() { return Name; }
public void setName(String Name) {
  NameChanged=BaseSQLClass.twoParms(this.Name, Name);
  this.Name = Name;
}

public boolean LocationTypeIDChanged;
/** Identifies the location type such as emergency device, door, etc.  Most type information is derived from the drawing, but type data which is finer-grained than the metadata types can be entered via the spread sheet. */ Long LocationTypeID;
public Long getLocationTypeID() { return LocationTypeID; }
public void setLocationTypeID(Long LocationTypeID) {
  LocationTypeIDChanged=BaseSQLClass.twoParms(this.LocationTypeID, LocationTypeID);
  this.LocationTypeID = LocationTypeID;
}

public boolean SpaceIDChanged;
/** Identifies the space in the building where the location appears.  Since all SpaceIDs are unique across all buildings, the SpaceID subsumes the floor and building IDs.  Thus, selecting all Locations which have a specific SpaceID joined with the FloorID get */ Long SpaceID;
public Long getSpaceID() { return SpaceID; }
public void setSpaceID(Long SpaceID) {
  SpaceIDChanged=BaseSQLClass.twoParms(this.SpaceID, SpaceID);
  this.SpaceID = SpaceID;
}

public boolean DetailsChanged;
/** Description of the location and / or notes. */ String Details;
public String getDetails() { return Details; }
public void setDetails(String Details) {
  DetailsChanged=BaseSQLClass.twoParms(this.Details, Details);
  this.Details = Details;
}

// 1 to 1 references from Location

protected dbaccess.Space SpaceSpaceIDSpaceIDObj;
public dbaccess.Space fetchOneSpaceSpaceIDSpaceID() { try {
  if (SpaceSpaceIDSpaceIDObj != null) { return SpaceSpaceIDSpaceIDObj;}
  return(SpaceSpaceIDSpaceIDObj =
    dbaccess.bridge._Space.fetchOne("SpaceID", (long)SpaceID));
  } catch (NullPointerException e) { return null; }}

protected dbaccess.LocationType LocationTypeLocationTypeIDLocationTypeIDObj;
public dbaccess.LocationType fetchOneLocationTypeLocationTypeIDLocationTypeID() { try {
  if (LocationTypeLocationTypeIDLocationTypeIDObj != null) { return LocationTypeLocationTypeIDLocationTypeIDObj;}
  return(LocationTypeLocationTypeIDLocationTypeIDObj =
    dbaccess.bridge._LocationType.fetchOne("LocationTypeID", (long)LocationTypeID));
  } catch (NullPointerException e) { return null; }}

protected dbaccess.Entity EntityEntityIDLocationIDObj;
public dbaccess.Entity fetchOneEntityEntityIDLocationID() { try {
  if (EntityEntityIDLocationIDObj != null) { return EntityEntityIDLocationIDObj;}
  return(EntityEntityIDLocationIDObj =
    dbaccess.bridge._Entity.fetchOne("EntityID", (long)LocationID));
  } catch (NullPointerException e) { return null; }}

// 1 to many references from Location

protected List<dbaccess.Device> DeviceLocationIDLocationIDLst;
public void addDeviceLocationIDLocationID(dbaccess.Device obj) {
  if (DeviceLocationIDLocationIDLst == null) { DeviceLocationIDLocationIDLst = new ArrayList<dbaccess.Device>();}
DeviceLocationIDLocationIDLst.add(obj); }
public List<dbaccess.Device> fetchAllDeviceLocationIDLocationID() { try {
  if (DeviceLocationIDLocationIDLst != null) { return DeviceLocationIDLocationIDLst;}
  return(DeviceLocationIDLocationIDLst =
    dbaccess.bridge._Device.fetchMany("LocationID", (long)LocationID));
  } catch (NullPointerException e) { return null; }}

protected List<dbaccess.Organization> OrganizationLocationIDLocationIDLst;
public void addOrganizationLocationIDLocationID(dbaccess.Organization obj) {
  if (OrganizationLocationIDLocationIDLst == null) { OrganizationLocationIDLocationIDLst = new ArrayList<dbaccess.Organization>();}
OrganizationLocationIDLocationIDLst.add(obj); }
public List<dbaccess.Organization> fetchAllOrganizationLocationIDLocationID() { try {
  if (OrganizationLocationIDLocationIDLst != null) { return OrganizationLocationIDLocationIDLst;}
  return(OrganizationLocationIDLocationIDLst =
    dbaccess.bridge._Organization.fetchMany("LocationID", (long)LocationID));
  } catch (NullPointerException e) { return null; }}

protected List<dbaccess.Person> PersonLocationIDLocationIDLst;
public void addPersonLocationIDLocationID(dbaccess.Person obj) {
  if (PersonLocationIDLocationIDLst == null) { PersonLocationIDLocationIDLst = new ArrayList<dbaccess.Person>();}
PersonLocationIDLocationIDLst.add(obj); }
public List<dbaccess.Person> fetchAllPersonLocationIDLocationID() { try {
  if (PersonLocationIDLocationIDLst != null) { return PersonLocationIDLocationIDLst;}
  return(PersonLocationIDLocationIDLst =
    dbaccess.bridge._Person.fetchMany("LocationID", (long)LocationID));
  } catch (NullPointerException e) { return null; }}

public void cleanDirtyFlags() {
  ImageChanged=false;
  YChanged=false;
  LocationIDChanged=false;
  XChanged=false;
  DisplayNameChanged=false;
  TagsChanged=false;
  NameChanged=false;
  LocationTypeIDChanged=false;
  SpaceIDChanged=false;
  DetailsChanged=false;
}

public boolean testDirtyFlags() { return (
  ImageChanged ||
  YChanged ||
  LocationIDChanged ||
  XChanged ||
  DisplayNameChanged ||
  TagsChanged ||
  NameChanged ||
  LocationTypeIDChanged ||
  SpaceIDChanged ||
  DetailsChanged);
}

public static final String primaryKey = "LocationID";
public static String fetchPrimaryKey() { return primaryKey; }
public static Location fetchByPrimaryKey(Long colValue) {
  return (Location)BaseSQLClass.getOne(Location.class, null, primaryKey, colValue);
}
public static boolean hasPrimaryKey() { return true; }
}
