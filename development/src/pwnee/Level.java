package gameEngine;

import java.awt.Component;
import java.awt.Graphics2D;

public abstract class Level
{
	public Component parent; //	a reference to the parent JPanel class, typically the one used for the game's main screen
	public ImageLoader imgLoader;
	
	public Level(Component parent)
	{
		this.parent = parent;
		imgLoader = new ImageLoader(parent);
	}
	
	/**
	*	loadData()
	*	loads image and sound data for this level into memory. This method should be called before 
	*	running the running the level's timer loop and render methods for the first time. 
	**/
	
	public abstract void loadData();
	
	/**
	*	clean()
	*	This method unloads the level's image and sound data from memory. Any other memory management clean-up for
	*	the level is also taken care of here.
	*
	**/
	
	public void clean()
	{
		imgLoader = null;
		parent = null;
	}
	
	/**
	*	timerLoop()
	*	This method steps through one animation frame for the level. 
	*	This method should be called by the game's timer event handler before rendering the level and after loadData is used.
	*
	**/
	
	public abstract void timerLoop();
	
	/**
	*	render(Graphics2D g2D
	*
	*
	**/
	
	public abstract void render(Graphics2D g2D);
}

