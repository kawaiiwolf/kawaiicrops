package com.kawaiiwolf.kawaiicrops.item;

import com.kawaiiwolf.kawaiicrops.lib.Constants;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModItems {

	public static ItemHungerPotion HungerPotion = new ItemHungerPotion();
	
	public static void register(){

		GameRegistry.registerItem(HungerPotion, Constants.MOD_ID + ".hungerpotion");
    	
	}
}
