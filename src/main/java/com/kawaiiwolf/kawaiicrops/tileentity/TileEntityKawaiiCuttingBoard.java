package com.kawaiiwolf.kawaiicrops.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TileEntityKawaiiCuttingBoard extends TileEntityKwaiiCooker 
{

	@Override
	protected int getInputSlots() { return 1; }

	@Override
	protected int getOutputSlots() { return 1; }



}
