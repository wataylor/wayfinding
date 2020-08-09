package dbaccess.bridge;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;import java.util.ArrayList;
import zetek.dbcommon.BaseSQLClass;
import dbaccess.TermType;

public class _TermType extends BaseSQLClass implements Serializable {
  private static final long serialVersionUID=1;

  public _TermType() {}

public static List<TermType> fetchMany(String colName, Object colValue) {
return (List<TermType>)BaseSQLClass.getMany(TermType.class, null, colName, colValue);
}

public static TermType fetchOne(String colName, Object colValue) {
return (TermType)BaseSQLClass.getOne(TermType.class, null, colName, colValue);
}

public boolean TermTypeIDChanged;
/** Used to identify a kind of address */ Long TermTypeID;
public Long getTermTypeID() { return TermTypeID; }
public void setTermTypeID(Long TermTypeID) {
  TermTypeIDChanged=BaseSQLClass.twoParms(this.TermTypeID, TermTypeID);
  this.TermTypeID = TermTypeID;
}

public boolean ListOrderChanged;
/** Order the items should appear in a pull down list for editing */ Long ListOrder;
public Long getListOrder() { return ListOrder; }
public void setListOrder(Long ListOrder) {
  ListOrderChanged=BaseSQLClass.twoParms(this.ListOrder, ListOrder);
  this.ListOrder = ListOrder;
}

public boolean TermIDChanged;
/** Term type name */ Long TermID;
public Long getTermID() { return TermID; }
public void setTermID(Long TermID) {
  TermIDChanged=BaseSQLClass.twoParms(this.TermID, TermID);
  this.TermID = TermID;
}

// 1 to 1 references from TermType

// 1 to many references from TermType

protected List<dbaccess.EnglishTerm> EnglishTermTermTypeIDTermTypeIDLst;
public void addEnglishTermTermTypeIDTermTypeID(dbaccess.EnglishTerm obj) {
  if (EnglishTermTermTypeIDTermTypeIDLst == null) { EnglishTermTermTypeIDTermTypeIDLst = new ArrayList<dbaccess.EnglishTerm>();}
EnglishTermTermTypeIDTermTypeIDLst.add(obj); }
public List<dbaccess.EnglishTerm> fetchAllEnglishTermTermTypeIDTermTypeID() { try {
  if (EnglishTermTermTypeIDTermTypeIDLst != null) { return EnglishTermTermTypeIDTermTypeIDLst;}
  return(EnglishTermTermTypeIDTermTypeIDLst =
    dbaccess.bridge._EnglishTerm.fetchMany("TermTypeID", (long)TermTypeID));
  } catch (NullPointerException e) { return null; }}

public void cleanDirtyFlags() {
  TermTypeIDChanged=false;
  ListOrderChanged=false;
  TermIDChanged=false;
}

public boolean testDirtyFlags() { return (
  TermTypeIDChanged ||
  ListOrderChanged ||
  TermIDChanged);
}

public static final String primaryKey = "TermTypeID";
public static String fetchPrimaryKey() { return primaryKey; }
public static TermType fetchByPrimaryKey(Long colValue) {
  return (TermType)BaseSQLClass.getOne(TermType.class, null, primaryKey, colValue);
}
public static boolean hasPrimaryKey() { return true; }
}
