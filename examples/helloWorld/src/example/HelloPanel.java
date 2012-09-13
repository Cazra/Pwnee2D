package example;

import java.awt.*;
import pwnee.*;

public class HelloPanel extends GamePanel {
    
    public HelloPanel() {
        super();
        this.start(60);
    }
    
    public void logic() {
        // Do nothing.
    }
    
    public void paint(Graphics g) {
        // set the background color to white.
        this.setBackground(new Color(0xFFFFFF));
        
        // clear the panel with the background color.
        super.paint(g);
        
        // cast our Graphics as a Graphics2D, since it has more features and g is actually a Graphics2D anyways.
        Graphics2D g2D = (Graphics2D) g;
        
        // Set our drawing color to black
        g2D.setColor(new Color(0x000000));
        
        // display the text "Hello World!" spazzing out near the middle of the window.
        g2D.drawString("Hello World!", 320 + GameMath.rand.nextInt(32),200 + GameMath.rand.nextInt(32));
        
        // display the current frame rate.
        g2D.drawString("" + timer.fpsCounter, 10,32);
    }
}
