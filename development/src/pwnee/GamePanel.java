package pwnee;

/*======================================================================
 * 
 * Pwnee - A lightweight 2D Java game engine
 * 
 * Copyright (c) 2012 by Stephen Lindberg (sllindberg21@students.tntech.edu)
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: 
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
======================================================================*/

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;

import pwnee.image.ImageLoader;
import pwnee.input.*;


/** A JPanel that houses our game's display, manages its top level resources, and handles the top level of the Game's timer-driven model. */
public abstract class GamePanel extends JPanel implements ActionListener {
   
  /** The game's timer */
  public GameTimer timer;

  /** Used for polling keyboard input */
  public Keyboard keyboard;

  /** Used for polling mouse input */
  public Mouse mouse;

  /** The game's ImageLoader, used to wait on filtered images to finish processing. */
  public ImageLoader imgLoader;

  /** 
   * The timer event handler will execute this many logic iterations each time 
   * it gets a timer event. 
   * This can be increased to produce fast-forward type effects. 
   */
  public int stepsPerFrame = 1;

  /** A conventience flag for showing everyone that the game is currently loading something.  */
  public boolean isLoading = false;

  /** 
   * Flag for whether the game is currently running. 
   * If this is false, then all timer events will be skipped 
   * by the timer event handler. 
   */
  public boolean isRunning = false;

  /** Our game's current level. */
  public Level curLevel = null;

  /** 
   * Flag to let our game know that we need to change levels at the before 
   * performing any logic or rendering on an iteration. 
   */
  public boolean changingLevel = false;

  /** The name of the level we are switching to if changingLevel is true. */
  public String changeToLevelName = "";

  /** The Level we are changing to if changingLevel is true. */
  public Level toLevel = null;
  
  
  /** Whether the game is hard-paused. If true, skip logic(). */
  public boolean isPaused = false;
  
   
  /** Initializes the GamePanel and its components. */
  public GamePanel() {
    super();
    timer = new GameTimer(0, this);
    keyboard = new Keyboard(this);
    mouse = new Mouse(this);
    imgLoader = new ImageLoader(this);
  }
   
  /** 
   * The game's timer event handler. This runs on the Event Dispatch Thread. 
   * We don't need to worry about synchronizing this method since the 
   * EventDispatch Thread handles only one event at a time. 
   */
  public void actionPerformed(ActionEvent e) {
    synchronized(this) {
      if(e.getSource() == timer) {
        // if the app isn't currently running, skip the timer event handler.
        if(!this.isRunning)
          return;
         
        // poll user input for this frame
        keyboard.poll();
        mouse.poll();
         
        // Run n iterations through our game's logic (most of the time, this will be 1.)
        // Then perform 1 rendering iteration.
        for(int i =0; i < stepsPerFrame; i++) {
          if(!isPaused) {
            this.logic();
          }
        }
        this.repaint();
     
        timer.updateFrameRateCounter();
      }
    }
  }
   
  /** 
   * Performs 1 iteration through the game's logic. 
   * This is automatically called by the GamePanel's timer event handler. 
   * Code is provided here for changing levels, but the user is expected to 
   * override this and implement their own game logic code. 
   */
  public void logic() {
    // change our level if we are scheduled to do so.
    if(changingLevel)
      doChangeLevel();
        
    //// example:
    // do some stuff();
    //
    //// run our current level's logic code.
    // curLevel.logic();
  }
   
  /** 
   * The top level rendering method for our game. 
   * This is automatically called by the GamePanel's timer event handler 
   * via repaint(). 
   * The user is expected to override this to do their own custom painting. 
   */
  public void paint(Graphics g) {
    super.paint(g);
      
    //// example:
    // paint some stuff(g);
    //
    //// render our current level:
    // curLevel.render(g);
  }
   
 /** Sets the frame rate (in frames per second) for the game's timer and starts the game. */
  public void start(int fps) {
    timer.setFPS(fps);
    this.isRunning = true;
    timer.start();
  }
  
  /** Starts the game with a default frame rate of 60 fps. */
  public void start() {
    start(60);
  }
  
  /** 
   * Schedules the game to change to a different level associated with some 
   * level name on the next timer event. 
   */
  public void changeLevel(String name) {
    this.changingLevel = true;
    this.changeToLevelName = name;
  }
  
  /** 
   * Schedules the game to change to a pre-loaded level on the next timer event. 
   */
  public void changeLevel(Level newLevel) {
    this.changingLevel = true;
    this.toLevel = newLevel;
  }
    
  /** 
   * Cleans up after the current level and then switches to a new level 
   * matching changeToLevelName before performing any logic or rendering 
   * on a game iteration.
   */
  private void doChangeLevel() {
    this.isLoading = true;
    this.changingLevel = false;

    if(curLevel != null)
      curLevel.clean();

    Level newLevel; 
    if(toLevel == null) {
      newLevel = makeLevelInstance(changeToLevelName);
      if(newLevel != null) {
        curLevel = newLevel;
      }
      else {
        System.err.println("GamePanel change level error : " + changeToLevelName + 
                            " is not a valid level in this game.");
      }
    }
    else {
      curLevel = toLevel;
    }

    changeToLevelName = "";
    toLevel = null;
    this.isLoading = false;
  }
    
  /** 
   * Given a level's name, an implementation of this method should return
   * a new Level corresponding to the name.
   */
  public Level makeLevelInstance(String levelName) {
      return null;
  }
}



