package com.kawaiiwolf.kawaiicrops.world;

import java.util.Random;

import com.kawaiiwolf.kawaiicrops.block.BlockKawaiiTreeBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.util.ForgeDirection;

public class WorldGenKawaiiTree extends WorldGenAbstractTree {

	private BlockKawaiiTreeBlocks tree = null; 
	
	public WorldGenKawaiiTree(BlockKawaiiTreeBlocks tree) {
		super(true);
		
		this.tree = tree;
	}

	@Override
    public boolean generate(World world, Random rand, int x, int y, int z)
    {
		return generateForest(world, rand, x, y, z);
    }
    
    public boolean generateForest(World world, Random rand, int x, int y, int z)
    {
        int l = rand.nextInt(3) + 5;

        if (tree.TreeIsTall)
        {
            l += rand.nextInt(7);
        }

        boolean flag = true;

        if (y >= 1 && y + l + 1 <= 256)
        {
            int j1;
            int k1;

            for (int i1 = y; i1 <= y + 1 + l; ++i1)
            {
                byte b0 = 1;

                if (i1 == y)
                {
                    b0 = 0;
                }

                if (i1 >= y + 1 + l - 2)
                {
                    b0 = 2;
                }

                for (j1 = x - b0; j1 <= x + b0 && flag; ++j1)
                {
                    for (k1 = z - b0; k1 <= z + b0 && flag; ++k1)
                    {
                        if (i1 >= 0 && i1 < 256)
                        {
                            Block block = world.getBlock(j1, i1, k1);

                            if (!this.isReplaceable(world, j1, i1, k1))
                            {
                                flag = false;
                            }
                        }
                        else
                        {
                            flag = false;
                        }
                    }
                }
            }

            if (!flag)
            {
                return false;
            }
            else
            {
                Block block2 = world.getBlock(x, y - 1, z);

                if (tree.SaplingGrowsOn.contains(block2) && y < 256 - l - 1)
                {
                    int k2;
                    for (k2 = y - 3 + l; k2 <= y + l; ++k2)
                    {
                        j1 = k2 - (y + l);
                        k1 = 1 - j1 / 2;

                        for (int l2 = x - k1; l2 <= x + k1; ++l2)
                        {
                            int l1 = l2 - x;

                            for (int i2 = z - k1; i2 <= z + k1; ++i2)
                            {
                                int j2 = i2 - z;

                                if (Math.abs(l1) != k1 || Math.abs(j2) != k1 || rand.nextInt(2) != 0 && j1 != 0)
                                {
                                    Block block1 = world.getBlock(l2, k2, i2);

                                    if (block1.isAir(world, l2, k2, i2) || block1.isLeaves(world, l2, k2, i2))
                                    {
                                    	world.setBlock(l2, k2, i2, tree, 1, 3);
                                        //this.setBlockAndNotifyAdequately(world, l2, k2, i2, Blocks.leaves, 2);
                                    }
                                }
                            }
                        }
                    }

                    for (k2 = 0; k2 < l; ++k2)
                    {
                        Block block3 = world.getBlock(x, y + k2, z);

                        if (block3.isAir(world, x, y + k2, z) || block3.isLeaves(world, x, y + k2, z))
                        {
                        	world.setBlock(x, y + k2, z, tree.LeafTrunkBlock, 0, 3);
                            //this.setBlockAndNotifyAdequately(world, x, y + k2, z, Blocks.log, 2);
                        }
                    }
                    
                    block2.onPlantGrow(world, x, y - 1, z, x, y, z);
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        else
        {
            return false;
        }
    }
	
	

}
