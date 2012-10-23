package example;

import java.awt.*;
import java.util.ArrayList;
import java.awt.event.KeyEvent;
import pwnee.*;
import pwnee.sprites.*;
import pwnee.sound.*;

/** The GamePanel for our breakout clone game! */
public class BreakoutPanel extends GamePanel {
    
    /** Our midi player */
    public MidiPlayer midi;
    
    /** Our sound player. */
    public SoundPlayer sounds;

    /** The player's score as a game global variable. */
    public int score = 0;
    
    public BreakoutPanel() {
        super();
        this.setPreferredSize(new Dimension(640, 480));
        
        midi = new MidiPlayer();
        
        sounds = new SoundPlayer();
        
        changeLevel("title");
    }
    
    public void logic() {
         super.logic();
            
         if(this.isLoading)
            return;
         
         // run our current level's logic. 
         curLevel.logic();
    }
    
    public void paint(Graphics g) {
        if(this.isLoading)
            return;
        
        // set the background color to dark blue.
        this.setBackground(new Color(0x000055));
        
        // clear the panel with the background color.
        super.paint(g);
        
        // cast our Graphics as a Graphics2D, since it has more features and g is actually a Graphics2D anyways.
        Graphics2D g2D = (Graphics2D) g;

        
        // render our current level.
        if(curLevel != null)
            curLevel.render(g2D);
        
        
        // Set our drawing color to red
        g2D.setColor(new Color(0xFF0000));

        // display the current frame rate.
        g2D.drawString("" + timer.fpsCounter, 10,32);
        
    }
    
    
    /** 
     * Returns an instance of the Level type used in this game associated with levelName. 
     * This is automatically called by Pwnee's internal level changing methods at the beginning 
     * of the frame after we tell our game to switch levels.
     */
    public Level makeLevelInstance(String levelName) {
        if(levelName == "title") 
           return new TitleLevel(this);
        else if(levelName == "breakout")
           return new BreakoutLevel(this);
        else if(levelName == "gameOver")
           return new GameOverLevel(this);
        else
           return null;
    }
}




