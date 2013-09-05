package pwnee.image;

/*======================================================================
 * 
 * Pwnee - A lightweight 2D Java game engine
 * 
 * Copyright (c) 2012 by Stephen Lindberg (sllindberg21@students.tntech.edu)
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: 
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
======================================================================*/

import java.awt.MediaTracker;
import java.awt.Component;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.net.URL;

/** A class that can force the application to wait while it finishes loading new Images. */
public class ImageLoader {
   
   /** This is the object that actually waits on the images. */
	private MediaTracker mt;
   
   /** Used by mt to track the images it is waiting on. */
	private int nextId;
   
   /** This will pretty much always be your application's GamePanel class. */
	private Component parent;
  
  /** Whether or not images should be loaded asynchronously. */
  public boolean isAsynchronous = false;
  
  /** Whether or not the ImageLoader is busy loading images (if it is asynchronous). */
  public boolean isLoading = false;
  
  /** Metric for displaying a loading bar for an asynchronous ImageLoader. */
  public int progressMax = 0;
  
  public int progressValue = 0;
	
   /** Creates an empty ImageLoader assigned to a rendering Component. */
	public ImageLoader(Component parent) {
		this.parent = parent;
		this.reset();
	}
   
   /** Creates an ImageLoader without specifying a rendering Component. */
   public ImageLoader() {
      this(new JPanel());
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
	
   /** 
    * Forces the application to wait while the ImageLoader finishes loading 
    * all the Images currently assigned to it. Afterwards it empties itself.
    */
	public void waitForAll() {
    if(isAsynchronous) {
      isLoading = true;
      
      try {
        final int _nextId = nextId;
        Thread imgLoaderThread = new Thread() {
          public void run() {
            progressMax = _nextId;
            progressValue = 0;
            
            for(int i = 0; i < progressMax; i++) {
              progressValue = i;
              try {
                mt.waitForID(i);
              }
              catch (Exception e) {
              }
            }
            progressValue = progressMax;
            
            isLoading = false;
            reset();
          }
        };
      }
      catch (Exception e) {
        e.printStackTrace();
        isLoading = false;
        reset();
      }
    }
    else {
      try {
        mt.waitForAll();
      }
      catch ( InterruptedException e ) {
           System.err.println("ImageLoader - failed to load an Image.");
        Thread.currentThread().interrupt();
      }
      reset();
    }
	}
	
  
  /** Attempts to load an image first as an external file, then as a resource. */
  public Image load(String path) {
    Image result = loadFile(path);
    if(result == null) {
      result = loadResource(path);
    }
    return result;
  }
  
   
   /** 
    * Loads an image from a resource file (a file included in your game's 
    * jar/classpath) and return that image. 
    */
   public Image loadResource(String path) {
      URL imageURL =  this.getClass().getClassLoader().getResource(path);
      Image image = Toolkit.getDefaultToolkit().getImage(imageURL);
      return image;
   }
   
  
  /** 
   * Loads an image from an external file 
   * (a file not included in the game's jar/classpath). 
   */
  public Image loadFile(String path) {
    return loadFile(new File(path));
  }
  
  public Image loadFile(File file) {
    try {
      return ImageIO.read(file);
    }
    catch(IOException e) {
      return null;
    }
  }
  
  
  /** 
   * A static convenience method for loading an image from a resource file 
   * (a file included in your game's jar/classpath). 
   * This blocks until the image is entirely loaded.
   */
  public static Image loadResourceAndWait(String path) {
    ImageLoader il = new ImageLoader();
    Image image = il.loadResource(path);
    il.addImage(image);
    il.waitForAll();
    return image;
  }
  
  /** 
   * A static convenience method for loading an image from an external file 
   * (a file not included in your game's jar/classpath). 
   * This blocks until the image is entirely loaded.
   */
  public static Image loadFileAndWait(String path) {
    ImageLoader il = new ImageLoader();
    Image image = il.loadFile(path);
    il.addImage(image);
    il.waitForAll();
    return image;
  }
}

