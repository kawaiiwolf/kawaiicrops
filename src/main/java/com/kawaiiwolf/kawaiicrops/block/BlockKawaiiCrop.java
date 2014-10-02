package com.kawaiiwolf.kawaiicrops.block;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import com.kawaiiwolf.kawaiicrops.item.ItemKawaiiFood;
import com.kawaiiwolf.kawaiicrops.item.ItemKawaiiIngredient;
import com.kawaiiwolf.kawaiicrops.item.ItemKawaiiSeed;
import com.kawaiiwolf.kawaiicrops.item.ItemKawaiiSeedFood;
import com.kawaiiwolf.kawaiicrops.lib.Constants;
import com.kawaiiwolf.kawaiicrops.lib.DropTable;
import com.kawaiiwolf.kawaiicrops.renderer.RenderingHandlerKawaiiCropBlock;
import com.kawaiiwolf.kawaiicrops.tileentity.TileEntityKawaiiCrop;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockKawaiiCrop extends BlockCrops implements ITileEntityProvider {
	
	///////////////////////////////////////////////////////////////////////////////////////
	// Rendering Code
	
	public enum EnumRenderType { CROSS, HASH }
	
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
	public int UnripeMeta = 4;
	
	// Hardness of unripe block
	public float UnripeHardness = 0.5f;
	
	// Growth Multiplier
	public float GrowthMutliplier = 1.0f;
	
	// Effectiveness of bonemeal.
	public int BoneMealMin = 1;
	public int BoneMealMax = 1;

	// What block can this plant grow on. Do we want to allow more than one ?
	public Block CropGrowsOn = Blocks.farmland;

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
	
	// Produce details
	public boolean CropEnabled = true;
	public boolean CropEdible = true;
	public int CropHunger = 4;
	public float CropSaturation = 0.6f;
	public String CropToolTip = "";
	

	/* TODO: Drops

	water plant ?
	
	growth rate
	
	*/
	
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
		//super(Material.plants);
		
		this.Name = cropName;
		this.setBlockName(Constants.MOD_ID + "." + this.Name );
	}
	
	public void register()
	{
		if (!this.Enabled) return; 
		GameRegistry.registerBlock(this, this.getUnlocalizedName());
		
		if (this.SeedsEnabled) {
			String seedName = this.Name + ".seed";
			seed = (Item) (this.SeedsEdible 
					? new ItemKawaiiSeedFood(seedName, this.SeedsToolTip, this.SeedsHunger, this.SeedsSaturation, this, this.CropGrowsOn) 
					: new ItemKawaiiSeed(seedName, this.SeedsToolTip, this, this.CropGrowsOn));
			
			GameRegistry.registerItem(seed, Constants.MOD_ID + "." + seedName);
		}
		
		if (this.CropEnabled) {
			String cropName = this.Name + ".crop";
			crop = (Item) (this.CropEdible
					? new ItemKawaiiFood(cropName, this.CropToolTip, this.CropHunger, this.CropSaturation, this)
					: new ItemKawaiiIngredient(cropName, this.CropToolTip, this));
			
			GameRegistry.registerItem(crop, Constants.MOD_ID + "." + cropName);
		}
		
		ModBlocks.AllCrops.add(this);
	}
	
	public void registerDropTables() {
		dropTableRipe = new DropTable(DropTableRipeString, seed, crop);
		dropTableUnripe = new DropTable(DropTableUnripeString, seed, crop);		
	}
	
	///////////////////////////////////////////////////////////////////////////////////////
	// Rendering Code
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg){
		
		iconArray = new IIcon[8];
		
		for (int i = 0; i < 8; i++)
			iconArray[i] = reg.registerIcon(Constants.MOD_ID + ":" + this.Name + "_stage_" + i);
	}
	
	private boolean printed = false;
	
	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
		return iconArray[world.getBlockMetadata(x, y, z)];
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return iconArray[7];
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public int getRenderType()
    {
        return RenderingHandlerKawaiiCropBlock.instance.getRenderId();
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
		
		if (this.MultiHarvest && te != null && te instanceof TileEntityKawaiiCrop && meta >= 7)
			((TileEntityKawaiiCrop)te).arm(block, this.UnripeMeta);
		else
			world.removeTileEntity(x, y, z);
    }
	
	///////////////////////////////////////////////////////////////////////////////////////
	// Block Height Code
	
	private int getBaseY(World world, int x, int y, int z) {
		for (;world.getBlock(x, y - 1, z) == this; y--);
		return y;
	}
	
	private int getTopY(World world, int x, int y, int z) {
		for (;world.getBlock(x, y + 1, z) == this; y++);
		return y;
	}
	
	private int getCropTotalHeight(World world, int x, int y, int z) {
		return 1 + getTopY(world, x, y, z) - getBaseY(world, x, y, z);
	}
	
	private int getCropCurrentHeight(World world, int x, int y, int z) {
		return 1 + y - getBaseY(world, x, y, z);
	}
	
	private boolean isBase(World world, int x, int y, int z) {
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
    	
    	if (below == this.CropGrowsOn) {
    		if (below.getMaterial() != Material.water && below.getMaterial() != Material.lava)
    			return true;
    		// We want to make sure a liquid is still
    		if (world.getBlockMetadata(x, y, z) == 0)
    			return true;
    	}
    	if (this.MaxHeight > 1 && below == this)
    		return true;
    	
        return false;
    }
    
    @Override
    protected boolean canPlaceBlockOn(Block block)
    {
        return block == this.CropGrowsOn || (this.MaxHeight > 1 && block == this);
    }
    
	///////////////////////////////////////////////////////////////////////////////////////
	// Custom Growth Code    
    
    @Override
    public void updateTick(World world, int x, int y, int z, Random rand)
    {
        super.updateTick(world, x, y, z, rand);

        if (world.getBlockLightValue(x, y + 1, z) >= 9)
        {
            int meta = world.getBlockMetadata(x, y, z);

            if (meta < 7)
            {
                float f = this.vanillaGrowth(world, x, y, z);

                if (rand.nextInt((int)(25.0F / f) + 1) == 0)
                {
                    ++meta;
                    world.setBlockMetadataWithNotify(x, y, z, meta, 2);
                }
            }
        }
    }
    
    private int getNeighborCount(World world, int x, int y, int z, int distance, int height) {
    	if (distance < 0 || height < 0) return 0;
    	if (distance > 16) distance = 16;
    	if (height > 16) height = 3;
    	
    	int count = 0;
    	for (int i = x - distance; i <= x + distance; i ++ )
        	for (int j = y - height; j <= y + height; j ++ )
            	for (int k = z - distance; k <= z + distance; z ++ ) 
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

                if (world.getBlock(i, y - 1, j) == this.CropGrowsOn)
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
        int meta = world.getBlockMetadata(x, y, z) + BoneMealMin + ((int)(world.rand.nextFloat() * (1 + BoneMealMax - BoneMealMin)));
        world.setBlockMetadataWithNotify(x, y, z, (meta > 7 ? 7 : meta), 2);
    }
    
	
}
