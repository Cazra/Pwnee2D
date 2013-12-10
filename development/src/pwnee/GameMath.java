package pwnee;

/*======================================================================
 * 
 * Pwnee - A lightweight 2D Java game engine
 * 
 * Copyright (c) 2012 by Stephen Lindberg (sllindberg21@students.tntech.edu)
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: 
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
======================================================================*/

import java.util.Comparator;
import java.util.Random;
import java.awt.geom.*;

/** 
 * A class with several static methods for performing various common 
 * mathematical computations. 
 * 
 * Some methods provided here are simply just faster version of methods or trig
 * methods that operate on degrees rather than radians. A lot of the methods in 
 * java.lang.Math invoke native handlers, which is slow. By reimplementing them 
 * here in pure Java, it is much faster!
 * 
 * See also the static methods available in 
 * java.awt.Line2D, and java.awt.Point2D.
 */
public class GameMath {
  
  /** A conventience Random object. */
	public static Random rand = new Random();
	
  //////// Angle math
  
   /** 
    * shorthand for Math.toRadians.
    * Converts an angle from degrees to radians. 
    */
	public static double d2r(double degrees) {
		return degrees*Math.PI/180.0;
	}
   
   /** 
    * shorthand for Math.toDegrees.
    * Converts an angle from radians to degrees. 
    */
   public static double r2d(double radians) {
      return radians*180.0/Math.PI;
   }
	
   /** Computes the sine of an angle given in degrees. */
	public static double sin(double degrees) {
		int angle = (int)degrees % 360;
		if(angle < 0)
			angle += 360;
		return Math.sin(d2r(degrees));
	}
	
   /** Computes the cosine of an angle given in degrees. */
	public static double cos(double degrees) {
		int angle = (int)degrees % 360;
		if(angle < 0)
			angle += 360;
		return Math.cos(d2r(degrees));
	}

	
	/** 
   * Returns the angle in degrees from one point to another, 
   * going clockwise from the positive X axis and assuming that the 
   * positive y axis is down. 
   * The result is in the range [0, 360).
   */
	public static double angleTo(double origX, double origY, double destX, double destY) {
		double dx = destX - origX;
		if(dx == 0)
			dx = Double.MIN_VALUE;
		double dy = origY - destY;
		
    double result = r2d(Math.atan(dy/dx));
    if(dx <= 0) {
      result += 180;
    }
    return angleNorm(result);
	}
	
	/** 
   * Returns + if faster to rotate clockwise. 
   * Returns - if faster to rotate counter-clockwise. 
   * Returns 0 if curAngle = destAngle. 
   */
	public static double rotateTowardAngle(double curAngle, double destAngle) {
		// normalize curAngle and destAngle to be in [0,360)
		curAngle = angleNorm(curAngle);
		destAngle = angleNorm(destAngle);
		
		double destAngle2;
		if(destAngle < 180) destAngle2 = destAngle + 360;
		else destAngle2 = destAngle - 360;
		
		double best;
		if(Math.abs(destAngle - curAngle) < Math.abs(destAngle2 - curAngle)) best = destAngle;
		else best = destAngle2;
		
		best -= curAngle;
		
		return best;
	}
	
	
  /** Normalizes an angle in degrees to be in the range [0, 360). */
  public static double angleNorm(double src) {
    src %= 360;
    if(src < 0) {
      src += 360;
    }
    return src;
  }
	
  
  
  
  //////// Ranges
  
  /** Clamps some value to be in the range [min, max]. */
  public static double clamp(double value, double min, double max) {
    return Math.min(1, Math.max(0, value));
  }
  
  /** Clamps some value to be in the range [0, 1]. */
  public static double clamp(double value) {
    return clamp(value, 0, 1);
  }
  
  
  /** Wraps some value to be in the range [min, max). */
  public static double wrap(double value, double min, double max) {
    double diff = max - min;
    double result = value % diff;
    if(result < 0) {
      result += diff;
    }
    result += min;
    return result;
  }
  
  /** Wraps some value to be in the range [0, 1). */
  public static double wrap(double value) {
    return wrap(value, 0, 1);
  }
  
  /** Wraps some value to be in the range [0, max). */
  public static double wrap(double value, double max) {
    return wrap(value, 0, max);
  }
  
  
  //////// Array wrapping
  
  /** Wraps some index to be within an the range for an array. */
  public static int wrap(int index, Object[] arr) {
    return arrWrap(index, arr.length);
  }
  
