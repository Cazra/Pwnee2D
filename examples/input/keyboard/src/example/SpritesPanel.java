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
        
        // Move the player with keyboard input.
        if(keyboard.isPressed(KeyEvent.VK_LEFT)) {
            kitteh.x -= kitteh.speed;
            kitteh.setDirection('w');
        }
        if(keyboard.isPressed(KeyEvent.VK_RIGHT)) {
            kitteh.x += kitteh.speed;
            kitteh.setDirection('e');
        }
        if(keyboard.isPressed(KeyEvent.VK_UP)) {
            kitteh.y -= kitteh.speed;
            kitteh.setDirection('n');
        }
        if(keyboard.isPressed(KeyEvent.VK_DOWN)) {
            kitteh.y += kitteh.speed;
            kitteh.setDirection('s');
        }
        
        // Press spacebar to create a monster at a random position.
        if(keyboard.justPressed(KeyEvent.VK_SPACE)) {
            SupmuwSprite monster = new SupmuwSprite(GameMath.rand.nextInt(640), GameMath.rand.nextInt(480));
            monsters.add(monster);
        }
        
        // Release spacebar to create an upsidedown monster at a random position.
        if(keyboard.justTyped(KeyEvent.VK_SPACE)) {
            SupmuwSprite monster = new SupmuwSprite(GameMath.rand.nextInt(640), GameMath.rand.nextInt(480));
            monster.isUpsidedown = true;
            monsters.add(monster);
        }
        
        // If enter is pressed, turn the kitteh counter-clockwise once. If enter continues to be pressed, turn him some more!
        if(keyboard.justPressedRep(KeyEvent.VK_ENTER)) {
            kitteh.turnAround();
        }
        
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
        g2D.drawString("Arrows to move", 10,47);
        g2D.drawString("Enter to spin", 10,62);
        g2D.drawString("Press Space once to create a monster", 10,77);
        g2D.drawString("Release Space to create an upsidedown monster", 10,92);
    }
}
