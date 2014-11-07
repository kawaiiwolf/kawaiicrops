package com.kawaiiwolf.kawaiicrops.block;

import com.kawaiiwolf.kawaiicrops.tileentity.TileEntityKawaiiFryingPan;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockKawaiiFryingPan extends BlockKawaiiCookingBlock {

	protected BlockKawaiiFryingPan() 
	{
		super(Material.iron, "fryingpan");
		maxY = 0.125d;
		minX = minZ = 0.0625d;
		maxX = maxZ = 1.0d - minX;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) 
	{
		return new TileEntityKawaiiFryingPan();
	}

}
