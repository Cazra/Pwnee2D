package pwnee.text;

import java.awt.*;
import java.awt.image.*;
import java.util.HashMap;
import pwnee.image.*;

/**
 * A "font" used to display blittered text. (The characters are drawn using a 
 * source image instead of a font vector). 
 *
 * Instances of this class is meant to be used by one or more TextSprites 
 * to do their rendering.
 */
public class BlitteredFont {
    
  /** A mapping of the ASCII characters in the blittered text to their images. */
  public HashMap<String, Image> images = new HashMap<String, Image>();
  
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
  
  /** The character for identifying the start points of escape sequences used for formatting. */
  public char esc = '\u001B';
  
  /** The character for identifying the end points of escape sequences used for formatting. */
  public char escEnd = '\u001B';
  
  /** Additive color formatting. */
  protected Color addColor; 
  
  /** Subtractive color formatting. */
  protected Color subColor;
  
  /** Bold formatting */
  protected boolean isBold = false;
  
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
   * The frames containing the character images are all assumed to be the same size: monoWidth*charHeight.
   *
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
      
      images.put("" + (char) c, frame);
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
    addColor = null;
    subColor = null;
    
    for(int i = 0; i < chars.length; i++) {
      char c = chars[i];
      if(c == '\n') {
        cursorX = 0;
        cursorY += charHeight + vPadding;
      }
      else if(c == ' ') {
        cursorX += spaceWidth + hPadding;
      }
      else if(c == esc) {
        i += handleEscSequence(chars, i);
      }
      else {
        try {
          renderChar(g, c, cursorX, cursorY);
          
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
   * Renders a character using this BlitteredFont at some cursor position. 
   * Special formatting may be applied here.
   * @param g         The graphics context.
   * @param c         The character to render.
   * @param cursorX   The cursor X position to render the character at relative to the graphics context's origin.
   *                  This is the character's left border position.
   * @param cursorY   The cursor Y position to render the character at relative to the graphics context's origin.
   *                  This is the character's top border position.
   */
  public boolean renderChar(Graphics2D g, char c, int cursorX, int cursorY) {
    Image srcImg = images.get("" + c);
    if(srcImg == null) 
      return false;
    
    // build the format suffix for our image caching key.
    String formatStr = "";
    if(addColor != null) {
      formatStr += "ac" + addColor.getRGB();
    }
    if(subColor != null) {
      formatStr += "sc" + subColor.getRGB();
    }
    
    
    // get the formatted character image. Either compute it if it doesn't exist, or use the one in the cache.
    Image cImg = images.get(c + formatStr);
    if(cImg == null) {
      cImg = srcImg;
      
      // additive color formatting
      if(addColor != null) {
        cImg = ImageEffects.addColor(cImg, addColor);
      }
      
      // additive color formatting
      if(subColor != null) {
        cImg = ImageEffects.subColor(cImg, subColor);
      }
      
      images.put(c + formatStr, cImg);
    }
    
    
    g.drawImage(cImg, cursorX, cursorY, null);
    if(isBold) {
      g.drawImage(cImg, cursorX+1, cursorY, null);
      g.drawImage(cImg, cursorX, cursorY-1, null);
      g.drawImage(cImg, cursorX+1, cursorY-1, null);
    }
    
    return true;
  }
  
  
  /** 
   * Handler for escape sequences. 
   * @param chars   The source character array
   * @param i       The current index into chars.
   * @return        The length of the escape sequence + 1. 
   *                0, if the escape sequence is incomplete.
   */
  protected int handleEscSequence(char[] chars, int i) {
    i++;
    String escSeq = "";
    boolean hasEnd = false;
    
    while(i < chars.length && !hasEnd) {
      if(chars[i] == escEnd)
        hasEnd = true;
      else
        escSeq += chars[i];
      
      i++;
    }
    
    // We found a complete escape sequence. Try to interpret it.
    if(hasEnd) {
      interpretEscSequence(escSeq);
      return escSeq.length()+1;
    }
    
    return 0;
  }
  
  
  /** 
   * Interprets and executes an escape character sequence. 
   * Some escape sequences may affec the position of the cursor. 
   * The cursor offset caused by the escape sequence is returned.
   * @param escSeq    The escape character sequence.
   * @return          The cursor offset caused by the escape sequence (usually +0,+0).
   */
  protected Dimension interpretEscSequence(String escSeq) {
    escSeq = escSeq.toLowerCase();
    
    int offX = 0;
    int offY = 0;
    
    // additive coloring : ac[HEX_COLOR] e.g. ac0xFF0099
    if(escSeq.startsWith("ac0x")) {
      try {
        escSeq = escSeq.substring(4);
        int rgb = Integer.parseInt(escSeq, 16);
        addColor = new Color(rgb);
      }
      catch(Exception e) {
      }
    }
    // reset additive coloring : acr
    else if(escSeq.equals("acr")) {
      addColor = null;
    }
    // subtractive coloring : sc[HEX_COLOR] e.g. ac0xFF0099
    else if(escSeq.startsWith("sc0x")) {
      try {
        escSeq = escSeq.substring(4);
        int rgb = Integer.parseInt(escSeq, 16);
        subColor = new Color(rgb);
      }
      catch(Exception e) {
      }
    }
    // reset subtractive coloring : scr
    else if(escSeq.equals("scr")) {
      subColor = null;
    }
    // bold text on
    else if(escSeq.equals("b")) {
      isBold = true;
    }
    // bold text off
    else if(escSeq.equals("br")) {
      isBold = false;
    }
    
    return new Dimension(offX, offY);
  }
  
  
  /** 
   * Determines the dimensions of a block of text rendered with this.
   * @param txt The String to get the dimensions for.
   * @return    The rendered dimensions of txt.
   */
  public Dimension getDimensions(String txt) {
    char[] chars = txt.toCharArray();
    int cursorX = 0;
    int cursorY = 0;
    
    int w = 0;
    int h = charHeight;
    
    for(int i = 0; i < chars.length; i++) {
      char c = chars[i];
      if(c == '\n') {
        cursorX = 0;
        cursorY += charHeight + vPadding;
      }
      else if(c == ' ') {
        cursorX += spaceWidth + hPadding;
      }
      else if(c == esc) {
        i += handleEscSequence(chars, i);
      }
      else {
        try {
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
      
      if(cursorX > w)
        w = cursorX;
      if(cursorY + charHeight > h)
        h = cursorY + charHeight;
    }
    
    return new Dimension(w, h);
  }
  
  
  
  
  
  
  
  /** 
   * @return  ASCII characters 32-127 followed by the string 
   *          "The quick brown fox
   *          jumps over the
   *          lazy dog."
   * 
   */
  public static String testString() {
    char esc = '\u001B';
    char endEsc = esc;
    
    // tags for making something brown using additive coloring.
    String tag1Start = esc + "ac0xAA0000" + endEsc;
    String tag1End = esc + "acr" + endEsc;
    
    // tags for making something turquoise by making it white, then using subtractive coloring.
    String tag2Start = esc + "ac0xFFFFFF" + endEsc + esc + "sc0xFF4444" + endEsc;
    String tag2End = esc + "scr" + endEsc + esc + "acr" + endEsc;
    
    String boldStart = esc + "b" + endEsc;
    String boldEnd = esc + "br" + endEsc;
    
    String testString = "";
    for(int i = 32; i <= 127; i++) {
      testString += (char) i;
      if(i % 16 == 15) {
        testString += "\n";
      }
    }
    testString += "\n";

    String brownFox = "The " + boldStart + "quick " + boldEnd + tag1Start + "brown fox" + tag1End + "\njumps over the\n" + tag2Start + "lazy" + tag2End + " dog.";
    testString += brownFox  + "\n" + brownFox.toUpperCase();
    
    return testString;
  }
}

