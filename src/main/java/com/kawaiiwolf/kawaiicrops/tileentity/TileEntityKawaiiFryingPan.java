package com.kawaiiwolf.kawaiicrops.tileentity;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.kawaiiwolf.kawaiicrops.block.BlockKawaiiFryingPan;
import com.kawaiiwolf.kawaiicrops.block.ModBlocks;
import com.kawaiiwolf.kawaiicrops.item.ModItems;
import com.kawaiiwolf.kawaiicrops.lib.NamespaceHelper;
import com.kawaiiwolf.kawaiicrops.lib.Util;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiCookingBase;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiFryingPan;
import com.kawaiiwolf.kawaiicrops.renderer.TexturedIcon;

public class TileEntityKawaiiFryingPan extends TileEntityKawaiiCookingBlock
{
	public boolean jitter = false;
	public boolean steam = false;
	
	public TileEntityKawaiiFryingPan()
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
				RecipeKawaiiFryingPan recipe = (RecipeKawaiiFryingPan) this.getCurrentRecipe();
				if (recipe != null && recipe.harvest == null && cookTime > recipe.cookTime)
				{
					dropAllItems(world, x, y, z);
					state = (recipe.greasy ? "oiled" : "clean");
					cookTime = 1;
					recipeHash = 0;
				}
			}
		}
		else
		{
			// DEBUG: fast cook
			if (player.getCurrentEquippedItem().getItem() == ModItems.MagicSpoon) { this.onRandomTick(world, x, y, z, world.rand); } 
			
			else if (player.getCurrentEquippedItem().getItem() == ModItems.Steamer && state.equals("clean") && !steam)
			{
				player.getCurrentEquippedItem().stackSize--;
				steam = true;
			}
			
			// We haven't started cooking just yet, but the pan could be heated
			else if (cookTime <= 1)
			{
				int slot = getFirstOpenSlot();
				
				// Check to grease up the pan
				if(state.equals("clean") && Util.arrayContains(RecipeKawaiiFryingPan.CookingOilItems, player.getCurrentEquippedItem()))
				{
					takeCurrentItemContainer(world, x, y, z, player);
					state = "oiled";
					
					particleBlast(world, x, y, z, "mobSpell", 8, 12, 1, 1, .6d);
				}
				
				// Check for valid ingredient
				else if (slot != -1 && isItemValidForSlot(slot, player.getCurrentEquippedItem()))
				{
					inventorySlots[slot] = takeCurrentItemContainer(world, x, y, z, player);
				}
				
				// If the pan is heated, start checking for instant cook recipes
				if (cookTime == 1 && recipeHash == 0)
				{
					RecipeKawaiiFryingPan recipe = (RecipeKawaiiFryingPan) getCompleteRecipe();
					if (recipe != null && recipe.cookTime == 0)
					{
						if(!recipe.steam && steam)
							dropSteamer();
						
						recipeHash = recipe.hashCode();
						state = "cooking";
						for (int i = 0; i < inventorySlots.length; i++)
							inventorySlots[i] = null;
						inventorySlots[0] = recipe.output.copy();
					}
				}
			}
			// Else we're cooking. Harvest ?
			else
			{
				RecipeKawaiiFryingPan recipe = (RecipeKawaiiFryingPan) this.getCurrentRecipe();
				if (cookTime > recipe.cookTime && inventorySlots[0] != null && ( recipe.harvest == null || (recipe.harvest.getItem() == player.getCurrentEquippedItem().getItem() && recipe.harvest.getItemDamage() == player.getCurrentEquippedItem().getItemDamage())))
				{
					if (recipe.harvest != null)
						player.getCurrentEquippedItem().stackSize--;
					this.dropBlockAsItem(world, x, y, z, new ItemStack(inventorySlots[0].getItem(),1));
					if (inventorySlots[0].stackSize > 1)
						inventorySlots[0].stackSize--;
					else
					{
						inventorySlots[0] = null;
						state = (recipe.greasy ? "oiled" : "clean");
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
			if (cookTime == 0)
				cookTime++;
			
			// Pan hot & no set recipe, try to start cooking
			else if (cookTime == 1 && recipeHash == 0)
			{
				RecipeKawaiiFryingPan recipe = (RecipeKawaiiFryingPan) getCompleteRecipe();
				if (recipe != null)
				{
					if(!recipe.steam && steam)
						dropSteamer();
					
					recipeHash = recipe.hashCode();
					state = "cooking";
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
				
				if (recipe.steam && !steam) return;
				
				cookTime++;

				// Burned
				if (recipe.burnTime > 0 && cookTime > recipe.cookTime + recipe.burnTime)
				{
					inventorySlots[0] = new ItemStack(recipe.steam ? ModItems.RuinedFood : ModItems.BurntFood);
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
			jitter = state.equals("cooking") || state.equals("burning");
			if (rand.nextFloat() > 0.66f)
			{
				if (state.equals("cooking"))
					this.particleBlast(world, x, y, z, "explode", 1, 1);
				if (state.equals("burning"))
					this.particleBlast(world, x, y, z, "smoke", 1, 1);
			}
			if (state.equals("ruined"))
				this.particleBlast(world, x, y, z, "largesmoke", 1, 2);	
		}
		else
			jitter = false;
	}
	
	@Override
	public void dropAllItems(World world, int x, int y, int z)
	{
		RecipeKawaiiFryingPan recipe = (RecipeKawaiiFryingPan) this.getCurrentRecipe();
		if (recipe == null || recipe.harvest == null)
			super.dropAllItems(world, x, y, z);
		dropSteamer();
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

	private TexturedIcon[] display = new TexturedIcon[getInputSlots()];
	private TexturedIcon[] fullIcon = new TexturedIcon[1];
	
	@Override
	public TexturedIcon[] getDisplayItems() 
	{
		if (DisplayCache != null) return DisplayCache;
		
		if (state.equals("ruined"))
		{
			fullIcon[0] = new TexturedIcon(ModBlocks.fryingPan.burntTexture, TextureMap.locationBlocksTexture);
			return (DisplayCache = fullIcon);
		}
		if (recipeHash != 0)
		{
			RecipeKawaiiFryingPan recipe = (RecipeKawaiiFryingPan) getCurrentRecipe();
			if (recipe != null && recipe.texture && cookTime > recipe.cookTime)
			{
				fullIcon[0] = new TexturedIcon(BlockKawaiiFryingPan.FoodTextures.get(recipe), TextureMap.locationBlocksTexture);
				return (DisplayCache = fullIcon);
			}
			if (recipe != null && cookTime > recipe.cookTime && inventorySlots[0] != null)
			{
				int i = 0;
				for (; i < inventorySlots[0].stackSize && i < display.length; i++)
					display[i] = inventorySlots[0] == null ? null : new TexturedIcon(inventorySlots[0]);
				for (; i < display.length; i++)
					display[i] = null;
				return (DisplayCache = display);
			}
		}
		for (int i = 0; i < inventorySlots.length && i < display.length; i++)
			display[i] = inventorySlots[i + 1] == null ? null : new TexturedIcon(inventorySlots[i + 1]);
		return (DisplayCache = display);
	}

	protected void dropSteamer()
	{
		if (!steam) return;
		steam = false;
		dropBlockAsItem(getWorldObj(), xCoord, yCoord, zCoord, new ItemStack(ModItems.Steamer));
	}
	
	@Override
	public void clearAllItems()
	{
		super.clearAllItems();
		dropSteamer();
	}
	
	@Override
	protected void readFromNBT(NBTTagCompound tags, boolean callSuper) 
	{ 
		super.readFromNBT(tags, callSuper);
		steam = tags.getBoolean("steam");
	}

	@Override
	protected void writeToNBT(NBTTagCompound tags, boolean callSuper) 
	{ 
		// Forced Update, check for valid state
		if (inventorySlots[0] == null && recipeHash != 0)
		{
			RecipeKawaiiFryingPan recipe = (RecipeKawaiiFryingPan) getCompleteRecipe();
			if (recipe == null || recipeHash != recipe.hashCode())
			{
				state = (recipe != null && recipe.greasy ? "oiled" : "clean");
				cookTime = 1;
				recipeHash = 0;
			}
		}
		
		super.writeToNBT(tags, callSuper);
		tags.setBoolean("steam", steam);
	}
	
	@Override
	public String getWAILATip() 
	{
		/*
		String s = "State: " + state + ", Time: " + cookTime + "\nCooking: ";
		if (state.equals("cooking"))
		{
			RecipeKawaiiFryingPan recipe = (RecipeKawaiiFryingPan) getCurrentRecipe();
			s += NamespaceHelper.getItemLocalizedName(recipe.output);
		}
		else s += "No Recipe";
		s += "\n[0] " + (inventorySlots[0] == null ? "" : NamespaceHelper.getItemLocalizedName(inventorySlots[0]));
		s += "\n[1] " + (inventorySlots[1] == null ? "" : NamespaceHelper.getItemLocalizedName(inventorySlots[1]));
		s += "\n[2] " + (inventorySlots[2] == null ? "" : NamespaceHelper.getItemLocalizedName(inventorySlots[2]));
		s += "\n[3] " + (inventorySlots[3] == null ? "" : NamespaceHelper.getItemLocalizedName(inventorySlots[3]));
		
		if (s.length() > 0)
			return s;
		*/
		
		if (state.equals("clean"))
		{
			if (steam)
				return "State: Ready to Steam";
			else
				return cookTime > 0 ? "State: Hot Pan" : "State: Squeaky Clean";
		}
		if (state.equals("burning")) return "State: Burning !";
		if (state.equals("ruined")) return "State: Completely Ruined";
		if (state.equals("oiled")) return "State: " + (cookTime > 0 ? "Hot " : "") + "Oiled Pan";
		if (state.equals("cooking"))
		{
			RecipeKawaiiFryingPan recipe = (RecipeKawaiiFryingPan) getCurrentRecipe();
			if (recipe == null) 
				return null;
			if (recipe.steam & !steam)
				return "State: Missing Steamer";
			return "State: " + (cookTime > recipe.cookTime ? "Finished " : (steam ? "Steaming " : "Cooking ")) + NamespaceHelper.getItemLocalizedName(recipe.output);
		}
		return null;
	}
}
