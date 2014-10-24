package com.kawaiiwolf.kawaiicrops.block;

import java.util.ArrayList;
import java.util.Random;

import com.kawaiiwolf.kawaiicrops.lib.Constants;

import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.IGrowable;
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

public class BlockKawaiiTreeBlocks extends BlockBush implements IShearable, IGrowable {

	private String name = "";
	public Boolean Enabled = false;
	
	public BlockKawaiiTreeBlocks(String name) {
		super(Material.leaves);
		
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
	
    /////////////////////////////////////////////////////////////////////////////////////
    // IShearable

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

    /////////////////////////////////////////////////////////////////////////////////////
    // IGrowable
    
	@Override
	public boolean func_149851_a(World world, int x, int y, int z, boolean p_149851_5_) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean func_149852_a(World world, Random rand, int x, int y, int z) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void func_149853_b(World world, Random rand,	int x, int y, int z) {
		// TODO Auto-generated method stub
		
	}
}
