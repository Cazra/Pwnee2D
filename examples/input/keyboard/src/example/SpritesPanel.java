package example;

import java.awt.*;
import java.util.ArrayList;
import pwnee.*;
import pwnee.image.ImageLoader;

public class SpritesPanel extends GamePanel {
    
    public ArrayList<KittehSprite> monsters = new ArrayList<KittehSprite>();
    
    public SpritesPanel() {
        super();
        this.setPreferredSize(new Dimension(640, 480));
        
        // Load the KittehSprites' images
        KittehSprite.loadImages(this.imgLoader);
        
        // Create the monsters.
        for(int i = 0; i < 500; i++) {
            monsters.add(new KittehSprite(GameMath.rand.nextInt(640), GameMath.rand.nextInt(480)));
        }
    }
    
    public void logic() {
        
        for(KittehSprite monster : monsters) {
            // move the monsters
            monster.move(this);
            
            // animate the monsters
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
        
        // render the monsters
        for(KittehSprite monster : monsters) {
           monster.render(g2D);
        }
        
        // Set our drawing color to black
        g2D.setColor(new Color(0x000000));
        
        // display the current frame rate.
        g2D.drawString("" + timer.fpsCounter, 10,32);
    }
}
