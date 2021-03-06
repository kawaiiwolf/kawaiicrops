package com.kawaiiwolf.kawaiicrops.lib;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

import com.kawaiiwolf.kawaiicrops.block.BlockKawaiiBarrel;
import com.kawaiiwolf.kawaiicrops.block.BlockKawaiiCake;
import com.kawaiiwolf.kawaiicrops.block.BlockKawaiiCrop;
import com.kawaiiwolf.kawaiicrops.block.BlockKawaiiTreeBlocks;
import com.kawaiiwolf.kawaiicrops.event.EventKawaiiLivingDrop;
import com.kawaiiwolf.kawaiicrops.item.ItemKawaiiClothes;
import com.kawaiiwolf.kawaiicrops.item.ItemKawaiiFood;
import com.kawaiiwolf.kawaiicrops.item.ItemKawaiiIngredient;
import com.kawaiiwolf.kawaiicrops.item.ModItems;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiBigPot;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiCookingBase;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiFryingPan;
import com.kawaiiwolf.kawaiicrops.world.WorldGenKawaiiBaseWorldGen;
import com.kawaiiwolf.kawaiicrops.world.WorldGenKawaiiBaseWorldGen.WorldGen;
import com.kawaiiwolf.kawaiicrops.world.WorldGenKawaiiTree;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.potion.Potion;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.common.DimensionManager;

public class ConfigurationLoader {

	// Parent folder for Configuration files.
	private String configFolder = null;
	
	// Dump a list of Block/Item names to a config file.
	private static boolean DumpIDs = false;
	private static boolean BonusOres = false;
	private static boolean BonusDrops = false;
	private static boolean SortLists = false;
	
	public static String WAILAName;
	
	public ConfigurationLoader(FMLPreInitializationEvent event) {
		configFolder = event.getSuggestedConfigurationFile().getParent();
		WAILAName = event.getModMetadata().name;
	}
	
	public static final String HEADER_COMMENT = ""+
			"Note: If your block/item doesn't show up in this file, make sure you set it in general.cfg, save\n" +
			"and open minecraft at least once. You don't need to load into a world, just get to the splash \n" +
			"screen and the necesary Configuration will be automatically generated for you.";

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
	
	
	public static final String GENERAL_TREE_COMMENT = "" + 
			"List the names of all fruit bearing trees for the mod to generate. Make sure each tree name is lower\n" +
			"case and has no spaces or punctuation. You can separate these with commas or spaces.\n" +
			"\n"+
			"Bad Name: Cherry\n"+
			"Good Name: cherry\n"+
			"\n"+
			"S:Cakes=cherry lemon walnut";
	
	public static final String GENERAL_BARREL_COMMENT = "" +
			"List the names of all barrels for mod to generate. Make sure each tree name is lower case and  has no\n" +
			"spaces or punctuation. You can separate these with commas or spaces. Barrels ferment/age and can spoil.\n" +
			"\n"+
			"Bad Name: Swiss Cheese\n"+
			"Good Name: swisscheese\n"+
			"\n"+
			"S:Barrels=cheddarcheese dillpickles peachbrandy";

	
	public static final String GENERAL_CAKE_COMMENT = "" + 
			"List the names of all cakes for the mod to generate. Make sure each cake name is lower case and has no\n" + 
			"spaces or punctuation. You can separate these with commas or spaces.\n" +
			"\n"+
			"Bad Name: Strawberry Shortcake\n"+
			"Good Name: strawberryshort\n"+
			"\n"+
			"S:Cakes=strawberryshort chocolate carrot";
	
	
	public static final String GENERAL_FOOD_COMMENT = "" + 
			"List the names of all foods for the mod to generate. Make sure each name is lower case and has no\n" + 
			"spaces or punctuation. You can separate these with commas or spaces.\n" +
			"\n"+
			"Bad Name: Raspberry Tea\n"+
			"Good Name: raspberrytea\n"+
			"\n"+
			"S:Cakes=raspberrytea banananutmuffin";	
	
	
	public static final String GENERAL_INGREDIENTS_COMMENT = "" + 
			"List the names of all non-food items for the mod to generate. Typically these will be used as byproducts\n" +
			"or half-steps in recipes, such as an unbaked cake or ground pepper. Make sure each name is lower case\n" +
			"and has no spaces or punctuation. You can separate these with commas or spaces.\n" +
			"\n"+
			"Bad Name: Unbaked Chocoalte Cake\n"+
			"Good Name: unbakedchocolatecake\n"+
			"\n"+
			"S:Cakes=unbakedchocolatecake groundpepper";
	
	
	public static final String GENERAL_CLOTHING_COMMENT = "" +
			"\nList the names of all clothing sets for the mod to generate. Typically these will be cosmetic armor"+
			"\npieces because we need more options than dyed leather, but you can add new armor if you really want."+
			"\nEach name provided here is a full armor set (hat, top, pants and shoes), but you can enable only "+
			"\nspecific pieces. Please list names for the clothing sets in lower case with no space or punctuation."+
			"\nYou can separate these with commas or spaces. You can include numbers !"+
			"\n"+
			"\nBad Name: Fuzzy Jammies"+
			"\nGood Name: fuzzyjammies\n"+
			"\n"+
			"S:Clothes=fuzzyjammies casual1 casual2 eveningwear1";
	
	
	public static final String REFERENCE_DROPTABLES_COMMENT = "" +
			"A drop table is defined with the following syntax, in BNF:\n"+
			"\n"+
			"<drop-table> ::= <items> | <items> \"|\" <drop-table>\n"+
			"     <items> ::= <item> | <item> \",\" <items>\n"+
			"      <item> ::= <item-name> | <item-name> \" \" <num-drops> | <item-name> \" \" <num-drops> \" \" <weight>\n"+
			" <item-name> ::= \"seed\" | \"sapling\" | \"crop\" | \"fruit\" | \"nothing\" | <minecraft-item-name>\n"+
			" <num-drops> ::= <integer-between-1-and-64>\n"+
			"    <weight> ::= <integer-between-1-and-64>\n"+
			"\n"+
			"\n"+
			"The keywords 'seed', 'crop', and 'nothing' are shorthand so you don't have to type out the fully\n"+
			"qualified names for the products of a crop (or empty blocks.) Number of drops and weight default to \n"+
			"one if not supplied. For Trees 'seed' acts as saplings, 'crop' as fruit, and you may use the word \n"+ 
			"\n'fruit' interchangably with 'crop' and 'sapling' with 'seed'."+
			"\n"+
			"Here are some examples of how to use this:\n"+
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

	
	public static final String REFERENCE_POTION_COMMENT = "" +
			"A Potion Effect is deifned with the following syntax:\n"+
			"\n"+
			"  <Potion-Effects> ::= <Potion-Effect> | <Potion-Effect> \"|\" <Potion-Effects>\n"+
			"   <Potion-Effect> ::= <potion-id> \" \" <duration-seconds> \" \" <Amplifier> \" \" <Chance>\n"+
			"       <potion-id> ::= <number-between-1-and-23>\n"+
			"<duration-seconds> ::= <positive-number>\n"+
			"       <Amplifier> ::= <positive-number>\n"+
			"          <Chance> ::= <decimal-between-0.0-and-1.0>\n"+
			"\n"+
			"\n"+
			"Ex: \n"+
			"  \"1 4 1 1.0\" This applies Speed I for 4 seconds 100% of the time\n"+
			"  \"9 10 1 .05\" This applies Nausea for 10 seconds, 5% of the time\n"+
			"  \"1 4 1 1.0 | 9 10 1 .05\" This always applies speed I and 5% of the time, Nausea\n"+
			"\n"+
			"\nTo see a list of all potion IDs & names, turn on \"Dump All IDs\" and see dump.cfg";

	
	public static final String REFERENCE_ORE_COMMENT = "" +
			"Use this field to add items to ore dictionary references. These can be used as\n"+
			"shortcuts when making recipes. Ore recipes should be separated by a space and may\n" +
			"contain upper and lowercase letters and numbers.\n" +
			"\n"+
			"For example, to the snowpea crop we could set the \"Ore Dictionary Entries\" to:\n"+
			"  \"vegetables stirFryVegetables saladVegetables\"" +
			"and use any of those three names in recipes.\n"+
			"\n"+
			"\nWarning: Unshaped recipes with different, overlapping ore dictionary references can"+
			"\nsometimes cause recipes not to be recognized if ingredients are in a certain order."+
			"\nThis includes custom cooking recipes when using ore dictionary."+
			"\n"+
			"To see a list of all Ore Dictionary names, turn on \"Dump All IDs\" and see dump.cfg";
	
	
	public static final String REFERENCE_BONUS_ORE_COMMENT = "" +
			"\nUse these fields to add existing items from Vanilla minecraft or other mods to the Ore"+
			"\nDictionary. Please see the note on the Ore Dictionary in general.cfg to see how to use"+
			"\nthe Ore Dictionary."+
			"\n"+
			"\nFormat:"+
			"\n  <item> <ore name> ... <ore name>"+
			"\n"+
			"\nWhere <item> is the name of the item to add to the ore dictionary, and ore names are" +
			"\nspace separated ore dictionary names. See general.cfg and dump.cfg to get a list of"+
			"\nall item names."+
			"\n"+
			"\nExamples:"+
			"\n"+
			"\n  minecraft:carrot veggieAll veggieRaw veggieSalad"+
			"\nAdds the vanilla carrot to the veggieAll, veggieRaw and veggieSalad ore dictionary groups.";
	

