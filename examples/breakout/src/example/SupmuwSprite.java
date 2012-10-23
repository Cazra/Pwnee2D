package example;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.HashMap;
import pwnee.sprites.Sprite;
import pwnee.GameMath;
import pwnee.image.*;


public class SupmuwSprite extends Sprite {
    
    /** A String -> Image mapping used as a library for this sprite's loaded images. Since all Sprites of this kind share the same pool of images, this is static. */
    public static HashMap<String, Image> images = new HashMap<String, Image>();
    
    /** A reference to the current image being used by this Sprite. */
    public Image curImg;
    
    /** A timer for this sprite's animation. */
    public int animTimer = 0;
    
    /** The monster's x velocity */
    public double dx = 0;
    
    /** The monster's y velocity */
    public double dy = 0;
    
    /** The monster's speed */
    public double speed = 2;
    
    /** The mosnter's direction (in angular degrees) */
    public double direction = 90;
    
    /** Flag to tell if the monster is moving */
    public boolean isMoving = false;
    
    /** The monster's radius */
    public double radius = 10;
    
    /** Creates the SupmuwSprite. */
    public SupmuwSprite(int x, int y) {
        super(x,y);
        
        // Start the animTimer at a random number in [0,32), so that it won't look like all the monsters are dancing in sync.
        animTimer = GameMath.rand.nextInt(32);
        
        scale(2.0,2.0);
        radius *= 2;
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
         
         // Add the cropped animation frames to our images map.
         images.put("dance1",frame1);
         images.put("dance2",frame2);
         
         // Add our cropped images to the image loader and wait for them to finish loading.
         il.addImage(frame1);
         il.addImage(frame2);
         il.waitForAll();
    }
    
    
    
   /** moves the monster at its current speed in its current direction. */
   public void move() {
      if(!isMoving)
         return;
      
      rotate(this.angle + 3);
      
      // keep the direction in the range [0, 360)
      normalizeDirection();
      
      // set the velocity according to its direction and speed
      dx = speed * GameMath.cos(direction);
      dy = -1 * speed * GameMath.sin(direction);
      
      // never let dy be 0.
      if(dy == 0)
         dy = 0.1;
      
      // move
      x += dx;
      y += dy;
   }
   
   /** Makes the direction be in the range [0,360) */
   public void normalizeDirection() {
      direction = direction % 360;
      if(direction < 0)
         direction += 360;
   }
   
   
   /** Causes the monster to bounce off a surface with the given normal vector angle. */
   public void bounce(double normal) {
      direction = (normal - (direction + 180)) + normal + GameMath.rand.nextDouble()*20 - 10;
      normalizeDirection();
   }
   
   
   
   /** Performs a circle-box collision between this sprite and another sprite. */
   public boolean collideWith(Sprite other) {
      // collapse our coordinate system so that we only have to work with one quadrant of other's rectangle.
      double rectCX = other.x - other.focalX + other.width/2;
      double rectCY = other.y - other.focalY + other.height/2;
      
      double circDistX = Math.abs(this.x - rectCX);
      double circDistY = Math.abs(this.y - rectCY);
      
      // case 1 : Our circle is too far for a collision to be possible.
      if(circDistX > other.width/2 + this.radius) 
         return false;
      if(circDistY > other.height/2 + this.radius) 
         return false;
      
      // case 2 : Circle is between rect's points on x axis.
      if(circDistX <= other.width/2 && circDistY <= other.height/2 + this.radius)
         return true;
         
      // case 3 : Circle is between rect's points on y axis.
      if(circDistY <= other.height/2 && circDistX <= other.width/2 + this.radius)
         return true;
         
      // case 4 : check dist to corner point
      return (GameMath.sqrDist(circDistX, circDistY, other.width/2, other.height/2) < this.radius*this.radius);
   }
    
    
    /** Performs an iteration of this Sprite's animation logic. */
    public void animate() {
         // "dance1" if animTimer is in [0,16].
         String imgName = "dance1";
         
         // "dance2" if animTimer is in [17,32].
         if(animTimer > 16) 
            imgName = "dance2";
         
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
    
    
    /** Draws the current frame of the monster's animation. */
    public void draw(Graphics2D g) {
         // draw the current image for the Sprite at 0,0 relative to the Sprite's own World coordinates. That last paramater is not important. 
         g.drawImage(curImg,0,0, null);
    }
    
}

