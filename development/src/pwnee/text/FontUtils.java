package pwnee.text;

import java.awt.*;
import java.awt.geom.*;

/** Does text rendering calculations for a given font. */
public class FontUtils {
  
  public static int ALIGN_LEFT = 0;
  
  public static int ALIGN_CENTER = 1;
  
  public static int ALIGN_RIGHT = 2;
  
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
  
  
  /** Draws a simple tooltip, a short string of text inside a filled rectangle. */
  public static void drawToolTip(Graphics2D g, String str, int alignHint, Color strokeColor, Color fillColor) {
    AffineTransform origTrans = g.getTransform();
    
    Dimension2D dims = getStringDimensions(str, g.getFont(), 1);
    Rectangle2D rect = new Rectangle2D.Double(0, 0, dims.getWidth()+10, dims.getHeight()+10);
    
    double offset = 0;
    if(alignHint == ALIGN_CENTER) {
      offset = rect.getWidth()/2;
    }
    if(alignHint == ALIGN_RIGHT) {
      offset = rect.getWidth();
    }
    g.translate(0-offset, 0);
    
    g.setColor(fillColor);
    g.fill(rect);
    g.setColor(strokeColor);
    g.draw(rect);
    
    g.translate(5, 2);
    drawString(g, str, 1);
    
    g.setTransform(origTrans);
  }
  
  
}

