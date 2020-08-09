package dbaccess.bridge;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;import java.util.ArrayList;
import zetek.dbcommon.BaseSQLClass;
import dbaccess.EntityType;

public class _EntityType extends BaseSQLClass implements Serializable {
  private static final long serialVersionUID=1;

  public _EntityType() {}

public static List<EntityType> fetchMany(String colName, Object colValue) {
return (List<EntityType>)BaseSQLClass.getMany(EntityType.class, null, colName, colValue);
}

public static EntityType fetchOne(String colName, Object colValue) {
return (EntityType)BaseSQLClass.getOne(EntityType.class, null, colName, colValue);
}

public boolean ListOrderChanged;
/** Order the items should appear in a list */ Long ListOrder;
public Long getListOrder() { return ListOrder; }
public void setListOrder(Long ListOrder) {
  ListOrderChanged=BaseSQLClass.twoParms(this.ListOrder, ListOrder);
  this.ListOrder = ListOrder;
}

public boolean EntityTypeIDChanged;
/** Identifies an entity type */ Long EntityTypeID;
public Long getEntityTypeID() { return EntityTypeID; }
public void setEntityTypeID(Long EntityTypeID) {
  EntityTypeIDChanged=BaseSQLClass.twoParms(this.EntityTypeID, EntityTypeID);
  this.EntityTypeID = EntityTypeID;
}

public boolean TermIDChanged;
/** Language specific name of the entity */ Long TermID;
public Long getTermID() { return TermID; }
public void setTermID(Long TermID) {
  TermIDChanged=BaseSQLClass.twoParms(this.TermID, TermID);
  this.TermID = TermID;
}

// 1 to 1 references from EntityType

protected dbaccess.EnglishTerm EnglishTermTermIDTermIDObj;
public dbaccess.EnglishTerm fetchOneEnglishTermTermIDTermID() { try {
  if (EnglishTermTermIDTermIDObj != null) { return EnglishTermTermIDTermIDObj;}
  return(EnglishTermTermIDTermIDObj =
    dbaccess.bridge._EnglishTerm.fetchOne("TermID", (long)TermID));
  } catch (NullPointerException e) { return null; }}

// 1 to many references from EntityType

protected List<dbaccess.Entity> EntityEntityTypeIDEntityTypeIDLst;
public void addEntityEntityTypeIDEntityTypeID(dbaccess.Entity obj) {
  if (EntityEntityTypeIDEntityTypeIDLst == null) { EntityEntityTypeIDEntityTypeIDLst = new ArrayList<dbaccess.Entity>();}
EntityEntityTypeIDEntityTypeIDLst.add(obj); }
public List<dbaccess.Entity> fetchAllEntityEntityTypeIDEntityTypeID() { try {
  if (EntityEntityTypeIDEntityTypeIDLst != null) { return EntityEntityTypeIDEntityTypeIDLst;}
  return(EntityEntityTypeIDEntityTypeIDLst =
    dbaccess.bridge._Entity.fetchMany("EntityTypeID", (long)EntityTypeID));
  } catch (NullPointerException e) { return null; }}

public void cleanDirtyFlags() {
  ListOrderChanged=false;
  EntityTypeIDChanged=false;
  TermIDChanged=false;
}

public boolean testDirtyFlags() { return (
  ListOrderChanged ||
  EntityTypeIDChanged ||
  TermIDChanged);
}

public static final String primaryKey = "EntityTypeID";
public static String fetchPrimaryKey() { return primaryKey; }
public static EntityType fetchByPrimaryKey(Long colValue) {
  return (EntityType)BaseSQLClass.getOne(EntityType.class, null, primaryKey, colValue);
}
public static boolean hasPrimaryKey() { return true; }
}
