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
    
    public String effect = "none";
    
    /** Creates the SupmuwSprite with a random velocity. */
    public SupmuwSprite(int x, int y) {
        super(x,y);
        
        // The image we're using for the Sprite in this example is pretty tiny. So let's scale it up a bit.
        this.scale(2,2);
        
        // Start the animTimer at a random number in [0,32), so that it won't look like all the monsters are dancing in sync.
        animTimer = GameMath.rand.nextInt(32);
    }
    
    
    /** Loads the images for this Sprite class. */
    public static void loadImages(ImageLoader il) {
         // Obtain the source image. (NO IMAGE EFFECTS)
         Image srcImg = il.loadFromFile("graphics/SupmuwSprite.png");
         images.put("none1",srcImg);
         images.put("none2",srcImg);
         il.addImage(srcImg);
         
         Image frame1;
         Image frame2;
         
         // CROP EFFECT : 
         
         // crop the source image to get the images for the individual animation frames.
         frame1 = ImageEffects.crop(srcImg, 1, 1, 24, 24);
         frame2 = ImageEffects.crop(srcImg, 27, 1, 24, 24);
         
         // Add the cropped animation frames to our images map.
         images.put("crop1",frame1);
         images.put("crop2",frame2);
         
         il.addImage(frame1);
         il.addImage(frame2);
         
         
         // TRANSPARENT COLOR EFFECT : Set magenta as the transparent color for the source image.
         Image srcImgTrans = ImageEffects.setTransparentColor(srcImg, new Color(0xFF00FF));
         
         // crop the source image to get the images for the individual animation frames.
         frame1 = ImageEffects.crop(srcImgTrans, 1, 1, 24, 24);
         frame2 = ImageEffects.crop(srcImgTrans, 27, 1, 24, 24);
         
         // Add the cropped animation frames to our images map.
         images.put("trans1",frame1);
         images.put("trans2",frame2);
         
         il.addImage(frame1);
         il.addImage(frame2);
         
         
         // ADD COLOR EFFECT : add some red to the sprite.
         // crop the source image to get the images for the individual animation frames.
         frame1 = ImageEffects.crop(srcImgTrans, 1, 1, 24, 24);
         frame1 = ImageEffects.addColor(frame1, new Color(0xFF0000));
         
         frame2 = ImageEffects.crop(srcImgTrans, 27, 1, 24, 24);
         frame2 = ImageEffects.addColor(frame2, new Color(0xFF0000));
         
         // Add the cropped animation frames to our images map.
         images.put("add1",frame1);
         images.put("add2",frame2);
         
         il.addImage(frame1);
         il.addImage(frame2);
         
         
         // INVERT COLOR EFFECT : invert the Sprite's colors.
         // crop the source image to get the images for the individual animation frames.
         frame1 = ImageEffects.crop(srcImgTrans, 1, 1, 24, 24);
         frame1 = ImageEffects.invert(frame1);
         
         frame2 = ImageEffects.crop(srcImgTrans, 27, 1, 24, 24);
         frame2 = ImageEffects.invert(frame2);
         
         // Add the cropped animation frames to our images map.
         images.put("invert1",frame1);
         images.put("invert2",frame2);
         
         il.addImage(frame1);
         il.addImage(frame2);
         
         
         
         // ALPHA MAP EFFECT : Apply an alpha map to the sprite.
         // crop the source image to get the images for the individual animation frames.
         frame1 = ImageEffects.crop(srcImgTrans, 1, 1, 24, 24);
         Image frame1alpha = ImageEffects.crop(srcImgTrans, 53, 1, 24, 24);
         frame1 = ImageEffects.applyAlphaMap(frame1, frame1alpha, 24, 24);
         
         frame2 = ImageEffects.crop(srcImgTrans, 27, 1, 24, 24);
         Image frame2alpha = ImageEffects.crop(srcImgTrans, 79, 1, 24, 24);
         frame2 = ImageEffects.applyAlphaMap(frame2, frame2alpha, 24, 24);
         
         // Add the cropped animation frames to our images map.
         images.put("alpha1",frame1);
         images.put("alpha2",frame2);
         
         il.addImage(frame1);
         il.addImage(frame2);
         
         // wait for the images to finish loading.
         il.waitForAll();
    }
    
    
    
    /** Performs an iteration of this Sprite's animation logic. */
    public void animate() {
         // frame 1 if animTimer is in [0,16].
         String imgName = effect + "1";
         
         // frame 2 if animTimer is in [17,32].
         if(animTimer > 16) 
            imgName = effect + "2";
         
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
         g.drawString("Effect: " + effect, 105, 24);
    }
    
}

