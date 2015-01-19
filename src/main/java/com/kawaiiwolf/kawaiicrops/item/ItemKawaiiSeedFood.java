package com.kawaiiwolf.kawaiicrops.item;

import java.util.List;

import com.kawaiiwolf.kawaiicrops.block.BlockKawaiiCrop;
import com.kawaiiwolf.kawaiicrops.lib.Constants;
import com.kawaiiwolf.kawaiicrops.lib.Pair;
import com.kawaiiwolf.kawaiicrops.lib.PotionEffectHelper;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSeedFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemKawaiiSeedFood extends ItemSeedFood {

	public String ToolTipText = "";
	
	private String name = "";
	private BlockKawaiiCrop plant = null;
	public PotionEffectHelper potion = null;
	public String OreDict = "";
	public boolean WaterPlant = false;
	public int MysterySeedWeight = 0;
	
	public ItemKawaiiSeedFood(String name, String toolTip, int hunger, float saturation, BlockKawaiiCrop plant) 
	{
		this(name, toolTip, hunger, saturation, plant, null, null);
	}

	public ItemKawaiiSeedFood(String name, String toolTip, int hunger, float saturation, BlockKawaiiCrop plant, Block soil, PotionEffectHelper potion) {
		super(hunger, saturation, plant, soil);
		
		this.setTextureName(Constants.MOD_ID + ":" + name);
		this.setUnlocalizedName(Constants.MOD_ID + "." + name);
		this.name = name;
		this.plant = plant;
		this.ToolTipText = toolTip;
		this.potion = potion;
		
		setCreativeTab(ModItems.KawaiiCreativeTab);
	}
	
	private boolean isRegistered = false;
	public void register()
	{
		if (isRegistered) return;
		isRegistered = true;
		
		ModItems.ModSeedFoods.add(this);
		GameRegistry.registerItem(this, Constants.MOD_ID + "." + name);
		
		if (MysterySeedWeight > 0)
			ItemKawaiiMysterySeed.SeedList.add(new Pair(plant,MysterySeedWeight));
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
            if (plant.CropGrowsOn.contains(world.getBlock(x, y, z)) && world.isAirBlock(x, y + 1, z))
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
    
	@Override
    public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player)
    {
		if (player.canEat(false) && !world.isRemote && this.potion != null)
        	for (com.kawaiiwolf.kawaiicrops.lib.PotionEffectHelper.Potion p : this.potion.Effects)
        		if (world.rand.nextFloat() < p.Chance)
                	player.addPotionEffect(p.getPotionEffect());
		
		return super.onEaten(stack, world, player);
    }
}
