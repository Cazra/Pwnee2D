package pwnee.text;

import java.awt.*;
import java.awt.geom.*;

/** Reusable object for displaying tooltips. */
public class Tooltip {
  
  public Color strokeColor;
  
  public Color fillColor;
  
  public Color textColor;
  
  
  public Tooltip(Color border, Color interior, Color text) {
    this.strokeColor = border;
    this.fillColor = interior;
    this.textColor = text;
  }
  
  
  /** Draws the tooltip in screen geometry, at a point given in the graphics context's current geometry. */
  public void render(Graphics2D g, String str, double worldX, double worldY) {
    AffineTransform origTrans = g.getTransform();
    
    Point2D screenPt = new Point2D.Double(worldX, worldY);
    screenPt = origTrans.transform(screenPt, null);
    
    g.setTransform(new AffineTransform());
    
    Dimension2D dims = FontUtils.getStringDimensions(str, g.getFont(), 1);
    Rectangle2D rect = new Rectangle2D.Double(0, 0, dims.getWidth()+10, dims.getHeight()+10);
    
    g.setColor(fillColor);
    g.fill(rect);
    g.setColor(strokeColor);
    g.draw(rect);
    
    g.translate(5,2);
    FontUtils.drawString(g, str, 1);
    
    g.setTransform(origTrans);
  }
}
