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

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/** Saves/Loads serializeable objects to/from files, typically game-save data. Also provides optional encryption/decrption using a String-generated key. */
public class ObjectFileIO {
    
    /** A String used to generate an encryption/decryption key for objects that need to be saved/loaded with this class cryptographically. */
    public String encryptorString = "default";
    
    /** The algorithm used for cryptography on our files. By default, this is Blowfish since that is relatively fast. */
    public String encryptorAlgorithm = "Blowfish";
    
    public ObjectFileIO() {
        // does nothing.
    }
    
    /** Saves obj to the file specified by path. */
    public void saveObject(Serializable obj, String path) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            
            oos.writeObject(obj);
            oos.close();
            fos.close();
        }
        catch (Exception e) {
            System.err.println("ObjectFileIO - failed to write to file " + path);
        }
    }
    
    /** Loads the object from the file specified by path. */
    public Object loadObject(String path) {
        Object result = null;
        
        try {
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fis);
            
            result = ois.readObject();
            ois.close();
            fis.close();
        }
        catch (Exception e) {
            System.err.println("ObjectFileIO - failed to load from file " + path);
        }
        
        return result;
    }
    
    
    /** Encrypts and saves obj a file specified by path. Typically these bytes are created from a string representing game-save data. */
    public void saveCryptoBytes(byte[] obj, String path) {
        try { 
            SecretKeySpec key = new SecretKeySpec(encryptorString.getBytes(), encryptorAlgorithm);
            Cipher cipher = Cipher.getInstance(encryptorAlgorithm);
            
            cipher.init(Cipher.ENCRYPT_MODE, key);
            
            byte[] cryptoObj = cipher.doFinal(obj);
            saveObject(cryptoObj, path);
        }
        catch (Exception e) {
            System.err.println("ObjectFileIO - failed to encrypt object destined to " + path);
        }
    }
    
    /** Loads, decrypts, and returns an array of bytes from a file specified by path. Typically these bytes are used to construct a string representing game-save data. */
    public byte[] loadCryptoBytes(String path) {
        try { 
            byte[] cryptoObj = (byte[]) loadObject(path);
            SecretKeySpec key = new SecretKeySpec(encryptorString.getBytes(), encryptorAlgorithm);
            Cipher cipher = Cipher.getInstance(encryptorAlgorithm);
            
            cipher.init(Cipher.DECRYPT_MODE, key);
            
            byte[] obj = cipher.doFinal(cryptoObj);
            return obj;
        }
        catch (Exception e) {
            System.err.println("ObjectFileIO - failed to encrypt object destined to " + path);
            return null;
        }
    }
    
    
    
    
    public static void main(String[] args) {
        ObjectFileIO fileIO = new ObjectFileIO();
        String origString;
        String loadedString;
        
        
        System.out.println("--- No encryption ---");
        
        origString = "The quick brown fox jumped over the lazy dog.";
        System.out.println("Original string: " + origString);
        fileIO.saveObject(origString,"savedString.txt");
        
        loadedString = (String) fileIO.loadObject("savedString.txt");
        System.out.println("Loaded string: " + loadedString);
        
        
        System.out.println();
        
        System.out.println("--- Encryption with Blowfish using key 'cupcakes' ---");
        fileIO.encryptorString = "cupcakes";
        
        origString = "The quick brown fox jumped over the lazy dog.";
        System.out.println("Original string: " + origString);
        fileIO.saveCryptoBytes(origString.getBytes(),"cryptoString.txt");
        
        loadedString = new String(fileIO.loadCryptoBytes("cryptoString.txt"));
        System.out.println("Loaded string: " + loadedString);
        
        
    }
}

