package com.kawaiiwolf.kawaiicrops.tileentity;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.kawaiiwolf.kawaiicrops.block.ModBlocks;
import com.kawaiiwolf.kawaiicrops.item.ModItems;
import com.kawaiiwolf.kawaiicrops.lib.NamespaceHelper;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiBigPot;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiCookingBase;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiFryingPan;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiGrill;
import com.kawaiiwolf.kawaiicrops.renderer.TexturedIcon;

public class TileEntityKawaiiGrill extends TileEntityKawaiiCookingBlock 
{

	public TileEntityKawaiiGrill()
	{
		super();
		state = "cool";
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

				state = "cool";
				cookTime = recipeHash = 0;
				
				particleBlast(world, x, y, z, "mobSpellAmbient", 8, 12, 0.1d, 0.1d, 1.0d);
			}
			// Haven't started cooking yet ! Pull recipe items.
			else if (cookTime <= 1 && recipeHash == 0)
				dropAllItems(world, x, y, z);
			
			// Pull out cooked recipe
			else if (recipeHash != 0)
			{
				RecipeKawaiiGrill recipe = (RecipeKawaiiGrill) this.getCurrentRecipe();
				if (recipe != null && cookTime > recipe.cookTime)
				{
					dropAllItems(world, x, y, z);
					state = (recipe.dry ? "cool" : "hot");
					cookTime = (recipe.dry ? 0 : 1);
					recipeHash = 0;
				}
			}
		}
		else
		{
			// DEBUG: fast cook
			if (player.getCurrentEquippedItem().getItem() == ModItems.MagicSpoon) { this.onRandomTick(world, x, y, z, world.rand); } 
			
			// We haven't started cooking just yet, but the pan could be heated
			else if (cookTime <= 1)
			{
				int slot = getFirstOpenSlot();
				
				// Check for valid ingredient
				if (slot != -1 && isItemValidForSlot(slot, player.getCurrentEquippedItem()))
				{
					setInventorySlotContents(slot, takeCurrentItemContainer(world, x, y, z, player));
				}
				
				// If the pan is heated, start checking for instant cook recipes
				if (cookTime == 1 && recipeHash == 0)
				{
					RecipeKawaiiGrill recipe = (RecipeKawaiiGrill) getCompleteRecipe();
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
		}
		world.markBlockForUpdate(x, y, z);

		return true;
	}
	
	@Override
	public void onRandomTick(World world, int x, int y, int z, Random rand) 
	{
		if(RecipeKawaiiCookingBase.CookingFire.contains(world.getBlock(x, y - 1, z)))
		{
			// Heat ruins drying food
			if (state.equals("drying") && recipeHash != 0)
			{
				RecipeKawaiiGrill recipe = (RecipeKawaiiGrill) this.getCurrentRecipe();
				
				if (recipe != null)
				{
					inventorySlots[0] = new ItemStack(ModItems.BurntFood, recipe.input.size());
					cookTime = 1;
					recipeHash = 0;
					state = "ruined";
					
					world.markBlockForUpdate(x, y, z);
				}
			}
			// State cool -> hot
			else if (state.equals("cool"))
				state = "hot";
			
			// Pan hot & no set recipe, try to start cooking
			else if (state.equals("hot") && recipeHash == 0)
			{
				RecipeKawaiiGrill recipe = (RecipeKawaiiGrill) getCompleteRecipe();
				if (recipe != null)
				{
					recipeHash = recipe.hashCode();
					state = "cooking";
					world.markBlockForUpdate(x, y, z);
				}
			}
		}
		else
		{
			// Lost the fire, just delay
			if (state.equals("cooking") || state.equals("burning"))
				return;
			
			// Pan cooled down
			if (state.equals("hot"))
			{
				state = "cool";
			}
			
			// If it's already cool, check to see if we should start drying.
			else if (state.equals("cool"))
			{
				RecipeKawaiiGrill recipe = (RecipeKawaiiGrill) getCompleteRecipe();
				if (recipe != null)
				{
					recipeHash = recipe.hashCode();
					state = "drying";
					world.markBlockForUpdate(x, y, z);
				}
			}
		}
			
		if (recipeHash != 0)
		{
			RecipeKawaiiGrill recipe = (RecipeKawaiiGrill) this.getCurrentRecipe();
			
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
				inventorySlots[0] = new ItemStack(ModItems.BurntFood, recipe.input.size());
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
	
///////////////////////////////////////////////////////////////////
	
	@Override
	protected int getInputSlots() 
	{
		return 4;
	}
	
	@Override
	protected ArrayList<RecipeKawaiiCookingBase> getRecipes(String state) 
	{
		if (state.equals("cool"))
			return dummy.getFilteredRecipes(true);
		if (state.equals("hot"))
			return dummy.getFilteredRecipes(false);
		
		return dummy.getAllRecipes();
	}
	private static RecipeKawaiiGrill dummy = new RecipeKawaiiGrill();

	private TexturedIcon[] display = new TexturedIcon[getInputSlots()];
	@Override
	public TexturedIcon[] getDisplayItems() 
	{
		if (DisplayCache != null) return DisplayCache;
		
		if (inventorySlots[0] != null)
		{
			int i = 0;
			for (; i < inventorySlots[0].stackSize && i < display.length; i++)
				display[i] = inventorySlots[0] == null ? null : new TexturedIcon(inventorySlots[0]);
			for (; i < display.length; i++)
				display[i] = null;
			return (DisplayCache = display);
		}
		for (int i = 0; i < inventorySlots.length && i < display.length; i++)
			display[i] = inventorySlots[i + 1] == null ? null : new TexturedIcon(inventorySlots[i + 1]);
		return (DisplayCache = display);
	}

	@Override
	protected void writeToNBT(NBTTagCompound tags, boolean callSuper) 
	{ 
		// Forced Update, check for valid state
		if (inventorySlots[0] == null && recipeHash != 0)
		{
			RecipeKawaiiGrill recipe = (RecipeKawaiiGrill) getCompleteRecipe();
			if (recipe == null || recipeHash != recipe.hashCode())
			{
				if (state.equals("cooking") || state.equals("burning")) state = "hot";
				if (state.equals("drying") || state.equals("ruined")) state = "cool";
				cookTime = (recipe == null || recipe.dry ? 0 : 1);
				recipeHash = 0;
			}
		}
		
		super.writeToNBT(tags, callSuper);
	}
	
	@Override
	public String getWAILATip() 
	{
		if (state.equals("cool")) return "State: Nice and Cool.";
		if (state.equals("hot")) return "State: Good and Hot.";
		if (state.equals("burning")) return "State: Burning !";
		if (state.equals("ruined")) return "State: Completely Ruined.";

		if (recipeHash != 0)
		{
			RecipeKawaiiGrill recipe = (RecipeKawaiiGrill) getCurrentRecipe();
			
			if (recipe == null) return null;
			if (state.equals("cooking"))
				return "State: " + (cookTime > recipe.cookTime ? "Finished " : "Cooking ") + NamespaceHelper.getItemLocalizedName(recipe.output);
			if (state.equals("drying"))
				return "State: " + (cookTime > recipe.cookTime ? "Finished " : "Drying ") + NamespaceHelper.getItemLocalizedName(recipe.output);
		}
		return null;
	}

}
