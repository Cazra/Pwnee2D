package pwnee.geom;

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

import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pwnee.GameMath;

/** 
 * A line segment whose endpoints are ordered from left to right, 
 * then top to bottom. 
 * This is primarily a convenience class used by Polygon2D.
 * The way that the endpoints are sorted causes their vector to point either 
 * anywhere to the left or straight down.
 */
public class Segment2D extends Line2D implements Comparable<Segment2D> {
  
  /** The endpoint coordinates of the segment. */
  private double x1, y1, x2, y2;
  
  
  /** Constructs the line segment with its endpoints sorted. */
  public Segment2D(double x1, double y1, double x2, double y2) {
    this.x1 = x1;
    this.x2 = x2;
    this.y1 = y1;
    this.y2 = y2;
  }
  
  public Segment2D(Point2D p1, Point2D p2) {
    this(p1.getX(), p1.getY(), p2.getX(), p2.getY());
  }
  
  
  /** Two segments are equal iff their sorted endpoints are the same. */
  public boolean equals(Object obj) {
    if(obj instanceof Segment2D) {
      Segment2D other = (Segment2D) obj;
      
      Point2D[] ends1 = this.getSortedEndpoints();
      Point2D[] ends2 = other.getSortedEndpoints();
      
      return (ends1[0].equals(ends2[0]) && ends1[1].equals(ends2[1]));
    }
    else {
      return false;
    }
  }
  
  
  /** 
   * Segments are compared in line sweep order.
   */
  public int compareTo(Segment2D other) {
    Point2D[] ends1 = this.getSortedEndpoints();
    Point2D[] ends2 = other.getSortedEndpoints();
    
    Comparator<Point2D> c = getLineSweepComparator();
    
    int compare = c.compare(ends1[0], ends2[0]);
    if(compare == 0) {
      return c.compare(ends1[1], ends2[1]);
    }
    else {
      return compare;
    }
  }
  
  
  
  /** A segment represents a point if its endpoints are the same. */
  public boolean isPoint() {
    return getP1().equals(getP2());
  }
  
  /** Gets the vector from the first point to the second point in the segment. */
  public double[] getVector() {
    return new double[] {x2 - x1, y2 - y1};
  }
  
  /** Returns the length of the segment. */
  public double length() {
    return GameMath.dist(x1, y1, x2, y2);
  }
  
  
  
  
  
  //////// Line2D implementation
  
  @Override
  public Point2D getP1() {
    return new Point2D.Double(x1, y1);
  }
  
  /** Gets the first point of the segment in line-sweep order. */
  public Point2D getSortedP1() {
    return getSortedEndpoints()[0];
  }
  
  @Override
  public Point2D getP2() {
    return new Point2D.Double(x2, y2);
  }
  
  /** Gets the second point of the segment in line-sweep order. */
  public Point2D getSortedP2() {
    return getSortedEndpoints()[1];
  }
  
  @Override
  public double getX1() {
    return x1;
  }
  
  @Override
  public double getX2() {
    return x2;
  }
  
  @Override
  public double getY1() {
    return y1;
  }
  
  @Override
  public double getY2() {
    return y2;
  }
  
  @Override
  public void setLine(double x1, double y1, double x2, double y2) {
    this.x1 = x1;
    this.y1 = y1;
    this.x2 = x2;
    this.y2 = y2;
  }
  
  @Override
  public Rectangle2D getBounds2D() {
    double minY = Math.min(y1, y2);
    double maxY = Math.max(y1, y2);
    
    return new Rectangle2D.Double(x1, minY, x2 - x1, maxY - minY);
  }
  
  
  //////// Endpoint comparison
  
  /** Cached Comparator. */
  private static Comparator<Point2D> lineSweepComp = null;
  
  /** Produces a Comparator that sorts end points from left to right, top to bottom. */
  public static Comparator<Point2D> getLineSweepComparator() {
    if(lineSweepComp == null) {
      lineSweepComp = new Comparator<Point2D>() {
        public int compare(Point2D p1, Point2D p2) {
          if(p1.getX() == p2.getX()) {
            if(p1.getY() == p2.getY()) {
              return 0;
            }
            else if(p1.getY() < p2.getY()) {
              return -1;
            }
            else {
              return 1;
            }
          }
          else if(p1.getX() < p2.getX()) {
            return -1;
          }
          else {
            return 1;
          }
        }
      };
    }
    return lineSweepComp;
  }
  
  
  /** 
   * Gets the points of this segment sorted left to right, top to bottom. 
   * This is convenient for several algorithms that do computations with 
   * line segments. 
   */
  public Point2D[] getSortedEndpoints() {
    Comparator<Point2D> c = getLineSweepComparator();
    
    Point2D p1 = getP1();
    Point2D p2 = getP2();
    
    int compare = c.compare(p1, p2);
    if(compare <= 0) {
      return new Point2D[] {p1, p2};
    }
    else {
      return new Point2D[] {p2, p1};
    }
  }
  
  //////// Intersection
  
