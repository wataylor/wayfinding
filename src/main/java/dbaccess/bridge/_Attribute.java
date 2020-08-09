package dbaccess.bridge;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;import java.util.ArrayList;
import zetek.dbcommon.BaseSQLClass;
import dbaccess.Attribute;

public class _Attribute extends BaseSQLClass implements Serializable {
  private static final long serialVersionUID=1;

  public _Attribute() {}

public static List<Attribute> fetchMany(String colName, Object colValue) {
return (List<Attribute>)BaseSQLClass.getMany(Attribute.class, null, colName, colValue);
}

public static Attribute fetchOne(String colName, Object colValue) {
return (Attribute)BaseSQLClass.getOne(Attribute.class, null, colName, colValue);
}

public boolean EntityIDChanged;
/** Any entity may have multiple attributes which is why there is no primary key */ Long EntityID;
public Long getEntityID() { return EntityID; }
public void setEntityID(Long EntityID) {
  EntityIDChanged=BaseSQLClass.twoParms(this.EntityID, EntityID);
  this.EntityID = EntityID;
}

public boolean AttributeTypeIDChanged;
/** The system supports a table of attribute types.  It is an error to define an attribute whose type is not specified. */ Long AttributeTypeID;
public Long getAttributeTypeID() { return AttributeTypeID; }
public void setAttributeTypeID(Long AttributeTypeID) {
  AttributeTypeIDChanged=BaseSQLClass.twoParms(this.AttributeTypeID, AttributeTypeID);
  this.AttributeTypeID = AttributeTypeID;
}

public boolean AttributeNameChanged;
/** Attribute names must be unique in a building, the mechanism for entering new attribute names enforces this.  An attribute name is owned by the person who entered it. */ String AttributeName;
public String getAttributeName() { return AttributeName; }
public void setAttributeName(String AttributeName) {
  AttributeNameChanged=BaseSQLClass.twoParms(this.AttributeName, AttributeName);
  this.AttributeName = AttributeName;
}

public boolean AttributeValueChanged;
/** The text value of the attribute is converted and displayed based on the specified attribute type. */ String AttributeValue;
public String getAttributeValue() { return AttributeValue; }
public void setAttributeValue(String AttributeValue) {
  AttributeValueChanged=BaseSQLClass.twoParms(this.AttributeValue, AttributeValue);
  this.AttributeValue = AttributeValue;
}

// 1 to 1 references from Attribute

protected dbaccess.AttributeType AttributeTypeAttributeTypeIDAttributeTypeIDObj;
public dbaccess.AttributeType fetchOneAttributeTypeAttributeTypeIDAttributeTypeID() { try {
  if (AttributeTypeAttributeTypeIDAttributeTypeIDObj != null) { return AttributeTypeAttributeTypeIDAttributeTypeIDObj;}
  return(AttributeTypeAttributeTypeIDAttributeTypeIDObj =
    dbaccess.bridge._AttributeType.fetchOne("AttributeTypeID", (long)AttributeTypeID));
  } catch (NullPointerException e) { return null; }}

protected dbaccess.Entity EntityEntityIDEntityIDObj;
public dbaccess.Entity fetchOneEntityEntityIDEntityID() { try {
  if (EntityEntityIDEntityIDObj != null) { return EntityEntityIDEntityIDObj;}
  return(EntityEntityIDEntityIDObj =
    dbaccess.bridge._Entity.fetchOne("EntityID", (long)EntityID));
  } catch (NullPointerException e) { return null; }}

// 1 to many references from Attribute

public void cleanDirtyFlags() {
  EntityIDChanged=false;
  AttributeTypeIDChanged=false;
  AttributeNameChanged=false;
  AttributeValueChanged=false;
}

public boolean testDirtyFlags() { return (
  EntityIDChanged ||
  AttributeTypeIDChanged ||
  AttributeNameChanged ||
  AttributeValueChanged);
}

public static final String primaryKey = "EntityID";
public static String fetchPrimaryKey() { return primaryKey; }
public static Attribute fetchByPrimaryKey(Long colValue) {
  return (Attribute)BaseSQLClass.getOne(Attribute.class, null, primaryKey, colValue);
}
public static boolean hasPrimaryKey() { return false; }
}
