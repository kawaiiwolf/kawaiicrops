package com.kawaiiwolf.kawaiicrops.block;

import com.kawaiiwolf.kawaiicrops.lib.Constants;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.material.Material;

public class BlockKawaiiCrop extends Block {
	
	public enum EnumRenderType { CROSS, HASH }
	
	// Don't register if false
	public boolean Enabled = false;
	
	// Number of crop stages
	public int CropStages = 8;

	// Render Type, # or X
	public EnumRenderType RenderType = EnumRenderType.HASH;
	
	// Max block height
	public int MaxHeight = 1;
	
	// Prevent lower blocks from hitting Meta 8 before the top
	public boolean MaxHeightRequiredToRipen = true;
	
	// Restore blocks to earlier Meta state on harvest
	public boolean MultiHarvest = false;
	
	// Meta to restore to if MultiHarvest is set
	public int UnripeMeta = 4;
	
	// Hardness of unripe block
	public float UnripeHardness = 0.5f;
	
	// Effectiveness of bonemeal.
	public int BoneMealMin = 1;
	public int BoneMealMax = 1;
	
	/* TODO: Drops

	drop tables
	grows on
	growth rate

	Number of drops
	bonus seeds
	seed return

	water ?
	seed ?
	seededible ?
	seedhunger ?
	seedsaturation ?
	Mystery Seed Weight

	seed tooltip
	fruit ?
	fruit hunger
	fruit saturation
	fruit tooltip
	*/
	
	
	public BlockKawaiiCrop(String cropName)
	{
		super(Material.rock);
		this.setBlockName(Constants.MOD_ID + "." + cropName );
		this.setBlockTextureName(Constants.MOD_ID + ":" + cropName);
		
		
		
		
		
		
	}
	
	public void register()
	{
		GameRegistry.registerBlock(this, this.getUnlocalizedName());
	}
}
