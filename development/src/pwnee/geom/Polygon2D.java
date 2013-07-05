package pwnee.geom;

import java.awt.*;
import java.awt.geom.*;

import pwnee.GameMath;

/** A polygon whose points are stored as doubles. */
public class Polygon2D {
  
  private double[] x;
  private double[] y;
  
  public Polygon2D(double[] xpoints, double[] ypoints) {
    int npoints = Math.min(xpoints.length, ypoints.length);
    
    x = new double[npoints];
    y = new double[npoints];
    
    for(int i = 0; i < npoints; i++) {
      x[i] = xpoints[i];
      y[i] = ypoints[i];
    }
  }
  
  
  //////// Model
  
  
  /** Returns the number of vertices in this polygon. */
  public int getNumPoints() {
    return x.length;
  }
  
  
  public Point2D getPoint(int index) {
    if(index < 0 || index >= getNumPoints()) {
      return null;
    }
    else {
      return new Point2D.Double(x[index], y[index]);
    }
  }
  
  
  
  //////// Collisions
  
  
  /** Returns true iff this polygon is convex and it contains the specified point. */
  public boolean contains(double x, double y) {
    int numPoints = getNumPoints();
    
    // The point will be "below" all the segments if the vertices were specified in cw order.
    boolean underAll = true; 
    
    // The point will be "above" all the segments if the vertices were specified in ccw order. 
    boolean aboveAll = true; 
    
    // iterate over this polygon's segments. 
    for(int j = 0; j < numPoints; j++) {
      // segment start and end points.
      double sx = this.x[j];
      double sy = this.y[j];
      double ex = this.x[(j+1) % numPoints];
      double ey = this.y[(j+1) % numPoints];
      
      // return false early if the point has been found to be neither above nor below all this polygon's segments.
      int relCCW = Line2D.relativeCCW(sx, sy, ex, ey, x, y);
      if(relCCW > 0) {
        underAll = false;
      }
      if(relCCW < 0) {
        aboveAll = false;
      }
      if(!underAll && !aboveAll) {
        return false;
      }
    }
    
    // either the point was above or above all this polygon's segments.
    return true;
  }
  
  public boolean contains(Point2D p) {
    if(p == null) {
      return false;
    }
    return contains(p.getX(), p.getY());
  }
  
  
  /** Tests if this and another convex polygon are intersecting using the Separating Axis Theorem. */
  public boolean intersects(Polygon2D other) {
    return (this.halfSAT(other) || other.halfSAT(this));
  }
  
  
  /** Tests if at least one of this polygon's vertices are inside another polygon. */
  private boolean halfSAT(Polygon2D other) {
    for(int i = 0; i < getNumPoints(); i++) {
      double xx = x[i];
      double yy = y[i];
      
      if(other.contains(xx, yy)) {
        return true;
      }
    }
    return false;
  }
  
  
  //////// Geometry 
  
  /** Translates the polygon. */
  public Polygon2D translate(double dx, double dy) {
    int numPts = getNumPoints();
    double[] newX = new double[numPts];
    double[] newY = new double[numPts];
    
    for(int i = 0; i < numPts; i++) {
      newX[i] = x[i] + dx;
      newY[i] = y[i] + dy;
    }
    
    return new Polygon2D(newX, newY);
  }
  
  
  /** Scales the polygon differently along the x and y axes. */
  public Polygon2D scale(double scaleX, double scaleY) {
    int numPts = getNumPoints();
    double[] newX = new double[numPts];
    double[] newY = new double[numPts];
    
    for(int i = 0; i < numPts; i++) {
      newX[i] = x[i]*scaleX;
      newY[i] = y[i]*scaleY;
    }
    
    return new Polygon2D(newX, newY);
  }
  
  /** Scales the polygon uniformly along the x and y axes. */
  public Polygon2D scale(double amt) {
    return scale(amt, amt);
  }
  
  
  /** Rotates the polygon counter-clockwise (in game geometry) by the specified number of degrees. */
  public Polygon2D rotate(double angle) {
    return transform(AffineTransform.getRotateInstance(0-GameMath.d2r(angle)));
  }
  
  
  /** Rotates the polygon counter-clockwise (in game geometry) by the specified number of radians. */
  public Polygon2D rrotate(double angle) {
    return transform(AffineTransform.getRotateInstance(0-angle));
  }
  
  
  /** Applies an affine transform to this polygon. */
  public Polygon2D transform(AffineTransform trans) {
    int numPts = getNumPoints();
    double[] newX = new double[numPts];
    double[] newY = new double[numPts];
    
    for(int i = 0; i < numPts; i++) {
      Point2D pt = trans.transform(new Point2D.Double(x[i], y[i]), null);
      newX[i] = pt.getX();
      newY[i] = pt.getY();
    }
    
    return new Polygon2D(newX, newY);
  }
  
  
  //////// Misc.
  
  
  /** Draws the polygon. */
  public void draw(Graphics2D g) {
    int numPoints = getNumPoints();
  
    for(int j = 0; j < numPoints; j++) {
      // segment start and end points.
      double sx = this.x[j];
      double sy = this.y[j];
      double ex = this.x[(j+1) % numPoints];
      double ey = this.y[(j+1) % numPoints];
      
      Line2D line = new Line2D.Double(sx, sy, ex, ey);
      g.draw(line);
    }
  }
} 

