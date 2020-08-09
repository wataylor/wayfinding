package dbaccess.bridge;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;import java.util.ArrayList;
import zetek.dbcommon.BaseSQLClass;
import dbaccess.Happening;

public class _Happening extends BaseSQLClass implements Serializable {
  private static final long serialVersionUID=1;

  public _Happening() {}

public static List<Happening> fetchMany(String colName, Object colValue) {
return (List<Happening>)BaseSQLClass.getMany(Happening.class, null, colName, colValue);
}

public static Happening fetchOne(String colName, Object colValue) {
return (Happening)BaseSQLClass.getOne(Happening.class, null, colName, colValue);
}

public boolean DescriptionChanged;
/** Description as needed */ String Description;
public String getDescription() { return Description; }
public void setDescription(String Description) {
  DescriptionChanged=BaseSQLClass.twoParms(this.Description, Description);
  this.Description = Description;
}

public boolean HappeningIDChanged;
/** Identifies the happening entry */ Long HappeningID;
public Long getHappeningID() { return HappeningID; }
public void setHappeningID(Long HappeningID) {
  HappeningIDChanged=BaseSQLClass.twoParms(this.HappeningID, HappeningID);
  this.HappeningID = HappeningID;
}

public boolean ToSpaceIDChanged;
/** To space */ Long ToSpaceID;
public Long getToSpaceID() { return ToSpaceID; }
public void setToSpaceID(Long ToSpaceID) {
  ToSpaceIDChanged=BaseSQLClass.twoParms(this.ToSpaceID, ToSpaceID);
  this.ToSpaceID = ToSpaceID;
}

public boolean ExternalTimeChanged;
/** Records the time the data source supplied */ Timestamp ExternalTime;
public Timestamp getExternalTime() { return ExternalTime; }
public void setExternalTime(Timestamp ExternalTime) {
  ExternalTimeChanged=BaseSQLClass.twoParms(this.ExternalTime, ExternalTime);
  this.ExternalTime = ExternalTime;
}

public boolean BuildingIDChanged;
/** Associate the happening with the building. */ Long BuildingID;
public Long getBuildingID() { return BuildingID; }
public void setBuildingID(Long BuildingID) {
  BuildingIDChanged=BaseSQLClass.twoParms(this.BuildingID, BuildingID);
  this.BuildingID = BuildingID;
}

public boolean HappeningTypeIDChanged;
/** Identifies the type of happening */ Long HappeningTypeID;
public Long getHappeningTypeID() { return HappeningTypeID; }
public void setHappeningTypeID(Long HappeningTypeID) {
  HappeningTypeIDChanged=BaseSQLClass.twoParms(this.HappeningTypeID, HappeningTypeID);
  this.HappeningTypeID = HappeningTypeID;
}

public boolean ToLocationIDChanged;
/** To location */ Long ToLocationID;
public Long getToLocationID() { return ToLocationID; }
public void setToLocationID(Long ToLocationID) {
  ToLocationIDChanged=BaseSQLClass.twoParms(this.ToLocationID, ToLocationID);
  this.ToLocationID = ToLocationID;
}

public boolean DeviceIDChanged;
/** Device ID for alarms */ Long DeviceID;
public Long getDeviceID() { return DeviceID; }
public void setDeviceID(Long DeviceID) {
  DeviceIDChanged=BaseSQLClass.twoParms(this.DeviceID, DeviceID);
  this.DeviceID = DeviceID;
}

public boolean FromSpaceIDChanged;
/** From space */ Long FromSpaceID;
public Long getFromSpaceID() { return FromSpaceID; }
public void setFromSpaceID(Long FromSpaceID) {
  FromSpaceIDChanged=BaseSQLClass.twoParms(this.FromSpaceID, FromSpaceID);
  this.FromSpaceID = FromSpaceID;
}

public boolean ClearedChanged;
/** Set to Y when alarm is cleared by a user */ String Cleared="n";
public String getCleared() { return Cleared; }
public void setCleared(String Cleared) {
  ClearedChanged=BaseSQLClass.twoParms(this.Cleared, Cleared);
  this.Cleared = Cleared;
}

public boolean FromLocationIDChanged;
/** From location */ Long FromLocationID;
public Long getFromLocationID() { return FromLocationID; }
public void setFromLocationID(Long FromLocationID) {
  FromLocationIDChanged=BaseSQLClass.twoParms(this.FromLocationID, FromLocationID);
  this.FromLocationID = FromLocationID;
}

// 1 to 1 references from Happening

protected dbaccess.Building BuildingBuildingIDBuildingIDObj;
public dbaccess.Building fetchOneBuildingBuildingIDBuildingID() { try {
  if (BuildingBuildingIDBuildingIDObj != null) { return BuildingBuildingIDBuildingIDObj;}
  return(BuildingBuildingIDBuildingIDObj =
    dbaccess.bridge._Building.fetchOne("BuildingID", (long)BuildingID));
  } catch (NullPointerException e) { return null; }}

protected dbaccess.Device DeviceDeviceIDDeviceIDObj;
public dbaccess.Device fetchOneDeviceDeviceIDDeviceID() { try {
  if (DeviceDeviceIDDeviceIDObj != null) { return DeviceDeviceIDDeviceIDObj;}
  return(DeviceDeviceIDDeviceIDObj =
    dbaccess.bridge._Device.fetchOne("DeviceID", (long)DeviceID));
  } catch (NullPointerException e) { return null; }}

protected dbaccess.HappeningType HappeningTypeHappeningTypeIDHappeningTypeIDObj;
public dbaccess.HappeningType fetchOneHappeningTypeHappeningTypeIDHappeningTypeID() { try {
  if (HappeningTypeHappeningTypeIDHappeningTypeIDObj != null) { return HappeningTypeHappeningTypeIDHappeningTypeIDObj;}
  return(HappeningTypeHappeningTypeIDHappeningTypeIDObj =
    dbaccess.bridge._HappeningType.fetchOne("HappeningTypeID", (long)HappeningTypeID));
  } catch (NullPointerException e) { return null; }}

// 1 to many references from Happening

public void cleanDirtyFlags() {
  DescriptionChanged=false;
  HappeningIDChanged=false;
  ToSpaceIDChanged=false;
  ExternalTimeChanged=false;
  BuildingIDChanged=false;
  HappeningTypeIDChanged=false;
  ToLocationIDChanged=false;
  DeviceIDChanged=false;
  FromSpaceIDChanged=false;
  ClearedChanged=false;
  FromLocationIDChanged=false;
}

public boolean testDirtyFlags() { return (
  DescriptionChanged ||
  HappeningIDChanged ||
  ToSpaceIDChanged ||
  ExternalTimeChanged ||
  BuildingIDChanged ||
  HappeningTypeIDChanged ||
  ToLocationIDChanged ||
  DeviceIDChanged ||
  FromSpaceIDChanged ||
  ClearedChanged ||
  FromLocationIDChanged);
}

public static final String primaryKey = "HappeningID";
public static String fetchPrimaryKey() { return primaryKey; }
public static Happening fetchByPrimaryKey(Long colValue) {
  return (Happening)BaseSQLClass.getOne(Happening.class, null, primaryKey, colValue);
}
public static boolean hasPrimaryKey() { return true; }
}
