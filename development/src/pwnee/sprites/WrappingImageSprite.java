package pwnee.sprites;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import pwnee.GameMath;

/** Displays an infinitely wrapping image with a camera that can be moved, zoomed, and rotated. */
public abstract class WrappingImageSprite extends BlitterSprite {
   
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


