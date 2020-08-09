package dbaccess.bridge;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;import java.util.ArrayList;
import zetek.dbcommon.BaseSQLClass;
import dbaccess.PhoneNumberType;

public class _PhoneNumberType extends BaseSQLClass implements Serializable {
  private static final long serialVersionUID=1;

  public _PhoneNumberType() {}

public static List<PhoneNumberType> fetchMany(String colName, Object colValue) {
return (List<PhoneNumberType>)BaseSQLClass.getMany(PhoneNumberType.class, null, colName, colValue);
}

public static PhoneNumberType fetchOne(String colName, Object colValue) {
return (PhoneNumberType)BaseSQLClass.getOne(PhoneNumberType.class, null, colName, colValue);
}

public boolean ListOrderChanged;
/** Order the items should appear in a pull down list for editing */ Long ListOrder;
public Long getListOrder() { return ListOrder; }
public void setListOrder(Long ListOrder) {
  ListOrderChanged=BaseSQLClass.twoParms(this.ListOrder, ListOrder);
  this.ListOrder = ListOrder;
}

public boolean PhoneNumberTypeIDChanged;
/** Uniquely identifies a phone number type */ Long PhoneNumberTypeID;
public Long getPhoneNumberTypeID() { return PhoneNumberTypeID; }
public void setPhoneNumberTypeID(Long PhoneNumberTypeID) {
  PhoneNumberTypeIDChanged=BaseSQLClass.twoParms(this.PhoneNumberTypeID, PhoneNumberTypeID);
  this.PhoneNumberTypeID = PhoneNumberTypeID;
}

public boolean TermIDChanged;
/** Type name */ Long TermID;
public Long getTermID() { return TermID; }
public void setTermID(Long TermID) {
  TermIDChanged=BaseSQLClass.twoParms(this.TermID, TermID);
  this.TermID = TermID;
}

// 1 to 1 references from PhoneNumberType

protected dbaccess.EnglishTerm EnglishTermTermIDTermIDObj;
public dbaccess.EnglishTerm fetchOneEnglishTermTermIDTermID() { try {
  if (EnglishTermTermIDTermIDObj != null) { return EnglishTermTermIDTermIDObj;}
  return(EnglishTermTermIDTermIDObj =
    dbaccess.bridge._EnglishTerm.fetchOne("TermID", (long)TermID));
  } catch (NullPointerException e) { return null; }}

// 1 to many references from PhoneNumberType

protected List<dbaccess.EntityPhoneNo> EntityPhoneNoPhoneNumberTypeIDPhoneNumberTypeIDLst;
public void addEntityPhoneNoPhoneNumberTypeIDPhoneNumberTypeID(dbaccess.EntityPhoneNo obj) {
  if (EntityPhoneNoPhoneNumberTypeIDPhoneNumberTypeIDLst == null) { EntityPhoneNoPhoneNumberTypeIDPhoneNumberTypeIDLst = new ArrayList<dbaccess.EntityPhoneNo>();}
EntityPhoneNoPhoneNumberTypeIDPhoneNumberTypeIDLst.add(obj); }
public List<dbaccess.EntityPhoneNo> fetchAllEntityPhoneNoPhoneNumberTypeIDPhoneNumberTypeID() { try {
  if (EntityPhoneNoPhoneNumberTypeIDPhoneNumberTypeIDLst != null) { return EntityPhoneNoPhoneNumberTypeIDPhoneNumberTypeIDLst;}
  return(EntityPhoneNoPhoneNumberTypeIDPhoneNumberTypeIDLst =
    dbaccess.bridge._EntityPhoneNo.fetchMany("PhoneNumberTypeID", (long)PhoneNumberTypeID));
  } catch (NullPointerException e) { return null; }}

public void cleanDirtyFlags() {
  ListOrderChanged=false;
  PhoneNumberTypeIDChanged=false;
  TermIDChanged=false;
}

public boolean testDirtyFlags() { return (
  ListOrderChanged ||
  PhoneNumberTypeIDChanged ||
  TermIDChanged);
}

public static final String primaryKey = "PhoneNumberTypeID";
public static String fetchPrimaryKey() { return primaryKey; }
public static PhoneNumberType fetchByPrimaryKey(Long colValue) {
  return (PhoneNumberType)BaseSQLClass.getOne(PhoneNumberType.class, null, primaryKey, colValue);
}
public static boolean hasPrimaryKey() { return true; }
}
