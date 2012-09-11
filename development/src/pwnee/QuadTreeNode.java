package gameEngine;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class QuadTreeNode
{
	public Point[] min;
	public Point[] max;
	public int level;
	
	public QuadTreeNode[] nodes; // 0 UL, 1 UR, 2 DL, 3 DR
	public Rectangle[] quads;
	public ArrayList<Sprite> list;
	
	public boolean isMaxDepth;
	public final int MAXDEPTH = 8;
	
	// CONSTRUCTORS
	
	public QuadTreeNode(int minX, int minY, int maxX, int maxY, int level)
	{	
		min = new Point[4];
		max = new Point[4];
		nodes = new QuadTreeNode[4];
		quads = new Rectangle[4];
		
		list = new ArrayList<Sprite>();
		this.level = level;
		
		// minimum width and height of a quadrant is 2 pixels.
		
		if(maxX - minX <= 2 || maxY - minY <= 2) 
			isMaxDepth = true;
		else
			isMaxDepth = false;
		
		// compute min/max points of quadrants
		
		int midX = (int) (minX/2.0 + maxX/2.0);
		int midY = (int) (minY/2.0 + maxY/2.0);
		
		min[0] = new Point(minX,minY);
		min[1] = new Point(midX,minY);
		min[2] = new Point(minX,midY);
		min[3] = new Point(midX,midY);
		
		max[0] = new Point(midX,midY);
		max[1] = new Point(maxX,midY);
		max[2] = new Point(midX,maxY);
		max[3] = new Point(maxX,maxY);
		
		// form quadrant rectangles
		
		for(int i = 0; i < 4 ; i++)
		{
			quads[i] = new Rectangle(min[i].x, min[i].y, max[i].x - min[i].x, max[i].y - min[i].y);
		}
	}
	
	public QuadTreeNode(Point min, Point max, int level)
	{
		this(min.x, min.y ,max.x ,max.y ,level);
	}
	
	
	/**
	*	void insert(Sprite s)
	*	inserts Sprite s into the quadTree.
	**/
	
	public void insert(Sprite s)
	{
		if(s.isDestroyed)
			return;
		Rectangle bBox = s.getBBox(true);
		
		// drop the Sprite if it is outside the quadtree's area
		
		if(bBox.x+bBox.width < min[0].x || bBox.x > max[3].x || bBox.y+bBox.height < min[0].y || bBox.y > max[3].y)
			return;
		
		// otherwise, insert it into whatever areas it overlaps
		
		insert(s, bBox);
	}
	
	
	
	/**
	*	void insert(Sprite s, Rectangle bBox)
	*	inserts Sprite s into the quadTree, given also its bounding Rectangle bBox.
	**/
	
	protected void insert(Sprite s, Rectangle bBox)
	{
		if(isMaxDepth || level == MAXDEPTH)
		{
			list.add(s);
			return;
		}
		
		// fits in quadrant i?
		
		for(int i = 0; i < 4 ; i++)
		{
			if(quadContains(bBox,min[i],max[i]))
			{
				// create new child node if necessary
				
				if(nodes[i] == null) 
					nodes[i] = new QuadTreeNode(min[i], max[i], level+1);
				
				// insert into quadrant's node
				
				nodes[i].insert(s);
				return;
			}
		}
		
		// if the sprite doesn't fit in any of the quadrants, insert it into this node's list
		
	//	System.out.println("inserted " + s + "  at level " + level);
	//	System.out.println("\tBBox: " + bBox);
		list.add(s);
	}
	
	
	
	
	
	
	private boolean quadContains(Rectangle bBox, Point min, Point max)
	{	
		if(bBox.x >= min.x && bBox.x + bBox.width <= max.x && bBox.y >= min.y && bBox.y + bBox.height <= max.y)
			return true;
		else
			return false;
	}
	
	
	/**
	*	ArrayList<Sprite> query(Sprite s)
	*	queries the location of s to determine what other Sprites it may be colliding with.
	*	Preconditions: s is the Sprite we're trying to find neighbors of.
	*	Postconditions: Returns a list of Sprites that s may be colliding with in resultList.
	**/
	
	public ArrayList<Sprite> query(Sprite s)
	{
		ArrayList<Sprite> resultList = new ArrayList<Sprite>();
		Rectangle bBox = s.getBBox(true);
		
		recQuery(s, bBox, resultList);
		
		return resultList;
	}
	
	/**
	*	void recQuery(Sprite s, Rectangle bBox, ArrayList<Sprite> resultList)
	*	queries the location of s to determine what other Sprites it may be colliding with.
	*	Preconditions: s is the Sprite we're trying to find neighbors of.
	*	Postconditions: Returns a list of Sprites that s may be colliding with in resultList.
	**/
	
	protected void recQuery(Sprite s, Rectangle bBox, ArrayList<Sprite> resultList)
	{
		// add current node's list to resultList
		
		resultList.addAll(this.list);
		
		// intersect quad i?
		
		for(int i = 0; i < 4 ; i++)
		{
			// if we're intersecting quad[i] and quad[i]'s node is not null, we'll recursively query that node.
			if(nodes[i] != null && quads[i].intersects(bBox))
				nodes[i].recQuery(s, bBox, resultList);
		}
	}
	
	
	
}




