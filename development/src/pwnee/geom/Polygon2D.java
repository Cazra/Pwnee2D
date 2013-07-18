package pwnee.geom;

import java.awt.*;
import java.awt.geom.*;

import pwnee.GameMath;

/** A polygon whose points are stored as doubles. */
public class Polygon2D {
  
  private double[] x;
  private double[] y;
  
  /** Creates a polygon by copying from two arrays of x and y coordinates. */
  public Polygon2D(double[] xpoints, double[] ypoints) {
    int npoints = Math.min(xpoints.length, ypoints.length);
    
    x = new double[npoints];
    y = new double[npoints];
    
    for(int i = 0; i < npoints; i++) {
      x[i] = xpoints[i];
      y[i] = ypoints[i];
    }
  }
  
  /** Creates a polygon by copying coordinate data from an array of points. */
  public Polygon2D(Point2D[] p) {
    int numPts = p.length;
    
    x = new double[numPts];
    y = new double[numPts];
    
    for(int i = 0; i < numPts; i++) {
      x[i] = p[i].getX();
      y[i] = p[i].getY();
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
    // There is only a collision if no separating axis could be found.
    return !(this.halfSAT(other) || other.halfSAT(this));
  }
  
  
  /** 
   * Return true if there is a tangent line on one of this poly's segments 
   * such that that line separates all the points of both polygons.
   * Right now this only works if the points are specified in clockwise order (in game geometry).
   */
  private boolean halfSAT(Polygon2D other) {
    /*
    for(int i = 0; i < getNumPoints(); i++) {
      double xx = x[i];
      double yy = y[i];
      
      if(other.contains(xx, yy)) {
        return true;
      }
    }
    return false;
    */
    
    // All the points in the other polygon will be "above" all the segments if the vertices were specified in ccw order. 
    int numPoints = getNumPoints();
    for(int j = 0; j < numPoints; j++) {
      // segment start and end points.
      double sx = this.x[j];
      double sy = this.y[j];
      double ex = this.x[(j+1) % numPoints];
      double ey = this.y[(j+1) % numPoints];
      
      // return false early if there is a separating axis.
      boolean sepAxis = true;
      for(int i = 0; i < other.getNumPoints(); i++) {
        Point2D pt = other.getPoint(i);
        int relCCW = Line2D.relativeCCW(sx, sy, ex, ey, pt.getX(), pt.getY());
        if(relCCW < 0) {
          sepAxis = false;
          break;
        }
      }
      if(sepAxis) {
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
  
  
  
  
  /** Convert a Rectangle2D into a Polygon2D. */
  public static Polygon2D rectToPoly(Rectangle2D rect) {
    double left = rect.getX();
    double top = rect.getY();
    double right = left + rect.getWidth();
    double bottom = top + rect.getHeight();
    
    int numPts = 4;
    
    // define the rectangle's points in clockwise order (in game coordinates, y axis is down).
    Point2D[] p = new Point2D[numPts];
    p[0] = new Point2D.Double(left, top);
    p[1] = new Point2D.Double(right, top);
    p[2] = new Point2D.Double(right, bottom);
    p[3] = new Point2D.Double(left, bottom);
    return new Polygon2D(p);
  }
  
  
  /** Convert an equilateral octogon defined by a rectangle into a Polygon2D. */
  public static Polygon2D octToPoly(Rectangle2D oct) {
    double left = oct.getX();
    double top = oct.getY();
    double right = left + oct.getWidth();
    double bottom = top + oct.getHeight();
    
    double cornerMult = 1/2.8284; // 1/(2*sqrt(2)) approx.
    double cornerW = cornerMult * oct.getWidth();
    double cornerH = cornerMult * oct.getHeight();
    
    int numPts = 8;
    
    // define the rectangle's points in clockwise order (in game coordinates, y axis is down).
    Point2D[] p = new Point2D[numPts];
    p[0] = new Point2D.Double(left + cornerW, top);
    p[1] = new Point2D.Double(right - cornerW, top);
    p[2] = new Point2D.Double(right, top + cornerH);
    p[3] = new Point2D.Double(right, bottom - cornerH);
    p[4] = new Point2D.Double(right - cornerW, bottom);
    p[5] = new Point2D.Double(left + cornerW, bottom);
    p[6] = new Point2D.Double(left, bottom - cornerH);
    p[7] = new Point2D.Double(left, top + cornerH);
    return new Polygon2D(p);
  }
  
  
  /** Convert an approximation of an ellipse, given a number of sides and its bounding rectangle, into a Polygon2D. */
  public static Polygon2D ellipseToPoly(Rectangle2D ellipseBounds, int sides) {
    double radW = ellipseBounds.getWidth()/2;
    double radH = ellipseBounds.getHeight()/2;
    
    double centerX = ellipseBounds.getX() + radW;
    double centerY = ellipseBounds.getY() + radH;
    
    Point2D[] p = new Point2D[sides];
    double angle = 90; // I picked 90 so that it is symetrical down its vertical center. 
    for(int i = 0; i < sides; i++) {
      double x = centerX + GameMath.cos(angle) * radW;
      double y = centerY - GameMath.sin(angle) * radH;
      
      p[i] = new Point2D.Double(x, y);
    }
    return new Polygon2D(p);
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

