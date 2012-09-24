package example;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import pwnee.*;

public class GameOverLevel extends Level {
   
   public BreakoutPanel theGame;
   
   public GameOverLevel(GamePanel game) {
      super(game);
      
      theGame = (BreakoutPanel) game;
      // play midi
      theGame.midi.load("midi/gladeintheforest.mid");
      theGame.midi.loop(-1);
      theGame.midi.play(true);
   }
   
   
   public void loadData() {
      
   }
   
   
   public void clean() {
      //
   }
   
   
   
   
   public void logic() {
      if(keyboard.justPressed(KeyEvent.VK_SPACE))
         theGame.changeLevel("title");
   }
   
   
   
   public void render(Graphics2D g) {
      // Draw background color dark purple
      g.setColor(new Color(0x110033));
      g.fillRect(0,0,640,480);
      
      // I got really lazy making the game over level. So it's just going to display your final score and tell you to press enter in red text.
      g.setColor(new Color(0xFF0000));
      
      g.drawString("GAME OVER!!!", 200,200);
      g.drawString("Score: " + theGame.score,200, 250);
      g.drawString("PRESS SPACE!",200,300);
      
   }
   
   
}

