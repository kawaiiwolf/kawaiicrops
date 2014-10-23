package com.kawaiiwolf.kawaiicrops.block;

import java.util.ArrayList;

import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.IShearable;

public class BlockKawaiiLeaf extends BlockLeavesBase implements IShearable {

	protected BlockKawaiiLeaf(Material p_i45433_1_, boolean p_i45433_2_) {
		super(p_i45433_1_, p_i45433_2_);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isShearable(ItemStack item, IBlockAccess world, int x, int y, int z) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<ItemStack> onSheared(ItemStack item, IBlockAccess world, int x, int y, int z, int fortune) {
		// TODO Auto-generated method stub
		return null;
	}

}
