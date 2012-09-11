package gameEngine;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.Hashtable;
import java.net.URL;
import gameEngine.*;

public abstract class Mode7Sprite extends Sprite
{
	//private static int numInstances = 0;
	//private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	//private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	public double cameraAngle;
	public double cameraX, cameraY, elevation, camDist;
	protected int[] pixels = null;
	protected int pixWidth;
	protected int pixHeight;
	protected int[] camColors = {0xFFFFCCCC, 0xFFCCFFCC,0xFFCCCCFF};
	protected int curCamColor = 0;
	
	public double horizonY;
	public boolean showCamPoint;
	public int blurTechnique = 0;
	
	// CONSTRUCTOR
	
	public Mode7Sprite(double x, double y, double w, double h)
	{
		super(x,y);
	//	numInstances++;
		
		width = w;
		height = h;
		
		pixWidth = -1;
		pixHeight = -1;
		
		cameraAngle = 0;
		cameraX = 0;
		cameraY = 0;
		camDist = 50;
		showCamPoint = false;
		
		elevation = 50;
		horizonY = 0;
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
	*	
	*	
	*	
	**/
	
	protected void draw(Graphics2D g)
	{
		//g.drawImage(this.curImage, null, null);
		
		BufferedImage mode7Img = new BufferedImage((int) width,(int) height - 1,BufferedImage.TYPE_INT_ARGB);
		
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
		
		
		// test code...
		
		double camDx = cameraX - camDist*GameMath.cos(cameraAngle);
		double camDy = cameraY + camDist*GameMath.sin(cameraAngle);
		
		double rCos = GameMath.cos(180-cameraAngle+90);
		double rSin = GameMath.sin(180-cameraAngle+90);
		
		int camColor = camColors[curCamColor];
		curCamColor++;
		
		//AffineTransform rotation = AffineTransform.getRotateInstance(GameMath.d2r(180-cameraAngle+90));
		double aspectRatio = width/height;
		double horizonCenter = width/2.0;
		
		if(curCamColor > 2)
			curCamColor = 0;
		
		for(int j = 1; j < height; j++)
		{
			
			for(int i = 0; i < width; i++)
			{	
				int[] pix = getPixelAt(i,j,camDx,camDy,rCos,rSin,horizonCenter,aspectRatio);
				
				int pixX = pix[0];
				int pixY = pix[1];
				
				boolean isCamPoint = false;
				if(Math.abs(pixX - cameraX) < 10 && Math.abs(pixY - cameraY) < 10)//  || !(pixX >=0 && pixX <pixWidth && pixY >= 0 && pixY < pixHeight))
					isCamPoint = true;

				pixX = pixX % pixWidth;
				pixY = pixY % pixHeight;
				if(pixX < 0)
					pixX = pixWidth+pixX;
				if(pixY < 0)
					pixY = pixHeight+pixY;
				
				int pixelColor =  pixels[pixY*pixWidth + pixX];
		//		if(blurTechnique == 1)
		//			pixelColor = mipMapComposite(i,j,camDx,camDy,rCos,rSin,horizonCenter,aspectRatio);
				
				try
				{
					if(!showCamPoint || !isCamPoint)
						writePixels[(int)width*(j-1)+i] = pixelColor;
					else
						writePixels[(int)width*(j-1)+i] = camColor;
						
					
				}
				catch(Exception ex)
				{
					System.out.println("Mode7 error: \n" + ex.getMessage());
					ex.printStackTrace();
				}
			}
		}
		
		if(blurTechnique == 2)
			nearbyBlur(writePixels, 1,1);
		
		// draw the resulting mode7 image onto the scene.
		
		g.drawImage(mode7Img, null, null);
	}
	
	
	protected int[] getPixelAt(double i,double j,double camDx, double camDy, double rCos, double rSin, double horizonCenter, double aspectRatio)
	{
		double z = (elevation)/(j+horizonY);
		double rasterX = (horizonCenter - i)*z;
		
		int pixX = (int) (rasterX);// % pixWidth;
		int pixY = (int) (elevation*z*aspectRatio);// % pixHeight;
		
		int rpixX = (int)(pixX*rCos - pixY*rSin + camDx);
		int rpixY = (int)(pixX*rSin + pixY*rCos + camDy);
		int[] result = {rpixX,rpixY};
		return result;
	}
	
	/*
	private int mipMapComposite(int i,int j,double camDx, double camDy, double rCos, double rSin, double horizonCenter, double aspectRatio)
	{
		double rComp,gComp,bComp,aComp;
		rComp = gComp = bComp = aComp = 0;
		
		double offset = 0.1;
		Point here = getPixelAt(i,j,camDx,camDy,rCos,rSin,horizonCenter,aspectRatio);
		Point up = getPixelAt(i-offset,j-offset,camDx,camDy,rCos,rSin,horizonCenter,aspectRatio);
		
		int pixX,pixY,pixelColor;
		double hereWeight = 0.4;
		double nearWeight = (1.0-hereWeight)/2.0;
		
		int sMin = Math.min(up.x,here.x);
		int sMax = Math.max(up.x,here.x);
		int tMin = Math.min(up.y,here.y);
		int tMax = Math.max(up.y,here.y);
		int texelWidth = Math.max(sMax - sMin,tMax - tMin);
		if(texelWidth > 3)
			texelWidth = 3;
		int numTexels = 0;
		
		
		for(int m = here.x-texelWidth; m <= here.x+texelWidth; m++)
		{
			for(int n = here.y-texelWidth; n <= here.y+texelWidth; n++)
			{
				pixX = (int)Math.abs(m) % pixWidth;
				pixY = (int)Math.abs(n) % pixHeight;
				pixelColor =  pixels[pixY*pixWidth + pixX];
				aComp += ((pixelColor >> 24) & 0xff);
				rComp += ((pixelColor >> 16) & 0xff);
				gComp += ((pixelColor >> 8) & 0xff);
				bComp += ((pixelColor ) & 0xff);
				
				numTexels++;
			}
		}
		
		double texelWeight = 1.0/(numTexels);
		
		aComp *= texelWeight;
		rComp *= texelWeight;
		gComp *= texelWeight;
		bComp *= texelWeight;
		
		return (int) (((int)aComp << 24) + ((int)rComp << 16) + ((int)gComp << 8) + bComp);
	} 
	*/
	
	private void nearbyBlur(int[] writePixels, int dist, int numBlurs)
	{
		
		
		for(int k = 0; k < numBlurs;k++)
		{
			for(int j = 0; j < height-1; j++)
			{
				double hereWeight = j*1.0/(height-1);
				double nearWeight = (1-hereWeight)/((1.0 +dist*2)*(1.0 +dist*2));
				
				for(int i = 0; i < width; i++)
				{	
					int aComp,rComp,gComp,bComp;
					int pixelColor;
					aComp = rComp = gComp = bComp = 0;
					Point up,down,left,right;
					
					for(int m = i-dist; m <= i+dist ; m++)
					{
						for(int n = j-dist; n <= j+dist; n++)
						{
							Point here = new Point(Math.abs(m%(int)width),Math.abs(n%(int)(height-1)));
							pixelColor = writePixels[(int)width*here.y + here.x];
							aComp += (int)(((pixelColor >> 24) & 0xff)*nearWeight);
							rComp += (int)(((pixelColor >> 16) & 0xff)*nearWeight);
							gComp += (int)(((pixelColor >> 8) & 0xff)*nearWeight);
							bComp += (int)(((pixelColor) & 0xff)*nearWeight);
						}
					}
					
					pixelColor = ((aComp << 24) + (rComp << 16) + (gComp << 8) + (bComp));
					
					writePixels[(int)width*j + i] = pixelColor;
				}
			}
		}
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
		
		animateSemiTransparencyFrame(il, "testImg");
		
		fx = curFocus.x;
		fy = curFocus.y;
		
		if(semiTransparency > 0.0 && colorTransformChanged) // apply semi-transparency
		{
		//	this.curImage = ColorFilters.setSemiTransparency(this.curImage, this.semiTransparency);
			colorTransformChanged = false;
		}	
		*/
		
	}
	
	
	/**
	*	animateSemiTransparencyFrame(ImageLoader il, String imgKey)
	*	Applies current semi-transparency to a frame of animation and stores the modified frame for later use.
	*	Preconditions: il is the ImgLoader passed in by animate(ImageLoader il).
	*		imgKey is the normal unmodified key name for the current frame of animation.
	*	Postconditions: the current image is modified with the current semitransparency. 
	*		If this semitransparent frame isn't already in the imageTable, it is added in.
	**/
	/*
	protected void animateSemiTransparencyFrame(ImageLoader il, String imgKey)
	{
		double keyTag = (Math.round(this.semiTransparency*256)/256.0);
		if(imageTable.containsKey(imgKey + "semiTrans" + keyTag))
		{
			curImage = imageTable.get(imgKey + "semiTrans" + keyTag);
		}
		else
		{
			Image semicurImage = ColorFilters.setSemiTransparency(curImage, this.semiTransparency);
			imageTable.put(imgKey + "semiTrans" + keyTag, semicurImage);
			
			curImage = semicurImage;
			il.addImage(curImage);
		}
	}
	*/
	
	
}
