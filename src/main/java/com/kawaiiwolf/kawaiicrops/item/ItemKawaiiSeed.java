package com.kawaiiwolf.kawaiicrops.item;

import com.kawaiiwolf.kawaiicrops.block.BlockKawaiiCrop;
import com.kawaiiwolf.kawaiicrops.lib.Constants;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemSeeds;

public class ItemKawaiiSeed extends ItemSeeds {

	public String ToolTipText = "";
	
	private String name = "";
	private BlockKawaiiCrop plant = null;
	
	public ItemKawaiiSeed(String name, String toolTip, BlockKawaiiCrop plant, Block soil) {
		super(plant, soil);

		this.setTextureName(Constants.MOD_ID + ":" + name);
		this.setUnlocalizedName(Constants.MOD_ID + "." + name);
		this.name = name;
		this.plant = plant;
		this.ToolTipText = toolTip;
	}

}
