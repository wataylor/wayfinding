package dbaccess.bridge;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;import java.util.ArrayList;
import zetek.dbcommon.BaseSQLClass;
import dbaccess.EntityRelation;

public class _EntityRelation extends BaseSQLClass implements Serializable {
  private static final long serialVersionUID=1;

  public _EntityRelation() {}

public static List<EntityRelation> fetchMany(String colName, Object colValue) {
return (List<EntityRelation>)BaseSQLClass.getMany(EntityRelation.class, null, colName, colValue);
}

public static EntityRelation fetchOne(String colName, Object colValue) {
return (EntityRelation)BaseSQLClass.getOne(EntityRelation.class, null, colName, colValue);
}

public boolean NameChanged;
/** In the case of a location being associated with the image, this holds the file name */ String Name;
public String getName() { return Name; }
public void setName(String Name) {
  NameChanged=BaseSQLClass.twoParms(this.Name, Name);
  this.Name = Name;
}

public boolean EntityIDChanged;
/** Identifies the entity which belongs to the other entity.  If this is null, the relationship is of a different type */ Long EntityID;
public Long getEntityID() { return EntityID; }
public void setEntityID(Long EntityID) {
  EntityIDChanged=BaseSQLClass.twoParms(this.EntityID, EntityID);
  this.EntityID = EntityID;
}

public boolean OfEntityIDChanged;
/** Identifies the entity which owns the other entity.  This value must be filled in. */ Long OfEntityID;
public Long getOfEntityID() { return OfEntityID; }
public void setOfEntityID(Long OfEntityID) {
  OfEntityIDChanged=BaseSQLClass.twoParms(this.OfEntityID, OfEntityID);
  this.OfEntityID = OfEntityID;
}

public boolean EntityRelationIDChanged;
/** Uniquely identify the relationship */ Long EntityRelationID;
public Long getEntityRelationID() { return EntityRelationID; }
public void setEntityRelationID(Long EntityRelationID) {
  EntityRelationIDChanged=BaseSQLClass.twoParms(this.EntityRelationID, EntityRelationID);
  this.EntityRelationID = EntityRelationID;
}

public boolean RelationshipChanged;
/** In the case of a location being associated with an image, this holds the caption */ String Relationship;
public String getRelationship() { return Relationship; }
public void setRelationship(String Relationship) {
  RelationshipChanged=BaseSQLClass.twoParms(this.Relationship, Relationship);
  this.Relationship = Relationship;
}

// 1 to 1 references from EntityRelation

protected dbaccess.Entity EntityEntityIDOfEntityIDObj;
public dbaccess.Entity fetchOneEntityEntityIDOfEntityID() { try {
  if (EntityEntityIDOfEntityIDObj != null) { return EntityEntityIDOfEntityIDObj;}
  return(EntityEntityIDOfEntityIDObj =
    dbaccess.bridge._Entity.fetchOne("EntityID", (long)OfEntityID));
  } catch (NullPointerException e) { return null; }}

protected dbaccess.Entity EntityEntityIDEntityIDObj;
public dbaccess.Entity fetchOneEntityEntityIDEntityID() { try {
  if (EntityEntityIDEntityIDObj != null) { return EntityEntityIDEntityIDObj;}
  return(EntityEntityIDEntityIDObj =
    dbaccess.bridge._Entity.fetchOne("EntityID", (long)EntityID));
  } catch (NullPointerException e) { return null; }}

// 1 to many references from EntityRelation

public void cleanDirtyFlags() {
  NameChanged=false;
  EntityIDChanged=false;
  OfEntityIDChanged=false;
  EntityRelationIDChanged=false;
  RelationshipChanged=false;
}

public boolean testDirtyFlags() { return (
  NameChanged ||
  EntityIDChanged ||
  OfEntityIDChanged ||
  EntityRelationIDChanged ||
  RelationshipChanged);
}

public static final String primaryKey = "EntityRelationID";
public static String fetchPrimaryKey() { return primaryKey; }
public static EntityRelation fetchByPrimaryKey(Long colValue) {
  return (EntityRelation)BaseSQLClass.getOne(EntityRelation.class, null, primaryKey, colValue);
}
public static boolean hasPrimaryKey() { return true; }
}
