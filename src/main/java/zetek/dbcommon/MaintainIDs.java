/* @name MaintainIDs.java

   Copyright (c) 2002-2008 Zetek Corporation (All Rights Reserved)

-------- Licensed Software Proprietary Information Notice -------------

This software is a working embodiment of certain trade secrets of
Zetek Corporation.  The software is licensed only for the
day-to-day business use of the licensee.  Use of this software for
reverse engineering, decompilation, use as a guide for the design of a
competitive product, or any other use not for day-to-day business use
is strictly prohibited.

All screens and their formats, color combinations, layouts, and
organization are proprietary to and copyrighted by Zetek Corporation.

All rights are reserved.

Authorized Zetek customer use of this software is subject to the
terms and conditions of the software license executed between Customer
and Zetek Corporation.

------------------------------------------------------------------------

*/

package zetek.dbcommon;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Module to maintain entity IDs for the wayfinding database.

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class MaintainIDs implements IDBCounter {

  public static final long serialVersionUID = 1;

  /** Obligatory constructor.*/
  public MaintainIDs() { /* */ }

  @Override
  public long makeNewEntityID() {
    return makeNewEntityID(0);
  }

  @Override
  public long makeNewEntityID(int entityTypeID) {
    Statement stmt = null;
    long newID = makeNewID();

    try {
      stmt = BaseSQLClass.dbParams.conn.createStatement();
      stmt.execute("insert into Entity (EntityID, SourceID, EntityTypeID) values (" +
                   newID + "," + BaseSQLClass.dbParams.machineID + "," + entityTypeID + ")");
      return newID;
    } catch (SQLException sqlE) {
      throw new RuntimeException(sqlE.toString());
    } catch (Exception e) {
      BaseSQLClass.logger.error("New ID", e);
      throw new RuntimeException(e.toString());
    } finally {
      BaseSQLClass.closeStatementAndResult(stmt, null);
    }
   }

  @Override
  public long makeNewID() {
    Statement stmt = null;
    ResultSet res  = null;
    long newID;
    long newCounter;
    String query = "insert into Counter (MachineID) values (" +
      BaseSQLClass.dbParams.machineID + ")";

    try {
      stmt = BaseSQLClass.dbParams.conn.createStatement();
      stmt.execute(query);
      res = stmt.executeQuery("select LAST_INSERT_ID()");
      if (res.next()) {
        newCounter = res.getLong(1);
        newID = newCounter | ((long) BaseSQLClass.dbParams.machineID) << 48;
        return newID;
      }
      throw new RuntimeException("No new count");
    } catch (SQLException sqlE) {
      BaseSQLClass.logger.error(query, sqlE);
      throw new RuntimeException(sqlE);
    } catch (Exception e) {
      BaseSQLClass.logger.error("New count " + query, e);
      throw new RuntimeException(e.toString());
    } finally {
      BaseSQLClass.closeStatementAndResult(stmt, res);
    }
  }
}
