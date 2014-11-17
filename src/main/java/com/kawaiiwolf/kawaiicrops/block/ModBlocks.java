package com.kawaiiwolf.kawaiicrops.block;

import java.util.ArrayList;

import com.kawaiiwolf.kawaiicrops.lib.NamespaceHelper;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiBigPot;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiCookingBase;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiFryingPan;

public class ModBlocks {
	
	public static ArrayList<BlockKawaiiCrop> AllCrops = new ArrayList<BlockKawaiiCrop>();
	public static ArrayList<BlockKawaiiTreeBlocks> AllTrees = new ArrayList<BlockKawaiiTreeBlocks>();
	
	public static BlockKawaiiCuttingBoard cuttingBoard = new BlockKawaiiCuttingBoard();
	public static BlockKawaiiFryingPan fryingPan = new BlockKawaiiFryingPan();
	public static BlockKawaiiBigPot bigPot = new BlockKawaiiBigPot();
	
	public static void register()
	{
		cuttingBoard.register();
		fryingPan.register();
		bigPot.register();
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
		RecipeKawaiiFryingPan.CookingOilItems = NamespaceHelper.getItemsByName(RecipeKawaiiFryingPan.CookingOilItemsString);
		RecipeKawaiiBigPot.CookingOilItems = NamespaceHelper.getItemsByName(RecipeKawaiiBigPot.CookingOilItemsString);
		RecipeKawaiiBigPot.CookingWaterItems = NamespaceHelper.getItemsByName(RecipeKawaiiBigPot.CookingWaterItemsString);
		RecipeKawaiiBigPot.CookingMilkItems = NamespaceHelper.getItemsByName(RecipeKawaiiBigPot.CookingMilkItemsString);
	}
}
