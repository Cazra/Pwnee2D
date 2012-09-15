package pwnee.input;

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

import java.awt.event.*;
import java.awt.Point;
import java.awt.Component;
import java.util.HashMap;

/** A class that can poll for keyboard input in a swing Component.*/
public class Mouse implements MouseListener, MouseMotionListener, MouseWheelListener {
   
   /** A reference to the Component this Keyboard is observing events for. */
   public Component publisher;
   
   /** The mouse's current screen position (by screen, I mean the component that is using this Mouse object). */
   public Point position = new Point(0,0);
   
   /** The mouse's screen X coordinate. */
   public int x = 0;
   
   /** The mouse's screen Y coordinate. */
   public int y = 0;
   
   /** The mouse's current system screen position (its absolute location on your computer's display) */
   public Point sysPosition = new Point(0,0);
   
   /** The mouse's system screen X coordinate. */
   public int sysX = 0;
   
   /** The mouse's system screen Y coordinate. */
   public int sysY = 0;
   
   /** True if any mouse button is pressed. */
   public boolean isAnyPressed = false;
   
   /** True if any mouse button was just pressed since last frame. */
   public boolean justAnyPressed = false;
   
   /** True if any mouse button was just released since last frame. */
   public boolean justAnyClicked = false;
   
   // private flags for keeping track of any button presses since last frame
   private boolean pressedAnySinceLastFrame = false;
   private boolean releasedAnySinceLastFrame = false;
   
   
   /** True if left button is pressed. */
   public boolean isLeftPressed = false;
   
   /** True if left button was just pressed since last frame. */
   public boolean justLeftPressed = false;
   
   /** True if left button was just released since last frame. */
   public boolean justLeftClicked = false;
   
   // private flags for keeping track of left presses.
   private boolean pressedLeftSinceLastFrame = false;
   private boolean releasedLeftSinceLastFrame = false;
   
   
   
   /** True if right button is pressed. */
   public boolean isRightPressed = false;
   
   /** True if right button was just pressed since last frame. */
   public boolean justRightPressed = false;
   
   /** True if right button was just released since last frame. */
   public boolean justRightClicked = false;
   
   // private flags for keeping track of right presses.
   private boolean pressedRightSinceLastFrame = false;
   private boolean releasedRightSinceLastFrame = false;
   
   
   
   /** True if middle button is pressed. */
   public boolean isMiddlePressed = false;
   
   /** True if middle button was just pressed since last frame. */
   public boolean justMiddlePressed = false;
   
   /** True if middle button was just released since last frame. */
   public boolean justMiddleClicked = false;
   
   // private flags for keeping track of Middle presses.
   private boolean pressedMiddleSinceLastFrame = false;
   private boolean releasedMiddleSinceLastFrame = false;
   
   
   
   /** 
    * Indicates which direction the mouse wheel was moved since the last frame.
    * 0 indicates no wheel movement. -1 means the wheel was moved up. 1 means the wheel was moved down.
    */
   public int wheel = 0;
    
   private boolean wheelDownSinceLastFrame = false;
   private boolean wheelUpSinceLastFrame = false;
    
    
    
    /** Creates the Mouse object and assigns it a Component to listen for events for. */
   public Mouse(Component publisher) {
      this.publisher = publisher;
      publisher.addMouseListener(this);
      publisher.addMouseMotionListener(this);
      publisher.addMouseWheelListener(this);
   }
    
    
    /** Updatees the state of the Mouse based on the input data it processed from events since the last frame. */
   public void poll() {
      // Any button
      justAnyPressed = false;
      justAnyClicked = false;
      
      if(pressedAnySinceLastFrame) {
         justAnyPressed = true;
         isAnyPressed = true;
      }
      
      if(releasedAnySinceLastFrame) {
         justAnyClicked = true;
         isAnyPressed = false;
      }
      
      // Left button
		justLeftPressed = false;
		justLeftClicked = false;
		
		if(pressedLeftSinceLastFrame) {
			justLeftPressed = true;
			isLeftPressed = true;
		}
		
		if(releasedLeftSinceLastFrame) {
			justLeftClicked = true;
			isLeftPressed = false;
		}
		
		// Middle button
		justMiddlePressed = false;
		justMiddleClicked = false;
		
		if(pressedMiddleSinceLastFrame) {
			justMiddlePressed = true;
			isMiddlePressed = true;
		}
		
		if(releasedMiddleSinceLastFrame) {
			justMiddleClicked = true;
			isMiddlePressed = false;
		}
		
		// Right button
		justRightPressed = false;
		justRightClicked = false;
		
		if(pressedRightSinceLastFrame) {
			justRightPressed = true;
			isRightPressed = true;
		}
		
		if(releasedRightSinceLastFrame) {
			justRightClicked = true;
			isRightPressed = false;
		}
		
		// Wheel
		wheel = 0;
		if(wheelDownSinceLastFrame) wheel = 1;
		if(wheelUpSinceLastFrame) wheel = -1;
		
		// reset all SinceLastFrame flags
		pressedAnySinceLastFrame = false;
		releasedAnySinceLastFrame = false;
		
		pressedLeftSinceLastFrame = false;
		releasedLeftSinceLastFrame = false;
		
		pressedMiddleSinceLastFrame = false;
		releasedMiddleSinceLastFrame = false;
		
		pressedRightSinceLastFrame = false;
		releasedRightSinceLastFrame = false;
		
		wheelDownSinceLastFrame = false;
		wheelUpSinceLastFrame = false;
   }
   
   
   
   
   // event handlers
   
   public void mouseClicked(MouseEvent e) {
      // Do nothing
   }
   
   public void mouseEntered(MouseEvent e) {
      // Do nothing
   }
   
   public void mouseExited(MouseEvent e) {
      // Do nothing
   }
   
   public void mousePressed(MouseEvent e) {
      int button = e.getButton();
      pressedAnySinceLastFrame = true;
      
      if(button == MouseEvent.BUTTON1)
         pressedLeftSinceLastFrame = true;
      if(button == MouseEvent.BUTTON2)
         pressedMiddleSinceLastFrame = true;
      if(button == MouseEvent.BUTTON3)
         pressedRightSinceLastFrame = true;
   }
   
   public void mouseReleased(MouseEvent e) {
      int button = e.getButton();
      releasedAnySinceLastFrame = true;
      
      if(button == MouseEvent.BUTTON1)
         releasedLeftSinceLastFrame = true;
      if(button == MouseEvent.BUTTON2)
         releasedMiddleSinceLastFrame = true;
      if(button == MouseEvent.BUTTON3)
         releasedRightSinceLastFrame = true;
   }
   
   public void mouseDragged(MouseEvent e) {
      // get the mouse's position in the component this Mouse object is observing events for.
      x = e.getX();
      y = e.getY();
      position = new Point(x,y);
      
      // get the mouse's absolute position on your computer's display
      sysX = e.getXOnScreen();
      sysY = e.getYOnScreen();
      sysPosition = new Point(sysX, sysY);
   }
   
   public void mouseMoved(MouseEvent e) {
      // get the mouse's position in the component this Mouse object is observing events for.
      x = e.getX();
      y = e.getY();
      position = new Point(x,y);
      
      // get the mouse's absolute position on your computer's display
      sysX = e.getXOnScreen();
      sysY = e.getYOnScreen();
      sysPosition = new Point(sysX, sysY);
   }
   
   public void mouseWheelMoved(MouseWheelEvent e) {
      int rotation = e.getWheelRotation();
      if(rotation < 0) wheelUpSinceLastFrame = true;
      if(rotation > 0) wheelDownSinceLastFrame = true;
   }
   
   
   
   
}








