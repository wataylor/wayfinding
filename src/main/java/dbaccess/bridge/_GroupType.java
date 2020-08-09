package dbaccess.bridge;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;import java.util.ArrayList;
import zetek.dbcommon.BaseSQLClass;
import dbaccess.GroupType;

public class _GroupType extends BaseSQLClass implements Serializable {
  private static final long serialVersionUID=1;

  public _GroupType() {}

public static List<GroupType> fetchMany(String colName, Object colValue) {
return (List<GroupType>)BaseSQLClass.getMany(GroupType.class, null, colName, colValue);
}

public static GroupType fetchOne(String colName, Object colValue) {
return (GroupType)BaseSQLClass.getOne(GroupType.class, null, colName, colValue);
}

public boolean GroupTypeIDChanged;
/** Identifies a group type */ Long GroupTypeID;
public Long getGroupTypeID() { return GroupTypeID; }
public void setGroupTypeID(Long GroupTypeID) {
  GroupTypeIDChanged=BaseSQLClass.twoParms(this.GroupTypeID, GroupTypeID);
  this.GroupTypeID = GroupTypeID;
}

public boolean ListOrderChanged;
/** Order the items should appear in a pull down list for editing */ Long ListOrder;
public Long getListOrder() { return ListOrder; }
public void setListOrder(Long ListOrder) {
  ListOrderChanged=BaseSQLClass.twoParms(this.ListOrder, ListOrder);
  this.ListOrder = ListOrder;
}

public boolean TermIDChanged;
/** Language specific name of the group */ Long TermID;
public Long getTermID() { return TermID; }
public void setTermID(Long TermID) {
  TermIDChanged=BaseSQLClass.twoParms(this.TermID, TermID);
  this.TermID = TermID;
}

// 1 to 1 references from GroupType

protected dbaccess.EnglishTerm EnglishTermTermIDTermIDObj;
public dbaccess.EnglishTerm fetchOneEnglishTermTermIDTermID() { try {
  if (EnglishTermTermIDTermIDObj != null) { return EnglishTermTermIDTermIDObj;}
  return(EnglishTermTermIDTermIDObj =
    dbaccess.bridge._EnglishTerm.fetchOne("TermID", (long)TermID));
  } catch (NullPointerException e) { return null; }}

// 1 to many references from GroupType

protected List<dbaccess.BuildingGroup> BuildingGroupGroupTypeIDGroupTypeIDLst;
public void addBuildingGroupGroupTypeIDGroupTypeID(dbaccess.BuildingGroup obj) {
  if (BuildingGroupGroupTypeIDGroupTypeIDLst == null) { BuildingGroupGroupTypeIDGroupTypeIDLst = new ArrayList<dbaccess.BuildingGroup>();}
BuildingGroupGroupTypeIDGroupTypeIDLst.add(obj); }
public List<dbaccess.BuildingGroup> fetchAllBuildingGroupGroupTypeIDGroupTypeID() { try {
  if (BuildingGroupGroupTypeIDGroupTypeIDLst != null) { return BuildingGroupGroupTypeIDGroupTypeIDLst;}
  return(BuildingGroupGroupTypeIDGroupTypeIDLst =
    dbaccess.bridge._BuildingGroup.fetchMany("GroupTypeID", (long)GroupTypeID));
  } catch (NullPointerException e) { return null; }}

public void cleanDirtyFlags() {
  GroupTypeIDChanged=false;
  ListOrderChanged=false;
  TermIDChanged=false;
}

public boolean testDirtyFlags() { return (
  GroupTypeIDChanged ||
  ListOrderChanged ||
  TermIDChanged);
}

public static final String primaryKey = "GroupTypeID";
public static String fetchPrimaryKey() { return primaryKey; }
public static GroupType fetchByPrimaryKey(Long colValue) {
  return (GroupType)BaseSQLClass.getOne(GroupType.class, null, primaryKey, colValue);
}
public static boolean hasPrimaryKey() { return true; }
}
