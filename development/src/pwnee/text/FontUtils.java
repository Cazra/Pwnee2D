package pwnee.text;

import java.awt.*;
import java.awt.geom.*;

/** Does text rendering calculations for a given font. */
public class FontUtils {
  
  public Font font;
  
  private FontMetrics fm;
  
  public int lineSpacing;
  
  public FontUtils(Font f, int lineSpace) {
    font = f;
    fm = Toolkit.getDefaultToolkit().getFontMetrics(font);
    
    lineSpacing = lineSpace;
  }
  
  
  /** 
   * Computes the bounding box for a String rendered with the font, assuming 
   * that the text's upper left corner is at the origin. 
   */
  public Dimension2D getStringDimensions(String str) {
    int lineHeight = fm.getHeight();
    
    int width = 0;
    int height = 0;
    
    String[] lines = str.split("\n");
    boolean first = true;
    for(String line : lines) {
      if(first) {
        first = false;
      }
      else {
        height += lineSpacing;
      }
      
      height += lineHeight;
      int w = fm.stringWidth(line);
      
      if(w > width) {
        width = w;
      }
    }
    
    return new Dimension(width, height);
  }
  
  /** Convenient static version of getStringDimensions. */
  public static Dimension2D getStringDimensions(String str, Font f, int spacing) {
    FontUtils util = new FontUtils(f, spacing);
    return util.getStringDimensions(str);
  }
  
  
  /** Draws a String, even if it has multiple lines. The upper-left corner of the String is rendered at 0,0. */
  public static void drawString(Graphics2D g, String str, int lineSpace) {
    int height = 0;
    
    FontMetrics fm = Toolkit.getDefaultToolkit().getFontMetrics(g.getFont());
    int lineHeight = fm.getHeight();
    
    String[] lines = str.split("\n");
    boolean first = true;
    for(String line : lines) {
      if(first) {
        first = false;
      }
      else {
        height += lineSpace;
      }
      
      height += lineHeight;
      
      g.drawString(line, 0, height);
    }
  }
  
  
}

