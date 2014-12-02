package com.kawaiiwolf.kawaiicrops.block;

import java.util.List;

import mcp.mobius.waila.api.IWailaBlock;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.SpecialChars;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.kawaiiwolf.kawaiicrops.lib.Constants;
import com.kawaiiwolf.kawaiicrops.tileentity.TileEntityKawaiiCuttingBoard;

public class BlockKawaiiCuttingBoard extends BlockKawaiiCookingBlock implements IWailaBlock
{
	protected BlockKawaiiCuttingBoard() 
	{
		super(Material.wood, "cuttingboard", false);
		maxY = 1.0d / 16.0d;
		minX = minZ = 1.0d / 16.0d;
		maxX = maxZ = 1.0d - minX;
		
		this.setBlockTextureName(Constants.MOD_ID + ":cuttingboard");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityKawaiiCuttingBoard();
	}
}
