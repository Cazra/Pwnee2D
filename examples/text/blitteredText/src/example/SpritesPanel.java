package example;

import java.awt.*;
import java.util.ArrayList;
import pwnee.*;
import pwnee.image.ImageLoader;
import pwnee.sprites.TextSprite;
import pwnee.text.BlitteredFont;

public class SpritesPanel extends GamePanel {
    
    public BlitteredFont monoFont;
    public BlitteredFont fittedFont;
    
    public TextSprite monoSprite;
    public TextSprite fittedSprite;
    
    public TextSprite dimsSprite;
    
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
        monoSprite = new TextSprite(10, 10, monoFont, "Mono-spaced font: \n" + text);
        fittedSprite = new TextSprite(250,100, fittedFont, "Fitted font: \n" + text);
        
        dimsSprite = new TextSprite(250,50, fittedFont, "");
        
        monoSprite.maxWidth = 160;
        fittedSprite.maxWidth = 160;
        
        monoSprite.lineWrap();
        fittedSprite.lineWrap();
    }
    
    public void logic() {
      Dimension monoDim = monoSprite.getDimensions();
      Dimension fittedDim = fittedSprite.getDimensions();
      
      dimsSprite.text = "Mono size: (" + monoDim.width + ", " + monoDim.height + ")\n" + 
                        "Fitted size: (" + fittedDim.width + ", " + fittedDim.height + ")\n";
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
        dimsSprite.render(g2D);
        
        // Draw borders around the text sprites to demonstrate that they 
        // are obeying their line-wrapping.
        Dimension monoDim = monoSprite.getDimensions();
        Dimension fittedDim = fittedSprite.getDimensions();
        
        g2D.drawRect((int) monoSprite.x, (int) monoSprite.y, monoSprite.maxWidth, monoDim.height);
        g2D.drawRect((int) fittedSprite.x, (int) fittedSprite.y, fittedSprite.maxWidth, fittedDim.height);
        
        // Set our drawing color to black
        g2D.setColor(new Color(0x000000));
        
        // display the current frame rate.
        g2D.drawString("" + timer.fpsCounter, 400,32);
        
    }
}
