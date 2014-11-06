package com.kawaiiwolf.kawaiicrops.tileentity;

import java.util.ArrayList;

import com.kawaiiwolf.kawaiicrops.lib.NamespaceHelper;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiCookingBase;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiCuttingBoard;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityKawaiiCuttingBoard extends TileEntityKawaiiCookingBlock 
{
	public ItemStack getDisplayItem()
	{
		ItemStack i = getStackInSlot(1);
		if (i != null)
			return i;
		return getStackInSlot(0);
	}
	
	@Override
	protected int getInputSlots() { return 1; }

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player) 
	{
		if (player.isSneaking())
			dropAllItems(world, x, y, z);
		else if (isItemValidForSlot(1, player.getCurrentEquippedItem()))
		{
			this.setInventorySlotContents(1, new ItemStack(player.getCurrentEquippedItem().getItem(), 1));
			player.getCurrentEquippedItem().stackSize--;
		} 
		else
		{
			ItemStack item = hasCompleteRecipe();
			if (tryCraft())
				world.playSoundAtEntity(player, Block.soundTypeWood.getBreakSound(), 1.0f, 1.0f);
			else
				dropAllItems(world, x, y, z);
		}
		world.markBlockForUpdate(x, y, z);
		return true;
	}
	
	@Override
	protected ArrayList<RecipeKawaiiCookingBase> getRecipies()
	{
		return dummy.getAllRecipies();
	}
	private static RecipeKawaiiCuttingBoard dummy = new RecipeKawaiiCuttingBoard();
	

}
