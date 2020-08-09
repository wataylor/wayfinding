package dbaccess.bridge;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;import java.util.ArrayList;
import zetek.dbcommon.BaseSQLClass;
import dbaccess.Permission;

public class _Permission extends BaseSQLClass implements Serializable {
  private static final long serialVersionUID=1;

  public _Permission() {}

public static List<Permission> fetchMany(String colName, Object colValue) {
return (List<Permission>)BaseSQLClass.getMany(Permission.class, null, colName, colValue);
}

public static Permission fetchOne(String colName, Object colValue) {
return (Permission)BaseSQLClass.getOne(Permission.class, null, colName, colValue);
}

public boolean NameChanged;
/** This name is the official system name of the entity.  The rules for naming vary with entity type.  For example a room name must be unique within a building floor and a device name must be unique within a building.  This name does not ever change.  A locat */ String Name;
public String getName() { return Name; }
public void setName(String Name) {
  NameChanged=BaseSQLClass.twoParms(this.Name, Name);
  this.Name = Name;
}

public boolean EntityIDChanged;
/** Uniquely identify an entity for which some sort of permission is being granted.  What the permission permits depends on the permission type. */ Long EntityID;
public Long getEntityID() { return EntityID; }
public void setEntityID(Long EntityID) {
  EntityIDChanged=BaseSQLClass.twoParms(this.EntityID, EntityID);
  this.EntityID = EntityID;
}

public boolean DisplayNameChanged;
/** This name is the display name for the entity. The rules vary with entity type.  A room display name must be unique within a building.  The display name defaults to the name when the entity is first created. */ String DisplayName;
public String getDisplayName() { return DisplayName; }
public void setDisplayName(String DisplayName) {
  DisplayNameChanged=BaseSQLClass.twoParms(this.DisplayName, DisplayName);
  this.DisplayName = DisplayName;
}

public boolean PersonIDChanged;
/** Person the permission is for */ Long PersonID;
public Long getPersonID() { return PersonID; }
public void setPersonID(Long PersonID) {
  PersonIDChanged=BaseSQLClass.twoParms(this.PersonID, PersonID);
  this.PersonID = PersonID;
}

public boolean PermissionsIDChanged;
/** Unique record identifier; may not be needed. */ Long PermissionsID;
public Long getPermissionsID() { return PermissionsID; }
public void setPermissionsID(Long PermissionsID) {
  PermissionsIDChanged=BaseSQLClass.twoParms(this.PermissionsID, PermissionsID);
  this.PermissionsID = PermissionsID;
}

public boolean PermissionTypeIDChanged;
/** A permission the persion has */ Long PermissionTypeID;
public Long getPermissionTypeID() { return PermissionTypeID; }
public void setPermissionTypeID(Long PermissionTypeID) {
  PermissionTypeIDChanged=BaseSQLClass.twoParms(this.PermissionTypeID, PermissionTypeID);
  this.PermissionTypeID = PermissionTypeID;
}

// 1 to 1 references from Permission

protected dbaccess.Person PersonPersonIDPersonIDObj;
public dbaccess.Person fetchOnePersonPersonIDPersonID() { try {
  if (PersonPersonIDPersonIDObj != null) { return PersonPersonIDPersonIDObj;}
  return(PersonPersonIDPersonIDObj =
    dbaccess.bridge._Person.fetchOne("PersonID", (long)PersonID));
  } catch (NullPointerException e) { return null; }}

protected dbaccess.Entity EntityEntityIDEntityIDObj;
public dbaccess.Entity fetchOneEntityEntityIDEntityID() { try {
  if (EntityEntityIDEntityIDObj != null) { return EntityEntityIDEntityIDObj;}
  return(EntityEntityIDEntityIDObj =
    dbaccess.bridge._Entity.fetchOne("EntityID", (long)EntityID));
  } catch (NullPointerException e) { return null; }}

protected dbaccess.PermissionType PermissionTypePermissionTypeIDPermissionTypeIDObj;
public dbaccess.PermissionType fetchOnePermissionTypePermissionTypeIDPermissionTypeID() { try {
  if (PermissionTypePermissionTypeIDPermissionTypeIDObj != null) { return PermissionTypePermissionTypeIDPermissionTypeIDObj;}
  return(PermissionTypePermissionTypeIDPermissionTypeIDObj =
    dbaccess.bridge._PermissionType.fetchOne("PermissionTypeID", (long)PermissionTypeID));
  } catch (NullPointerException e) { return null; }}

// 1 to many references from Permission

public void cleanDirtyFlags() {
  NameChanged=false;
  EntityIDChanged=false;
  DisplayNameChanged=false;
  PersonIDChanged=false;
  PermissionsIDChanged=false;
  PermissionTypeIDChanged=false;
}

public boolean testDirtyFlags() { return (
  NameChanged ||
  EntityIDChanged ||
  DisplayNameChanged ||
  PersonIDChanged ||
  PermissionsIDChanged ||
  PermissionTypeIDChanged);
}

public static final String primaryKey = "PermissionsID";
public static String fetchPrimaryKey() { return primaryKey; }
public static Permission fetchByPrimaryKey(Long colValue) {
  return (Permission)BaseSQLClass.getOne(Permission.class, null, primaryKey, colValue);
}
public static boolean hasPrimaryKey() { return true; }
}
