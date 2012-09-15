package example;

import java.awt.*;
import java.util.HashMap;
import pwnee.sprites.Sprite;
import pwnee.GameMath;
import pwnee.image.*;

/** A simple sprite that displays a large picture. */
public class ArtSprite extends Sprite {
    
    /** A String -> Image mapping used as a library for this sprite's loaded images. Since all Sprites of this kind share the same pool of images, this is static. */
    public static HashMap<String, Image> images = new HashMap<String, Image>();
    
    /** A reference to the current image being used by this Sprite. */
    public Image curImg;
    
    /** Creates the ArtSprite. */
    public ArtSprite(int x, int y) {
        super(x,y);
    }
    
    /** Loads the images for this Sprite class. */
    public static void loadImages(ImageLoader il) {
         // Obtain the source image.
         Image srcImg = il.loadFromFile("graphics/UnicornDevourerOfWorlds.jpg");
         images.put("img",srcImg);
         
         il.addImage(srcImg);
         il.waitForAll();
    }
    
    /** Sets the current image for the sprite */
    public void animate() {
         curImg = images.get("img");
    }
    
    /** Draws the current frame of the Sprite's animation. */
    public void draw(Graphics2D g) {
         // draw the current image for the Sprite at 0,0 relative to the Sprite's own World coordinates. That last paramater is not important. 
         g.drawImage(curImg,0,0, null);
    }
    
}

