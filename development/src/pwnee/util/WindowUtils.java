package pwnee.util;

import java.awt.*;


/** 
 * This class provides static methods for doing things with UI windows.
 */
public class WindowUtils {
  
  /** 
   * Gets the Frame containing a Component. If the Frame cannot be obtained, 
   * then null is returned.
   */
  public static Frame getParentFrame(Component comp) {
    if(comp instanceof Frame) {
      return (Frame) comp;
    }
    else if(comp != null) {
      return getParentFrame(comp.getParent());
    }
    else {
      return null;
    }
  }
  
  /** Centers a window on the screen relative to its owner. */
  public static void centerInParent(Window child) {
    if(child != null) {
      Window parent = child.getOwner();
      Dimension size = child.getPreferredSize();
      
      int x, y;
      if(parent != null) {
        Dimension parentSize = parent.getPreferredSize();
        x = parent.getLocation().x + (parentSize.width - size.width)/2;
        y = parent.getLocation().y + (parentSize.height - size.height)/2;
      }
      else {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        x = (screenSize.width - size.width)/2;
        y = (screenSize.height - size.height)/2;
      }
      
      child.setLocation(x, y);
    }
  }
}
