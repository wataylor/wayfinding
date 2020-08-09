package dbaccess.bridge;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;import java.util.ArrayList;
import zetek.dbcommon.BaseSQLClass;
import dbaccess.Address;

public class _Address extends BaseSQLClass implements Serializable {
  private static final long serialVersionUID=1;

  public _Address() {}

public static List<Address> fetchMany(String colName, Object colValue) {
return (List<Address>)BaseSQLClass.getMany(Address.class, null, colName, colValue);
}

public static Address fetchOne(String colName, Object colValue) {
return (Address)BaseSQLClass.getOne(Address.class, null, colName, colValue);
}

public boolean AddressIDChanged;
/** Identifies a particular address uniquely */ Long AddressID;
public Long getAddressID() { return AddressID; }
public void setAddressID(Long AddressID) {
  AddressIDChanged=BaseSQLClass.twoParms(this.AddressID, AddressID);
  this.AddressID = AddressID;
}

public boolean CountryIDChanged;
/** Country the address is located in */ Long CountryID;
public Long getCountryID() { return CountryID; }
public void setCountryID(Long CountryID) {
  CountryIDChanged=BaseSQLClass.twoParms(this.CountryID, CountryID);
  this.CountryID = CountryID;
}

public boolean StateIDChanged;
/** The state, province, or territory */ Long StateID;
public Long getStateID() { return StateID; }
public void setStateID(Long StateID) {
  StateIDChanged=BaseSQLClass.twoParms(this.StateID, StateID);
  this.StateID = StateID;
}

public boolean RetractedChanged;
/** non-null means address retracted */ Timestamp Retracted;
public Timestamp getRetracted() { return Retracted; }
public void setRetracted(Timestamp Retracted) {
  RetractedChanged=BaseSQLClass.twoParms(this.Retracted, Retracted);
  this.Retracted = Retracted;
}

public boolean CityChanged;
/** City/county the address is located in */ String City;
public String getCity() { return City; }
public void setCity(String City) {
  CityChanged=BaseSQLClass.twoParms(this.City, City);
  this.City = City;
}

public boolean Address3Changed;
/** Usually the street name and number */ String Address3;
public String getAddress3() { return Address3; }
public void setAddress3(String Address3) {
  Address3Changed=BaseSQLClass.twoParms(this.Address3, Address3);
  this.Address3 = Address3;
}

public boolean Address2Changed;
/** Usually the suite number */ String Address2;
public String getAddress2() { return Address2; }
public void setAddress2(String Address2) {
  Address2Changed=BaseSQLClass.twoParms(this.Address2, Address2);
  this.Address2 = Address2;
}

public boolean Address1Changed;
/** Usually the person or company name */ String Address1;
public String getAddress1() { return Address1; }
public void setAddress1(String Address1) {
  Address1Changed=BaseSQLClass.twoParms(this.Address1, Address1);
  this.Address1 = Address1;
}

public boolean PostalCodeChanged;
/** Postal code or zip code. */ String PostalCode;
public String getPostalCode() { return PostalCode; }
public void setPostalCode(String PostalCode) {
  PostalCodeChanged=BaseSQLClass.twoParms(this.PostalCode, PostalCode);
  this.PostalCode = PostalCode;
}

// 1 to 1 references from Address

protected dbaccess.Entity EntityEntityIDAddressIDObj;
public dbaccess.Entity fetchOneEntityEntityIDAddressID() { try {
  if (EntityEntityIDAddressIDObj != null) { return EntityEntityIDAddressIDObj;}
  return(EntityEntityIDAddressIDObj =
    dbaccess.bridge._Entity.fetchOne("EntityID", (long)AddressID));
  } catch (NullPointerException e) { return null; }}

protected dbaccess.Country CountryCountryIDCountryIDObj;
public dbaccess.Country fetchOneCountryCountryIDCountryID() { try {
  if (CountryCountryIDCountryIDObj != null) { return CountryCountryIDCountryIDObj;}
  return(CountryCountryIDCountryIDObj =
    dbaccess.bridge._Country.fetchOne("CountryID", (long)CountryID));
  } catch (NullPointerException e) { return null; }}

protected dbaccess.State StateStateIDStateIDObj;
public dbaccess.State fetchOneStateStateIDStateID() { try {
  if (StateStateIDStateIDObj != null) { return StateStateIDStateIDObj;}
  return(StateStateIDStateIDObj =
    dbaccess.bridge._State.fetchOne("StateID", (long)StateID));
  } catch (NullPointerException e) { return null; }}

// 1 to many references from Address

protected List<dbaccess.EntityAddress> EntityAddressAddressIDAddressIDLst;
public void addEntityAddressAddressIDAddressID(dbaccess.EntityAddress obj) {
  if (EntityAddressAddressIDAddressIDLst == null) { EntityAddressAddressIDAddressIDLst = new ArrayList<dbaccess.EntityAddress>();}
EntityAddressAddressIDAddressIDLst.add(obj); }
public List<dbaccess.EntityAddress> fetchAllEntityAddressAddressIDAddressID() { try {
  if (EntityAddressAddressIDAddressIDLst != null) { return EntityAddressAddressIDAddressIDLst;}
  return(EntityAddressAddressIDAddressIDLst =
    dbaccess.bridge._EntityAddress.fetchMany("AddressID", (long)AddressID));
  } catch (NullPointerException e) { return null; }}

public void cleanDirtyFlags() {
  AddressIDChanged=false;
  CountryIDChanged=false;
  StateIDChanged=false;
  RetractedChanged=false;
  CityChanged=false;
  Address3Changed=false;
  Address2Changed=false;
  Address1Changed=false;
  PostalCodeChanged=false;
}

public boolean testDirtyFlags() { return (
  AddressIDChanged ||
  CountryIDChanged ||
  StateIDChanged ||
  RetractedChanged ||
  CityChanged ||
  Address3Changed ||
  Address2Changed ||
  Address1Changed ||
  PostalCodeChanged);
}

public static final String primaryKey = "AddressID";
public static String fetchPrimaryKey() { return primaryKey; }
public static Address fetchByPrimaryKey(Long colValue) {
  return (Address)BaseSQLClass.getOne(Address.class, null, primaryKey, colValue);
}
public static boolean hasPrimaryKey() { return true; }
}
