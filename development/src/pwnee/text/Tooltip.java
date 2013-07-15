package pwnee.text;

import java.awt.*;
import java.awt.geom.*;

/** Reusable object for displaying tooltips for any component implementing Tooltipable. */
public class Tooltip {
  
  /** The color of the tooltip's border. */
  public Color strokeColor;
  
  /** The color of the tooltip's interior. */
  public Color fillColor;
  
  /** The color of the tooltip's text. */
  public Color textColor;
  
  
  private Tooltipable component;
  
  private long lastTime = 0;
  private long focalTime = 0;
  
  /** The delay, in milliseconds, to wait before displaying a tooltip for a component.  */
  public int delay;
  
  
  /** 
   * Constructs the reuseable tooltip. 
   * @param border    The border color.
   * @param interior  The interior color.
   * @param text      The text color.
   * @param delay     The amount of time a component must be focused on (i.e. mouse-overed) 
   *                  to display the tooltip, in milliseconds.
   */
  public Tooltip(Color border, Color interior, Color text, int delay) {
    this.strokeColor = border;
    this.fillColor = interior;
    this.textColor = text;
    this.delay = delay;
  }
  
  
  /** 
   * Updates which component we are getting ready to display a tooltip for. 
   * The delay is reset if the component is changed. 
   */
  public void updateComponent(Tooltipable comp) {
    if(comp != component) {
      lastTime = 0;
      focalTime = 0;
    }
    component = comp;
    
    long curTime = System.currentTimeMillis();
    if(lastTime > 0) {
      focalTime += curTime - lastTime;
    }
    lastTime = curTime;
  }
  
  
  /** Draws the tooltip in screen geometry, at a point given in the graphics context's current geometry. */
  public void render(Graphics2D g) {
    if(component == null || focalTime < delay) {
      return;
    }
    
    double worldX = component.getX();
    double worldY = component.getY();
    String str = component.getTooltipString();
    
    AffineTransform origTrans = g.getTransform();
    
    Point2D screenPt = new Point2D.Double(worldX, worldY);
    screenPt = origTrans.transform(screenPt, null);
    
    g.setTransform(new AffineTransform());
    g.translate(screenPt.getX(), screenPt.getY()+10);
    
    Dimension2D dims = FontUtils.getStringDimensions(str, g.getFont(), 1);
    Rectangle2D rect = new Rectangle2D.Double(0, 0, dims.getWidth()+10, dims.getHeight()+10);
    
    g.setColor(fillColor);
    g.fill(rect);
    g.setColor(strokeColor);
    g.draw(rect);
    g.setColor(textColor);
    g.translate(5,2);
    FontUtils.drawString(g, str, 1);
    
    g.setTransform(origTrans);
  }
}
