package gameEngine;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
*	MidiPlayer
*	author: Stephen Lindberg
*	Last modified: Oct 14, 2011
*	
*	A class that allows midi files to be loaded and played.
**/

public class MidiPlayer
{
   private Sequence seq;
   private Sequencer seqr;
   private Synthesizer synth;
   private Receiver receiver;
   private File midiFile;
   private String midiID;
   private boolean loaded;
   private boolean usingHardwareSoundbank;
   private float defaultTempo;
   
   // CONSTRUCTORS
   
   public MidiPlayer()
   {
      loaded = false;
      try
      {
         seqr = MidiSystem.getSequencer();
         synth = MidiSystem.getSynthesizer();
         
         // print the user's midi device info
         System.out.println("Setting up Midi Player...");
         System.out.println("MidiDeviceInfo: ");
         for(MidiDevice.Info info : MidiSystem.getMidiDeviceInfo())
         {
            System.out.println("\t" + info.getName() + ": " +info.getDescription());
         }
         System.out.println();
         
         // obtain the receiver. This will be used for changing volume.
         
         Soundbank soundbank = synth.getDefaultSoundbank();
         if(soundbank == null)
         {
            receiver = MidiSystem.getReceiver();
            usingHardwareSoundbank = true;
            System.out.println("using hardware soundbank");
         }
         else
         {
            synth.loadAllInstruments(soundbank);
            receiver = synth.getReceiver();
            usingHardwareSoundbank = false;
            System.out.println("using default software soundbank:" + soundbank);
         }
         seqr.getTransmitter().setReceiver(receiver);
      }
      catch(Exception e)
      {
         System.out.println("MIDI error: It appears your system doesn't have a MIDI device or your device is not working.");
      }
   }
   
   /**
   *	MidiPlayer(String fileName)
   *	Constructor that also loads an initial midi file.
   *	Preconditions: fileName is the name of the midi file to be loaded. 
   *	Postconditions: The MidiPlayer is created and loaded with the midi specified by fileName.
   **/
   
   public MidiPlayer(String fileName)
   {
      this();
      load(fileName);
   }
   
   
   // DATA METHODS
   
   /**
   *	load(String fileName)
   *	loads a midi file into this MidiPlayer.
   *	Preconditions: fileName is the name of the midi file to be loaded.
   *	Postconditions: fileName is loaded and is ready to be played.
   **/
   
   public void load(String fileName)
   {
      this.unload();
      try
      {
         URL midiURL =  getClass().getClassLoader().getResource(fileName);
         seq = MidiSystem.getSequence(midiURL);
         
         seqr.open();
         synth.open();

         // load our sequence into the sequencer.
         
         seqr.setSequence(seq);
         loaded = true;
         defaultTempo = seqr.getTempoInBPM();
      }
      catch(IOException ioe)
      {
         System.out.println("MIDI error: Problem occured while reading " + midiFile.getName() + ".");
      }
      catch(InvalidMidiDataException imde)
      {
         System.out.println("MIDI error: " + midiFile.getName() + " is not a valid MIDI file or is unreadable.");
      }
      catch(Exception e)
      {
         System.out.println("MIDI error: Unexplained error occured while loading midi.");
      }
   }
   
   /**
   *	unload()
   *	Unloads the current midi from the MidiPlayer and releases its resources from memory.
   **/
   
   public void unload()
   {
      this.stop();
      seqr.close();
      synth.close();
      midiFile = null;
      loaded = false;
   }
   
   // OTHER METHODS
   
   /**
   *	setMidiID(String id)
   *	associates a String ID with the current midi.
   *	Preconditions: id is the ID we are associating with the current midi.
   **/
   
   public void setMidiID(String id)
   {
      midiID = id;
   }
   
   /**
   *	getMidiID(String id)
   *
   **/
   
   public String getMidiID()
   {
      return new String(midiID);
   }
   
