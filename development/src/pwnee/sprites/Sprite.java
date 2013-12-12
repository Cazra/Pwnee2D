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

	/** X position of the sprite in model coordinates. */
	public double x = 0;
   
   /** Y position of the sprite in model coordinates. */
  public double y = 0;	
	
	/** The offset of the sprite's X position relative to its image. */
	protected double focalX = 0;
   
  /** The offset of the sprite's Y position relative to its image. */
  protected double focalY = 0; 
	
	/** The width of the Sprite's bounding box. Optional, but useful for collisions. */
	protected double width = 1;
   
  /** The height of the Sprite's bounding box. Optional, but useful for collisions. */
  protected double height = 1;
	
	/** Controls the scale transform of the Sprite's image on its x-axis. */
	protected double scaleX = 1;
   
  /** Controls the scale transform of the Sprite's image on its y-axis. */
  protected double scaleY = 1;
 
  /** 
   * Controls the uniform transform of the Sprite's image. 
   * This multiplies both its scaleX and scaleY. 
   */
  protected double scaleUni = 1;
 
  /** 
   * The rotational angle for the sprite in degrees. 
   * Given the vector z coming out of the screen, this is the angle of the 
   * sprite around z going counter-clockwise from the positive x axis.
   */
	protected double angle = 0;
   
  /** The model-view transform of this sprite the last time it was rendered. */
  protected AffineTransform transform = new AffineTransform();
   
  /** A flag to let everyone know that this Sprite is destroyed and scheduled to be discarded. */
	protected boolean isDestroyed = false;
   
  /** If this is false, then the Sprite won't be rendered. */
	protected boolean isVisible = true;
	
	/** 
   * Controls how opaque or transparent this Sprite is. The value for this 
   * should be in the range [0.0, 1.0], where 0.0 is completely transparent 
   * and 1.0 is completely opaque. 
   */
  protected float opacity = 1.0f;
	
	// CONSTRUCTORS
	
   /** Default constructor creates the Sprite at world coordinates (0,0). */
	public Sprite() {
		this(0,0);
	}
	
   /** Creates the Sprite at world coordinates (x,y). */
	public Sprite(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/** 
   * Marks this Sprite as "destroyed". The sprite will no longer be rendered
   * and will return false for all collision detections, but it will not remove
   * itself from any sprite data structures containing it. That is up to the user.
   *
   * Override this method if you want
   * to do additional clean-up processing when this Sprite is destroyed. 
   */
	public void destroy() {
		this.isDestroyed = true;
	}
	
  /** Returns true iff this sprite is currently flagged as "destroyed". */
  public boolean isDestroyed() {
    return isDestroyed;
  }
  
  // LOCATION/GEOMETRY
  
  /** Returns the X position of the sprite in model coordinates. */
  public double getX() {
    return x;
  }
  
  /** Returns the Y position of the sprite in model coordinates. */
  public double getY() {
    return y;
  }
  
  
  /** Returns the point that defines the sprite's location in model coordinates. */
  public Point2D getPosition() {
    return new Point2D.Double(x, y);
  }
  
  /** Returns the point that defines the sprite's location in view coordinates*/
  public Point2D getViewPosition() {
    return transform.transform(new Point2D.Double(0, 0), null);
  }
  
  /** 
   * Returns the focal point that defines the offset of the sprite's 
   * actual location relative to its image.
   */
  public Point2D getFocalPoint() {
    return new Point2D.Double(focalX, focalY);
  }
  
  /** Sets the focal point for the sprite. */
  public void setFocalPoint(double x, double y) {
    focalX = x;
    focalY = y;
  }
  
  
  /** 
   * Returns the dimensions of the sprite's bounding box.
   */
  public Dimension2D getDimensions() {
    Rectangle2D bounds = getCollisionBox();
    return new DimensionDouble( bounds.getWidth(), bounds.getHeight());
  }
  
  /** Returns the width of the sprite's bounding box. */
  public double getWidth() {
    return getCollisionBox().getWidth();
  }
  
  /** Returns the height of the sprite's bounding box. */
  public double getHeight() {
    return getCollisionBox().getHeight();
  }
  
  
  /** Sets the unscaled width of the sprite's collision box. */
  public void setWidth(double width) {
    this.width = width;
  }
  
  /** Sets the unscaled height of the sprite's collision box. */
  public void setHeight(double height) {
    this.height = height;
  }
  
  /** Sets the unscaled dimensions of the sprite's bounding box. */
  public void setDimensions(double width, double height) {
    this.width = width;
    this.height = height;
  }
  
	
	// RENDERING METHODS
	
	/**
	 * Updates the transforms for drawing the Sprite if needed and then calls 
   * the Sprite's draw method. 
	 * @param g		The graphics context this is being rendered on.
	 */
	public void render(Graphics2D g) {
    
    // Update our object's model-view transform. 
    this.transform = createTransform(g.getTransform());
		if(isDestroyed || opacity == 0 || !isVisible) {
			return;
    }
		
    // Save the original graphics state.
		AffineTransform oldTrans = g.getTransform(); 
		Composite oldComp = g.getComposite();
		
    // Use an AlphaComposite to apply semi-transparency to the Sprite's image.
		if(opacity < 1.0) {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
		}
    
    // Apply the Sprite's transform to our Graphics context.
		g.setTransform(this.transform);
		
    // Actual drawing is delegated to the draw() method defined by concrete Sprites.
		this.draw(g);
		
    // Restore the original graphics state.
		g.setTransform(oldTrans);
		g.setComposite(oldComp);
	}
  
  /** 
   * Creates the model-view transform for the sprite. 
   * The order here is very important.
   */
  private AffineTransform createTransform(AffineTransform baseTransform) {
    AffineTransform transform = baseTransform;
		transform.translate(this.x, this.y);
    transform.rotate(0-GameMath.d2r(angle));
    transform.scale(scaleX*scaleUni, scaleY*scaleUni);
		transform.translate(0-focalX, 0-focalY);
    return transform;
  }
	
	/**	
	 * Called by the render method to draw the current image for the Sprite. 
	 * @param g		The Graphics context this is being rendered with.
	 **/
  public abstract void draw(Graphics2D g);
   
	
	// COLOR TRANSFORM METHODS/VISIBILITY
	
	/**
	 * Sets the sprite's opacity, clamped to the range [0.0, 1.0].
   * A sprite with opacity 0.0 is completely transparent, and a sprite with 
   * opacity 1.0 is completely opaque.
   * @param opacity   The desired opacity.
	 */
	public void setOpacity(double opacity) {
		opacity = (float) Math.min(1.0, Math.max(0.0, opacity));
	}
  
  /** 
   * Returns the sprite's current opacity in the range [0.0, 1.0). 
   * A sprite with opacity 0.0 is completely transparent, and a sprite with 
   * opacity 1.0 is completely opaque.
   */
	public float getOpacity() {
    return opacity;
  }
  
  
  /** Set whether this sprite is visible. */
  public void setVisible(boolean visible) {
    this.isVisible = visible;
  }
  
  
  /** Returns true iff this sprite is set as visible. */
  public boolean isVisible() {
    return isVisible;
  }
  
	// IMAGE TRANSFORM METHODS
	
	/** Sets the sprite's x and y scales. */
	public void setScale(double x, double y) {
		this.scaleX = x;
		this.scaleY = y;
	}
  
  /** Gets the scale of the sprite along its X axis. */
  public double getScaleX() {
    return scaleX;
  }
  
  /** Gets the scale of the sprite along its Y axis. */
  public double getScaleY() {
    return scaleY;
  }
  
  /** Sets the sprite's uniform scale. */
  public void setScale(double u) {
    this.scaleUni = u;
  }
  
  /** Gets the uniform scale of the sprite. */
  public double getScale() {
    return scaleUni;
  }
	
  /** Sets the sprite's rotational angle in degrees and wraps it within the range [0.0, 360). */
	public void setAngle(double degrees) {
    this.angle = degrees % 360;
    if(this.angle < 0) {
      this.angle += 360;
    }
	}
  
  /** Sets the sprite's rotational angle in radians and wraps it within the range [0.0, 2*PI). */
  public void setAngleR(double radians) {
    setAngle(GameMath.r2d(radians));
  }
  
  /** Gets the rotational angle of the sprite in degrees. */
  public double getAngle() {
    return angle;
  }
  
  /** Gets the rotational angle of the sprite in radians. */
  public double getAngleR() {
    return GameMath.d2r(angle);
  }
	
  /** Returns a copy of the model-view transform for this sprite. */
  public AffineTransform getTransform() {
    return new AffineTransform(transform);
  }
	
	
	//////////////////// COLLISION METHODS
	
  
	/** 
   * Returns the unrotated bounding box of the Sprite as a Rectangle2D object. 
   * Override this to suit your sprite's needs.
   */
	public Rectangle2D getCollisionBox() {	
		return new Rectangle2D.Double(x-focalX*scaleX, y-focalY*scaleY, width*scaleX, height*scaleY);
	}
  
  
  /** 
   * Returns the sprite's bounding convex polygon used for Separating Axis Theorem collision tests. 
   * The default implementation returns the sprite's transformed bounding box (including rotation!). 
   * Override this to suit your sprite's needs.
   */
  public Polygon2D getCollisionPoly() {
    double scaleX = this.scaleX*scaleUni;
    double scaleY = this.scaleY*scaleUni;
    
    double left = -focalX*scaleX;
    double top = -focalY*scaleY;
    double right = left + width*scaleX;
    double bottom = top + height*scaleY;
    
    // Define the rectangle's points in clockwise order (in game coordinates, y axis is down).
    // These points are in object coordinates.
    Point2D[] p = new Point2D[4];
    p[0] = new Point2D.Double(left, top);
    p[1] = new Point2D.Double(right, top);
    p[2] = new Point2D.Double(right, bottom);
    p[3] = new Point2D.Double(left, bottom);
    
    double[] x = new double[4];
    double[] y = new double[4];
    
    // Rotate, then translate each point to get them in model coordinates.
    for(int i = 0; i < 4; i++) {
      AffineTransform transform = AffineTransform.getTranslateInstance(this.x, this.y);
      transform.rotate(0-GameMath.d2r(angle));
      
      p[i] = transform.transform(p[i], null);
      x[i] = p[i].getX();
      y[i] = p[i].getY();
    }
    
    // Use our model coordinates to produce the collision polygon.
    return new Polygon2D(x, y);
  } 
   
  /**
   * Tests if this sprite is colliding with some other sprite. 
   * The user is expected to override this method in most cases.
   * A default definition is provided here which does a non-rotated bounding 
   * box collision test between the sprites.
   * @param other     The sprite we are checking for a collision with this sprite.
   * @return          true if this and other are colliding. Otherwise false.
   */
  public boolean isOverlapping(Sprite other) {
    if(this.isDestroyed() || other.isDestroyed()) {
      return false;
    }
    else {
      Rectangle2D tBox = this.getCollisionBox();
      Rectangle2D oBox = other.getCollisionBox();
      
      if(tBox.getX() > oBox.getX() + oBox.getWidth()) return false;
      if(tBox.getX() + tBox.getWidth() < oBox.getX()) return false;
      if(tBox.getY() > oBox.getY() + oBox.getHeight()) return false;
      if(tBox.getY() + tBox.getHeight() < oBox.getY()) return false;
      return true;
    }
  }
  
  
  //////// Anonymous properties
  
  /** 
   * This method can be overridden to provide access to numerical  
   * properties of an anonymous instance of some Sprite. 
   * This is convenient for game objects that use the same Sprite class, but
   * may have slightly different properties, such as generic NPCs in an RPG.
   * The empty implementation here just returns 0.
   */
  public double getNumericalProperty(int propertyCode) {
    return 0;
  }
  
  /** 
   * Sets the value for some numerical property of an anonymous instance. 
   * See getNumericalProperty(int). 
   * The default implementation is empty.
   */
  public void setNumericalProperty(int propertyCode, double value) {
  }
  
  /** 
   * This method can be overridden to provide access to numerical  
   * properties of an anonymous instance of some Sprite. 
   * This is convenient for game objects that use the same Sprite class, but
   * may have slightly different properties, such as generic NPCs in an RPG.
   * The empty implementation here just returns 0.
   */
  public double getNumericalProperty(String propertyName) {
    return 0;
  }
  
  /** 
   * Sets the value for some numerical property of an anonymous instance. 
   * See getNumericalProperty(String). 
   * The default implementation is empty.
   */
  public void setNumericalProperty(String propertyName, double value) {
  }
  
  
  /** 
   * This method can be overridden to provide access to boolean  
   * properties of an anonymous instance of some Sprite. 
   * This is convenient for game objects that use the same Sprite class, but
   * may have slightly different properties, such as generic NPCs in an RPG.
   * The empty implementation here just returns false.
   */
  public boolean getBooleanProperty(int propertyCode) {
    return false;
  }
  
  /** 
   * Sets the value for some boolean property of an anonymous instance. 
   * See getBooleanProperty(int). 
   * The default implementation is empty.
   */
  public void setBooleanProperty(int propertyCode, boolean value) {
  }
  
  /** 
   * This method can be overridden to provide access to boolean  
   * properties of an anonymous instance of some Sprite. 
   * This is convenient for game objects that use the same Sprite class, but
   * may have slightly different properties, such as generic NPCs in an RPG.
   * The empty implementation here just returns false.
   */
  public boolean getBooleanProperty(String propertyName) {
    return false;
  }
  
  /** 
   * Sets the value for some boolean property of an anonymous instance. 
   * See getBooleanProperty(String). 
   * The default implementation is empty.
   */
  public void setBooleanProperty(String propertyName, boolean value) {
  }
  
  
  /** 
   * This method can be overridden to provide access to String  
   * properties of an anonymous instance of some Sprite. 
   * This is convenient for game objects that use the same Sprite class, but
   * may have slightly different properties, such as generic NPCs in an RPG.
   * The empty implementation here just returns "".
   */
  public String getStringProperty(int propertyCode) {
    return "";
  }
  
  /** 
   * Sets the value for some String property of an anonymous instance. 
   * See getStringProperty(int). 
   * The default implementation is empty.
   */
  public void setStringProperty(int propertyCode, String value) {
  }
  
  /** 
   * This method can be overridden to provide access to String  
   * properties of an anonymous instance of some Sprite. 
   * This is convenient for game objects that use the same Sprite class, but
   * may have slightly different properties, such as generic NPCs in an RPG.
   * The empty implementation here just returns "".
   */
  public String getStringProperty(String flagName) {
    return "";
  }
  
  /** 
   * Sets the value for some String property of an anonymous instance. 
   * See getStringProperty(String). 
   * The default implementation is empty.
   */
  public void setStringProperty(String propertyName, String value) {
  }
}