  /** Wraps some index to be within an the range for an array. */
  public static int wrap(int index, byte[] arr) {
    return arrWrap(index, arr.length);
  }
  
  /** Wraps some index to be within an the range for an array. */
  public static int wrap(int index, short[] arr) {
    return arrWrap(index, arr.length);
  }
  
  /** Wraps some index to be within an the range for an array. */
  public static int wrap(int index, int[] arr) {
    return arrWrap(index, arr.length);
  }
  
  /** Wraps some index to be within an the range for an array. */
  public static int wrap(int index, long[] arr) {
    return arrWrap(index, arr.length);
  }
  
  /** Wraps some index to be within an the range for an array. */
  public static int wrap(int index, float[] arr) {
    return arrWrap(index, arr.length);
  }
  
  /** Wraps some index to be within an the range for an array. */
  public static int wrap(int index, double[] arr) {
    return arrWrap(index, arr.length);
  }
  
  /** Wraps some index to be within an the range for an array. */
  public static int wrap(int index, boolean[] arr) {
    return arrWrap(index, arr.length);
  }
  
  /** Wraps some index to be within an the range for an array. */
  private static int arrWrap(int index, int size) {
    index = index % size;
    if(index < 0) {
      index += size;
    }
    return index;
  }
  
  
  //////// Distance math
  
  /** 
   * shorthand for Point2D.distanceSq.
   * Returns the square distance from one point to another. This is faster for 
   * doing things such as collision detections. 
   */
	public static double distSq(double origX, double origY, double destX, double destY) {
		double a = origX - destX;
		double b = origY - destY;
		
		return a*a + b*b;
	}
	
	/** 
   * shorthand for Point2D.distance.
   * Returns the distance from one point to another. 
   */
	public static double dist(double origX, double origY, double destX, double destY) {
		double a = origX - destX;
		double b = origY - destY;
		
		return Math.sqrt(a*a + b*b);
	}
  
  
  /** Computes the square distance between 2 n-dimensional points. Returns -1 if p1 and p2 are not of the same dimensions. */
  public static double distSq(double[] p1, double[] p2) {
    if(p1.length != p2.length) {
      return -1;
    }
    else {
      double sqLen = 0;
      for(int i = 0 ; i < p1.length ; i++) {
        double dN = p1[i] - p2[i];
        sqLen += dN*dN;
      }
      return sqLen;
    }
  }
  
  /** Computes the distance between 2 n-dimensional points. Returns -1 if p1 and p2 are not of the same dimensions. */
  public static double dist(double[] p1, double[] p2) {
    if(p1.length != p2.length) {
      return -1;
    }
    else {
      double sqLen = 0;
      for(int i = 0 ; i < p1.length ; i++) {
        double dN = p1[i] - p2[i];
        sqLen += dN*dN;
      }
      return Math.sqrt(sqLen);
    }
  }
  
  
  
  
  //////// Vector math
  
  /** Returns the length of a vector of n-dimensional size. */
  public static double length(double[] vector) {
    double sqLen = 0;
    for(int i = 0; i < vector.length; i++) {
      sqLen += vector[i]*vector[i];
    }
    return Math.sqrt(sqLen);
  }
  
  /** Computes the normalized form of an n-dimensional vector. */
  public static double[] normalize(double[] vector) {
    double[] result = new double[vector.length];
    
    double length = length(vector);
    for(int i = 0; i < vector.length; i++) {
      result[i] = vector[i]/length;
    }
    return result;
  }
  
  /** Computes the negation of an n-dimensional vector. */
  public static double[] negate(double[] vector) {
    return scale(vector, -1);
  }
  
  /** Computes the scaled form of an n-dimensional vector. */
  public static double[] scale(double[] vector, double scale) {
    double[] result = new double[vector.length];
    
    for(int i = 0; i < vector.length; i++) {
      result[i] = vector[i] * scale;
    }
    return result;
  }
  
  /** Computes the translated form of a vector. */
  public static double[] translate(double[] vector, double[] t) {
    return add(vector, t);
  }
  
  /** Computes the sum of two n-dimensional vectors. The result vector's size is the maximum size of the input vectors. */
  public static double[] add(double[] v1, double[] v2) {
    double[] result;
    if(v1.length > v2.length) {
      result = new double[v1.length];
      
      for(int i = 0; i < v1.length; i++) {
        if(i < v2.length) {
          result[i] = v1[i] + v2[i];
        }
        else {
          result[i] = v1[i];
        }
      }
    }
    else {
      result = new double[v2.length];
      
      for(int i = 0; i < v2.length; i++) {
        if(i < v1.length) {
          result[i] = v1[i] + v2[i];
        }
        else {
          result[i] = v2[i];
        }
      }
    }
    
    return result;
  }
  
