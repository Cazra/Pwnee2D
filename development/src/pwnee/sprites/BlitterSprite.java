package pwnee.sprites;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

/** A rectangular sprite that displays a transformed image. The pixels of the Sprite's rendered image are each mapped to a pixel of a source image by using a transformation method on its coordinates. (or possibly multiple source images.) This can be used to achieve many special effects such as an infinitely wrapping background image, or the Mode 7 fake 3D effect used in many Super Nintendo games. */
public abstract class BlitterSprite extends Sprite {

	/** A pixel array used to create the resulting image for this Sprite. */
   public int[] pixels = null;
   
   /** This will get set to the source image's width. */
	public int pixWidth = -1;
   
   /** This will get set to the source image's height. */
	public int pixHeight = -1;
   
   /** The current source image for the BlitterSprite*/
	public Image srcImg = null;
   
   /** Flag for wrapping over the source image's pixels on its X axis. If this is false and we go out of bounds, the color for our pixel will be transparent. */
   public boolean srcWrapX = true;
   
   /** Flag for wrapping over the source image's pixels on its Y axis. If this is false and we go out of bounds, the color for our pixel will be transparent. */
   public boolean srcWrapY = true;
      
	/** 
    * Creates the BlitterSprite. 
    * @param srcImg  The source image for the Sprite.
    * @param x    The world x coordinate of the Sprite.
    * @param y    The world y coordinate of the Sprite.
    * @param w    The desired width of the Sprite's result image.
    * @param h    The desired height of the Sprite's result image.
    */
	public BlitterSprite(Image srcImg, double x, double y, double w, double h) {
		super(x,y);
		
		width = w;
		height = h;
		
		pixWidth = -1;
		pixHeight = -1;
      
      this.srcImg = srcImg;
	}
	
	// RENDERING METHODS
	
	/** 
    * Creates and draws the blittered image of the Sprite using a method for mapping rendering coordinates to source image pixel coordinates. 
    * The resulting image will always be bound in a rectangular area defined by the Sprite's width and height. You may want to override this methode to do more achieve 
    * more complicated effects, like make a blittered image from multiple source images.
    * @param g    The transformed graphics context.
    */
	public void draw(Graphics2D g) {
		
		// obtain the image raster that we will write the pixels of our resulting image to
		BufferedImage img = new BufferedImage((int) width,(int) height,BufferedImage.TYPE_INT_ARGB);
		int[] writePixels = ((DataBufferInt)(img.getRaster().getDataBuffer())).getData(); 
		
		// obtain the source image's pixels and its width/height
		grabPixels();
		
		// compute any values that will be reused for each pixel.
		computeReusedValues();

		// process each pixel in the sprite's viewport.
		for(int j = 0; j < height; j++) {
			for(int i = 0; i < width; i++) {
            
            // Transform this pixel's coordinates to source pixel coordinates using our custom transform method.
				int[] pix = transformPixel(i,j);
            int pixX = pix[0];
            int pixY = pix[1];
				
            // default color is transparent black if we map to an invalid pixel.
            int pixelColor = 0x00000000;
            
            // apply image wrapping if our flags are true.
            if(srcWrapX) {
               pixX = pixX % pixWidth;
               if(pixX < 0)
                  pixX = pixWidth+pixX;
            }
            if(srcWrapY) {
               pixY = pixY % pixHeight;
               if(pixY < 0)
                  pixY = pixHeight+pixY;
            }
            
            // get our pixel if it is within our source image's bounds.
				if(pixX >= 0 && pixX < pixWidth && pixY >= 0 && pixY < pixHeight) 
               pixelColor =  pixels[pixY*pixWidth + pixX];
				
            // write the pixel to our result image's pixel array.
				try {
					writePixels[(int)width*j+i] = pixelColor;
				}
				catch(Exception ex) {
               System.err.println("BlitterSprite - error writing pixels to result image");
					ex.printStackTrace();
				}
			}
		}
		
		// draw the resulting image onto the scene.
		g.drawImage(img, null, null);
	}
	
   /** Used to compute any values that will be the same for all the pixels during the blittering. These values should be stored as member variables of this Sprite. */
   public abstract void computeReusedValues();
   
	/** Converts from rendering coordinates to source image pixel coordinates*/
	public abstract int[] transformPixel(double i,double j);
   
   /** Loads the pixels from the source image into our pixels array. Also loads the width and height of the image into pixWidth and pixHeight. */
   protected void grabPixels() {
      // obtain the source image's pixels and its width/height
		if(pixels == null) {
			PixelGrabber pg = new PixelGrabber(this.srcImg, 0,0, -1, -1, false);
			try {
				pg.grabPixels();
			}
			catch(Exception ex) {
				System.out.println(ex.getMessage());
				ex.printStackTrace();
			}
			
			pixWidth = pg.getWidth();
			pixHeight = pg.getHeight();
			
			pixels = (int[]) pg.getPixels();
		}
   }
}

