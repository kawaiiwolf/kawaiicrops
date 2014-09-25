package com.kawaiiwolf.kawaiicrops.lib;

import java.io.File;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;

public class ConfigurationLoader {

	public static void loadConfiguration(String configFolder) {
		Configuration cfg_general = new Configuration(new File(configFolder + Constants.CONFIG_GENERAL));
		Configuration cfg_blocks = new Configuration(new File(configFolder + Constants.CONFIG_BLOCKS));
		Configuration cfg_items = new Configuration(new File(configFolder + Constants.CONFIG_ITEMS));
		Configuration cfg_recipies = new Configuration(new File(configFolder + Constants.CONFIG_RECIPIES));
		
		Configuration[] configs = {cfg_general, cfg_blocks, cfg_items, cfg_recipies};
		
		for (int i = 0; i < 4; i++) configs[i].load();
		
		cfg_general.setCategoryComment(Configuration.CATEGORY_GENERAL, "Global Settings for KawaiiCraft");
		cfg_general.setCategoryComment("KawaiiCraft Crops", "line1\nline2");
		
		for (int i = 0; i < 4; i++) configs[i].save();
	}
	
	private static Block loadBlock(Configuration config, String name) {
		return null;
	}
	
	private static Item loadItem(Configuration config, String name){
		return null;
	}
	
	private static void loadRecipe(Configuration config, String name){
		
	}
}
