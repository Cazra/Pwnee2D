package gameEngine;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Graphics2D;

public class SpriteList implements Iterable
{
	public LinkedList<Sprite> sprList;
	public boolean isVisible;
	
	public SpriteList()
	{
		sprList = new LinkedList<Sprite>();
		isVisible = true;
	}
	
	public int size()
	{
		return sprList.size();
	}
	
	public boolean add(Sprite s)
	{
		if(s.spriteList != null)
			s.spriteList.remove(s);
		
		s.spriteList = this;
		
		return sprList.add(s);
	}
	
	public boolean remove(Sprite s)
	{
		s.spriteList = null;
		return sprList.remove(s);
	}
	
	public void destroyAll()
	{
		ArrayList<Sprite> trash = new ArrayList<Sprite>(sprList);
		for(Sprite s : trash)
		{
			s.destroy();
		}
		
		sprList.clear();
	}
	
	
	public void update()
	{
		ArrayList<Sprite> trash = new ArrayList<Sprite>(sprList);
		for(Sprite s : trash)
		{
			if(s.isDestroyed)
				sprList.remove(s);
		}
	}
	
	public Iterator<Sprite> iterator()
	{
		return (new ArrayList<Sprite>(sprList)).iterator();
	}
	
	public void animate(ImageLoader il)
	{
		for(Sprite s : sprList)
			s.animate(il);
	}
	
	
	public void render(Graphics2D g2D)
	{
		if(!isVisible)
			return;
		for(Sprite s : sprList)
			s.render(g2D);
	}
	
}

