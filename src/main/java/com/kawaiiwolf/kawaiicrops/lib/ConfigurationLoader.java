package com.kawaiiwolf.kawaiicrops.lib;

import java.io.File;

import com.kawaiiwolf.kawaiicrops.block.BlockKawaiiCrop;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;

public class ConfigurationLoader {

	
	public static final String GENERAL_CROP_COMMENT = "" +
			"Here you'll list the names of all the plants that you want the mod to generate."+"\n"+
			"Make sure each crop name is lower case and has no spaces or punctuation. You"+"\n"+
			"can separate these with commas or spaces. Once this list is completed, a new"+"\n"+
			"entry will be created in the blocks.cfg file to hold all the details for this"+"\n"+
			"plant. Plants must appear in this list to be included in the mod."+"\n"+
			""+"\n"+
			"Bad Name: Snow Peas"+"\n"+
			"Good Name: snowpeas"+"\n"+
			""+"\n"+
			"example: \"snowpea tomato broccoli\"";
	
	public static void loadConfiguration(String configFolder) {
		Configuration cfg_general = new Configuration(new File(configFolder + Constants.CONFIG_GENERAL));
		Configuration cfg_blocks = new Configuration(new File(configFolder + Constants.CONFIG_BLOCKS));
		Configuration cfg_items = new Configuration(new File(configFolder + Constants.CONFIG_ITEMS));
		Configuration cfg_recipies = new Configuration(new File(configFolder + Constants.CONFIG_RECIPIES));
		
		Configuration[] configs = {cfg_general, cfg_blocks, cfg_items, cfg_recipies};
		
		for (int i = 0; i < 4; i++) configs[i].load();
		
		cfg_general.setCategoryComment(Configuration.CATEGORY_GENERAL, "Global Settings for KawaiiCraft");

		
		/* Begin processing Crop Blocks.
		 * - Read crop list from General Config & Cleanup
		 * - For each crop, loadBlock() and register.
		 */
		cfg_general.setCategoryComment("KawaiiCrop Crops", GENERAL_CROP_COMMENT);
		String cropsRaw = cfg_general.getString("Crops", "KawaiiCrop Crops", "snowpea tomato broccoli","Crop List");
		String[] cropsParsed = cropsRaw.toLowerCase().replaceAll("[^a-z, ]", "").replaceAll("  ", " ").replaceAll(",,", ",").split("[, ]");
		
		for (int i = 0; i < cropsParsed.length; i++)
		{
			BlockKawaiiCrop b = loadBlock(cfg_blocks, cropsParsed[i]);
		}
		
		
		
		for (int i = 0; i < 4; i++) configs[i].save();
	}
	
	private static BlockKawaiiCrop loadBlock(Configuration config, String name) {
		
		
		
		return (name == null || name.length() == 0 ? null : new BlockKawaiiCrop(name)); 
	}
	
	private static Item loadItem(Configuration config, String name){
		return null;
	}
	
	private static void loadRecipe(Configuration config, String name){
		
	}
	
	
	
}
