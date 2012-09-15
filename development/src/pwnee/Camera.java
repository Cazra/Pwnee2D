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

import java.awt.geom.AffineTransform;
import java.awt.Component;
import java.awt.geom.Point2D;

import pwnee.GameMath;

/** A "camera" object that creates a transform for a panned, zoomed, and rotated view. */
public class Camera {
   
   /** A reference to Component using this Camera. */
   public Component screen;
   
   /** The screen's current width. */
   public int width;
   
   /** The screen's current height. */
   public int height;
   
   /** The world position of the Camera's focus. */
   public Point2D position = new Point2D.Double(0,0);
   
   /** The world X coordinate of the Camera's focus. */
   public double x = 0.0;
   
   /** The world Y coordinate of the Camera's focus. */
   public double y = 0.0;
   
   /** The screen position of the Camera's focus. (Usually this stays at the center of the screen) */
   public Point2D focalPosition;
   
   /** The screen X coordinate of the Camera's focus. */
   public double focalX;
   
   /** The screen Y coordinate of the Camera's focus. */
   public double focalY;
   
   /** 
    * The zoom level of the camera. Default is 1.0. 
    * When zoom < 1.0, it is zoomed out. 
    * When zoom > 1.0, it is zoomed in. 
    * This is effectively applies a uniform scale on the entire scene centered on the Camera's
    * focal position.
    */
   public double zoom = 1.0;
   
   /** The camera's tilt, in degrees. This is the angle of the Camera's positive X axis from the world's positive X axis going counter-clockwise. */
   public double angle = 0.0;
   
   /** The world position of the top-left corner of the screen. */
   public Point2D screenTopLeft;
   
   /** The world position of the top-right corner of the screen. */
   public Point2D screenTopRight;
   
   /** The world position of the bottom-left corner of the screen. */
   public Point2D screenBottomLeft;
   
   /** The world position of the bottom-right corner of the screen. */
   public Point2D screenBottomRight;
   
   /** The world X coordinate of the screen's left edge. (Only useful if the Camera's angle = 0.0) */
   public double xLeftEdge;
   
   /** The world X coordinate of the screen's right edge. (Only useful if the Camera's angle = 0.0) */
   public double xRightEdge;
   
   /** The world Y coordinate of the screen's top edge. (Only useful if the Camera's angle = 0.0) */
   public double yTopEdge;
   
   /** The world Y coordinate of the screen's bottom edge. (Only useful if the Camera's angle = 0.0) */
   public double yBottomEdge;
   
   /** A reference to the Camera's current concatenated view transform. Intended to be read-only! (Useful to convert a point from world coordinates to screen coordinates) */
   public AffineTransform trans = new AffineTransform();
   
   /** A reference to the Camera's current concatenated view inverse transform. Intended to be read-only! (Useful for converting a point from screen coordinates to world coordinates) */
   public AffineTransform inv = new AffineTransform();
   
   /** Creates the Camera with its focal world coordinates at (0,0), its focal screen coordinates in the center of its screen, its zoom at 1.0, and its angle at 0.0. */
   public Camera(Component screen) {
      this.screen = screen;
      width = screen.getSize().width;
      height = screen.getSize().height;
      
      focalX = width/2.0;
      focalY = height/2.0;
      
      update();
   }
   
   /** Resets the Camera to its default state. */
   public void reset() {
      width = screen.getSize().width;
      height = screen.getSize().height;
      x = 0.0;
      y = 0.0;
      focalX = width/2.0;
      focalY = height/2.0;
      zoom = 1.0;
      angle = 0.0;
      
      update();
   }
   
   /** Updates the current view transform and screen data to reflect the Camera's state. */
   public void update() {
      // Create the concatenated view transform.
      trans = new AffineTransform();
      trans.translate(focalX, focalY);
      trans.scale(zoom,zoom);
      trans.rotate(GameMath.d2r(angle));
      trans.translate(0-x, 0-y);
      
      // Create its inverse.
      try {
         inv = trans.createInverse();
      }
      catch(Exception e) {
      }
      
      // compute the world coordinate data for the screen's corners and edges.
      screenTopLeft = inv.transform(new Point2D.Double(0,0), null);
      screenTopRight = inv.transform(new Point2D.Double(width, 0), null);
      screenBottomLeft = inv.transform(new Point2D.Double(0, height), null);
      screenBottomRight = inv.transform(new Point2D.Double(width, height), null);
      
      xLeftEdge = screenTopLeft.getX();
      xRightEdge = screenBottomRight.getX();
      yTopEdge = screenTopLeft.getY();
      yBottomEdge = screenBottomRight.getY();
      
      // update the Point2D representation of the camera's focal positions.
      position = new Point2D.Double(x,y);
      focalPosition = new Point2D.Double(focalX, focalY);
   }
   
   
   /** Returns a copy of the Camera's current view transform, in case you REALLY want to do operations on it. */
   public AffineTransform getTrans() {
      return new AffineTransform(trans);
   }
   
