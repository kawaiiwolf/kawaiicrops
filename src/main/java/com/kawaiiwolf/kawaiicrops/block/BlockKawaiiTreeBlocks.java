package com.kawaiiwolf.kawaiicrops.block;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import com.kawaiiwolf.kawaiicrops.item.ItemKawaiiFood;
import com.kawaiiwolf.kawaiicrops.item.ItemKawaiiIngredient;
import com.kawaiiwolf.kawaiicrops.item.ItemKawaiiSeed;
import com.kawaiiwolf.kawaiicrops.lib.Constants;
import com.kawaiiwolf.kawaiicrops.lib.DropTable;
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
    public void updateTick(World world, int x, int y, int z, Random rand)
    {
    	if(world.isRemote) return;
    }
    

	
}
