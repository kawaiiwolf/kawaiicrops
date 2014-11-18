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
import com.kawaiiwolf.kawaiicrops.block.BlockKawaiiFryingPan;
import com.kawaiiwolf.kawaiicrops.item.ModItems;
import com.kawaiiwolf.kawaiicrops.lib.NamespaceHelper;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiCookingBase;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiBigPot;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiFryingPan;
import com.kawaiiwolf.kawaiicrops.renderer.TexturedIcon;

public class TileEntityKawaiiBigPot extends TileEntityKawaiiCookingBlock
{
	public TileEntityKawaiiBigPot()
	{
		super();
		state = "clean";
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player) 
	{
		// If clicking with an empty hand
		if (player.getCurrentEquippedItem() == null)
		{
			// Clean out & Dump pan
			if (player.isSneaking() || state.equals("ruined"))
			{
				if (state.equals("ruined"))
					dropAllItems(world, x, y, z);
				else
					clearAllItems();
				state = "clean";
				cookTime = recipeHash = 0;
				
				particleBlast(world, x, y, z, "mobSpellAmbient", 8, 12, 0.1d, 0.1d, 1.0d);
			}
			// Haven't started cooking yet ! Pull recipe items.
			else if (cookTime <= 1 && recipeHash == 0)
				dropAllItems(world, x, y, z);
			
			// Pull out cooked recipe
			else if (recipeHash != 0)
			{
				RecipeKawaiiBigPot recipe = (RecipeKawaiiBigPot) this.getCurrentRecipe();
				if (recipe != null && recipe.harvest == null && cookTime > recipe.cookTime)
				{
					dropAllItems(world, x, y, z);

					if (recipe.keepLiquid && recipe.milk) state = "milk";
					else if (recipe.keepLiquid && recipe.oil) state = "oil";
					else if (recipe.keepLiquid && recipe.water) state = "water";
					else state = "clean";

					cookTime = recipeHash = 1;
				}
			}
		}
		else 
		{
			// DEBUG: Stick = fast cook
			if (player.getCurrentEquippedItem().getItem() == Items.stick) { this.onRandomTick(world, x, y, z, world.rand); } else
			
			// We haven't started cooking just yet, but the pot could be heated
			if (cookTime <= 1)
			{
				int slot = getFirstOpenSlot();
				
				// Fill the pot with Oil
				if(state.equals("clean") && RecipeKawaiiBigPot.CookingOilItems.contains(player.getCurrentEquippedItem().getItem()))
				{
					takeCurrentItemContainer(world, x, y, z, player);
					state = "oil";
				}
				
				// Fill the pot with Milk
				else if(state.equals("clean") && RecipeKawaiiBigPot.CookingMilkItems.contains(player.getCurrentEquippedItem().getItem()))
				{
					takeCurrentItemContainer(world, x, y, z, player);
					state = "milk";
				}
				
				// Fill the pot with Water
				else if(state.equals("clean") && RecipeKawaiiBigPot.CookingWaterItems.contains(player.getCurrentEquippedItem().getItem()))
				{
					takeCurrentItemContainer(world, x, y, z, player);
					state = "water";
				}
				
				// Check for valid ingredient
				else if (slot != -1 && isItemValidForSlot(slot, player.getCurrentEquippedItem()))
				{
					setInventorySlotContents(slot, takeCurrentItemContainer(world, x, y, z, player));
				}
				
				// If the pot is heated, start checking for instant cook recipes
				if (cookTime == 1 && recipeHash == 0)
				{
					RecipeKawaiiBigPot recipe = (RecipeKawaiiBigPot) getCompleteRecipe();
					if (recipe != null && recipe.cookTime == 0)
					{
						recipeHash = recipe.hashCode();
						state = "cooking";
						for (int i = 0; i < inventorySlots.length; i++)
							inventorySlots[i] = null;
						inventorySlots[0] = recipe.output.copy();
					}
				}
			}
			// Else we're cooking. Check to see if we've got the correct item to harvest
			else
			{
				RecipeKawaiiBigPot recipe = (RecipeKawaiiBigPot) this.getCurrentRecipe();
				if (cookTime > recipe.cookTime && recipe.harvest == player.getCurrentEquippedItem().getItem())
				{
					player.getCurrentEquippedItem().stackSize--;
					this.dropBlockAsItem(world, x, y, z, new ItemStack(inventorySlots[0].getItem(),1));
					if (inventorySlots[0].stackSize > 1)
						inventorySlots[0].stackSize--;
					else
					{
						inventorySlots[0] = null;
						if (recipe.keepLiquid && recipe.milk) state = "milk";
						else if (recipe.keepLiquid && recipe.oil) state = "oil";
						else if (recipe.keepLiquid && recipe.water) state = "water";
						else state = "clean";
						cookTime = recipeHash = 0;
					}
				}
			}
		}
		world.markBlockForUpdate(x, y, z);

		return true;
	}

