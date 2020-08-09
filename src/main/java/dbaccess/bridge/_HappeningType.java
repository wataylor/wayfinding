package dbaccess.bridge;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;import java.util.ArrayList;
import zetek.dbcommon.BaseSQLClass;
import dbaccess.HappeningType;

public class _HappeningType extends BaseSQLClass implements Serializable {
  private static final long serialVersionUID=1;

  public _HappeningType() {}

public static List<HappeningType> fetchMany(String colName, Object colValue) {
return (List<HappeningType>)BaseSQLClass.getMany(HappeningType.class, null, colName, colValue);
}

public static HappeningType fetchOne(String colName, Object colValue) {
return (HappeningType)BaseSQLClass.getOne(HappeningType.class, null, colName, colValue);
}

public boolean ListOrderChanged;
/** Order the items should appear in a pull down list for editing */ Long ListOrder;
public Long getListOrder() { return ListOrder; }
public void setListOrder(Long ListOrder) {
  ListOrderChanged=BaseSQLClass.twoParms(this.ListOrder, ListOrder);
  this.ListOrder = ListOrder;
}

public boolean TermIDChanged;
/** Language specific name of the happening type */ Long TermID;
public Long getTermID() { return TermID; }
public void setTermID(Long TermID) {
  TermIDChanged=BaseSQLClass.twoParms(this.TermID, TermID);
  this.TermID = TermID;
}

public boolean HappeningTypeIDChanged;
/** Identifies an happening type */ Long HappeningTypeID;
public Long getHappeningTypeID() { return HappeningTypeID; }
public void setHappeningTypeID(Long HappeningTypeID) {
  HappeningTypeIDChanged=BaseSQLClass.twoParms(this.HappeningTypeID, HappeningTypeID);
  this.HappeningTypeID = HappeningTypeID;
}

// 1 to 1 references from HappeningType

protected dbaccess.EnglishTerm EnglishTermTermIDTermIDObj;
public dbaccess.EnglishTerm fetchOneEnglishTermTermIDTermID() { try {
  if (EnglishTermTermIDTermIDObj != null) { return EnglishTermTermIDTermIDObj;}
  return(EnglishTermTermIDTermIDObj =
    dbaccess.bridge._EnglishTerm.fetchOne("TermID", (long)TermID));
  } catch (NullPointerException e) { return null; }}

// 1 to many references from HappeningType

protected List<dbaccess.Happening> HappeningHappeningTypeIDHappeningTypeIDLst;
public void addHappeningHappeningTypeIDHappeningTypeID(dbaccess.Happening obj) {
  if (HappeningHappeningTypeIDHappeningTypeIDLst == null) { HappeningHappeningTypeIDHappeningTypeIDLst = new ArrayList<dbaccess.Happening>();}
HappeningHappeningTypeIDHappeningTypeIDLst.add(obj); }
public List<dbaccess.Happening> fetchAllHappeningHappeningTypeIDHappeningTypeID() { try {
  if (HappeningHappeningTypeIDHappeningTypeIDLst != null) { return HappeningHappeningTypeIDHappeningTypeIDLst;}
  return(HappeningHappeningTypeIDHappeningTypeIDLst =
    dbaccess.bridge._Happening.fetchMany("HappeningTypeID", (long)HappeningTypeID));
  } catch (NullPointerException e) { return null; }}

public void cleanDirtyFlags() {
  ListOrderChanged=false;
  TermIDChanged=false;
  HappeningTypeIDChanged=false;
}

public boolean testDirtyFlags() { return (
  ListOrderChanged ||
  TermIDChanged ||
  HappeningTypeIDChanged);
}

public static final String primaryKey = "HappeningTypeID";
public static String fetchPrimaryKey() { return primaryKey; }
public static HappeningType fetchByPrimaryKey(Long colValue) {
  return (HappeningType)BaseSQLClass.getOne(HappeningType.class, null, primaryKey, colValue);
}
public static boolean hasPrimaryKey() { return true; }
}
