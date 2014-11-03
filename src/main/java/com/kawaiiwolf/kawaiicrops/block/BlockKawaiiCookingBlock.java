package com.kawaiiwolf.kawaiicrops.block;

import com.kawaiiwolf.kawaiicrops.lib.Constants;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;

public abstract class BlockKawaiiCookingBlock extends BlockContainer {

	public String Name = "";
	
	protected BlockKawaiiCookingBlock(Material material, String name) 
	{
		super(material);
		this.Name = name;
		setBlockName(Constants.MOD_ID + "." + name);
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

	private boolean isRegistered = false;
	public void register()
	{
		if (isRegistered) return;
		isRegistered = true;
		
		GameRegistry.registerBlock(this, Constants.MOD_ID + "." + Name);
	}

}
