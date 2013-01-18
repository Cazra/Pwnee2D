package example;

import java.awt.*;
import java.util.HashMap;
import pwnee.sprites.Sprite;
import pwnee.GameMath;
import pwnee.image.*;


public class SupmuwSprite extends Sprite {
    
    /** A String -> Image mapping used as a library for this sprite's loaded images. Since all Sprites of this kind share the same pool of images, this is static. */
    public static HashMap<String, Image> images = new HashMap<String, Image>();
    
    /** A reference to the current image being used by this Sprite. */
    public Image curImg;
    
    /** The ball's current x velocity. */
    public double dx;
    
    /** The ball's current y velocity. */
    public double dy;
    
    /** A timer for this sprite's animation. */
    public int animTimer = 0;
    
    /** The supmuw's collision radius. */
    public double cRadius;
    
    /** Flag tells it whether this supmuw should be dead (due to colliding with another ball). */
    public boolean isDead = false;
    
    /** Creates the SupmuwSprite with a random velocity. */
    public SupmuwSprite(int x, int y, double scale) {
        super(x,y);
        dx = GameMath.rand.nextDouble()*6-3;
        dy = GameMath.rand.nextDouble()*6-3;
        
        // The image we're using for the Sprite in this example is pretty tiny. So let's scale it up a bit.
        this.scale(scale,scale);
        width = 24;
        height = 24;
        cRadius = 8*scale;
        
        // Start the animTimer at a random number in [0,32), so that it won't look like all the monsters are dancing in sync.
        animTimer = GameMath.rand.nextInt(32);
    }
    
    
    /** Loads the images for this Sprite class. */
    public static void loadImages(ImageLoader il) {
         // Obtain the source image.
         Image srcImg = il.loadFromFile("graphics/SupmuwSprite.png");
         
         // Set magenta as the transparent color for the source image.
         srcImg = ImageEffects.setTransparentColor(srcImg, new Color(0xFF00FF));
         
         // crop the source image to get the images for the individual animation frames.
         Image frame1 = ImageEffects.crop(srcImg, 1, 1, 24, 24);
         Image frame2 = ImageEffects.crop(srcImg, 27, 1, 24, 24);
         Image frame3 = ImageEffects.crop(srcImg, 1, 27, 24, 24);
         
         // Add the cropped animation frames to our images map.
         images.put("dance1",frame1);
         images.put("dance2",frame2);
         
         images.put("dead",frame3);
         
         // Add our cropped images to the image loader and wait for them to finish loading.
         il.addImage(frame1);
         il.addImage(frame2);
         il.addImage(frame3);
         il.waitForAll();
         
         System.err.println("loaded SupmuwSprite's images.");
    }
    
    
    
    /** Performs an iteration of this Sprite's animation logic. */
    public void animate() {
         // "dance1" if animTimer is in [0,16].
         String imgName = "dance1";
         
         // "dance2" if animTimer is in [17,32].
         if(animTimer > 16) 
            imgName = "dance2";
          
         if(isDead)
          imgName = "dead";
         
         // set the focal point of the monster to be its image's center.
         focalX = 12;
         focalY = 12;
         
         // Look up our current animation frame in images using imgName as our key and set it as the current image.
         curImg = images.get(imgName);
         
         // Increment the animation timer and loop it around after 32 frames.
         animTimer++;
         if(animTimer > 32)
            animTimer = 0;
    }
    
    
    
    /** Makes the monster move at its current velocity and bounce whenever it hits the edge of the screen. */
    public void move(Component screen) {
        x += dx;
        y += dy;
        
        int w = screen.getSize().width;
        int h = screen.getSize().height;
        
        if(x > w) {
            x = w;
            if(dx > 0)
                dx *= -1;
        }
        
        if(y > h) {
            y = h;
            if(dy > 0)
                dy *= -1;
        }
        
        if(x < 0) {
            x = 0;
            if(dx < 0)
                dx *= -1;
        }
        
        if(y < 0) {
            y = 0;
            if(dy < 0)
                dy *= -1;
        }
    }
    
    
    
    /** Checks if this supmuw is colliding with another supmuw using radial collision detection. */
    public boolean collidesWith(SupmuwSprite other) {
      if(other == this)
        return false;
      return (GameMath.sqrDist(this.x, this.y, other.x, other.y) <= (this.cRadius + other.cRadius)*(this.cRadius + other.cRadius));
    }
    
    
    
    /** Draws the current frame of the monster's animation. */
    public void draw(Graphics2D g) {
         // draw the current image
         g.drawImage(curImg,0,0, null);
    }
    
    
    
}

