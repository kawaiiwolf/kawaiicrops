package com.kawaiiwolf.kawaiicrops.item;

import java.util.ArrayList;

import net.minecraft.item.Item;
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
	public static ItemKawaiiStickOfTruth Stick = new ItemKawaiiStickOfTruth();
	
	public static void register()
	{
		GameRegistry.registerItem(HungerPotion, Constants.MOD_ID + ".hungerpotion");
		GameRegistry.registerItem(Stick, Constants.MOD_ID + ".stickoftruth");
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
