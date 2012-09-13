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

/**
*	Camera.java
*	author: Stephen Lindberg
*	Last modified: 10/04/2011
*	
*	A basic camera object that produces a camera AffineTransformation from 
*	its properties.
**/

public class Camera
{
	private double parentCX, parentCY; // screen coordinates of the parent screen's center
	private double centerX, centerY; // world coordinates of the camera's center
	private double scale;	
	private double rotate;
	
	AffineTransform trans;	// the camera's transform
	
	/**
	*	Constructor(double pw, double ph)
	*	Constructs a camera from the parent screen's dimensions.
	*	Preconditions: pw and ph are the width and height of the parent screen.
	*	Postconditions: the initial camera is created with its center in the center of the parent screen.
	**/
	
	public Camera(double pw, double ph)
	{
		this(pw,ph,pw,ph);
	}
	
	/**
	*	Constructor(double x, double y, double pw, double ph)
	*	Construct's a camera from the parent screen's dimensions centered on a given point.
	*	Preconditions: x and y are the coordinates of the Camera's center. 
	*		pw and ph are the width and height of the parent screen.
	*	Postconditions: the initial camera is created with its center at x,y. 
	**/
	
	public Camera(double x, double y, double pw, double ph)
	{
		centerX = x;
		centerY = y;
		parentCX = pw/2;
		parentCY = ph/2;
		
		scale = 1.0;
		rotate = 0;
		
		updateTransform();
	}
	
	
	// get() methods
	
	public double getX()
	{
		return centerX;
	}
	
	public double getY()
	{
		return centerY;
	}
	
	public double getScale()
	{
		return scale;
	}
	
	public double getRotate()
	{
		return rotate*360/(Math.PI*2); // convert to degrees
	}

	
	/**
	*	moveTo(double x, double y)
	*	Moves the camera.
	*	Preconditions: x and y are the world coordinates we want to move the camera's center to.
	*	Postconditions: the camera's center is moved to (x,y).
	**/
	
	public void moveTo(double x, double y)
	{
		centerX = x;
		centerY = y;
		updateTransform();
	}
	
	/**
	*	scale(double s)
	*	Zooms the camera in or out.
	*	Preconditions: s is the scaling factor for zooming the camera. s = 1.0 is unscaled. 
	*		s < 1.0 is zoomed out. s > 1.0 is zoomed in.
	*	Postconditions: The camera's zoom is changed.
	**/
	
	public void scale(double s)
	{
		scale = s;
		updateTransform();
	}
	
	/**
	*	rotate(double r)
	*	Rotates the camera.
	*	Preconditions: r is the angle we wand the camera's x axis to be oriented at relative to the world's x-axis. r is in degrees.
	*	Postconditions: The camera is rotated by r degrees.
	**/
	
	public void rotate(double r)
	{
		rotate = r*(Math.PI*2) / 360; // convert to radians
		updateTransform();
	}
	
	/**
	*	updateTransform()
	*	Helper method updates the camera's AffineTransform
	*	Preconditions: none
	*	Postconditions: the camera's AffineTransform is changed to reflect its current position, scale, and rotation.
	**/
	
	private void updateTransform()
	{
		trans = new AffineTransform(); // identity
		trans.translate(parentCX,parentCY); // move origin to center of screen
		trans.scale(scale,scale); // perform scale and rotate around new origin.
		trans.rotate(rotate);
		trans.translate(0-centerX, 0- centerY); // move the camera
	}
	
	/**
	*	getTransform()
	*	Produces a copy of the camera's current AffineTransform
	*	Preconditions: none.
	*	Postconditions: returns a copy of the camera's current AffineTransform.
	**/
	
	public AffineTransform getTransform()
	{
		return new AffineTransform(trans);
	}
	
}

