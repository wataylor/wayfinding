package dbaccess.bridge;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;import java.util.ArrayList;
import zetek.dbcommon.BaseSQLClass;
import dbaccess.OrganizationType;

public class _OrganizationType extends BaseSQLClass implements Serializable {
  private static final long serialVersionUID=1;

  public _OrganizationType() {}

public static List<OrganizationType> fetchMany(String colName, Object colValue) {
return (List<OrganizationType>)BaseSQLClass.getMany(OrganizationType.class, null, colName, colValue);
}

public static OrganizationType fetchOne(String colName, Object colValue) {
return (OrganizationType)BaseSQLClass.getOne(OrganizationType.class, null, colName, colValue);
}

public boolean ListOrderChanged;
/** Order the items should appear in a pull down list for editing */ Long ListOrder;
public Long getListOrder() { return ListOrder; }
public void setListOrder(Long ListOrder) {
  ListOrderChanged=BaseSQLClass.twoParms(this.ListOrder, ListOrder);
  this.ListOrder = ListOrder;
}

public boolean OrganizationTypeIDChanged;
/** Identifies an organization type */ Long OrganizationTypeID;
public Long getOrganizationTypeID() { return OrganizationTypeID; }
public void setOrganizationTypeID(Long OrganizationTypeID) {
  OrganizationTypeIDChanged=BaseSQLClass.twoParms(this.OrganizationTypeID, OrganizationTypeID);
  this.OrganizationTypeID = OrganizationTypeID;
}

public boolean TermIDChanged;
/** Language specific name of the organization */ Long TermID;
public Long getTermID() { return TermID; }
public void setTermID(Long TermID) {
  TermIDChanged=BaseSQLClass.twoParms(this.TermID, TermID);
  this.TermID = TermID;
}

// 1 to 1 references from OrganizationType

protected dbaccess.EnglishTerm EnglishTermTermIDTermIDObj;
public dbaccess.EnglishTerm fetchOneEnglishTermTermIDTermID() { try {
  if (EnglishTermTermIDTermIDObj != null) { return EnglishTermTermIDTermIDObj;}
  return(EnglishTermTermIDTermIDObj =
    dbaccess.bridge._EnglishTerm.fetchOne("TermID", (long)TermID));
  } catch (NullPointerException e) { return null; }}

// 1 to many references from OrganizationType

protected List<dbaccess.Organization> OrganizationOrganizationTypeIDOrganizationTypeIDLst;
public void addOrganizationOrganizationTypeIDOrganizationTypeID(dbaccess.Organization obj) {
  if (OrganizationOrganizationTypeIDOrganizationTypeIDLst == null) { OrganizationOrganizationTypeIDOrganizationTypeIDLst = new ArrayList<dbaccess.Organization>();}
OrganizationOrganizationTypeIDOrganizationTypeIDLst.add(obj); }
public List<dbaccess.Organization> fetchAllOrganizationOrganizationTypeIDOrganizationTypeID() { try {
  if (OrganizationOrganizationTypeIDOrganizationTypeIDLst != null) { return OrganizationOrganizationTypeIDOrganizationTypeIDLst;}
  return(OrganizationOrganizationTypeIDOrganizationTypeIDLst =
    dbaccess.bridge._Organization.fetchMany("OrganizationTypeID", (long)OrganizationTypeID));
  } catch (NullPointerException e) { return null; }}

public void cleanDirtyFlags() {
  ListOrderChanged=false;
  OrganizationTypeIDChanged=false;
  TermIDChanged=false;
}

public boolean testDirtyFlags() { return (
  ListOrderChanged ||
  OrganizationTypeIDChanged ||
  TermIDChanged);
}

public static final String primaryKey = "OrganizationTypeID";
public static String fetchPrimaryKey() { return primaryKey; }
public static OrganizationType fetchByPrimaryKey(Long colValue) {
  return (OrganizationType)BaseSQLClass.getOne(OrganizationType.class, null, primaryKey, colValue);
}
public static boolean hasPrimaryKey() { return true; }
}
