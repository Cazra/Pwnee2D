package pwnee.sound;

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

import javax.sound.sampled.*;
import java.io.*;
import java.net.URL;
import java.util.LinkedList;

/** A sampled sound that is created from a file and can be played multiple times, even at overlapping intervals. It does this by starting new threads to play instances of itself in. These threads close when their sound finishes. */
public class Sound {
   /** A count of the number of instances of this sound currently playing */
	protected int instances;
   
   /** The loaded resource for the sound's file. */
	protected URL soundURL;

	/** Creates the sound from a file and loads its resource so that we can play instances of it as SoundThreads. */
	public Sound(String name) {
		try {
			soundURL =  getClass().getClassLoader().getResource(name);
			instances = 0;
		}
		catch(Exception ex) {
			System.out.println("An error occured while loading the sound file: " + name);
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}

	}
	
   /** 
    * Plays an instance of this sound in its own thread at a specified volume. 
    * @param volume     the desired volume to play the sound at. This is a number in the range [0.0, 1.0], where 0.0 is silent and 1.0 is the current system volume for sampled sounds.
    */
	public void play(double volume) {
		try {
			SoundThread clip = new SoundThread(this);
			synchronized(this) {
				instances++;
			}
			clip.setVolume(volume);
			clip.start();
		}
		catch(Exception e) {
			System.out.println("Sound error: Error playing sound");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}

/** A thread created by a Sound to play an instance of itself in. */
class SoundThread extends Thread {
   /** A sampled sound thingy. */
	public SourceDataLine line;
   
   /** The stream for reading the sampled sound from its resource. */
	public AudioInputStream stream;
   
   /** Another sound thingy. */
	private int bufferSize = 50;
   
   /** The Sound object that this thread plays an instance of. */
	private Sound parent;
	
   /** Creates the instance thread for a Sound. */
	public SoundThread(Sound parentSound) {
		try {
			parent = parentSound;
         
         // obtains input stream from AudioSystem to read from the file.
			stream = AudioSystem.getAudioInputStream(parentSound.soundURL); 
			AudioFormat format = stream.getFormat();
         
         // ready the sound's data line.
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, format); 
         line = (SourceDataLine) AudioSystem.getLine(info); 
			line.open(format);
		}
		catch(Exception e) {
			System.out.println("error playing sound");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
   /** Plays this sound instance */
	public void run() {
		try {
			line.start();
			int bytesRead = 0;
			byte[] soundData = new byte[bufferSize];
			while(bytesRead != -1) {
				bytesRead = stream.read(soundData,0,bufferSize);
				if(bytesRead >= 0) {
					line.write(soundData, 0, bytesRead);
				}
				else { 
					sleep(500);
					
					line.stop();
					line.close();
					line = null;
					stream.close();
					stream = null;
				}
			}
			
         // The sound is finished playing. Decrement the instance count for the sound and end the sound instance thread.
			synchronized(parent) {
				parent.instances--;
			}
		}
		catch(Exception e) {
			System.err.println("SoundThread - error playing instance of sound " + parent.soundURL.getFile());
		}
	}
	
	/** 
    * Sets the volume of this sound instance.
    * @param volume     the desired volume to play the sound at. This is a number in the range [0.0, 1.0], where 0.0 is silent and 1.0 is the current system volume for sampled sounds.
    */
	public void setVolume(double vol) {
		try {
			FloatControl volCtrl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
			if(vol > 1.0)
				vol = 1.0;
			if(vol < 0.0)
				vol = 0.0;
			
			// tricking, confusing sound math stuff.
			
			float dB = (float) (Math.log(vol) / Math.log(10.0) * 20.0);
			if(dB > volCtrl.getMaximum())
				dB = volCtrl.getMaximum();
			if(dB < volCtrl.getMinimum())
				dB = volCtrl.getMinimum();
			volCtrl.setValue(dB);
		}
		catch(Exception e) {
			System.out.println("set volume failed");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
}


