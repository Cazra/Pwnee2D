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
import pwnee.GameMath;

/** Displays an infinitely wrapping image with a camera that can be moved, zoomed, and rotated. */
public class WrappingImageSprite extends BlitterSprite {
   
   /** The rotation of the blittered image in degrees */
	public double cameraAngle = 0;
   
   /** The source image x coordinate of the Sprite's center. */
	public double cameraX = 0;
   
   /** The source image y coordinate of the Sprite's center. */
   public double cameraY = 0;
   
   /** The zoom scale of the blittered image. */
	public double cameraZoom = 1.0;
	
   // private values for the sine and cosine of cameraAngle. This will be reused for all pixels during a rendering iteration.
   private double camCos = 1;
   private double camSin = 0;
   
	// CONSTRUCTOR
	
	public WrappingImageSprite(Image srcImg, double x, double y, double w, double h) {
		super(srcImg, x,y,w,h);
	}
	
	// RENDERING METHODS
	
	/** Computes and stores cameraAngle's sine and cosine. */
   public void computeReusedValues() {
		camCos = GameMath.cos(cameraAngle);
		camSin = GameMath.sin(cameraAngle);
   }
   
   /** Performs a camera transform with the camera's focal coordinates at the result image's center. */
	public int[] transformPixel(double i,double j) {
		i-=width/2;
		j-=height/2;
		Point2D rotated = new Point((int)(i*camCos - j*camSin), (int)(i*camSin + j*camCos));
      int[] result = {(int)((rotated.getX()*cameraZoom + cameraX)), (int)((rotated.getY()*cameraZoom + cameraY))};
		return result;
	}
	
}


