package com.kawaiiwolf.kawaiicrops.block;

import java.util.ArrayList;

import com.kawaiiwolf.kawaiicrops.lib.NamespaceHelper;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiBigPot;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiCookingBase;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiFryingPan;

public class ModBlocks 
{
	public static ArrayList<BlockKawaiiCrop> AllCrops = new ArrayList<BlockKawaiiCrop>();
	public static ArrayList<BlockKawaiiTreeBlocks> AllTrees = new ArrayList<BlockKawaiiTreeBlocks>();
	public static ArrayList<BlockKawaiiCake> AllCakes = new ArrayList<BlockKawaiiCake>();
	
	public static BlockKawaiiCuttingBoard cuttingBoard = new BlockKawaiiCuttingBoard();
	public static BlockKawaiiFryingPan fryingPan = new BlockKawaiiFryingPan();
	public static BlockKawaiiBigPot bigPot = new BlockKawaiiBigPot();
	public static BlockKawaiiChurn churn = new BlockKawaiiChurn();
	public static BlockKawaiiMill mill = new BlockKawaiiMill();
	public static BlockKawaiiGrill grill = new BlockKawaiiGrill();
	
	public static void register()
	{
		cuttingBoard.register();
		fryingPan.register();
		bigPot.register();
		churn.register();
		mill.register();
		grill.register();
	}
	
	public static void registerDropTables() 
	{
		for (BlockKawaiiCrop crop : AllCrops)
			crop.registerDropTables();
		
		for (BlockKawaiiTreeBlocks tree : AllTrees)
			tree.registerDropTables();
	}
	
	public static void registerCookingBlockLists()
	{
		RecipeKawaiiCookingBase.CookingHeatSources = NamespaceHelper.getBlocksByName(RecipeKawaiiCookingBase.CookingHeatSourcesString);
		RecipeKawaiiCookingBase.CookingFire = NamespaceHelper.getBlocksByName(RecipeKawaiiCookingBase.CookingFireString);
		RecipeKawaiiFryingPan.CookingOilItems = NamespaceHelper.getItemsByName(RecipeKawaiiFryingPan.CookingOilItemsString);
		RecipeKawaiiBigPot.CookingOilItems = NamespaceHelper.getItemsByName(RecipeKawaiiBigPot.CookingOilItemsString);
		RecipeKawaiiBigPot.CookingWaterItems = NamespaceHelper.getItemsByName(RecipeKawaiiBigPot.CookingWaterItemsString);
		RecipeKawaiiBigPot.CookingMilkItems = NamespaceHelper.getItemsByName(RecipeKawaiiBigPot.CookingMilkItemsString);
	}
}
