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
import java.awt.geom.Point2D;

/** A class with several static methods for performing various common mathematical computations. */
public class GameMath {
   /** A conventience Random object. */
	public static Random rand = new Random();
	
   /** Converts an angle from degrees to radians. */
	public static double d2r(double degrees) {
		return degrees*Math.PI/180.0;
	}
   
   /** Converts an angle from radians to degrees. */
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
	public static double cos(double degrees)
	{
		int angle = (int)degrees % 360;
		if(angle < 0)
			angle += 360;
		return Math.cos(d2r(degrees));
	}
	
	
	/** Returns the angle in degrees from one point to another, going clockwise from the positive X axis. */
	public static double angleTo(double origX, double origY, double destX, double destY)
	{
		double dx = destX - origX;
		if(dx == 0)
			dx = 0.0001;
		double dy = origY - destY;
		
		if(dx > 0)
		{
			return r2d(Math.atan(dy/dx));
		}
		else
		{
			return 180 + r2d(Math.atan(dy/dx));
		}
	}
	
	/** Returns the square distance from one point to another. This is faster for doing things such as collision detections. */
	public static double sqrDist(double origX, double origY, double destX, double destY)
	{
		double a = origX - destX;
		double b = origY - destY;
		
		return a*a + b*b;
	}
	
	/** Returns the distance from one point to another. */
	public static double dist(double origX, double origY, double destX, double destY)
	{
		double a = origX - destX;
		double b = origY - destY;
		
		return Math.sqrt(a*a + b*b);
	}
	
	/** Returns + if faster to rotate clockwise. Returns - if faster to rotate counter-clockwise. Returns 0 if curAngle = destAngle. */
	public static double rotateTowardAngle(double curAngle, double destAngle) {
		// normalize curAngle to be in [0,360)
		if(curAngle >= 360) curAngle -= 360*((int)curAngle/360);
		if(curAngle < 0) curAngle += 360*((int)curAngle/360 + 1);
		
		// normalize destAngle to be in [0,360)
		if(destAngle >= 360) destAngle -= 360*((int)destAngle/360);
		if(destAngle < 0) destAngle += 360*((int)destAngle/360 + 1);
		
		double destAngle2;
		if(destAngle < 180) destAngle2 = destAngle + 360;
		else destAngle2 = destAngle - 360;
		
		double best;
		if(Math.abs(destAngle - curAngle) < Math.abs(destAngle2 - curAngle)) best = destAngle;
		else best = destAngle2;
		
		best -= curAngle;
		
		return best;
	}
	
	
	
	/** 
    * Returns the signed normal distance of point p from a vector intersecting q in the direction towards r. 
    * If the result is positive, then p is above the vector. 
    * If it is negative, then p is below the vector.  
    * @param q    The "start" point of the vector.
    * @param r    The "end" point of the vector.
    * @param p    The point we're checking to be above or below the vector.
    */
	public static double isPointAboveVector(Point2D q, Point2D r, Point2D p) {
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
		
		
	}

}




