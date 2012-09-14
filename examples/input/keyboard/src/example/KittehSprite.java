package example;

import java.awt.*;
import java.util.HashMap;
import pwnee.sprites.Sprite;
import pwnee.GameMath;
import pwnee.image.*;


public class KittehSprite extends Sprite {
    
    /** A String -> Image mapping used as a library for this sprite's loaded images. Since all Sprites of this kind share the same pool of images, this is static. */
    public static HashMap<String, Image> images = new HashMap<String, Image>();
    
    /** A reference to the current image being used by this Sprite. */
    public Image curImg;
    
    /** The speed at which the kitteh moves. */
    public double speed = 2;
    
    /** A timer for this sprite's animation. */
    public int animTimer = 0;
    
    /** The direction of the Sprite's animation. */
    public String direction = "south";
    
    /** Creates the KittehSprite. */
    public KittehSprite(int x, int y) {
        super(x,y);
        
        // The image we're using for the Sprite in this example is pretty tiny. So let's scale it up a bit.
        this.scale(2,2);
    }
    
    
    /** Loads the images for this Sprite class. */
    public static void loadImages(ImageLoader il) {
         // Obtain the source image.
         Image srcImg = il.loadFromFile("graphics/KittehSprite.png");
         
         // Set magenta as the transparent color for the source image.
         srcImg = ImageEffects.setTransparentColor(srcImg, new Color(0xFF00FF));
         
         // crop the source image to get the images for the individual animation frames.
         Image east1 = ImageEffects.crop(srcImg, 1, 1, 24, 24);
         Image east2 = ImageEffects.crop(srcImg, 27, 1, 24, 24);
         
         Image north1 = ImageEffects.crop(srcImg, 53, 1, 24, 24);
         Image north2 = ImageEffects.crop(srcImg, 79, 1, 24, 24);
         
         Image west1 = ImageEffects.crop(srcImg, 105, 1, 24, 24);
         Image west2 = ImageEffects.crop(srcImg, 131, 1, 24, 24);
         
         Image south1 = ImageEffects.crop(srcImg, 157, 1, 24, 24);
         Image south2 = ImageEffects.crop(srcImg, 183, 1, 24, 24);
         
         // Add the cropped animation frames to our images map.
         images.put("east1",east1);
         images.put("east2",east2);
         
         images.put("north1",north1);
         images.put("north2",north2);
         
         images.put("west1",west1);
         images.put("west2",west2);
         
         images.put("south1",south1);
         images.put("south2",south2);
         
         // Add our cropped images to the image loader and wait for them to finish loading.
         il.addImage(east1);
         il.addImage(east2);
         il.addImage(north1);
         il.addImage(north2);
         il.addImage(west1);
         il.addImage(west2);
         il.addImage(south1);
         il.addImage(south2);
         il.waitForAll();
    }
    
    
    
    /** Performs an iteration of this Sprite's animation logic. */
    public void animate() {
         String imgName = direction;
         
         // "1" if animTimer is in [0,16].
         int frameNum = 1;
         
         // "2" if animTimer is in [17,32].
         if(animTimer > 16) 
            frameNum = 2;
         
         // append the frame number to our direction to form our complete imgName.
         imgName += frameNum;
         
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
    
    
    /** Sets the direction for this Sprite's animation. */
    public void setDirection(char dir) {
         if(dir == 'n' || dir == 'N')
            direction = "north";
         if(dir == 'w' || dir == 'W')
            direction = "west";
         if(dir == 's' || dir == 'S')
            direction = "south";
         if(dir == 'e' || dir == 'E')
            direction = "east";
    }
    
    /** Makes the Sprite turn counterclockwise */
    public void turnAround() {
        if(direction == "north")
            direction = "west";
        else if(direction == "west")
            direction = "south";
        else if(direction == "south")
            direction = "east";
        else
            direction = "north";
    }
    
    
    /** Draws the current frame of the Sprite's animation. */
    public void draw(Graphics2D g) {
         // draw the current image for the Sprite at 0,0 relative to the Sprite's own World coordinates. That last paramater is not important. 
         g.drawImage(curImg,0,0, null);
    }
    
}

