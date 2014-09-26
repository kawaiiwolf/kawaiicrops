package com.kawaiiwolf.kawaiicrops.lib;

import java.io.File;

import com.kawaiiwolf.kawaiicrops.block.BlockKawaiiCrop;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;

public class ConfigurationLoader {

	
	public static final String GENERAL_CROP_COMMENT = "" +
			"Here you'll list the names of all the plants that you want the mod to generate. Make sure each crop\n"+
			"name is lower case and has no spaces or punctuation. You can separate these with commas or spaces.\n"+
			"Once this list is completed, a new entry will be created in the blocks.cfg file to hold all the\n"+
			"details for this plant. Plants must appear in this list to be included in the mod.\n"+
			"\n"+
			"Bad Name: Snow Peas\n"+
			"Good Name: snowpeas\n"+
			"\n"+
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
		String cropsRaw = cfg_general.getString("Crops", "KawaiiCrop Crops", "snowpea","Crop List");
		String[] cropsParsed = cropsRaw.toLowerCase().replaceAll("[^a-z, ]", "").replaceAll("  ", " ").replaceAll(",,", ",").split("[, ]");
		
		for (int i = 0; i < cropsParsed.length; i++)
		{
			BlockKawaiiCrop b = loadBlock(cfg_blocks, cropsParsed[i]);
		}
		
		
		
		for (int i = 0; i < 4; i++) configs[i].save();
	}

	private static BlockKawaiiCrop loadBlock(Configuration config, String name) {
		
		String category = "KawaiiCrops: " + name;
		BlockKawaiiCrop b = new BlockKawaiiCrop(name);
		
		// Get crop variables from config file
		b.Enabled = config.getBoolean("Enabled", category, false, "Is this a block in minecraft ? Defaults to false to allow you to configure before putting it in game.");
		b.CropStages = config.getInt("CropStates", category, b.CropStages, 2, 8, "Number of crop states ?  Valid values are between 2 and 8. (Ex: Carrots = 4, Wheat = 8)");
		b.RenderType = (config.getString("RenderType", category, "Hash", "How will the crop render ? Valid values are 'Hash' (Ex: Carrots) or 'Cross' (Ex: Mushrooms)").toLowerCase() == "cross" ? BlockKawaiiCrop.EnumRenderType.CROSS : BlockKawaiiCrop.EnumRenderType.HASH);
		b.MaxHeight = config.getInt("MaxHeight", category, b.MaxHeight, 1, 32, "How many blocks tall will this crop grow ?");
		b.MaxHeightRequiredToRipen = config.getBoolean("MaxHeightRequired", category, b.MaxHeightRequiredToRipen, "Does the plant need to be at max height before lower blocks are ready to harvest ?");
		b.MultiHarvest = config.getBoolean("MultiHarvest", category, b.MultiHarvest, "Upon harvesting this crop, does it grow back to an earlier, unripe state ?");
		b.UnripeMeta = config.getInt("UnripeMeta", category, b.UnripeMeta, 0, 7, "If MultiHarvest, upon harvesting the crop goes from Metadata value 7 to Meta ?");
		b.UnripeHardness = config.getFloat("UnripeHardness", category, b.UnripeHardness, 0.0f, 1.0f, "Hardness of unripe crops (0 breaks instantly. Set higher to prevent accidental harvests) ?");
		b.BoneMealMin = config.getInt("BoneMealMin", category, b.BoneMealMin, 0, 8, "Minimum stages of growth when using bonemeal.");
		b.BoneMealMax = config.getInt("BoneMealMax", category, b.BoneMealMax, 0, 8, "Maximum stages of growth when using bonemeal.");
		
		return (name == null || name.length() == 0 ? null : b); 
	}
	
	private static Item loadItem(Configuration config, String name){
		return null;
	}
	
	private static void loadRecipe(Configuration config, String name){
		
	}
	
	
	
}
