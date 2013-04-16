package example;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import pwnee.*;
import pwnee.image.ImageLoader;

public class SpritesPanel extends GamePanel {
    
    SupmuwSprite monster;
    
    public SpritesPanel() {
      super();
      this.setPreferredSize(new Dimension(640, 480));
      
      // Load the SupmuwSprites' images
      SupmuwSprite.loadImages(this.imgLoader);
      
      // Create the monster.
      monster = new SupmuwSprite(320, 240);
    }
    
    public void logic() {
      // press space to make the Supmuw flash.
      if(keyboard.justPressed(KeyEvent.VK_SPACE)) {
        monster.flash = 1.0;
      }
      
      // animate the Supmuw.
      monster.animate();
    }
    
    public void paint(Graphics g) {
        // set the background color to black.
        this.setBackground(new Color(0x000000));
        
        // clear the panel with the background color.
        super.paint(g);
        
        // cast our Graphics as a Graphics2D, since it has more features and g is actually a Graphics2D anyways.
        Graphics2D g2D = (Graphics2D) g;
        
        // render the monster
        monster.render(g2D);
        
        // Set our drawing color to white
        g2D.setColor(new Color(0xFFFFFF));
        
        // display the current frame rate.
        g2D.drawString("" + timer.fpsCounter, 10,32);
        g2D.drawString("Press space to make the monster flash!", 10,64);
    }
}
