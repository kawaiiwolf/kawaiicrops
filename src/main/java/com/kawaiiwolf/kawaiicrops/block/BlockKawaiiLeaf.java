package com.kawaiiwolf.kawaiicrops.block;

import java.util.ArrayList;
import java.util.Random;

import com.kawaiiwolf.kawaiicrops.lib.Constants;

import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockKawaiiLeaf extends BlockLeavesBase implements IShearable {

	private String name = "";
	
	public BlockKawaiiLeaf(String name) {
		super(Material.leaves, false);
		
        this.setTickRandomly(true);
        this.setHardness(0.2F);
        this.setLightOpacity(1);
        this.setStepSound(soundTypeGrass);
        
        this.name = name;
        this.setBlockName(Constants.MOD_ID + "." + name);
	}

	@SideOnly(Side.CLIENT)
	private IIcon iicon;
	
	@Override
    @SideOnly(Side.CLIENT)
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) 
	{
		return iicon;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg)
    {
		iicon = reg.registerIcon(Constants.MOD_ID + ":" + name);
	}

	@Override
	public boolean isShearable(ItemStack item, IBlockAccess world, int x, int y, int z) 
	{
		return false;
	}

	@Override
	public ArrayList<ItemStack> onSheared(ItemStack item, IBlockAccess world, int x, int y, int z, int fortune) 
	{
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		return ret;
	}
	
    @Override
    public void updateTick(World world, int x, int y, int z, Random rand)
    {
    	if(world.isRemote) return;
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
    	// INSIDE UNTOUCHED FROM BLOCK grandparent
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

        int count = quantityDropped(metadata, fortune, world.rand);
        for(int i = 0; i < count; i++)
        {
            Item item = getItemDropped(metadata, world.rand, fortune);
            if (item != null)
            {
                ret.add(new ItemStack(item, 1, damageDropped(metadata)));
            }
        }
        return ret;
    }
}
