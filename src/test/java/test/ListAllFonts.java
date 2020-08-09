package test;

/*
 * Copyright (c) Ian F. Darwin, http://www.darwinsys.com/, 1996-2002.
 * All rights reserved. Software written by Ian F. Darwin and others.
 * $Id: LICENSE,v 1.8 2004/02/09 03:33:38 ian Exp $
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * Java, the Duke mascot, and all variants of Sun's Java "steaming coffee
 * cup" logo are trademarks of Sun Microsystems. Sun's, and James Gosling's,
 * pioneering role in inventing and promulgating (and standardizing) the Java
 * language and environment is gratefully acknowledged.
 *
 * The pioneering role of Dennis Ritchie and Bjarne Stroustrup, of AT&T, for
 * inventing predecessor languages C and C++ is also gratefully acknowledged.
 */

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

/**
 * Finds and displays available fonts
 * <p>
 * TODO: should be a JTable with the text name in one column and the demo in a
 * JLabel in the other.
 *
 * @author Ian Darwin (original)
 */
public class ListAllFonts extends JComponent {

  public static final long serialVersionUID = 1;

  /** The list of Fonts */
  protected String[] fontNames;

  /** The fonts themselves */
  protected Font[] fonts;

  /** How much space between each name */
  static final int YINCR = 20;

  /**
   * Construct a ListAllFonts -- Sets title and gets array of fonts on
   * the system
   */
  public ListAllFonts() {

    // Toolkit toolkit = Toolkit.getDefaultToolkit();
    // For JDK 1.1: returns about 10 names (Serif, SansSerif, etc.)
    // fontNames = toolkit.getFontList();
    // For JDK 1.2: a much longer list; most of the names that come
    // with your OS (e.g., Arial, Lucida, Lucida Bright, Lucida Sans...)
    fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment()
    .getAvailableFontFamilyNames();
    fonts = new Font[fontNames.length];
  }

  public Dimension getPreferredSize() {
    return new Dimension(500, fontNames.length * YINCR);
  }

  /**
   * Draws the font names in its font. Called by AWT when painting is needed
   * Does lazy evaluation of Font creation, caching the results (without this,
   * scrolling performance suffers even on a P3-750).
   */
  public void paint(Graphics g) {
    Graphics2D gg = (Graphics2D)g;
    gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
    gg.setRenderingHint(RenderingHints.KEY_RENDERING,
        RenderingHints.VALUE_RENDER_QUALITY);

    for (int i = 0; i < fontNames.length; i += 1) {
      if (fonts[i] == null) {
        fonts[i] = new Font(fontNames[i], Font.BOLD, 14);
      }
      gg.setFont(fonts[i]);
      int x = 20;
      int y = 20 + (YINCR * i);
      gg.drawString(fontNames[i], x, y);
    }
  }

  /** Simple main program to start it running */
  public static void main(String[] args) {
    JFrame f = new JFrame("Font Demo");
    f.getContentPane().add(new JScrollPane(new ListAllFonts()));
    f.setSize(600, 700);
    f.setVisible(true);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}
