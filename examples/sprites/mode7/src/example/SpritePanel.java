package example;

import java.awt.*;
import java.util.ArrayList;
import java.awt.event.KeyEvent;
import pwnee.*;
import pwnee.sprites.*;
import pwnee.sound.MidiPlayer;

public class SpritePanel extends GamePanel {
    
    public Mode7Sprite mode7;
    
    public SupmuwSprite monster;
    
    public MidiPlayer midi;
    
    public SpritePanel() {
        super();
        this.setPreferredSize(new Dimension(640, 480));
        
        midi = new MidiPlayer();
        midi.load("midi/canyoufeelthefear.mid");
        midi.loop(-1);
        midi.play(true);
        
        // load the images for our Sprite
        Image mode7Img = imgLoader.loadFromFile("graphics/UnicornDevourerOfWorlds.jpg");
        imgLoader.addImage(mode7Img);
        imgLoader.waitForAll();
        
        SupmuwSprite.loadImages(imgLoader);
        
        // Create the Mode7Sprite
        mode7 = new Mode7Sprite(mode7Img,0,100,640,380);
        mode7.elevation = 200;
        mode7.camDist = 200;
        
        // Create the monster sprite which will be positioned in the Mode7 world.
        monster = new SupmuwSprite(0,0);
    }
    
    public void logic() {
         // left/right to control camera's direction
         if(keyboard.isPressed(KeyEvent.VK_LEFT))
            mode7.cameraAngle+= 3;
         if(keyboard.isPressed(KeyEvent.VK_RIGHT))
            mode7.cameraAngle-= 3;
          
         // up/down to move camera's focal point forwards and backwards
         if(keyboard.isPressed(KeyEvent.VK_UP)) {
            double dir = mode7.cameraAngle;
            mode7.cameraX += 5*GameMath.cos(dir);
            mode7.cameraY += -5*GameMath.sin(dir);
         }
         if(keyboard.isPressed(KeyEvent.VK_DOWN)) {
            double dir = mode7.cameraAngle + 180;
            mode7.cameraX += 5*GameMath.cos(dir);
            mode7.cameraY += -5*GameMath.sin(dir);
         }
         
         // W/S to change the camera's elevation
         if(keyboard.isPressed(KeyEvent.VK_W)) {
            mode7.elevation += 5;
            mode7.camDist += 5;
            
         }
         if(keyboard.isPressed(KeyEvent.VK_S)) {
            mode7.elevation -= 5;
            mode7.camDist -= 5;
         }
         
         // Enter to change the camera focal point's visibility
         if(keyboard.justPressed(KeyEvent.VK_ENTER)) {
            mode7.showCamPoint = !mode7.showCamPoint;
         }
         
         
         // Position the monster sprite to be in the Mode7 object's world. We'll put it at (500,500) in mode7 coordinates. 
         double[] pos = mode7.untransformPixel(500,500);
         monster.x = mode7.x + pos[0];
         monster.y = mode7.y + pos[1];
         
         // The untransform may put the monster somewhere weird if it's offscreen. 
         // Make it invisible if this happens.
         monster.isVisible = true;
         if(monster.y < 0)
            monster.isVisible = false;
         
         // scale the sprite to mimic perspective.
         double monsterDist = GameMath.sqrDist(500,500,mode7.camDx, mode7.camDy)+mode7.elevation*mode7.elevation;
         double sq1000 = 1000*1000;
         monster.scale(sq1000/monsterDist, sq1000/monsterDist);
         
         // clip the monster sprite if it's too close
         if(monsterDist < 50*50)
            monster.isVisible = false;
         
         // clip the monster sprite if it's too far. Also account for overflow.
         if(monsterDist > 10000*10000 || monsterDist < 0)
            monster.isVisible = false;
         
         monster.animate();
    }
    
    public void paint(Graphics g) {
        // set the background color to dark blue.
        this.setBackground(new Color(0x000055));
        
        // clear the panel with the background color.
        super.paint(g);
        
        // cast our Graphics as a Graphics2D, since it has more features and g is actually a Graphics2D anyways.
        Graphics2D g2D = (Graphics2D) g;
        
        // render the mode7 Sprite
        mode7.render(g2D);
        double[] point = mode7.untransformPixel(500,500);
        
        monster.render(g2D);
        
        // Set our drawing color to red
        g2D.setColor(new Color(0xFF0000));

        // display the current frame rate.
        g2D.drawString("" + timer.fpsCounter, 10,32);
        
        
        // draw other stats
        g2D.drawString("Camera focus position: (" + Math.round(mode7.cameraX) + ", " + Math.round(mode7.cameraY) + ")", 10,47);
        g2D.drawString("Camera angle: " + mode7.cameraAngle, 10,62);
        g2D.drawString("Camera elevation: " + mode7.elevation,10,77);
        
    }
}
