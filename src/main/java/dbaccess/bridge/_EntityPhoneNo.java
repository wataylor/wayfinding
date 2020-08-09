package dbaccess.bridge;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;import java.util.ArrayList;
import zetek.dbcommon.BaseSQLClass;
import dbaccess.EntityPhoneNo;

public class _EntityPhoneNo extends BaseSQLClass implements Serializable {
  private static final long serialVersionUID=1;

  public _EntityPhoneNo() {}

public static List<EntityPhoneNo> fetchMany(String colName, Object colValue) {
return (List<EntityPhoneNo>)BaseSQLClass.getMany(EntityPhoneNo.class, null, colName, colValue);
}

public static EntityPhoneNo fetchOne(String colName, Object colValue) {
return (EntityPhoneNo)BaseSQLClass.getOne(EntityPhoneNo.class, null, colName, colValue);
}

public boolean EntityPhoneNoIDChanged;
/** Uniquely identifies a phone number */ Long EntityPhoneNoID;
public Long getEntityPhoneNoID() { return EntityPhoneNoID; }
public void setEntityPhoneNoID(Long EntityPhoneNoID) {
  EntityPhoneNoIDChanged=BaseSQLClass.twoParms(this.EntityPhoneNoID, EntityPhoneNoID);
  this.EntityPhoneNoID = EntityPhoneNoID;
}

public boolean AreaCodeChanged;
/**  */ Long AreaCode;
public Long getAreaCode() { return AreaCode; }
public void setAreaCode(Long AreaCode) {
  AreaCodeChanged=BaseSQLClass.twoParms(this.AreaCode, AreaCode);
  this.AreaCode = AreaCode;
}

public boolean ExchangeChanged;
/**  */ Long Exchange;
public Long getExchange() { return Exchange; }
public void setExchange(Long Exchange) {
  ExchangeChanged=BaseSQLClass.twoParms(this.Exchange, Exchange);
  this.Exchange = Exchange;
}

public boolean PhoneNumberTypeIDChanged;
/** The type of number this is */ Long PhoneNumberTypeID;
public Long getPhoneNumberTypeID() { return PhoneNumberTypeID; }
public void setPhoneNumberTypeID(Long PhoneNumberTypeID) {
  PhoneNumberTypeIDChanged=BaseSQLClass.twoParms(this.PhoneNumberTypeID, PhoneNumberTypeID);
  this.PhoneNumberTypeID = PhoneNumberTypeID;
}

public boolean CountryIDChanged;
/** Determines the format of the other parts of the number */ Long CountryID;
public Long getCountryID() { return CountryID; }
public void setCountryID(Long CountryID) {
  CountryIDChanged=BaseSQLClass.twoParms(this.CountryID, CountryID);
  this.CountryID = CountryID;
}

public boolean ExchNumberChanged;
/**  */ Long ExchNumber;
public Long getExchNumber() { return ExchNumber; }
public void setExchNumber(Long ExchNumber) {
  ExchNumberChanged=BaseSQLClass.twoParms(this.ExchNumber, ExchNumber);
  this.ExchNumber = ExchNumber;
}

public boolean EntityIDChanged;
/** The entity the number calls */ Long EntityID;
public Long getEntityID() { return EntityID; }
public void setEntityID(Long EntityID) {
  EntityIDChanged=BaseSQLClass.twoParms(this.EntityID, EntityID);
  this.EntityID = EntityID;
}

public boolean RetractedChanged;
/** non-null means number retracted */ Timestamp Retracted;
public Timestamp getRetracted() { return Retracted; }
public void setRetracted(Timestamp Retracted) {
  RetractedChanged=BaseSQLClass.twoParms(this.Retracted, Retracted);
  this.Retracted = Retracted;
}

public boolean ExtentionChanged;
/**  */ Long Extention;
public Long getExtention() { return Extention; }
public void setExtention(Long Extention) {
  ExtentionChanged=BaseSQLClass.twoParms(this.Extention, Extention);
  this.Extention = Extention;
}

// 1 to 1 references from EntityPhoneNo

protected dbaccess.Entity EntityEntityIDEntityIDObj;
public dbaccess.Entity fetchOneEntityEntityIDEntityID() { try {
  if (EntityEntityIDEntityIDObj != null) { return EntityEntityIDEntityIDObj;}
  return(EntityEntityIDEntityIDObj =
    dbaccess.bridge._Entity.fetchOne("EntityID", (long)EntityID));
  } catch (NullPointerException e) { return null; }}

protected dbaccess.PhoneNumberType PhoneNumberTypePhoneNumberTypeIDPhoneNumberTypeIDObj;
public dbaccess.PhoneNumberType fetchOnePhoneNumberTypePhoneNumberTypeIDPhoneNumberTypeID() { try {
  if (PhoneNumberTypePhoneNumberTypeIDPhoneNumberTypeIDObj != null) { return PhoneNumberTypePhoneNumberTypeIDPhoneNumberTypeIDObj;}
  return(PhoneNumberTypePhoneNumberTypeIDPhoneNumberTypeIDObj =
    dbaccess.bridge._PhoneNumberType.fetchOne("PhoneNumberTypeID", (long)PhoneNumberTypeID));
  } catch (NullPointerException e) { return null; }}

protected dbaccess.Country CountryCountryIDCountryIDObj;
public dbaccess.Country fetchOneCountryCountryIDCountryID() { try {
  if (CountryCountryIDCountryIDObj != null) { return CountryCountryIDCountryIDObj;}
  return(CountryCountryIDCountryIDObj =
    dbaccess.bridge._Country.fetchOne("CountryID", (long)CountryID));
  } catch (NullPointerException e) { return null; }}

// 1 to many references from EntityPhoneNo

public void cleanDirtyFlags() {
  EntityPhoneNoIDChanged=false;
  AreaCodeChanged=false;
  ExchangeChanged=false;
  PhoneNumberTypeIDChanged=false;
  CountryIDChanged=false;
  ExchNumberChanged=false;
  EntityIDChanged=false;
  RetractedChanged=false;
  ExtentionChanged=false;
}

public boolean testDirtyFlags() { return (
  EntityPhoneNoIDChanged ||
  AreaCodeChanged ||
  ExchangeChanged ||
  PhoneNumberTypeIDChanged ||
  CountryIDChanged ||
  ExchNumberChanged ||
  EntityIDChanged ||
  RetractedChanged ||
  ExtentionChanged);
}

public static final String primaryKey = "EntityPhoneNoID";
public static String fetchPrimaryKey() { return primaryKey; }
public static EntityPhoneNo fetchByPrimaryKey(Long colValue) {
  return (EntityPhoneNo)BaseSQLClass.getOne(EntityPhoneNo.class, null, primaryKey, colValue);
}
public static boolean hasPrimaryKey() { return true; }
}
