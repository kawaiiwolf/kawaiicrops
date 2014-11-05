package com.kawaiiwolf.kawaiicrops.tileentity;

import java.util.ArrayList;

import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiCookingBase;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiCuttingBoard;

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
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player) 
	{
		if (isItemValidForSlot(0, player.getCurrentEquippedItem()))
		{
			this.setInventorySlotContents(0, new ItemStack(player.getCurrentEquippedItem().getItem(), 1));
			player.getCurrentEquippedItem().stackSize--;
			world.markBlockForUpdate(x, y, z);
		}
		else if (getStackInSlot(0) != null)
		{
			dropBlockAsItem(world, x, y, z, takeStack(0));
			world.markBlockForUpdate(x, y, z);
		}
		return true;
	}
	
	@Override
	protected ArrayList<RecipeKawaiiCookingBase> getRecipies()
	{
		return dummy.getAllRecipies();
	}
	private static RecipeKawaiiCuttingBoard dummy = new RecipeKawaiiCuttingBoard();
	

}
