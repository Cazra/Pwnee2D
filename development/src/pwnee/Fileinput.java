package gameEngine;

import java.util.Scanner;
import java.io.InputStream;
import java.io.File;

public class Fileinput
{
	private Scanner scanner;

	public Fileinput()
	{
		scanner = new Scanner(System.in);
	}
	
	public Fileinput(String filename)
	{
		try
		{
			scanner = new Scanner(new File(filename));
		}
		catch(Exception e)
		{}
	}
	
	public String[] getParsedLine(String delim)
	{
		return (scanner.nextLine()).split(delim);
	}
	
	public int[] getParsedInts(String delim)
	{
		String[] data = (scanner.nextLine()).split(delim);
		
		int[] ints = new int[data.length];
		
		for(int i = 0; i<data.length; i++)
		{
			ints[i] = Integer.parseInt(data[i]);
		}
		
		return ints;
	}
	
	public String getLine()
	{
		return scanner.nextLine();
	}
	
	public int getInt()
	{
		String str = scanner.nextLine();
		Integer myInt = new Integer(str);
		return myInt.intValue();
	}
	
	public double getDouble()
	{
		String str = scanner.nextLine();
		Double myInt = new Double(str);
		return myInt.doubleValue();
	}
	
	public boolean hasNext()
	{
		 return scanner.hasNextLine();
	}
}