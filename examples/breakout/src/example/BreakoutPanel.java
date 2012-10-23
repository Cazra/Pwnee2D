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
    
    /** Our game's current level. */
    public Level curLevel = null;
    
    /** Flag to let our game know that we need to change levels at the before performing any logic or rendering on an iteration. */
    public boolean changingLevel = false;
    
    /** The player's score as a game global variable. */
    public int score = 0;
    
    /** The name of the level we are switching to if changingLevel is true. */
    public String changeToLevelName = "";
    
    public BreakoutPanel() {
        super();
        this.setPreferredSize(new Dimension(640, 480));
        
        midi = new MidiPlayer();
        
        sounds = new SoundPlayer();
        
        changeLevel("title");
    }
    
    public void logic() {
         // change our level if we are scheduled to do so.
         if(changingLevel)
            doChangeLevel();
            
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
    
    
    /** Schedules the game to change to a different level on the next timer event. */
    public void changeLevel(String name) {
         this.changingLevel = true;
         this.changeToLevelName = name;
    }
    
    
    /** 
     * Cleans up after the current level and then switches to a new level matching changeToLevelName before performing any logic or rendering on a game iteration.
     */
    private void doChangeLevel() {
         this.isLoading = true;
         
         this.changingLevel = false;
         
         // TODO: create a thread that performs the level's resource loading for us so 
         // that we can display a loading screen instead of just freezing while it loads.
         
         if(curLevel != null)
            curLevel.clean();
         
         // swith to the level matching the name we gave.
         // In our breakout game, we only have 3 levels to switch among.
         if(changeToLevelName == "title") 
            curLevel = new TitleLevel(this);
         else if(changeToLevelName == "breakout")
            curLevel = new BreakoutLevel(this);
         else if(changeToLevelName == "gameOver")
            curLevel = new GameOverLevel(this);
         else
            System.err.println("BreakoutPanel change level error : " + changeToLevelName + " is not a level in this game.");
         
         this.isLoading = false;
    }
}




