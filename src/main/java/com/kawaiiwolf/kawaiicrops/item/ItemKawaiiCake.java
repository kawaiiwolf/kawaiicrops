package com.kawaiiwolf.kawaiicrops.item;

import java.util.List;

import com.kawaiiwolf.kawaiicrops.block.BlockKawaiiCake;
import com.kawaiiwolf.kawaiicrops.lib.Constants;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemReed;
import net.minecraft.item.ItemStack;

public class ItemKawaiiCake extends ItemReed {

	private BlockKawaiiCake cake = null;
	
	public ItemKawaiiCake(BlockKawaiiCake cake) {
		super(cake);
		this.setTextureName(Constants.MOD_ID + ":" + cake.Name + ".cake");
		this.setUnlocalizedName(Constants.MOD_ID + "." + cake.Name + ".cake");
		this.cake = cake;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_) {
		if (cake.ToolTipText.length() > 0)
			list.add(cake.ToolTipText);
	}

}
