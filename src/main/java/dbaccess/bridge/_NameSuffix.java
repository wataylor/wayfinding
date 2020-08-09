package dbaccess.bridge;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;import java.util.ArrayList;
import zetek.dbcommon.BaseSQLClass;
import dbaccess.NameSuffix;

public class _NameSuffix extends BaseSQLClass implements Serializable {
  private static final long serialVersionUID=1;

  public _NameSuffix() {}

public static List<NameSuffix> fetchMany(String colName, Object colValue) {
return (List<NameSuffix>)BaseSQLClass.getMany(NameSuffix.class, null, colName, colValue);
}

public static NameSuffix fetchOne(String colName, Object colValue) {
return (NameSuffix)BaseSQLClass.getOne(NameSuffix.class, null, colName, colValue);
}

public boolean ListOrderChanged;
/** Order the items should appear in a list */ Long ListOrder;
public Long getListOrder() { return ListOrder; }
public void setListOrder(Long ListOrder) {
  ListOrderChanged=BaseSQLClass.twoParms(this.ListOrder, ListOrder);
  this.ListOrder = ListOrder;
}

public boolean SuffixIDChanged;
/** Id of the suffix */ Long SuffixID;
public Long getSuffixID() { return SuffixID; }
public void setSuffixID(Long SuffixID) {
  SuffixIDChanged=BaseSQLClass.twoParms(this.SuffixID, SuffixID);
  this.SuffixID = SuffixID;
}

public boolean TermIDChanged;
/** Suffix name */ Long TermID;
public Long getTermID() { return TermID; }
public void setTermID(Long TermID) {
  TermIDChanged=BaseSQLClass.twoParms(this.TermID, TermID);
  this.TermID = TermID;
}

// 1 to 1 references from NameSuffix

protected dbaccess.EnglishTerm EnglishTermTermIDTermIDObj;
public dbaccess.EnglishTerm fetchOneEnglishTermTermIDTermID() { try {
  if (EnglishTermTermIDTermIDObj != null) { return EnglishTermTermIDTermIDObj;}
  return(EnglishTermTermIDTermIDObj =
    dbaccess.bridge._EnglishTerm.fetchOne("TermID", (long)TermID));
  } catch (NullPointerException e) { return null; }}

// 1 to many references from NameSuffix

public void cleanDirtyFlags() {
  ListOrderChanged=false;
  SuffixIDChanged=false;
  TermIDChanged=false;
}

public boolean testDirtyFlags() { return (
  ListOrderChanged ||
  SuffixIDChanged ||
  TermIDChanged);
}

public static final String primaryKey = "SuffixID";
public static String fetchPrimaryKey() { return primaryKey; }
public static NameSuffix fetchByPrimaryKey(Long colValue) {
  return (NameSuffix)BaseSQLClass.getOne(NameSuffix.class, null, primaryKey, colValue);
}
public static boolean hasPrimaryKey() { return true; }
}
