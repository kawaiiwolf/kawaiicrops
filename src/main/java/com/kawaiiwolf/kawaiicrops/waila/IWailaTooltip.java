package com.kawaiiwolf.kawaiicrops.waila;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public interface IWailaTooltip 
{
	public ItemStack getDisplayStack(World world, int x, int y, int z, int meta, TileEntity te);
	public String getBody(World world, int x, int y, int z, int meta, TileEntity te);
}