	public static final String REFERENCE_BONUS_DROPS_COMMENT = "" +
			"\nUse these fields to add an additional drop table to a mob upon it's death. Please see the"+
			"\nnote on the Drop Tables in general.cfg. For these drop tables, keywords for seeds/fruit/etc"+
			"\n have no meaning and will be treated as \"nothing\"."+
			"\n"+
			"\nFormat:"+
			"\n  <entity name> [cull] <Drop Table> [$ <Baby Drop Table>]"+
			"\n"+
			"\nWhere <entity name> is the name of a living entity (underscores subsituted for spaces) and"+
			"\n<Drop Table> is a full drop table. Please don't include the same mob twice, if you wish to"+
			"\nadd additional items to a mob, do this in one drop table. See general.cfg and dump.cfg to "+
			"\nget a list of all entity and item names. You can include the word \"cull\" after the "+
			"\nentity name in order to remove their default drops. If you like, include a second drop "+
			"\ntable seperated with a $. If an entity has a baby version, this second drop table will "+
			"\nbe applied instead of the first for the children."+
			"\n"+
			"\nExamples:"+
			"\n"+
			"\n  Cow minecraft:bone 1, minecraft:bone 2, nothing 1 2"+
			"\n"+
			"\nAdds the occasional bone or two to a minecraft cow. It's not like they don't have them too."+
			"\n"+
			"\n"+
			"\n  Cow cull minecraft:bone 1, nothing | minecraft:beef 2 minecraft:beef 1 $ minecraft:beef"+
			"\n"+
			"\nStrips the drops from cows and changes it so adults drop a bone sometimes, and between"+
			"\none or two beef. Baby cows just drop a single beef"+
			"";

	
	
	public static final String REFERENCE_RECIPES = "" +
			"These values control the number of recipes to be parsed. If it's not enough\n" +
			"simply increase these numbers and load up the game to automatically create\n" +
			"new slots below.";
	
	
	public static final String REFERENCE_RECIPES_2 = "" +
			"Format for 2x2 Shaped Crafting recipes:\n"+
			"\n"+
			"+---+---+\n"+
			"| 1 | 2 |\n"+
			"+---+---+\n"+
			"| 3 | 4 |\n"+
			"+---+---+\n"+
			"\n"+
			"\n"+
			"\"<item/block name> <number crafted> <1> <2> <3> <4>\"\n"+
			"\n"+
			"Where 1,2,3 or 4 are the names of blocks, items or ore dictonary names.\n"+
			"You can use \"nothing\" if you want that spot to be blank. For a list of\n"+
			"all valid IDs, turn on \"Dump All IDs\" in general.cfg\n"+
			"\n"+
			"Example:\n"+
			"\n"+
			"\"minecraft:lever 1 stickWood nothing cobblestone nothing\"\n"+
			"Makes a vanilla lever using stick and cobble ore dictionary entries.\n"+
			"\n"+
			"\"minecraft:arrow 8 minecraft:feather minecraft:feather minecraft:feather minecraft:feather\"\n"+
			"Makes 8 arrows out of 4 feathers";
	

	public static final String REFERENCE_RECIPES_3 = "" +
			"Format for 2x2 Shaped Crafting recipes:\n"+
			"\n"+
			"+---+---+---+\n"+
			"| 1 | 2 | 3 |\n"+
			"+---+---+---+\n"+
			"| 4 | 5 | 6 |\n"+
			"+---+---+---+\n"+
			"| 7 | 8 | 9 |\n"+
			"+---+---+---+\n"+
			"\n"+
			"\n"+
			"\"<item/block name> <number crafted> <1> <2> <3> <4> <5> <6> <7> <8> <9>\"\n"+
			"\n"+
			"Where 1, 2, ..., 9 are the names of blocks, items or ore dictonary names.\n"+
			"You can use \"nothing\" if you want that spot to be blank. For a list of\n"+
			"all valid IDs, turn on \"Dump All IDs\" in general.cfg\n"+
			"\n"+
			"Example:\n"+
			"\n"+
			"\"minecraft:furnace 1 cobblestone cobblestone cobblestone cobblestone \n"+
			"nothing cobblestone cobblestone cobblestone cobblestone \"\n"+
			"Makes a vanilla furnace cobblestone ore dictionary entries.";
	

	public static final String REFERENCE_RECIPES_U = "" +
			"Format for Shapeless Crafting recipes:\n"+
			"\n"+
			"\"<item/block name> <number crafted> <1> <2> <3> ...\"\n"+
			"\n"+
			"Where 1 is the names of blocks or item you're throwing in the furnace.\n"+
			"For a list of all valid IDs, turn on \"Dump All IDs\" in general.cfg\n"+
			"\n"+
			"Example:\n"+
			"\n"+
			"\"minecraft:mushroom_stew 4 minecraft:bowl minecraft:bowl minecraft:bowl \n" +
			"minecraft:bowl minecraft:brown_mushroom minecraft:red_mushroom minecraft:carrots\"\n"+
			"Makes 4 bowls of soup with 4 bowls, one of each mushroom and a carrot\n"+
			"\n"+
			"\"minecraft:record_13 1 dustRedstone record\"\n"+
			"Create record 13 by adding redstone to any record.";
	
	
	public static final String REFERENCE_RECIPES_S = "" +
			"Format for Smelting Crafting recipes:\n"+
			"\n"+
			"\"<item/block name> <number crafted> <1>\"\n"+
			"\n"+
			"Where 1 is the names of blocks or item you're throwing in the furnace.\n"+
			"For a list of all valid IDs, turn on \"Dump All IDs\" in general.cfg\n"+
			"\n"+
			"Example:\n"+
			"\n"+
			"\"minecraft:stick 1 minecraft:sapling \"\n"+
			"Smelting a sapling ";
	
	public static final String REFERENCE_RECIPES_CUST_CUTTING_BOARD = "" +
			"Format for Cutting Board Crafting recipes:\n"+
			"\n"+
			"\"<result item/block name> <number crafted> <1> \"\n"+
			"\n"+
			"Where <1> is the name of the block, item or ore dictonary name for the\n"+
			"ingredient to be chopped into the result.\n"+
			"For a list of all valid IDs, turn on \"Dump All IDs\" in general.cfg\n"+
			"\n"+
			"Example:\n"+
			"\n"+
			"\"minecraft:stick 3 minecraft:sapling \"\n"+
			"\nChops a sapling into 3 sticks.";

	public static final String REFERENCE_RECIPES_CUST_CHURN = "" +
			"Format for Churn & Milling Crafting recipes:\n"+
			"\n"+
			"\n\"<result item/block name> <number crafted> <ingredient> [<cookTime>] [churn | mill]\""+
			"\n"+
			"\nWhere <ingredient> is the name of the block, item or ore dictonary name "+
			"\nfor theingredient to be chopped into the result. <cookTime> is the number"+
			"\nof consecutive clicks to fully churn. Muts be between 1 and 64 (64 is just"+
			"\ncruel. <cookTime> is optional and defaults to 1 if not supplied. [churn]"+
			"\nindicates that we need to use a churn block and [mill] to use the"+
			"\nmillstone block, but defaults to churn if not supplied. For a list of all"+
			"\nvalid IDs, turn on \"Dump All IDs\" in general.cfg\n"+
			"\n"+
			"Example:\n"+
			"\n"+
			"\"minecraft:flint 3 minecraft:gravel 4 mill\"\n"+
			"\nMills a block of gravle into 3 flint.";

	public static final String REFERENCE_RECIPES_CUST_FRYING_PAN = "" +
			"Format for Frying Pan Crafting recipes:\n"+
			"\n"+
			"\"<result item/block name> <number crafted> <1> [<2> <3>] <cook time> <burn time> <options>\"\n"+
			"\n"+
			"\nWhere <1>, <2> and <3> are the names of the block, item or ore dictonary names for the"+
			"\ningredients to be cooked into the result. You can provide between 1 and 3 ingredients."+
			"\nFor a list of all valid IDs, turn on \"Dump All IDs\" in general.cfg"+
			"\n"+
			"\n<cook time> is the number of random ticks it will take for the recipe to finish cooking."+
			"\n  A number less than 1 indicates it cooks instantly if the pan is hot ( at least one "+
			"\n  random tick ontop of a heat source block)"+
			"\n"+
			"\n<burn time> is the number of random ticks it will take for the recipe to be ruined by"+
			"\n  overcooking. A number less than 1 indicates the food will never burn."+
			"\n"+
			"\nOptions: The following options can be provided or ommited to change the nature of the"+
			"\nrecipe."+
			"\n  - \"oil\": recipes require some sort of oil to be added to the pan before you can add"+
			"\n           other ingredeints. See general.cfg to see a list of items that count as a type"+
			"\n           of oil."+
			"\n  - \"greasy\": Oil the pan after cooking this dish"+
			"\n  - \"steam\": recipes require a bamboo steam box to be added to the pan before you can"+
			"\n             cook this recipe. Does not work with the oil or greasy keywords."+
			"\n  - \"texture\": On a completed recipe, render a different texture in the pan. The file"+
			"\n               for this texture sould be named the same as the item plus \".fryingpan\""+
			"\n               (minus the mod id) and placed in the kawaiicrops\\textures\\blocks folder."+
			"\n  - <Item Name>: Harvesting a complete recipe requires this item and will use it up."+
			"\n"+
			"\n"+
			"\nExample:"+
			"\n"+
			"\n\"minecraft:cooked_porkchop 1 minecraft:porkchop 1 4 oil\""+
			"\nCooks a porkchop in 1 random tick that will burn after 4 more random ticks"+
			"\n"+
			"\n\"minecraft:mushroom_stew 2 minecraft:brown_mushroom minecraft:red_mushroom 4 0 texture minecraft:bowl\""+
			"\ncooks 2 mushroom stews after 4 random ticks with no chance of burning. You must click on the pan with a"+
			"\nwooden bowl in hand to harvest the soup. Additionally, once fully cooked, instead of rendering a"+
			"\nbowl of mushroom stew in the pan, it will instead display the texture found at"+
			"\n  kawaiicrops\\textures\\blocks\\mushroom_stew.fryingpan.png";
	
