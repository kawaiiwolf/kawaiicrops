package com.kawaiiwolf.kawaiicrops.block;

import com.kawaiiwolf.kawaiicrops.lib.Constants;
import com.kawaiiwolf.kawaiicrops.tileentity.TileEntityKawaiiBigPot;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockKawaiiBigPot extends BlockKawaiiCookingBlock 
{
	protected BlockKawaiiBigPot()
	{
		super(Material.iron, "bigpot", true);
		
		this.setBlockTextureName(Constants.MOD_ID + ":bigpot");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) 
	{
		return new TileEntityKawaiiBigPot();
	}

}
