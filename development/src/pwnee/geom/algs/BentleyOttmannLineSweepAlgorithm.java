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
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import pwnee.GameMath;
import pwnee.util.BalancedBinaryTree;
import pwnee.util.BinaryTreeNode;
import pwnee.util.SortedArrayList;

/** 
 * A utility class for performing the Bentley-Ottmann line sweep algorithm for 
 * finding the points of intersection between two sets of line segments.
 * See: http://geomalgorithms.com/a09-_intersect-3.html
 */
public class BentleyOttmannLineSweepAlgorithm {
  
  private Set<Point2D> result;
  private Set<SweepIntersection> visited;
  private PriorityQueue<SweepEvent> eventQueue;
  private BalancedBinaryTree<SweepLeft> sweepLine;
  
  private BentleyOttmannLineSweepAlgorithm() {
    result = new HashSet<>();
    visited = new HashSet<>();
    eventQueue = new PriorityQueue<>();
    sweepLine = new BalancedBinaryTree(getSweepComparator());
  }
  
  public static Set<Point2D> getIntersections(Set<Line2D> segments) {
    BentleyOttmannLineSweepAlgorithm algorithm = new BentleyOttmannLineSweepAlgorithm();
    return algorithm.run(segments);
  }
  
  
  private Set<Point2D> run(Set<Line2D> segments) {
    
    // Populate our event queue with the segment endpoints.
    for(Line2D seg : segments) {
      SweepEvent[] segEvents = SweepEvent.createEvents(seg);
      eventQueue.add(segEvents[0]);
      eventQueue.add(segEvents[1]);
    }
    
    
    while(!eventQueue.isEmpty()) {
      SweepEvent e = eventQueue.remove();
      
      if(e instanceof SweepLeft) {
        SweepLeft left = (SweepLeft) e;
        sweepLine.add(left);
        
        // Test for intersections between this segment and the ones above and below it.
        BinaryTreeNode<SweepLeft> curNode = sweepLine.getNode(left);
        BinaryTreeNode<SweepLeft> prevNode = curNode.getPrev();
        BinaryTreeNode<SweepLeft> nextNode = curNode.getNext();
        
        tryIntersection(curNode, prevNode);
        tryIntersection(curNode, nextNode);
      }
      else if(e instanceof SweepRight) {
        SweepRight right = (SweepRight) e;
        
        // Test for intersection between the segments above and below this one.
        BinaryTreeNode<SweepLeft> curNode = sweepLine.getNode(right.left);
        BinaryTreeNode<SweepLeft> prevNode = curNode.getPrev();
        BinaryTreeNode<SweepLeft> nextNode = curNode.getNext();
        
        tryIntersection(prevNode, nextNode);
        
        // Remove the segment from the sweep line.
        sweepLine.remove(right.left);
      }
      else if(e instanceof SweepIntersection) {
        SweepIntersection intersection = (SweepIntersection) e;
        
        if(!visited.contains(intersection)) {
          visited.add(intersection);
          
          // The intersecting segments swap places after adjustment.
          sweepLine.remove(intersection.upperLeft);
          sweepLine.remove(intersection.lowerLeft);
          SweepLeft newUpper = new SweepLeft(intersection.point, new Line2D.Double(intersection.point, intersection.lowerLeft.right));
          newUpper.setRight(intersection.lowerLeft.right);
          SweepLeft newLower = new SweepLeft(intersection.point, new Line2D.Double(intersection.point, intersection.upperLeft.right));
          newLower.setRight(intersection.upperLeft.right);
          
          eventQueue.add(newUpper);
          eventQueue.add(newLower);
        }
      }
    }
    
    
    return result;
  }
  
  private static Comparator<SweepLeft> sweepComp = null;
  
  private static Comparator<SweepLeft> getSweepComparator() {
    if(sweepComp == null) {
      final Comparator<Point2D> yComp = GameMath.getYComparator();
      
      sweepComp = new Comparator<SweepLeft>() {
        public int compare(SweepLeft line1, SweepLeft line2) {
          Point2D[] points1 = sortEndPoints(line1.segment);
          Point2D[] points2 = sortEndPoints(line2.segment);
          
          int compare1 = yComp.compare(points1[0], points2[0]);
          if(compare1 == 0) {
            return yComp.compare(points1[1], points2[1]);
          }
          else {
            return compare1;
          }
        }
      };
    }
    return sweepComp;
  }
  
  
  /** Sorts the two endpoints of a line in XY order. */
  private static Point2D[] sortEndPoints(Line2D line) {
    Point2D p1 = line.getP1();
    Point2D p2 = line.getP2();
    
    Comparator<Point2D> comp = GameMath.getXYComparator();
    if(comp.compare(p1, p2) > 0) {
      Point2D temp = p1;
      p1 = p2;
      p2 = temp;
    }
    
    return new Point2D[] {p1, p2};
  }
  
  
  /** Tries to add the intersection between two events to the event queue. */
  private void tryIntersection(BinaryTreeNode<SweepLeft> upperNode, BinaryTreeNode<SweepLeft> lowerNode) {
    if(upperNode == null || lowerNode == null) {
      return;
    }
    SweepLeft upper = upperNode.getValue();
    SweepLeft lower = lowerNode.getValue();
    
    Point2D i = upper.getIntersection(lower);
    if(i != null) {
      result.add(i);
      SweepIntersection intersection = new SweepIntersection(i, upper, lower);
      eventQueue.add(intersection);
    }
  }
}


