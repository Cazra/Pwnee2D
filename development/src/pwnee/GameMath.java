package gameEngine;

import java.util.Random;
import java.awt.geom.Point2D;

public class GameMath
{
	public static double[] sinTable = new double[360];
	public static double[] cosTable = new double[360];
	public static Random rand = new Random();
	
	public static void setUpTrigTables()
	{
		for(int i = 0; i < 360; i++)
		{
			sinTable[i] = Math.sin(d2r(i));
			cosTable[i] = Math.cos(d2r(i));
		}
	}
	
	
	public static double d2r(double degrees)
	{
		return degrees/360.0*2*Math.PI;
	}
	
	public static double sin(double degrees)
	{
		int angle = (int)degrees % 360;
		if(angle < 0)
			angle += 360;
		return sinTable[angle];
		//return Math.sin(d2r(degrees));
	}
	
	public static double cos(double degrees)
	{
		int angle = (int)degrees % 360;
		if(angle < 0)
			angle += 360;
		return cosTable[angle];
		//return Math.cos(d2r(degrees));
	}
	
	public static double radToDeg = 360.0/(2.0*Math.PI);
	
	public static double getAngleTo(double origX, double origY, double destX, double destY)
	{
		double dx = destX - origX;
		if(dx == 0)
			dx = 0.0001;
		double dy = origY - destY;
		
		if(dx > 0)
		{
			return Math.atan(dy/dx)*radToDeg;
		}
		else
		{
			return 180 + Math.atan(dy/dx)*radToDeg;
		}
	}
	
	
	public static double getSqrDist(double origX, double origY, double destX, double destY)
	{
		double a = origX - destX;
		double b = origY - destY;
		
		return a*a + b*b;
	}
	
	
	public static double getDist(double origX, double origY, double destX, double destY)
	{
		double a = origX - destX;
		double b = origY - destY;
		
		return Math.sqrt(a*a + b*b);
	}
	
	/** Returns + to if faster to rotate clockwise. Returns - if faster to rotate counter-clockwise. Returns 0 if curAngle = destAngle. */
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




