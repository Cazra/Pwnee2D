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
  
  private static final double TOL = 0.000001;
  
  
  /** The endpoint coordinates of the segment. */
  private double x1, y1, x2, y2;
  
  /** Whether this segment is known to exist on the inside of a polygon. */
  private boolean insidePoly = false;
  
  /** Constructs the line segment with its endpoints sorted. */
  public Segment2D(double x1, double y1, double x2, double y2) {
    setSortedLine(x1, y1, x2, y2);
  }
  
  
  
  /** Sets the endpoints for the segment sorted from left to right, then top to bottom. */
  private void setSortedLine(double x1, double y1, double x2, double y2) {
    if(x1 < x2) {
      this.x1 = x1;
      this.y1 = y1;
      
      this.x2 = x2;
      this.y2 = y2;
    }
    else if(x2 < x1) {
      this.x1 = x2;
      this.y1 = y2;
      
      this.x2 = x1;
      this.y2 = y1;
    }
    else {
      if(y1 < y2) {
        this.x1 = x1;
        this.y1 = y1;
        
        this.x2 = x2;
        this.y2 = y2;
      }
      else {
        this.x1 = x2;
        this.y1 = y2;
        
        this.x2 = x1;
        this.y2 = y1;
      }
    }
  }
  
  
  
  
  /** Two segments are equal iff their sorted endpoints are the same. */
  public boolean equals(Object obj) {
    if(obj instanceof Segment2D) {
      Segment2D other = (Segment2D) obj;
      
      return (this.x1 == other.x1 && 
              this.x2 == other.x2 && 
              this.y1 == other.y1 &&
              this.y2 == other.y2);
    }
    else {
      return false;
    }
  }
  
  
  /** 
   * Segments are ordered by their start points from left to right, 
   * then top to bottom. 
   * If two segments share start points, they are ordered the same way by their
   * end points.
   */
  public int compareTo(Segment2D other) {
    int compareP1 = this.compareP1(other);
    if(compareP1 == 0) {
      return this.compareP2(other);
    }
    else {
      return compareP1;
    }
  }
  
  /** Compares two segments by their start points. */
  public int compareP1(Segment2D other) {
    if(this.x1 < other.x1) {
      return -1;
    }
    else if(other.x1 < this.x1) {
      return 1;
    }
    else {
      if(this.y1 < other.y1) {
        return -1;
      }
      else if(other.y1 < this.y1) {
        return 1;
      }
      else {
        return 0;
      }
    }
  }
  
  /** Compares two segments by their end points. */
  public int compareP2(Segment2D other) {
    if(this.x2 < other.x2) {
      return -1;
    }
    else if(other.x2 < this.x2) {
      return 1;
    }
    else {
      if(this.y2 < other.y2) {
        return -1;
      }
      else if(other.y2 < this.y2) {
        return 1;
      }
      else {
        return 0;
      }
    }
  }
  
  
  /** Returns a Comparator that compares segments only by their y position. */
  public Comparator<Segment2D> getYComparator() {
    return new Comparator<Segment2D>() {
      
      public int compare(Segment2D s1, Segment2D s2) {
        if(s1.y1 < s2.y1) {
          return -1;
        }
        else if(s2.y1 < s1.y1) {
          return 1;
        }
        else {
          return 0;
        }
      }
    };
  }
  
  
  /** A segment represents a point if its endpoints are the same. */
  public boolean isPoint() {
    return (x1 == x2 && y1 == y2);
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
  
  public Point2D getP1() {
    return new Point2D.Double(x1, y1);
  }
  
  public Point2D getP2() {
    return new Point2D.Double(x2, y2);
  }
  
  public double getX1() {
    return x1;
  }
  
  public double getX2() {
    return x2;
  }
  
  public double getY1() {
    return y1;
  }
  
  public double getY2() {
    return y2;
  }
  
  public void setLine(double x1, double y1, double x2, double y2) {
    setSortedLine(x1, y1, x2, y2);
  }
  
  
  public Rectangle2D getBounds2D() {
    double minY = Math.min(y1, y2);
    double maxY = Math.max(y1, y2);
    
    return new Rectangle2D.Double(x1, minY, x2 - x1, maxY - minY);
  }
  
  
  //////// Intersection
  
  /** 
   * Decomposes two intersecting segments into a new sorted list of non-intersecting segments. 
   * If the segments don't intersect, a sorted list of the two segments is returned.
   */
  public static Segment2D[] breakIntersection(Segment2D s1, Segment2D s2) {
    double[] intersection = getIntersection(s1, s2);
    if(intersection != null) {
    
      // Decompose the intersecting segments and return the new sorted list of segments. 
      Segment2D sA = new Segment2D(s1.x1, s1.y1, intersection[0], intersection[1]);
      Segment2D sB = new Segment2D(s2.x1, s2.y1, intersection[0], intersection[1]);
      
      Segment2D sC = new Segment2D(intersection[0], intersection[1], s1.x2, s1.y2);
      Segment2D sD = new Segment2D(intersection[0], intersection[1], s2.x2, s2.y2);
      
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
  public double[] getIntersection(Segment2D other) {
    return Segment2D.getIntersection(this, other);
  }
  
  /** 
   * Returns the point at which two segments intersect. 
   * Returns null if they don't intersect or if they are collinear. 
   */
  public static double[] getIntersection(Segment2D s1, Segment2D s2) {
    
    double[] p1 = new double[] {s1.x1, s1.y1};
    double[] p2 = new double[] {s2.x1, s2.y1};
    double[] u = s1.getVector();
    double[] v = s2.getVector();
    
    double[] bottom = GameMath.cross3D(u, v);
    if(GameMath.isZero(bottom, TOL)) {
      // The segments are parallel.
      return null;
    }
    double[] top = GameMath.cross3D(GameMath.sub(p2, p1), v);
    
    double alpha = top[2]/bottom[2];
    if(alpha >= 0 && alpha <= 1) {
    
      // The segments intersect.
      double x = p1[0] + alpha*u[0];
      double y = p1[1] + alpha*u[1];
      
      return new double[] {x, y};
    }
    else {
      
      // The segments don't intersect.
      return null;
    }
  }
  
  
  
  /** Returns true iff the given point lies on this segment. */
  public boolean containsPoint(double[] p) {
    double[] u = new double[] {p[0] - x1, p[1] - y1};
    double[] v = getVector();
    
    // Is the segment dialogal?
    if(Math.abs(v[0]) >= TOL && Math.abs(v[1]) >= TOL) {
      
      double alphaX = u[0]/v[0];
      double alphaY = u[1]/v[1];
      
      if(Math.abs(alphaX - alphaY) < TOL) {
        return (alphaX >= 0 && alphaX <= 1);
      }
      else {
        return false;
      }
    }
    
    // Is the line segment a point equivalent to p?
    else if(Math.abs(v[0]) < TOL && Math.abs(v[1]) < TOL) {
      return (Math.abs(u[0]) < TOL && Math.abs(u[1]) < TOL);
    }
    
    // Are the segment and point collinear with a vertical line?
    else if(Math.abs(v[0]) < TOL && Math.abs(u[0]) < TOL) {
      double alphaY = u[1]/v[1];
      return (alphaY >= 0 && alphaY <= 1);
    }
    
    // Are the segment and point collinear with a horizontal line?
    else if(Math.abs(v[1]) < TOL && Math.abs(u[1]) < TOL) {
      double alphaX = u[0]/v[0];
      return (alphaX >= 0 && alphaX <= 1);
    }
    
    // Error case.
    else {
      return false;
    }
  }
  
  
  //////// Parallel/Collinear
  
  /** Returns true iff this segment is parellel with another segment. */
  public boolean parallel(Segment2D other) {
    return Segment2D.parallel(this, other);
  }
  
  /** Returns true iff two segments are parallel.*/
  public static boolean parallel(Segment2D s1, Segment2D s2) {
  
    // The segments are effectively parallel if the angle between their vectors
    // is below our tolerance level.
    double[] u = new double[] {s1.x2 - s1.x1, s1.y2 - s1.y1};
    double[] v = new double[] {s2.x2 - s2.x1, s2.y2 - s2.y1};
    return (GameMath.angle(u, v) < TOL);
  }
  
  
  /** Returns the union of two collinear segments. Returns null if the segments aren't collinear. */
  public static Segment2D union(Segment2D s1, Segment2D s2) {
    if(parallel(s1, s2)) {
    
      // Get the start points sorted.
      Segment2D sTemp;
      if(s1.compareP1(s2) > 0) {
        sTemp = s1;
        s1 = s2;
        s2 = sTemp;
      }
      double[] p = new double[] {s1.x1, s1.y1};
      double[] q = new double[] {s2.x1, s2.y1};
      
      // Get the end points sorted.
      if(s1.compareP2(s2) > 0) {
        sTemp = s1;
        s1 = s2;
        s2 = sTemp;
      }
      double[] r = new double[] {s1.x2, s1.y2};
      double[] s = new double[] {s2.x2, s2.y2};
      
      Segment2D ps = new Segment2D(p[0], p[1], s[0], s[1]);
      if(ps.containsPoint(q) && ps.containsPoint(r)) {
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