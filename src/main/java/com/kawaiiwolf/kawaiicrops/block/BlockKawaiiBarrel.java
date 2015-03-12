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
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
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
	
	public String UnfinishedDropTableString = "";
	public DropTable UnfinishedDropTable = null;
	
	public String FinishedDropTableString = "";
	public DropTable FinishedDropTable = null;
	
	public String RuinedDropTableString = "";
	public DropTable RuinedDropTable = null;
	
	public String UnfinishedTooltip = "Status: Not Ready Yet";
	public String FinishedTooltip = "Status: Aged to perfection";
	public String RuinedTooltip = "Status: Something spoiled";
	
	public boolean ResetOnRuined = false;

	public BlockKawaiiBarrel(String name)
	{
		super(Material.wood);
		this.Name = name;
		this.setBlockName(Constants.MOD_ID + "." + this.Name + ".barrel");
		this.setBlockTextureName(Constants.MOD_ID + ":" + this.Name + ".barrel");
		
		setTickRandomly(true);
		setCreativeTab(ModItems.KawaiiCreativeTab);
		setHardness(1);
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
		GameRegistry.registerBlock(this, Name);
	}
	
	public void registerDropTables()
	{
		Item i = Item.getItemFromBlock(this);
		
		RequiredBlocks = NamespaceHelper.getBlocksByName(RequiredBlockString);
		ForbiddenBlocks = NamespaceHelper.getBlocksByName(ForbiddenBlockString);
		
		UnfinishedDropTable = new DropTable(UnfinishedDropTableString, new ItemStack(i), null);
		FinishedDropTable = new DropTable(FinishedDropTableString, new ItemStack(i), null);
		RuinedDropTable = new DropTable(RuinedDropTableString, new ItemStack(i), null);
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
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitx, float htiy, float hitz)
    {
		if (player.getCurrentEquippedItem().getItem() == ModItems.MagicSpoon)
		{
			updateTick(world, x, y, z, world.rand);
			return true;
		}
		return false;
    }
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random rand)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof TileEntityKawaiiBarrel && !world.isRemote)
		{
			TileEntityKawaiiBarrel t = (TileEntityKawaiiBarrel)te;
			
			if(t.isRuined) return;
			if(t.cookTime >= RuinedTime && RuinedTime != 0)
				t.isRuined = true;
			else
				t.cookTime++;
			
			boolean approved = (RequiredBlocks.size() == 0 && !t.isRuined);
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
								if (ResetOnRuined)
									t.cookTime = 0;
								else
									t.isRuined = true;
								world.markBlockForUpdate(x, y, z);
								return;
							}
						}
			}
			if (!approved)
				t.isRuined = true;
			
			if (t.isRuined && ResetOnRuined)
			{
				t.isRuined = false;
				t.cookTime = 0;
			}
			
			world.markBlockForUpdate(x, y, z);
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
				ret.addAll(UnfinishedDropTable.generateLoot(world.rand));
			else
				ret.addAll(FinishedDropTable.generateLoot(world.rand));
		}
        return ret;
    }

    // Block requires tile entity to still exist for proper drops.
    @Override
    public void onBlockPreDestroy(World world, int x, int y, int z, int meta) 
    {
    	ArrayList<ItemStack> ret = getDrops(world, x, y, z, meta, 0);
    	for ( ItemStack item : ret)
    		dropBlockAsItem(world, x, y, z, item);
    }
    
    // Sanity check
    @Override
    public void harvestBlock(World p_149636_1_, EntityPlayer p_149636_2_, int p_149636_3_, int p_149636_4_, int p_149636_5_, int p_149636_6_) {}
    
	public IIcon labelUnfinished;
	public IIcon labelFinished;
	public IIcon labelRuined;
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg)
	{
		super.registerBlockIcons(reg);
		
		labelUnfinished = reg.registerIcon(Constants.MOD_ID + ":" + Name + ".barrel_label_unfinished");
		labelFinished = reg.registerIcon(Constants.MOD_ID + ":" + Name + ".barrel_label_finished");
		labelRuined = reg.registerIcon(Constants.MOD_ID + ":" + Name + ".barrel_label_ruined");
	}
	
	public enum BarrelModel { BARREL, CRATE };
	public BarrelModel model = BarrelModel.BARREL;

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // WAILA Mod Integration

	@Override
	public ItemStack getDisplayStack(World world, int x, int y, int z, int meta, TileEntity te) {
		return null;
	}

	@Override
	public String getBody(World world, int x, int y, int z, int meta, TileEntity te) 
	{
		if (te instanceof TileEntityKawaiiBarrel)
		{
			TileEntityKawaiiBarrel t = (TileEntityKawaiiBarrel)te;
			
			if (t.isRuined)
				return RuinedTooltip;
			else if (t.cookTime < this.FinishedTime)
				return UnfinishedTooltip;
			else
				return FinishedTooltip;
		}
		return null;
	}
}
