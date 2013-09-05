package example;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import pwnee.*;
import pwnee.sprites.Sprite;
import pwnee.collisions.QuadTree;

public class SpritesPanel extends GamePanel {
    
    public ArrayList<SupmuwSprite> supmuws = new ArrayList<SupmuwSprite>();
    public QuadTreeSprite qtSprite;
    
    public SpritesPanel() {
        super();
        this.setPreferredSize(new Dimension(640, 480));
        
        // Load the SupmuwSprites' images
        SupmuwSprite.loadImages(this.imgLoader);
        
        // create our supmuws.
        for(int i = 0; i < 1; i++) {
            supmuws.add(new SupmuwSprite(GameMath.rand.nextInt(640), GameMath.rand.nextInt(480), GameMath.rand.nextDouble()*4+0.1));
        }
        
        // create a sprite for visualizing our quadtrees.
        qtSprite = new QuadTreeSprite();
    }
    
    public void logic() {
        // Create a quadtree and populate it with our supmuws.
        QuadTree qTree = new QuadTree(0,0,getSize().width,getSize().height,10);
        for(SupmuwSprite supmuw : supmuws) {
          qTree.insert(supmuw);
        }
        
        qtSprite.qTree = qTree;
        
        // query for collisions among the supmuws.
        for(SupmuwSprite supmuw : supmuws) {
            supmuw.isDead = false;
            
            List<Sprite> cList = qTree.query(supmuw);
            for(Sprite s : cList) {
                SupmuwSprite other = (SupmuwSprite) s;
                
                // if this supmuw collides with another supmuw, change their animation to appear dead.
                if(supmuw.collidesWith(other))
                    supmuw.isDead = true;
            }
        }
        
        
        
        // Try commenting out the above code for quadtree collision detection and uncomment this block. See how slow it becomes using the naive 
        // collision detection approach without a quadtree.
        /*
        for(SupmuwSprite supmuw : supmuws) {
            supmuw.isDead = false;
            
            for(SupmuwSprite other : (List<SupmuwSprite>) supmuws.clone()) {
                // if this supmuw collides with another supmuw, change their animation to appear dead.
                if(supmuw.collidesWith(other))
                    supmuw.isDead = true;
            }
        }
        */
        
        // move and animate the supmuws
        for(SupmuwSprite supmuw : supmuws) {
            supmuw.move(this);
            supmuw.animate();
        }
        
        
    }
    
    public void paint(Graphics g) {
        // set the background color to white.
        this.setBackground(new Color(0xFFFFFF));
        
        // clear the panel with the background color.
        super.paint(g);
        
        // cast our Graphics as a Graphics2D, since it has more features and g is actually a Graphics2D anyways.
        Graphics2D g2D = (Graphics2D) g;
        
        // render the supmuws
        for(SupmuwSprite supmuw : supmuws) {
           supmuw.render(g2D);
        }
        
        // render the quadtree.
        qtSprite.render(g2D);
        
        // Set our drawing color to black
        g2D.setColor(new Color(0x000000));
        
        // display the current frame rate.
        g2D.drawString("" + timer.fpsCounter, 10,32);
    }
}