  /** Computes the difference of two n-dimensional vectors. */
  public static double[] sub(double[] v1, double[] v2) {
    return add(v1, negate(v2));
  }
  
  
  /** Compute the dot-product of two n-dimensional vectors. */
  public static double dot(double[] v1, double[] v2) {
    double result = 0;
    for(int i = 0; i < v1.length; i++) {
      result += v1[i]*v2[i];
    }
    return result;
  }
  
  /** 
   * Compute the cross-product of two 3D homogenous vectors. 
   * The result is the 3D homogenous vector orthogonal to the input vectors. 
   */
  public static double[] cross3D(double[] v1, double[] v2) {
    v1 = extend(v1, 3);
    v2 = extend(v2, 3);
    
    double[] result = new double[4];
    result[0] = v1[1]*v2[2] - v1[2]*v2[1];
    result[1] = v1[2]*v2[0] - v1[0]*v2[2];
    result[2] = v1[0]*v2[1] - v1[1]*v2[0];
    result[3] = 0;
    return result;
  }
  
  
  /** Compute the smaller angle between two vectors, in degrees. */
  public static double angle(double[] v1, double[] v2) {
    double dot = dot(v1, v2);
    double cos = dot/length(v1)/length(v2);
    cos = clamp(cos, -1, 1);
    return r2d(Math.acos(cos));
  }
  
  /** Compute the smaller angle between two vectors, in radians. */
  public static double angleR(double[] v1, double[] v2) {
    double dot = dot(v1, v2);
    double cos = dot/length(v1)/length(v2);
    return Math.acos(cos);
  }
  
  /** Returns a String representation for a vector. */
  public static String toString(double[] vector) {
    String result = "[";
    for(int i = 0; i < vector.length; i++) {
      if(i > 0) {
        result += ", ";
      }
      result += vector[i];
    }
    return result + "]";
  }
  
  
  /** Truncates a vector to be no larger than the given size. */
  public static double[] truncate(double[] vector, int size) {
    if(vector.length <= size) {
      return vector;
    }
    else {
      double[] result = new double[size];
      for(int i = 0; i < size; i++) {
        result[i] = vector[i];
      }
      return result;
    }
  }
  
  
  /** Extends a vector to be no smaller than the given size by padding with 0s. */
  public static double[] extend(double[] vector, int size) {
    if(vector.length >= size) {
      return vector;
    }
    else {
      double[] result = new double[size];
      for(int i = 0; i < size; i++) {
        if(i < vector.length) {
          result[i] = vector[i];
        }
        else {
          result[i] = 0;
        }
      }
      return result;
    }
  }
  
  /** Sets the size for a vector, truncating it or extending it as needed. */
  public static double[] setSize(double[] vector, int size) {
    return truncate(extend(vector, size), size);
  }
  
  
  /** Returns true iff the vector is zero, given some tolerance. */
  public static boolean isZero(double[] vector, double tolerance) {
    for(int i = 0; i < vector.length; i++) {
      if(Math.abs(vector[i]) >= tolerance) {
        return false;
      }
    }
    return true;
  }
  
  /** Returns true iff the vector is zero. */
  public static boolean isZero(double[] vector) {
    for(int i = 0; i < vector.length; i++) {
      if(vector[i] == 0) {
        return false;
      }
    }
    return true;
  }
  
  
  
  //////// Line-Line math
  
  /** 
   * Computes the point at which two infinitely stretching, 2D, straight 
   * lines intersect. Returns null if the lines don't intersect. 
   */
  public static Point2D lineIntersection(Line2D line1, Line2D line2) {
    
    double pX = line1.getX1();
    double pY = line1.getY1();
    double qX = line1.getX2();
    double qY = line1.getY2();
    
    double rX = line2.getX1();
    double rY = line2.getY1();
    double sX = line2.getX2();
    double sY = line2.getY2();

    return lineIntersection(pX, pY, qX, qY, rX, rY, sX, sY);
  }
  
