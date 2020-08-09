package dbaccess.bridge;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;import java.util.ArrayList;
import zetek.dbcommon.BaseSQLClass;
import dbaccess.Language;

public class _Language extends BaseSQLClass implements Serializable {
  private static final long serialVersionUID=1;

  public _Language() {}

public static List<Language> fetchMany(String colName, Object colValue) {
return (List<Language>)BaseSQLClass.getMany(Language.class, null, colName, colValue);
}

public static Language fetchOne(String colName, Object colValue) {
return (Language)BaseSQLClass.getOne(Language.class, null, colName, colValue);
}

public boolean NameChanged;
/** English name of the language */ String Name;
public String getName() { return Name; }
public void setName(String Name) {
  NameChanged=BaseSQLClass.twoParms(this.Name, Name);
  this.Name = Name;
}

public boolean AbbrevChanged;
/** Standard abbreviation of the language name as in EN, FR, etc. */ String Abbrev;
public String getAbbrev() { return Abbrev; }
public void setAbbrev(String Abbrev) {
  AbbrevChanged=BaseSQLClass.twoParms(this.Abbrev, Abbrev);
  this.Abbrev = Abbrev;
}

public boolean LanguageIDChanged;
/** Id to reference a language */ Long LanguageID;
public Long getLanguageID() { return LanguageID; }
public void setLanguageID(Long LanguageID) {
  LanguageIDChanged=BaseSQLClass.twoParms(this.LanguageID, LanguageID);
  this.LanguageID = LanguageID;
}

// 1 to 1 references from Language

// 1 to many references from Language

protected List<dbaccess.Person> PersonLanguageIDLanguageIDLst;
public void addPersonLanguageIDLanguageID(dbaccess.Person obj) {
  if (PersonLanguageIDLanguageIDLst == null) { PersonLanguageIDLanguageIDLst = new ArrayList<dbaccess.Person>();}
PersonLanguageIDLanguageIDLst.add(obj); }
public List<dbaccess.Person> fetchAllPersonLanguageIDLanguageID() { try {
  if (PersonLanguageIDLanguageIDLst != null) { return PersonLanguageIDLanguageIDLst;}
  return(PersonLanguageIDLanguageIDLst =
    dbaccess.bridge._Person.fetchMany("LanguageID", (long)LanguageID));
  } catch (NullPointerException e) { return null; }}

protected List<dbaccess.TermTranslation> TermTranslationLanguageIDLanguageIDLst;
public void addTermTranslationLanguageIDLanguageID(dbaccess.TermTranslation obj) {
  if (TermTranslationLanguageIDLanguageIDLst == null) { TermTranslationLanguageIDLanguageIDLst = new ArrayList<dbaccess.TermTranslation>();}
TermTranslationLanguageIDLanguageIDLst.add(obj); }
public List<dbaccess.TermTranslation> fetchAllTermTranslationLanguageIDLanguageID() { try {
  if (TermTranslationLanguageIDLanguageIDLst != null) { return TermTranslationLanguageIDLanguageIDLst;}
  return(TermTranslationLanguageIDLanguageIDLst =
    dbaccess.bridge._TermTranslation.fetchMany("LanguageID", (long)LanguageID));
  } catch (NullPointerException e) { return null; }}

public void cleanDirtyFlags() {
  NameChanged=false;
  AbbrevChanged=false;
  LanguageIDChanged=false;
}

public boolean testDirtyFlags() { return (
  NameChanged ||
  AbbrevChanged ||
  LanguageIDChanged);
}

public static final String primaryKey = "LanguageID";
public static String fetchPrimaryKey() { return primaryKey; }
public static Language fetchByPrimaryKey(Long colValue) {
  return (Language)BaseSQLClass.getOne(Language.class, null, primaryKey, colValue);
}
public static boolean hasPrimaryKey() { return true; }
}
