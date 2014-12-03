package com.kawaiiwolf.kawaiicrops.tileentity;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiChurn;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiCookingBase;
import com.kawaiiwolf.kawaiicrops.renderer.TexturedIcon;

public class TileEntityKawaiiChurn extends TileEntityKawaiiCookingBlock {

	@Override
	protected int getInputSlots() { return 1; }

	@Override
	public TexturedIcon[] getDisplayItems() { return null; }

	@Override
	public String getWAILATip() { return null; }
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player) 
	{
		return true;
	}

	@Override
	protected ArrayList<RecipeKawaiiCookingBase> getRecipes(String filter) 
	{
		return dummy.getAllRecipes();
	}
	private static RecipeKawaiiChurn dummy = new RecipeKawaiiChurn();
}
