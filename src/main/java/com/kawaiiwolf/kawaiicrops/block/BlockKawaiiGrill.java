package com.kawaiiwolf.kawaiicrops.block;

import com.kawaiiwolf.kawaiicrops.lib.Constants;
import com.kawaiiwolf.kawaiicrops.tileentity.TileEntityKawaiiGrill;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockKawaiiGrill extends BlockKawaiiCookingBlock {

	protected BlockKawaiiGrill() 
	{
		super(Material.iron, "grill", true);
		
		maxY = 3.0d / 16.0d;
		minX = minZ = 0.0d / 16.0d;
		maxX = maxZ = 1.0d - minX;
		
		ToolTipText = "Remember to heat your grill before cooking !";
		
		this.setBlockTextureName(Constants.MOD_ID + ":grill");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) 
	{
		return new TileEntityKawaiiGrill();
	}
	

}