	public static final String REFERENCE_RECIPES_CUST_BIG_POT = "" +
			"Format for Big Pot Crafting recipes:\n"+
			"\n"+
			"\n\"<result item/block name> <number crafted> <1> [<2> ... <6>] <cook time> <burn time> <options>\""+
			"\n"+
			"\nWhere <1>, <2> ... <6> are the names of the block, item or ore dictonary names for the"+
			"\ningredients to be cooked into the result. You can provide between 1 and 6 ingredients."+
			"\nFor a list of all valid IDs, turn on \"Dump All IDs\" in general.cfg"+
			"\n"+
			"\n<cook time> is the number of random ticks it will take for the recipe to finish cooking."+
			"\n  A number less than 1 indicates it cooks instantly if the pan is hot ( at least one "+
			"\n  random tick ontop of a heat source block)"+
			"\n"+
			"\n<burn time> is the number of random ticks it will take for the recipe to be ruined by"+
			"\n  overcooking. A number less than 1 indicates the food will never burn."+
			"\n"+
			"\nOptions: The following options can be provided or ommited to change the nature of the"+
			"\nrecipe."+
			"\n  - \"water\": Recipes require some water to be added to the pot before you can add"+
			"\n             other ingredeints. See general.cfg to see a list of items that add water "+
			"\n             to the pot. If oil is not set, this option is assumed."+
			"\n  - \"milk\": Recipes require some sort of milk to be added to the pot before you can add"+
			"\n            other ingredeints. See general.cfg to see a list of items that count as a type"+
			"\n            of milk."+
			"\n  - \"oil\": Recipes require some sort of oil to be added to the pot before you can add"+
			"\n           other ingredeints. See general.cfg to see a list of items that count as a type"+
			"\n           of oil."+
			"\n  - \"keep\": Liquids in this pot remain after cooking this dish"+
			"\n  - \"texture\": On a completed recipe, render a different texture in the pan. The file"+
			"\n               for this texture sould be named the same as the item plus \".bigpot\""+
			"\n               (minus the mod id) and placed in the kawaiicrops\\textures\\blocks folder."+
			"\n  - <Item Name>: Harvesting a complete recipe requires this item and will use it up."+
			"\n"+
			"\n"+
			"\nExample:"+
			"\n"+
			"\n\"minecraft:cooked_porkchop 1 minecraft:porkchop 1 4 oil\""+
			"\nCooks a porkchop in 1 random tick that will burn after 4 more random ticks"+
			"\n"+
			"\n\"minecraft:mushroom_stew 2 minecraft:brown_mushroom minecraft:red_mushroom 4 0 texture minecraft:bowl\""+
			"\ncooks 2 mushroom stews after 4 random ticks with no chance of burning. You must click on the pan with a"+
			"\nwooden bowl in hand to harvest the soup. Additionally, once fully cooked, instead of rendering a"+
			"\nbowl of mushroom stew in the pan, it will instead display the texture found at"+
			"\n  kawaiicrops\\textures\\blocks\\mushroom_stew.bigpot.png";
	
	public static final String REFERENCE_RECIPES_CUST_GRILL = "" +
			"Format for Grill Crafting recipes:\n"+
			"\n"+
			"\n\"<result item/block name> <number crafted> <1> [<2> ... <4>] <cook time> <burn time | dry>\""+
			"\n"+
			"\nWhere <1>, <2> ... <4> are the names of the block, item or ore dictonary names for the"+
			"\ningredients to be cooked into the result. You can provide between 1 and 4 ingredients."+
			"\nFor a list of all valid IDs, turn on \"Dump All IDs\" in general.cfg"+
			"\n"+
			"\n<cook time> is the number of random ticks it will take for the recipe to finish cooking."+
			"\n  A number less than 1 indicates it cooks instantly if the pan is hot ( at least one "+
			"\n  random tick ontop of a heat source block)"+
			"\n"+
			"\n<burn time> is the number of random ticks it will take for the recipe to be ruined by"+
			"\n  overcooking. A number less than 1 indicates the food will never burn. Alternatively"+
			"\n  use the word 'dry' to indicate that the grill does not need a heat source to cook"+
			"\n  and can't overcook. Great for drying out jerkey."+
			"\n"+
			"\n"+
			"\nExample:"+
			"\n"+
			"\n\"minecraft:cooked_beef 1 minecraft:beef 2 3\""+
			"\nCooks a steak in 2 random ticks that will burn after 3 more random ticks"+
			"\n"+
			"\n\"minecraft:cooked_fished 2 minecraft:fish minecraft:fish 4 dry\""+
			"\nDries out 2 fish over 4 random ticks.";

	public static final String REFERENCE_METADATA = "" +
			"\nItems names now support metadata/damage values ! Just use a " + Constants.META + " character" +
			"\nafter the item name followed by the damage value."+
			"\n"+
			"\nEx: minecraft:sapling" + Constants.META + "0 for an Oak Sapling."+
			"\nEx: minecraft:shears" + Constants.META + "10 for partially damaged Shears."+
			"";
	
