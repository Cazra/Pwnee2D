package example;

import java.awt.*;
import java.util.HashMap;
import pwnee.sprites.Sprite;
import pwnee.GameMath;
import pwnee.image.*;


public class PlayerSprite extends Sprite {
    
    /** A String -> Image mapping used as a library for this sprite's loaded images. Since all Sprites of this kind share the same pool of images, this is static. */
    public static HashMap<String, Image> images = new HashMap<String, Image>();
    
    /** A reference to the current image being used by this Sprite. */
    public Image curImg;
    
    /** A timer for this sprite's animation. */
    public int animTimer = 0;
    
    /** The player's speed */
    public double speed = 4;
    
    
    /** Creates the PlayerSprite. */
    public PlayerSprite(int x, int y) {
        super(x,y);
        
        width = 96;
        height = 24;
    }
    
    
    /** Loads the images for this Sprite class. */
    public static void loadImages(ImageLoader il) {
         // Obtain the source image.
         Image srcImg = il.loadFromFile("graphics/player.png");
         
         // Set magenta as the transparent color for the source image.
         srcImg = ImageEffects.setTransparentColor(srcImg, new Color(0xFF00FF));
         
         // crop the source image to get the images for the individual animation frames.
         Image frame1 = ImageEffects.crop(srcImg, 1, 1, 96, 24);
         Image frame2 = ImageEffects.crop(srcImg, 1, 27, 96, 24);
         Image frame3 = ImageEffects.crop(srcImg, 1, 53, 96, 24);
         
         // Add the cropped animation frames to our images map.
         images.put("1",frame1);
         images.put("2",frame2);
         images.put("3",frame3);
         
         // Add our cropped images to the image loader and wait for them to finish loading.
         il.addImage(frame1);
         il.addImage(frame2);
         il.addImage(frame3);
         il.waitForAll();
    }
    
   
   
    
    
    /** Performs an iteration of this Sprite's animation logic. */
    public void animate() {
         String imgName = "1";
         if(animTimer >= 2) 
            imgName = "2";
         if(animTimer >= 4) 
            imgName = "3";
         
         // set the focal point of the monster to be its image's center.
         focalX = width/2;
         focalY = height/2;
         
         // Look up our current animation frame in images using imgName as our key and set it as the current image.
         curImg = images.get(imgName);
         
         // Increment the animation timer and loop it around after 32 frames.
         animTimer++;
         if(animTimer >= 6)
            animTimer = 0;
    }
    
    
    /** Draws the current frame of the monster's animation. */
    public void draw(Graphics2D g) {
         // draw the current image for the Sprite at 0,0 relative to the Sprite's own World coordinates. That last paramater is not important. 
         g.drawImage(curImg,0,0, null);
    }
    
}

