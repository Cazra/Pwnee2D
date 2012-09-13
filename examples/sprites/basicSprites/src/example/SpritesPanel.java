package example;

import java.awt.*;
import java.util.ArrayList;
import pwnee.*;

public class SpritesPanel extends GamePanel {
    
    public ArrayList<BallSprite> balls = new ArrayList<BallSprite>();
    
    public SpritesPanel() {
        super();
        this.start(60);
        
        for(int i = 0; i < 500; i++) {
            balls.add(new BallSprite(GameMath.rand.nextInt(640), GameMath.rand.nextInt(480)));
        }
    }
    
    public void logic() {
        // move the balls
        for(BallSprite ball : balls) {
            ball.move(this);
        }
    }
    
    public void paint(Graphics g) {
        // set the background color to white.
        this.setBackground(new Color(0xFFFFFF));
        
        // clear the panel with the background color.
        super.paint(g);
        
        // cast our Graphics as a Graphics2D, since it has more features and g is actually a Graphics2D anyways.
        Graphics2D g2D = (Graphics2D) g;
        
        // render the balls
        for(BallSprite ball
        
        // Set our drawing color to black
        g2D.setColor(new Color(0x000000));
        
        // display the current frame rate.
        g2D.drawString("" + timer.fpsCounter, 10,32);
    }
}
