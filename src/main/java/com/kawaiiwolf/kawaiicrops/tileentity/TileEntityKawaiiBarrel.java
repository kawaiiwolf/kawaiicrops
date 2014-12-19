package com.kawaiiwolf.kawaiicrops.tileentity;

import java.util.List;

import com.kawaiiwolf.kawaiicrops.lib.ConfigurationLoader;

import mcp.mobius.waila.api.IWailaBlock;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.SpecialChars;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;

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
}