	private LockedConfiguration cfg_general = null;
	public void loadConfiguration_PreInit() 
	{
		cfg_general = new LockedConfiguration(new File(configFolder + Constants.CONFIG_GENERAL));
		cfg_general.load();
		
		cfg_general.setCategoryComment(Configuration.CATEGORY_GENERAL, "Global Settings for KawaiiCraft");
		cfg_general.setCategoryComment("Reference: Drop Table Help", REFERENCE_DROPTABLES_COMMENT);
		cfg_general.setCategoryComment("Reference: Potions Help", REFERENCE_POTION_COMMENT);
		cfg_general.setCategoryComment("Reference: Ore Dictionary Help", REFERENCE_ORE_COMMENT);
		cfg_general.setCategoryComment("Reference: Item Metadata", REFERENCE_METADATA);
		
		String category = Configuration.CATEGORY_GENERAL;
		
		DumpIDs = cfg_general.getBoolean("Dump All IDs", category, DumpIDs, "Creates a list of Block and Item Names in the Configuration directory ?");
		BonusOres = cfg_general.getBoolean("Bonus Ore Dictionary", category, BonusOres, "Add items from other mods to ore dictionary references ?  If enabled, see ore.cfg");
		BonusDrops = cfg_general.getBoolean("Bonus Mob Drops", category, BonusDrops, "Add items to the drop tables of living entities ?  If enabled, see mobs.cfg");
		WAILAName = cfg_general.getString("WAILA Plugin Mod Name", category, WAILAName, "If the WAILA Mod is installed, what mod name do you want to show up ?  You can override the default with a custom name for your configuration/mod pack.");
		SortLists = cfg_general.getBoolean("Sort Lists", category, SortLists, "Automatically sort lists (Foods, Crops, Etc.) in this file in alphabetic order ? Helps find entries in long lists.");

		category = Configuration.CATEGORY_GENERAL + " Item Config";

		cfg_general.setCategoryComment(category, "Configuration Settings for special KawaiiCrops Items");
		ModItems.HungerPotionEnabled = cfg_general.getBoolean("Hunger Potion", category, ModItems.HungerPotionEnabled, "Enable the Potion of Hunger ?  This debug item makes you hungrier by drinking it.");
		ModItems.MagicSpoonEnabled = cfg_general.getBoolean("Magic Spoon", category, ModItems.MagicSpoonEnabled, "Enable the Magic Spoon ?  This debug item makes cooking blocks tick instantly.");
		ModItems.MysterySeedEnabled = cfg_general.getBoolean("Mystery Seed Enabled", category, ModItems.MysterySeedEnabled, "Enable the Myster Seed to drop from tall grass ?  When planted it could grow into just about anything !");
		ModItems.MysterySeedVanilla = cfg_general.getBoolean("Vanilla Mystery Seed Crops", category, ModItems.MysterySeedVanilla, "Include Vanilla Crops/Plants in the Mystery Seed's Drop List ?");
		
		category = Configuration.CATEGORY_GENERAL + " Cooking Config";
		
		cfg_general.setCategoryComment(category, "Configuration Settings for special KawaiiCrops Crafting Blocks");
		RecipeKawaiiCookingBase.CookingHeatSourcesString = cfg_general.getString("Heat Sources", category, "minecraft:lava minecraft:fire minecraft:lit_furnace ", "Which blocks act as heat sources on which cooking blocks (pots/pans/etc) can cook ontop of ?  Please separate blocks with spaces. Enable \"Dump All IDs\" to see a list of valid block names.");
		RecipeKawaiiCookingBase.CookingFireString = cfg_general.getString("Grill Fire Sources", category, "minecraft:lava minecraft:fire", "Which blocks act as fire sources on which a grill can cook ontop of ?  Please separate blocks with spaces. Enable \"Dump All IDs\" to see a list of valid block names.");
		RecipeKawaiiFryingPan.CookingOilItemsString = cfg_general.getString("Frying Pan Oil Items", category, "kawaiicrops:kawaiicrops.cookingoil", "What items can be used as a cooking oil for frying pan recipes ?  Please separate items with spaces.");
		RecipeKawaiiBigPot.CookingOilItemsString = cfg_general.getString("Big Pot Oil Items", category, "kawaiicrops:kawaiicrops.cookingoil", "What items can be used as a cooking oil for Big Pot recipes ?  Please separate items with spaces.");
		RecipeKawaiiBigPot.CookingWaterItemsString = cfg_general.getString("Big Pot Water Items", category, "minecraft:water_bucket", "What items can be used as water for Big Pot recipes ?  Please separate items with spaces.");
		RecipeKawaiiBigPot.CookingMilkItemsString = cfg_general.getString("Big Pot Milk Items", category, "minecraft:milk_bucket", "What items can be used as milk for Big Pot recipes ?  Please separate items with spaces.");
		
		// Crops
		
		cfg_general.setCategoryComment("KawaiiCrops Crops", GENERAL_CROP_COMMENT);
		String cropsRaw = cfg_general.getString("Crops", "KawaiiCrops Crops", "","Crop List");
		String[] cropsParsed = cropsRaw.toLowerCase().replaceAll("[^a-z, 0-9]", "").replaceAll("  ", " ").replaceAll(",,", ",").split("[, ]");
		sortCategory(cfg_general, "Crops", "KawaiiCrops Crops", cropsParsed);

		if(arrayHasString(cropsParsed))
		{
			LockedConfiguration cfg = new LockedConfiguration(new File(configFolder + Constants.CONFIG_CROPS));
			cfg.load();
			cfg.setCategoryComment("0", HEADER_COMMENT);
			for (String crop : cropsParsed)
				loadCrop(cfg, crop);
			cfg.save();
		}
		
		// Trees
		
		cfg_general.setCategoryComment("KawaiiCrops Trees", GENERAL_TREE_COMMENT);
		String treesRaw = cfg_general.getString("Trees", "KawaiiCrops Trees", "", "Tree List");
		String[] treesParsed = treesRaw.toLowerCase().replaceAll("[^a-z, 0-9]", "").replaceAll("  ", " ").replaceAll(",,", ",").split("[, ]");
		sortCategory(cfg_general, "Trees", "KawaiiCrops Trees", treesParsed);

		if(arrayHasString(treesParsed))
		{
			LockedConfiguration cfg = new LockedConfiguration(new File(configFolder + Constants.CONFIG_TREES));
			cfg.load();
			cfg.setCategoryComment("0", HEADER_COMMENT + "\n\nSpecial thanks to mDiyo & the Natura Mod for the tree generation code for type: Eucalyptus & Sakura");
			for (String tree : treesParsed)
				loadTree(cfg, tree);
			cfg.save();
		}
		
		// Barrels
		
		cfg_general.setCategoryComment("KawaiiCrops Barrels", GENERAL_BARREL_COMMENT);
		String barrelsRaw = cfg_general.getString("Barrels", "KawaiiCrops Barrels", "", "Barrels List");
		String[] barrelsParsed = barrelsRaw.toLowerCase().replaceAll("[^a-z, 0-9]", "").replaceAll("  ", " ").replaceAll(",,", ",").split("[, ]");
		sortCategory(cfg_general, "Barrels", "KawaiiCrops Barrels", barrelsParsed);
		
		if(arrayHasString(barrelsParsed))
		{
			LockedConfiguration cfg = new LockedConfiguration(new File(configFolder + Constants.CONFIG_BARRELS));
			cfg.load();
			cfg.setCategoryComment("0", HEADER_COMMENT);
			for (String barrel : barrelsParsed)
				loadBarrel(cfg, barrel);
			cfg.save();
		}
		
		// Cakes
		
		cfg_general.setCategoryComment("KawaiiCrops Yummy Cakes", GENERAL_CAKE_COMMENT);
		String cakesRaw = cfg_general.getString("Cakes", "KawaiiCrops Yummy Cakes", "", "Cake List");
		String[] cakesParsed = cakesRaw.toLowerCase().replaceAll("[^a-z, 0-9]", "").replaceAll("  ", " ").replaceAll(",,", ",").split("[, ]");
		sortCategory(cfg_general, "Cakes", "KawaiiCrops Yummy Cakes", cakesParsed);
		
		if(arrayHasString(cakesParsed))
		{
			LockedConfiguration cfg = new LockedConfiguration(new File(configFolder + Constants.CONFIG_CAKES));
			cfg.load();
			cfg.setCategoryComment("0", HEADER_COMMENT);
			for (String cake : cakesParsed)
				loadCake(cfg, cake);
			cfg.save();
		}
		
		// Foods
		
		cfg_general.setCategoryComment("KawaiiCrops Foods", GENERAL_FOOD_COMMENT);
		String foodsRaw = cfg_general.getString("Foods", "KawaiiCrops Foods", "", "Food List");
		String[] foodsParsed = foodsRaw.toLowerCase().replaceAll("[^a-z, 0-9]", "").replaceAll("  ", " ").replaceAll(",,", ",").split("[, ]");
		sortCategory(cfg_general, "Foods", "KawaiiCrops Foods", foodsParsed);
		
		if(arrayHasString(foodsParsed))
		{
			LockedConfiguration cfg = new LockedConfiguration(new File(configFolder + Constants.CONFIG_FOODS));
			cfg.load();
			cfg.setCategoryComment("0", HEADER_COMMENT);
			for (String food : foodsParsed)
				loadFood(cfg, food);
			cfg.save();
		}
		
		// Ingredients
		
		cfg_general.setCategoryComment("KawaiiCrops Ingredients", GENERAL_INGREDIENTS_COMMENT);
		String ingredientsRaw = cfg_general.getString("Ingredients", "KawaiiCrops Ingredients", "", "Ingredients List");
		String[] ingredientsParsed = ingredientsRaw.toLowerCase().replaceAll("[^a-z, 0-9]", "").replaceAll("  ", " ").replaceAll(",,", ",").split("[, ]");
		sortCategory(cfg_general, "Ingredients", "KawaiiCrops Ingredients", ingredientsParsed);
		
		if(arrayHasString(ingredientsParsed))
		{
			LockedConfiguration cfg = new LockedConfiguration(new File(configFolder + Constants.CONFIG_INGREDIENTS));
			cfg.load();
			cfg.setCategoryComment("0", HEADER_COMMENT);
			for (String ingredient : ingredientsParsed)
				loadIngredient(cfg, ingredient);
			cfg.save();
		}
		
		// Clothes
		
		cfg_general.setCategoryComment("KawaiiCrops Clothes", GENERAL_CLOTHING_COMMENT);
		String clothesRaw = cfg_general.getString("Clothes", "KawaiiCrops Clothes", "", "Clothes List");
		String[] clothesParsed = clothesRaw.toLowerCase().replaceAll("[^a-z, 0-9]", "").replaceAll("  ", " ").replaceAll(",,", ",").split("[, ]");
		sortCategory(cfg_general, "Clothes", "KawaiiCrops Clothes", clothesParsed);
		
		if(arrayHasString(clothesParsed))
		{
			LockedConfiguration cfg = new LockedConfiguration(new File(configFolder + Constants.CONFIG_CLOTHES));
			cfg.load();
			cfg.setCategoryComment("0", HEADER_COMMENT);
			ModItems.ClothArmorMaterialDurability = cfg.getInt("Cloth Armor Durability", "0", ModItems.ClothArmorMaterialDurability, 1, 500, "What do you want the durability for your clothes to be ? Ex: Leather 5, Gold 7, Chain & Iron 15, Diamond 33.");
			ModItems.initilizeClothArmorMaterial();
			for (String clothes : clothesParsed)
				loadClothes(cfg, clothes);
			cfg.save();
		}		
		
		//cfg_general.save();
	}
	
	public void loadConfiguration_Init()
	{
		if (BonusOres) 
			loadBonusOres();
	}
	
	public void loadConfiguration_PostInit() 
	{
		if (DumpIDs) 
			dumpIDs();
		if (this.BonusDrops)
			loadBonusDrops();			
		
		LockedConfiguration cfg = new LockedConfiguration(new File(configFolder + Constants.CONFIG_RECIPES));
		cfg.load();
		
		String category = "0 Main Settings";
		cfg.setCategoryComment(category, REFERENCE_RECIPES);
		
		int defaultRecipes = 3;
		int recipes2 = cfg.getInt("2 by 2", category, defaultRecipes, 0, 10000, "Number of 2x2 Shaped crafting recipes ?");
		int recipes3 = cfg.getInt("3 by 3", category, defaultRecipes, 0, 10000, "Number of 3x3 Shaped crafting recipes ?");
		int recipesU = cfg.getInt("Unshaped", category, defaultRecipes, 0, 10000, "Number of Unshaped crafting recipes ?");
		int recipesS = cfg.getInt("Smelting", category, defaultRecipes, 0, 10000, "Number of Smelting crafting recipes ?");
		int recipesC_cut = cfg.getInt("Kawaiicraft Cutting Board", category, defaultRecipes, 0, 10000, "Number of Kawaiicraft Cutting Board crafting recipes ?");
		int recipesC_fry = cfg.getInt("Kawaiicraft Frying Pan", category, defaultRecipes, 0, 10000, "Number of Kawaiicraft Frying Pan crafting recipes ?");
		int recipesC_pot = cfg.getInt("Kawaiicraft Big Pot", category, defaultRecipes, 0, 10000, "Number of Kawaiicraft Big Pot crafting recipes ?");
		int recipesC_churn = cfg.getInt("Kawaiicraft Churn", category, defaultRecipes, 0, 10000, "Number of Kawaiicraft Churn crafting recipes ?");
		int recipesC_grill = cfg.getInt("Kawaiicraft Grill", category, defaultRecipes, 0, 10000, "Number of Kawaiicraft Grill crafting recipes ?");
		
		category = "2 by 2 Shaped Crafting Recipes";
		cfg.setCategoryComment(category, REFERENCE_RECIPES_2);
		for (int i = 0; i < recipes2; i++)
		{
			String recipe = cfg.getString("" + i, category, "", "");
			RecipeHelper.register2x2recipe(recipe);
		}

		category = "3 by 3 Shaped Crafting Recipes";
		cfg.setCategoryComment(category, REFERENCE_RECIPES_3);
		for (int i = 0; i < recipes3; i++)
		{
			String recipe = cfg.getString("" + i, category, "", "");
			RecipeHelper.register3x3recipe(recipe);
		}
		
		category = "Shapeless Crafting Recipes";
		cfg.setCategoryComment(category, REFERENCE_RECIPES_U);
		for (int i = 0; i < recipesU; i++)
		{
			String recipe = cfg.getString("" + i, category, "", "");
			RecipeHelper.registerShapelessRecipe(recipe);
		}
		
		category = "Smelting Crafting Recipes";
		cfg.setCategoryComment(category, REFERENCE_RECIPES_S);
		for (int i = 0; i < recipesS; i++)
		{
			String recipe = cfg.getString("" + i, category, "", "");
			RecipeHelper.registerSmeltingRecipe(recipe);
		}
		
		category = "Kawaiicraft Cutting Board Recipes";
		cfg.setCategoryComment(category, this.REFERENCE_RECIPES_CUST_CUTTING_BOARD);
		for (int i = 0; i < recipesC_cut; i++)
		{
			String recipe = cfg.getString("" + i, category, "", "");
			RecipeHelper.registerCustomCuttingBoardRecpie(recipe);
		}

		category = "Kawaiicraft Churn Recipes";
		cfg.setCategoryComment(category, this.REFERENCE_RECIPES_CUST_CHURN);
		for (int i = 0; i < recipesC_churn; i++)
		{
			String recipe = cfg.getString("" + i, category, "", "");
			RecipeHelper.registerCustomChurnRecpie(recipe);
		}

		category = "Kawaiicraft Frying Pan Recipes";
		cfg.setCategoryComment(category, this.REFERENCE_RECIPES_CUST_FRYING_PAN);
		for (int i = 0; i < recipesC_fry; i++)
		{
			String recipe = cfg.getString("" + i, category, "", "");
			RecipeHelper.registerCustomFryingPanRecipe(recipe);
		}
		
		category = "Kawaiicraft Big Pot Recipes";
		cfg.setCategoryComment(category, this.REFERENCE_RECIPES_CUST_BIG_POT);
		for (int i = 0; i < recipesC_pot; i++)
		{
			String recipe = cfg.getString("" + i, category, "", "");
			RecipeHelper.registerCustomBigPotRecipe(recipe);
		}
		
		category = "Kawaiicraft Grill Recipes";
		cfg.setCategoryComment(category, REFERENCE_RECIPES_CUST_GRILL);
		for (int i = 0; i < recipesC_grill; i++)
		{
			String recipe = cfg.getString("" + i, category, "", "");
			RecipeHelper.registerCustomGrillRecipe(recipe);
		}
		
		cfg.save();
		cfg_general.save();
	}
	
