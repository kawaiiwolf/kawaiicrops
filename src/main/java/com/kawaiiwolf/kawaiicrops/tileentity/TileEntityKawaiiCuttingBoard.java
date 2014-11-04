package com.kawaiiwolf.kawaiicrops.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityKawaiiCuttingBoard extends TileEntityKwaiiCooker 
{

	@Override
	protected int getInputSlots() { return 1; }

	@Override
	protected int getOutputSlots() { return 1; }

	@Override
	protected boolean itemAllowedByRecipie(ItemStack item, ItemStack[] current) 
	{
		return true;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player) 
	{
		if (isItemValidForSlot(0, player.getCurrentEquippedItem()))
		{
			this.setInventorySlotContents(0, player.getCurrentEquippedItem());
			player.setCurrentItemOrArmor(0, null);
			world.markBlockForUpdate(x, y, z);
			return true;
		}
		if (getStackInSlot(0) != null)
		{
			dropBlockAsItem(world, x, y, z, takeStack(0));
			world.markBlockForUpdate(x, y, z);
			return true;
		}
		return false;
	}

	

}
