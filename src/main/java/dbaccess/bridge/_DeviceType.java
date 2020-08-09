package dbaccess.bridge;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;import java.util.ArrayList;
import zetek.dbcommon.BaseSQLClass;
import dbaccess.DeviceType;

public class _DeviceType extends BaseSQLClass implements Serializable {
  private static final long serialVersionUID=1;

  public _DeviceType() {}

public static List<DeviceType> fetchMany(String colName, Object colValue) {
return (List<DeviceType>)BaseSQLClass.getMany(DeviceType.class, null, colName, colValue);
}

public static DeviceType fetchOne(String colName, Object colValue) {
return (DeviceType)BaseSQLClass.getOne(DeviceType.class, null, colName, colValue);
}

public boolean DeviceTypeIDChanged;
/** Identifies a device type */ Long DeviceTypeID;
public Long getDeviceTypeID() { return DeviceTypeID; }
public void setDeviceTypeID(Long DeviceTypeID) {
  DeviceTypeIDChanged=BaseSQLClass.twoParms(this.DeviceTypeID, DeviceTypeID);
  this.DeviceTypeID = DeviceTypeID;
}

public boolean ListOrderChanged;
/** Order the items should appear in a list */ Long ListOrder;
public Long getListOrder() { return ListOrder; }
public void setListOrder(Long ListOrder) {
  ListOrderChanged=BaseSQLClass.twoParms(this.ListOrder, ListOrder);
  this.ListOrder = ListOrder;
}

public boolean TermIDChanged;
/** Language specific name of the device type */ Long TermID;
public Long getTermID() { return TermID; }
public void setTermID(Long TermID) {
  TermIDChanged=BaseSQLClass.twoParms(this.TermID, TermID);
  this.TermID = TermID;
}

// 1 to 1 references from DeviceType

protected dbaccess.EnglishTerm EnglishTermTermIDTermIDObj;
public dbaccess.EnglishTerm fetchOneEnglishTermTermIDTermID() { try {
  if (EnglishTermTermIDTermIDObj != null) { return EnglishTermTermIDTermIDObj;}
  return(EnglishTermTermIDTermIDObj =
    dbaccess.bridge._EnglishTerm.fetchOne("TermID", (long)TermID));
  } catch (NullPointerException e) { return null; }}

// 1 to many references from DeviceType

protected List<dbaccess.Device> DeviceDeviceTypeIDDeviceTypeIDLst;
public void addDeviceDeviceTypeIDDeviceTypeID(dbaccess.Device obj) {
  if (DeviceDeviceTypeIDDeviceTypeIDLst == null) { DeviceDeviceTypeIDDeviceTypeIDLst = new ArrayList<dbaccess.Device>();}
DeviceDeviceTypeIDDeviceTypeIDLst.add(obj); }
public List<dbaccess.Device> fetchAllDeviceDeviceTypeIDDeviceTypeID() { try {
  if (DeviceDeviceTypeIDDeviceTypeIDLst != null) { return DeviceDeviceTypeIDDeviceTypeIDLst;}
  return(DeviceDeviceTypeIDDeviceTypeIDLst =
    dbaccess.bridge._Device.fetchMany("DeviceTypeID", (long)DeviceTypeID));
  } catch (NullPointerException e) { return null; }}

public void cleanDirtyFlags() {
  DeviceTypeIDChanged=false;
  ListOrderChanged=false;
  TermIDChanged=false;
}

public boolean testDirtyFlags() { return (
  DeviceTypeIDChanged ||
  ListOrderChanged ||
  TermIDChanged);
}

public static final String primaryKey = "DeviceTypeID";
public static String fetchPrimaryKey() { return primaryKey; }
public static DeviceType fetchByPrimaryKey(Long colValue) {
  return (DeviceType)BaseSQLClass.getOne(DeviceType.class, null, primaryKey, colValue);
}
public static boolean hasPrimaryKey() { return true; }
}
