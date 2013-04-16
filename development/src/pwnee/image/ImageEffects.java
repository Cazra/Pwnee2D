package pwnee.image;

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
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;


/** A class with a bunch of static methods for applying filters on images. */
public class ImageEffects {
	
   /** Produces an image using a cropped section of the source image. */
   public static Image crop(Image srcImage, int x, int y, int w, int h) {
      ImageProducer ip = new FilteredImageSource(srcImage.getSource(), new CropImageFilter(x,y,w,h));
		
		Image result = Toolkit.getDefaultToolkit().createImage(ip);
		return result;
   }
   
	/** Produces an image with one color set to be completely transparent. */
	public static Image setTransparentColor(Image srcImg, final Color transColor) {	
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
    * @param _alpha      the value that will be assigned to the pixels' alpha components in the range [0,255].
    */
   public static Image setOpacity(Image srcImg, int _alpha) {	
      // normalize alpha to be in the range [0,255]
      final int alpha = Math.min(255, Math.max(0, _alpha));
      
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
  
  /** Produces an image whose rgb values are decremented by an subtractive color. */
	public static Image subColor(Image srcImg, Color subtractive) {	
		final int r = subtractive.getRed();
		final int g = subtractive.getGreen();
		final int b = subtractive.getBlue();
			
		ImageFilter filter = new RGBImageFilter() {
			public int  filterRGB(int x, int y, int rgb) {
				int newr = ((rgb >> 16) & 0x000000FF) - r;
				if(newr < 0)
					newr = 0;
					
				int newg = ((rgb >> 8) & 0x000000FF) - g;
				if(newg < 0)
					newg = 0;	
				
				int newb = ((rgb) & 0x000000FF) - b;
				if(newb < 0)
					newb = 0;	
				
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
			return srcImg;
		}
		if ((pg.getStatus() & ImageObserver.ABORT) != 0) {
			return srcImg;
		}
			
		ImageFilter filter = new RGBImageFilter() {
			public int filterRGB(int x, int y, int rgb) {
            if((rgb & 0xFF000000) == 0)
               return rgb;
				int curAlpha = ( pixels[y*w + x] & 0x000000FF);
				
				return (rgb & 0x00FFFFFF) + (curAlpha << 24);
			}
		};
		
		ImageProducer ip = new FilteredImageSource(srcImg.getSource(),filter);
		Image result = Toolkit.getDefaultToolkit().createImage(ip);
		
		return result;
	}
	
	
	/** 
   * Composites the source image with a color, using the source image's 
   * pixels' alpha components. That, is the color of the source image's pixels
   * are adjusted towards the composited color by a certain amount.
   * 
   * This effect is similar to the addColor effect, except it is parametric 
   * between srcImg's colors and color, and it is also much faster.
   * If alpha is 0.0, srcImg will be drawn using its original colors. 
   * If alpha is 1.0, then the shape of srcImg will be drawn with all
   * its pixels set to color.
   * @param srcImg    The source image.
   * @param color     The color we are compositing with the source image.
   * @param alpha     A parametric value in range [0.0, 1.0] that determines
   *                  how much of the source image's colors or the composite
                      color to determine the result pixels.
   */
  public static Image colorComposite(Image srcImg, Color color, double alpha) {
    alpha = Math.max(0, Math.min(1, alpha));
    int w = (int) srcImg.getWidth(null);
    int h = (int) srcImg.getHeight(null);
    
    BufferedImage result = new BufferedImage(w,h, BufferedImage.TYPE_INT_ARGB);
    BufferedImage overlay = new BufferedImage(w,h, BufferedImage.TYPE_INT_ARGB);
    AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.DST_IN, (float) alpha);
    
    Graphics2D g = overlay.createGraphics();
    // clearing color is transparent.
    g.setBackground(new Color(0x00000000, true));
    
    // draw our color and overlay the image on it using our AlphaComposite.
    g.setColor(color);
    g.fillRect(0, 0, w, h);
    g.setComposite(ac);
    g.drawImage(srcImg, 0, 0, null);
    
    // apply the overlay ontop of the source image.
    g = result.createGraphics();
    g.drawImage(srcImg, 0, 0, null);
    g.drawImage(overlay, 0, 0, null);
    
    return result;
  }
	
	
}