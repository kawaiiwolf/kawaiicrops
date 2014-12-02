package com.kawaiiwolf.kawaiicrops.block;

import java.util.List;
import java.util.Random;

import com.kawaiiwolf.kawaiicrops.item.ModItems;
import com.kawaiiwolf.kawaiicrops.lib.ConfigurationLoader;
import com.kawaiiwolf.kawaiicrops.lib.Constants;
import com.kawaiiwolf.kawaiicrops.tileentity.TileEntityKawaiiCookingBlock;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mcp.mobius.waila.api.IWailaBlock;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.SpecialChars;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public abstract class BlockKawaiiCookingBlock extends BlockContainer implements IWailaBlock {

	public String Name = "";
	public IIcon burntTexture = null;
	
	protected BlockKawaiiCookingBlock(Material material, String name, boolean randomTick) 
	{
		super(material);
		this.Name = name;
		setBlockName(Constants.MOD_ID + "." + name);
		setHardness(1.0f);
		setTickRandomly(randomTick);
		
		setCreativeTab(ModItems.KawaiiCreativeTab);
	}
	
	@Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitx, float htiy, float hitz)
    {
		TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof TileEntityKawaiiCookingBlock)
			return ((TileEntityKawaiiCookingBlock)te).onBlockActivated(world, x, y, z, player);
        return false;
    }
	
    @Override
    public void onBlockPreDestroy(World world, int x, int y, int z, int meta) 
    {
    	super.onBlockPreDestroy(world, x, y, z, meta);
    	TileEntity te = world.getTileEntity(x, y, z);
    	if (te instanceof TileEntityKawaiiCookingBlock)
    		((TileEntityKawaiiCookingBlock)te).dropAllItems(world, x, y, z);
    }

    @Override
    public boolean canHarvestBlock(EntityPlayer player, int meta)
    {
    	return true;
    }
	
	@Override
	public boolean isOpaqueCube() 
	{
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() 
	{
		return false;
	}
	
	@Override
	public int getRenderType() 
	{
		return -1;
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack item) 
	{
		Vec3 look = entity.getLookVec();
		int meta = 0;
		if (Math.abs(look.xCoord) >= Math.abs(look.zCoord))
			meta = (look.xCoord > 0.0d ? 1 : 3);
		else
			meta = (look.zCoord > 0.0d ? 0 : 2);
		world.setBlockMetadataWithNotify(x, y, z, meta, 2);
	}
	
	@Override
	public void dropBlockAsItem(World world, int x, int y, int z, ItemStack item)
	{
		super.dropBlockAsItem(world, x, y, z, item);
	}
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random rand)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof TileEntityKawaiiCookingBlock && !world.isRemote)
			((TileEntityKawaiiCookingBlock) te).onRandomTick(world, x, y, z, rand);
	}
	
    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) 
    {
		TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof TileEntityKawaiiCookingBlock)
			((TileEntityKawaiiCookingBlock) te).onRandomDisplayTick(world, x, y, z, rand);   	
    }

	private boolean isRegistered = false;
	public void register()
	{
		if (isRegistered) return;
		isRegistered = true;
		
		GameRegistry.registerBlock(this, Constants.MOD_ID + "." + Name);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register)
    {
		super.registerBlockIcons(register);
		this.burntTexture = register.registerIcon(Constants.MOD_ID + ":ruined");
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // WAILA Mod Integration ( implements IWailaBlock )
    
	@Override public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) { return null; }
	@Override public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) { currenttip.add(SpecialChars.WHITE + StatCollector.translateToLocal(getUnlocalizedName() + ".name")); return currenttip; }
	@Override public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) { currenttip.add(SpecialChars.BLUE + SpecialChars.ITALIC + ConfigurationLoader.WAILAName); return currenttip; }

	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) 
	{
		if (accessor != null && accessor.getTileEntity() != null && accessor.getTileEntity() instanceof TileEntityKawaiiCookingBlock)
		{
			String s = ((TileEntityKawaiiCookingBlock)accessor.getTileEntity()).getWAILATip();
			if ( s != null && s.length() > 0)
				currenttip.add(s);
			//currenttip.add("DEBUG: Meta is " + accessor.getMetadata());
		}
		return currenttip;
	}
}
