package example;

import java.awt.*;

import pwnee.collisions.QuadTree;
import pwnee.sprites.Sprite;

/** A sprite used to visualize a quadtree collision structure. */
public class QuadTreeSprite extends Sprite {

    public QuadTree qTree;
    
    public QuadTreeSprite() {
        super(0,0);
    }
    
    public void draw(Graphics2D g) {
        if(qTree == null)
            return;
        
        g.setColor(new Color(0xFF5555));
        _drawRec(g, qTree);
    }
    
    /** Recursively draws all the existing subquadrants of our quadtree. */
    private void _drawRec(Graphics2D g, QuadTree q) {
        int x = (int) q.minX;
        int y = (int) q.minY;
        int w = (int) (q.maxX - q.minX);
        int h = (int) (q.maxY - q.minY);
        g.drawRect(x,y,w,h);
        
        for(QuadTree quad : q.quadrants) {
            if(quad != null)
              _drawRec(g, quad);
        }
    }
    
}
