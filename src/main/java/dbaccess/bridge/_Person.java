package dbaccess.bridge;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;import java.util.ArrayList;
import zetek.dbcommon.BaseSQLClass;
import dbaccess.Person;

public class _Person extends BaseSQLClass implements Serializable {
  private static final long serialVersionUID=1;

  public _Person() {}

public static List<Person> fetchMany(String colName, Object colValue) {
return (List<Person>)BaseSQLClass.getMany(Person.class, null, colName, colValue);
}

public static Person fetchOne(String colName, Object colValue) {
return (Person)BaseSQLClass.getOne(Person.class, null, colName, colValue);
}

public boolean BuildingIDChanged;
/** Associate the person with the building. */ Long BuildingID;
public Long getBuildingID() { return BuildingID; }
public void setBuildingID(Long BuildingID) {
  BuildingIDChanged=BaseSQLClass.twoParms(this.BuildingID, BuildingID);
  this.BuildingID = BuildingID;
}

public boolean FaxChanged;
/**  */ String Fax;
public String getFax() { return Fax; }
public void setFax(String Fax) {
  FaxChanged=BaseSQLClass.twoParms(this.Fax, Fax);
  this.Fax = Fax;
}

public boolean UserChanged;
/** The user name of this person, null means the person cannot log on */ String User;
public String getUser() { return User; }
public void setUser(String User) {
  UserChanged=BaseSQLClass.twoParms(this.User, User);
  this.User = User;
}

public boolean TitleChanged;
/** Required field */ String Title;
public String getTitle() { return Title; }
public void setTitle(String Title) {
  TitleChanged=BaseSQLClass.twoParms(this.Title, Title);
  this.Title = Title;
}

public boolean PassChanged;
/** The password for authentication */ String Pass;
public String getPass() { return Pass; }
public void setPass(String Pass) {
  PassChanged=BaseSQLClass.twoParms(this.Pass, Pass);
  this.Pass = Pass;
}

public boolean SpaceIDChanged;
/** Links the person to the space where a visitor would go to make contact with that person.  NULL means that the person has not been assigned to a space. */ Long SpaceID;
public Long getSpaceID() { return SpaceID; }
public void setSpaceID(Long SpaceID) {
  SpaceIDChanged=BaseSQLClass.twoParms(this.SpaceID, SpaceID);
  this.SpaceID = SpaceID;
}

public boolean MiddleNameChanged;
/** Not a required field, may be an initial */ String MiddleName;
public String getMiddleName() { return MiddleName; }
public void setMiddleName(String MiddleName) {
  MiddleNameChanged=BaseSQLClass.twoParms(this.MiddleName, MiddleName);
  this.MiddleName = MiddleName;
}

public boolean TimesLoggedOnChanged;
/** Identifies hard-core users */ Long TimesLoggedOn;
public Long getTimesLoggedOn() { return TimesLoggedOn; }
public void setTimesLoggedOn(Long TimesLoggedOn) {
  TimesLoggedOnChanged=BaseSQLClass.twoParms(this.TimesLoggedOn, TimesLoggedOn);
  this.TimesLoggedOn = TimesLoggedOn;
}

public boolean DetailsChanged;
/** Description of the device and / or notes. */ String Details;
public String getDetails() { return Details; }
public void setDetails(String Details) {
  DetailsChanged=BaseSQLClass.twoParms(this.Details, Details);
  this.Details = Details;
}

public boolean RecentLogonChanged;
/** Used in the welcome greeting */ Timestamp RecentLogon;
public Timestamp getRecentLogon() { return RecentLogon; }
public void setRecentLogon(Timestamp RecentLogon) {
  RecentLogonChanged=BaseSQLClass.twoParms(this.RecentLogon, RecentLogon);
  this.RecentLogon = RecentLogon;
}

public boolean MobilePhoneChanged;
/**  */ String MobilePhone;
public String getMobilePhone() { return MobilePhone; }
public void setMobilePhone(String MobilePhone) {
  MobilePhoneChanged=BaseSQLClass.twoParms(this.MobilePhone, MobilePhone);
  this.MobilePhone = MobilePhone;
}

public boolean LocationIDChanged;
/** Links the person to the location where a visitor would go to make contact with that person.  NULL means that the person has not been assigned to a location. */ Long LocationID;
public Long getLocationID() { return LocationID; }
public void setLocationID(Long LocationID) {
  LocationIDChanged=BaseSQLClass.twoParms(this.LocationID, LocationID);
  this.LocationID = LocationID;
}

public boolean WorkPhoneChanged;
/**  */ String WorkPhone;
public String getWorkPhone() { return WorkPhone; }
public void setWorkPhone(String WorkPhone) {
  WorkPhoneChanged=BaseSQLClass.twoParms(this.WorkPhone, WorkPhone);
  this.WorkPhone = WorkPhone;
}

public boolean LastNameChanged;
/** Required field */ String LastName;
public String getLastName() { return LastName; }
public void setLastName(String LastName) {
  LastNameChanged=BaseSQLClass.twoParms(this.LastName, LastName);
  this.LastName = LastName;
}

public boolean PersonIDChanged;
/** The common entity data for the person */ Long PersonID;
public Long getPersonID() { return PersonID; }
public void setPersonID(Long PersonID) {
  PersonIDChanged=BaseSQLClass.twoParms(this.PersonID, PersonID);
  this.PersonID = PersonID;
}

public boolean FirstNameChanged;
/** Required field */ String FirstName;
public String getFirstName() { return FirstName; }
public void setFirstName(String FirstName) {
  FirstNameChanged=BaseSQLClass.twoParms(this.FirstName, FirstName);
  this.FirstName = FirstName;
}

public boolean TagsChanged;
/** Semicolon-separated search categories. */ String Tags;
public String getTags() { return Tags; }
public void setTags(String Tags) {
  TagsChanged=BaseSQLClass.twoParms(this.Tags, Tags);
  this.Tags = Tags;
}

public boolean OrganizationIDChanged;
/** Links the person to an organization.  NULL means that the person has not been associated with a company. */ Long OrganizationID;
public Long getOrganizationID() { return OrganizationID; }
public void setOrganizationID(Long OrganizationID) {
  OrganizationIDChanged=BaseSQLClass.twoParms(this.OrganizationID, OrganizationID);
  this.OrganizationID = OrganizationID;
}

public boolean ImageChanged;
/** File path to the preferred image. */ String Image;
public String getImage() { return Image; }
public void setImage(String Image) {
  ImageChanged=BaseSQLClass.twoParms(this.Image, Image);
  this.Image = Image;
}

public boolean EmailAddressChanged;
/** Optional field, must be formatted correctly but is not checked for a valid email address */ String EmailAddress;
public String getEmailAddress() { return EmailAddress; }
public void setEmailAddress(String EmailAddress) {
  EmailAddressChanged=BaseSQLClass.twoParms(this.EmailAddress, EmailAddress);
  this.EmailAddress = EmailAddress;
}

public boolean LanguageIDChanged;
/** Defines the individual language preference, defaults to English.  When people ask for routes without logging on, language preference is taken from the browser locale. */ Long LanguageID;
public Long getLanguageID() { return LanguageID; }
public void setLanguageID(Long LanguageID) {
  LanguageIDChanged=BaseSQLClass.twoParms(this.LanguageID, LanguageID);
  this.LanguageID = LanguageID;
}

// 1 to 1 references from Person

protected dbaccess.Building BuildingBuildingIDBuildingIDObj;
public dbaccess.Building fetchOneBuildingBuildingIDBuildingID() { try {
  if (BuildingBuildingIDBuildingIDObj != null) { return BuildingBuildingIDBuildingIDObj;}
  return(BuildingBuildingIDBuildingIDObj =
    dbaccess.bridge._Building.fetchOne("BuildingID", (long)BuildingID));
  } catch (NullPointerException e) { return null; }}

protected dbaccess.Language LanguageLanguageIDLanguageIDObj;
public dbaccess.Language fetchOneLanguageLanguageIDLanguageID() { try {
  if (LanguageLanguageIDLanguageIDObj != null) { return LanguageLanguageIDLanguageIDObj;}
  return(LanguageLanguageIDLanguageIDObj =
    dbaccess.bridge._Language.fetchOne("LanguageID", (long)LanguageID));
  } catch (NullPointerException e) { return null; }}

protected dbaccess.Organization OrganizationOrganizationIDOrganizationIDObj;
public dbaccess.Organization fetchOneOrganizationOrganizationIDOrganizationID() { try {
  if (OrganizationOrganizationIDOrganizationIDObj != null) { return OrganizationOrganizationIDOrganizationIDObj;}
  return(OrganizationOrganizationIDOrganizationIDObj =
    dbaccess.bridge._Organization.fetchOne("OrganizationID", (long)OrganizationID));
  } catch (NullPointerException e) { return null; }}

protected dbaccess.Entity EntityEntityIDPersonIDObj;
public dbaccess.Entity fetchOneEntityEntityIDPersonID() { try {
  if (EntityEntityIDPersonIDObj != null) { return EntityEntityIDPersonIDObj;}
  return(EntityEntityIDPersonIDObj =
    dbaccess.bridge._Entity.fetchOne("EntityID", (long)PersonID));
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

// 1 to many references from Person

protected List<dbaccess.ChangeLog> ChangeLogPersonIDPersonIDLst;
public void addChangeLogPersonIDPersonID(dbaccess.ChangeLog obj) {
  if (ChangeLogPersonIDPersonIDLst == null) { ChangeLogPersonIDPersonIDLst = new ArrayList<dbaccess.ChangeLog>();}
ChangeLogPersonIDPersonIDLst.add(obj); }
public List<dbaccess.ChangeLog> fetchAllChangeLogPersonIDPersonID() { try {
  if (ChangeLogPersonIDPersonIDLst != null) { return ChangeLogPersonIDPersonIDLst;}
  return(ChangeLogPersonIDPersonIDLst =
    dbaccess.bridge._ChangeLog.fetchMany("PersonID", (long)PersonID));
  } catch (NullPointerException e) { return null; }}

protected List<dbaccess.LogInHistory> LogInHistoryPersonIDPersonIDLst;
public void addLogInHistoryPersonIDPersonID(dbaccess.LogInHistory obj) {
  if (LogInHistoryPersonIDPersonIDLst == null) { LogInHistoryPersonIDPersonIDLst = new ArrayList<dbaccess.LogInHistory>();}
LogInHistoryPersonIDPersonIDLst.add(obj); }
public List<dbaccess.LogInHistory> fetchAllLogInHistoryPersonIDPersonID() { try {
  if (LogInHistoryPersonIDPersonIDLst != null) { return LogInHistoryPersonIDPersonIDLst;}
  return(LogInHistoryPersonIDPersonIDLst =
    dbaccess.bridge._LogInHistory.fetchMany("PersonID", (long)PersonID));
  } catch (NullPointerException e) { return null; }}

protected List<dbaccess.Permission> PermissionPersonIDPersonIDLst;
public void addPermissionPersonIDPersonID(dbaccess.Permission obj) {
  if (PermissionPersonIDPersonIDLst == null) { PermissionPersonIDPersonIDLst = new ArrayList<dbaccess.Permission>();}
PermissionPersonIDPersonIDLst.add(obj); }
public List<dbaccess.Permission> fetchAllPermissionPersonIDPersonID() { try {
  if (PermissionPersonIDPersonIDLst != null) { return PermissionPersonIDPersonIDLst;}
  return(PermissionPersonIDPersonIDLst =
    dbaccess.bridge._Permission.fetchMany("PersonID", (long)PersonID));
  } catch (NullPointerException e) { return null; }}

public void cleanDirtyFlags() {
  BuildingIDChanged=false;
  FaxChanged=false;
  UserChanged=false;
  TitleChanged=false;
  PassChanged=false;
  SpaceIDChanged=false;
  MiddleNameChanged=false;
  TimesLoggedOnChanged=false;
  DetailsChanged=false;
  RecentLogonChanged=false;
  MobilePhoneChanged=false;
  LocationIDChanged=false;
  WorkPhoneChanged=false;
  LastNameChanged=false;
  PersonIDChanged=false;
  FirstNameChanged=false;
  TagsChanged=false;
  OrganizationIDChanged=false;
  ImageChanged=false;
  EmailAddressChanged=false;
  LanguageIDChanged=false;
}

public boolean testDirtyFlags() { return (
  BuildingIDChanged ||
  FaxChanged ||
  UserChanged ||
  TitleChanged ||
  PassChanged ||
  SpaceIDChanged ||
  MiddleNameChanged ||
  TimesLoggedOnChanged ||
  DetailsChanged ||
  RecentLogonChanged ||
  MobilePhoneChanged ||
  LocationIDChanged ||
  WorkPhoneChanged ||
  LastNameChanged ||
  PersonIDChanged ||
  FirstNameChanged ||
  TagsChanged ||
  OrganizationIDChanged ||
  ImageChanged ||
  EmailAddressChanged ||
  LanguageIDChanged);
}

public static final String primaryKey = "PersonID";
public static String fetchPrimaryKey() { return primaryKey; }
public static Person fetchByPrimaryKey(Long colValue) {
  return (Person)BaseSQLClass.getOne(Person.class, null, primaryKey, colValue);
}
public static boolean hasPrimaryKey() { return true; }
}