  /** 
   * Computes the point at which two infinitely stretching, 2D, straight 
   * lines intersect. Returns null if the lines don't intersect. 
   */
  public static Point2D lineIntersection(double pX, double pY, double qX, double qY, double rX, double rY, double sX, double sY) {
    double pqx = pX - qX;
    double pqy = pY - qY;
    double sqx = sX - qX;
    double rsy = rY - sY;
    double rsx = rX - sX;
    double sqy = sY - qY;
    
    double top    = sqx*pqy - pqx*sqy;
    double bottom = pqx*rsy - rsx*pqy;
    
    if(bottom == 0) {
      return null;
    }
    
    double alpha = top/bottom;
    double x = alpha * rX + (1-alpha)*sX;
    double y = alpha * rY + (1-alpha)*sY;
    
    return new Point2D.Double(x, y);
  }
  
  
  /** 
   * Finds the point of intersection between two line segments. 
   * Returns null if the segments don't intersect. 
   */
  public static Point2D segIntersection(double px, double py, double qx, double qy, double rx, double ry, double sx, double sy) {
    
    double[] p = new double[] {px, py};
    double[] r = new double[] {rx, ry};
    double[] u = new double[] {qx - px, qy - py};
    double[] v = new double[] {sx - rx, sy - ry};
    
    // compute the parametric scalar for our first line.
    double[] bottom = GameMath.cross3D(u, v);
    if(bottom[2] == 0) {
      // The segments are parallel.
      return null;
    }
    double[] top = GameMath.cross3D(GameMath.sub(r, p), v);
    double alpha = top[2]/bottom[2];
    
    // Compute the potential point of intersection.
    double x = p[0] + alpha*u[0];
    double y = p[1] + alpha*u[1];
    
    // compute the parametric scalar for our second line.
    double beta;
    if(v[0] != 0) {
      beta = (x - rx)/v[0];
    }
    else {
      beta = (y - ry)/v[1];
    }
    
    // The segments intersect if both alpha and beta lie in the range [0,1].
    if(alpha >= 0 && alpha <= 1 && beta >= 0 && beta <= 1) {
      return new Point2D.Double(x, y);
    }
    else {
      return null;
    }
  }
  
  /** 
   * Finds the point of intersection between two line segments. 
   * Returns null if the segments don't intersect. 
   */
  public static Point2D segIntersection(Line2D line1, Line2D line2) {
    return segIntersection(line1.getX1(), line1.getY1(), line1.getX2(), line1.getY2(),
                           line2.getX1(), line2.getY1(), line2.getX2(), line2.getY2());
  }
  
  
  /** 
   * Returns true iff the given lines are effectively parallel 
   * within some tolerance. 
   */
  public static boolean parallel(double px, double py, double qx, double qy, double rx, double ry, double sx, double sy, double tolerance) {
    double[] u = new double[] {qx - px, qy - py};
    double[] v = new double[] {sx - rx, sy - ry};
    
    return (angleR(u, v) < tolerance);
  }
  
  /** 
   * Returns true iff the given lines are effectively parallel 
   * within some tolerance. 
   */
  public static boolean parallel(Line2D line1, Line2D line2, double tolerance) {
    return parallel(line1.getX1(), line1.getY1(), line1.getX2(), line1.getY2(),
                    line2.getX1(), line2.getY1(), line2.getX2(), line2.getY2(),
                    tolerance);
  }
  
  
  /** 
   * Returns true iff the given lines are effectively parallel within a 
   * tolerance of 0.0001.
   */
  public static boolean parallel(double px, double py, double qx, double qy, double rx, double ry, double sx, double sy) {
    return parallel(px, py, qx, qy,
                    rx, ry, sx, sy,
                    0.0001);
  }
  
  /** 
   * Returns true iff the given lines are effectively parallel within a 
   * tolerance of 0.0001.
   */
  public static boolean parallel(Line2D line1, Line2D line2) {
    return parallel(line1.getX1(), line1.getY1(), line1.getX2(), line1.getY2(),
                    line2.getX1(), line2.getY1(), line2.getX2(), line2.getY2());
  }
  
  
  
  public double[] toVector(Line2D line) {
    return new double[] {line.getX2() - line.getX1(), line.getY2() - line.getY1(), 0, 0};
  }
  
  public double[] toVector(Point2D p) {
    return new double[] {p.getX(), p.getY(), 0, 1};
  }
  
  
  //////// Point-Line math
  
  /** 
   * Determines whether some point lies above a line in the default  
   * Java 2D coordinate system. 
   * If the system is rotated such that the line's vector points in the 
   * positive x axis direction, this method returns true if the given point
   * in the rotated system is above the line (that is, it's y position is less 
   * than the horizontal y position of the line). 
   * Otherwise, it returns false.
   */
  public static boolean isPointAboveLine(double px, double py, double x1, double y1, double x2, double y2) {
    return (Line2D.relativeCCW(x1, y1, x2, y2, px, py) > 0);
  }
  
