package dbaccess.bridge;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;import java.util.ArrayList;
import zetek.dbcommon.BaseSQLClass;
import dbaccess.SpaceType;

public class _SpaceType extends BaseSQLClass implements Serializable {
  private static final long serialVersionUID=1;

  public _SpaceType() {}

public static List<SpaceType> fetchMany(String colName, Object colValue) {
return (List<SpaceType>)BaseSQLClass.getMany(SpaceType.class, null, colName, colValue);
}

public static SpaceType fetchOne(String colName, Object colValue) {
return (SpaceType)BaseSQLClass.getOne(SpaceType.class, null, colName, colValue);
}

public boolean SpaceTypeIDChanged;
/** Identifies a type */ Long SpaceTypeID;
public Long getSpaceTypeID() { return SpaceTypeID; }
public void setSpaceTypeID(Long SpaceTypeID) {
  SpaceTypeIDChanged=BaseSQLClass.twoParms(this.SpaceTypeID, SpaceTypeID);
  this.SpaceTypeID = SpaceTypeID;
}

public boolean ListOrderChanged;
/** Order the items should appear in a list */ Long ListOrder;
public Long getListOrder() { return ListOrder; }
public void setListOrder(Long ListOrder) {
  ListOrderChanged=BaseSQLClass.twoParms(this.ListOrder, ListOrder);
  this.ListOrder = ListOrder;
}

public boolean TermIDChanged;
/** Language specific name of the type */ Long TermID;
public Long getTermID() { return TermID; }
public void setTermID(Long TermID) {
  TermIDChanged=BaseSQLClass.twoParms(this.TermID, TermID);
  this.TermID = TermID;
}

// 1 to 1 references from SpaceType

protected dbaccess.EnglishTerm EnglishTermTermIDTermIDObj;
public dbaccess.EnglishTerm fetchOneEnglishTermTermIDTermID() { try {
  if (EnglishTermTermIDTermIDObj != null) { return EnglishTermTermIDTermIDObj;}
  return(EnglishTermTermIDTermIDObj =
    dbaccess.bridge._EnglishTerm.fetchOne("TermID", (long)TermID));
  } catch (NullPointerException e) { return null; }}

// 1 to many references from SpaceType

protected List<dbaccess.Space> SpaceSpaceTypeIDSpaceTypeIDLst;
public void addSpaceSpaceTypeIDSpaceTypeID(dbaccess.Space obj) {
  if (SpaceSpaceTypeIDSpaceTypeIDLst == null) { SpaceSpaceTypeIDSpaceTypeIDLst = new ArrayList<dbaccess.Space>();}
SpaceSpaceTypeIDSpaceTypeIDLst.add(obj); }
public List<dbaccess.Space> fetchAllSpaceSpaceTypeIDSpaceTypeID() { try {
  if (SpaceSpaceTypeIDSpaceTypeIDLst != null) { return SpaceSpaceTypeIDSpaceTypeIDLst;}
  return(SpaceSpaceTypeIDSpaceTypeIDLst =
    dbaccess.bridge._Space.fetchMany("SpaceTypeID", (long)SpaceTypeID));
  } catch (NullPointerException e) { return null; }}

public void cleanDirtyFlags() {
  SpaceTypeIDChanged=false;
  ListOrderChanged=false;
  TermIDChanged=false;
}

public boolean testDirtyFlags() { return (
  SpaceTypeIDChanged ||
  ListOrderChanged ||
  TermIDChanged);
}

public static final String primaryKey = "SpaceTypeID";
public static String fetchPrimaryKey() { return primaryKey; }
public static SpaceType fetchByPrimaryKey(Long colValue) {
  return (SpaceType)BaseSQLClass.getOne(SpaceType.class, null, primaryKey, colValue);
}
public static boolean hasPrimaryKey() { return true; }
}
