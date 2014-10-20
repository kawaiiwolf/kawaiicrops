package com.kawaiiwolf.kawaiicrops.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class PotionEffectHelper 
{
	public static final int MIN_ID = 1;
	public static final int MAX_ID = 23;

	public ArrayList<Potion> Effects = new ArrayList<Potion>();
	
	public PotionEffectHelper(String effects) 
	{
		Pattern pattern = Pattern.compile("([0-9]{1,2})[ ]*([0-9]{1,2})[ ]*([0-9]{1,2})[ ]*(1|0?[.][0-9]*)");
		
		for (String effect : effects.split("[|]"))
		{
			Matcher match = pattern.matcher(effect.replaceAll("[^0-9 .]", ""));
	
			Potion p = new Potion();
			if (match.find())
			{
				p.PotionID = Integer.parseInt(match.group(1));
				p.Duration = Integer.parseInt(match.group(2));
				p.Amplitude = Integer.parseInt(match.group(3));
				p.Chance = Float.parseFloat(match.group(4));
				
				if (p.PotionID < MIN_ID || p.PotionID > MAX_ID)
					p.PotionID = 0;
			}
			if (p.PotionID != 0)
				Effects.add(p);
		}
	}
	
	public class Potion
	{
		public int PotionID = 0;
		public int Duration = 1;
		public int Amplitude = 1;
		public float Chance = 1.0f;
		
		public Potion() {}
		
		public PotionEffect getPotionEffect() 
		{
			if (this.PotionID == 0) 
				return null;
			return new PotionEffect(PotionID, 20 * Duration, Amplitude);
		}
	}
}
