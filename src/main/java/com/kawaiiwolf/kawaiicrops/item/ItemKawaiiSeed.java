package com.kawaiiwolf.kawaiicrops.item;

import java.util.List;

import com.kawaiiwolf.kawaiicrops.block.BlockKawaiiCrop;
import com.kawaiiwolf.kawaiicrops.block.BlockKawaiiTreeBlocks;
import com.kawaiiwolf.kawaiicrops.lib.Constants;
import com.kawaiiwolf.kawaiicrops.lib.Pair;

import cpw.mods.fml.common.registry.GameRegistry;
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
	private SeedType type = null;
	
	private BlockKawaiiCrop plant = null;
	private BlockKawaiiTreeBlocks tree = null;
	public String OreDict = "";
	public boolean WaterPlant = false;
	public int MysterySeedWeight = 0;
	
	private enum SeedType { CROP, TREE }
	
	public ItemKawaiiSeed(String name, String toolTip, BlockKawaiiCrop plant) {
		super(plant, null);
		type = SeedType.CROP;

		this.setTextureName(Constants.MOD_ID + ":" + name);
		this.setUnlocalizedName(Constants.MOD_ID + "." + name);
		this.name = name;
		this.plant = plant;
		this.ToolTipText = toolTip;
	}
	
	public ItemKawaiiSeed(String name, String toolTip, BlockKawaiiTreeBlocks tree) {
		super(tree, null);
		type = SeedType.TREE;

		this.setTextureName(Constants.MOD_ID + ":" + name);
		this.setUnlocalizedName(Constants.MOD_ID + "." + name);
		this.name = name;
		this.tree = tree;
		this.ToolTipText = toolTip;
	}
	
	private boolean isRegistered = false;
	public void register()
	{
		if (isRegistered) return;
		isRegistered = true;
		
		ModItems.ModSeeds.add(this);
		GameRegistry.registerItem(this, Constants.MOD_ID + "." + name);
		
		if (MysterySeedWeight > 0)
		{
			if(type == SeedType.CROP) 
				ItemKawaiiMysterySeed.SeedList.add(new Pair(plant,MysterySeedWeight));
			if(type == SeedType.TREE) 
				ItemKawaiiMysterySeed.SeedList.add(new Pair(tree,MysterySeedWeight));
		}	
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
    	if (WaterPlant) {
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
            if (	(
            			(type == SeedType.CROP &&   plant.CropGrowsOn.contains(world.getBlock(x, y, z))) ||
            			(type == SeedType.TREE && tree.SaplingSoilBlocks.contains(world.getBlock(x, y, z)))
            		) && world.isAirBlock(x, y + 1, z)
               )
            {
            	switch (type)
            	{
            		case CROP:
            			world.setBlock(x, y + 1, z, plant);
            		break;
            		case TREE:
            			world.setBlock(x, y + 1, z, tree);
            		break;
            	}
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
