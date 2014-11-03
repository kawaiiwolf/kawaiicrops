package com.kawaiiwolf.kawaiicrops.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntityKwaiiCooker extends TileEntity implements IInventory
{

	ItemStack[] inventorySlots = new ItemStack[getSizeInventory()];

	@Override
	public void readFromNBT(NBTTagCompound tags) 
	{
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
	public int getSizeInventory() 
	{
		return getInputSlots() + getOutputSlots();
	}
	
	protected abstract int getInputSlots();
	
	protected abstract int getOutputSlots();

	@Override
	public void writeToNBT(NBTTagCompound tags) 
	{
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public ItemStack getStackInSlot(int slot) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack item) 
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	

	///////////////////////////////////////////////////////
	
	@Override
	public ItemStack getStackInSlotOnClosing(int slot) { return null; }

	@Override
	public String getInventoryName() { return null; }

	@Override
	public boolean hasCustomInventoryName() { return false; }

	@Override
	public int getInventoryStackLimit() { return 64; }

	@Override
	public boolean isUseableByPlayer(EntityPlayer p) { return true; }

	@Override
	public void openInventory() { }

	@Override
	public void closeInventory() { }
	
	/////////////////////////////////////////////////////////
	
	/*
	 * Might possibly need these ? And the read/write don't call super method ? 
	 * 

	@Override
	public Packet getDescriptionPacket() 
	{
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		writeToNBT(nbttagcompound);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, -999, nbttagcompound);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) 
	{
		super.onDataPacket(net, packet);
		readFromNBT(packet.func_148857_g());
	}
	*/
}
