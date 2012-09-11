package gameEngine;

import java.awt.event.*;
import java.util.Hashtable;

public class Keyboard implements KeyListener
{
	public int lastTyped = 0;
	private Hashtable<Integer,Boolean> keysPressed; // a keycode is mapped to true if it is pressed, false if it isn't pressed.
	private Hashtable<Integer,Integer> keysTyped; // a keycode is mapped to true if it is typed, false if it wasn't just typed. 
	private boolean isPolled;
	private boolean anyKeyPressed;
	
	public Keyboard()
	{
		keysPressed = new Hashtable<Integer,Boolean>();
		
		keysTyped = new Hashtable<Integer,Integer>();
		
		isPolled = false;
		anyKeyPressed = false;
	}
	
	
	public void poll()
	{
		isPolled = true;
	}
	
	
	public void keyPressed(KeyEvent e)
	{
		keysPressed.put(e.getKeyCode(),true);
		Integer typedVal = keysTyped.get(e.getKeyCode());
		
		if(typedVal == null)
		{
			keysTyped.put(e.getKeyCode(),1);
		}
		else
		{
			typedVal += 1;
			if(typedVal > 2)
				typedVal = 2;
			keysTyped.put(e.getKeyCode(),typedVal.intValue());
			
		}
		
		lastTyped = e.getKeyCode();
		anyKeyPressed = true;
	}
	
	public void keyTyped(KeyEvent e)
	{
		//lastTyped = e.getKeyCode();
	}
	
	public void keyReleased(KeyEvent e)
	{
		keysPressed.put(new Integer(e.getKeyCode()),new Boolean(false));
		keysTyped.put(new Integer(e.getKeyCode()),0);
		
		anyKeyPressed = false;
	}
	
	public boolean isPressed(int keycode)
	{
		//return false;
		try
		{
			Boolean mykey = keysPressed.get(keycode);
			Integer typedVal = keysTyped.get(keycode);
			if(mykey == null)
				return false;
			else if(mykey)
			{
				keysTyped.put(keycode,typedVal.intValue() + 1);
				return true;
			}
			return false;
		}
		catch(Exception e)
		{
			return false;
		}
		
	}
	
	public boolean isTyped(int keycode)
	{
		try
		{
			Integer typedVal = keysTyped.get(keycode);
			if(typedVal == null)
				return false;
			if(typedVal.intValue() > 0 && typedVal.intValue() < 10)
			{
				keysTyped.put(keycode,typedVal.intValue() + 10);
				return true;
			}
			return false;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	public int isAnyKeyTyped()
	{
		if(anyKeyPressed)
			return lastTyped;
		else
			return -1;
	}
	

}
