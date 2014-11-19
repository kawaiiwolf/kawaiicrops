package com.kawaiiwolf.kawaiicrops.block;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.kawaiiwolf.kawaiicrops.item.ItemKawaiiFood;
import com.kawaiiwolf.kawaiicrops.item.ItemKawaiiIngredient;
import com.kawaiiwolf.kawaiicrops.item.ItemKawaiiSeed;
import com.kawaiiwolf.kawaiicrops.item.ItemKawaiiSeedFood;
import com.kawaiiwolf.kawaiicrops.lib.ConfigurationLoader;
import com.kawaiiwolf.kawaiicrops.lib.Constants;
import com.kawaiiwolf.kawaiicrops.lib.DropTable;
import com.kawaiiwolf.kawaiicrops.lib.NamespaceHelper;
import com.kawaiiwolf.kawaiicrops.lib.PotionEffectHelper;
import com.kawaiiwolf.kawaiicrops.renderer.RenderingHandlerKawaiiCropBlocks;
import com.kawaiiwolf.kawaiicrops.tileentity.TileEntityKawaiiCrop;
import com.kawaiiwolf.kawaiicrops.world.ModWorldGen;
import com.kawaiiwolf.kawaiicrops.world.WorldGenKawaiiBaseWorldGen;
import com.kawaiiwolf.kawaiicrops.world.WorldGenKawaiiCrop;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mcp.mobius.waila.api.IWailaBlock;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.SpecialChars;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockKawaiiCrop extends BlockCrops implements ITileEntityProvider, IWailaBlock {
	
	///////////////////////////////////////////////////////////////////////////////////////
	// Rendering Code
	
	public enum EnumRenderType { CROSS, HASH, BLOCK }
	
	// Crop Name
	public String Name = "";
	
	// Don't register if false
	public boolean Enabled = false;
	
	// Number of crop stages
	public int CropStages = 8;

	// Render Type, # or X
	public EnumRenderType RenderType = EnumRenderType.HASH;
	
	// Max block height
	public int MaxHeight = 1;
	
	// Prevent lower blocks from hitting Meta 8 before the top
	public boolean MaxHeightRequiredToRipen = true;
	
	// Restore blocks to earlier Meta state on harvest
	public boolean MultiHarvest = false;
	
	// Meta to restore to if MultiHarvest is set
	public int UnripeStage = 4;
	
	// Hardness of unripe block
	public float UnripeHardness = 0.5f;
	
	// Growth Multiplier
	public float GrowthMutliplier = 1.0f;
	
	// Effectiveness of bonemeal.
	public int BoneMealMin = 2;
	public int BoneMealMax = 4;
	
	// Required Light levels
	public int LightLevelMin = 9;
	public int LightLevelMax = 15;

	// What block can this plant grow on. Do we want to allow more than one ?
	public HashSet<Block> CropGrowsOn = new HashSet<Block>();

	// Unparsed Drop Tables
	public String DropTableRipeString = "seed 1, seed 2 | crop";
	public String DropTableUnripeString = "seed";
	
	// Seeds details
	public boolean SeedsEnabled = true;
	public boolean SeedsEdible = false;
	public int SeedsHunger = 4;
	public float SeedsSaturation = 0.6f;
	public String SeedsToolTip = "";
	public int SeedsMysterySeedWeight = 0;
	public PotionEffectHelper SeedsPotionEffects = null;
	public String SeedOreDict = "";
	
	// Produce details
	public boolean CropEnabled = true;
	public boolean CropEdible = true;
	public int CropHunger = 4;
	public float CropSaturation = 0.6f;
	public String CropToolTip = "";
	public PotionEffectHelper CropPotionEffects = null;
	public String CropOreDict = "";

	
	private IIcon[] iconArray;
	private Item seed = null;
    private Item crop = null;
    private DropTable dropTableRipe = null;
    private DropTable dropTableUnripe = null;
	
	///////////////////////////////////////////////////////////////////////////////////////
	// Configuration
    
	public BlockKawaiiCrop(String cropName)
	{
		super();
		
		this.Name = cropName;
		this.setBlockName(Constants.MOD_ID + "." + this.Name );
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}
	
	public void register(WorldGenKawaiiBaseWorldGen.WorldGen gen)
	{		
		if (!this.Enabled) return; 
		GameRegistry.registerBlock(this, this.getUnlocalizedName());
		
		if (this.SeedsEnabled) {
			String seedName = this.Name + ".seed";
			
			boolean water = false;
			for (Block b : CropGrowsOn)
				if (b.getMaterial() == Material.water || b.getMaterial() == Material.lava)
					if (SeedsEdible) 
						water = true;
			
			if (SeedsEdible)
			{
				ItemKawaiiSeedFood seed = new ItemKawaiiSeedFood(seedName, this.SeedsToolTip, this.SeedsHunger, this.SeedsSaturation, this);
				seed.OreDict = SeedOreDict;
				seed.WaterPlant = water;
				seed.MysterySeedWeight = SeedsMysterySeedWeight;
				seed.register();
				this.seed = seed;
			} 
			else
			{
				ItemKawaiiSeed seed = new ItemKawaiiSeed(seedName, this.SeedsToolTip, this);
				seed.OreDict = SeedOreDict;
				seed.WaterPlant = water;
				seed.MysterySeedWeight = SeedsMysterySeedWeight;
				seed.register();
				this.seed = seed;
			}
		}

		if (CropEnabled)
		{
			String cropName = this.Name + ".crop";

			if (CropEdible)
			{
				ItemKawaiiFood crop = new ItemKawaiiFood(cropName, this.CropToolTip, this.CropHunger, this.CropSaturation, this.CropPotionEffects);
				crop.OreDict = CropOreDict;
				
				crop.register();
				this.crop = crop;
			}
			else
			{
				ItemKawaiiIngredient crop = new ItemKawaiiIngredient(cropName, this.CropToolTip);
				crop.OreDict = CropOreDict;
				
				crop.register();
				this.crop = crop;
			}
		}
		
		if (gen.weight > 0)
		{
			gen.generator = new WorldGenKawaiiCrop(this);
			ModWorldGen.WorldGen.generators.add(gen);
		}
		
		ModBlocks.AllCrops.add(this);
	}
	
	public void registerDropTables() {
		dropTableRipe = new DropTable(DropTableRipeString, seed, crop);
		dropTableUnripe = new DropTable(DropTableUnripeString, seed, crop);		
	}
	
    @Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int x, int y, int z)
    {
        return seed;
    }
	
	///////////////////////////////////////////////////////////////////////////////////////
	// Rendering Code
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg){
		
		iconArray = new IIcon[this.CropStages * (MaxHeightRequiredToRipen ? MaxHeight : 1)];
		
		for (int i = 0; i < (MaxHeightRequiredToRipen ? MaxHeight : 1); i++)
			for (int j = 0; j < CropStages; j++)
				iconArray[i * CropStages + j] = reg.registerIcon(Constants.MOD_ID + ":" + Name + "_stage_"  + (MaxHeightRequiredToRipen && MaxHeight > 1? i + "_" : "") + j);
	}
		
	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
		int height = MaxHeightRequiredToRipen ? (y - getBaseY(world, x, y, z)) : 0;
		int stage = MathHelper.clamp_int(world.getBlockMetadata(x, y, z) + CropStages - 8, 0, this.CropStages - 1);
		return iconArray[height * CropStages + stage];
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return iconArray[iconArray.length - 1];
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public int getRenderType()
    {
        return RenderingHandlerKawaiiCropBlocks.instance.getRenderId();
    }

	@Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) 
	{
		int top = this.getTopY(world, x, y, z);
		float f = 0.0625F;
		
		this.setBlockBounds(0.0F + f, 0.0F, 0.0F + f, 1.0F - f, (y == top ? 0.25F : 1.0F), 1.0F - f);
	}
	
	///////////////////////////////////////////////////////////////////////////////////////
	// Tile Entity Code
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return (this.MultiHarvest ? new TileEntityKawaiiCrop() : null);
	}
	
	@Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta)
    {
		// No SUPER. Prevent base BLock class from destroying the tile entity.
		TileEntity te = world.getTileEntity(x, y, z);
		
		if (te != null)
		{
			if (this.MultiHarvest && te != null && te instanceof TileEntityKawaiiCrop && meta == 7)
			{
				((TileEntityKawaiiCrop)te).arm(block, 8 - CropStages + UnripeStage);
			}
			else 
				world.removeTileEntity(x, y, z);
		}
		
		for (int i = -1; i <= 1; i += 2)
			if (world.getBlock(x, y + i, z) == this && (!MultiHarvest || meta < 7)) 
			{
				if (!MaxHeightRequiredToRipen)
					dropBlockAsItem(world, x, y + i, z, world.getBlockMetadata(x, y + i, z), 0);
				world.setBlock(x, y + i, z, getBlockById(0), 0, 2);
	        }
    }
	
	///////////////////////////////////////////////////////////////////////////////////////
	// Block Height Code
	
	private int getBaseY(IBlockAccess world, int x, int y, int z) {
		for (;world.getBlock(x, y - 1, z) == this; y--);
		return y;
	}
	
	private int getTopY(IBlockAccess world, int x, int y, int z) {
		for (;world.getBlock(x, y + 1, z) == this; y++);
		return y;
	}
	
	private int getCropTotalHeight(IBlockAccess world, int x, int y, int z) {
		return 1 + getTopY(world, x, y, z) - getBaseY(world, x, y, z);
	}
	
	private int getCropCurrentHeight(IBlockAccess world, int x, int y, int z) {
		return 1 + y - getBaseY(world, x, y, z);
	}
	
	private boolean isBase(IBlockAccess world, int x, int y, int z) {
		return (y == getBaseY(world, x, y, z)); 
	}

	///////////////////////////////////////////////////////////////////////////////////////
	// Item Drop Code
	
	@Override
    public int quantityDropped(Random r)
    {
        return 0;
    }
	
    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
    	if (world.isRemote) return null;
    	
        if (metadata >= 7)
        	return this.dropTableRipe.generateLoot(world.rand);
        else
        	return this.dropTableUnripe.generateLoot(world.rand);
    }
    
    @Override
    protected Item func_149866_i()
    {
        return seed;
    }

    @Override
    protected Item func_149865_P()
    {
        return crop;
    }
    
	///////////////////////////////////////////////////////////////////////////////////////
	// Hardness Tweak
    
    @Override
    public float getBlockHardness(World world, int x, int y, int z) {
    	return (world.getBlockMetadata(x, y, z) >=7 ? 0.0f : this.UnripeHardness);
    }
    
	///////////////////////////////////////////////////////////////////////////////////////
	// Custom Soil
    
    @Override
    public boolean canBlockStay(World world, int x, int y, int z)
    {
    	Block below = world.getBlock(x, y - 1, z);
    	
    	if (CropGrowsOn.contains(below)) {
    		if (below.getMaterial() != Material.water && below.getMaterial() != Material.lava)
    			return true;
    		// We want to make sure a liquid is still
    		if (world.getBlockMetadata(x, y, z) == 0)
    			return true;
    	}
    	if (this.MaxHeight > 1 && below == this)
    		return true;
    	
    	TileEntity te = world.getTileEntity(x, y - 1, z);
    	if (te != null && te instanceof TileEntityKawaiiCrop && ((TileEntityKawaiiCrop)te).isArmed())
    		return true;
    	
        return false;
    }
    
    @Override
    protected boolean canPlaceBlockOn(Block block)
    {
        return CropGrowsOn.contains(block) || (this.MaxHeight > 1 && block == this);
    }
    
	///////////////////////////////////////////////////////////////////////////////////////
	// Custom Growth Code
    
    @Override
    public void onBlockAdded(World world, int x, int y, int z)
    {
        super.onBlockAdded(world, x, y, z);
        
        if (world.getBlockMetadata(x, y, z) < 8 - this.CropStages)
        	world.setBlockMetadataWithNotify(x, y, z, 8 - this.CropStages, 2);
    }
    
    private void growPlant(World world, int x, int y, int z) 
    {
    	if (world.getBlock(x, y, z) != this) return;
    	int meta = world.getBlockMetadata(x, y, z);
    	
    	if (this.MaxHeight == 1) 
    	{
	    	if (meta < 7)
	    		world.setBlockMetadataWithNotify(x, y, z, meta + 1, 2);
    	} 
    	else 
    	{
    		int top = this.getTopY(world, x, y, z);
    		int base = this.getBaseY(world, x, y, z);
    		
    		if (this.MaxHeightRequiredToRipen)
    		{
    			if (world.getBlock(x, y, z) == world.getBlock(x, top, z) && (1 + top - base) == this.MaxHeight && world.getBlockMetadata(x, top, z) >= (this.UnripeStage + 8 - this.CropStages))
    			{
    				if (meta < 7)
	    				for (int i = base; i <= top; i++)
	    					world.setBlockMetadataWithNotify(x, i, z, meta + 1, 2);
    			}
    			else if (meta < (this.UnripeStage + 8 - this.CropStages))
    				world.setBlockMetadataWithNotify(x, y, z, meta + 1, 2);
    			else
    				growPlant(world, x, y + 1, z);
    		}
    		else if (meta < 7)
   	    		world.setBlockMetadataWithNotify(x, y, z, meta + 1, 2);

    		// Grow block above
    		if (meta >= (this.UnripeStage + 8 - this.CropStages) && world.isAirBlock(x, y + 1, z) && (top - base + 1) < this.MaxHeight)
    			world.setBlock(x, y + 1, z, this);
    	}
    }    
    
    @Override
    public void updateTick(World world, int x, int y, int z, Random rand)
    {
    	if(world.isRemote) return;
    	
    	this.checkAndDropBlock(world, x, y, z);
    	int lightLevel = world.getBlockLightValue(x, y + 1, z);
    	
    	if (	(getTopY(world, x, y, z) == y || !MaxHeightRequiredToRipen) &&
    			(lightLevel >= LightLevelMin && lightLevel <= LightLevelMax) &&
    			(rand.nextInt((int)(25.0F / vanillaGrowth(world, x, y, z)) + 1) == 0))
    		growPlant(world, x, y, z);
    }
    
    private int getNeighborCount(World world, int x, int y, int z, int distance, int height) 
    {
    	if (distance < 0 || height < 0) return 0;
    	if (distance > 16) distance = 16;
    	if (height > 16) height = 3;
    	
    	int count = 0;
    	for (int i = x - distance; i <= x + distance; i ++ )
        	for (int j = y - height; j <= y + height; j ++ )
            	for (int k = z - distance; k <= z + distance; k ++ ) 
            		if ((i != x || j != y || k != z) && world.getBlock(x, y, z) == world.getBlock(i, j, k))
            			count++;
    	return count;
    }
    
    private float vanillaGrowth(World world, int x, int y, int z)
    {
        float ret = 1.0F;

        for (int i = x - 1; i <= x + 1; ++i)
        {
            for (int j = z - 1; j <= z + 1; ++j)
            {
                float tmp = 0.0F;

                if (this.CropGrowsOn.contains(world.getBlock(i, y - 1, j)))
                {
                    tmp = 1.0F;
                    if (world.getBlock(i, y - 1, j).isFertile(world, i, y - 1, j))
                        tmp = 3.0F;
                }

                if (i != x || j != z)
                    tmp /= 4.0F;

                ret += tmp;
            }
        }

        if (this.getNeighborCount(world, x, y, z, 1, 0) > 0)
            ret /= 2.0F;

        return ret * this.GrowthMutliplier;
    }
    
    // Bonemeal Fertilize Call
    @Override
    public void func_149863_m(World world, int x, int y, int z)
    {
    	if (world.isRemote) return;
        int growth = BoneMealMin + ((int)(world.rand.nextFloat() * (1 + BoneMealMax - BoneMealMin)));
        
        for (int i = 0; i < growth; i++)
        	growPlant(world, x, y, z);
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // WAILA Mod Integration ( implements IWailaBlock )
    
	@Override public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) { return null; }
	@Override public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) { currenttip.add(SpecialChars.WHITE + StatCollector.translateToLocal(getUnlocalizedName() + ".name")); return currenttip; }
	@Override public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) { currenttip.add(SpecialChars.BLUE + SpecialChars.ITALIC + ConfigurationLoader.WAILAName); return currenttip; }

	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) 
	{
		World world = accessor.getWorld();
		int x = accessor.getPosition().blockX, y = accessor.getPosition().blockY, z = accessor.getPosition().blockZ;

		if (MaxHeight == 1 || !MaxHeightRequiredToRipen)
		{
			if (accessor.getMetadata() == 7)
				currenttip.add("Crop: Mature");
			else
				currenttip.add("Crop: " + ((accessor.getMetadata() + CropStages - 7) * 100 / CropStages) + "% Grown");
		}
		else
		{
			int max = (CropStages - UnripeStage /*+ 1 Possibly ?*/) * (MaxHeight - 1) + CropStages;
			
			// Multi-tier % calculation !
		}

		return currenttip;
	}
	
}