  public static boolean isPointAboveLine(Point2D p, double x1, double y1, double x2, double y2) {
    return isPointAboveLine(p.getX(), p.getY(), x1, y1, x2, y2);
  }
  
  public static boolean isPointAboveLine(Point2D p, Line2D line) {
    return (line.relativeCCW(p) > 0);
  }
  
  /** 
   * Determines whether some point lies below a line in the default 
   * Java 2D coordinate system.
   * See isPointAboveLine.
   */
  public static boolean isPointBelowLine(double px, double py, double x1, double y1, double x2, double y2) {
    return (Line2D.relativeCCW(x1, y1, x2, y2, px, py) < 0);
  }
  
  public static boolean isPointBelowLine(Point2D p, double x1, double y1, double x2, double y2) {
    return isPointBelowLine(p.getX(), p.getY(), x1, y1, x2, y2);
  }
  
  public static boolean isPointBelowLine(Point2D p, Line2D line) {
    return (line.relativeCCW(p) < 0);
  }
  
  /** Returns the distance of a point to a line extending infinitely in two directions. */
  public static double distToLine(double px, double py, double x1, double y1, double x2, double y2) {
    return Line2D.ptLineDist(x1, y1, x2, y2, px, py);
  }
  
  public static double distToLine(Point2D p, Line2D line) {
    return distToLine(p.getX(), p.getY(), line.getX1(), line.getY1(), line.getX2(), line.getY2());
  }
  
  /** Returns the distance of a point to a line segment. */
  public static double distToSegment(double px, double py, double x1, double y1, double x2, double y2) {
    return Line2D.ptSegDist(x1, y1, x2, y2, px, py);
  }
  
  public static double distToSegment(Point2D p, Line2D line) {
    return distToSegment(p.getX(), p.getY(), line.getX1(), line.getY1(), line.getX2(), line.getY2());
  }
  
  
  //////// java.lang.Math reimplementations
  
  /** 
   * Raise a number to an integer power (which are much more commonly used in 
   * game programming anyways). 
   * This method is a little faster than Math.pow, because we don't need to make
   * any JNI calls here.
   */
  public static double pow(double a, int b) {
    double result = 1;
    if(b > 0) {
      result = 1;
      for(int i = 1; i <= b; i++) {
        result *= a;
      }      
    }
    else if(b < 0) {
      result = 1;
      for(int i = 1; i <= Math.abs(b); i++) {
        result /= a;
      }
    }
    
    return result;
  }
  
  
  //////// Comparators
  
  private static Comparator<Point2D> xyComp = null;
  
  /** 
   * Returns a Comparator that compares points first in ascending X order, 
   * then in ascending Y order.
   */
  public static Comparator<Point2D> getXYComparator() {
    if(xyComp == null) {
      xyComp = new Comparator<Point2D>() {
        
        public int compare(Point2D p1, Point2D p2) {
          if(p1.getX() < p2.getX()) {
            return -1;
          }
          else if(p1.getX() > p2.getX()) {
            return 1;
          }
          else {
            if(p1.getY() < p2.getY()) {
              return -1;
            }
            else if(p1.getY() > p2.getY()) {
              return 1;
            }
            else {
              return 0;
            }
          }
        }
      };
    }
    return xyComp;
  }
  
  
  
  private static Comparator<Point2D> xComp = null;
  
  /** Returns a Comparator that compares points in ascending X order. */
  public static Comparator<Point2D> getXComparator() {
    if(xComp == null) {
      xComp = new Comparator<Point2D>() {
        public int compare(Point2D p1, Point2D p2) {
          if(p1.getX() < p2.getX()) {
            return -1;
          }
          else if(p1.getX() > p2.getX()) {
            return 1;
          }
          else {
            return 0;
          }
        }
      };
    }
    return xComp;
  }
  
  
  private static Comparator<Point2D> yComp = null;
  
  /** Returns a Comparator that compares points in ascending Y order. */
  public static Comparator<Point2D> getYComparator() {
    if(yComp == null) {
      yComp = new Comparator<Point2D>() {
        public int compare(Point2D p1, Point2D p2) {
          if(p1.getY() < p2.getY()) {
            return -1;
          }
          else if(p1.getY() > p2.getY()) {
            return 1;
          }
          else {
            return 0;
          }
        }
      };
    }
    return yComp;
  }
}




