package pwnee.geom;

import java.awt.*;
import java.awt.geom.*;

import pwnee.GameMath;
import pwnee.sprites.Sprite;

/** 
 * Provides static methods for detecting collisions between various 2D shapes. 
 * In a lot of methods, I opted to perform operations using lighter classes and
 * primitives rather than operating on heavier classes like Shapes. 
 * I did this because it is more important for the collisions code to be 
 * fast than readable. 
 */
public class Collisions {
  
  /** Test for a collision between two circles. (fast) */
  public static boolean circle2circle(double x1, double y1, double r1, double x2, double y2, double r2) {
    double sqrR = r1 + r2;
    sqrR *= sqrR;
    return (GameMath.distSq(x1, y1, x2, y2) <= sqrR);
  }
  
  public static boolean circle2circle(Sprite s1, Sprite s2) {
    double r1 = s1.getDimensions().getWidth()/2;
    double r2 = s2.getDimensions().getWidth()/2;
    
    double sqrR = r1 + r2;
    sqrR *= sqrR;
    return (GameMath.distSq(s1.x, s1.y, s2.x, s2.y) <= sqrR);
  }
  
  public static boolean circle2circle(Ellipse2D c1, Ellipse2D c2) {
    double r1 = c1.getWidth()/2;
    double r2 = c2.getWidth()/2;
    
    double sqrR = r1 + r2;
    sqrR *= sqrR;
    return (GameMath.distSq(c1.getCenterX(), c1.getCenterY(), c2.getCenterX(), c2.getCenterY()) <= sqrR);
  }
  
  
  
  /** Test for a collision between two unrotated rectangles. (fast) */
  public static boolean rect2rectFast(double x1, double y1, double w1, double h1, double x2, double y2, double w2, double h2) {
    return !(x1 > x2 + w2 || x1 + w1 < x2 || y1 > y2 + h2 || y1 + h1 < y2);
  }
  
  public static boolean rect2rectFast(Sprite s1, Sprite s2) {
    Rectangle2D r1 = s1.getCollisionBox();
    Rectangle2D r2 = s2.getCollisionBox();
    return rect2rectFast(r1.getX(), r1.getY(), r1.getWidth(), r1.getHeight(), r2.getX(), r2.getY(), r2.getWidth(), r2.getHeight());
  }
  
  public static boolean rect2rectFast(Rectangle2D r1, Rectangle2D r2) {
    return rect2rectFast(r1.getX(), r1.getY(), r1.getWidth(), r1.getHeight(), r2.getX(), r2.getY(), r2.getWidth(), r2.getHeight());
  }
  
  
  
  /** Unit test for convex polygon collision. */
  public static void testPoly() {
    { // true
      double[] x1s = {1, 0, -1, 0};
      double[] y1s = {0, 1, 0, -1};
      
      double[] x2s = {0, 0, 2, 2};
      double[] y2s = {0, 2, 2, 0};
      
      Polygon2D poly1 = new Polygon2D(x1s, y1s);
      Polygon2D poly2 = new Polygon2D(x2s, y2s);
      
      System.out.println(poly1.intersects(poly2));
    }
    { // true
      double[] x1s = {1, 0, -1, 0};
      double[] y1s = {0, -1, 0, 1};
      
      double[] x2s = {0, 0, 2, 2};
      double[] y2s = {0, 2, 2, 0};
      
      Polygon2D poly1 = new Polygon2D(x1s, y1s);
      Polygon2D poly2 = new Polygon2D(x2s, y2s);
      
      System.out.println(poly1.intersects(poly2));
    }
    { // false
      double[] x1s = {1, 0, -1, 0};
      double[] y1s = {0, -1, 0, 1};
      
      double[] x2s = {1.5, 1.5, 2, 2};
      double[] y2s = {0, 2, 2, 0};
      
      Polygon2D poly1 = new Polygon2D(x1s, y1s);
      Polygon2D poly2 = new Polygon2D(x2s, y2s);
      
      System.out.println(poly1.intersects(poly2));
    }
    { // true, barely
      double[] x1s = {1, 0, -1, 0};
      double[] y1s = {0, -1, 0, 1};
      
      double[] x2s = {1, 1.5, 2, 2};
      double[] y2s = {0, 2, 2, 0};
      
      Polygon2D poly1 = new Polygon2D(x1s, y1s);
      Polygon2D poly2 = new Polygon2D(x2s, y2s);
      
      System.out.println(poly1.intersects(poly2));
    }
  }
  
}
