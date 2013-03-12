package example;

import java.awt.*;
import java.util.ArrayList;
import pwnee.*;
import pwnee.image.ImageLoader;

public class SpritesPanel extends GamePanel {
    
    public BlitteredFont monoFont;
    public BlitteredFont fittedFont;
    
    public TextSprite monoSprite;
    public TextSprite fittedSprite;
    
    public SpritesPanel() {
        super();
        this.setPreferredSize(new Dimension(640, 480));
        
        // Load the BlitteredFonts.
        monoFont = new BlitteredFont(true, 10, 10, 1, 1);
        monoFont.loadImages(this.imgLoader, "graphics/myFont.png");
        
        fittedFont = new BlitteredFont(false, 10, 10, 1, 1);
        fittedFont.spaceWidth = 5;
        fittedFont.loadImages(this.imgLoader, "graphics/myFont.png");
        
        String text = BlitteredFont.testString();
        monoSprite = new TextSprite(10, 10, monoFont, text);
        fittedSprite = new TextSprite(10,200, fittedFont, text);
        
    }
    
    public void logic() {
    }
    
    public void paint(Graphics g) {
        // set the background color to white.
        this.setBackground(new Color(0xFFFFFF));
        
        // clear the panel with the background color.
        super.paint(g);
        
        // cast our Graphics as a Graphics2D, since it has more features and g is actually a Graphics2D anyways.
        Graphics2D g2D = (Graphics2D) g;
        
        // render the blittered Strings.
        monoSprite.render(g2D);
        fittedSprite.render(g2D);
        
        // Set our drawing color to black
        g2D.setColor(new Color(0x000000));
        
        // display the current frame rate.
        g2D.drawString("" + timer.fpsCounter, 400,32);
        
    }
}
