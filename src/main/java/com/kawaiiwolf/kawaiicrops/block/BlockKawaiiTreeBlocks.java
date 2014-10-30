package com.kawaiiwolf.kawaiicrops.block;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import com.kawaiiwolf.kawaiicrops.item.ItemKawaiiFood;
import com.kawaiiwolf.kawaiicrops.item.ItemKawaiiIngredient;
import com.kawaiiwolf.kawaiicrops.item.ItemKawaiiSeed;
import com.kawaiiwolf.kawaiicrops.lib.Constants;
import com.kawaiiwolf.kawaiicrops.lib.DropTable;
import com.kawaiiwolf.kawaiicrops.lib.NamespaceHelper;
import com.kawaiiwolf.kawaiicrops.lib.PotionEffectHelper;
import com.kawaiiwolf.kawaiicrops.renderer.RenderingHandlerKawaiiCropBlocks;
import com.kawaiiwolf.kawaiicrops.world.WorldGenKawaiiTree;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockKawaiiTreeBlocks extends BlockBush implements IGrowable {

	private String name = "";
	public Boolean Enabled = false;
	
	public float SaplingGrowthMultiplier = 1.0f;
	public float SaplingGrowthChanceBonemeal = 0.125f;
	public int SaplingMinimumLight = 4;
	public HashSet<Block> SaplingGrowsOn = null;
	public String SaplingOreDict = "";
	public String SaplingToolTip = "";

	public Block LeafTrunkBlock = null;
	public float LeafGrowthMultiplier = 1.0f;
	public Boolean LeafExternalFruit = true;
	public float LeafGravityChance = 0.0f;
	
	
	public Boolean FruitEdible = true;
	public int FruitHunger = 2;
	public float FruitSaturation = 0.1f;
	public float FruitGrowthMultiplier = 1.0f;
	public PotionEffectHelper FruitPotionEffets = null;
	public String FruitOreDict = "";
	public String FruitToolTip = "";

	public String LeafDropTableRipeString = "";
	private DropTable LeafDropTableRipe = null;
	
	public String LeafDropTableUnripeString = "";
	private DropTable LeafDropTableUnripe = null;
	
	public String LeafDropTableDestroyedString = "";
	private DropTable LeafDropTableDestroyed = null;

	private Item Sapling;
	private Item Fruit;
	
	/*
	 * tree wood shape ?
	 */
	
	public boolean TreeIsTall = false; 
	
	public BlockKawaiiTreeBlocks(String name) {
		super(Material.leaves);
		
        this.setTickRandomly(true);
        this.setHardness(0.2F);
        this.setLightOpacity(1);
        this.setStepSound(soundTypeGrass);
        
        this.name = name;
        this.setBlockName(Constants.MOD_ID + "." + name);
	}
    
    /////////////////////////////////////////////////////////////////////////////////////
    // Block & Item Registration
   
	public void register()
	{	
		if (!this.Enabled) return; 
		GameRegistry.registerBlock(this, this.getUnlocalizedName());
		
		String saplingName = name + ".sapling";
		Sapling = new ItemKawaiiSeed(saplingName, SaplingToolTip, this);
		((ItemKawaiiSeed)Sapling).OreDict = SaplingOreDict;

		GameRegistry.registerItem(Sapling, saplingName);
		
		String fruitName = name + ".fruit";
		Fruit = (Item) (FruitEdible
				? new ItemKawaiiFood(fruitName, FruitToolTip, FruitHunger, FruitSaturation, FruitPotionEffets)
				: new ItemKawaiiIngredient(fruitName, FruitToolTip));
		
		if (FruitEdible)
			((ItemKawaiiFood)Fruit).OreDict = FruitOreDict;
		else
			((ItemKawaiiIngredient)Fruit).OreDict = FruitOreDict;
		
		GameRegistry.registerItem(Fruit, Constants.MOD_ID + "." + fruitName);

		ModBlocks.AllTrees.add(this);
	}
    
	public void registerDropTables()
	{
		LeafDropTableRipe = new DropTable(LeafDropTableRipeString, Sapling, Fruit);
		LeafDropTableUnripe = new DropTable(LeafDropTableUnripeString, Sapling, Fruit);
		LeafDropTableDestroyed = new DropTable(LeafDropTableDestroyedString, Sapling, Fruit);
	}
	
	/////////////////////////////////////////////////////////////////////////////////////
    // Rendering

	@SideOnly(Side.CLIENT)
	public IIcon[] icons = new IIcon[6];
	
	@Override
    @SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) 
	{
		return icons[0];
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg)
    {
		icons[0] = reg.registerIcon(Constants.MOD_ID + ":" + name + ".sapling");
		icons[1] = reg.registerIcon(Constants.MOD_ID + ":" + name + ".leaf");
		icons[2] = reg.registerIcon(Constants.MOD_ID + ":" + name + ".fruit.stage_0");
		icons[3] = reg.registerIcon(Constants.MOD_ID + ":" + name + ".fruit.stage_1");
		icons[4] = reg.registerIcon(Constants.MOD_ID + ":" + name + ".fruit.stage_2");
		icons[5] = reg.registerIcon(Constants.MOD_ID + ":" + name + ".fruit.stage_3");
	}

    @SideOnly(Side.CLIENT)
    public IIcon getSaplingIcon()
    {
    	return icons[0];
    }
	
    @SideOnly(Side.CLIENT)
    public IIcon getLeafIcon()
    {
    	return icons[1];
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getFruitForStage(IBlockAccess world, int x, int y, int z)
    {
    	return getFruitForStage(world.getBlockMetadata(x, y, z));
    }
    
	@Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) 
    {
		float f = 0.2F;
		switch(getState(world,x,y,z))
		{
			case FRUIT:
			case FRUITRIPE:
				setBlockBounds(0.5F - f, 1 - (f * 3.0F), 0.5F - f, 0.5F + f, 1.0F, 0.5F + f);
				break;
			case LEAF:
			case FRUITLEAF:
			case FRUITLEAFRIPE:
				setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
				break;
			case SAPLING:
				setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 3.0F, 0.5F + f);
				break;
		}
    }
	
	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
		switch(getState(world,x,y,z))
		{
			case LEAF:
			case FRUITLEAF:
			case FRUITLEAFRIPE:
				return AxisAlignedBB.getBoundingBox((double)x + this.minX, (double)y + this.minY, (double)z + this.minZ, (double)x + this.maxX, (double)y + this.maxY, (double)z + this.maxZ);
			default:
				return null;
		}
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getFruitForStage(int meta)
    {
    	if (meta >= 2 && meta <= 4)
    		return icons[meta];
    	if (meta >=5 && meta <= 7)
    		return icons[meta - 3];
    	return icons[5];
    }
    
	@Override
	@SideOnly(Side.CLIENT)
    public int getRenderType()
    {
        return RenderingHandlerKawaiiCropBlocks.instance.getRenderId();
    }
	
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand)
    {
        if (world.canLightningStrikeAt(x, y + 1, z) && !World.doesBlockHaveSolidTopSurface(world, x, y - 1, z) && rand.nextInt(15) == 1)
        {
        	double i = (double)((float)x + rand.nextFloat());
            double j = (double)y - 0.05D;
            double k = (double)((float)z + rand.nextFloat());
            world.spawnParticle("dripWater", i, j, k, 0.0D, 0.0D, 0.0D);
        }
    }
    	
    /////////////////////////////////////////////////////////////////////////////////////
    // States 
    
    public enum TreeState {SAPLING, LEAF, FRUIT, FRUITRIPE, FRUITLEAF, FRUITLEAFRIPE }
    
    public TreeState getState(IBlockAccess world, int x, int y, int z)
    {
    	return getState(world.getBlockMetadata(x, y, z));
    }

    public TreeState getState(int meta)
    {
	    if (meta == 0)
	    	return TreeState.SAPLING;
	    if (meta == 1)
	    	return TreeState.LEAF;
	    if (meta >= 2 && meta <= 4)
	    	return TreeState.FRUITLEAF;
	    if (meta >= 5 && meta <= 7 )
	    	return TreeState.FRUIT;
	    if (meta == 8)
	    	return ( LeafExternalFruit ? TreeState.FRUITRIPE : TreeState.FRUITLEAFRIPE);
	    
	    return TreeState.LEAF;
    }

    /////////////////////////////////////////////////////////////////////////////////////
    // IGrowable
    
	@Override // can bonemeal
	public boolean func_149851_a(World world, int x, int y, int z, boolean p_149851_5_) 
	{
		if (LeafExternalFruit && getState(world, x, y, z) == TreeState.LEAF)
			return (world.getBlock(x, y - 1, z) == Blocks.air);
		return (world.getBlockMetadata(x, y, z) < 8);
	}

	@Override // true if during bonemeal event, conditions for growth are acceptable
	public boolean func_149852_a(World world, Random rand, int x, int y, int z) 
	{
		return true;
	}

	@Override // onBonemeal
	public void func_149853_b(World world, Random rand,	int x, int y, int z) 
	{
		updateTick(world, x, y, z, rand);
	}
	
    /////////////////////////////////////////////////////////////////////////////////////
    // Behavior 
   
    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

        switch(getState(metadata ))
        {
			case FRUIT:
				ret.addAll(this.LeafDropTableUnripe.generateLoot(world.rand));
				break;

			case FRUITRIPE:
				ret.addAll(this.LeafDropTableRipe.generateLoot(world.rand));
				break;

			case FRUITLEAF:
				ret.addAll(this.LeafDropTableDestroyed.generateLoot(world.rand));
				ret.addAll(this.LeafDropTableUnripe.generateLoot(world.rand));
				break;
			case FRUITLEAFRIPE:
				ret.addAll(this.LeafDropTableDestroyed.generateLoot(world.rand));
				ret.addAll(this.LeafDropTableRipe.generateLoot(world.rand));
				break;
			case LEAF:
				ret.addAll(this.LeafDropTableDestroyed.generateLoot(world.rand));
				break;

			case SAPLING:
				ret.add(new ItemStack(getItemDropped(0,world.rand, 0)));
				break;
			default:
				break;
        }
        return ret;
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xhit, float yhit, float zhit)
    {
    	if (getState(world, x, y, z) == TreeState.FRUITLEAFRIPE)
    		; // Gravity Drop
        return false;
    }
    
    @Override
    public boolean canBlockStay(World world, int x, int y, int z)
    {
    	switch(getState(world, x, y, z))
    	{
			case FRUIT:
			case FRUITRIPE:
				return ( world.getBlock(x, y + 1, z) == this && getState(world, x, y + 1, z) == TreeState.LEAF ); 
			case SAPLING:
				return super.canBlockStay(world, x, y, z);
			default:
				return true;
    	}
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand)
    {
    	if(world.isRemote) return;
    	
    	switch (getState(world,x,y,z))
    	{
    		case SAPLING:
    			growSapling(world, x, y, z,rand);
    			break;
    			
			case FRUIT:
				matureFruit(world, x, y, z, rand);
				break;
			case FRUITRIPE:
				gravityFruit(world, x, y, z, rand);
				break;

			case LEAF:
				if(!decayLeaves(world, x, y ,z)) return;
				growFruit(world, x, y, z, rand);
				break;
			case FRUITLEAF:
				if(!decayLeaves(world, x, y ,z)) return;
				matureFruit(world, x, y, z, rand);
				break;
			case FRUITLEAFRIPE:
				if(!decayLeaves(world, x, y ,z)) return;
				gravityFruit(world, x, y, z, rand);
				break;
    	}
    	
    }
    
    /////////////////////////////////////////////////////////////////////////////////////
    // private Helper Functions 
    
    private boolean growSapling(World world, int x, int y, int z, Random rand)
    {
    	// Total value of this crop
    	float value = 0;
    	
    	// Not bright enough for the sapling to grow.
    	if (world.getBlockLightValue(x, y + 1, z) < SaplingMinimumLight) 
    		return true;
    	
    	// Radius to check nearby ground blocks.
    	int r = 2;
    	for (int i = -r + x; i <= r + x; i++)
        	for (int k = -r + z; k <= r + z; k++)
            	for (int j = -2 + y; j <= 0 + y; j++)
            	{
            		// Ignore corners, sapling block and mounds next to sapling. Maintain surface gradient!
            		if (Math.abs(i - x) <= 1 && j == y && Math.abs(k - z) <= 1) continue;
            		if (i == x && j == (y - 1) && k == z) continue;
            		if (Math.abs(i - x) == r && Math.abs(k - z) == r) continue;
            		
            		Block block = world.getBlock(i, j, k);
            		Material above = world.getBlock(i, j + 1, k).getMaterial();
            		if (SaplingGrowsOn.contains(block) && (above == Material.air || above == Material.vine || above == Material.plants || above == Material.snow || above == Material.leaves)) 
            		{
            			// Light level above soil
            			int light = world.getBlockLightValue(i, j + 1, k);

            			// Light is "natural" if consistent over 4 y levels
            			boolean natural = true;
            			for (int l = 2; l <= 4; l++)
            				if (world.getBlockLightValue(i, j + 1, k) != light) 
            					natural = false;
            			
            			value += (float)light * (natural ? 1.0f : 0.75f) * (above == Material.plants || above == Material.snow ? 0.75f : 1.0f) * (above == Material.vine || above == Material.leaves ? 0.5f : 1.0f);
            		}
            	}
    	
    	if (rand.nextDouble() < (SaplingGrowthMultiplier * 0.25d / Math.pow(2.0d, (300.0d - value) / 75.0d)))
    		return !(new WorldGenKawaiiTree(this)).generate(world, rand, x, y, z);
    	
    	return true;
    }
    
    private boolean growFruit(World world, int x, int y, int z, Random rand)
    {
    	int leaf = 0;
    	int fruit = 0;
    	
    	// If we're to grow fruit beneath and there's no room to grow, abort
    	if (LeafExternalFruit && world.getBlock(x, y - 1, z) != Blocks.air) return true;

    	// Minimum light to grow
    	if (world.getBlockLightValue(x, y - (LeafExternalFruit ? 1 : 0), z) < SaplingMinimumLight) 
    		return true;

    	// Sum up each leaf & fruit in a 1 block radius
    	for (int i = -1 + x; i <= 1 + x; i++)
        	for (int k = -1 + z; k <= 1 + z; k++)
            	for (int j = -1 + y; j <= 1 + y; j++)
            	{
            		if (this != world.getBlock(i, j, k)) 
            			continue;
            		TreeState state = getState(world, i, j, k);
            		if (state == TreeState.LEAF)
            			leaf++;
            		if (state == TreeState.FRUIT || state == TreeState.FRUITRIPE)
            			fruit++;
            		if (state == TreeState.FRUITLEAF || state == TreeState.FRUITLEAFRIPE)
            		{
            			leaf++; 
            			fruit++;
            		}
            	}
    	
		if (rand.nextInt((int)(35 - leaf + (6 * fruit) / LeafGrowthMultiplier * 15.0f / world.getBlockLightValue(x, y - (LeafExternalFruit ? 1 : 0), z)) + 1) == 0)
		{
	    	if (LeafExternalFruit)
    			world.setBlock(x, y - 1, z, this, 5, 3);
	    	else
    			world.setBlockMetadataWithNotify(x, y, z, 2, 3);
    	}
    	
    	return true;
    }
    
    private boolean matureFruit(World world, int x, int y, int z, Random rand)
    {
    	int light = world.getBlockLightValue(x, y, z);
    	if (light < SaplingMinimumLight || rand.nextInt((int)(4.0f / FruitGrowthMultiplier * 15.0f / light) + 1) != 0)
    		return true;
    	
    	int meta = world.getBlockMetadata(x, y, z);
    	if (meta < 8)
    		world.setBlockMetadataWithNotify(x, y, z, (meta == 4 ? 8 : meta + 1), 2);
        
    	return true;
    }
    
    private boolean gravityFruit(World world, int x, int y, int z, Random rand)
    {
    	// if (chance) dropBlockAsItem(...params from get drop ? ...)
    	return true;
    }

    private boolean decayLeaves(World world, int x, int y, int z)
    {
    	boolean tree = false, leaf = false;
    	
    	for (int i = -3; i <= 3; i++)
        	for (int j = -3; j <= 3; j++)
            	for (int k = -3; k <= 3; k++)
            	{
            		if (i == j && j == k && k == 0) continue;
            		Block b = world.getBlock(x + i, y + j, z + k);
            		
            		if (Math.abs(i) <= 1 && Math.abs(j) <= 1 && Math.abs(k) <= 1)
            		{
                		if (b == LeafTrunkBlock) return true;
                		TreeState s = getState(world, x, y, z);
                		if (b == this && (s == TreeState.LEAF || s == TreeState.FRUITLEAF || s == TreeState.FRUITLEAFRIPE))
                			leaf = true;
            		}
            		else if (b == LeafTrunkBlock) tree = true;
            		if (tree && leaf) return true;
            	}

    	// Do Decay Drop
    	world.setBlock(x, y, z, Blocks.air, 0, 3);
    	
    	return false;
    }
}
