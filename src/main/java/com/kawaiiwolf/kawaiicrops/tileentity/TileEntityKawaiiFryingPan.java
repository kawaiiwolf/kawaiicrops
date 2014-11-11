package com.kawaiiwolf.kawaiicrops.tileentity;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.kawaiiwolf.kawaiicrops.lib.NamespaceHelper;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiCookingBase;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiFryingPan;

public class TileEntityKawaiiFryingPan extends TileEntityKawaiiCookingBlock
{
	public boolean jitter = false;
	
	public TileEntityKawaiiFryingPan()
	{
		super();
		state = "clean";
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player) 
	{
		// If we're done cooking, pop off items ! 
		
		if (player.getCurrentEquippedItem() == null)
		{
			if (player.isSneaking())
			{
				dropAllItems(world, x, y, z);
				state = "clean";
				cookTime = 0;
				
				System.out.println("Sparkly Clean");
			}
			else if (cookTime <= 1)
				dropAllItems(world, x, y, z);
		}
		else
		{
			if (cookTime <= 1)
			{
				int slot = getFirstOpenSlot();
				if(state.equals("clean") && RecipeKawaiiFryingPan.CookingOilItems.contains(player.getCurrentEquippedItem().getItem()))
				{
					player.getCurrentEquippedItem().stackSize--;
					state = "oiled";
					
					System.out.println("Oiling Pan");
				}
				else if (slot != -1 && isItemValidForSlot(slot, player.getCurrentEquippedItem()))
				{
					System.out.println("Adding " + NamespaceHelper.getItemName(player.getCurrentEquippedItem()) + " to the frying pan.");

					setInventorySlotContents(slot, new ItemStack(player.getCurrentEquippedItem().getItem(), 1));
					player.getCurrentEquippedItem().stackSize--;
				}
			}
		}
		return true;
	}

	@Override
	public void onRandomTick(World world, int x, int y, int z, Random rand) 
	{
		if(RecipeKawaiiCookingBase.CookingHeatSources.contains(world.getBlock(x, y - 1, z)))
		{
			System.out.println("Random Tick: Heat source below me.");
			
			if (cookTime == 0)
			{
				System.out.println("Pan is all heated up");
				cookTime++;
			}
			else if (cookTime == 1)
			{
				RecipeKawaiiFryingPan recipe = (RecipeKawaiiFryingPan) getCompleteRecipe();
				if (recipe != null)
				{
					System.out.println("Full Recipe in pan. Sizzle Sizzle Sizzle");
				}
			}
		}
		else System.out.println("Random Tick: NO HEAT SOURCE FOUND.");
	}
	
	@Override
	public void onRandomDisplayTick(World world, int x, int y, int z, Random rand) 
	{ 
		
	}
	
	@Override
	protected int getInputSlots() 
	{
		return 3;
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
	private static RecipeKawaiiFryingPan dummy = new RecipeKawaiiFryingPan();
}