  /** 
   * Decomposes two intersecting segments into a new sorted list of non-intersecting segments. 
   * They are sorted from left to right, top to bottom.
   * If the segments don't intersect, a sorted list of the two segments is returned.
   */
  public static Segment2D[] breakIntersection(Segment2D s1, Segment2D s2) {
    Point2D intersection = getIntersection(s1, s2);
    if(intersection != null) {
    
      // Decompose the intersecting segments and return the new sorted list of segments. 
      Segment2D sA = new Segment2D(s1.x1, s1.y1, intersection.getX(), intersection.getY());
      Segment2D sB = new Segment2D(s2.x1, s2.y1, intersection.getX(), intersection.getY());
      
      Segment2D sC = new Segment2D(intersection.getX(), intersection.getY(), s1.x2, s1.y2);
      Segment2D sD = new Segment2D(intersection.getX(), intersection.getY(), s2.x2, s2.y2);
      
      return new Segment2D[] {sA, sB, sD, sC};
    }
    else {
      
      // no intersection. Return a list with the original segments, sorted.
      int compare = s1.compareTo(s2);
      if(compare < 0) {
        return new Segment2D[] {s1, s2};
      }
      else if (compare > 0) {
        return new Segment2D[] {s2, s1};
      }
      else {
        
        // The two segments were equal. Return a list with just one instance of the segment.
        return new Segment2D[] {s1};
      }
    }
  }
  
  
  /** 
   * Returns the point at which this segment intersects another segment. 
   * Returns null if they don't intersect or if they are collinear.
   */
  public Point2D getIntersection(Segment2D other) {
    return Segment2D.getIntersection(this, other);
  }
  
  /** 
   * Returns the point at which two segments intersect. 
   * Returns null if they don't intersect or if they are collinear. 
   */
  public static Point2D getIntersection(Segment2D s1, Segment2D s2) {
    
    double[] p1 = new double[] {s1.x1, s1.y1};
    double[] p2 = new double[] {s2.x1, s2.y1};
    double[] u = s1.getVector();
    double[] v = s2.getVector();
    
    double[] bottom = GameMath.cross3D(u, v);
    if(bottom[2] == 0) {
      // The segments are parallel.
      return null;
    }
    double[] top = GameMath.cross3D(GameMath.sub(p2, p1), v);
    
    double alpha = top[2]/bottom[2];
    if(alpha >= 0 && alpha <= 1) {
    
      // The segments intersect.
      double x = p1[0] + alpha*u[0];
      double y = p1[1] + alpha*u[1];
      
      return new Point2D.Double(x, y);
    }
    else {
      
      // The segments don't intersect.
      return null;
    }
  }
  
  
  
  /** Returns true iff the given point lies on this segment. */
  @Override
  public boolean contains(Point2D p) {
    double[] u = new double[] {p.getX() - x1, p.getY() - y1};
    double[] v = getVector();
    
    // Is the segment dialogal?
    if(v[0] != 0 && v[1] != 0) {
      
      double alphaX = u[0]/v[0];
      double alphaY = u[1]/v[1];
      
      if(alphaX - alphaY == 0) {
        return (alphaX >= 0 && alphaX <= 1);
      }
      else {
        return false;
      }
    }
    
    // Is the line segment a point equivalent to p?
    else if(v[0] == 0 && v[1] == 0) {
      return getP1().equals(p);
    }
    
    // Are the segment and point collinear with a vertical line?
    else if(v[0] == 0 && u[0] == 0) {
      double alphaY = u[1]/v[1];
      return (alphaY >= 0 && alphaY <= 1);
    }
    
    // Are the segment and point collinear with a horizontal line?
    else if(v[1] == 0 && u[1] == 0) {
      double alphaX = u[0]/v[0];
      return (alphaX >= 0 && alphaX <= 1);
    }
    
    // Error case.
    else {
      return false;
    }
  }
  
  
  /** Returns true iff this segment shares an endpoint with another segment. */
  public boolean sharesEndpoint(Segment2D other) {
    Point2D p1 = this.getP1();
    Point2D p2 = this.getP2();
    
    Point2D q1 = other.getP1();
    Point2D q2 = other.getP2();
    
    return (p1.equals(q1) || p1.equals(q2) || p2.equals(q1) || p2.equals(q2));
  }
  
  
  //////// Parallel/Collinear
  
  /** Returns true iff this segment is parellel with another segment. */
  public boolean parallel(Segment2D other) {
    return Segment2D.parallel(this, other, 0.0001);
  }
  
  /** Returns true iff two segments are parallel.*/
  public static boolean parallel(Segment2D s1, Segment2D s2, double tolerance) {
    Point2D[] ends1 = s1.getSortedEndpoints();
    Point2D[] ends2 = s2.getSortedEndpoints();
    
    // The segments are effectively parallel if the angle between their vectors
    // is below our tolerance level.
    double[] u = new double[] {ends1[1].getX() - ends1[0].getX(), ends1[1].getY() - ends1[0].getY()};
    double[] v = new double[] {ends2[1].getX() - ends2[0].getX(), ends2[1].getY() - ends2[0].getY()};
    return (GameMath.angle(u, v) < tolerance);
  }
  
  
  /** Returns the union of two collinear segments. Returns null if the segments aren't collinear. */
  public static Segment2D union(Segment2D s1, Segment2D s2) {
    if(s1.parallel(s2)) {
    
      // Get the start points sorted.
      List<Point2D> points = new ArrayList<>();
      Point2D[] pq = s1.getSortedEndpoints();
      points.add(pq[0]);
      points.add(pq[1]);
      Point2D[] rs = s2.getSortedEndpoints();
      points.add(rs[0]);
      points.add(rs[1]);
      Collections.sort(points, getLineSweepComparator());
      
      Segment2D ps = new Segment2D(points.get(0), points.get(3));
      if(ps.contains(points.get(1)) && ps.contains(points.get(2))) {
        return ps;
      }
      else {
        return null;
      } 
    }
    else {
    
      // Segments can't form a union if they aren't parallel.
      return null;
    }
  }
  
  
  
  
  //////// Rendering
  public String toString() {
    return "Segment2D((" + x1 + ", " + y1 + "), (" + x2 + ", " + y2 + "))";
  }
}