package com.kawaiiwolf.kawaiicrops.block;

import java.util.ArrayList;

public class ModBlocks {
	
	public static ArrayList<BlockKawaiiCrop> AllCrops = new ArrayList<BlockKawaiiCrop>();
	
	public static void registerDropTables() {
		
		for (BlockKawaiiCrop block : AllCrops)
			block.registerDropTables();
	}
	
}
