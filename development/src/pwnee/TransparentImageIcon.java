package gameEngine;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;


public class TransparentImageIcon extends ImageIcon
{
	public TransparentImageIcon(String filename)
	{
		super(filename);
	}
	
	public void setTransparentColor(final Color tColor) // method accepts a transparent color.
																	 // It'll transform all pixels of the transparent color to transparent.
	{
		Image image = this.getImage(); // convert the ImageIcon to an Image
		
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
		
		ImageProducer ip = new FilteredImageSource(image.getSource(),filter);
		image = Toolkit.getDefaultToolkit().createImage(ip);
		
		this.setImage(image);
	}
	
}