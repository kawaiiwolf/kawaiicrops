package com.kawaiiwolf.kawaiicrops.lib;

import java.nio.ByteBuffer;

public class FNV {

	private static final int INIT = 0x811c9dc5;
	private static final int PRIME = 0x01000193;
	
	public static int hash(final byte[] k) 
	{ 
		return hash(INIT, k); 
	}
	
	private static int hash(int init, final byte[] k) 
	{
		int hash = init;
		
		for (int i = 0; i < k.length; i++) 
		{
			hash ^= k[i];
			hash *= PRIME;
		}
		
		return hash;
	}

	public static int hash(int k) 
	{ 
		return hash(INIT, k); 
	}
	
	private static int hash(int init, int k) 
	{ 
		return hash(init, ByteBuffer.allocate(4).putInt(k).array()); 
	}
	
	public static int hash (int x, int y, int z) 
	{ 
		return hash(hash(hash(x), y), z); 
	}
	
	public static int rand (int x, int y, int z, int max) 
	{
		return hash(x,y,z) % max;
	}
}
