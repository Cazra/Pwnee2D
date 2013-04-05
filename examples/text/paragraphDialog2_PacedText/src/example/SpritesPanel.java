package example;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import pwnee.*;
import pwnee.image.ImageLoader;
import pwnee.sprites.TextSprite;
import pwnee.text.BlitteredFont;

public class SpritesPanel extends GamePanel {
    
    public BlitteredFont font;
    public PacedTextSprite textSprite;
    public DiaBoxSprite diaBox;
    public SupmuwSprite supmuw;
    
    
    public SpritesPanel() {
        super();
        this.setPreferredSize(new Dimension(640, 480));
        
        // Load the BlitteredFonts.
        font = new BlitteredFont(true, 10, 10, 1, 1);
        font.loadImages(this.imgLoader, "graphics/finalFontasy.png");
        
        // Load the other sprites.
        diaBox = new DiaBoxSprite(20, 120);
        diaBox.loadImages(this.imgLoader);
        
        supmuw = new SupmuwSprite(160, 100);
        supmuw.loadImages(this.imgLoader);
        
        // load the TextSprite.
        textSprite = new PacedTextSprite(diaBox.x + 20, diaBox.y + 20, font, "herpity derp");
        textSprite.slowness = 5;
        textSprite.maxWidth = 240;
        initTextSprite();
    }
    
    
    protected void initTextSprite() {
      textSprite.addPara("Hello! Press ENTER to advance the text.");
      textSprite.addPara("Welcome to the second paragraphed dialog text tutorial!");
      textSprite.addPara("It is good to see you again, my friend!");
      textSprite.addPara("Do you notice something different about the dialog in this tutorial? ");
      textSprite.addPara("Instead of appearing all at once, the text in this example is displayed gradually, ");
      textSprite.addPara("very much like dialog text used in many retro games.");
      textSprite.addPara("This is done using a derivative class of TextSprite that comes with this example: PacedTextSprite. ");
      textSprite.addPara("It also lets you control how fast or slow the text will be displayed by modifying its 'slowness' variable.");
      textSprite.addPara("Currently it is set to " + textSprite.slowness +".");
      textSprite.addPara("Trying changing its slowness in SpritesPanel.java's constructor to change how quickly (or slowly) text appears.");
      textSprite.addPara("Setting the slowness to 0 or lower will cause the text to appear all at once.");
      textSprite.addPara("This concludes the paragraphDialog example.");
      textSprite.setPara(0);
    }
    
    public void logic() {
      textSprite.advOne();
      
      // advance the text by pressing ENTER.
      if(keyboard.justPressed(KeyEvent.VK_ENTER) && textSprite.paraIndex < textSprite.paragraphs.size()) {
        if(textSprite.curLength < textSprite.text.length()) {
          textSprite.advAll();
        }
        else {
          textSprite.nextPara();
        }
      }
      
      // hide the text box when the dialog is over.
      if(textSprite.paraIndex == textSprite.paragraphs.size()) {
        textSprite.isVisible = false;
        diaBox.isVisible = false;
      }
      
      supmuw.animate();
      diaBox.animate();
    }
    
    public void paint(Graphics g) {
        // set the background color to white.
        this.setBackground(new Color(0xFFFFFF));
        
        // clear the panel with the background color.
        super.paint(g);
        
        // cast our Graphics as a Graphics2D, since it has more features and g is actually a Graphics2D anyways.
        Graphics2D g2D = (Graphics2D) g;
        
        // enlarge our display to x2 size.
        g2D.scale(2.0, 2.0);
        
        // render the sprites.
        supmuw.render(g2D);
        diaBox.render(g2D);
        textSprite.render(g2D);
        
        // Set our drawing color to black
        g2D.setColor(new Color(0x000000));
        
        // display the current frame rate.
        g2D.drawString("" + timer.fpsCounter, 400,32);
        
    }
}
