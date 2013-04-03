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

import java.awt.Component;
import java.awt.Graphics2D;
import pwnee.input.*;
import pwnee.image.ImageLoader;

/**
 * A level is used as a abstract layer for game logic and rendering.
 * It can be used as an actual level for a game, or as a containing logic layer 
 * for groups of levels, forming a level hierarchy.
 */
public abstract class Level {
   
   /** The level above this level. Optional. */
	public Level parent;
   
   /** The GamePanel this level is running in. */
   public GamePanel game;
   
   /** Convenient reference to the game's Keyboard. */
   public Keyboard keyboard;
   
   /** Convenient reference to the game's mouse. */
   public Mouse mouse;
   
   /** True iff this level has finished loading. */
   public boolean hasLoaded = false;
	
   /** Creates the level and calls its loadData method to load its resources, if any. */
	public Level(GamePanel game, Level parent) {
		this.parent = parent;
		this.game = game;
      this.keyboard = game.keyboard;
      this.mouse = game.mouse;
      
      loadData();
      hasLoaded = true;
	}
   
   
   /** Creates the level without a containing level. */
   public Level(GamePanel game) {
      this(game, null);
   }
	
   
   // The user is expected to implement all of the following methods:
	
	/** Loads the resources needed by this level. */
	public abstract void loadData();
   
   /** Performs cleanup duties for when we are done using this level, */
   public abstract void clean();
   
   /** Performs 1 iteration through the level's logic. May call logic of lower levels. */
	public abstract void logic();

   /** Performs rendering for this level. May call render of lower levels. */
	public abstract void render(Graphics2D g2D);
}

