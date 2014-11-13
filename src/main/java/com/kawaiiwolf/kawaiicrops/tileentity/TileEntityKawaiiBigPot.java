package com.kawaiiwolf.kawaiicrops.tileentity;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiBigPot;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiCookingBase;
import com.kawaiiwolf.kawaiicrops.renderer.TexturedIcon;

public class TileEntityKawaiiBigPot extends TileEntityKawaiiCookingBlock 
{

	@Override
	protected int getInputSlots() 
	{
		return 6;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	protected ArrayList<RecipeKawaiiCookingBase> getRecipes(String filter) 
	{
		return dummy.getAllRecipes();
	}
	private static RecipeKawaiiBigPot dummy = new RecipeKawaiiBigPot();
	
	
	@Override
	public TexturedIcon[] getDisplayItems() 
	{
		return null;
	}

}
