/* @name CommandArgs.java

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

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

/**
 * Utility to handle command-line arguments of the form key=value,
 * +key, -key, or plain strings.  Strings are stored in a linked list
 * in the order in which they are found on the command line.  Values
 * can be retrieved by referencing keys.  The value of a + or - key
 * will be a boolean object of TRUE or FALSE.  It is up to the caller
 * to know what type a given arg should be and to parse it
 * accordingly.</p>

 * <P>The calling program specifies defaults by setting a default
 * string before parsing the command line args.

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class CommandArgs extends Hashtable<String, Object> {

  public static final long serialVersionUID = 1;

  List<String> strings=new LinkedList<String>();

  /** Obligatory constructor.*/
  public CommandArgs() { /* */ }

  public CommandArgs(String[] args) {
    parseArgs(args);
  }

  /** Add whatever arguments are specified by the string array to the
   * hash table.
   * @param args array of argument strings.  Quotes are removed and
   * null or zero-length entries are skipped.  */
  public void parseArgs(String[] args) {
    String param;
    for(String arg : args) {
      if (arg == null) { continue; }
      // If a string begins with a quote, assume it has two and remove both
      if (arg.startsWith("\"")) { arg = arg.substring(1, arg.length()-1); }

      // Skip empty strings
      if (arg.length() <= 0) { continue; }

      param=arg.substring(1);
      if(arg.indexOf('=')>=0) {
        put(arg.substring(0, arg.indexOf('=')),
	    arg.substring(arg.indexOf('=')+1));
      } else if(arg.charAt(0)=='-') {
        put(param, Boolean.FALSE);
      } else if(arg.charAt(0)=='+') {
        put(param, Boolean.TRUE);
      } else {
        strings.add(arg);
      }
    }
  }

  /** Parameters which are not of the form name=value are stored in a
   * list in the order they are encountered on the command line.  This
   * method returns a list of all such command line parameters.*/
  public List<String> getStrings() {
    return strings;
  }

  /** Return the value of a parameter as a boolean; defaults to
   * <code>false</code> unless the parameter has been entered as
   * +&lt;param name&gt; in which case, it returns true
   * @param param name of the parameter to be returned.*/
  public boolean getBoolean(String param) {
    Boolean b;
    if ( (b = (Boolean)get(param)) == null) { return false; }
    return b.booleanValue();
  }

  /**
   * Convert a string to an integer, returning 0 on failure
   * @param va string which represents an integer
   * @return the value of the integer or 0 if the string is null or
   * does not represent a properly-formatted integer.
   */
  public static int integerFromString(String va) {
    try {
      return Integer.decode(va);
    } catch (Exception e) { /* */ }
    return 0;
  }

  /**
   * Get the value of a parameter as an integer.  Returns 0 if the
   * parameter is not found or if it is not a properly-formatted
   * integer
   * @param param name of the parameter to be converted to an integer
   * @return value of the integer or 0 if the parameter is not found
   */
  public int getInt(String param) {
    return integerFromString((String)get(param));
  }

  /**
   * Convert a string to a double, returning 0 if the string is null
   * or is not a properly-formatted double
   * @param va string to be converted
   * @return value of the string as a double or 0 if anything goes
   * wrong.
   */
  public static double doubleFromString(String va) {
    try {
      return Double.valueOf(va);
    } catch (Exception e) { /* */ }
    return 0;
  }

  /**
   * Return the value of a parameter as a double.  Returns 0 if the
   * parameter is not found or is not a properly-formatted double
   * @param param name of the desired parameter
   * @return value of the parameter as a double or 0 if it is not a
   * proper double
   */
  public double getDouble(String param) {
    return doubleFromString((String)get(param));
  }

  /**
   * Convert a string to a long, returning 0 if the string is null or
   * is not a properly-formatted long
   * @param va string to be converted
   * @return value of the string as a long or 0 if anything goes wrong.
   */
  public static long longFromString(String va) {
    try {
      return Long.decode(va);
    } catch (Exception e) { /* */ }
    return 0;
  }

  /**
   * Return the value of a parameter as a long, returns 0 if the
   * parameter is not found or it is not a properly-formatted long
   * @param param name of the parameter to be converted to long
   * @return value of the parameter as a long, returns 0 if the
   * parameter is not found
   */
  public long getLong(String param) {
    return longFromString((String)get(param));
  }

  /**
   * Convert a point to a string representation which is suitable for
   * use in command line parameters.  There are many plausible ways to
   * represent a point as a string, strings produced by this method
   * can be converted back to points by <code>pointFromString</code>.
   * X and Y are space-separated rather than comma-separated for the sake
   * of converting strings back to lines.
   * @param pt point to be converted
   * @return string value of the point.
   */
  public static String stringFromPoint(Point2D pt) {
    if (pt == null) { return ""; }
    return "[" + pt.getX() + " " + pt.getY() + "]";
  }

  /**
   * Represent a line as a text string.  The comma makes it easy to reverse
   * the process
   * @param ln the line
   * @return string representation of the line as two comma-separated points.
   */
  public static String stringFromLine(Line2D ln) {
    return stringFromPoint(ln.getP1()) + "," + stringFromPoint(ln.getP2());
  }

  public static Line2D lineFromString(String l) {
    try {
      String[] s = l.split(",");
      return new Line2D.Double(pointFromString(s[0]), pointFromString(s[1]));
    } catch (Exception e) {}
    return null;
  }
  /**
   * Convert a string generated by <code>stringFromPoint</code> to a
   * point.
   * @param p string representation of the point
   * @return point or null
   */
  public static Point2D pointFromString(String p) {
    Point2D ptd = null;
    double d;
    int ix;

    try {
      ix = p.indexOf(" ");
      d = Double.valueOf(p.substring(1, ix));
      ptd =
	new Point2D.Double(d, Double.valueOf(p.substring(ix+1, p.length()-1)));
    } catch (Exception e) {
      return null;
    }
    return ptd;
  }

  /**
   * Return the value of a parameter as a point.
   * @param param name of the value to be converted to a point
   * @return point described by the value of the parameter or null if
   * the parameter does not exist or is not properly formatted.
   */
  public Point2D getPoint(String param) {
    return pointFromString((String)get(param));
  }

  /**
   * Convert a Rectangle into a string.  There are many plausible
   * string representations of a Rectangle; this method is intended to
   * be compatible with <code>rectFromString</code>
   * @param r input rectangle
   * @return string representation of the rectangle
   */
  public static String stringFromRect(Rectangle r) {
    if (r == null) { return ""; }
    return "x" + r.x + "y" + r.y + "w" + r.width + "h" + r.height;
  }

  /**
   * Return a rectangle by parsing a string x y w h as generated by
   * <code>stringFromRect</code>
   * @param va string containing the properties of the rectangle
   * @return rectangle or null in case of errors
   */
  public static Rectangle rectFromString(String va) {
    int x=0;
    int y=0;
    int w=0;
    int h=0;
    int ix;
    int ix1;

    try {
      ix  = va.indexOf("x");
      ix1 = va.indexOf("y");
      x   = integerFromString(va.substring(ix+1, ix1).trim());
      ix  = va.indexOf("w");
      y   = integerFromString(va.substring(ix1+1, ix).trim());
      ix1 = va.indexOf("h");
      w   = integerFromString(va.substring(ix+1, ix1).trim());
      h   = integerFromString(va.substring(ix1+1).trim());
    } catch (Exception e) {
      return null;
    }
    return new Rectangle (x, y, w, h);
  }

  /** Return the value of a parameter as a rectangle.
   * @param param name of the parameter which is to be converted to a
   * Rectangle
   * @return the Rectangle or null if there are any errors
   */
  public Rectangle getRectangle(String param) {
    return rectFromString((String)get(param));
  }

  /**
   * Convert a Dimension into a string representation.  There are many
   * plausible string representations of a Dimension; this method is
   * intended to be compatible with <code>dimFromString</code>
   * @param dim
   * @return
   */
  public static String stringFromDim(Dimension dim) {
    return "" + dim.width + "x" + dim.height;
  }

  /**
   * Return a Dimension object from a String of the form wxh as
   * generated by <code>stringFromDim</code>
   * @param va String of the form width x height with no spaces
   * @return Dimension or null on any errors
   */
  public static Dimension dimFromString(String va) {
    int w = 0;
    int h = 0;
    int ix;
    try {
      ix = va.indexOf("x");
      w  = integerFromString(va.substring(0, ix).trim());
      h  = integerFromString(va.substring(ix+1).trim());
    } catch (Exception e) {
      return null;
    }
    return new Dimension(w, h);
  }

  /**
   * Return the value of a parameter as a Dimension object
   * @param param name of the parameter to be converted to a Dimension
   * @return Dimensio nobject or null if there are any errors.
   */
  public Dimension getDimension(String param) {
    return dimFromString((String)get(param));
  }

  /**
   * Put a new file type on a file name.
   * @param fileName original file name
   * @param ext new extension without a .  Passing a zero-length
   * string as the extension results in returning the original file
   * name without the "." or the original extension.  This is useful
   * for getting the file name without the extension.
   * @return file name with the file type replaced by the new
   * extension.
   */
  public static String newFileType(String fileName, String ext) {
    int ix;

    if (!fileName.endsWith("." + ext)) {
      if ( (ix = fileName.lastIndexOf(".")) >= 0) {
	fileName = fileName.substring(0, ix);
      }
    }
    if (ext.length() <= 0) { return fileName; }
    return fileName + "." + ext;
  }

  /**
   * Return the file name part of a file path
   * @param pathName the name of the path
   * @return the file name portion of the path
   */
  public static String fileNameOnly(String pathName) {
    File file = new File(pathName);
    return file.getName();
  }

  /**
   * Prepare a string for comparison with a string which is in standard format
   * @param in input string
   * @return trimmed, and with all redundant spaces removed.
   */
  public static String canonize(String in) {
    in = in.trim().replace("  ", " ");
    while (in.indexOf("  ") >= 0) { in = in.replace("  ", " "); }
    return in;
  }

  /** Convert a string to initial caps, which is sometimes known as
   * camel-case, also collapses successive white space characters to one
   * space.*/
  public static String initCaps(String input) {
    if (input == null) { return null; }
    // Note that there is no trailing or leading white space due to the trim
    StringBuilder sb = new StringBuilder(input.toLowerCase().trim());
    boolean hadSpace = true;
    char ch;
    int i;

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
}