abstract class SweepEvent extends Point2D implements Comparable<SweepEvent> {
  
  /** The endpoint this event represents. */
  public Point2D point;
  
  public Line2D segment;
  
  public SweepEvent(Point2D point, Line2D segment) {
    this.point = point;
    this.segment = segment;
  }
  
  public static SweepEvent[] createEvents(Line2D segment) {
    Point2D p1 = segment.getP1();
    Point2D p2 = segment.getP2();
    
    Comparator<Point2D> comp = GameMath.getXYComparator();
    if(comp.compare(p1, p2) > 0) {
      Point2D temp = p1;
      p1 = p2;
      p2 = temp;
    }
    
    SweepLeft left = new SweepLeft(p1, segment);
    SweepRight right = new SweepRight(p2, segment);
    
    left.setRight(right);
    
    return new SweepEvent[] {left, right};
  }
  
  
  public double getX() {
    return point.getX();
  }
  
  public double getY() {
    return point.getY();
  }
  
  public void setLocation(double x, double y) {
    // do nothing.
  }
  
  
  public boolean intersects(SweepEvent other) {
    return this.segment.intersectsLine(other.segment);
  }
  
  
  public Point2D getIntersection(SweepEvent other) {
    return GameMath.segIntersection(this.segment, other.segment);
  }
  
  
  public int compareTo(SweepEvent other) {
    int compare = GameMath.getXYComparator().compare(this, other);
    if(compare == 0) {
      int thisPriority = this.getPriority();
      int otherPriority = other.getPriority();
      
      if(thisPriority < otherPriority) {
        return -1;
      }
      else if(thisPriority > otherPriority) {
        return 1;
      }
      else {
        return 0;
      }
    }
    else {
      return compare;
    }
  }
  
  
  public abstract int getPriority();
} 

/** An event representing the "left" endpoint of a segment. */
class SweepLeft extends SweepEvent {
  
  public SweepRight right;
  
  public SweepLeft(Point2D point, Line2D segment) {
    super(point, segment);
  }
  
  public void setRight(SweepRight right) {
    this.right = right;
    right.left = this;
  }
  
  public int getPriority() {
    return 1;
  }
}

/** An event representing the "right" endpoint of a segment. */
class SweepRight extends SweepEvent {
  
  public SweepLeft left;
  
  public SweepRight(Point2D point, Line2D segment) {
    super(point, segment);
  }
  
  public void setLeft(SweepLeft left) {
    this.left = left;
    left.right = this;
  }
  
  public int getPriority() {
    return 3;
  }
}


/** An event representing an intersection between two segments. */
class SweepIntersection extends SweepEvent {
  
  public Line2D segment2;
  
  public SweepLeft upperLeft;
  
  public SweepLeft lowerLeft;
  
  public SweepIntersection(Point2D point, SweepLeft upperLeft, SweepLeft lowerLeft) {
    super(point, upperLeft.segment);
    this.upperLeft = upperLeft;
    this.lowerLeft = lowerLeft;
    this.segment2 = lowerLeft.segment;
  }
  
  /** Two intersections are equal if they have the same intersecting segments. */
  public boolean equals(Object o) {
    if(o instanceof SweepIntersection) {
      SweepIntersection other = (SweepIntersection) o;
      if(linesEqual(this.segment, other.segment) && linesEqual(this.segment2, other.segment2)) {
        return true;
      }
      else if(linesEqual(this.segment, other.segment2) && linesEqual(this.segment2, other.segment)) {
        return true;
      }
      else {
        return false;
      }
    }
    else {
      return false;
    }
  }
  
  
  private boolean linesEqual(Line2D line1, Line2D line2) {
    return ((line1.getP1().equals(line2.getP1()) && line1.getP2().equals(line2.getP2())) || 
            (line1.getP1().equals(line2.getP2()) && line1.getP2().equals(line2.getP1())));
  }
  
  public int getPriority() {
    return 2;
  }
}


