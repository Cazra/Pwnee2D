package example;

import java.awt.*;
import pwnee.sprites.Sprite;
import pwnee.GameMath;


public class BallSprite extends Sprite {
    
    /** The ball's current x velocity. */
    public double dx;
    
    /** The ball's current y velocity. */
    public double dy;
    
    /** Flag tells it whether this ball should be painted red (due to colliding with another ball). */
    public boolean isRed = false;
    
    /** Creates the BallSprite with a random velocity. */
    public BallSprite(int x, int y, double scale) {
        super(x,y);
        dx = GameMath.rand.nextDouble()*6-3;
        dy = GameMath.rand.nextDouble()*6-3;
        width = 32;
        height = 32;
        
        scale(scale, scale);
        
    }
    
    /** Makes the ball move at its current velocity and bounce whenever it hits the edge of the screen. */
    public void move(Component screen) {
        x += dx;
        y += dy;
        
        int w = screen.getSize().width;
        int h = screen.getSize().height;
        
        if(x > w) {
            x = w;
            if(dx > 0)
                dx *= -1;
        }
        
        if(y > h) {
            y = h;
            if(dy > 0)
                dy *= -1;
        }
        
        if(x < 0) {
            x = 0;
            if(dx < 0)
                dx *= -1;
        }
        
        if(y < 0) {
            y = 0;
            if(dy < 0)
                dy *= -1;
        }
    }
    
    /** Draws a green circle. */
    public void draw(Graphics2D g) {
        if(isRed)
          g.setColor(new Color(0xFF5555));
        else
          g.setColor(new Color(0x55FF55));
        g.fillOval(0,0,32,32);
        
        Stroke origStroke = g.getStroke();
        
        if(isRed)
          g.setColor(new Color(0xAA2222));
        else
          g.setColor(new Color(0x22AA22));
        g.drawOval(0,0,32,32);
        
        g.setStroke(origStroke);
    }
    
}

