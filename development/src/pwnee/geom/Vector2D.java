package pwnee.geom;

import java.awt.*;
import java.awt.geom.*;

import pwnee.GameMath;

/** A geometric 2D vector, not a fancy array. */
public class Vector2D {
  
  /** Value fo the vector's x component. */
  public double dx;
  
  /** Value for the vector's y component. */
  public double dy;
  
  public Vector2D(double dx, double dy) {
    this.dx = dx;
    this.dy = dy;
  }
  
  
  //////// vector math
  
  
  /** Returns the magnitude of this vector. */
  public double length() {
    return GameMath.dist(0, 0, dx, dy);
  }
  
  /** Returns the dot product of this and another vector. */
  public double dotProduct(Vector2D other) {
    return this.dx*other.dx + this.dy*other.dy;
  }
  
  
  /** Returns the angle in radians from this vector to another vector, between 0.0 and PI. */
  public double rangleTo(Vector2D other) {
    return Math.acos(this.dotProduct(other)/(this.length()*other.length()));
  }
  
  /** Returns the angle in degrees from this vector to another vector, between 0 and 180 degrees. */
  public double angleTo(Vector2D other) {
    return GameMath.r2d(rangleTo(other));
  }
  
  /** 
   * Returns the signed angle in degrees from this vector to another vector. 
   * This assumes we are working in game geometry where the positive y-axis
   * points down.
   * This would need to be rotated by the returned amount to become parallel
   * with other.
   */
  public double angleToS(Vector2D other) {
    double v1A = GameMath.angleTo(0, 0, this.dx, this.dy);
    double v2A = GameMath.angleTo(0, 0, other.dx, other.dy);
    
    return v1A - v2A;
  }
  
  /** Returns the sum of two vectors. */
  public Vector2D add(Vector2D other) {
    return new Vector2D(this.dx + other.dx, this.dy + other.dy);
  }
  
  
  /** Scales the dx and dy component by the same amount. */
  public Vector2D scale(double amt) {
    return new Vector2D(this.dx*amt, this.dy*amt);
  }
  
  /** Scales the dx and dy components by differing amounts. */
  public Vector2D scale(double scaleX, double scaleY) {
    return new Vector2D(this.dx*scaleX, this.dy*scaleY);
  }
  
  
  /** Rotates the vector counter-clockwise (in game geometry) by the specified number of degrees. */
  public Vector2D rotate(double degrees) {
    return rotateR(GameMath.d2r(degrees));
  }
  
  /** Rotates the vector counter-clockwise (in game geometry) by the specified number of radians. */
  public Vector2D rotateR(double radians) {
    AffineTransform trans = AffineTransform.getRotateInstance(0-radians);
    Point2D pt = new Point2D.Double(dx, dy);
    pt = trans.transform(pt, null);
    
    return new Vector2D(pt.getX(), pt.getY());
  }
  
  
  /** Applies an affine transform to the vector. */
  public Vector2D transform(AffineTransform trans) {
    Point2D start = new Point2D.Double(0, 0);
    Point2D end = new Point2D.Double(dx, dy);
    
    start = trans.transform(start, null);
    end = trans.transform(end, null);
    
    return new Vector2D(end.getX() - start.getX(), end.getY() - start.getY());
  }
  
  //////// Misc
  
  
  public String toString() {
    return "Vector2D(" + dx + ", " + dy +")";
  }
  
  /** Two vectors are equal iff their components are equal. */
  public boolean equals(Object o) {
    if(o instanceof Vector2D) {
      Vector2D other = (Vector2D) o;
      return (this.dx == other.dx && this.dy == other.dy);
    }
    else {
      return false;
    }
  }
  
  
  /** 
   * Draws the vector for testing purposes. 
   * It is represented as a line with a circle at the start point. 
   */
  public void draw(Graphics2D g, double x, double y) {
    Shape shape = new Line2D.Double(x, y, x + dx, y + dy);
    g.draw(shape);
    shape = new Ellipse2D.Double(x-1, y-1, 2, 2);
    g.draw(shape);
  }
  
  public void draw(Graphics2D g) {
    draw(g, 0, 0);
  }
  
  
  /** 
   * Unit test takes 4 doubles as arguments to define 2 vectors. 
   * It outputs the result of the vector operations between these two vectors. 
   */
  public static void main(String[] args) {
    try {
      // correctness test
      double x1 = Double.parseDouble(args[0]);
      double y1 = Double.parseDouble(args[1]);
      double x2 = Double.parseDouble(args[2]);
      double y2 = Double.parseDouble(args[3]);
      
      Vector2D v1 = new Vector2D(x1, y1);
      Vector2D v2 = new Vector2D(x2, y2);
      
      System.out.println("v1: " + v1);
      System.out.println("v2: " + v2);
      
      System.out.println("length(v1) = " + v1.length());
      System.out.println("length(v2) = " + v2.length());
      
      System.out.println("sum(v1, v2) = " + v1.add(v2));
      
      System.out.println("dotProduct(v1, v2) = " + v1.dotProduct(v2));
      System.out.println("unsigned angle from v1 to v2: " + v1.angleTo(v2));
      System.out.println("signed angle from v1 to v2: " + v1.angleToS(v2));
      
      // performance test: compute the angles between many vectors.
      int testSize = 10;
      int totalVectors = testSize*testSize*testSize*testSize;
      System.out.println("Doing performance test on " + totalVectors + " vectors.");
      long time = System.currentTimeMillis();
      for(x1 = -testSize/2; x1 < testSize/2; x1++) {
        for(y1 = -testSize/2; y1 < testSize/2; y1++) {
          
          for(x2 = -testSize/2; x2 < testSize/2; x2++) {
            for(y2 = -testSize/2; y2 < testSize/2; y2++) {
              
              v1 = new Vector2D(x1, y1);
              v2 = new Vector2D(x2, y2);
              
              v1.angleTo(v2);
              
            }
          }
        }
      }
      time = System.currentTimeMillis() - time;
      System.out.println("Performance test: " + time + " ms on " + totalVectors + " vector comparisons.");
    }
    catch(ArrayIndexOutOfBoundsException e) {
      System.out.println("Error: expects 4 double arguments - vector1.dx, vector1.dy, vector2.dx, vector2.dy");
    }
    catch(NumberFormatException e) {
      System.out.println("Error: expects 4 double arguments - vector1.dx, vector1.dy, vector2.dx, vector2.dy");
    }
  }
}

