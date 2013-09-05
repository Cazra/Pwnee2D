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

import java.awt.Image;
import java.io.File;
import java.io.Serializable;
import java.util.HashMap;

import pwnee.fileio.ObjectFileIO;

public class ImageLibrary {
  
  /** A mapping of names to serialized images. */
  public HashMap<String, Image> images = new HashMap<>();
  
  /** Whether the image library loads asynchronously. */
  public boolean isAsynchronous = false;;
  
  /** Whether the image library is busy loading (if it is asynchronous).*/
  public boolean isLoading = false;
  
  public ImageLibrary() {
  }
  
  /** Create this image library as a shallow copy of another image library. */
  public ImageLibrary(ImageLibrary other) {
    images = new HashMap<>(other.images);
  }
  
  /** Create this image library from serialized file. */
  public ImageLibrary(String path) {
    this(ImageLibrary.load(path));
  }
  
  
  /** Returns the unserialized Image associated with key. */
  public Image get(String key) {
    try {
      Image result = images.get(key);
      return result;
    }
    catch (Exception e) {
      System.err.println("ImageLibrary could not get image for " + key);
      return null;
    }
  }
  
  
  
  /** Serializes an image and binds it to key. */
  public void put(String key, Image img) {
    try {
      images.put(key, img);
    }
    catch (Exception e) {
      System.err.println("ImageLibrary could not put image for " + key);
    }
  }
  
  
  
  /** Removes a key and its image from the library. */
  public Image remove(String key) {
    try {
      Image result = images.remove(key);
      return result;
    }
    catch (Exception e) {
      System.err.println("ImageLibrary could not get image for " + key);
      return null;
    }
  }
    
    
  /** 
   * Inserts an image from a file/resource into this library with the given key. 
   * It will try to load the image from a file first, then as a resource.
   */
  public Image loadImg(String key, String path) {
    try {
      ImageLoader il = new ImageLoader();
      Image img = il.load(path);
      put(key, img);
      return img;
    }
    catch(Exception e) {
      System.err.println("ImageLibrary could not load image for " + key + " at " + path);
      return null;
    }
  }
  
  
  /** Saves this image library to a serialized file. */
  public void save(String path) {
    ObjectFileIO ofio = new ObjectFileIO();
    HashMap<String, SerializedImage> result = new HashMap<>();
    
    for(String key : images.keySet()) {
      Image img = images.get(key);
      result.put(key, new SerializedImage(img));
    }
    
    ofio.saveObject(result, path);
  }
  
  
  /** Loads an image library from a serialized file. */
  public static ImageLibrary load(String path) {
    ObjectFileIO ofio = new ObjectFileIO();
    ImageLibrary result = new ImageLibrary();
    
    HashMap<String, SerializedImage> serial = (HashMap<String, SerializedImage>) ofio.loadObject(path);
    
    for(String key : serial.keySet()) {
      Image img = serial.get(key).toImage();
      result.put(key, img);
    }
    
    return result;
  }
}

