package com.kawaiiwolf.kawaiicrops.block;

import com.kawaiiwolf.kawaiicrops.lib.Constants;
import com.kawaiiwolf.kawaiicrops.tileentity.TileEntityKawaiiChurn;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockKawaiiMill extends BlockKawaiiCookingBlock {

	public BlockKawaiiMill()
	{
		super(Material.rock, "mill", false);
		maxY = 8.0d / 16.0d;
		minX = minZ = 5.0d / 16.0d;
		maxX = maxZ = 1.0d - minX;
		
		ToolTipText = "Hand Powered ... and mini !";
		
		this.setBlockTextureName(Constants.MOD_ID + ":mill");
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) 
	{
		return new TileEntityKawaiiChurn(false);
	}

}
