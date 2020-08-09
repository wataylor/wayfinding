package dbaccess.bridge;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;import java.util.ArrayList;
import zetek.dbcommon.BaseSQLClass;
import dbaccess.AttributeType;

public class _AttributeType extends BaseSQLClass implements Serializable {
  private static final long serialVersionUID=1;

  public _AttributeType() {}

public static List<AttributeType> fetchMany(String colName, Object colValue) {
return (List<AttributeType>)BaseSQLClass.getMany(AttributeType.class, null, colName, colValue);
}

public static AttributeType fetchOne(String colName, Object colValue) {
return (AttributeType)BaseSQLClass.getOne(AttributeType.class, null, colName, colValue);
}

public boolean ListOrderChanged;
/** Order the items should appear in a list */ Long ListOrder;
public Long getListOrder() { return ListOrder; }
public void setListOrder(Long ListOrder) {
  ListOrderChanged=BaseSQLClass.twoParms(this.ListOrder, ListOrder);
  this.ListOrder = ListOrder;
}

public boolean AttributeTypeIDChanged;
/** Identifies a alias */ Long AttributeTypeID;
public Long getAttributeTypeID() { return AttributeTypeID; }
public void setAttributeTypeID(Long AttributeTypeID) {
  AttributeTypeIDChanged=BaseSQLClass.twoParms(this.AttributeTypeID, AttributeTypeID);
  this.AttributeTypeID = AttributeTypeID;
}

public boolean TermIDChanged;
/** Language specific name of the alias */ Long TermID;
public Long getTermID() { return TermID; }
public void setTermID(Long TermID) {
  TermIDChanged=BaseSQLClass.twoParms(this.TermID, TermID);
  this.TermID = TermID;
}

// 1 to 1 references from AttributeType

protected dbaccess.EnglishTerm EnglishTermTermIDTermIDObj;
public dbaccess.EnglishTerm fetchOneEnglishTermTermIDTermID() { try {
  if (EnglishTermTermIDTermIDObj != null) { return EnglishTermTermIDTermIDObj;}
  return(EnglishTermTermIDTermIDObj =
    dbaccess.bridge._EnglishTerm.fetchOne("TermID", (long)TermID));
  } catch (NullPointerException e) { return null; }}

// 1 to many references from AttributeType

protected List<dbaccess.Attribute> AttributeAttributeTypeIDAttributeTypeIDLst;
public void addAttributeAttributeTypeIDAttributeTypeID(dbaccess.Attribute obj) {
  if (AttributeAttributeTypeIDAttributeTypeIDLst == null) { AttributeAttributeTypeIDAttributeTypeIDLst = new ArrayList<dbaccess.Attribute>();}
AttributeAttributeTypeIDAttributeTypeIDLst.add(obj); }
public List<dbaccess.Attribute> fetchAllAttributeAttributeTypeIDAttributeTypeID() { try {
  if (AttributeAttributeTypeIDAttributeTypeIDLst != null) { return AttributeAttributeTypeIDAttributeTypeIDLst;}
  return(AttributeAttributeTypeIDAttributeTypeIDLst =
    dbaccess.bridge._Attribute.fetchMany("AttributeTypeID", (long)AttributeTypeID));
  } catch (NullPointerException e) { return null; }}

public void cleanDirtyFlags() {
  ListOrderChanged=false;
  AttributeTypeIDChanged=false;
  TermIDChanged=false;
}

public boolean testDirtyFlags() { return (
  ListOrderChanged ||
  AttributeTypeIDChanged ||
  TermIDChanged);
}

public static final String primaryKey = "AttributeTypeID";
public static String fetchPrimaryKey() { return primaryKey; }
public static AttributeType fetchByPrimaryKey(Long colValue) {
  return (AttributeType)BaseSQLClass.getOne(AttributeType.class, null, primaryKey, colValue);
}
public static boolean hasPrimaryKey() { return true; }
}
