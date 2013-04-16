package example;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import pwnee.*;
import pwnee.image.ImageLoader;

public class SpritesPanel extends GamePanel {
    
    public ArrayList<SupmuwSprite> monsters = new ArrayList<SupmuwSprite>();
    
    public SpritesPanel() {
      super();
      this.setPreferredSize(new Dimension(640, 480));
      
      // Load the SupmuwSprites' images
      SupmuwSprite.loadImages(this.imgLoader);
      
      // Create the monsters.
      for(int i = 0; i < 100; i++) {
          monsters.add(new SupmuwSprite(GameMath.rand.nextInt(640), GameMath.rand.nextInt(480)));
      }
    }
    
    public void logic() {
      for(SupmuwSprite monster : monsters) {
        // press space to make the Supmuw flash.
        if(keyboard.justPressed(KeyEvent.VK_SPACE)) {
          monster.flash = 1.0;
        }
        
        monster.move(this);
        
        // animate the Supmuw.
        monster.animate();
      }
    }
    
    public void paint(Graphics g) {
        // set the background color to black.
        this.setBackground(new Color(0x000000));
        
        // clear the panel with the background color.
        super.paint(g);
        
        // cast our Graphics as a Graphics2D, since it has more features and g is actually a Graphics2D anyways.
        Graphics2D g2D = (Graphics2D) g;
        
        // render the monsters
        for(SupmuwSprite monster : monsters) {
           monster.render(g2D);
        }
        
        // Set our drawing color to white
        g2D.setColor(new Color(0xFFFFFF));
        
        // display the current frame rate.
        g2D.drawString("" + timer.fpsCounter, 10,32);
        g2D.drawString("Press space to make the monsters flash!", 10,64);
    }
}
