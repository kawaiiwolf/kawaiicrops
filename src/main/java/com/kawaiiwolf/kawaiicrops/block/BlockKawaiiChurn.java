package com.kawaiiwolf.kawaiicrops.block;

import com.kawaiiwolf.kawaiicrops.lib.Constants;
import com.kawaiiwolf.kawaiicrops.tileentity.TileEntityKawaiiChurn;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockKawaiiChurn extends BlockKawaiiCookingBlock {

	public BlockKawaiiChurn()
	{
		super(Material.wood, "churn", false);
		maxY = 16.0d / 16.0d;
		minX = minZ = 2.0d / 16.0d;
		maxX = maxZ = 1.0d - minX;
		
		ToolTipText = "Hand Powered.";
		
		this.setBlockTextureName(Constants.MOD_ID + ":churn");
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) 
	{
		return new TileEntityKawaiiChurn(true);
	}

}
