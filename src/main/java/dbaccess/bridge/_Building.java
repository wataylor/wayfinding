package dbaccess.bridge;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;import java.util.ArrayList;
import zetek.dbcommon.BaseSQLClass;
import dbaccess.Building;

public class _Building extends BaseSQLClass implements Serializable {
  private static final long serialVersionUID=1;

  public _Building() {}

public static List<Building> fetchMany(String colName, Object colValue) {
return (List<Building>)BaseSQLClass.getMany(Building.class, null, colName, colValue);
}

public static Building fetchOne(String colName, Object colValue) {
return (Building)BaseSQLClass.getOne(Building.class, null, colName, colValue);
}

public boolean TopStreetChanged;
/** Street name which appears above the top side of the building shape. */ String TopStreet;
public String getTopStreet() { return TopStreet; }
public void setTopStreet(String TopStreet) {
  TopStreetChanged=BaseSQLClass.twoParms(this.TopStreet, TopStreet);
  this.TopStreet = TopStreet;
}

public boolean BuildingGroupIDChanged;
/** Links a building to a group, NULL means that the building is not a member of a group. */ Long BuildingGroupID;
public Long getBuildingGroupID() { return BuildingGroupID; }
public void setBuildingGroupID(Long BuildingGroupID) {
  BuildingGroupIDChanged=BaseSQLClass.twoParms(this.BuildingGroupID, BuildingGroupID);
  this.BuildingGroupID = BuildingGroupID;
}

public boolean BuildingIDChanged;
/**  */ Long BuildingID;
public Long getBuildingID() { return BuildingID; }
public void setBuildingID(Long BuildingID) {
  BuildingIDChanged=BaseSQLClass.twoParms(this.BuildingID, BuildingID);
  this.BuildingID = BuildingID;
}

public boolean LeftStreetChanged;
/** Street name which appears to the left of the left side of the building shape. */ String LeftStreet;
public String getLeftStreet() { return LeftStreet; }
public void setLeftStreet(String LeftStreet) {
  LeftStreetChanged=BaseSQLClass.twoParms(this.LeftStreet, LeftStreet);
  this.LeftStreet = LeftStreet;
}

public boolean ZipChanged;
/** This field helps make building names unique; it ought to be rare to find two buildings with the same name in the same zip code. */ String Zip;
public String getZip() { return Zip; }
public void setZip(String Zip) {
  ZipChanged=BaseSQLClass.twoParms(this.Zip, Zip);
  this.Zip = Zip;
}

public boolean Address1Line1Changed;
/** Main building street address where visitors enter */ String Address1Line1;
public String getAddress1Line1() { return Address1Line1; }
public void setAddress1Line1(String Address1Line1) {
  Address1Line1Changed=BaseSQLClass.twoParms(this.Address1Line1, Address1Line1);
  this.Address1Line1 = Address1Line1;
}

public boolean MainFaxChanged;
/**  */ String MainFax;
public String getMainFax() { return MainFax; }
public void setMainFax(String MainFax) {
  MainFaxChanged=BaseSQLClass.twoParms(this.MainFax, MainFax);
  this.MainFax = MainFax;
}

public boolean NameChanged;
/** This name is the official system name of the entity.  The rules for naming vary with entity type.  For example a room name must be unique within a building floor and a device name must be unique within a building.  This name does not ever change.  A locat */ String Name;
public String getName() { return Name; }
public void setName(String Name) {
  NameChanged=BaseSQLClass.twoParms(this.Name, Name);
  this.Name = Name;
}

public boolean MainPhoneChanged;
/**  */ String MainPhone;
public String getMainPhone() { return MainPhone; }
public void setMainPhone(String MainPhone) {
  MainPhoneChanged=BaseSQLClass.twoParms(this.MainPhone, MainPhone);
  this.MainPhone = MainPhone;
}

public boolean BottomStreetChanged;
/** Street name which appears below the bottom side of the building shape. */ String BottomStreet;
public String getBottomStreet() { return BottomStreet; }
public void setBottomStreet(String BottomStreet) {
  BottomStreetChanged=BaseSQLClass.twoParms(this.BottomStreet, BottomStreet);
  this.BottomStreet = BottomStreet;
}

public boolean RightStreetChanged;
/** Street name which appears to the right of the right side of the building shape. */ String RightStreet;
public String getRightStreet() { return RightStreet; }
public void setRightStreet(String RightStreet) {
  RightStreetChanged=BaseSQLClass.twoParms(this.RightStreet, RightStreet);
  this.RightStreet = RightStreet;
}

public boolean DetailsChanged;
/** Description of the building and / or notes. */ String Details;
public String getDetails() { return Details; }
public void setDetails(String Details) {
  DetailsChanged=BaseSQLClass.twoParms(this.Details, Details);
  this.Details = Details;
}

public boolean LocationIDChanged;
/** If nonzero, this is the default location from which all routes in the building start.  A user may override the start location when picking a route, of course.  This cannot be a constraint because Locations must be defined after buildings. */ Long LocationID;
public Long getLocationID() { return LocationID; }
public void setLocationID(Long LocationID) {
  LocationIDChanged=BaseSQLClass.twoParms(this.LocationID, LocationID);
  this.LocationID = LocationID;
}

public boolean MainEmailChanged;
/**  */ String MainEmail;
public String getMainEmail() { return MainEmail; }
public void setMainEmail(String MainEmail) {
  MainEmailChanged=BaseSQLClass.twoParms(this.MainEmail, MainEmail);
  this.MainEmail = MainEmail;
}

public boolean TagsChanged;
/** Semicolon-separated search categories. */ String Tags;
public String getTags() { return Tags; }
public void setTags(String Tags) {
  TagsChanged=BaseSQLClass.twoParms(this.Tags, Tags);
  this.Tags = Tags;
}

public boolean DisplayNameChanged;
/** This name is the display name for the entity. The rules vary with entity type.  A room display name must be unique within a building.  The display name defaults to the name when the entity is first created. */ String DisplayName;
public String getDisplayName() { return DisplayName; }
public void setDisplayName(String DisplayName) {
  DisplayNameChanged=BaseSQLClass.twoParms(this.DisplayName, DisplayName);
  this.DisplayName = DisplayName;
}

public boolean NickNameChanged;
/** This name is the nick name for the building. */ String NickName;
public String getNickName() { return NickName; }
public void setNickName(String NickName) {
  NickNameChanged=BaseSQLClass.twoParms(this.NickName, NickName);
  this.NickName = NickName;
}

// 1 to 1 references from Building

protected dbaccess.Entity EntityEntityIDBuildingIDObj;
public dbaccess.Entity fetchOneEntityEntityIDBuildingID() { try {
  if (EntityEntityIDBuildingIDObj != null) { return EntityEntityIDBuildingIDObj;}
  return(EntityEntityIDBuildingIDObj =
    dbaccess.bridge._Entity.fetchOne("EntityID", (long)BuildingID));
  } catch (NullPointerException e) { return null; }}

protected dbaccess.BuildingGroup BuildingGroupGroupIDBuildingGroupIDObj;
public dbaccess.BuildingGroup fetchOneBuildingGroupGroupIDBuildingGroupID() { try {
  if (BuildingGroupGroupIDBuildingGroupIDObj != null) { return BuildingGroupGroupIDBuildingGroupIDObj;}
  return(BuildingGroupGroupIDBuildingGroupIDObj =
    dbaccess.bridge._BuildingGroup.fetchOne("GroupID", (long)BuildingGroupID));
  } catch (NullPointerException e) { return null; }}

// 1 to many references from Building

protected List<dbaccess.Floor> FloorBuildingIDBuildingIDLst;
public void addFloorBuildingIDBuildingID(dbaccess.Floor obj) {
  if (FloorBuildingIDBuildingIDLst == null) { FloorBuildingIDBuildingIDLst = new ArrayList<dbaccess.Floor>();}
FloorBuildingIDBuildingIDLst.add(obj); }
public List<dbaccess.Floor> fetchAllFloorBuildingIDBuildingID() { try {
  if (FloorBuildingIDBuildingIDLst != null) { return FloorBuildingIDBuildingIDLst;}
  return(FloorBuildingIDBuildingIDLst =
    dbaccess.bridge._Floor.fetchMany("BuildingID", (long)BuildingID));
  } catch (NullPointerException e) { return null; }}

protected List<dbaccess.Happening> HappeningBuildingIDBuildingIDLst;
public void addHappeningBuildingIDBuildingID(dbaccess.Happening obj) {
  if (HappeningBuildingIDBuildingIDLst == null) { HappeningBuildingIDBuildingIDLst = new ArrayList<dbaccess.Happening>();}
HappeningBuildingIDBuildingIDLst.add(obj); }
public List<dbaccess.Happening> fetchAllHappeningBuildingIDBuildingID() { try {
  if (HappeningBuildingIDBuildingIDLst != null) { return HappeningBuildingIDBuildingIDLst;}
  return(HappeningBuildingIDBuildingIDLst =
    dbaccess.bridge._Happening.fetchMany("BuildingID", (long)BuildingID));
  } catch (NullPointerException e) { return null; }}

protected List<dbaccess.Organization> OrganizationBuildingIDBuildingIDLst;
public void addOrganizationBuildingIDBuildingID(dbaccess.Organization obj) {
  if (OrganizationBuildingIDBuildingIDLst == null) { OrganizationBuildingIDBuildingIDLst = new ArrayList<dbaccess.Organization>();}
OrganizationBuildingIDBuildingIDLst.add(obj); }
public List<dbaccess.Organization> fetchAllOrganizationBuildingIDBuildingID() { try {
  if (OrganizationBuildingIDBuildingIDLst != null) { return OrganizationBuildingIDBuildingIDLst;}
  return(OrganizationBuildingIDBuildingIDLst =
    dbaccess.bridge._Organization.fetchMany("BuildingID", (long)BuildingID));
  } catch (NullPointerException e) { return null; }}

protected List<dbaccess.Person> PersonBuildingIDBuildingIDLst;
public void addPersonBuildingIDBuildingID(dbaccess.Person obj) {
  if (PersonBuildingIDBuildingIDLst == null) { PersonBuildingIDBuildingIDLst = new ArrayList<dbaccess.Person>();}
PersonBuildingIDBuildingIDLst.add(obj); }
public List<dbaccess.Person> fetchAllPersonBuildingIDBuildingID() { try {
  if (PersonBuildingIDBuildingIDLst != null) { return PersonBuildingIDBuildingIDLst;}
  return(PersonBuildingIDBuildingIDLst =
    dbaccess.bridge._Person.fetchMany("BuildingID", (long)BuildingID));
  } catch (NullPointerException e) { return null; }}

public void cleanDirtyFlags() {
  TopStreetChanged=false;
  BuildingGroupIDChanged=false;
  BuildingIDChanged=false;
  LeftStreetChanged=false;
  ZipChanged=false;
  Address1Line1Changed=false;
  MainFaxChanged=false;
  NameChanged=false;
  MainPhoneChanged=false;
  BottomStreetChanged=false;
  RightStreetChanged=false;
  DetailsChanged=false;
  LocationIDChanged=false;
  MainEmailChanged=false;
  TagsChanged=false;
  DisplayNameChanged=false;
  NickNameChanged=false;
}

public boolean testDirtyFlags() { return (
  TopStreetChanged ||
  BuildingGroupIDChanged ||
  BuildingIDChanged ||
  LeftStreetChanged ||
  ZipChanged ||
  Address1Line1Changed ||
  MainFaxChanged ||
  NameChanged ||
  MainPhoneChanged ||
  BottomStreetChanged ||
  RightStreetChanged ||
  DetailsChanged ||
  LocationIDChanged ||
  MainEmailChanged ||
  TagsChanged ||
  DisplayNameChanged ||
  NickNameChanged);
}

public static final String primaryKey = "BuildingID";
public static String fetchPrimaryKey() { return primaryKey; }
public static Building fetchByPrimaryKey(Long colValue) {
  return (Building)BaseSQLClass.getOne(Building.class, null, primaryKey, colValue);
}
public static boolean hasPrimaryKey() { return true; }
}
