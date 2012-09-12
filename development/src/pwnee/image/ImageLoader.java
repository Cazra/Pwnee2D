package pwnee.image;

import java.awt.MediaTracker;
import java.awt.Component;
import java.awt.Image;

/** A class that can force the application to wait while it finishes loading new Images. */
public class ImageLoader {
   
   /** This is the object that actually waits on the images. */
	private MediaTracker mt;
   
   /** Used by mt to track the images it is waiting on. */
	private int nextId;
   
   /** This will pretty much always be your application's GamePanel class. */
	private Component parent;
	
   /** Creates an empty ImageLoader assigned to a rendering Component. */
	public ImageLoader(Component parent) {
		this.parent = parent;
		this.reset();
	}
	
   /** Empties the ImageLoader. */
	public void reset() {
		mt = new MediaTracker(this.parent);
		nextId = 0;
	}
	
   /** Adds an Image to the ImageLoader for it to wait on. */
	public void addImage(Image img) {
		mt.addImage(img, this.nextId);
		this.nextId++;
	}
	
   /** Forces the application to wait while the ImageLoader finishes loading all the Images currently assigned to it. Afterwards it empties itself.*/
	public void waitForAll() {
		try {
			mt.waitForAll();	// wait for the filtered image to load before drawing anything.
		}
		catch ( InterruptedException e ) {
         System.err.println("ImageLoader - failed to load an Image.");
			Thread.currentThread().interrupt();
		}
		reset();
	}
	
	
}

