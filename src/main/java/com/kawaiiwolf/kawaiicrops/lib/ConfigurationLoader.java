package com.kawaiiwolf.kawaiicrops.lib;

import java.io.File;
import java.util.Iterator;

import com.kawaiiwolf.kawaiicrops.block.BlockKawaiiCrop;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;

public class ConfigurationLoader {

	// Parent folder for configuration files.
	private String configFolder = null;
	
	// Dump a list of Block/Item names to a config file.
	private static boolean DumpIDs = false;
	
	public ConfigurationLoader(String configFolder) {
		this.configFolder = configFolder;
	}
	
	public static final String GENERAL_CROP_COMMENT = "" +
			"Here you'll list the names of all the plants that you want the mod to generate. Make sure each crop\n"+
			"name is lower case and has no spaces or punctuation. You can separate these with commas or spaces.\n"+
			"Once this list is completed, a new entry will be created in the blocks.cfg file to hold all the\n"+
			"details for this plant. Plants must appear in this list to be included in the mod.\n"+
			"\n"+
			"Bad Name: Snow Peas\n"+
			"Good Name: snowpeas\n"+
			"\n"+
			"S:Crops=snowpea tomato broccoli";
	
	public static final String REFERENCE_DROPTABLES_COMMENT = "" +
			"A drop table is defined with the following syntax, in BNF:\n"+
			"\n"+
			"<drop-table> ::= <items> | <items> \"|\" <drop-table>\n"+
			"     <items> ::= <item> | <item> \",\" <items>\n"+
			"      <item> ::= <item-name> | <item-name> \" \" <num-drops> | <item-name> \" \" <num-drops> \" \" <weight>\n"+
			" <item-name> ::= \"seed\" | \"crop\" | \"nothing\" | <minecraft-item-name>\n"+
			" <num-drops> ::= <integer-between-1-and-64>\n"+
			"    <weight> ::= <integer-between-1-and-64>\n"+
			"\n"+
			"\n"+
			"The keywords 'seed', 'crop', and 'nothing' are shorthand so you don't have to type out the fully\n"+
			"qualified names for the products of a crop (or empty blocks.) Number of drops and weight default to \n"+
			"one if not supplied. Here are some examples of how to use this:\n"+
			"\n"+
			"\n"+
			"Example 1:\n"+
			"   S:DropTable=seed\n"+
			"\n"+
			"Drops a single seed 100% of the time. This is great for breaking unripe plants.\n"+
			"\n"+
			"\n"+
			"Example 2:\n"+
			"   S:DropTable=seed 1, seed 2, seed 3 | crop 1 1, crop 2 2, crop 3 1 | nothing 1 99, minecraft:diamond 1 1\n"+
			"\n"+
			"This drops between 1 and 3 seeds, all with equal chance. It will also drop one crop 1 in 4 times, two\n"+
			"crops 2 in 4 times and three crops 1 in 4 times. Lastly, it has a 1 in 100 chance of dropping a diamond.\n"+
			"\n"+
			"\n"+
			"Example 3:\n"+
			"   S:DropTable=seed 2 | minecraft:carrot, minecraft:potato, minecraft:apple 1 2\n"+
			"\n"+
			"This drops two seeds and either a carrot, potato or apple with a 25%, 25% or 50% chance, respectively.\n"+
			"\n"+
			"\n"+
			"Example 4:\n"+
			"   S:DropTable=nothing\n"+
			"\n"+
			"No drops.\n";

	
	public void loadConfiguration_PreInit() {
		Configuration cfg_general = new Configuration(new File(configFolder + Constants.CONFIG_GENERAL));
		Configuration cfg_blocks = new Configuration(new File(configFolder + Constants.CONFIG_BLOCKS));
		Configuration cfg_items = new Configuration(new File(configFolder + Constants.CONFIG_ITEMS));
		Configuration cfg_recipies = new Configuration(new File(configFolder + Constants.CONFIG_RECIPIES));
		
		Configuration[] configs = {cfg_general, cfg_blocks, cfg_items, cfg_recipies};
		
		for (int i = 0; i < 4; i++) configs[i].load();
		
		cfg_general.setCategoryComment(Configuration.CATEGORY_GENERAL, "Global Settings for KawaiiCraft");
		cfg_general.setCategoryComment("Reference: Drop Table Help", REFERENCE_DROPTABLES_COMMENT);
		DumpIDs = cfg_general.getBoolean("DumpNames", Configuration.CATEGORY_GENERAL, DumpIDs, "Creates a list of Block and Item Names in the configuration directory ?");
		
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
	
	public void loadConfiguration_PostInit() {
		
		if (DumpIDs) dumpIDs();
		
	}
	
	private void dumpIDs() {
		
		File f = new File(configFolder + Constants.CONFIG_DUMP);
		
		// Try to clear it out if it exists. Fresh File
		try { if (f.exists()) f.delete(); } catch (Exception e) { }
		
		String blockList = "", itemList = "";
		
		Iterator<Block> blocks = NamespaceHelper.getBlockIterator();
		while (blocks.hasNext())
			blockList += NamespaceHelper.getBlockName(blocks.next()) + "\n";
		
		Iterator<Item> items = NamespaceHelper.getItemIterator();
		while (items.hasNext())
			itemList += NamespaceHelper.getItemName(items.next()) + "\n";
		
		Configuration config = new Configuration(f);
		config.load();
		config.setCategoryComment("Blocks", blockList);
		config.setCategoryComment("Items", itemList);
		config.save();
	}

	private BlockKawaiiCrop loadBlock(Configuration config, String name) {

		if (name == null || name.length() == 0) return null;

		String category = "KawaiiCrops: " + name;
		BlockKawaiiCrop b = new BlockKawaiiCrop(name);
		
		// Get crop variables from config file
		b.Enabled = config.getBoolean("Enabled", category, false, "Is this a block in minecraft ? Defaults to false to allow you to configure before putting it in game.");
		b.CropStages = config.getInt("States", category, b.CropStages, 2, 8, "Number of crop states ?  Valid values are between 2 and 8. (Ex: Carrots = 4, Wheat = 8)");
		b.RenderType = (config.getString("RenderType", category, "Hash", "How will the crop render ? Valid values are 'Hash' (Ex: Carrots) or 'Cross' (Ex: Mushrooms)").toLowerCase() == "cross" ? BlockKawaiiCrop.EnumRenderType.CROSS : BlockKawaiiCrop.EnumRenderType.HASH);
		b.MaxHeight = config.getInt("MaxHeight", category, b.MaxHeight, 1, 32, "How many blocks tall will this crop grow ?");
		b.MaxHeightRequiredToRipen = config.getBoolean("MaxHeightRequired", category, b.MaxHeightRequiredToRipen, "Does the plant need to be at max height before lower blocks are ready to harvest ?");
		b.MultiHarvest = config.getBoolean("MultiHarvest", category, b.MultiHarvest, "Upon harvesting this crop, does it grow back to an earlier, unripe state ?");
		b.UnripeMeta = config.getInt("UnripeMeta", category, b.UnripeMeta, 0, 7, "If MultiHarvest, upon harvesting the crop goes from Metadata value 7 to Meta ?");
		b.UnripeHardness = config.getFloat("UnripeHardness", category, b.UnripeHardness, 0.0f, 1.0f, "Hardness of unripe crops (0 breaks instantly. Set higher to prevent accidental harvests) ?");
		b.BoneMealMin = config.getInt("BoneMealMin", category, b.BoneMealMin, 0, 8, "Minimum stages of growth when using bonemeal.");
		b.BoneMealMax = config.getInt("BoneMealMax", category, b.BoneMealMax, 0, 8, "Maximum stages of growth when using bonemeal.");
		
		b.DropTableRipeString = config.getString("DropTableRipe", category, b.DropTableRipeString, "What is the drop table for Ripe crops ? Please see General.cfg to see how to use these.");
		b.DropTableUnripeString = config.getString("DropTableUnripe", category, b.DropTableUnripeString, "What is the drop table for Unripe crops ? Please see General.cfg to see how to use these.");

		Block tmp = NamespaceHelper.getBlockByName(config.getString("SeedsGrowOn", category, NamespaceHelper.getBlockName(b.CropGrowsOn), "What block does this grow on ? For a list of blocks, see [DumpNames] setting in General.cfg. (Note, 'minecraft:water' is an option.)"));
		b.CropGrowsOn = (tmp == Blocks.air ? b.CropGrowsOn : tmp);
				
		String category_seeds = category + " Seeds";
		
		b.SeedsEnabled = config.getBoolean("SeedsEnabled", category_seeds, b.SeedsEnabled, "Does this crop have seeds ?");
		b.SeedsEdible = config.getBoolean("SeedsEdible", category_seeds, b.SeedsEdible, "Are seeds also a food ?");
		b.SeedsHunger = config.getInt("SeedsHunger", category_seeds, b.SeedsHunger, 0, 20, "If SeedsEdible, how many half shanks of food does this restore ?");
		b.SeedsSaturation = config.getFloat("SeedsSaturation", category_seeds, b.SeedsSaturation, 0, 20.0f, "If SeedsEdible, what is the saturation level of this food ?");
		b.SeedsMysterySeedWeight = config.getInt("SeedsMysterySeedWeight", category_seeds, b.SeedsMysterySeedWeight, 0, 1000, "If mystery seeds enabled, what weight should this have on mystery seed results (0 = None)");
		b.SeedsToolTip = config.getString("SeedsToolTip", category_seeds, b.SeedsToolTip, "Tooltip for the seed in game.");

		String category_crops = category + " Crops";
		
		b.CropEnabled = config.getBoolean("CropEnabled", category_crops, b.CropEnabled, "Does this plant drop other crops ?");
		b.CropEdible = config.getBoolean("CropEdible", category_crops, b.CropEdible, "Are Crop also a food ?");
		b.CropHunger = config.getInt("CropHunger", category, b.CropHunger, 0, 20, "If CropEdible, how many half shanks of food does this restore ?");
		b.CropSaturation = config.getFloat("CropSaturation", category_crops, b.CropSaturation, 0, 20.0f, "If CropEdible, what is the saturation level of this food ?");
		b.CropToolTip = config.getString("CropToolTip", category_crops, b.CropToolTip, "Tooltip for the crop in game.");
		
		b.register();
		
		return b; 
	}
	
	private Item loadItem(Configuration config, String name){
		return null;
	}
	
	private void loadRecipe(Configuration config, String name){
		
	}
	
	
	
}
