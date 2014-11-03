package com.kawaiiwolf.kawaiicrops.item;

import java.util.ArrayList;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;

import com.kawaiiwolf.kawaiicrops.lib.Constants;
import com.kawaiiwolf.kawaiicrops.lib.NamespaceHelper;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModItems {

	public static ArrayList<ItemKawaiiCake> ModCakes = new ArrayList<ItemKawaiiCake>();
	public static ArrayList<ItemKawaiiFood> ModFoods = new ArrayList<ItemKawaiiFood>();
	public static ArrayList<ItemKawaiiIngredient> ModIngredients = new ArrayList<ItemKawaiiIngredient>();
	public static ArrayList<ItemKawaiiSeed> ModSeeds = new ArrayList<ItemKawaiiSeed>();
	public static ArrayList<ItemKawaiiSeedFood> ModSeedFoods = new ArrayList<ItemKawaiiSeedFood>();
	
	public static ItemHungerPotion HungerPotion = new ItemHungerPotion();
	public static ItemKawaiiMysterySeed MysterySeed = null;
	
	public static boolean HungerPotionEnabled = true;
	public static boolean MysterySeedEnabled = true;
	public static boolean MysterySeedVanilla = true;
	
	public static void register()
	{
		if (HungerPotionEnabled)
			GameRegistry.registerItem(HungerPotion, Constants.MOD_ID + ".hungerpotion");

		if (MysterySeedEnabled)
		{
			MysterySeed = new ItemKawaiiMysterySeed(MysterySeedVanilla);
			GameRegistry.registerItem(MysterySeed, Constants.MOD_ID + ".mysteryseed");
			MinecraftForge.addGrassSeed(new ItemStack(MysterySeed), 12);
		}
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
	}
	
	private static void insertIntoOreDictionary(Item item, String oreEntries)
	{
		if (item == null || oreEntries == null) 
			return;
		
		// Make sure this item has been registered/enabled
		if (NamespaceHelper.getItemName(item) == null) 
			return;
		
		for (String ore : oreEntries.split("[ ]"))
			if (ore != null && ore.length() >= 0)
				OreDictionary.registerOre(ore, item);
	}
}
