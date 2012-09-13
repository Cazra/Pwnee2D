package pwnee.sound;

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
