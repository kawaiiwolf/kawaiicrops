package com.kawaiiwolf.kawaiicrops.tileentity;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.kawaiiwolf.kawaiicrops.item.ModItems;
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
				clearAllItems();
				state = "clean";
				cookTime = 0;
			}
			// Haven't started cooking yet ! Pull recipe items.
			else if (cookTime <= 1)
				dropAllItems(world, x, y, z);
			else if (recipeHash != 0)
			{
				RecipeKawaiiFryingPan recipe = (RecipeKawaiiFryingPan) this.getCurrentRecipe();
				if (recipe.harvest == null && cookTime > recipe.cookTime)
				{
					dropAllItems(world, x, y, z);
					state = (recipe.greasy ? "oiled" : "clean");
					cookTime = 1;
				}
			}
		}
		else
		{
			// DEBUG !
			if (player.getCurrentEquippedItem().getItem() == Items.stick)
			{
				this.onRandomTick(world, x, y, z, world.rand);
			}
			else if (cookTime <= 1)
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
			else
			{
				RecipeKawaiiFryingPan recipe = (RecipeKawaiiFryingPan) this.getCurrentRecipe();
				if (cookTime > recipe.cookTime && recipe.harvest == player.getCurrentEquippedItem().getItem())
				{
					player.getCurrentEquippedItem().stackSize--;
					this.dropBlockAsItem(world, x, y, z, new ItemStack(inventorySlots[0].getItem(),1));
					if (inventorySlots[0].stackSize > 1)
						inventorySlots[0].stackSize--;
					else
					{
						inventorySlots[0] = null;
						state = (recipe.greasy ? "oiled" : "clean");
						cookTime = 1;
					}
				}
			}
			world.markBlockForUpdate(x, y, z);
		}
		return true;
	}

	@Override
	public void onRandomTick(World world, int x, int y, int z, Random rand) 
	{
		if(RecipeKawaiiCookingBase.CookingHeatSources.contains(world.getBlock(x, y - 1, z)))
		{
			if (cookTime == 0)
				cookTime++;
			
			// Pan hot & no set recipe, try to start cooking
			else if (cookTime == 1 && recipeHash == 0)
			{
				RecipeKawaiiFryingPan recipe = (RecipeKawaiiFryingPan) getCompleteRecipe();
				if (recipe != null)
				{
					recipeHash = recipe.hashCode();
					world.markBlockForUpdate(x, y, z);
				}
			}
			
			if (recipeHash != 0)
			{
				RecipeKawaiiFryingPan recipe = (RecipeKawaiiFryingPan) this.getCurrentRecipe();
				
				// Handle changed/removed recipes
				if (recipe == null)
				{
					recipeHash = 0; 
					return;
				}
				cookTime++;

				// Burned
				if (recipe.burnTime > 0 && cookTime > recipe.cookTime + recipe.burnTime)
				{
					inventorySlots[0] = new ItemStack(ModItems.BurntFood);
					cookTime = recipeHash = 0;
					
					world.markBlockForUpdate(x, y, z);
				}
				// Cooked
				else if (inventorySlots[0] == null && cookTime > recipe.cookTime)
				{
					for (int i = 0; i < inventorySlots.length; i++)
						inventorySlots[i] = null;
					inventorySlots[0] = recipe.output.copy();
					
					
					world.markBlockForUpdate(x, y, z);
				}
			}
		}
	}
	
	@Override
	public void onRandomDisplayTick(World world, int x, int y, int z, Random rand) 
	{ 
		if (cookTime > 1)
			jitter = true;
	}
	
	@Override
	public void dropAllItems(World world, int x, int y, int z)
	{
		RecipeKawaiiFryingPan recipe = (RecipeKawaiiFryingPan) this.getCurrentRecipe();
		if (recipe == null || recipe.harvest == null)
			super.dropAllItems(world, x, y, z);
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
