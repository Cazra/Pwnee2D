package pwnee.sprites;

import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import pwnee.image.*;
import pwnee.text.BlitteredFont;

/** 
 * A sprite used to render text. It is meant to use a BlitteredFont, but 
 * will use the graphics context's current font if one isn't provided.
 * It also supports paragraph-based text where Strings are displayed 
 * iteratively, allowing for dialog box text like those found in many RPG games.
 */
public class TextSprite extends Sprite {
  /** 
   * The blittered font used to render the text. If this is null, 
   * the graphics context's font will be used.
   */
  public BlitteredFont bfont;
  
  /** The text that will be rendered with this. */
  public String text = "";
  
  /** The paragraph list. */
  public List<String> paragraphs = new ArrayList<String>();
  
  /** The current index into the paragraph list. */
  public int paraIndex = 0;
  
  public TextSprite(double x, double y, BlitteredFont bf, String txt) {
    super(x,y);
    bfont = bf;
    text = txt;
  }
  
  
  public void draw(Graphics2D g) {
    if(bfont != null)
      bfont.renderString(g, text);
    else
      g.drawString(text, 0, 0);
  }
  
  /** 
   * Appends a paragraph to the paragraph list. 
   * @param para  The paragraph being appended.
   * @return      para, for chaining.
   */
  public String addPara(String para) {
    paragraphs.add(para);
    return para;
  }
  
  /** 
   * Sets the text to the contents of a specific paragraph. 
   * @param index   The index of the paragraph to use.
   * @return        The paragraph's text.
   */
  public String setPara(int index) {
    try {
      text = paragraphs.get(index);
      paraIndex = index;
    }
    catch(Exception e) {
      text = "Hello. I am ERROR.";
    }
    return text;
  }
  
  /** 
   * Sets the text to the contents of the next paragraph. 
   * @return    The paragraph's text.
   */
  public String nextPara() {
    paraIndex++;
    
    if(paraIndex < paragraphs.size()) {
      text = paragraphs.get(paraIndex);
    }
    else {
      text = "Hello. I am ERROR.";
    }
    return text;
  }
  
  /** 
   * Sets the text to the contents of the previous paragraph. 
   * @return    The paragraph's text.
   */
  public String prevPara() {
    paraIndex--;
    
    if(paraIndex >= 0) {
      text = paragraphs.get(paraIndex);
    }
    else {
      text = "Hello. I am ERROR.";
    }
    return text;
  }
}
