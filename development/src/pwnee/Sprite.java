package gameEngine;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.ArrayList;


public abstract class Sprite
{
	public Component parent; // the parent component that this Sprite is drawn to.
	
	public double x,y,z;	// x, y, and (if necessary) z coordinates for the Sprite
	protected double fx,fy,fz; // current focal coordinates for the Sprite used by its current Image.
	public double width,height,depth;	// width, height, and (if necessary) depth for the Sprite 
	
	protected double scaleX,scaleY;	// used for resize transformations
	protected double rotation;		// used for rotational transformations.
	protected AffineTransform transform; // the current transform matrix
	protected boolean transformChanged; // lets our rendering method know whether we need to update transform
	
	protected double r; // distance used for circular/spherical hit detection. If this sprite doesn't use circle-circle hit detection, 
							// then r should be at least large enough to be an admissible heuristic for correctly determining the Sprite's 
							// location in its level's QuadTreeNode structure.
	protected double rx; // x component of elliptical radius.
	protected double ry; // y component of elliptical radius.
	
	protected double boxScale; // scale of hitbox
	
	protected int collisionType; // -1 = box/fine collision only, 0 = ellipse, 1 = ray, 2 = line segment
	
	public boolean isDestroyed;	// used to let any SpriteLayers that have a reference to this know that it has been destroyed. 
	public boolean isVisible;
	public boolean screenKillable;
	
	protected Image curImage; // The current image displayed by this Sprite's animation before transforms.
	protected Image curImageTrans; // The current image displayed by this Sprite's animation after transforms.
	protected AffineTransform colorTransform;
	protected boolean colorTransformChanged;
	protected double semiTransparency; // semiTransparency percentage. 0.0 = 100% opaque, 1.0 = 100% transparent (invisible).
	
	private ArrayList<Long> memLeakTester;
	
	//static int numInstances; // Every child of this class should have a static counter to keep track of the number of instances of it exist.
	//static Hashtable<String,Image> imageTable;
	//static Hashtable<String,Point> focusTable;
	
	public SpriteList spriteList; // the SpriteList to which this Sprite currently belongs. 
	
	// Public Generic Variables
	
	public double[] vars;
	
	
	// CONSTRUCTORS
	
	
	public Sprite()
	{
		this(0,0);
	}
	
	public Sprite(double x, double y)
	{
		this.x = x;
		this.y = y;
		this.z = 0;
		width = 0;
		height = 0;
		depth = 0;
		
		r = 0;
		rx = 0;
		ry = 0;
		
		scaleX = 1.0;
		scaleY = 1.0;
		boxScale = 1.0;
		rotation = 0;
		transform = new AffineTransform(); // identity matrix reflects no scale or rotation.
		transformChanged = false;
		colorTransformChanged = false;
		semiTransparency = 0.0; // opaque
		
		vars = new double[10];
		for(int i = 0 ; i < 10 ; i++) // initialize vars to have 10 personal variables initialized to 0.0.
		{
			//vars.add(0.0);
			vars[0] = 0.0;
		}
		
		isVisible = true;
		isDestroyed = false;
		screenKillable = true;
		
	/*	memLeakTester = new ArrayList<Long>();
		for(long i = 0; i < 10000 ; i++)
		{
			memLeakTester.add(i);
		}*/
	}
	
	/**
	*	void destroy()
	*	This method should set isDestroyed to true and decrement the static variable tracking the number of instances of the Sprite.
	**/
	
	public void destroy()
	{
		this.isDestroyed = true;

		if(spriteList != null)
			spriteList.remove(this);
		spriteList = null;
		
	//	memLeakTester.clear();
		parent = null;
	}
	
	// DATA JUGGLING METHODS
	
	public void setParent(Component comp)
	{
		parent = comp;
	}
	
	// EXPECTED STATIC METHODS
	
	
	// public static void loadImages() 
			// loads this Spite's base images and their focal coordinates from their external files to hashtables keyed by image name
	
	// public static void clean()
			// cleans up all references and static members of this Sprite when its level is unloaded.
	
	
	// NECESSARY METHODS THAT USE EXPECTED STATIC FIELDS
	
	/*
	
	public void destroy()
	{
		numInstances++;
		this.isDestroyed = true;
	}
	*/
	
	// RENDERING METHODS
	
	/**
	*	render(Graphics2D g)
	*	Renders this sprite.
	*	Preconditions: g is the Graphics context used by the parent component of this Sprite.
	*	Postconditions: This sprite is rendered unless it is invisible or is flagged as destroyed. Returns false if it is flagged as destroyed
	*		to let its caller know to get rid of its reference to this Sprite. Otherwise returns true.
	**/
	