   /** Returns a copy of the Camera's current view inverse transform. */
   public AffineTransform getInv() {
      return new AffineTransform(trans);
   }
   
   
   /** Converts a point from screen coordinates to camera world coordinates. */
   public Point2D screenToWorld(Point2D p) {
      return inv.transform(p,null);
   }
   
   /** Shorthand screenToWorld. */
   public Point2D s2w(Point2D p) {
      return screenToWorld(p);
   }
   
   /** Converts a point from camera world coordinates to screen coordinates. */
   public Point2D worldToScreen(Point2D p) {
      return trans.transform(p,null);
   }
   
   /** Shorthand worldToScreen */
   public Point2D w2s(Point2D p) {
      return worldToScreen(p);
   }
   
   
   
   /** Moves the Camera's focal position to new screen coordinates without altering the current view transform. */
   public void moveCenter(Point2D screenPos) {
      // Create a vector for the change in the Camera's focal screen position.
      double dfx = screenPos.getX() - focalX;
      double dfy = screenPos.getY() - focalY;
      Point2D df = new Point2D.Double(dfx, dfy);
      
      // Set the Camera's new focal screen coordinates.
      focalX = screenPos.getX();
      focalY = screenPos.getY();
      
      // Create an inverse transform of only the scale and rotate components of the current view transform.
      AffineTransform scaleRotate = new AffineTransform();
      scaleRotate.scale(zoom, zoom);
      scaleRotate.rotate(GameMath.d2r(angle));
      AffineTransform scaleRotateInv = new AffineTransform();
      try {
         scaleRotateInv = scaleRotate.createInverse();
      }
      catch(Exception e) {
      }
      
      // apply this transform to our vector and add the transformed vector to the Camera's focal world coordinates.
      Point2D df2 = scaleRotateInv.transform(df, null);
      x += df2.getX();
      y += df2.getY();
      
      update();
   }
   
   
   // used by the drag ad endDrag methods
   private boolean wasDragging = false;
   private Point2D dragAnchor;
   private Point2D dragStartPt;
   private AffineTransform dragInv;
   
   /** Used to move the camera using some object with screen coordinates (such as the mouse). */
   public void drag(Point2D screenPt) {
      // If we just started dragging the camera, update its anchor state.
      if(!wasDragging) this.updateDrag(screenPt);
      
      // Create a vector from screenPt's world position to the drag anchor point.
      Point2D worldPt = s2w(screenPt);
      Point2D vector = new Point2D.Double(dragAnchor.getX() - worldPt.getX(), dragAnchor.getY() - worldPt.getY());
      
      // update the Camera's focal world and screen coordinates.
      x = dragStartPt.getX() + vector.getX();
      y = dragStartPt.getY() + vector.getY();
      moveCenter(screenPt);
      
      // update the anchor state and drag timer.
      updateDrag(screenPt);
   }
   
   /** Updates the drag state */
   private void updateDrag(Point2D screenPt) {
      dragAnchor = s2w(screenPt);
      dragStartPt = new Point2D.Double(x,y);
      dragInv = new AffineTransform(inv);
      wasDragging = true;
   }
   
   /** Ends the camera drag. */
   public void endDrag() {
      wasDragging = false;
   }
   
   
   /** Changes the zoom and immediately updates the Camera. */
   public void updateZoom(double zoom) {
      this.zoom = zoom;
      update();
   }
   
   /** Changes the angle and immediately updates the Camera. */
   public void updateAngle(double angle) {
      this.angle = angle;
      update();
   }
   
   /** Changes the Camera focus's world position and immediately updates the Camera. */
   public void updatePos(double x, double y) {
      this.x = x;
      this.y = y;
      update();
   }
   
   
   /** Zooms in or out on a point in screen coordinates. */
   public void zoomAtScreen(double zoomMult, Point2D screenPt) {
      moveCenter(screenPt);
      zoom *= zoomMult;
      update();
   }
   
   /** Zooms in or out on a point in screen coordinates. */
   public void zoomAtScreen(double zoomMult, double x, double y) {
      zoomAtScreen(zoomMult, x, y);
   }
   
   
   /** Zooms in or out on a point in world coordinates. */
   public void zoomAtWorld(double zoomMult, Point2D worldPt) {
      Point2D screenPt = w2s(worldPt);
      zoomAtScreen(zoomMult, screenPt);
   }
   
   /** Zooms in or out on a point in world coordinates. */
   public void zoomAtWorld(double zoomMult, double x, double y) {
      zoomAtWorld(zoomMult, x, y);
   }
   
}






