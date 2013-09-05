package example;

import java.awt.*;
import java.util.HashMap;
import pwnee.sprites.Sprite;
import pwnee.GameMath;
import pwnee.image.*;

/** A classic RPG-style dialog box to frame text in. */
public class DiaBoxSprite extends Sprite {
    
    /** A String -> Image mapping used as a library for this sprite's loaded images. Since all Sprites of this kind share the same pool of images, this is static. */
    public static HashMap<String, Image> images = new HashMap<String, Image>();
    
    /** A reference to the current image being used by this Sprite. */
    public Image curImg;
    
    public DiaBoxSprite(int x, int y) {
        super(x,y);
    }
    
    /** Loads the images for this Sprite class. */
    public static void loadImages(ImageLoader il) {
         // Obtain the source image.
         Image srcImg = il.load("graphics/dialogBox.png");
         
         // Set magenta as the transparent color for the source image.
         srcImg = ImageEffects.setTransparentColor(srcImg, new Color(0xFF00FF));
         
         // Add the cropped animation frames to our images map.
         images.put("default",srcImg);
         
         // Add our cropped images to the image loader and wait for them to finish loading.
         il.addImage(srcImg);
         il.waitForAll();
    }
    
    
    
    /** Performs an iteration of this Sprite's animation logic. */
    public void animate() {
         // Look up our current animation frame in images using imgName as our key and set it as the current image.
         curImg = images.get("default");
    }

    
    /** Draws the current frame of the monster's animation. */
    public void draw(Graphics2D g) {
         // draw the current image for the Sprite at 0,0 relative to the Sprite's own World coordinates. That last paramater is not important. 
         g.drawImage(curImg,0,0, null);
    }
    
}

