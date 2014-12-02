package com.kawaiiwolf.kawaiicrops.block;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import com.kawaiiwolf.kawaiicrops.item.ItemKawaiiFood;
import com.kawaiiwolf.kawaiicrops.item.ItemKawaiiIngredient;
import com.kawaiiwolf.kawaiicrops.item.ItemKawaiiSeed;
import com.kawaiiwolf.kawaiicrops.lib.ConfigurationLoader;
import com.kawaiiwolf.kawaiicrops.lib.Constants;
import com.kawaiiwolf.kawaiicrops.lib.DropTable;
import com.kawaiiwolf.kawaiicrops.lib.NamespaceHelper;
import com.kawaiiwolf.kawaiicrops.lib.PotionEffectHelper;
import com.kawaiiwolf.kawaiicrops.renderer.RenderingHandlerKawaiiCropBlocks;
import com.kawaiiwolf.kawaiicrops.world.ModWorldGen;
import com.kawaiiwolf.kawaiicrops.world.WorldGenKawaiiBaseWorldGen;
import com.kawaiiwolf.kawaiicrops.world.WorldGenKawaiiCrop;
import com.kawaiiwolf.kawaiicrops.world.WorldGenKawaiiTree;

import mcp.mobius.waila.api.IWailaBlock;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.SpecialChars;
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
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockKawaiiTreeBlocks extends BlockBush implements IGrowable, IWailaBlock {

	private String name = "";
	public Boolean Enabled = false;
	
	public float GrowthMultiplierSapling = 1.0f;
	public int SaplingMinimumLight = 4;
	public HashSet<Block> SaplingSoilBlocks = null;
	public String SaplingOreDict = "";
	public String SaplingToolTip = "";
	public WorldGenKawaiiTree.TreeShape TreeShape = WorldGenKawaiiTree.TreeShape.FOREST;

	public Block TreeTrunkBlock = null;
	public float GrowthMultiplierLeaf = 1.0f;
	public Boolean TreeExternalFruit = true;
	public float TreeGravityChance = 0.0f;
	
	public Boolean FruitEdible = true;
	public int FruitHunger = 2;
	public float FruitSaturation = 0.1f;
	public float GrowthMultiplierFruit = 1.0f;
	public PotionEffectHelper FruitPotionEffets = null;
	public String FruitOreDict = "";
	public String FruitToolTip = "";

	public String DropTableRipeString = "";
	private DropTable LeafDropTableRipe = null;
	
	public String DropTableUnripeString = "";
	private DropTable LeafDropTableUnripe = null;
	
	public String DropTableDestroyedString = "";
	private DropTable LeafDropTableDestroyed = null;

	private ItemKawaiiSeed Sapling;
	private Item Fruit;
	
	public int SeedsMysterySeedWeight = 0; 
	
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
   
	public void register(WorldGenKawaiiBaseWorldGen.WorldGen gen)
	{	
		if (!this.Enabled) return; 
		GameRegistry.registerBlock(this, this.getUnlocalizedName());
		
		String saplingName = name + ".sapling";
		Sapling = new ItemKawaiiSeed(saplingName, SaplingToolTip, this);
		Sapling.OreDict = SaplingOreDict;
		Sapling.MysterySeedWeight = SeedsMysterySeedWeight;
		Sapling.register();

		String fruitName = name + ".fruit";
		if (FruitEdible)
		{
			ItemKawaiiFood fruit = new ItemKawaiiFood(fruitName, FruitToolTip, FruitHunger, FruitSaturation, FruitPotionEffets);
			Fruit = fruit;
			fruit.OreDict = FruitOreDict;
			
			fruit.register();
		}
		else
		{
			ItemKawaiiIngredient fruit = new ItemKawaiiIngredient(fruitName, FruitToolTip);
			Fruit = fruit;
			fruit.OreDict = FruitOreDict;
			
			fruit.register();
		}
		
		if (gen.weight > 0)
		{
			gen.generator = new WorldGenKawaiiTree(this);
			ModWorldGen.WorldGen.generators.add(gen);
		}

		ModBlocks.AllTrees.add(this);		
	}
    
	public void registerDropTables()
	{
		LeafDropTableRipe = new DropTable(DropTableRipeString, Sapling, Fruit);
		LeafDropTableUnripe = new DropTable(DropTableUnripeString, Sapling, Fruit);
		LeafDropTableDestroyed = new DropTable(DropTableDestroyedString, Sapling, Fruit);
	}
	
	/////////////////////////////////////////////////////////////////////////////////////
    // Rendering

	public IIcon[] icons = null;
	
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
		icons = new IIcon[6];
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
	    	return ( TreeExternalFruit ? TreeState.FRUITRIPE : TreeState.FRUITLEAFRIPE);
	    
	    return TreeState.LEAF;
    }

    /////////////////////////////////////////////////////////////////////////////////////
    // IGrowable
    
	@Override // can bonemeal
	public boolean func_149851_a(World world, int x, int y, int z, boolean p_149851_5_) 
	{
		if (TreeExternalFruit && getState(world, x, y, z) == TreeState.LEAF)
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
		switch (getState(world, x, y, z))
		{
			case FRUITLEAF:
				decayLeaves(world, x, y, z);
			case FRUIT:
				this.doMatureFruit(world, x, y, z, rand);
				break;
			case LEAF:
				decayLeaves(world, x, y, z);
				doGrowFruit(world, x, y, z, rand);
				break;
			case SAPLING:
				updateTick(world, x, y, z, rand);
				break;
			default:
				decayLeaves(world, x, y, z);
				break;
		}
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
				ret.add(new ItemStack(Sapling));
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
    		doFruitDrop(world, x, y, z, world.rand);
    	
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
    			growSapling(world, x, y, z, rand);
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
    
    @Override
    public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face)
    {
        return 250;
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
            		if (SaplingSoilBlocks.contains(block) && (above == Material.air || above == Material.vine || above == Material.plants || above == Material.snow || above == Material.leaves)) 
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
    	
    	if (rand.nextDouble() < (GrowthMultiplierSapling * 0.25d / Math.pow(2.0d, (300.0d - value) / 75.0d)))
    		return !(new WorldGenKawaiiTree(this)).generate(world, rand, x, y, z);
    	
    	return true;
    }
    
    private boolean growFruit(World world, int x, int y, int z, Random rand)
    {
    	int leaf = 0;
    	int fruit = 0;
    	
    	// If we're to grow fruit beneath and there's no room to grow, abort
    	if (TreeExternalFruit && world.getBlock(x, y - 1, z) != Blocks.air) return true;

    	// Minimum light to grow
    	if (world.getBlockLightValue(x, y - (TreeExternalFruit ? 1 : 0), z) < SaplingMinimumLight) 
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
    	
		if (rand.nextInt((int)(35 - leaf + (6 * fruit) / GrowthMultiplierLeaf * 15.0f / world.getBlockLightValue(x, y - (TreeExternalFruit ? 1 : 0), z)) + 1) == 0)
		{
	    	if (TreeExternalFruit)
    			world.setBlock(x, y - 1, z, this, 5, 3);
	    	else
    			world.setBlockMetadataWithNotify(x, y, z, 2, 3);
    	}
    	
    	return true;
    }
    
    private void doGrowFruit(World world, int x, int y, int z, Random rand)
    {
    	if (world.getBlock(x, y, z) != this) return;
    	
    	if (!TreeExternalFruit)
			world.setBlockMetadataWithNotify(x, y, z, 2, 3);
    	else if (world.getBlock(x, y - 1, z) == Blocks.air)
			world.setBlock(x, y - 1, z, this, 5, 3);
    }
    
    private boolean matureFruit(World world, int x, int y, int z, Random rand)
    {
    	int light = world.getBlockLightValue(x, y, z);
    	if (light < SaplingMinimumLight || rand.nextInt((int)(4.0f / GrowthMultiplierFruit * 15.0f / light) + 1) != 0)
    		return true;
    	
    	doMatureFruit(world, x, y, z, rand);
        
    	return true;
    }
    
    private void doMatureFruit(World world, int x, int y, int z, Random rand)
    {
    	TreeState s = getState(world, x, y, z);
    	if (s != TreeState.FRUIT && s != TreeState.FRUITLEAF) return;
    	
    	int meta = world.getBlockMetadata(x, y, z);
    	if (meta < 8)
    		world.setBlockMetadataWithNotify(x, y, z, (meta == 4 ? 8 : meta + 1), 2);
    }
    
    private boolean gravityFruit(World world, int x, int y, int z, Random rand)
    {
    	if (rand.nextFloat() < TreeGravityChance)
    		doFruitDrop(world, x, y, z, rand);

    	return true;
    }
    
    private void doFruitDrop(World world, int x, int y, int z, Random rand)
    {
    	if (world.getBlockMetadata(x, y, z) != 8) return;
    	
    	int j;
    	for (j = y - 1; world.getBlock(x, j, z) == this; j--);

    	for (ItemStack item : LeafDropTableRipe.generateLoot(rand))
    		this.dropBlockAsItem(world, x, j, z, item);
		
		if (TreeExternalFruit)
			world.setBlock(x, y, z, Blocks.air);
		else
			world.setBlockMetadataWithNotify(x, y, z, 1, 3);
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
                		if (b == TreeTrunkBlock) return true;
                		TreeState s = getState(world, x, y, z);
                		if (b == this && (s == TreeState.LEAF || s == TreeState.FRUITLEAF || s == TreeState.FRUITLEAFRIPE))
                			leaf = true;
            		}
            		else if (b == TreeTrunkBlock) tree = true;
            		if (tree && leaf) return true;
            	}

    	// Do Decay Drop
    	world.setBlock(x, y, z, Blocks.air, 0, 3);
    	for (ItemStack item : LeafDropTableDestroyed.generateLoot(world.rand))
    		dropBlockAsItem(world, x, y, z, item);
    	
    	return false;
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // WAILA Mod Integration ( implements IWailaBlock )
    
	@Override public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) { return null; }
	@Override public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) { currenttip.add(SpecialChars.WHITE + StatCollector.translateToLocal(getUnlocalizedName() + ".name")); return currenttip; }
	@Override public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) { currenttip.add(SpecialChars.BLUE + SpecialChars.ITALIC + ConfigurationLoader.WAILAName); return currenttip; }

	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) 
	{
		switch(this.getState(accessor.getMetadata()))
		{
		case SAPLING:
			currenttip.add("Sapling");
			break;
		case LEAF:
			currenttip.add("Leaves");
			break;
		case FRUIT:
			currenttip.add("Fruit: " + ((accessor.getMetadata() - 4) * 25) + "% Grown");
			break;
		case FRUITLEAF:
			currenttip.add("Fruit: " + ((accessor.getMetadata() - 1) * 25) + "% Grown");
			break;
		case FRUITRIPE:
		case FRUITLEAFRIPE:
			currenttip.add("Fruit: Mature");
			break;
		}
		
		return currenttip;
	}
}
