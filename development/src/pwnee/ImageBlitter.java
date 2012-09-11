package gameEngine;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

public class ImageBlitter
{
	public static Image crop(Image image, int x, int y, int w, int h)
	{
		ImageProducer source = image.getSource();
		
		FilteredImageSource newSource = new FilteredImageSource(source, new CropImageFilter(x,y,w,h));
		
		Image result = Toolkit.getDefaultToolkit().createImage(newSource);
		
		return result;
	}
	
	public static Image cropTiled(Image image, int x, int y, int w, int h)
	{
		ImageProducer source = image.getSource();
		
		int b = 1; // assumes a border of width 0 around each frame
		
		FilteredImageSource newSource = new FilteredImageSource(source, new CropImageFilter(b + b*2*x + x*w, b + b*2*y + y*h,w,h));
		
		Image result = Toolkit.getDefaultToolkit().createImage(newSource);
		
		return result;
	}
}