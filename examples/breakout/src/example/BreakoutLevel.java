package example;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import pwnee.*;

public class BreakoutLevel extends Level {
   
   public BreakoutPanel theGame;
   
   /** Flag for pausing the game */
   public boolean isPaused = false;
   
   public PlayerSprite player = null;
   
   public SupmuwSprite monster = null;
   
   public String playerHitSound = "sound/HITSNTH4.WAV";
   public String blockHitSound = "sound/Bear.wav";
   public String wallHitSound = "sound/PULSE02.WAV";
   public String winSound = "sound/ARABIA.WAV";
   
   
   public LinkedList<BlockSprite> blocks = null;
   
   public int topWallY = 20;
   public int leftWallX = 20;
   public int rightWallX = leftWallX + 64*8;
   
   
   public BreakoutLevel(GamePanel game) {
      super(game);
      
      theGame = (BreakoutPanel) game;
      
      // load, loop, and start playing midi.
      theGame.midi.load("midi/confrontation.mid");
      theGame.midi.loop(-1);
      theGame.midi.play(true);
      
      // load sounds
      theGame.sounds.load(playerHitSound);
      theGame.sounds.load(blockHitSound);
      theGame.sounds.load(winSound);
      theGame.sounds.load(wallHitSound);
      
      reset();
   }
   
   
   public void loadData() {
      // load the images for our sprites
      PlayerSprite.loadImages(game.imgLoader);
      SupmuwSprite.loadImages(game.imgLoader);
      BlockSprite.loadImages(game.imgLoader);
      
      
   }
   
   
   public void clean() {
      //
   }
   
   
   /** 
   * Resets the level so that the player and monster are centered at the bottom, 
   * the monster is not yet moving, and there is a fresh grid of blocks ready to be broken. 
   */
   public void reset() {
      // create the player and monster near the bottom of the screen.
      monster = new SupmuwSprite(320,420);
      player = new PlayerSprite(320,450);
      
      // create our grid of blocks.
      blocks = new LinkedList<BlockSprite>();
      
      for(int i = 40; i <= 40 + 24*12; i += 24) {
         for(int j = leftWallX + (i % 48); j <= rightWallX-64; j+=64) {
            
            BlockSprite block = new BlockSprite(j,i);
            blocks.add(block);
            
         }
      }
   }
   
   
   
   public void logic() {
   
      // The player can pause the game with Esc
      if(keyboard.justPressed(KeyEvent.VK_ESCAPE))
         isPaused = !isPaused;
      if(isPaused)
         return;
      
      // Arrow keys to move the player
      if(keyboard.isPressed(KeyEvent.VK_LEFT))
         player.x -= player.speed;
      if(keyboard.isPressed(KeyEvent.VK_RIGHT))
         player.x += player.speed;
      
      // prevent the player from leaving the play area.
      if(player.x < leftWallX + player.width/2)
         player.x = leftWallX + player.width/2;
      if(player.x > rightWallX - player.width/2)
         player.x = rightWallX - player.width/2;
         
      // press spacebar to make the monster start moving.
      if(!monster.isMoving && keyboard.justPressed(KeyEvent.VK_SPACE)) {
         monster.direction = 45 + GameMath.rand.nextDouble()*90;
         monster.isMoving = true;
      }
      
      // move the monster
      monster.move();
      
      // Make the monster bounce off the side walls.
      if(monster.x < leftWallX + monster.radius && monster.dx < 0) {
         theGame.sounds.play(wallHitSound);
         monster.bounce(0);
      }
      if(monster.x > rightWallX - monster.radius && monster.dx > 0) {
         theGame.sounds.play(wallHitSound);
         monster.bounce(180);
      }
      if(monster.y < topWallY + monster.radius && monster.dy < 0) {
         theGame.sounds.play(wallHitSound);
         monster.bounce(270);
      }
            
      // monster bounce upwards off of player.
      if(monster.collideWith(player) && monster.dy > 0) {
         monster.bounce(90);
         theGame.sounds.play(playerHitSound);
      }
      
      // monster bounces off of and break blocks
      for(BlockSprite block : blocks) {
         if(monster.collideWith(block)) {
            if(monster.x < block.x && monster.dx > 0)
               monster.bounce(180);
            else if(monster.x > block.x + block.width && monster.dx < 0)
               monster.bounce(0);
            else if(monster.dy <= 0)
               monster.bounce(270);
            else if(monster.dy > 0)
               monster.bounce(90);
            
            monster.speed += 0.05;
            theGame.score += 10;
            block.destroy();
            theGame.sounds.play(blockHitSound);
         }
            
      }
      
      // update the list of blocks by removing all blocks from it that have been destroyed.
      // Make a copy of the list to iterate through, since Java won't let us iterate through the actual list and remove its elements at the same time.
      LinkedList<BlockSprite> blocksCpy = new LinkedList<BlockSprite>(blocks);
      for(BlockSprite block : blocksCpy) {
         if(block.isDestroyed)
            blocks.remove(block);
      }
      
      // If the monster goes off the bottom of the screen, we lose and go to the Game Over level.
      if(monster.y > 480)
         theGame.changeLevel("gameOver");
      
      // If all the blocks have been destroyed, restart the level.
      if(blocks.size() == 0) {
         theGame.score += 1001;
         this.reset();
         theGame.sounds.play(winSound);
      }
      
      
      // animate the sprites
      player.animate();
      monster.animate();
   }
   
   
   
   public void render(Graphics2D g) {
      // Draw background color dark purple
      g.setColor(new Color(0x110033));
      g.fillRect(0,0,640,480);
      
      // render the breakout game objects
      player.render(g);
      
      for(BlockSprite block : blocks) {
         block.render(g);
      }
      
      monster.render(g);
      
      // Draw the walls bounding the play area
      g.setColor(new Color(0xAA00FF));
      g.fillRect(0,0,leftWallX,480);
      g.fillRect(rightWallX,0,640-rightWallX,480);
      g.fillRect(leftWallX,0,rightWallX-leftWallX,topWallY); 
      g.setColor(new Color(0x7700BB));
      g.drawRect(0,0,leftWallX,480);
      g.drawRect(rightWallX,0,640-rightWallX,480);
      g.drawRect(leftWallX,0,rightWallX-leftWallX,topWallY); 
      
      // Draw HUD text
      g.setColor(new Color(0xFF0000));
      if(isPaused)
         g.drawString("PAUSED!!!",200,240);
      
      // draw the player's score box
      g.setColor(new Color(0x110033));
      g.fillRect(rightWallX + 20, 210, 60, 18);
      g.setColor(new Color(0xCCCCCC));
      g.drawString("Score: ", rightWallX + 20, 200);
      g.drawString("" + theGame.score, rightWallX +24, 226);
      
      
         
      
   }
   
   
}

