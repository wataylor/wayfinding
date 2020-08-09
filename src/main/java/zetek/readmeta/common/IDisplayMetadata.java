/* @name IDisplayMetadata.java

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

package zetek.readmeta.common;

import java.awt.Color;
import java.awt.Rectangle;

import zetek.connectivity.GraphInfo;

/**
 * Interface to the routine which displays meta data after it has been
 * extracted from the .txt file representation of the .dwg database.
 * This interface is defined so that several different display classes
 * can be developed.  The program chooses which to instantiate via a
 * command-line parameter, and menu action routines can instantiate
 * classes which implement this interface in different ways.

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public interface IDisplayMetadata {

  /**
   * Array of colors to show each successive layer in a different
   * color. The display routine should walk through the array by one
   * each time a layer is drawn. This should be replaced as soon as it
   * is possible to convert .dwg layer color names to Java colors.
   * These colors are used in the order in which layers appear in
   * SGB.LAYER_NAMES.
   */
  public static final Color[] colorS = { Color.orange, Color.green, Color.blue,
      Color.black, Color.yellow, Color.cyan };

  /**
   * Generate a frame which displays all of the block references by
   * running in the event thread.  This method causes the frame and is
   * MouseyPanel subclass to be created asynchronously.  This causes
   * the frames to appear in a different order from the order in which
   * they are instantiated.
   * @param title Frame title
   * @param graphInfo Information about the graph
   * @param floorOrder specifies which building level to display
   */
  public abstract void displayBlocksLater(String title, GraphInfo graphInfo,
      short floorOrder);

  /**
   * Generate a frame which displays all of the block references.
   * This frame is created synchronously.  This method is used when
   * creating frames during startup based on the properties file.  The
   * method which instantiates all the required frames runs in the
   * event thread, so there is no need to defer instantiation.
   * @param title Frame title
   * @param graphInfo Information about the graph
   * @param floorOrder specifies which building level to display
   */
  public abstract void showDerivedResults(String title, GraphInfo graphInfo,
      short floorOrder);
  /** Trigger a repaint */
  public abstract void repaint();
  /** Restore original scale, pan, and zoom*/
  public abstract void homeDisplay();
  /** Return a string containing enough information to restore the panel
   * on startup. These strings are generally saved in properties files.  */
  public abstract String restorationParams();
  /** Restore the frame configuration after it has been automatically
   * created based on reading restoration parameters from the
   * properties file during startup.  */
  public abstract void restore(Rectangle bounds, double xPan, double yPan,
			       double scale, double rot);

}
