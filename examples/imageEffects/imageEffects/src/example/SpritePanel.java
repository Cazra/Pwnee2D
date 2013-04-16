package example;

import java.awt.*;
import java.util.ArrayList;
import pwnee.*;

public class SpritePanel extends GamePanel {
    
    public ArrayList<SupmuwSprite> sprites = new ArrayList<SupmuwSprite>();
    
    public SpritePanel() {
        super();
        this.setPreferredSize(new Dimension(640, 480));
        
        // load the images for our Sprite
        SupmuwSprite.loadImages(imgLoader);
        
        // Create our sprites with various image effects applied.
        SupmuwSprite monster;
        
        monster = new SupmuwSprite(100,50);
        monster.effect = "none";
        sprites.add(monster);
        
        monster = new SupmuwSprite(100,120);
        monster.effect = "crop";
        sprites.add(monster);
        
        monster = new SupmuwSprite(100,190);
        monster.effect = "trans";
        sprites.add(monster);
        
        monster = new SupmuwSprite(100,260);
        monster.effect = "add";
        sprites.add(monster);
        
        monster = new SupmuwSprite(100,330);
        monster.effect = "invert";
        sprites.add(monster);
        
        monster = new SupmuwSprite(100,400);
        monster.effect = "alpha";
        sprites.add(monster);
    }
    
    public void logic() {
         
         // animate our sprites
         for(SupmuwSprite monster : sprites) {
            monster.animate();
         }
    }
    
    public void paint(Graphics g) {
        // set the background color to white.
        this.setBackground(new Color(0xFFFFFF));
        
        // clear the panel with the background color.
        super.paint(g);
        
        // cast our Graphics as a Graphics2D, since it has more features and g is actually a Graphics2D anyways.
        Graphics2D g2D = (Graphics2D) g;
        
        // draw a green rectangle behind the sprites just to show off transparency effects.
        g.setColor(new Color(0x008800));
        g.fillRect(0,0,100,480);
        
        // render the sprites
        for(SupmuwSprite monster : sprites) {
            monster.render(g2D);
        }
        
        // Set our drawing color to red
        g2D.setColor(new Color(0xFF0000));
        
        // display the current frame rate.
        g2D.drawString("" + timer.fpsCounter, 10,32);
        
    }
}
