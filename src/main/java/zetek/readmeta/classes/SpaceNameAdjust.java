/* @name SpaceNameAdjust.java

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

package zetek.readmeta.classes;

import zetek.graphserve.SGB;

/**
 * This module expands various abbreviations which are applied to
 * space names found in the .dwg database.  An abbreviation must be at
 * the beginning of the name.  Abbreviations are independent of case
 * and may or may not be followed by a period.  Any number of
 * successive spaces are replaced by one space.  All words in the
 * final string are converted to initial caps regardless of initial
 * case.</p>

 * <P>The abbreviation is processed ONLY when it is the first word of
 * the name.  If the abbreviation is the ONLY word in the name, it is
 * NOT processed.  Thus, a name for a vertical consisting of only "el"
 * or "el." would only be changed to initial caps.</p>

 * <P>Each type of space has its own abbreviation list.  If a door
 * name starts with, for example, "bd" which is a room-specific
 * abbreviation for "bedroom," it is ignored because that abbreviation
 * is only applied to room names.</p>

 * <P>There is also a universal abbreviation list which is applied
 * anywhere WITHIN a name provided it is bounded on both sides by
 * spaces.  Thus, " rm " and " rm. " are replaced by " room ".  Since
 * these abbreviations must be bounded by spaces, they have no effect
 * at the beginning or end of the label.  Thus,

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class SpaceNameAdjust {

  public static final long serialVersionUID = 1;

  /** General abbreviations are applied to all space types except for
   * verticals.  Verticals are only processed against the list of
   * abbreviations for verticals.*/
  public static final String[][] generalAbbrevs = {
    {"ap",   "Access Panel"},
    {"arch", "Architectural"},
    {"bsmt", "Basement"},
    {"bldg", "Building"},
    {"col",  "Column"},
    {"equip", "Equipment"},
    {"exh",  "Exhaust"},
    {"ext",  "Exterior"},
    {"fnd",  "Foundation"},
    {"int",  "Interior"},
    {"mach", "Machine"},
    {"maint","Maintenance"},
    {"mech", "Mechanical"},
    {"mep",  "Mechanical/Electrical/Plumbing"},
    {"out",  "Outside" },
    {"struct", "Structural"},
  };

  /** Room abbreviations.*/
  public static final String[][] roomAbbrevs = {
    {"bd",    "Bedroom"},
    {"cl",    "Closet"},
    {"clo",   "Closet"},
    {"clos",  "Closet"},
    {"col",   "Column"},
    {"conf",  "Conference"},
    {"el",    "Elevator"}, // Repeated from verticals because of elevator lobby
    {"elev",  "Elevator"},
    {"elect", "Electrical"},
    {"encl",  "Enclosure"},
    {"furn",  "Furnace"},
    {"hvac",  "Heating, Venting & Air Conditioning"},
    {"jan",   "Janitor"},
    {"kit",   "Kitchen"},
    {"lab",   "Laboratory"},
    {"lob",   "Lobby"},
    {"m. bath", "Master Bathroom"}, // Periods are required after m
    {"m. bed",  "Master Bedroom"}, // Periods are required after m
    {"mech",  "Mechanical"},
    {"ofc",   "Office"},
    {"off",   "Office"},
    {"pant",  "Pantry"},
    {"pd",    "Powder Room"},
    {"rm",    "Room"},
    {"sto",   "Storage"},
    {"st",    "Stair"},		// Required for stair access lobbies
    {"str",   "Stair"},
    {"t",     "Toilet"},
    {"tel",   "Telephone"},
    {"vest",  "Vestibule"},
    {"wc",    "Water Closet"},
    {"wh",    "Water Heater"},
  };

  /** Hall abbreviations*/
  public static final String[][] hallAbbrevs = {
    {"corr", "Corridor"},		// "Hall" is not used
    {"hall", "Corridor"},
  };

  /** Vertical abbreviations */
  public static final String[][] vertAbbrevs = {
    {"dn",   "Down"},
    {"el",   "Elevator"},
    {"elev", "Elevator"},
    {"es",   "Escalator"},
    {"esc",  "Escalator"},
    {"sh",   "Shaft"},
    {"shft", "Shaft"},
    {"st",   "Stair"},
    {"str",  "Stair"},
    {"up",   "Up"},
  };

  /* Door / connector abbreviations */
  public static final String[][] doorAbbrevs = {
    {"dr",  "Door"},
    {"ent", "Entrance"},
    {"me",  "Main Entrance"},
  };

  /** These abbreviations are substituted regardless of where they
   * occur in the string.  Note that the substitutions are bracketed
   * by spaces, so the substitution does not happen unless they are
   * stand-alone words.  It also does not happen if they fall at the
   * end of a name.  This should not usually be a problem because room
   * names end with numbers, so "rm." should not fall at the end.*/
  public static final String[][] embeddedAbbrevs = {
    {" encl ",   " enclosure "},
    {" encl. ",  " enclosure "},
    {" enc ",    " enclosure "},
    {" enc. ",   " enclosure "},
    {" equip ",  " equipment "},
    {" equip. ", " equipment "},
    {" equipt ", " equipment "},
    {" ofc ",    " office "},
    {" ofc. ",   " office "},
    {" lob ",    " lobby "},
    {" lob. ",   " lobby "},
    {" lby ",    " lobby "},
    {" lby. ",   " lobby "},
    {" sto ",    " storage "},
    {" sto. ",   " storage "},
    {" rm ",     " room " },
    {" rm. ",    " room "},
  };

  /** Obligatory constructor.*/
  public SpaceNameAdjust() { /* */ }

  /**
   * Convert a string to initial caps.  Extra white space is
   * eliminated so that any sequence of consecutive white space
   * characters is reduced to the initial space.
   * @param input string to be converted
   * @param subs if not null, is a 2-dimensional array of strings to be
   * substituted in the input string regardless of where they occur.
   * The usual practice is to surround the strings with spaces so that
   * substitutions occur only on complete words.
   * @return output string
   */
  public static String makeInitCaps(String input, String[][] subs) {
    if (input == null) { return null; }
    // Note that there is no trailing or leading white space due to the trim
    StringBuffer sb = new StringBuffer(input.toLowerCase().trim());
    boolean hadSpace = true;
    char ch;
    int i;
    int ix;

    if (subs != null) {
      for (i=0; i<subs.length; i++) {
        if ( (ix = sb.indexOf(subs[i][0])) >= 0) {
          sb.replace(ix, ix+subs[i][0].length(), subs[i][1]);
        }
      }
    }

    for (i=0; i<sb.length(); i++) {
      if (hadSpace) {
	if (Character.isLowerCase(ch = sb.charAt(i))) {
	  sb.setCharAt(i, Character.toUpperCase(ch));
	  hadSpace = false;
	} else if (Character.isWhitespace(ch)) {
	  sb.deleteCharAt(i);
	  while (Character.isWhitespace(sb.charAt(i))) {
	    sb.deleteCharAt(i);
	  }
	  if (Character.isLowerCase(ch = sb.charAt(i))) {
	    sb.setCharAt(i, Character.toUpperCase(ch));
	    hadSpace = false;
	  }
	} else {
	  hadSpace = false;
	}
      } else {
	if (Character.isWhitespace(ch = sb.charAt(i)) || (ch == '.')) {
	  hadSpace = true;
	}
      }
    }
    return sb.toString();
  }

  /**
   * Substitute the substitution string for the first word of the name
   * @param sub substitution string
   * @param name string whose first word is to be replaced
   * @return substituted string
   */
  public static String subIn(String sub, String name) {
    int ix;

    if (name == null)                   { return null; }
    if ( (ix = name.indexOf(" ")) < 0) { return name; }
    return sub + name.substring(ix);
  }

  /**
   * Check a string against an array of abbreviations
   * @param comp string to compare against the abbreviations
   * @param name original string
   * @param abbrevs array of abbreviations and their expansions
   * @param useGlobal if true, try the global array if there is no match
   * @return modified string
   */
  public static String substituteOne(String comp, String name,
				     String[][] abbrevs, boolean useGlobal) {
    int i;
    for (i=0; i<abbrevs.length; i++) {
      if (comp.equals(abbrevs[i][0])) {
	return makeInitCaps(subIn(abbrevs[i][1], name), embeddedAbbrevs);
      }
    }

    if (useGlobal) {
      for (i=0; i<generalAbbrevs.length; i++) {
	if (comp.equals(generalAbbrevs[i][0])) {
	  return makeInitCaps(subIn(generalAbbrevs[i][1], name), embeddedAbbrevs);
	}
      }
    }
    return makeInitCaps(name, embeddedAbbrevs);
  }

  /**
   * Convert a space name from the .dwg database to a canonical form
   * by expanding abbreviations.
   * @param name original space name
   * @param ilk indicate what type of space is being named
   * @return name converted by expanding initial abbreviations and
   * changing case to initial caps.
   */
  public static String adjustSpaceName(String name, short ilk) {
    int ix;
    String comparand;

    if (name == null)                   { return null; }
    name = name.replace("_", " "); // All underscores are turned to spaces
    if ( (ix = name.indexOf(" ")) <= 0) { return makeInitCaps(name, embeddedAbbrevs); }
    comparand = name.substring(0, ix).toLowerCase();
    if (comparand.endsWith(".")) {
      comparand = comparand.substring(0, comparand.length()-1);
    }
    if (comparand.length() <= 0)        { return makeInitCaps(name, embeddedAbbrevs); }

    switch (ilk) {
    case SGB.DOOR_ILK:
      return substituteOne(comparand, name, doorAbbrevs, true);
    case SGB.HALL_ILK:
      return substituteOne(comparand, name, hallAbbrevs, true);
    case SGB.ROOM_ILK:
      return substituteOne(comparand, name, roomAbbrevs, true);
    case SGB.VERTICAL_ILK:
      return substituteOne(comparand, name, vertAbbrevs, false);

    case SGB.LOCATION_ILK:
      return makeInitCaps(name, embeddedAbbrevs);

    default:			// Explicitly show that default drops through
      break;
    }
    return substituteOne(comparand, name, generalAbbrevs, false);
  }
}
