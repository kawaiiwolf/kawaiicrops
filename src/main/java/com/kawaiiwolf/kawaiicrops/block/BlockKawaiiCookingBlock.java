package com.kawaiiwolf.kawaiicrops.block;

import com.kawaiiwolf.kawaiicrops.lib.Constants;
import com.kawaiiwolf.kawaiicrops.tileentity.TileEntityKwaiiCooker;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public abstract class BlockKawaiiCookingBlock extends BlockContainer {

	public String Name = "";
	
	protected BlockKawaiiCookingBlock(Material material, String name) 
	{
		super(material);
		this.Name = name;
		setBlockName(Constants.MOD_ID + "." + name);
	}
	
	@Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitx, float htiy, float hitz)
    {
		TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof TileEntityKwaiiCooker)
			return ((TileEntityKwaiiCooker)te).onBlockActivated(world, x, y, z, player);
        return false;
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
