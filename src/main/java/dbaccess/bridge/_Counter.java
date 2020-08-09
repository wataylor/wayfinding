package dbaccess.bridge;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;import java.util.ArrayList;
import zetek.dbcommon.BaseSQLClass;
import dbaccess.Counter;

public class _Counter extends BaseSQLClass implements Serializable {
  private static final long serialVersionUID=1;

  public _Counter() {}

public static List<Counter> fetchMany(String colName, Object colValue) {
return (List<Counter>)BaseSQLClass.getMany(Counter.class, null, colName, colValue);
}

public static Counter fetchOne(String colName, Object colValue) {
return (Counter)BaseSQLClass.getOne(Counter.class, null, colName, colValue);
}

public boolean MachineIDChanged;
/** The ID of the machine which created this entry */ Long MachineID;
public Long getMachineID() { return MachineID; }
public void setMachineID(Long MachineID) {
  MachineIDChanged=BaseSQLClass.twoParms(this.MachineID, MachineID);
  this.MachineID = MachineID;
}

public boolean CounterChanged;
/** Uniquely identify an entity with respect to the local machine.  This key stays local */ Long Counter;
public Long getCounter() { return Counter; }
public void setCounter(Long Counter) {
  CounterChanged=BaseSQLClass.twoParms(this.Counter, Counter);
  this.Counter = Counter;
}

// 1 to 1 references from Counter

// 1 to many references from Counter

public void cleanDirtyFlags() {
  MachineIDChanged=false;
  CounterChanged=false;
}

public boolean testDirtyFlags() { return (
  MachineIDChanged ||
  CounterChanged);
}

public static final String primaryKey = "Counter";
public static String fetchPrimaryKey() { return primaryKey; }
public static Counter fetchByPrimaryKey(Long colValue) {
  return (Counter)BaseSQLClass.getOne(Counter.class, null, primaryKey, colValue);
}
public static boolean hasPrimaryKey() { return true; }
}
