package dbaccess.bridge;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;import java.util.ArrayList;
import zetek.dbcommon.BaseSQLClass;
import dbaccess.ChangeLog;

public class _ChangeLog extends BaseSQLClass implements Serializable {
  private static final long serialVersionUID=1;

  public _ChangeLog() {}

public static List<ChangeLog> fetchMany(String colName, Object colValue) {
return (List<ChangeLog>)BaseSQLClass.getMany(ChangeLog.class, null, colName, colValue);
}

public static ChangeLog fetchOne(String colName, Object colValue) {
return (ChangeLog)BaseSQLClass.getOne(ChangeLog.class, null, colName, colValue);
}

public boolean TableNameChanged;
/** Records the name of the table whose column value was changed */ String TableName;
public String getTableName() { return TableName; }
public void setTableName(String TableName) {
  TableNameChanged=BaseSQLClass.twoParms(this.TableName, TableName);
  this.TableName = TableName;
}

public boolean OldValueChanged;
/**  */ String OldValue;
public String getOldValue() { return OldValue; }
public void setOldValue(String OldValue) {
  OldValueChanged=BaseSQLClass.twoParms(this.OldValue, OldValue);
  this.OldValue = OldValue;
}

public boolean RecordIDChanged;
/** Identifies the record within the table which was changed */ Long RecordID;
public Long getRecordID() { return RecordID; }
public void setRecordID(Long RecordID) {
  RecordIDChanged=BaseSQLClass.twoParms(this.RecordID, RecordID);
  this.RecordID = RecordID;
}

public boolean PersonIDChanged;
/** Identifies the perpetrator of the change */ Long PersonID;
public Long getPersonID() { return PersonID; }
public void setPersonID(Long PersonID) {
  PersonIDChanged=BaseSQLClass.twoParms(this.PersonID, PersonID);
  this.PersonID = PersonID;
}

public boolean ColumnNameChanged;
/** Records the column name within the table whose value was changed */ String ColumnName;
public String getColumnName() { return ColumnName; }
public void setColumnName(String ColumnName) {
  ColumnNameChanged=BaseSQLClass.twoParms(this.ColumnName, ColumnName);
  this.ColumnName = ColumnName;
}

public boolean NewValueChanged;
/**  */ String NewValue;
public String getNewValue() { return NewValue; }
public void setNewValue(String NewValue) {
  NewValueChanged=BaseSQLClass.twoParms(this.NewValue, NewValue);
  this.NewValue = NewValue;
}

public boolean CommentsChanged;
/**  */ String Comments;
public String getComments() { return Comments; }
public void setComments(String Comments) {
  CommentsChanged=BaseSQLClass.twoParms(this.Comments, Comments);
  this.Comments = Comments;
}

// 1 to 1 references from ChangeLog

protected dbaccess.Person PersonPersonIDPersonIDObj;
public dbaccess.Person fetchOnePersonPersonIDPersonID() { try {
  if (PersonPersonIDPersonIDObj != null) { return PersonPersonIDPersonIDObj;}
  return(PersonPersonIDPersonIDObj =
    dbaccess.bridge._Person.fetchOne("PersonID", (long)PersonID));
  } catch (NullPointerException e) { return null; }}

// 1 to many references from ChangeLog

public void cleanDirtyFlags() {
  TableNameChanged=false;
  OldValueChanged=false;
  RecordIDChanged=false;
  PersonIDChanged=false;
  ColumnNameChanged=false;
  NewValueChanged=false;
  CommentsChanged=false;
}

public boolean testDirtyFlags() { return (
  TableNameChanged ||
  OldValueChanged ||
  RecordIDChanged ||
  PersonIDChanged ||
  ColumnNameChanged ||
  NewValueChanged ||
  CommentsChanged);
}

public static final String primaryKey = "PersonID";
public static String fetchPrimaryKey() { return primaryKey; }
public static ChangeLog fetchByPrimaryKey(Long colValue) {
  return (ChangeLog)BaseSQLClass.getOne(ChangeLog.class, null, primaryKey, colValue);
}
public static boolean hasPrimaryKey() { return false; }
}
