package example;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.event.KeyEvent;
import pwnee.*;
import pwnee.sound.*;

public class SoundPanel extends GamePanel {
    
    public SoundPlayer soundPlayer = new SoundPlayer();
    public String[] soundFiles = {"sounds/Galaxian 2.wav","sounds/Move 2.wav","sounds/WHIZZ.WAV","sounds/WHOOSH09.WAV"};
    public int soundIndex = 0;
    
    public SoundPanel() {
        super();
        this.setPreferredSize(new Dimension(640, 480));

        // load our SoundPlayer's library with our sound files.
        for(int i = 0; i < soundFiles.length; i++) {
            soundPlayer.load(soundFiles[i]);
        }
    }
    
    public void logic() {
         
         // use arrow keys to change the selected sound.
         if(keyboard.justPressed(KeyEvent.VK_LEFT)) {
            soundIndex--;
            if(soundIndex < 0)
               soundIndex = soundFiles.length - 1;
         }
         
         if(keyboard.justPressed(KeyEvent.VK_RIGHT)) {
            soundIndex++;
            if(soundIndex >= soundFiles.length)
               soundIndex = 0;
         }
         
         // press space to play the selected sound.
         if(keyboard.justPressed(KeyEvent.VK_SPACE)) {
            soundPlayer.play(soundFiles[soundIndex]);
         }
    }

    
    public void paint(Graphics g) {
        // set the background color to white.
        this.setBackground(new Color(0xFFFFFF));
        
        // clear the panel with the background color.
        super.paint(g);
        
        // cast our Graphics as a Graphics2D, since it has more features and g is actually a Graphics2D anyways.
        Graphics2D g2D = (Graphics2D) g;

        // Set our drawing color to red
        g2D.setColor(new Color(0xFF0000));
        
        // display the current frame rate.
        g2D.drawString("" + timer.fpsCounter, 10,32);
        
        g2D.drawString("Sound selected: " + soundFiles[soundIndex],200, 240);
        g2D.drawString("Sounds playing: " + soundPlayer.soundsPlaying(),200,260);
        
        // display instructions.
        g2D.drawString("Press left/right to select a sound.", 10,62);
        g2D.drawString("Press space to play an instance of the currently selected sound.", 10,77);
    }
}
