package com.kawaiiwolf.kawaiicrops.item;

import java.util.List;

import com.kawaiiwolf.kawaiicrops.block.BlockKawaiiCrop;
import com.kawaiiwolf.kawaiicrops.lib.Constants;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemKawaiiFood extends ItemFood {
	
	public String ToolTipText = "";
	
	private String name = "";
	private BlockKawaiiCrop plant = null;

	public ItemKawaiiFood(String name, String toolTip, int hunger, float saturation, BlockKawaiiCrop plant) {
		super(hunger, saturation, false);
		
		this.setTextureName(Constants.MOD_ID + ":" + name);
		this.setUnlocalizedName(Constants.MOD_ID + "." + name);
		this.name = name;
		this.plant = plant;
		this.ToolTipText = toolTip;
	}
	
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_) {
		if (ToolTipText.length() > 0)
			list.add(ToolTipText);
	}

	@Override
    public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player)
    {
		//player.addPotionEffect(new PotionEffect(intPotionId, intDurationTicks, intAmplifier));
		return super.onEaten(stack, world, player);
    }
	

}
