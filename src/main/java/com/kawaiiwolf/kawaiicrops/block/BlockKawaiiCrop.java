package com.kawaiiwolf.kawaiicrops.block;

import com.kawaiiwolf.kawaiicrops.lib.Constants;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.material.Material;

public class BlockKawaiiCrop extends Block {
	
	public BlockKawaiiCrop(String cropName)
	{
		super(Material.rock);
		this.setBlockName(Constants.MOD_ID + "." + cropName );
		this.setBlockTextureName(Constants.MOD_ID + ":" + cropName);
		
		GameRegistry.registerBlock(this, this.getUnlocalizedName());
	}
}
