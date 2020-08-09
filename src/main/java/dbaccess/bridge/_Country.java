package dbaccess.bridge;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;import java.util.ArrayList;
import zetek.dbcommon.BaseSQLClass;
import dbaccess.Country;

public class _Country extends BaseSQLClass implements Serializable {
  private static final long serialVersionUID=1;

  public _Country() {}

public static List<Country> fetchMany(String colName, Object colValue) {
return (List<Country>)BaseSQLClass.getMany(Country.class, null, colName, colValue);
}

public static Country fetchOne(String colName, Object colValue) {
return (Country)BaseSQLClass.getOne(Country.class, null, colName, colValue);
}

public boolean ListOrderChanged;
/** Order the items should appear in a list */ Long ListOrder;
public Long getListOrder() { return ListOrder; }
public void setListOrder(Long ListOrder) {
  ListOrderChanged=BaseSQLClass.twoParms(this.ListOrder, ListOrder);
  this.ListOrder = ListOrder;
}

public boolean CountryIDChanged;
/** Identifies the country with its name */ Long CountryID;
public Long getCountryID() { return CountryID; }
public void setCountryID(Long CountryID) {
  CountryIDChanged=BaseSQLClass.twoParms(this.CountryID, CountryID);
  this.CountryID = CountryID;
}

public boolean TermIDChanged;
/** Country name */ Long TermID;
public Long getTermID() { return TermID; }
public void setTermID(Long TermID) {
  TermIDChanged=BaseSQLClass.twoParms(this.TermID, TermID);
  this.TermID = TermID;
}

// 1 to 1 references from Country

protected dbaccess.EnglishTerm EnglishTermTermIDTermIDObj;
public dbaccess.EnglishTerm fetchOneEnglishTermTermIDTermID() { try {
  if (EnglishTermTermIDTermIDObj != null) { return EnglishTermTermIDTermIDObj;}
  return(EnglishTermTermIDTermIDObj =
    dbaccess.bridge._EnglishTerm.fetchOne("TermID", (long)TermID));
  } catch (NullPointerException e) { return null; }}

// 1 to many references from Country

protected List<dbaccess.Address> AddressCountryIDCountryIDLst;
public void addAddressCountryIDCountryID(dbaccess.Address obj) {
  if (AddressCountryIDCountryIDLst == null) { AddressCountryIDCountryIDLst = new ArrayList<dbaccess.Address>();}
AddressCountryIDCountryIDLst.add(obj); }
public List<dbaccess.Address> fetchAllAddressCountryIDCountryID() { try {
  if (AddressCountryIDCountryIDLst != null) { return AddressCountryIDCountryIDLst;}
  return(AddressCountryIDCountryIDLst =
    dbaccess.bridge._Address.fetchMany("CountryID", (long)CountryID));
  } catch (NullPointerException e) { return null; }}

protected List<dbaccess.EntityPhoneNo> EntityPhoneNoCountryIDCountryIDLst;
public void addEntityPhoneNoCountryIDCountryID(dbaccess.EntityPhoneNo obj) {
  if (EntityPhoneNoCountryIDCountryIDLst == null) { EntityPhoneNoCountryIDCountryIDLst = new ArrayList<dbaccess.EntityPhoneNo>();}
EntityPhoneNoCountryIDCountryIDLst.add(obj); }
public List<dbaccess.EntityPhoneNo> fetchAllEntityPhoneNoCountryIDCountryID() { try {
  if (EntityPhoneNoCountryIDCountryIDLst != null) { return EntityPhoneNoCountryIDCountryIDLst;}
  return(EntityPhoneNoCountryIDCountryIDLst =
    dbaccess.bridge._EntityPhoneNo.fetchMany("CountryID", (long)CountryID));
  } catch (NullPointerException e) { return null; }}

public void cleanDirtyFlags() {
  ListOrderChanged=false;
  CountryIDChanged=false;
  TermIDChanged=false;
}

public boolean testDirtyFlags() { return (
  ListOrderChanged ||
  CountryIDChanged ||
  TermIDChanged);
}

public static final String primaryKey = "CountryID";
public static String fetchPrimaryKey() { return primaryKey; }
public static Country fetchByPrimaryKey(Long colValue) {
  return (Country)BaseSQLClass.getOne(Country.class, null, primaryKey, colValue);
}
public static boolean hasPrimaryKey() { return true; }
}
