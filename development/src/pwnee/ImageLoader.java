package gameEngine;
import java.awt.MediaTracker;
import java.awt.Component;
import java.awt.Image;

public class ImageLoader
{
	private MediaTracker mt;
	private int nextId;
	private Component parent;
	
	public ImageLoader(Component parent)
	{
		this.parent = parent;
		this.reset();
	}
	
	public void reset()
	{
		mt = new MediaTracker(this.parent);
		nextId = 0;
	}
	
	public void addImage(Image img)
	{
		mt.addImage(img, this.nextId);
		this.nextId++;
	}
	
	public void waitForAll()
	{
		try
		{
			mt.waitForAll();	// wait for the filtered image to load before drawing anything.
		}
		catch ( InterruptedException e )
		{
			Thread.currentThread().interrupt();
		}
		reset();
	}
	
	
}

