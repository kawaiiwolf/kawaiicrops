package com.kawaiiwolf.kawaiicrops.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.kawaiiwolf.kawaiicrops.item.ModItems;
import com.kawaiiwolf.kawaiicrops.lib.ConfigurationLoader;
import com.kawaiiwolf.kawaiicrops.lib.Constants;
import com.kawaiiwolf.kawaiicrops.lib.DropTable;
import com.kawaiiwolf.kawaiicrops.lib.NamespaceHelper;
import com.kawaiiwolf.kawaiicrops.tileentity.TileEntityKawaiiBarrel;
import com.kawaiiwolf.kawaiicrops.waila.IWailaTooltip;

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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockKawaiiBarrel extends BlockContainer implements IWailaTooltip
{
	
	public String Name = "";
	public int FinishedTime = 0;
	public int RuinedTime = 0;
	public boolean Enabled = false;
	public int SearchRadius = 2;
	
	public String RequiredBlockString = "";
	public ArrayList<Block> RequiredBlocks = null;
	
	public String ForbiddenBlockString = "";
	public ArrayList<Block> ForbiddenBlocks = null;
	
	public String UnripeDropTableString = "";
	public DropTable UnripeDropTable = null;
	
	public String RipeDropTableString = "";
	public DropTable RipeDropTable = null;
	
	public String RuinedDropTableString = "";
	public DropTable RuinedDropTable = null;

	public BlockKawaiiBarrel(String name)
	{
		super(Material.wood);
		this.Name = name;
		this.setBlockName(Constants.MOD_ID + "." + this.Name + ".barrel");
		this.setBlockTextureName(Constants.MOD_ID + ":" + this.Name + ".barrel");
		
		setTickRandomly(true);
		setCreativeTab(ModItems.KawaiiCreativeTab);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) 
	{
		return new TileEntityKawaiiBarrel();
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

	private boolean isRegistered = false;
	public void register()
	{
		if (isRegistered || !Enabled) return;
		isRegistered = true;
		
		ModBlocks.AllBarrels.add(this);
		GameRegistry.registerBlock(this, Constants.MOD_ID + "." + Name);
	}
	
	public void registerDropTables()
	{
		Item i = Item.getItemFromBlock(this);
		
		RequiredBlocks = NamespaceHelper.getBlocksByName(RequiredBlockString);
		ForbiddenBlocks = NamespaceHelper.getBlocksByName(ForbiddenBlockString);
		
		UnripeDropTable = new DropTable(UnripeDropTableString, new ItemStack(i), null);
		RipeDropTable = new DropTable(RipeDropTableString, new ItemStack(i), null);
		RuinedDropTable = new DropTable(RuinedDropTableString, new ItemStack(i), null);
	}
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random rand)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof TileEntityKawaiiBarrel && !world.isRemote)
		{
			TileEntityKawaiiBarrel t = (TileEntityKawaiiBarrel)te;
			
			if(t.isRuined) return;
			if(t.cookTime > RuinedTime && RuinedTime != 0)
				t.isRuined = true;
			else
				t.cookTime++;
			
			boolean approved = (RequiredBlocks.size() == 0);
			boolean denyCheck = (ForbiddenBlocks.size() > 0);
			if (approved && !denyCheck)
			{
				for (int i = x - SearchRadius; i <= x + SearchRadius; i++)
					for (int j = y - SearchRadius; j <= y + SearchRadius; j++)
						for (int k = z - SearchRadius; j <= z + SearchRadius; k++)
						{
							if ((i == x && j == y && k == z) || j <= 0) continue;
							if (!approved && RequiredBlocks.contains(world.getBlock(i, j, k)))
								approved = true;
							if (denyCheck && ForbiddenBlocks.contains(world.getBlock(i, j, k)))
							{
								t.isRuined = true;
								return;
							}
						}
			}
			if (!approved)
				t.isRuined = true;
		}
	}
	
    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

		TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof TileEntityKawaiiBarrel && !world.isRemote)
		{
			TileEntityKawaiiBarrel t = (TileEntityKawaiiBarrel)te;
			
			if (t.isRuined)
				ret.addAll(RuinedDropTable.generateLoot(world.rand));
			else if (t.cookTime < this.FinishedTime)
				ret.addAll(UnripeDropTable.generateLoot(world.rand));
			else
				ret.addAll(RipeDropTable.generateLoot(world.rand));
		}
        
        return ret;
    }
    
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg)
	{
		super.registerBlockIcons(reg);
		
		// Gotta load the labels !
	}

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // WAILA Mod Integration

	@Override
	public ItemStack getDisplayStack(World world, int x, int y, int z, int meta, TileEntity te) {
		return null;
	}

	@Override
	public List<String> getBody(World world, int x, int y, int z, int meta, TileEntity te) {
		// TODO Auto-generated method stub
		return null;
	}
}
