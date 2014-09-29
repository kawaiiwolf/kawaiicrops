package com.kawaiiwolf.kawaiicrops.item;

import com.kawaiiwolf.kawaiicrops.block.BlockKawaiiCrop;
import com.kawaiiwolf.kawaiicrops.lib.Constants;

import net.minecraft.block.Block;
import net.minecraft.item.ItemSeedFood;

public class ItemKawaiiSeedFood extends ItemSeedFood {

	public String ToolTipText = "";
	
	private String name = "";
	private BlockKawaiiCrop plant = null;

	public ItemKawaiiSeedFood(String name, String toolTip, int hunger, float saturation, BlockKawaiiCrop plant, Block soil) {
		super(hunger, saturation, plant, soil);
		
		this.setTextureName(Constants.MOD_ID + ":" + name);
		this.setUnlocalizedName(Constants.MOD_ID + "." + "name");
		this.name = name;
		this.plant = plant;	
		this.ToolTipText = toolTip;
	}

}
