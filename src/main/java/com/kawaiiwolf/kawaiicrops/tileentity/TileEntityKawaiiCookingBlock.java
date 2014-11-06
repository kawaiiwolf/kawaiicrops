package com.kawaiiwolf.kawaiicrops.tileentity;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import com.kawaiiwolf.kawaiicrops.block.BlockKawaiiCookingBlock;
import com.kawaiiwolf.kawaiicrops.lib.NamespaceHelper;
import com.kawaiiwolf.kawaiicrops.recipe.RecipeKawaiiCookingBase;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class TileEntityKawaiiCookingBlock extends TileEntity implements IInventory
{

	private ItemStack[] inventorySlots = new ItemStack[getSizeInventory()];

	@Override
	public int getSizeInventory() 
	{
		return getInputSlots() + 1;
	}
	
	protected abstract int getInputSlots();
	
	@Override
	public void readFromNBT(NBTTagCompound tags) { readFromNBT(tags, true); }
	private void readFromNBT(NBTTagCompound tags, boolean callSuper)
	{
		if (callSuper)
			super.readFromNBT(tags);
		NBTTagList items = tags.getTagList("Items", 10);
		inventorySlots = new ItemStack[getSizeInventory()];
		for (int i = 0; i < items.tagCount(); ++i) 
		{
			NBTTagCompound compound = items.getCompoundTagAt(i);
			byte slot = compound.getByte("Slot");
			if (slot >= 0 && slot < inventorySlots.length)
				inventorySlots[slot] = ItemStack.loadItemStackFromNBT(compound);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tags) { writeToNBT(tags, true); }
	private void writeToNBT(NBTTagCompound tags, boolean callSuper)
	{
		if (callSuper)
			super.writeToNBT(tags);
		NBTTagList items = new NBTTagList();
		for (int i = 0; i < inventorySlots.length; ++i) 
		{
			if (inventorySlots[i] != null) 
			{
				NBTTagCompound compound = new NBTTagCompound();
				compound.setByte("Slot", (byte) i);
				inventorySlots[i].writeToNBT(compound);
				items.appendTag(compound);
			}
		}
		tags.setTag("Items", items);
	}
	
	@Override
	public void setInventorySlotContents(int slot, ItemStack item) 
	{
		if (slot < 0 || slot >= inventorySlots.length || !isItemValidForSlot(slot, item)) return; 
		inventorySlots[slot] = item.copy();
	}

	@Override
	public ItemStack getStackInSlot(int slot) 
	{
		if (slot < 0 || slot >= inventorySlots.length)
			return null;
		return inventorySlots[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if (slot < 0 || slot >= inventorySlots.length || inventorySlots[0] != null || inventorySlots[slot] == null || inventorySlots[slot].stackSize < amount)
			return null;
		ItemStack ret = new ItemStack(inventorySlots[slot].getItem(), amount, inventorySlots[slot].getItemDamage());
		if (inventorySlots[slot].stackSize == amount)
			inventorySlots[slot] = null;
		else
			inventorySlots[slot].stackSize -= amount;
		return ret;
	}
	
	public ItemStack takeStack(int slot)
	{
		if (slot < 0 || slot >= inventorySlots.length || inventorySlots[slot] == null)
			return null;
		ItemStack ret = inventorySlots[slot];
		inventorySlots[slot] = null;
		return ret;
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack item) 
	{
		if (slot <= 0 || slot >= inventorySlots.length || item == null || inventorySlots[slot] != null || !itemAllowedByRecipie(item))
			return false;
		return true;
	}
	
	protected abstract ArrayList<RecipeKawaiiCookingBase> getRecipies();
	
	public boolean itemAllowedByRecipie(ItemStack item)
	{
		if (inventorySlots[0] != null || getOpenSlots() < 1) return false;

		inventorySlots[0] = item.copy();
		List<ItemStack> ingredients = Arrays.asList(inventorySlots);

		boolean found = false;
		for (RecipeKawaiiCookingBase recipe : getRecipies())
			if (!found && recipe.matches(ingredients) >= 0)
			{
				found = true;
				break;
			}

		inventorySlots[0] = null;
		return found;
	}
	
	public void clearAllItems()
	{
		for (int i = 0; i < inventorySlots.length; i++)
			inventorySlots[i] = null;
	}	
	
	public boolean tryCraft()
	{
		ItemStack result = hasCompleteRecipe();
		if (result == null) 
			return false;
		
		clearAllItems();
		inventorySlots[0] = result;
		
		return true;
	}
	
	public ItemStack hasCompleteRecipe()
	{
		if (inventorySlots[0] != null) return null;
		List<ItemStack> ingredients = Arrays.asList(inventorySlots);
		for (RecipeKawaiiCookingBase recipe : getRecipies())
			if (recipe.matches(ingredients) == 0)
				return new ItemStack(recipe.output.getItem(), recipe.output.stackSize);
		return null;
	}
	
	public abstract boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player);
	
	public int getFirstOpenSlot()
	{
		for (int i = 1; i < inventorySlots.length; i++)
			if(inventorySlots[i] == null)
				return i;
		return -1;
	}
	
	public int getOpenSlots()
	{
		int count = 0;
		for (int i = 1; i < inventorySlots.length; i++)
			if(inventorySlots[i] == null)
				count++;
		return count;
	}

	public int getOccupiedSlots()
	{
		return getInputSlots() - getOpenSlots();
	}
	
    protected void dropBlockAsItem(World world, int x, int y, int z, ItemStack item)
    {
    	Block b = world.getBlock(x, y, z);
    	if (b instanceof BlockKawaiiCookingBlock)
    		((BlockKawaiiCookingBlock)b).dropBlockAsItem(world, x, y, z, item);
    }
    
    public void dropAllItems(World world, int x, int y, int z)
    {
    	for(int i = 0; i < inventorySlots.length; i++)
    		if (inventorySlots[i] != null)
    			dropBlockAsItem(world, x, y, z, inventorySlots[i]);
    	clearAllItems();
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    // Multi-Player Sync Code - Just make sure to call: world.markBlockForUpdate(x, y, z);
    
	@Override
	public Packet getDescriptionPacket() 
	{
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		writeToNBT(nbttagcompound, false);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, -999, nbttagcompound);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) 
	{
		super.onDataPacket(net, packet);
		readFromNBT(packet.func_148857_g(), false);
	}

    /////////////////////////////////////////////////////////////////////////////////////////
    // Other Tile Entity Stuff
	
	@Override
	public ItemStack getStackInSlotOnClosing(int slot) { return null; }

	@Override
	public String getInventoryName() { return null; }

	@Override
	public boolean hasCustomInventoryName() { return false; }

	@Override
	public int getInventoryStackLimit() { return 1; }

	@Override
	public boolean isUseableByPlayer(EntityPlayer p) { return true; }

	@Override
	public void openInventory() { }

	@Override
	public void closeInventory() { }
	
}
