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
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockKawaiiTreeBlocks extends BlockBush implements IShearable, IGrowable {

	private String name = "";
	public Boolean Enabled = false;
	
	public float SaplingGrowthChance = 0.125f;
	public float SaplingGrowthChanceBonemeal = 0.125f;
	public HashSet<Block> SaplingGrowsOn = null;
	public String SaplingOreDict = "";
	public String SaplingToolTip = "";

	public Block LeafTrunkBlock = null;
	public float LeafGrowthMultiplier = 1.0f;
	public Boolean LeafExternalFruit = false;
	public float LeafGravityChance = 0.0f;
	
	public Boolean FruitEdible = true;
	public int FruitHunger = 2;
	public float FruitSaturation = 0.1f;
	public PotionEffectHelper FruitPotionEffets = null;
	public String FruitOreDict = "";
	public String FruitToolTip = "";

	public String LeafDropTableRipeString = "";
	private DropTable LeafDropTableRipe = null;
	
	public String LeafDropTableUnripeString = "";
	private DropTable LeafDropTableUnripe = null;
	
	public String LeafDropTableDestroyedString = "";
	private DropTable LeafDropTableDestroyed = null;

	public String LeafDropTableShearedString = "";
	private DropTable LeafDropTableSheared = null;

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
		LeafDropTableSheared = new DropTable(LeafDropTableShearedString, Sapling, Fruit);
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

    /////////////////////////////////////////////////////////////////////////////////////
    // IGrowable
    
	
	@Override
	public boolean func_149851_a(World world, int x, int y, int z, boolean p_149851_5_) 
	{
		// can bonemeal
		return (world.getBlockMetadata(x, y, z) < 7);
	}

	@Override 
	public boolean func_149852_a(World world, Random rand, int x, int y, int z) 
	{
		// true if during bonemeal event, conditions for growth are acceptable
		return true;
	}

	@Override // onBonemeal
	public void func_149853_b(World world, Random rand,	int x, int y, int z) 
	{
		//updateTick(world, x, y, z, rand);
		
		if (world.getBlockMetadata(x, y, z) == 0)
			(new WorldGenKawaiiTree(this)).generate(world, rand, x, y, z);
		
		//world.setBlockMetadataWithNotify(x, y, z, 1 + (world.getBlockMetadata(x, y, z) + 1 ) % 6, 2);
	}
	
    /////////////////////////////////////////////////////////////////////////////////////
    // Behavior 
   
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
    
    @Override
    public boolean canBlockStay(World world, int x, int y, int z)
    {
    	switch(getState(world, x, y, z))
    	{
			case FRUIT:
			case FRUITRIPE:
				return true;
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
    			growSapling(world, x, y, z);
    			break;
    			
			case FRUIT:
				matureFruit(world, x, y, z);
				break;
			case FRUITRIPE:
				gravityFruit(world, x, y, z);
				break;

			case LEAF:
				if(!decayLeaves(world, x, y ,z)) return;
				growFruit(world, x, y, z);
				break;
			case FRUITLEAF:
				if(!decayLeaves(world, x, y ,z)) return;
				matureFruit(world, x, y, z);
				break;
			case FRUITLEAFRIPE:
				if(!decayLeaves(world, x, y ,z)) return;
				gravityFruit(world, x, y, z);
				break;
    	}
    	
    }
    
    private boolean growSapling(World world, int x, int y, int z)
    {
    	// x & z from -3 to 3, y from -2 to 4
    	// Based on: + Fertile blocks with air above & Sufficient light
    	//           - unnatural lighting (differing values in y ?
    	// Ideal: Cuts spawn chance to 1 in 4
    	// Half-coverage: Cuts spawn chance to 1 in 16
    	// Worst case: Cuts spawn chance to 1 in 32
    	
    	// (new WorldGenKawaiiTree(this)).generate(world, rand, x, y, z);

    	// Total value of this crop
    	float value = 0;
    	
    	// Not bright enough for the sapling to grow.
    	if (world.getBlockLightValue(x, y + 1, z) < 9) 
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
            		if (SaplingGrowsOn.contains(block) && (above == Material.air || above == Material.grass || above == Material.plants || above == Material.snow || above == Material.leaves)) 
            		{
            			// Light level above soil
            			int light = world.getBlockLightValue(i, j + 1, k);

            			// Light is "natural" if consistent over 4 y levels
            			boolean natural = true;
            			for (int l = 2; l <= 4; l++)
            				if (world.getBlockLightValue(i, j + 1, k) != light) 
            					natural = false;
            			
            			value += (float)light * (natural ? 1.0f : 0.75f) * (above == Material.grass || above == Material.snow ? 0.75f : 1.0f) * (above == Material.plants || above == Material.leaves ? 0.5f : 1.0f);
            			
            			System.out.println("Soil Block @ " + i + "," + j + "," + k + " - Above: " + NamespaceHelper.getBlockName(world.getBlock(i, j + 1, k)) + "Light: " + light + (natural?", Unnatural":", Natural") + ", Value: " + ((float)light * (natural ? 1.0f : 0.75f) * (above == Material.grass || above == Material.snow ? -.75f : 1.0f) * (above == Material.plants || above == Material.leaves ? 0.5f : 1.0f)));
            		}
            	}
    	
    	System.out.println("Total Value: " + value);
    	
    	// if fancy math & random, return generate()
    	
    	return true;
    }
    
    private boolean growFruit(World world, int x, int y, int z)
    {
    	int leaf = 0;
    	int leaffruit = 0;
    	int fruit = 0;
    	
    	for (int i = -1 + x; i <= 1 + x; i++)
        	for (int k = -1 + z; k <= 1 + z; k++)
            	for (int j = -1 + y; j <= 1 + y; j++)
            	{
            		if (this != world.getBlock(i, j, k)) 
            			continue;
            		TreeState state = getState(world, i, j, k);
            		if (state == TreeState.LEAF)
            			leaf++;
            		if (state == TreeState.FRUITLEAF || state == TreeState.FRUITLEAFRIPE)
            			leaffruit++;
            		if (state == TreeState.FRUIT || state == TreeState.FRUITRIPE)
            			fruit++;
            	}
    	
    	// Some kind of formula for possibly spawning fruit    	
    	return true;
    }
    
    private boolean matureFruit(World world, int x, int y, int z)
    {
    	// Check light level above tree or in block or something ?
        
    	return true;
    }
    
    private boolean gravityFruit(World world, int x, int y, int z)
    {
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
