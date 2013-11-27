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
    return r2d(Math.acos(cos));
  }
  
  /** Compute the smaller angle between two vectors, in radians. */
  public static double angleR(double[] v1, double[] v2) {
    double dot = dot(v1, v2);
    double cos = dot/length(v1)/length(v2);
    return Math.acos(cos);
  }
  
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
  
  //////// Line/Segment math
  
  
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
    
    return new Point2D.Double(x,y);
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
    
    return new Point2D.Double(x,y);
  }
  
  
  
  
  
  /** 
   * Computes the point of intersection between 2 segments. If an intersection
   * doesn't exist, return null.
   */
  public static Point2D segmentIntersection(double pX, double pY, double qX, double qY, double rX, double rY, double sX, double sY) {
    // TODO
    return null;
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
}