   /**
   *	play(boolean reset)
   *	plays the currently loaded midi.
   *	Preconditions: reset tells our midi whether or nor to begin playing from the start of the midi file's current loop start point.
   *	Postconditions: If reset is true, then the loaded midi begins playing from its loop start point (default 0). 
   *		If reset is false, then the loaded midi resumes playing from its current position.
   **/
   
   public void play(boolean reset)
   {
	//	return;
      if(reset)
         seqr.setTickPosition(seqr.getLoopStartPoint());
      seqr.start();
   }
   
   /**
   *	stop()
   *	Pauses the current midi if it was playing.
   **/
   
   public void stop()
   {
      if(seqr.isOpen())
         seqr.stop();
   }
   
   /**
   *	isRunning()
   *	Returns true if the current midi is playing. Returns false otherwise.
   **/
   
   public boolean isRunning()
   {
      return seqr.isRunning();
   }
   
   
   
   /**
   *	getTempo()
   *	Returns the current tempo of the MidiPlayer in BPM (Beats per Minute).
   **/
   
   public float getTempo()
   {
      return seqr.getTempoInBPM();
   }
   
   /**
   *	loop(int times)
   *	Sets the current midi to loop from start to finish a specific number of times.
   *	Preconditions: times is the number of times we want our midi to loop.
   *	Postconditions: The current midi is set to loop times times. 
   *		If times = -1, the current midi will be set to loop infinitely.
   **/
   
   public void loop(int times)
   {
      loop(times,0,-1);
   }
   
   /**
   *	loop(int times)
   *	Sets the current midi to loop from a specified start point to a specified end point a specific number of times.
   *	Preconditions: times is the number of times we want our midi to loop.
   *		start is our loop's start point in ticks.
   *		end is our loop's end point in ticks.
   *	Postconditions: The current midi is set to loop from tick start to tick end times times. 
   *		If times = -1, the current midi will be set to loop infinitely.
   **/
   
   public void loop(int times, long start, long end)
   {
      if(start < 0)
         start = 0;
      if(end > seqr.getSequence().getTickLength() || end <= 0)
         end = seqr.getSequence().getTickLength();
         
      if(start >= end && end != -1)
         start = end-1;
      
      seqr.setLoopStartPoint(start);
      seqr.setLoopEndPoint(end);
      
      if(times == -1)
         seqr.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
      else
         seqr.setLoopCount(times);
      
   }
   
   /**
   *	resetTempo()
   *	Resets the MidiPlayer's tempo the the initial tempo of its current midi.
   **/
   
   public void resetTempo()
   {
      this.changeTempo(this.defaultTempo);
   }
   
   /**
   *	changeTempo(float bpm)
   *	Changes the MidiPlayer's current tempo.
   *	Preconditions: bpm is the MidiPlayer's new tempo in BPM (Beats per Minute).
   *	Postconditions: The MidiPlayer's current tempo is set to bpm BPM.
   **/
   
   public void changeTempo(float bpm)
   {
      double lengthCoeff = bpm/seqr.getTempoInBPM();
      
      seqr.setLoopStartPoint((long) (seqr.getLoopStartPoint()*lengthCoeff));
      seqr.setLoopEndPoint((long) (seqr.getLoopEndPoint()*lengthCoeff));
      
      seqr.setTempoInBPM(bpm);
   }
   
   
   public void setVolume(double vol)
   {
      System.out.println("Midi volume change request: " + vol);
      
      try 
      {
         if(usingHardwareSoundbank)
         {
            ShortMessage volumeMessage = new ShortMessage();
            for ( int i = 0; i < 16; i++ ) 
            {
               volumeMessage.setMessage( ShortMessage.CONTROL_CHANGE, i, 7, (int)(vol*127) );
               receiver.send( volumeMessage, -1 );
            }
         }
         else
         {
            MidiChannel[] channels = synth.getChannels();
            
            for( int c = 0; c < channels.length; c++ )
            {
               if(channels[c] != null) {
                  channels[c].controlChange( 7, (int)( vol*127) );
               }
            }
         }
         // Very important!
        //seqr.setSequence(seq);

      } 
      catch ( Exception e ) 
      {
         e.printStackTrace();
      }
   }

}