package dbaccess.bridge;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;import java.util.ArrayList;
import zetek.dbcommon.BaseSQLClass;
import dbaccess.EnglishTerm;

public class _EnglishTerm extends BaseSQLClass implements Serializable {
  private static final long serialVersionUID=1;

  public _EnglishTerm() {}

public static List<EnglishTerm> fetchMany(String colName, Object colValue) {
return (List<EnglishTerm>)BaseSQLClass.getMany(EnglishTerm.class, null, colName, colValue);
}

public static EnglishTerm fetchOne(String colName, Object colValue) {
return (EnglishTerm)BaseSQLClass.getOne(EnglishTerm.class, null, colName, colValue);
}

public boolean NameChanged;
/** Term which is used if the chosen language is English */ String Name;
public String getName() { return Name; }
public void setName(String Name) {
  NameChanged=BaseSQLClass.twoParms(this.Name, Name);
  this.Name = Name;
}

public boolean TermTypeIDChanged;
/**  */ Long TermTypeID;
public Long getTermTypeID() { return TermTypeID; }
public void setTermTypeID(Long TermTypeID) {
  TermTypeIDChanged=BaseSQLClass.twoParms(this.TermTypeID, TermTypeID);
  this.TermTypeID = TermTypeID;
}

public boolean TermIDChanged;
/** Id to reference a specific term or phrase */ Long TermID;
public Long getTermID() { return TermID; }
public void setTermID(Long TermID) {
  TermIDChanged=BaseSQLClass.twoParms(this.TermID, TermID);
  this.TermID = TermID;
}

// 1 to 1 references from EnglishTerm

protected dbaccess.TermType TermTypeTermTypeIDTermTypeIDObj;
public dbaccess.TermType fetchOneTermTypeTermTypeIDTermTypeID() { try {
  if (TermTypeTermTypeIDTermTypeIDObj != null) { return TermTypeTermTypeIDTermTypeIDObj;}
  return(TermTypeTermTypeIDTermTypeIDObj =
    dbaccess.bridge._TermType.fetchOne("TermTypeID", (long)TermTypeID));
  } catch (NullPointerException e) { return null; }}

// 1 to many references from EnglishTerm

protected List<dbaccess.AddressType> AddressTypeTermIDTermIDLst;
public void addAddressTypeTermIDTermID(dbaccess.AddressType obj) {
  if (AddressTypeTermIDTermIDLst == null) { AddressTypeTermIDTermIDLst = new ArrayList<dbaccess.AddressType>();}
AddressTypeTermIDTermIDLst.add(obj); }
public List<dbaccess.AddressType> fetchAllAddressTypeTermIDTermID() { try {
  if (AddressTypeTermIDTermIDLst != null) { return AddressTypeTermIDTermIDLst;}
  return(AddressTypeTermIDTermIDLst =
    dbaccess.bridge._AddressType.fetchMany("TermID", (long)TermID));
  } catch (NullPointerException e) { return null; }}

protected List<dbaccess.AttributeType> AttributeTypeTermIDTermIDLst;
public void addAttributeTypeTermIDTermID(dbaccess.AttributeType obj) {
  if (AttributeTypeTermIDTermIDLst == null) { AttributeTypeTermIDTermIDLst = new ArrayList<dbaccess.AttributeType>();}
AttributeTypeTermIDTermIDLst.add(obj); }
public List<dbaccess.AttributeType> fetchAllAttributeTypeTermIDTermID() { try {
  if (AttributeTypeTermIDTermIDLst != null) { return AttributeTypeTermIDTermIDLst;}
  return(AttributeTypeTermIDTermIDLst =
    dbaccess.bridge._AttributeType.fetchMany("TermID", (long)TermID));
  } catch (NullPointerException e) { return null; }}

protected List<dbaccess.Country> CountryTermIDTermIDLst;
public void addCountryTermIDTermID(dbaccess.Country obj) {
  if (CountryTermIDTermIDLst == null) { CountryTermIDTermIDLst = new ArrayList<dbaccess.Country>();}
CountryTermIDTermIDLst.add(obj); }
public List<dbaccess.Country> fetchAllCountryTermIDTermID() { try {
  if (CountryTermIDTermIDLst != null) { return CountryTermIDTermIDLst;}
  return(CountryTermIDTermIDLst =
    dbaccess.bridge._Country.fetchMany("TermID", (long)TermID));
  } catch (NullPointerException e) { return null; }}

protected List<dbaccess.DeviceType> DeviceTypeTermIDTermIDLst;
public void addDeviceTypeTermIDTermID(dbaccess.DeviceType obj) {
  if (DeviceTypeTermIDTermIDLst == null) { DeviceTypeTermIDTermIDLst = new ArrayList<dbaccess.DeviceType>();}
DeviceTypeTermIDTermIDLst.add(obj); }
public List<dbaccess.DeviceType> fetchAllDeviceTypeTermIDTermID() { try {
  if (DeviceTypeTermIDTermIDLst != null) { return DeviceTypeTermIDTermIDLst;}
  return(DeviceTypeTermIDTermIDLst =
    dbaccess.bridge._DeviceType.fetchMany("TermID", (long)TermID));
  } catch (NullPointerException e) { return null; }}

protected List<dbaccess.EntityType> EntityTypeTermIDTermIDLst;
public void addEntityTypeTermIDTermID(dbaccess.EntityType obj) {
  if (EntityTypeTermIDTermIDLst == null) { EntityTypeTermIDTermIDLst = new ArrayList<dbaccess.EntityType>();}
EntityTypeTermIDTermIDLst.add(obj); }
public List<dbaccess.EntityType> fetchAllEntityTypeTermIDTermID() { try {
  if (EntityTypeTermIDTermIDLst != null) { return EntityTypeTermIDTermIDLst;}
  return(EntityTypeTermIDTermIDLst =
    dbaccess.bridge._EntityType.fetchMany("TermID", (long)TermID));
  } catch (NullPointerException e) { return null; }}

protected List<dbaccess.GroupType> GroupTypeTermIDTermIDLst;
public void addGroupTypeTermIDTermID(dbaccess.GroupType obj) {
  if (GroupTypeTermIDTermIDLst == null) { GroupTypeTermIDTermIDLst = new ArrayList<dbaccess.GroupType>();}
GroupTypeTermIDTermIDLst.add(obj); }
public List<dbaccess.GroupType> fetchAllGroupTypeTermIDTermID() { try {
  if (GroupTypeTermIDTermIDLst != null) { return GroupTypeTermIDTermIDLst;}
  return(GroupTypeTermIDTermIDLst =
    dbaccess.bridge._GroupType.fetchMany("TermID", (long)TermID));
  } catch (NullPointerException e) { return null; }}

protected List<dbaccess.HappeningType> HappeningTypeTermIDTermIDLst;
public void addHappeningTypeTermIDTermID(dbaccess.HappeningType obj) {
  if (HappeningTypeTermIDTermIDLst == null) { HappeningTypeTermIDTermIDLst = new ArrayList<dbaccess.HappeningType>();}
HappeningTypeTermIDTermIDLst.add(obj); }
public List<dbaccess.HappeningType> fetchAllHappeningTypeTermIDTermID() { try {
  if (HappeningTypeTermIDTermIDLst != null) { return HappeningTypeTermIDTermIDLst;}
  return(HappeningTypeTermIDTermIDLst =
    dbaccess.bridge._HappeningType.fetchMany("TermID", (long)TermID));
  } catch (NullPointerException e) { return null; }}

protected List<dbaccess.LocationType> LocationTypeTermIDTermIDLst;
public void addLocationTypeTermIDTermID(dbaccess.LocationType obj) {
  if (LocationTypeTermIDTermIDLst == null) { LocationTypeTermIDTermIDLst = new ArrayList<dbaccess.LocationType>();}
LocationTypeTermIDTermIDLst.add(obj); }
public List<dbaccess.LocationType> fetchAllLocationTypeTermIDTermID() { try {
  if (LocationTypeTermIDTermIDLst != null) { return LocationTypeTermIDTermIDLst;}
  return(LocationTypeTermIDTermIDLst =
    dbaccess.bridge._LocationType.fetchMany("TermID", (long)TermID));
  } catch (NullPointerException e) { return null; }}

protected List<dbaccess.NamePrefix> NamePrefixTermIDTermIDLst;
public void addNamePrefixTermIDTermID(dbaccess.NamePrefix obj) {
  if (NamePrefixTermIDTermIDLst == null) { NamePrefixTermIDTermIDLst = new ArrayList<dbaccess.NamePrefix>();}
NamePrefixTermIDTermIDLst.add(obj); }
public List<dbaccess.NamePrefix> fetchAllNamePrefixTermIDTermID() { try {
  if (NamePrefixTermIDTermIDLst != null) { return NamePrefixTermIDTermIDLst;}
  return(NamePrefixTermIDTermIDLst =
    dbaccess.bridge._NamePrefix.fetchMany("TermID", (long)TermID));
  } catch (NullPointerException e) { return null; }}

protected List<dbaccess.NameSuffix> NameSuffixTermIDTermIDLst;
public void addNameSuffixTermIDTermID(dbaccess.NameSuffix obj) {
  if (NameSuffixTermIDTermIDLst == null) { NameSuffixTermIDTermIDLst = new ArrayList<dbaccess.NameSuffix>();}
NameSuffixTermIDTermIDLst.add(obj); }
public List<dbaccess.NameSuffix> fetchAllNameSuffixTermIDTermID() { try {
  if (NameSuffixTermIDTermIDLst != null) { return NameSuffixTermIDTermIDLst;}
  return(NameSuffixTermIDTermIDLst =
    dbaccess.bridge._NameSuffix.fetchMany("TermID", (long)TermID));
  } catch (NullPointerException e) { return null; }}

protected List<dbaccess.OrganizationType> OrganizationTypeTermIDTermIDLst;
public void addOrganizationTypeTermIDTermID(dbaccess.OrganizationType obj) {
  if (OrganizationTypeTermIDTermIDLst == null) { OrganizationTypeTermIDTermIDLst = new ArrayList<dbaccess.OrganizationType>();}
OrganizationTypeTermIDTermIDLst.add(obj); }
public List<dbaccess.OrganizationType> fetchAllOrganizationTypeTermIDTermID() { try {
  if (OrganizationTypeTermIDTermIDLst != null) { return OrganizationTypeTermIDTermIDLst;}
  return(OrganizationTypeTermIDTermIDLst =
    dbaccess.bridge._OrganizationType.fetchMany("TermID", (long)TermID));
  } catch (NullPointerException e) { return null; }}

protected List<dbaccess.PermissionType> PermissionTypeTermIDTermIDLst;
public void addPermissionTypeTermIDTermID(dbaccess.PermissionType obj) {
  if (PermissionTypeTermIDTermIDLst == null) { PermissionTypeTermIDTermIDLst = new ArrayList<dbaccess.PermissionType>();}
PermissionTypeTermIDTermIDLst.add(obj); }
public List<dbaccess.PermissionType> fetchAllPermissionTypeTermIDTermID() { try {
  if (PermissionTypeTermIDTermIDLst != null) { return PermissionTypeTermIDTermIDLst;}
  return(PermissionTypeTermIDTermIDLst =
    dbaccess.bridge._PermissionType.fetchMany("TermID", (long)TermID));
  } catch (NullPointerException e) { return null; }}

protected List<dbaccess.PhoneNumberType> PhoneNumberTypeTermIDTermIDLst;
public void addPhoneNumberTypeTermIDTermID(dbaccess.PhoneNumberType obj) {
  if (PhoneNumberTypeTermIDTermIDLst == null) { PhoneNumberTypeTermIDTermIDLst = new ArrayList<dbaccess.PhoneNumberType>();}
PhoneNumberTypeTermIDTermIDLst.add(obj); }
public List<dbaccess.PhoneNumberType> fetchAllPhoneNumberTypeTermIDTermID() { try {
  if (PhoneNumberTypeTermIDTermIDLst != null) { return PhoneNumberTypeTermIDTermIDLst;}
  return(PhoneNumberTypeTermIDTermIDLst =
    dbaccess.bridge._PhoneNumberType.fetchMany("TermID", (long)TermID));
  } catch (NullPointerException e) { return null; }}

protected List<dbaccess.SpaceType> SpaceTypeTermIDTermIDLst;
public void addSpaceTypeTermIDTermID(dbaccess.SpaceType obj) {
  if (SpaceTypeTermIDTermIDLst == null) { SpaceTypeTermIDTermIDLst = new ArrayList<dbaccess.SpaceType>();}
SpaceTypeTermIDTermIDLst.add(obj); }
public List<dbaccess.SpaceType> fetchAllSpaceTypeTermIDTermID() { try {
  if (SpaceTypeTermIDTermIDLst != null) { return SpaceTypeTermIDTermIDLst;}
  return(SpaceTypeTermIDTermIDLst =
    dbaccess.bridge._SpaceType.fetchMany("TermID", (long)TermID));
  } catch (NullPointerException e) { return null; }}

protected List<dbaccess.State> StateTermIDTermIDLst;
public void addStateTermIDTermID(dbaccess.State obj) {
  if (StateTermIDTermIDLst == null) { StateTermIDTermIDLst = new ArrayList<dbaccess.State>();}
StateTermIDTermIDLst.add(obj); }
public List<dbaccess.State> fetchAllStateTermIDTermID() { try {
  if (StateTermIDTermIDLst != null) { return StateTermIDTermIDLst;}
  return(StateTermIDTermIDLst =
    dbaccess.bridge._State.fetchMany("TermID", (long)TermID));
  } catch (NullPointerException e) { return null; }}

protected List<dbaccess.TermTranslation> TermTranslationTermIDTermIDLst;
public void addTermTranslationTermIDTermID(dbaccess.TermTranslation obj) {
  if (TermTranslationTermIDTermIDLst == null) { TermTranslationTermIDTermIDLst = new ArrayList<dbaccess.TermTranslation>();}
TermTranslationTermIDTermIDLst.add(obj); }
public List<dbaccess.TermTranslation> fetchAllTermTranslationTermIDTermID() { try {
  if (TermTranslationTermIDTermIDLst != null) { return TermTranslationTermIDTermIDLst;}
  return(TermTranslationTermIDTermIDLst =
    dbaccess.bridge._TermTranslation.fetchMany("TermID", (long)TermID));
  } catch (NullPointerException e) { return null; }}

public void cleanDirtyFlags() {
  NameChanged=false;
  TermTypeIDChanged=false;
  TermIDChanged=false;
}

public boolean testDirtyFlags() { return (
  NameChanged ||
  TermTypeIDChanged ||
  TermIDChanged);
}

public static final String primaryKey = "TermID";
public static String fetchPrimaryKey() { return primaryKey; }
public static EnglishTerm fetchByPrimaryKey(Long colValue) {
  return (EnglishTerm)BaseSQLClass.getOne(EnglishTerm.class, null, primaryKey, colValue);
}
public static boolean hasPrimaryKey() { return true; }
}
