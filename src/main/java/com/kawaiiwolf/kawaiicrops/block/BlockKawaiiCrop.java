package com.kawaiiwolf.kawaiicrops.block;

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
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockKawaiiCrop extends BlockCrops implements ITileEntityProvider {
	
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
	
	// Effectiveness of bonemeal.
	public int BoneMealMin = 1;
	public int BoneMealMax = 1;

	// What block can this plant grow on. Do we want to allow more than one ?
	public Block CropGrowsOn = Blocks.farmland;

	// Unparsed Drop Tables
	public String DropTableRipeString = "seed 1, seed 2 | crop";
	public String DropTableUnripeString = "seed";
	
	public boolean SeedsEnabled = true;
	public boolean SeedsEdible = false;
	public int SeedsHunger = 4;
	public float SeedsSaturation = 0.6f;
	public String SeedsToolTip = "";
	public int SeedsMysterySeedWeight = 0;
	
	public boolean CropEnabled = true;
	public boolean CropEdible = false;
	public int CropHunger = 4;
	public float CropSaturation = 0.6f;
	public String CropToolTip = "";
	

	/* TODO: Drops

	drop tables
	
	water plant ?
	
	growth rate
	
	*/
	
	private IIcon[] iconArray;
	private Item seed = null;
    private Item crop = null;
    private DropTable dropTableRipe = null;
    private DropTable dropTableUnripe = null;
	
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
		return new TileEntityKawaiiCrop();
	}
	
}
