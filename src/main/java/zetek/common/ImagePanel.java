/* @name ImagePanel.java

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
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Panel whose sole purpose is to display an image.  It sets its size
 * to the same size as the image.  These panels are usually used in
 * frames; when the frame is disposed, the panel is disposed which
 * releases its image for garbage collection.

 * @author Bill Taylor
 * @version %I%, %G%
 * @since
 *
 * @see <classname>
 */

public class ImagePanel extends JPanel {

  public static final long serialVersionUID = 1;

  Image toDraw;

  /** Obligatory constructor.*/
  public ImagePanel() { /* */ }

  /**
   * create an instance to display an Image
   * @param toDraw image to display
   */
  public ImagePanel(Image toDraw) {
    this.toDraw = toDraw;
    complete();
  }

  /**
   * create an instance to display an Image Icon
   * @param imic image icon to display
   */
  public ImagePanel(ImageIcon imic) {
    this.toDraw = imic.getImage();
    complete();
  }

  private void complete() {
    Dimension dim = new Dimension(toDraw.getWidth(null),toDraw.getHeight(null));
    UIUtils.forceSizes(this, dim);
  }

  public void paintComponent(Graphics g) {
    g.drawImage(toDraw, 0, 0, null);
  }
}
