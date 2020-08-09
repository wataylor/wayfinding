package dbaccess.bridge;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;import java.util.ArrayList;
import zetek.dbcommon.BaseSQLClass;
import dbaccess.LogInHistory;

public class _LogInHistory extends BaseSQLClass implements Serializable {
  private static final long serialVersionUID=1;

  public _LogInHistory() {}

public static List<LogInHistory> fetchMany(String colName, Object colValue) {
return (List<LogInHistory>)BaseSQLClass.getMany(LogInHistory.class, null, colName, colValue);
}

public static LogInHistory fetchOne(String colName, Object colValue) {
return (LogInHistory)BaseSQLClass.getOne(LogInHistory.class, null, colName, colValue);
}

public boolean PersonIDChanged;
/** Identifies the person logging in */ Long PersonID;
public Long getPersonID() { return PersonID; }
public void setPersonID(Long PersonID) {
  PersonIDChanged=BaseSQLClass.twoParms(this.PersonID, PersonID);
  this.PersonID = PersonID;
}

public boolean UserAgentChanged;
/** Records the browser which was used to log in */ String UserAgent;
public String getUserAgent() { return UserAgent; }
public void setUserAgent(String UserAgent) {
  UserAgentChanged=BaseSQLClass.twoParms(this.UserAgent, UserAgent);
  this.UserAgent = UserAgent;
}

public boolean FromHostChanged;
/**  */ String FromHost;
public String getFromHost() { return FromHost; }
public void setFromHost(String FromHost) {
  FromHostChanged=BaseSQLClass.twoParms(this.FromHost, FromHost);
  this.FromHost = FromHost;
}

public boolean FromIPChanged;
/**  */ String FromIP;
public String getFromIP() { return FromIP; }
public void setFromIP(String FromIP) {
  FromIPChanged=BaseSQLClass.twoParms(this.FromIP, FromIP);
  this.FromIP = FromIP;
}

public boolean ResultChanged;
/** Users should take particular note of failed attempts to log in or of sessions which timed out without proper log out. */ String Result;
public String getResult() { return Result; }
public void setResult(String Result) {
  ResultChanged=BaseSQLClass.twoParms(this.Result, Result);
  this.Result = Result;
}

// 1 to 1 references from LogInHistory

protected dbaccess.Person PersonPersonIDPersonIDObj;
public dbaccess.Person fetchOnePersonPersonIDPersonID() { try {
  if (PersonPersonIDPersonIDObj != null) { return PersonPersonIDPersonIDObj;}
  return(PersonPersonIDPersonIDObj =
    dbaccess.bridge._Person.fetchOne("PersonID", (long)PersonID));
  } catch (NullPointerException e) { return null; }}

// 1 to many references from LogInHistory

public void cleanDirtyFlags() {
  PersonIDChanged=false;
  UserAgentChanged=false;
  FromHostChanged=false;
  FromIPChanged=false;
  ResultChanged=false;
}

public boolean testDirtyFlags() { return (
  PersonIDChanged ||
  UserAgentChanged ||
  FromHostChanged ||
  FromIPChanged ||
  ResultChanged);
}

public static final String primaryKey = "PersonID";
public static String fetchPrimaryKey() { return primaryKey; }
public static LogInHistory fetchByPrimaryKey(Long colValue) {
  return (LogInHistory)BaseSQLClass.getOne(LogInHistory.class, null, primaryKey, colValue);
}
public static boolean hasPrimaryKey() { return false; }
}
