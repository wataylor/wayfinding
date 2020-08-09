/* @name PropNames.java

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

/**
 * Define the property names which are used when passing property data
 * between programs.  Defining these data in one place minimizes errors.</p>

 * <P>Property names are used by the meta data manager and by the database
 * maintenance modules in addition to the read meta data modules.

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class PropNames {

  public static final long serialVersionUID = 1;

  /** Obligatory constructor.*/
  public PropNames() { /* */ }

  /** Name of the property that holds recent file names.*/
  public static final String RF_PROP       = "RfUs";
  /** Name of the property that holds the current directory from which
   * the .xls file came and which stores all of the.dwg files. */
  public static final String DWG_DIR_PROP  = "dwgDir";
  /** All working text and .xml files are stored in a working
   * directory.  This directory name is stored in this property.*/
  public static final String WORK_DIR_PROP = "workDir";
  public static final String BOTTOM_STREET_PROP = "bS";
  public static final String DEFAULT_LOC_PROP   = "dL";
  public static final String BUILD_NAME_PROP    = "buildingName";
  public static final String BUILD_NIK_NAME_PROP= "buildNikName";
  public static final String BUILD_ZIP_PROP     = "buildingZip";
  public static final String ERRORS_PROP        = "errors";
  public static final String ERROR_COUNT_PROP   = "errCount";
  public static final String FLOOR_DATA_PROP    = "floor";
  public static final String FRAME_RST_PROP     = "fRr";
  public static final String FROM_PROP          = "fM";
  public static final String INFOS_PROP         = "infos";
  public static final String LEFT_STREET_PROP   = "lS";
  public static final String RIGHT_STREET_PROP  = "rS";
  public static final String TEMP_PROP          = "Tp";
  public static final String TOP_STREET_PROP    = "tS";
  public static final String TO_PROP            = "tO";

  /** Property list property for main frame bounds*/
  public static final String BLDG_FRAME_PROP   = "BuildingFrame";
  /** Comment put in the .properties file to identify it*/
  public static final String BLDG_PROP_COMMENT = "Building Floor Data";

  /** This string is put in front of a .dwg name to produce a name for the
   * pipe-separated list of spaces which are known not to connect on that
   * floor.*/
  public static final String ISOLATED_PRE_PROP  = "iX";
  /** This string is put in front of a .dwg name to produce a name for the
   *  pipe-separated list of all the vertical connections to the floor.*/
  public static final String VERT_CONN_PROP     = "vX";
  /** This string is put in front of a .dwg name to produce a name for the
   *  pipe-separated list of all the external doors which give access to the
   *  floor.*/
  public static final String EXTERIOR_DOOR_PROP = "dX";

  /** This string is put in front of a .dwg name to produce a name for the
   *  multi-valued property which gives a pipe-separated string for each of
   *  the external doors which give access to the floor and their street
   *  addresses.*/
  public static final String EXTERIOR_ADDR_PROP = "AdX";

  /** Comment to identify the main manager property file. */
  public static final String PROP_COMMENT = "Zetek Wayfinding Metadata Manager";

  /** Command line key word to identify a floor DWG file name without .dwg
   * extension.*/
  public static final String DWG_KEY       = "dw";
  /** Command line key word to identify the building property file path*/
  public static final String BUIL_PROP_KEY = "bp";

  /** The name of the working directory is formed by adding this sub
   * directory to the directory which holds all of the .dwg files.*/
  public static final String WORK_DIR_NAME      = "workFiles";
}
