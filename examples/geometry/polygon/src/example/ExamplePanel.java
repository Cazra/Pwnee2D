package example;

import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

import pwnee.*;
import pwnee.geom.Polygon2D;

public class ExamplePanel extends GamePanel {
    
    /** List of points for polygon currently under construction by the user. */
    public List<Point2D> points = new ArrayList<>();
    
    /** The last polygon created by the user. */
    public Polygon2D lastPoly = null;
    
    /** The list of polygons created. */
    public List<Polygon2D> polies = new ArrayList();
    
    
    public ExamplePanel() {
      super();
    }
    
    public void logic() {
      if(mouse.justLeftPressed) {
        points.add(new Point2D.Double(mouse.x, mouse.y));
      }
      
      if(mouse.justRightPressed) {
        if(!points.isEmpty()) {
          Point2D[] pointsArray = new Point2D[points.size()];
          points.toArray(pointsArray);
          lastPoly = new Polygon2D(pointsArray);
          polies.add(lastPoly);
          points.clear();
        }
      }
    }
    
    
    public void paint(Graphics g) {
        // set the background color to white.
        this.setBackground(new Color(0xFFFFFF));
        
        // clear the panel with the background color.
        super.paint(g);
        
        // cast our Graphics as a Graphics2D, since it has more features and g is actually a Graphics2D anyways.
        Graphics2D g2D = (Graphics2D) g;
        
        // Set our drawing color to red
        g2D.setColor(new Color(0xFF0000));
        
        for(Point2D point : points) {
          g2D.fillRect((int) point.getX(), (int) point.getY(), 1, 1);
        }
        
        for(Polygon2D poly : polies) {
          poly.draw(g2D);
        }
    }
}
