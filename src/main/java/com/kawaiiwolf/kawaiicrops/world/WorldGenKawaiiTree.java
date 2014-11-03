package com.kawaiiwolf.kawaiicrops.world;

import java.util.HashSet;
import java.util.Random;

import com.kawaiiwolf.kawaiicrops.block.BlockKawaiiTreeBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.util.ForgeDirection;

public class WorldGenKawaiiTree extends WorldGenAbstractTree implements IWorldGenWithSoilBlocks {

	private BlockKawaiiTreeBlocks tree = null;
	
	public WorldGenKawaiiTree(BlockKawaiiTreeBlocks tree) 
	{
		super(true);
		
		this.tree = tree;
	}
	
	public enum TreeShape { FOREST, TAIGA, SAVANAH, CANOPY, EUCALYPTUS, SAKURA };

	@Override
    public boolean generate(World world, Random rand, int x, int y, int z)
    {
		switch (tree.TreeShape)
		{
			case CANOPY:
				return generateCanopy(world, rand, x, y, z);
			case FOREST:
				return generateForest(world, rand, x, y, z);
			case SAVANAH:
				return generateSavanah(world, rand, x, y, z);
			case TAIGA:
				return generateTaiga(world, rand, x, y, z);
			case EUCALYPTUS:
				return generateEucalyptus(world, rand, x, y, z);
			case SAKURA:
				return generateSakura(world, rand, x, y, z);
		}
		return false;		/**/
    }
	