	private void dumpIDs() {
		
		File f = new File(configFolder + Constants.CONFIG_DUMP);
		
		// Try to clear it out if it exists. Fresh File
		try { if (f.exists()) f.delete(); } catch (Exception e) { }
		
		String blockList = "", itemList = "", oreList = "", biomeList = "", entityList = "", potionList = "";
		
		Iterator<Block> blocks = NamespaceHelper.getBlockIterator();
		while (blocks.hasNext())
			blockList += NamespaceHelper.getBlockName(blocks.next()) + "\n";
		
		Iterator<Item> items = NamespaceHelper.getItemIterator();
		while (items.hasNext())
			itemList += NamespaceHelper.getItemName(items.next()) + "\n";
		
		for (String name : OreDictionary.getOreNames())
			oreList += name + "\n";
		
		for (BiomeGenBase biome : BiomeGenBase.getBiomeGenArray())
			if (biome != null)
			biomeList += "\"" + (biome.biomeName + "\"                                                  ").substring(0, 50)
					+ "  Temperature [" + biome.temperature + "]  Humidity [" + biome.rainfall + "]" + "\n";
		
		for (Object entity : EntityList.classToStringMapping.keySet())
			if (entity != null && entity instanceof Class && EntityLiving.class.isAssignableFrom((Class) entity))
				entityList += EntityList.classToStringMapping.get(entity).toString().replace(' ', '_') + "\n";
		
		for (Potion potion : Potion.potionTypes)
			if (potion != null)
				potionList += potion.id + " : " + potion.getName() + "\n";
		
		Configuration config = new Configuration(f);
		config.load();
		config.setCategoryComment("Blocks", blockList);
		config.setCategoryComment("Items", itemList);
		config.setCategoryComment("OreDictionary", oreList);
		config.setCategoryComment("Biomes", biomeList);
		config.setCategoryComment("Living Entities", entityList);
		config.setCategoryComment("Potions", potionList);
		config.save();
	}

	private void loadBonusOres()
	{
		LockedConfiguration cfg = new LockedConfiguration(new File(configFolder + Constants.CONFIG_ORES));
		cfg.load();

		cfg.setCategoryComment("0 General", "Number of Ore Dictionary References");
		int ores = cfg.getInt("Number of references", "0 General", 3, 1, 10000, "");
		String ore, name, dict;
		
		cfg.setCategoryComment("Ore Dictionary References", REFERENCE_BONUS_ORE_COMMENT);
		for (int i = 1; i <= ores; i++)
		{
			ore = cfg.getString("" + i, "Ore Dictionary References", "", "");
			try
			{
				name = ore.substring(0, ore.indexOf(" ")).trim();
				dict = ore.substring(ore.indexOf(" ")).trim();
				
				ModItems.OreDictionaryBonus.put(name, dict);
			} 
			catch (Exception e) { }
		}
		cfg.save();
	}
	
	private void loadBonusDrops()
	{
		LockedConfiguration cfg = new LockedConfiguration(new File(configFolder + Constants.CONFIG_DROPS));
		cfg.load();
		
		cfg.setCategoryComment("0 General", "Number of Mob Drop References");
		int drops = cfg.getInt("Number of references", "0 General", 3, 1, 10000, "");
		String raw;

		cfg.setCategoryComment("Bonus Mob Drops", REFERENCE_BONUS_DROPS_COMMENT);
		for (int i = 1; i <= drops; i++)
		{
			raw = cfg.getString("" + i, "Bonus Mob Drops", "", "");
			try
			{
				Class entity = null;
				for (Object e : EntityList.classToStringMapping.keySet())
					if (raw.substring(0, raw.indexOf(" ")).trim().replace(' ', '_').equals(EntityList.classToStringMapping.get(e).toString().replace(' ', '_')))
					{
						entity = (Class) e;
						break;
					}
				if (entity == null) continue;
				
				raw = raw.substring(raw.indexOf(" ")).trim();
				if (raw.toLowerCase().startsWith("cull"))
				{
					EventKawaiiLivingDrop.cull.add(entity);
					raw = raw.substring(4).trim();
				}
				
				DropTable[] tables;
				
				if (raw.contains("$"))
				{
					String[] split = raw.split("\\$");
					tables = new DropTable[] { new DropTable(split[0].trim(), null, null), new DropTable(split[1].trim(), null, null) };
				}
				else
					tables = new DropTable[] { new DropTable(raw.trim(), null, null) };

				EventKawaiiLivingDrop.drops.put(entity, tables);
			} 
			catch (Exception e) { }
		}
		
		cfg.save();
	}

