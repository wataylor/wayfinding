package zetek.ui;

import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ButtonUtil {

  private ButtonUtil() {}

  public static JButton makeNavigationButton(String imageName,
      String actionCommand,
      String toolTipText,
      String altText,
      ActionListener listener) {
    //Look for the image.
    //    String imgLocation = "images/"
    //      + imageName
    //      + ".gif";
    //    URL imageURL = ToolBarDemo.class.getResource(imgLocation);
    URL imageURL = null;

    //Create and initialize the button.
    JButton button = new JButton();
    button.setActionCommand(actionCommand);
    button.setToolTipText(toolTipText);
    button.setFocusable(false);
    button.addActionListener(listener);

    if (imageURL != null) {                      //image found
      button.setIcon(new ImageIcon(imageURL, altText));
    } else {                                     //no image found
      button.setText(altText);
      //System.err.println("Resource not found: " + imgLocation);
    }
    return button;
  }
}
