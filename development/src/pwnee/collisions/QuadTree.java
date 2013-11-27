package pwnee.collisions;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import pwnee.sprites.Sprite;

/** 
 * Quadtrees are recursive data structures used for efficient collision 
 * detection within a bounded area. They work by dividing an area of the
 * world into 4 recursive quadrants. Sprites are then assigned to a subquadrant
 * based on their size and position. 
 *
 * When a sprite is queried in the quadtree, it returns a list of sprites that
 * reside in the same quadtree quadrant path. If another sprite is in this list,
 * then it is possible (but not certain) that it is colliding with the queried
 * sprite, and collision detection can then be used to confirm this.
 *
 * They are most useful for performing many-to-many collision detections. It
 * effectively reduces the many-to-many collision problem's time from O(n^2) to
 * O(n*log4_n).
 */
public class QuadTree {
  /** The x position of this quadrant's left border. */
  public double minX;
  
  /** The y position of this quadrant's top border. */
  public double minY;
  
  /** The x position of this quadrant's center. */
  public double midX;
  
  /** The y position of this quadrant's center. */
  public double midY;
  
  /** The x position of this quadrant's right border. */
  public double maxX;
  
  /** The y position of this quadrant's bottom border. */
  public double maxY;
  
  /** The depth level of this quadtree. The topmost quadtree in the structure */
  public int depth;
  
  /** The maximum depth allowed for insertion/query. */
  public int maxDepth;
  
  /** The list of Sprites contained by this quadrant. */
  public ArrayList<Sprite> sprites = new ArrayList<Sprite>();
  
  /** 
   * The 4 subquadrants contained by this quadrant. They are in the following 
   * order: northwest, northeast, southwest, southeast.
   */
  public QuadTree[] quadrants = new QuadTree[4];
  
  public double[] qMinXs = new double[4];
  public double[] qMinYs = new double[4];
  public double[] qMaxXs = new double[4];
  public double[] qMaxYs = new double[4];
  
  /** Constructor for a quadrant. */
  public QuadTree(double minX, double minY, double maxX, double maxY, int d, int md) {
    this.minX = minX;
    this.minY = minY;
    this.maxX = maxX;
    this.maxY = maxY;
    this.depth = d;
    this.maxDepth = md;
    
    midX = minX/2 + maxX/2;
    midY = minY/2 + maxY/2;
    
    // determine dimensions for subquadrants.
    qMinXs[0] = minX;
    qMaxXs[0] = midX;
    qMinYs[0] = minY;
    qMaxYs[0] = midY;
    
    qMinXs[1] = midX;
    qMaxXs[1] = maxX;
    qMinYs[1] = minY;
    qMaxYs[1] = midY;
    
    qMinXs[2] = minX;
    qMaxXs[2] = midX;
    qMinYs[2] = midY;
    qMaxYs[2] = maxY;
    
    qMinXs[3] = midX;
    qMaxXs[3] = maxX;
    qMinYs[3] = midY;
    qMaxYs[3] = maxY;
  }
  
  /** Constructor for the top quadtree in a structure. */
  public QuadTree(double minX, double minY, double maxX, double maxY, int md) {
    this(minX,minY,maxX,maxY,0,md);
  }
  
  
  /** Inserts a sprite into the quadtree. */
  public boolean insert(Sprite s) {
    // Do not insert sprites that are marked as destroyed.
    if(s.isDestroyed())
      return false;
    
    // Do not insert sprites that are completely outside of the quadtree's area.
    Rectangle2D bbox = s.getCollisionBox();
    if(!this.intersects(bbox))
      return false;
    
    // recursively insert the sprite into the smallest possible subquadrant.
    _insertRec(s, bbox);
    
    return true;
  }
  
  /** Recursively inserts the sprite into the smallest subquadrant that it completely fits it.*/
  private void _insertRec(Sprite s, Rectangle2D bbox) {
    if(depth < maxDepth) {
      // determine whether to insert this sprite into a subquadrant.
      for(int i = 0; i < 4; i++) {
        if(quadContains(bbox,i)) {
          if(quadrants[i] == null) 
            quadrants[i] = new QuadTree(qMinXs[i], qMinYs[i], qMaxXs[i], qMaxYs[i], depth+1, maxDepth);
          quadrants[i]._insertRec(s, bbox);
          return;
        }
      }
    }
    
    sprites.add(s);
  }
  
  
  /** 
   * Queries for a sprite inside the quadtree and returns the accumulated sprite  
   * list of the every subquadrant that can completely contain it. 
   */
  public List<Sprite> query(Sprite s) {
    // Do not query sprites that are marked as destroyed.
    if(s.isDestroyed())
      return new ArrayList<Sprite>();
    
    // Do not query sprites that are completely outside of the quadtree's area.
    Rectangle2D bbox = s.getCollisionBox();
    if(!this.intersects(bbox))
      return new ArrayList<Sprite>();
    
    return _queryRec(s, bbox);
  }
  
  /** Recursively queries for a sprite's collision neighbors in the quadtree. */
  private List<Sprite> _queryRec(Sprite s, Rectangle2D bbox) {    
    // query inside any quadrants intersecting bbox.
    for(int i = 0; i < 4; i++) {
      if(quadrants[i] != null && quadIntersects(bbox, i)) {
        
        List<Sprite> result = quadrants[i]._queryRec(s,bbox);
        result.addAll(sprites);
        return result;
      }
    }
    
    // It fit in none of the quadrants, so return this quadtree's sprite list.
    return (List<Sprite>) sprites.clone();
  }
  
  
  
  /** Returns true iff bbox is intersecting quadrant i. */
  public boolean quadIntersects(Rectangle2D bbox, int i) {
    double minX = qMinXs[i];
    double minY = qMinYs[i];
    double maxX = qMaxXs[i];
    double maxY = qMaxYs[i];
    return !(bbox.getX() > maxX || bbox.getY() > maxY || bbox.getX() + bbox.getWidth() < minX || bbox.getY() + bbox.getHeight() < minY);
  }
  
  /** Returns true iff bbox is completely contained by quadrant i. */
  public boolean quadContains(Rectangle2D bbox, int i) {
    double minX = qMinXs[i];
    double minY = qMinYs[i];
    double maxX = qMaxXs[i];
    double maxY = qMaxYs[i];
    return (bbox.getX() >= minX && bbox.getY() >= minY && bbox.getX() + bbox.getWidth() <= maxX && bbox.getY() + bbox.getHeight() <= maxY);
  }
  
  
  /** 
   * Checks if a rectangular area (such as a sprite's bounding box) is 
   * intersecting the area of this quadrant. 
   */
  public boolean intersects(Rectangle2D bbox) {
    return !(bbox.getX() > maxX || bbox.getY() > maxY || bbox.getX() + bbox.getWidth() < minX || bbox.getY() + bbox.getHeight() < minY);
  }
  
  /** 
   * Checks if a rectangular area (such as a sprite's bounding box) is
   * completed contained in the area of this quadrant.
   */
  public boolean contains(Rectangle2D bbox) {
    return (bbox.getX() >= minX && bbox.getY() >= minY && bbox.getX() + bbox.getWidth() <= maxX && bbox.getY() + bbox.getHeight() <= maxY);
  }
}