	private BlockKawaiiCrop loadCrop(Configuration config, String name) 
	{
		if (name == null || name.length() == 0) return null;

		String category = "KawaiiCrops: " + name;
		BlockKawaiiCrop b = new BlockKawaiiCrop(name);
		
		// Get crop variables from config file
		b.Enabled = config.getBoolean("0. Enabled", category, b.Enabled, "Is this a block in minecraft ? Defaults to false to allow you to configure before putting it in game.");
		
		String r = config.getString("1.General  Render Type", category, "Hash", "How will the crop render ? Valid values are [Hash, Cross, Block]").toLowerCase();
		if (r.equals("cross"))
			b.RenderType = BlockKawaiiCrop.EnumRenderType.CROSS;
		else if (r.equals("hash"))
			b.RenderType = BlockKawaiiCrop.EnumRenderType.HASH;
		else
			b.RenderType = BlockKawaiiCrop.EnumRenderType.BLOCK;
		
		b.CropStages = config.getInt("1.General  Crop Stages", category, b.CropStages, 2, 8, "Number of crop states ?  Valid values are between 2 and 8. (Ex: Carrots = 4, Wheat = 8)");
		b.MaxHeight = config.getInt("1.General  Max Height", category, b.MaxHeight, 1, 32, "How many blocks tall will this crop grow ?");
		b.CropGrowsOn = new HashSet<Block>(NamespaceHelper.getBlocksByName(config.getString("1.General  Soil Block", category, "minecraft:farmland", "What blocks does this grow on ? Seperate blocks with a space or comma. For a list of blocks, see [DumpNames] setting in General.cfg. (Note, 'minecraft:water' is an option.)"))); 
		
		b.MaxHeightRequiredToRipen = config.getBoolean("2.Harvest  Max Height Required to Ripen", category, b.MaxHeightRequiredToRipen, "Does the plant need to be at max height before lower blocks are ready to harvest ?");
		b.MultiHarvest = config.getBoolean("2. Multi Harvest", category, b.MultiHarvest, "Upon harvesting this crop, does it grow back to an earlier, unripe state ?");
		b.UnripeStage = config.getInt("2.Harvest  Unripe Stage", category, b.UnripeStage, 0, 7, "What stage is a crop finished growing before starting to ripen ?  For MultiHarvest, the stage the crop is set to after harvesting. For crops more than 1 block tall, the stage at which blocks will grow above.");
		b.UnripeHardness = config.getFloat("2.Harvest  Unripe Hardness", category, b.UnripeHardness, 0.0f, 1.0f, "Hardness of unripe crops (0 breaks instantly. Set higher to prevent accidental harvests) ?");
		
		b.GrowthMutliplier = config.getFloat("3.Growth  Growth Multiplier", category, b.GrowthMutliplier, 0.001f, 1000.0f, "How fast does your plant grow ? (1.0 is normal vanilla speeds, 3.0 is growth rate on wet farmland when growing on other block types.");
		b.BoneMealMin = config.getInt("3.Growth  Bonemeal Min", category, b.BoneMealMin, 0, 8, "Minimum stages of growth when using bonemeal.");
		b.BoneMealMax = config.getInt("3.Growth  Bonemeal Max", category, b.BoneMealMax, 0, 8, "Maximum stages of growth when using bonemeal.");
		b.LightLevelMin = config.getInt("3.Growth  Light Minimum", category, b.LightLevelMin, 0, 15, "Minimum level of light required for plant to grow naturally.");
		b.LightLevelMax = config.getInt("3.Growth  Light Maximum", category, b.LightLevelMax, 0, 15, "Maximum level of light required for plant to grow naturally.");
				
		if (b.BoneMealMin > b.BoneMealMax) b.BoneMealMin = b.BoneMealMax;
		if (b.BoneMealMax < b.BoneMealMin) b.BoneMealMax = b.BoneMealMin;
		
		b.DropTableRipeString = config.getString("4.Drops  Ripe Drop Table", category, b.DropTableRipeString, "What is the drop table for Ripe crops ? Please see General.cfg to see how to use these.");
		b.DropTableUnripeString = config.getString("4.Drops  Unripe Drop Table", category, b.DropTableUnripeString, "What is the drop table for Unripe crops ? Please see General.cfg to see how to use these.");
		
		WorldGenKawaiiBaseWorldGen.WorldGen gen = new WorldGen();
		gen.weight = config.getInt("5.WorldGen  Weight", category, gen.weight, 0, 1000, "How often should this crop attempt to spawn ?  A weight of 1 will attempt to spawn this crop in every chunk, 5 in every 5 chunks, etc.. A weight of 0 disables world gen for this crop.");
		gen.minRainfall = config.getFloat("5.WorldGen  Biome Humidity Minimum", category, gen.minRainfall, 0.0f, 1.0f, "What is the minimum Humidity a biome must have to spawn this crop ?");
		gen.maxRainfall = config.getFloat("5.WorldGen  Biome Humidity Maximum", category, gen.maxRainfall, 0.0f, 1.0f, "What is the maximum Humidity a biome must have to spawn this crop ?");
		gen.minTemperature = config.getFloat("5.WorldGen  Biome Temperature Minimum", category, gen.minTemperature, -0.5f, 2.0f, "What is the minimum Temperature a biome must have to spawn this crop ?");
		gen.maxTemperature = config.getFloat("5.WorldGen  Biome Temperature Maximum", category, gen.maxTemperature, -0.5f, 2.0f, "What is the maximum Temperature a biome must have to spawn this crop ?");
		gen.biomeBlacklist = config.getString("5.WorldGen  Biome Blacklist", category, gen.biomeBlacklist, "What biomes do you not want this to spawn on ?  For a list of biomes and their Humidity/Temperature, see [DumpNames] setting in General.cfg").toLowerCase();

		String comment = "Resource Pack settings for " + name + "\n\nLangage Name: tile.kawaiicrops." + name + ".name\n\n";
		for (int i = 0; i < (b.MaxHeightRequiredToRipen ? b.MaxHeight : 1); i++)
			for (int j = 0; j < b.CropStages; j++)
				comment += "Texture Name: textures/blocks/" + name + "_stage_" + (b.MaxHeightRequiredToRipen && b.MaxHeight > 1 ? i + "_" : "") + j + ".png\n";
		comment += "\nWarning: Texture names listed above will change as \"1.General  Crop Stages\", \"1.General  Max Height\" " +
				"\n and \"2.Harvest  Max Height Required to Ripen\" settings are changed."; 
		
		config.setCategoryComment(category, comment);
		
		String category_seeds = category + " Seeds";
		
		b.SeedsEnabled = config.getBoolean("0.  Enabled", category_seeds, b.SeedsEnabled, "Does this crop have seeds ?");
		b.SeedsEdible = config.getBoolean("1.General  Edible", category_seeds, b.SeedsEdible, "Are seeds also a food ?");
		b.SeedsHunger = config.getInt("1.General  Hunger", category_seeds, b.SeedsHunger, 0, 20, "If SeedsEdible, how many half shanks of food does this restore ?");
		b.SeedsSaturation = config.getFloat("1.General  Saturation", category_seeds, b.SeedsSaturation, 0, 20.0f, "If SeedsEdible, how saturating is this food ?");
		b.SeedsMysterySeedWeight = config.getInt("2.Other  Mystery Seed Weight", category_seeds, b.SeedsMysterySeedWeight, 0, 1000, "If mystery seeds enabled, what weight should this have on mystery seed results (0 = None)");
		b.SeedsToolTip = config.getString("2.Other  Tool Tip", category_seeds, b.SeedsToolTip, "What is the Tooltip for the seed in game ?");
		b.SeedsPotionEffects = new PotionEffectHelper(config.getString("2.Other  Potion Effect", category_seeds, "", "What potion effect do you want triggered on eating this seed ?  Please see General.cfg to see how to use these."));
		b.SeedOreDict = config.getString("2.Other  Ore Dictionary Entries", category_seeds, b.SeedOreDict, "This item is part of which Forge Ore Dictionary entries ?  Please see General.cfg to see how to use these.");

		config.setCategoryComment(category_seeds, 
				"Resource Pack settings for " + name + " seed\n\n" +
				"Langage Name: item.kawaiicrops." + name + ".seed.name\n" +
				"Texture Name: textures/items/" + name + ".seed.png");
		
		String category_crops = category + " Crops";
		
		b.CropEnabled = config.getBoolean("0.  CropEnabled", category_crops, b.CropEnabled, "Does this plant drop crops other than seeds ?");
		b.CropEdible = config.getBoolean("1.General  Edible", category_crops, b.CropEdible, "Are Crop also a food ?");
		b.CropHunger = config.getInt("1.General  Hunger", category_crops, b.CropHunger, 0, 20, "If CropEdible, how many half shanks of food does this restore ?");
		b.CropSaturation = config.getFloat("1.General  Saturation", category_crops, b.CropSaturation, 0, 20.0f, "If CropEdible, how is the saturating is this food ?");
		b.CropToolTip = config.getString("2.Other  Tool Tip", category_crops, b.CropToolTip, "What is the Tooltip for the crop in game ?");
		b.CropPotionEffects = new PotionEffectHelper(config.getString("2.Other  Potion Effect", category_crops, "", "What potion effect do you want triggered on eating this crop ?  Please see General.cfg to see how to use these."));
		b.CropOreDict = config.getString("2.Other  Ore Dictionary Entries", category_crops, b.CropOreDict, "This item is part of which Forge Ore Dictionary entries ?  Please see General.cfg to see how to use these.");

		config.setCategoryComment(category_crops, 
				"Resource Pack settings for " + name + " crop\n\n" +
				"Langage Name: item.kawaiicrops." + name + ".crop.name\n" +
				"Texture Name: textures/items/" + name + ".crop.png");
		
		b.register(gen);
		
		return b; 
	}
	
