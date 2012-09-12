package pwnee;

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
      return radians*180.0/Math.PI
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




