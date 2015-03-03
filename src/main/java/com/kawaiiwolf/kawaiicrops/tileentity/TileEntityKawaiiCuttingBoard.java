package com.kawaiiwolf.kawaiicrops.tileentity;

import java.util.ArrayList;

import com.kawaiiwolf.kawaiicrops.lib.NamespaceHelper;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiCookingBase;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiCuttingBoard;
import com.kawaiiwolf.kawaiicrops.renderer.TexturedIcon;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class TileEntityKawaiiCuttingBoard extends TileEntityKawaiiCookingBlock 
{
	public TexturedIcon[] display = new TexturedIcon[1];
			
	@Override
	public TexturedIcon[] getDisplayItems()
	{
		if (DisplayCache != null) return DisplayCache;
		
		if (inventorySlots[1] != null)
			display[0] = new TexturedIcon(inventorySlots[1]);
		else if (inventorySlots[0] != null)
			display[0] = new TexturedIcon(inventorySlots[0]);
		else
			display[0] = null;
		return (DisplayCache = display);
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
			setInventorySlotContents(1, takeCurrentItemContainer(world, x, y, z, player));
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
	protected ArrayList<RecipeKawaiiCookingBase> getRecipes(String filter)
	{
		return dummy.getAllRecipes();
	}
	private static RecipeKawaiiCuttingBoard dummy = new RecipeKawaiiCuttingBoard();

	@Override
	public String getWAILATip() 
	{
		return null;
	}
	

}