	public boolean render(Graphics2D g)
	{
		if(isDestroyed)	// flag the thing trying to render this that this is marked for deletion and the caller ought to delete its reference to this.
			return false;
		
		if(semiTransparency == 1.0 || !isVisible)	// if the Sprite is invisible, we don't need to draw its image.
			return true;
		
		if(transformChanged) // update our image transform so that it's ready for rendering
			updateTransform();
			
		AffineTransform oldTrans = g.getTransform(); // save the Graphics context's original transform.
		Composite oldComp = g.getComposite();
		
		if(semiTransparency > 0.0)
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) (1.0-semiTransparency)));
		
		AffineTransform curTrans = g.getTransform();
		curTrans.translate(this.x,this.y);
		curTrans.concatenate(this.transform);
		curTrans.translate(0-fx,0-fy); // offset image using focus point.
		g.setTransform(curTrans);
		
		this.draw(g);
		
		g.setTransform(oldTrans);	// restore the Graphics context's original transform.
		g.setComposite(oldComp);
		
		return true;
	}
	
	/**
	*	draw(Graphics2D g)
	*	Helper method draws the image of this Sprite after transformations are readied. Called by render(Graphics2D g).
	*	If you need to do something fancy while rendering this sprite's image, override this method.
	*	Preconditions: g is the Graphics2D object passed in by render(Graphcis2D g).
	*	Postconditions: this sprite is drawn, applying the transformations readied in render(Graphics2D g).
	**/
	
	protected void draw(Graphics2D g)
	{
		g.drawImage(this.curImage, null, null);
	}
	
	/**
	*	animate()
	*	Prepares the current animation frame and then prepares the sprite for computing its next frame of animation. Called by render(Graphics2D g).
	*	Preconditions: ImageLoader is used to load the next image of the Sprite if it is a transformed version of one of its base images.
	*	Postconditions: curImage is set to the image of this Sprite's current animation frame 
	*		and necessary values for computing the next frame of animation are prepared.
	**/
	
	protected void animate(ImageLoader il)
	{
		if(this.isDestroyed)
			return;
		
		// compute the base image from current animation math

		// do next animation math
			
		// If a filter is applied to the computed base image, add it to the ImageLoader 
		//	so that the game will be forced to wait for the image to load before rendering.
		// This can be done by doing this:
		
		//	il.addImage(this.curImage);
	}
	
	// COLOR TRANSFORM METHODS
	
	/**
	*	setSemiTransparency(double alpha)
	*	Sets a uniform semi-transparency for this Sprite.
	*	Preconditions: alpha is a value between 0.0 and 1.0 where 0.0 indicates 100% opaque and 1.0 indicates 100% transparent.
	*	Postconditions: The nontransparent pixels in the Sprite's images become alpha percent semi-transparent when drawn. The original images 
	*		in our imageTable are unchanged.
	**/
	
	public void setSemiTransparency(double alpha)
	{
	//	if(parent == null) // the Sprite must have a specific parent component to load its filtered image properly. See render(Graphics2D g).
	//		semiTransparency = 1.0;
			
		if(alpha > 1.0)
			alpha = 1.0;
		if(alpha < 0.0)
			alpha = 0.0;
		
		colorTransformChanged = true;
		semiTransparency = alpha;
	}
	
	/**
	*	getSemiTransparency()
	*	Returns the current semitransparency percentage for this sprite.
	**/
	
	public double getSemiTransparency()
	{
		return semiTransparency;
	}
	
	// IMAGE TRANSFORM METHODS
	
	
	public void scale(double x, double y)
	{
		this.scaleX = x;
		this.scaleY = y;
		this.transformChanged = true;
		
		this.rx = this.r * x;
		this.ry = this.r * y;
	}
	
	public double getScaleX()
	{
		return this.scaleX;
	}
	
	public double getScaleY()
	{
		return this.scaleY;
	}
	
	public void rotate(double degrees)
	{
		this.rotation = degrees;
		this.transformChanged = true;
	}
	
	public double getRotate()
	{
		return this.rotation;
	}
	
	/**
	*	updateTransform()
	*	Helper method updates the transform matrix for this shape, excluding translation for its position.
	*	Preconditions: none.
	*	Postconditions: The AffineTransform for this Sprite's image is updated.
	**/
	
	protected void updateTransform()
	{
		transform = new AffineTransform(); // ID matrix
		
		transform.rotate(0-((rotation/360.0)*Math.PI*2.0)); // converted from degrees to radians
		transform.scale(scaleX,scaleY);
	}
	
	
	//////////////////// COLLISION METHODS
	
	/**
	*	void setRadius(double rad)
	*	Sets the radii for this sprite such that rx = ry.
	*	Preconditions: rad is the desired radius for this sprite.
	*	Postconditions: r, rx, and ry are set to rad.
	**/
	
	public void setRadius(double rad)
	{
		r = rad;
		rx = rad;
		ry = rad;
	}
	
	/**
	*	void getRadius(double rad)
	*	Gets the radius of this Sprite.
	**/
	
	public double getRadius()
	{
		return r;
	}
	
	/**
	*	void setRadius(double radX, double radY)
	*	Sets the x and y radii for this sprite separately.
	*	Preconditions: rx is the desired x radius for this sprite. ry is the desired y radius.
	*	Postconditions: rx is set to radX, ry is set to radY, and r is set to the smallest of radX and radY.
	**/
	
	public void setRadius(double radX, double radY)
	{
		r = Math.min(radX, radY);
		rx = radX;
		ry = radY;
	}
	
	
	
	/**
	*	Rectangle getBBox(boolean admissible)
	*	Returns an the bounding box of this Sprite.
	*	Preconditions: if admissible is true, then a less accurate version of the bounding box is 
	*		returned which is guaranteed to be at least as big as the actual bounding box.
	*	Postconditions: returns the bounding box.
	**/
	
	public Rectangle getBBox(boolean admissible)
	{	
		if(admissible)
		{
			// keep our bBox as a square so that it is an admissible heuristic in our tree.
			
			double scale;
			
			if(scaleX < scaleY)
				scale = scaleY;
			else
				scale = scaleX;
			if(rotation > 0.0)
				scale *= 1.42; // approx. sqrt(2)
			
		/*	int bBoxLeft = (int) (r*scale + x);
			int bBoxUp = (int) (r*scale + y);
			int bBoxWidth = (int) (r*scale*2 + 0.5);
			int bBoxHeight = (int) (r*scale*2 + 0.5);
			*/
			
			double offset = Math.max(fx,fy);
			double sideLength = Math.max(width,height);
			
			int bBoxLeft = (int) (x-offset*scale);
			int bBoxUp = (int) (y-offset*scale);
			int bBoxWidth = (int) (sideLength*scale);
			int bBoxHeight = (int) (sideLength*scale);
			
			return new Rectangle(bBoxLeft , bBoxUp, bBoxWidth, bBoxHeight);
		}
		else
		{
			return null; // I'll come up with this later...
		}
	}
	
	/**
	*	collision(Sprite other)
	*	Checks for a collision between this Sprite and another Sprite.
	**/
	
	public boolean collision(Sprite other)
	{
		if(this.collisionType != 0 && other.collisionType == 0)
			return other.collision(this);
		
		if(this.collisionType == 0 && other.collisionType == 1)
		{
			boolean collision = circle2RayCollision(other);
			return collision;
		}
		else
			return circle2CircleCollision(other);
	}
	
	
	/**
	*	circle2CircleCollision(Sprite other)
	*	Checks for a collision between this Sprite and another Sprite by seeing if their collision circles overlap.
	**/
	
	public boolean circle2CircleCollision(Sprite other)
	{
		double dist = (this.x-other.x)*(this.x-other.x) + (this.y-other.y)*(this.y-other.y);
		
		if(this.rx != this.ry || other.rx != other.ry) // ellipse problem
		{
			return this.ellipseCollision(other);
		}
		else if(dist <= (other.rx + this.rx) * (other.rx + this.rx))
			return true;
		else
			return false;
	}
	
	/**
	*	ellipseCollision(Sprite other)
	*	tests for a collision between this Sprite and another Sprite in the case that this or other's hitbox is an ellipse.
	**/
	
	private boolean ellipseCollision(Sprite other)
	{
		
		double otherAngle = other.rotation;
		double otherRx2 = other.rx*other.rx;
		double otherRy2 = other.ry*other.ry;
		
		double thisR;
		if(this.rx == 0)
			thisR = this.r;
		else
			thisR = this.rx;
		
		// translate system so p is at origin
		
		Point2D.Double p = new Point2D.Double( 0, 0); // this
		Point2D.Double q = new Point2D.Double( other.x - this.x, other.y - this.y); // other
		
		
		if(this.rx != this.ry) // reduce this's hitbox to a circle
		{	
			double scaleY = this.rx/this.ry; // will squash y axis so that this's ry = this's rx.
			double thisAngle = this.rotation;
			
			// create a point representing the endpoint of other's x-radius.
			
			Point2D.Double rA = new Point2D.Double(q.x + other.rx*GameMath.cos(otherAngle), q.y - other.rx*GameMath.sin(otherAngle));
			
			// create a point representing the endpoint of other's y-radius.
			
			Point2D.Double rB = new Point2D.Double(q.x + other.ry*GameMath.cos(otherAngle+90), q.y - other.ry*GameMath.sin(otherAngle+90));
			
			// rotate so that this is aligned with the major axises.
			
		//	System.out.println(rA + " " + rB);
			
			AffineTransform rotate = AffineTransform.getRotateInstance(GameMath.d2r(thisAngle));
			rotate.transform(q,q);
			rotate.transform(rA,rA);
			rotate.transform(rB,rB);
			
			// scale system's y axis so that this is now a circle.
			
			AffineTransform scale = AffineTransform.getScaleInstance(1.0, scaleY);
			scale.transform(q,q);
			scale.transform(rA,rA);
			scale.transform(rB,rB);
			
		//	System.out.println(rA + " " + rB);
			
			otherAngle = GameMath.getAngleTo(q.x, q.y, rA.x, rA.y);
			otherRx2 = (rA.x - q.x)*(rA.x - q.x) + (rA.y - q.y)*(rA.y - q.y);
			otherRy2 = (rB.x - q.x)*(rB.x - q.x) + (rB.y - q.y)*(rB.y - q.y);
			double alpha = Math.abs(GameMath.cos(otherAngle));
			if(this.rx > this.ry)
			{
				scaleY = 1.0/scaleY;
			}
			
			otherRy2 = alpha*scaleY + (1.0-alpha);
			
			
		}
		
	//	System.out.println(other.rotation + " " + otherAngle);
		
		// translate origin to other's center
		
		AffineTransform translate = new AffineTransform();
		translate.translate(0-q.x,0-q.y);
		translate.transform(p,p);
		translate.transform(q,q);
		
	//	System.out.println(other.rotation + " " + p);
		
		// rotate system so that other is aligned with the major axises
		
		AffineTransform rotate = AffineTransform.getRotateInstance(GameMath.d2r(otherAngle));
		rotate.transform(p,p);
		rotate.transform(q,q);
		
		// subsume this's radius into the radial parts of other.
		
		double otherRx = Math.sqrt(otherRx2);
		double otherRy = Math.sqrt(otherRy2);
		
		otherRx2 += thisR*thisR + 2*thisR*otherRx;
		otherRy2 += thisR*thisR + 2*thisR*otherRy;
		
		// scale system so that other is a circle.
		
		double scaleY = Math.sqrt(otherRx2)/Math.sqrt(otherRy2);
		
		AffineTransform scale = AffineTransform.getScaleInstance(1.0, scaleY);
		scale.transform(p,p);
		scale.transform(q,q);
		
		double dist = p.x*p.x + p.y*p.y;
		
		if(dist <= otherRx2)
			return true;
		else
			return false;
	}
	
	/**
	*	circle2RayCollision(Sprite other)
	*	tests for a collision between a circular hitbox and a ray-like hitbox (such as a continuous laser beam).
	**/
	
	public boolean circle2RayCollision(Sprite other)
	{
		Point p = new Point((int) (this.x - other.x), (int) (this.y - other.y));
		AffineTransform rotate = AffineTransform.getRotateInstance(GameMath.d2r(other.rotation));
		
	//	System.out.println(p);
		rotate.transform(p,p);
	//	System.out.println(p + "\n");
		
		double dist = (p.x*p.x) + (p.y*p.y);
		double totalRadius = (this.r*this.r) + (other.r*other.r);
		
		if(p.x < 0)
		{	
		//	System.out.println("< 0 " + dist + " " + totalRadius);
			return (dist <= totalRadius);
		}
		else
		{
		//	System.out.println(">=0 " + (p.y*p.y) + " " + totalRadius);
			return ((p.y*p.y) <= totalRadius);
		}
		
		
	}
	
	/**
	 * boxCollision(Sprite other)
	 *	Checks for a collision between this Sprite and another Sprite by seeing if their images' bounding boxes overlap.
	 *	Preconditions: other is the sprite we are checking for a collision with this Sprite.
	 *	Postconditions: returns true if the Sprites' bounding boxes overlap. Returns false otherwise.
	 **/
	
	public boolean boxCollision(Sprite other)
	{
		if(this.transformChanged)
			this.updateTransform();
		if(other.transformChanged)
			other.updateTransform();
		
		// set up the Points describing this's bounding box
		
		int x1 = (int) (this.x - this.fx * this.boxScale + 0.5);
		int w1 = (int) (this.width * this.boxScale + 0.5);
		int y1 = (int) (this.y - this.fy * this.boxScale + 0.5);
		int h1 = (int) (this.height * this.boxScale + 0.5);
		
		AffineTransform thisTrans = AffineTransform.getTranslateInstance(this.x,this.y);
		thisTrans.concatenate(this.transform);
		thisTrans.translate(0-this.x, 0-this.y);
		
		Point2D[] tPoints = new Point2D[4];
		tPoints[0] = thisTrans.transform(new Point(x1,y1), tPoints[0]);
		tPoints[1] = thisTrans.transform(new Point(x1+w1,y1), tPoints[1]);
		tPoints[2] = thisTrans.transform(new Point(x1+w1,y1+h1), tPoints[2]);
		tPoints[3] = thisTrans.transform(new Point(x1,y1+h1), tPoints[3]);
		
		// set up the Points describing other's bounding box
		
		int x2 = (int) (other.x - other.fx * other.boxScale + 0.5);
		int w2 = (int) (other.width * other.boxScale + 0.5);
		int y2 = (int) (other.y - other.fy * other.boxScale + 0.5);
		int h2 = (int) (other.height * other.boxScale + 0.5);
		
		AffineTransform otherTrans = AffineTransform.getTranslateInstance(other.x,other.y);
		otherTrans.concatenate(other.transform);
		otherTrans.translate(0-other.x,0-other.y);
		
		
		Point2D[] oPoints = new Point2D[4];
		oPoints[0] = otherTrans.transform(new Point(x2,y2), oPoints[0]);
		oPoints[1] = otherTrans.transform(new Point(x2+w2,y2), oPoints[1]);
		oPoints[2] = otherTrans.transform(new Point(x2+w2,y2+h2), oPoints[2]);
		oPoints[3] = otherTrans.transform(new Point(x2,y2+h2), oPoints[3]);
		
		// use axis of separation theorem to test for collisions.
		
		//System.out.println("AoST this vs other");
		if(axisOfSeparation(tPoints,oPoints))
		{
			//System.out.println("AoST other vs this");
			return axisOfSeparation(oPoints,tPoints);
		}
		else
			return false;
	}
	
	/**
	*	axisOfSeparation(Point[] tPoitns, Point[] oPoints)
	*	Helper method for box collision that checks for collisions using the Axis of Separation Theorem.
	*	Preconditions: tPoints is the array of points of the shape whose segments we are using as axises.
	*		oPoints is the array of points of the shape whose points we are testing against the axises of the first shape.
	*		The shapes described by tPoints and oPoints are assumed to be convex.
	*	Postconditions: returns false if a collision between the shapes is impossible. Otherwise returns true.
	**/
	
	private boolean axisOfSeparation(Point2D[] tPoints, Point2D[] oPoints)
	{
		// loop over this Sprite's bounding box's segments. 
		// Vectors describing the segments travel clockwise around the bounding box.
		
		for (int i = 0; i < 4; i++) 
		{
			Point2D q = tPoints[i];
			Point2D r = tPoints[(i+1)%4];
			
			//System.out.println("Comparing segment " + q + "->" + r);
			
			// vector b pointing in the direction of this segment from q to r. 
			
			int bx = (int) (r.getX() - q.getX());
			int by = (int) (r.getY() - q.getY());
			
			// vector n is the normal vector to b and a (0,0,1) defined by a x b.
			
			int nx = by;
			int ny = 0-bx;
			
			// compute denominator for alpha test
			
			int denom = bx*ny - by*nx;
			if( denom == 0)
				denom = 1;
			
			// assume no collision until proved that a collision is still possible.
			
			boolean impossible = true;
			
			// loop through each point in the other polygon to check whether it is above or below the current segment.
			
			for(int j = 0; j < 4 ; j++)
			{
				Point2D p = oPoints[j];
				double alpha = (bx*(p.getY() - q.getY()) + by*(q.getX() - p.getX()))/denom;
				
				//System.out.println("...to Point " + p);
				
				// alpha < 0 if other's point is below current axis of separation. This means that it is probable that there is a collision.
				
				if(alpha < 0)
				{
					impossible = false;
					break;
				}
			}
			
			// impossible if all of other's points are above current axis of separation. This means no collision is possible.
			
			if(impossible)
				return false;
		}
		return true;
	}
	
	
	// 
	
}

