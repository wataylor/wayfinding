package dbaccess.bridge;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;import java.util.ArrayList;
import zetek.dbcommon.BaseSQLClass;
import dbaccess.NamePrefix;

public class _NamePrefix extends BaseSQLClass implements Serializable {
  private static final long serialVersionUID=1;

  public _NamePrefix() {}

public static List<NamePrefix> fetchMany(String colName, Object colValue) {
return (List<NamePrefix>)BaseSQLClass.getMany(NamePrefix.class, null, colName, colValue);
}

public static NamePrefix fetchOne(String colName, Object colValue) {
return (NamePrefix)BaseSQLClass.getOne(NamePrefix.class, null, colName, colValue);
}

public boolean ListOrderChanged;
/** Order the items should appear in a sorted select list */ Long ListOrder;
public Long getListOrder() { return ListOrder; }
public void setListOrder(Long ListOrder) {
  ListOrderChanged=BaseSQLClass.twoParms(this.ListOrder, ListOrder);
  this.ListOrder = ListOrder;
}

public boolean TermIDChanged;
/** Prefix name */ Long TermID;
public Long getTermID() { return TermID; }
public void setTermID(Long TermID) {
  TermIDChanged=BaseSQLClass.twoParms(this.TermID, TermID);
  this.TermID = TermID;
}

public boolean PrefixIDChanged;
/** Id of the prefix */ Long PrefixID;
public Long getPrefixID() { return PrefixID; }
public void setPrefixID(Long PrefixID) {
  PrefixIDChanged=BaseSQLClass.twoParms(this.PrefixID, PrefixID);
  this.PrefixID = PrefixID;
}

// 1 to 1 references from NamePrefix

protected dbaccess.EnglishTerm EnglishTermTermIDTermIDObj;
public dbaccess.EnglishTerm fetchOneEnglishTermTermIDTermID() { try {
  if (EnglishTermTermIDTermIDObj != null) { return EnglishTermTermIDTermIDObj;}
  return(EnglishTermTermIDTermIDObj =
    dbaccess.bridge._EnglishTerm.fetchOne("TermID", (long)TermID));
  } catch (NullPointerException e) { return null; }}

// 1 to many references from NamePrefix

public void cleanDirtyFlags() {
  ListOrderChanged=false;
  TermIDChanged=false;
  PrefixIDChanged=false;
}

public boolean testDirtyFlags() { return (
  ListOrderChanged ||
  TermIDChanged ||
  PrefixIDChanged);
}

public static final String primaryKey = "PrefixID";
public static String fetchPrimaryKey() { return primaryKey; }
public static NamePrefix fetchByPrimaryKey(Long colValue) {
  return (NamePrefix)BaseSQLClass.getOne(NamePrefix.class, null, primaryKey, colValue);
}
public static boolean hasPrimaryKey() { return true; }
}
