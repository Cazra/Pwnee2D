package gameEngine;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.Hashtable;
import java.net.URL;
import gameEngine.*;

public abstract class WrappingImageSprite extends Sprite
{
	//private static int numInstances = 0;
	//private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	//private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	public double cameraAngle;
	public double cameraX, cameraY;
	public double cameraZoom;
	public int[] pixels = null;
	public int pixWidth;
	public int pixHeight;
	
	// CONSTRUCTOR
	
	public WrappingImageSprite(double x, double y, double w, double h)
	{
		super(x,y);
	//	numInstances++;
		
		width = w;
		height = h;
		
		pixWidth = -1;
		pixHeight = -1;
		
		cameraAngle = 0;
		cameraZoom = 1.0;
		cameraX = 0;
		cameraY = 0;
	}
	
	// MEM MANAGEMENT METHODS
	
	/**
	*	destroy()
	*	decrements number of instances of this class and marks it as destroyed.
	**/
	
	public void destroy()
	{
		this.isDestroyed = true;
		//numInstances--;
	}
	
	/**
	*	loadImages()
	*	loads and stores image data for this Sprite.
	**/
	/*
	public static void loadImages(ImageLoader imgLoader)
	{
		// get our default toolkit
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		URL imageURL;
		
		// load our images
		
		imageURL = Mode7Sprite.class.getResource("mode7TestGraphic.png");
		Image bullet1Img = tk.getImage(imageURL);
	//	bullet1Img = ColorFilters.setTransparentColor(bullet1Img, new Color(0xFF00FF));
		imgLoader.addImage(bullet1Img);
		
		// setup focus points for the images
		
		focusTable.put("testImg",new Point(0,0));
		
		// store them into our hashtables
		
		imageTable.put("testImg",bullet1Img);
		
		System.out.println("loaded image data for Mode7Sprite");
		
	}*/
	
	/**
	*	clean()
	*	Used to unload image data and cleans up static members of this Sprite extension 
	*		when the parent component is done with it.
	**/
	/*
	public static void clean()
	{
		numInstances = 0;
		focusTable.clear();
		imageTable.clear();
	}
	*/
	
	// RENDERING METHODS
	
	/**
	*	void draw(Graphics2D g)
	*	Preconditions: g is the parent context's graphics handle.
	*	Postconditions: the sprite is drawn in a width*height rectangular viewport such that the sprite 
	*		image wraps around infinitely and can be zoomed, panned, and rotated.
	**/
	
	protected void draw(Graphics2D g)
	{
		
		// obtain the raster that we will write the pixels of our resulting image to
		
		BufferedImage mode7Img = new BufferedImage((int) width,(int) height,BufferedImage.TYPE_INT_ARGB);
		int[] writePixels = ((DataBufferInt)(mode7Img.getRaster().getDataBuffer())).getData(); 
		
		// obtain the source image's pixels
		
		if(pixels == null)
		{
			PixelGrabber pg = new PixelGrabber(this.curImage, 0,0, -1, -1, false);
			try
			{
				pg.grabPixels();
			}
			catch(Exception ex)
			{
				System.out.println(ex.getMessage());
				ex.printStackTrace();
			}
			
			pixWidth = pg.getWidth();
			pixHeight = pg.getHeight();
			
			pixels = (int[]) pg.getPixels();
		}
		
		// compute any values that will be reused for each pixel.
		
		double camDx = cameraX;
		double camDy = cameraY;
		
		double rCos = GameMath.cos(cameraAngle);
		double rSin = GameMath.sin(cameraAngle);

		// process each pixel in the sprite's viewport.
		
		for(int j = 0; j < height; j++)
		{
			for(int i = 0; i < width; i++)
			{	
				Point pix = getPixelAt(i,j,camDx,camDy,rCos,rSin);
				
				int pixX = pix.x % pixWidth;
				int pixY = pix.y % pixHeight;
				if(pixX < 0)
					pixX = pixWidth+pixX;
				if(pixY < 0)
					pixY = pixHeight+pixY;
				int pixelColor =  pixels[pixY*pixWidth + pixX];
				int alpha = (pixelColor >> 24) & 0xff;
				if(alpha > 0)
					alpha = (int)(alpha*(1-semiTransparency));
				pixelColor = (pixelColor & 0x00ffffff) + (alpha << 24);
				
				try
				{
					writePixels[(int)width*j+i] = pixelColor;
				}
				catch(Exception ex)
				{
					System.out.println("WrappingImage error: \n" + ex.getMessage());
					ex.printStackTrace();
				}
			}
		}
		
		// draw the resulting mode7 image onto the scene.
		
		g.drawImage(mode7Img, null, null);
	}
	
	
	protected Point getPixelAt(double i,double j,double camDx, double camDy, double rCos, double rSin)
	{
		i-=width/2;
		j-=height/2;
		Point2D rotated = new Point((int)(i*rCos - j*rSin), (int)(i*rSin + j*rCos));
		return new Point((int)((rotated.getX()*cameraZoom + camDx)), (int)((rotated.getY()*cameraZoom + camDy)));
	}
	
	
	/**
	*	animate()
	*	Prepares the current animation frame and then prepares the sprite for computing its next frame of animation. Called by render(Graphics2D g).
	*	Preconditions: none.
	*	Postconditions: curImage is set to the image of this Sprite's current animation frame 
	*		and necessary values for computing the next frame of animation are prepared.
	**/
	
	public void animate(ImageLoader il)
	{
		super.animate(il);
		
		/*
		curImage = imageTable.get("testImg");
		Point curFocus = focusTable.get("testImg");
		
		fx = curFocus.x;
		fy = curFocus.y;
		
		if(semiTransparency > 0.0 && colorTransformChanged) // apply semi-transparency
		{
		//	this.curImage = ColorFilters.setSemiTransparency(this.curImage, this.semiTransparency);
			colorTransformChanged = false;
		}	
		*/
		
	}
	
	
	
	
	
}
