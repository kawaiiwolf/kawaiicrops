package com.kawaiiwolf.kawaiicrops.item;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModItems {

	public static ItemHungerPotion HungerPotion = new ItemHungerPotion();
	
	public static void register(){

		GameRegistry.registerItem(HungerPotion, HungerPotion.getUnlocalizedName());
    	
	}
}
