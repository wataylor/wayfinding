package dbaccess.bridge;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;import java.util.ArrayList;
import zetek.dbcommon.BaseSQLClass;
import dbaccess.LocationType;

public class _LocationType extends BaseSQLClass implements Serializable {
  private static final long serialVersionUID=1;

  public _LocationType() {}

public static List<LocationType> fetchMany(String colName, Object colValue) {
return (List<LocationType>)BaseSQLClass.getMany(LocationType.class, null, colName, colValue);
}

public static LocationType fetchOne(String colName, Object colValue) {
return (LocationType)BaseSQLClass.getOne(LocationType.class, null, colName, colValue);
}

public boolean ListOrderChanged;
/** Order the items should appear in a list */ Long ListOrder;
public Long getListOrder() { return ListOrder; }
public void setListOrder(Long ListOrder) {
  ListOrderChanged=BaseSQLClass.twoParms(this.ListOrder, ListOrder);
  this.ListOrder = ListOrder;
}

public boolean LocationTypeIDChanged;
/** Identifies a location type */ Long LocationTypeID;
public Long getLocationTypeID() { return LocationTypeID; }
public void setLocationTypeID(Long LocationTypeID) {
  LocationTypeIDChanged=BaseSQLClass.twoParms(this.LocationTypeID, LocationTypeID);
  this.LocationTypeID = LocationTypeID;
}

public boolean TermIDChanged;
/** Language specific name of the location type */ Long TermID;
public Long getTermID() { return TermID; }
public void setTermID(Long TermID) {
  TermIDChanged=BaseSQLClass.twoParms(this.TermID, TermID);
  this.TermID = TermID;
}

// 1 to 1 references from LocationType

protected dbaccess.EnglishTerm EnglishTermTermIDTermIDObj;
public dbaccess.EnglishTerm fetchOneEnglishTermTermIDTermID() { try {
  if (EnglishTermTermIDTermIDObj != null) { return EnglishTermTermIDTermIDObj;}
  return(EnglishTermTermIDTermIDObj =
    dbaccess.bridge._EnglishTerm.fetchOne("TermID", (long)TermID));
  } catch (NullPointerException e) { return null; }}

// 1 to many references from LocationType

protected List<dbaccess.Location> LocationLocationTypeIDLocationTypeIDLst;
public void addLocationLocationTypeIDLocationTypeID(dbaccess.Location obj) {
  if (LocationLocationTypeIDLocationTypeIDLst == null) { LocationLocationTypeIDLocationTypeIDLst = new ArrayList<dbaccess.Location>();}
LocationLocationTypeIDLocationTypeIDLst.add(obj); }
public List<dbaccess.Location> fetchAllLocationLocationTypeIDLocationTypeID() { try {
  if (LocationLocationTypeIDLocationTypeIDLst != null) { return LocationLocationTypeIDLocationTypeIDLst;}
  return(LocationLocationTypeIDLocationTypeIDLst =
    dbaccess.bridge._Location.fetchMany("LocationTypeID", (long)LocationTypeID));
  } catch (NullPointerException e) { return null; }}

public void cleanDirtyFlags() {
  ListOrderChanged=false;
  LocationTypeIDChanged=false;
  TermIDChanged=false;
}

public boolean testDirtyFlags() { return (
  ListOrderChanged ||
  LocationTypeIDChanged ||
  TermIDChanged);
}

public static final String primaryKey = "LocationTypeID";
public static String fetchPrimaryKey() { return primaryKey; }
public static LocationType fetchByPrimaryKey(Long colValue) {
  return (LocationType)BaseSQLClass.getOne(LocationType.class, null, primaryKey, colValue);
}
public static boolean hasPrimaryKey() { return true; }
}
