package pwnee.input;

import java.awt.event.*;
import java.awt.Component;
import java.util.HashMap;

/** A class that can poll for keyboard input in a swing Component.*/
public class Keyboard implements KeyListener {
	
   /** A reference to the Component observing the Keyboard. */
   public Component observer;
   
   /** True if any key was pressed or has remained pressed since the last frame */
   public boolean isAnyPressed = false;
   
   /** True if the user just began pressing any key since the last frame */
   public boolean justAnyPressed = false;
   
   /** Like justAnyPressed, but it repeats firing true after any key has been held for at least a few seconds. */
   public boolean justAnyPressedRep = false;
   
   /** True if the user just released any key */
   public boolean justAnyTyped = false;
   
   /** Stores the keycode of the last key that was pressed. */
   public int lastKeyPressed = -1;
   
   
   // The keycode -> flag hashmaps used for polling key presses. 
   // Don't use these directly. Instead, use their corresponding 
   // methods which are guaranteed to return a boolean value.
   
   /** For polling whether keys are currently being held. */
   private HashMap<Integer, Boolean> _isPressed = new HashMap<Integer, Boolean>();
   
   /** For polling whether keys have just been pressed since the last frame. */
   private HashMap<Integer, Boolean> _justPressed = new HashMap<Integer, Boolean>();
   
   /** Like _justPressed, but acts like _isPressed for keys that have been held for more than a couple seconds. */
   private HashMap<Integer, Boolean> _justPressedRep = new HashMap<Integer, Boolean>();
   
   /** For polling whether keys have just been released since the last frame. */
   private HashMap<Integer, Boolean> _justTyped = new HashMap<Integer, Boolean>();
   

   // Used by the polling algorithm to keep a record of what keys have been pressed or released since the last frame.
   private HashMap<Integer, Boolean> pressedSinceLastFrame = new HashMap<Integer, Boolean>();
   private HashMap<Integer, Boolean> releasedSinceLastFrame = new HashMap<Integer, Boolean>();

   private boolean pressedAnySinceLastFrame = false;
   private boolean releasedAnySinceLastFrame = false;
   
   
   
   /** Creates the Keyboard object and assigns a Component to observer it. */
	public Keyboard(Component observer) {
      this.observer = observer;
      observer.addKeyListener(this);
	}
	
	public void poll() {
      justAnyPressed = false;
      justAnyPressedRep = false;
      justAnyTyped = false;
      
      if(pressedAnySinceLastFrame) {
         justAnyPressedRep = true;
         if(!isAnyPressed) 
            justAnyPressed = true;
         isAnyPressed = true;
      }
      
      if(releasedAnySinceLastFrame) {
         justAnyTyped = true;
         isAnyPressed = false;
      }
      
      pressedAnySinceLastFrame = false;
      releasedAnySinceLastFrame = false;
      
      // update our hash maps.
      for(int key : pressedSinceLastFrame.keySet()) {
         _justPressed.put(key, false);
         _justPressedRep.put(key, false);
         _justTyped.put(key, false);
         
         if(pressedSinceLastFrame.get(key)) {
            _justPressedRep.put(key, true);
            if(!_isPressed.get(key))
               _justPressed.put(key, true);
            _isPressed.put(key, true);
            
            lastKeyPressed = key;
         }
         
         if(releasedSinceLastFrame.get(key)) {
            _justTyped.put(key,true);
            _isPressed.put(key, false);
         }
         
         pressedSinceLastFrame.put(key,false);
         releasedSinceLastFrame.put(key,false);
         
      }
   }
   
   
   public void keyPressed(KeyEvent e) {
      pressedAnySinceLastFrame = true;
      pressedSinceLastFrame.put(e.getKeyCode(), true);
   }
   
   public void keyReleased(KeyEvent e) {
      releasedAnySinceLastFrame = true;
      releasedSinceLastFrame.put(e.getKeyCode(), true);
   }
   
   public void keyTyped(KeyEvent e) {
      // Do nothing.
   }
   
   // Use these methods to actually check our keyboard hash maps.
   
   /** For polling whether keys are currently being held. */
   public boolean isPressed(int key) {
      if(_isPressed.get(key)) return true;
      else return false;
   }
   
   /** For polling whether keys have just been pressed since the last frame. */
   public boolean justPressed(int key) {
      if(_justPressed.get(key)) return true;
      else return false;
   }
   
   /** Like _justPressed, but acts like _isPressed for keys that have been held for more than a couple seconds. */
   public boolean justPressedRep(int key) {
      if(_justPressedRep.get(key)) return true;
      else return false;
   }
   
   /** For polling whether keys have just been released since the last frame. */
   public boolean justTyped(int key) {
      if(_justTyped.get(key)) return true;
      else return false;
   }
   

}