	@Override
	public void onRandomTick(World world, int x, int y, int z, Random rand) 
	{
		if(RecipeKawaiiCookingBase.CookingHeatSources.contains(world.getBlock(x, y - 1, z)))
		{
			if (cookTime == 0 && !state.equals("clean"))
				cookTime++;
			
			// Pot hot & no set recipe, try to start cooking
			else if (cookTime == 1 && recipeHash == 0)
			{
				RecipeKawaiiBigPot recipe = (RecipeKawaiiBigPot) getCompleteRecipe();
				if (recipe != null)
				{
					recipeHash = recipe.hashCode();
					state = "cooking";
					world.markBlockForUpdate(x, y, z);
				}
			}
			
			if (recipeHash != 0)
			{
				RecipeKawaiiBigPot recipe = (RecipeKawaiiBigPot) this.getCurrentRecipe();
				
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
					cookTime = 1;
					recipeHash = 0;
					state = "ruined";
					
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
				// The tick after cooking, start burning!
				else if (cookTime > recipe.cookTime && recipe.burnTime > 0 && state.equals("cooking"))
				{
					state = "burning";
					world.markBlockForUpdate(x, y, z);
				}
			}
		}		
	}
	
	@Override
	public void onRandomDisplayTick(World world, int x, int y, int z, Random rand) 
	{ 
		if (RecipeKawaiiCookingBase.CookingHeatSources.contains(world.getBlock(x, y - 1, z)))
		{
			if (rand.nextFloat() > 0.66f)
			{
				if (state.equals("cooking") || cookTime == 1) 
					this.particleBlast(world, x, y, z, "explode", 1, 1);
				if (state.equals("burning")) 
					this.particleBlast(world, x, y, z, "smoke", 1, 1);
			}
			if (state.equals("ruined"))
				this.particleBlast(world, x, y, z, "largesmoke", 1, 2);	
		}
	}
	
	@Override
	public void dropAllItems(World world, int x, int y, int z)
	{
		RecipeKawaiiBigPot recipe = (RecipeKawaiiBigPot) this.getCurrentRecipe();
		if (recipe == null || recipe.harvest == null)
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
		if (state.equals("oil"))
			return dummy.getFilteredRecipes("oil");
		if (state.equals("milk"))
			return dummy.getFilteredRecipes("milk");
		if (state.equals("water"))
			return dummy.getFilteredRecipes("water");
		if (state.equals("clean"))
			return new ArrayList<RecipeKawaiiCookingBase>();
		
		return dummy.getAllRecipes();
	}
	private static RecipeKawaiiBigPot dummy = new RecipeKawaiiBigPot();

	private TexturedIcon[] display = new TexturedIcon[getInputSlots() + 1];
	private TexturedIcon[] fullIcon = new TexturedIcon[1];
	@Override
	public TexturedIcon[] getDisplayItems() 
	{
		if (recipeHash != 0)
		{
			RecipeKawaiiBigPot recipe = (RecipeKawaiiBigPot) getCurrentRecipe();
			if (recipe != null && recipe.texture && cookTime > recipe.cookTime)
			{
				fullIcon[0] = new TexturedIcon(BlockKawaiiBigPot.FoodTextures.get(recipe), TextureMap.locationBlocksTexture);
				return fullIcon;
			}
		}
		for (int i = 0; i < display.length && i < inventorySlots.length; i++)
			display[i] = inventorySlots[i] == null ? null : new TexturedIcon(inventorySlots[i].getIconIndex(), NamespaceHelper.isItemBlock(inventorySlots[i]) ? TextureMap.locationBlocksTexture : TextureMap.locationItemsTexture);
		return display;
	}
	
	public IIcon getDisplayLiquid()
	{
		RecipeKawaiiBigPot recipe = (RecipeKawaiiBigPot) getCurrentRecipe();
		
		if (state.equals("milk") || (recipe != null && recipe.milk))
			return BlockKawaiiBigPot.MilkTexture;
		if (state.equals("oil") || (recipe != null && recipe.oil))
			return BlockKawaiiBigPot.OilTexture;
		if (state.equals("water") || (recipe != null && recipe.water))
			return BlockKawaiiBigPot.WaterTexture;
		
		return null;
	}
}
