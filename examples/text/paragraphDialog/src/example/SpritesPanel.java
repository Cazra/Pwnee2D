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
    public TextSprite textSprite;
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
        textSprite = new TextSprite(diaBox.x + 20, diaBox.y + 20, font, "herpity derp");
        textSprite.maxWidth = 240;
        initTextSprite();
    }
    
    
    protected void initTextSprite() {
      textSprite.addPara("Hello! Press ENTER to advance the text.");
      textSprite.addPara("Welcome to the paragraphed dialog text tutorial!");
      textSprite.addPara("I am the Supmuw, a friendly monster from the Creator's game: Wumpus World.");
      textSprite.addPara("You probably saw me in some of the other Pwnee2D game engine examples, ");
      textSprite.addPara("but I've just been so busy before that I didn't have a chance to talk.");
      textSprite.addPara("As you can see, the text in this dialog is rendered using a blittered font.");
      textSprite.addPara("That means that instead of using a vector font like Arial or Times New Roman, ");
      textSprite.addPara("we're using a custom bitmap font to display the characters in the text.");
      textSprite.addPara("You might also notice that the TextSprite and BlitteredFont classes ");
      textSprite.addPara("are smart enough to implement linewrapping!");
      textSprite.addPara("That way you can confine dialogs like this to a small area without worrying");
      textSprite.addPara("about it running outside the dialog box's image ");
      textSprite.addPara("or words getting chopped up if they're at the end of a line!");
      textSprite.addPara("Isn't that great?");
      textSprite.addPara("I hope that you're finding the examples provided with Pwnee to be very helpful!");
      textSprite.addPara("Have a great day, and be sure to check out the creator's game studio website and Deviant Art site!");
      textSprite.addPara("Neonair Games:\n   http://www.neonairgames.net");
      textSprite.addPara("Deviant Art:\n   http://cazra.deviantart.com");
      textSprite.addPara("This concludes the paragraphDialog example.");
      textSprite.addPara("");
      textSprite.setPara(0);
    }
    
    public void logic() {
      // advance the text by pressing ENTER.
      if(keyboard.justPressed(KeyEvent.VK_ENTER) && textSprite.paraIndex < textSprite.paragraphs.size() - 1) {
        textSprite.nextPara();
      }
      
      // hide the text box when the dialog is over.
      if(textSprite.paraIndex == textSprite.paragraphs.size() - 1) {
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
