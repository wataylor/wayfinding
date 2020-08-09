/* @name IDBCounter.java

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

/**
 * Each use of the table class generation system must supply a class
 * which generates new database keys in the proper manner.  It is up
 * to the callers to know whether a table contains entities or
 * ordinary objects.

 * @author W Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public interface IDBCounter {

  /**
   * Get a new ID value from the local counter table.  This value may
   * be replicated across other machines depending on how many objects
   * have been created, so this count alone is not sufficient to be
   * unique, The result is ORed with the machine ID to make it unique.
   * This method does not do anything to the Entity table.*/
  public long makeNewID();

  /**
   * Return a new entity ID.  Each machine has a separate count table
   * which has an auto-incrementing count field.  The value of this
   * field is ORed with the local data source ID in the high order 16
   * bits to create a system-wide unique ID for the entity.  All
   * entity IDs are unique regardless of entity type or data source.
   * The master Entity table is updated with an empty entry so that
   * foreign keys can refer to it and add information about the entity.
   * @return a new and unique entity ID
   */
  public long makeNewEntityID();

  /** Records the entity type when the entity is created.  The type is
   * an unsigned short in the database, but java does not have such a
   * type, so the type is passed as an int.
   * @param entityType selects an entity type from the entity type
   * table.*/
  public long makeNewEntityID(int entityType);
}
