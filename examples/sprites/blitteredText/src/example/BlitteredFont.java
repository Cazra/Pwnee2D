package example;

import java.awt.*;
import java.awt.image.*;
import java.util.HashMap;
import pwnee.image.*;

/**
 * A "font" used to display blittered text. (The characters are drawn using a 
 * source image instead of a font vector). 
 *
 * This class is meant to be used by a TextSprite to do its rendering.
 */
public class BlitteredFont {
    
  /** A mapping of the ASCII characters in the blittered text to their images. */
  public HashMap<Character, Image> images = new HashMap<Character, Image>();
  
  /** A mapping of ASCII characters to their width (used only if the characters aren't monospaced). */
  public HashMap<Character, Integer> charWidth = new HashMap<Character, Integer>();
  
  /** Whether all the characters' images are the same width. */
  public boolean isMonospaced;
  
  /** The monospaced width of the characters. */
  public int monoWidth;
  
  /** The height of the characters. */
  public int charHeight;
  
  /** The amount of horizontal space between characters on the same row. 0 by default. */
  public int hPadding = 0;
  
  /** The amount of vertical space between rows of characters. 0 by default. */
  public int vPadding = 0;
  
  /** The width of spaces for this font. */
  public int spaceWidth;
  
  /** 
   * Loads a blittered font from a source image file with the specified metrics.
   * @param mono      Whether the blittered font will be monospaced.
   * @param cWid      The monospaced width of the characters. (The width of their frames in the source image)
   * @param cHei      The height of the characters. (The height of their frames in the source image)
   * @param hPad      The horizontal padding amount.
   * @param vPad      The vertical padding amount.
   */
  public BlitteredFont(boolean mono, int cWid, int cHei, int hPad, int vPad) {
    isMonospaced = mono;
    monoWidth = cWid;
    charHeight = cHei;
    hPadding = hPad;
    vPadding = vPad;
    spaceWidth = cWid;
  }
  
  public BlitteredFont(boolean mono, int cWid, int cHei) {
    this(mono, cWid, cHei, 0, 0);
  }
  
  public BlitteredFont(int cWid, int cHei) {
    this(true, cWid, cHei, 0, 0);
  }
  
  
  
  /** 
   * Populates images with the character image data from the given source image file. 
   * In the default implementation, each character frame is assumed to have a 1-pixel border
   * (so each frame is 2 pixels apart). ASCII characters 32-47 are on the 1st row, 48-63 are on the 2nd,
   * 64-79 are on the 3rd, 80-95 are on the 4th, 96-111 are on the 5th, and 112-127 are on the 6th. 
   * You can override this method if you need to accomodate a different subset of the ASCII characters
   * or if you have other special needs for the characters' images.
   *
   * This method must be called before the font can be used to render any Strings.
   *
   * @param il        The ImageLoader used to wait on the character images to load.
   * @param imgPath   The path to the image containing the images for the characters used by the font.
   * @param tColor    The transparent color in the source image.
   */
  public void loadImages(ImageLoader il, String imgPath, Color tColor) {
    // Obtain the source image.
    Image srcImg = il.loadFromFile(imgPath);
     
    // Set magenta as the transparent color for the source image.
    srcImg = ImageEffects.setTransparentColor(srcImg, tColor);
    
    // crop each character frame and load it into images.
    for(int c = 32; c <= 127; c++) {
      int i = (c-32) % 16;
      int j = (c-32) / 16;
      int x = 1 + i*(monoWidth+2);
      int y = 1 + j*(charHeight+2);
      
      Image frame = ImageEffects.crop(srcImg,x,y, monoWidth, charHeight);
      
      // If the font isn't monospaced, crop the image so that the the character fits
      // its frame's width exactly.
      if(!isMonospaced) {
        frame = fitCharWidth(frame);
        int w = frame.getWidth(null);
        while(w == -1) {
          try {
            Thread.sleep(1);
            w = frame.getWidth(null);
          }
          catch(Exception e) {
            w = monoWidth;
          }
        }
        charWidth.put((char) c, w);
      }
      
      images.put((char) c, frame);
      il.addImage(frame);
    }
       
    il.waitForAll();
  }
  
  public void loadImages(ImageLoader il, String imgPath) {
    loadImages(il, imgPath, new Color(0xFF00FF));
  }
  
  
  /** Crops the source image so that its width is fitted to its nontransparent part. */
  protected Image fitCharWidth(Image src) {
    int left = 0;
    int right = monoWidth - 1;
    
    // obtain the pixel array for the frame.
    PixelGrabber pg = new PixelGrabber(src, 0, 0, -1, -1, false);
    try {
      pg.grabPixels();
    }
    catch(Exception ex) {
      System.out.println(ex.getMessage());
      ex.printStackTrace();
    }
    int pixWidth = pg.getWidth();
    int pixHeight = pg.getHeight();
    int[][] pixels = to2DIntArray((int[]) pg.getPixels(), pixWidth, pixHeight);
    
    // find the left border.
    for(int x = 0; x < pixWidth; x++) {
      for(int y = 0; y < pixHeight; y++) {
        int pixel = pixels[x][y];
        if(((pixel >> 24) & 0x000000FF) > 0) {
          left = x;
          x = pixWidth;
          y = pixHeight;
        }
      }
    }
    
    // find the right border.
    for(int x = pixWidth-1; x >= 0; x--) {
      for(int y = 0; y < pixHeight; y++) {
        int pixel = pixels[x][y];
        if(((pixel >> 24) & 0x000000FF) > 0) {
          right = x;
          x = -1;
          y = pixHeight;
        }
      }
    }
    
    return ImageEffects.crop(src, left, 0, right-left + 1, charHeight);
  }
  
  
  /** Converts a 1D int array into a 2D int array. */
  protected int[][] to2DIntArray(int[] src, int w, int h) {
    int[][] result = new int[w][h];
    
    for(int i = 0; i < w*h; i++) {
      int x = i % w;
      int y = i / w;
      
      result[x][y] = src[i];
    }
    
    return result;
  }

  
  
  /** 
   * Renders a String using this BlitteredFont.
   * Override this if you have special rendering needs.
   * @param g   The graphics context used to draw the String.
   * @param txt The String being drawn.
   */
  public void renderString(Graphics2D g, String txt) {
    char[] chars = txt.toCharArray();
    int cursorX = 0;
    int cursorY = 0;
    
    for(char c : chars) {
      if(c == '\n') {
        cursorX = 0;
        cursorY += charHeight + vPadding;
      }
      else if(c == ' ') {
        cursorX += spaceWidth + hPadding;
      }
      else {
        try {
          Image cImg = images.get(c);
          g.drawImage(cImg, cursorX, cursorY, null);
          if(isMonospaced) {
            cursorX += monoWidth;
          }
          else {
            cursorX += charWidth.get(c);
          }
          cursorX += hPadding;
        }
        catch(Exception e) {
          // We probably got here by trying to get the image for a 
          // character not used as a key in images.
        }
      }
    }
  }
  
  
  
  
  /** 
   * @return  ASCII characters 32-127 followed by the string 
   *          "The quick brown fox
   *          jumps over the
   *          lazy dog."
   * 
   */
  public static String testString() {
    String testString = "";
    for(int i = 32; i <= 127; i++) {
      testString += (char) i;
      if(i % 16 == 15) {
        testString += "\n";
      }
    }
    testString += "\n";
    
    String brownFox = "The quick brown fox\njumps over the\nlazy dog.";
    testString += brownFox  + "\n" + brownFox.toUpperCase();
    
    return testString;
  }
}

