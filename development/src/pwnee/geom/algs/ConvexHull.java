package pwnee.geom.algs;

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

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pwnee.GameMath;

/** 
 * A utility class for performing the convex hull algorithm for a set of points.
 */
public class ConvexHull {
  
  /** 
   * Find the set of points in clockwise order (in default Java2D geometry) 
   * defining the convex polygon containing all the points in the input set. 
   * At each step, this algorithm bisects the set of points and only keeps 
   * the points above the bisection vectors.
   * Then the points are merged.
   */
  public static List<Point2D> hull(Collection<Point2D> points) {
    List<Point2D> result = new ArrayList<>();
    
    // 3 or less points automatically form a convex hull.
    if(points.size() <= 3) {
      result.addAll(points);
    }
    else {
    
      // Sort the points from left to right.
      List<Point2D> sortedPoints = new ArrayList<>(points);
      Collections.sort(sortedPoints, GameMath.getXComparator());
      
      Point2D leftmost = sortedPoints.get(0);
      Point2D rightmost = sortedPoints.get(sortedPoints.size()-1);
      
      // Add the hull "above" the line from the leftmost point to the rightmost point.
      result.add(leftmost);
      result.addAll(_hull(sortedPoints, new Line2D.Double(leftmost, rightmost)));
      
      // Add the hull "above" the line from the rightmost point to the leftmost point.
      result.add(rightmost);
      result.addAll(_hull(sortedPoints, new Line2D.Double(rightmost, leftmost)));
    }
    
    return result;
  }
  
  
  private static List<Point2D> _hull(List<Point2D> points, Line2D line) {
    List<Point2D> result = new ArrayList<>();
    
    // Get the list of points above the bisection line.
    // Also get the point that is furthest above the line to form our next bisections.
    List<Point2D> abovePoints = new ArrayList<>();
    Point2D highest = null;
    double highestDist = -1;
    for(Point2D point : points) {
      if(GameMath.isPointAboveLine(point, line)) {
        abovePoints.add(point);
        double dist = GameMath.distToLine(point, line);
        if(dist > highestDist) {
          highestDist = dist;
          highest = point;
        }
      }
    }
    
    if(highest != null) {
      // Add hull to left.
      result.addAll(_hull(abovePoints, new Line2D.Double(line.getP1(), highest)));
      
      // Add highest as the midpoint between the two hulls.
      result.add(highest);
      
      // add hull to right
      result.addAll(_hull(abovePoints, new Line2D.Double(highest, line.getP2())));
    }
    return result;
  }

}

