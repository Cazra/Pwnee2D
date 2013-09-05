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
    
    /** A timer for this sprite's animation. */
    public int animTimer = 0;
    
    
    public SupmuwSprite(int x, int y) {
        super(x,y);
        
        // Start the animTimer at a random number in [0,32), so that it won't look like all the monsters are dancing in sync.
        animTimer = GameMath.rand.nextInt(32);
    }
    
    
    /** Loads the images for this Sprite class. */
    public static void loadImages(ImageLoader il) {
         // Obtain the source image.
         Image srcImg = il.load("graphics/SupmuwSprite.png");
         
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

