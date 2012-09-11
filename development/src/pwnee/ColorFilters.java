package gameEngine;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;


/**
*	ColorFilters.java
*	By Stephen Lindberg
*	Last modified: 10/11/2011
*	A class of static methods used to apply color filters to images.
**/


public class ColorFilters
{
	/**
	*	setTransparentColor(Image srcImg, final Color tColor)
	*	changes all pixels of a specified color to be 100% transparent.
	*	Preconditions: srcImg is our source Image.
	*		tColor is the color that we want to be made transparent in our result image.
	*	Postconditions: returns a copy of the srcImg, except all pixels with tColor as their color are transparent.
	**/
	
	public static Image setTransparentColor(Image srcImg, final Color tColor) // method accepts a transparent color.
																	 // It'll transform all pixels of the transparent color to transparent.
	{	
		ImageFilter filter = new RGBImageFilter() // overriding part of the RGBImageFilterClass to produce a specialized filter.
		{
			public int testColor = tColor.getRGB() | 0xFF000000; // establish the transparent color as a hexidecimal value for bit-wise filtering.
			
			public int  filterRGB(int x, int y, int rgb) // overriden method
			{
				if((rgb | 0xFF000000 ) == testColor) // if transparent color matches the color being tested, make it transparent.
				{
					return rgb & 0x00FFFFFF; // alpha bits set to 0 yields transparency.
				}
				else // otherwise leave it alone.
					return rgb;
			}
		};
		
		ImageProducer ip = new FilteredImageSource(srcImg.getSource(),filter);
		Image result = Toolkit.getDefaultToolkit().createImage(ip);
		
		return result;
	}
	
	/**
	*	setSemiTransparency(Image srcImg, double semiTrans)
	*	Makes an image uniformly semi-transparent by multiplying the alpha components of its pixels.
	*	Preconditions: srcImg is our source Image.
	*		semiTrans is a value in [0.0, 1.0] specifying how semi-transparent to make the image. 0.0 is completely opaque. 1.0 is completely transparent.
	*	Postconditions: returns a copy of the srcImg, except with all pixels are now semiTrans% more transparent than they were before.
	**/
	
	public static Image setSemiTransparency(Image srcImg, double semiTrans) // method accepts an alpha value in [0.0 , 1.0].
	{	
		if(semiTrans > 1.0) 
			semiTrans = 1.0;
		if(semiTrans < 0.0)
			semiTrans = 0.0;
		//final int alpha = (int) (255 * (1.0 - semiTrans));
		final double alphaMult = 1.0 - semiTrans;	
		
		ImageFilter filter = new RGBImageFilter() // overriding part of the RGBImageFilterClass to produce a specialized filter.
		{
			public int  filterRGB(int x, int y, int rgb) // overriden method
			{
				int curAlpha = (rgb >> 24) & 0xFF;
				
			/*	if((rgb & 0xFF000000) != 0)
					return (rgb & 0x00FFFFFF) | (alpha << 24); // alpha bits set to 0 yields transparency.	
				else
					return rgb;
				*/
				int resAlpha = (int) (curAlpha * alphaMult);
				
				return (rgb & 0x00FFFFFF) | ( resAlpha << 24);
			}
		};
		
		ImageProducer ip = new FilteredImageSource(srcImg.getSource(),filter);
		Toolkit tk = Toolkit.getDefaultToolkit();
		tk.sync();
		Image result = tk.createImage(ip);
		
		
		return result;
	}
	
	/**
	*	invert(Image srcImg)
	*	Inverts the rgb colors of an image.
	*	Preconditions: srcImg is our source Image.
	*	Postconditions: returns a copy of the srcImg with inverted colors. The alpha components of each pixel are left alone.
	**/
	
	public static Image invert(Image srcImg)
	{		
		ImageFilter filter = new RGBImageFilter() // overriding part of the RGBImageFilterClass to produce a specialized filter.
		{
			public int  filterRGB(int x, int y, int rgb) // overriden method
			{
				int newr = 255 - ((rgb >> 16) & 0xFF);
				if(newr > 255)
					newr = 255;
					
				int newg = 255 - ((rgb >> 8) & 0xFF);
				if(newg > 255)
					newg = 255;	
				
				int newb = 255 - ((rgb) & 0xFF);
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
		Toolkit tk = Toolkit.getDefaultToolkit();
		tk.sync();
		Image result = tk.createImage(ip);
		
		
		return result;
	}
	
	
	/**
	*	addColor(Image srcImg, final Color tColor)
	*	Adds the specified color to the rgb values of all pixels in an image.
	*	Preconditions: srcImg is our source Image.
	*		additive is the color that we want to add to our image's current rgb values.
	*	Postconditions: returns a copy of the srcImg, the rgb values of additive are added to the rgb values of each pixel in the image.
	**/
	
	public static Image addColor(Image srcImg, Color additive) // method accepts a color, ignores adding alpha component.
																	 // It'll transform all pixels of the transparent color to transparent.
	{	
	//	int a = additive.getAlpha();
		final int r = additive.getRed();
		final int g = additive.getGreen();
		final int b = additive.getBlue();
			
		ImageFilter filter = new RGBImageFilter() // overriding part of the RGBImageFilterClass to produce a specialized filter.
		{
			public int  filterRGB(int x, int y, int rgb) // overriden method
			{
				int newr = ((rgb >> 16) & 0xFF) + r;
				if(newr > 255)
					newr = 255;
					
				int newg = ((rgb >> 8) & 0xFF) + g;
				if(newg > 255)
					newg = 255;	
				
				int newb = ((rgb) & 0xFF) + b;
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
		Toolkit tk = Toolkit.getDefaultToolkit();
		tk.sync();
		Image result = tk.createImage(ip);
		
		
		return result;
	}
	
	/**
	*	applyAlphaMap(Image srcImg, final Image alphaImg, final int w, final int h)
	*	Applies semi-transparency to an image using an alpha map image.
	*	Preconditions: srcImg is our source Image.
	*		alphaImg is the alpha map for srcImg. We will assume that the widths are the same and the heights are the same for srcImg and alphaImg.
	*		w is the width of srcImg and alphaImg.
	*		h is the height of srcImg and alphaImg.
	*	Postconditions: returns a copy of srcImage with alphaImg applied as an map for its pixels' alpha values.
	**/
	
	
	public static Image applyAlphaMap(Image srcImg, final Image alphaImg, final int w, final int h) // method accepts a color, ignores adding alpha component.
																	 // It'll transform all pixels of the transparent color to transparent.
	{	
		final int[] pixels = new int[w * h];
		PixelGrabber pg = new PixelGrabber(alphaImg, 0,0, w, h, pixels, 0, w);
		try 
		{
			pg.grabPixels();
		} 
		catch (InterruptedException e) 
		{
			System.err.println("interrupted waiting for pixels!");
			return null;
		}
		if ((pg.getStatus() & ImageObserver.ABORT) != 0) 
		{
			System.err.println("image fetch aborted or errored");
			return null;
		}
			
		ImageFilter filter = new RGBImageFilter() // overriding part of the RGBImageFilterClass to produce a specialized filter.
		{
			public int  filterRGB(int x, int y, int rgb) // overriden method
			{
				int curAlpha = ( pixels[y*w + x] & 0x00FF0000) >> 16;
				
				return (rgb & 0x00FFFFFF) + (curAlpha << 24);
			}
		};
		
		ImageProducer ip = new FilteredImageSource(srcImg.getSource(),filter);
		Toolkit tk = Toolkit.getDefaultToolkit();
		tk.sync();
		Image result = tk.createImage(ip);
		
		
		return result;
	}
	
	
	
	
	
}