	private BlockKawaiiTreeBlocks loadTree(Configuration config, String name)
	{
		if (name == null || name.length() == 0) return null;
		
		BlockKawaiiTreeBlocks t = new BlockKawaiiTreeBlocks(name);
		
		String category = "Kawaiicrops: " + name + " tree";
		
		config.setCategoryComment(category, 
				"Resource Pack settings for " + name + " Tree\n\n" +
				"Langage Name: tile.kawaiicrops." + name + ".name\n\n" +
				"Texture Name: textures/blocks/" + name + ".sapling.png\n"+
				"Texture Name: textures/blocks/" + name + ".leaf.png\n"+
				"Texture Name: textures/blocks/" + name + ".fruit.stage_0.png\n"+
				"Texture Name: textures/blocks/" + name + ".fruit.stage_1.png\n"+
				"Texture Name: textures/blocks/" + name + ".fruit.stage_2.png\n"+
				"Texture Name: textures/blocks/" + name + ".fruit.stage_3.png\n");
		
		t.Enabled = config.getBoolean("0.  Enabled", category, t.Enabled, "Is this a block in minecraft ? Defaults to false to allow you to configure before putting it in game.");
		
		t.SaplingMinimumLight = config.getInt("1.Sapling  Minimum light", category, t.SaplingMinimumLight, 1, 14, "What is the minimum light required to grow this tree and it's fruit ?");
		t.SaplingSoilBlocks = new HashSet<Block>(NamespaceHelper.getBlocksByName(config.getString("1.Sapling  Soil Blocks", category, "minecraft:dirt minecraft:grass", "What blocks does this grow on ? Seperate blocks with a space or comma. For a list of blocks, see [DumpNames] setting in General.cfg. (Note, 'minecraft:water' is an option.)")));;
		t.SaplingOreDict = config.getString("1.Sapling  Ore Dictionary Entries", category, t.SaplingOreDict, "This item is part of which Forge Ore Dictionary entries ?  Please see General.cfg to see how to use these.");
		t.SaplingToolTip = config.getString("1.Sapling  Tool Tip Text", category, t.SaplingToolTip, "What is the Tooltip for this sapling in game ?");
		
		String shape = config.getString("2.Tree  Tree Shape", category, "Forest", "What shape should the resulting tree be ? Options: [Forest, Taiga, Savanah, Canopy, Eucalyptus, Sakura, Shrub]");
		if (shape.toLowerCase().equals("canopy"))
			t.TreeShape = WorldGenKawaiiTree.TreeShape.CANOPY;
		else if (shape.toLowerCase().equals("savanah"))
			t.TreeShape = WorldGenKawaiiTree.TreeShape.SAVANAH;
		else if (shape.toLowerCase().equals("taiga"))
			t.TreeShape = WorldGenKawaiiTree.TreeShape.TAIGA;
		else if (shape.toLowerCase().equals("eucalyptus"))
			t.TreeShape = WorldGenKawaiiTree.TreeShape.EUCALYPTUS;
		else if (shape.toLowerCase().equals("sakura"))
			t.TreeShape = WorldGenKawaiiTree.TreeShape.SAKURA;
		else if (shape.toLowerCase().equals("shrub"))
			t.TreeShape = WorldGenKawaiiTree.TreeShape.SHRUB;
		else
			t.TreeShape = WorldGenKawaiiTree.TreeShape.FOREST;
		
		t.TreeTrunkBlock = NamespaceHelper.getBlockByName(config.getString("2.Tree  Trunk Block", category, "minecraft:log", "What block acts as a trunk for this tree ?  For a list of blocks, see [DumpNames] setting in General.cfg."));
		t.TreeExternalFruit = config.getBoolean("2.Tree  External Fruit", category, t.TreeExternalFruit, "Does fruit grow external to the block ?  (If false, fruit grows inside leaf block");
		t.TreeGravityChance = config.getFloat("2.Tree  Fruit Gravity Chance", category, t.TreeGravityChance, 0.0f, 1.0f, "What is the chance, per tick, that ripe fruit will drop to the ground ?");
		t.FruitHarvest = config.getBoolean("2.Tree  Fruit Harvest", category, t.FruitHarvest, "Can right clicking on a mature fruit harvest it ?");

		t.GrowthMultiplierSapling = config.getFloat("3.Growth  Sapling Growth Multiplier", category, t.GrowthMultiplierSapling, 0.01f, 100.0f, "What growth mutlipler to apply to the growth of this tree ?");
		t.GrowthMultiplierLeaf = config.getFloat("3.Growth  Fruit Spawn Multiplier", category, t.GrowthMultiplierLeaf, 0.01f, 100.0f, "What growth multiplier to apply to the generation of fruit ?");
		t.GrowthMultiplierFruit = config.getFloat("3.Growth  Fruit Growth Multiplier", category, t.GrowthMultiplierFruit, 0.01f, 100.0f, "What growth multiplier to apply to the maturation of fruit ?");
		
		t.DropTableRipeString = config.getString("4.Drops  Ripe Fruit Drop Table", category, t.DropTableDestroyedString, "What is the drop table for Ripe fruit ? Please see General.cfg to see how to use these.");
		t.DropTableUnripeString = config.getString("4.Drops  Unripe Fruit Drop Table", category, t.DropTableDestroyedString, "What is the drop table for Unripe fruit ? Please see General.cfg to see how to use these.");;
		t.DropTableDestroyedString = config.getString("4.Drops  Destroyed Leaf Drop Table", category, t.DropTableDestroyedString, "What is the drop table for Leaf Blocks without fruit ? Please see General.cfg to see how to use these.");
		t.DropTableShearedString = config.getString("4.Drops  Sheared Leaf Drop Table", category, t.DropTableShearedString, "What is the drop table for Leaf Blocks when sheared ? Please see General.cfg to see how to use these.");
		
		WorldGenKawaiiBaseWorldGen.WorldGen gen = new WorldGen();
		gen.weight = config.getInt("5.WorldGen  Weight", category, gen.weight, 0, 1000, "How often should this tree attempt to spawn ?  A weight of 1 will attempt to spawn this treey in every chunk, 5 in every 5 chunks, etc.. A weight of 0 disables world gen for this tree.");
		gen.minRainfall = config.getFloat("5.WorldGen  Biome Humidity Minimum", category, gen.minRainfall, 0.0f, 1.0f, "What is the minimum Humidity a biome must have to spawn this tree ?");
		gen.maxRainfall = config.getFloat("5.WorldGen  Biome Humidity Maximum", category, gen.maxRainfall, 0.0f, 1.0f, "What is the maximum Humidity a biome must have to spawn this tree ?");
		gen.minTemperature = config.getFloat("5.WorldGen  Biome Temperature Minimum", category, gen.minTemperature, -0.5f, 2.0f, "What is the minimum Temperature a biome must have to spawn this tree ?");
		gen.maxTemperature = config.getFloat("5.WorldGen  Biome Temperature Maximum", category, gen.maxTemperature, -0.5f, 2.0f, "What is the maximum Temperature a biome must have to spawn this tree ?");
		gen.biomeBlacklist = config.getString("5.WorldGen  Biome Blacklist", category, gen.biomeBlacklist, "What biomes do you not want this to spawn on ?  For a list of biomes and their Humidity/Temperature, see [DumpNames] setting in General.cfg").toLowerCase();

		
		category = "Kawaiicrops: " + name + " tree fruit";
		
		config.setCategoryComment(category, 
				"Resource Pack settings for " + name + " Tree Fruit\n\n" +
				"Langage Name: item.kawaiicrops." + name + ".sapling.name\n" +
				"Langage Name: item.kawaiicrops." + name + ".fruit.name\n\n" +
				"Texture Name: textures/items/" + name + ".sapling.png\n"+
				"Texture Name: textures/items/" + name + ".fruit.png");
		
		t.FruitEdible = config.getBoolean("1.Fruit  Edible", category, t.FruitEdible, "Is the fruit also a food ?");
		t.FruitHunger = config.getInt("1.Fruit  Hunger", category, t.FruitHunger, 0, 20, "If Edible, how many half shanks of food does this restore ?");
		t.FruitSaturation = config.getFloat("1.Fruit  Saturation", category, t.FruitSaturation, 0.0f, 20.0f, "If Edible, how is the saturating is this food ?");
		t.FruitPotionEffets = new PotionEffectHelper(config.getString("2.Other  Potion Effects", category, "", "What potion effect do you want triggered on eating this crop ?  Please see General.cfg to see how to use these."));
		t.FruitOreDict = config.getString("2.Other  Ore Dictionary Entries", category, t.FruitOreDict, "This item is part of which Forge Ore Dictionary entries ?  Please see General.cfg to see how to use these.");
		t.FruitToolTip = config.getString("2.Other  Tool Tip Text", category, t.FruitToolTip, "What is the Tooltip for this fruit in game ?");
		t.SeedsMysterySeedWeight = config.getInt("2.Other  Mystery Seed Weight", category, t.SeedsMysterySeedWeight, 0, 1000, "If mystery seeds enabled, what weight should this have on mystery seed results (0 = None)");
	
		t.register(gen);
		
		return t;
	}
	
	private BlockKawaiiBarrel loadBarrel(Configuration config, String name)
	{
		if (name == null || name.length() == 0) return null;
		
		BlockKawaiiBarrel b = new BlockKawaiiBarrel(name);
		String category = "Kawaiicrops: " + name + " barrel";
		
		config.setCategoryComment(category, 
				"Resource Pack settings for " + name + " Barrel\n\n"+
				"Langage Name: tile.kawaiicrops." + name + ".barrel.name\n\n"+
				"Texture Name: textures/blocks/"+ name + ".barrel.png\n"+
				"Texture Name: textures/blocks/"+ name + ".barrel_label_unfinished.png\n"+
				"Texture Name: textures/blocks/"+ name + ".barrel_label_finished.png\n"+
				"Texture Name: textures/blocks/"+ name + ".barrel_label_ruined.png");
		
		b.Enabled = config.getBoolean("0.  Enabled", category, b.Enabled, "Is this a block in minecraft ?  Defaults to false to allow you to configure before putting it in the game.");
		
		b.FinishedTime = config.getInt("1.  Finished Time", category, b.FinishedTime, 1, 1000, "How many random ticks before the barrel is finished/ripe ?");
		b.RuinedTime = config.getInt("1.  Ruined Time", category, b.RuinedTime, 0, 1000, "How many random ticks before the barrel is ruined/spoiled ? A value of 0 means the barrel never spoils");
		
		boolean barrel = config.getBoolean("1.  Render as Barrel", category, true, "Render this barrel as a barrel ? If false, renders as a crate instead");
		b.model = (barrel ? BlockKawaiiBarrel.BarrelModel.BARREL : BlockKawaiiBarrel.BarrelModel.CRATE);
		
		b.RequiredBlockString = config.getString("2.  Required Blocks", category, b.RequiredBlockString, "Which blocks are required (such as ice) ?  If this block is not nearby the barrel will be ruined. Separate blocks with a space. Leave this empty to have no required blocks. See general.cfg and dump.cfg for a list of block IDs.");
		b.ForbiddenBlockString = config.getString("2.  Forbidden Blocks", category, b.ForbiddenBlockString, "Which blocks are forbidden (such as lava) ?  If this block is nearby the barrel will be ruined. Separate blocks with a space. Leave this empty to have no required blocks. See general.cfg and dump.cfg for a list of block IDs.");
		b.SearchRadius = config.getInt("2.  Search Radius", category, b.SearchRadius, 1, 4, "How many blocks should the barrel look for forbidden and required blocks ?");
		b.ResetOnRuined = config.getBoolean("2.  Reset On Ruined", category, b.ResetOnRuined, "Should the barrel reset to a fresh (just placed) state if ruined ?");
		
		b.UnfinishedDropTableString = config.getString("3.  Unfinished Drop Table", category, "", "What is the drop table for an unfinished barrel ? Please see General.cfg to see how to use these.");
		b.FinishedDropTableString = config.getString("3.  Finished Drop Table", category, "", "What is the drop table for a finished barrel ? Please see General.cfg to see how to use these.");
		b.RuinedDropTableString = config.getString("3.  Ruined Drop Table", category, "", "What is the drop table for a ruined barrel ? Please see General.cfg to see how to use these.");
		
		b.UnfinishedTooltip = config.getString("4.  Unfinished Tooltip Text", category, b.UnfinishedTooltip, "What tooltip do you want to use for an unfinished barrel ?");
		b.FinishedTooltip = config.getString("4.  Finished Tooltip Text", category, b.FinishedTooltip, "What tooltip do you want to use for a finished barrel ?");
		b.RuinedTooltip = config.getString("4.  Ruined Tooltip Text", category, b.RuinedTooltip, "What tooltip do you want to use for a ruined barrel ?");
		
		b.register();
		
		return b;
	}
	
