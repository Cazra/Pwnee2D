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
import java.util.HashMap;
import java.util.Collection;

/** A class that manages and plays instances of loaded sound resources. */
public class SoundPlayer {
   /** A HashMap storing our library of loaded sound resources. */
	public HashMap<String,Sound> sounds;
   
   /** The sound player's global sampled sound volume. */
	public double volume;
	
   /** Creates a SoundPlayer with no loaded sounds. */
	public SoundPlayer() {
		sounds = new HashMap<String,Sound>();
		volume = 1.0;
	}
	
	
	/** Plays a Sound from a file. If the Sound's file string is already in the SoundPlayer's library, it saves time by obtaining it from the library where it is already loaded. If it is not in the library, it loads the Sound into the library and plays it. */
	public Sound play(String path) {
		Sound newSound;
		
		newSound = load(path);
      if(volume == 0)
			return newSound;
		newSound.play(volume);
		
		return newSound;
	}
	
	/** Loads a Sound from a file into the SoundPlayer's library. Returns the loaded Sound. */
	public Sound load(String path) {
		Sound newSound;
		
		if(sounds.containsKey(path))
			newSound = sounds.get(path);
		else {
			newSound = new Sound(path);
			sounds.put(path,newSound);
		}
		return newSound;
	}
	
   /** Returns the current number of sound instances being played by this SoundPlayer. */
	public int soundsPlaying()
	{
		int count = 0;
		
		Collection<Sound> soundsIterable = sounds.values();
		for(Sound sound : soundsIterable)
		{
			count += sound.instances;
		}
		return count;
	}
}
