package com.kawaiiwolf.kawaiicrops.tileentity;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.kawaiiwolf.kawaiicrops.block.BlockKawaiiBigPot;
import com.kawaiiwolf.kawaiicrops.item.ModItems;
import com.kawaiiwolf.kawaiicrops.lib.NamespaceHelper;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiCookingBase;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiBigPot;
import com.kawaiiwolf.kawaiicrops.renderer.TexturedIcon;

public class TileEntityKawaiiBigPot extends TileEntityKawaiiCookingBlock
{
	public TileEntityKawaiiBigPot()
	{
		super();
		state = "empty";
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player) 
	{
		if (player.getCurrentEquippedItem() == null)
		{
			dropAllItems(world, x, y, z);
		}
		else 
		{
			int slot = this.getFirstOpenSlot();
			if (slot == -1)
				return true;
			inventorySlots[slot] = new ItemStack(player.getCurrentEquippedItem().getItem());
			player.getCurrentEquippedItem().stackSize--;
		}
		
		return true;
	}

	@Override
	public void onRandomTick(World world, int x, int y, int z, Random rand) 
	{
		
	}
	
	@Override
	public void onRandomDisplayTick(World world, int x, int y, int z, Random rand) 
	{ 

	}
	
	@Override
	public void dropAllItems(World world, int x, int y, int z)
	{
		//RecipeKawaiiBigPot recipe = (RecipeKawaiiBigPot) this.getCurrentRecipe();
		//if (recipe == null || recipe.harvest == null)
			super.dropAllItems(world, x, y, z);
	}
	
	@Override
	protected int getInputSlots() 
	{
		return 6;
	}
	
	@Override
	protected ArrayList<RecipeKawaiiCookingBase> getRecipes(String state) 
	{
		if (state.equals("oiled"))
			return dummy.getFilteredRecipes(true);
		if (state.equals("clean"))
			return dummy.getFilteredRecipes(false);
		
		return dummy.getAllRecipes();
	}
	private static RecipeKawaiiBigPot dummy = new RecipeKawaiiBigPot();

	private TexturedIcon[] display = new TexturedIcon[getInputSlots() + 1];
	private TexturedIcon[] fullIcon = new TexturedIcon[1];
	@Override
	public TexturedIcon[] getDisplayItems() 
	{
		for (int i = 0; i < display.length && i < inventorySlots.length; i++)
			display[i] = inventorySlots[i] == null ? null : new TexturedIcon(inventorySlots[i].getIconIndex(), NamespaceHelper.isItemBlock(inventorySlots[i]) ? TextureMap.locationBlocksTexture : TextureMap.locationItemsTexture);
		return display;
	}
}