	private BlockKawaiiCake loadCake(Configuration config, String name)
	{
		if (name == null || name.length() == 0) return null;
		
		BlockKawaiiCake c = new BlockKawaiiCake(name);
		String category = "Kawaiicrops: " + name + " cake";
		
		c.Enabled = config.getBoolean("0.  Enabled", category, c.Enabled, "Is this a block in minecraft ? Defaults to false to allow you to configure before putting it in game.");
		c.Hunger = config.getInt("1.General  Hunger Restored", category, c.Hunger, 0, 20, "How many half shanks of food does eating a slice of cake restore ?");
		c.Saturation = config.getFloat("1.General  Saturation", category, c.Saturation, 0.0F, 20.0f, "How saturating is a slice of cake ?");
		c.SliceItemString = config.getString("1.General  Slice Item", category, c.SliceItemString, "Should this cake give you an item when clicked ? If so, which item; please see General.cfg and Dump.cfg to get a list of items.");
		c.ToolTipText = config.getString("2.Other  Tool Tip Text", category, c.ToolTipText, "What is the Tooltip for the cake in game ?");
		c.Potion = new PotionEffectHelper(config.getString("2.Other  Potion Effect", category, "", "What potion effect do you want triggered on eating this cake ?  Please see General.cfg to see how to use these."));
		c.OreDict = config.getString("2.Other  Ore Dictionary Entries", category, "", "This item is part of which Forge Ore Dictionary entries ?  Please see General.cfg to see how to use these.");
		
		
		config.setCategoryComment(category, 
				"Resource Pack settings for " + name + " Cake\n\n" +
				"Langage Name: item.kawaiicrops." + name + ".cake.name\n" +
				"Langage Name: tile.kawaiicrops." + name + ".cake.name\n\n" +
				"Texture Name: textures/items/" + name + ".cake.png\n\n" +
				"Texture Name: textures/blocks/" + name + ".cake_top.png\n" +
				"Texture Name: textures/blocks/" + name + ".cake_bottom.png\n" +
				"Texture Name: textures/blocks/" + name + ".cake_side.png\n" +
				"Texture Name: textures/blocks/" + name + ".cake_inner.png");
		
		c.register();
		
		return c;
	}
	
	private ItemKawaiiFood loadFood(Configuration config, String name)
	{
		if (name == null || name.length() == 0) return null;
		
		String category = "Kawaiicrops: " + name;
		
		boolean enabled = config.getBoolean("0.  Enabled", category, false, "Is this an item in minecraft ? Defaults to false to allow you to configure before putting it in the game.");
		int hunger = config.getInt("1.General  Hunger", category, 1, 0, 20, "How many half shanks of food does this restore ?");
		float saturation = config.getFloat("1.General  Saturation", category, 0, 0, 20.0F, "How saturating is this food ?");
		boolean drinkable = config.getBoolean("1.General  Drink", category, false, "Do you drink this instead of eat ?");
		boolean anytime = config.getBoolean("1.General  Eat Anytime", category, false, "Can you eat this food even while full ?");
		String toolTipText = config.getString("2.Other  Tool Tip Text", category, "", "What is the Tooltip for this food ?");
		PotionEffectHelper potion = new PotionEffectHelper(config.getString("2.Other  Potion Effect", category, "", "What potion effect do you want triggered on eating this food ?  Please see General.cfg to see how to use these."));
		String oreDict = config.getString("2.Other  Ore Dictionary Entries", category, "", "This item is part of which Forge Ore Dictionary entries ?  Please see General.cfg to see how to use these.");
		String container = config.getString("2.Other  Container Item", category, "", "What item is used to hold this food, which might be returned upon eating it (Ex: minecraft:bowl) ?  Please see General.cfg and enable Dump to get a list of all items.");
		
		config.setCategoryComment(category, 
				"Resource Pack settings for " + name + "\n\n" +
				"Langage Name: item.kawaiicrops." + name + ".name\n" +
				"Texture Name: textures/items/" + name + ".png");
		
		ItemKawaiiFood food = new ItemKawaiiFood(name, toolTipText, hunger, saturation, potion, drinkable, anytime);
		food.OreDict = oreDict;
		food.ContainerItemString = container;

		if (enabled)
			food.register();
		
		return food;
	}
	
	private ItemKawaiiIngredient loadIngredient(Configuration config, String name)
	{
		if (name == null || name.length() == 0) return null;
		
		String category = "Kawaiicrops: " + name;
		
		
		boolean enabled = config.getBoolean("0.  Enabled", category, false, "Is this an item in minecraft ? Defaults to false to allow you to configure before putting it in the game.");
		int stack = config.getInt("1.  Stack Size", category, 64, 1, 64, "What is the maximum number of items this ingredient can be stacked to ?");
		String toolTipText = config.getString("2.Other  Tool Tip Text", category, "", "What is the Tooltip for this ingredient ?");
		String oreDict = config.getString("2.Other  Ore Dictionary Entries", category, "", "This item is part of which Forge Ore Dictionary entries ?  Please see General.cfg to see how to use these.");
		String container = config.getString("2.Other  Container Item", category, "", "What item is used to hold this ingredient, which might be returned upon crafting with it (Ex: minecraft:bowl) ?  Please see General.cfg and enable Dump to get a list of all items.");
		
		config.setCategoryComment(category, 
				"Resource Pack settings for " + name + "\n\n" +
				"Langage Name: item.kawaiicrops." + name + ".name\n" +
				"Texture Name: textures/items/" + name + ".png");
		
		ItemKawaiiIngredient ingredient = new ItemKawaiiIngredient(name, toolTipText);
		ingredient.OreDict = oreDict;
		ingredient.ContainerItemString = container;
		ingredient.setMaxStackSize(stack);
		
		if (enabled)
			ingredient.register();
		
		return ingredient;
	}
	
	private void loadClothes(Configuration config, String name)
	{
		if (name == null || name.length() == 0) return;
		
		String category = "Kawaiicrops: " + name;
		
		boolean enabledHat = config.getBoolean("0.  Enabled - Hat", category, false, "Is this an hat in minecraft ?");
		boolean enabledTop = config.getBoolean("0.  Enabled - Top", category, false, "Is this an top in minecraft ?");
		boolean enabledPants = config.getBoolean("0.  Enabled - Pants", category, false, "Are these pants in minecraft ?");
		boolean enabledShoes = config.getBoolean("0.  Enabled - Shoes", category, false, "Are these shoes in minecraft ?");
		
		String toolTipHat = config.getString("1.  ToolTip - Hat", category, "", "What tooltip do you want this hat to have ?");
		String toolTipTop = config.getString("1.  ToolTip - Top", category, "", "What tooltip do you want this top to have ?");
		String toolTipPants = config.getString("1.  ToolTip - Pants", category, "", "What tooltip do you want these pants to have ?");
		String toolTipShoes = config.getString("1.  ToolTip - Shoes", category, "", "What tooltip do you want these shoes to have ?");

		String armorType = config.getString("2.  Armor Type", category, "cloth", "What armor type is this ? [Options: cloth, leather, gold, iron, chain, diamond]").toLowerCase();
		
		ArmorMaterial mat;
		if (armorType.equals("leather")) mat = ArmorMaterial.CLOTH;
		else if (armorType.equals("gold")) mat = ArmorMaterial.GOLD;
		else if (armorType.equals("iron")) mat = ArmorMaterial.IRON;
		else if (armorType.equals("chain")) mat = ArmorMaterial.IRON;
		else if (armorType.equals("diamond")) mat = ArmorMaterial.DIAMOND;
		else mat = ModItems.ClothArmorMaterial;
	
		config.setCategoryComment(category, 
				"Resource Pack settings for " + name + "\n\n" +
				"Langage Name: item.kawaiicrops." + name + ".hat.name\n" +
				"Langage Name: item.kawaiicrops." + name + ".top.name\n" +
				"Langage Name: item.kawaiicrops." + name + ".pants.name\n" +
				"Langage Name: item.kawaiicrops." + name + ".shoes.name\n" +
				"\n"+
				"Texture Name: textures/items/" + name + ".hat.png\n" +
				"Texture Name: textures/items/" + name + ".top.png\n" +
				"Texture Name: textures/items/" + name + ".pants.png\n" +
				"Texture Name: textures/items/" + name + ".shoes.png\n" +
				"\n"+
				"Texture Name: textures/models/armor/" + name + "_1.png\n" +
				"Texture Name: textures/models/armor/" + name + "_2.png\n" +
				"");
		
		if (enabledHat)
			(new ItemKawaiiClothes(name, mat, 0, new String[] { toolTipHat, toolTipTop, toolTipPants, toolTipShoes })).register();
		if (enabledTop)
			(new ItemKawaiiClothes(name, mat, 1, new String[] { toolTipHat, toolTipTop, toolTipPants, toolTipShoes })).register();
		if (enabledPants)
			(new ItemKawaiiClothes(name, mat, 2, new String[] { toolTipHat, toolTipTop, toolTipPants, toolTipShoes })).register();
		if (enabledShoes)
			(new ItemKawaiiClothes(name, mat, 3, new String[] { toolTipHat, toolTipTop, toolTipPants, toolTipShoes })).register();
	}
	
	private boolean arrayHasString(String[] array)
	{
		for (String s : array)
			if (s.length() > 0)
				return true;
		return false;
	}
	
	private void sortCategory(Configuration config, String key, String category, String[] vars) 
	{
		if (!SortLists) return;
		
		String sorted = "";
		Arrays.sort(vars);
		
		for (String var : vars)
			sorted += var + " ";
		
		config.get(category, key, "").set(sorted);
	}
	
}
