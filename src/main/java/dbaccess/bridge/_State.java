package dbaccess.bridge;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;import java.util.ArrayList;
import zetek.dbcommon.BaseSQLClass;
import dbaccess.State;

public class _State extends BaseSQLClass implements Serializable {
  private static final long serialVersionUID=1;

  public _State() {}

public static List<State> fetchMany(String colName, Object colValue) {
return (List<State>)BaseSQLClass.getMany(State.class, null, colName, colValue);
}

public static State fetchOne(String colName, Object colValue) {
return (State)BaseSQLClass.getOne(State.class, null, colName, colValue);
}

public boolean ListOrderChanged;
/** Order the items should appear in a list */ Long ListOrder;
public Long getListOrder() { return ListOrder; }
public void setListOrder(Long ListOrder) {
  ListOrderChanged=BaseSQLClass.twoParms(this.ListOrder, ListOrder);
  this.ListOrder = ListOrder;
}

public boolean StateIDChanged;
/** Identifies the state with its name */ Long StateID;
public Long getStateID() { return StateID; }
public void setStateID(Long StateID) {
  StateIDChanged=BaseSQLClass.twoParms(this.StateID, StateID);
  this.StateID = StateID;
}

public boolean StateAbbrevChanged;
/**  */ String StateAbbrev;
public String getStateAbbrev() { return StateAbbrev; }
public void setStateAbbrev(String StateAbbrev) {
  StateAbbrevChanged=BaseSQLClass.twoParms(this.StateAbbrev, StateAbbrev);
  this.StateAbbrev = StateAbbrev;
}

public boolean TermIDChanged;
/** State name */ Long TermID;
public Long getTermID() { return TermID; }
public void setTermID(Long TermID) {
  TermIDChanged=BaseSQLClass.twoParms(this.TermID, TermID);
  this.TermID = TermID;
}

// 1 to 1 references from State

protected dbaccess.EnglishTerm EnglishTermTermIDTermIDObj;
public dbaccess.EnglishTerm fetchOneEnglishTermTermIDTermID() { try {
  if (EnglishTermTermIDTermIDObj != null) { return EnglishTermTermIDTermIDObj;}
  return(EnglishTermTermIDTermIDObj =
    dbaccess.bridge._EnglishTerm.fetchOne("TermID", (long)TermID));
  } catch (NullPointerException e) { return null; }}

// 1 to many references from State

protected List<dbaccess.Address> AddressStateIDStateIDLst;
public void addAddressStateIDStateID(dbaccess.Address obj) {
  if (AddressStateIDStateIDLst == null) { AddressStateIDStateIDLst = new ArrayList<dbaccess.Address>();}
AddressStateIDStateIDLst.add(obj); }
public List<dbaccess.Address> fetchAllAddressStateIDStateID() { try {
  if (AddressStateIDStateIDLst != null) { return AddressStateIDStateIDLst;}
  return(AddressStateIDStateIDLst =
    dbaccess.bridge._Address.fetchMany("StateID", (long)StateID));
  } catch (NullPointerException e) { return null; }}

public void cleanDirtyFlags() {
  ListOrderChanged=false;
  StateIDChanged=false;
  StateAbbrevChanged=false;
  TermIDChanged=false;
}

public boolean testDirtyFlags() { return (
  ListOrderChanged ||
  StateIDChanged ||
  StateAbbrevChanged ||
  TermIDChanged);
}

public static final String primaryKey = "StateID";
public static String fetchPrimaryKey() { return primaryKey; }
public static State fetchByPrimaryKey(Long colValue) {
  return (State)BaseSQLClass.getOne(State.class, null, primaryKey, colValue);
}
public static boolean hasPrimaryKey() { return true; }
}
