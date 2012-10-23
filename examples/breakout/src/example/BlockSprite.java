package example;

import java.awt.*;
import java.util.HashMap;
import pwnee.sprites.Sprite;
import pwnee.GameMath;
import pwnee.image.*;


public class BlockSprite extends Sprite {
    
    /** A String -> Image mapping used as a library for this sprite's loaded images. Since all Sprites of this kind share the same pool of images, this is static. */
    public static HashMap<String, Image> images = new HashMap<String, Image>();
    
    /** A reference to the current image being used by this Sprite. */
    public Image curImg;
    
    /** A timer for this sprite's animation. */
    public int animTimer = 0;
    
    /** The player's speed */
    public double speed = 3;
    
    
    /** Creates the BlockSprite with a random color. */
    public BlockSprite(int x, int y) {
        super(x,y);
        
        width = 64;
        height = 24;
        
        // Create a randomly colored block image for this particular block.
        Image baseImg = images.get("1");
        Color randColor = new Color(GameMath.rand.nextInt(255), GameMath.rand.nextInt(255), GameMath.rand.nextInt(255));
        curImg = ImageEffects.addColor(baseImg,randColor); 
    }
    
    
    /** Loads the images for this Sprite class. */
    public static void loadImages(ImageLoader il) {
         // Obtain the source image.
         Image srcImg = il.loadFromFile("graphics/block.png");
         
         // Add the cropped animation frames to our images map.
         images.put("1",srcImg);
         
         // Add our cropped images to the image loader and wait for them to finish loading.
         il.addImage(srcImg);
         
         il.waitForAll();
    }

    
    
    /** Draws the current frame of the monster's animation. */
    public void draw(Graphics2D g) {
         // draw the current image for the Sprite at 0,0 relative to the Sprite's own World coordinates. That last paramater is not important. 
         g.drawImage(curImg,0,0, null);
    }
    
}

