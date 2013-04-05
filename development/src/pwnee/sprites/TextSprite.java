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
  
  /** 
   * The maximum width for the text. If -1, then there is no limit. 
   * Otherwise the text will be line-wrapped accordingly.
   */
  public int maxWidth = -1;
  
  public TextSprite(double x, double y, BlitteredFont bf, String txt) {
    super(x,y);
    bfont = bf;
    text = txt;
  }
  
  
  /** Sets the text and applies linewrapping if it can. */
  public String setText(String txt) {
    text = txt;
    lineWrap();
    return text;
  }
  
  
  /** Gets the unscaled dimensions of this sprite's text. */
  public Dimension getDimensions() {
    return bfont.getDimensions(text);
  }
  
  
  /** Applies linewrapping to the current text. */
  public void lineWrap() {
    text = bfont.lineWrap(text, maxWidth);
  }
  
  
  public void draw(Graphics2D g) {
    if(bfont != null)
      bfont.renderString(g, text);
    else
      g.drawString(text, 0, 0);
  }
  

  /** 
   * Reads one big block of text to automatically create linewrapped paragraphs of. 
   * New paragraphs can be explicitly made using '\n\n'.
   * Otherwise, it will fill generate the paragraphs such that the number of
   * lines shown at once doesn't exceed maxLines.
   * @param src       The source wall of text.
   * @param maxLines  Each paragraph shall not exceed this many lines.
   * @return          The resulting paragraph list, for chaining.
   */
  public List<String> makeParagraphs(String src, int maxLines) {
    String textTemp = text;
    paragraphs.clear();
    
    String[] paras = src.split("\n\n");
    for(String para : paras) {
      // line-wrap this "paragraph"
      text = para;
      lineWrap();
      para = text;
      
      String curPara = "";
      int lineLen = para.indexOf('\n') + 1;
      int numLines = 1;
      
      while(lineLen > 0) {
        String line = para.substring(0, lineLen);
        curPara += line;
        numLines++;
        
        if(numLines > maxLines) {
          paragraphs.add(curPara);
          curPara = "";
          numLines = 1;
        }
        
        para = para.substring(lineLen);
        lineLen = para.indexOf('\n') + 1;
      }
      paragraphs.add(curPara + para.substring(0));
    }
    
    text = textTemp;
    return paragraphs;
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
    if(index >=0 && index < paragraphs.size()) {
    //  text = paragraphs.get(index);
    //  paraIndex = index;
      paraIndex = index;
      setText(paragraphs.get(index));
    }
    else {
      text = "Hello. I am ERROR.";
    }
    
    // lineWrap();
    
    return text;
  }
  
  /** 
   * Sets the text to the contents of the next paragraph. 
   * @return    The paragraph's text.
   */
  public String nextPara() {
    paraIndex++;
    setPara(paraIndex);
    
  //  if(paraIndex >=0 && paraIndex < paragraphs.size()) {
  //    text = paragraphs.get(paraIndex);
  //  }
  //  else {
  //    text = "Hello. I am ERROR.";
  //  }
  //  
  //  lineWrap();
    
    return text;
  }
  
  /** 
   * Sets the text to the contents of the previous paragraph. 
   * @return    The paragraph's text.
   */
  public String prevPara() {
    paraIndex--;
    setPara(paraIndex);
    
  //  if(paraIndex >= 0) {
  //    text = paragraphs.get(paraIndex);
  //  }
  //  else {
  //    text = "Hello. I am ERROR.";
  //  }
  //  
  //  lineWrap();
    
    return text;
  }
}
