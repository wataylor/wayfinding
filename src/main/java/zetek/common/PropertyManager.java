/* @name PropertyManager.java

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

package zetek.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * Manage properties to handle user-selected defaults.
 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class PropertyManager extends Properties {

  public static final long serialVersionUID = 1;

  public static NumberFormat keyDigs = NumberFormat.getIntegerInstance();
  static {
    keyDigs.setMinimumIntegerDigits(3);
  }

  public static final String defPropFile = ".zetek.manager.properties";

  /** Current property file name. */
  public String propFileName;
  /** Stores the result of the most recent read or write operations.
   * If this value is non-null, an error has occurred. */
  public String recentResult;

  /** Creates and manages the default property file object in XML.
   * The default property file goes in the current user home
   * directory.  */
  public PropertyManager() {
    propFileName = System.getProperty("user.home") + File.separator +
      defPropFile;

    recentResult = loadPropertyFile(propFileName);
  }

  /** Create and manage a property object based on the specified file
   * name.
   * @param propFileName name of the file to be read to set initial property
   * values.  If it is null or the empty string, no initial values are set.
   * Calling this constructor with null and then setting the file name is
   * equivalent to clearing the property file after reading it except that it
   * saves I/O.  */
  public PropertyManager(String propFileName) {
    if ((propFileName != null) && (propFileName.length() > 0)) {
      this.propFileName = propFileName;
      recentResult = loadPropertyFile(propFileName);
    }
  }

  public String loadPropertyFile(String fileName) {
    if ((fileName == null) || (fileName.length() <= 0)) {
      return (recentResult = "No file name to load");
    }
    File file = new File(fileName);
    String retString = null;

    if (!file.canWrite()) {
      setDefaults();
    } else {
      try {
        loadFromXML(new BufferedInputStream(new FileInputStream(file)));
      } catch (InvalidPropertiesFormatException e) {
        retString = fileName + " has invalid format\n" + e.toString();
      } catch (FileNotFoundException e) {
        retString = fileName + " was not found";
      } catch (IOException e) {
        retString = fileName + " had an I/O Exception\n" + e.toString();
      }
    }
    return (recentResult = retString);
  }

  public void setDefaults() {
    // setProperty("directory", "");
  }

  public String writePropertyFile(String propFileName, String comment) {
    this.propFileName = propFileName;
    return writePropertyFile(comment);
  }

  public String writePropertyFile(String comment) {
    String retString = null;

    setProperty("now", (new Date()).toString());

    if ((propFileName == null) || (propFileName.length() <= 0)) {
      retString = "Property file name is not set.";
    } else {
      try {
	storeToXML(new BufferedOutputStream(new FileOutputStream(new File(propFileName))), comment);
      } catch (FileNotFoundException e) {
	retString = propFileName + " was not found\n" + e.toString();
      } catch (IOException e) {
	retString = propFileName + " had an I/O Exception\n" + e.toString();
      }
    }
    return (recentResult = retString);
  }

  /**
   * Return a string array containing the keys sorted into string order
   * @return sorted keys.
   */
  public String[] sortedStringKeys() {
    Set<String> keys = stringPropertyNames();
    String[] os = keys.toArray(new String[0]);
    Arrays.sort(os);
    return os;
  }

  /**
   * Get rid of all properties whose keys begin with the specified
   * string.  This is called in preparation for specifying a list of
   * properties in a category.
   * @param cat key category to eliminate
   */
  public void clearKeyCategory(String cat) {
    String[] keys = sortedStringKeys();
    for (String k : keys) {
      if (k.startsWith(cat)) { remove(k); }
    }
  }

  /** Add a specified category of properties
   * @param cat property category, all properties are added given
   * numbers starting with 000 to maintain the order in which the
   * properties are supplied.
   * @param inputs list of strings which are to be stored as
   * properties under the specified category.
   */
  public void addListMembers(String cat, List<String> inputs) {
    addListMembers(cat, inputs.toArray(new String[0]));
  }

  /** Add a specified category of properties
   * @param cat property category, all properties are added given
   * numbers starting with 000 to maintain the order in which the
   * properties are supplied.  Order is not guaranteed for more than
   * 1,000 members of a category; this could be fixed by changing
   * <code>keyDigs</code>
   * @param inputs list of strings which are to be stored as
   * properties under the specified category.
   */
  public void addListMembers(String cat, String[] inputs) {
    clearKeyCategory(cat);
    int i = 0;

    for (String v : inputs) {
      setProperty(cat + keyDigs.format(i++), v);
    }
  }

  /**
   * Return an ordered list of all properties which fall into a category
   * @param cat name of the property category
   * @return ordered list
   */
  public List<String> getCategoryList(String cat) {
    ArrayList<String> ls = new ArrayList<String>();
    String[] keys = sortedStringKeys();

    for (String s : keys) {
      if (s.startsWith(cat)) { ls.add(getProperty(s)); }
    }
    return ls;
  }

  /**
   * Return an ordered array of all properties which fall into a category
   * @param cat name of the property category
   * @return ordered array
   */
  public String[] getCategoryArray(String cat) {
    return getCategoryList(cat).toArray(new String[0]);
  }
}
