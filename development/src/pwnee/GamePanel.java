package pwnee;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;

import pwnee.image.ImageLoader;
import pwnee.input.*;


/** A JPanel that houses our game's display, manages its top level resources, and handles the top level of the Game's timer-driven model. */
public class GamePanel extends JPanel implements ActionListener {
   
   /** The game's timer */
   public GameTimer timer;
   
   /** Used for polling keyboard input */
   public Keyboard keyboard;
   
   /** The timer event handler will execute this many logic iterations each time it gets a timer event. This can be increased to produce fast-forward type effects. */
   public int stepsPerFrame = 1;
   
   public boolean isLoading = false;
   
   public boolean isRunning = false;
   
   
   public GamePanel() {
      timer = new GameTimer(0, this);
      timer.setFPS(60);
      keyboard = new Keyboard(this);
   }
   
   /** The game's timer event handler. This runs on the Event Dispatch Thread. We don't need to worry about synchronizing this method since the EventDispatch Thread handles only one event at a time. */
   public void actionPerformed(ActionEvent e) {
      if(e.getSource() == timer) {
         // if the app isn't currently running, skip the timer event handler.
         if(!this.isRunning)
            return;
         
         // poll user input for this frame
         keyboard.poll();
         
         // Run n iterations through our game's logic (most of the time, this will be 1.)
         // Then perform 1 rendering iteration.
         for(int i =0; i < stepsPerFrame; i++) {
            this.logic();
         }
         this.repaint();
         
         timer.updateFrameRateCounter();
      }
   }
}



