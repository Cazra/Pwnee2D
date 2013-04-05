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
        textSprite.slowness = 2;
        textSprite.maxWidth = 240;
        initTextSprite();
    }
    
    
    protected void initTextSprite() {
      String dialog =   "Hello! Press ENTER to advance the text." + 
                        "\n\n" + 
                        "Welcome to the third paragraphed dialog tutorial!" +
                        "In this tutorial, we have the same paced dialog " + 
                        "from the second tutorial, but there is a new feature " + 
                        "that has been added to our custom TextSprite class " + 
                        "which makes it much easier for developers to automate " +
                        "the creation of text dialog without having to worry about " + 
                        "having too many lines shown at once in a single paragraph " + 
                        "of dialog." +
                        "\n\n" + 
                        "This is done with the new makeParagraphs method in our " + 
                        "custom TextSprite! This isn't really something the " +
                        "players would notice at all, but it will definitely " + 
                        "make your writers' jobs easier!" +
                        "\n\n" + 
                        "Check out the initTextSprite method in SpritesPanel.java " +
                        "and the makeParagraphs method in TextSprite.java " + 
                        "to see how this convenient feature is used. Perhaps, you " + 
                        "would like to play around with it a bit by changing the " + 
                        "maximum number of lines to display per paragraph?" + 
                        "\n\n" + 
                        "This concludes the paragraphDialog example.";
      int maxLines = 3;
      
      textSprite.makeParagraphs(dialog, maxLines);
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
