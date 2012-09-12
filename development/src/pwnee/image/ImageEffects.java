package pwnee.image;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;


/** A class with a bunch of static methods for applying color filters on images. */
public class ImageEffects {
	
	/** Produces an image with one color set to be completely transparent. */
	public static Image setTransparentColor(Image srcImg, Color transColor) {	
		ImageFilter filter = new RGBImageFilter() {
			public int transHex = transColor.getRGB() | 0xFF000000;
			
			public int  filterRGB(int x, int y, int rgb) {
            // if the current pixel's opaque color matches transColor, then make this pixel completely transparent.
				if((rgb | 0xFF000000 ) == transHex)
					return rgb & 0x00FFFFFF;
				else
					return rgb;
			}
		};
		
		ImageProducer ip = new FilteredImageSource(srcImg.getSource(),filter);
		Image result = Toolkit.getDefaultToolkit().createImage(ip);
		
		return result;
	}
	
   /** Makes all the pixels in the resulting image opaque. */
   public static Image makeOpaque(Image srcImg) {	
		ImageFilter filter = new RGBImageFilter() {
			public int  filterRGB(int x, int y, int rgb) {
					return rgb | 0xFF000000;
			}
		};
		
		ImageProducer ip = new FilteredImageSource(srcImg.getSource(),filter);
		Image result = Toolkit.getDefaultToolkit().createImage(ip);
		
		return result;
	}
   
   /** 
    * Produces an image with a uniform opacity applied to all pixels that aren't completely transparent. 
    * @param srcImg     the source image
    * @param alpha      the value that will be assigned to the pixels' alpha components in the range [0,255].
    */
   public static Image setOpacity(Image srcImg, int alpha) {	
      // normalize alpha to be in the range [0,255]
      alpha = Math.min(255, Math.max(0, alpha));
      
		ImageFilter filter = new RGBImageFilter() {
			public int  filterRGB(int x, int y, int rgb) {
            if((rgb & 0xFF000000) != 0)
               return rgb;
            else
               return (rgb & 0x00FFFFFF) | ((alpha << 24) & 0xFF000000);
			}
		};
		
		ImageProducer ip = new FilteredImageSource(srcImg.getSource(),filter);
		Image result = Toolkit.getDefaultToolkit().createImage(ip);
		
		return result;
	}
   
   /** 
    * Produces an image with a uniform opacity applied to all pixels that aren't completely transparent. 
    * @param srcImg     the source image
    * @param alpha      the value that will be assigned to the pixels' alpha components in the range [0.0, 1.0].
    */
   public static Image setOpacity(Image srcImg, double alpha) {	
      return setOpacity(srcImg, (int)(alpha*255));
   }
   
   
   
   
	/** Produces an image with inverted RGB values. */
	public static Image invert(Image srcImg) {		
		ImageFilter filter = new RGBImageFilter() {
			public int  filterRGB(int x, int y, int rgb) {
				return (rgb & 0xFF000000) | ((~rgb) & 0x00FFFFFF);
			}
		};
		
		ImageProducer ip = new FilteredImageSource(srcImg.getSource(),filter);
		Image result = Toolkit.getDefaultToolkit().createImage(ip);
		
		return result;
	}
	
	
	/** Produces an image whose rgb values are incremented by an additive color. */
	public static Image addColor(Image srcImg, Color additive) {	
		final int r = additive.getRed();
		final int g = additive.getGreen();
		final int b = additive.getBlue();
			
		ImageFilter filter = new RGBImageFilter() {
			public int  filterRGB(int x, int y, int rgb) {
				int newr = ((rgb >> 16) & 0x000000FF) + r;
				if(newr > 255)
					newr = 255;
					
				int newg = ((rgb >> 8) & 0x000000FF) + g;
				if(newg > 255)
					newg = 255;	
				
				int newb = ((rgb) & 0x000000FF) + b;
				if(newb > 255)
					newb = 255;	
				
				int newrgb = rgb & 0xFF000000;
				newrgb += (newr << 16);
				newrgb += (newg << 8);
				newrgb += (newb);
				
				return newrgb;
			}
		};
		
		ImageProducer ip = new FilteredImageSource(srcImg.getSource(),filter);
		Image result = Toolkit.getDefaultToolkit().createImage(ip);
		
		return result;
	}
	
	/** 
    * Produces an image with alpha values obtained from an alpha map image. 
    * @param srcImg     The source image.
    * @param alphaImg   The image being used as our alpha map.
    * @param w          The width of both srcImg and alphaImg.
    * @param h          The heigh of both srcImg and alphaImg.
    */
	public static Image applyAlphaMap(Image srcImg, final Image alphaImg, final int w, final int h) {	
		final int[] pixels = new int[w * h];
		PixelGrabber pg = new PixelGrabber(alphaImg, 0,0, w, h, pixels, 0, w);
		try {
			pg.grabPixels();
		} 
		catch (InterruptedException e) {
			return srcImage;
		}
		if ((pg.getStatus() & ImageObserver.ABORT) != 0) {
			return srcImage;
		}
			
		ImageFilter filter = new RGBImageFilter() {
			public int filterRGB(int x, int y, int rgb) {
            if((rgb & 0xFF000000) == 0)
               return rgb
				int curAlpha = ( pixels[y*w + x] & 0x000000FF);
				
				return (rgb & 0x00FFFFFF) + (curAlpha << 24);
			}
		};
		
		ImageProducer ip = new FilteredImageSource(srcImg.getSource(),filter);
		Image result = Toolkit.getDefaultToolkit().createImage(ip);
		
		return result;
	}
	
	
	
	
	
}