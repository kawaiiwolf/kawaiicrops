package com.kawaiiwolf.kawaiicrops.block;

import java.util.List;

import com.google.common.collect.Lists;
import com.kawaiiwolf.kawaiicrops.item.ItemKawaiiCake;
import com.kawaiiwolf.kawaiicrops.lib.ConfigurationLoader;
import com.kawaiiwolf.kawaiicrops.lib.Constants;
import com.kawaiiwolf.kawaiicrops.lib.NamespaceHelper;
import com.kawaiiwolf.kawaiicrops.lib.PotionEffectHelper;
import com.kawaiiwolf.kawaiicrops.waila.IWailaTooltip;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mcp.mobius.waila.api.IWailaBlock;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.SpecialChars;
import net.minecraft.block.BlockCake;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class BlockKawaiiCake extends BlockCake implements IWailaTooltip {

	public String Name = "";
	public int Hunger = 2;
	public float Saturation = 0.1F;
	public boolean Enabled = false;
	public String ToolTipText = "";
	public PotionEffectHelper Potion = null;
	public String OreDict = "";
	public String SliceItemString = "";
	
	private ItemKawaiiCake cake = null;
	private ItemStack slice = null;
	
	public BlockKawaiiCake(String name)
	{
		super();
		this.Name = name;
		this.setBlockName(Constants.MOD_ID + "." + this.Name + ".cake");
		this.setBlockTextureName(Constants.MOD_ID + ":" + this.Name + ".cake");
	}
	
	public void register() 
	{
		if (!this.Enabled) return; 
		GameRegistry.registerBlock(this, this.getUnlocalizedName());
		
		cake = new ItemKawaiiCake(this);
		cake.OreDict = OreDict;
		
		GameRegistry.registerItem(cake, Constants.MOD_ID + "." + this.Name + ".cake");
		ModBlocks.AllCakes.add(this);
	}
	
	public void registerCakeSlice()
	{
		if (SliceItemString.length() > 0)
			slice = NamespaceHelper.getItemByName(SliceItemString);
	}
	
	@Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xHit, float yHit, float zHit)
    {
		onBlockClicked(world, x, y, z, player);
        return true;
    }

    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player)
    {
    	if (player.isSneaking() && world.getBlockMetadata(x, y, z) == 0)
    	{
    		player.inventory.addItemStackToInventory(new ItemStack(cake));
    		world.setBlockToAir(x, y, z);
    	}
    	else if (slice == null)
    		onEat(world, x, y, z, player);
    	else
    	{
    		if(player.inventory.addItemStackToInventory(slice.copy()))
    			removeSlice(world, x, y, z);
    	}
    }

    private void onEat(World world, int x, int y, int z, EntityPlayer player)
    {
        if (player.canEat(false))
        {
            player.getFoodStats().addStats(this.Hunger, this.Saturation);

            if (!world.isRemote && this.Potion != null) {
            	for (com.kawaiiwolf.kawaiicrops.lib.PotionEffectHelper.Potion p : this.Potion.Effects)
            	{
            		if (world.rand.nextFloat() < p.Chance)
                    	player.addPotionEffect(p.getPotionEffect());
            	}
            }
            
            removeSlice(world, x, y, z);
        }
    }
    
    private void removeSlice(World world, int x, int y, int z)
    {
        int l = world.getBlockMetadata(x, y, z) + 1;

        if (l >= 6)
        {
            world.setBlockToAir(x, y, z);
        }
        else
        {
            world.setBlockMetadataWithNotify(x, y, z, l, 2);
        }
    }
	
    @Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int x, int y, int z)
    {
        return cake;
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // WAILA Mod Integration
    
	@Override
	public ItemStack getDisplayStack(World world, int x, int y, int z, int meta, TileEntity te) {
		return null;
	}

	@Override
	public String getBody(World world, int x, int y, int z, int meta, TileEntity te) 
	{
		return "Slices remaining: " + (6 - meta);
	}
    
}
