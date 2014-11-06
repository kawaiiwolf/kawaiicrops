package com.kawaiiwolf.kawaiicrops.tileentity;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiCookingBase;

public class TileEntityKawaiiBigPot extends TileEntityKawaiiCookingBlock 
{

	@Override
	protected int getInputSlots() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected ArrayList<RecipeKawaiiCookingBase> getRecipies() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player) {
		// TODO Auto-generated method stub
		return false;
	}

	public ItemStack getDisplayItem() 
	{
		// TODO Auto-generated method stub
		return null;
	}

}
