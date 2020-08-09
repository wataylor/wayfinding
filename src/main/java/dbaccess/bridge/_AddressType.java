package dbaccess.bridge;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;import java.util.ArrayList;
import zetek.dbcommon.BaseSQLClass;
import dbaccess.AddressType;

public class _AddressType extends BaseSQLClass implements Serializable {
  private static final long serialVersionUID=1;

  public _AddressType() {}

public static List<AddressType> fetchMany(String colName, Object colValue) {
return (List<AddressType>)BaseSQLClass.getMany(AddressType.class, null, colName, colValue);
}

public static AddressType fetchOne(String colName, Object colValue) {
return (AddressType)BaseSQLClass.getOne(AddressType.class, null, colName, colValue);
}

public boolean ListOrderChanged;
/** Order the items should appear in a pull down list for editing */ Long ListOrder;
public Long getListOrder() { return ListOrder; }
public void setListOrder(Long ListOrder) {
  ListOrderChanged=BaseSQLClass.twoParms(this.ListOrder, ListOrder);
  this.ListOrder = ListOrder;
}

public boolean AddressTypeIDChanged;
/** Used to identify a kind of address */ Long AddressTypeID;
public Long getAddressTypeID() { return AddressTypeID; }
public void setAddressTypeID(Long AddressTypeID) {
  AddressTypeIDChanged=BaseSQLClass.twoParms(this.AddressTypeID, AddressTypeID);
  this.AddressTypeID = AddressTypeID;
}

public boolean DescriptionChanged;
/** Longer description of the address type */ String Description;
public String getDescription() { return Description; }
public void setDescription(String Description) {
  DescriptionChanged=BaseSQLClass.twoParms(this.Description, Description);
  this.Description = Description;
}

public boolean TermIDChanged;
/** Address type name */ Long TermID;
public Long getTermID() { return TermID; }
public void setTermID(Long TermID) {
  TermIDChanged=BaseSQLClass.twoParms(this.TermID, TermID);
  this.TermID = TermID;
}

// 1 to 1 references from AddressType

protected dbaccess.EnglishTerm EnglishTermTermIDTermIDObj;
public dbaccess.EnglishTerm fetchOneEnglishTermTermIDTermID() { try {
  if (EnglishTermTermIDTermIDObj != null) { return EnglishTermTermIDTermIDObj;}
  return(EnglishTermTermIDTermIDObj =
    dbaccess.bridge._EnglishTerm.fetchOne("TermID", (long)TermID));
  } catch (NullPointerException e) { return null; }}

// 1 to many references from AddressType

protected List<dbaccess.EntityAddress> EntityAddressAddressTypeIDAddressTypeIDLst;
public void addEntityAddressAddressTypeIDAddressTypeID(dbaccess.EntityAddress obj) {
  if (EntityAddressAddressTypeIDAddressTypeIDLst == null) { EntityAddressAddressTypeIDAddressTypeIDLst = new ArrayList<dbaccess.EntityAddress>();}
EntityAddressAddressTypeIDAddressTypeIDLst.add(obj); }
public List<dbaccess.EntityAddress> fetchAllEntityAddressAddressTypeIDAddressTypeID() { try {
  if (EntityAddressAddressTypeIDAddressTypeIDLst != null) { return EntityAddressAddressTypeIDAddressTypeIDLst;}
  return(EntityAddressAddressTypeIDAddressTypeIDLst =
    dbaccess.bridge._EntityAddress.fetchMany("AddressTypeID", (long)AddressTypeID));
  } catch (NullPointerException e) { return null; }}

public void cleanDirtyFlags() {
  ListOrderChanged=false;
  AddressTypeIDChanged=false;
  DescriptionChanged=false;
  TermIDChanged=false;
}

public boolean testDirtyFlags() { return (
  ListOrderChanged ||
  AddressTypeIDChanged ||
  DescriptionChanged ||
  TermIDChanged);
}

public static final String primaryKey = "AddressTypeID";
public static String fetchPrimaryKey() { return primaryKey; }
public static AddressType fetchByPrimaryKey(Long colValue) {
  return (AddressType)BaseSQLClass.getOne(AddressType.class, null, primaryKey, colValue);
}
public static boolean hasPrimaryKey() { return true; }
}
