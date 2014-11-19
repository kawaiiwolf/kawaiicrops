package com.kawaiiwolf.kawaiicrops.item;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;

import com.kawaiiwolf.kawaiicrops.lib.Constants;
import com.kawaiiwolf.kawaiicrops.lib.NamespaceHelper;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ModItems {

	public static CreativeTabs KawaiiCreativeTab = new CreativeTabs("kawaiicrops") {
	    @Override
	    @SideOnly(Side.CLIENT)
	    public Item getTabIconItem() {
	        return MysterySeed;
	    }
	};
	
	public static ArrayList<ItemKawaiiCake> ModCakes = new ArrayList<ItemKawaiiCake>();
	public static ArrayList<ItemKawaiiFood> ModFoods = new ArrayList<ItemKawaiiFood>();
	public static ArrayList<ItemKawaiiIngredient> ModIngredients = new ArrayList<ItemKawaiiIngredient>();
	public static ArrayList<ItemKawaiiSeed> ModSeeds = new ArrayList<ItemKawaiiSeed>();
	public static ArrayList<ItemKawaiiSeedFood> ModSeedFoods = new ArrayList<ItemKawaiiSeedFood>();
	
	public static HashMap<String,String> OreDictionaryBonus = new HashMap<String,String>();
	
	public static ItemHungerPotion HungerPotion = new ItemHungerPotion();
	public static ItemKawaiiMysterySeed MysterySeed = null;
	public static ItemKawaiiIngredient BurntFood = new ItemKawaiiIngredient("burntfood"," Beyond Unappetizing");
	public static ItemKawaiiIngredient CookingOil = new ItemKawaiiIngredient("cookingoil", "Perfect for frying or greasing a pan.");
	public static ItemKawaiiIngredient MagicSpoon = new ItemKawaiiMagicSpoon();
	
	public static boolean HungerPotionEnabled = true;
	public static boolean MysterySeedEnabled = true;
	public static boolean MysterySeedVanilla = true;
	
	public static void register()
	{
		if (HungerPotionEnabled)
			GameRegistry.registerItem(HungerPotion, Constants.MOD_ID + ".hungerpotion");

		MysterySeed = new ItemKawaiiMysterySeed(MysterySeedVanilla);
		GameRegistry.registerItem(MysterySeed, Constants.MOD_ID + ".mysteryseed");

		if (MysterySeedEnabled)
			MinecraftForge.addGrassSeed(new ItemStack(MysterySeed), 12);
		
		GameRegistry.registerItem(BurntFood, Constants.MOD_ID + ".burntfood");
		GameRegistry.registerItem(CookingOil, Constants.MOD_ID + ".cookingoil");
		GameRegistry.registerItem(MagicSpoon, Constants.MOD_ID + ".houchou");
	}
	
	public static void registerOreDictionary()
	{
		for (ItemKawaiiCake cake : ModCakes)
			insertIntoOreDictionary(cake, cake.OreDict);
		
		for (ItemKawaiiFood food : ModFoods)
			insertIntoOreDictionary(food, food.OreDict);
		
		for (ItemKawaiiIngredient ingredient : ModIngredients)
			insertIntoOreDictionary(ingredient, ingredient.OreDict);
		
		for (ItemKawaiiSeed seed : ModSeeds)
			insertIntoOreDictionary(seed, seed.OreDict);
		
		for (ItemKawaiiSeedFood seedFood : ModSeedFoods)
			insertIntoOreDictionary(seedFood, seedFood.OreDict);
		
		for (String key : OreDictionaryBonus.keySet())
			insertIntoOreDictionary(NamespaceHelper.getItemByName(key), OreDictionaryBonus.get(key));
	}
	
	private static void insertIntoOreDictionary(Item item, String oreEntries)
	{
		if (item == null || oreEntries == null) 
			return;
		
		// Make sure this item has been registered/enabled
		if (NamespaceHelper.getItemName(item) == null) 
			return;
		
		for (String ore : oreEntries.split("[ ]"))
			if (ore != null && ore.length() > 0)
				OreDictionary.registerOre(ore, item);
	}
}
