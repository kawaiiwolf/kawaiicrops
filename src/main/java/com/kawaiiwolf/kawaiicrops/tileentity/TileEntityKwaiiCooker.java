package com.kawaiiwolf.kawaiicrops.tileentity;

import java.util.ArrayList;

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

public abstract class TileEntityKwaiiCooker extends TileEntity implements IInventory
{

	ItemStack[] inventorySlots = new ItemStack[getSizeInventory()];

	@Override
	public int getSizeInventory() 
	{
		return getInputSlots() + getOutputSlots();
	}
	
	protected abstract int getInputSlots();
	
	protected abstract int getOutputSlots();
	
	@Override
	public void readFromNBT(NBTTagCompound tags) { readFromNBT(tags, true); }
	protected void readFromNBT(NBTTagCompound tags, boolean callSuper)
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
	protected void writeToNBT(NBTTagCompound tags, boolean callSuper)
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
		inventorySlots[slot] = item;
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
		if (slot < 0 || slot >= inventorySlots.length || inventorySlots[slot] == null || inventorySlots[slot].stackSize < amount)
			return null;
		ItemStack ret = new ItemStack(inventorySlots[slot].getItem(), amount);
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
		if (slot < 0 || slot >= getInputSlots() || inventorySlots[slot] != null || !itemAllowedByRecipie(item, inventorySlots))
			return false;
		return true;
	}
	
	protected abstract ArrayList<RecipeKawaiiCookingBase> getRecipies();
	
	protected boolean itemAllowedByRecipie(ItemStack item, ItemStack[] current)
	{
		// Check to see if we have too many ingredients !
		for(int i = 0, count = 0; i < getInputSlots() ; i++)
			if (current[i] != null)
				if (count++ >= getInputSlots())
					return false;
		
		ArrayList<ItemStack> ingredients = new ArrayList<ItemStack>();
		for (ItemStack ingredient : current)
			ingredients.add(ingredient);
		ingredients.add(item);
		
		for (RecipeKawaiiCookingBase recipe : getRecipies())
			if (recipe.matches(ingredients) >= 0)
				return true;
		
		return false;
	}
	
	protected ItemStack hasCompleteRecipe(ItemStack[] current)
	{
		ArrayList<ItemStack> ingredients = new ArrayList<ItemStack>();
		for (ItemStack ingredient : current)
			ingredients.add(ingredient);
		
		for (RecipeKawaiiCookingBase recipe : getRecipies())
			if (recipe.matches(ingredients) >= 0)
				return new ItemStack(recipe.output.getItem(), recipe.output.stackSize);
		
		return null;
	}
	
	public abstract boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player);
	
	protected int getFirstOpenSlot()
	{
		for (int i = 0; i < getInputSlots(); i++)
			if(inventorySlots[i] == null)
				return i;
		return -1;
	}
	
    protected void dropBlockAsItem(World world, int x, int y, int z, ItemStack item)
    {
    	Block b = world.getBlock(x, y, z);
    	if (b instanceof BlockKawaiiCookingBlock)
    		((BlockKawaiiCookingBlock)b).dropBlockAsItem(world, x, y, z, item);
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
