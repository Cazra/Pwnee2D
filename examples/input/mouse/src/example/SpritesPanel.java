package example;

import java.awt.*;
import java.util.ArrayList;
import java.awt.event.KeyEvent;
import pwnee.*;
import pwnee.image.ImageLoader;

public class SpritesPanel extends GamePanel {
    
    public KittehSprite kitteh;
    public ArrayList<SupmuwSprite> monsters = new ArrayList<SupmuwSprite>();
    
    public SpritesPanel() {
        super();
        this.setPreferredSize(new Dimension(640, 480));
        
        // Load the Sprites' images
        KittehSprite.loadImages(this.imgLoader);
        SupmuwSprite.loadImages(this.imgLoader);
        
        // Create the kitteh, controlled by the player
        kitteh = new KittehSprite(320,240);
    }
    
    public void logic() {
        
        // move the monsters
        for(SupmuwSprite monster : monsters) {
            monster.move(this);
        }
        
        // Hold the left mouse button to make the kitteh move towards the mouse.
        if(mouse.isLeftPressed) {
            kitteh.move(mouse.x, mouse.y);
        }
        
        // Hold the middle button to make the kitteh move away from the mouse.
        if(mouse.isMiddlePressed) {
            kitteh.moveAway(mouse.x, mouse.y);
        }
        
        // Right click to create a monster.
        if(mouse.justRightPressed) {
            SupmuwSprite monster = new SupmuwSprite(mouse.x, mouse.y);
            monsters.add(monster);
        }
        
        // Release the right click to create an upsidedown monster.
        if(mouse.justRightClicked) {
            SupmuwSprite monster = new SupmuwSprite(mouse.x, mouse.y);
            monster.isUpsidedown = true;
            monsters.add(monster);
        }
        
        // Mouse wheel down to make the kitteh move 32 pixels down.
        if(mouse.wheel > 0)
            kitteh.y += 32;
        
        // Mouse wheel up to make the kitteh move 32 pixels up.
        if(mouse.wheel < 0)
            kitteh.y -= 32;
        
        // animate the sprites
        for(SupmuwSprite monster : monsters) {
            monster.animate();
        }
        kitteh.animate();
    }
    
    public void paint(Graphics g) {
        // set the background color to white.
        this.setBackground(new Color(0xFFFFFF));
        
        // clear the panel with the background color.
        super.paint(g);
        
        // cast our Graphics as a Graphics2D, since it has more features and g is actually a Graphics2D anyways.
        Graphics2D g2D = (Graphics2D) g;
        
        // render the sprites
        kitteh.render(g2D);
        for(SupmuwSprite monster : monsters) {
           monster.render(g2D);
        }
        
        
        // Set our drawing color to black
        g2D.setColor(new Color(0x000000));
        
        // display the current frame rate.
        g2D.drawString("" + timer.fpsCounter, 10,32);
        
        // display instructions.
        g2D.drawString("Hold the left mouse button to move toward the mouse.", 10,47);
        g2D.drawString("Hold the middle mouse button to move away from the mouse.", 10,62);
        g2D.drawString("Right-click to create a monster", 10,77);
        g2D.drawString("Release the right-click to create an upsidedown monster", 10,92);
        g2D.drawString("Mouse wheel up to move up 32 pixels.", 10,107);
        g2D.drawString("Move wheel down to move down 32 pixels.", 10,122);
    }
}
