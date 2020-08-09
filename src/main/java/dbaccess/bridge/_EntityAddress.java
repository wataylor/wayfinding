package dbaccess.bridge;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;import java.util.ArrayList;
import zetek.dbcommon.BaseSQLClass;
import dbaccess.EntityAddress;

public class _EntityAddress extends BaseSQLClass implements Serializable {
  private static final long serialVersionUID=1;

  public _EntityAddress() {}

public static List<EntityAddress> fetchMany(String colName, Object colValue) {
return (List<EntityAddress>)BaseSQLClass.getMany(EntityAddress.class, null, colName, colValue);
}

public static EntityAddress fetchOne(String colName, Object colValue) {
return (EntityAddress)BaseSQLClass.getOne(EntityAddress.class, null, colName, colValue);
}

public boolean RetractedChanged;
/** non-null means the relationship has been retracted */ Timestamp Retracted;
public Timestamp getRetracted() { return Retracted; }
public void setRetracted(Timestamp Retracted) {
  RetractedChanged=BaseSQLClass.twoParms(this.Retracted, Retracted);
  this.Retracted = Retracted;
}

public boolean AddressIDChanged;
/** Reference to the table that actually holds all addresses and which is also stored in the Entity table */ Long AddressID;
public Long getAddressID() { return AddressID; }
public void setAddressID(Long AddressID) {
  AddressIDChanged=BaseSQLClass.twoParms(this.AddressID, AddressID);
  this.AddressID = AddressID;
}

public boolean EntityIDChanged;
/** The entity the address is for */ Long EntityID;
public Long getEntityID() { return EntityID; }
public void setEntityID(Long EntityID) {
  EntityIDChanged=BaseSQLClass.twoParms(this.EntityID, EntityID);
  this.EntityID = EntityID;
}

public boolean AddressTypeIDChanged;
/** The kind of relationship the address has with the entity */ Long AddressTypeID;
public Long getAddressTypeID() { return AddressTypeID; }
public void setAddressTypeID(Long AddressTypeID) {
  AddressTypeIDChanged=BaseSQLClass.twoParms(this.AddressTypeID, AddressTypeID);
  this.AddressTypeID = AddressTypeID;
}

// 1 to 1 references from EntityAddress

protected dbaccess.Address AddressAddressIDAddressIDObj;
public dbaccess.Address fetchOneAddressAddressIDAddressID() { try {
  if (AddressAddressIDAddressIDObj != null) { return AddressAddressIDAddressIDObj;}
  return(AddressAddressIDAddressIDObj =
    dbaccess.bridge._Address.fetchOne("AddressID", (long)AddressID));
  } catch (NullPointerException e) { return null; }}

protected dbaccess.Entity EntityEntityIDEntityIDObj;
public dbaccess.Entity fetchOneEntityEntityIDEntityID() { try {
  if (EntityEntityIDEntityIDObj != null) { return EntityEntityIDEntityIDObj;}
  return(EntityEntityIDEntityIDObj =
    dbaccess.bridge._Entity.fetchOne("EntityID", (long)EntityID));
  } catch (NullPointerException e) { return null; }}

protected dbaccess.AddressType AddressTypeAddressTypeIDAddressTypeIDObj;
public dbaccess.AddressType fetchOneAddressTypeAddressTypeIDAddressTypeID() { try {
  if (AddressTypeAddressTypeIDAddressTypeIDObj != null) { return AddressTypeAddressTypeIDAddressTypeIDObj;}
  return(AddressTypeAddressTypeIDAddressTypeIDObj =
    dbaccess.bridge._AddressType.fetchOne("AddressTypeID", (long)AddressTypeID));
  } catch (NullPointerException e) { return null; }}

// 1 to many references from EntityAddress

public void cleanDirtyFlags() {
  RetractedChanged=false;
  AddressIDChanged=false;
  EntityIDChanged=false;
  AddressTypeIDChanged=false;
}

public boolean testDirtyFlags() { return (
  RetractedChanged ||
  AddressIDChanged ||
  EntityIDChanged ||
  AddressTypeIDChanged);
}

public static final String primaryKey = "AddressID";
public static String fetchPrimaryKey() { return primaryKey; }
public static EntityAddress fetchByPrimaryKey(Long colValue) {
  return (EntityAddress)BaseSQLClass.getOne(EntityAddress.class, null, primaryKey, colValue);
}
public static boolean hasPrimaryKey() { return false; }
}
