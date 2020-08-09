package dbaccess.bridge;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;import java.util.ArrayList;
import zetek.dbcommon.BaseSQLClass;
import dbaccess.PermissionType;

public class _PermissionType extends BaseSQLClass implements Serializable {
  private static final long serialVersionUID=1;

  public _PermissionType() {}

public static List<PermissionType> fetchMany(String colName, Object colValue) {
return (List<PermissionType>)BaseSQLClass.getMany(PermissionType.class, null, colName, colValue);
}

public static PermissionType fetchOne(String colName, Object colValue) {
return (PermissionType)BaseSQLClass.getOne(PermissionType.class, null, colName, colValue);
}

public boolean ListOrderChanged;
/** Order the items should appear in a pull down list for editing */ Long ListOrder;
public Long getListOrder() { return ListOrder; }
public void setListOrder(Long ListOrder) {
  ListOrderChanged=BaseSQLClass.twoParms(this.ListOrder, ListOrder);
  this.ListOrder = ListOrder;
}

public boolean TermIDChanged;
/** Language specific name of the permission */ Long TermID;
public Long getTermID() { return TermID; }
public void setTermID(Long TermID) {
  TermIDChanged=BaseSQLClass.twoParms(this.TermID, TermID);
  this.TermID = TermID;
}

public boolean PermissionTypeIDChanged;
/** Identifies a permission */ Long PermissionTypeID;
public Long getPermissionTypeID() { return PermissionTypeID; }
public void setPermissionTypeID(Long PermissionTypeID) {
  PermissionTypeIDChanged=BaseSQLClass.twoParms(this.PermissionTypeID, PermissionTypeID);
  this.PermissionTypeID = PermissionTypeID;
}

// 1 to 1 references from PermissionType

protected dbaccess.EnglishTerm EnglishTermTermIDTermIDObj;
public dbaccess.EnglishTerm fetchOneEnglishTermTermIDTermID() { try {
  if (EnglishTermTermIDTermIDObj != null) { return EnglishTermTermIDTermIDObj;}
  return(EnglishTermTermIDTermIDObj =
    dbaccess.bridge._EnglishTerm.fetchOne("TermID", (long)TermID));
  } catch (NullPointerException e) { return null; }}

// 1 to many references from PermissionType

protected List<dbaccess.Permission> PermissionPermissionTypeIDPermissionTypeIDLst;
public void addPermissionPermissionTypeIDPermissionTypeID(dbaccess.Permission obj) {
  if (PermissionPermissionTypeIDPermissionTypeIDLst == null) { PermissionPermissionTypeIDPermissionTypeIDLst = new ArrayList<dbaccess.Permission>();}
PermissionPermissionTypeIDPermissionTypeIDLst.add(obj); }
public List<dbaccess.Permission> fetchAllPermissionPermissionTypeIDPermissionTypeID() { try {
  if (PermissionPermissionTypeIDPermissionTypeIDLst != null) { return PermissionPermissionTypeIDPermissionTypeIDLst;}
  return(PermissionPermissionTypeIDPermissionTypeIDLst =
    dbaccess.bridge._Permission.fetchMany("PermissionTypeID", (long)PermissionTypeID));
  } catch (NullPointerException e) { return null; }}

public void cleanDirtyFlags() {
  ListOrderChanged=false;
  TermIDChanged=false;
  PermissionTypeIDChanged=false;
}

public boolean testDirtyFlags() { return (
  ListOrderChanged ||
  TermIDChanged ||
  PermissionTypeIDChanged);
}

public static final String primaryKey = "PermissionTypeID";
public static String fetchPrimaryKey() { return primaryKey; }
public static PermissionType fetchByPrimaryKey(Long colValue) {
  return (PermissionType)BaseSQLClass.getOne(PermissionType.class, null, primaryKey, colValue);
}
public static boolean hasPrimaryKey() { return true; }
}
