package com.kawaiiwolf.kawaiicrops.tileentity;

import com.kawaiiwolf.kawaiicrops.lib.Constants;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModTileEntities {
	
	public static void register(){
		GameRegistry.registerTileEntity(TileEntityKawaiiCrop.class, Constants.MOD_ID + ".te.kawaiicrop");
		GameRegistry.registerTileEntity(TileEntityKawaiiCuttingBoard.class, Constants.MOD_ID + ".te.kawaiicuttingboard");
		GameRegistry.registerTileEntity(TileEntityKawaiiFryingPan.class, Constants.MOD_ID + ".te.kawaiifryingpan");
		GameRegistry.registerTileEntity(TileEntityKawaiiBigPot.class, Constants.MOD_ID + ".te.kawaiibigot");
		GameRegistry.registerTileEntity(TileEntityKawaiiChurn.class, Constants.MOD_ID + ".te.kawaiichurn");
	}
}
