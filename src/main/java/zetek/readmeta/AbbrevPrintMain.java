/* @name AbbrevPrintMain.java

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

package zetek.readmeta;

import zetek.readmeta.classes.SpaceNameAdjust;

/**
 * Print the abbreviation list in a format suitable for use

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class AbbrevPrintMain {

  public static final long serialVersionUID = 1;

  /** Obligatory constructor.*/
  public AbbrevPrintMain() { /* */ }

  public static void twoStrings(String[][] ray) {
    System.out.println();
    for (String[] s : ray) {
      System.out.println(s[0] + "\t" + "-> " + s[1]);
    }
  }

  /**
   * @param args ignored
   */
  public static void main(String[] args) {
    System.out.print("The meta data software expands abbreviations when processing ");
    System.out.print("space names which are found in the .dwg database.  An abbreviation must be at ");
    System.out.print("the beginning of the name and it must end with a space.  Thus, the program checks ONLY the first word of the name against the abbreviation list.  Abbreviations are independent of case ");
    System.out.print("and may or may not be followed by a period.  In addition to expanding abbreviations, the program replaces sny number of ");
    System.out.print("successive spaces by one space.  All words ");
    System.out.println("are converted to initial caps regardless of their initial case.");
    System.out.println();

    System.out.print("The abbreviation is processed ONLY when it is the first word of ");
    System.out.print("the name.  If the abbreviation is the ONLY word in the name, it is ");
    System.out.print("NOT processed.  Thus, a name for a vertical consisting of only \"el\" ");
    System.out.println("or \"el.\" would be changed to initial caps but would not be expanded to \"Elevator\" as expected.");
    System.out.println();

    System.out.print("Each type of space has its own abbreviation list.  If, for example, a door ");
    System.out.print("name starts with \"bd\" which is a room-specific ");
    System.out.print("abbreviation for \"bedroom,\" the abbreviation is ignored because that abbreviation ");
    System.out.println("is only applied to room names.");
    System.out.println();

    System.out.println("There is also a universal abbreviation list which is applied at the beginning of any space name EXCEPT for verticals.  Vertical names are treated differently because vertical names must be consistent from floor to floor and the universal abbreviations do not generally apply to verticals such as shafts and elevators.  This list is ignored if a space-specific abbreviation is expanded.");
    System.out.println();

    System.out.print("There is also a universal abbreviation list which is applied ");
    System.out.print("anywhere WITHIN a name provided it is bounded on both sides by ");
    System.out.print("spaces.  Thus, \" rm \" and \" rm. \" are replaced by \" room \" anywhere in a name.  Since ");
    System.out.print("these abbreviations must be bounded by spaces to make them complete words, they have no effect ");
    System.out.println("at the beginning or end of a space name.  Any number of these abbreviations may be applied regardless of whether any other abbreviation is expanded at the beginning of the name.");

    System.out.println("\nAbbreviations which apply to doors:");
    twoStrings(SpaceNameAdjust.doorAbbrevs);

    System.out.println("\nAbbreviations which apply to halls:");
    twoStrings(SpaceNameAdjust.hallAbbrevs);

    System.out.println("\nAbbreviations which apply to verticals:");
    twoStrings(SpaceNameAdjust.vertAbbrevs);

    System.out.println("\nAbbreviations which apply to rooms:");
    twoStrings(SpaceNameAdjust.roomAbbrevs);

    System.out.print("\nAbbreviations which are applied to all space types EXCEPT VERTICALS if none of the type-specific abbreviations is applied to the name.  ");
    System.out.println("For example, a door named \"ap 03057\" would be renamed \"Access Panel 03057\" because \"ap\" is a general abbreviation for \"Access Panel.\"");
    twoStrings(SpaceNameAdjust.generalAbbrevs);

    System.out.print("\nThese abbreviations may be embedded anywhere in a space name, BUT they MUST be surrounded by spaces.  For example, a room named \"t rm.\" would be renamed \"Toilet Rm.\" because \"t\" is a room-specific abbreviation for \"Toilet.\"  The \"rm\" abbreviation would be ignored because \"Rm.\" is not followed by a space in the original name.  Putting a space after the name would not work because leading and trailing spaces are discarded before the name is examined.  If, however, the room number follows the name as in \"t rm. 0573,\" the \"rm\" is surrounded by spaces and the result is \"Toilet Room 0573\" as expected.  ");
    System.out.println("Thus, a room name of \"el equip enc rm 40045\" expands to \"Elevator Equipment Enclosure Room 40045\".  \"el\" is a room-specific abbreviation for \"Elevator\" and the other embedded abbreviations are expanded one at a time because they are surrounded by spaces.");
    twoStrings(SpaceNameAdjust.embeddedAbbrevs);
  }

}
