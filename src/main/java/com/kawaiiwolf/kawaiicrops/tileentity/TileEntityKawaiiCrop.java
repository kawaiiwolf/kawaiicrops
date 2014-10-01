package com.kawaiiwolf.kawaiicrops.tileentity;

import com.kawaiiwolf.kawaiicrops.lib.NamespaceHelper;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityKawaiiCrop extends TileEntity {
	 
	private String blockName = "";
	private byte meta = 0;
	private static final byte mask = (byte)0x80;
	 
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);

		this.blockName = nbt.getString("block");
		this.meta = nbt.getByte("meta");
	}
	 
	 @Override
	 public void writeToNBT(NBTTagCompound nbt)
	 {
		 super.writeToNBT(nbt);
		 
		 nbt.setString("block", blockName);
		 nbt.setByte("meta",meta);
	 }
	 
	 public void arm(Block block, int meta)
	 {
		 this.blockName = NamespaceHelper.getBlockName(block);
		 this.meta = (byte) (meta | mask);
	 }
	 
	 public void disarm()
	 {
		 blockName = "";
		 this.meta = 0;
	 }
	 
	 public boolean isArmed()
	 {
		 return (this.meta & mask) == mask;
	 }
	 
	 @Override
	 public void updateEntity()
	 {
		 if ( (this.meta & mask) != mask ) return;
		 this.worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, NamespaceHelper.getBlockByName(blockName));
		 this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, this.meta, 2);
		 disarm();
	 }
}
