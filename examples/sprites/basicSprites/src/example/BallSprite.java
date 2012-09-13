package example;

import java.awt.*
import pwnee.sprites.Sprite;
import pwnee.GameMath;


public class BallSprite extends Sprite {
    
    // The ball's current x velocity.
    public double dx;
    
    // The ball's current y velocity.
    public double dy;
    
    /** Creates the BallSprite with a random velocity. */
    public BallSprite(x,y) {
        super(x,y);
        dx = GameMath.rand.nextDouble()*6-3;
        dy = GameMath.rand.nextDouble()*6-3;
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
        g.setColor(new Color(0xAAFFAA));
        g.fillOval(-16,-16,32,32);
        
        g.setColor(new Color(0x77AA77));
        g.drawOval(-16,-16,32,32);
    }
    
}

