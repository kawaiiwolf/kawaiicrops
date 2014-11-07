package com.kawaiiwolf.kawaiicrops.block;

import com.kawaiiwolf.kawaiicrops.lib.Constants;
import com.kawaiiwolf.kawaiicrops.tileentity.TileEntityKawaiiCookingBlock;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public abstract class BlockKawaiiCookingBlock extends BlockContainer {

	public String Name = "";
	
	protected BlockKawaiiCookingBlock(Material material, String name) 
	{
		super(material);
		this.Name = name;
		setBlockName(Constants.MOD_ID + "." + name);
		setHardness(1.0f);
	}
	
	@Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitx, float htiy, float hitz)
    {
		TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof TileEntityKawaiiCookingBlock)
			return ((TileEntityKawaiiCookingBlock)te).onBlockActivated(world, x, y, z, player);
        return false;
    }
	
    @Override
    public void onBlockPreDestroy(World world, int x, int y, int z, int meta) 
    {
    	super.onBlockPreDestroy(world, x, y, z, meta);
    	TileEntity te = world.getTileEntity(x, y, z);
    	if (te instanceof TileEntityKawaiiCookingBlock)
    		((TileEntityKawaiiCookingBlock)te).dropAllItems(world, x, y, z);
    }

    @Override
    public boolean canHarvestBlock(EntityPlayer player, int meta)
    {
    	return true;
    }
	
	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) { }
	

	@Override
	public IIcon getIcon(int par1, int par2) 
	{
		return Blocks.planks.getIcon(par1, par2);
	}
	
	@Override
	public boolean isOpaqueCube() 
	{
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() 
	{
		return false;
	}
	
	@Override
	public int getRenderType() 
	{
		return -1;
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack item) 
	{
		Vec3 look = entity.getLookVec();
		int meta = 0;
		if (Math.abs(look.xCoord) >= Math.abs(look.zCoord))
			meta = (look.xCoord > 0.0d ? 1 : 3);
		else
			meta = (look.zCoord > 0.0d ? 0 : 2);
		world.setBlockMetadataWithNotify(x, y, z, meta, 2);
	}
	
	@Override
	public void dropBlockAsItem(World world, int x, int y, int z, ItemStack item)
	{
		super.dropBlockAsItem(world, x, y, z, item);
	}

	private boolean isRegistered = false;
	public void register()
	{
		if (isRegistered) return;
		isRegistered = true;
		
		GameRegistry.registerBlock(this, Constants.MOD_ID + "." + Name);
	}

}
