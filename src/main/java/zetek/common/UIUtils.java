/* @name UIUtils.java

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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Shared utilities which are helpful in managing user interfaces.

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class UIUtils {

  public static final long serialVersionUID = 1;

  public static final Dimension whiteDim = new Dimension(5,5);

  /** Obligatory constructor.*/
  public UIUtils() { /* */ }

  /** Recursively delete all files and directories in a directory.  If
   * the argument is a file, it deletes it without recursion. */
  public static boolean deleteDirectory(File path) {
    if (path.exists() && path.isDirectory()) {
      File[] files = path.listFiles();
      for(int i=0; i<files.length; i++) {
	if (files[i].isDirectory()) {
	  deleteDirectory(files[i]);
	} else {
	  files[i].delete();
	}
      }
    }
    return( path.delete() );
  }

  /**
   * Utility method for creating a panel with a box layout.
   * @param orientation tells whether to go H or V
   * @return panel with layout specified and left alignment
   */
  public static JPanel makePan(int orientation) {
    JPanel pan = new JPanel();
    pan.setLayout(new BoxLayout(pan, orientation));
    pan.setAlignmentX(Component.LEFT_ALIGNMENT);
    return pan;
    // .setAlignmentX(Component.CENTER_ALIGNMENT); BoxLayout.Y_AXIS
    // Box.createRigidArea(dim);
  }

  /** Force a component to be of a specified size. */
  public static void forceSizes(Component comp, Dimension dim) {
    comp.setSize(dim);
    comp.setPreferredSize(dim);
    comp.setMinimumSize(dim);
    comp.setMaximumSize(dim);
  }

  /** Force a component to be of its preferred size. */
  public static void forceSizes(Component comp) {
    forceSizes(comp, comp.getPreferredSize());
  }

  /** Scan a list of components and force them to have the same sizes
   * based on the maximum height and width in any component in the list.
   * @param l list of components*/
  public static void sizeAList(List<Component> l) {
    Dimension dim = new Dimension(0,0);
    Dimension compDim;
    for (Component compo : l) {
      compDim = compo.getPreferredSize();
      if (dim.height < compDim.height) { dim.height = compDim.height; }
      if (dim.width  < compDim.width)  { dim.width  = compDim.width;  }
    }
    for (Component compo : l) {
      forceSizes(compo, dim);
    }
  }

  /**
   * Return a panel full of buttons arranged horizontally.  All
   * buttons are the same size.  They are all assigned to the same
   * action listener and given sequentially-ascending action
   * commands.
   * @param al ActionListener for all buttons
   * @param off offset from 0 for the first sequential action command
   * @param lbls names of the buttons
   * @return panel with the buttons arranged horizontally.
   */
  public static JPanel buttonPan(ActionListener al, int off, String[] lbls) {
    List<Component> list = new ArrayList<Component>(lbls.length);

    JPanel  pan = UIUtils.makePan(BoxLayout.X_AXIS);
    JButton but;
    pan.add(but = new JButton(lbls[0]));
    but.addActionListener(al);
    but.setActionCommand("" + (0+off));
    list.add(but);
    for (int i=1; i<lbls.length; i++) {
      pan.add(Box.createRigidArea(whiteDim));
      pan.add(but = new JButton(lbls[i]));
      but.addActionListener(al);
      but.setActionCommand("" + (i+off));
      list.add(but);
    }
    pan.setAlignmentX(Component.LEFT_ALIGNMENT);
    sizeAList(list);
    return pan;
  }

  /**
   * Generate a panel of labels which are oriented in the specified
   * direction.
   * @param first If not null, generates a label before the labels in
   * the array.
   * @param lbls Array of label strings; the length of the array
   * specifies how many labels are put in the panel.
   * @param orient orientation as passed to <code>makePan</code>
   * @param list list of components created; the list is intended for
   * later use in sizing the labels.
   * @return panel with all labels in place
   */
  public static JPanel labelPanel(String first, String[] lbls, int orient,
				  List<Component> list) {
    JPanel pan = UIUtils.makePan(orient);
    JLabel lbl;
    if (first != null) {
      pan.add(lbl = new JLabel(first));
      list.add(lbl);
      lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
    }
    for (int i=0; i<lbls.length; i++) {
      pan.add(lbl = new JLabel(lbls[i]));
      list.add(lbl);
      lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
    }
    pan.setAlignmentX(Component.LEFT_ALIGNMENT);
    return pan;
  }

}
