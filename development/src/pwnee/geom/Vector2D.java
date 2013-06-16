package pwnee.geom;

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
  
  /** Returns the magnitude of this vector. */
  public double length() {
    return GameMath.dist(0, 0, dx, dy);
  }
  
  /** Returns the dot product of this and another vector. */
  public double dotProduct(Vector2D other) {
    return this.dx*other.dx + this.dy*other.dy;
  }
  
  
  /** Returns the angle in radians from this vector to another vector. */
  public double rangleTo(Vector2D other) {
    return Math.acos(this.dotProduct(other)/(this.length()*other.length()));
  }
  
  /** Returns the angle in degrees from this vector to another vector. */
  public double angleTo(Vector2D other) {
    return GameMath.r2d(rangleTo(other));
  }
  
  
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
      
      System.out.println("dotProduct(v1, v2) = " + v1.dotProduct(v2));
      System.out.println("angle from v1 to v2: " + v1.angleTo(v2));
      
      // performance test
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
              
              v1.dotProduct(v2);
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

