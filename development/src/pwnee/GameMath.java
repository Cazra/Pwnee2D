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
  
  
  
  
  
  //////// Line/Segment math
  
	/** 
   * Returns the signed normal distance of point p from a vector intersecting q in the direction towards r. 
   * It is assumed that the positive y axis points down.
   * If the result is positive, then p is above the vector. 
   * If it is negative, then p is below the vector.  
   * @param q    The "start" point of the vector.
   * @param r    The "end" point of the vector.
   * @param p    The point we're checking to be above or below the vector.
   */
//	public static double isPointAboveSeg(Point2D q, Point2D r, Point2D p) {
    /** 6/17/2013 - The same functional behavior is provided in Line2D.relativeCCW. 
    
    // vector b pointing in the direction of this segment from q to r. 
		double bx = r.getX() - q.getX();
		double by = r.getY() - q.getY();
		double length = Math.sqrt((bx*bx) + (by*by));
		bx/=length;
		by/=length;
		
		// vector n is the normal vector to b and a (0,0,1) defined by a x b.
		double nx = by;
		double ny = 0-bx;
		
		// compute denominator for alpha test
		double denom = bx*ny - by*nx;
		if( denom == 0)
			denom = 1;
			
		// compute alpha (the signed distance of p from b's axis).
		return (bx*(p.getY() - q.getY()) + by*(q.getX() - p.getX()))/denom;
    */
//    return Line2D.relativeCCW(q.getX(), q.getY(), r.getX(), r.getY(), p.getX(), p.getY());
//	}
  
  
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
  
  
  
  
  //////// java.lang.Math reimplementations
  
  /** 
   * Raise a number to an integer power (which are much more commonly used in 
   * game programming anyways). 
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




