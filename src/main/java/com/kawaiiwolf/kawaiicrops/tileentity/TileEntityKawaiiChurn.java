package com.kawaiiwolf.kawaiicrops.tileentity;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.kawaiiwolf.kawaiicrops.lib.NamespaceHelper;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiChurn;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiCookingBase;
import com.kawaiiwolf.kawaiicrops.renderer.TexturedIcon;

public class TileEntityKawaiiChurn extends TileEntityKawaiiCookingBlock 
{
	public boolean isChurn = true;
	
	public TileEntityKawaiiChurn(boolean isChurn)
	{
		super();
		this.isChurn = isChurn;
	}
	
	@Override
	protected int getInputSlots() { return 1; }

	@Override
	public TexturedIcon[] getDisplayItems() { return null; }

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player) 
	{
		if (player.isSneaking())
			dropAllItems(world, x, y, z);
		else if (getChunMovementTick() > 0) 
			return true; // In the middle of an animation. Move along !
		else if (isItemValidForSlot(1, player.getCurrentEquippedItem()))
		{
			setInventorySlotContents(1, takeCurrentItemContainer(world, x, y, z, player));
			cookTime = 0;
		} 
		else
		{
			RecipeKawaiiChurn recipe = (RecipeKawaiiChurn) getCompleteRecipe();
			
			if (recipe != null && (cookTime >> 6) < recipe.ChurnTime)
				cookTime += (1 << 6) + 20;
			else if(tryCraft())
				dropAllItems(world, x, y, z);
		}
		world.markBlockForUpdate(x, y, z);
		return true;
	}

	@Override
	public void updateEntity()
	{
		if ((cookTime & 31) > 0) cookTime--;
	}
	
	public int getChunMovementTick()
	{
		return (cookTime & 31);
	}
	
	@Override
	protected ArrayList<RecipeKawaiiCookingBase> getRecipes(String filter) 
	{
		if (isChurn)
			return dummy.churnRecipes;
		else
			return dummy.millRecipes;
	}
	private static RecipeKawaiiChurn dummy = new RecipeKawaiiChurn();

	@Override
	protected void readFromNBT(NBTTagCompound tags, boolean callSuper)
	{
		super.readFromNBT(tags, callSuper);
		isChurn = tags.getBoolean("isChurn");
	}
	
	@Override
	protected void writeToNBT(NBTTagCompound tags, boolean callSuper)
	{
		super.writeToNBT(tags, callSuper);
		tags.setBoolean("isChurn", isChurn);
	}

	@Override
	public String getWAILATip() 
	{ 
		return (inventorySlots[1] == null ? "Empty" : (isChurn ? "Churning: " : "Milling: ") + NamespaceHelper.getItemLocalizedName(inventorySlots[1])); 
	}
}
