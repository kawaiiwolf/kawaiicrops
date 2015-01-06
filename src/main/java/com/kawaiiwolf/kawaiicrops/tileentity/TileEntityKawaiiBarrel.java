package com.kawaiiwolf.kawaiicrops.tileentity;

import com.kawaiiwolf.kawaiicrops.block.BlockKawaiiBarrel;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;

public class TileEntityKawaiiBarrel extends TileEntity
{
	public boolean isRuined = false;
	public int cookTime = 0;
	public int label = 0;

	@Override
	public void readFromNBT(NBTTagCompound tags) { readFromNBT(tags, true); }
	protected void readFromNBT(NBTTagCompound tags, boolean callSuper)
	{
		if (callSuper)
			super.readFromNBT(tags);
		cookTime = tags.getInteger("CookTicks");
		label = tags.getInteger("Label");
		isRuined = tags.getBoolean("Ruined");
	}

	@Override
	public void writeToNBT(NBTTagCompound tags) { writeToNBT(tags, true); }
	protected void writeToNBT(NBTTagCompound tags, boolean callSuper)
	{
		if (callSuper)
			super.writeToNBT(tags);
		tags.setInteger("CookTicks", cookTime);
		tags.setInteger("Label", label);
		tags.setBoolean("Ruined", isRuined);
	}
	
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
	
	public IIcon getDisplay()
	{
		if (worldObj.getBlock(xCoord, yCoord, zCoord) instanceof BlockKawaiiBarrel)
		{
			BlockKawaiiBarrel b = (BlockKawaiiBarrel)worldObj.getBlock(xCoord, yCoord, zCoord);
			
			if (this.isRuined)
				return b.labelRuined;
			if (this.cookTime < b.FinishedTime)
				return b.labelUnfinished;
			return b.labelFinished;
		}
		
		return null;
	}
	
	public BlockKawaiiBarrel.BarrelModel getModel()
	{
		if (worldObj.getBlock(xCoord, yCoord, zCoord) instanceof BlockKawaiiBarrel)
			return ((BlockKawaiiBarrel)worldObj.getBlock(xCoord, yCoord, zCoord)).model;
		return null;
	}
}
