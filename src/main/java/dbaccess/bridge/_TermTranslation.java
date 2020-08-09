package dbaccess.bridge;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;import java.util.ArrayList;
import zetek.dbcommon.BaseSQLClass;
import dbaccess.TermTranslation;

public class _TermTranslation extends BaseSQLClass implements Serializable {
  private static final long serialVersionUID=1;

  public _TermTranslation() {}

public static List<TermTranslation> fetchMany(String colName, Object colValue) {
return (List<TermTranslation>)BaseSQLClass.getMany(TermTranslation.class, null, colName, colValue);
}

public static TermTranslation fetchOne(String colName, Object colValue) {
return (TermTranslation)BaseSQLClass.getOne(TermTranslation.class, null, colName, colValue);
}

public boolean TermChanged;
/** The actual translated term */ String Term;
public String getTerm() { return Term; }
public void setTerm(String Term) {
  TermChanged=BaseSQLClass.twoParms(this.Term, Term);
  this.Term = Term;
}

public boolean LanguageIDChanged;
/** The language this translation is for */ Long LanguageID;
public Long getLanguageID() { return LanguageID; }
public void setLanguageID(Long LanguageID) {
  LanguageIDChanged=BaseSQLClass.twoParms(this.LanguageID, LanguageID);
  this.LanguageID = LanguageID;
}

public boolean TermIDChanged;
/** The term this is a translation for */ Long TermID;
public Long getTermID() { return TermID; }
public void setTermID(Long TermID) {
  TermIDChanged=BaseSQLClass.twoParms(this.TermID, TermID);
  this.TermID = TermID;
}

// 1 to 1 references from TermTranslation

protected dbaccess.EnglishTerm EnglishTermTermIDTermIDObj;
public dbaccess.EnglishTerm fetchOneEnglishTermTermIDTermID() { try {
  if (EnglishTermTermIDTermIDObj != null) { return EnglishTermTermIDTermIDObj;}
  return(EnglishTermTermIDTermIDObj =
    dbaccess.bridge._EnglishTerm.fetchOne("TermID", (long)TermID));
  } catch (NullPointerException e) { return null; }}

protected dbaccess.Language LanguageLanguageIDLanguageIDObj;
public dbaccess.Language fetchOneLanguageLanguageIDLanguageID() { try {
  if (LanguageLanguageIDLanguageIDObj != null) { return LanguageLanguageIDLanguageIDObj;}
  return(LanguageLanguageIDLanguageIDObj =
    dbaccess.bridge._Language.fetchOne("LanguageID", (long)LanguageID));
  } catch (NullPointerException e) { return null; }}

// 1 to many references from TermTranslation

public void cleanDirtyFlags() {
  TermChanged=false;
  LanguageIDChanged=false;
  TermIDChanged=false;
}

public boolean testDirtyFlags() { return (
  TermChanged ||
  LanguageIDChanged ||
  TermIDChanged);
}

public static final String primaryKey = "TermID";
public static String fetchPrimaryKey() { return primaryKey; }
public static TermTranslation fetchByPrimaryKey(Long colValue) {
  return (TermTranslation)BaseSQLClass.getOne(TermTranslation.class, null, primaryKey, colValue);
}
public static boolean hasPrimaryKey() { return true; }
}
