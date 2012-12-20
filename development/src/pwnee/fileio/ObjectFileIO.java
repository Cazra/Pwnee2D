package pwnee.fileio;

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

import java.io.*;
import java.util.zip.*;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/** Saves/Loads serializeable objects to/from files, typically game-save data. Also provides optional encryption/decrption using a String-generated key. */
public class ObjectFileIO {
    
    /** option for encrypting/decrypting saved/loaded data. */
    public static int ENCRYPT = 1;
    
    /** option for encrypting/decrypting saved/loaded data. Same value as ENCRYPT, just given alternative name for readability. */
    public static int DECRYPT = 1;
    
    /** option for compressing/decompressing saved/loaded data. */
    public static int COMPRESS = 1 << 1;
    
    /** option for compressing/decompressing saved/loaded data. Same value as COMPRESS, just given alternative name for readability. */
    public static int DECOMPRESS = 1 << 1;
    
    /** A String used to generate an encryption/decryption key for objects that need to be saved/loaded with this class cryptographically. */
    public String encryptorString = "default";
    
    /** The algorithm used for cryptography on our files. By default, this is Blowfish since that is relatively fast. */
    public String encryptorAlgorithm = "Blowfish";
    
    public ObjectFileIO() {
        // does nothing.
    }
    
    
    
    
    /** Saves obj to the file specified by path with the given options. Returns true if successful. */
    public boolean saveObject(Serializable obj, String path, int options) {
      byte[] bytes = obj2bytes(obj);
      
      if((options & COMPRESS) > 0) {
        bytes = compressBytes(bytes);
      }
      
      if((options & ENCRYPT) > 0) {
        bytes = encryptBytes(bytes);
      }
      
      return saveBytes(bytes, path);
    }
    
    
    /** Loads an object from the file specified by path with the given options. */
    public Object loadObject(String path, int options) {
      byte[] bytes = loadBytes(path);
      
      if((options & DECRYPT) > 0) {
        bytes = decryptBytes(bytes);
      }
      
      if((options & DECOMPRESS) > 0) {
        bytes = decompressBytes(bytes);
      }
      
      return bytes2obj(bytes);
    }
    
    
    
    
    
    
    /** Saves obj to the file specified by path. Returns true if successful. */
    public boolean saveObject(Serializable obj, String path) {
        return saveObject(obj, path, 0);
    }
    
    /** Loads the object from the file specified by path. */
    public Object loadObject(String path) {
        return loadObject(path, 0);
    }
    
    
    
    
    
    /** Saves bytes to the file specified by path. Returns true if successful. */
    public boolean saveBytes(byte[] bytes, String path) {
      boolean result = false;
      
      FileOutputStream fos = null;
      
      try {
        fos = new FileOutputStream(path);
        fos.write(bytes);
        fos.close();
        
        result = true;
      }
      catch (Exception e) {
        System.err.println("ObjectFileIO - failed to write to file " + path);
      }
      
      return result;
    }
    
    /** Loads bytes from the file specified by path. Returns null if unsuccessful. */
    public byte[] loadBytes(String path) {
      File file = new File(path); 
      byte[] bytes = null;
      
      try {
        FileInputStream fis = new FileInputStream(file);
        bytes = new byte[(int) file.length()];
        fis.read(bytes);
        fis.close();
      }
      catch (Exception e) {
        System.err.println("ObjectFileIO - failed to load from file " + path);
      }
      
      return bytes;
    }
    
    
    
    
    
    
    
    
    
    /** 
     * Encrypts and saves obj a file specified by path. 
     * Typically these bytes are created from a string representing game-save data. 
     * @deprecated Use saveObject(Serializable obj, String path, int options) 
     *    with the option ObjectFileIO.ENCRYPT instead.
     */
    public void saveCryptoBytes(byte[] obj, String path) {
      byte[] cryptoObj = encryptBytes(obj);
      saveObject(cryptoObj, path);
    }
    
    /** 
     * Loads, decrypts, and returns an array of bytes from a file specified by path. 
     * Typically these bytes are used to construct a string representing game-save data. 
     * @deprecated Use loadObject(Serializable obj, String path, int options) 
     *    with the option ObjectFileIO.DECRYPT instead.
     */
    public byte[] loadCryptoBytes(String path) {
      byte[] cryptoObj = (byte[]) loadObject(path);
      byte[] obj = decryptBytes(cryptoObj);
      return obj;
    }
    
    
    
    
    
    
    
    
    
    /** Encypts an array of bytes. */
    public byte[] encryptBytes(byte[] orig) {
      try { 
        SecretKeySpec key = new SecretKeySpec(encryptorString.getBytes(), encryptorAlgorithm);
        Cipher cipher = Cipher.getInstance(encryptorAlgorithm);
        
        cipher.init(Cipher.ENCRYPT_MODE, key);
        
        byte[] cryptoObj = cipher.doFinal(orig);
        return cryptoObj;
      }
      catch (Exception e) {
        System.err.println("ObjectFileIO - encryption failed. ");
        return orig;
      }
    }
    
