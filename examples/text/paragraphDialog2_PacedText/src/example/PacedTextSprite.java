package example;

import java.awt.*;
import pwnee.sprites.TextSprite;
import pwnee.text.BlitteredFont;

/** 
 * A TextSprite that displays its text gradually, letter by letter, often 
 * used in retro game dialogs.
 */
public class PacedTextSprite extends TextSprite {
  
  /** Only the current text up to this index - 1 will be shown. */
  public int curLength;
  
  /** Helps calculate curLength. */
  protected int _frames;
  
  /** The frame delay between displaying characters in our text. */
  public int slowness;
  
  public PacedTextSprite(double x, double y, BlitteredFont bf, String txt) {
    super(x,y,bf,txt);
    curLength = 0;
    _frames = 0;
    slowness = 0;
  }
  
  /** Sets the text, does linewrapping, and resets curLength. */
  public String setText(String txt) {
    String result = super.setText(txt);
    _frames = 0;
    curLength = 0;
    return result;
  }
  
  /** Gradually makes more text be displayed. */
  public void advOne() {
    if(slowness <= 0) {
      curLength = text.length();
    }
    else if(curLength < text.length()) {
      _frames++;
      curLength = _frames/slowness;
    }
  }
  
  /** 
   * Makes all the of text visible. This is good for if the player wants to 
   * hurry through the dialog. 
   */
  public void advAll() {
    curLength = text.length();
  }
  
  public void draw(Graphics2D g) {
    String temp = text;
    text = text.substring(0, curLength);
    super.draw(g);
    
    text = temp;
  }
  
  
}
