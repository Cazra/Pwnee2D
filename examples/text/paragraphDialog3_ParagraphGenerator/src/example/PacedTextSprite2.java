package example;

import java.awt.*;
import java.util.List;
import pwnee.sprites.TextSprite;
import pwnee.text.BlitteredFont;

/** 
 * A TextSprite that displays its text gradually, letter by letter, often 
 * used in retro game dialogs.
 */
public class PacedTextSprite2 extends TextSprite {
  
  /** Only the current text up to this index - 1 will be shown. */
  public int curLength;
  
  /** Helps calculate curLength. */
  protected int _frames;
  
  /** The frame delay between displaying characters in our text. */
  public int slowness;
  
  public PacedTextSprite2(double x, double y, BlitteredFont bf, String txt) {
    super(x,y,bf,txt);
    curLength = 0;
    _frames = 0;
    slowness = 0;
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
      System.out.println(para + "\n");
      
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