    /** Decrypts an array of bytes. */
    public byte[] decryptBytes(byte[] cryptoObj) {
      try { 
        SecretKeySpec key = new SecretKeySpec(encryptorString.getBytes(), encryptorAlgorithm);
        Cipher cipher = Cipher.getInstance(encryptorAlgorithm);
        
        cipher.init(Cipher.DECRYPT_MODE, key);
        
        byte[] orig = cipher.doFinal(cryptoObj);
        return orig;
      }
      catch (Exception e) {
        System.err.println("ObjectFileIO - decryption failed. ");
        return null;
      }
    }
    
    
    
    
    /** Compresses an array of bytes using GZIP. */
    public byte[] compressBytes(byte[] orig) {
      try { 
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream gzos = new GZIPOutputStream(baos);
        gzos.write(orig);
        
        baos.close();
        gzos.close();
        
        return baos.toByteArray();
      }
      catch (Exception e) {
        System.err.println("ObjectFileIO - compression failed. ");
        return orig;
      }
    }
    
    /** Decompresses an array of bytes using GZIP. */
    public byte[] decompressBytes(byte[] bytes) {
      try { 
        int numBytes = 0;
        
        // on our first pass, we will figure out the size of the uncompressed data.
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        GZIPInputStream gzis = new GZIPInputStream(bais);
        while(gzis.available() == 1) {
          gzis.read();
          numBytes++;
        }
        bais.close();
        gzis.close();
        
        byte[] orig = new byte[numBytes];

        // on our second pass, we will actually read the uncompressed data into a byte array.
        bais = new ByteArrayInputStream(bytes);
        gzis = new GZIPInputStream(bais);
        for(int i = 0; i < numBytes; i++) {
          orig[i] = (byte) gzis.read();
        }
        bais.close();
        gzis.close();
        
        return orig;
      }
      catch (Exception e) {
        System.err.println("ObjectFileIO - compression failed. ");
        return null;
      }
    }
    
    
    
    /** Transforms a serialized object into its corresponding array of bytes. */
    public byte[] obj2bytes(Serializable obj) {
      byte[] bytes = null;
      
      ByteArrayOutputStream baos = null;
      ObjectOutputStream oos = null;
      try {
        baos = new ByteArrayOutputStream();
        oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        bytes = baos.toByteArray();
        oos.close();
        baos.close();
      }
      catch (Exception e) {
        System.err.println("ObjectFileIO - failed converting object to bytes. ");
      }
      
      return bytes;
    }
    
    /** Transforms an array of bytes into an object. The user is responsible for knowing what type of object the bytes create. */
    public Object bytes2obj(byte[] bytes) {
      Object obj = null;
      
      ByteArrayInputStream bais = null;
      ObjectInputStream ois = null;
      try {
        bais = new ByteArrayInputStream(bytes);
        ois = new ObjectInputStream(bais);
        obj = ois.readObject();
        bais.close();
        ois.close();
      }
      catch (Exception e) {
        System.err.println("ObjectFileIO - failed converting bytes to object. ");
      }
      
      return obj;
    }
    
    
    
    public static void main(String[] args) {
        ObjectFileIO fileIO = new ObjectFileIO();
        String origString;
        String loadedString;
        
        origString = "The quick brown fox jumped over the lazy dog.";
        
        
        
        
        System.out.println("--- No encryption ---");
        
        System.out.println("Original string: " + origString);
        fileIO.saveObject(origString,"savedString.txt");
        
        loadedString = (String) fileIO.loadObject("savedString.txt");
        System.out.println("Loaded string: " + loadedString);
        
        
        System.out.println();
        
        System.out.println("--- Encryption with Blowfish using key 'cupcakes' ---");
        fileIO.encryptorString = "cupcakes";
        
        System.out.println("Original string: " + origString);
        fileIO.saveObject(origString, "cryptoString.txt", ObjectFileIO.ENCRYPT);
        
        loadedString = (String) fileIO.loadObject("cryptoString.txt", ObjectFileIO.DECRYPT);
        System.out.println("Loaded string: " + loadedString);
        
        
        
        System.out.println();
        
        System.out.println("--- Encryption with Blowfish using key 'cupcakes' | GZIP Compression ---");
        
        System.out.println("Original string: " + origString);
        fileIO.saveObject(origString, "cryptoStringCompressed.txt", ObjectFileIO.ENCRYPT | ObjectFileIO.COMPRESS);
        
        loadedString = (String) fileIO.loadObject("cryptoStringCompressed.txt", ObjectFileIO.DECRYPT | ObjectFileIO.DECOMPRESS);
        System.out.println("Loaded string: " + loadedString);
        
    }
}

