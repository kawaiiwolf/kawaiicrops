package com.kawaiiwolf.kawaiicrops.item;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
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
	
	public static int ClothArmorMaterialDurability = 30;
	public static ArmorMaterial ClothArmorMaterial;
	
	public static ArrayList<ItemKawaiiCake> ModCakes = new ArrayList<ItemKawaiiCake>();
	public static ArrayList<ItemKawaiiFood> ModFoods = new ArrayList<ItemKawaiiFood>();
	public static ArrayList<ItemKawaiiIngredient> ModIngredients = new ArrayList<ItemKawaiiIngredient>();
	public static ArrayList<ItemKawaiiSeed> ModSeeds = new ArrayList<ItemKawaiiSeed>();
	public static ArrayList<ItemKawaiiSeedFood> ModSeedFoods = new ArrayList<ItemKawaiiSeedFood>();
	public static ArrayList<ItemKawaiiClothes> ModClothes = new ArrayList<ItemKawaiiClothes>();
	
	public static HashMap<String,String> OreDictionaryBonus = new HashMap<String,String>();
	
	public static ItemHungerPotion HungerPotion = new ItemHungerPotion();
	public static ItemKawaiiMysterySeed MysterySeed = null;
	public static ItemKawaiiIngredient BurntFood = new ItemKawaiiIngredient("burntfood"," Beyond Unappetizing");
	public static ItemKawaiiIngredient RuinedFood = new ItemKawaiiIngredient("ruinedfood","Beyond Unappetizing");
	public static ItemKawaiiIngredient CookingOil = new ItemKawaiiIngredient("cookingoil", "Perfect for frying or greasing a pan.");
	public static ItemKawaiiIngredient MagicSpoon = new ItemKawaiiMagicSpoon();
	public static ItemKawaiiIngredient Steamer = new ItemKawaiiIngredient("steamer","Set on a frying pan to cook with steam.");
	
	public static boolean HungerPotionEnabled = false;
	public static boolean MagicSpoonEnabled = false;
	public static boolean MysterySeedEnabled = true;
	public static boolean MysterySeedVanilla = true;
	
	public static void register()
	{
		if (HungerPotionEnabled)
			GameRegistry.registerItem(HungerPotion, Constants.MOD_ID + ".hungerpotion");
		
		if (MagicSpoonEnabled)
			GameRegistry.registerItem(MagicSpoon, Constants.MOD_ID + ".houchou");

		MysterySeed = new ItemKawaiiMysterySeed(MysterySeedVanilla);
		GameRegistry.registerItem(MysterySeed, Constants.MOD_ID + ".mysteryseed");

		if (MysterySeedEnabled)
			MinecraftForge.addGrassSeed(new ItemStack(MysterySeed), 12);
		
		GameRegistry.registerItem(BurntFood, Constants.MOD_ID + ".burntfood");
		GameRegistry.registerItem(CookingOil, Constants.MOD_ID + ".cookingoil");
		GameRegistry.registerItem(Steamer, Constants.MOD_ID + ".steamer");
	}
	
	private static boolean hasClothArmorMaterialized = false;
	public static void initilizeClothArmorMaterial()
	{
		if (!hasClothArmorMaterialized) 
		{
			ClothArmorMaterial = EnumHelper.addArmorMaterial(Constants.MOD_ID + ".cloth.material", ClothArmorMaterialDurability, new int[]{1, 1, 1, 1}, 25);
			hasClothArmorMaterialized = true;
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
		
		for (String key : OreDictionaryBonus.keySet())
			insertIntoOreDictionary(NamespaceHelper.getItemByName(key), OreDictionaryBonus.get(key));
	}
	
	private static void insertIntoOreDictionary(Item item, String oreEntries) { insertIntoOreDictionary(new ItemStack(item), oreEntries); } 
	private static void insertIntoOreDictionary(ItemStack item, String oreEntries)
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
