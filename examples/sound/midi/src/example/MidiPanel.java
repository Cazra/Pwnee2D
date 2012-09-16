package example;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.event.KeyEvent;
import pwnee.*;
import pwnee.sound.*;

public class MidiPanel extends GamePanel {
    
    public MidiPlayer midi = new MidiPlayer();
    public String[] midiFiles = {"midi/confrontation.mid","midi/dreamyworlds.mid","midi/gladeintheforest.mid","midi/velocity.mid"};
    public int midiIndex = 0;
    
    public MidiPanel() {
        super();
        this.setPreferredSize(new Dimension(640, 480));
        midi.load(midiFiles[midiIndex]);
        midi.loop(-1);
        midi.play(true);
        
    }
    
    public void logic() {
         if(keyboard.justPressed(KeyEvent.VK_SPACE) && midi.isPlaying())
            midi.pause();
         else if(keyboard.justPressed(KeyEvent.VK_SPACE)) {
            midi.play(false);
            midi.loop(-1);
         }
         
         if(keyboard.justPressed(KeyEvent.VK_LEFT)) {
            midiIndex--;
            if(midiIndex < 0)
               midiIndex = midiFiles.length - 1;
               
            midi.load(midiFiles[midiIndex]);
            midi.loop(-1);
            midi.play(true);
         }
         
         if(keyboard.justPressed(KeyEvent.VK_RIGHT)) {
            midiIndex++;
            if(midiIndex >= midiFiles.length)
               midiIndex = 0;
               
            midi.load(midiFiles[midiIndex]);
            midi.loop(-1);
            midi.play(true);
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
        
        if(midi.isPlaying())
            g2D.drawString("Currently playing: " + midiFiles[midiIndex],200, 240);
        else
            g2D.drawString("Currently paused: " + midiFiles[midiIndex],200, 240);
        
        // display instructions.
        g2D.drawString("Press left/right to change the midi.", 10,62);
        g2D.drawString("Press space to play/pause the currently playing midi.", 10,77);
    }
}
