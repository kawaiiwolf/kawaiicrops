package com.kawaiiwolf.kawaiicrops.item;

import com.kawaiiwolf.kawaiicrops.block.BlockKawaiiCrop;
import com.kawaiiwolf.kawaiicrops.lib.Constants;

import net.minecraft.item.ItemFood;

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

}
