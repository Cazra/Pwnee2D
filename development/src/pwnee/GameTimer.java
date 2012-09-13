package pwnee;

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

import javax.swing.Timer;
import java.awt.event.ActionListener;

public class GameTimer extends Timer {
    
    /** The preferred delay between the GameTimer's events in milliseconds. This is meant to be read-only, as changing this won't actually change the GameTimer's delay. */
    public int preferredDelay;
    
    /** Keeps track of the current actual frame rate for the application running on this GameTimer. */
    public double fpsCounter = 0.0;
    
    /** Used to update fpsCounter */
    private int ticks = 0;
    
    /** Used to update fpsCounter */
    private long startTime = System.currentTimeMillis();
    
    /** Creates the game timer and assigns it an initial delay and a listener which will handle the timer's events. */
    public GameTimer(int delay, ActionListener listener) {
        super(delay, listener);
    }
    
    
    
    /** Updates fpsCounter. This should be called only once per frame in your application. */
    public void updateFrameRateCounter() {
        ticks++;
        long currentTime = System.currentTimeMillis();
        
        if(currentTime - startTime >= 500) {
            fpsCounter = 1000.0 * ticks/(1.0*currentTime - startTime);
            fpsCounter = Math.round(fpsCounter*10)/10.0;
            startTime = currentTime;
            ticks = 0;
        }
    }
    
    /** 
     * Sets the GameTimer's millisecond delay to reflect a desired frame rate. 
     * @param fps       The desired frame rate in frames per second.
     */
    public void setFPS(int fps) {
        int millis = (int) (1000.0 / fps + 0.5);
        preferredDelay = millis;
        setDelay(millis);
    }
}
