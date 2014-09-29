package com.kawaiiwolf.kawaiicrops.item;

import com.kawaiiwolf.kawaiicrops.block.BlockKawaiiCrop;
import com.kawaiiwolf.kawaiicrops.lib.Constants;

import net.minecraft.item.Item;

public class ItemKawaiiIngredient extends Item {
	public String ToolTipText = "";
	
	private String name = "";
	private BlockKawaiiCrop plant = null;

	public ItemKawaiiIngredient(String name, String toolTip, BlockKawaiiCrop plant) {
		
		this.setTextureName(Constants.MOD_ID + ":" + name);
		this.setUnlocalizedName(Constants.MOD_ID + "." + name);
		this.name = name;
		this.plant = plant;
		this.ToolTipText = toolTip;
	}
}