	@Override
	public HashSet<Block> getSoil() {
		return tree.SaplingSoilBlocks;
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////
	// The below is shamelessly adapted from Vanilla code, with the following adaptations:
	//
	//		boolean isSoil = tree.SaplingGrowsOn.contains(block);
	//		world.setBlock(x, y, z, tree, 1, 3);
	//		world.setBlock(x, y, z, tree.TreeTrunkBlock, 0, 3);
	//////////////////////////////////////////////////////////////////////////////////////////////////

	public boolean generateForest(World world, Random rand, int x, int y, int z)
    {
        int l = rand.nextInt(5) + 5;

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

                if (tree.SaplingSoilBlocks.contains(block2) && y < 256 - l - 1)
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
                        	world.setBlock(x, y + k2, z, tree.TreeTrunkBlock, 0, 3);
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
	
	//////////////////////////////////////////////////////////////////////////////////////////////////
	
    public boolean generateTaiga(World world, Random rand, int x, int y, int z)
    {
        int l = rand.nextInt(4) + 6;
        int i1 = 1 + rand.nextInt(2);
        int j1 = l - i1;
        int k1 = 2 + rand.nextInt(2);
        boolean flag = true;

        if (y >= 1 && y + l + 1 <= 256)
        {
            int i2;
            int l3;

            for (int l1 = y; l1 <= y + 1 + l && flag; ++l1)
            {
                boolean flag1 = true;

                if (l1 - y < i1)
                {
                    l3 = 0;
                }
                else
                {
                    l3 = k1;
                }

                for (i2 = x - l3; i2 <= x + l3 && flag; ++i2)
                {
                    for (int j2 = z - l3; j2 <= z + l3 && flag; ++j2)
                    {
                        if (l1 >= 0 && l1 < 256)
                        {
                            Block block = world.getBlock(i2, l1, j2);

                            if (!block.isAir(world, i2, l1, j2) && !block.isLeaves(world, i2, l1, j2))
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
                Block block1 = world.getBlock(x, y - 1, z);

                boolean isSoil = tree.SaplingSoilBlocks.contains(block1);
                if (isSoil && y < 256 - l - 1)
                {
                    block1.onPlantGrow(world, x, y - 1, z, x, y, z);
                    l3 = rand.nextInt(2);
                    i2 = 1;
                    byte b0 = 0;
                    int k2;
                    int i4;

                    for (i4 = 0; i4 <= j1; ++i4)
                    {
                        k2 = y + l - i4;

                        for (int l2 = x - l3; l2 <= x + l3; ++l2)
                        {
                            int i3 = l2 - x;

                            for (int j3 = z - l3; j3 <= z + l3; ++j3)
                            {
                                int k3 = j3 - z;

                                if ((Math.abs(i3) != l3 || Math.abs(k3) != l3 || l3 <= 0) && world.getBlock(l2, k2, j3).canBeReplacedByLeaves(world, l2, k2, j3))
                                {
                                    //this.setBlockAndNotifyAdequately(world, l2, k2, j3, Blocks.leaves, 1);
                                    world.setBlock(l2, k2, j3, tree, 1, 3);
                                }
                            }
                        }

                        if (l3 >= i2)
                        {
                            l3 = b0;
                            b0 = 1;
                            ++i2;

                            if (i2 > k1)
                            {
                                i2 = k1;
                            }
                        }
                        else
                        {
                            ++l3;
                        }
                    }

                    i4 = rand.nextInt(3);

                    for (k2 = 0; k2 < l - i4; ++k2)
                    {
                        Block block2 = world.getBlock(x, y + k2, z);

                        if (block2.isAir(world, x, y + k2, z) || block2.isLeaves(world, x, y + k2, z))
                        {
                            //this.setBlockAndNotifyAdequately(world, x, y + k2, z, Blocks.log, 1);
                        	world.setBlock(x,  y + k2, z, tree.TreeTrunkBlock, 0, 3);
                        }
                    }

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

	//////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean generateSavanah(World world, Random rand, int x, int y, int z)
    {
        int l = rand.nextInt(3) + rand.nextInt(3) + 5;
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
                Block block3 = world.getBlock(x, y - 1, z);

                boolean isSoil = tree.SaplingSoilBlocks.contains(block3);
                if (isSoil && y < 256 - l - 1)
                {
                    block3.onPlantGrow(world, x, y - 1, z, x, y, z);
                    int j3 = rand.nextInt(4);
                    j1 = l - rand.nextInt(4) - 1;
                    k1 = 3 - rand.nextInt(3);
                    int k3 = x;
                    int l1 = z;
                    int i2 = 0;
                    int j2;
                    int k2;

                    for (j2 = 0; j2 < l; ++j2)
                    {
                        k2 = y + j2;

                        if (j2 >= j1 && k1 > 0)
                        {
                            k3 += Direction.offsetX[j3];
                            l1 += Direction.offsetZ[j3];
                            --k1;
                        }

                        Block block1 = world.getBlock(k3, k2, l1);

                        if (block1.isAir(world, k3, k2, l1) || block1.isLeaves(world, k3, k2, l1))
                        {
                        	world.setBlock(k3, k2, l1, tree.TreeTrunkBlock, 0, 3);
                            //this.setBlockAndNotifyAdequately(world, k3, k2, l1, Blocks.log2, 0);
                            i2 = k2;
                        }
                    }

                    for (j2 = -1; j2 <= 1; ++j2)
                    {
                        for (k2 = -1; k2 <= 1; ++k2)
                        {
                            this.helperSavanah(world, k3 + j2, i2 + 1, l1 + k2);
                        }
                    }

                    this.helperSavanah(world, k3 + 2, i2 + 1, l1);
                    this.helperSavanah(world, k3 - 2, i2 + 1, l1);
                    this.helperSavanah(world, k3, i2 + 1, l1 + 2);
                    this.helperSavanah(world, k3, i2 + 1, l1 - 2);

                    for (j2 = -3; j2 <= 3; ++j2)
                    {
                        for (k2 = -3; k2 <= 3; ++k2)
                        {
                            if (Math.abs(j2) != 3 || Math.abs(k2) != 3)
                            {
                                this.helperSavanah(world, k3 + j2, i2, l1 + k2);
                            }
                        }
                    }

                    k3 = x;
                    l1 = z;
                    j2 = rand.nextInt(4);

                    if (j2 != j3)
                    {
                        k2 = j1 - rand.nextInt(2) - 1;
                        int l3 = 1 + rand.nextInt(3);
                        i2 = 0;
                        int l2;
                        int i3;

                        for (l2 = k2; l2 < l && l3 > 0; --l3)
                        {
                            if (l2 >= 1)
                            {
                                i3 = y + l2;
                                k3 += Direction.offsetX[j2];
                                l1 += Direction.offsetZ[j2];
                                Block block2 = world.getBlock(k3, i3, l1);

                                if (block2.isAir(world, k3, i3, l1) || block2.isLeaves(world, k3, i3, l1))
                                {
                                	world.setBlock(k3, i3, l1, tree.TreeTrunkBlock, 0, 3);
                                    //this.setBlockAndNotifyAdequately(world, k3, i3, l1, Blocks.log2, 0);
                                    i2 = i3;
                                }
                            }

                            ++l2;
                        }

                        if (i2 > 0)
                        {
                            for (l2 = -1; l2 <= 1; ++l2)
                            {
                                for (i3 = -1; i3 <= 1; ++i3)
                                {
                                    this.helperSavanah(world, k3 + l2, i2 + 1, l1 + i3);
                                }
                            }

                            for (l2 = -2; l2 <= 2; ++l2)
                            {
                                for (i3 = -2; i3 <= 2; ++i3)
                                {
                                    if (Math.abs(l2) != 2 || Math.abs(i3) != 2)
                                    {
                                        this.helperSavanah(world, k3 + l2, i2, l1 + i3);
                                    }
                                }
                            }
                        }
                    }

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

    private void helperSavanah(World world, int x, int y, int z)
    {
        Block block = world.getBlock(x, y, z);

        if (block.isAir(world, x, y, z) || block.isLeaves(world, x, y, z))
        {
        	world.setBlock(x, y, z, tree, 1, 3);
            //this.setBlockAndNotifyAdequately(world, x, y, z, Blocks.leaves2, 0);
        }
    }
    

    //////////////////////////////////////////////////////////////////////////////////////////////////
    
    public boolean generateCanopy(World world, Random rand, int x, int y, int z)
    {
        int l = rand.nextInt(3) + rand.nextInt(2) + 6;
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

                //boolean isSoil = block2.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, (BlockSapling)Blocks.sapling);
                boolean isSoil = tree.SaplingSoilBlocks.contains(block2);
                if (isSoil && y < 256 - l - 1)
                {
                    canopyHelper2(world, x,     y - 1, z,     x, y, z);
                    canopyHelper2(world, x + 1, y - 1, z,     x, y, z);
                    canopyHelper2(world, x + 1, y - 1, z + 1, x, y, z);
                    canopyHelper2(world, x,     y - 1, z + 1, x, y, z);
                    int j3 = rand.nextInt(4);
                    j1 = l - rand.nextInt(4);
                    k1 = 2 - rand.nextInt(3);
                    int k3 = x;
                    int l1 = z;
                    int i2 = 0;
                    int j2;
                    int k2;

                    for (j2 = 0; j2 < l; ++j2)
                    {
                        k2 = y + j2;

                        if (j2 >= j1 && k1 > 0)
                        {
                            k3 += Direction.offsetX[j3];
                            l1 += Direction.offsetZ[j3];
                            --k1;
                        }

                        Block block1 = world.getBlock(k3, k2, l1);

                        if (block1.isAir(world, k3, k2, l1) || block1.isLeaves(world, k3, k2, l1))
                        {
                            //this.setBlockAndNotifyAdequately(world, k3, k2, l1, Blocks.log2, 1);
                            //this.setBlockAndNotifyAdequately(world, k3 + 1, k2, l1, Blocks.log2, 1);
                            //this.setBlockAndNotifyAdequately(world, k3, k2, l1 + 1, Blocks.log2, 1);
                            //this.setBlockAndNotifyAdequately(world, k3 + 1, k2, l1 + 1, Blocks.log2, 1);
                            
                            world.setBlock(k3, k2, l1, tree.TreeTrunkBlock, 0, 3);
                            world.setBlock(k3 + 1, k2, l1, tree.TreeTrunkBlock, 0, 3);
                            world.setBlock(k3, k2, l1 + 1, tree.TreeTrunkBlock, 0, 3);
                            world.setBlock(k3 + 1, k2, l1 + 1, tree.TreeTrunkBlock, 0, 3);
                            
                            i2 = k2;
                        }
                    }

                    for (j2 = -2; j2 <= 0; ++j2)
                    {
                        for (k2 = -2; k2 <= 0; ++k2)
                        {
                            byte b1 = -1;
                            this.canopyHelper1(world, k3 + j2, i2 + b1, l1 + k2);
                            this.canopyHelper1(world, 1 + k3 - j2, i2 + b1, l1 + k2);
                            this.canopyHelper1(world, k3 + j2, i2 + b1, 1 + l1 - k2);
                            this.canopyHelper1(world, 1 + k3 - j2, i2 + b1, 1 + l1 - k2);

                            if ((j2 > -2 || k2 > -1) && (j2 != -1 || k2 != -2))
                            {
                                byte b2 = 1;
                                this.canopyHelper1(world, k3 + j2, i2 + b2, l1 + k2);
                                this.canopyHelper1(world, 1 + k3 - j2, i2 + b2, l1 + k2);
                                this.canopyHelper1(world, k3 + j2, i2 + b2, 1 + l1 - k2);
                                this.canopyHelper1(world, 1 + k3 - j2, i2 + b2, 1 + l1 - k2);
                            }
                        }
                    }

                    if (rand.nextBoolean())
                    {
                        this.canopyHelper1(world, k3, i2 + 2, l1);
                        this.canopyHelper1(world, k3 + 1, i2 + 2, l1);
                        this.canopyHelper1(world, k3 + 1, i2 + 2, l1 + 1);
                        this.canopyHelper1(world, k3, i2 + 2, l1 + 1);
                    }

                    for (j2 = -3; j2 <= 4; ++j2)
                    {
                        for (k2 = -3; k2 <= 4; ++k2)
                        {
                            if ((j2 != -3 || k2 != -3) && (j2 != -3 || k2 != 4) && (j2 != 4 || k2 != -3) && (j2 != 4 || k2 != 4) && (Math.abs(j2) < 3 || Math.abs(k2) < 3))
                            {
                                this.canopyHelper1(world, k3 + j2, i2, l1 + k2);
                            }
                        }
                    }

                    for (j2 = -1; j2 <= 2; ++j2)
                    {
                        for (k2 = -1; k2 <= 2; ++k2)
                        {
                            if ((j2 < 0 || j2 > 1 || k2 < 0 || k2 > 1) && rand.nextInt(3) <= 0)
                            {
                                int l3 = rand.nextInt(3) + 2;
                                int l2;

                                for (l2 = 0; l2 < l3; ++l2)
                                {
                                    //this.setBlockAndNotifyAdequately(world, x + j2, i2 - l2 - 1, z + k2, Blocks.log2, 1);
                                	world.setBlock(x + j2, i2 - l2 - 1, z + k2, tree.TreeTrunkBlock, 0, 3);
                                }

                                int i3;

                                for (l2 = -1; l2 <= 1; ++l2)
                                {
                                    for (i3 = -1; i3 <= 1; ++i3)
                                    {
                                        this.canopyHelper1(world, k3 + j2 + l2, i2 - 0, l1 + k2 + i3);
                                    }
                                }

                                for (l2 = -2; l2 <= 2; ++l2)
                                {
                                    for (i3 = -2; i3 <= 2; ++i3)
                                    {
                                        if (Math.abs(l2) != 2 || Math.abs(i3) != 2)
                                        {
                                            this.canopyHelper1(world, k3 + j2 + l2, i2 - 1, l1 + k2 + i3);
                                        }
                                    }
                                }
                            }
                        }
                    }

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

    private void canopyHelper1(World world, int x, int y, int z)
    {
        Block block = world.getBlock(x, y, z);

        if (block.isAir(world, x, y, z))
        {
        	world.setBlock(x, y, z, tree, 1, 3);
            //this.setBlockAndNotifyAdequately(world, x, y, z, Blocks.leaves2, 1);
        }
    }

    //Just a helper macro
    private void canopyHelper2(World world, int x, int y, int z, int sourceX, int sourceY, int sourceZ)
    {
        world.getBlock(x, y, z).onPlantGrow(world, x, y, z, sourceX, sourceY, sourceZ);
    }
    
	//////////////////////////////////////////////////////////////////////////////////////////////////
    // Below was shamelessly adapted from Natura under the Creative Commons 0 License. Thanks guys <3
    
    public boolean generateEucalyptus(World world, Random random, int posX, int posY, int posZ) 
    {
    	int height = random.nextInt(3) + 6; // Height
    	boolean flag = true;
    	if (posY < 1 || posY + height + 1 > 256) {
    		return false;
    	}
    	for (int i1 = posY; i1 <= posY + 1 + height; i1++) {
    		byte byte0 = 1;
    		if (i1 == posY) {
    			byte0 = 0;
    		}
    		if (i1 >= (posY + 1 + height) - 2) {
    			byte0 = 2;
    		}
    		label0: for (int l1 = posX - byte0; l1 <= posX + byte0 && flag; l1++) {
    			int j2 = posZ - byte0;
    			do {
    				if (j2 > posZ + byte0 || !flag) {
    					continue label0;
    				}
    				if (i1 >= 0 && i1 < 256) {
    					Block k2 = world.getBlock(l1, i1, j2);
    					if (k2 != Blocks.air && k2 != tree) {
    						flag = false;
    						continue label0;
    					}
    				} else {
    					flag = false;
    					continue label0;
    				}
    				j2++;
    			} while (true);
    		}
    	}
    	if (!flag) {
    		return false;
    	}
    	Block j1 = world.getBlock(posX, posY - 1, posZ);
    	if (!tree.SaplingSoilBlocks.contains(j1) || posY >= 256 - height - 1) {
    		return false;
    	}
    	//world.setBlock(posX, posY - 1, posZ, Blocks.dirt);
    	world.setBlock(posX, posY, posZ, Blocks.air);
    	Block test = world.getBlock(posX, posY, posZ);
    	boolean test1;
    	if (test == Blocks.air) {
    		test1 = world.setBlock(posX, posY, posZ, tree.TreeTrunkBlock, 0, 3);
    	}
    	for (int k1 = 0; k1 < height; k1++) {
    		Block i2 = world.getBlock(posX, posY + k1, posZ);
    		if (i2 == Blocks.air || i2 == tree) 
    		{
    			world.setBlock(posX, posY + k1, posZ, tree.TreeTrunkBlock, 0, 3);
    			//this.setBlockAndNotifyAdequately(world, , NContent.tree, mdWood);
    		}
    	}
    	eucalyptusHelper1(world, random, posX, posY, posZ, height, 1);
    	eucalyptusHelper1(world, random, posX, posY, posZ, height, 2);
    	eucalyptusHelper1(world, random, posX, posY, posZ, height, 3);
    	eucalyptusHelper1(world, random, posX, posY, posZ, height, 4);
    	eucalyptusHelper2(world, random, posX, posY, posZ, height, 1);
    	eucalyptusHelper2(world, random, posX, posY, posZ, height, 2);
    	eucalyptusHelper2(world, random, posX, posY, posZ, height, 3);
    	eucalyptusHelper2(world, random, posX, posY, posZ, height, 4);
    	eucalyptusHelper3(world, random, posX, posY + height, posZ);
    	return true;
    }

    private void eucalyptusHelper1(World world, Random random, int x, int y, int z,
    		int height, int direction) {
    	int posX = x;
    	int posY = y + height - 3;
    	int posZ = z;
    	byte byte0 = 0;
    	byte byte1 = 0;
    	switch (direction) {
    	case 1:
    		byte0 = 1;
    		byte1 = 1;
    		break;
    	case 2:
    		byte0 = -1;
    		byte1 = 1;
    		break;
    	case 3:
    		byte0 = 1;
    		byte1 = -1;
    		break;
    	case 4:
    		byte0 = -1;
    		byte1 = -1;
    		break;
    	}
    	int heightShift = random.nextInt(6);
    	for (int bIter = 4; bIter > 0; bIter--) {
    		if (heightShift % 3 != 0) {
    			posX += byte0;
    		}
    		if (heightShift % 3 != 1) {
    			posZ += byte1;
    		}
    		int branch = heightShift % 3;
    		posY += branch;
    		if (branch == 2)
    			world.setBlock(posX, posY - 1, posZ, tree.TreeTrunkBlock, 0, 3);
    		world.setBlock(posX, posY, posZ, tree.TreeTrunkBlock, 0, 3);
    		if (bIter == 1)
    			eucalyptusHelper3(world, random, posX, posY, posZ);
    		heightShift = random.nextInt(6);
    	}
    }

    private void eucalyptusHelper2(World world, Random random, int x, int y,
    		int z, int height, int direction) {
    	int posX = x;
    	int posY = y + height - 3;
    	int posZ = z;
    	byte xShift = 0;
    	byte zShift = 0;
    	switch (direction) {
    	case 1:
    		xShift = 1;
    		zShift = 0;
    		break;
    	case 2:
    		xShift = 0;
    		zShift = 1;
    		break;
    	case 3:
    		xShift = -1;
    		zShift = 0;
    		break;
    	case 4:
    		xShift = 0;
    		zShift = -1;
    		break;
    	}
    	int heightShift = random.nextInt(6);
    	for (int j2 = 4; j2 > 0; j2--) {
    		if (xShift == 0) {
    			posX = (posX + random.nextInt(3)) - 1;
    			posZ += zShift;
    		}
    		if (zShift == 0) {
    			posX += xShift;
    			posZ = (posZ + random.nextInt(3)) - 1;
    		}
    		int branch = heightShift % 3;
    		posY += branch;
    		if (branch == 2)
    			world.setBlock(posX, posY - 1, posZ, tree.TreeTrunkBlock, 0, 3);
    		world.setBlock(posX, posY, posZ, tree.TreeTrunkBlock, 0, 3);
    		if (j2 == 1)
    			eucalyptusHelper3(world, random, posX, posY, posZ);
    		heightShift = random.nextInt(6);
    	}
    }

    public boolean eucalyptusHelper3(World world, Random random, int x, int y, int z) 
    {
    	world.setBlock(x, y, z, tree.TreeTrunkBlock, 0, 3);
    	for (int xIter = x - 2; xIter <= x + 2; xIter++) {
    		for (int zIter = z - 1; zIter <= z + 1; zIter++) {
    			Block bID = world.getBlock(xIter, y, zIter);
    			if (bID != tree && !bID.func_149730_j()) {
    				world.setBlock(xIter, y, zIter, tree, 1, 3);
    			}
    		}
    	}
    	for (int xIter = x - 1; xIter <= x + 1; xIter++) {
    		for (int zIter = z - 2; zIter <= z + 2; zIter++) {
    			Block bID = world.getBlock(xIter, y, zIter);
    			if (bID != tree && !bID.func_149730_j()) {
    				world.setBlock(xIter, y, zIter, tree, 1, 3);
    			}
    		}
    	}
    	for (int xIter = x - 1; xIter <= x + 1; xIter++) {
    		for (int zIter = z - 1; zIter <= z + 1; zIter++) {
    			Block bID = world.getBlock(xIter, y + 1, zIter);
    			if (bID != tree && !bID.func_149730_j()) {
    				world.setBlock(xIter, y + 1, zIter, tree, 1, 3);
    			}
    		}
    	}
    	return true;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////

    private class SakuraTreeVars
    {
        public final byte[] otherCoordPairs = new byte[] {(byte)2, (byte)0, (byte)0, (byte)1, (byte)2, (byte)1};
        public int[] basePos = new int[] {0, 0, 0};
        public int heightLimit;
        public int height;
        public double heightAttenuation = 0.618D;
        public double branchDensity = 1.0D;
        public double branchSlope = 0.381D;
        public double scaleWidth = 1.0D;
        public double leafDensity = 1.0D;
        public int trunkSize = 1;
        public int heightLimitLimit = 12;
        public int leafDistanceLimit = 4;
        public int[][] leafNodes;
    } private SakuraTreeVars sakuraVars = new SakuraTreeVars();

	public boolean generateSakura(World world, Random rand, int par3, int par4, int par5) 
	{
		sakuraVars.basePos[0] = par3;
		sakuraVars.basePos[1] = par4;
		sakuraVars.basePos[2] = par5;
		if (sakuraVars.heightLimit == 0) {
			sakuraVars.heightLimit = 5 + rand.nextInt(sakuraVars.heightLimitLimit);
		}
		if (!this.sakuraHelper12(world)) {
			return false;
		} else {
			this.sakuraHelper1(world, rand);
			this.sakuraHelper7(world);
			this.sakuraHelper9(world);
			this.sakuraHelper10(world);
			return true;
		}
	}
    
	void sakuraHelper1(World world, Random rand) {
		sakuraVars.height = (int) ((double) sakuraVars.heightLimit * sakuraVars.heightAttenuation);
		if (sakuraVars.height >= sakuraVars.heightLimit) {
			sakuraVars.height = sakuraVars.heightLimit - 1;
		}
		int i = (int) (1.382D + Math.pow(sakuraVars.leafDensity
				* (double) sakuraVars.heightLimit / 13.0D, 2.0D));
		if (i < 1) {
			i = 1;
		}
		int[][] aint = new int[i * sakuraVars.heightLimit][4];
		int j = sakuraVars.basePos[1] + sakuraVars.heightLimit - sakuraVars.leafDistanceLimit;
		int k = 1;
		int l = sakuraVars.basePos[1] + sakuraVars.height;
		int i1 = j - sakuraVars.basePos[1];
		aint[0][0] = sakuraVars.basePos[0];
		aint[0][1] = j;
		aint[0][2] = sakuraVars.basePos[2];
		aint[0][3] = l;
		--j;
		while (i1 >= 0) {
			int j1 = 0;
			float f = this.sakuraHelper3(i1);
			if (f < 0.0F) {
				--j;
				--i1;
			} else {
				for (double d0 = 0.5D; j1 < i; ++j1) {
					double d1 = sakuraVars.scaleWidth * (double) f
							* ((double) rand.nextFloat() + 0.328D);
					double d2 = (double) rand.nextFloat() * 2.0D * Math.PI;
					int k1 = MathHelper.floor_double(d1 * Math.sin(d2)
							+ (double) sakuraVars.basePos[0] + d0);
					int l1 = MathHelper.floor_double(d1 * Math.cos(d2)
							+ (double) sakuraVars.basePos[2] + d0);
					int[] aint1 = new int[] { k1, j, l1 };
					int[] aint2 = new int[] { k1, j + sakuraVars.leafDistanceLimit,
							l1 };
					if (this.sakuraHelper11(world, aint1, aint2) == -1) {
						int[] aint3 = new int[] { sakuraVars.basePos[0],
								sakuraVars.basePos[1], sakuraVars.basePos[2] };
						double d3 = Math.sqrt(Math.pow(
								(double) Math.abs(sakuraVars.basePos[0] - aint1[0]),
								2.0D)
								+ Math.pow(
										(double) Math.abs(sakuraVars.basePos[2]
												- aint1[2]), 2.0D));
						double d4 = d3 * sakuraVars.branchSlope;
						if ((double) aint1[1] - d4 > (double) l) {
							aint3[1] = l;
						} else {
							aint3[1] = (int) ((double) aint1[1] - d4);
						}
						if (this.sakuraHelper11(world, aint3, aint1) == -1) {
							aint[k][0] = k1;
							aint[k][1] = j;
							aint[k][2] = l1;
							aint[k][3] = aint3[1];
							++k;
						}
					}
				}
				--j;
				--i1;
			}
		}
		sakuraVars.leafNodes = new int[k][4];
		System.arraycopy(aint, 0, sakuraVars.leafNodes, 0, k);
	}

	void sakuraHelper2(World world, int x, int y, int z, float p_150529_4_,byte p_150529_5_, Block block) {
		int l = (int) ((double) p_150529_4_ + 0.618D);
		byte b1 = sakuraVars.otherCoordPairs[p_150529_5_];
		byte b2 = sakuraVars.otherCoordPairs[p_150529_5_ + 3];
		int[] aint = new int[] { x, y, z };
		int[] aint1 = new int[] { 0, 0, 0 };
		int i1 = -l;
		int j1 = -l;
		for (aint1[p_150529_5_] = aint[p_150529_5_]; i1 <= l; ++i1) {
			aint1[b1] = aint[b1] + i1;
			j1 = -l;
			while (j1 <= l) {
				double d0 = Math.pow((double) Math.abs(i1) + 0.5D, 2.0D)
						+ Math.pow((double) Math.abs(j1) + 0.5D, 2.0D);
				if (d0 > (double) (p_150529_4_ * p_150529_4_)) {
					++j1;
				} else {
					aint1[b2] = aint[b2] + j1;
					Block block1 = world.getBlock(aint1[0], aint1[1],
							aint1[2]);
					if (!block1.isAir(world, aint1[0], aint1[1], aint1[2]) && !block1.isLeaves(world, aint1[0], aint1[1], aint1[2])) 
					{
						++j1;
					} else {
						world.setBlock(aint1[0], aint1[1], aint1[2], block, 1, 3);
						//this.setBlockAndNotifyAdequately(world, aint1[0], aint1[1], aint1[2], block, metaLeaves);
						++j1;
					}
				}
			}
		}
	}

	float sakuraHelper3(int par1) {
		if ((double) par1 < (double) ((float) sakuraVars.heightLimit) * 0.3D) {
			return -1.618F;
		} else {
			float f = (float) sakuraVars.heightLimit / 2.0F;
			float f1 = (float) sakuraVars.heightLimit / 2.0F - (float) par1;
			float f2;
			if (f1 == 0.0F) {
				f2 = f;
			} else if (Math.abs(f1) >= f) {
				f2 = 0.0F;
			} else {
				f2 = (float) Math.sqrt(Math.pow((double) Math.abs(f), 2.0D)	- Math.pow((double) Math.abs(f1), 2.0D));
			}
			f2 *= 0.5F;
			return f2;
		}
	}

	float sakuraHelper4(int par1) {
		return par1 >= 0 && par1 < sakuraVars.leafDistanceLimit ? (par1 != 0
				&& par1 != sakuraVars.leafDistanceLimit - 1 ? 3.0F : 2.0F) : -1.0F;
	}

	void sakuraHelper5(World world, int par1, int par2, int par3) {
		int l = par2;
		for (int i1 = par2 + sakuraVars.leafDistanceLimit; l < i1; ++l) {
			float f = this.sakuraHelper4(l - par2);
			this.sakuraHelper2(world, par1, l, par3, f, (byte) 1, tree);
		}
	}

	void sakuraHelper6(World world, int[] p_150530_1_, int[] p_150530_2_, Block block) {
		int[] aint2 = new int[] { 0, 0, 0 };
		byte b0 = 0;
		byte b1;
		for (b1 = 0; b0 < 3; ++b0) {
			aint2[b0] = p_150530_2_[b0] - p_150530_1_[b0];
			if (Math.abs(aint2[b0]) > Math.abs(aint2[b1])) {
				b1 = b0;
			}
		}
		if (aint2[b1] != 0) {
			byte b2 = sakuraVars.otherCoordPairs[b1];
			byte b3 = sakuraVars.otherCoordPairs[b1 + 3];
			byte b4;
			if (aint2[b1] > 0) {
				b4 = 1;
			} else {
				b4 = -1;
			}
			double d0 = (double) aint2[b2] / (double) aint2[b1];
			double d1 = (double) aint2[b3] / (double) aint2[b1];
			int[] aint3 = new int[] { 0, 0, 0 };
			int i = 0;
			for (int j = aint2[b1] + b4; i != j; i += b4) {
				aint3[b1] = MathHelper
						.floor_double((double) (p_150530_1_[b1] + i) + 0.5D);
				aint3[b2] = MathHelper.floor_double((double) p_150530_1_[b2]
						+ (double) i * d0 + 0.5D);
				aint3[b3] = MathHelper.floor_double((double) p_150530_1_[b3]
						+ (double) i * d1 + 0.5D);
				int k = Math.abs(aint3[0] - p_150530_1_[0]);
				int l = Math.abs(aint3[2] - p_150530_1_[2]);
				int i1 = Math.max(k, l);
				
				world.setBlock(aint3[0], aint3[1], aint3[2], block, 1, 3);
				//this.setBlockAndNotifyAdequately(this.worldObj, aint3[0], aint3[1], aint3[2], block, metadata);
			}
		}
	}

	void sakuraHelper7(World world) {
		int i = 0;
		for (int j = sakuraVars.leafNodes.length; i < j; ++i) {
			int k = sakuraVars.leafNodes[i][0];
			int l = sakuraVars.leafNodes[i][1];
			int i1 = sakuraVars.leafNodes[i][2];
			this.sakuraHelper5(world, k, l, i1);
		}
	}

	boolean sakuraHelper8(int par1) {
		return (double) par1 >= (double) sakuraVars.heightLimit * 0.2D;
	}

	void sakuraHelper9(World world) {
		int i = sakuraVars.basePos[0];
		int j = sakuraVars.basePos[1];
		int k = sakuraVars.basePos[1] + sakuraVars.height;
		int l = sakuraVars.basePos[2];
		int[] aint = new int[] { i, j, l };
		int[] aint1 = new int[] { i, k, l };
		this.sakuraHelper6(world, aint, aint1, tree.TreeTrunkBlock);
		if (sakuraVars.trunkSize == 2) {
			++aint[0];
			++aint1[0];
			this.sakuraHelper6(world, aint, aint1, tree.TreeTrunkBlock);
			++aint[2];
			++aint1[2];
			this.sakuraHelper6(world, aint, aint1, tree.TreeTrunkBlock);
			aint[0] += -1;
			aint1[0] += -1;
			this.sakuraHelper6(world, aint, aint1, tree.TreeTrunkBlock);
		}
	}

	void sakuraHelper10(World world) {
		int i = 0;
		int j = sakuraVars.leafNodes.length;
		for (int[] aint = new int[] { sakuraVars.basePos[0], sakuraVars.basePos[1], sakuraVars.basePos[2] }; i < j; ++i) {
			int[] aint1 = sakuraVars.leafNodes[i];
			int[] aint2 = new int[] { aint1[0], aint1[1], aint1[2] };
			aint[1] = aint1[3];
			int k = aint[1] - sakuraVars.basePos[1];
			if (this.sakuraHelper8(k)) {
				this.sakuraHelper6(world, aint, aint2, tree.TreeTrunkBlock);
			}
		}
	}

	int sakuraHelper11(World world, int[] par1ArrayOfInteger, int[] par2ArrayOfInteger) {
		int[] aint2 = new int[] { 0, 0, 0 };
		byte b0 = 0;
		byte b1;
		for (b1 = 0; b0 < 3; ++b0) {
			aint2[b0] = par2ArrayOfInteger[b0] - par1ArrayOfInteger[b0];
			if (Math.abs(aint2[b0]) > Math.abs(aint2[b1])) {
				b1 = b0;
			}
		}
		if (aint2[b1] == 0) {
			return -1;
		} else {
			byte b2 = sakuraVars.otherCoordPairs[b1];
			byte b3 = sakuraVars.otherCoordPairs[b1 + 3];
			byte b4;
			if (aint2[b1] > 0) {
				b4 = 1;
			} else {
				b4 = -1;
			}
			double d0 = (double) aint2[b2] / (double) aint2[b1];
			double d1 = (double) aint2[b3] / (double) aint2[b1];
			int[] aint3 = new int[] { 0, 0, 0 };
			int i = 0;
			int j;
			for (j = aint2[b1] + b4; i != j; i += b4) {
				aint3[b1] = par1ArrayOfInteger[b1] + i;
				aint3[b2] = MathHelper
						.floor_double((double) par1ArrayOfInteger[b2]
								+ (double) i * d0);
				aint3[b3] = MathHelper
						.floor_double((double) par1ArrayOfInteger[b3]
								+ (double) i * d1);
				if (!this.isReplaceable(world, aint3[0], aint3[1], aint3[2])) {
					break;
				}
			}
			return i == j ? -1 : Math.abs(i);
		}
	}

	boolean sakuraHelper12(World world) {
		int[] aint = new int[] { sakuraVars.basePos[0], sakuraVars.basePos[1],
				sakuraVars.basePos[2] };
		int[] aint1 = new int[] { sakuraVars.basePos[0],
				sakuraVars.basePos[1] + sakuraVars.heightLimit - 1, sakuraVars.basePos[2] };
		Block block = world.getBlock(sakuraVars.basePos[0],
				sakuraVars.basePos[1] - 1, sakuraVars.basePos[2]);
		
		boolean isSoil = tree.SaplingSoilBlocks.contains(block); 
		if (!isSoil) {
			return false;
		} else {
			int i = this.sakuraHelper11(world, aint, aint1);
			if (i == -1) {
				return true;
			} else if (i < 6) {
				return false;
			} else {
				sakuraVars.heightLimit = i;
				return true;
			}
		}
	}

	int sakuraHelper13(World world, int x, int y, int z) {
		boolean foundGround = false;
		int height = 128;
		do {
			height--;
			Block underID = world.getBlock(x, height, z);
			if (tree.SaplingSoilBlocks.contains(underID) || height < 32)
				foundGround = true;
		} while (!foundGround);
		return height + 1;
	}
    
}
