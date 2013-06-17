package pwnee.sprites;

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
import java.awt.image.*;
import java.util.ArrayList;

import pwnee.GameMath;
import pwnee.geom.*;

/** 
 * An abstract class used to represent some sort of renderable object. 
 * It has a position, dimensions, scale and rotate transforms, opacity, 
 * and some other neat and flexible features. 
 */
public abstract class Sprite {

	/** World X coordinate of the Sprite. */
	public double x = 0;
   
   /** World Y coordinate of the Sprite. */
   public double y = 0;	
	
	/** The central X coordinate of the Sprite relative to its image. */
	public double focalX = 0;
   
   /** The central Y coordinate of the Sprite relative to its image. */
   public double focalY = 0; 
	
	/** The width of the Sprite's bounding box. Optional, but useful for collisions. */
	public double width = 1;
   
   /** The height of the Sprite's bounding box. Optional, but useful for collisions. */
   public double height = 1;
	
	/** Controls the scale transform of the Sprite's image on its x-axis. */
	public double scaleX = 1;
   
   /** Controls the scale transform of the Sprite's image on its y-axis. */
   public double scaleY = 1;
   
   /** Controls the uniform transform of the Sprite's image. This multiplies both its scaleX and scaleY. */
   public double scaleUni = 1;
   
   /** Used for rotational transformations. This is the angle in degrees of this Sprite's positive x axis to the world's positive x axis. */
	public double angle = 0;
   
   /** used to store the Sprite's current rotate-scale transform */
	public AffineTransform transform = new AffineTransform(); 
   
   /** The complete affine transform used to render this sprite on the last frame. */
   public AffineTransform curTrans = new AffineTransform();
   
   /** 
	 * This becomes true whenever any of the methods for manipulating 
	 * this Sprite's rotation or scale are called. It lets the render method know to update 
	 * its rotate-scale transform before drawing.
	 */
	public boolean transformChanged = false; 
   
   /** A flag to let everyone know that this Sprite is destroyed and scheduled to be discarded. */
	public boolean isDestroyed = false;
   
   /** If this is false, then the Sprite won't be rendered. */
	public boolean isVisible = true;
	
	/** 
   * Controls how opaque or transparent this Sprite is. The value for this 
   * should be in the range [0.0, 1.0], where 0.0 is completely transparent 
   * and 1.0 is completely opaque. 
   */
   public double opacity = 1.0;
	
   /** A convenient array of doubles that can be used for whatever. */
	public double[] vars = new double[10];
   
   
	
	
	// CONSTRUCTORS
	
   /** Default constructor creates the Sprite at world coordinates (0,0). */
	public Sprite() {
		this(0,0);
	}
	
   /** Creates the Sprite at world coordinates (x,y). */
	public Sprite(double x, double y) {
		this.x = x;
		this.y = y;
      
      for(int i = 0; i < 10; i++) {
         vars[i] = 0.0;
      }
	}
	
	/** 
   * Marks this Sprite as destroyed. You may also want to override this method 
   * to do additional processing when this Sprite is destroyed.
   */
	public void destroy() {
		this.isDestroyed = true;
	}
	
	
	// RENDERING METHODS
	
	/**
	 * Updates the transforms for drawing the Sprite if needed and then calls 
   * the Sprite's draw method. 
	 * @param g		The graphics context this is being rendered on.
	 */
	public void render(Graphics2D g) {
		if(isDestroyed || opacity == 0 || !isVisible)
			return;
		
      // update our image transform so that it's ready for rendering
		if(transformChanged) 
			updateTransform();
		
      // save the Graphics context's original transform and composite.
		AffineTransform oldTrans = g.getTransform(); 
		Composite oldComp = g.getComposite();
		
      // Use an AlphaComposite to apply semi-transparency to the Sprite's image.
		if(opacity < 1.0)
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) opacity));
		
      // Apply the Sprite's transform to our Graphics context.
		AffineTransform curTrans = g.getTransform();
		curTrans.translate(this.x,this.y);
		curTrans.concatenate(this.transform);
		curTrans.translate(0-focalX,0-focalY);
		g.setTransform(curTrans);
		
      // The draw method shall be defined by the user.
		this.draw(g);
		
      // Restore the Graphics context's original transform and composite.
		g.setTransform(oldTrans);
		g.setComposite(oldComp);
        
    this.curTrans = curTrans;
	}
	
	/**	
	 * Called by the render method to draw the image for the Sprite. 
    * The user is expected to define this to do their own custom drawing.
	 * @param g		The Graphics context this is being rendered with.
	 **/
   public abstract void draw(Graphics2D g);
   
	
	// COLOR TRANSFORM METHODS
	
	/**
	 *	Safely sets the opacity so that it is always in the range [0.0, 1.0].
    * @param alpha   The desired opacity.
	 */
	public void setOpacity(double alpha) {
		opacity = Math.min(1.0, Math.max(0.0, alpha));
	}
   
	
	// IMAGE TRANSFORM METHODS
	
	/** Sets the x and y scale components of this Sprite's transform. */
	public void scale(double x, double y) {
		this.scaleX = x;
		this.scaleY = y;
		this.transformChanged = true;
	}
	
   /** Sets the rotation component of this Sprite's transform. */
	public void rotate(double degrees) {
		this.angle = degrees;
		this.transformChanged = true;
	}
	
	/** 	Helper method updates the rotate-scale transform matrix for this Sprite. */
	public void updateTransform() {
      // Start with an ID transform
		transform = new AffineTransform();
		
		transform.rotate(0-GameMath.d2r(angle));
		transform.scale(scaleX*scaleUni,scaleY*scaleUni);
	}
	
	
	//////////////////// COLLISION METHODS
	
  
  /** 
   * Returns the dimensions of the sprite's bounding box. 
   * Override this to suit your sprite's needs.
   */
  public Dimension2D getDimensions() {
    return new DimensionDouble( width*scaleX, height*scaleY);
  }  
  
	
	/** 
   * Returns the bounding box of the Sprite as a Rectangle2D object. 
   * Override this to suit your sprite's needs.
   */
	public Rectangle2D getCollisionBox() {	
		return new Rectangle2D.Double(x-focalX*scaleX, y-focalY*scaleY, width*scaleX, height*scaleY);
	}
   
   /**
   * Tests if this sprite is colliding with some other sprite. 
   * The user is expected to override this method in most cases.
   * A default definition is provided here which does a non-rotated bounding box collision test between the sprites.
   * @param other     The sprite we are checking for a collision with this sprite.
   * @return          true if this and other are colliding. Otherwise false.
   */
   public boolean isOverlapping(Sprite other) {
      Rectangle2D tBox = this.getCollisionBox();
      Rectangle2D oBox = other.getCollisionBox();
      
      if(tBox.getX() > oBox.getX() + oBox.getWidth()) return false;
      if(tBox.getX() + tBox.getWidth() < oBox.getX()) return false;
      if(tBox.getY() > oBox.getY() + oBox.getHeight()) return false;
      if(tBox.getY() + tBox.getHeight() < oBox.getY()) return false;
      return true;
   }

}

