package com.kawaiiwolf.kawaiicrops.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.kawaiiwolf.kawaiicrops.lib.Constants;
import com.kawaiiwolf.kawaiicrops.lib.NamespaceHelper;
import com.kawaiiwolf.kawaiicrops.lib.Pair;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemKawaiiMysterySeed extends ItemSeeds {

	public static ArrayList<Pair<Block,Integer>> SeedList = new ArrayList<Pair<Block,Integer>>(); 
	
	public ItemKawaiiMysterySeed(boolean vanilla) 
	{
		super(null, null);

		setUnlocalizedName(Constants.MOD_ID + ".mysteryseed");
		setTextureName(Constants.MOD_ID + ":mysteryseed");
		
		setCreativeTab(ModItems.KawaiiCreativeTab);
		
		if (vanilla)
		{
			SeedList.add(new Pair(Blocks.pumpkin_stem, 1));
			SeedList.add(new Pair(Blocks.melon_stem, 1));
			SeedList.add(new Pair(Blocks.carrots, 2));
			SeedList.add(new Pair(Blocks.potatoes, 2));
			SeedList.add(new Pair(Blocks.wheat, 3));
			SeedList.add(new Pair(Blocks.sapling, 1));
		}
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_) 
	{
		list.add("Who knows what it'll grow into ?");
		list.add("Try planting it in tilled soil.");
	}
	
	private Block getRandomBlock(Random rand)
	{
		int weight = 0;
		for (int i = 0; i < SeedList.size(); i++)
			weight += SeedList.get(i).value;
		
		if (weight == 0) return null;
		
		weight = rand.nextInt(weight);
		int index = 0;
		
		while (weight >= SeedList.get(index).value)
			weight -= SeedList.get(index++).value;
		
		return SeedList.get(index).key;
	}
	
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xhit, float yhit, float zhit)
    {
        if (side != 1 || world.isRemote)
        {
            return false;
        }
        else if (player.canPlayerEdit(x, y, z, side, stack) && player.canPlayerEdit(x, y + 1, z, side, stack))
        {
            if (world.getBlock(x, y, z) == Blocks.farmland && world.isAirBlock(x, y + 1, z))
            {
            	Block r = getRandomBlock(world.rand);
                world.setBlock(x, y + 1, z, r, 0, 3);
                if (r instanceof BlockSapling)
                	world.getBlock(x, y, z).onPlantGrow(world, x, y, z, x, y + 1, z);
                --stack.stackSize;
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

}
