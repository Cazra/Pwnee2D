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

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.*;
import java.io.Serializable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;


/** A class that can force the application to wait while it finishes loading new Images. */
public class SerializedImage implements Serializable {
   
  /** The byte array containing all of the image's pixels. */
  public byte[] pixels;

   
  /** 
   * Creates an ImageLoader used only for loading images from a file. 
   * This doesn't initialize a MediaTracker and is only used by this 
   * class's static methods. 
   */
  public SerializedImage(Image img) {
    try {
      int width = img.getWidth(null);
      int height = img.getHeight(null);
      
      BufferedImage bimg = new BufferedImage(width,height, BufferedImage.TYPE_INT_ARGB);
      Graphics2D g = bimg.createGraphics();
      g.drawImage(img, 0, 0, null);
      g.dispose();
      
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ImageIO.write(bimg,"png",baos);
      pixels = baos.toByteArray();
    }
    catch (Exception e) {
      System.err.println("Error converting image to bytes." + e);
    }
  }
   
   
   
  /** 
   * Transforms the pixel bytes of the serialized image back into their 
   * original image. 
   */
  public Image toImage() {
    try {
      ByteArrayInputStream bais = new ByteArrayInputStream(pixels);
      return ImageIO.read(bais);
    }
    catch (Exception e) {
      System.err.println("Error converting bytes to image.");
      return null;
    }
  }
}

