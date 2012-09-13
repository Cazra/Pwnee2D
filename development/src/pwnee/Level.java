package pwnee;

import java.awt.Component;
import java.awt.Graphics2D;
import pwnee.image.ImageLoader;

public abstract class Level
{
	public Component parent;
	public ImageLoader imgLoader;
	
	public Level(Component parent)
	{
		this.parent = parent;
		imgLoader = new ImageLoader(parent);
	}
	
	
	
	public abstract void loadData();

	
	public void clean()
	{
		imgLoader = null;
		parent = null;
	}
	

	
	public abstract void timerLoop();

	
	public abstract void render(Graphics2D g2D);
}

