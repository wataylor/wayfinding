/* @name ProcessManager.java

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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Methods to handle executing tasks.  It is up to the caller to investigate
 * the operating system and issue a command which is appropriate for the
 * milieu.

 * @author WAT
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class ProcessManager {
  public static final long serialVersionUID = 1;

  private ProcessManager() {
  }

  /** Command and args which supply process information on a Linux
   * system. */
  public static final String[] pscmd = {"ps", "ax"};

  /** Clean out all instances of some task in preparation for a
   * start-up or shut down.  This should be done on start-up as well
   * as on shut down because it is difficult to guarantee a clean shut
   * down under all circumstances.*/
  public static List<String> shutDownMatchingTasks(String pattern) {
    String aLine;
    String[] cargs   = null;
    List<String>vec  = new ArrayList<String>();
    List<String>msgs = new ArrayList<String>();
    int ix;

    vec.add("kill");            // Put in the command

    try {
      BufferedReader lis = scrapeProc(pscmd);

      while ( (aLine = lis.readLine()) != null) {
        if (aLine.indexOf(pattern) > 0) { // Target task, PID is first field
	  aLine = aLine.trim(); // Get rid of leading spaces
          if ( (ix = aLine.indexOf(" ")) > 0) {
            vec.add(aLine.substring(0, ix));
          }
        }
      }

      if (vec.size() <= 1) {
	msgs.add("INFNo tasks matched " + pattern);
      } else {
        msgs.add("INF" + vec.size() + " task(s) matched " + pattern);
        cargs = new String[vec.size()];
        cargs = vec.toArray(cargs);
        Process p = new ProcessBuilder(cargs).start();
        p.waitFor();
      }
    } catch (Exception e) {
      msgs.add("ERRCommand " + stringArrayToString(cargs) + " " + e.toString());
    }
    return msgs;
  }

  /**
   * Prepare to screen-scrape a process by returning a line input
   * stream which supplies any output the command generates.
   * @param cargs the command line arguments which start the process.
   * @return line input stream which returns successive lines of
   * process output via the readLine() method.  Returns null on EOF or
   * any error.  Combines stdout and stderr.
   */
  public static BufferedReader scrapeProc(String[] cargs)
    throws IOException {
    ProcessBuilder pb = new ProcessBuilder(cargs);
    Process proc;

    pb.redirectErrorStream(true); // merge stderr with stdout
    proc = pb.start(); // Starts the process as well as creating it
    return new BufferedReader(new InputStreamReader(proc.getInputStream()));
  }

  /**
   * Scan the first element of a List of arrays testing it to see if it
   * represents an executable command.
   *
   * @param commandList
   *            List of arrays where element[0] is a file name which might be
   *            executable
   * @return Array whose first element is the name of a valid executable--or
   *         null if none are executable. DOS command names should be in short
   *         directory notation, 8.3 format, so that there are no spaces in the
   *         command names.
   */
  public static String[] firstExecutable(List<String[]> commandList) {
    if ((commandList == null) || (commandList.isEmpty())) {
      return null;
    }

    File file;
    for (String[] commandArray : commandList) {
      file = new File(commandArray[0]);
      if (file.canExecute()) {
        return commandArray;
      }
    }
    return null; // Did not find an executable file
  }

  /**
   * Convert members of a string array into a long string with the individual
   * strings separated by newline characters
   *
   * @param ray
   *            array of strings
   * @return space-separated string containing all of the strings in ray
   * or the empty string.
   */
  public static String stringArrayToString(String[] ray) {
    if ((ray == null) || (ray.length <= 0)) {
      return "";
    }
    StringBuffer sb = new StringBuffer(10 * ray.length);

    for (String s : ray) {
      sb.append(s + " ");
    }
    return sb.toString();
  }

  /**
   * Scan an array for the first path to an executable program and return its
   * subscript.
   *
   * @param exes
   *            array of file names
   * @return the index of the first executable file in the array or -1 if none
   *         of them are executable. the -1 should cause an array access
   *         exception in the caller.
   */
  public static int findFirstExecutable(String[] exes) {
    File file;
    if (exes != null) {
      for (int i = 0; i < exes.length; i++) {
        file = new File(exes[i]);
        if (file.canExecute()) {
          return i;
        }
      }
    }
    return -1; // Will cause array access exception
  }

  /**
   * Execute a process by plugging an array of arguments into an array
   * of command strings. The arguments are plugged in to the null
   * strings in the array as they occur.  It is up to the caller to
   * fill in all of the null strings; odd eventualities may eventuate
   * if an incomplete command string is executed.

   * @param cmdArray
   *            all of the strings needed to activate the command
   * @param args
   *            arguments to be plugged into the null strings in the array.
   *            The array is used as-is if args is null or if there are no null
   *            elements of the array.
   * @return process object or null
   */
  public static Process runPluggedCommand(String[] cmdArray, String[] args) {
    /* Clone the array because of the static references, otherwise the null we
       are expecting to replace at the end is already gone after the first
       time we print */
    String array[] = cmdArray.clone();
    int j = 0;
    if (args != null) {
      for (int i = 0; i < array.length; i++) {
        if (array[i] == null) {
          array[i] = args[j++];
        }
      }
    }
    try {
      System.out.println(stringArrayToString(array));
      return Runtime.getRuntime().exec(array);
    } catch (IOException e) {
      String message = "Exec error\n" + stringArrayToString(cmdArray) + "\n" +
	e.toString();
      System.out.println(message);
      throw new RuntimeException(message, e);
    }
  }
}
