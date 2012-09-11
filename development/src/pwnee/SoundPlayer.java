package gameEngine;

import javax.sound.sampled.*;
import java.util.HashMap;
import java.util.Collection;

public class SoundPlayer
{
	private HashMap<String,Sound> sounds;
	private double volume;
	private int soundsPlaying;
	
	public SoundPlayer()
	{
		sounds = new HashMap<String,Sound>();
		volume = 1.0;
		soundsPlaying = 0;
	}
	
	
	/**
	*	playSound(String path)
	*	creates and plays a Sound specified by path.
	*	Preconditions: path is the file path for the sound.
	*	Postconditions: The sound is loaded and begins playing. This method returns a reference to that sound.
	**/
	
	public Sound play(String path)
	{
	//	System.out.println("Playing sound: " + path);
			
		Sound newSound;
		
		if(volume == 0)
			return null;
		
		if(sounds.containsKey(path))
		{
		//	System.out.println("\tSound loaded from sound cache");
			newSound = sounds.get(path);
		}
		else
		{
		//	System.out.println("\tloading sound into sound cache");
			newSound = new Sound(path);
			sounds.put(path,newSound);
		}

		newSound.play(volume);
		
		return newSound;
	}
	
	/**
	*	load(String path)
	*	preloads a sound to be play instances of at a later time
	*	Preconditions: path is the file path for the sound.
	*	Postconditions: The sound is loaded. This method returns a reference to that sound.
	**/
	
	public Sound load(String path)
	{
	//	System.out.println("Loading sound: " + path);
			
		Sound newSound;
		
		if(sounds.containsKey(path))
		{
			newSound = sounds.get(path);
		}
		else
		{
			newSound = new Sound(path);
			sounds.put(path,newSound);
		}
		return newSound;
	}
	
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
	
	public int soundsLoaded()
	{
		return sounds.size();
	}
	
	
	public void setVolume(double vol)
	{
		this.volume = vol;
	}
	
	
	/**
	*	update()
	*	This method should be called at the end of your game's main timer loop. 
	*	It frees the memory from any Sounds that have finished playing.
	**/

	
	/*
	public void update()
	{
		ArrayList<Sound> garbageSounds = new ArrayList<Sound>();
		
		for(Sound sound : sounds)
		{
			if(!sound.clip.isOpen())
			{
				garbageSounds.add(sound);
			}
		}
		for(Sound sound : garbageSounds)
		{
		//	sound.close();
			sounds.remove(sound);
		}
		
	}
	
	
	
	public void update(LineEvent le)
	{
		LineEvent.Type type = le.getType();
		
		if(type == LineEvent.Type.STOP)
		{
			Line line = le.getLine();
			line.close();
		}
	}
	*/
}
