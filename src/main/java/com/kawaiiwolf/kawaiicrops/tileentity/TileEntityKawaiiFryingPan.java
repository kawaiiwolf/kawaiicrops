package com.kawaiiwolf.kawaiicrops.tileentity;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiCookingBase;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiFryingPan;

public class TileEntityKawaiiFryingPan extends TileEntityKawaiiCookingBlock {

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player) 
	{
		if (player.isSneaking())
		{
			dropAllItems(world, x, y, z);
		}
		else
		{
			int slot = getFirstOpenSlot();
			if (slot == -1) 
				return true;
			getDisplayItems()[slot] = new ItemStack(player.getCurrentEquippedItem().getItem(), 1);
			player.getCurrentEquippedItem().stackSize--;
		}
		
		return true;
	}

	@Override
	protected int getInputSlots() 
	{
		return 3;
	}
	
	@Override
	protected ArrayList<RecipeKawaiiCookingBase> getRecipies() 
	{
		return dummy.getAllRecipies();
	}
	private static RecipeKawaiiFryingPan dummy = new RecipeKawaiiFryingPan();
}
