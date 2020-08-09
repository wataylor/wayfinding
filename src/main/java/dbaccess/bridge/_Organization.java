package dbaccess.bridge;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;import java.util.ArrayList;
import zetek.dbcommon.BaseSQLClass;
import dbaccess.Organization;

public class _Organization extends BaseSQLClass implements Serializable {
  private static final long serialVersionUID=1;

  public _Organization() {}

public static List<Organization> fetchMany(String colName, Object colValue) {
return (List<Organization>)BaseSQLClass.getMany(Organization.class, null, colName, colValue);
}

public static Organization fetchOne(String colName, Object colValue) {
return (Organization)BaseSQLClass.getOne(Organization.class, null, colName, colValue);
}

public boolean MainFaxChanged;
/**  */ String MainFax;
public String getMainFax() { return MainFax; }
public void setMainFax(String MainFax) {
  MainFaxChanged=BaseSQLClass.twoParms(this.MainFax, MainFax);
  this.MainFax = MainFax;
}

public boolean ImageChanged;
/** File path to the preferred logo image. */ String Image;
public String getImage() { return Image; }
public void setImage(String Image) {
  ImageChanged=BaseSQLClass.twoParms(this.Image, Image);
  this.Image = Image;
}

public boolean LocationIDChanged;
/** Links the organization to the default location where a visitor would go to make contact with that organization. */ Long LocationID;
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

public boolean BuildingIDChanged;
/** Associate the organization with the building. */ Long BuildingID;
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

public boolean DivisionChanged;
/** An organization name and division must be unique within a building */ String Division;
public String getDivision() { return Division; }
public void setDivision(String Division) {
  DivisionChanged=BaseSQLClass.twoParms(this.Division, Division);
  this.Division = Division;
}

public boolean NameChanged;
/** This name is the official system name of the entity.  The rules for naming vary with entity type.  For example a room name must be unique within a building floor and a device name must be unique within a building.  This name does not ever change.  A locat */ String Name;
public String getName() { return Name; }
public void setName(String Name) {
  NameChanged=BaseSQLClass.twoParms(this.Name, Name);
  this.Name = Name;
}

public boolean OrganizationTypeIDChanged;
/** Links to an organization type, it is up to the software to handle group types appropriately.  IT is an error to define an organization without specifying its type. */ Long OrganizationTypeID;
public Long getOrganizationTypeID() { return OrganizationTypeID; }
public void setOrganizationTypeID(Long OrganizationTypeID) {
  OrganizationTypeIDChanged=BaseSQLClass.twoParms(this.OrganizationTypeID, OrganizationTypeID);
  this.OrganizationTypeID = OrganizationTypeID;
}

public boolean SpaceIDChanged;
/** Links the organization to the space where a visitor would go to make contact with that organization.  NULL means that the organization has not been assigned to a space. */ Long SpaceID;
public Long getSpaceID() { return SpaceID; }
public void setSpaceID(Long SpaceID) {
  SpaceIDChanged=BaseSQLClass.twoParms(this.SpaceID, SpaceID);
  this.SpaceID = SpaceID;
}

public boolean DetailsChanged;
/** Description of the organization and / or notes */ String Details;
public String getDetails() { return Details; }
public void setDetails(String Details) {
  DetailsChanged=BaseSQLClass.twoParms(this.Details, Details);
  this.Details = Details;
}

public boolean OrganizationIDChanged;
/** Uniquely identify the organization within the entity table. */ Long OrganizationID;
public Long getOrganizationID() { return OrganizationID; }
public void setOrganizationID(Long OrganizationID) {
  OrganizationIDChanged=BaseSQLClass.twoParms(this.OrganizationID, OrganizationID);
  this.OrganizationID = OrganizationID;
}

public boolean MainPhoneChanged;
/**  */ String MainPhone;
public String getMainPhone() { return MainPhone; }
public void setMainPhone(String MainPhone) {
  MainPhoneChanged=BaseSQLClass.twoParms(this.MainPhone, MainPhone);
  this.MainPhone = MainPhone;
}

public boolean MainEmailChanged;
/**  */ String MainEmail;
public String getMainEmail() { return MainEmail; }
public void setMainEmail(String MainEmail) {
  MainEmailChanged=BaseSQLClass.twoParms(this.MainEmail, MainEmail);
  this.MainEmail = MainEmail;
}

// 1 to 1 references from Organization

protected dbaccess.Building BuildingBuildingIDBuildingIDObj;
public dbaccess.Building fetchOneBuildingBuildingIDBuildingID() { try {
  if (BuildingBuildingIDBuildingIDObj != null) { return BuildingBuildingIDBuildingIDObj;}
  return(BuildingBuildingIDBuildingIDObj =
    dbaccess.bridge._Building.fetchOne("BuildingID", (long)BuildingID));
  } catch (NullPointerException e) { return null; }}

protected dbaccess.OrganizationType OrganizationTypeOrganizationTypeIDOrganizationTypeIDObj;
public dbaccess.OrganizationType fetchOneOrganizationTypeOrganizationTypeIDOrganizationTypeID() { try {
  if (OrganizationTypeOrganizationTypeIDOrganizationTypeIDObj != null) { return OrganizationTypeOrganizationTypeIDOrganizationTypeIDObj;}
  return(OrganizationTypeOrganizationTypeIDOrganizationTypeIDObj =
    dbaccess.bridge._OrganizationType.fetchOne("OrganizationTypeID", (long)OrganizationTypeID));
  } catch (NullPointerException e) { return null; }}

protected dbaccess.Space SpaceSpaceIDSpaceIDObj;
public dbaccess.Space fetchOneSpaceSpaceIDSpaceID() { try {
  if (SpaceSpaceIDSpaceIDObj != null) { return SpaceSpaceIDSpaceIDObj;}
  return(SpaceSpaceIDSpaceIDObj =
    dbaccess.bridge._Space.fetchOne("SpaceID", (long)SpaceID));
  } catch (NullPointerException e) { return null; }}

protected dbaccess.Location LocationLocationIDLocationIDObj;
public dbaccess.Location fetchOneLocationLocationIDLocationID() { try {
  if (LocationLocationIDLocationIDObj != null) { return LocationLocationIDLocationIDObj;}
  return(LocationLocationIDLocationIDObj =
    dbaccess.bridge._Location.fetchOne("LocationID", (long)LocationID));
  } catch (NullPointerException e) { return null; }}

// 1 to many references from Organization

protected List<dbaccess.Person> PersonOrganizationIDOrganizationIDLst;
public void addPersonOrganizationIDOrganizationID(dbaccess.Person obj) {
  if (PersonOrganizationIDOrganizationIDLst == null) { PersonOrganizationIDOrganizationIDLst = new ArrayList<dbaccess.Person>();}
PersonOrganizationIDOrganizationIDLst.add(obj); }
public List<dbaccess.Person> fetchAllPersonOrganizationIDOrganizationID() { try {
  if (PersonOrganizationIDOrganizationIDLst != null) { return PersonOrganizationIDOrganizationIDLst;}
  return(PersonOrganizationIDOrganizationIDLst =
    dbaccess.bridge._Person.fetchMany("OrganizationID", (long)OrganizationID));
  } catch (NullPointerException e) { return null; }}

public void cleanDirtyFlags() {
  MainFaxChanged=false;
  ImageChanged=false;
  LocationIDChanged=false;
  DisplayNameChanged=false;
  BuildingIDChanged=false;
  TagsChanged=false;
  DivisionChanged=false;
  NameChanged=false;
  OrganizationTypeIDChanged=false;
  SpaceIDChanged=false;
  DetailsChanged=false;
  OrganizationIDChanged=false;
  MainPhoneChanged=false;
  MainEmailChanged=false;
}

public boolean testDirtyFlags() { return (
  MainFaxChanged ||
  ImageChanged ||
  LocationIDChanged ||
  DisplayNameChanged ||
  BuildingIDChanged ||
  TagsChanged ||
  DivisionChanged ||
  NameChanged ||
  OrganizationTypeIDChanged ||
  SpaceIDChanged ||
  DetailsChanged ||
  OrganizationIDChanged ||
  MainPhoneChanged ||
  MainEmailChanged);
}

public static final String primaryKey = "OrganizationID";
public static String fetchPrimaryKey() { return primaryKey; }
public static Organization fetchByPrimaryKey(Long colValue) {
  return (Organization)BaseSQLClass.getOne(Organization.class, null, primaryKey, colValue);
}
public static boolean hasPrimaryKey() { return true; }
}
