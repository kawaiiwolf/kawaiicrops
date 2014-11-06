package com.kawaiiwolf.kawaiicrops.block;

import java.util.ArrayList;

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
}
