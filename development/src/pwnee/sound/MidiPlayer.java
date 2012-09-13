package pwnee.sound;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;


/** 
 * A class that can load and play midi files. 
 * It is a known issue that there are problems with setting the midis' volume.
 * I've tried everything on the Internet to fix it, and I'm pretty convinced that it is an issue with
 * Java's midi code, as it has worked in the past. 
 */
public class MidiPlayer {
   
   /** The midi sequence that is currently loaded. */
   private Sequence seq;
   
   /** Our midi sequencer. */
   private Sequencer seqr;
   
   /** Our midi synthesizer */
   private Synthesizer synth;
   
   /** Used to send messages to the midi device for changing the volume. */
   private Receiver receiver;
   
   /** Flag telling us whether a midi is loaded in the MidiPlayer. */
   private boolean loaded;
   
   /** Flag is true if we are using the hardware soundbank instead of a software soundbank. Currently this is only used for setting midi volume. */
   private boolean usingHardwareSoundbank;
   
   /** The default tempo of the loaded midi. */
   private float defaultTempo;
   
   // CONSTRUCTORS
   
   public MidiPlayer() {
      loaded = false;
      try {
         seqr = MidiSystem.getSequencer();
         synth = MidiSystem.getSynthesizer();
         
         // print the user's midi device info
         System.out.println("Setting up Midi Player...");
         System.out.println("MidiDeviceInfo: ");
         
         for(MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) 
            System.out.println("\t" + info.getName() + ": " +info.getDescription());
         System.out.println();
         
         // obtain the receiver. This will be used for changing volume.
         Soundbank soundbank = synth.getDefaultSoundbank();
         if(soundbank == null) {
            receiver = MidiSystem.getReceiver();
            usingHardwareSoundbank = true;
            System.out.println("using hardware soundbank");
         }
         else {
            synth.loadAllInstruments(soundbank);
            receiver = synth.getReceiver();
            usingHardwareSoundbank = false;
            System.out.println("using default software soundbank:" + soundbank);
         }
         
         seqr.getTransmitter().setReceiver(receiver);
      }
      catch(Exception e) {
         System.out.println("MIDI error - Could not initialize the MidiPlayer.");
      }
   }
   
   /**
   *	MidiPlayer(String fileName)
   *	Constructor that also loads an initial midi file.
   *	Preconditions: fileName is the name of the midi file to be loaded. 
   *	Postconditions: The MidiPlayer is created and loaded with the midi specified by fileName.
   **/
   
   public MidiPlayer(String fileName) {
      this();
      load(fileName);
   }
   
   
   // DATA METHODS
   
   /** Loads a midi from a file into the MidiPlayer. */
   public void load(String fileName)
   {
      this.unload();
      try {
         // Load the midi sequence from our file.
         URL midiURL =  getClass().getClassLoader().getResource(fileName);
         seq = MidiSystem.getSequence(midiURL);
         
         // ready the sequencer and synthesizer to play our midi.
         seqr.open();
         synth.open();

         // load our sequence into the sequencer.
         seqr.setSequence(seq);
         loaded = true;
         defaultTempo = seqr.getTempoInBPM();
      }
      catch(Exception e) {
         System.out.println("MIDI error: Problem occured while reading " + fileName + ".");
      }
   }
   
   /** Stops and unloads the midi currently loaded in the MidiPlayer. */
   public void unload() {
      this.stop();
      seqr.close();
      synth.close();
      midiFile = null;
      loaded = false;
   }
   
   // OTHER METHODS

   /** 
    * Plays the midi currently loaded in the MidiPlayer. 
    * @param reset   If this is true, then the midi will play from the beginning. If false, it will resume playing from its current position.
    */
   public void play(boolean reset) {
      if(reset)
         seqr.setTickPosition(seqr.getLoopStartPoint());
      seqr.start();
   }
   
   /** Pauses the currently playing midi without reseting it to the beginning. */
   public void pause() {
      if(seqr.isOpen())
         seqr.stop();
   }
   
   /** Stops the currently playing midi and resets its position to the beginning. */
   public void stop() {
      if(seqr.isOpen())
         seqr.stop();
      seqr.setTickPosition(seqr.getLoopStartPoint());
   }
   
   /** Returns true if a midi is currently playing. Otherwise false. */
   public boolean isPlaying() {
      return seqr.isRunning();
   }
   
   
   
   /** Returns the current tempo of the MidiPlayer in BPM (Beats per Minute). */
   public float getTempo() {
      return seqr.getTempoInBPM();
   }
   
   
   /**
    * Sets the current midi to loop from a specified start point to a specified end point a specific number of times.
    * @param times    number of times to repeat. -1 to repeat infinitely.
    * @param start    The loop's starting tick position.
    * @param end      The loop's ending tick posiition. If -1, this will be last tick position of the currently loaded midi.
    */
   public void loop(int times, long start, long end) {
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
    * Sets the current midi to loop from start to end a number of times. 
    * @param times     number of times to loop. -1 to loop infinitely.
    */
   public void loop(int times) {
      loop(times,0,-1);
   }
   
   /** Sets the current midi to loop infinitely from start to end. */
   public void loop() {
      loop(-1, 0, -1);
   }
   
   /** Restores the MidiPlayer's tempo to the current midi's default tempo. */
   public void resetTempo() {
      this.changeTempo(this.defaultTempo);
   }
   
   /** 
    * Changes the current tempo of the MidiPlayer. 
    * @param bpm     the desired dempo in beats per minute.
    */
   public void changeTempo(float bpm) {
      double lengthCoeff = bpm/seqr.getTempoInBPM();
      
      seqr.setLoopStartPoint((long) (seqr.getLoopStartPoint()*lengthCoeff));
      seqr.setLoopEndPoint((long) (seqr.getLoopEndPoint()*lengthCoeff));
      
      seqr.setTempoInBPM(bpm);
   }
   
   
   /** 
    * Attempts to set the master volume of the currently playing midi. 
    * @param vol     A number in the range [0.0, 1.0], where 0.0 is silent and 1.0 is the current system midi volume.
    */
   public void setVolume(double vol) {
      System.out.println("Midi volume change request: " + vol);
      
      // The master volume of a midi can be changed by sending it control change message 7 with an integer in range [0,127] to all its channels.
      try {
         if(usingHardwareSoundbank) {
            ShortMessage volumeMessage = new ShortMessage();
            for ( int i = 0; i < 16; i++ ) {
               volumeMessage.setMessage( ShortMessage.CONTROL_CHANGE, i, 7, (int)(vol*127) );
               receiver.send( volumeMessage, -1 );
            }
         }
         else {
            MidiChannel[] channels = synth.getChannels();
            
            for( int c = 0; c < channels.length; c++ ) {
               if(channels[c] != null) {
                  channels[c].controlChange( 7, (int)( vol*127) );
               }
            }
         }
      } 
      catch ( Exception e ) {
         e.printStackTrace();
      }
   }

}