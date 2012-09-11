package gameEngine;

import javax.sound.sampled.*;
import java.io.*;
import java.net.URL;
import java.util.LinkedList;

public class Sound // implements LineListener
{
	public String path;
//	private AudioInputStream stream;
//	public DataLine.Info info;
	protected int instances;
	protected URL soundURL;

	
	public Sound(String name)
	{
		try
		{
			soundURL =  getClass().getClassLoader().getResource(name);
			loadFile(soundURL);
			
			instances = 0;
			path = name;
		}
		catch(Exception ex)
		{
			System.out.println("An error occured while loading the sound file: " + name);
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}

	}
	
	
	public void loadFile(URL soundFile)
	{
		try
		{
			// stream = AudioSystem.getAudioInputStream(soundFile); // obtains input stream from AudioSystem to read from the file.
		}
		catch(Exception e)
		{
			System.out.println("Error loading sound " + soundFile);
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void play(double volume)
	{
		try
		{
			SoundThread clip = new SoundThread(this);
			synchronized(this)
			{
				instances++;
			}
			clip.setVolume(volume);
			clip.start();
			
		}
		catch(Exception e)
		{
			System.out.println("Sound error: Error playing sound");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		
	}
	
	
	
	
	/*
	public void update(LineEvent le)
	{
		LineEvent.Type type = le.getType();
		
		if(type == LineEvent.Type.STOP)
		{
			Line line = le.getLine();
			line.close();
			synchronized(this)
			{
				instances--;
			}
		}
		
		
	}
	*/
}


class SoundThread extends Thread
{
	public SourceDataLine line;
	public AudioInputStream stream;
	private int bufferSize = 50;
	private Sound parent;
	
	public SoundThread(Sound parentSound)
	{
		try
		{
			parent = parentSound;
			//AudioInputStream stream = AudioSystem.getAudioInputStream(streamSource.getFormat(), streamSource);
			stream = AudioSystem.getAudioInputStream(parentSound.soundURL); // obtains input stream from AudioSystem to read from the file.
			AudioFormat format = stream.getFormat();
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, format); // obtains the sound file's line
			line = (SourceDataLine) AudioSystem.getLine(info); // loads the line into the line
			line.open(format); // opens the line onto the stream
		//	System.out.println("Sound buffer size: " + line.getBufferSize());
		//	line.addLineListener(parentSound);
		}
		catch(Exception e)
		{
			System.out.println("error playing sound");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void run()
	{
		try
		{
			//line.setFramePosition(0);
			line.start();
			int bytesRead = 0;
			byte[] soundData = new byte[bufferSize];
			while(bytesRead != -1)
			{
				bytesRead = stream.read(soundData,0,bufferSize);
				if(bytesRead >= 0)
				{
					line.write(soundData, 0, bytesRead);
				}
				else
				{
				//	line.drain();
					sleep(500);
					
					line.stop();
					line.close();
					line = null;
					stream.close();
					stream = null;
				}
			}
			
			
			
			synchronized(parent)
			{
				parent.instances--;
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	public void setVolume(double vol)
	{
		try
		{
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
		catch(Exception e)
		{
			System.out.println("set volume failed");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
}


