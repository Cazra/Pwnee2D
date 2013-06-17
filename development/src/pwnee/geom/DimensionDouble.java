package pwnee.geom;

import java.awt.geom.Dimension2D;

/** 
 * A Dimension2D with double precision. 
 * It's pretty basic stuff...
 */
public class DimensionDouble extends Dimension2D {
  
  public double width;
  
  public double height;
  
  public DimensionDouble(double w, double h) {
    width = w;
    height = h;
  }
  
  public double getHeight() {
    return height;
  }
  
  public double getWidth() {
    return width;
  }
  
  public void setSize(double w, double h) {
    width = w;
    height = h;
  }
  
}
