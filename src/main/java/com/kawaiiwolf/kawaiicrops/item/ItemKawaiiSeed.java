package com.kawaiiwolf.kawaiicrops.item;

import java.util.List;

import com.kawaiiwolf.kawaiicrops.block.BlockKawaiiCrop;
import com.kawaiiwolf.kawaiicrops.lib.Constants;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemKawaiiSeed extends ItemSeeds {

	public String ToolTipText = "";
	
	private String name = "";
	private BlockKawaiiCrop plant = null;
	private Block soil = null;
	
	public ItemKawaiiSeed(String name, String toolTip, BlockKawaiiCrop plant, Block soil) {
		super(plant, soil);

		this.setTextureName(Constants.MOD_ID + ":" + name);
		this.setUnlocalizedName(Constants.MOD_ID + "." + name);
		this.name = name;
		this.plant = plant;
		this.soil = soil;
		this.ToolTipText = toolTip;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_) {
		if (ToolTipText.length() > 0)
			list.add(ToolTipText);
	}

    @Override
    public boolean onItemUse(ItemStack items, EntityPlayer entity, World world, int x, int y, int z, int side, float xHit, float yHit, float zHit)
    {
    	if (soil.getMaterial() == Material.water || soil.getMaterial() == Material.lava) {
    		MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(world, entity, true);
        	if (mop != null && mop.typeOfHit == MovingObjectType.BLOCK)
        	{
                x = mop.blockX;
                y = mop.blockY;
                z = mop.blockZ;
                side = mop.sideHit;
        	}    		
    	}
    	
        if (side != 1)
        {
            return false;
        }
        else if (entity.canPlayerEdit(x, y, z, side, items) && entity.canPlayerEdit(x, y + 1, z, side, items))
        {
            if (world.getBlock(x, y, z) == soil && world.isAirBlock(x, y + 1, z))
            {
                world.setBlock(x, y + 1, z, this.plant);
                --items.stackSize;
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }
}
